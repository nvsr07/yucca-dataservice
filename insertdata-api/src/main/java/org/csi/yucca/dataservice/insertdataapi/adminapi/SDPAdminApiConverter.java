package org.csi.yucca.dataservice.insertdataapi.adminapi;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.ComponentResponse;
import org.csi.yucca.dataservice.insertdataapi.model.output.CollectionConfDto;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetInfo;
import org.csi.yucca.dataservice.insertdataapi.model.output.FieldsDto;
import org.csi.yucca.dataservice.insertdataapi.model.output.StreamInfo;

import com.mongodb.BasicDBObject;

public class SDPAdminApiConverter {

	public static DatasetInfo convertBackofficeDettaglioStreamDatasetResponseToDatasetInfo(
			BackofficeDettaglioStreamDatasetResponse dettaglio) throws Exception {
		DatasetInfo ret = null;

		if (dettaglio!=null && dettaglio.getDataset()!=null)
		{
			ret = new DatasetInfo();
			ret.setCampi(SDPAdminApiConverter.convertComponentToFields(dettaglio.getComponents(),dettaglio.getDataset().getIddataset(), dettaglio.getVersion() ));
			ret.setDatasetId(dettaglio.getDataset().getIddataset());
			ret.setDatasetVersion(dettaglio.getVersion());
			ret.setDatasetType(dettaglio.getDataset().getDatasetType().getDatasetType());
			ret.setDatasetSubType(dettaglio.getDataset().getDatasetSubtype().getDatasetSubtype());
			ret.setTenantcode(dettaglio.getTenantManager().getTenantcode());
			ret.setDatasetCode(dettaglio.getDataset().getDatasetcode());
			ret.setDatasetDomain(dettaglio.getDomain().getDomaincode());
			ret.setDatasetSubdomain(dettaglio.getSubdomain().getSubdomaincode());
			ret.setOrganizationCode(dettaglio.getOrganization().getOrganizationcode());
		}
		
		return ret;
	}

	public static ArrayList<FieldsDto> convertComponentToFields(
			List<ComponentResponse> components, Integer idDataset, Integer datasetVersion) throws Exception {
		ArrayList<FieldsDto> fields = null;
		
		if (components!=null && !components.isEmpty()) 
		{
			fields = new ArrayList<FieldsDto>();
			for (ComponentResponse componentResponse : components) {
				FieldsDto fieldsDto = new FieldsDto(
						componentResponse.getName(), 
						componentResponse.getDataType().getDatatypecode(), 
						idDataset, 
						datasetVersion);
				fields.add(fieldsDto);
			}
		}
		return fields;
	}

	public static StreamInfo convertBackofficeDettaglioStreamDatasetResponseToStreamInfo(
			BackofficeDettaglioStreamDatasetResponse dettaglio) throws Exception {
		StreamInfo ret = null;

		if (dettaglio!=null && dettaglio.getDataset()!=null)
		{
			ret = new StreamInfo();
			ret.setCampi(SDPAdminApiConverter.convertComponentToFields(dettaglio.getComponents(),dettaglio.getDataset().getIddataset(), dettaglio.getVersion() ));
			ret.setDatasetId(dettaglio.getDataset().getIddataset());
			ret.setDatasetVersion(dettaglio.getVersion());
			ret.setDatasetType(dettaglio.getDataset().getDatasetType().getDatasetType());
			ret.setDatasetSubType(dettaglio.getDataset().getDatasetSubtype().getDatasetSubtype());
			ret.setTenantcode(dettaglio.getTenantManager().getTenantcode());
			ret.setDatasetCode(dettaglio.getDataset().getDatasetcode());
			ret.setDatasetDomain(dettaglio.getDomain().getDomaincode());
			ret.setDatasetSubdomain(dettaglio.getSubdomain().getSubdomaincode());
			
			if (dettaglio.getStream()!=null)
			{
				ret.setSensorCode(dettaglio.getStream().getSmartobject().getSocode());
				ret.setStreamCode(dettaglio.getStream().getStreamcode());
				ret.setStreamDeploymentVersion(dettaglio.getVersion());
				ret.setStreamId(dettaglio.getStream().getIdstream());
				ret.setTipoStream(dettaglio.getStream().getSmartobject().getSoType().getIdSoType());
				ret.setVirtualEntitySlug(dettaglio.getStream().getSmartobject().getSlug());
			}
		}
		
		return ret;
	}

	public static CollectionConfDto convertBackofficeDettaglioStreamDatasetResponseToCollectionConfDto(
			BackofficeDettaglioStreamDatasetResponse dettaglio) {
		CollectionConfDto collectionConfDto = null;
		
		if (dettaglio!=null)
		{
			collectionConfDto = new CollectionConfDto();
			collectionConfDto.setSolrCollectionName(dettaglio.getDataset().getSolrcollectionname());
			collectionConfDto.setPhoenixSchemaName(dettaglio.getDataset().getPhoenixschemaname());
			collectionConfDto.setPhoenixTableName(dettaglio.getDataset().getPhoenixtablename());
		}
		
		return collectionConfDto;
	}

}
