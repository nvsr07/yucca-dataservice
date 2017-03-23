package it.csi.smartdata.odata.datadiscovery;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.edm.EdmMultiplicity;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.EdmTargetPath;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationEnd;
import org.apache.olingo.odata2.api.edm.provider.AssociationSet;
import org.apache.olingo.odata2.api.edm.provider.AssociationSetEnd;
import org.apache.olingo.odata2.api.edm.provider.CustomizableFeedMappings;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.edm.provider.EntityContainer;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Facets;
import org.apache.olingo.odata2.api.edm.provider.Key;
import org.apache.olingo.odata2.api.edm.provider.NavigationProperty;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.PropertyRef;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.apache.olingo.odata2.api.exception.ODataException;

public class SmartDataDiscoveryEdmProvider extends EdmProvider{

	static final String NAMESPACE_DISCOVERY = "it.csi.smartdata.odata.discovery";

	static final String SMART_ENTITY_CONTAINER="SmartDataDiscoveryEntityContainer";

	static final String ENTITY_SET_NAME_DATASETS = "Datasets";
	static final String ENTITY_SET_NAME_FIELDS = "Fields";
	static final String ENTITY_SET_NAME_STREAMS = "Streams";

	static final String ENTITY_NAME_DATASET = "Dataset";
	static final String ENTITY_NAME_FIELD = "Field";
	static final String ENTITY_NAME_STREAM = "Stream";

	private static final FullQualifiedName ENTITY_TYPE_1_1 = new FullQualifiedName(NAMESPACE_DISCOVERY, ENTITY_NAME_DATASET);
	private static final FullQualifiedName ENTITY_TYPE_1_2 = new FullQualifiedName(NAMESPACE_DISCOVERY, ENTITY_NAME_FIELD);
	private static final FullQualifiedName ENTITY_TYPE_1_3 = new FullQualifiedName(NAMESPACE_DISCOVERY, ENTITY_NAME_STREAM);

	private static final FullQualifiedName ASSOCIATION_FIELD_DATASET = new FullQualifiedName(NAMESPACE_DISCOVERY,"Field_Dataset_Dataset_Fields");
	private static final FullQualifiedName ASSOCIATION_STREAM_DATASET = new FullQualifiedName(NAMESPACE_DISCOVERY,"Stream_Dataset_Dataset_Stream");

	private static final String ROLE_1_1 = "Field_Dataset";
	private static final String ROLE_1_2 = "Dataset_Fields";
	private static final String ROLE_1_3 = "Dataset_Stream";
	private static final String ROLE_1_4 = "Stream_Dataset";

