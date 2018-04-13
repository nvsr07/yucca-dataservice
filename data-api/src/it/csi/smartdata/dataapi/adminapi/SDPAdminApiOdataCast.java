package it.csi.smartdata.dataapi.adminapi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationSet;
import org.apache.olingo.odata2.api.edm.provider.ComplexType;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Facets;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.csi.yucca.adminapi.client.AdminApiClientException;
import org.csi.yucca.adminapi.client.db.BackofficeDettaglioClientDB;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.adminapi.response.ComponentResponse;

import it.csi.smartdata.dataapi.adminapi.edmprovider.LookupOdataProvider;
import it.csi.smartdata.dataapi.adminapi.edmprovider.SchemaOdataProvider;
import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;
import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;
import it.csi.smartdata.dataapi.dto.SDPDataResult;
import it.csi.smartdata.dataapi.exception.SDPCustomQueryOptionException;
import it.csi.smartdata.dataapi.exception.SDPOrderBySizeException;
import it.csi.smartdata.dataapi.exception.SDPPageSizeException;
import it.csi.smartdata.dataapi.odata.SDPOdataFilterExpression;
import it.csi.smartdata.dataapi.odata.SDPPhoenixExpression;
import it.csi.smartdata.dataapi.solr.CloudSolrSingleton;
import it.csi.smartdata.dataapi.solr.KnoxSolrSingleton;

public class SDPAdminApiOdataCast {

	public static final String DATA_TYPE_MEASURE="measures";
	public static final String DATA_TYPE_DATA="data";
	public static final String DATA_TYPE_SOCIAL="social";

	
	static Logger log = Logger.getLogger(SDPAdminApiOdataCast.class
			.getPackage().getName());

	private String codiceApi = null;
	private BackofficeDettaglioApiResponse configObject = null;
	private SolrClient server = null;

	
	public SDPAdminApiOdataCast() {
		if ("KNOX".equalsIgnoreCase(SDPDataApiConfig.getInstance().getSolrTypeAccess()))
		{
			server = KnoxSolrSingleton.getServer();
		}
		else {
			server = CloudSolrSingleton.getServer();
		}

	}

	private void initDbObject(String codiceApi) throws ODataException {
		if (null == configObject || !codiceApi.equals(this.codiceApi)) {
			this.codiceApi = codiceApi;
			try {
				log.info("[SDPAdminApiOdataCast::initDbObject] Calling for codiceApi:"+codiceApi);
//				this.configObject = BackofficeDettaglioClient.getBackofficeDettaglioApi(SDPDataApiConfig.getInstance().getAdminApiUrl(),  codiceApi, log.getName());
				this.configObject = BackofficeDettaglioClientDB.getBackofficeDettaglioApi(codiceApi, log.getName());
				log.info("[SDPAdminApiOdataCast::initDbObject] Calling for codiceApi:"+codiceApi+"..done!");
			} catch (AdminApiClientException e) {
				log.error("[SDPAdminApiOdataCast::initDbObject] Error", e);
				throw new ODataException(e);
			}
		}
	}

	
	public EntityType getEntityType(FullQualifiedName edmFQName,
			String codiceApi) throws ODataException {
		initDbObject(codiceApi);
		SchemaOdataProvider provider = new LookupOdataProvider();
		return provider.getEntityType(edmFQName, configObject);
	}

	public ComplexType getComplexType(FullQualifiedName edmFQName,
			String codiceApi) throws ODataException {
		initDbObject(codiceApi);
		SchemaOdataProvider provider = new LookupOdataProvider();
		return provider.getComplexType(edmFQName, configObject);
	}

	public AssociationSet getAssociationSet(String entityContainer,
			FullQualifiedName association, String sourceEntitySetName,
			String sourceEntitySetRole, String codiceApi) throws ODataException {
		initDbObject(codiceApi);
		SchemaOdataProvider provider = new LookupOdataProvider();
		return provider.getAssociationSet(entityContainer,
			 association, sourceEntitySetName,
			 sourceEntitySetRole, configObject);
	}

	public EntitySet getEntitySet(String entityContainer, String name,
			String codiceApi)throws ODataException {
		initDbObject(codiceApi);
		SchemaOdataProvider provider = new LookupOdataProvider();
		return provider.getEntitySet(entityContainer,
			 name, configObject);
	}

	public Association getAssociation(FullQualifiedName edmFQName,
			String codiceApi) throws ODataException {
		initDbObject(codiceApi);
		SchemaOdataProvider provider = new LookupOdataProvider();
		return provider.getAssociation(edmFQName, configObject);
	}

	public EntityContainerInfo getEntityContainerInfo(String name,
			String codiceApi) throws ODataException {
		initDbObject(codiceApi);
		SchemaOdataProvider provider = new LookupOdataProvider();
		return provider.getEntityContainerInfo(name, configObject);
	}
	
	
	public List<Schema> getSchemasInternal(String codiceApi) throws ODataException,Exception {
		try {
			log.debug("[SDPMongoOdataCast::getSchemasInternal] BEGIN");
			log.info("[SDPMongoOdataCast::getSchemasInternal] codiceApi="+codiceApi);

			List<Schema> schemas = new ArrayList<Schema>();
			initDbObject(codiceApi);
			String nameSpace= configObject.getEntitynamespace();

			SchemaOdataProvider provider = new LookupOdataProvider();
			Schema schema = new Schema();
			schema.setNamespace(nameSpace);
			schema.setEntityTypes(provider.getEntityTypes(configObject));
			schema.setEntityContainers(provider.getEntityContainers(configObject));
			schema.setAssociations(provider.getAssociations(configObject));
			schema.setComplexTypes(provider.getComplexTypes(configObject));
			schemas.add(schema);
		
			return schemas;
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getSchemasInternal] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getSchemasInternal] END");
		}		
	}	
	

	public HashMap<String, String> getDatasetMetadata(String codiceApi)
			throws Exception {
		initDbObject(codiceApi);
		
		List<ComponentResponse> elencoCampi = configObject.getDettaglioStreamDatasetResponse().getComponents();
		HashMap<String, String> mappaCampi = new HashMap<String, String>();

		for (int i = 0; i < elencoCampi.size(); i++) {
			ComponentResponse cur = elencoCampi.get(i);
			String nome = cur.getName();
			String tipo = cur.getDataType().getDatatypecode();
			mappaCampi.put(nome, tipo);

		}

		return mappaCampi;
	}


	
	/**
	 * DATI
	 * 
	 * @param codiceApi
	 * @param nameSpace
	 * @param entityContainer
	 * @param internalId
	 * @return
	 */
	public SDPDataResult getMeasuresPerApi(String codiceApi, String nameSpace,
			EdmEntityContainer entityContainer, String internalId,
			Object userQuery, Object userOrderBy, int skip, int limit,
			String dataType) throws Exception {
	
		// TODO YUCCA-74 odata evoluzione
		try {
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] BEGIN");
			log.info("[SDPAdminApiOdataData::getMeasuresPerApi] codiceApi = "
					+ codiceApi);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] nameSpace = "
					+ nameSpace);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] entityContainer = "
					+ entityContainer);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] internalId = "
					+ internalId);
			log.info("[SDPAdminApiOdataData::getMeasuresPerApi] userQuery = "
					+ userQuery);
	
			initDbObject(codiceApi);
			List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
			int totCnt = 0;

			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * elencodataset potrebbe contenere pi? elementi dello stesso
			 * dataset in versione differente ad es: idDataset= 1,
			 * datasetVersion=1, [campo1:int,camp2:string,campo3:date]
			 * idDataset= 1, datasetVersion=2,
			 * [campo1:int,camp2:string,campo3:date,campo11:long] idDataset= 3,
			 * datasetVersion=1, [campo1:log]
			 * 
			 * deve diventare idDataset= 1, datasetVersion=1,2,
			 * [campo1:int,camp2
			 * :string,campo3:date,campo1:int,camp2:string,campo3
			 * :date,campo11:long] idDataset= 3, datasetVersion=1 [campo1:log]
			 * 
			 * 
			 * si dovrebbe trasformare List<DBObject> elencoDataset in un array
			 * di oggetti di questo tipo:
			 * 
			 * idDataset array di datasetVersion array dei campi ottenuto come
			 * join dei campi delle varie versioni di quel dataset parte di
			 * config (presa da una versione a caso) info presa da una versione
			 * a caso
			 */
	
			String dsCodes = "|";
			String tenantsCodes = "|";


			String nameSpaceStream = configObject.getEntitynamespace(); 
