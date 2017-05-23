package it.csi.smartdata.dataapi.mongo;

import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;
import it.csi.smartdata.dataapi.mongo.dto.DbConfDto;
import it.csi.smartdata.dataapi.odata.SDPSingleProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

public class MongoTenantDbSingleton {

	static Logger log = Logger.getLogger(SDPSingleProcessor.class.getPackage().getName());


	public static final String DB_MESURES="DBMEASURES";
	public static final String DB_DATA="DBDATA";
	public static final String DB_MESURES_TRASH="DBMEASURES_TRASH";
	public static final String DB_DATA_TRASH="DBDATA_TRASH";


	public static final String DB_MEDIA="DBMEDIA";

	
	public static final String DB_MESURES_SOLR="DBMEASURES_SOLR";
	public static final String DB_DATA_SOLR="DBDATA_SOLR";
	public static final String DB_MEDIA_SOLR="DBMEDIA_SOLR";
	public static final String DB_SOCIAL_SOLR="DBSOCIAL_SOLR";
	
	
	public static final String DB_MESURES_PHOENIX="DBMEASURES_PHOENIX";
	public static final String DB_DATA_PHOENIX="DBDATA_PHOENIX";
	public static final String DB_MEDIA_PHOENIX="DBMEDIA_PHOENIX";
	public static final String DB_SOCIAL_PHOENIX="DBSOCIAL_PHOENIX";	

	
	public static final String MAX_DOC_PER_PAGE="MAX_DOC_PER_PAGE";
	
	
	
	public static MongoTenantDbSingleton instance=null;
	private static int anno_init = 0;
	private static int mese_init = 0;
	private static int giorno_init = 0;


	private static HashMap<String, DbConfDto> params = new HashMap<String, DbConfDto>();
	private static HashMap<String, String> functionalParams = new HashMap<String, String>();
	private static HashMap<String, MongoClient> mongoConnection = new HashMap<String, MongoClient>();



	private static boolean singletonToRefresh() {
		int curAnno = Calendar.getInstance().get(Calendar.YEAR);
		int curMese = Calendar.getInstance().get(Calendar.MONTH);
		int curGiorno = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if (curAnno > anno_init) return true;
		else if (curMese > mese_init) return true;
		//per refresh mensile
		//else if (curGiorno > giorno_init)return true;
		return false;
	}

	private void cleanMongoConnection() {
		if (mongoConnection!=null && mongoConnection.size()>0) {
			Iterator<String> chiavi=mongoConnection.keySet().iterator();
			while (chiavi.hasNext()) {
				String chiave=chiavi.next();
				mongoConnection.get(chiave).close();
			}

		}
	}


	public synchronized static MongoTenantDbSingleton getInstance() throws Exception{
		log.info("[MongoTenantDbSingleton::getInstance] refresh singleton mongoConnection.size()="+ (instance==null ? -1 : instance.mongoConnection.size()));




		//if(instance == null || singletonToRefresh()) {
		if(instance == null ) {

			log.info("[MongoTenantDbSingleton::getInstance] refresh singleton instanceNull=" +(instance==null));
			//if (instance!=null) instance.cleanMongoConnection(); 
			instance = new MongoTenantDbSingleton();
			anno_init = Calendar.getInstance().get(Calendar.YEAR);
			mese_init = Calendar.getInstance().get(Calendar.MONTH);
			giorno_init = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		}
		return instance;
	}

	private String takeNvlValues(Object obj) {
		if (null==obj) return null;
		else return obj.toString();
	}


