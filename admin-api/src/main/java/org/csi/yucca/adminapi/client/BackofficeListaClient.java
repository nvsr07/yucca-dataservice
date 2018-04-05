package org.csi.yucca.adminapi.client;

import java.util.List;

import org.csi.yucca.adminapi.client.cache.CacheUtil2k;
import org.csi.yucca.adminapi.client.cache.key.KeyCache;
import org.csi.yucca.adminapi.response.AllineamentoScaricoDatasetResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.OrganizationResponse;
import org.csi.yucca.adminapi.response.TenantManagementResponse;

import com.google.common.cache.CacheLoader.InvalidCacheLoadException;

public class BackofficeListaClient {

	public static List<TenantManagementResponse> getTenants(String adminApiBaseUrl, String logger)
			throws AdminApiClientException {
		try {
			return CacheUtil2k.getTenants(new KeyCache(adminApiBaseUrl, logger));	
		} 
		catch (InvalidCacheLoadException exception) {
			return null;
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
		
	}

	public static List<BackofficeDettaglioStreamDatasetResponse> getListStreamDataset(String adminApiBaseUrl,
			String organizationCode, String logger) throws AdminApiClientException {
		try {
			return CacheUtil2k.getListStreamDataset(new KeyCache(adminApiBaseUrl, logger).code(organizationCode));	
		} 
		catch (InvalidCacheLoadException exception) {
			return null;
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
		
	}

	public static List<OrganizationResponse> getOrganizations(String adminApiBaseUrl, String logger)
			throws AdminApiClientException {
		
		try {
			return CacheUtil2k.getOrganizations(new KeyCache(adminApiBaseUrl, logger));	
		} 
		catch (InvalidCacheLoadException exception) {
			return null;
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}		
		
	}

	public static List<AllineamentoScaricoDatasetResponse> getAllineamentoByIdOrganization(String adminApiBaseUrl,
			Integer idOrganization, String logger) throws AdminApiClientException {
		try {
			return CacheUtil2k.getAllineamento(new KeyCache(adminApiBaseUrl, logger).id(idOrganization));
		} 
		catch (InvalidCacheLoadException exception) {
			return null;
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}

}