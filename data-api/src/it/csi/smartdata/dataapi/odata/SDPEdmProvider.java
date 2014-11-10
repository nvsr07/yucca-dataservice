package it.csi.smartdata.dataapi.odata;

import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;
import it.csi.smartdata.dataapi.mongo.SDPMongoOdataCast;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationSet;
import org.apache.olingo.odata2.api.edm.provider.ComplexType;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.FunctionImport;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.api.exception.ODataException;

public class SDPEdmProvider extends EdmProvider {




	//private ArrayList<DBObject> configObject=null;

	
	private SDPMongoOdataCast mongoAccess= new SDPMongoOdataCast();
	private String codiceApi=null;

	public String getCodiceApi() {
		return codiceApi;
	}

	public void setCodiceApi(String codiceApi) {
		this.codiceApi = codiceApi;
	}

	static Logger log = Logger.getLogger(SDPEdmProvider.class.getPackage().getName());















 

	@Override
	public EntityType getEntityType(final FullQualifiedName edmFQName) throws ODataException {
		log.info("[SDPEdmProvider::getEntityType] BEGIN - calling SDPMongoOdataCast" );
		return mongoAccess.getEntityType(edmFQName, this.codiceApi);
	}


	@Override
	public ComplexType getComplexType(final FullQualifiedName edmFQName) throws ODataException {
		log.info("[SDPEdmProvider::getComplexType] BEGIN - calling SDPMongoOdataCast" );
		return mongoAccess.getComplexType(edmFQName, this.codiceApi);

	}


	@Override
	public AssociationSet getAssociationSet(final String entityContainer, final FullQualifiedName association,
			final String sourceEntitySetName, final String sourceEntitySetRole) throws ODataException {
		log.info("[SDPEdmProvider::getAssociationSet] BEGIN - calling SDPMongoOdataCast" );
		return mongoAccess.getAssociationSet(entityContainer, association, sourceEntitySetName, sourceEntitySetRole,  this.codiceApi);
		
	}

	@Override
	public EntitySet getEntitySet(final String entityContainer, final String name) throws ODataException {
		log.info("[SDPEdmProvider::getEntitySet] BEGIN - calling SDPMongoOdataCast" );
		return mongoAccess.getEntitySet(entityContainer, name, this.codiceApi);
	}
			
	@Override
	public Association getAssociation(final FullQualifiedName edmFQName) throws ODataException {
		log.info("[SDPEdmProvider::getAssociation] BEGIN - calling SDPMongoOdataCast" );
		return mongoAccess.getAssociation(edmFQName, this.codiceApi);
	}



	@Override
	public FunctionImport getFunctionImport(final String entityContainer, final String name) throws ODataException {
		log.info("[SDPEdmProvider::getFunctionImport] BEGIN - return null" );
		return null;
	}

	@Override
	public EntityContainerInfo getEntityContainerInfo(final String name) throws ODataException {
		log.info("[SDPEdmProvider::getEntityContainerInfo] BEGIN - calling SDPMongoOdataCast" );
		return mongoAccess.getEntityContainerInfo(name, this.codiceApi);
	}





	@Override
	public List<Schema> getSchemas() throws ODataException {
		log.info("[SDPEdmProvider::getSchemas] BEGIN " );
		try {
			return mongoAccess.getSchemasInternal(this.codiceApi);
		} catch (Exception ex) {
			log.error("[SDPEdmProvider::getSchemas] unexpected exeption",ex);
			ODataException oex = new ODataException("unexpected",ex);
			throw oex;
		} finally {
			log.info("[SDPEdmProvider::getSchemas] END " );
		}
	}


	

}