	private MongoTenantDbSingleton() throws Exception{
		try {
			//			MongoClient mongoClient = new MongoClient(
			//					SDPDataApiConfig.getInstance().getMongoCfgHost(SDPDataApiConfig.MONGO_DB_CFG_TENANT), 
			//					SDPDataApiConfig.getInstance().getMongoCfgPort(SDPDataApiConfig.MONGO_DB_CFG_TENANT));


			String host=SDPDataApiConfig.getInstance().getMongoCfgHost(SDPDataApiConfig.MONGO_DB_CFG_TENANT);
			StringTokenizer st=new StringTokenizer(host,";",false);
			ArrayList<ServerAddress> arrServerAddr=new ArrayList<ServerAddress>();
			while (st.hasMoreTokens()) {
				String hostnew=st.nextToken();
				ServerAddress serverAddr=new ServerAddress(hostnew,SDPDataApiConfig.getInstance().getMongoCfgPort(SDPDataApiConfig.MONGO_DB_CFG_TENANT));
				arrServerAddr.add(serverAddr);
			}

			
			MongoClientOptions.Builder optionsBuilder = new MongoClientOptions.Builder();
			optionsBuilder.readPreference(ReadPreference.secondaryPreferred());
			MongoClientOptions options = optionsBuilder.build();
			
			MongoClient mongoClient = null;
			if (SDPDataApiConfig.getInstance().getMongoDefaultPassword()!=null && SDPDataApiConfig.getInstance().getMongoDefaultPassword().trim().length()>0 && 
					SDPDataApiConfig.getInstance().getMongoDefaultUser()!=null && SDPDataApiConfig.getInstance().getMongoDefaultUser().trim().length()>0	) {
				MongoCredential credential = MongoCredential.createMongoCRCredential(SDPDataApiConfig.getInstance().getMongoDefaultUser(), 
						"admin", 
						SDPDataApiConfig.getInstance().getMongoDefaultPassword().toCharArray());
				mongoClient = new MongoClient(arrServerAddr,Arrays.asList(credential),options);
			} else {
				mongoClient = new MongoClient(arrServerAddr,options);
			}			

			DB db = mongoClient.getDB(SDPDataApiConfig.getInstance().getMongoCfgDB(SDPDataApiConfig.MONGO_DB_CFG_TENANT));
			DBCollection coll = db.getCollection(SDPDataApiConfig.getInstance().getMongoCfgCollection(SDPDataApiConfig.MONGO_DB_CFG_TENANT));

			DBCursor cursor = coll.find();
			try {
				while (cursor.hasNext()) {

					DBObject obj=cursor.next();

					String tenant=obj.get("tenantCode").toString();
					log.info("[MongoTenantDbSingleton::MongoTenantDbSingleton] Loading tenat:"+tenant);


					DbConfDto dataDbTrash=new DbConfDto();
					//dataDbTrash.setHost(takeNvlValues( obj.get("trashDataCollectionHost") ));
					dataDbTrash.setHost(SDPDataApiConfig.getInstance().getMongoDefaultHost());
					dataDbTrash.setDataBase(takeNvlValues( obj.get("archiveDataCollectionDb")));
					dataDbTrash.setCollection(takeNvlValues( obj.get("archiveDataCollectionName")));
					//dataDbTrash.setPort(Integer.parseInt(obj.get("trashDataCollectionPort").toString()));
					dataDbTrash.setPort(SDPDataApiConfig.getInstance().getMongoDefaultPort());



					DbConfDto measureDbTrahs=new DbConfDto();
					measureDbTrahs.setHost(SDPDataApiConfig.getInstance().getMongoDefaultHost());
					measureDbTrahs.setDataBase(takeNvlValues( obj.get("archiveMeasuresCollectionDb")));
					measureDbTrahs.setCollection(takeNvlValues( obj.get("archiveMeasuresCollectionName")));
					measureDbTrahs.setPort(SDPDataApiConfig.getInstance().getMongoDefaultPort());



					DbConfDto measureDb=new DbConfDto();
					measureDb.setHost(SDPDataApiConfig.getInstance().getMongoDefaultHost());
					measureDb.setDataBase(takeNvlValues( obj.get("measuresCollectionDb")));
					measureDb.setCollection(takeNvlValues( obj.get("measuresCollectionName")));
					measureDb.setPort(SDPDataApiConfig.getInstance().getMongoDefaultPort());


					DbConfDto mediaDb=new DbConfDto();
					mediaDb.setHost(SDPDataApiConfig.getInstance().getMongoDefaultHost());
					mediaDb.setDataBase(takeNvlValues( obj.get("mediaCollectionDb")));
					mediaDb.setCollection(takeNvlValues( obj.get("mediaCollectionName")));
					mediaDb.setPort(SDPDataApiConfig.getInstance().getMongoDefaultPort());


					DbConfDto dataDb=new DbConfDto();
					dataDb.setHost(SDPDataApiConfig.getInstance().getMongoDefaultHost());
					dataDb.setDataBase(takeNvlValues( obj.get("dataCollectionDb")));
					dataDb.setCollection(takeNvlValues( obj.get("dataCollectionName")));
					dataDb.setPort(SDPDataApiConfig.getInstance().getMongoDefaultPort());

					DbConfDto dataDbSolr=new DbConfDto();
					dataDbSolr.setCollection(takeNvlValues( obj.get("dataSolrCollectionName")));
					
					DbConfDto mediaDbSolr=new DbConfDto();
					mediaDbSolr.setCollection(takeNvlValues( obj.get("mediaSolrCollectionName")));

					DbConfDto measureDbSolr=new DbConfDto();
					measureDbSolr.setCollection(takeNvlValues( obj.get("measuresSolrCollectionName")));
					
					
					DbConfDto socialDbSolr=new DbConfDto();
					socialDbSolr.setCollection(takeNvlValues( obj.get("socialSolrCollectionName")));
					

					
					DbConfDto dataDbPhoenix=new DbConfDto();
					dataDbPhoenix.setCollection(takeNvlValues( obj.get("dataPhoenixCollectionName")));
					dataDbPhoenix.setDataBase(takeNvlValues( obj.get("dataPhoenixSchemaName")));
					
					DbConfDto mediaDbPhoenix=new DbConfDto();
					mediaDbPhoenix.setCollection(takeNvlValues( obj.get("mediaPhoenixCollectionName")));
					mediaDbPhoenix.setDataBase(takeNvlValues( obj.get("mediaPhoenixSchemaName")));

					DbConfDto measureDbPhoenix=new DbConfDto();
					measureDbPhoenix.setCollection(takeNvlValues( obj.get("measuresPhoenixCollectionName")));
					measureDbPhoenix.setDataBase(takeNvlValues( obj.get("measuresPhoenixSchemaName")));
					
					
					DbConfDto socialDbPhoenix=new DbConfDto();
					socialDbPhoenix.setCollection(takeNvlValues( obj.get("socialPhoenixCollectionName")));
					socialDbPhoenix.setDataBase(takeNvlValues( obj.get("socialPhoenixSchemaName")));
					

					params.put(tenant+"__"+DB_SOCIAL_PHOENIX, socialDbPhoenix);
					params.put(tenant+"__"+DB_MESURES_PHOENIX, measureDbPhoenix);
					params.put(tenant+"__"+DB_DATA_PHOENIX, dataDbPhoenix);
					params.put(tenant+"__"+DB_MEDIA_PHOENIX, mediaDbPhoenix);	
					
					
					
					params.put(tenant+"__"+DB_SOCIAL_SOLR, socialDbSolr);
					params.put(tenant+"__"+DB_MESURES_SOLR, measureDbSolr);
					params.put(tenant+"__"+DB_DATA_SOLR, dataDbSolr);
					params.put(tenant+"__"+DB_MEDIA_SOLR, mediaDbSolr);	




					params.put(tenant+"__"+DB_MESURES, measureDb);
					params.put(tenant+"__"+DB_MESURES_TRASH, measureDbTrahs);
					params.put(tenant+"__"+DB_DATA, dataDb);
					params.put(tenant+"__"+DB_DATA_TRASH, dataDbTrash);
					params.put(tenant+"__"+DB_MEDIA, mediaDb);
					
					
					functionalParams.put(tenant+"__"+MAX_DOC_PER_PAGE,takeNvlValues( obj.get("maxOdataResultPerPage")));
					
					log.info("[MongoTenantDbSingleton::MongoTenantDbSingleton] refresh add client  " +SDPDataApiConfig.getInstance().getMongoCfgHost(SDPDataApiConfig.MONGO_DB_CFG_TENANT)+"___"+SDPDataApiConfig.getInstance().getMongoCfgPort(SDPDataApiConfig.MONGO_DB_CFG_TENANT));
					mongoConnection.put(SDPDataApiConfig.getInstance().getMongoCfgHost(SDPDataApiConfig.MONGO_DB_CFG_TENANT)+"___"+SDPDataApiConfig.getInstance().getMongoCfgPort(SDPDataApiConfig.MONGO_DB_CFG_TENANT), mongoClient);

				}				
			} finally {
				cursor.close();
			}



		} catch (Exception e) {
			//TODO log
			e.printStackTrace();
		}
	}

