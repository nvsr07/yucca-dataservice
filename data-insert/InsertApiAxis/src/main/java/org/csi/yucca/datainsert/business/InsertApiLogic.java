package org.csi.yucca.datainsert.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.csi.yucca.datainsert.constants.SDPInsertApiConfig;
import org.csi.yucca.datainsert.dto.DatasetBulkInsert;
import org.csi.yucca.datainsert.dto.DbConfDto;
import org.csi.yucca.datainsert.dto.FieldsMongoDto;
import org.csi.yucca.datainsert.dto.MongoDatasetInfo;
import org.csi.yucca.datainsert.dto.MongoStreamInfo;
import org.csi.yucca.datainsert.exception.InsertApiBaseException;
import org.csi.yucca.datainsert.mongo.SDPInsertApiMongoConnectionSingleton;
import org.csi.yucca.datainsert.mongo.SDPInsertApiMongoDataAccess;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class InsertApiLogic {
	private static final Logger log=Logger.getLogger("org.csi.yucca.datainsert");


	public HashMap<String, DatasetBulkInsert> insertManager  (String tenant, HashMap<String, DatasetBulkInsert> datiToIns) throws Exception{

		Iterator<String> iter= datiToIns.keySet().iterator();

		//System.out.println(" TIMETIME insertManager -- start --> "+System.currentTimeMillis());

		SDPInsertApiMongoDataAccess mongoAccess=new SDPInsertApiMongoDataAccess();
		DatasetBulkInsert curBulkToIns=null;

		while (iter.hasNext()) {
			try {
				String key=iter.next();
				curBulkToIns=datiToIns.get(key);
				long millis=new Date().getTime();
				curBulkToIns.setTimestamp(millis);
				curBulkToIns.setRequestId(curBulkToIns.getIdDataset()+"_"+curBulkToIns.getDatasetVersion()+"_"+curBulkToIns.getTimestamp());
				curBulkToIns.setStatus(DatasetBulkInsert.STATUS_START_INS);
				datiToIns.put(key, curBulkToIns);
			} catch (Exception e) {
				curBulkToIns.setStatus(DatasetBulkInsert.STATUS_KO_INS);
				
			}
		}
		
		//System.out.println(" TIMETIME insertManager -- fine ciclo UNO --> "+System.currentTimeMillis());
		
		String idRequest=mongoAccess.insertStatusRecordArray(tenant,datiToIns);

		//System.out.println(" TIMETIME insertManager -- fine inserimento start ins --> "+System.currentTimeMillis());






		iter= datiToIns.keySet().iterator();

		
		int cnt=0;
		while (iter.hasNext()) {
			try {
				String key=iter.next();
				curBulkToIns=datiToIns.get(key);

				int righeinserite=mongoAccess.insertBulk(tenant, curBulkToIns);
				//System.out.println(" TIMETIME insertManager -- insert bulk blocco "+cnt+"--> "+System.currentTimeMillis());
				
				
				//TODO CONTROLLI
				curBulkToIns.setStatus(DatasetBulkInsert.STATUS_END_INS);
				curBulkToIns.setGlobalReqId(idRequest);

				datiToIns.put(key, curBulkToIns);
			} catch (Exception e) {
				log.log(Level.SEVERE, "[InsertApiLogic::insertManager] GenericException "+e);
				log.log(Level.WARNING, "[InsertApiLogic::insertManager] Fallito inserimento blocco --> globalRequestId="+idRequest + "    blockRequestId="+curBulkToIns.getRequestId() );
				curBulkToIns.setStatus(DatasetBulkInsert.STATUS_KO_INS);
				try {
					curBulkToIns.setStatus(DatasetBulkInsert.STATUS_KO_INS);

				} catch (Exception k) {

				}

			}
		}
		//System.out.println(" TIMETIME insertManager -- fine inserimento bulk --> "+System.currentTimeMillis());
		mongoAccess.updateStatusRecordArray(tenant, idRequest, "end_ins", datiToIns);
		
		//System.out.println(" TIMETIME insertManager -- end --> "+System.currentTimeMillis());
		
		
		return datiToIns;

	}	


	public HashMap<String, DatasetBulkInsert> parseJsonInputDataset(String tenant, String jsonInput) throws Exception {
		int i =0;
		boolean endArray=false;
		JSONObject ooo=null;
		HashMap<String, DatasetBulkInsert> ret= new HashMap<String, DatasetBulkInsert>();
		SDPInsertApiMongoDataAccess mongoAccess=new SDPInsertApiMongoDataAccess();
		MongoDatasetInfo infoDataset=null;
		Integer datasetVersion=null;
		int reqVersion=-1;
		
		int totalDocumentsToIns=0;
		
		while (i<100000 && !endArray) {
			try {
				//System.out.println(" TIMETIME parseJsonInputDataset -- inizio blocco "+i+"--> "+System.currentTimeMillis());
				
				
				ooo = JsonPath.read(jsonInput, "$["+i+"]");
				if (null==ooo) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DATA_NOTARRAY);
				String datasetCode=(String)ooo.get("datasetCode");


				// recuepro info dataset inclusi i campi
				//datasetVersion=JsonPath.read(jsonInput, "$.datasetVersion");
				datasetVersion=(Integer)ooo.get("$.datasetVersion");
				reqVersion=(datasetVersion==null ? -1 : datasetVersion.intValue());
				//System.out.println(" TIMETIME parseJsonInputDataset -- lettura oggetto--> "+System.currentTimeMillis());
				infoDataset=mongoAccess.getInfoDataset(datasetCode, reqVersion,tenant);
				//System.out.println(" TIMETIME parseJsonInputDataset -- recuperata info dataset--> "+System.currentTimeMillis());
				
				if (null==infoDataset)  throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_DATASETVERSION_INVALID, " for dataset "+datasetCode);
				

				String insStrConst="";
				insStrConst+= "  idDataset : "+infoDataset.getDatasetId();
				insStrConst+= " , datasetVersion : "+infoDataset.getDatasetVersion();


				// se dataset è stream, recupero info stream
				boolean isStream=false;
				String streamCode=null;
				String sensor=null;
				if ("streamDataset".equals(infoDataset.getDatasetSubType())) {
					//aggiungo il campo fittizio Time
					infoDataset.getCampi().add(new FieldsMongoDto("time", FieldsMongoDto.DATA_TYPE_DATETIME,infoDataset.getDatasetId(),infoDataset.getDatasetVersion()));
					MongoStreamInfo infoStream =mongoAccess.getStreamInfoForDataset(tenant, infoDataset.getDatasetId(),infoDataset.getDatasetVersion());
					//System.out.println(" TIMETIME parseJsonInputDataset -- recuperata info stream--> "+System.currentTimeMillis());

					//aggiungo stream e sensore alla costante 
					insStrConst+= " , sensor : \""+infoStream.getSensorCode()+"\"";
					insStrConst+= " , streamCode : \"" + infoStream.getStreamCode() +"\"";
					sensor=infoStream.getSensorCode();
					streamCode=infoStream.getStreamCode();
				}
				// se dataset è dataset va bene cosi' 

				if (ret.get(datasetCode)!=null) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DUPLICATE, " for dataset "+datasetCode);
				
				
				//DatasetBulkInsert datiToins=parseGenericDataset(tenant, ooo.toJSONString(), insStrConst, infoDataset);
				DatasetBulkInsert datiToins=parseGenericDataset(tenant, ooo, insStrConst, infoDataset);
				
				
				//System.out.println(" TIMETIME parseJsonInputDataset -- parsificato dataset info--> "+System.currentTimeMillis());
				datiToins.setDatasetCode(datasetCode);
				datiToins.setStream(streamCode);
				datiToins.setSensor(sensor);
				datiToins.setStatus(DatasetBulkInsert.STATUS_SYNTAX_CHECKED);
				
				ret.put(datasetCode, datiToins);
				totalDocumentsToIns=totalDocumentsToIns+datiToins.getNumRowToInsFromJson();
				if (totalDocumentsToIns>SDPInsertApiConfig.MAX_DOCUMENTS_IN_REQUEST) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_MAXRECORDS);

				i++;

	
			} catch (PathNotFoundException e) {
				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException) {
					endArray=true;
				} else {
					log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputDataset] PathNotFoundException imprevisto --> " + e );
					throw e;
				}
			} catch (Exception ex) {
				log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputDataset] GenericEsxception" + ex );
				i++;
				endArray=true;
				throw ex;
			} finally {
				//System.out.println(" TIMETIME parseJsonInputDataset -- fine metodo--> "+System.currentTimeMillis());
				
				
			}
		}
		return ret;

	}	

	public HashMap<String, DatasetBulkInsert> parseJsonInputStream(String tenant, String jsonInput) throws Exception {
		int i =0;
		boolean endArray=false;
		JSONObject ooo=null;
		HashMap<String, DatasetBulkInsert> ret= new HashMap<String, DatasetBulkInsert>();
		
		int totalDocumentsToIns=0;
		String sensor=null;
		String application=null;
		String stream=null;
		String streamToFind=null;
		while (i<100000 && !endArray) {
			try {
				ooo = JsonPath.read(jsonInput, "$["+i+"]");
				if (null==ooo) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DATA_NOTARRAY);
				 sensor=(String)ooo.get("sensor");
				application=(String)ooo.get("application");
				stream=(String)ooo.get("stream");



				streamToFind=(stream!=null ? stream: application);

				//TODO non so se e' bloccante ... 
				if (streamToFind == null) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_STREAM_MANCANTE);
				if (sensor == null ) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE);



				if (ret.get(sensor+"_"+streamToFind)!=null) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DUPLICATE, " for stream "+streamToFind);


				DatasetBulkInsert datiToins=parseMisura(tenant, ooo);

				//TODO .. controllo sul numero di record da inserire

				datiToins.setStream(streamToFind);
				datiToins.setSensor(sensor);
				datiToins.setStatus(DatasetBulkInsert.STATUS_SYNTAX_CHECKED);
				
				totalDocumentsToIns=totalDocumentsToIns+datiToins.getNumRowToInsFromJson();
				if (totalDocumentsToIns>SDPInsertApiConfig.MAX_DOCUMENTS_IN_REQUEST) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_MAXRECORDS);
				//System.out.println("           "+datiToins.getRowsToInsert().size()+"/"+ datiToins.getNumRowToInsFromJson());

				ret.put(sensor+"_"+streamToFind, datiToins);


				i++;
			} catch (PathNotFoundException e) {
				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException) {
					endArray=true;
				} else {
					log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputStream] PathNotFoundException imprevisto --> " + e );
					throw e;
				}
			} catch (Exception ex) {
				log.log(Level.SEVERE, "[InsertApiLogic::parseJsonInputStream] GenericEsxception" + ex );
				i++;
				endArray=true;
				throw ex;
			} finally {
				//System.out.println(" TIMETIME parseJsonInputDataset -- fine metodo--> "+System.currentTimeMillis());
				
				
			}
		}
		return ret;

	}	


	private DatasetBulkInsert parseGenericDataset(String tenant, JSONObject bloccoDaIns,String insStrConst,MongoDatasetInfo datasetMongoInfo) throws Exception {
		//System.out.println(" TIMETIME parseGenericDataset -- inizio--> "+System.currentTimeMillis());
		DatasetBulkInsert ret=null;

		JSONObject ooo=null;
		JSONObject components=null;
		boolean endArray=false;
		ArrayList<String> rigadains= new ArrayList<String>();

		ArrayList<FieldsMongoDto> elencoCampi=datasetMongoInfo.getCampi();

		HashMap<String, FieldsMongoDto> campiMongo= new HashMap<String, FieldsMongoDto>();
		for (int i = 0; i< elencoCampi.size();i++) {
			campiMongo.put(elencoCampi.get(i).getFieldName(), elencoCampi.get(i));
		}

		
		
		int i =0;
		JSONArray arrayValori=(JSONArray)bloccoDaIns.get("values");

		//System.out.println(" TIMETIME parseGenericDataset -- inzio ciclo controllo--> "+System.currentTimeMillis());
		int numeroCampiMongo=elencoCampi.size();
		while (!endArray && i<arrayValori.size()) {
			try {
				//System.out.println(" TIMETIME parseGenericDataset -- blocco ("+i+")--> "+System.currentTimeMillis());
				//components = JsonPath.read(jsonInput, "$.values["+i+"]");
				
				components=(JSONObject)arrayValori.get(i);
				
				//System.out.println(" TIMETIME parseGenericDataset -- blocco ("+i+") JsonPath--> "+System.currentTimeMillis());
				//rigadains.add(parseComponents(components, insStrConst, elencoCampi));
				rigadains.add(parseComponents(components, insStrConst, campiMongo));
				
				
				//System.out.println(" TIMETIME parseGenericDataset -- dentro ciclo ("+i+") parse componenti--> "+System.currentTimeMillis());

				
				
				if (components.keySet().size()!=numeroCampiMongo) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE);
//				Iterator<String> itCampiCheck=campiMongo.keySet().iterator();
//				while (itCampiCheck.hasNext()) {
//					String nome=itCampiCheck.next();
//					FieldsMongoDto campoMongo=campiMongo.get(nome);
//					if (campoMongo.isSuccessChecked()==false) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE,
//							" - field "+nome+" ("+insStrConst+") not found in input data : "+ooo); 
//					(campiMongo.get(nome)).setSuccessChecked(false);
//				}				
				//System.out.println(" TIMETIME parseGenericDataset -- dentro ciclo ("+i+") ciclo controllo--> "+System.currentTimeMillis());
				//System.out.println(" TIMETIME parseGenericDataset -- blocco ("+i+") fine--> "+System.currentTimeMillis());

				i++;
			} catch (PathNotFoundException e) {
				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException) endArray=true;
			}




		}
		//System.out.println(" TIMETIME parseGenericDataset -- fine ciclo controllo--> "+System.currentTimeMillis());
		ret= new DatasetBulkInsert();
		ret.setIdDataset(datasetMongoInfo.getDatasetId());
		ret.setDatasetVersion(datasetMongoInfo.getDatasetVersion());
		ret.setNumRowToInsFromJson(i);
		ret.setRowsToInsert(rigadains);
		//System.out.println(" TIMETIME parseGenericDataset -- fine--> "+System.currentTimeMillis());
		
		return ret;
	}	
	
	private DatasetBulkInsert parseGenericDataset(String tenant, String jsonInput,String insStrConst,MongoDatasetInfo datasetMongoInfo) throws Exception {
		//System.out.println(" TIMETIME parseGenericDataset -- inizio--> "+System.currentTimeMillis());
		DatasetBulkInsert ret=null;

		JSONObject ooo=null;
		JSONObject components=null;
		boolean endArray=false;
		ArrayList<String> rigadains= new ArrayList<String>();

		ArrayList<FieldsMongoDto> elencoCampi=datasetMongoInfo.getCampi();

		HashMap<String, FieldsMongoDto> campiMongo= new HashMap<String, FieldsMongoDto>();
		for (int i = 0; i< elencoCampi.size();i++) {
			campiMongo.put(elencoCampi.get(i).getFieldName(), elencoCampi.get(i));
		}

		
		
		int i =0;


		//System.out.println(" TIMETIME parseGenericDataset -- inzio ciclo controllo--> "+System.currentTimeMillis());
		int numeroCampiMongo=elencoCampi.size();
		while (i<100000 && !endArray) {
			try {
				//System.out.println(" TIMETIME parseGenericDataset -- blocco ("+i+")--> "+System.currentTimeMillis());
				components = JsonPath.read(jsonInput, "$.values["+i+"]");
				//System.out.println(" TIMETIME parseGenericDataset -- blocco ("+i+") JsonPath--> "+System.currentTimeMillis());
				//rigadains.add(parseComponents(components, insStrConst, elencoCampi));
				rigadains.add(parseComponents(components, insStrConst, campiMongo));
				
				
				//System.out.println(" TIMETIME parseGenericDataset -- dentro ciclo ("+i+") parse componenti--> "+System.currentTimeMillis());

				
				
				if (components.keySet().size()!=numeroCampiMongo) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE);
//				Iterator<String> itCampiCheck=campiMongo.keySet().iterator();
//				while (itCampiCheck.hasNext()) {
//					String nome=itCampiCheck.next();
//					FieldsMongoDto campoMongo=campiMongo.get(nome);
//					if (campoMongo.isSuccessChecked()==false) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE,
//							" - field "+nome+" ("+insStrConst+") not found in input data : "+ooo); 
//					(campiMongo.get(nome)).setSuccessChecked(false);
//				}				
				//System.out.println(" TIMETIME parseGenericDataset -- dentro ciclo ("+i+") ciclo controllo--> "+System.currentTimeMillis());
				//System.out.println(" TIMETIME parseGenericDataset -- blocco ("+i+") fine--> "+System.currentTimeMillis());

				i++;
			} catch (PathNotFoundException e) {
				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException) endArray=true;
			}




		}
		//System.out.println(" TIMETIME parseGenericDataset -- fine ciclo controllo--> "+System.currentTimeMillis());
		ret= new DatasetBulkInsert();
		ret.setIdDataset(datasetMongoInfo.getDatasetId());
		ret.setDatasetVersion(datasetMongoInfo.getDatasetVersion());
		ret.setNumRowToInsFromJson(i);
		ret.setRowsToInsert(rigadains);
		//System.out.println(" TIMETIME parseGenericDataset -- fine--> "+System.currentTimeMillis());
		
		return ret;
	}
	
	
	//private String parseComponents (JSONObject components,String insStrConst,ArrayList<FieldsMongoDto> elencoCampi) throws Exception {
	private String parseComponents (JSONObject components,String insStrConst,HashMap<String, FieldsMongoDto> campiMongo) throws Exception {		
		ArrayList<String> rigadains= new ArrayList<String>();
		Iterator<String> itCampiJson=components.keySet().iterator();


		String currRigaIns=null;
//		HashMap<String, FieldsMongoDto> campiMongo= new HashMap<String, FieldsMongoDto>();
//
//
//		for (int i = 0; i< elencoCampi.size();i++) {
//			campiMongo.put(elencoCampi.get(i).getFieldName(), elencoCampi.get(i));
//		}

		while (itCampiJson.hasNext()) {
			String jsonField=(String)itCampiJson.next();

			// 1. verifico che il campo sia tra quelli presenti nei metadati
			
			FieldsMongoDto campoMongo= campiMongo.get(jsonField);
			if (null==campoMongo ) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INVALID_COMPONENTS,
					" component "+jsonField+" not found in stream configuration ("+insStrConst+")");

			String valore= (components.get(jsonField)).toString();
			//validazione del valore
			if (!campoMongo.validateValue(valore))  throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE,
					" - field "+jsonField+" ("+insStrConst+"): "+valore);
			(campiMongo.get(jsonField)).setSuccessChecked(true);


			if (currRigaIns==null) currRigaIns= campoMongo.getInsertJson(valore);
			else currRigaIns+=" , "+  campoMongo.getInsertJson(valore);

		}
		if (currRigaIns!=null) {
			//currRigaIns = insStrConst+", time: {$date :\""+timeStamp+"\"} , "+currRigaIns;
			currRigaIns = insStrConst+", "+currRigaIns;
			//System.out.println(currRigaIns);
			//rigadains.add(currRigaIns);

		} else {
			//System.out.println(       "OKKKKIOOOO riga vuota ???? ");

		}

		return currRigaIns;


	}
	
	
	private DatasetBulkInsert parseMisura(String tenant, JSONObject bloccoDaIns) throws Exception {
		//Integer datasetVersion=JsonPath.read(jsonInput, "$.datasetVersion");
		Integer datasetVersion=(Integer)bloccoDaIns.get("datasetVersion");
		int reqVersion=-1;
		DatasetBulkInsert ret=null;

//		String stream=JsonPath.read(jsonInput, "$.stream");
//		String sensor=JsonPath.read(jsonInput, "$.sensor");
//		String application=JsonPath.read(jsonInput, "$.application");

		String stream=(String)bloccoDaIns.get("stream");
		String sensor=(String)bloccoDaIns.get("sensor");
		String application=(String)bloccoDaIns.get("application");
		
		
		if (application == null && stream == null) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_STREAM_MANCANTE);
		if (sensor == null ) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE);
		if (null!=datasetVersion) reqVersion=datasetVersion.intValue();

		SDPInsertApiMongoDataAccess mongoAccess=new SDPInsertApiMongoDataAccess();
		ArrayList<MongoStreamInfo> elencoStream=mongoAccess.getStreamInfo(tenant, (stream!=null ? stream : application), sensor);

		if (elencoStream==null || elencoStream.size()<=0) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_STREAM_NOT_FOUND, ": "+(stream!=null ? stream : application) +" (sensor: "+sensor+")");
		ArrayList<FieldsMongoDto> elencoCampi=mongoAccess.getCampiDataSet(elencoStream, Long.parseLong(""+reqVersion) );

		if (elencoCampi==null || elencoCampi.size()<=0) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_DATASETVERSION_INVALID, ": "+(stream!=null ? stream : application) +" (sensor: "+sensor+")");
		HashMap<String, FieldsMongoDto> campiMongo= new HashMap<String, FieldsMongoDto>();
		long idDatasetTrovato=-1;
		long datasetVersionTrovato=-1;
		for (int i = 0; i< elencoCampi.size();i++) {
			campiMongo.put(elencoCampi.get(i).getFieldName(), elencoCampi.get(i));
			idDatasetTrovato=elencoCampi.get(i).getDatasetId();
			datasetVersionTrovato=elencoCampi.get(i).getDatasetVersion();
		}
		//JSONObject ooo=null;
		JSONObject components=null;
		int i =0;
		boolean endArray=false;

		String insStrConstBase= "streamCode : \"" + (stream!=null ? stream : application) +"\"";
		insStrConstBase+= " , idDataset : "+idDatasetTrovato;
		insStrConstBase+= " , datasetVersion : "+datasetVersionTrovato;
		insStrConstBase+= " , sensor : \""+sensor+"\"";

		//			    "sensor" : "88c8dfb2-6323-5445-bf7d-6af67f0166b6",
		//			    "time" : ISODate("2014-09-03T05:35:00.000Z"),
		//			    "idDataset" : 4,
		//			    "datasetVersion" : 1,
		//			    "streamCode" : "TrFl",

		JSONArray arrayValori=(JSONArray)bloccoDaIns.get("values");
		ArrayList<String> rigadains= new ArrayList<String>();
		int numeroCampiMongo=elencoCampi.size();
		FieldsMongoDto campotimeStamp=null;
		String timeStamp=null;
		campotimeStamp= new FieldsMongoDto("aaa",FieldsMongoDto.DATA_TYPE_DATETIME);
		
		JSONObject curElem=null;
		while (i<arrayValori.size() && !endArray) {
			try {
				//System.out.println(" TIMETIME parseMisura -- valore ("+i+") inizio--> "+System.currentTimeMillis());
				
				
				curElem=(JSONObject)arrayValori.get(i);
				
				
				components=(JSONObject)curElem.get("components");
				

				//Controllo del timeStamp
				timeStamp= (String)curElem.get("time");

				//System.out.println(" TIMETIME parseMisura -- valore ("+i+") recuperati oggetti--> "+System.currentTimeMillis());
				
				if (!campotimeStamp.validateValue(timeStamp))  throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE,
						" - field time ("+insStrConstBase+"): "+timeStamp);




				//insStrConst+= ", time: {$date :\""+timeStamp+"\"} ";
				//rigadains.add(parseComponents(components, insStrConst, elencoCampi));
				
				rigadains.add(parseComponents(components, insStrConstBase+", time: {$date :\""+timeStamp+"\"} ", campiMongo));
				//System.out.println(" TIMETIME parseMisura -- valore ("+i+") parsing components--> "+System.currentTimeMillis());
				


				//ora controllo che tra i campi che arrivano dalla cfg non ce ne sia qualcuno che non era presente nel json in input
				
				if (components.keySet().size()!=numeroCampiMongo) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE);
				
				