//			String tenantStrean = ((DBObject) elencoDataset.get(i).get(
//					"configData")).get("tenantCode").toString();

			SDPDataResult cur = getMeasuresPerStreamNewLimitSolr(codiceApi,
					nameSpaceStream, entityContainer,
							 internalId,
							dataType, userQuery, userOrderBy, skip, limit);

			List<Map<String, Object>> misureCur = cur.getDati();

			for (int k = 0; misureCur != null && k < misureCur.size(); k++) {
				ret.add(misureCur.get(k));
			}
			totCnt += cur.getTotalCount();

	
			return new SDPDataResult(ret, totCnt, tenantsCodes, dsCodes);
	
		} catch (SDPOrderBySizeException e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerApi] SDPOrderBySizeException"
					+ e);
			throw (SDPOrderBySizeException) e;
		} catch (SDPPageSizeException e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerDataset] SDPPageSizeException"
					+ e);
			throw (SDPPageSizeException) e;
		} catch (Exception e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerApi] " + e);
			throw e;
		} finally {
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] END");
	
		}
	}
	
	public SDPDataResult getMeasuresStatsPerApi(String codiceApi,
			String nameSpace, EdmEntityContainer entityContainer,
			String internalId, Object userQuery, Object userOrderBy, int skip,
			int limit, String timeGroupByParam, String timeGroupOperatorsParam,
			Object groupOutQuery, String dataType) throws Exception {
	
		// TODO YUCCA-74 odata evoluzione
	
		try {
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] BEGIN");
			log.info("[SDPAdminApiOdataData::getMeasuresPerApi] codiceApi="
					+ codiceApi);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] nameSpace="
					+ nameSpace);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] entityContainer="
					+ entityContainer);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] internalId="
					+ internalId);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] userQuery="
					+ userQuery);
	
			initDbObject(codiceApi);
			List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
			int totCnt = 0;
	

			String nameSpaceStream = configObject.getEntitynamespace();

			SDPDataResult cur = getMeasuresStatsPerStreamPhoenix(
							nameSpaceStream, entityContainer,
							internalId,
							dataType, userQuery, userOrderBy, skip, limit,
							timeGroupByParam, timeGroupOperatorsParam,
							groupOutQuery);
			List<Map<String, Object>> misureCur = cur.getDati();

			for (int k = 0; misureCur != null && k < misureCur.size(); k++) {
				ret.add(misureCur.get(k));
			}
			totCnt += cur.getTotalCount();
	
	
			return new SDPDataResult(ret, totCnt, 
					configObject.getDettaglioStreamDatasetResponse().getTenantManager().getTenantcode(),
					configObject.getDettaglioStreamDatasetResponse().getDataset().getDatasetcode());
		} catch (Exception e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerApi] " + e);
			throw e;
		} finally {
			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] END");
	
		}
	}
	
	public SDPDataResult getMeasuresPerDataset(String codiceApi,
			String nameSpace, EdmEntityContainer entityContainer,
			String internalId, Object userQuery, Object userOrderBy, int skip,
			int limit) throws Exception {
	
		// TODO YUCCA-74 odata evoluzione
		try {
			log.debug("[SDPAdminApiOdataData::getMeasuresPerDataset] BEGIN");
			log.info("[SDPAdminApiOdataData::getMeasuresPerDataset] codiceApi="
					+ codiceApi);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerDataset] nameSpace="
					+ nameSpace);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerDataset] entityContainer="
					+ entityContainer);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerDataset] internalId="
					+ internalId);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerDataset] userQuery="
					+ userQuery);
	
			initDbObject(codiceApi);
			List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
	
//	
//			List<DBObject> elencoDataset = orderNestDS(mongoDataAccess
//					.getDatasetPerApi(codiceApi));

			int totCnt = 0;
	
			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * elencodataset potrebbe contenere più elementi dello stesso
			 * dataset in versione differente ad es: idDataset= 1,
			 * datasetVersion=1, [campo1:int,camp2:string,campo3:date]
			 * idDataset= 1, datasetVersion=2,
			 * [campo1:int,camp2:string,campo3:date,campo11:long] idDataset= 3,
			 * datasetVersion=1, [campo1:log]
			 * 
			 * deve diventare idDataset= 1, datasetVersion=1,2,
			 * [campo1:int,camp2
			 * :string,campo3:date,campo1:int,camp2:string,campo3
			 * :date,campo11:long] idDataset= 3, datasetVersion=1 [campo1:log]
			 * 
			 * 
			 * si dovrebbe trasformare List<DBObject> elencoDataset in un array
			 * di oggetti di questo tipo:
			 * 
			 * idDataset array di datasetVersion array dei campi ottenuto come
			 * join dei campi delle varie versioni di quel dataset parte di
			 * config (presa da una versione a caso) info presa da una versione
			 * a caso
			 */
	
