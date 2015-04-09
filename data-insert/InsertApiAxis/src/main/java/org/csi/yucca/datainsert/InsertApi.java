package org.csi.yucca.datainsert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.csi.yucca.datainsert.business.InsertApiLogic;
import org.csi.yucca.datainsert.dto.DatasetBulkInsert;
import org.csi.yucca.datainsert.dto.DatasetBulkInsertIOperationReport;

public class InsertApi {

	
	public List<DatasetBulkInsertIOperationReport> insertApi(String codTenant, String jsonData) throws Exception{
		InsertApiLogic insApiLogic=new InsertApiLogic();
		
		HashMap<String, DatasetBulkInsert>aaaaa = insApiLogic.parseJsonInput(codTenant,jsonData);
		 HashMap<String, DatasetBulkInsert> retHm = insApiLogic.insertManager("smartlab",aaaaa);	
		 ArrayList<DatasetBulkInsertIOperationReport> ret = new ArrayList<DatasetBulkInsertIOperationReport>();
		 Iterator<String> it=retHm.keySet().iterator();
		 DatasetBulkInsertIOperationReport retElement=null;
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
			 
			 ret.add(retElement);
		 }
		return ret;
	}

	
	public boolean copyData (String codTenant, String globalIdRequest) throws Exception{
		
		
		InsertApiLogic insApiLogic=new InsertApiLogic();
		
		
		return true;
	}
	
}