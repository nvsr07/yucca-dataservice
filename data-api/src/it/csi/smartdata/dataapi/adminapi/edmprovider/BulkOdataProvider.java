package it.csi.smartdata.dataapi.adminapi.edmprovider;

import it.csi.smartdata.dataapi.adminapi.Util;
import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.edm.EdmMultiplicity;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationEnd;
import org.apache.olingo.odata2.api.edm.provider.AssociationSet;
import org.apache.olingo.odata2.api.edm.provider.AssociationSetEnd;
import org.apache.olingo.odata2.api.edm.provider.ComplexType;
import org.apache.olingo.odata2.api.edm.provider.EntityContainer;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Facets;
import org.apache.olingo.odata2.api.edm.provider.Key;
import org.apache.olingo.odata2.api.edm.provider.NavigationProperty;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.PropertyRef;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.adminapi.response.ComponentResponse;

public class BulkOdataProvider extends AbstractOdataProvider implements SchemaOdataProvider {

	static Logger log = Logger.getLogger(BulkOdataProvider.class.getPackage().getName());
	
	@Override
	public List<EntityType> getEntityTypes(
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		String nameSpace = dettaglio.getEntitynamespace();
		boolean hasBinaryAttached = (dettaglio.getDettaglioStreamDatasetResponse().getDataset().getIdDataSourceBinary()!=null);
		
		List<EntityType> entityTypes = new ArrayList<EntityType>();
		entityTypes.add(getUploadDataType(nameSpace,dettaglio.getDettaglioStreamDatasetResponse().getComponents(),false,hasBinaryAttached));
		//1.2 binary
		if (hasBinaryAttached) 
			entityTypes.add(getBinaryDataType(nameSpace,dettaglio.getDettaglioStreamDatasetResponse().getComponents()));

		return entityTypes;
	}
	
	@Override
	public List<EntityContainer> getEntityContainers(
			BackofficeDettaglioApiResponse dettaglio) {
		String nameSpace = dettaglio.getEntitynamespace();
		String entContainerDB=SDPDataApiConstants.SMART_ENTITY_CONTAINER+"_"+nameSpace.replace('.', '_');
		boolean hasBinaryAttached = (dettaglio.getDettaglioStreamDatasetResponse().getDataset().getIdDataSourceBinary()!=null);
		
		List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
		EntityContainer entityContainer = new EntityContainer();
		entityContainer.setName(entContainerDB).setDefaultEntityContainer(true);

		List<EntitySet> entitySets = new ArrayList<EntitySet>();
		entitySets.add(new EntitySet().setName(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_UPLOADDATA)));