//			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] Dataset.size = "
//					+ elencoDataset.size());
//			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] elencoDataset = "
//					+ elencoDataset);
	
	
//			log.debug("[SDPAdminApiOdataData::getMeasuresPerApi] Dataset = "
//					+ ((DBObject) elencoDataset.get(i)));
			// TODO log a debug
			String nameSpaceStrean = configObject.getEntitynamespace();

			SDPDataResult cur = getMeasuresPerStreamNewLimitSolr(codiceApi,
							nameSpaceStrean, entityContainer,
							internalId,
							DATA_TYPE_DATA,
							userQuery, userOrderBy, skip, limit);

			List<Map<String, Object>> misureCur = cur.getDati();
			for (int k = 0; misureCur != null && k < misureCur.size(); k++) {
				ret.add(misureCur.get(k));
			}
			totCnt += cur.getTotalCount();
	
			return new SDPDataResult(ret, totCnt, configObject.getDettaglioStreamDatasetResponse().getTenantManager().getTenantcode(), configObject.getApicode());
		} catch (SDPOrderBySizeException e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerDataset] SDPOrderBySizeException"
					+ e);
			throw (SDPOrderBySizeException) e;
		} catch (SDPPageSizeException e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerDataset] SDPPageSizeException"
					+ e);
			throw (SDPPageSizeException) e;
		} catch (Exception e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerDataset] " + e);
			throw e;
		} finally {
			log.debug("[SDPAdminApiOdataData::getMeasuresPerDataset] END");
	
		}
	}
	
	public SDPDataResult getBynaryPerDataset(String codiceApi,
			String nameSpace, EdmEntityContainer entityContainer,
			String internalId, Object userQuery, Object userOrderBy,
			ArrayList<String> elencoIdBinary, int skip, int limit)
			throws Exception {
	
		// TODO YUCCA-74 odata evoluzione
	
		try {
			log.debug("[SDPAdminApiOdataData::getBynaryPerDataset] BEGIN");
			log.info("[SDPAdminApiOdataData::getBynaryPerDataset] codiceApi="
					+ codiceApi);
			log.debug("[SDPAdminApiOdataData::getBynaryPerDataset] nameSpace="
					+ nameSpace);
			log.debug("[SDPAdminApiOdataData::getBynaryPerDataset] entityContainer="
					+ entityContainer);
			log.debug("[SDPAdminApiOdataData::getBynaryPerDataset] internalId="
					+ internalId);
			log.info("[SDPAdminApiOdataData::getBynaryPerDataset] userQuery="
					+ userQuery);
	
			initDbObject(codiceApi);
			List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
	
			int totCnt = 0;
	
			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * elencodataset potrebbe contenere più elementi dello stesso
			 * dataset in versione differente ad es: idDataset= 1,
			 * datasetVersion=1, [campo1:int,camp2:string,campo3:date]
			 * idDataset= 1, datasetVersion=2,
			 * [campo1:int,camp2:string,campo3:date,campo11:long] idDataset= 3,
			 * datasetVersion=1, [campo1:log]
			 * 
			 * deve diventare idDataset= 1, datasetVersion=1,2,
			 * [campo1:int,camp2
			 * :string,campo3:date,campo1:int,camp2:string,campo3
			 * :date,campo11:long] idDataset= 3, datasetVersion=1 [campo1:log]
			 * 
			 * 
			 * si dovrebbe trasformare List<DBObject> elencoDataset in un array
			 * di oggetti di questo tipo:
			 * 
			 * idDataset array di datasetVersion array dei campi ottenuto come
			 * join dei campi delle varie versioni di quel dataset parte di
			 * config (presa da una versione a caso) info presa da una versione
			 * a caso
			 */
	
			String nameSpaceStream = configObject.getEntitynamespace();

			SDPDataResult cur = getBinary(
					nameSpaceStream, entityContainer,
					internalId,
					DATA_TYPE_DATA, userQuery,
					userOrderBy, elencoIdBinary, codiceApi, skip, limit);
			List<Map<String, Object>> misureCur = cur.getDati();
			for (int k = 0; misureCur != null && k < misureCur.size(); k++) {
				ret.add(misureCur.get(k));
			}
			totCnt += cur.getTotalCount();
	
			return new SDPDataResult(ret, totCnt, 
					configObject.getDettaglioStreamDatasetResponse().getTenantManager().getTenantcode(),
					configObject.getDettaglioStreamDatasetResponse().getDataset().getDatasetcode());
		} catch (Exception e) {
			log.error("[SDPAdminApiOdataData::getBynaryPerDataset] " + e);
			throw e;
		} finally {
			log.debug("[SDPAdminApiOdataData::getBynaryPerDataset] END");
	
		}
	}
	
	
	
	public SDPDataResult getMeasuresPerStreamNewLimitSolr(String codiceApi, String nameSpace, EdmEntityContainer entityContainer,String internalId,String datatType,Object userQuery, Object userOrderBy,
			int skipI,
			int limitI
			) throws ODataException {
		String collection=null;
		String idDataset=null;
		String datasetCode=null;
//		String datasetToFindVersion=null;
		List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();
		long cnt = 0;
		long skipL=skipI;
		long limitL=limitI;

		initDbObject(codiceApi);

		// TODO YUCCA-74 odata evoluzione

		try {
			log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] BEGIN");
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] codiceApi="+codiceApi);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] nameSpace="+nameSpace);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] entityContainer="+entityContainer);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] internalId="+internalId);
			log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] datatType="+datatType);
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] userQuery="+userQuery);
//			String solrCollection= codiceTenant;
//			String codiceTenantOrig=codiceTenant;

			List<Property> compPropsTot=new ArrayList<Property>();
//			List<Property> compPropsCur=new ArrayList<Property>();			

			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] limit_init --> "+skipL);
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] skip_init --> "+skipL);

			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero di collencion,host, port, db specifici per il dataset
			//       - modificare eventualmente la logica di recupero dell'idDataset
			//INVARIATO!!

			idDataset=configObject.getDettaglioStreamDatasetResponse().getDataset().getIddataset().toString();
			datasetCode=configObject.getDettaglioStreamDatasetResponse().getDataset().getDatasetcode();
			String streamSubtype=configObject.getDettaglioStreamDatasetResponse().getDataset().getDatasetSubtype().getDatasetSubtype();


			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * ATTENZIONE!!!!!! datasetVersion sara un array da mettere in in
			 * INUTILE
			 */

