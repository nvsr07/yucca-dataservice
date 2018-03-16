package org.csi.yucca.adminapi.client;

import org.csi.yucca.adminapi.response.AllineamentoScaricoDatasetResponse;

public class BackOfficeCreateClient {
	
	public static AllineamentoScaricoDatasetResponse createOrUpdateAllineamento(String adminApiBaseUrl, String json, Integer idOrganization, String logger) throws AdminApiClientException {
		return AdminApiClientDelegate.postFromAdminApi(AllineamentoScaricoDatasetResponse.class, adminApiBaseUrl + "/allineamento/idOrganization="+idOrganization, logger, json);
	}
	
//	TEST
//	public static void main(String [] args){
//		try {
//			
//			String json = "{\"idDataset\":56,\"datasetVersion\":8,\"lastMongoObjectId\":\"ciccio\"}";
//			AllineamentoScaricoDatasetResponse allineamentoResponse = createOrUpdateAllineamento("http://localhost:8080/adminapi/1/backoffice", json, 9, "");
//			String stop = "";
//			stop = "";
//			
//		} 
//		catch (Exception e) {
//			String stop = "";
//			stop = "";
//		}
//	}
	
}