		//1.2 binary
		if (hasBinaryAttached)   
			entitySets.add( new EntitySet().setName(SDPDataApiConstants.ENTITY_SET_NAME_BINARY).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_BINARY)));
		
		
		entityContainer.setEntitySets(entitySets);
		

		//1.2 binary
		List<AssociationSet> associationSets = new ArrayList<AssociationSet>();
		if (hasBinaryAttached)   associationSets.add(
				 new AssociationSet().setName(SDPDataApiConstants.ASSOCIATION_SET_DATASETUPLOAD_BINARY)
					.setAssociation(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY))
					.setEnd1(new AssociationSetEnd().setRole(SDPDataApiConstants.ROLE_DATASETUPLOAD_BINARY).setEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA))
					.setEnd2(new AssociationSetEnd().setRole(SDPDataApiConstants.ROLE_BINARY_DATASETUPLOAD).setEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_BINARY)));
		entityContainer.setAssociationSets(associationSets);	
		
		entityContainers.add(entityContainer); 
		
		return entityContainers;
		
	}


	
	
	@Override
	public List<ComplexType> getComplexTypes(
			BackofficeDettaglioApiResponse dettaglio) {
		boolean hasBinaryAttached = (dettaglio.getDettaglioStreamDatasetResponse().getDataset().getIdDataSourceBinary()!=null);
		if (hasBinaryAttached)  {
		    List<ComplexType> complexTypes = new ArrayList<ComplexType>();
		    List<Property> properties = new ArrayList<Property>();
	        properties.add(new SimpleProperty().setName("idBinary").setType(EdmSimpleTypeKind.String));
		    complexTypes.add( new ComplexType().setName(SDPDataApiConstants.COMPLEX_TYPE_BINARYREF).setProperties(properties));
		    return complexTypes;
		}
		return null;
	}
	
	@Override
	public List<Association> getAssociations(
			BackofficeDettaglioApiResponse dettaglio) {
		String nameSpace = dettaglio.getEntitynamespace();
		boolean hasBinaryAttached = (dettaglio.getDettaglioStreamDatasetResponse().getDataset().getIdDataSourceBinary()!=null);
		
		if (hasBinaryAttached)   
		{
			List<Association> associations = new ArrayList<Association>();
			associations.add(new Association().setName(SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY)
					.setEnd1(
							new AssociationEnd().setType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_UPLOADDATA)).setRole(SDPDataApiConstants.ROLE_DATASETUPLOAD_BINARY).setMultiplicity(EdmMultiplicity.MANY))
					.setEnd2(
							new AssociationEnd().setType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_BINARY)).setRole(SDPDataApiConstants.ROLE_BINARY_DATASETUPLOAD).setMultiplicity(EdmMultiplicity.MANY)));
			return associations;
		}
		return null;
	
	}
		
	
	private EntityType getUploadDataType (String nameSpace,List<ComponentResponse> eleCampi,boolean historical, boolean hasBinaryAttached) throws ODataException{
		try {
			log.debug("[SDPMongoOdataCast::getUploadDataType] BEGIN");
			log.debug("[SDPMongoOdataCast::getUploadDataType] nameSpace="+nameSpace);
			log.debug("[SDPMongoOdataCast::getUploadDataType] historical="+historical);
			List<Property> dataAttributes=new ArrayList<Property>();

			dataAttributes.add(new SimpleProperty().setName("internalId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			dataAttributes.add(new SimpleProperty().setName("datasetVersion").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
//			dataAttributes.add(new SimpleProperty().setName("current").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
			dataAttributes.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			
			if(historical) {
				dataAttributes.add(new SimpleProperty().setName("startdate").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
				dataAttributes.add(new SimpleProperty().setName("enddate").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
				dataAttributes.add(new SimpleProperty().setName("parentObjId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));

			}
			List<Property> componentProp= Util.convertFromComponentResponseToProperty(eleCampi,nameSpace);
			for (int i=0;componentProp!=null && i<componentProp.size();i++) {
				dataAttributes.add(componentProp.get(i));
			}
			List<PropertyRef> keyPropertiesDataAttributes = new ArrayList<PropertyRef>();

			keyPropertiesDataAttributes.add(new PropertyRef().setName("internalId"));
			Key keyMeasure = new Key().setKeys(keyPropertiesDataAttributes);

			//1.2 binary
			 
			List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
			navigationProperties = new ArrayList<NavigationProperty>();
//			navigationProperties.add(new NavigationProperty().setName(SDPDataApiConstants.ENTITY_SET_NAME_BINARY)
//					.setRelationship(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY)).setFromRole(SDPDataApiConstants.ROLE_DATASETUPLOAD_BINARY).setToRole(SDPDataApiConstants.ROLE_BINARY_DATASETUPLOAD)
//					);
			if (hasBinaryAttached) navigationProperties.add(new NavigationProperty().setName(SDPDataApiConstants.ENTITY_SET_NAME_BINARY)
					.setRelationship(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY)).setFromRole(SDPDataApiConstants.ROLE_DATASETUPLOAD_BINARY).setToRole(SDPDataApiConstants.ROLE_BINARY_DATASETUPLOAD));


			if(historical) {
				return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_UPLOADDATA_HISTORY)
						.setProperties(dataAttributes).setKey(keyMeasure).setNavigationProperties(navigationProperties);

			} else {
				return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_UPLOADDATA)
						.setProperties(dataAttributes).setKey(keyMeasure).setNavigationProperties(navigationProperties);
			}

			//			if(historical) {
			//				return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_UPLOADDATA_HISTORY)
			//						.setProperties(dataAttributes).setKey(keyMeasure);
			//			} else {
			//				return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_UPLOADDATA)
			//						.setProperties(dataAttributes).setKey(keyMeasure);
			//			}
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getUploadDataType] " + e);
			throw new ODataException(e);
		} finally {
			log.debug("[SDPMongoOdataCast::getUploadDataType] END");
		}			
	}	
	
	
	//1.2 binary
		private EntityType getBinaryDataType (String nameSpace,List<ComponentResponse> eleCapmpi) throws ODataException{
			try {
				log.debug("[SDPMongoOdataCast::getBinaryDataType] BEGIN");
				log.debug("[SDPMongoOdataCast::getBinaryDataType] nameSpace="+nameSpace);
				List<Property> dataAttributes=new ArrayList<Property>();

				dataAttributes.add(new SimpleProperty().setName("internalId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
				dataAttributes.add(new SimpleProperty().setName("datasetVersion").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
				//			dataAttributes.add(new SimpleProperty().setName("current").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
				dataAttributes.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
				dataAttributes.add(new SimpleProperty().setName("idBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
				dataAttributes.add(new SimpleProperty().setName("filenameBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
				dataAttributes.add(new SimpleProperty().setName("aliasNameBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
				dataAttributes.add(new SimpleProperty().setName("sizeBinary").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
//				dataAttributes.add(new SimpleProperty().setName("insertDateBinary").setType(EdmSimpleTypeKind.DateTimeOffset).setFacets(new Facets().setNullable(true)));
//				dataAttributes.add(new SimpleProperty().setName("lastUpdateDateBinary").setType(EdmSimpleTypeKind.DateTimeOffset).setFacets(new Facets().setNullable(true)));
				dataAttributes.add(new SimpleProperty().setName("contentTypeBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
				//dataAttributes.add(new SimpleProperty().setName("pathHdfsBinary ").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
				dataAttributes.add(new SimpleProperty().setName("urlDownloadBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
				dataAttributes.add(new SimpleProperty().setName("metadataBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));

				//			if(historical) {
				//				dataAttributes.add(new SimpleProperty().setName("startdate").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
				//				dataAttributes.add(new SimpleProperty().setName("enddate").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
				//				dataAttributes.add(new SimpleProperty().setName("parentObjId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
				//
				//			}
				//			List<Property> componentProp= getDatasetField(eleCapmpi);
				//			for (int i=0;componentProp!=null && i<componentProp.size();i++) {
				//				dataAttributes.add(componentProp.get(i));
				//			}
				List<PropertyRef> keyPropertiesDataAttributes = new ArrayList<PropertyRef>();

				keyPropertiesDataAttributes.add(new PropertyRef().setName("internalId"));
				Key keyMeasure = new Key().setKeys(keyPropertiesDataAttributes);

				List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
				navigationProperties = new ArrayList<NavigationProperty>();
				//		return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_MEASURES)
				//				.setProperties(measureProps).setKey(keyMeasure).setNavigationProperties(navigationProperties);	

				navigationProperties.add(new NavigationProperty().setName(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA)
						.setRelationship(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY)).setFromRole(SDPDataApiConstants.ROLE_BINARY_DATASETUPLOAD).setToRole(SDPDataApiConstants.ROLE_DATASETUPLOAD_BINARY));

				return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_BINARY)
						.setProperties(dataAttributes).setKey(keyMeasure);

			} catch (Exception e) {
				log.error("[SDPMongoOdataCast::getBinaryDataType] " + e);
				throw new ODataException(e);
			} finally {
				log.debug("[SDPMongoOdataCast::getBinaryDataType] END");
			}			
		}	
		
		
		@Override
		public EntityType getEntityType(FullQualifiedName edmFQName,
				BackofficeDettaglioApiResponse dettaglio) throws ODataException {
			EntityType ret = null;
			boolean hasBinaryAttached = (dettaglio.getDettaglioStreamDatasetResponse().getDataset().getIdDataSourceBinary()!=null);
			if (edmFQName.getNamespace().equals(dettaglio.getEntitynamespace()))
			{
				if (edmFQName.getName().equals(SDPDataApiConstants.ENTITY_NAME_UPLOADDATA))
					return getUploadDataType(edmFQName.getNamespace(),dettaglio.getDettaglioStreamDatasetResponse().getComponents(), false, hasBinaryAttached);
				else if (edmFQName.getName().equals(SDPDataApiConstants.ENTITY_NAME_BINARY) && hasBinaryAttached)
					return getBinaryDataType(edmFQName.getNamespace(),dettaglio.getDettaglioStreamDatasetResponse().getComponents());
			}
			
			return null;
		}

		@Override
		public ComplexType getComplexType(FullQualifiedName edmFQName,
				BackofficeDettaglioApiResponse dettaglio) throws ODataException {
			boolean hasBinaryAttached = (dettaglio.getDettaglioStreamDatasetResponse().getDataset().getIdDataSourceBinary()!=null);
			if (edmFQName.getNamespace().equals(dettaglio.getEntitynamespace()))
			{
				if (edmFQName.getName().equals(SDPDataApiConstants.COMPLEX_TYPE_BINARYREF) && hasBinaryAttached)
					return getBinaryDataType(edmFQName.getNamespace(),dettaglio.getDettaglioStreamDatasetResponse().getComponents());
			}
			
			return null;
		}

		@Override
		public AssociationSet getAssociationSet(String entityContainer,
				FullQualifiedName association, String sourceEntitySetName,
				String sourceEntitySetRole,
				BackofficeDettaglioApiResponse dettaglio) {
			boolean hasBinaryAttached = (dettaglio.getDettaglioStreamDatasetResponse().getDataset().getIdDataSourceBinary()!=null);
			if (association.getNamespace().equals(dettaglio.getEntitynamespace()) && hasBinaryAttached)
			{
			 return new AssociationSet().setName(SDPDataApiConstants.ASSOCIATION_SET_DATASETUPLOAD_BINARY)
				.setAssociation(new FullQualifiedName(dettaglio.getEntitynamespace(), SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY))
				.setEnd1(new AssociationSetEnd().setRole(SDPDataApiConstants.ROLE_DATASETUPLOAD_BINARY).setEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA))
				.setEnd2(new AssociationSetEnd().setRole(SDPDataApiConstants.ROLE_BINARY_DATASETUPLOAD).setEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_BINARY));
			}
			
			return null;
		}

		@Override
		public EntitySet getEntitySet(String entityContainer, String name,
				BackofficeDettaglioApiResponse dettaglio) throws ODataException {
			String entContainerDB=SDPDataApiConstants.SMART_ENTITY_CONTAINER+"_"+dettaglio.getEntitynamespace().replace('.', '_');
			String nameSpace= dettaglio.getEntitynamespace();
			if (entContainerDB.equals(entityContainer))
			{
				if (SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA.equals(name)) {
					return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_UPLOADDATA));						
				} else if (SDPDataApiConstants.ENTITY_SET_NAME_BINARY.equals(name)) {
					return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_BINARY));						
				} 			
			}
			return null;
		}

		@Override
		public Association getAssociation(FullQualifiedName edmFQName,
				BackofficeDettaglioApiResponse dettaglio) throws ODataException {
			String nameSpace = dettaglio.getEntitynamespace();
			boolean hasBinaryAttached = (dettaglio.getDettaglioStreamDatasetResponse().getDataset().getIdDataSourceBinary()!=null);
			
			if (edmFQName.getNamespace().equals(dettaglio.getEntitynamespace()) && hasBinaryAttached)   
			{
				Association	association = new Association().setName(SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY)
						.setEnd1(
								new AssociationEnd().setType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_UPLOADDATA)).setRole(SDPDataApiConstants.ROLE_DATASETUPLOAD_BINARY).setMultiplicity(EdmMultiplicity.MANY))
						.setEnd2(
								new AssociationEnd().setType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_BINARY)).setRole(SDPDataApiConstants.ROLE_BINARY_DATASETUPLOAD).setMultiplicity(EdmMultiplicity.MANY));
				return association;
			}
			return null;
		}
} 