//			datasetToFindVersion=takeNvlValues(streamMetadata.get("datasetVersion"));

			collection = configObject.getDettaglioStreamDatasetResponse().getDataset().getSolrcollectionname();

			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero dell'elenco dei campi che contiene il join di info.fuields di tutte le versioni di quel dataset
			List<ComponentResponse> eleCapmpi= configObject.getDettaglioStreamDatasetResponse().getComponents();
			compPropsTot = Util.convertFromComponentResponseToProperty(eleCapmpi, nameSpace);
			Map<String, String> campoTipoMetadato= Util.convertFromComponentResponseToMap(eleCapmpi); 
					
			String queryTotSolr="(iddataset_l:"+idDataset+")";
			String queryTotCntSolr="(iddataset_l:"+idDataset+")";

			queryTotSolr+= " AND (datasetversion_l : [ 0 TO * ])";

			if (null!=internalId) {
				queryTotSolr += "AND (id : "+internalId+")";
				queryTotCntSolr += "AND (id : "+internalId+")";

			}
			if (null != userQuery) {
				log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] userQuery="+userQuery);
				if (userQuery instanceof SDPOdataFilterExpression) {
					queryTotSolr += "AND ("+userQuery+")";
					queryTotCntSolr += "AND ("+userQuery+")";
				} else {
					queryTotSolr += "AND "+userQuery;
					queryTotCntSolr += "AND "+userQuery;
				}

				//query.append("$and", userQuery);
			}

			String  query = queryTotSolr;

			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] total data query ="+query);
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] collection ="+collection);

			//yucca-1080
			//			queryTotSolr=queryTotSolr.toLowerCase().replaceAll("iddataset_l", "idDataset_l").replaceAll("datasetversion_l", "datasetVersion_l");
			//			queryTotCntSolr=queryTotCntSolr.toLowerCase().replaceAll("iddataset_l", "idDataset_l").replaceAll("datasetversion_l", "datasetVersion_l");

			//CloudSolrClient solrServer =  CloudSolrSingleton.getServer();
			SolrClient solrServer = server;

			SolrQuery solrQuery = new SolrQuery();
			solrQuery.setQuery("*:*");
			solrQuery.setFilterQueries(queryTotCntSolr);
			solrQuery.setRows(1);

			long starTtime=0;
			long deltaTime=-1;

			starTtime=System.currentTimeMillis();
			QueryResponse rsp = solrServer.query(collection,solrQuery);
			SolrDocumentList aaa = (SolrDocumentList)rsp.getResponse().get("response");
			cnt = aaa.getNumFound();		
			try {
				deltaTime=System.currentTimeMillis()-starTtime;
			} catch (Exception e) {}
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] total data query COUNT executed in --> "+deltaTime);

			starTtime=0;
			deltaTime=-1;

			int maxdocPerPage=SDPDataApiConfig.getInstance().getMaxDocumentPerPage();
			try {
				// TODO add maxodatapage
				//maxdocPerPage=Integer.parseInt(MongoTenantDbSingleton.getInstance().getMaxDocPerPage(codiceTenantOrig));
				log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] max doc per page from configuration --> "+maxdocPerPage);
			} catch (Exception e) {
				
			}
			
			/** nuovi controlli skip e limit **/
			if (skipL<0) skipL=0;

			//controlli sui valoris massimi ammessi 
			if(skipL>0 && skipL>SDPDataApiConfig.getInstance().getMaxSkipPages()) throw new SDPPageSizeException("invalid skip value: max skip = "+SDPDataApiConfig.getInstance().getMaxSkipPages(),Locale.UK);
			if(limitL> 0 && limitL>maxdocPerPage) throw new SDPPageSizeException("invalid top value: max document per page = "+maxdocPerPage,Locale.UK);


			//se lo skip porta oltre il numero di risultati eccezione
			if (skipL>cnt) throw new SDPPageSizeException("skip value out of range: max document in query result = "+cnt,Locale.UK);

			if (limitL<0) {

				// se limit non valorizzato si restituisce tutto il resultset (limit=cnt) e si solleva eccezione se il resulset supera il numero massimo di risultati per pagina
				if ((cnt>maxdocPerPage)) throw new SDPPageSizeException("too many documents; use top parameter: max document per page = "+maxdocPerPage,Locale.UK);
				limitL=cnt;
			} 


			if (limitL>0 && limitL>(cnt-skipL)) limitL=cnt-skipL;

			ArrayList<SortClause> orderSolr=null;

			if (null!=userOrderBy) {
				//log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] ORDINE  "+((ArrayList<Object>)userOrderBy).get(0));
				
				boolean orderByAllowed=false;
				if (cnt<SDPDataApiConstants.SDP_MAX_DOC_FOR_ORDERBY) {
					orderByAllowed=true;
				} else if ((DATA_TYPE_MEASURE.equals(datatType) || DATA_TYPE_SOCIAL.equals(datatType) )&& ((ArrayList<SortClause>)userOrderBy).size()<=1) {

					SortClause elemOrder=(SortClause)((ArrayList<SortClause>)userOrderBy).get(0);
					if (elemOrder.getItem().equalsIgnoreCase("time_dt")) orderByAllowed=true;
					if (elemOrder.getItem().equalsIgnoreCase("retweetParentId") && ("socialDataset".equalsIgnoreCase(streamSubtype))) orderByAllowed=true;
					if (elemOrder.getItem().equalsIgnoreCase("userId") && ("socialDataset".equalsIgnoreCase(streamSubtype))) orderByAllowed=true;
					if (elemOrder.getItem().equalsIgnoreCase("id")) orderByAllowed=true;
				} else if (DATA_TYPE_DATA.equals(datatType) && ((ArrayList<SortClause>)userOrderBy).size()<=1) {
					//SDPMongoOrderElement elemOrder=(SDPMongoOrderElement)((ArrayList<SDPMongoOrderElement>)userOrderBy).get(0);
					SortClause elemOrder=(SortClause)((ArrayList<SortClause>)userOrderBy).get(0);
					if (elemOrder.getItem().equalsIgnoreCase("id")) orderByAllowed=true;
				}

				if (!orderByAllowed) throw new SDPOrderBySizeException("too many documents for order clause;",Locale.UK);


				for (int kkk=0;kkk<((ArrayList<String>)userOrderBy).size();kkk++) {
					if (null==orderSolr) orderSolr=new ArrayList<SortClause>();
					//yucca-1080
					SortClause cc=((ArrayList<SortClause>)userOrderBy).get(kkk);
					orderSolr.add(new SortClause(cc.getItem().toLowerCase(),cc.getOrder()));
					//orderSolr.add(((ArrayList<SortClause>)userOrderBy).get(kkk));
				}

				starTtime=System.currentTimeMillis();
				//cursor = collMisure.find(query).sort(dbObjUserOrder).skip(skip).limit(limit);
				try {
					deltaTime=System.currentTimeMillis()-starTtime;
				} catch (Exception e) {}
			}
			else {
				starTtime=System.currentTimeMillis();
				//cursor = collMisure.find(query).skip(skip).limit(limit);
				try {
					deltaTime=System.currentTimeMillis()-starTtime;
				} catch (Exception e) {}

			}

			solrQuery = new SolrQuery();
			solrQuery.setQuery("*:*");
			solrQuery.setFilterQueries(queryTotSolr);
			solrQuery.setRows(new Integer( new Long(limitL).intValue()));			
			solrQuery.setStart(new Integer( new Long(skipL).intValue()));			
			if (null!=orderSolr) solrQuery.setSorts(orderSolr);

			starTtime=System.currentTimeMillis();
			rsp = solrServer.query(collection,solrQuery);
			SolrDocumentList results = rsp.getResults();

			try {
				deltaTime=System.currentTimeMillis()-starTtime;
			} catch (Exception e) {}
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] total data query executed in --> "+deltaTime);
			//log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] count --> "+cursor.count());
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] orderby ="+orderSolr);
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] limit --> "+limitL);
			log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] skip --> "+skipL);

			SolrDocument curSolrDoc=null;
			try {
				for (int j = 0; j < results.size(); ++j) {
					curSolrDoc=results.get(j);


					String internalID=curSolrDoc.get("id").toString();
					//String datasetVersion=takeNvlValues(curSolrDoc.get("datasetVersion_l"));
					String datasetVersion=Util.takeNvlValues(curSolrDoc.get("datasetversion_l"));
					Map<String, Object> misura = new HashMap<String, Object>();
					misura.put("internalId",  internalID);

					if (DATA_TYPE_MEASURE.equals(datatType) || DATA_TYPE_SOCIAL.equals(datatType)) {
						String streamId=curSolrDoc.get("streamcode_s").toString();
						String sensorId=curSolrDoc.get("sensor_s").toString();
						misura.put("streamCode", streamId);
						misura.put("sensor", sensorId);


						java.util.Date sddd=(java.util.Date)curSolrDoc.get("time_dt");

						misura.put("time", sddd );
					}					
					//String iddataset=takeNvlValues(curSolrDoc.get("idDataset_l"));
					String iddataset=Util.takeNvlValues(curSolrDoc.get("iddataset_l"));
					if (null!= iddataset ) misura.put("idDataset",  Integer.parseInt(iddataset));
					if (null!= datasetVersion ) misura.put("datasetVersion",  Integer.parseInt(datasetVersion));


					ArrayList<String> elencoBinaryId=new ArrayList<String>();
					for (int i=0;i<compPropsTot.size();i++) {

						String chiave=compPropsTot.get(i).getName();
						String chiaveL=Util.getPropertyName(compPropsTot.get(i));

						chiaveL=compPropsTot.get(i).getName()+SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(
								campoTipoMetadato.get(compPropsTot.get(i).getName()));



						//
						//						if (curSolrDoc.keySet().contains(chiaveL) ) {
						//							Object oo = curSolrDoc.get(chiaveL);
						//
						//							String  valore=takeNvlValues(curSolrDoc.get(chiaveL));
						//							
						if (curSolrDoc.keySet().contains(chiaveL.toLowerCase()) ) {
							Object oo = curSolrDoc.get(chiaveL.toLowerCase());

							String  valore=Util.takeNvlValues(curSolrDoc.get(chiaveL.toLowerCase()));							
							if (null!=valore) {
								if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Boolean)) {
									misura.put(chiave, Boolean.valueOf(valore));
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.String)) {
									misura.put(chiave, valore);
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Int32)) {
									misura.put(chiave, Integer.parseInt(valore));
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Int64)) {
									misura.put(chiave, Long.parseLong(valore));
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Double)) {
									misura.put(chiave, Double.parseDouble(valore));
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.DateTimeOffset)) {
									//Object dataObj=obj.get(chiave);
									java.util.Date dtSolr=(java.util.Date)oo; 
									misura.put(chiave, dtSolr);
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.DateTime)) {
//									//Sun Oct 19 07:01:17 CET 1969  TODO chiedere a Fabrizio
//									//EEE MMM dd HH:mm:ss zzz yyyy
//									Object dataObj=obj.get(chiave);// ?
//									//System.out.println("------------------------------"+dataObj.getClass().getName());
//									misura.put(chiave, dataObj);
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Decimal)) {
									//comppnenti.put(chiave, Float.parseFloat(valore));
									misura.put(chiave, Double.parseDouble(valore));
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Binary)) {
									Map<String, Object> mappaBinaryRef=new HashMap<String, Object>();
									mappaBinaryRef.put("idBinary", (String)valore);
									misura.put(chiave, mappaBinaryRef);
									elencoBinaryId.add((String)valore);

								}
							}
						} else {
							String a = "bb";
							String b= a;
						}
					}					
					if (elencoBinaryId.size()>0) misura.put("____binaryIdsArray", elencoBinaryId);					



					ret.add(misura);
				}


				try {
					deltaTime=System.currentTimeMillis()-starTtime;
				} catch (Exception e) {}
				log.info("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] total fetch in --> "+deltaTime);


			} catch (Exception e) {
				throw e;
			}  finally {
				//cursor.close();			
			} 


		} catch (SDPOrderBySizeException e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] SDPOrderBySizeException",e);
			throw (SDPOrderBySizeException)e;
		} catch (SDPPageSizeException e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] SDPPageSizeException",e);
			throw (SDPPageSizeException)e;
		} catch (Exception e) {
			log.error("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] GenericException",e);
			log.error("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] INGORED" +e);
		} finally {
			log.debug("[SDPAdminApiOdataData::getMeasuresPerStreamNewLimitSolr] END");
		}


		SDPDataResult outres= new SDPDataResult(ret, cnt);
		return outres;
	}			


	public SDPDataResult getMeasuresStatsPerStreamPhoenix(
			String nameSpace, 
			EdmEntityContainer entityContainer,
			String internalId,
			String datatType,
			Object userQuery, 
			Object userOrderBy,
			int skip,
			int limit,
			String timeGroupByParam,
			String timeGroupOperatorsParam,
			Object groupOutQuery
			) throws ODataException{
		String collection=null;
		//		String sensore=null;
		//		String stream=null;
		String idDataset=null;
		String datasetCode=null;
		String datasetToFindVersion=null;
		List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();
		int cnt = 0;

		// TODO YUCCA-74 odata evoluzione
		Connection conn = null;
		initDbObject(codiceApi);

		try {
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] BEGIN");
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] nameSpace="+nameSpace);
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] entityContainer="+entityContainer);
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] internalId="+internalId);
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] datatType="+datatType);
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] userQuery="+userQuery);

			List<Property> compPropsTot=new ArrayList<Property>();
