package it.csi.smartdata.dataapi.adminapi.edmprovider;

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

public class ExceptionOdataProvider implements SchemaOdataProvider {

	@Override
	public List<EntityType> getEntityTypes(
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		throw new ODataException("Not provider found for "+dettaglio.getApicode());
	}

	@Override
	public List<EntityContainer> getEntityContainers(
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		throw new ODataException("Not provider found for "+dettaglio.getApicode());
	}

	@Override
	public List<ComplexType> getComplexTypes(
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		throw new ODataException("Not provider found for "+dettaglio.getApicode());
	}

	@Override
	public List<Association> getAssociations(
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		throw new ODataException("Not provider found for "+dettaglio.getApicode());
	}

	@Override
	public EntityType getEntityType(FullQualifiedName edmFQName,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		throw new ODataException("Not provider found for "+dettaglio.getApicode());
	}

	@Override
	public ComplexType getComplexType(FullQualifiedName edmFQName,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		throw new ODataException("Not provider found for "+dettaglio.getApicode());
	}

	@Override
	public AssociationSet getAssociationSet(String entityContainer,
			FullQualifiedName association, String sourceEntitySetName,
			String sourceEntitySetRole, BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		throw new ODataException("Not provider found for "+dettaglio.getApicode());
	}

	@Override
	public EntitySet getEntitySet(String entityContainer, String name,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		throw new ODataException("Not provider found for "+dettaglio.getApicode());
	}

	@Override
	public Association getAssociation(FullQualifiedName edmFQName,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		throw new ODataException("Not provider found for "+dettaglio.getApicode());
	}

	@Override
	public EntityContainerInfo getEntityContainerInfo(String name,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		throw new ODataException("Not provider found for "+dettaglio.getApicode());
	}

}