	private static final String ASSOCIATION_SET = "Fields_Datasets";
	private static final String ASSOCIATION_STREAM_SET = "Streams_Datasets";
	static Logger log = Logger.getLogger(SmartDataDiscoveryEdmProvider.class);
	@Override
	public List<Schema> getSchemas() throws ODataException {
		List<Schema> schemas = new ArrayList<Schema>();

		Schema schema = new Schema();
		schema.setNamespace(NAMESPACE_DISCOVERY);

		List<EntityType> entityTypes = new ArrayList<EntityType>();
		entityTypes.add(getEntityType(new FullQualifiedName(NAMESPACE_DISCOVERY, ENTITY_NAME_DATASET)));
		entityTypes.add(getEntityType(new FullQualifiedName(NAMESPACE_DISCOVERY, ENTITY_NAME_STREAM)));
		schema.setEntityTypes(entityTypes);

		//	    List<ComplexType> complexTypes = new ArrayList<ComplexType>();
		//	    complexTypes.add(getComplexType(COMPLEX_TYPE));
		//	    schema.setComplexTypes(complexTypes);

		List<Association> associations = new ArrayList<Association>();
		associations.add(getAssociation(ASSOCIATION_FIELD_DATASET));
		associations.add(getAssociation(ASSOCIATION_STREAM_DATASET));
		schema.setAssociations(associations);

		List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
		EntityContainer entityContainer = new EntityContainer();
		entityContainer.setName(SMART_ENTITY_CONTAINER).setDefaultEntityContainer(true);

		List<EntitySet> entitySets = new ArrayList<EntitySet>();
		entitySets.add(getEntitySet(SMART_ENTITY_CONTAINER, ENTITY_SET_NAME_DATASETS));
		entitySets.add(getEntitySet(SMART_ENTITY_CONTAINER, ENTITY_SET_NAME_STREAMS));
		entityContainer.setEntitySets(entitySets);

		List<AssociationSet> associationSets = new ArrayList<AssociationSet>();
		associationSets.add(getAssociationSet(SMART_ENTITY_CONTAINER, ASSOCIATION_FIELD_DATASET, ENTITY_SET_NAME_DATASETS, ROLE_1_2));
		associationSets.add(getAssociationSet(SMART_ENTITY_CONTAINER, ASSOCIATION_STREAM_DATASET, ENTITY_SET_NAME_DATASETS, ROLE_1_3));
//		associationSets.add(getAssociationSet(SMART_ENTITY_CONTAINER, ASSOCIATION_STREAM_DATASET, ENTITY_SET_NAME_DATASETS, ROLE_1_4));
		entityContainer.setAssociationSets(associationSets);

		//	    List<FunctionImport> functionImports = new ArrayList<FunctionImport>();
		//	    functionImports.add(getFunctionImport(ENTITY_CONTAINER, FUNCTION_IMPORT));
		//	    entityContainer.setFunctionImports(functionImports);

		entityContainers.add(entityContainer);
		schema.setEntityContainers(entityContainers);


		schemas.add(schema);

		return schemas;
	}

	public EntityType getEntityType(final FullQualifiedName edmFQName) throws ODataException {
		EntityType ret=null;
		if (NAMESPACE_DISCOVERY.equals(edmFQName.getNamespace())) {
			if (ENTITY_NAME_DATASET.equals(edmFQName.getName())) {
				ret=getDataSetType();
			}else if (ENTITY_NAME_FIELD.equals(edmFQName.getName())) {
				ret=getFieldType();
			}else if (ENTITY_NAME_STREAM.equals(edmFQName.getName())) {
				ret=getStreamType();
			}
		}
		return ret;
	}

	@Override
	public EntitySet getEntitySet(final String entityContainer, final String name) throws ODataException {
		if (SMART_ENTITY_CONTAINER.equals(entityContainer)) {
			if (ENTITY_SET_NAME_DATASETS.equals(name)) {
				return new EntitySet().setName(name).setEntityType(new FullQualifiedName(NAMESPACE_DISCOVERY, ENTITY_NAME_DATASET));
			}else if (ENTITY_SET_NAME_FIELDS.equals(name)) {
				return new EntitySet().setName(name).setEntityType(new FullQualifiedName(NAMESPACE_DISCOVERY, ENTITY_NAME_FIELD));
			} else if (ENTITY_SET_NAME_STREAMS.equals(name)) {
				return new EntitySet().setName(name).setEntityType(new FullQualifiedName(NAMESPACE_DISCOVERY, ENTITY_NAME_STREAM));
			} 
		}
		return null;
	}

	@Override
	public Association getAssociation(final FullQualifiedName edmFQName) throws ODataException {
		if (NAMESPACE_DISCOVERY.equals(edmFQName.getNamespace())) {
			if (ASSOCIATION_FIELD_DATASET.getName().equals(edmFQName.getName())) {
				return new Association().setName(ASSOCIATION_FIELD_DATASET.getName())
						.setEnd1(
								new AssociationEnd().setType(ENTITY_TYPE_1_1).setRole(ROLE_1_2).setMultiplicity(EdmMultiplicity.ONE))
								.setEnd2(
										new AssociationEnd().setType(ENTITY_TYPE_1_2).setRole(ROLE_1_1).setMultiplicity(EdmMultiplicity.MANY));
			}else if(ASSOCIATION_STREAM_DATASET.getName().equals(edmFQName.getName())) {
				return new Association().setName(ASSOCIATION_STREAM_DATASET.getName())
						.setEnd1(
								new AssociationEnd().setType(ENTITY_TYPE_1_1).setRole(ROLE_1_3).setMultiplicity(EdmMultiplicity.ONE))
								.setEnd2(
										new AssociationEnd().setType(ENTITY_TYPE_1_3).setRole(ROLE_1_4).setMultiplicity(EdmMultiplicity.ONE));

			}
		}
		return null;
	}

