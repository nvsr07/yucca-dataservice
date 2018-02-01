package org.csi.yucca.dataservice.insertdataapi.adminapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.csi.yucca.adminapi.client.AdminApiClientException;
import org.csi.yucca.adminapi.client.BackofficeDettaglioClient;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.TenantResponse;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiRuntimeException;
import org.csi.yucca.dataservice.insertdataapi.exception.MongoAccessException;
import org.csi.yucca.dataservice.insertdataapi.metadata.SDPInsertMetadataApiAccess;
import org.csi.yucca.dataservice.insertdataapi.model.output.CollectionConfDto;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetInfo;
import org.csi.yucca.dataservice.insertdataapi.model.output.FieldsDto;
import org.csi.yucca.dataservice.insertdataapi.model.output.StreamInfo;
import org.csi.yucca.dataservice.insertdataapi.model.output.TenantInfo;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class SDPAdminApiAccess implements SDPInsertMetadataApiAccess {
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("org.csi.yucca.datainsert");

	@Override
	public DatasetInfo getInfoDataset(String datasetCode, long datasetVersion,
			String codiceTenant) throws Exception {
		BackofficeDettaglioStreamDatasetResponse dettaglio = getBackofficeDettaglioForDatasetCodeDatasetVersion(
				datasetCode, datasetVersion);
		dettaglio = checkTenantCanSendData(dettaglio, codiceTenant);
		return SDPAdminApiConverter.convertBackofficeDettaglioStreamDatasetResponseToDatasetInfo(dettaglio);
	}

	@Override
	public DatasetInfo getInfoDataset(Long idDataset, Long datasetVersion,
			String codiceTenant) throws Exception {
		BackofficeDettaglioStreamDatasetResponse dettaglio = getBackofficeDettaglioForIdDatasetDatasetVersion(
				idDataset, datasetVersion);
		
		dettaglio = checkTenantCanSendData(dettaglio, codiceTenant);
		
		return SDPAdminApiConverter.convertBackofficeDettaglioStreamDatasetResponseToDatasetInfo(dettaglio);
	}

	private BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioForIdDatasetDatasetVersion(
			Long idDataset, Long datasetVersion) throws AdminApiClientException {
		BackofficeDettaglioStreamDatasetResponse dettaglio =  null;
		if (datasetVersion == null || datasetVersion == -1) {
			dettaglio = BackofficeDettaglioClient.getBackofficeDettaglioStreamDatasetByIdDataset(
					SDPInsertApiConfig.getInstance().getAdminApiUrl(),
					idDataset.intValue(), log.getName());
		}
		else {
			dettaglio = BackofficeDettaglioClient.getBackofficeDettaglioStreamDatasetByIdDatasetDatasetVersion(
					SDPInsertApiConfig.getInstance().getAdminApiUrl(),
					idDataset.intValue(), datasetVersion.intValue(), log.getName());
		}
		return dettaglio;
	}
	private BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioForDatasetCodeDatasetVersion(
			String datasetCode, Long datasetVersion) throws AdminApiClientException {
		BackofficeDettaglioStreamDatasetResponse dettaglio =  null;
		if (datasetVersion == null || datasetVersion == -1) {
			dettaglio = BackofficeDettaglioClient.getBackofficeDettaglioStreamDatasetByDatasetCode(
					SDPInsertApiConfig.getInstance().getAdminApiUrl(),
					datasetCode, log.getName());
		}
		else {
			dettaglio = BackofficeDettaglioClient.getBackofficeDettaglioStreamDatasetByDatasetCodeDatasetVersion(
					SDPInsertApiConfig.getInstance().getAdminApiUrl(),
					datasetCode, datasetVersion.intValue(), log.getName());
		}
		return dettaglio;
	}
	
	@Override
	public ArrayList<FieldsDto> getCampiDataSet(Long idDataset,
			long datasetVersion) throws Exception {
		BackofficeDettaglioStreamDatasetResponse dettaglio = getBackofficeDettaglioForIdDatasetDatasetVersion(
				idDataset, datasetVersion);
		return SDPAdminApiConverter.convertComponentToFields(dettaglio.getComponents(), 
				dettaglio.getDataset().getIddataset(), dettaglio.getVersion());
	}

	@Override
	public Set<String> getTenantList() throws MongoAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<StreamInfo> getStreamInfo(String tenant, String streamApplication, String sensor) {
		 ArrayList<StreamInfo> infos = new ArrayList<>();
		
		try {
			BackofficeDettaglioStreamDatasetResponse dettaglio = BackofficeDettaglioClient
					.getBackofficeDettaglioStreamDatasetBySoCodeStreamCode(SDPInsertApiConfig.getInstance().getAdminApiUrl(),
							sensor, streamApplication, log.getName());
			
			dettaglio = checkTenantCanSendData(dettaglio, tenant);
			
			StreamInfo streamInfo = SDPAdminApiConverter.convertBackofficeDettaglioStreamDatasetResponseToStreamInfo(dettaglio);
			if (streamInfo!=null)
				infos.add(streamInfo);
			
		} catch (Exception e) {
			log.error("Error", e);
			throw new InsertApiRuntimeException(e);
		}
		
		return infos;
	}

	public StreamInfo getStreamInfoForDataset(String tenant, long idDataset, long datasetVersion) {
		StreamInfo streamInfo = null;
		try {
			BackofficeDettaglioStreamDatasetResponse dettaglio = getBackofficeDettaglioForIdDatasetDatasetVersion(
					idDataset, datasetVersion);
			
			dettaglio = checkTenantCanSendData(dettaglio, tenant);
			
			streamInfo = SDPAdminApiConverter.convertBackofficeDettaglioStreamDatasetResponseToStreamInfo(dettaglio);
			
		} catch (Exception e) {
			log.error("Error", e);
			throw new InsertApiRuntimeException(e);
		}
		
		return streamInfo;
	}


	@Override
	public CollectionConfDto getCollectionInfo(String tenant,
			long idDataset, long datasetVersion,
			String datasetType) {
		BackofficeDettaglioStreamDatasetResponse dettaglio;
		try {
			dettaglio = getBackofficeDettaglioForIdDatasetDatasetVersion(
					idDataset, datasetVersion);
		} catch (AdminApiClientException e) {
			log.error("Error", e);
			throw new InsertApiRuntimeException(e);
		}
		
		dettaglio = checkTenantCanSendData(dettaglio, tenant);
		return SDPAdminApiConverter.convertBackofficeDettaglioStreamDatasetResponseToCollectionConfDto(dettaglio);
	}

	
	private BackofficeDettaglioStreamDatasetResponse checkTenantCanSendData(
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

}
