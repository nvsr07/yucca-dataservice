package org.csi.yucca.adminapi.client;

import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;

public class BackofficeDettaglioApiClient {

	
	public static  BackofficeDettaglioApiResponse getBackofficeDettaglioApi(String codapi) throws AdminApiClientException
	{
		return AdminApiClientDelegate.getFromAdminApi("1/backoffice/api/"+codapi, BackofficeDettaglioApiResponse.class, null);
	}
}
