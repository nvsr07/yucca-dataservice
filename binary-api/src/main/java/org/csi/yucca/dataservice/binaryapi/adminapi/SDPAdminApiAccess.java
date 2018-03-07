package org.csi.yucca.dataservice.binaryapi.adminapi;

import java.util.List;

import org.csi.yucca.adminapi.client.BackofficeDettaglioClient;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.TenantResponse;
import org.csi.yucca.dataservice.binaryapi.util.BinaryConfig;

public class SDPAdminApiAccess  {
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("org.csi.yucca.datainsert");

	
	
	
	
	public static BackofficeDettaglioApiResponse getInfoDatasetByApi(String apiCode) throws Exception {
		BackofficeDettaglioApiResponse dettaglio = 
				BackofficeDettaglioClient.getBackofficeDettaglioApi(
						BinaryConfig.getInstance().getApiAdminServicesUrl(), apiCode, log.getName());
		return dettaglio;
	}

	public static BackofficeDettaglioStreamDatasetResponse getInfoDatasetByIdDatasetDatasetVersion(Integer idDataset, Integer datasetVersion) throws Exception {
		BackofficeDettaglioStreamDatasetResponse dettaglio = 
				BackofficeDettaglioClient.getBackofficeDettaglioStreamDatasetByIdDatasetDatasetVersion(
						BinaryConfig.getInstance().getApiAdminServicesUrl(), idDataset, datasetVersion, log.getName());
		return dettaglio;
	}
	
	public static BackofficeDettaglioStreamDatasetResponse getInfoDatasetByDatasetCodeDatasetVersion(String datasetCode, Integer datasetVersion) throws Exception {
		BackofficeDettaglioStreamDatasetResponse dettaglio = 
				BackofficeDettaglioClient.getBackofficeDettaglioStreamDatasetByDatasetCodeDatasetVersion(
						BinaryConfig.getInstance().getApiAdminServicesUrl(), datasetCode, datasetVersion, log.getName());
		return dettaglio;
	}
	
	public static BackofficeDettaglioStreamDatasetResponse checkTenantCanSendData(
			BackofficeDettaglioStreamDatasetResponse dettaglio, String tenantCode) {
		if (dettaglio!=null)
		{
			if (!dettaglio.getTenantManager().getTenantcode().equals(tenantCode)) // TODO .. rimuovere controllo tenant manager... lo sharing dovrebbe essere sempre a posto
			{
				List<TenantResponse> tenants = dettaglio.getSharingTenanst();
				boolean senderFound = false;
				if (tenants!=null)
				{
					for (TenantResponse tenantResponse : tenants) {
						if (tenantResponse.getTenantcode().equals(tenantCode))
						{
							if (tenantResponse.getDataoptions()>=3)
							{
								senderFound = true;
								break;
							}
						}
					}
				}
				if (!senderFound)
					return null;
			}
		}
		return dettaglio;
	}

	public static BackofficeDettaglioStreamDatasetResponse checkIsInstalled(
			BackofficeDettaglioStreamDatasetResponse dettaglio) {
		if (dettaglio!=null)
		{
			if (dettaglio.getStatus().getIdStatus() != 2)
				return null;
		}
		return dettaglio;
	}

}
