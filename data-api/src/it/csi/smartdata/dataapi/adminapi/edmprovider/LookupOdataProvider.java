package it.csi.smartdata.dataapi.adminapi.edmprovider;

import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;

import java.util.List;

import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationSet;
import org.apache.olingo.odata2.api.edm.provider.ComplexType;
import org.apache.olingo.odata2.api.edm.provider.EntityContainer;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;

public class LookupOdataProvider implements SchemaOdataProvider {

	
	private SchemaOdataProvider getCorrectProvider(BackofficeDettaglioApiResponse dettaglio)
	{
		if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_DATASETBULK
				.equals(dettaglio.getDettaglioStreamDatasetResponse().getDataset().getDatasetSubtype().getDatasetSubtype()))
			return new BulkOdataProvider();
		else if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_DATASETSTREAM
				.equals(dettaglio.getDettaglioStreamDatasetResponse().getDataset().getDatasetSubtype().getDatasetSubtype()))
			return new StreamOdataProvider();
		else if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_DATASETSOCIAL
				.equals(dettaglio.getDettaglioStreamDatasetResponse().getDataset().getDatasetSubtype().getDatasetSubtype()))
			return new SocialOdataProvider();
		else return new ExceptionOdataProvider();
	}
	
	@Override
	public List<EntityType> getEntityTypes(
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		return getCorrectProvider(dettaglio).getEntityTypes(dettaglio);
	}

	@Override
	public List<EntityContainer> getEntityContainers(
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		return getCorrectProvider(dettaglio).getEntityContainers(dettaglio);
	}

	@Override
	public List<ComplexType> getComplexTypes(
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		return getCorrectProvider(dettaglio).getComplexTypes(dettaglio);
	}

	@Override
	public List<Association> getAssociations(
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		return getCorrectProvider(dettaglio).getAssociations(dettaglio);
	}

	@Override
	public EntityType getEntityType(FullQualifiedName edmFQName,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		return getCorrectProvider(dettaglio).getEntityType(edmFQName, dettaglio);
	}

	@Override
	public ComplexType getComplexType(FullQualifiedName edmFQName,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		return getCorrectProvider(dettaglio).getComplexType(edmFQName, dettaglio);
	}

	@Override
	public AssociationSet getAssociationSet(String entityContainer,
			FullQualifiedName association, String sourceEntitySetName,
			String sourceEntitySetRole, BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		return getCorrectProvider(dettaglio).getAssociationSet(entityContainer, association, sourceEntitySetName, sourceEntitySetRole, dettaglio);
	}

	@Override
	public EntitySet getEntitySet(String entityContainer, String name,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		return getCorrectProvider(dettaglio).getEntitySet(entityContainer, name, dettaglio);
	}

	@Override
	public Association getAssociation(FullQualifiedName edmFQName,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		return getCorrectProvider(dettaglio).getAssociation(edmFQName, dettaglio);
	}

	@Override
	public EntityContainerInfo getEntityContainerInfo(String name,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		return getCorrectProvider(dettaglio).getEntityContainerInfo(name, dettaglio);
	}

}