//				Iterator<String> itCampiCheck=campiMongo.keySet().iterator();
//				while (itCampiCheck.hasNext()) {
//					String nome=itCampiCheck.next();
//					FieldsMongoDto campoMongo=campiMongo.get(nome);
//					if (campoMongo.isSuccessChecked()==false) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE,
//							" - field "+nome+" ("+insStrConst+") not found in input data : "+((JSONObject)arrayValori.get(i)).toJSONString()); 
//					(campiMongo.get(nome)).setSuccessChecked(false);
//				}


				i++;
				//System.out.println(" TIMETIME parseMisura -- valore ("+i+") fine--> "+System.currentTimeMillis());


			} catch (PathNotFoundException e) {
				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException) endArray=true;
			}

		}
		ret= new DatasetBulkInsert();
		ret.setDatasetVersion(datasetVersionTrovato);
		ret.setIdDataset(idDatasetTrovato);
		ret.setNumRowToInsFromJson(i);
		ret.setRowsToInsert(rigadains);
		return ret;

	}		
	
	private DatasetBulkInsert parseMisuraOLD(String tenant, String jsonInput) throws Exception {
		Integer datasetVersion=JsonPath.read(jsonInput, "$.datasetVersion");
		int reqVersion=-1;
		DatasetBulkInsert ret=null;

		String stream=JsonPath.read(jsonInput, "$.stream");
		String sensor=JsonPath.read(jsonInput, "$.sensor");
		String application=JsonPath.read(jsonInput, "$.application");
		if (application == null && stream == null) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_STREAM_MANCANTE);
		if (sensor == null ) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE);
		if (null!=datasetVersion) reqVersion=datasetVersion.intValue();

		SDPInsertApiMongoDataAccess mongoAccess=new SDPInsertApiMongoDataAccess();
		ArrayList<MongoStreamInfo> elencoStream=mongoAccess.getStreamInfo(tenant, (stream!=null ? stream : application), sensor);

		if (elencoStream==null || elencoStream.size()<=0) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_STREAM_NOT_FOUND, ": "+(stream!=null ? stream : application) +" (sensor: "+sensor+")");
		ArrayList<FieldsMongoDto> elencoCampi=mongoAccess.getCampiDataSet(elencoStream, Long.parseLong(""+reqVersion) );

		if (elencoCampi==null || elencoCampi.size()<=0) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_DATASETVERSION_INVALID, ": "+(stream!=null ? stream : application) +" (sensor: "+sensor+")");
		HashMap<String, FieldsMongoDto> campiMongo= new HashMap<String, FieldsMongoDto>();
		long idDatasetTrovato=-1;
		long datasetVersionTrovato=-1;
		for (int i = 0; i< elencoCampi.size();i++) {
			campiMongo.put(elencoCampi.get(i).getFieldName(), elencoCampi.get(i));
			idDatasetTrovato=elencoCampi.get(i).getDatasetId();
			datasetVersionTrovato=elencoCampi.get(i).getDatasetVersion();
		}
		JSONObject ooo=null;
		JSONObject components=null;
		int i =0;
		boolean endArray=false;

		String insStrConst= "streamCode : \"" + (stream!=null ? stream : application) +"\"";
		insStrConst+= " , idDataset : "+idDatasetTrovato;
		insStrConst+= " , datasetVersion : "+datasetVersionTrovato;
		insStrConst+= " , sensor : \""+sensor+"\"";

		//			    "sensor" : "88c8dfb2-6323-5445-bf7d-6af67f0166b6",
		//			    "time" : ISODate("2014-09-03T05:35:00.000Z"),
		//			    "idDataset" : 4,
		//			    "datasetVersion" : 1,
		//			    "streamCode" : "TrFl",


		ArrayList<String> rigadains= new ArrayList<String>();
		while (i<100000 && !endArray) {
			try {
				ooo = JsonPath.read(jsonInput, "$.values["+i+"]");
				components= JsonPath.read(jsonInput, "$.values["+i+"].components");


				//Controllo del timeStamp
				String timeStamp= (String)ooo.get("time");
				FieldsMongoDto campotimeStamp= new FieldsMongoDto("aaa",FieldsMongoDto.DATA_TYPE_DATETIME);
				if (!campotimeStamp.validateValue(timeStamp))  throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE,
						" - field time ("+insStrConst+"): "+timeStamp);


				//Controllo sui fields
				//Iterator<String> itCampiJson=components.keySet().iterator();


				//				String currRigaIns=null;
				//
				//				while (itCampiJson.hasNext()) {
				//					String jsonField=(String)itCampiJson.next();
				//
				//					// 1. verifico che il campo sia tra quelli presenti nei metadati
				//					FieldsMongoDto campoMongo= campiMongo.get(jsonField);
				//					if (null==campoMongo ) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INVALID_COMPONENTS,
				//							" component "+jsonField+" not found in stream configuration ("+insStrConst+")");
				//
				//					String valore= (components.get(jsonField)).toString();
				//					//validazione del valore
				//					if (!campoMongo.validateValue(valore))  throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE,
				//							" - field "+jsonField+" ("+insStrConst+"): "+valore);
				//					(campiMongo.get(jsonField)).setSuccessChecked(true);
				//
				//
				//					if (currRigaIns==null) currRigaIns= campoMongo.getInsertJson(valore);
				//					else currRigaIns+=" , "+  campoMongo.getInsertJson(valore);
				//
				//				}
				//				if (currRigaIns!=null) {
				//					currRigaIns = insStrConst+", time: {$date :\""+timeStamp+"\"} , "+currRigaIns;
				//					System.out.println(currRigaIns);
				//					rigadains.add(currRigaIns);
				//
				//				} else {
				//					System.out.println(       "OKKKKIOOOO riga vuota ???? ");
				//
				//				}

				insStrConst+= ", time: {$date :\""+timeStamp+"\"} ";
				//rigadains.add(parseComponents(components, insStrConst, elencoCampi));
				
				rigadains.add(parseComponents(components, insStrConst, campiMongo));
				


				//ora controllo che tra i campi che arrivano dalla cfg non ce ne sia qualcuno che non era presente nel json in input
				Iterator<String> itCampiCheck=campiMongo.keySet().iterator();
				while (itCampiCheck.hasNext()) {
					String nome=itCampiCheck.next();
					FieldsMongoDto campoMongo=campiMongo.get(nome);
					if (campoMongo.isSuccessChecked()==false) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE,
							" - field "+nome+" ("+insStrConst+") not found in input data : "+ooo); 
					(campiMongo.get(nome)).setSuccessChecked(false);
				}


				i++;


			} catch (PathNotFoundException e) {
				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException) endArray=true;
			}

		}
		ret= new DatasetBulkInsert();
		ret.setDatasetVersion(datasetVersionTrovato);
		ret.setIdDataset(idDatasetTrovato);
		ret.setNumRowToInsFromJson(i);
		ret.setRowsToInsert(rigadains);
		return ret;

	}	


	public void copyData(String tenantCode,String globlalRequestId) throws Exception {
		SDPInsertApiMongoDataAccess mongoAccess=new SDPInsertApiMongoDataAccess();
		boolean startCopy= mongoAccess.updateGlobalRequestStatus(globlalRequestId, DatasetBulkInsert.STATUS_START_COPY,DatasetBulkInsert.STATUS_END_INS) ;
		if (startCopy==false) throw new Exception("richiesta non trovata o in stato non corretto");

		ArrayList<DatasetBulkInsert> datiRibalta= mongoAccess.getElencoRichiesteByGlobRequestId(globlalRequestId);
		if (null==datiRibalta || datiRibalta.size()<=0)  throw new Exception("nessun blocco da elaborare per la richiesta "+globlalRequestId);



		boolean allOk=true;
		for (int i = 0; i<datiRibalta.size();i++) {

			boolean curblockOk=true;
			log.log(Level.INFO, "[InsertApiLogic::copyData]**** Start Elaboration block: "+i);
			log.log(Level.INFO, "[InsertApiLogic::copyData]               datasetId:"+datiRibalta.get(i).getIdDataset());
			log.log(Level.INFO, "[InsertApiLogic::copyData]               datasetVersion:"+datiRibalta.get(i).getDatasetVersion());
			log.log(Level.INFO, "[InsertApiLogic::copyData]               requestId:"+datiRibalta.get(i).getRequestId());
			log.log(Level.INFO, "[InsertApiLogic::copyData]               numberofDocs:"+datiRibalta.get(i).getNumRowToInsFromJson());
			log.log(Level.INFO, "[InsertApiLogic::copyData]               startstatus:"+datiRibalta.get(i).getStatus());
			if (!DatasetBulkInsert.STATUS_END_INS.equals(datiRibalta.get(i).getStatus())) {
				log.log(Level.INFO, "[InsertApiLogic::copyData]               ERROR:incorrect status");
				allOk=false;
			} else {
				curblockOk= copyBlock(datiRibalta.get(i),globlalRequestId,tenantCode);
			}
			log.log(Level.INFO, "[InsertApiLogic::copyData] end copy block --> " +curblockOk);

			if(!curblockOk) allOk=false;

		}


		boolean fineOp=false;
		if (allOk)
			fineOp= mongoAccess.updateGlobalRequestStatus(globlalRequestId, DatasetBulkInsert.STATUS_END_COPY,DatasetBulkInsert.STATUS_START_COPY) ;
		else 
			fineOp= mongoAccess.updateGlobalRequestStatus(globlalRequestId, DatasetBulkInsert.STATUS_KO_COPY,DatasetBulkInsert.STATUS_START_COPY) ;



	}
	private boolean copyBlock(DatasetBulkInsert blockInfo,String globlalRequestId,String tenantCode) throws Exception {
		SDPInsertApiMongoDataAccess mongoAccess=new SDPInsertApiMongoDataAccess();
		//TODO okkio... .ragioniamo solo 
		
		MongoDatasetInfo infoDataset = null;
		
		try {
			log.log(Level.INFO, "[InsertApiLogic::copyBlock] blockInfo --> "+blockInfo);
			log.log(Level.INFO, "[InsertApiLogic::copyBlock] blockInfogetDatasetCode --> "+blockInfo.getDatasetCode());
			log.log(Level.INFO, "[InsertApiLogic::copyBlock] blockInfogetDatasetVersion --> "+blockInfo.getDatasetVersion());
			log.log(Level.INFO, "[InsertApiLogic::copyBlock] tenantCode --> "+tenantCode);
			infoDataset=mongoAccess.getInfoDataset(blockInfo.getDatasetCode(), blockInfo.getDatasetVersion(),tenantCode);
		} catch (Exception e ) {
			log.log(Level.WARNING, "[InsertApiLogic::copyBlock] errore nel recupero di info dataset "+e);
		}
		
		DbConfDto dbConfgi= null;
		if (null != infoDataset && "bulkDataset".equals(infoDataset.getDatasetSubType())) {
			dbConfgi= SDPInsertApiMongoConnectionSingleton.getInstance().getDataDbConfiguration(SDPInsertApiMongoConnectionSingleton.DB_DATA, tenantCode);	
			
		} else {
			dbConfgi= SDPInsertApiMongoConnectionSingleton.getInstance().getDataDbConfiguration(SDPInsertApiMongoConnectionSingleton.DB_MESURES, tenantCode);	
		}

		
		log.log(Level.INFO, "[InsertApiLogic::copyBlock] DBTARGET-->"+ dbConfgi.getDataBase());
		log.log(Level.INFO, "[InsertApiLogic::copyBlock] COLLECTION-->"+ dbConfgi.getCollection());
		
		try {

			boolean startCopyExecuted = mongoAccess.updateSingleArreayRequestStatus(globlalRequestId, DatasetBulkInsert.STATUS_START_COPY,blockInfo.getRequestId()) ;
			if (!startCopyExecuted) throw new Exception("error in setting start copy for blocrk "+blockInfo.getRequestId());


			int documnetsCopied=mongoAccess.copyRecords(tenantCode, globlalRequestId, blockInfo, dbConfgi);

			if (documnetsCopied!=blockInfo.getNumRowToInsFromJson()) {
				boolean KoCopyExecuted = mongoAccess.updateSingleArreayRequestStatus(globlalRequestId, DatasetBulkInsert.STATUS_KO_COPY,blockInfo.getRequestId()) ;
				if (!KoCopyExecuted) throw new Exception("error in setting ko copy for blocrk "+blockInfo.getRequestId());
				return  false;
			} else {
				boolean endtCopyExecuted = mongoAccess.updateSingleArreayRequestStatus(globlalRequestId, DatasetBulkInsert.STATUS_END_COPY,blockInfo.getRequestId()) ;
				if (!endtCopyExecuted) throw new Exception("error in setting end copy for blocrk "+blockInfo.getRequestId());
			}
			return true;
		} catch (Exception e ) {
			log.log(Level.SEVERE, "[InsertApiLogic::copyBlock] errore nel recupero di info dataset "+e);
			return false;
		}
	}	

}
