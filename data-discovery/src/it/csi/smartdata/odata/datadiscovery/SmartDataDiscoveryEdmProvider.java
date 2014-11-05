package it.csi.smartdata.odata.datadiscovery;

import java.util.ArrayList;
import java.util.List;

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

	static final String ENTITY_NAME_DATASET = "Dataset";
	static final String ENTITY_NAME_FIELD = "Field";

	private static final FullQualifiedName ENTITY_TYPE_1_1 = new FullQualifiedName(NAMESPACE_DISCOVERY, ENTITY_NAME_DATASET);
	private static final FullQualifiedName ENTITY_TYPE_1_2 = new FullQualifiedName(NAMESPACE_DISCOVERY, ENTITY_NAME_FIELD);

	private static final FullQualifiedName ASSOCIATION_FIELD_DATASET = new FullQualifiedName(NAMESPACE_DISCOVERY,"Field_Dataset_Dataset_Fields");

	private static final String ROLE_1_1 = "Field_Dataset";
	private static final String ROLE_1_2 = "Dataset_Fields";

	private static final String ASSOCIATION_SET = "Fields_Datasets";

	@Override
	public List<Schema> getSchemas() throws ODataException {
		List<Schema> schemas = new ArrayList<Schema>();

		Schema schema = new Schema();
		schema.setNamespace(NAMESPACE_DISCOVERY);

		List<EntityType> entityTypes = new ArrayList<EntityType>();
		entityTypes.add(getEntityType(new FullQualifiedName(NAMESPACE_DISCOVERY, ENTITY_NAME_DATASET)));
		schema.setEntityTypes(entityTypes);

		//	    List<ComplexType> complexTypes = new ArrayList<ComplexType>();
		//	    complexTypes.add(getComplexType(COMPLEX_TYPE));
		//	    schema.setComplexTypes(complexTypes);

		List<Association> associations = new ArrayList<Association>();
		associations.add(getAssociation(ASSOCIATION_FIELD_DATASET));
		schema.setAssociations(associations);

		List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
		EntityContainer entityContainer = new EntityContainer();
		entityContainer.setName(SMART_ENTITY_CONTAINER).setDefaultEntityContainer(true);

		List<EntitySet> entitySets = new ArrayList<EntitySet>();
		entitySets.add(getEntitySet(SMART_ENTITY_CONTAINER, ENTITY_SET_NAME_DATASETS));
		entityContainer.setEntitySets(entitySets);

		List<AssociationSet> associationSets = new ArrayList<AssociationSet>();
		associationSets.add(getAssociationSet(SMART_ENTITY_CONTAINER, ASSOCIATION_FIELD_DATASET, ENTITY_SET_NAME_DATASETS, ROLE_1_2));
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

		propertiesSmartObject.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)).setCustomizableFeedMappings(cfeed));

		propertiesSmartObject.add(new SimpleProperty().setName("tenant").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("dataDomain").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("licence").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
		propertiesSmartObject.add(new SimpleProperty().setName("fps").setType(EdmSimpleTypeKind.Double).setFacets(new Facets().setNullable(true)));
		
		propertiesSmartObject.add(new SimpleProperty().setName("name").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
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

		
		
		//Keys
		List<PropertyRef> keyPropertiesSmartObject = new ArrayList<PropertyRef>();
		keyPropertiesSmartObject.add(new PropertyRef().setName("idDataset"));
		Key keySmartObject = new Key().setKeys(keyPropertiesSmartObject);

		// Navigation Properties
		List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
		navigationProperties.add(new NavigationProperty().setName("Fields")
				.setRelationship(ASSOCIATION_FIELD_DATASET).setFromRole(ROLE_1_2).setToRole(ROLE_1_1));

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

		return new EntityType().setName(ENTITY_NAME_DATASET)
				.setProperties(propertiesSmartObject)
				.setKey(keySmartObject);
	}

}
