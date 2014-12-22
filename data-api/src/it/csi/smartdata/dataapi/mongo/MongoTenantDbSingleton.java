package it.csi.smartdata.dataapi.mongo;

import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;
import it.csi.smartdata.dataapi.mongo.dto.DbConfDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoTenantDbSingleton {
	
	public static final String DB_MESURES="DBMEASURES";
	public static final String DB_DATA="DBDATA";
	public static final String DB_MESURES_TRASH="DBMEASURES_TRASH";
	public static final String DB_DATA_TRASH="DBDATA_TRASH";
	
	public static MongoTenantDbSingleton instance=null;
	private static int anno_init = 0;
	private static int mese_init = 0;
	private static int giorno_init = 0;

	
	private HashMap<String, DbConfDto> params = new HashMap<String, DbConfDto>();
	private HashMap<String, MongoClient> mongoConnection = new HashMap<String, MongoClient>();
		

	
	private static boolean singletonToRefresh() {
		int curAnno = Calendar.getInstance().get(Calendar.YEAR);
		int curMese = Calendar.getInstance().get(Calendar.MONTH);
		int curGiorno = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if (curAnno > anno_init) return true;
		else if (curMese > mese_init) return true;
		else if (curGiorno > giorno_init)return true;
		return false;
	}
	public synchronized static MongoTenantDbSingleton getInstance() throws Exception{
		if(instance == null || singletonToRefresh()) {
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
			
			DB db = mongoClient.getDB(SDPDataApiConfig.getInstance().getMongoCfgDB(SDPDataApiConfig.MONGO_DB_CFG_TENANT));
			DBCollection coll = db.getCollection(SDPDataApiConfig.getInstance().getMongoCfgCollection(SDPDataApiConfig.MONGO_DB_CFG_TENANT));

			DBCursor cursor = coll.find();
			try {
				while (cursor.hasNext()) {

					DBObject obj=cursor.next();
					
					String tenant=obj.get("tenantCode").toString();

					
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

					
					DbConfDto dataDb=new DbConfDto();
					dataDb.setHost(SDPDataApiConfig.getInstance().getMongoDefaultHost());
					dataDb.setDataBase(takeNvlValues( obj.get("dataCollectionDb")));
					dataDb.setCollection(takeNvlValues( obj.get("dataCollectionName")));
					dataDb.setPort(SDPDataApiConfig.getInstance().getMongoDefaultPort());
					
					
					params.put(tenant+"__"+DB_MESURES, measureDb);
					params.put(tenant+"__"+DB_MESURES_TRASH, measureDbTrahs);
					params.put(tenant+"__"+DB_DATA, dataDb);
					params.put(tenant+"__"+DB_DATA_TRASH, dataDbTrash);
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
	
	public DbConfDto getDataDbConfiguration(String dbType,String tenantCode) {
		return params.get(tenantCode+"__"+dbType);
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
