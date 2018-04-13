package org.csi.yucca.dataservice.binaryapi.adminapi;

import java.util.List;

import org.csi.yucca.adminapi.client.db.BackofficeDettaglioClientDB;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.TenantResponse;

public class SDPAdminApiAccess  {
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("org.csi.yucca.binaryapi");
	
	public static BackofficeDettaglioApiResponse getInfoDatasetByApi(String apiCode) throws Exception {
		log.info("[SDPAdminApiAccess::getInfoDatasetByApi] BEGIN apiCode: " + apiCode );
		BackofficeDettaglioApiResponse dettaglio = 
//				BackofficeDettaglioClient.getBackofficeDettaglioApi(BinaryConfig.getInstance().getApiAdminServicesUrl(), apiCode, log.getName());
				BackofficeDettaglioClientDB.getBackofficeDettaglioApi(apiCode, log.getName());
		log.info("[SDPAdminApiAccess::getInfoDatasetByApi] END");
		return dettaglio;
	}

	public static BackofficeDettaglioStreamDatasetResponse getInfoDatasetByIdDatasetDatasetVersion(Integer idDataset, Integer datasetVersion) throws Exception {
		log.info("[SDPAdminApiAccess::getInfoDatasetByIdDatasetDatasetVersion] BEGIN idDataset, datasetVersion: " + idDataset +","+datasetVersion);
		BackofficeDettaglioStreamDatasetResponse dettaglio = 
//				BackofficeDettaglioClient.getBackofficeDettaglioStreamDatasetByIdDatasetDatasetVersion(
//						BinaryConfig.getInstance().getApiAdminServicesUrl(), idDataset, datasetVersion, log.getName());
				BackofficeDettaglioClientDB.getBackofficeDettaglioStreamDatasetByIdDatasetDatasetVersion(
						idDataset, datasetVersion, log.getName());
		log.info("[SDPAdminApiAccess::getInfoDatasetByIdDatasetDatasetVersion] END");
		return dettaglio;
	}
	
	public static BackofficeDettaglioStreamDatasetResponse getInfoDatasetByDatasetCodeDatasetVersion(String datasetCode, Integer datasetVersion) throws Exception {
		log.info("[SDPAdminApiAccess::getInfoDatasetByDatasetCodeDatasetVersion] BEGIN datasetCode, datasetVersion: " + datasetCode +","+datasetVersion);
		
		BackofficeDettaglioStreamDatasetResponse dettaglio = 
//				BackofficeDettaglioClient.getBackofficeDettaglioStreamDatasetByDatasetCodeDatasetVersion(
//						BinaryConfig.getInstance().getApiAdminServicesUrl(), datasetCode, datasetVersion, log.getName());
				BackofficeDettaglioClientDB.getBackofficeDettaglioStreamDatasetByDatasetCodeDatasetVersion(
						datasetCode, datasetVersion, log.getName());
		log.info("[SDPAdminApiAccess::getInfoDatasetByIdDatasetDatasetVersion] END");
		
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
