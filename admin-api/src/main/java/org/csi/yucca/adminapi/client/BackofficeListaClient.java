package org.csi.yucca.adminapi.client;

import java.util.List;

import org.csi.yucca.adminapi.client.cache.CacheUtil;
import org.csi.yucca.adminapi.client.cache.key.KeyCache;
import org.csi.yucca.adminapi.response.TenantManagementResponse;

public class BackofficeListaClient {
	
	public static List<TenantManagementResponse> getTenants(
			String adminApiBaseUrl, String logger) throws AdminApiClientException {
		return CacheUtil.getTenants(new KeyCache(adminApiBaseUrl, logger));
	}
	
}