//			List<Property> compPropsCur=new ArrayList<Property>();			


			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero di collencion,host, port, db specifici per il dataset
			//       - modificare eventualmente la logica di recupero dell'idDataset
			//INVARIATO

			idDataset=configObject.getDettaglioStreamDatasetResponse().getDataset().getIddataset().toString();
			datasetCode=configObject.getDettaglioStreamDatasetResponse().getDataset().getDatasetcode();
			String streamSubtype=configObject.getDettaglioStreamDatasetResponse().getDataset().getDatasetSubtype().getDatasetSubtype();
			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * ATTENZIONE!!!!!! datasetVersion sara un array da mettere in in
			 */

			//idDataset=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("idDataset") );


			String schema = configObject.getDettaglioStreamDatasetResponse().getDataset().getPhoenixschemaname();
			String table = configObject.getDettaglioStreamDatasetResponse().getDataset().getPhoenixtablename();

			List<ComponentResponse> eleCapmpi= configObject.getDettaglioStreamDatasetResponse().getComponents();
			HashMap<String, String> campoTipoMetadato= new HashMap<String, String>();

			String campiPh=null;
			for (ComponentResponse componentResponse : eleCapmpi) {
				String nome=componentResponse.getName();
				String tipo=componentResponse.getDataType().getDatatypecode();
				campoTipoMetadato.put(nome, tipo);
				
//				if (campiPh == null) campiPh=nome+SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(tipo)+ " " + SDPDataApiConstants.SDP_DATATYPE_PHOENIXTYPES.get(tipo);
//				else campiPh+=","+nome+SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(tipo)+ " " + SDPDataApiConstants.SDP_DATATYPE_PHOENIXTYPES.get(tipo);
				String nomeTot="\""+(nome+SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(tipo)).toUpperCase()+"\""+ " " + SDPDataApiConstants.SDP_DATATYPE_PHOENIXTYPES.get(tipo);
				if (campiPh == null) campiPh=nomeTot;
				else campiPh+=","+nomeTot;
			}
			
			conn = DriverManager.getConnection(SDPDataApiConfig.getInstance().getPhoenixUrl());
			String queryBaseSolr="( iddataset_l = ? and datasetversion_l>=0 ) ";

			String sql = " FROM "+schema+"."+table+" ("+campiPh+")  WHERE  " + queryBaseSolr;
			if (null!=internalId) sql+=" AND (objectid=?)";
			if (null != userQuery) sql+=" AND ("+((SDPPhoenixExpression)userQuery).toString()+")";

			String groupby="";
			String groupbysleect="";

			if ("year".equals(timeGroupByParam)) {
				groupby = " YEAR(time_dt)";
				groupbysleect = " YEAR(time_dt) as year, -1 as month, -1 as dayofmonth, -1 as hour, -1 as minute, -1 as dayofweek ";
			} else if ("month_year".equals(timeGroupByParam)) {
				groupby = " YEAR(time_dt), MONTH(time_dt)";
				groupbysleect = " YEAR(time_dt) as year , MONTH(time_dt) as month, -1 as dayofmonth, -1 as hour, -1 as minute, -1 as dayofweek";
			} else if ("dayofmonth_month_year".equals(timeGroupByParam)) {
				groupby = " YEAR(time_dt), MONTH(time_dt), DAYOFMONTH(time_dt) ";
				groupbysleect = " YEAR(time_dt) as year, MONTH(time_dt) as month , DAYOFMONTH(time_dt) as dayofmonth ,  -1 as hour, -1 as minute, -1 as dayofweek";
			} else if ("hour_dayofmonth_month_year".equals(timeGroupByParam)) {
				groupby = " YEAR(time_dt), MONTH(time_dt), DAYOFMONTH(time_dt), HOUR(time_dt) ";
				groupbysleect = " YEAR(time_dt) as year, MONTH(time_dt) as month , DAYOFMONTH(time_dt) as dayofmonth, HOUR(time_dt) as hour ,  -1 as minute, -1 as dayofweek";
			} else if ("minute_hour_dayofmonth_month_year".equals(timeGroupByParam)) {
				groupby = " YEAR(time_dt), MONTH(time_dt), DAYOFMONTH(time_dt), HOUR(time_dt), MINUTE(time_dt)";
				groupbysleect = " YEAR(time_dt) as year, MONTH(time_dt) as month , DAYOFMONTH(time_dt) as dayofmonth, HOUR(time_dt) as hour , MINUTE(time_dt) as minute , -1 as dayofweek";
			} else if ("month".equals(timeGroupByParam)) {
				//YUCCA-388
				groupby = " MONTH(time_dt)";
				groupbysleect = " MONTH(time_dt) as month , -1 as year, -1 as dayofmonth, -1 as hour, -1 as minute, -1 as dayofweek ";
			} else if ("dayofmonth_month".equals(timeGroupByParam)) {
				groupby = " MONTH(time_dt), DAYOFMONTH(time_dt)";
				groupbysleect = " MONTH(time_dt) as month , DAYOFMONTH(time_dt) as dayfomonth  -1 as year, -1 as dayofmonth, -1 as hour, -1 as minute, -1 as dayofweek ";
			} else if ("dayofweek_month".equals(timeGroupByParam)) {
				//////groupby = " MONTH(time_dt), DAYOFMONTH(time_dt)";
				groupby = " MONTH(time_dt), DAYOFWEEK(time_dt)";
				groupbysleect = " MONTH(time_dt) as month , -1 as dayfomonth,  -1 as year, -1 as dayofmonth, -1 as hour, -1 as minute, DAYOFWEEK(time_dt) as dayofweek ";
			} else if ("dayofweek".equals(timeGroupByParam)) {
				groupby = " DAYOFWEEK(time_dt)";
				groupbysleect = " -1 as month , -1 as dayfomonth,  -1 as year, -1 as dayofmonth, -1 as hour, -1 as minute, DAYOFWEEK(time_dt) as dayofweek ";
			} else if ("hour_dayofweek".equals(timeGroupByParam)) {
				groupby = " DAYOFWEEK(time_dt),HOUR(time_dt)";
				groupbysleect = " -1 as month , -1 as dayfomonth,  -1 as year, -1 as dayofmonth, HOUR(time_dt) as hour, -1 as minute, DAYOFWEEK(time_dt) as dayofweek ";
			} else if ("hour".equals(timeGroupByParam)) {
				//YUCCA-388
				groupby = "  HOUR(time_dt) ";
				groupbysleect = "  HOUR(time_dt) as hour, -1 as year, -1 as month,  -1 as dayofmonth,  -1 as minute, -1 as dayofweek";
			} else if ("retweetparentid".equals(timeGroupByParam)) {
				if (!("socialDataset".equalsIgnoreCase(streamSubtype))) throw new SDPCustomQueryOptionException("invalid timeGroupBy value: retweetparentid aggregations is aveailable only for social dataset", Locale.UK);
				groupby = "  retweetParentId_l";
				groupbysleect = "  retweetParentId_l as retweetParentId, -1 as year, -1 as month, -1 as dayofmonth, -1 as hour, -1 as minute, -1 as dayofweek";
			} else if ("iduser".equals(timeGroupByParam)) {
				//YUCCA-388
				if (!("socialDataset".equalsIgnoreCase(streamSubtype))) throw new SDPCustomQueryOptionException("invalid timeGroupBy value: iduser aggregations is aveailable only for social dataset", Locale.UK);
				groupby = "  userId_l ";
				groupbysleect = "  userId_l as userId , -1 as year, -1 as month, -1 as dayofmonth, -1 as hour, -1 as minute, -1 as dayofweek";
			} else {
				throw new SDPCustomQueryOptionException("invalid timeGroupBy value", Locale.UK);
			}			

			if (groupbysleect.indexOf("userId_l")==-1 && "socialDataset".equalsIgnoreCase(streamSubtype)) {
				groupbysleect+=", -1 as userId_l";
			}
			if (groupbysleect.indexOf("retweetParentId_l")==-1 && "socialDataset".equalsIgnoreCase(streamSubtype)) {
				groupbysleect+=", -1 as retweetParentId_l";
			}


			//operazioni statistiche 
			if (null==timeGroupOperatorsParam || timeGroupOperatorsParam.trim().length()<=0) throw new SDPCustomQueryOptionException("invalid timeGroupOperators value", Locale.UK);
			StringTokenizer st=new StringTokenizer(timeGroupOperatorsParam,";",false);
			HashMap<String, String> campoOperazione=new HashMap<String, String>();
			while (st.hasMoreTokens()) {
				String curOperator=st.nextToken();
				StringTokenizer stDue=new StringTokenizer(curOperator,",",false);
				if (stDue.countTokens()!=2) throw new SDPCustomQueryOptionException("invalid timeGroupOperators value: '" + curOperator+"'", Locale.UK);
				String op=stDue.nextToken();
				String field=stDue.nextToken();
				if (!hasField(compPropsTot,field)) throw new SDPCustomQueryOptionException("invalid timeGroupOperators filed '"+field+"' in '" + curOperator +"' not fund in edm" , Locale.UK);
				String opPhoenix=null;
				boolean extraOp=false;
				if ("avg".equals(op)) opPhoenix="avg";
				else if ("first".equals(op)) {opPhoenix="FIRST_VALUE"; extraOp=true; }
				else if ("last".equals(op)) {opPhoenix="LAST_VALUE"; extraOp=true; }
				else if ("sum".equals(op)) opPhoenix="sum";
				else if ("max".equals(op)) opPhoenix="max";
				else if ("min".equals(op)) opPhoenix="min";
				else throw new SDPCustomQueryOptionException("invalid timeGroupOperators invalid operation '"+op+"' in '" + curOperator  +"'", Locale.UK);

				if (campoOperazione.containsKey(field)) throw new SDPCustomQueryOptionException("invalid timeGroupOperators filed '"+field+"' present in more than one operation" , Locale.UK);

				campoOperazione.put(field, opPhoenix);


				String campoCompleto=field+SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(campoTipoMetadato.get(field));
				campoCompleto="\""+campoCompleto.toUpperCase()+"\"";
				groupbysleect+=", "+opPhoenix + "(";
				groupbysleect+=campoCompleto;
				groupbysleect+= ")";
				if (extraOp) {
					groupbysleect+=  " WITHIN GROUP (ORDER BY "+campoCompleto+" asc) "; 
				}
				//groupbysleect+=  " as " + field +"_sts";
				groupbysleect+=  " as \"" + (field +"_sts").toUpperCase() +"\"";

			}			



			sql = groupbysleect+", count(1) as totale " +sql + " GROUP BY "+groupby; 





			sql = "select * from (select " + sql +")";

			if (null!=groupOutQuery) sql += " where "+((SDPPhoenixExpression)groupOutQuery).toString();

			if (null!=userOrderBy) sql += " ORDER BY " +  (String)userOrderBy;
			log.info("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] sqlPhoenix="+sql);

			int strtINdex=2;
			PreparedStatement stmt=conn.prepareStatement(sql);
			stmt.setInt(1, new Double(idDataset).intValue()); 
			if (null!=internalId) {
				stmt.setString(2, internalId);
				strtINdex=3;
			}
			if (null != userQuery) {
				for (int i =0;i<((SDPPhoenixExpression)userQuery).getParameters().size();i++) {
					Object curpar=((SDPPhoenixExpression)userQuery).getParameters().get(i);
					stmt.setObject(strtINdex, curpar);
					strtINdex++;
				}
			}
			if (null != groupOutQuery) {
				for (int i =0;i<((SDPPhoenixExpression)groupOutQuery).getParameters().size();i++) {
					Object curpar=((SDPPhoenixExpression)groupOutQuery).getParameters().get(i);

					stmt.setObject(strtINdex, curpar);
					strtINdex++;
				}
			}

			long starTtime=0;
			long deltaTime=-1;
			starTtime=System.currentTimeMillis();
			ResultSet rs=stmt.executeQuery();

			//Cursor cursor =collMisure.aggregate(pipeline,aggregationOptions);

			try {
				deltaTime=System.currentTimeMillis()-starTtime;
			} catch (Exception e) {}
			log.info("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] QUERY TIME ="+deltaTime);

			starTtime=System.currentTimeMillis();
			deltaTime=-1;

			int cntRet=1;
			cnt=0;

			while (rs.next()) {
				//System.out.println("num: "+cntRet+ "------------" +rs.getString("iddataset_l"));
				//DBObject obj=result;
				String giorno=rs.getString("dayofmonth");
				String mese=rs.getString("month");
				String anno=rs.getString("year");
				String ora=rs.getString("hour");
				//YUCCA-346
				String minuto=rs.getString("minute");


				//YUCCA-388
				String dayofweek=rs.getString("dayofweek");
				String retweetparentid=null;
				String iduser=null;
				if ("socialDataset".equalsIgnoreCase(streamSubtype)) {
					retweetparentid=rs.getString("retweetParentId_l");
					iduser=rs.getString("userId_l");
				}

				String count=rs.getString("totale");


				Integer dayOfweekInt=(dayofweek==null ? -1 : new Integer(dayofweek));
				if (dayOfweekInt > 0) {
					dayOfweekInt++;
					if (dayOfweekInt > 7) dayOfweekInt=1; 
				}

				Map<String, Object> misura = new HashMap<String, Object>();
				misura.put("dayofmonth",  (giorno==null ? -1 : new Integer(giorno)));
				misura.put("month",  (mese==null ? -1 : new Integer(mese)));
				misura.put("year",  (anno==null ? -1 : new Integer(anno)));
				misura.put("hour",  (ora==null ? -1 : new Integer(ora)));
				//YUCCA-346
				misura.put("minute",  (minuto==null ? -1 : new Integer(minuto)));
				//YUCCA-388
				misura.put("dayofweek", dayOfweekInt );
				//TODO solo se e' social
				misura.put("retweetparentid",  (retweetparentid==null ? -1 : new Long(retweetparentid)));
				misura.put("iduser",  (iduser==null ? -1 : new Long(iduser)));
				misura.put("count",  (count==null ? 0 : new Integer(count)));
				for (int i=0;i<compPropsTot.size();i++) {
					String chiave=compPropsTot.get(i).getName();
					String chiaveEdm=chiave+"_sts";
					if (campoOperazione.get(chiave)!=null) {
						String valore=rs.getString(chiaveEdm);
						if (null!=valore) {
							if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Boolean)) {
								misura.put(chiaveEdm, Boolean.valueOf(valore));
							} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.String)) {
								misura.put(chiaveEdm, valore);
							} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Int32)) {
								//misura.put(chiaveEdm, Integer.parseInt(valore));
								misura.put(chiaveEdm, rs.getInt(chiaveEdm));
							} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Int64)) {
								//misura.put(chiaveEdm, Long.parseLong(valore.replace(',','.')));
								misura.put(chiaveEdm, rs.getLong(chiaveEdm));
							} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Double)) {
								//misura.put(chiaveEdm, Double.parseDouble(valore.replace(',','.')));
								misura.put(chiaveEdm, rs.getDouble(chiaveEdm));
							} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.DateTimeOffset)) {
								//								Object dataObj=obj.get(chiave);
								//								misura.put(chiave, dataObj);
							} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.DateTime)) {
								//								Object dataObj=obj.get(chiave);
								//								misura.put(chiave, dataObj);
							} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Decimal)) {
								//misura.put(chiaveEdm, Double.parseDouble(valore.replace(',','.')));
								misura.put(chiaveEdm, rs.getDouble(chiaveEdm));

							}					
						}

					}
				}
				cnt++;
				ret.add(misura);
			}

			try {
				deltaTime=System.currentTimeMillis()-starTtime;
			} catch (Exception e) {}
			log.info("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] FETCH TIME ="+deltaTime);


		} catch (Exception e) {
			if (e instanceof SDPCustomQueryOptionException) {
				log.error("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] rethrow" ,e);
				throw (SDPCustomQueryOptionException) e;
			} else log.error("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] INGORED" ,e);
		} finally {
			log.debug("[SDPAdminApiOdataData::getMeasuresStatsPerStreamPhoenix] END");
		}


		SDPDataResult outres= new SDPDataResult(ret, cnt);
		return outres;
	}		

	private boolean hasField(List<Property> compPropsTot,String fieldName) {
		for (int i=0;compPropsTot!=null && i<compPropsTot.size();i++) {
			String chiave=compPropsTot.get(i).getName();
			if (chiave!= null && chiave.equals(fieldName))  return true;
		}
		return false;
	}



	public SDPDataResult getBinary(String nameSpace, EdmEntityContainer entityContainer,String internalId,String datatType,Object userQuery, Object userOrderBy,
			ArrayList<String> elencoIdBinary,
			String codiceApi,
			int skipI,
			int limitI
			) throws ODataException {

		// TODO YUCCA-74 odata evoluzione
		// potrebbe no nsubire modifiche verificare solo info.binaryIdDataset e info.binaryDatasetVersion in base a come viene modificato streamMetadata

		initDbObject(codiceApi);
		
		String collection=null;
		List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();
		long cnt = -1;
		long skipL=skipI;
		long limitL=limitI;
		try {
			log.debug("[SDPAdminApiOdataData::getBinary] BEGIN");
			log.debug("[SDPAdminApiOdataData::getBinary] nameSpace="+nameSpace);
			log.debug("[SDPAdminApiOdataData::getBinary] entityContainer="+entityContainer);
			log.debug("[SDPAdminApiOdataData::getBinary] internalId="+internalId);
			log.debug("[SDPAdminApiOdataData::getBinary] datatType="+datatType);
			log.debug("[SDPAdminApiOdataData::getBinary] userQuery="+userQuery);

			
			collection=configObject.getDettaglioStreamDatasetResponse().getBinarydataset().getSolrcollectionname();

			Integer idDatasetBinary= configObject.getDettaglioStreamDatasetResponse().getBinarydataset().getIddataset();
			Integer binaryDatasetVersion= configObject.getDettaglioStreamDatasetResponse().getDataset().getDatasourceversionBinary();

			List<Property> compPropsTot=new ArrayList<Property>();

			compPropsTot.add(new SimpleProperty().setName("internalId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			compPropsTot.add(new SimpleProperty().setName("datasetVersion").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("idBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			compPropsTot.add(new SimpleProperty().setName("filenameBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("aliasNameBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("sizeBinary").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("contentTypeBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("urlDownloadBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("metadataBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));

			HashMap<String, String> campoTipoMetadato= new HashMap<String, String>();
			campoTipoMetadato.put("internalId", "id");
			campoTipoMetadato.put("datasetVersion", "datasetVersion_l");
			campoTipoMetadato.put("idDataset", "idDataset_l");
			campoTipoMetadato.put("idBinary", "idBinary_s");
			campoTipoMetadato.put("filenameBinary", "filenameBinary_s");
			campoTipoMetadato.put("aliasNameBinary", "aliasNameBinary_s");
			campoTipoMetadato.put("sizeBinary", "sizeBinary_l");
			campoTipoMetadato.put("contentTypeBinary", "contentTypeBinary_s");
			campoTipoMetadato.put("urlDownloadBinary", "urlDownloadBinary_s");
			campoTipoMetadato.put("metadataBinary", "metadataBinary_s");


			if (collection==null)  return null;

			String queryTotSolr="(iddataset_l:"+idDatasetBinary+" AND datasetversion_l : "+binaryDatasetVersion+" ";
			if (null!=internalId) {
				queryTotSolr += "AND id : "+internalId;
			}
			queryTotSolr += ")";
			if (null != userQuery) {
				log.debug("[SDPAdminApiOdataData::getBinary] userQuery="+userQuery);
				if (userQuery instanceof SDPOdataFilterExpression) {
					queryTotSolr += "AND ("+userQuery+")";
				} else {
					queryTotSolr += "AND "+userQuery;
				}
			}
			String inClause=null;
			for (int kki=0; null!=elencoIdBinary && kki<elencoIdBinary.size(); kki++ ) {
				if (inClause==null) inClause="("+ elencoIdBinary.get(kki);
				else inClause=" OR "+ elencoIdBinary.get(kki);
			}
			String  query = queryTotSolr;
			if (inClause!=null) query+= " AND (idbinary_s : " + inClause +"))";

			log.info("[SDPAdminApiOdataData::getBinary] total data query ="+query);
			if (skipL<0) skipL=0;
			if (limitL<0) limitL=SDPDataApiConfig.getInstance().getMaxDocumentPerPage();

			// per ordinamento su max TODO
			limitL=SDPDataApiConfig.getInstance().getMaxDocumentPerPage()+SDPDataApiConfig.getInstance().getMaxSkipPages();
			skipL=0;

			ArrayList<SortClause> orderSolr=null;

			for (int kkk=0;userOrderBy!=null && kkk<((ArrayList<String>)userOrderBy).size();kkk++) {
				if (null==orderSolr) orderSolr=new ArrayList<SortClause>();
				orderSolr.add(((ArrayList<SortClause>)userOrderBy).get(kkk));
			}
			SolrClient solrServer = server;
			
			SolrQuery solrQuery = new SolrQuery();
			solrQuery.setQuery("*:*");
			solrQuery.setFilterQueries(query);
			solrQuery.setRows(new Integer( new Long(limitL).intValue()));			
			solrQuery.setStart(new Integer( new Long(skipL).intValue()));			
			if (null!=orderSolr) solrQuery.setSorts(orderSolr);

			QueryResponse rsp = solrServer.query(collection,solrQuery);
			SolrDocumentList results = rsp.getResults();
			SolrDocument curSolrDoc=null;

			cnt = results.getNumFound();	

			try {
				for (int j = 0; j < results.size(); ++j) {
					curSolrDoc=results.get(j);
					String internalID=curSolrDoc.get("id").toString();
					String datasetVersion=Util.takeNvlValues(curSolrDoc.get("datasetversion_l"));
					Map<String, Object> misura = new HashMap<String, Object>();
					misura.put("internalId",  internalID);
					String iddataset=Util.takeNvlValues(curSolrDoc.get("iddataset_l"));
					if (null!= iddataset ) misura.put("idDataset",  Integer.parseInt(iddataset));
					if (null!= datasetVersion ) misura.put("datasetVersion",  Integer.parseInt(datasetVersion));


					for (int i=0;i<compPropsTot.size();i++) {

						String chiave=compPropsTot.get(i).getName();
						String chiaveL=Util.getPropertyName(compPropsTot.get(i));

						chiaveL=campoTipoMetadato.get(compPropsTot.get(i).getName());




						if (curSolrDoc.keySet().contains(chiaveL.toLowerCase()) ) {
							Object oo = curSolrDoc.get(chiaveL.toLowerCase());

							String  valore=Util.takeNvlValues(curSolrDoc.get(chiaveL.toLowerCase()));
							if (null!=valore) {
								if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Boolean)) {
									misura.put(chiave, Boolean.valueOf(valore));
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.String)) {
									misura.put(chiave, valore);
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Int32)) {
									misura.put(chiave, Integer.parseInt(valore));
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Int64)) {
									misura.put(chiave, Long.parseLong(valore));
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Double)) {
									misura.put(chiave, Double.parseDouble(valore));
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.DateTimeOffset)) {
									//Object dataObj=obj.get(chiave);
									java.util.Date dtSolr=(java.util.Date)oo; 
									misura.put(chiave, dtSolr);
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.DateTime)) {
									//Sun Oct 19 07:01:17 CET 1969
									//EEE MMM dd HH:mm:ss zzz yyyy
									//Object dataObj=obj.get(chiave);
									// TODO ??? chiedere a Fabrizio
									//System.out.println("------------------------------"+dataObj.getClass().getName());
									//misura.put(chiave, dataObj);
									//																 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
									//															     Date data = dateFormat.parse(valore);								
									//																	misura.put(chiave, data);
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Decimal)) {
									//comppnenti.put(chiave, Float.parseFloat(valore));
									misura.put(chiave, Double.parseDouble(valore));
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Binary)) {
									Map<String, Object> mappaBinaryRef=new HashMap<String, Object>();
									mappaBinaryRef.put("idBinary", (String)valore);
									misura.put(chiave, mappaBinaryRef);
								}
							}
						} else {
							String a = "bb";
							String b= a;
						}
					}					
					String path="/api/"+codiceApi+"/attachment/"+idDatasetBinary+"/"+binaryDatasetVersion+"/"+misura.get("idBinary");
					misura.put("urlDownloadBinary", path);
					ret.add(misura);
				}

				log.info("[SDPAdminApiOdataData::getBinary] total fetch in --> nodata");

			} catch (Exception e) {
				throw e;
			}  finally {
				//cursor.close();			
			}
		} catch (Exception e) {
			log.error("[SDPAdminApiOdataData::getBinary] INGORED" +e);
		} finally {
			log.debug("[SDPAdminApiOdataData::getBinary] END");
		}


		SDPDataResult outres= new SDPDataResult(ret, cnt);
		return outres;
	}		



}