package org.csi.yucca.datainsert.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import net.minidev.json.JSONObject;

import org.csi.yucca.datainsert.dto.DatasetBulkInsert;
import org.csi.yucca.datainsert.dto.FieldsMongoDto;
import org.csi.yucca.datainsert.dto.MongoStreamInfo;
import org.csi.yucca.datainsert.exception.InsertApiBaseException;
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
	
	
	public HashMap<String, DatasetBulkInsert> parseJsonInput(String tenant, String jsonInput) throws Exception {
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

				
				//TODO non so se e' bloccante ... 
				if (application == null && stream == null) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_STREAM_MANCANTE);
				if (sensor == null ) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE);

				String streamToFind=(stream!=null ? stream: application);

				
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
			}		
		}
		return ret;
		
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
				Iterator<String> itCampiJson=components.keySet().iterator();
				
				
				String currRigaIns=null;
				
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

					
//					if (currRigaIns==null) currRigaIns="{" + campoMongo.getInsertJson(valore);
//					else currRigaIns+=" , "+  campoMongo.getInsertJson(valore);
					if (currRigaIns==null) currRigaIns= campoMongo.getInsertJson(valore);
					else currRigaIns+=" , "+  campoMongo.getInsertJson(valore);

				}
				if (currRigaIns!=null) {
					//currRigaIns+="}";
					//currRigaIns = insStrConst+", time: ISODate(\""+timeStamp+"\"), "+currRigaIns;
					currRigaIns = insStrConst+", time: {$date :\""+timeStamp+"\"} , "+currRigaIns;
							System.out.println(currRigaIns);
					rigadains.add(currRigaIns);
					
				} else {
					System.out.println(       "OKKKKIOOOO riga vuota ???? ");
					
				}
				
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
	
}