	@Override
	public AssociationSet getAssociationSet(final String entityContainer, final FullQualifiedName association,
			final String sourceEntitySetName, final String sourceEntitySetRole) throws ODataException {
		if (SMART_ENTITY_CONTAINER.equals(entityContainer)) {
			if (ASSOCIATION_FIELD_DATASET.equals(association)) {
				return new AssociationSet().setName(ASSOCIATION_SET)
						.setAssociation(ASSOCIATION_FIELD_DATASET)
						.setEnd1(new AssociationSetEnd().setRole(ROLE_1_2).setEntitySet(ENTITY_SET_NAME_DATASETS))
						.setEnd2(new AssociationSetEnd().setRole(ROLE_1_1).setEntitySet(ENTITY_SET_NAME_FIELDS));
			}else if (ASSOCIATION_STREAM_DATASET.equals(association)) {
				return new AssociationSet().setName(ASSOCIATION_STREAM_SET)
						.setAssociation(ASSOCIATION_STREAM_DATASET)
						.setEnd1(new AssociationSetEnd().setRole(ROLE_1_3).setEntitySet(ENTITY_SET_NAME_DATASETS))
						.setEnd2(new AssociationSetEnd().setRole(ROLE_1_4).setEntitySet(ENTITY_SET_NAME_STREAMS));
			}
		}
		return null;
	}

	@Override
	public EntityContainerInfo getEntityContainerInfo(final String name) throws ODataException {
		if (name == null || SMART_ENTITY_CONTAINER.equals(name)) {
			return new EntityContainerInfo().setName(SMART_ENTITY_CONTAINER).setDefaultEntityContainer(true);
		}
		return null;
	}

	private EntityType getDataSetType() {
		List<Property> propertiesSmartObject = new ArrayList<Property>();
		CustomizableFeedMappings cfeed = new CustomizableFeedMappings();
		cfeed.setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE);

