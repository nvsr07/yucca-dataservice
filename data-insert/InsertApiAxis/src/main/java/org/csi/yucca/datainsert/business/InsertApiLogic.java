package org.csi.yucca.datainsert.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import net.minidev.json.JSONObject;

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


	public HashMap<String, DatasetBulkInsert> insertManager  (String tenant, HashMap<String, DatasetBulkInsert> datiToIns) throws Exception{

		Iterator<String> iter= datiToIns.keySet().iterator();


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
				System.out.println("KO-->"+curBulkToIns.getRequestId());
			}
		}
		String idRequest=mongoAccess.insertStatusRecordArray(tenant,datiToIns);







		iter= datiToIns.keySet().iterator();

		while (iter.hasNext()) {
			try {
				String key=iter.next();
				curBulkToIns=datiToIns.get(key);
				//				boolean statains=mongoAccess.insertStatusRecord(tenant, curBulkToIns);
				//				if (!statains) {
				//					curBulkToIns.setStatus(DatasetBulkInsert.STATUS_KO_INS);
				//				} 

				int righeinserite=mongoAccess.insertBulk(tenant, curBulkToIns);
				System.out.println("righeinserite-->"+righeinserite);
				//TODO CONTROLLI
				curBulkToIns.setStatus(DatasetBulkInsert.STATUS_END_INS);
				curBulkToIns.setGlobalReqId(idRequest);

				datiToIns.put(key, curBulkToIns);
			} catch (Exception e) {
				curBulkToIns.setStatus(DatasetBulkInsert.STATUS_KO_INS);
				try {
					curBulkToIns.setStatus(DatasetBulkInsert.STATUS_KO_INS);

				} catch (Exception k) {

				}
				System.out.println("KO-->"+curBulkToIns.getRequestId());
			}
		}
		mongoAccess.updateStatusRecordArray(tenant, idRequest, "end_ins", datiToIns);
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
		while (i<100000 && !endArray) {
			try {
				
				System.out.println(" **** parseJsonInputDataset -- BEGIN ");
				
				ooo = JsonPath.read(jsonInput, "$["+i+"]");
				if (null==ooo) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DATA_NOTARRAY);
				String datasetCode=(String)ooo.get("datasetCode");


				// recuepro info dataset inclusi i campi
				//datasetVersion=JsonPath.read(jsonInput, "$.datasetVersion");
				datasetVersion=(Integer)ooo.get("$.datasetVersion");
				reqVersion=(datasetVersion==null ? -1 : datasetVersion.intValue());
				infoDataset=mongoAccess.getInfoDataset(datasetCode, reqVersion);
				

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

					//aggiungo stream e sensore alla costante 
					insStrConst+= " , sensor : \""+infoStream.getSensorCode()+"\"";
					insStrConst+= " , streamCode : \"" + infoStream.getStreamCode() +"\"";
					sensor=infoStream.getSensorCode();
					streamCode=infoStream.getStreamCode();
				}
				// se dataset è dataset va bene cosi' 

				DatasetBulkInsert datiToins=parseGenericDataset(tenant, ooo.toJSONString(), insStrConst, infoDataset);
				datiToins.setDatasetCode(datasetCode);
				datiToins.setStream(streamCode);
				datiToins.setSensor(sensor);
				datiToins.setStatus(DatasetBulkInsert.STATUS_SYNTAX_CHECKED);
				
				ret.put(datasetCode, datiToins);

				i++;

				//
				//				
				//				
				//				
				//				String streamToFind=(stream!=null ? stream: application);
				//				
				//				//TODO non so se e' bloccante ... 
				//				if (streamToFind == null) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_STREAM_MANCANTE);
				//				if (sensor == null ) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE);
				//
				//
				//				
				//				if (ret.get(sensor+"_"+streamToFind)!=null) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DUPLICATE, " for stream "+streamToFind);
				//
				//				System.out.println(ooo);
				//
				//				DatasetBulkInsert datiToins=parseMisura(tenant, ooo.toJSONString());
				//
				//				//TODO .. controllo sul numero di record da inserire
				//				
				//				datiToins.setStream(streamToFind);
				//				datiToins.setSensor(sensor);
				//				datiToins.setStatus(DatasetBulkInsert.STATUS_SYNTAX_CHECKED);
				//				System.out.println("           "+datiToins.getRowsToInsert().size()+"/"+ datiToins.getNumRowToInsFromJson());
				//				
				//				ret.put(sensor+"_"+streamToFind, datiToins);


				//				i++;
			} catch (PathNotFoundException e) {
				System.out.println(" **** parseJsonInputDataset -- PathNotFoundException "+e);
				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException) endArray=true;
				else throw e;
			} catch (Exception ex) {
				System.out.println(" **** parseJsonInputDataset -- ERROR "+ex);
				i++;
				endArray=true;
				ex.printStackTrace();
				throw ex;
			} finally {
				System.out.println(" **** parseJsonInputDataset -- END ");
				
			}
		}
		return ret;

	}	

	public HashMap<String, DatasetBulkInsert> parseJsonInputStream(String tenant, String jsonInput) throws Exception {
		int i =0;
		boolean endArray=false;
		JSONObject ooo=null;
		HashMap<String, DatasetBulkInsert> ret= new HashMap<String, DatasetBulkInsert>();
		while (i<100000 && !endArray) {
			try {
				ooo = JsonPath.read(jsonInput, "$["+i+"]");
				if (null==ooo) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DATA_NOTARRAY);
				String sensor=(String)ooo.get("sensor");
				String application=(String)ooo.get("application");
				String stream=(String)ooo.get("stream");



				String streamToFind=(stream!=null ? stream: application);

				//TODO non so se e' bloccante ... 
				if (streamToFind == null) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_STREAM_MANCANTE);
				if (sensor == null ) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE);



				if (ret.get(sensor+"_"+streamToFind)!=null) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DUPLICATE, " for stream "+streamToFind);

				System.out.println(ooo);

				DatasetBulkInsert datiToins=parseMisura(tenant, ooo.toJSONString());

				//TODO .. controllo sul numero di record da inserire

				datiToins.setStream(streamToFind);
				datiToins.setSensor(sensor);
				datiToins.setStatus(DatasetBulkInsert.STATUS_SYNTAX_CHECKED);
				System.out.println("           "+datiToins.getRowsToInsert().size()+"/"+ datiToins.getNumRowToInsFromJson());

				ret.put(sensor+"_"+streamToFind, datiToins);


				i++;
			} catch (PathNotFoundException e) {
				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException) endArray=true;
				else throw e;
			}		
		}
		return ret;

	}	


	private DatasetBulkInsert parseGenericDataset(String tenant, String jsonInput,String insStrConst,MongoDatasetInfo datasetMongoInfo) throws Exception {
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


		while (i<100000 && !endArray) {
			try {
				components = JsonPath.read(jsonInput, "$.values["+i+"]");
				rigadains.add(parseComponents(components, insStrConst, elencoCampi));

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
		ret.setDatasetVersion(datasetMongoInfo.getDatasetId());
		ret.setIdDataset(datasetMongoInfo.getDatasetVersion());
		ret.setNumRowToInsFromJson(i);
		ret.setRowsToInsert(rigadains);
		return ret;
	}
	private String parseComponents (JSONObject components,String insStrConst,ArrayList<FieldsMongoDto> elencoCampi) throws Exception {
		ArrayList<String> rigadains= new ArrayList<String>();
		Iterator<String> itCampiJson=components.keySet().iterator();


		String currRigaIns=null;
		HashMap<String, FieldsMongoDto> campiMongo= new HashMap<String, FieldsMongoDto>();


		for (int i = 0; i< elencoCampi.size();i++) {
			campiMongo.put(elencoCampi.get(i).getFieldName(), elencoCampi.get(i));
		}

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
			System.out.println(currRigaIns);
			//rigadains.add(currRigaIns);

		} else {
			System.out.println(       "OKKKKIOOOO riga vuota ???? ");

		}

		return currRigaIns;


	}
	private DatasetBulkInsert parseMisura(String tenant, String jsonInput) throws Exception {
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
				rigadains.add(parseComponents(components, insStrConst, elencoCampi));

				System.out.println("--------------------------------");


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
		System.out.println("global request status setted --> "+startCopy);

		ArrayList<DatasetBulkInsert> datiRibalta= mongoAccess.getElencoRichiesteByGlobRequestId(globlalRequestId);
		if (null==datiRibalta || datiRibalta.size()<=0)  throw new Exception("nessun blocco da elaborare per la richiesta "+globlalRequestId);

		System.out.println("start Copy elaboration number of block to process --> "+datiRibalta.size());


		boolean allOk=true;
		for (int i = 0; i<datiRibalta.size();i++) {

			boolean curblockOk=true;
			System.out.println("**** Start Elaboration block: "+i);
			System.out.println("               datasetId:"+datiRibalta.get(i).getIdDataset());
			System.out.println("               datasetVersion:"+datiRibalta.get(i).getDatasetVersion());
			System.out.println("               requestId:"+datiRibalta.get(i).getRequestId());
			System.out.println("               numberofDocs:"+datiRibalta.get(i).getNumRowToInsFromJson());
			System.out.println("               startstatus:"+datiRibalta.get(i).getStatus());
			if (!DatasetBulkInsert.STATUS_END_INS.equals(datiRibalta.get(i).getStatus())) {
				System.out.println("               ERROR:incorrect status");
				allOk=false;
			} else {
				curblockOk= copyBlock(datiRibalta.get(i),globlalRequestId,tenantCode);
			}
			System.out.println("end copy block --> " +curblockOk);

			if(!curblockOk) allOk=false;

		}


		boolean fineOp=false;
		if (allOk)
			fineOp= mongoAccess.updateGlobalRequestStatus(globlalRequestId, DatasetBulkInsert.STATUS_END_COPY,DatasetBulkInsert.STATUS_START_COPY) ;
		else 
			fineOp= mongoAccess.updateGlobalRequestStatus(globlalRequestId, DatasetBulkInsert.STATUS_KO_COPY,DatasetBulkInsert.STATUS_START_COPY) ;



	}
	private static boolean copyBlock(DatasetBulkInsert blockInfo,String globlalRequestId,String tenantCode) throws Exception {
		SDPInsertApiMongoDataAccess mongoAccess=new SDPInsertApiMongoDataAccess();
		//TODO okkio... .ragioniamo solo 
		
		MongoDatasetInfo infoDataset = null;
		
		try {
			infoDataset=mongoAccess.getInfoDataset(blockInfo.getDatasetCode(), blockInfo.getDatasetVersion());
		} catch (Exception e ) {
			
		}
		
		DbConfDto dbConfgi= null;
		if (null != infoDataset && "bulkDataset".equals(infoDataset.getDatasetSubType())) {
			dbConfgi= SDPInsertApiMongoConnectionSingleton.getInstance().getDataDbConfiguration(SDPInsertApiMongoConnectionSingleton.DB_DATA, tenantCode);	
			
		} else {
			dbConfgi= SDPInsertApiMongoConnectionSingleton.getInstance().getDataDbConfiguration(SDPInsertApiMongoConnectionSingleton.DB_MESURES, tenantCode);	
		}

		
		System.out.println("COPY DBTARGET-->"+ dbConfgi.getDataBase());
		System.out.println("COPY COLLECTION-->"+ dbConfgi.getCollection());
		
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
			e.printStackTrace();
			return false;
		}
	}	

}
