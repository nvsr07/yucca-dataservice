package it.csi.smartdata.dataapi.mongo;

import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;
import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;
import it.csi.smartdata.dataapi.mongo.dto.DbConfDto;
import it.csi.smartdata.dataapi.mongo.dto.SDPDataResult;
import it.csi.smartdata.dataapi.mongo.exception.SDPCustomQueryOptionException;

import java.util.ArrayList;
import java.util.Arrays;
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
			log.info("[SDPDataApiMongoAccess::initConfDbObject] BEGIN");
			log.info("[SDPDataApiMongoAccess::initConfDbObject] codiceApi="+codiceApi);



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




					if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISTREAM.equals(subType)) {

						//BasicDBList objStreams = (BasicDBList)((DBObject)obj.get("streams")).get("stream");
						BasicDBList objStreams = (BasicDBList)obj.get("dataset");
						BasicDBList queryStreams=createQueryStreamPerApi(objStreams);
						BasicDBList compPropsTot = getMergedStreamComponentsPerQueryString(queryStreams);
						((DBObject)obj).put("mergedComponents", compPropsTot);
					} else 	if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTIBULK.equals(subType)) {
						BasicDBList objStreams = (BasicDBList)obj.get("dataset");
						BasicDBList queryStreams=createQueryStreamPerApi(objStreams);
						BasicDBList compPropsTot = getMergedStreamComponentsPerQueryString(queryStreams);
						((DBObject)obj).put("mergedComponents", compPropsTot);

					}
				}

			}
		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::initConfDbObject] INGORED" +e);
		} finally {
			log.info("[SDPDataApiMongoAccess::initConfDbObject] END");

		}

		return this.configObject;
	}	


	public BasicDBList  getMergedStreamComponentsPerQueryString(BasicDBList queryStreams ) {
		List<Property> compPropsTot=new ArrayList<Property>();
		BasicDBList ret2=new BasicDBList();
		try {

			log.info("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] BEGIN");
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

			cursor = coll.find(query);
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
			log.info("[SDPDataApiMongoAccess::getMergedStreamComponentsPerQueryString] END");

		}
		return ret2;

	}	


	private BasicDBList getQueryStreamPerApi(String codiceApi) {
		BasicDBList queryStreams=new BasicDBList();
		try {
			log.info("[SDPDataApiMongoAccess::getQueryStreamPerApi] BEGIN");
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


					if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISTREAM.equals(subType)) {
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
			log.info("[SDPDataApiMongoAccess::getQueryStreamPerApi] END");

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
			log.info("[SDPDataApiMongoAccess::createQueryStreamPerApi] BEGIN");
			log.info("[SDPDataApiMongoAccess::createQueryStreamPerApi] objElencoStream="+objElencoStream);



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
			log.info("[SDPDataApiMongoAccess::createQueryStreamPerApi] END");
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
			log.info("SDPDataApiMongoAccess.getDatasetFiledsDbList --> ERROR " + e);

		}
		return dblistout;
	}



	public List<DBObject> getDatasetPerApi(String codiceApi) {
		List<DBObject> ret= new ArrayList<DBObject>();

		try {
			log.info("[SDPDataApiMongoAccess::getDatasetPerApi] BEGIN");
			log.info("[SDPDataApiMongoAccess::getDatasetPerApi] codiceApi="+codiceApi);

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

			cursor = coll.find(query);
			try {
				while (cursor.hasNext()) {
					DBObject obj=cursor.next();
					log.debug("[SDPDataApiMongoAccess::getDatasetPerApi] objfound added="+obj);
					ret.add(obj);
				}

			}finally {
				cursor.close();
			}
			log.info("[SDPDataApiMongoAccess::getDatasetPerApi] ret="+ret);

		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::getDatasetPerApi] INGORED" +e);
		} finally {
			log.info("[SDPDataApiMongoAccess::getDatasetPerApi] END");
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
			) {
		String collection=null;
		//		String sensore=null;
		//		String stream=null;
		String idDataset=null;
		String datasetToFindVersion=null;
		List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();
		int cnt = -1;
		try {
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] BEGIN");
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] codiceTenant="+codiceTenant);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] nameSpace="+nameSpace);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] entityContainer="+entityContainer);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] internalId="+internalId);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] datatType="+datatType);
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] userQuery="+userQuery);
			log.debug("[SDPDataApiMongoAccess::getMeasuresPerStream] streamMetadata="+streamMetadata);

			List<Property> compPropsTot=new ArrayList<Property>();
			List<Property> compPropsCur=new ArrayList<Property>();			


			collection=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("collection") );
			String host=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("host"));
			String port =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("port") );
			String dbcfg =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("db") );
			idDataset=takeNvlValues(streamMetadata.get("idDataset"));
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

			host=SDPDataApiConfig.getInstance().getMongoDefaultHost();
			port=""+SDPDataApiConfig.getInstance().getMongoDefaultPort();

			Object eleCapmpi=((DBObject)streamMetadata.get("info")).get("fields");

			BasicDBList campiDbList= getDatasetFiledsDbList(eleCapmpi);
			for (int k=0;k<campiDbList.size();k++) {
				boolean present=false;
				compPropsCur=getDatasetFiledsOdataPros(campiDbList.get(k));
				for (int i=0;i<compPropsTot.size();i++) {
					if (compPropsTot.get(i).getName().equals(compPropsCur.get(0).getName())) present=true;

				}
				if (!present) {
					compPropsTot.add(compPropsCur.get(0));
				}
			}





			if (collection==null)  return null;

			DBCursor cursor=null;



			//MongoClient mongoClient = new MongoClient(host,Integer.parseInt(port));
			MongoClient mongoClient = getMongoClient(host,Integer.parseInt(port));			


			DB db = mongoClient.getDB(dbcfg);


			DBCollection collMisure = db.getCollection(collection);
			BasicDBList queryTot=new BasicDBList();

			//queryTot.add( new BasicDBObject("idDataset",idDataset));
			queryTot.add( new BasicDBObject("idDataset",new Integer(new Double(idDataset).intValue())));

			queryTot.add( new BasicDBObject("datasetVersion",new Integer(new Double(datasetToFindVersion).intValue())));
			
			

			//BasicDBObject query = new BasicDBObject("idDataset",idDataset);
			if (null!=internalId) {
				//query.append("_id",new ObjectId(internalId));
				queryTot.add( new BasicDBObject("_id",new ObjectId(internalId)));

			}
			if (null != userQuery) {
				log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] userQuery="+userQuery);
				if (userQuery instanceof BasicDBList) {
					queryTot.addAll((BasicDBList)userQuery);
				} else if (userQuery instanceof BasicDBObject) {
					queryTot.add((BasicDBObject)userQuery);
				}

				//query.append("$and", userQuery);
			}

			BasicDBObject query = new BasicDBObject("$and", queryTot);

			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] total data query ="+query);
			//cursor = collMisure.find(query);

			cnt = collMisure.find(query).count();

			if (skip<0) skip=0;
			if (limit<0) limit=SDPDataApiConfig.getInstance().getMaxDocumentPerPage();
			
			
			// per ordinamento su max 
			limit=SDPDataApiConfig.getInstance().getMaxDocumentPerPage()+SDPDataApiConfig.getInstance().getMaxSkipPages();
			skip=0;
			
			
			if (null!=userOrderBy) cursor = collMisure.find(query).skip(skip).limit(limit).sort((BasicDBList)userOrderBy);
			else cursor = collMisure.find(query).skip(skip).limit(limit);
			try {
				while (cursor.hasNext()) {



					DBObject obj=cursor.next();
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
			} catch (Exception e) {
				throw e;
			}  finally {
				cursor.close();			
			} 


		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStream] INGORED" +e);
		} finally {
			log.info("[SDPDataApiMongoAccess::getMeasuresPerStream] END");
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
		String datasetToFindVersion=null;
		List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();
		int cnt = 1212;
		try {
			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] BEGIN");
			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] codiceTenant="+codiceTenant);
			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] nameSpace="+nameSpace);
			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] entityContainer="+entityContainer);
			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] internalId="+internalId);
			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] datatType="+datatType);
			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] userQuery="+userQuery);
			log.debug("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] streamMetadata="+streamMetadata);

			List<Property> compPropsTot=new ArrayList<Property>();
			List<Property> compPropsCur=new ArrayList<Property>();			


			collection=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("collection") );
			String host=takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("host"));
			String port =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("port") );
			String dbcfg =takeNvlValues( ((DBObject)streamMetadata.get("configData")).get("db") );
			idDataset=takeNvlValues(streamMetadata.get("idDataset"));
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

			host=SDPDataApiConfig.getInstance().getMongoDefaultHost();
			port=""+SDPDataApiConfig.getInstance().getMongoDefaultPort();

			Object eleCapmpi=((DBObject)streamMetadata.get("info")).get("fields");

			BasicDBList campiDbList= getDatasetFiledsDbList(eleCapmpi);
			for (int k=0;k<campiDbList.size();k++) {
				boolean present=false;
				compPropsCur=getDatasetFiledsOdataPros(campiDbList.get(k));
				for (int i=0;i<compPropsTot.size();i++) {
					if (compPropsTot.get(i).getName().equals(compPropsCur.get(0).getName())) present=true;

				}
				if (!present) {
					compPropsTot.add(compPropsCur.get(0));
				}
			}





			if (collection==null)  return null;

			//DBCursor cursor=null;



			//MongoClient mongoClient = new MongoClient(host,Integer.parseInt(port));
			MongoClient mongoClient = getMongoClient(host,Integer.parseInt(port));			


			DB db = mongoClient.getDB(dbcfg);


			DBCollection collMisure = db.getCollection(collection);
			BasicDBList queryTot=new BasicDBList();

			//queryTot.add( new BasicDBObject("idDataset",idDataset));
			queryTot.add( new BasicDBObject("idDataset",new Integer(new Double(idDataset).intValue())));

			queryTot.add( new BasicDBObject("datasetVersion",new Integer(new Double(datasetToFindVersion).intValue())));
			
			

			//BasicDBObject query = new BasicDBObject("idDataset",idDataset);
			if (null!=internalId) {
				//query.append("_id",new ObjectId(internalId));
				queryTot.add( new BasicDBObject("_id",new ObjectId(internalId)));

			}
			if (null != userQuery) {
				log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] userQuery="+userQuery);
				if (userQuery instanceof BasicDBList) {
					queryTot.addAll((BasicDBList)userQuery);
				} else if (userQuery instanceof BasicDBObject) {
					queryTot.add((BasicDBObject)userQuery);
				}

				//query.append("$and", userQuery);
			}

			BasicDBList queryOutTot=new BasicDBList();
			if (null != groupOutQuery) {
				log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] groupOutQuery="+groupOutQuery);
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
				for (int kk=0;kk<((BasicDBList)userOrderBy).size();kk++ ) {
					BasicDBObject curObj=(BasicDBObject)((BasicDBList)userOrderBy).get(kk);
					Iterator<String> it = curObj.keySet().iterator();
					while (it.hasNext()) {
						String key=it.next();
						Integer orderVersus=new Integer(curObj.getString(key));
						objeSort.append(key, orderVersus);
						
					}
					
					
					//objeSort.append(((BasicDBObject)((BasicDBList)userOrderBy).get(kk)).get, val)
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
			
			Cursor cursor =collMisure.aggregate(pipeline,aggregationOptions);
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

					
					//String datasetVersion=takeNvlValues(obj.get("datasetVersion"));

					
					String count=takeNvlValues( obj.get("count"));
					


					Map<String, Object> misura = new HashMap<String, Object>();
					misura.put("dayofmonth",  (giorno==null ? -1 : new Integer(giorno)));
					misura.put("month",  (mese==null ? -1 : new Integer(mese)));
					misura.put("year",  (anno==null ? -1 : new Integer(anno)));
					misura.put("hour",  (ora==null ? -1 : new Integer(ora)));
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


		} catch (Exception e) {
			if (e instanceof SDPCustomQueryOptionException) {
				log.error("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] rethrow" +e);
				throw (SDPCustomQueryOptionException) e;
			} else log.error("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] INGORED" +e);
		} finally {
			log.info("[SDPDataApiMongoAccess::getMeasuresStatsPerStream] END");
		}


		SDPDataResult outres= new SDPDataResult(ret, cnt);
		return outres;
	}		
	
	public SDPDataResult getBinary(String codiceTenant, String nameSpace, EdmEntityContainer entityContainer,DBObject streamMetadata,String internalId,String datatType,Object userQuery, Object userOrderBy,
			ArrayList<String> elencoIdBinary,
			String codiceApi,
			int skip,
			int limit
			) {
		String collection=null;
		//		String sensore=null;
		//		String stream=null;
//		String idDataset=null;
//		String datasetToFindVersion=null;
		List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();
		int cnt = -1;
		try {
			log.info("[SDPDataApiMongoAccess::getBinary] BEGIN");
			log.info("[SDPDataApiMongoAccess::getBinary] codiceTenant="+codiceTenant);
			log.info("[SDPDataApiMongoAccess::getBinary] nameSpace="+nameSpace);
			log.info("[SDPDataApiMongoAccess::getBinary] entityContainer="+entityContainer);
			log.info("[SDPDataApiMongoAccess::getBinary] internalId="+internalId);
			log.info("[SDPDataApiMongoAccess::getBinary] datatType="+datatType);
			log.info("[SDPDataApiMongoAccess::getBinary] userQuery="+userQuery);
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
			tanantDbCfg=MongoTenantDbSingleton.getInstance().getDataDbConfiguration(MongoTenantDbSingleton.DB_MEDIA, codiceTenant);
			collection=tanantDbCfg.getCollection();
			String host=tanantDbCfg.getHost();
			String port=""+tanantDbCfg.getPort();
			String dbcfg=tanantDbCfg.getDataBase();
			
			
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



			if (collection==null)  return null;

			DBCursor cursor=null;



			//MongoClient mongoClient = new MongoClient(host,Integer.parseInt(port));
			MongoClient mongoClient = getMongoClient(host,Integer.parseInt(port));			


			DB db = mongoClient.getDB(dbcfg);


			DBCollection collMisure = db.getCollection(collection);
			BasicDBList queryTot=new BasicDBList();

			//queryTot.add( new BasicDBObject("idDataset",idDataset));
			queryTot.add( new BasicDBObject("idDataset",idDatasetBinary));

			queryTot.add( new BasicDBObject("datasetVersion",binaryDatasetVersion));
			
			

			//BasicDBObject query = new BasicDBObject("idDataset",idDataset);
			if (null!=internalId) {
				//query.append("_id",new ObjectId(internalId));
				queryTot.add( new BasicDBObject("_id",new ObjectId(internalId)));

			}
			if (null != userQuery) {
				log.info("[SDPDataApiMongoAccess::getBinary] userQuery="+userQuery);
				if (userQuery instanceof BasicDBList) {
					queryTot.addAll((BasicDBList)userQuery);
				} else if (userQuery instanceof BasicDBObject) {
					queryTot.add((BasicDBObject)userQuery);
				}

				//query.append("$and", userQuery);
			}

			
			if (null!=elencoIdBinary && elencoIdBinary.size()>0) {
				BasicDBObject inQuery = new BasicDBObject();
				inQuery.put("idBinary", new BasicDBObject("$in", elencoIdBinary));
				queryTot.add(inQuery);
			}
			
			BasicDBObject query = new BasicDBObject("$and", queryTot);

			log.info("[SDPDataApiMongoAccess::getBinary] total data query ="+query);
			//cursor = collMisure.find(query);

			cnt = collMisure.find(query).count();

			if (skip<0) skip=0;
			if (limit<0) limit=SDPDataApiConfig.getInstance().getMaxDocumentPerPage();
			
			
			// per ordinamento su max 
			limit=SDPDataApiConfig.getInstance().getMaxDocumentPerPage()+SDPDataApiConfig.getInstance().getMaxSkipPages();
			skip=0;
			
			
			if (null!=userOrderBy) cursor = collMisure.find(query).skip(skip).limit(limit).sort((BasicDBList)userOrderBy);
			else cursor = collMisure.find(query).skip(skip).limit(limit);
			try {
				while (cursor.hasNext()) {
	


					DBObject obj=cursor.next();
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
									

								}
							}
						}
					}
					
///binary/{apiCode}/{dataSetCode}/{dataSetVersion}/{idBinary}
					

					
					String path="/api/"+codiceApi+"/attachment/"+idDatasetBinary+"/"+binaryDatasetVersion+"/"+misura.get("idBinary");
					
					misura.put("urlDownloadBinary", path);

					ret.add(misura);
				}	
			} catch (Exception e) {
				throw e;
			}  finally {
				cursor.close();			
			} 


		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::getBinary] INGORED" +e);
		} finally {
			log.info("[SDPDataApiMongoAccess::getBinary] END");
		}


		SDPDataResult outres= new SDPDataResult(ret, cnt);
		return outres;
	}		

}