	public DbConfDto getDataDbConfigurationOld(String dbType,String tenantCode) {
		return params.get(tenantCode+"__"+dbType);
	}
	public DbConfDto getDataDbConfiguration(String dbType,String tenantCode) {
		if (null==params.get(tenantCode+"__"+dbType))  reloadTenantDbConfiguration();
		return params.get(tenantCode+"__"+dbType);
	}

	public String getMaxDocPerPage(String tenantCode) {
		return functionalParams.get(tenantCode+"__"+MAX_DOC_PER_PAGE);
	}

	private void reloadTenantDbConfiguration () {
		try {
			log.info("[MongoTenantDbSingleton::reloadTenantDbConfiguration] BEGIN");
			MongoClient mongoClient=getMongoClient(SDPDataApiConfig.getInstance().getMongoCfgHost(SDPDataApiConfig.MONGO_DB_CFG_TENANT),SDPDataApiConfig.getInstance().getMongoCfgPort(SDPDataApiConfig.MONGO_DB_CFG_TENANT));
			DB db = mongoClient.getDB(SDPDataApiConfig.getInstance().getMongoCfgDB(SDPDataApiConfig.MONGO_DB_CFG_TENANT));
			DBCollection coll = db.getCollection(SDPDataApiConfig.getInstance().getMongoCfgCollection(SDPDataApiConfig.MONGO_DB_CFG_TENANT));

			DBCursor cursor = coll.find();
			try {
				while (cursor.hasNext()) {

					DBObject obj=cursor.next();

					String tenant=obj.get("tenantCode").toString();
					log.info("[MongoTenantDbSingleton::reloadTenantDbConfiguration] Loading tenat:"+tenant);


					DbConfDto dataDbTrash=new DbConfDto();
					//dataDbTrash.setHost(takeNvlValues( obj.get("trashDataCollectionHost") ));
					dataDbTrash.setHost(SDPDataApiConfig.getInstance().getMongoDefaultHost());
					dataDbTrash.setDataBase(takeNvlValues( obj.get("archiveDataCollectionDb")));
					dataDbTrash.setCollection(takeNvlValues( obj.get("archiveDataCollectionName")));
					//dataDbTrash.setPort(Integer.parseInt(obj.get("trashDataCollectionPort").toString()));
					dataDbTrash.setPort(SDPDataApiConfig.getInstance().getMongoDefaultPort());



					DbConfDto measureDbTrahs=new DbConfDto();
					measureDbTrahs.setHost(SDPDataApiConfig.getInstance().getMongoDefaultHost());
					measureDbTrahs.setDataBase(takeNvlValues( obj.get("archiveMeasuresCollectionDb")));
					measureDbTrahs.setCollection(takeNvlValues( obj.get("archiveMeasuresCollectionName")));
					measureDbTrahs.setPort(SDPDataApiConfig.getInstance().getMongoDefaultPort());



					DbConfDto measureDb=new DbConfDto();
					measureDb.setHost(SDPDataApiConfig.getInstance().getMongoDefaultHost());
					measureDb.setDataBase(takeNvlValues( obj.get("measuresCollectionDb")));
					measureDb.setCollection(takeNvlValues( obj.get("measuresCollectionName")));
					measureDb.setPort(SDPDataApiConfig.getInstance().getMongoDefaultPort());


					DbConfDto mediaDb=new DbConfDto();
					mediaDb.setHost(SDPDataApiConfig.getInstance().getMongoDefaultHost());
					mediaDb.setDataBase(takeNvlValues( obj.get("mediaCollectionDb")));
					mediaDb.setCollection(takeNvlValues( obj.get("mediaCollectionName")));
					mediaDb.setPort(SDPDataApiConfig.getInstance().getMongoDefaultPort());


					DbConfDto dataDb=new DbConfDto();
					dataDb.setHost(SDPDataApiConfig.getInstance().getMongoDefaultHost());
					dataDb.setDataBase(takeNvlValues( obj.get("dataCollectionDb")));
					dataDb.setCollection(takeNvlValues( obj.get("dataCollectionName")));
					dataDb.setPort(SDPDataApiConfig.getInstance().getMongoDefaultPort());


					
					
					
					DbConfDto dataDbSolr=new DbConfDto();
					dataDbSolr.setCollection(takeNvlValues( obj.get("dataSolrCollectionName")));
					
					DbConfDto mediaDbSolr=new DbConfDto();
					mediaDbSolr.setCollection(takeNvlValues( obj.get("mediaSolrCollectionName")));

					DbConfDto measureDbSolr=new DbConfDto();
					measureDbSolr.setCollection(takeNvlValues( obj.get("measuresSolrCollectionName")));
					
					DbConfDto socialDbSolr=new DbConfDto();
					socialDbSolr.setCollection(takeNvlValues( obj.get("socialSolrCollectionName")));
					
					DbConfDto dataDbPhoenix=new DbConfDto();
					dataDbPhoenix.setCollection(takeNvlValues( obj.get("dataPhoenixCollectionName")));
					dataDbPhoenix.setDataBase(takeNvlValues( obj.get("dataPhoenixSchemaName")));
					
					DbConfDto mediaDbPhoenix=new DbConfDto();
					mediaDbPhoenix.setCollection(takeNvlValues( obj.get("mediaPhoenixCollectionName")));
					mediaDbPhoenix.setDataBase(takeNvlValues( obj.get("mediaPhoenixSchemaName")));

					DbConfDto measureDbPhoenix=new DbConfDto();
					measureDbPhoenix.setCollection(takeNvlValues( obj.get("measuresPhoenixCollectionName")));
					measureDbPhoenix.setDataBase(takeNvlValues( obj.get("measuresPhoenixSchemaName")));
					
					
					DbConfDto socialDbPhoenix=new DbConfDto();
					socialDbPhoenix.setCollection(takeNvlValues( obj.get("socialPhoenixCollectionName")));
					socialDbPhoenix.setDataBase(takeNvlValues( obj.get("socialPhoenixSchemaName")));
					

					params.put(tenant+"__"+DB_SOCIAL_PHOENIX, socialDbPhoenix);
					params.put(tenant+"__"+DB_MESURES_PHOENIX, measureDbPhoenix);
					params.put(tenant+"__"+DB_DATA_PHOENIX, dataDbPhoenix);
					params.put(tenant+"__"+DB_MEDIA_PHOENIX, mediaDbPhoenix);	

					
					params.put(tenant+"__"+DB_SOCIAL_SOLR, socialDbSolr);
					
					params.put(tenant+"__"+DB_MESURES_SOLR, measureDbSolr);
					params.put(tenant+"__"+DB_DATA_SOLR, dataDbSolr);
					params.put(tenant+"__"+DB_MEDIA_SOLR, mediaDbSolr);	
					

					params.put(tenant+"__"+DB_MESURES, measureDb);
					params.put(tenant+"__"+DB_MESURES_TRASH, measureDbTrahs);
					params.put(tenant+"__"+DB_DATA, dataDb);
					params.put(tenant+"__"+DB_DATA_TRASH, dataDbTrash);
					params.put(tenant+"__"+DB_MEDIA, mediaDb);	
					
					functionalParams.put(tenant+"__"+MAX_DOC_PER_PAGE,takeNvlValues( obj.get("maxOdataResultPerPage")));
					
				}
			} finally {
				cursor.close();
			}
		} catch (Exception e ) {
			//TODO
		}
	}

