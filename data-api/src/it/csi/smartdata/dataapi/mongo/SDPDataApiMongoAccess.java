package it.csi.smartdata.dataapi.mongo;

import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;
import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;
import it.csi.smartdata.dataapi.mongo.dto.DbConfDto;
import it.csi.smartdata.dataapi.mongo.dto.SDPDataResult;
import it.csi.smartdata.dataapi.mongo.dto.SDPMongoOrderElement;
import it.csi.smartdata.dataapi.mongo.exception.SDPCustomQueryOptionException;
import it.csi.smartdata.dataapi.mongo.exception.SDPOrderBySizeException;
import it.csi.smartdata.dataapi.mongo.exception.SDPPageSizeException;

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
			log.debug("[SDPDataApiMongoAccess::initConfDbObject] END");

		}

		return this.configObject;
	}	


	public BasicDBList  getMergedStreamComponentsPerQueryString(BasicDBList queryStreams ) {
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
			MongoClient mongoClient = getMongoClient(host,Integer.parseInt(port));			
			DB db = mongoClient.getDB(dbcfg);

			DBCollection collMisure = db.getCollection(collection);
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






			if (null!=userOrderBy) {

				boolean orderByAllowed=false;
				if (cnt<SDPDataApiConstants.SDP_MAX_DOC_FOR_ORDERBY) {
					orderByAllowed=true;
				} else if (DATA_TYPE_MEASURE.equals(datatType) && ((ArrayList<SDPMongoOrderElement>)userOrderBy).size()<=1) {


					SDPMongoOrderElement elemOrder=(SDPMongoOrderElement)((ArrayList<SDPMongoOrderElement>)userOrderBy).get(0);
					//if (elemOrder.toString().indexOf("\"time\"")!=-1) orderByAllowed=true;
					if (elemOrder.getNomeCampo().equalsIgnoreCase("time")) orderByAllowed=true;
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
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStream] SDPOrderBySizeException" +e);
			throw (SDPOrderBySizeException)e;
		} catch (Exception e) {
			log.error("[SDPDataApiMongoAccess::getMeasuresPerStream] GenericException" +e);
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
			MongoClient mongoClient = getMongoClient(host,Integer.parseInt(port));			


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
				//TODO .. eccezione per dataset sbagliato

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

	public SDPDataResult getBinary(String codiceTenant, String nameSpace, EdmEntityContainer entityContainer,DBObject streamMetadata,String internalId,String datatType,Object userQuery, Object userOrderBy,
			ArrayList<String> elencoIdBinary,
			String codiceApi,
			int skip,
			int limit
			) {


		// TODO YUCCA-74 odata evoluzione
		// potrebbe no nsubire modifiche verificare solo info.binaryIdDataset e info.binaryDatasetVersion in base a come viene modificato streamMetadata


		String collection=null;
		//		String sensore=null;
		//		String stream=null;
		//		String idDataset=null;
		//		String datasetToFindVersion=null;
		List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();
		int cnt = -1;
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
				log.debug("[SDPDataApiMongoAccess::getBinary] userQuery="+userQuery);
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
			log.debug("[SDPDataApiMongoAccess::getBinary] END");
		}


		SDPDataResult outres= new SDPDataResult(ret, cnt);
		return outres;
	}		

	
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
