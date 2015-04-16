package org.csi.yucca.datainsert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.csi.yucca.datainsert.business.InsertApiLogic;
import org.csi.yucca.datainsert.dto.DatasetBulkInsert;
import org.csi.yucca.datainsert.dto.DatasetBulkInsertIOperationReport;
import org.csi.yucca.datainsert.dto.DatasetBulkInsertOutput;
import org.csi.yucca.datainsert.exception.InsertApiBaseException;

public class InsertApi {

	public DatasetBulkInsertOutput insertApiDataset(String codTenant, String jsonData) throws Exception{
		 DatasetBulkInsertOutput outData=new DatasetBulkInsertOutput();
		 InsertApiLogic insApiLogic=new InsertApiLogic();
		try {
			
			
			 HashMap<String, DatasetBulkInsert>aaaaa = insApiLogic.parseJsonInputDataset(codTenant,jsonData);
			outData=inserimentoGeneralizzato(codTenant, aaaaa);

			
			
		} catch (InsertApiBaseException insEx) {
			System.out.print("-------------insertApiException  name--> " + insEx.getErrorName());
			System.out.print("-------------insertApiException  code--> " + insEx.getErrorCode());
			
			
			
			outData.setInsertException((InsertApiBaseException)insEx);
		} catch (Exception e) {
			InsertApiBaseException newEx=new InsertApiBaseException("UNKNOWN");
			outData.setInsertException(newEx);
		} finally {
			
		}
		 
		return outData;
		
	}

	
	private DatasetBulkInsertOutput inserimentoGeneralizzato(String codTenant,HashMap<String, DatasetBulkInsert>datiDains) throws Exception{
		 DatasetBulkInsertOutput outData=new DatasetBulkInsertOutput();
			
		try {
			
		System.out.print("-------------------------- dentro");
		
		InsertApiLogic insApiLogic=new InsertApiLogic();
		 HashMap<String, DatasetBulkInsert> retHm = insApiLogic.insertManager(codTenant,datiDains);	
		 ArrayList<DatasetBulkInsertIOperationReport> ret = new ArrayList<DatasetBulkInsertIOperationReport>();
		 Iterator<String> it=retHm.keySet().iterator();
		 DatasetBulkInsertIOperationReport retElement=null;
		 String idRichieste=null;
		 while (it.hasNext()) {
			 String key = it.next();
			 retElement=new DatasetBulkInsertIOperationReport();
			 retElement.setDatasetVersion(retHm.get(key).getDatasetVersion());
			 retElement.setIdDataset(retHm.get(key).getIdDataset());

			 //TODO forse non ha senso, commentato
			 //retElement.setNumRowInserted(retHm.get(key).getNumRowToInsFromJson());
			 retElement.setNumRowToInsFromJson(retHm.get(key).getNumRowToInsFromJson());
			 retElement.setRequestId(retHm.get(key).getRequestId());
			 retElement.setSensor(retHm.get(key).getSensor());
			 retElement.setStream(retHm.get(key).getStream());
			 
			 //TODO
			 retElement.setStatus(retHm.get(key).getStatus());
			 
			 //TODO serve?
			 retElement.setTimestamp(retHm.get(key).getTimestamp());
			 idRichieste=retHm.get(key).getGlobalReqId();
			 ret.add(retElement);
		 }
		 
		 outData.setDataBLockreport(ret);
		 outData.setGlobalRequestId(idRichieste);
		 
		} catch (InsertApiBaseException insEx) {
			System.out.print("-------------insertApiException  name--> " + insEx.getErrorName());
			System.out.print("-------------insertApiException  code--> " + insEx.getErrorCode());
			
			
			
			outData.setInsertException((InsertApiBaseException)insEx);
		} catch (Exception e) {
			InsertApiBaseException newEx=new InsertApiBaseException("UNKNOWN");
			outData.setInsertException(newEx);
		}
		 
		return outData;
	}
	
	public DatasetBulkInsertOutput insertApi(String codTenant, String jsonData) throws Exception{
		 DatasetBulkInsertOutput outData=new DatasetBulkInsertOutput();
		
		try {
			
		System.out.print("-------------------------- dentro");
		
		InsertApiLogic insApiLogic=new InsertApiLogic();
		
		
		
		HashMap<String, DatasetBulkInsert>aaaaa = insApiLogic.parseJsonInputStream(codTenant,jsonData);
		
		outData=inserimentoGeneralizzato(codTenant, aaaaa);
		
//		 HashMap<String, DatasetBulkInsert> retHm = insApiLogic.insertManager(codTenant,aaaaa);	
//		 ArrayList<DatasetBulkInsertIOperationReport> ret = new ArrayList<DatasetBulkInsertIOperationReport>();
//		 Iterator<String> it=retHm.keySet().iterator();
//		 DatasetBulkInsertIOperationReport retElement=null;
//		 String idRichieste=null;
//		 while (it.hasNext()) {
//			 String key = it.next();
//			 retElement=new DatasetBulkInsertIOperationReport();
//			 retElement.setDatasetVersion(retHm.get(key).getDatasetVersion());
//			 retElement.setIdDataset(retHm.get(key).getIdDataset());
//
//			 //TODO forse non ha senso, commentato
//			 //retElement.setNumRowInserted(retHm.get(key).getNumRowToInsFromJson());
//			 retElement.setNumRowToInsFromJson(retHm.get(key).getNumRowToInsFromJson());
//			 retElement.setRequestId(retHm.get(key).getRequestId());
//			 retElement.setSensor(retHm.get(key).getSensor());
//			 retElement.setStream(retHm.get(key).getStream());
//			 
//			 //TODO
//			 retElement.setStatus(retHm.get(key).getStatus());
//			 
//			 //TODO serve?
//			 retElement.setTimestamp(retHm.get(key).getTimestamp());
//			 idRichieste=retHm.get(key).getGlobalReqId();
//			 ret.add(retElement);
//		 }
//		 
//		 outData.setDataBLockreport(ret);
//		 outData.setGlobalRequestId(idRichieste);
		 
		} catch (InsertApiBaseException insEx) {
			System.out.print("-------------insertApiException  name--> " + insEx.getErrorName());
			System.out.print("-------------insertApiException  code--> " + insEx.getErrorCode());
			
			
			
			outData.setInsertException((InsertApiBaseException)insEx);
		} catch (Exception e) {
			InsertApiBaseException newEx=new InsertApiBaseException("UNKNOWN");
			outData.setInsertException(newEx);
		}
		 
		return outData;
	}

	
	public void copyData (String codTenant, String globalIdRequest) throws Exception{
		System.out.print("-------------------------- startCopy");
		try {
		    Thread.sleep(10000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		System.out.print("-------------------------- endSleep");
		
		InsertApiLogic insApiLogic=new InsertApiLogic();
		insApiLogic.copyData(codTenant, globalIdRequest);
		System.out.print("-------------------------- endCopy");
		
		
	}
	
}