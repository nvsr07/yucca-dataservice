package org.csi.yucca.dataservice.insertdataapi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.bson.types.ObjectId;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiBaseException;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiRuntimeException;
import org.csi.yucca.dataservice.insertdataapi.hdfs.SDPInsertApiHdfsDataAccess;
import org.csi.yucca.dataservice.insertdataapi.metadata.SDPInsertMedataFactory;
import org.csi.yucca.dataservice.insertdataapi.metadata.SDPInsertMetadataApiAccess;
import org.csi.yucca.dataservice.insertdataapi.model.output.CollectionConfDto;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetBulkInsert;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetDeleteOutput;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetInfo;
import org.csi.yucca.dataservice.insertdataapi.model.output.FieldsDto;
import org.csi.yucca.dataservice.insertdataapi.model.output.StreamInfo;
import org.csi.yucca.dataservice.insertdataapi.model.output.TenantInfo;
import org.csi.yucca.dataservice.insertdataapi.mongo.SDPInsertApiMongoDataAccess;
import org.csi.yucca.dataservice.insertdataapi.phoenix.SDPInsertApiPhoenixDataAccess;
import org.csi.yucca.dataservice.insertdataapi.solr.SDPInsertApiSolrDataAccess;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class InsertApiLogic {
	private static final Logger log = Logger.getLogger("org.csi.yucca.datainsert");

	ExecutorService solrInsertService;

	public HashMap<String, DatasetBulkInsert> insertManager(String tenant, HashMap<String, DatasetBulkInsert> datiToIns) throws Exception {

		Iterator<String> iter = datiToIns.keySet().iterator();

		solrInsertService = Executors.newSingleThreadExecutor();

		// System.out.println(" TIMETIME insertManager -- start --> "+System.currentTimeMillis());

		// SDPInsertApiSolrDataAccess solrAccess = new
		// SDPInsertApiSolrDataAccess();
		SDPInsertApiPhoenixDataAccess phoenixAccess = new SDPInsertApiPhoenixDataAccess();
		DatasetBulkInsert curBulkToIns = null;

		while (iter.hasNext()) {
			try {
				String key = iter.next();
				curBulkToIns = datiToIns.get(key);
				long millis = new Date().getTime();
				curBulkToIns.setTimestamp(millis);
				curBulkToIns.setRequestId(curBulkToIns.getIdDataset() + "_" + curBulkToIns.getDatasetVersion() + "_" + curBulkToIns.getTimestamp());
				curBulkToIns.setStatus(DatasetBulkInsert.STATUS_START_INS);
				datiToIns.put(key, curBulkToIns);
			} catch (Exception e) {
				curBulkToIns.setStatus(DatasetBulkInsert.STATUS_KO_INS);

			}
		}

		// System.out.println(" TIMETIME insertManager -- fine ciclo UNO --> "+System.currentTimeMillis());

		// String
		// idRequest=mongoAccess.insertStatusRecordArray(tenant,datiToIns);
		long millis = new Date().getTime();
		String idRequest = tenant + "_" + millis;

		// System.out.println(" TIMETIME insertManager -- fine inserimento start ins --> "+System.currentTimeMillis());

		iter = datiToIns.keySet().iterator();

		// int cnt = 0;
		// boolean indiceDaCReare=true;
		while (iter.hasNext()) {
			try {
				String key = iter.next();
				curBulkToIns = datiToIns.get(key);
				curBulkToIns.setGlobalReqId(idRequest);

				
				// int righeinserite=mongoAccess.insertBulk(tenant,
				// curBulkToIns,indiceDaCReare);
				Long startTimeX = System.currentTimeMillis();
				log.finest("[InsertApiLogic::insertManager] BEGIN phoenixInsert ...");
				phoenixAccess.insertBulk(curBulkToIns.getCollectionConfDto().getPhoenixSchemaName(),
						curBulkToIns.getCollectionConfDto().getPhoenixTableName(), curBulkToIns);
				log.finest("[InsertApiLogic::insertManager] END phoenixInsert  Elapsed[" + (System.currentTimeMillis() - startTimeX) + "]");

				// TODO CONTROLLI
				curBulkToIns.setStatus(DatasetBulkInsert.STATUS_END_INS);
				// indiceDaCReare=false;

				datiToIns.put(key, curBulkToIns);

				if (!SDPInsertApiConfig.getInstance().isSolrIndexerEnabled()) {
					try {
						startTimeX = System.currentTimeMillis();
						log.info("[InsertApiLogic::insertManager] BEGIN SOLRInsert ...");
						solrInsertService.execute(solrInsertRunnable(curBulkToIns.getCollectionConfDto().getSolrCollectionName(), curBulkToIns));
						log.finest("[InsertApiLogic::insertManager] END SOLRInsert  Elapsed[" + (System.currentTimeMillis() - startTimeX) + "]");
					} catch (Exception e) {
						log.log(Level.SEVERE, "[InsertApiLogic::insertManager] SOLR GenericException " + e);
					}
				}
			} catch (InsertApiRuntimeException e1) {
				throw e1;
			} catch (InsertApiBaseException e) {

				log.log(Level.SEVERE, "[InsertApiLogic::insertManager] GenericException " + e);
				log.log(Level.WARNING,
						"[InsertApiLogic::insertManager] Fallito inserimento blocco --> globalRequestId=" + idRequest + "    blockRequestId=" + curBulkToIns.getRequestId());
				curBulkToIns.setStatus(DatasetBulkInsert.STATUS_KO_INS);
				try {
					curBulkToIns.setStatus(DatasetBulkInsert.STATUS_KO_INS);

				} catch (Exception k) {
					log.log(Level.SEVERE, "[InsertApiLogic::insertManager] SOLR GenericException " + e);
					log.log(Level.WARNING,
							"[InsertApiLogic::insertManager] Fallito indicizzazione blocco --> globalRequestId=" + idRequest + "    blockRequestId=" + curBulkToIns.getRequestId());

				}

			} catch (Throwable e2) {
				log.log(Level.SEVERE, "[InsertApiLogic::insertManager] UnknownException, presume redelivery " + e2);
				throw new InsertApiRuntimeException(e2);
			}
		}
		long startTimeX = System.currentTimeMillis();
		// mongoAccess.updateStatusRecordArray(tenant, idRequest, "end_ins",
		// datiToIns);
		log.finest("[InsertApiLogic::insertManager] END updateStatus  Elapsed[" + (System.currentTimeMillis() - startTimeX) + "]");
		return datiToIns;

	}

	private Runnable solrInsertRunnable(final String collection, final DatasetBulkInsert curBulkToIns) {
		Runnable solrInsertRunnable = new Runnable() {
			public void run() {
				try {
					SDPInsertApiSolrDataAccess solrAccess = new SDPInsertApiSolrDataAccess();
					solrAccess.insertBulk(collection, curBulkToIns);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		return solrInsertRunnable;
	}

	public HashMap<String, DatasetBulkInsert> parseJsonInputDataset(String tenant, String jsonInput) throws Exception {
		int i = 0;
		boolean endArray = false;
		JSONObject ooo = null;
		HashMap<String, DatasetBulkInsert> ret = new HashMap<String, DatasetBulkInsert>();
		SDPInsertMetadataApiAccess metadataAccess = SDPInsertMedataFactory.getSDPInsertMetadataApiAccess();
		DatasetInfo infoDataset = null;
		Integer datasetVersion = null;
		int reqVersion = -1;

		int totalDocumentsToIns = 0;
		try {
			if (JsonPath.read(jsonInput, "$[" + i + "]") == null)
				jsonInput = "[" + jsonInput + "]";
		} catch (PathNotFoundException e) {
			log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputDataset] PathNotFoundException imprevisto --> " + e);
			throw new InsertApiBaseException("E012");
		} catch (IllegalArgumentException e) {
			log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputDataset] PathNotFoundException imprevisto --> " + e);
			throw new InsertApiBaseException("E012");
		}

		while (i < 100000 && !endArray) {
			try {
				// System.out.println(" TIMETIME parseJsonInputDataset -- inizio blocco "+i+"--> "+System.currentTimeMillis());

				ooo = JsonPath.read(jsonInput, "$[" + i + "]");
				if (null == ooo)
					throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DATA_NOTARRAY);
				String datasetCode = (String) ooo.get("datasetCode");

				datasetVersion = (Integer) ooo.get("$.datasetVersion");
				reqVersion = (datasetVersion == null ? -1 : datasetVersion.intValue());
				infoDataset = metadataAccess.getInfoDataset(datasetCode, reqVersion, tenant);

				if (null == infoDataset)
					throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_DATASETVERSION_INVALID, " for dataset " + datasetCode);

				DatasetInfo infoDatasetV1 = metadataAccess.getInfoDataset(datasetCode, 1, tenant);

				String insStrConst = "";
				insStrConst += "  idDataset : " + infoDataset.getDatasetId();
				insStrConst += " , datasetVersion : " + infoDataset.getDatasetVersion();

				boolean isVerOneRequired = false;

				// se dataset � stream, recupero info stream
				String streamCode = null;
				String sensor = null;
				if ("streamDataset".equals(infoDataset.getDatasetSubType())) {
					// aggiungo il campo fittizio Time
					infoDataset.getCampi().add(new FieldsDto("time", FieldsDto.DATA_TYPE_DATETIME, infoDataset.getDatasetId(), infoDataset.getDatasetVersion()));
					StreamInfo infoStream = metadataAccess.getStreamInfoForDataset(tenant, infoDataset.getDatasetId(), infoDataset.getDatasetVersion());
					// System.out.println(" TIMETIME parseJsonInputDataset -- recuperata info stream--> "+System.currentTimeMillis());

					// aggiungo stream e sensore alla costante
					insStrConst += " , sensor : \"" + infoStream.getSensorCode() + "\"";
					insStrConst += " , streamCode : \"" + infoStream.getStreamCode() + "\"";
					sensor = infoStream.getSensorCode();
					streamCode = infoStream.getStreamCode();
					isVerOneRequired = true;
				}
				if ("socialDataset".equals(infoDataset.getDatasetSubType())) {
					// aggiungo il campo fittizio Time
					infoDataset.getCampi().add(new FieldsDto("time", FieldsDto.DATA_TYPE_DATETIME, infoDataset.getDatasetId(), infoDataset.getDatasetVersion()));
					StreamInfo infoStream = metadataAccess.getStreamInfoForDataset(tenant, infoDataset.getDatasetId(), infoDataset.getDatasetVersion());
					// System.out.println(" TIMETIME parseJsonInputDataset -- recuperata info stream--> "+System.currentTimeMillis());

					// aggiungo stream e sensore alla costante
					insStrConst += " , sensor : \"" + infoStream.getSensorCode() + "\"";
					insStrConst += " , streamCode : \"" + infoStream.getStreamCode() + "\"";
					sensor = infoStream.getSensorCode();
					streamCode = infoStream.getStreamCode();

				}
				if ("bulkDataset".equals(infoDataset.getDatasetSubType())) {
					isVerOneRequired = true;
				}

				// se dataset � dataset va bene cosi'

				if (ret.get(datasetCode) != null)
					throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DUPLICATE, " for dataset " + datasetCode);

				// DatasetBulkInsert datiToins=parseGenericDataset(tenant,
				// ooo.toJSONString(), insStrConst, infoDataset);
				DatasetBulkInsert datiToins = parseGenericDataset(tenant, ooo, insStrConst, infoDataset, infoDatasetV1, isVerOneRequired);

				// System.out.println(" TIMETIME parseJsonInputDataset -- parsificato dataset info--> "+System.currentTimeMillis());
				datiToins.setDatasetCode(datasetCode);
				datiToins.setStream(streamCode);
				datiToins.setSensor(sensor);
				datiToins.setStatus(DatasetBulkInsert.STATUS_SYNTAX_CHECKED);
				datiToins.setDatasetType(infoDataset.getDatasetSubType());

				ret.put(datasetCode, datiToins);
				totalDocumentsToIns = totalDocumentsToIns + datiToins.getNumRowToInsFromJson();
				if (totalDocumentsToIns > SDPInsertApiConfig.MAX_DOCUMENTS_IN_REQUEST)
					throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_MAXRECORDS);

				i++;

			} catch (PathNotFoundException e) {
				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException) {
					endArray = true;
				} else {
					log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputDataset] PathNotFoundException imprevisto --> " + e);
					throw new InsertApiBaseException("E012");
				}
			} catch (IllegalArgumentException e) {
				log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputDataset] PathNotFoundException imprevisto --> " + e);
				throw new InsertApiBaseException("E012");
			} catch (Exception ex) {
				log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputDataset] GenericEsxception" + ex);
				i++;
				endArray = true;
				throw ex;
			} finally {
				// System.out.println(" TIMETIME parseJsonInputDataset -- fine metodo--> "+System.currentTimeMillis());

			}
		}
		return ret;

	}

	public HashMap<String, DatasetBulkInsert> parseJsonInputStream(String tenant, String jsonInput) throws Exception {
		int i = 0;
		boolean endArray = false;
		JSONObject ooo = null;
		HashMap<String, DatasetBulkInsert> ret = new HashMap<String, DatasetBulkInsert>();

		int totalDocumentsToIns = 0;
		String sensor = null;
		String application = null;
		String stream = null;
		String streamToFind = null;
		String sensorToFind = null;
		try {
			if (JsonPath.read(jsonInput, "$[" + i + "]") == null)
				jsonInput = "[" + jsonInput + "]";
		} catch (PathNotFoundException e) {
			log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputDataset] PathNotFoundException imprevisto --> " + e);
			throw new InsertApiBaseException("E012");
		} catch (IllegalArgumentException e) {
			log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputDataset] PathNotFoundException imprevisto --> " + e);
			throw new InsertApiBaseException("E012");
		}

		while (i < 100000 && !endArray) {
			try {
				ooo = JsonPath.read(jsonInput, "$[" + i + "]");
				if (null == ooo)
					throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DATA_NOTARRAY);
				sensor = (String) ooo.get("sensor");
				application = (String) ooo.get("application");
				stream = (String) ooo.get("stream");

				streamToFind = stream;

				sensorToFind = (sensor != null ? sensor : application);

				// TODO non so se e' bloccante ...
				if (streamToFind == null)
					throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_STREAM_MANCANTE);
				if (sensorToFind == null)
					throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE);

				log.info("[InsertApiLogic::parseJsonInputStream] Parsing tenant=[" + tenant + "] sensor=[" + sensorToFind + "] stream=[" + streamToFind + "]");

				if (ret.get(sensorToFind + "_" + streamToFind) != null)
					throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DUPLICATE, " for stream " + streamToFind);

				DatasetBulkInsert datiToins = parseMisura(tenant, ooo);

				// TODO .. controllo sul numero di record da inserire

				datiToins.setStream(streamToFind);
				datiToins.setSensor(sensorToFind);
				datiToins.setStatus(DatasetBulkInsert.STATUS_SYNTAX_CHECKED);
				// datiToins.setDatasetType("streamDataset");

				totalDocumentsToIns = totalDocumentsToIns + datiToins.getNumRowToInsFromJson();
				if (totalDocumentsToIns > SDPInsertApiConfig.MAX_DOCUMENTS_IN_REQUEST)
					throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_MAXRECORDS);
				// System.out.println("           "+datiToins.getRowsToInsert().size()+"/"+
				// datiToins.getNumRowToInsFromJson());

				ret.put(sensorToFind + "_" + streamToFind, datiToins);

				i++;
			} catch (PathNotFoundException e) {
				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException) {
					endArray = true;
				} else {
					log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputStream] PathNotFoundException imprevisto --> ", e);
					throw new InsertApiBaseException("E012");
				}
			} catch (IllegalArgumentException e) {
				log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputDataset] PathNotFoundException imprevisto --> " + e);
				throw new InsertApiBaseException("E012");
			} catch (Exception ex) {
				log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputStream] GenericEsxception", ex);
				i++;
				endArray = true;
				throw ex;
			} finally {
				// System.out.println(" TIMETIME parseJsonInputDataset -- fine metodo--> "+System.currentTimeMillis());

			}
		}
		return ret;

	}

	public static JSONObject getSmartobject_StreamFromJson(String tenant, String jsonInput) throws Exception {

		JSONObject correctedMsg = new JSONObject();
		

		if (JsonPath.read(jsonInput, "$[0]") == null)
			jsonInput = "[" + jsonInput + "]";

		JSONObject ooo = JsonPath.read(jsonInput, "$[0]");

		String oldSensor = (String) ooo.get("sensor");
		String oldApplication = (String) ooo.get("application");
		String oldStream = (String) ooo.get("stream");

		
		if (null == ooo)
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DATA_NOTARRAY);


		correctedMsg.put("stream", oldStream);
		if (oldSensor!=null)
			correctedMsg.put("sensor", oldSensor);
		if (oldApplication!=null)
			correctedMsg.put("application", oldApplication);
		
		Object values = ooo.get("values");
		
		if (values instanceof JSONArray)
		{
			// "MEssage from internal! already correct format!");
			return ooo;
		}
		
		
		JSONObject oldValues = (JSONObject) values;
		String oldTime = (String) oldValues.get("time");
		JSONObject oldComponents = (JSONObject) oldValues.get("components");
		JSONObject newComponents = new JSONObject();

		for (Iterator<Map.Entry<String, Object>> iterator = oldComponents.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, Object> obj = iterator.next();
			if (obj.getValue()!=null)
				newComponents.put(obj.getKey(), String.valueOf(obj.getValue()));
			else
				newComponents.put(obj.getKey(), null);
		}
		
		
		JSONArray newValues = new JSONArray();
		JSONObject newValue = new JSONObject();
		newValue.put("time", oldTime);
		newValue.put("components", newComponents);
		newValues.add(newValue);
		correctedMsg.put("values", newValues);

		
		
		
		if (oldStream == null)
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_STREAM_MANCANTE);
		if (oldSensor == null && oldApplication == null)
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE);

		return correctedMsg;

	}

	public HashMap<String, DatasetBulkInsert> parseJsonInputMedia(String tenant, String jsonInput) throws Exception {
		int i = 0;
		boolean endArray = false;
		JSONObject ooo = null;
		HashMap<String, DatasetBulkInsert> ret = new HashMap<String, DatasetBulkInsert>();
		SDPInsertMetadataApiAccess mongoAccess = SDPInsertMedataFactory.getSDPInsertMetadataApiAccess();
		DatasetInfo infoDataset = null;
		Integer datasetVersion = null;
		int reqVersion = -1;

		int totalDocumentsToIns = 0;
		try {
			if (JsonPath.read(jsonInput, "$[" + i + "]") == null)
				jsonInput = "[" + jsonInput + "]";
		} catch (PathNotFoundException e) {
			log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputDataset] PathNotFoundException imprevisto --> " + e);
			throw new InsertApiBaseException("E012");
		} catch (IllegalArgumentException e) {
			log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputDataset] PathNotFoundException imprevisto --> " + e);
			throw new InsertApiBaseException("E012");
		}

		while (i < 100000 && !endArray) {
			try {
				ooo = JsonPath.read(jsonInput, "$[" + i + "]");
				if (null == ooo)
					throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DATA_NOTARRAY);
				String datasetCode = (String) ooo.get("datasetCode");

				datasetVersion = (Integer) ooo.get("$.datasetVersion");
				reqVersion = (datasetVersion == null ? -1 : datasetVersion.intValue());
				infoDataset = mongoAccess.getInfoDataset(datasetCode, reqVersion, tenant);

				if (null == infoDataset)
					throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_DATASETVERSION_INVALID, " for dataset " + datasetCode);

				if (!infoDataset.getDatasetSubType().equalsIgnoreCase("binaryDataset"))
					throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_DATASETVERSION_INVALID, " for dataset " + datasetCode + ". "
							+ "Required binaryDataset, found " + infoDataset.getDatasetSubType());

				String insStrConst = "";
				insStrConst += "  idDataset : " + infoDataset.getDatasetId();
				insStrConst += " , datasetVersion : " + infoDataset.getDatasetVersion();

				// se dataset � dataset va bene cosi'

				if (ret.get(datasetCode) != null)
					throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DUPLICATE, " for dataset " + datasetCode);

				DatasetBulkInsert datiToins = parseMediaDataset(tenant, ooo, insStrConst, infoDataset);

				// System.out.println(" TIMETIME parseJsonInputDataset -- parsificato dataset info--> "+System.currentTimeMillis());
				datiToins.setDatasetCode(datasetCode);
				datiToins.setStatus(DatasetBulkInsert.STATUS_SYNTAX_CHECKED);
				datiToins.setDatasetType(infoDataset.getDatasetSubType());

				ret.put(datasetCode, datiToins);
				totalDocumentsToIns = totalDocumentsToIns + datiToins.getNumRowToInsFromJson();
				if (totalDocumentsToIns > SDPInsertApiConfig.MAX_DOCUMENTS_IN_REQUEST)
					throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_MAXRECORDS);

				i++;

			} catch (PathNotFoundException e) {
				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException) {
					endArray = true;
				} else {
					log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputMedia] PathNotFoundException imprevisto --> " + e);
					throw new InsertApiBaseException("E012");
				}
			} catch (IllegalArgumentException e) {
				log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputDataset] PathNotFoundException imprevisto --> " + e);
				throw new InsertApiBaseException("E012");
			} catch (Exception ex) {
				log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputMedia] GenericEsxception" + ex);
				i++;
				endArray = true;
				throw ex;
			} finally {
				// System.out.println(" TIMETIME parseJsonInputDataset -- fine metodo--> "+System.currentTimeMillis());

			}
		}
		return ret;

	}

	private DatasetBulkInsert parseGenericDataset(String tenant, JSONObject bloccoDaIns, String insStrConst, DatasetInfo datasetMongoInfo,
			DatasetInfo datasetMongoInfoV1, boolean isVerOneRequired) throws Exception {
		// System.out.println(" TIMETIME parseGenericDataset -- inizio--> "+System.currentTimeMillis());
		DatasetBulkInsert ret = null;

		// JSONObject ooo = null;
		JSONObject components = null;
		boolean endArray = false;
		// ArrayList<String> rigadains = new ArrayList<String>();

		ArrayList<FieldsDto> elencoCampi = datasetMongoInfo.getCampi();

		HashMap<String, FieldsDto> campiMongo = new HashMap<String, FieldsDto>();
		for (int i = 0; i < elencoCampi.size(); i++) {
			campiMongo.put(elencoCampi.get(i).getFieldName(), elencoCampi.get(i));
		}

		HashMap<String, FieldsDto> campiMongoV1 = new HashMap<String, FieldsDto>();
		for (int i = 0; i < datasetMongoInfoV1.getCampi().size(); i++) {
			campiMongoV1.put(datasetMongoInfoV1.getCampi().get(i).getFieldName(), datasetMongoInfoV1.getCampi().get(i));
		}

		int i = 0;
		Object valuesObject = bloccoDaIns.get("values");
		JSONArray arrayValori;

		if (valuesObject instanceof JSONArray) {
			arrayValori = (JSONArray) bloccoDaIns.get("values");
		} else {
			arrayValori = new JSONArray();
			arrayValori.add(valuesObject);
		}
		
		ArrayList<JSONObject> listJson = new ArrayList<JSONObject>();
		// System.out.println(" TIMETIME parseGenericDataset -- inzio ciclo controllo--> "+System.currentTimeMillis());
		// int numeroCampiMongo = elencoCampi.size();
		while (!endArray && i < arrayValori.size()) {
			try {
				// System.out.println(" TIMETIME parseGenericDataset -- blocco ("+i+")--> "+System.currentTimeMillis());
				// components = JsonPath.read(jsonInput, "$.values["+i+"]");

				components = (JSONObject) arrayValori.get(i);

				// System.out.println(" TIMETIME parseGenericDataset -- blocco ("+i+") JsonPath--> "+System.currentTimeMillis());
				// rigadains.add(parseComponents(components, insStrConst,
				// elencoCampi));
				parseComponents(components, insStrConst, campiMongo, campiMongoV1, isVerOneRequired);
				components.put("objectid", ObjectId.get().toString());
				listJson.add(components);

				// System.out.println(" TIMETIME parseGenericDataset -- dentro ciclo ("+i+") parse componenti--> "+System.currentTimeMillis());

				// if (components.keySet().size()!=numeroCampiMongo) throw new
				// InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE);

				i++;
			} catch (PathNotFoundException e) {
				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException)
					endArray = true;
			}

		}
		CollectionConfDto collectionConfDto = SDPInsertMedataFactory.getSDPInsertMetadataApiAccess().getCollectionInfo(tenant, 
				datasetMongoInfo.getDatasetId(), datasetMongoInfo.getDatasetVersion(), datasetMongoInfo.getDatasetSubType());

		ret = new DatasetBulkInsert(collectionConfDto);
		ret.setIdDataset(datasetMongoInfo.getDatasetId());
		ret.setDatasetVersion(datasetMongoInfo.getDatasetVersion());
		ret.setNumRowToInsFromJson(i);
		// ret.setRowsToInsert(rigadains);
		ret.setFieldsType(campiMongo);
		ret.setJsonRowsToInsert(listJson);
		// System.out.println(" TIMETIME parseGenericDataset -- fine--> "+System.currentTimeMillis());

		return ret;
	}

	private DatasetBulkInsert parseMediaDataset(String tenant, JSONObject bloccoDaIns, String insStrConst, DatasetInfo datasetMongoInfo) throws Exception {
		// System.out.println(" TIMETIME parseGenericDataset -- inizio--> "+System.currentTimeMillis());
		DatasetBulkInsert ret = null;

		// JSONObject ooo = null;
		JSONObject components = null;
		boolean endArray = false;
		ArrayList<String> rigadains = new ArrayList<String>();

		ArrayList<FieldsDto> elencoCampi = datasetMongoInfo.getCampi();

		HashMap<String, FieldsDto> campiMongo = new HashMap<String, FieldsDto>();
		for (int i = 0; i < elencoCampi.size(); i++) {
			campiMongo.put(elencoCampi.get(i).getFieldName(), elencoCampi.get(i));
		}

		campiMongo.remove("urlDownloadBinary");
		campiMongo.remove("idBinary");
		FieldsDto filePath = new FieldsDto("pathHdfsBinary", FieldsDto.DATA_TYPE_STRING);
		campiMongo.put(filePath.getFieldName(), filePath);
		FieldsDto tenantBinary = new FieldsDto("tenantBinary", FieldsDto.DATA_TYPE_STRING);
		campiMongo.put(tenantBinary.getFieldName(), tenantBinary);
		FieldsDto idBinary = new FieldsDto("idBinary", FieldsDto.DATA_TYPE_STRING);
		campiMongo.put(idBinary.getFieldName(), idBinary);

		int i = 0;
		JSONArray arrayValori = (JSONArray) bloccoDaIns.get("values");
		ArrayList<JSONObject> listJson = new ArrayList<JSONObject>();
		// System.out.println(" TIMETIME parseGenericDataset -- inzio ciclo controllo--> "+System.currentTimeMillis());
		// int numeroCampiMongo = elencoCampi.size();
		while (!endArray && i < arrayValori.size()) {
			try {
				components = (JSONObject) arrayValori.get(i);
				rigadains.add(parseComponents(components, insStrConst, campiMongo, campiMongo, true));
				components.put("objectid", ObjectId.get().toString());
				listJson.add(components);
				i++;
			} catch (PathNotFoundException e) {
				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException)
					endArray = true;
			}

		}
		
		CollectionConfDto collectionConfDto = SDPInsertMedataFactory.getSDPInsertMetadataApiAccess().getCollectionInfo(tenant, 
				datasetMongoInfo.getDatasetId(), datasetMongoInfo.getDatasetVersion(), datasetMongoInfo.getDatasetSubType());
		
		// System.out.println(" TIMETIME parseGenericDataset -- fine ciclo controllo--> "+System.currentTimeMillis());
		ret = new DatasetBulkInsert(collectionConfDto);
		ret.setIdDataset(datasetMongoInfo.getDatasetId());
		ret.setDatasetVersion(datasetMongoInfo.getDatasetVersion());
		ret.setNumRowToInsFromJson(i);
		// ret.setRowsToInsert(rigadains);
		ret.setFieldsType(campiMongo);
		ret.setJsonRowsToInsert(listJson);
		// System.out.println(" TIMETIME parseGenericDataset -- fine--> "+System.currentTimeMillis());

		return ret;
	}

	private String parseComponents(JSONObject components, String insStrConst, HashMap<String, FieldsDto> campiMongo, HashMap<String, FieldsDto> campiMongoV1,
			boolean isVerOneRequired) throws Exception {
		Iterator<String> itCampiJson = components.keySet().iterator();

		String currRigaIns = null;

		int numCampiInV1 = 0;
		while (itCampiJson.hasNext()) {
			String jsonField = (String) itCampiJson.next();

			FieldsDto campoMongo = campiMongo.get(jsonField);
			FieldsDto campoMongoV1 = campiMongoV1.get(jsonField);
			
			// check if jsonField is in metadata definition. If not, there is a component unknown 
			if (null == campoMongo)
				throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INVALID_COMPONENTS, " component " + jsonField + " not found in stream configuration ("
						+ insStrConst + ")");

			
			String valore = null;
			if (null != (components.get(jsonField)))
				valore = (components.get(jsonField)).toString();

			if (!campoMongo.validateValue(valore, true))
				throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE, " - field " + jsonField + " (" + insStrConst + "): " + valore);

			log.finest("[InsertApiLogic::parseCompnents] ---------------- campo : " + campoMongo.getFieldName());
			log.finest("[InsertApiLogic::parseCompnents] campoMongoV1 is null: " + (campoMongoV1 == null));
			log.finest("[InsertApiLogic::parseCompnents] valore: " + valore);
			if (campoMongoV1 != null) {
				log.finest("[InsertApiLogic::parseCompnents] campoMongoV1.versione=" + campoMongoV1.getDatasetVersion());
				log.finest("[InsertApiLogic::parseCompnents] campoMongoV1.nome=" + campoMongoV1.getFieldName());
				log.finest("[InsertApiLogic::parseCompnents] campoMongoV1.gettype=" + campoMongoV1.getFieldType());
				if (null != campoMongoV1) {
					log.finest("[InsertApiLogic::parseCompnents] campoMongoV1.validateValue(valore)-->" + campoMongoV1.validateValue(valore, isVerOneRequired));
					numCampiInV1++;
				}
			}
			log.finest("[InsertApiLogic::parseCompnents] .................");
			log.finest("[InsertApiLogic::parseCompnents]          jsonField=" + jsonField);
			log.finest("[InsertApiLogic::parseCompnents]          insStrConst=" + insStrConst);
			log.finest("[InsertApiLogic::parseCompnents]          valore=" + valore);


			(campiMongo.get(jsonField)).setSuccessChecked(true);

			if (currRigaIns == null)
				currRigaIns = campoMongo.getInsertJson(valore);
			else
				currRigaIns += " , " + campoMongo.getInsertJson(valore);

		}

		if (isVerOneRequired && (numCampiInV1 != campiMongoV1.size()))
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INVALID_COMPONENTS, "fields present in dataset v1 definition must be presents");

		if (currRigaIns != null) {
			currRigaIns = insStrConst + ", " + currRigaIns;
		} else {
			// System.out.println( "OKKKKIOOOO riga vuota ???? ");
		}
		return currRigaIns;
	}

	private DatasetBulkInsert parseMisura(String tenant, JSONObject bloccoDaIns) throws Exception {
		// Integer datasetVersion=JsonPath.read(jsonInput, "$.datasetVersion");
		Integer datasetVersion = (Integer) bloccoDaIns.get("datasetVersion");
		int reqVersion = -1;
		DatasetBulkInsert ret = null;

		// String stream=JsonPath.read(jsonInput, "$.stream");
		// String sensor=JsonPath.read(jsonInput, "$.sensor");
		// String application=JsonPath.read(jsonInput, "$.application");

		String stream = (String) bloccoDaIns.get("stream");
		String sensor = (String) bloccoDaIns.get("sensor");
		String application = (String) bloccoDaIns.get("application");

		// if (application == null && stream == null) throw new
		// InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_STREAM_MANCANTE);
		// if (sensor == null ) throw new
		// InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE);

		if (sensor == null && application == null)
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE);
		if (stream == null)
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_STREAM_MANCANTE);

		if (null != datasetVersion)
			reqVersion = datasetVersion.intValue();

		SDPInsertMetadataApiAccess metadataAccess = SDPInsertMedataFactory.getSDPInsertMetadataApiAccess();
		// ArrayList<MongoStreamInfo>
		// elencoStream=mongoAccess.getStreamInfo(tenant, (stream!=null ? stream
		// : application), sensor);
		ArrayList<StreamInfo> elencoStream = metadataAccess.getStreamInfo(tenant, stream, (sensor != null ? sensor : application));

		if (elencoStream == null || elencoStream.size() <= 0)
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE, ": " + (sensor != null ? sensor : application) + " (stream: " + stream + ")");

		boolean isVerOneRequired = true;
		String datasetType = "streamDataset";
		long datasetId = elencoStream.get(0).getDatasetId();

		for (int i = 0; i < elencoStream.size(); i++) {

			log.finest("[InsertApiLogic::parseMisura] nome stream, tipo stream: " + elencoStream.get(i).getStreamCode() + "," + elencoStream.get(i).getTipoStream());

			if (elencoStream.get(i).getTipoStream() == StreamInfo.STREAM_TYPE_APPLICATION && application == null)
				throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE, " application code expected, found sensor: "
						+ (sensor != null ? sensor : application) + " (stream: " + stream + ")");
			if (elencoStream.get(i).getTipoStream() == StreamInfo.STREAM_TYPE_SENSOR && sensor == null)
				throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE, " sensor code expected, found application: "
						+ (sensor != null ? sensor : application) + " (stream: " + stream + ")");
			if (elencoStream.get(i).getTipoStream() == StreamInfo.STREAM_TYPE_TWEET && sensor == null)
				throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE, " sensor code expected, found application: "
						+ (sensor != null ? sensor : application) + " (stream: " + stream + ")");
			if (elencoStream.get(i).getTipoStream() == StreamInfo.STREAM_TYPE_UNDEFINED)
				throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_STREAM_NOT_FOUND,
						" invalid virtual object tpye: data insert allowed only for sensors and applications ");

			log.finest("[InsertApiLogic::parseMisura]      OK --------------");

			if (elencoStream.get(i).getTipoStream() == StreamInfo.STREAM_TYPE_TWEET) {
				isVerOneRequired = false;
				datasetType = "socialDataset";
			}
			if (elencoStream.get(i).getTipoStream() == StreamInfo.STREAM_TYPE_INTERNAL) {
				isVerOneRequired = false;
			}
		}

		ArrayList<FieldsDto> elencoCampi = metadataAccess.getCampiDataSet(datasetId, Long.parseLong("" + reqVersion));
		ArrayList<FieldsDto> elencoCampiV1 = elencoCampi;
		if (reqVersion != 1)
			elencoCampiV1 = metadataAccess.getCampiDataSet(datasetId, Long.parseLong("1"));

		if (elencoCampi == null || elencoCampi.size() <= 0)
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_DATASETVERSION_INVALID, ": " + (stream != null ? stream : application) + " (sensor: "
					+ sensor + ")");
		HashMap<String, FieldsDto> campiMongo = new HashMap<String, FieldsDto>();
		long idDatasetTrovato = -1;
		long datasetVersionTrovato = -1;
		for (int i = 0; i < elencoCampi.size(); i++) {
			campiMongo.put(elencoCampi.get(i).getFieldName(), elencoCampi.get(i));
			idDatasetTrovato = elencoCampi.get(i).getDatasetId();
			datasetVersionTrovato = elencoCampi.get(i).getDatasetVersion();
		}

		HashMap<String, FieldsDto> campiMongoV1 = new HashMap<String, FieldsDto>();
		if (elencoCampiV1!=null)
		{
			for (int i = 0; i < elencoCampiV1.size(); i++) {
				campiMongoV1.put(elencoCampiV1.get(i).getFieldName(), elencoCampiV1.get(i));
			}
		}

		// JSONObject ooo=null;
		JSONObject components = null;
		int i = 0;
		boolean endArray = false;

		String insStrConstBase = "streamCode : \"" + (stream != null ? stream : application) + "\"";
		insStrConstBase += " , idDataset : " + idDatasetTrovato;
		insStrConstBase += " , datasetVersion : " + datasetVersionTrovato;
		insStrConstBase += " , sensor : \"" + sensor + "\"";

		// "sensor" : "88c8dfb2-6323-5445-bf7d-6af67f0166b6",
		// "time" : ISODate("2014-09-03T05:35:00.000Z"),
		// "idDataset" : 4,
		// "datasetVersion" : 1,
		// "streamCode" : "TrFl",

		Object valuesObject = bloccoDaIns.get("values");
		JSONArray arrayValori;

		if (valuesObject instanceof JSONArray) {
			arrayValori = (JSONArray) bloccoDaIns.get("values");
		} else {
			arrayValori = new JSONArray();
			arrayValori.add(valuesObject);
		}

		// ArrayList<String> rigadains = new ArrayList<String>();
		// int numeroCampiMongo = elencoCampi.size();
		FieldsDto campotimeStamp = null;
		String timeStamp = null;
		campotimeStamp = new FieldsDto("aaa", FieldsDto.DATA_TYPE_DATETIME);
		ArrayList<JSONObject> listJson = new ArrayList<JSONObject>();
		JSONObject curElem = null;
		while (i < arrayValori.size() && !endArray) {
			try {
				// System.out.println(" TIMETIME parseMisura -- valore ("+i+") inizio--> "+System.currentTimeMillis());

				curElem = (JSONObject) arrayValori.get(i);

				components = (JSONObject) curElem.get("components");

				// Controllo del timeStamp
				timeStamp = (String) curElem.get("time");

				// System.out.println(" TIMETIME parseMisura -- valore ("+i+") recuperati oggetti--> "+System.currentTimeMillis());

				if (!campotimeStamp.validateValue(timeStamp, false))
					throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE, " - field time (" + insStrConstBase + "): " + timeStamp);

				// insStrConst+= ", time: {$date :\""+timeStamp+"\"} ";
				// rigadains.add(parseComponents(components, insStrConst,
				// elencoCampi));

				parseComponents(components, insStrConstBase + ", time: {$date :\"" + timeStamp + "\"} ", campiMongo, campiMongoV1, isVerOneRequired);
				// System.out.println(" TIMETIME parseMisura -- valore ("+i+") parsing components--> "+System.currentTimeMillis());
				components.put("objectid", ObjectId.get().toString());
				components.put("time", timeStamp);
				listJson.add(components);
				i++;
				// System.out.println(" TIMETIME parseMisura -- valore ("+i+") fine--> "+System.currentTimeMillis());

			} catch (PathNotFoundException e) {
				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException)
					endArray = true;
			}

		}
		CollectionConfDto collectionConfDto = metadataAccess.getCollectionInfo(tenant, idDatasetTrovato, datasetVersionTrovato,  datasetType);
		
		ret = new DatasetBulkInsert(collectionConfDto);
		ret.setDatasetVersion(datasetVersionTrovato);
		ret.setDatasetType(datasetType);
		ret.setIdDataset(idDatasetTrovato);
		ret.setNumRowToInsFromJson(i);
		// ret.setRowsToInsert(rigadains);
		ret.setFieldsType(campiMongo);
		ret.setJsonRowsToInsert(listJson);
		return ret;

	}

	public static void main(String[] args) throws Exception {
		
		String tenant = "pippo";
		String jsonInput = "{\"stream\":\"symontest\",\"sensor\":\"20e\",\"values\":{\"time\":\"2014-05-13T17:08:58Z\","
				+ "\"components\":{\"testvalue\":17.4}}}";
		

		jsonInput = "{stream:\"symontest\",\"sensor\":20e,\"values\":{\"time\":\"2014-05-13T17:08:58Z\","
				+ "\"components\":{\"testvalue\":\"17.4\", boolean:true, boolean2:NaN, pippo:12d, nullato:null}}}";
		
		System.out.println(getSmartobject_StreamFromJson(tenant, jsonInput).toJSONString());	
	}

	public DatasetDeleteOutput deleteManager( String codTenant, Long idDataset, Long datasetVersion) throws Exception {

		DatasetDeleteOutput outData = new DatasetDeleteOutput();

		SDPInsertApiPhoenixDataAccess phoenixAccess = new SDPInsertApiPhoenixDataAccess();

		SDPInsertMetadataApiAccess metadataAccess = SDPInsertMedataFactory.getSDPInsertMetadataApiAccess();
		
		DatasetInfo infoDataset = metadataAccess.getInfoDataset(idDataset, datasetVersion, codTenant);
		if (infoDataset == null)
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_DATASETVERSION_INVALID);
		log.finest("[InsertApiLogic::deleteManager]     infoDataset " + infoDataset);


		int numrowDeleted = phoenixAccess.deleteData(infoDataset, codTenant, idDataset, datasetVersion);

		outData.setDataDeleted(true);
		outData.setNumRowDeleted(numrowDeleted);
		outData.setDataDeletedMessage("Number of rows deleted on Phonix: " + numrowDeleted);

		// vado su hdfs
		try {
			String streamCode = null;
			String streamVirtualEntitySlug = null;
			if ("streamDataset".equals(infoDataset.getDatasetSubType()) || "socialDataset".equals(infoDataset.getDatasetSubType())) {
				StreamInfo streamInfo = metadataAccess.getStreamInfoForDataset(codTenant, idDataset, datasetVersion);
				streamCode = streamInfo.getStreamCode();
				streamVirtualEntitySlug = streamInfo.getVirtualEntitySlug();
			}

			String tenantOrganization = infoDataset.getOrganizationCode();
			
			// TODO codice necessario per mongo che non riesce a recuperare l'organizzazione dai metadata di mongo
			if (tenantOrganization == null)
			{
				tenantOrganization = new SDPInsertApiMongoDataAccess().getTenantInfo(codTenant).getOrganizationCode();
			}
			
			log.info("[InsertApiLogic::deleteManager]     tenantOrganization " + tenantOrganization);
			SDPInsertApiHdfsDataAccess sdpInsertApiHdfsDataAccess = new SDPInsertApiHdfsDataAccess();
			String responseStatus = sdpInsertApiHdfsDataAccess.deleteData(infoDataset.getDatasetType(), infoDataset.getDatasetSubType(), infoDataset.getDatasetDomain(),
					infoDataset.getDatasetSubdomain(), tenantOrganization, infoDataset.getDatasetCode(), streamVirtualEntitySlug, streamCode, idDataset, datasetVersion);
			outData.setFileDeleted(responseStatus != null && responseStatus.startsWith("OK"));
			outData.setFileDeletedMessage("HDFS response: " + responseStatus);
		} catch (Exception e) {
			log.warning("[InsertApi::dataDelete] Error on delete from HDFS: " + e.getMessage());
			outData.setFileDeleted(false);
			outData.setFileDeletedMessage("Error on delete HDFS: " + e.getMessage());
		}

		// vado su solr
		try {
			SDPInsertApiSolrDataAccess sdpInsertApiSolrDataAccess = new SDPInsertApiSolrDataAccess();
			int responseStatus = sdpInsertApiSolrDataAccess.deleteData(infoDataset.getDatasetSubType(), codTenant, idDataset, datasetVersion);
			outData.setIndexDeleted(true);
			outData.setIndexDeletedMessage("Sorl response status: " + responseStatus);

		} catch (Exception e) {
			log.warning("[InsertApi::dataDelete] Error on delete from Solr: " + e.getMessage());
			outData.setIndexDeleted(false);
			outData.setIndexDeletedMessage("Error on delete from Solr: " + e.getMessage());
		}

		return outData;

	}
}
