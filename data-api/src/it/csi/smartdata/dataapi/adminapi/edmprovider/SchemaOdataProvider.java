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

public interface SchemaOdataProvider {
	List<EntityType> getEntityTypes(BackofficeDettaglioApiResponse dettaglio)
			throws ODataException;

	List<EntityContainer> getEntityContainers(
			BackofficeDettaglioApiResponse dettaglio) throws ODataException;

	List<ComplexType> getComplexTypes(BackofficeDettaglioApiResponse dettaglio)
			throws ODataException;

	List<Association> getAssociations(BackofficeDettaglioApiResponse dettaglio)
			throws ODataException;

	EntityType getEntityType(FullQualifiedName edmFQName,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException;

	ComplexType getComplexType(FullQualifiedName edmFQName,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException;

	AssociationSet getAssociationSet(String entityContainer,
			FullQualifiedName association, String sourceEntitySetName,
			String sourceEntitySetRole, BackofficeDettaglioApiResponse dettaglio)
			throws ODataException;

	EntitySet getEntitySet(String entityContainer, String name,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException;

	Association getAssociation(FullQualifiedName edmFQName,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException;

	EntityContainerInfo getEntityContainerInfo(String name,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException;
}
