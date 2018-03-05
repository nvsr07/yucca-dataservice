package org.csi.yucca.adminapi.client;

import org.csi.yucca.adminapi.response.AllineamentoResponse;

public class BackOfficeCreateClient {

	public static AllineamentoResponse createOrUpdateAllineamento(String adminApiBaseUrl, String json, String logger) throws AdminApiClientException {
		return AdminApiClientDelegate.postFromAdminApi(AllineamentoResponse.class, adminApiBaseUrl + "/allineamento", logger, json);
	}

	
//	TEST
//	public static void main(String [] args){
//		try {
//			String json = "{\"idOrganization\":17,\"locked\":0,\"lastobjectid\":\"rrrr\"}";
//			AllineamentoResponse allineamentoResponse = createOrUpdateAllineamento("http://localhost:8080/adminapi/1/backoffice", json, "");
//		} 
//		catch (Exception e) {
//			String stop = "";
//			stop = "";
//		}
//	}
	
}