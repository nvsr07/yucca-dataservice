package org.csi.yucca.adminapi.client.db;

import java.util.List;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.client.AdminApiClientException;
import org.csi.yucca.adminapi.response.AllineamentoScaricoDatasetResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.OrganizationResponse;
import org.csi.yucca.adminapi.response.TenantManagementResponse;

public class BackofficeListaClientDB {

	
	@SuppressWarnings("unchecked")
	public static List<TenantManagementResponse> getTenants(String logger)
			throws AdminApiClientException {
		try {
			
			return (List<TenantManagementResponse>)AdminDBClientDelegate.getInstance().getTenantService().selectTenants(null).getObject();
		} 
		catch (Exception e) {
			
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);
			
			throw new AdminApiClientException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static List<BackofficeDettaglioStreamDatasetResponse> getListStreamDataset(
			String organizationCode, String logger) throws AdminApiClientException {

		try {
			return (List<BackofficeDettaglioStreamDatasetResponse>)
					AdminDBClientDelegate.getInstance().getDatasetService().selectDatasetByOrganizationCode(organizationCode).getObject();
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);

			throw new AdminApiClientException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static List<OrganizationResponse> getOrganizations(String logger)
			throws AdminApiClientException {
		
		try {
			
			return (List<OrganizationResponse>)
					AdminDBClientDelegate.getInstance().getClassificationService().selectOrganization().getObject();
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);

			throw new AdminApiClientException(e);
		}		
		
	}
	
	@SuppressWarnings("unchecked")
	public static List<AllineamentoScaricoDatasetResponse> getAllineamentoByIdOrganization(
			Integer idOrganization, String logger) throws AdminApiClientException {
		try {
			return (List<AllineamentoScaricoDatasetResponse>)
					AdminDBClientDelegate.getInstance().getDatasetService().selectAllineamentoScaricoDataset(idOrganization).getObject();
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);

			throw new AdminApiClientException(e);
		}
	}

}