		propertiesSmartObject.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(false)).setCustomizableFeedMappings(cfeed));

		propertiesSmartObject.add(new SimpleProperty().setName("tenantCode").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("dataDomain").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("codSubDomain").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("license").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("fps").setType(EdmSimpleTypeKind.Double).setFacets(new Facets().setNullable(true)));

		propertiesSmartObject.add(new SimpleProperty().setName("tenantsharing").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("description").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("download").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("datasetName").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("visibility").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("registrationDate").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));		
		propertiesSmartObject.add(new SimpleProperty().setName("startIngestionDate").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("endIngestionDate").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("importFileType").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));		
		propertiesSmartObject.add(new SimpleProperty().setName("datasetStatus").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));

		propertiesSmartObject.add(new SimpleProperty().setName("measureUnit").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("tags").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));

		propertiesSmartObject.add(new SimpleProperty().setName("API").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("STREAM").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));

		propertiesSmartObject.add(new SimpleProperty().setName("datasetCode").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("datasetVersion").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("disclaimer").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("copyright").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));


		propertiesSmartObject.add(new SimpleProperty().setName("externalReference").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		//opendata
		propertiesSmartObject.add(new SimpleProperty().setName("isOpendata").setType(EdmSimpleTypeKind.Boolean).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("author").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("dataUpdateDate").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("language").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));

		//Keys
		List<PropertyRef> keyPropertiesSmartObject = new ArrayList<PropertyRef>();
		keyPropertiesSmartObject.add(new PropertyRef().setName("idDataset"));
		Key keySmartObject = new Key().setKeys(keyPropertiesSmartObject);

		// Navigation Properties
		List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
		navigationProperties.add(new NavigationProperty().setName("Fields")
				.setRelationship(ASSOCIATION_FIELD_DATASET).setFromRole(ROLE_1_2).setToRole(ROLE_1_1));
		navigationProperties.add(new NavigationProperty().setName("Stream")
				.setRelationship(ASSOCIATION_STREAM_DATASET).setFromRole(ROLE_1_3).setToRole(ROLE_1_4));

		return new EntityType().setName(ENTITY_NAME_DATASET)
				.setProperties(propertiesSmartObject)
				.setKey(keySmartObject).setNavigationProperties(navigationProperties);
	}

	private EntityType getFieldType() {
		List<Property> propertiesSmartObject = new ArrayList<Property>();
		CustomizableFeedMappings cfeed = new CustomizableFeedMappings();
		cfeed.setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE);

		propertiesSmartObject.add(new SimpleProperty().setName("fieldName").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)).setCustomizableFeedMappings(cfeed));
		propertiesSmartObject.add(new SimpleProperty().setName("fieldAlias").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("dataType").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("sourceColumn").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("isKey").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("measureUnit").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));


		//FIXME FIX LINk of Field Entities
		//Keys
		List<PropertyRef> keyPropertiesSmartObject = new ArrayList<PropertyRef>();
		keyPropertiesSmartObject.add(new PropertyRef().setName("fieldName"));
		Key keySmartObject = new Key().setKeys(keyPropertiesSmartObject);

		return new EntityType().setName(ENTITY_NAME_FIELD)
				.setProperties(propertiesSmartObject)
				.setKey(keySmartObject);
	}
	private EntityType getStreamType() {
		List<Property> propertiesSmartObject = new ArrayList<Property>();
		CustomizableFeedMappings cfeed = new CustomizableFeedMappings();
		cfeed.setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE);

		propertiesSmartObject.add(new SimpleProperty().setName("idStream").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(false)).setCustomizableFeedMappings(cfeed));
		propertiesSmartObject.add(new SimpleProperty().setName("idSensor").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(false)));
		propertiesSmartObject.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(false)));
		propertiesSmartObject.add(new SimpleProperty().setName("datasetVersion").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(false)));
		propertiesSmartObject.add(new SimpleProperty().setName("tenantCode").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("visibility").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("tenantsharing").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		
		propertiesSmartObject.add(new SimpleProperty().setName("streamCode").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("streamName").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("streamDescription").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		
		propertiesSmartObject.add(new SimpleProperty().setName("smartOCode").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("smartOName").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("smartOType").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("smartOCategory").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));

		propertiesSmartObject.add(new SimpleProperty().setName("latitude").setType(EdmSimpleTypeKind.Double).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("longitude").setType(EdmSimpleTypeKind.Double).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("elevation").setType(EdmSimpleTypeKind.Double).setFacets(new Facets().setNullable(true)));

		
		propertiesSmartObject.add(new SimpleProperty().setName("twtResultType").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("twtMaxStreamsOfVE").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("twtRatePercentage").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("twtCount").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("twtUntil").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("twtLocale").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("twtLang").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("twtGeolocUnit").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("twtQuery").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("twtGeolocLat").setType(EdmSimpleTypeKind.Double).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("twtGeolocLon").setType(EdmSimpleTypeKind.Double).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("twtGeolocRadius").setType(EdmSimpleTypeKind.Double).setFacets(new Facets().setNullable(true)));
		
		propertiesSmartObject.add(new SimpleProperty().setName("externalReference").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		//opendata
		propertiesSmartObject.add(new SimpleProperty().setName("isOpendata").setType(EdmSimpleTypeKind.Boolean).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("author").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("dataUpdateDate").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("language").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		
		
		List<PropertyRef> keyPropertiesSmartObject = new ArrayList<PropertyRef>();
		keyPropertiesSmartObject.add(new PropertyRef().setName("idStream"));
		Key keySmartObject = new Key().setKeys(keyPropertiesSmartObject);


		// Navigation Properties
		List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
		navigationProperties.add(new NavigationProperty().setName("Dataset")
				.setRelationship(ASSOCIATION_STREAM_DATASET).setFromRole(ROLE_1_4).setToRole(ROLE_1_3));


		return new EntityType().setName(ENTITY_NAME_STREAM)
				.setProperties(propertiesSmartObject)
				.setKey(keySmartObject).setNavigationProperties(navigationProperties);
	}

}