	public MongoClient getMongoClient(String host, int port) throws Exception{
		MongoClient ret=mongoConnection.get(host+"___"+port);
		if (ret!=null) return ret;
		if (host.indexOf(";")==-1) return  getMongoClientold(host, port);
		StringTokenizer st= new StringTokenizer(host,";",false);
		ArrayList<ServerAddress> arrServerAddr=new ArrayList<ServerAddress>();
		while (st.hasMoreTokens()) {
			String newHost=st.nextToken();
			ServerAddress serverAddr=new ServerAddress(newHost,port);
			arrServerAddr.add(serverAddr);
		}



		MongoClient mongoClient = null;
		if (SDPDataApiConfig.getInstance().getMongoDefaultPassword()!=null && SDPDataApiConfig.getInstance().getMongoDefaultPassword().trim().length()>0 && 
				SDPDataApiConfig.getInstance().getMongoDefaultUser()!=null && SDPDataApiConfig.getInstance().getMongoDefaultUser().trim().length()>0	) {
			MongoCredential credential = MongoCredential.createMongoCRCredential(SDPDataApiConfig.getInstance().getMongoDefaultUser(), 
					"admin", 
					SDPDataApiConfig.getInstance().getMongoDefaultPassword().toCharArray());
			mongoClient = new MongoClient(arrServerAddr,Arrays.asList(credential));
		} else {
			mongoClient = new MongoClient(arrServerAddr);
		}
		log.info("[MongoTenantDbSingleton::getMongoClient] refresh add client  " +host+"___"+port);

		mongoConnection.put(host+"___"+port, mongoClient);
		return mongoClient;


	}

	public MongoClient getMongoClientold(String host, int port) throws Exception{
		MongoClient ret=mongoConnection.get(host+"___"+port);
		if (ret!=null) return ret;
		ServerAddress serverAddr=new ServerAddress(host,port);
		MongoClient mongoClient = null;
		if (SDPDataApiConfig.getInstance().getMongoDefaultPassword()!=null && SDPDataApiConfig.getInstance().getMongoDefaultPassword().trim().length()>0 && 
				SDPDataApiConfig.getInstance().getMongoDefaultUser()!=null && SDPDataApiConfig.getInstance().getMongoDefaultUser().trim().length()>0	) {
			MongoCredential credential = MongoCredential.createMongoCRCredential(SDPDataApiConfig.getInstance().getMongoDefaultUser(), 
					"admin", 
					SDPDataApiConfig.getInstance().getMongoDefaultPassword().toCharArray());
			mongoClient = new MongoClient(serverAddr,Arrays.asList(credential));
		} else {
			mongoClient = new MongoClient(serverAddr);
		}
		mongoConnection.put(host+"___"+port, mongoClient);
		return mongoClient;


	}	


}
