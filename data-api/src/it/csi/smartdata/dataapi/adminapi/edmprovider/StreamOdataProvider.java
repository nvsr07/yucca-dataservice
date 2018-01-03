package it.csi.smartdata.dataapi.adminapi.edmprovider;

import it.csi.smartdata.dataapi.adminapi.Util;
import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.EdmTargetPath;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationSet;
import org.apache.olingo.odata2.api.edm.provider.ComplexType;
import org.apache.olingo.odata2.api.edm.provider.CustomizableFeedMappings;
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

public class StreamOdataProvider extends AbstractOdataProvider implements SchemaOdataProvider {

	static Logger log = Logger.getLogger(StreamOdataProvider.class.getPackage().getName());
	
	@Override
	public List<EntityType> getEntityTypes(
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		String nameSpace = dettaglio.getEntitynamespace();
		List<EntityType> entityTypes = new ArrayList<EntityType>();
		entityTypes.add(getMeasureType(nameSpace,dettaglio.getDettaglioStreamDatasetResponse().getComponents()));
		entityTypes.add(getMeasureStatsType(nameSpace,dettaglio.getDettaglioStreamDatasetResponse().getComponents()));
		return entityTypes;
	}
	
	@Override
	public List<EntityContainer> getEntityContainers(
			BackofficeDettaglioApiResponse dettaglio) {
		String nameSpace = dettaglio.getEntitynamespace();
		String entContainerDB=SDPDataApiConstants.SMART_ENTITY_CONTAINER+"_"+nameSpace.replace('.', '_');
		List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
		EntityContainer entityContainer = new EntityContainer();
		entityContainer.setName(entContainerDB).setDefaultEntityContainer(true);

		List<EntitySet> entitySets = new ArrayList<EntitySet>();
		entitySets.add(new EntitySet().setName(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASURES)));	
		entitySets.add(new EntitySet().setName(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASURES_STATS)));	

		//entitySets.add(getEntitySet(entContainerDB, SDPDataApiConstants.ENTITY_SET_NAME_SMARTOBJECT,codiceApi));
		//entitySets.add(getEntitySet(entContainerDB, SDPDataApiConstants.ENTITY_SET_NAME_STREAMS,codiceApi));
		entityContainer.setEntitySets(entitySets);
		entityContainers.add(entityContainer); 
		return entityContainers;
	}


	
	
	@Override
	public List<ComplexType> getComplexTypes(
			BackofficeDettaglioApiResponse dettaglio) {
		return null;
	}

	
	
	private EntityType getMeasureType (String nameSpace,List<ComponentResponse> eleCapmpi) throws ODataException{
		try {
			log.debug("[StreamOdataProvider::getMeasureType] BEGIN");
			List<Property> measureProps=new ArrayList<Property>();
			
			// SPOSTATI IN CFGd
			measureProps.add(new SimpleProperty().setName("streamCode").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			measureProps.add(new SimpleProperty().setName("sensor").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			measureProps.add(new SimpleProperty().setName("time").setType(EdmSimpleTypeKind.DateTimeOffset).setFacets(new Facets().setNullable(false)));

			measureProps.add(new SimpleProperty().setName("internalId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			measureProps.add(new SimpleProperty().setName("datasetVersion").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));

			/* VECCHI eliminati */
			//measureProps.add(new ComplexProperty().setName("values").setType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASUREVALUES)));
			//measureProps.add(new SimpleProperty().setName("current").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
			
			List<Property> componentProp= Util.convertFromComponentResponseToProperty(eleCapmpi,nameSpace);
			for (int i=0;componentProp!=null && i<componentProp.size();i++) {
				measureProps.add(componentProp.get(i));
			}
			List<PropertyRef> keyPropertiesMeasure = new ArrayList<PropertyRef>();

			keyPropertiesMeasure.add(new PropertyRef().setName("internalId"));
			Key keyMeasure = new Key().setKeys(keyPropertiesMeasure);
			List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
			navigationProperties = new ArrayList<NavigationProperty>();
			navigationProperties.add(new NavigationProperty().setName(SDPDataApiConstants.ENTITY_SET_NAME_STREAMS)
					.setRelationship(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_MEASURE_STREAM)).setFromRole(SDPDataApiConstants.ROLE_MEASURE_STREAM).setToRole(SDPDataApiConstants.ROLE_STREAM_MEASURE));
			//		return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_MEASURES)
			//				.setProperties(measureProps).setKey(keyMeasure).setNavigationProperties(navigationProperties);	
			return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_MEASURES)
					.setProperties(measureProps).setKey(keyMeasure);
		} catch (Exception e) {
			log.error("[StreamOdataProvider::getMeasureType] " + e);
			throw new ODataException(e);
		} finally {
			log.debug("[StreamOdataProvider::getMeasureType] END");
		}			
	}

	private EntityType getMeasureStatsType (String nameSpace,List<ComponentResponse> eleCapmpi) throws ODataException{
		try {
			log.debug("[StreamOdataProvider::getMeasureStatsType] BEGIN");
			List<Property> measureProps=new ArrayList<Property>();
			
//			measureProps.add(new SimpleProperty().setName("internalId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
//			measureProps.add(new SimpleProperty().setName("datasetVersion").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
//			measureProps.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("year").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("month").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("dayofmonth").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("hour").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			//YUCCA-346
			measureProps.add(new SimpleProperty().setName("minute").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));

			//YUCCA-388
			measureProps.add(new SimpleProperty().setName("dayofweek").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			
			
			List<Property> componentProp= Util.convertFromComponentResponseToProperty(eleCapmpi,nameSpace);
			for (int i=0;componentProp!=null && i<componentProp.size();i++) {
				
				SimpleProperty curProp=new SimpleProperty()
				.setName( ((SimpleProperty)componentProp.get(i)).getName()+"_sts")
				.setType(((SimpleProperty)componentProp.get(i)).getType())
				.setFacets(new Facets().setNullable(true));
				
				measureProps.add(curProp);
			}
			List<PropertyRef> keyPropertiesMeasure = new ArrayList<PropertyRef>();
			
			measureProps.add(new SimpleProperty().setName("count").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));

			keyPropertiesMeasure.add(new PropertyRef().setName("year"));
			keyPropertiesMeasure.add(new PropertyRef().setName("month"));
			keyPropertiesMeasure.add(new PropertyRef().setName("dayofmonth"));
			keyPropertiesMeasure.add(new PropertyRef().setName("hour"));
			//YUCCA-346
			keyPropertiesMeasure.add(new PropertyRef().setName("minute"));

			//YUCCA-388
			keyPropertiesMeasure.add(new PropertyRef().setName("dayofweek"));
			
			
			Key keyMeasure = new Key().setKeys(keyPropertiesMeasure);
			return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_MEASURES_STATS)
					.setProperties(measureProps).setKey(keyMeasure);
		} catch (Exception e) {
			log.error("[StreamOdataProvider::getMeasureStatsType] " + e);
			throw new ODataException(e);
		} finally {
			log.debug("[StreamOdataProvider::getMeasureStatsType] END");
		}			
	}

	@Override
	public List<Association> getAssociations(
			BackofficeDettaglioApiResponse dettaglio) {
		return null;
	}	
	
	
	
	
	// methods unused
	
	private EntityType getSmartObjectType()throws ODataException {
		try {
			log.debug("[SDPMongoOdataCast::getSmartObjectType] BEGIN");
			List<Property> propertiesSmartObject = new ArrayList<Property>();

			CustomizableFeedMappings cfeed = new CustomizableFeedMappings();
			cfeed.setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE);
			propertiesSmartObject.add(new SimpleProperty().setName("idSmartObject").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)).setCustomizableFeedMappings(cfeed));

			propertiesSmartObject.add(new SimpleProperty().setName("codiceSmartObject").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			List<PropertyRef> keyPropertiesSmartObject = new ArrayList<PropertyRef>();
			keyPropertiesSmartObject.add(new PropertyRef().setName("idSmartObject"));
			Key keySmartObject = new Key().setKeys(keyPropertiesSmartObject);
			return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_SMARTOBJECT)
					.setProperties(propertiesSmartObject)
					.setKey(keySmartObject);
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getSmartObjectType] " + e);
			throw new ODataException(e);
		} finally {
			log.debug("[SDPMongoOdataCast::getSmartObjectType] END");
		}		
	}

	private EntityType getStreamType(String nameSpace)throws ODataException {
		try {
			log.debug("[SDPMongoOdataCast::getStreamType] BEGIN");
			List<Property> propertiesStream = new ArrayList<Property>();

			CustomizableFeedMappings cfeed = new CustomizableFeedMappings();
			cfeed.setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE);

			propertiesStream.add(new SimpleProperty().setName("idStream").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)).setCustomizableFeedMappings(cfeed));
			propertiesStream.add(new SimpleProperty().setName("codiceStream").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			propertiesStream.add(new SimpleProperty().setName("codiceTenant").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			propertiesStream.add(new SimpleProperty().setName("nomeStream").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			List<PropertyRef> keyPropertiesStream = new ArrayList<PropertyRef>();
			keyPropertiesStream.add(new PropertyRef().setName("idStream"));
			Key key = new Key().setKeys(keyPropertiesStream);
			List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
			navigationProperties.add(new NavigationProperty().setName(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES)
					.setRelationship(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_MEASURE_STREAM)).setFromRole(SDPDataApiConstants.ROLE_STREAM_MEASURE).setToRole(SDPDataApiConstants.ROLE_MEASURE_STREAM));
			return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_STREAMS)
					.setProperties(propertiesStream)
					.setKey(key);					
			//		return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_STREAMS)
			//				.setProperties(propertiesStream)
			//				.setKey(key)
			//				.setNavigationProperties(navigationProperties);
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getStreamType] " + e);
			throw new ODataException(e);
		} finally {
			log.debug("[SDPMongoOdataCast::getStreamType] END");
		}			
	}

	@Override
	public EntityType getEntityType(FullQualifiedName edmFQName,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		EntityType ret = null;
		
		if (edmFQName.getNamespace().equals(dettaglio.getEntitynamespace()))
		{
			if (edmFQName.getName().equals(SDPDataApiConstants.ENTITY_NAME_MEASURES))
				return getMeasureType(edmFQName.getNamespace(),dettaglio.getDettaglioStreamDatasetResponse().getComponents());
			else if (edmFQName.getName().equals(SDPDataApiConstants.ENTITY_NAME_MEASURES_STATS))
				return getMeasureStatsType(edmFQName.getNamespace(),dettaglio.getDettaglioStreamDatasetResponse().getComponents());
		}
		
		return null;
	}

	@Override
	public ComplexType getComplexType(FullQualifiedName edmFQName,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		return null;
	}

	@Override
	public AssociationSet getAssociationSet(String entityContainer,
			FullQualifiedName association, String sourceEntitySetName,
			String sourceEntitySetRole, BackofficeDettaglioApiResponse dettaglio) {
		return null;
	}
	
	@Override
	public EntitySet getEntitySet(String entityContainer, String name,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		String entContainerDB=SDPDataApiConstants.SMART_ENTITY_CONTAINER+"_"+dettaglio.getEntitynamespace().replace('.', '_');
		String nameSpace= dettaglio.getEntitynamespace();
		if (entContainerDB.equals(entityContainer))
		{
			if (SDPDataApiConstants.ENTITY_SET_NAME_MEASURES.equals(name)) {
				return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASURES));						
			} else if (SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS.equals(name)) {
				return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASURES_STATS));						
			} 			
		}
		return null;
	}

	@Override
	public Association getAssociation(FullQualifiedName edmFQName,
			BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		return null;
	}
}
