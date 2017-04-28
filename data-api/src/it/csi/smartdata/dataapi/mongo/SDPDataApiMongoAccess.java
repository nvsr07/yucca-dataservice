package it.csi.smartdata.dataapi.mongo;



import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;
import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;
import it.csi.smartdata.dataapi.mongo.dto.DbConfDto;
import it.csi.smartdata.dataapi.mongo.dto.SDPDataResult;
import it.csi.smartdata.dataapi.mongo.dto.SDPMongoOrderElement;
import it.csi.smartdata.dataapi.mongo.exception.SDPCustomQueryOptionException;
import it.csi.smartdata.dataapi.mongo.exception.SDPOrderBySizeException;
import it.csi.smartdata.dataapi.mongo.exception.SDPPageSizeException;
import it.csi.smartdata.dataapi.odata.SDPOdataFilterExpression;
import it.csi.smartdata.dataapi.odata.SDPPhoenixExpression;
import it.csi.smartdata.dataapi.solr.CloudSolrSingleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.provider.Facets;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;
import org.bson.types.ObjectId;

import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class SDPDataApiMongoAccess {

	static Logger log = Logger.getLogger(SDPDataApiMongoAccess.class.getPackage().getName());

	private ArrayList<DBObject> configObject=null;
	private String codiceApi=null;


	public static final String DATA_TYPE_MEASURE="measures";
	public static final String DATA_TYPE_DATA="data";
	public static final String DATA_TYPE_SOCIAL="social";

	public SDPDataApiMongoAccess (String codiceApi) {
		this.initConfDbObject(codiceApi);
	}

	public SDPDataApiMongoAccess () {
	}



	private MongoClient getMongoClient (String host,int port) throws Exception{
		//		ServerAddress serverAddr=new ServerAddress(host,port);
		//		MongoClient mongoClient = null;
		//		if (SDPDataApiConfig.getInstance().getMongoDefaultPassword()!=null && SDPDataApiConfig.getInstance().getMongoDefaultPassword().trim().length()>0 && 
		//				SDPDataApiConfig.getInstance().getMongoDefaultUser()!=null && SDPDataApiConfig.getInstance().getMongoDefaultUser().trim().length()>0	) {
		//			MongoCredential credential = MongoCredential.createMongoCRCredential(SDPDataApiConfig.getInstance().getMongoDefaultUser(), 
		//					"admin", 
		//					SDPDataApiConfig.getInstance().getMongoDefaultPassword().toCharArray());
		//			mongoClient = new MongoClient(serverAddr,Arrays.asList(credential));
		//		} else {
		//			mongoClient = new MongoClient(serverAddr);
		//		}
		//		return mongoClient;


		return MongoTenantDbSingleton.getInstance().getMongoClient(host, port);

	}



	public ArrayList<DBObject> initConfDbObject(String codiceApi) {
		try {
			log.debug("[SDPDataApiMongoAccess::initConfDbObject] BEGIN");
			log.debug("[SDPDataApiMongoAccess::initConfDbObject] codiceApi="+codiceApi);



			if (null==configObject || !codiceApi.equals(this.codiceApi)) {
				this.codiceApi=codiceApi;
				configObject=new ArrayList<DBObject>();
				//				MongoClient mongoClient = new MongoClient(
				//						SDPDataApiConfig.getInstance().getMongoCfgHost(SDPDataApiConfig.MONGO_DB_CFG_API), 
				//						SDPDataApiConfig.getInstance().getMongoCfgPort(SDPDataApiConfig.MONGO_DB_CFG_API));

				MongoClient mongoClient = getMongoClient(SDPDataApiConfig.getInstance().getMongoCfgHost(SDPDataApiConfig.MONGO_DB_CFG_API), 
						SDPDataApiConfig.getInstance().getMongoCfgPort(SDPDataApiConfig.MONGO_DB_CFG_API));				

				DB db = mongoClient.getDB(SDPDataApiConfig.getInstance().getMongoCfgDB(SDPDataApiConfig.MONGO_DB_CFG_API));
				DBCollection coll = db.getCollection(SDPDataApiConfig.getInstance().getMongoCfgCollection(SDPDataApiConfig.MONGO_DB_CFG_API));

				//				BasicDBObject query = new BasicDBObject("configData.codiceApi",codiceApi);
				BasicDBObject query = new BasicDBObject("apiCode",codiceApi);

				log.debug("[SDPDataApiMongoAccess::initConfDbObject] API query--> "+query);
				DBCursor cursor = coll.find(query);
				//DBCursor cursor = coll.find();
				try {
					while (cursor.hasNext()) {

						DBObject obj=cursor.next();
						configObject.add(obj);
						log.debug("[SDPDataApiMongoAccess::initConfDbObject] Result objc fount --> "+obj);
					}				
				} finally {
					cursor.close();
				}

				for (int i=0;i<configObject.size();i++) {
					DBObject obj=this.configObject.get(i);
					String type=((DBObject)obj.get("configData")).get("type").toString();
					String subType=((DBObject)obj.get("configData")).get("subtype").toString();




					if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && 
							(SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISTREAM.equals(subType) ||
									SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISOCIAL.equals(subType)
									)) {

						//BasicDBList objStreams = (BasicDBList)((DBObject)obj.get("streams")).get("stream");
						BasicDBList objStreams = (BasicDBList)obj.get("dataset");
						BasicDBList queryStreams=createQueryStreamPerApi(objStreams);

						HashMap<String , Object> returnData=getMergedStreamComponentsPerQueryString(queryStreams);

						BasicDBList compPropsTot = (null!=returnData.get("mergedComponents") ? (BasicDBList) returnData.get("mergedComponents"): null);
						String binaryIdDataset=(null!=returnData.get("binaryIdDataset") ? (String) returnData.get("binaryIdDataset"): null);;
						((DBObject)obj).put("mergedComponents", compPropsTot);
						((DBObject)obj).put("binaryIdDataset", binaryIdDataset);
					} else 	if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTIBULK.equals(subType)) {
						BasicDBList objStreams = (BasicDBList)obj.get("dataset");
						BasicDBList queryStreams=createQueryStreamPerApi(objStreams);
						HashMap<String , Object> returnData=getMergedStreamComponentsPerQueryString(queryStreams);
						BasicDBList compPropsTot = (null!=returnData.get("mergedComponents") ? (BasicDBList) returnData.get("mergedComponents"): null);
						String binaryIdDataset=(null!=returnData.get("binaryIdDataset") ? (String) returnData.get("binaryIdDataset"): null);;
						((DBObject)obj).put("mergedComponents", compPropsTot);
						((DBObject)obj).put("binaryIdDataset", binaryIdDataset);

					}
				}

			}
		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::initConfDbObject] INGORED" +e);
		} finally {
			log.debug("[SDPDataApiMongoAccess::initConfDbObject] END");

		}

		return this.configObject;
	}	


	public HashMap<String, Object>  getMergedStreamComponentsPerQueryString(BasicDBList queryStreams ) {
		List<Property> compPropsTot=new ArrayList<Property>();
		BasicDBList ret2=new BasicDBList();
		HashMap<String , Object> returnData=new HashMap<String , Object> ();

		try {

			log.debug("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] BEGIN");
			log.info("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] queryStreams="+queryStreams);


			BasicDBObject query =null;
			DBCursor cursor=null;

			//			MongoClient mongoClient = new MongoClient(
			//					SDPDataApiConfig.getInstance().getMongoCfgHost(SDPDataApiConfig.MONGO_DB_CFG_DATASET), 
			//					SDPDataApiConfig.getInstance().getMongoCfgPort(SDPDataApiConfig.MONGO_DB_CFG_DATASET));

			MongoClient mongoClient = getMongoClient(SDPDataApiConfig.getInstance().getMongoCfgHost(SDPDataApiConfig.MONGO_DB_CFG_DATASET), 
					SDPDataApiConfig.getInstance().getMongoCfgPort(SDPDataApiConfig.MONGO_DB_CFG_DATASET));				


			DB db = mongoClient.getDB(SDPDataApiConfig.getInstance().getMongoCfgDB(SDPDataApiConfig.MONGO_DB_CFG_DATASET));
			DBCollection coll = db.getCollection(SDPDataApiConfig.getInstance().getMongoCfgCollection(SDPDataApiConfig.MONGO_DB_CFG_DATASET));


			query = new BasicDBObject("$or", queryStreams);
			log.info("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] mongo query="+query);

			//YUCCA-264 recuperare i metadati comuni a tutte le cersioni dalla current
			BasicDBObject order= new BasicDBObject("configData.current",-1);
			cursor = coll.find(query).sort(order);
			//cursor = coll.find(query);
			String binaryIdDataset=null;
			try {
				List<Property> compProps=new ArrayList<Property>();
				BasicDBList campiDbList=null;
				while (cursor.hasNext()) {
					DBObject obj=cursor.next();
					//					Object eleCapmpi=((BasicDBObject)obj.get("dataset")).get("fields");				
					Object eleCapmpi=((BasicDBObject)obj.get("info")).get("fields");				

					try {
						binaryIdDataset=takeNvlValues(    ((BasicDBObject)obj.get("info")).get("binaryIdDataset"));
					} catch (Exception e) {
						binaryIdDataset=null;
					}					

					campiDbList= getDatasetFiledsDbList(eleCapmpi);

					log.debug("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] current dataset="+obj);
					log.debug("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] current dataset campiDbList="+campiDbList);


					for (int k=0;k<campiDbList.size();k++) {
						boolean present=false;
						compProps=getDatasetFiledsOdataPros(campiDbList.get(k));
						for (int i=0;i<compPropsTot.size();i++) {
							if (compPropsTot.get(i).getName().equals(compProps.get(0).getName())) present=true;

						}
						if (!present) {
							compPropsTot.add(compProps.get(0));
							log.debug("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString]        filed added="+campiDbList.get(k));

							ret2.add(campiDbList.get(k));
						}
					}


				}



			}finally {
				cursor.close();
			}


			returnData.put("binaryIdDataset", binaryIdDataset);
			returnData.put("mergedComponents", ret2);




		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] INGORED" +e);

		} finally {
			log.debug("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] END");

		}
		return returnData;

	}		


	public BasicDBList  getMergedStreamComponentsPerQueryString_orig(BasicDBList queryStreams ) {
		List<Property> compPropsTot=new ArrayList<Property>();
		BasicDBList ret2=new BasicDBList();
		try {

			log.debug("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] BEGIN");
			log.info("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] queryStreams="+queryStreams);


			BasicDBObject query =null;
			DBCursor cursor=null;

			//			MongoClient mongoClient = new MongoClient(
			//					SDPDataApiConfig.getInstance().getMongoCfgHost(SDPDataApiConfig.MONGO_DB_CFG_DATASET), 
			//					SDPDataApiConfig.getInstance().getMongoCfgPort(SDPDataApiConfig.MONGO_DB_CFG_DATASET));

			MongoClient mongoClient = getMongoClient(SDPDataApiConfig.getInstance().getMongoCfgHost(SDPDataApiConfig.MONGO_DB_CFG_DATASET), 
					SDPDataApiConfig.getInstance().getMongoCfgPort(SDPDataApiConfig.MONGO_DB_CFG_DATASET));				


			DB db = mongoClient.getDB(SDPDataApiConfig.getInstance().getMongoCfgDB(SDPDataApiConfig.MONGO_DB_CFG_DATASET));
			DBCollection coll = db.getCollection(SDPDataApiConfig.getInstance().getMongoCfgCollection(SDPDataApiConfig.MONGO_DB_CFG_DATASET));


			query = new BasicDBObject("$or", queryStreams);
			log.info("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] mongo query="+query);

			//YUCCA-264 recuperare i metadati comuni a tutte le cersioni dalla current
			BasicDBObject order= new BasicDBObject("configData.current",-1);
			cursor = coll.find(query).sort(order);
			//cursor = coll.find(query);
			try {
				List<Property> compProps=new ArrayList<Property>();
				BasicDBList campiDbList=null;
				while (cursor.hasNext()) {
					DBObject obj=cursor.next();
					//					Object eleCapmpi=((BasicDBObject)obj.get("dataset")).get("fields");				
					Object eleCapmpi=((BasicDBObject)obj.get("info")).get("fields");				

					campiDbList= getDatasetFiledsDbList(eleCapmpi);

					log.debug("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] current dataset="+obj);
					log.debug("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] current dataset campiDbList="+campiDbList);


					for (int k=0;k<campiDbList.size();k++) {
						boolean present=false;
						compProps=getDatasetFiledsOdataPros(campiDbList.get(k));
						for (int i=0;i<compPropsTot.size();i++) {
							if (compPropsTot.get(i).getName().equals(compProps.get(0).getName())) present=true;

						}
						if (!present) {
							compPropsTot.add(compProps.get(0));
							log.debug("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString]        filed added="+campiDbList.get(k));

							ret2.add(campiDbList.get(k));
						}
					}


				}



			}finally {
				cursor.close();
			}



		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] INGORED" +e);

		} finally {
			log.debug("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] END");

		}
		return ret2;

	}	


	private BasicDBList getQueryStreamPerApi(String codiceApi) {
		BasicDBList queryStreams=new BasicDBList();
		try {
			log.debug("[SDPDataApiMongoAccess::getQueryStreamPerApi] BEGIN");
			log.info("[SDPDataApiMongoAccess::getQueryStreamPerApi] codiceApi="+codiceApi);



			//			MongoClient mongoClient = new MongoClient(
			//					SDPDataApiConfig.getInstance().getMongoCfgHost(SDPDataApiConfig.MONGO_DB_CFG_API), 
			//					SDPDataApiConfig.getInstance().getMongoCfgPort(SDPDataApiConfig.MONGO_DB_CFG_API));

			MongoClient mongoClient = getMongoClient(SDPDataApiConfig.getInstance().getMongoCfgHost(SDPDataApiConfig.MONGO_DB_CFG_API), 
					SDPDataApiConfig.getInstance().getMongoCfgPort(SDPDataApiConfig.MONGO_DB_CFG_API));				


			DB db = mongoClient.getDB(SDPDataApiConfig.getInstance().getMongoCfgDB(SDPDataApiConfig.MONGO_DB_CFG_API));
			DBCollection coll = db.getCollection(SDPDataApiConfig.getInstance().getMongoCfgCollection(SDPDataApiConfig.MONGO_DB_CFG_API));

			//			BasicDBObject query = new BasicDBObject("configData.codiceApi",codiceApi);
			BasicDBObject query = new BasicDBObject("apiCode",codiceApi);



			log.info("[SDPDataApiMongoAccess::getQueryStreamPerApi] query="+query);
			DBCursor cursor = coll.find(query);

			try {
				while (cursor.hasNext()) {
					DBObject obj=cursor.next();
					String type=((DBObject)obj.get("configData")).get("type").toString();
					String subType=((DBObject)obj.get("configData")).get("subtype").toString();


					if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && ( 
							SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISTREAM.equals(subType)
							|| SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISOCIAL.equals(subType))
							){
						//BasicDBList objStreams = (BasicDBList)((DBObject)obj.get("streams")).get("stream");

						BasicDBList objDataset = (BasicDBList)obj.get("dataset");



						queryStreams.addAll(createQueryStreamPerApi(objDataset));
					} else 					if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTIBULK.equals(subType)) {
						//BasicDBList objStreams = (BasicDBList)((DBObject)obj.get("streams")).get("stream");

						BasicDBList objDataset = (BasicDBList)obj.get("dataset");



						queryStreams.addAll(createQueryStreamPerApi(objDataset));
					}					


				}
			}finally {
				cursor.close();
			}
			log.info("[SDPDataApiMongoAccess::getQueryStreamPerApi] queryStreamsResult="+queryStreams);


		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::getQueryStreamPerApi] INGORED" +e);
		} finally {
			log.debug("[SDPDataApiMongoAccess::getQueryStreamPerApi] END");

		}	
		return queryStreams;
	}


	private List<Property> getDatasetFiledsOdataPros(Object eleCapmpi) {

		List<Property> propOut=new ArrayList<Property>();

		BasicDBList lista=null;
		if (eleCapmpi instanceof BasicDBList) {
			lista=(BasicDBList)eleCapmpi;
		} else {
			lista=new BasicDBList();
			lista.add(eleCapmpi);
		}

		for (int i=0;i<lista.size();i++) {
			DBObject elemento=(DBObject)lista.get(i);
			Set<String> chivi= elemento.keySet();


			String propName=null;
			String porpType=null;
			Iterator<String> itcomp=chivi.iterator();
			while (itcomp.hasNext()) {
				String chiaveCur=itcomp.next();
				String valor=takeNvlValues(elemento.get(chiaveCur));

				if (chiaveCur.equals("fieldName")) propName=valor;
				if (chiaveCur.equals("dataType")) porpType=valor;



			}
			propOut.add(new SimpleProperty().setName(propName).setType(SDPDataApiConstants.SDP_DATATYPE_MAP.get(porpType)).setFacets(new Facets().setNullable(false)));
		}

		return propOut;
	}




	/**
	 * Modificata per generico dataset
	 * @param objElencoStream
	 * @return
	 */
	private BasicDBList createQueryStreamPerApi(BasicDBList objElencoStream) {
		BasicDBList queryStreams=new BasicDBList();
		try {
			log.debug("[SDPDataApiMongoAccess::createQueryStreamPerApi] BEGIN");
			log.debug("[SDPDataApiMongoAccess::createQueryStreamPerApi] objElencoStream="+objElencoStream);



			for (int k=0;k<objElencoStream.size();k++) {
				String codiceTenant=((DBObject)objElencoStream.get(k)).get("tenantCode").toString();
				String stream=takeNvlValues(((DBObject)objElencoStream.get(k)).get("streamCode"));
				String sensore=takeNvlValues(((DBObject)objElencoStream.get(k)).get("virtualEntityCode"));
				String idDataset=((DBObject)objElencoStream.get(k)).get("idDataset").toString();


				//DBObject clause = new BasicDBObject("configData.tenant", codiceTenant).append("streams.stream.codiceStream", stream).append("streams.stream.codiceVirtualEntity",sensore).append("configData.type", "stream");

				//DBObject clause = new BasicDBObject("configData.idDataset", idDataset);
				//DBObject clause = new BasicDBObject("idDataset", new Integer(idDataset));
				DBObject clause = new BasicDBObject("idDataset", new Integer(new Double(idDataset).intValue()));

				queryStreams.add(clause);

			}
			log.info("[SDPDataApiMongoAccess::createQueryStreamPerApi] queryStreams="+queryStreams);

		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::createQueryStreamPerApi] INGORED" +e);
		} finally {
			log.debug("[SDPDataApiMongoAccess::createQueryStreamPerApi] END");
		}	
		return queryStreams;
	}


	private BasicDBList getDatasetFiledsDbList(Object eleCapmpi) {
		BasicDBList dblistout=new BasicDBList();
		try {

			BasicDBList lista=null;
			if (eleCapmpi instanceof BasicDBList) {
				lista=(BasicDBList)eleCapmpi;
			} else {
				lista=new BasicDBList();
				lista.add(eleCapmpi);
			}

			for (int i=0;i<lista.size();i++) {
				DBObject elemento=(DBObject)lista.get(i);
				dblistout.add(elemento);
				Set<String> chivi= elemento.keySet();


				String propName=null;
				String porpType=null;
				Iterator<String> itcomp=chivi.iterator();
				while (itcomp.hasNext()) {
					String chiaveCur=itcomp.next();
					String valor=takeNvlValues(elemento.get(chiaveCur));

					if (chiaveCur.equals("fieldName")) propName=valor;
					if (chiaveCur.equals("dataType")) porpType=valor;



				}
			}
		} catch (Exception e) {
			log.error("SDPDataApiMongoAccess.getDatasetFiledsDbList --> ERROR " + e);

		}
		return dblistout;
	}



	public List<DBObject> getDatasetPerApi(String codiceApi) {
		List<DBObject> ret= new ArrayList<DBObject>();

		try {
			log.debug("[SDPDataApiMongoAccess::getDatasetPerApi] BEGIN");
			log.debug("[SDPDataApiMongoAccess::getDatasetPerApi] codiceApi="+codiceApi);

			BasicDBList queryStreams=getQueryStreamPerApi(codiceApi);
			BasicDBObject query =null;
			DBCursor cursor=null;
			//			MongoClient mongoClient = new MongoClient("tst-sdnet-bgslave1.sdp.csi.it", 27017);
			//			DB db = mongoClient.getDB("smartlab");
			//			DBCollection coll = db.getCollection("configCollection01");
			//			MongoClient mongoClient = new MongoClient(
			//					SDPDataApiConfig.getInstance().getMongoCfgHost(SDPDataApiConfig.MONGO_DB_CFG_DATASET), 
			//					SDPDataApiConfig.getInstance().getMongoCfgPort(SDPDataApiConfig.MONGO_DB_CFG_DATASET));
			MongoClient mongoClient = getMongoClient(SDPDataApiConfig.getInstance().getMongoCfgHost(SDPDataApiConfig.MONGO_DB_CFG_DATASET), 
					SDPDataApiConfig.getInstance().getMongoCfgPort(SDPDataApiConfig.MONGO_DB_CFG_DATASET));				

			DB db = mongoClient.getDB(SDPDataApiConfig.getInstance().getMongoCfgDB(SDPDataApiConfig.MONGO_DB_CFG_DATASET));
			DBCollection coll = db.getCollection(SDPDataApiConfig.getInstance().getMongoCfgCollection(SDPDataApiConfig.MONGO_DB_CFG_DATASET));

			query = new BasicDBObject("$or", queryStreams);
			log.info("[SDPDataApiMongoAccess::getDatasetPerApi] query="+query);


			//YUCCA-264 recuperare i metadati comuni a tutte le cersioni dalla current
			//cursor = coll.find(query);
			BasicDBObject order= new BasicDBObject("configData.current",-1);
			cursor = coll.find(query).sort(order);
			try {
				while (cursor.hasNext()) {
					DBObject obj=cursor.next();
					log.debug("[SDPDataApiMongoAccess::getDatasetPerApi] objfound added="+obj);
					ret.add(obj);
				}

			}finally {
				cursor.close();
			}
			log.debug("[SDPDataApiMongoAccess::getDatasetPerApi] ret="+ret);

		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::getDatasetPerApi] INGORED" +e);
		} finally {
			log.debug("[SDPDataApiMongoAccess::getDatasetPerApi] END");
		}

		return ret;		
	}

	private String takeNvlValues(Object obj) {
		if (null==obj) return null;
		else return obj.toString();
	}

	//TODO ... rinominare 
	//TODO gestire eccezioni
	public SDPDataResult getMeasuresPerStream(String codiceTenant, String nameSpace, EdmEntityContainer entityContainer,DBObject streamMetadata,String internalId,String datatType,Object userQuery, Object userOrderBy,
			int skip,
			int limit
			) throws SDPOrderBySizeException {
		String collection=null;
		//		String sensore=null;
		//		String stream=null;
		String idDataset=null;
		String datasetCode=null;
		String datasetToFindVersion=null;
		List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();
		int cnt = 0;


		// TODO YUCCA-74 odata evoluzione

		try {
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] BEGIN");
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] codiceTenant="+codiceTenant);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] nameSpace="+nameSpace);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] entityContainer="+entityContainer);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] internalId="+internalId);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] datatType="+datatType);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] userQuery="+userQuery);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] streamMetadata="+streamMetadata);

			List<Property> compPropsTot=new ArrayList<Property>();
			List<Property> compPropsCur=new ArrayList<Property>();			

			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] limit_init --> "+limit);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] skip_init --> "+skip);

			int limit_init=limit;
			int skip_init=skip;


			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero di collencion,host, port, db specifici per il dataset
			//       - modificare eventualmente la logica di recupero dell'idDataset
			//INVARIATO!!

			collection=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("collection") );
			String host=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("host"));
			String port =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("port") );
			String dbcfg =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("db") );
			idDataset=takeNvlValues(streamMetadata.get("idDataset"));
			datasetCode=takeNvlValues(streamMetadata.get("datasetCode"));

			String streamSubtype=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("subtype") );


			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * ATTENZIONE!!!!!! datasetVersion sarà un array da mettere in in
			 */

			datasetToFindVersion=takeNvlValues(streamMetadata.get("datasetVersion"));
			//idDataset=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("idDataset") );

			if (null==collection || collection.trim().length()<=0) {
				DbConfDto tanantDbCfg=new DbConfDto();

				if (DATA_TYPE_MEASURE.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_MESURES, codiceTenant);
				} else if (DATA_TYPE_DATA.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_DATA, codiceTenant);
				}  
				collection=tanantDbCfg.getCollection();
				host=tanantDbCfg.getHost();
				port=""+tanantDbCfg.getPort();
				dbcfg=tanantDbCfg.getDataBase();
			}

			if (null==dbcfg || dbcfg.trim().length()<=0) {
				DbConfDto tanantDbCfg=new DbConfDto();

				if (DATA_TYPE_MEASURE.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_MESURES, codiceTenant);
				} else if (DATA_TYPE_DATA.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_DATA, codiceTenant);
				}  
				dbcfg=tanantDbCfg.getDataBase();
			}


			host=SDPDataApiConfig.getInstance().getMongoDefaultHost();
			port=""+SDPDataApiConfig.getInstance().getMongoDefaultPort();

			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero dell'elenco dei campi che contiene il join di info.fuields di tutte le versioni di quel dataset
			Object eleCapmpi=((DBObject)streamMetadata.get("info")).get("fields");

			BasicDBList campiDbList= getDatasetFiledsDbList(eleCapmpi);
			for (int k=0;k<campiDbList.size();k++) {
				boolean present=false;
				compPropsCur=getDatasetFiledsOdataPros(campiDbList.get(k));
				for (int i=0;i<compPropsTot.size();i++) {
					if (compPropsTot.get(i).getName().equals(compPropsCur.get(0).getName())) present=true;

				}
				// TODO nel caso in cui present= true si potrebbe verficare il tipo che abbiamo in compsproptot con quello in  campiDbList.get(k)
				// sollevando eccezione se son diversi
				if (!present) {
					compPropsTot.add(compPropsCur.get(0));
				}
			}

			if (collection==null)  return null;

			DBCursor cursor=null;

			//MongoClient mongoClient = getMongoClient(host,Integer.parseInt(port));			
			//			DB db = mongoClient.getDB(dbcfg);
			//			MongoClient mongoClient = null;			
			//			DB db = null;
			DBCollection collMisure =null;
			BasicDBList queryTot=new BasicDBList();
			BasicDBList queryTotCnt=new BasicDBList();

			queryTot.add( new BasicDBObject("idDataset",new Integer(new Double(idDataset).intValue())));
			queryTotCnt.add( new BasicDBObject("idDataset",new Integer(new Double(idDataset).intValue())));

			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * ATTENZIONE!!!!!! datasetVersion sarà un array da mettere in in
			 * 
			 * la parte aggiunta a query tot per il dataset versione  deve essere  {datasetversion: {$in: [........ ] }}
			 */

			ArrayList<Integer> vals = new ArrayList<Integer>();

			List<DBObject> listDatasetVersion = (List<DBObject>) streamMetadata.get("listDatasetVersion");
			Iterator<DBObject> listaIterator = listDatasetVersion.iterator();
			while (listaIterator.hasNext()) {
				DBObject el = listaIterator.next();
				Integer tmpVersDSCode = (Integer) el.get("datasetVersion"+datasetCode);
				vals.add(tmpVersDSCode);
			}

			//queryTot.add( new BasicDBObject("datasetVersion",new Integer(new Double(datasetToFindVersion).intValue())));
			//queryTot.add( new BasicDBObject("datasetVersion", new BasicDBObject("$in", vals)));
			queryTot.add( new BasicDBObject("datasetVersion", new BasicDBObject("$gt", 0)));



			//BasicDBObject query = new BasicDBObject("idDataset",idDataset);
			if (null!=internalId) {
				//query.append("_id",new ObjectId(internalId));
				queryTot.add( new BasicDBObject("_id",new ObjectId(internalId)));
				queryTotCnt.add( new BasicDBObject("_id",new ObjectId(internalId)));

			}
			if (null != userQuery) {
				log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] userQuery="+userQuery);
				if (userQuery instanceof BasicDBList) {
					queryTot.addAll((BasicDBList)userQuery);
					queryTotCnt.addAll((BasicDBList)userQuery);
				} else if (userQuery instanceof BasicDBObject) {
					queryTot.add((BasicDBObject)userQuery);
					queryTotCnt.add((BasicDBObject)userQuery);
				}

				//query.append("$and", userQuery);
			}

			BasicDBObject query = new BasicDBObject("$and", queryTot);

			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] total data query ="+query);
			//cursor = collMisure.find(query);

			long starTtime=0;
			long deltaTime=-1;


			starTtime=System.currentTimeMillis();
			cnt = collMisure.find( new BasicDBObject("$and", queryTotCnt)).count();
			try {
				deltaTime=System.currentTimeMillis()-starTtime;
			} catch (Exception e) {}
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] total data query COUNT executed in --> "+deltaTime);


			starTtime=0;
			deltaTime=-1;


			if (skip<0) skip=0;
			if (limit<0) limit=SDPDataApiConfig.getInstance().getMaxDocumentPerPage();


			// per ordinamento su max 
			limit=SDPDataApiConfig.getInstance().getMaxDocumentPerPage()+SDPDataApiConfig.getInstance().getMaxSkipPages();
			skip=0;



			if (limit>cnt) limit=cnt;




			if (null!=userOrderBy) {

				boolean orderByAllowed=false;
				if (cnt<SDPDataApiConstants.SDP_MAX_DOC_FOR_ORDERBY) {
					orderByAllowed=true;
				} else if (DATA_TYPE_MEASURE.equals(datatType) && ((ArrayList<SDPMongoOrderElement>)userOrderBy).size()<=1) {

					SDPMongoOrderElement elemOrder=(SDPMongoOrderElement)((ArrayList<SDPMongoOrderElement>)userOrderBy).get(0);
					if (elemOrder.getNomeCampo().equalsIgnoreCase("time")) orderByAllowed=true;
					if (elemOrder.getNomeCampo().equalsIgnoreCase("retweetParentId") && ("socialDataset".equalsIgnoreCase(streamSubtype))) orderByAllowed=true;
					if (elemOrder.getNomeCampo().equalsIgnoreCase("userId") && ("socialDataset".equalsIgnoreCase(streamSubtype))) orderByAllowed=true;

				}


				if (!orderByAllowed) throw new SDPOrderBySizeException("too many documents for order clause;",Locale.UK);

				BasicDBObject dbObjUserOrder=null;

				for (int kkk=0;kkk<((ArrayList<SDPMongoOrderElement>)userOrderBy).size();kkk++) {
					SDPMongoOrderElement curOrdElem=(SDPMongoOrderElement)((ArrayList<SDPMongoOrderElement>)userOrderBy).get(kkk);
					if (null==dbObjUserOrder) dbObjUserOrder=new BasicDBObject(curOrdElem.getNomeCampo(),curOrdElem.getOrdine());
					else dbObjUserOrder.append(curOrdElem.getNomeCampo(),curOrdElem.getOrdine());
				}
				starTtime=System.currentTimeMillis();
				cursor = collMisure.find(query).sort(dbObjUserOrder).skip(skip).limit(limit);
				try {
					deltaTime=System.currentTimeMillis()-starTtime;
				} catch (Exception e) {}
			}
			else {
				starTtime=System.currentTimeMillis();
				cursor = collMisure.find(query).skip(skip).limit(limit);
				try {
					deltaTime=System.currentTimeMillis()-starTtime;
				} catch (Exception e) {}

			}
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] total data query executed in --> "+deltaTime);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] count --> "+cursor.count());
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] limit --> "+limit);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] skip --> "+skip);


			int [] limiti = checkPagesData((skip_init>=0 ? new Integer(skip_init): null) ,
					(limit_init>=0 ? new Integer(limit_init): null), cursor.count())	;		
			int startindex=limiti[0];
			int endindex=limiti[1];

			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] startindex --> "+startindex);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] endindex --> "+endindex);

			int recCount=0;
			DBObject obj=null;
			starTtime=System.currentTimeMillis();

			cursor.skip(startindex);

			try {
				while (cursor.hasNext() && recCount<(endindex-startindex)) {

					//log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] ciclo  recCount--> "+recCount);

					obj=cursor.next();


					if ((1==1) || (recCount>=startindex && recCount<endindex )) {
						log.info("[SDPDataApiMongoAccess::getMeasuresPerStream]       TAKEN recCount --> "+recCount);


						String internalID=obj.get("_id").toString();
						String datasetVersion=takeNvlValues(obj.get("datasetVersion"));
						//					String current=takeNvlValues(obj.get("current"));



						Map<String, Object> misura = new HashMap<String, Object>();
						misura.put("internalId",  internalID);

						if (DATA_TYPE_MEASURE.equals(datatType)) {
							String streamId=obj.get("streamCode").toString();
							String sensorId=obj.get("sensor").toString();
							misura.put("streamCode", streamId);
							misura.put("sensor", sensorId);
							misura.put("time",  obj.get("time"));
						}					


						String iddataset=takeNvlValues(obj.get("idDataset"));
						if (null!= iddataset ) misura.put("idDataset",  Integer.parseInt(iddataset));
						if (null!= datasetVersion ) misura.put("datasetVersion",  Integer.parseInt(datasetVersion));



						ArrayList<String> elencoBinaryId=new ArrayList<String>();
						for (int i=0;i<compPropsTot.size();i++) {

							String chiave=compPropsTot.get(i).getName();
							if (obj.keySet().contains(chiave) ) {
								String  valore=takeNvlValues(obj.get(chiave));
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
										Object dataObj=obj.get(chiave);
										misura.put(chiave, dataObj);
									} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.DateTime)) {
										//Sun Oct 19 07:01:17 CET 1969
										//EEE MMM dd HH:mm:ss zzz yyyy
										Object dataObj=obj.get(chiave);

										//System.out.println("------------------------------"+dataObj.getClass().getName());

										misura.put(chiave, dataObj);


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
										elencoBinaryId.add((String)valore);

									}
								}
							}
						}					
						if (elencoBinaryId.size()>0) misura.put("____binaryIdsArray", elencoBinaryId);

						ret.add(misura);


					}
					recCount++;

				}	

				try {
					deltaTime=System.currentTimeMillis()-starTtime;
				} catch (Exception e) {}
				log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] total fetch in --> "+deltaTime);


			} catch (Exception e) {
				throw e;
			}  finally {
				cursor.close();			
			} 


		} catch (SDPOrderBySizeException e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStream] SDPOrderBySizeException",e);
			throw (SDPOrderBySizeException)e;
		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStream] GenericException",e);
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStream] INGORED" +e);
		} finally {
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] END");
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

	public SDPDataResult getMeasuresStatsPerStream(String codiceTenant, 
			String nameSpace, 
			EdmEntityContainer entityContainer,
			DBObject streamMetadata,
			String internalId,
			String datatType,
			Object userQuery, 
			Object userOrderBy,
			int skip,
			int limit,
			String timeGroupByParam,
			String timeGroupOperatorsParam,
			Object groupOutQuery
			) throws SDPCustomQueryOptionException{
		String collection=null;
		//		String sensore=null;
		//		String stream=null;
		String idDataset=null;
		String datasetCode=null;
		String datasetToFindVersion=null;
		List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();
		int cnt = 0;

		// TODO YUCCA-74 odata evoluzione

		try {
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] BEGIN");
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] codiceTenant="+codiceTenant);
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] nameSpace="+nameSpace);
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] entityContainer="+entityContainer);
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] internalId="+internalId);
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] datatType="+datatType);
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] userQuery="+userQuery);
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] streamMetadata="+streamMetadata);

			List<Property> compPropsTot=new ArrayList<Property>();
			List<Property> compPropsCur=new ArrayList<Property>();			


			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero di collencion,host, port, db specifici per il dataset
			//       - modificare eventualmente la logica di recupero dell'idDataset
			//INVARIATO

			collection=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("collection") );
			String host=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("host"));
			String port =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("port") );
			String dbcfg =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("db") );
			idDataset=takeNvlValues(streamMetadata.get("idDataset"));
			datasetCode=takeNvlValues(streamMetadata.get("datasetCode"));

			//TODO = socialDataset
			String streamSubtype=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("subtype") );
			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * ATTENZIONE!!!!!! datasetVersion sarà un array da mettere in in
			 */

			datasetToFindVersion=takeNvlValues(streamMetadata.get("datasetVersion"));
			//idDataset=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("idDataset") );

			if (null==collection || collection.trim().length()<=0) {
				DbConfDto tanantDbCfg=new DbConfDto();

				if (DATA_TYPE_MEASURE.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_MESURES, codiceTenant);
				} else if (DATA_TYPE_DATA.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_DATA, codiceTenant);
				}  
				collection=tanantDbCfg.getCollection();
				host=tanantDbCfg.getHost();
				port=""+tanantDbCfg.getPort();
				dbcfg=tanantDbCfg.getDataBase();
			}
			if (null==dbcfg || dbcfg.trim().length()<=0) {
				DbConfDto tanantDbCfg=new DbConfDto();

				if (DATA_TYPE_MEASURE.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_MESURES, codiceTenant);
				} else if (DATA_TYPE_DATA.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_DATA, codiceTenant);
				}  
				dbcfg=tanantDbCfg.getDataBase();
			}

			host=SDPDataApiConfig.getInstance().getMongoDefaultHost();
			port=""+SDPDataApiConfig.getInstance().getMongoDefaultPort();

			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero dell'elenco dei campi che contiene il join di info.fuields di tutte le versioni di quel dataset
			Object eleCapmpi=((DBObject)streamMetadata.get("info")).get("fields");

			BasicDBList campiDbList= getDatasetFiledsDbList(eleCapmpi);
			for (int k=0;k<campiDbList.size();k++) {
				boolean present=false;
				compPropsCur=getDatasetFiledsOdataPros(campiDbList.get(k));
				for (int i=0;i<compPropsTot.size();i++) {
					if (compPropsTot.get(i).getName().equals(compPropsCur.get(0).getName())) present=true;

				}
				//TODO nel caso in cui present= true si potrebbe verficare il tipo che abbiamo in compsproptot con quello in  campiDbList.get(k)
				// sollevando eccezione se son diversi
				if (!present) {
					compPropsTot.add(compPropsCur.get(0));
				}
			}





			if (collection==null)  return null;

			//DBCursor cursor=null;



			//MongoClient mongoClient = new MongoClient(host,Integer.parseInt(port));
			//MongoClient mongoClient = getMongoClient(host,Integer.parseInt(port));			
			MongoClient mongoClient = null;			


			DB db = mongoClient.getDB(dbcfg);


			DBCollection collMisure = db.getCollection(collection);
			BasicDBList queryTot=new BasicDBList();

			//queryTot.add( new BasicDBObject("idDataset",idDataset));
			queryTot.add( new BasicDBObject("idDataset",new Integer(new Double(idDataset).intValue())));

			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * ATTENZIONE!!!!!! datasetVersion sarà un array da mettere in in
			 * 
			 * la parte aggiunta a query tot per il dataset versione  deve essere  {datasetversion: {$in: [........ ] }}
			 */

			ArrayList<Integer> vals = new ArrayList<Integer>();

			List<DBObject> listDatasetVersion = (List<DBObject>) streamMetadata.get("listDatasetVersion");
			Iterator<DBObject> listaIterator = listDatasetVersion.iterator();
			while (listaIterator.hasNext()) {
				DBObject el = listaIterator.next();
				Integer tmpVersDSCode = (Integer) el.get("datasetVersion"+datasetCode);
				vals.add(tmpVersDSCode);
			}

			//queryTot.add( new BasicDBObject("datasetVersion",new Integer(new Double(datasetToFindVersion).intValue())));
			//queryTot.add( new BasicDBObject("datasetVersion", new BasicDBObject("$in", vals)));
			queryTot.add( new BasicDBObject("datasetVersion", new BasicDBObject("$gt", 0)));



			//BasicDBObject query = new BasicDBObject("idDataset",idDataset);
			if (null!=internalId) {
				//query.append("_id",new ObjectId(internalId));
				queryTot.add( new BasicDBObject("_id",new ObjectId(internalId)));

			}
			if (null != userQuery) {
				log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] userQuery="+userQuery);
				if (userQuery instanceof BasicDBList) {
					queryTot.addAll((BasicDBList)userQuery);
				} else if (userQuery instanceof BasicDBObject) {
					queryTot.add((BasicDBObject)userQuery);
				}

				//query.append("$and", userQuery);
			}

			BasicDBList queryOutTot=new BasicDBList();
			if (null != groupOutQuery) {
				log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] groupOutQuery="+groupOutQuery);
				if (groupOutQuery instanceof BasicDBList) {
					queryOutTot.addAll((BasicDBList)groupOutQuery);
				} else if (groupOutQuery instanceof BasicDBObject) {
					queryOutTot.add((BasicDBObject)groupOutQuery);
				}

				//query.append("$and", userQuery);
			}


			BasicDBObject query = new BasicDBObject("$and", queryTot);
			BasicDBObject queryGroupOut = new BasicDBObject("$and", queryOutTot);

			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] total data query ="+query);
			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] total data groupOut query ="+queryGroupOut);
			//cursor = collMisure.find(query);

			//cnt = collMisure.find(query).count();

			//			if (skip<0) skip=0;
			//			if (limit<0) limit=SDPDataApiConfig.getInstance().getMaxDocumentPerPage();
			//			
			//			
			//			// per ordinamento su max 
			//			limit=SDPDataApiConfig.getInstance().getMaxDocumentPerPage()+SDPDataApiConfig.getInstance().getMaxSkipPages();
			//			skip=0;


			DBObject match=new BasicDBObject("$match",query);
			DBObject matchAggregationOut=new BasicDBObject("$match",queryGroupOut);

			// groupby id
			BasicDBObject groupFiledsId= new BasicDBObject();

			if ("year".equals(timeGroupByParam)) {
				groupFiledsId.put("year", new BasicDBObject("$year","$time"));
			} else if ("month_year".equals(timeGroupByParam)) {
				groupFiledsId.put("year", new BasicDBObject("$year","$time"));
				groupFiledsId.put("month", new BasicDBObject("$month","$time"));
			} else if ("dayofmonth_month_year".equals(timeGroupByParam)) {
				groupFiledsId.put("year", new BasicDBObject("$year","$time"));
				groupFiledsId.put("dayofmonth", new BasicDBObject("$dayOfMonth","$time"));
				groupFiledsId.put("month", new BasicDBObject("$month","$time"));
			} else if ("hour_dayofmonth_month_year".equals(timeGroupByParam)) {
				groupFiledsId.put("year", new BasicDBObject("$year","$time"));
				groupFiledsId.put("dayofmonth", new BasicDBObject("$dayOfMonth","$time"));
				groupFiledsId.put("month", new BasicDBObject("$month","$time"));
				groupFiledsId.put("hour", new BasicDBObject("$hour","$time"));
			} else if ("minute_hour_dayofmonth_month_year".equals(timeGroupByParam)) {
				//YUCCA-346
				groupFiledsId.put("year", new BasicDBObject("$year","$time"));
				groupFiledsId.put("dayofmonth", new BasicDBObject("$dayOfMonth","$time"));
				groupFiledsId.put("month", new BasicDBObject("$month","$time"));
				groupFiledsId.put("hour", new BasicDBObject("$hour","$time"));
				groupFiledsId.put("minute", new BasicDBObject("$minute","$time"));
			} else if ("month".equals(timeGroupByParam)) {
				//YUCCA-388
				groupFiledsId.put("month", new BasicDBObject("$month","$time"));
			} else if ("dayofmonth_month".equals(timeGroupByParam)) {
				//YUCCA-388
				groupFiledsId.put("dayofmonth", new BasicDBObject("$dayOfMonth","$time"));
				groupFiledsId.put("month", new BasicDBObject("$month","$time"));
			} else if ("dayofweek_month".equals(timeGroupByParam)) {
				//YUCCA-388
				groupFiledsId.put("dayofweek", new BasicDBObject("$dayOfWeek","$time"));
				groupFiledsId.put("month", new BasicDBObject("$month","$time"));
			} else if ("dayofweek".equals(timeGroupByParam)) {
				//YUCCA-388
				groupFiledsId.put("dayofweek", new BasicDBObject("$dayOfWeek","$time"));
			} else if ("hour_dayofweek".equals(timeGroupByParam)) {
				//YUCCA-388
				groupFiledsId.put("dayofweek", new BasicDBObject("$dayOfWeek","$time"));
				groupFiledsId.put("hour", new BasicDBObject("$hour","$time"));
			} else if ("hour".equals(timeGroupByParam)) {
				//YUCCA-388
				groupFiledsId.put("hour", new BasicDBObject("$hour","$time"));

			} else if ("retweetparentid".equals(timeGroupByParam)) {
				//YUCCA-388

				if (!("socialDataset".equalsIgnoreCase(streamSubtype))) throw new SDPCustomQueryOptionException("invalid timeGroupBy value: retweetparentid aggregations is aveailable only for social dataset", Locale.UK);
				groupFiledsId.put("retweetparentid", "$retweetParentId");
			} else if ("iduser".equals(timeGroupByParam)) {
				//YUCCA-388

				if (!("socialDataset".equalsIgnoreCase(streamSubtype))) throw new SDPCustomQueryOptionException("invalid timeGroupBy value: iduser aggregations is aveailable only for social dataset", Locale.UK);
				groupFiledsId.put("iduser", "$userId");

			} else {
				throw new SDPCustomQueryOptionException("invalid timeGroupBy value", Locale.UK);
			}
			BasicDBObject groupId= new BasicDBObject("_id",groupFiledsId);


			//BasicDBList groupFiledsFileds= new BasicDBList();
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
				String opMongo=null;
				if ("avg".equals(op)) opMongo="$avg";
				else if ("first".equals(op)) opMongo="$first";
				else if ("last".equals(op)) opMongo="$last";
				else if ("sum".equals(op)) opMongo="$sum";
				else if ("max".equals(op)) opMongo="$max";
				else if ("min".equals(op)) opMongo="$min";
				else throw new SDPCustomQueryOptionException("invalid timeGroupOperators invalid operation '"+op+"' in '" + curOperator  +"'", Locale.UK);

				if (campoOperazione.containsKey(field)) throw new SDPCustomQueryOptionException("invalid timeGroupOperators filed '"+field+"' present in more than one operation" , Locale.UK);

				campoOperazione.put(field, opMongo);
				groupId.put(field+"_sts", new BasicDBObject(opMongo, "$"+field));

			}
			groupId.put("count", new BasicDBObject("$sum", 1));

			DBObject group = new BasicDBObject("$group", groupId);			

			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] total match ="+match);
			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] total group ="+group);


			ArrayList<DBObject> pipeline = new ArrayList<DBObject>();

			pipeline.add(match);
			pipeline.add(group);

			//Arrays.asList(match,  group );
			if (groupOutQuery != null) {
				pipeline.add(matchAggregationOut);
			}
			if (null!=userOrderBy) {
				//pipeline.add(new BasicDBObject("$sort",(BasicDBList)userOrderBy));
				BasicDBObject objeSort=new BasicDBObject();
				//				for (int kk=0;kk<((BasicDBList)userOrderBy).size();kk++ ) {
				//					BasicDBObject curObj=(BasicDBObject)((BasicDBList)userOrderBy).get(kk);
				//					Iterator<String> it = curObj.keySet().iterator();
				//					while (it.hasNext()) {
				//						String key=it.next();
				//						Integer orderVersus=new Integer(curObj.getString(key));
				//						objeSort.append(key, orderVersus);
				//						
				//					}
				//				}
				for (int kkk=0;kkk<((ArrayList<SDPMongoOrderElement>)userOrderBy).size();kkk++) {
					SDPMongoOrderElement curOrdElem=(SDPMongoOrderElement)((ArrayList<SDPMongoOrderElement>)userOrderBy).get(kkk);
					objeSort.append(curOrdElem.getNomeCampo(),curOrdElem.getOrdine());
				}


				pipeline.add(new BasicDBObject("$sort",objeSort));

			}

			//if (null!=userOrderBy) cursor = collMisure.find(query).skip(skip).limit(limit).sort((BasicDBList)userOrderBy);
			//else cursor = collMisure.find(query).skip(skip).limit(limit);

			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] total pipeline ="+pipeline.toString());

			AggregationOptions aggregationOptions = AggregationOptions.builder()
					.batchSize(100)
					.outputMode(AggregationOptions.OutputMode.CURSOR)
					.allowDiskUse(true)
					.build();			

			long starTtime=0;
			long deltaTime=-1;

			starTtime=System.currentTimeMillis();
			Cursor cursor =collMisure.aggregate(pipeline,aggregationOptions);

			try {
				deltaTime=System.currentTimeMillis()-starTtime;
			} catch (Exception e) {}
			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] QUERY TIME ="+deltaTime);

			starTtime=System.currentTimeMillis();
			deltaTime=-1;



			cnt=0;
			try {
				//for (DBObject result : output.results()) {
				while (cursor.hasNext()) {



					DBObject obj=cursor.next();				

					cnt++;

					//DBObject obj=result;
					String giorno=takeNvlValues( ((DBObject)obj.get("_id")).get("dayofmonth"));
					String mese=takeNvlValues( ((DBObject)obj.get("_id")).get("month"));
					String anno=takeNvlValues( ((DBObject)obj.get("_id")).get("year"));
					String ora=takeNvlValues( ((DBObject)obj.get("_id")).get("hour"));
					//YUCCA-346
					String minuto=takeNvlValues( ((DBObject)obj.get("_id")).get("minute"));


					//YUCCA-388
					String dayofweek=takeNvlValues( ((DBObject)obj.get("_id")).get("dayofweek"));
					String retweetparentid=takeNvlValues( ((DBObject)obj.get("_id")).get("retweetparentid"));
					String iduser=takeNvlValues( ((DBObject)obj.get("_id")).get("iduser"));


					//String datasetVersion=takeNvlValues(obj.get("datasetVersion"));


					String count=takeNvlValues( obj.get("count"));



					Map<String, Object> misura = new HashMap<String, Object>();
					misura.put("dayofmonth",  (giorno==null ? -1 : new Integer(giorno)));
					misura.put("month",  (mese==null ? -1 : new Integer(mese)));
					misura.put("year",  (anno==null ? -1 : new Integer(anno)));
					misura.put("hour",  (ora==null ? -1 : new Integer(ora)));
					//YUCCA-346
					misura.put("minute",  (minuto==null ? -1 : new Integer(minuto)));
					//YUCCA-388
					misura.put("dayofweek",  (dayofweek==null ? -1 : new Integer(dayofweek)));

					//TODO solo se e' social
					misura.put("retweetparentid",  (retweetparentid==null ? -1 : new Long(retweetparentid)));
					misura.put("iduser",  (iduser==null ? -1 : new Long(iduser)));


					misura.put("count",  (count==null ? 0 : new Integer(count)));



					//					if (DATA_TYPE_MEASURE.equals(datatType)) {
					//						String streamId=obj.get("streamCode").toString();
					//						String sensorId=obj.get("sensor").toString();
					//						misura.put("streamCode", streamId);
					//						misura.put("sensor", sensorId);
					//						misura.put("time",  obj.get("time"));
					//					}					
					//
					//					
					//					String iddataset=takeNvlValues(obj.get("idDataset"));
					//					if (null!= iddataset ) misura.put("idDataset",  Integer.parseInt(iddataset));
					//					if (null!= datasetVersion ) misura.put("datasetVersion",  Integer.parseInt(datasetVersion));


					for (int i=0;i<compPropsTot.size();i++) {

						String chiave=compPropsTot.get(i).getName();
						chiave=chiave+"_sts";
						if (obj.keySet().contains(chiave) ) {
							String  valore=takeNvlValues(obj.get(chiave));
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
									Object dataObj=obj.get(chiave);
									misura.put(chiave, dataObj);
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.DateTime)) {
									//Sun Oct 19 07:01:17 CET 1969
									//EEE MMM dd HH:mm:ss zzz yyyy
									Object dataObj=obj.get(chiave);

									//System.out.println("------------------------------"+dataObj.getClass().getName());

									misura.put(chiave, dataObj);


									//																 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
									//															     Date data = dateFormat.parse(valore);								
									//																	misura.put(chiave, data);


								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Decimal)) {
									//comppnenti.put(chiave, Float.parseFloat(valore));
									misura.put(chiave, Double.parseDouble(valore));

								}
							}
						}
					}					


					ret.add(misura);
				}	
			} catch (Exception e) {
				throw e;
			}  finally {
				//cursor.close();			
			} 
			try {
				deltaTime=System.currentTimeMillis()-starTtime;
			} catch (Exception e) {}
			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] FETCH TIME ="+deltaTime);


		} catch (Exception e) {
			if (e instanceof SDPCustomQueryOptionException) {
				log.error("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] rethrow" +e);
				throw (SDPCustomQueryOptionException) e;
			} else log.error("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] INGORED" +e);
		} finally {
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] END");
		}


		SDPDataResult outres= new SDPDataResult(ret, cnt);
		return outres;
	}	











	public SDPDataResult getMeasuresStatsPerStreamPhoenix(String codiceTenant, 
			String nameSpace, 
			EdmEntityContainer entityContainer,
			DBObject streamMetadata,
			String internalId,
			String datatType,
			Object userQuery, 
			Object userOrderBy,
			int skip,
			int limit,
			String timeGroupByParam,
			String timeGroupOperatorsParam,
			Object groupOutQuery
			) throws SDPCustomQueryOptionException{
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

		try {
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamPhoenix] BEGIN");
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamPhoenix] codiceTenant="+codiceTenant);
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamPhoenix] nameSpace="+nameSpace);
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamPhoenix] entityContainer="+entityContainer);
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamPhoenix] internalId="+internalId);
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamPhoenix] datatType="+datatType);
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamPhoenix] userQuery="+userQuery);
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamPhoenix] streamMetadata="+streamMetadata);

			List<Property> compPropsTot=new ArrayList<Property>();
			List<Property> compPropsCur=new ArrayList<Property>();			


			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero di collencion,host, port, db specifici per il dataset
			//       - modificare eventualmente la logica di recupero dell'idDataset
			//INVARIATO

			collection=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("collection") );
			String host=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("host"));
			String port =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("port") );
			String dbcfg =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("db") );
			idDataset=takeNvlValues(streamMetadata.get("idDataset"));
			datasetCode=takeNvlValues(streamMetadata.get("datasetCode"));

			//TODO = socialDataset
			String streamSubtype=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("subtype") );
			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * ATTENZIONE!!!!!! datasetVersion sarà un array da mettere in in
			 */

			datasetToFindVersion=takeNvlValues(streamMetadata.get("datasetVersion"));
			//idDataset=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("idDataset") );


			String schema = "db_"+codiceTenant;
			String table = "";
			DbConfDto tanantDbCfg=new DbConfDto();
			if (DATA_TYPE_MEASURE.equals(datatType)) {
				table = "measures";
				tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_MESURES_PHOENIX, codiceTenant);
			} else if (DATA_TYPE_SOCIAL.equals(datatType)) {
				table="social";
				tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_SOCIAL_PHOENIX, codiceTenant);
			}

			if(tanantDbCfg!=null && tanantDbCfg.getDataBase()!=null && tanantDbCfg.getDataBase().trim().length()>0) schema=tanantDbCfg.getDataBase(); 
			if(tanantDbCfg!=null && tanantDbCfg.getCollection()!=null && tanantDbCfg.getCollection().trim().length()>0) table=tanantDbCfg.getCollection(); 



			Object eleCapmpi=((DBObject)streamMetadata.get("info")).get("fields");
			HashMap<String, String> campoTipoMetadato= new HashMap<String, String>();
			BasicDBList lista=new BasicDBList();
			BasicDBList campiDbList= getDatasetFiledsDbList(eleCapmpi);
			for (int k=0;k<campiDbList.size();k++) {
				boolean present=false;
				compPropsCur=getDatasetFiledsOdataPros(campiDbList.get(k));

				for (int i=0;i<compPropsTot.size();i++) {
					if (compPropsTot.get(i).getName().equals(compPropsCur.get(0).getName())) present=true;

				}
				// TODO nel caso in cui present= true si potrebbe verficare il tipo che abbiamo in compsproptot con quello in  campiDbList.get(k)
				// sollevando eccezione se son diversi
				if (!present) {
					compPropsTot.add(compPropsCur.get(0));
					lista.add(campiDbList.get(k));
				}
			}


			String campiPh=null;
			for (int i=0; i<lista.size();i++) {
				BasicDBObject cur= (BasicDBObject)lista.get(i);
				String nome=cur.getString("fieldName");
				String tipo=cur.getString("dataType");
				campoTipoMetadato.put(nome, tipo);
				if (campiPh == null) campiPh=nome+SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(tipo)+ " " + SDPDataApiConstants.SDP_DATATYPE_PHOENIXTYPES.get(tipo);
				else campiPh+=","+nome+SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(tipo)+ " " + SDPDataApiConstants.SDP_DATATYPE_PHOENIXTYPES.get(tipo);

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

				groupbysleect+=", "+opPhoenix + "(";
				groupbysleect+=campoCompleto;
				groupbysleect+= ")";
				if (extraOp) {
					groupbysleect+=  " WITHIN GROUP (ORDER BY "+campoCompleto+" asc) "; 
				}
				groupbysleect+=  " as " + field +"_sts";

			}			



			sql = groupbysleect+", count(1) as totale " +sql + " GROUP BY "+groupby; 





			sql = "select * from (select " + sql +")";

			if (null!=groupOutQuery) sql += " where "+((SDPPhoenixExpression)groupOutQuery).toString();

			if (null!=userOrderBy) sql += " ORDER BY " +  (String)userOrderBy;
			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamPhoenix] sqlPhoenix="+sql);

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
			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamPhoenix] QUERY TIME ="+deltaTime);

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



				Map<String, Object> misura = new HashMap<String, Object>();
				misura.put("dayofmonth",  (giorno==null ? -1 : new Integer(giorno)));
				misura.put("month",  (mese==null ? -1 : new Integer(mese)));
				misura.put("year",  (anno==null ? -1 : new Integer(anno)));
				misura.put("hour",  (ora==null ? -1 : new Integer(ora)));
				//YUCCA-346
				misura.put("minute",  (minuto==null ? -1 : new Integer(minuto)));
				//YUCCA-388
				misura.put("dayofweek",  (dayofweek==null ? -1 : new Integer(dayofweek)));

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
			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamPhoenix] FETCH TIME ="+deltaTime);


		} catch (Exception e) {
			if (e instanceof SDPCustomQueryOptionException) {
				log.error("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamPhoenix] rethrow" ,e);
				throw (SDPCustomQueryOptionException) e;
			} else log.error("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamPhoenix] INGORED" ,e);
		} finally {
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStreamPhoenix] END");
		}


		SDPDataResult outres= new SDPDataResult(ret, cnt);
		return outres;
	}		






































	public SDPDataResult getBinary(String codiceTenant, String nameSpace, EdmEntityContainer entityContainer,DBObject streamMetadata,String internalId,String datatType,Object userQuery, Object userOrderBy,
			ArrayList<String> elencoIdBinary,
			String codiceApi,
			int skipI,
			int limitI
			) {


		// TODO YUCCA-74 odata evoluzione
		// potrebbe no nsubire modifiche verificare solo info.binaryIdDataset e info.binaryDatasetVersion in base a come viene modificato streamMetadata


		String collection=null;
		//		String sensore=null;
		//		String stream=null;
		//		String idDataset=null;
		//		String datasetToFindVersion=null;
		List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();
		long cnt = -1;
		long skipL=skipI;
		long limitL=limitI;
		try {
			log.debug("[SDPDataApiMongoAccess::getBinary] BEGIN");
			log.debug("[SDPDataApiMongoAccess::getBinary] codiceTenant="+codiceTenant);
			log.debug("[SDPDataApiMongoAccess::getBinary] nameSpace="+nameSpace);
			log.debug("[SDPDataApiMongoAccess::getBinary] entityContainer="+entityContainer);
			log.debug("[SDPDataApiMongoAccess::getBinary] internalId="+internalId);
			log.debug("[SDPDataApiMongoAccess::getBinary] datatType="+datatType);
			log.debug("[SDPDataApiMongoAccess::getBinary] userQuery="+userQuery);
			log.debug("[SDPDataApiMongoAccess::getBinary] streamMetadata="+streamMetadata);

			//			List<Property> compPropsTot=new ArrayList<Property>();
			//			List<Property> compPropsCur=new ArrayList<Property>();			


			//			collection=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("collection") );
			//			String host=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("host"));
			//			String port =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("port") );
			//			String dbcfg =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("db") );
			//			idDataset=takeNvlValues(streamMetadata.get("idDataset"));
			//			datasetToFindVersion=takeNvlValues(streamMetadata.get("datasetVersion"));


			DbConfDto tanantDbCfg=new DbConfDto();
			tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_MEDIA_SOLR, codiceTenant);
			collection=tanantDbCfg.getCollection();
			String host=tanantDbCfg.getHost();
			String port=""+tanantDbCfg.getPort();
			String dbcfg=tanantDbCfg.getDataBase();


			if (null==collection || collection.trim().length()<=0) {
				//TODO aggoungere int per integrazione

				collection="sdp_"+SDPDataApiConfig.getInstance().getSdpAmbiente()+codiceTenant+"_media";
			}			




			Integer idDatasetBinary=(Integer)((DBObject)streamMetadata.get("info")).get("binaryIdDataset");
			Integer binaryDatasetVersion=(Integer)((DBObject)streamMetadata.get("info")).get("binaryDatasetVersion");


			host=SDPDataApiConfig.getInstance().getMongoDefaultHost();
			port=""+SDPDataApiConfig.getInstance().getMongoDefaultPort();

			//			Object eleCapmpi=((DBObject)streamMetadata.get("info")).get("fields");
			//
			//			BasicDBList campiDbList= getDatasetFiledsDbList(eleCapmpi);
			//			for (int k=0;k<campiDbList.size();k++) {
			//				boolean present=false;
			//				compPropsCur=getDatasetFiledsOdataPros(campiDbList.get(k));
			//				for (int i=0;i<compPropsTot.size();i++) {
			//					if (compPropsTot.get(i).getName().equals(compPropsCur.get(0).getName())) present=true;
			//
			//				}
			//				if (!present) {
			//					compPropsTot.add(compPropsCur.get(0));
			//				}
			//			}


			List<Property> compPropsTot=new ArrayList<Property>();

			compPropsTot.add(new SimpleProperty().setName("internalId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			compPropsTot.add(new SimpleProperty().setName("datasetVersion").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));





			compPropsTot.add(new SimpleProperty().setName("idBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			compPropsTot.add(new SimpleProperty().setName("filenameBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("aliasNameBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
			compPropsTot.add(new SimpleProperty().setName("sizeBinary").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			//			compPropsTot.add(new SimpleProperty().setName("insertDateBinary").setType(EdmSimpleTypeKind.DateTimeOffset).setFacets(new Facets().setNullable(true)));
			//			compPropsTot.add(new SimpleProperty().setName("lastUpdateDateBinary").setType(EdmSimpleTypeKind.DateTimeOffset).setFacets(new Facets().setNullable(true)));
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

			DBCursor cursor=null;



			//MongoClient mongoClient = new MongoClient(host,Integer.parseInt(port));
			//MongoClient mongoClient = getMongoClient(host,Integer.parseInt(port));			


			//DB db = mongoClient.getDB(dbcfg);


			//DBCollection collMisure = db.getCollection(collection);

			DBCollection collMisure =null;
			BasicDBList queryTot=new BasicDBList();

			//queryTot.add( new BasicDBObject("idDataset",idDataset));


			String queryTotSolr="(iddataset_l:"+idDatasetBinary+" AND datasetversion_l : "+binaryDatasetVersion+" ";



			//			queryTot.add( new BasicDBObject("idDataset",idDatasetBinary));
			//
			//			queryTot.add( new BasicDBObject("datasetVersion",binaryDatasetVersion));



			//BasicDBObject query = new BasicDBObject("idDataset",idDataset);
			if (null!=internalId) {
				//query.append("_id",new ObjectId(internalId));
				//queryTot.add( new BasicDBObject("_id",new ObjectId(internalId)));
				queryTotSolr += "AND id : "+internalId;


			}
			queryTotSolr += ")";
			if (null != userQuery) {
				log.debug("[SDPDataApiMongoAccess::getBinary] userQuery="+userQuery);
				if (userQuery instanceof SDPOdataFilterExpression) {
					queryTotSolr += "AND ("+userQuery+")";
				} else {
					queryTotSolr += "AND "+userQuery;
				}


				//query.append("$and", userQuery);
			}


			//			if (null!=elencoIdBinary && elencoIdBinary.size()>0) {
			//				BasicDBObject inQuery = new BasicDBObject();
			//				inQuery.put("idBinary", new BasicDBObject("$in", elencoIdBinary));
			//				queryTot.add(inQuery);
			//			}
			String inClause=null;
			for (int kki=0; null!=elencoIdBinary && kki<elencoIdBinary.size(); kki++ ) {
				if (inClause==null) inClause="("+ elencoIdBinary.get(kki);
				else inClause=" OR "+ elencoIdBinary.get(kki);
			}
			String  query = queryTotSolr;
			if (inClause!=null) query+= " AND (idbinary_s : " + inClause +"))";

			//BasicDBObject query = new BasicDBObject("$and", queryTot);

			log.info("[SDPDataApiMongoAccess::getBinary] total data query ="+query);
			//cursor = collMisure.find(query);

			//cnt = collMisure.find(query).count();

			if (skipL<0) skipL=0;
			if (limitL<0) limitL=SDPDataApiConfig.getInstance().getMaxDocumentPerPage();


			// per ordinamento su max 
			limitL=SDPDataApiConfig.getInstance().getMaxDocumentPerPage()+SDPDataApiConfig.getInstance().getMaxSkipPages();
			skipL=0;


			ArrayList<SortClause> orderSolr=null;

			for (int kkk=0;userOrderBy!=null && kkk<((ArrayList<String>)userOrderBy).size();kkk++) {
				if (null==orderSolr) orderSolr=new ArrayList<SortClause>();

				orderSolr.add(((ArrayList<SortClause>)userOrderBy).get(kkk));
			}



			//HttpSolrServer solrServer = new HttpSolrServer( "http://sdnet-solr.sdp.csi.it:8983/solr/"+codiceTenant+"/" );


			CloudSolrClient solrServer =  CloudSolrSingleton.getServer();	
			//HttpSolrServer solrServer = new HttpSolrServer( "http://sdnet-solr.sdp.csi.it:8983/solr/" );

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
					String datasetVersion=takeNvlValues(curSolrDoc.get("datasetversion_l"));
					Map<String, Object> misura = new HashMap<String, Object>();
					misura.put("internalId",  internalID);
					String iddataset=takeNvlValues(curSolrDoc.get("iddataset_l"));
					if (null!= iddataset ) misura.put("idDataset",  Integer.parseInt(iddataset));
					if (null!= datasetVersion ) misura.put("datasetVersion",  Integer.parseInt(datasetVersion));


					for (int i=0;i<compPropsTot.size();i++) {

						String chiave=compPropsTot.get(i).getName();
						String chiaveL=getPropertyName(compPropsTot.get(i));

						chiaveL=campoTipoMetadato.get(compPropsTot.get(i).getName());




						if (curSolrDoc.keySet().contains(chiaveL.toLowerCase()) ) {
							Object oo = curSolrDoc.get(chiaveL.toLowerCase());

							String  valore=takeNvlValues(curSolrDoc.get(chiaveL.toLowerCase()));
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
									//elencoBinaryId.add((String)valore);

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


				try {
					//deltaTime=System.currentTimeMillis()-starTtime;
				} catch (Exception e) {}
				log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] total fetch in --> nodata");


			} catch (Exception e) {
				throw e;
			}  finally {
				//cursor.close();			
			}








			//			if (null!=userOrderBy) cursor = collMisure.find(query).skip(skip).limit(limit).sort((BasicDBList)userOrderBy);
			//			else cursor = collMisure.find(query).skip(skip).limit(limit);
			//			try {
			//				while (cursor.hasNext()) {
			//
			//
			//
			//					DBObject obj=cursor.next();
			//					String internalID=obj.get("_id").toString();
			//					String datasetVersion=takeNvlValues(obj.get("datasetVersion"));
			//					//					String current=takeNvlValues(obj.get("current"));
			//
			//
			//
			//					Map<String, Object> misura = new HashMap<String, Object>();
			//					misura.put("internalId",  internalID);
			//
			//					if (DATA_TYPE_MEASURE.equals(datatType)) {
			//						String streamId=obj.get("streamCode").toString();
			//						String sensorId=obj.get("sensor").toString();
			//						misura.put("streamCode", streamId);
			//						misura.put("sensor", sensorId);
			//						misura.put("time",  obj.get("time"));
			//					}					
			//
			//
			//					String iddataset=takeNvlValues(obj.get("idDataset"));
			//					if (null!= iddataset ) misura.put("idDataset",  Integer.parseInt(iddataset));
			//					if (null!= datasetVersion ) misura.put("datasetVersion",  Integer.parseInt(datasetVersion));
			//
			//
			//
			//
			//					for (int i=0;i<compPropsTot.size();i++) {
			//
			//						String chiave=compPropsTot.get(i).getName();
			//						if (obj.keySet().contains(chiave) ) {
			//							String  valore=takeNvlValues(obj.get(chiave));
			//							if (null!=valore) {
			//								if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Boolean)) {
			//									misura.put(chiave, Boolean.valueOf(valore));
			//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.String)) {
			//									misura.put(chiave, valore);
			//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Int32)) {
			//									misura.put(chiave, Integer.parseInt(valore));
			//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Int64)) {
			//									misura.put(chiave, Long.parseLong(valore));
			//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Double)) {
			//									misura.put(chiave, Double.parseDouble(valore));
			//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.DateTimeOffset)) {
			//									Object dataObj=obj.get(chiave);
			//									misura.put(chiave, dataObj);
			//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.DateTime)) {
			//									//Sun Oct 19 07:01:17 CET 1969
			//									//EEE MMM dd HH:mm:ss zzz yyyy
			//									Object dataObj=obj.get(chiave);
			//
			//									//System.out.println("------------------------------"+dataObj.getClass().getName());
			//
			//									misura.put(chiave, dataObj);
			//
			//
			//									//																 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			//									//															     Date data = dateFormat.parse(valore);								
			//									//																	misura.put(chiave, data);
			//
			//
			//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Decimal)) {
			//									//comppnenti.put(chiave, Float.parseFloat(valore));
			//									misura.put(chiave, Double.parseDouble(valore));
			//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Binary)) {
			//									Map<String, Object> mappaBinaryRef=new HashMap<String, Object>();
			//									mappaBinaryRef.put("idBinary", (String)valore);
			//									misura.put(chiave, mappaBinaryRef);
			//
			//
			//								}
			//							}
			//						}
			//					}
			//
			//					///binary/{apiCode}/{dataSetCode}/{dataSetVersion}/{idBinary}
			//
			//
			//
			//					String path="/api/"+codiceApi+"/attachment/"+idDatasetBinary+"/"+binaryDatasetVersion+"/"+misura.get("idBinary");
			//
			//					misura.put("urlDownloadBinary", path);
			//
			//					ret.add(misura);
			//				}	
			//			} catch (Exception e) {
			//				throw e;
			//			}  finally {
			//				cursor.close();			
			//			} 


		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::getBinary] INGORED" +e);
		} finally {
			log.debug("[SDPDataApiMongoAccess::getBinary] END");
		}


		SDPDataResult outres= new SDPDataResult(ret, cnt);
		return outres;
	}		



	public SDPDataResult getMeasuresPerStreamNewLimit(String codiceTenant, String nameSpace, EdmEntityContainer entityContainer,DBObject streamMetadata,String internalId,String datatType,Object userQuery, Object userOrderBy,
			int skip,
			int limit
			) throws SDPOrderBySizeException,SDPPageSizeException {
		String collection=null;
		//		String sensore=null;
		//		String stream=null;
		String idDataset=null;
		String datasetCode=null;
		String datasetToFindVersion=null;
		List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();
		int cnt = 0;


		// TODO YUCCA-74 odata evoluzione

		try {
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] BEGIN");
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] codiceTenant="+codiceTenant);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] nameSpace="+nameSpace);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] entityContainer="+entityContainer);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] internalId="+internalId);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] datatType="+datatType);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] userQuery="+userQuery);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] streamMetadata="+streamMetadata);

			List<Property> compPropsTot=new ArrayList<Property>();
			List<Property> compPropsCur=new ArrayList<Property>();			

			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] limit_init --> "+limit);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] skip_init --> "+skip);



			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero di collencion,host, port, db specifici per il dataset
			//       - modificare eventualmente la logica di recupero dell'idDataset
			//INVARIATO!!

			collection=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("collection") );
			String host=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("host"));
			String port =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("port") );
			String dbcfg =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("db") );
			idDataset=takeNvlValues(streamMetadata.get("idDataset"));
			datasetCode=takeNvlValues(streamMetadata.get("datasetCode"));

			String streamSubtype=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("subtype") );


			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * ATTENZIONE!!!!!! datasetVersion sarà un array da mettere in in
			 */

			datasetToFindVersion=takeNvlValues(streamMetadata.get("datasetVersion"));
			//idDataset=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("idDataset") );

			if (null==collection || collection.trim().length()<=0) {
				DbConfDto tanantDbCfg=new DbConfDto();

				if (DATA_TYPE_MEASURE.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_MESURES, codiceTenant);
				} else if (DATA_TYPE_DATA.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_DATA, codiceTenant);
				}  
				collection=tanantDbCfg.getCollection();
				host=tanantDbCfg.getHost();
				port=""+tanantDbCfg.getPort();
				dbcfg=tanantDbCfg.getDataBase();
			}

			if (null==dbcfg || dbcfg.trim().length()<=0) {
				DbConfDto tanantDbCfg=new DbConfDto();

				if (DATA_TYPE_MEASURE.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_MESURES, codiceTenant);
				} else if (DATA_TYPE_DATA.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_DATA, codiceTenant);
				}  
				dbcfg=tanantDbCfg.getDataBase();
			}


			host=SDPDataApiConfig.getInstance().getMongoDefaultHost();
			port=""+SDPDataApiConfig.getInstance().getMongoDefaultPort();

			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero dell'elenco dei campi che contiene il join di info.fuields di tutte le versioni di quel dataset
			Object eleCapmpi=((DBObject)streamMetadata.get("info")).get("fields");

			BasicDBList campiDbList= getDatasetFiledsDbList(eleCapmpi);
			for (int k=0;k<campiDbList.size();k++) {
				boolean present=false;
				compPropsCur=getDatasetFiledsOdataPros(campiDbList.get(k));
				for (int i=0;i<compPropsTot.size();i++) {
					if (compPropsTot.get(i).getName().equals(compPropsCur.get(0).getName())) present=true;

				}
				// TODO nel caso in cui present= true si potrebbe verficare il tipo che abbiamo in compsproptot con quello in  campiDbList.get(k)
				// sollevando eccezione se son diversi
				if (!present) {
					compPropsTot.add(compPropsCur.get(0));
				}
			}

			if (collection==null)  return null;

			DBCursor cursor=null;

			//MongoClient mongoClient = new MongoClient(host,Integer.parseInt(port));
			//MongoClient mongoClient = getMongoClient(host,Integer.parseInt(port));			
			MongoClient mongoClient = null;			
			//DB db = mongoClient.getDB(dbcfg);

			DBCollection collMisure = null;
			BasicDBList queryTot=new BasicDBList();
			BasicDBList queryTotCnt=new BasicDBList();

			queryTot.add( new BasicDBObject("idDataset",new Integer(new Double(idDataset).intValue())));
			queryTotCnt.add( new BasicDBObject("idDataset",new Integer(new Double(idDataset).intValue())));

			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * ATTENZIONE!!!!!! datasetVersion sarà un array da mettere in in
			 * 
			 * la parte aggiunta a query tot per il dataset versione  deve essere  {datasetversion: {$in: [........ ] }}
			 */

			ArrayList<Integer> vals = new ArrayList<Integer>();

			List<DBObject> listDatasetVersion = (List<DBObject>) streamMetadata.get("listDatasetVersion");
			Iterator<DBObject> listaIterator = listDatasetVersion.iterator();
			while (listaIterator.hasNext()) {
				DBObject el = listaIterator.next();
				Integer tmpVersDSCode = (Integer) el.get("datasetVersion"+datasetCode);
				vals.add(tmpVersDSCode);
			}

			//queryTot.add( new BasicDBObject("datasetVersion",new Integer(new Double(datasetToFindVersion).intValue())));
			//queryTot.add( new BasicDBObject("datasetVersion", new BasicDBObject("$in", vals)));
			queryTot.add( new BasicDBObject("datasetVersion", new BasicDBObject("$gt", 0)));



			//BasicDBObject query = new BasicDBObject("idDataset",idDataset);
			if (null!=internalId) {
				//query.append("_id",new ObjectId(internalId));
				queryTot.add( new BasicDBObject("_id",new ObjectId(internalId)));
				queryTotCnt.add( new BasicDBObject("_id",new ObjectId(internalId)));

			}
			if (null != userQuery) {
				log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] userQuery="+userQuery);
				if (userQuery instanceof BasicDBList) {
					queryTot.addAll((BasicDBList)userQuery);
					queryTotCnt.addAll((BasicDBList)userQuery);
				} else if (userQuery instanceof BasicDBObject) {
					queryTot.add((BasicDBObject)userQuery);
					queryTotCnt.add((BasicDBObject)userQuery);
				}

				//query.append("$and", userQuery);
			}

			BasicDBObject query = new BasicDBObject("$and", queryTot);

			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] total data query ="+query);
			//cursor = collMisure.find(query);

			long starTtime=0;
			long deltaTime=-1;


			starTtime=System.currentTimeMillis();
			cnt = collMisure.find( new BasicDBObject("$and", queryTotCnt)).count();
			try {
				deltaTime=System.currentTimeMillis()-starTtime;
			} catch (Exception e) {}
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] total data query COUNT executed in --> "+deltaTime);


			starTtime=0;
			deltaTime=-1;


			/** nuovi controlli skip e limit **/
			if (skip<0) skip=0;

			//controlli sui valoris massimi ammessi 
			if(skip>0 && skip>SDPDataApiConfig.getInstance().getMaxSkipPages()) throw new SDPPageSizeException("invalid skip value: max skip = "+SDPDataApiConfig.getInstance().getMaxSkipPages(),Locale.UK);
			if(limit> 0 && limit>SDPDataApiConfig.getInstance().getMaxDocumentPerPage()) throw new SDPPageSizeException("invalid top value: max document per page = "+SDPDataApiConfig.getInstance().getMaxDocumentPerPage(),Locale.UK);


			//se lo skip porta oltre il numero di risultati eccezione
			if (skip>cnt) throw new SDPPageSizeException("skip value out of range: max document in query result = "+cnt,Locale.UK);

			if (limit<0) {

				// se limit non valorizzato si restituisce tutto il resultset (limit=cnt) e si solleva eccezione se il resulset supera il numero massimo di risultati per pagina
				if ((cnt>SDPDataApiConfig.getInstance().getMaxDocumentPerPage())) throw new SDPPageSizeException("too many documents; use top parameter: max document per page = "+SDPDataApiConfig.getInstance().getMaxDocumentPerPage(),Locale.UK);
				limit=cnt;
			} 


			if (limit>0 && limit>(cnt-skip)) limit=cnt-skip;







			if (null!=userOrderBy) {

				boolean orderByAllowed=false;
				if (cnt<SDPDataApiConstants.SDP_MAX_DOC_FOR_ORDERBY) {
					orderByAllowed=true;
				} else if (DATA_TYPE_MEASURE.equals(datatType) && ((ArrayList<SDPMongoOrderElement>)userOrderBy).size()<=1) {

					SDPMongoOrderElement elemOrder=(SDPMongoOrderElement)((ArrayList<SDPMongoOrderElement>)userOrderBy).get(0);
					if (elemOrder.getNomeCampo().equalsIgnoreCase("time")) orderByAllowed=true;
					if (elemOrder.getNomeCampo().equalsIgnoreCase("retweetParentId") && ("socialDataset".equalsIgnoreCase(streamSubtype))) orderByAllowed=true;
					if (elemOrder.getNomeCampo().equalsIgnoreCase("userId") && ("socialDataset".equalsIgnoreCase(streamSubtype))) orderByAllowed=true;
					if (elemOrder.getNomeCampo().equalsIgnoreCase("_id")) orderByAllowed=true;
				} else if (DATA_TYPE_DATA.equals(datatType) && ((ArrayList<SDPMongoOrderElement>)userOrderBy).size()<=1) {
					SDPMongoOrderElement elemOrder=(SDPMongoOrderElement)((ArrayList<SDPMongoOrderElement>)userOrderBy).get(0);
					if (elemOrder.getNomeCampo().equalsIgnoreCase("_id")) orderByAllowed=true;
				}


				if (!orderByAllowed) throw new SDPOrderBySizeException("too many documents for order clause;",Locale.UK);

				BasicDBObject dbObjUserOrder=null;

				for (int kkk=0;kkk<((ArrayList<SDPMongoOrderElement>)userOrderBy).size();kkk++) {
					SDPMongoOrderElement curOrdElem=(SDPMongoOrderElement)((ArrayList<SDPMongoOrderElement>)userOrderBy).get(kkk);
					if (null==dbObjUserOrder) dbObjUserOrder=new BasicDBObject(curOrdElem.getNomeCampo(),curOrdElem.getOrdine());
					else dbObjUserOrder.append(curOrdElem.getNomeCampo(),curOrdElem.getOrdine());
				}
				starTtime=System.currentTimeMillis();
				cursor = collMisure.find(query).sort(dbObjUserOrder).skip(skip).limit(limit);
				try {
					deltaTime=System.currentTimeMillis()-starTtime;
				} catch (Exception e) {}
			}
			else {
				starTtime=System.currentTimeMillis();
				cursor = collMisure.find(query).skip(skip).limit(limit);
				try {
					deltaTime=System.currentTimeMillis()-starTtime;
				} catch (Exception e) {}

			}
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] total data query executed in --> "+deltaTime);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] count --> "+cursor.count());
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] limit --> "+limit);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] skip --> "+skip);



			DBObject obj=null;
			starTtime=System.currentTimeMillis();


			try {
				while (cursor.hasNext() ) {

					//log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] ciclo  recCount--> "+recCount);

					obj=cursor.next();




					String internalID=obj.get("_id").toString();
					String datasetVersion=takeNvlValues(obj.get("datasetVersion"));
					//					String current=takeNvlValues(obj.get("current"));



					Map<String, Object> misura = new HashMap<String, Object>();
					misura.put("internalId",  internalID);

					if (DATA_TYPE_MEASURE.equals(datatType)) {
						String streamId=obj.get("streamCode").toString();
						String sensorId=obj.get("sensor").toString();
						misura.put("streamCode", streamId);
						misura.put("sensor", sensorId);
						misura.put("time",  obj.get("time"));
					}					


					String iddataset=takeNvlValues(obj.get("idDataset"));
					if (null!= iddataset ) misura.put("idDataset",  Integer.parseInt(iddataset));
					if (null!= datasetVersion ) misura.put("datasetVersion",  Integer.parseInt(datasetVersion));



					ArrayList<String> elencoBinaryId=new ArrayList<String>();
					for (int i=0;i<compPropsTot.size();i++) {

						String chiave=compPropsTot.get(i).getName();
						if (obj.keySet().contains(chiave) ) {
							String  valore=takeNvlValues(obj.get(chiave));
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
									Object dataObj=obj.get(chiave);
									misura.put(chiave, dataObj);
								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.DateTime)) {
									//Sun Oct 19 07:01:17 CET 1969
									//EEE MMM dd HH:mm:ss zzz yyyy
									Object dataObj=obj.get(chiave);

									//System.out.println("------------------------------"+dataObj.getClass().getName());

									misura.put(chiave, dataObj);


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
									elencoBinaryId.add((String)valore);

								}
							}
						}
					}					
					if (elencoBinaryId.size()>0) misura.put("____binaryIdsArray", elencoBinaryId);

					ret.add(misura);



				}	

				try {
					deltaTime=System.currentTimeMillis()-starTtime;
				} catch (Exception e) {}
				log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] total fetch in --> "+deltaTime);


			} catch (Exception e) {
				throw e;
			}  finally {
				cursor.close();			
			} 


		} catch (SDPOrderBySizeException e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStream] SDPOrderBySizeException",e);
			throw (SDPOrderBySizeException)e;
		} catch (SDPPageSizeException e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStream] SDPPageSizeException",e);
			throw (SDPPageSizeException)e;
		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStream] GenericException",e);
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStream] INGORED" +e);
		} finally {
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] END");
		}


		SDPDataResult outres= new SDPDataResult(ret, cnt);
		return outres;
	}		


	public BasicDBList getMetadataComponents(DBObject streamMetadata) {
		List<Property> compPropsTot=new ArrayList<Property>();

		List<Property> compPropsCur=new ArrayList<Property>();			

		Object eleCapmpi=((DBObject)streamMetadata.get("info")).get("fields");
		BasicDBList lista=new BasicDBList();

		BasicDBList campiDbList= getDatasetFiledsDbList(eleCapmpi);
		for (int k=0;k<campiDbList.size();k++) {
			boolean present=false;
			compPropsCur=getDatasetFiledsOdataPros(campiDbList.get(k));
			for (int i=0;i<compPropsTot.size();i++) {
				if (compPropsTot.get(i).getName().equals(compPropsCur.get(0).getName())) present=true;

			}
			// TODO nel caso in cui present= true si potrebbe verficare il tipo che abbiamo in compsproptot con quello in  campiDbList.get(k)
			// sollevando eccezione se son diversi
			if (!present) {
				compPropsTot.add(compPropsCur.get(0));
				lista.add(campiDbList.get(k));
			}
		}

		return lista;
	}

	public SDPDataResult getMeasuresPerStreamNewLimitSolr_cusordef(String codiceTenant, String nameSpace, EdmEntityContainer entityContainer,DBObject streamMetadata,String internalId,String datatType,Object userQuery, Object userOrderBy,
			int skipI,
			int limitI
			) throws SDPOrderBySizeException,SDPPageSizeException {
		String collection=null;
		//		String sensore=null;
		//		String stream=null;
		String idDataset=null;
		String datasetCode=null;
		String datasetToFindVersion=null;
		List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();
		long cnt = 0;
		long skipL=skipI;
		long limitL=limitI;


		// TODO YUCCA-74 odata evoluzione

		try {
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] BEGIN");
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] codiceTenant="+codiceTenant);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] nameSpace="+nameSpace);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] entityContainer="+entityContainer);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] internalId="+internalId);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] datatType="+datatType);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] userQuery="+userQuery);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] streamMetadata="+streamMetadata);
			String solrCollection= codiceTenant;
			String codiceTenantOrig=codiceTenant;

			List<Property> compPropsTot=new ArrayList<Property>();
			List<Property> compPropsCur=new ArrayList<Property>();			

			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] limit_init --> "+skipL);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] skip_init --> "+skipL);



			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero di collencion,host, port, db specifici per il dataset
			//       - modificare eventualmente la logica di recupero dell'idDataset
			//INVARIATO!!

			collection=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("collection") );
			String host=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("host"));
			String port =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("port") );
			String dbcfg =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("db") );
			idDataset=takeNvlValues(streamMetadata.get("idDataset"));
			datasetCode=takeNvlValues(streamMetadata.get("datasetCode"));


			String streamSubtype=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("subtype") );


			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * ATTENZIONE!!!!!! datasetVersion sarà un array da mettere in in
			 */

			datasetToFindVersion=takeNvlValues(streamMetadata.get("datasetVersion"));


			//TODO calcolarlo in base a stream subtype

			//			if (null==collection || collection.trim().length()<=0) {
			//				DbConfDto tanantDbCfg=new DbConfDto();
			//
			//				collection=tanantDbCfg.getCollection();
			//				host=tanantDbCfg.getHost();
			//				port=""+tanantDbCfg.getPort();
			//				dbcfg=tanantDbCfg.getDataBase();
			//			}

			if (null==dbcfg || dbcfg.trim().length()<=0) {
				DbConfDto tanantDbCfg=new DbConfDto();

				if (DATA_TYPE_MEASURE.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_MESURES_SOLR, codiceTenantOrig);
				} else if (DATA_TYPE_DATA.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_DATA_SOLR, codiceTenantOrig);
				}  else if (DATA_TYPE_SOCIAL.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_SOCIAL_SOLR, codiceTenantOrig);

				}

				dbcfg=tanantDbCfg.getDataBase();
				collection=tanantDbCfg.getCollection();
			}


			if (null==collection || collection.trim().length()<=0) {
				//TODO aggoungere int per integrazione

				collection="sdp_"+SDPDataApiConfig.getInstance().getSdpAmbiente()+codiceTenant;

				if (DATA_TYPE_MEASURE.equals(datatType)) {
					collection+="_measures";

				} else if (DATA_TYPE_DATA.equals(datatType)) {
					collection+="_data";
				} else if (DATA_TYPE_SOCIAL.equals(datatType)) {
					collection+="_social";
				}

			}

			//			host=SDPDataApiConfig.getInstance().getMongoDefaultHost();
			//			port=""+SDPDataApiConfig.getInstance().getMongoDefaultPort();

			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero dell'elenco dei campi che contiene il join di info.fuields di tutte le versioni di quel dataset
			Object eleCapmpi=((DBObject)streamMetadata.get("info")).get("fields");


			HashMap<String, String> campoTipoMetadato= new HashMap<String, String>();

			BasicDBList lista=new BasicDBList();
			BasicDBList campiDbList= getDatasetFiledsDbList(eleCapmpi);
			for (int k=0;k<campiDbList.size();k++) {
				boolean present=false;
				compPropsCur=getDatasetFiledsOdataPros(campiDbList.get(k));

				for (int i=0;i<compPropsTot.size();i++) {
					if (compPropsTot.get(i).getName().equals(compPropsCur.get(0).getName())) present=true;

				}
				// TODO nel caso in cui present= true si potrebbe verficare il tipo che abbiamo in compsproptot con quello in  campiDbList.get(k)
				// sollevando eccezione se son diversi
				if (!present) {
					compPropsTot.add(compPropsCur.get(0));
					lista.add(campiDbList.get(k));
				}
			}
			for (int i=0; i<lista.size();i++) {
				BasicDBObject cur= (BasicDBObject)lista.get(i);
				String nome=cur.getString("fieldName");
				String tipo=cur.getString("dataType");
				campoTipoMetadato.put(nome, tipo);

			}


			DBCursor cursor=null;

			//MongoClient mongoClient = getMongoClient(host,Integer.parseInt(port));			
			//			DB db = mongoClient.getDB(dbcfg);
			//			DBCollection collMisure = db.getCollection(collection);

			DBCollection collMisure =null;

			String queryTotSolr="(iddataset_l:"+idDataset+")";
			String queryTotCntSolr="(iddataset_l:"+idDataset+")";
			//String queryTotSolr="(idDataset_l:"+idDataset+")";
			//String queryTotCntSolr="(idDataset_l:"+idDataset+")";



			//			BasicDBList queryTot=new BasicDBList();
			//			BasicDBList queryTotCnt=new BasicDBList();

			//			queryTot.add( new BasicDBObject("idDataset",new Integer(new Double(idDataset).intValue())));
			//			queryTotCnt.add( new BasicDBObject("idDataset",new Integer(new Double(idDataset).intValue())));


			ArrayList<Integer> vals = new ArrayList<Integer>();

			List<DBObject> listDatasetVersion = (List<DBObject>) streamMetadata.get("listDatasetVersion");
			Iterator<DBObject> listaIterator = listDatasetVersion.iterator();
			while (listaIterator.hasNext()) {
				DBObject el = listaIterator.next();
				Integer tmpVersDSCode = (Integer) el.get("datasetVersion"+datasetCode);
				vals.add(tmpVersDSCode);
			}

			//queryTot.add( new BasicDBObject("datasetVersion", new BasicDBObject("$gt", 0)));

			//queryTotSolr+= " AND (datasetVersion_l : [ 0 TO * ])";
			queryTotSolr+= " AND (datasetversion_l : [ 0 TO * ])";

			if (null!=internalId) {
				//				queryTot.add( new BasicDBObject("_id",new ObjectId(internalId)));
				//				queryTotCnt.add( new BasicDBObject("_id",new ObjectId(internalId)));
				queryTotSolr += "AND (id : "+internalId+")";
				queryTotCntSolr += "AND (id : "+internalId+")";

			}
			if (null != userQuery) {
				log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] userQuery="+userQuery);
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

			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] total data query ="+query);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] collection ="+collection);

			//yucca-1080
			//			queryTotSolr=queryTotSolr.toLowerCase().replaceAll("iddataset_l", "idDataset_l").replaceAll("datasetversion_l", "datasetVersion_l");
			//			queryTotCntSolr=queryTotCntSolr.toLowerCase().replaceAll("iddataset_l", "idDataset_l").replaceAll("datasetversion_l", "datasetVersion_l");



			long starTtime=0;
			long deltaTime=-1;


			if (skipL<0) skipL=0;

			//controlli sui valoris massimi ammessi 
			if(skipL>0 && skipL>SDPDataApiConfig.getInstance().getMaxSkipPages()) throw new SDPPageSizeException("invalid skip value: max skip = "+SDPDataApiConfig.getInstance().getMaxSkipPages(),Locale.UK);
			if(limitL> 0 && limitL>SDPDataApiConfig.getInstance().getMaxDocumentPerPage()) throw new SDPPageSizeException("invalid top value: max document per page = "+SDPDataApiConfig.getInstance().getMaxDocumentPerPage(),Locale.UK);



			BasicDBObject dbObjUserOrder=null;
			ArrayList<SortClause> orderSolr=null;
			if (null!=userOrderBy) {
				for (int kkk=0;kkk<((ArrayList<String>)userOrderBy).size();kkk++) {
					if (null==orderSolr) orderSolr=new ArrayList<SortClause>();
					//yucca-1080
					SortClause cc=((ArrayList<SortClause>)userOrderBy).get(kkk);
					orderSolr.add(new SortClause(cc.getItem().toLowerCase(),cc.getOrder()));
					//orderSolr.add(((ArrayList<SortClause>)userOrderBy).get(kkk));
				}			
			}
			if (null==orderSolr) orderSolr=new ArrayList<SolrQuery.SortClause>();
			SortClause sortId=new SortClause("id", ORDER.asc);
			orderSolr.add(sortId);

			/* CONTEGGIO */
			String cursorMark = CursorMarkParams.CURSOR_MARK_START;
			CloudSolrClient solrServer =  CloudSolrSingleton.getServer();	

			SolrQuery solrQuery = new SolrQuery();
			solrQuery.setQuery("*:*");
			solrQuery.setFilterQueries(queryTotCntSolr);
			solrQuery.setRows((skipL==0 ? 0 : new Long(skipL).intValue()));
			solrQuery.setFields("id");
			if (null!=orderSolr) solrQuery.setSorts(orderSolr);
			solrQuery.set( CursorMarkParams.CURSOR_MARK_PARAM, cursorMark );
			


			starTtime=System.currentTimeMillis();
			QueryResponse rsp = solrServer.query(collection,solrQuery);

			//SolrDocumentList aaa = (SolrDocumentList)rsp.getResponse().get("response");
			SolrDocumentList aaa =rsp.getResults();
			cnt = aaa.getNumFound();		
			String nextCursorMark = rsp.getNextCursorMark();


			try {
				deltaTime=System.currentTimeMillis()-starTtime;
			} catch (Exception e) {}
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] total data query COUNT executed in --> "+deltaTime + "nrec:"+cnt+"    nextCursorMark:"+nextCursorMark);
			 

			starTtime=0;
			deltaTime=-1;


			/** nuovi controlli skip e limit **/


			//se lo skip porta oltre il numero di risultati eccezione
			if (skipL>cnt) throw new SDPPageSizeException("skip value out of range: max document in query result = "+cnt,Locale.UK);

			if (limitL<0) {

				// se limit non valorizzato si restituisce tutto il resultset (limit=cnt) e si solleva eccezione se il resulset supera il numero massimo di risultati per pagina
				if ((cnt>SDPDataApiConfig.getInstance().getMaxDocumentPerPage())) throw new SDPPageSizeException("too many documents; use top parameter: max document per page = "+SDPDataApiConfig.getInstance().getMaxDocumentPerPage(),Locale.UK);
				limitL=cnt;
			} 


			if (limitL>0 && limitL>(cnt-skipL)) limitL=cnt-skipL;








			if (null!=userOrderBy) {

				boolean orderByAllowed=false;
				if (cnt<SDPDataApiConstants.SDP_MAX_DOC_FOR_ORDERBY) {
					orderByAllowed=true;
				} else if ((DATA_TYPE_MEASURE.equals(datatType) || DATA_TYPE_SOCIAL.equals(datatType) )&& ((ArrayList<SDPMongoOrderElement>)userOrderBy).size()<=1) {

					SortClause elemOrder=(SortClause)((ArrayList<SortClause>)userOrderBy).get(0);
					if (elemOrder.getItem().equalsIgnoreCase("time_dt")) orderByAllowed=true;
					if (elemOrder.getItem().equalsIgnoreCase("retweetParentId") && ("socialDataset".equalsIgnoreCase(streamSubtype))) orderByAllowed=true;
					if (elemOrder.getItem().equalsIgnoreCase("userId") && ("socialDataset".equalsIgnoreCase(streamSubtype))) orderByAllowed=true;
					if (elemOrder.getItem().equalsIgnoreCase("id")) orderByAllowed=true;
				} else if (DATA_TYPE_DATA.equals(datatType) && ((ArrayList<SDPMongoOrderElement>)userOrderBy).size()<=1) {
					SDPMongoOrderElement elemOrder=(SDPMongoOrderElement)((ArrayList<SDPMongoOrderElement>)userOrderBy).get(0);
					if (elemOrder.getNomeCampo().equalsIgnoreCase("id")) orderByAllowed=true;
				}


				if (!orderByAllowed) throw new SDPOrderBySizeException("too many documents for order clause;",Locale.UK);






			}








			



			//long passo= skipL;
			long passo= Long.parseLong("25000");

			
			boolean done = false;

			solrQuery = new SolrQuery();
			solrQuery.setQuery("*:*");
			solrQuery.setFilterQueries(queryTotSolr);
			solrQuery.setFields(null);
			solrQuery.setRows(new Integer( new Long(limitL).intValue()));
			if (null!=orderSolr) solrQuery.setSorts(orderSolr);
			solrQuery.set( CursorMarkParams.CURSOR_MARK_PARAM, skipL==0 ? cursorMark : nextCursorMark );


			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] orderby ="+orderSolr);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] limit --> "+limitL);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] skip --> "+skipL);



			
				starTtime=System.currentTimeMillis();

				rsp = solrServer.query(collection,solrQuery);
				SolrDocumentList results = rsp.getResults();
				try {
					deltaTime=System.currentTimeMillis()-starTtime;
				} catch (Exception e) {}



				log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] total data query executed in --> "+deltaTime + "  nrec:"+results.getNumFound()+"     nextcur:"+nextCursorMark);				



					DBObject obj=null;
					SolrDocument curSolrDoc=null;
					starTtime=System.currentTimeMillis();
					try {
						for (int j = 0; j < results.size(); ++j) {
							curSolrDoc=results.get(j);


							String internalID=curSolrDoc.get("id").toString();
							//String datasetVersion=takeNvlValues(curSolrDoc.get("datasetVersion_l"));
							String datasetVersion=takeNvlValues(curSolrDoc.get("datasetversion_l"));
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
							String iddataset=takeNvlValues(curSolrDoc.get("iddataset_l"));
							if (null!= iddataset ) misura.put("idDataset",  Integer.parseInt(iddataset));
							if (null!= datasetVersion ) misura.put("datasetVersion",  Integer.parseInt(datasetVersion));


							ArrayList<String> elencoBinaryId=new ArrayList<String>();
							for (int i=0;i<compPropsTot.size();i++) {

								String chiave=compPropsTot.get(i).getName();
								String chiaveL=getPropertyName(compPropsTot.get(i));

								chiaveL=compPropsTot.get(i).getName()+SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(campoTipoMetadato.get(compPropsTot.get(i).getName()));



								//
								//						if (curSolrDoc.keySet().contains(chiaveL) ) {
								//							Object oo = curSolrDoc.get(chiaveL);
								//
								//							String  valore=takeNvlValues(curSolrDoc.get(chiaveL));
								//							
								if (curSolrDoc.keySet().contains(chiaveL.toLowerCase()) ) {
									Object oo = curSolrDoc.get(chiaveL.toLowerCase());

									String  valore=takeNvlValues(curSolrDoc.get(chiaveL.toLowerCase()));							
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
											Object dataObj=obj.get(chiave);

											//System.out.println("------------------------------"+dataObj.getClass().getName());

											misura.put(chiave, dataObj);


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
						log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] total fetch in --> "+deltaTime);


					} catch (Exception e) {
						throw e;
					}  finally {
						//cursor.close();			
					} 					



					

			














		} catch (SDPOrderBySizeException e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] SDPOrderBySizeException",e);
			throw (SDPOrderBySizeException)e;
		} catch (SDPPageSizeException e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] SDPPageSizeException",e);
			throw (SDPPageSizeException)e;
		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] GenericException",e);
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] INGORED" +e);
		} finally {
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] END");
		}


		SDPDataResult outres= new SDPDataResult(ret, cnt);
		return outres;
	}					

	public SDPDataResult getMeasuresPerStreamNewLimitSolr_provecursor(String codiceTenant, String nameSpace, EdmEntityContainer entityContainer,DBObject streamMetadata,String internalId,String datatType,Object userQuery, Object userOrderBy,
			int skipI,
			int limitI
			) throws SDPOrderBySizeException,SDPPageSizeException {
		String collection=null;
		//		String sensore=null;
		//		String stream=null;
		String idDataset=null;
		String datasetCode=null;
		String datasetToFindVersion=null;
		List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();
		long cnt = 0;
		long skipL=skipI;
		long limitL=limitI;


		// TODO YUCCA-74 odata evoluzione

		try {
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] BEGIN");
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] codiceTenant="+codiceTenant);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] nameSpace="+nameSpace);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] entityContainer="+entityContainer);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] internalId="+internalId);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] datatType="+datatType);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] userQuery="+userQuery);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] streamMetadata="+streamMetadata);
			String solrCollection= codiceTenant;
			String codiceTenantOrig=codiceTenant;

			List<Property> compPropsTot=new ArrayList<Property>();
			List<Property> compPropsCur=new ArrayList<Property>();			

			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] limit_init --> "+skipL);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] skip_init --> "+skipL);



			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero di collencion,host, port, db specifici per il dataset
			//       - modificare eventualmente la logica di recupero dell'idDataset
			//INVARIATO!!

			collection=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("collection") );
			String host=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("host"));
			String port =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("port") );
			String dbcfg =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("db") );
			idDataset=takeNvlValues(streamMetadata.get("idDataset"));
			datasetCode=takeNvlValues(streamMetadata.get("datasetCode"));


			String streamSubtype=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("subtype") );


			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * ATTENZIONE!!!!!! datasetVersion sarà un array da mettere in in
			 */

			datasetToFindVersion=takeNvlValues(streamMetadata.get("datasetVersion"));


			//TODO calcolarlo in base a stream subtype

			//			if (null==collection || collection.trim().length()<=0) {
			//				DbConfDto tanantDbCfg=new DbConfDto();
			//
			//				collection=tanantDbCfg.getCollection();
			//				host=tanantDbCfg.getHost();
			//				port=""+tanantDbCfg.getPort();
			//				dbcfg=tanantDbCfg.getDataBase();
			//			}

			if (null==dbcfg || dbcfg.trim().length()<=0) {
				DbConfDto tanantDbCfg=new DbConfDto();

				if (DATA_TYPE_MEASURE.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_MESURES_SOLR, codiceTenantOrig);
				} else if (DATA_TYPE_DATA.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_DATA_SOLR, codiceTenantOrig);
				}  else if (DATA_TYPE_SOCIAL.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_SOCIAL_SOLR, codiceTenantOrig);

				}

				dbcfg=tanantDbCfg.getDataBase();
				collection=tanantDbCfg.getCollection();
			}


			if (null==collection || collection.trim().length()<=0) {
				//TODO aggoungere int per integrazione

				collection="sdp_"+SDPDataApiConfig.getInstance().getSdpAmbiente()+codiceTenant;

				if (DATA_TYPE_MEASURE.equals(datatType)) {
					collection+="_measures";

				} else if (DATA_TYPE_DATA.equals(datatType)) {
					collection+="_data";
				} else if (DATA_TYPE_SOCIAL.equals(datatType)) {
					collection+="_social";
				}

			}

			//			host=SDPDataApiConfig.getInstance().getMongoDefaultHost();
			//			port=""+SDPDataApiConfig.getInstance().getMongoDefaultPort();

			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero dell'elenco dei campi che contiene il join di info.fuields di tutte le versioni di quel dataset
			Object eleCapmpi=((DBObject)streamMetadata.get("info")).get("fields");


			HashMap<String, String> campoTipoMetadato= new HashMap<String, String>();

			BasicDBList lista=new BasicDBList();
			BasicDBList campiDbList= getDatasetFiledsDbList(eleCapmpi);
			for (int k=0;k<campiDbList.size();k++) {
				boolean present=false;
				compPropsCur=getDatasetFiledsOdataPros(campiDbList.get(k));

				for (int i=0;i<compPropsTot.size();i++) {
					if (compPropsTot.get(i).getName().equals(compPropsCur.get(0).getName())) present=true;

				}
				// TODO nel caso in cui present= true si potrebbe verficare il tipo che abbiamo in compsproptot con quello in  campiDbList.get(k)
				// sollevando eccezione se son diversi
				if (!present) {
					compPropsTot.add(compPropsCur.get(0));
					lista.add(campiDbList.get(k));
				}
			}
			for (int i=0; i<lista.size();i++) {
				BasicDBObject cur= (BasicDBObject)lista.get(i);
				String nome=cur.getString("fieldName");
				String tipo=cur.getString("dataType");
				campoTipoMetadato.put(nome, tipo);

			}


			DBCursor cursor=null;

			//MongoClient mongoClient = getMongoClient(host,Integer.parseInt(port));			
			//			DB db = mongoClient.getDB(dbcfg);
			//			DBCollection collMisure = db.getCollection(collection);

			DBCollection collMisure =null;

			String queryTotSolr="(iddataset_l:"+idDataset+")";
			String queryTotCntSolr="(iddataset_l:"+idDataset+")";
			//String queryTotSolr="(idDataset_l:"+idDataset+")";
			//String queryTotCntSolr="(idDataset_l:"+idDataset+")";



			//			BasicDBList queryTot=new BasicDBList();
			//			BasicDBList queryTotCnt=new BasicDBList();

			//			queryTot.add( new BasicDBObject("idDataset",new Integer(new Double(idDataset).intValue())));
			//			queryTotCnt.add( new BasicDBObject("idDataset",new Integer(new Double(idDataset).intValue())));


			ArrayList<Integer> vals = new ArrayList<Integer>();

			List<DBObject> listDatasetVersion = (List<DBObject>) streamMetadata.get("listDatasetVersion");
			Iterator<DBObject> listaIterator = listDatasetVersion.iterator();
			while (listaIterator.hasNext()) {
				DBObject el = listaIterator.next();
				Integer tmpVersDSCode = (Integer) el.get("datasetVersion"+datasetCode);
				vals.add(tmpVersDSCode);
			}

			//queryTot.add( new BasicDBObject("datasetVersion", new BasicDBObject("$gt", 0)));

			//queryTotSolr+= " AND (datasetVersion_l : [ 0 TO * ])";
			queryTotSolr+= " AND (datasetversion_l : [ 0 TO * ])";

			if (null!=internalId) {
				//				queryTot.add( new BasicDBObject("_id",new ObjectId(internalId)));
				//				queryTotCnt.add( new BasicDBObject("_id",new ObjectId(internalId)));
				queryTotSolr += "AND (id : "+internalId+")";
				queryTotCntSolr += "AND (id : "+internalId+")";

			}
			if (null != userQuery) {
				log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] userQuery="+userQuery);
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

			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] total data query ="+query);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] collection ="+collection);

			//yucca-1080
			//			queryTotSolr=queryTotSolr.toLowerCase().replaceAll("iddataset_l", "idDataset_l").replaceAll("datasetversion_l", "datasetVersion_l");
			//			queryTotCntSolr=queryTotCntSolr.toLowerCase().replaceAll("iddataset_l", "idDataset_l").replaceAll("datasetversion_l", "datasetVersion_l");



			CloudSolrClient solrServer =  CloudSolrSingleton.getServer();	

			//HttpSolrServer solrServer = new HttpSolrServer( "http://sdnet-solr.sdp.csi.it:8983/solr/"+codiceTenant+"/" );
			//HttpSolrServer solrServer = new HttpSolrServer( "http://sdnet-solr.sdp.csi.it:8983/solr/" );
			SolrQuery solrQuery = new SolrQuery();
			solrQuery.setQuery("*:*");
			solrQuery.setFilterQueries(queryTotCntSolr);
			solrQuery.setRows(1);



			long starTtime=0;
			long deltaTime=-1;


			starTtime=System.currentTimeMillis();
			QueryResponse rsp = solrServer.query(collection,solrQuery);
			//cnt = collMisure.find( new BasicDBObject("$and", queryTotCnt)).count();

			SolrDocumentList aaa = (SolrDocumentList)rsp.getResponse().get("response");

			cnt = aaa.getNumFound();		



			try {
				deltaTime=System.currentTimeMillis()-starTtime;
			} catch (Exception e) {}
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] total data query COUNT executed in --> "+deltaTime);


			starTtime=0;
			deltaTime=-1;


			/** nuovi controlli skip e limit **/
			if (skipL<0) skipL=0;

			//controlli sui valoris massimi ammessi 
			if(skipL>0 && skipL>SDPDataApiConfig.getInstance().getMaxSkipPages()) throw new SDPPageSizeException("invalid skip value: max skip = "+SDPDataApiConfig.getInstance().getMaxSkipPages(),Locale.UK);
			if(limitL> 0 && limitL>SDPDataApiConfig.getInstance().getMaxDocumentPerPage()) throw new SDPPageSizeException("invalid top value: max document per page = "+SDPDataApiConfig.getInstance().getMaxDocumentPerPage(),Locale.UK);


			//se lo skip porta oltre il numero di risultati eccezione
			if (skipL>cnt) throw new SDPPageSizeException("skip value out of range: max document in query result = "+cnt,Locale.UK);

			if (limitL<0) {

				// se limit non valorizzato si restituisce tutto il resultset (limit=cnt) e si solleva eccezione se il resulset supera il numero massimo di risultati per pagina
				if ((cnt>SDPDataApiConfig.getInstance().getMaxDocumentPerPage())) throw new SDPPageSizeException("too many documents; use top parameter: max document per page = "+SDPDataApiConfig.getInstance().getMaxDocumentPerPage(),Locale.UK);
				limitL=cnt;
			} 


			if (limitL>0 && limitL>(cnt-skipL)) limitL=cnt-skipL;





			ArrayList<SortClause> orderSolr=null;


			if (null!=userOrderBy) {

				boolean orderByAllowed=false;
				if (cnt<SDPDataApiConstants.SDP_MAX_DOC_FOR_ORDERBY) {
					orderByAllowed=true;
				} else if ((DATA_TYPE_MEASURE.equals(datatType) || DATA_TYPE_SOCIAL.equals(datatType) )&& ((ArrayList<SDPMongoOrderElement>)userOrderBy).size()<=1) {

					SortClause elemOrder=(SortClause)((ArrayList<SortClause>)userOrderBy).get(0);
					if (elemOrder.getItem().equalsIgnoreCase("time_dt")) orderByAllowed=true;
					if (elemOrder.getItem().equalsIgnoreCase("retweetParentId") && ("socialDataset".equalsIgnoreCase(streamSubtype))) orderByAllowed=true;
					if (elemOrder.getItem().equalsIgnoreCase("userId") && ("socialDataset".equalsIgnoreCase(streamSubtype))) orderByAllowed=true;
					if (elemOrder.getItem().equalsIgnoreCase("id")) orderByAllowed=true;
				} else if (DATA_TYPE_DATA.equals(datatType) && ((ArrayList<SDPMongoOrderElement>)userOrderBy).size()<=1) {
					SDPMongoOrderElement elemOrder=(SDPMongoOrderElement)((ArrayList<SDPMongoOrderElement>)userOrderBy).get(0);
					if (elemOrder.getNomeCampo().equalsIgnoreCase("id")) orderByAllowed=true;
				}


				if (!orderByAllowed) throw new SDPOrderBySizeException("too many documents for order clause;",Locale.UK);

				BasicDBObject dbObjUserOrder=null;

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








			if (null==orderSolr) orderSolr=new ArrayList<SolrQuery.SortClause>();
			SortClause sortId=new SortClause("id", ORDER.asc);
			orderSolr.add(sortId);



			//long passo= skipL;
			long passo= Long.parseLong("25000");

			String cursorMark = CursorMarkParams.CURSOR_MARK_START;
			boolean done = false;

			solrQuery = new SolrQuery();
			solrQuery.setQuery("*:*");
			solrQuery.setFilterQueries(queryTotSolr);
			//solrQuery.setRows(new Integer( new Long(limitL).intValue()));
			solrQuery.setRows(new Integer( new Long(passo).intValue()));
			solrQuery.setFields("id");
			if (null!=orderSolr) solrQuery.setSorts(orderSolr);

			long skippedDocs=passo;

			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] orderby ="+orderSolr);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] limit --> "+limitL);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] skip --> "+skipL);



			while (! done) {
				solrQuery.set( CursorMarkParams.CURSOR_MARK_PARAM, cursorMark );
				starTtime=System.currentTimeMillis();
				if (skippedDocs+passo>=skipL ) {
					solrQuery.setFields(null);
					solrQuery.setRows(new Integer( new Long(limitL).intValue()));
				}

				rsp = solrServer.query(collection,solrQuery);
				SolrDocumentList results = rsp.getResults();

				try {
					deltaTime=System.currentTimeMillis()-starTtime;
				} catch (Exception e) {}



				String nextCursorMark = rsp.getNextCursorMark();
				log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] total data query executed in ("+skippedDocs+")--> "+deltaTime + "  nrec:"+results.getNumFound()+"     nextcur:"+nextCursorMark);				


				if (skippedDocs>=skipL) {

					done = true;

					DBObject obj=null;
					SolrDocument curSolrDoc=null;
					starTtime=System.currentTimeMillis();
					try {
						for (int j = 0; j < results.size(); ++j) {
							curSolrDoc=results.get(j);


							String internalID=curSolrDoc.get("id").toString();
							//String datasetVersion=takeNvlValues(curSolrDoc.get("datasetVersion_l"));
							String datasetVersion=takeNvlValues(curSolrDoc.get("datasetversion_l"));
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
							String iddataset=takeNvlValues(curSolrDoc.get("iddataset_l"));
							if (null!= iddataset ) misura.put("idDataset",  Integer.parseInt(iddataset));
							if (null!= datasetVersion ) misura.put("datasetVersion",  Integer.parseInt(datasetVersion));


							ArrayList<String> elencoBinaryId=new ArrayList<String>();
							for (int i=0;i<compPropsTot.size();i++) {

								String chiave=compPropsTot.get(i).getName();
								String chiaveL=getPropertyName(compPropsTot.get(i));

								chiaveL=compPropsTot.get(i).getName()+SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(campoTipoMetadato.get(compPropsTot.get(i).getName()));



								//
								//						if (curSolrDoc.keySet().contains(chiaveL) ) {
								//							Object oo = curSolrDoc.get(chiaveL);
								//
								//							String  valore=takeNvlValues(curSolrDoc.get(chiaveL));
								//							
								if (curSolrDoc.keySet().contains(chiaveL.toLowerCase()) ) {
									Object oo = curSolrDoc.get(chiaveL.toLowerCase());

									String  valore=takeNvlValues(curSolrDoc.get(chiaveL.toLowerCase()));							
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
											Object dataObj=obj.get(chiave);

											//System.out.println("------------------------------"+dataObj.getClass().getName());

											misura.put(chiave, dataObj);


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
						log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] total fetch in --> "+deltaTime);


					} catch (Exception e) {
						throw e;
					}  finally {
						//cursor.close();			
					} 					



				}

				if (cursorMark.equals(nextCursorMark)) {
					done = true;
				}
				skippedDocs=skippedDocs+passo;

				cursorMark = nextCursorMark;				

			}














		} catch (SDPOrderBySizeException e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] SDPOrderBySizeException",e);
			throw (SDPOrderBySizeException)e;
		} catch (SDPPageSizeException e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] SDPPageSizeException",e);
			throw (SDPPageSizeException)e;
		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] GenericException",e);
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] INGORED" +e);
		} finally {
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] END");
		}


		SDPDataResult outres= new SDPDataResult(ret, cnt);
		return outres;
	}				

	public SDPDataResult getMeasuresPerStreamNewLimitSolr(String codiceTenant, String nameSpace, EdmEntityContainer entityContainer,DBObject streamMetadata,String internalId,String datatType,Object userQuery, Object userOrderBy,
			int skipI,
			int limitI
			) throws SDPOrderBySizeException,SDPPageSizeException {
		String collection=null;
		//		String sensore=null;
		//		String stream=null;
		String idDataset=null;
		String datasetCode=null;
		String datasetToFindVersion=null;
		List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();
		long cnt = 0;
		long skipL=skipI;
		long limitL=limitI;


		// TODO YUCCA-74 odata evoluzione

		try {
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] BEGIN");
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] codiceTenant="+codiceTenant);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] nameSpace="+nameSpace);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] entityContainer="+entityContainer);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] internalId="+internalId);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] datatType="+datatType);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] userQuery="+userQuery);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] streamMetadata="+streamMetadata);
			String solrCollection= codiceTenant;
			String codiceTenantOrig=codiceTenant;

			List<Property> compPropsTot=new ArrayList<Property>();
			List<Property> compPropsCur=new ArrayList<Property>();			

			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] limit_init --> "+skipL);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] skip_init --> "+skipL);



			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero di collencion,host, port, db specifici per il dataset
			//       - modificare eventualmente la logica di recupero dell'idDataset
			//INVARIATO!!

			collection=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("collection") );
			String host=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("host"));
			String port =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("port") );
			String dbcfg =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("db") );
			idDataset=takeNvlValues(streamMetadata.get("idDataset"));
			datasetCode=takeNvlValues(streamMetadata.get("datasetCode"));


			String streamSubtype=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("subtype") );


			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 * ATTENZIONE!!!!!! datasetVersion sarà un array da mettere in in
			 */

			datasetToFindVersion=takeNvlValues(streamMetadata.get("datasetVersion"));


			//TODO calcolarlo in base a stream subtype

			//			if (null==collection || collection.trim().length()<=0) {
			//				DbConfDto tanantDbCfg=new DbConfDto();
			//
			//				collection=tanantDbCfg.getCollection();
			//				host=tanantDbCfg.getHost();
			//				port=""+tanantDbCfg.getPort();
			//				dbcfg=tanantDbCfg.getDataBase();
			//			}

			if (null==dbcfg || dbcfg.trim().length()<=0) {
				DbConfDto tanantDbCfg=new DbConfDto();

				if (DATA_TYPE_MEASURE.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_MESURES_SOLR, codiceTenantOrig);
				} else if (DATA_TYPE_DATA.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_DATA_SOLR, codiceTenantOrig);
				}  else if (DATA_TYPE_SOCIAL.equals(datatType)) {
					tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_SOCIAL_SOLR, codiceTenantOrig);

				}

				dbcfg=tanantDbCfg.getDataBase();
				collection=tanantDbCfg.getCollection();
			}


			if (null==collection || collection.trim().length()<=0) {
				//TODO aggoungere int per integrazione

				collection="sdp_"+SDPDataApiConfig.getInstance().getSdpAmbiente()+codiceTenant;

				if (DATA_TYPE_MEASURE.equals(datatType)) {
					collection+="_measures";

				} else if (DATA_TYPE_DATA.equals(datatType)) {
					collection+="_data";
				} else if (DATA_TYPE_SOCIAL.equals(datatType)) {
					collection+="_social";
				}

			}

			//			host=SDPDataApiConfig.getInstance().getMongoDefaultHost();
			//			port=""+SDPDataApiConfig.getInstance().getMongoDefaultPort();

			// TODO YUCCA-74 odata evoluzione - dettaglio
			// l'oggetto streamMetadata camvbia (vedere SDPMongoOdataCast)
			//       - modificare eventualmente la logica di recupero dell'elenco dei campi che contiene il join di info.fuields di tutte le versioni di quel dataset
			Object eleCapmpi=((DBObject)streamMetadata.get("info")).get("fields");


			HashMap<String, String> campoTipoMetadato= new HashMap<String, String>();

			BasicDBList lista=new BasicDBList();
			BasicDBList campiDbList= getDatasetFiledsDbList(eleCapmpi);
			for (int k=0;k<campiDbList.size();k++) {
				boolean present=false;
				compPropsCur=getDatasetFiledsOdataPros(campiDbList.get(k));

				for (int i=0;i<compPropsTot.size();i++) {
					if (compPropsTot.get(i).getName().equals(compPropsCur.get(0).getName())) present=true;

				}
				// TODO nel caso in cui present= true si potrebbe verficare il tipo che abbiamo in compsproptot con quello in  campiDbList.get(k)
				// sollevando eccezione se son diversi
				if (!present) {
					compPropsTot.add(compPropsCur.get(0));
					lista.add(campiDbList.get(k));
				}
			}
			for (int i=0; i<lista.size();i++) {
				BasicDBObject cur= (BasicDBObject)lista.get(i);
				String nome=cur.getString("fieldName");
				String tipo=cur.getString("dataType");
				campoTipoMetadato.put(nome, tipo);

			}


			DBCursor cursor=null;

			//MongoClient mongoClient = getMongoClient(host,Integer.parseInt(port));			
			//			DB db = mongoClient.getDB(dbcfg);
			//			DBCollection collMisure = db.getCollection(collection);

			DBCollection collMisure =null;

			String queryTotSolr="(iddataset_l:"+idDataset+")";
			String queryTotCntSolr="(iddataset_l:"+idDataset+")";
			//String queryTotSolr="(idDataset_l:"+idDataset+")";
			//String queryTotCntSolr="(idDataset_l:"+idDataset+")";



			//			BasicDBList queryTot=new BasicDBList();
			//			BasicDBList queryTotCnt=new BasicDBList();

			//			queryTot.add( new BasicDBObject("idDataset",new Integer(new Double(idDataset).intValue())));
			//			queryTotCnt.add( new BasicDBObject("idDataset",new Integer(new Double(idDataset).intValue())));


			ArrayList<Integer> vals = new ArrayList<Integer>();

			List<DBObject> listDatasetVersion = (List<DBObject>) streamMetadata.get("listDatasetVersion");
			Iterator<DBObject> listaIterator = listDatasetVersion.iterator();
			while (listaIterator.hasNext()) {
				DBObject el = listaIterator.next();
				Integer tmpVersDSCode = (Integer) el.get("datasetVersion"+datasetCode);
				vals.add(tmpVersDSCode);
			}

			//queryTot.add( new BasicDBObject("datasetVersion", new BasicDBObject("$gt", 0)));

			//queryTotSolr+= " AND (datasetVersion_l : [ 0 TO * ])";
			queryTotSolr+= " AND (datasetversion_l : [ 0 TO * ])";

			if (null!=internalId) {
				//				queryTot.add( new BasicDBObject("_id",new ObjectId(internalId)));
				//				queryTotCnt.add( new BasicDBObject("_id",new ObjectId(internalId)));
				queryTotSolr += "AND (id : "+internalId+")";
				queryTotCntSolr += "AND (id : "+internalId+")";

			}
			if (null != userQuery) {
				log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] userQuery="+userQuery);
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

			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] total data query ="+query);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] collection ="+collection);

			//yucca-1080
			//			queryTotSolr=queryTotSolr.toLowerCase().replaceAll("iddataset_l", "idDataset_l").replaceAll("datasetversion_l", "datasetVersion_l");
			//			queryTotCntSolr=queryTotCntSolr.toLowerCase().replaceAll("iddataset_l", "idDataset_l").replaceAll("datasetversion_l", "datasetVersion_l");



			CloudSolrClient solrServer =  CloudSolrSingleton.getServer();	

			//HttpSolrServer solrServer = new HttpSolrServer( "http://sdnet-solr.sdp.csi.it:8983/solr/"+codiceTenant+"/" );
			//HttpSolrServer solrServer = new HttpSolrServer( "http://sdnet-solr.sdp.csi.it:8983/solr/" );
			SolrQuery solrQuery = new SolrQuery();
			solrQuery.setQuery("*:*");
			solrQuery.setFilterQueries(queryTotCntSolr);
			solrQuery.setRows(1);



			long starTtime=0;
			long deltaTime=-1;


			starTtime=System.currentTimeMillis();
			QueryResponse rsp = solrServer.query(collection,solrQuery);
			//cnt = collMisure.find( new BasicDBObject("$and", queryTotCnt)).count();

			SolrDocumentList aaa = (SolrDocumentList)rsp.getResponse().get("response");

			cnt = aaa.getNumFound();		



			try {
				deltaTime=System.currentTimeMillis()-starTtime;
			} catch (Exception e) {}
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] total data query COUNT executed in --> "+deltaTime);


			starTtime=0;
			deltaTime=-1;


			/** nuovi controlli skip e limit **/
			if (skipL<0) skipL=0;

			//controlli sui valoris massimi ammessi 
			if(skipL>0 && skipL>SDPDataApiConfig.getInstance().getMaxSkipPages()) throw new SDPPageSizeException("invalid skip value: max skip = "+SDPDataApiConfig.getInstance().getMaxSkipPages(),Locale.UK);
			if(limitL> 0 && limitL>SDPDataApiConfig.getInstance().getMaxDocumentPerPage()) throw new SDPPageSizeException("invalid top value: max document per page = "+SDPDataApiConfig.getInstance().getMaxDocumentPerPage(),Locale.UK);


			//se lo skip porta oltre il numero di risultati eccezione
			if (skipL>cnt) throw new SDPPageSizeException("skip value out of range: max document in query result = "+cnt,Locale.UK);

			if (limitL<0) {

				// se limit non valorizzato si restituisce tutto il resultset (limit=cnt) e si solleva eccezione se il resulset supera il numero massimo di risultati per pagina
				if ((cnt>SDPDataApiConfig.getInstance().getMaxDocumentPerPage())) throw new SDPPageSizeException("too many documents; use top parameter: max document per page = "+SDPDataApiConfig.getInstance().getMaxDocumentPerPage(),Locale.UK);
				limitL=cnt;
			} 


			if (limitL>0 && limitL>(cnt-skipL)) limitL=cnt-skipL;





			ArrayList<SortClause> orderSolr=null;



			if (null!=userOrderBy) {

				boolean orderByAllowed=false;
				if (cnt<SDPDataApiConstants.SDP_MAX_DOC_FOR_ORDERBY) {
					orderByAllowed=true;
				} else if ((DATA_TYPE_MEASURE.equals(datatType) || DATA_TYPE_SOCIAL.equals(datatType) )&& ((ArrayList<SDPMongoOrderElement>)userOrderBy).size()<=1) {

					SortClause elemOrder=(SortClause)((ArrayList<SortClause>)userOrderBy).get(0);
					if (elemOrder.getItem().equalsIgnoreCase("time_dt")) orderByAllowed=true;
					if (elemOrder.getItem().equalsIgnoreCase("retweetParentId") && ("socialDataset".equalsIgnoreCase(streamSubtype))) orderByAllowed=true;
					if (elemOrder.getItem().equalsIgnoreCase("userId") && ("socialDataset".equalsIgnoreCase(streamSubtype))) orderByAllowed=true;
					if (elemOrder.getItem().equalsIgnoreCase("id")) orderByAllowed=true;
				} else if (DATA_TYPE_DATA.equals(datatType) && ((ArrayList<SDPMongoOrderElement>)userOrderBy).size()<=1) {
					SDPMongoOrderElement elemOrder=(SDPMongoOrderElement)((ArrayList<SDPMongoOrderElement>)userOrderBy).get(0);
					if (elemOrder.getNomeCampo().equalsIgnoreCase("id")) orderByAllowed=true;
				}


				if (!orderByAllowed) throw new SDPOrderBySizeException("too many documents for order clause;",Locale.UK);

				BasicDBObject dbObjUserOrder=null;

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



			DBObject obj=null;
			starTtime=System.currentTimeMillis();

			rsp = solrServer.query(collection,solrQuery);
			SolrDocumentList results = rsp.getResults();


			try {
				deltaTime=System.currentTimeMillis()-starTtime;
			} catch (Exception e) {}
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] total data query executed in --> "+deltaTime);
			//log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] count --> "+cursor.count());
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] orderby ="+orderSolr);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] limit --> "+limitL);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] skip --> "+skipL);



			SolrDocument curSolrDoc=null;
			try {
				for (int j = 0; j < results.size(); ++j) {
					curSolrDoc=results.get(j);


					String internalID=curSolrDoc.get("id").toString();
					//String datasetVersion=takeNvlValues(curSolrDoc.get("datasetVersion_l"));
					String datasetVersion=takeNvlValues(curSolrDoc.get("datasetversion_l"));
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
					String iddataset=takeNvlValues(curSolrDoc.get("iddataset_l"));
					if (null!= iddataset ) misura.put("idDataset",  Integer.parseInt(iddataset));
					if (null!= datasetVersion ) misura.put("datasetVersion",  Integer.parseInt(datasetVersion));


					ArrayList<String> elencoBinaryId=new ArrayList<String>();
					for (int i=0;i<compPropsTot.size();i++) {

						String chiave=compPropsTot.get(i).getName();
						String chiaveL=getPropertyName(compPropsTot.get(i));

						chiaveL=compPropsTot.get(i).getName()+SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(campoTipoMetadato.get(compPropsTot.get(i).getName()));



						//
						//						if (curSolrDoc.keySet().contains(chiaveL) ) {
						//							Object oo = curSolrDoc.get(chiaveL);
						//
						//							String  valore=takeNvlValues(curSolrDoc.get(chiaveL));
						//							
						if (curSolrDoc.keySet().contains(chiaveL.toLowerCase()) ) {
							Object oo = curSolrDoc.get(chiaveL.toLowerCase());

							String  valore=takeNvlValues(curSolrDoc.get(chiaveL.toLowerCase()));							
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
									Object dataObj=obj.get(chiave);

									//System.out.println("------------------------------"+dataObj.getClass().getName());

									misura.put(chiave, dataObj);


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

				//				while (cursor.hasNext() ) {
				//
				//					//log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] ciclo  recCount--> "+recCount);
				//
				//					obj=cursor.next();
				//
				//
				//
				//
				//					String internalID=obj.get("_id").toString();
				//					String datasetVersion=takeNvlValues(obj.get("datasetVersion"));
				//					//					String current=takeNvlValues(obj.get("current"));
				//
				//
				//
				//					Map<String, Object> misura = new HashMap<String, Object>();
				//					misura.put("internalId",  internalID);
				//
				//					if (DATA_TYPE_MEASURE.equals(datatType)) {
				//						String streamId=obj.get("streamCode").toString();
				//						String sensorId=obj.get("sensor").toString();
				//						misura.put("streamCode", streamId);
				//						misura.put("sensor", sensorId);
				//						misura.put("time",  obj.get("time"));
				//					}					
				//
				//
				//					String iddataset=takeNvlValues(obj.get("idDataset"));
				//					if (null!= iddataset ) misura.put("idDataset",  Integer.parseInt(iddataset));
				//					if (null!= datasetVersion ) misura.put("datasetVersion",  Integer.parseInt(datasetVersion));
				//
				//
				//
				//					ArrayList<String> elencoBinaryId=new ArrayList<String>();
				//					for (int i=0;i<compPropsTot.size();i++) {
				//
				//						String chiave=compPropsTot.get(i).getName();
				//						if (obj.keySet().contains(chiave) ) {
				//							String  valore=takeNvlValues(obj.get(chiave));
				//							if (null!=valore) {
				//								if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Boolean)) {
				//									misura.put(chiave, Boolean.valueOf(valore));
				//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.String)) {
				//									misura.put(chiave, valore);
				//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Int32)) {
				//									misura.put(chiave, Integer.parseInt(valore));
				//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Int64)) {
				//									misura.put(chiave, Long.parseLong(valore));
				//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Double)) {
				//									misura.put(chiave, Double.parseDouble(valore));
				//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.DateTimeOffset)) {
				//									Object dataObj=obj.get(chiave);
				//									misura.put(chiave, dataObj);
				//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.DateTime)) {
				//									//Sun Oct 19 07:01:17 CET 1969
				//									//EEE MMM dd HH:mm:ss zzz yyyy
				//									Object dataObj=obj.get(chiave);
				//
				//									//System.out.println("------------------------------"+dataObj.getClass().getName());
				//
				//									misura.put(chiave, dataObj);
				//
				//
				//									//																 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				//									//															     Date data = dateFormat.parse(valore);								
				//									//																	misura.put(chiave, data);
				//
				//
				//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Decimal)) {
				//									//comppnenti.put(chiave, Float.parseFloat(valore));
				//									misura.put(chiave, Double.parseDouble(valore));
				//								} else if (((SimpleProperty)compPropsTot.get(i)).getType().equals(EdmSimpleTypeKind.Binary)) {
				//									Map<String, Object> mappaBinaryRef=new HashMap<String, Object>();
				//									mappaBinaryRef.put("idBinary", (String)valore);
				//									misura.put(chiave, mappaBinaryRef);
				//									elencoBinaryId.add((String)valore);
				//
				//								}
				//							}
				//						}
				//					}					
				//					if (elencoBinaryId.size()>0) misura.put("____binaryIdsArray", elencoBinaryId);
				//
				//					ret.add(misura);
				//
				//
				//
				//				}	

				try {
					deltaTime=System.currentTimeMillis()-starTtime;
				} catch (Exception e) {}
				log.info("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] total fetch in --> "+deltaTime);


			} catch (Exception e) {
				throw e;
			}  finally {
				//cursor.close();			
			} 


		} catch (SDPOrderBySizeException e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] SDPOrderBySizeException",e);
			throw (SDPOrderBySizeException)e;
		} catch (SDPPageSizeException e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] SDPPageSizeException",e);
			throw (SDPPageSizeException)e;
		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] GenericException",e);
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] INGORED" +e);
		} finally {
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStreamNewLimitSolr] END");
		}


		SDPDataResult outres= new SDPDataResult(ret, cnt);
		return outres;
	}			


	private String getPropertyName(Property prop) {
		String chiave=prop.getName();
		if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.Boolean)) {
			return chiave+"_b";
		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.String)) {
			return chiave+"_s";
		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.Int32)) {
			return chiave+"_i";
		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.Int64)) {
			return chiave+"_i";
		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.Double)) {
			//return chiave+"_d";
			return chiave+"_f";
		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.DateTimeOffset)) {
			return chiave+"_dt";
		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.DateTime)) {
			return chiave+"_dt";


		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.Decimal)) {
			return chiave+"_d";
		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.Binary)) {
			return chiave+"_by";


		}	
		return chiave; }

	private int [] checkPagesData(Integer skip,Integer top, int resultSize) throws Exception{
		int startindex=0;
		int endindex=SDPDataApiConfig.getInstance().getMaxDocumentPerPage();


		log.debug("[SDPDataApiMongoAccess::checkPagesData] skipParameter="+skip);
		log.debug("[SDPDataApiMongoAccess::checkPagesData] topParameter="+top);

		//se skip è valorizzato
		if(skip!=null) {
			startindex=startindex+skip.intValue();
		}

		if(skip!=null && skip.intValue()>SDPDataApiConfig.getInstance().getMaxSkipPages()) throw new SDPPageSizeException("invalid skip value: max page = "+SDPDataApiConfig.getInstance().getMaxSkipPages(),Locale.UK);


		//controlli ... sollevo eccezione quando:
		// top valorizzato e > di maxsize
		// top non valorizzato e size - start > max
		if(top!=null && top.intValue()>SDPDataApiConfig.getInstance().getMaxDocumentPerPage()) throw new SDPPageSizeException("invalid top value: max document per page = "+endindex,Locale.UK);
		if(top==null && (resultSize-startindex)>SDPDataApiConfig.getInstance().getMaxDocumentPerPage())  throw new SDPPageSizeException("too many documents; use top parameter: max document per page = "+endindex,Locale.UK);
		if(skip!=null && skip.intValue()>resultSize) throw new SDPPageSizeException("skip value out of range: max document in query result = "+resultSize,Locale.UK);




		// a questo punto i parametri sono buoni ... valorizzo endindex in base al top se valorizzato (sempre con start index >0
		if(top!=null) endindex=top.intValue();

		endindex=startindex+endindex;

		// riporto endinx a resultsize nel caso in cui sia maggiore
		if (endindex>resultSize) endindex=resultSize;






		log.debug("[SDPDataApiMongoAccess::checkPagesData] checkPagesData="+startindex);
		log.debug("[SDPDataApiMongoAccess::checkPagesData] checkPagesData="+endindex);	



		int [] ret = new int[] {startindex,endindex, ((top!=null) ? top.intValue() : -1 ) , ((skip!=null) ? skip.intValue() : -1 ) }; 
		return ret; 

	}		

}
