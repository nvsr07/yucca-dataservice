package org.csi.yucca.adminapi.client.db;

import org.csi.yucca.adminapi.client.AdminApiClientException;
import org.csi.yucca.adminapi.request.AllineamentoScaricoDatasetRequest;
import org.csi.yucca.adminapi.response.AllineamentoScaricoDatasetResponse;
import org.csi.yucca.adminapi.util.Util;

public class BackOfficeCreateClientDB {
	
	public static AllineamentoScaricoDatasetResponse createOrUpdateAllineamento(
			String adminApiBaseUrl, String json, Integer idOrganization, String logger) throws AdminApiClientException {
		
		AllineamentoScaricoDatasetResponse response = null;
		
		try {

			AllineamentoScaricoDatasetRequest request = Util.getFromJsonString(json, AllineamentoScaricoDatasetRequest.class);
			
			response =  (AllineamentoScaricoDatasetResponse) 
					AdminDBClientDelegate.getInstance().getDatasetService().insertLastMongoObjectId(request, idOrganization).getObject();
			
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
		
		return response;
	}
	
}