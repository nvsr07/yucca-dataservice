package org.csi.yucca.adminapi.client.db;

import java.util.List;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.client.AdminApiClientException;
import org.csi.yucca.adminapi.response.AllineamentoScaricoDatasetResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.OrganizationResponse;
import org.csi.yucca.adminapi.response.TenantManagementResponse;

public class BackofficeListaClientDB {
	
	
	/**
	 * 
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static List<TenantManagementResponse> getTenants(String logger) throws AdminApiClientException {
		
		Logger log = Logger.getLogger(logger + ".BackofficeListaClientDB");
		
		log.info("==> BEGIN");
		
		try {
			return CacheUtilDB.getTenants(new KeyCacheDB());
		} 
		catch (Exception e) {
			log.error("Exception ===>>>> ", e);
			throw new AdminApiClientException(e);
		}
	}

	/**
	 * 
	 * @param organizationCode
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static List<BackofficeDettaglioStreamDatasetResponse> getListStreamDataset(
			String organizationCode, String logger) throws AdminApiClientException {

		try {
			return CacheUtilDB.getListStreamDataset(new KeyCacheDB().organizationCode(organizationCode));
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);

			throw new AdminApiClientException(e);
		}
	}
	
	/**
	 * 
	 * @param idOrganization
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static List<AllineamentoScaricoDatasetResponse> getAllineamentoByIdOrganization(
			Integer idOrganization, String logger) throws AdminApiClientException {
		try {
			return CacheUtilDB.getAllineamento(new KeyCacheDB().idOrganization(idOrganization));
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);

			throw new AdminApiClientException(e);
		}
	}
	
	/**
	 * 
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static List<OrganizationResponse> getOrganizations(String logger)
			throws AdminApiClientException {
		
		try {
			return CacheUtilDB.getOrganizations(new KeyCacheDB());
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);

			throw new AdminApiClientException(e);
		}		
		
	}
	

}