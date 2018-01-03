package org.csi.yucca.adminapi.client;

import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;

public class BackofficeDettaglioApiClient {

	
	public static  BackofficeDettaglioApiResponse getBackofficeDettaglioApi(String adminApiBaseUrl, String codapi, String logger) throws AdminApiClientException
	{
		return AdminApiClientDelegate.getFromAdminApi(adminApiBaseUrl+"/1/backoffice/api/"+codapi,BackofficeDettaglioApiResponse.class,logger, null);
	}
}
