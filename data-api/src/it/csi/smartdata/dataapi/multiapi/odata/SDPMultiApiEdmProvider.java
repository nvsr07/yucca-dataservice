package it.csi.smartdata.dataapi.multiapi.odata;

import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;
import it.csi.smartdata.dataapi.mongo.SDPMongoOdataCast;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationSet;
import org.apache.olingo.odata2.api.edm.provider.ComplexType;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.edm.provider.EntityContainer;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Facets;
import org.apache.olingo.odata2.api.edm.provider.FunctionImport;
import org.apache.olingo.odata2.api.edm.provider.Key;
import org.apache.olingo.odata2.api.edm.provider.NavigationProperty;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.PropertyRef;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.apache.olingo.odata2.api.exception.ODataException;

public class SDPMultiApiEdmProvider extends EdmProvider {




	//private ArrayList<DBObject> configObject=null;

	
	private String codiceApi=null;

	public String getCodiceApi() {
		return codiceApi;
	}

	public void setCodiceApi(String codiceApi) {
		this.codiceApi = codiceApi;
	}

	static Logger log = Logger.getLogger(SDPMultiApiEdmProvider.class.getPackage().getName());















 

	@Override
	public EntityType getEntityType(final FullQualifiedName edmFQName) throws ODataException {
		log.debug("[SDPMultiApiEdmProvider::getEntityType] BEGIN - calling SDPMongoOdataCast" );
		//return mongoAccess.getEntityType(edmFQName, this.codiceApi);
		return null;

	}


	@Override
	public ComplexType getComplexType(final FullQualifiedName edmFQName) throws ODataException {
		log.debug("[SDPMultiApiEdmProvider::getComplexType] BEGIN - calling SDPMongoOdataCast" );
		
		//return mongoAccess.getComplexType(edmFQName, this.codiceApi);
		return null;

	}


	@Override
	public AssociationSet getAssociationSet(final String entityContainer, final FullQualifiedName association,
			final String sourceEntitySetName, final String sourceEntitySetRole) throws ODataException {
		log.debug("[SDPMultiApiEdmProvider::getAssociationSet] BEGIN - calling SDPMongoOdataCast" );
		//return mongoAccess.getAssociationSet(entityContainer, association, sourceEntitySetName, sourceEntitySetRole,  this.codiceApi);
		return null;

	}

	@Override
	public EntitySet getEntitySet(final String entityContainer, final String name) throws ODataException {
		log.debug("[SDPMultiApiEdmProvider::getEntitySet] BEGIN - calling SDPMongoOdataCast" );
		//return mongoAccess.getEntitySet(entityContainer, name, this.codiceApi);
		return null;
	}
			
	@Override
	public Association getAssociation(final FullQualifiedName edmFQName) throws ODataException {
		log.debug("[SDPMultiApiEdmProvider::getAssociation] BEGIN - calling SDPMongoOdataCast" );
		//return mongoAccess.getAssociation(edmFQName, this.codiceApi);
		return null;
	}



	@Override
	public FunctionImport getFunctionImport(final String entityContainer, final String name) throws ODataException {
		log.debug("[SDPMultiApiEdmProvider::getFunctionImport] BEGIN - return null" );
		return null;
	}

	@Override
	public EntityContainerInfo getEntityContainerInfo(final String name) throws ODataException {
		log.debug("[SDPMultiApiEdmProvider::getEntityContainerInfo] BEGIN - calling SDPMongoOdataCast" );
		//return mongoAccess.getEntityContainerInfo(name, this.codiceApi);
		return null;

	}




	@Override
	public List<Schema> getSchemas() throws ODataException {
		log.debug("[SDPMultiApiEdmProvider::getSchemas] BEGIN " );
		try {
			return getSchemasInternal(this.codiceApi);
		} catch (Exception ex) {
			log.error("[SDPMultiApiEdmProvider::getSchemas] unexpected exeption",ex);
			ODataException oex = new ODataException("unexpected",ex);
			throw oex;
		} finally {
			log.debug("[SDPMultiApiEdmProvider::getSchemas] END " );
		}
	}

	public List<Schema> getSchemasInternal(String codiceApi) throws ODataException,Exception {
		List<Schema> schemas = new ArrayList<Schema>();
		

		schemas.add(getSchema("AlloggiVacan_1671","it.csi.smartdata.odata.regpie.AlloggiVacan_1671"));
		schemas.add(getSchema("ScuolePiemon_1282","it.csi.smartdata.odata.regpie.ScuolePiemon_1282"));
		schemas.add(getSchema("Scuole_costr_1198","it.csi.smartdata.odata.regpie.Scuole_costr_1198"));

		
		return schemas;
		
		
	}
	
	private Schema getSchema(String remoteApi, String remoteNamespace) {
		
		
		Schema schema = new Schema();
		schema.setNamespace(remoteNamespace);		
		
		List<EntityType> entityTypes = new ArrayList<EntityType>();
		
		List<Property> dataAttributes=new ArrayList<Property>();

		dataAttributes.add(new SimpleProperty().setName("internalId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
		dataAttributes.add(new SimpleProperty().setName("datasetVersion").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
		dataAttributes.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
		List<PropertyRef> keyPropertiesDataAttributes = new ArrayList<PropertyRef>();

		keyPropertiesDataAttributes.add(new PropertyRef().setName("internalId"));
		Key keyMeasure = new Key().setKeys(keyPropertiesDataAttributes);
		List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
		navigationProperties = new ArrayList<NavigationProperty>();
		
		EntityType entity=new EntityType().setName( SDPDataApiConstants.ENTITY_NAME_UPLOADDATA)
				.setProperties(dataAttributes).setKey(keyMeasure).setNavigationProperties(navigationProperties);

		
		entityTypes.add(entity);
		

		
		//CONTAINER
		String entContainerDB=SDPDataApiConstants.SMART_ENTITY_CONTAINER+"_"+remoteNamespace.replace('.', '_');
		List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
		EntityContainer entityContainer = new EntityContainer();
		entityContainer.setName(entContainerDB).setDefaultEntityContainer(true);

		
		//ENTITY SETS
		List<EntitySet> entitySets = new ArrayList<EntitySet>();
		EntitySet entityset=new EntitySet().setName(remoteApi+"__"+SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA).setEntityType( new FullQualifiedName(remoteNamespace, SDPDataApiConstants.ENTITY_NAME_UPLOADDATA)); 
		entitySets.add(entityset);
		
		entityContainer.setEntitySets(entitySets);
		

		
		
		
		schema.setEntityTypes(entityTypes);
		entityContainers.add(entityContainer); 
		schema.setEntityContainers(entityContainers);		
		
		return schema;
	}
	
}
