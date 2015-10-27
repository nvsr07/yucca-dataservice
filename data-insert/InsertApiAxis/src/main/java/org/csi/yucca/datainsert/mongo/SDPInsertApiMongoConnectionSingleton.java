package org.csi.yucca.datainsert.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.csi.yucca.datainsert.constants.SDPInsertApiConfig;
import org.csi.yucca.datainsert.dto.DbConfDto;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class SDPInsertApiMongoConnectionSingleton {
	public static final String MONGO_DB_CFG_TENANT="MONGO_DB_CFG_TENANT";
	public static final String MONGO_DB_CFG_METADATA="MONGO_DB_CFG_METADATA";
	public static final String MONGO_DB_CFG_STREAM="MONGO_DB_CFG_STREAM";
	public static final String MONGO_DB_CFG_APPOGGIO="MONGO_DB_CFG_APPOGGIO";
	public static final String MONGO_DB_CFG_STATUS="MONGO_DB_CFG_STATUS";


	public static final String DB_MESURES="DBMEASURES";
	public static final String DB_DATA="DBDATA";
	public static final String DB_MESURES_TRASH="DBMEASURES_TRASH";
	public static final String DB_DATA_TRASH="DBDATA_TRASH";
	
	
	private static int anno_init = 0;
	private static int mese_init = 0;
	private static int giorno_init = 0;
	public static SDPInsertApiMongoConnectionSingleton instance=null;

	private HashMap<String, DbConfDto> params = new HashMap<String, DbConfDto>();
	private HashMap<String, MongoClient> mongoConnection = new HashMap<String, MongoClient>();
	private HashMap<String, MongoClient> mongoTenantConnection = new HashMap<String, MongoClient>();
	private static boolean singletonToRefresh() {
		int curAnno = Calendar.getInstance().get(Calendar.YEAR);
		int curMese = Calendar.getInstance().get(Calendar.MONTH);
		int curGiorno = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if (curAnno > anno_init) return true;
		else if (curMese > mese_init) return true;
		else if (curGiorno > giorno_init)return true;
		return false;
	}
	public synchronized static SDPInsertApiMongoConnectionSingleton getInstance() throws Exception{
		if(instance == null || singletonToRefresh()) {
			instance = new SDPInsertApiMongoConnectionSingleton();
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
	private SDPInsertApiMongoConnectionSingleton() throws Exception{
		DBCursor cursor=null;
		try {
			mongoConnection = new HashMap<String, MongoClient>();
			params = new HashMap<String, DbConfDto>();
			mongoTenantConnection = new HashMap<String, MongoClient>();
			//STREAM
			String host=SDPInsertApiConfig.getInstance().getMongoCfgHost(SDPInsertApiConfig.MONGO_DB_CFG_STREAM);
			int port=SDPInsertApiConfig.getInstance().getMongoCfgPort(SDPInsertApiConfig.MONGO_DB_CFG_STREAM);
			
			
			mongoConnection.put(MONGO_DB_CFG_STREAM, getMongoClient(host, port));

			host=SDPInsertApiConfig.getInstance().getMongoCfgHost(SDPInsertApiConfig.MONGO_DB_CFG_METADATA);
			port=SDPInsertApiConfig.getInstance().getMongoCfgPort(SDPInsertApiConfig.MONGO_DB_CFG_METADATA);
			mongoConnection.put(MONGO_DB_CFG_METADATA, getMongoClient(host, port));

			host=SDPInsertApiConfig.getInstance().getMongoCfgHost(SDPInsertApiConfig.MONGO_DB_CFG_APPOGGIO);
			port=SDPInsertApiConfig.getInstance().getMongoCfgPort(SDPInsertApiConfig.MONGO_DB_CFG_APPOGGIO);
			mongoConnection.put(MONGO_DB_CFG_APPOGGIO, getMongoClient(host, port));


			host=SDPInsertApiConfig.getInstance().getMongoCfgHost(SDPInsertApiConfig.MONGO_DB_CFG_STATUS);
			port=SDPInsertApiConfig.getInstance().getMongoCfgPort(SDPInsertApiConfig.MONGO_DB_CFG_STATUS);
			mongoConnection.put(MONGO_DB_CFG_STATUS, getMongoClient(host, port));

			host=SDPInsertApiConfig.getInstance().getMongoCfgHost(SDPInsertApiConfig.MONGO_DB_CFG_TENANT);
			port=SDPInsertApiConfig.getInstance().getMongoCfgPort(SDPInsertApiConfig.MONGO_DB_CFG_TENANT);
			mongoConnection.put(MONGO_DB_CFG_TENANT, getMongoClient(host, port));

			MongoClient mongoClient =getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_TENANT);	
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_TENANT));
			String collection=SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_TENANT);
			DBCollection coll = db.getCollection(collection);

			cursor = coll.find();
			try {
				while (cursor.hasNext()) {

					DBObject obj=cursor.next();

					String tenant=obj.get("tenantCode").toString();
					
					DbConfDto measureDb=new DbConfDto();
					measureDb.setHost(SDPInsertApiConfig.getInstance().getMongoCfgHost(SDPInsertApiConfig.MONGO_DB_DEFAULT));
					measureDb.setDataBase(takeNvlValues( obj.get("measuresCollectionDb")));
					measureDb.setCollection(takeNvlValues( obj.get("measuresCollectionName")));
					measureDb.setPort(SDPInsertApiConfig.getInstance().getMongoCfgPort(SDPInsertApiConfig.MONGO_DB_DEFAULT));

					
					DbConfDto dataDb=new DbConfDto();
					dataDb.setHost(SDPInsertApiConfig.getInstance().getMongoCfgHost(SDPInsertApiConfig.MONGO_DB_DEFAULT));
					dataDb.setDataBase(takeNvlValues( obj.get("dataCollectionDb")));
					dataDb.setCollection(takeNvlValues( obj.get("dataCollectionName")));
					dataDb.setPort(SDPInsertApiConfig.getInstance().getMongoCfgPort(SDPInsertApiConfig.MONGO_DB_DEFAULT));
					
					
					params.put(tenant+"__"+DB_MESURES, measureDb);
					params.put(tenant+"__"+DB_DATA, dataDb);
					
				}
			} catch (Exception e) {
				//TODO log
			} finally {

			}


		} catch (Exception e) {
			//TODO log
			e.printStackTrace();
		}finally {
			try { cursor.close(); } catch (Exception ec) {}
		}
	}

	public DbConfDto getDataDbConfiguration(String dbType,String tenantCode) {
		return params.get(tenantCode+"__"+dbType);
	}



	public MongoClient getMongoClient(String databaseType) throws Exception{
		if (MONGO_DB_CFG_TENANT.equals(databaseType)) return mongoConnection.get(MONGO_DB_CFG_TENANT);
		if (MONGO_DB_CFG_METADATA.equals(databaseType)) return mongoConnection.get(MONGO_DB_CFG_METADATA);
		if (MONGO_DB_CFG_STATUS.equals(databaseType)) return mongoConnection.get(MONGO_DB_CFG_STATUS);
		if (MONGO_DB_CFG_APPOGGIO.equals(databaseType)) return mongoConnection.get(MONGO_DB_CFG_APPOGGIO);
		if (MONGO_DB_CFG_STREAM.equals(databaseType)) return mongoConnection.get(MONGO_DB_CFG_STREAM);
		throw new  Exception ("invalid mongo db or configuration error");
	}


	private MongoClient getMongoClientLocal(String host, int port) throws Exception{
		StringTokenizer st= new StringTokenizer(host,";",false);
		ArrayList<ServerAddress> arrServerAddr=new ArrayList<ServerAddress>();
		while (st.hasMoreTokens()) {
			String newHost=st.nextToken();
			ServerAddress serverAddr=new ServerAddress(newHost,port);
			arrServerAddr.add(serverAddr);
		}
		MongoCredential credential = MongoCredential.createMongoCRCredential(SDPInsertApiConfig.getInstance().getMongoDefaultUser(), 
				"admin", 
				SDPInsertApiConfig.getInstance().getMongoDefaultPassword().toCharArray());
		MongoClient mongoClient = null;
		mongoClient = new MongoClient(arrServerAddr,Arrays.asList(credential));
		return mongoClient;


	}


	public MongoClient getMongoClient(String host, int port) throws Exception{
		MongoClient ret=mongoConnection.get(host+"___"+port);
		if (ret!=null) return ret;
		ret=  getMongoClientLocal(host, port);
		
		mongoConnection.put(host+"___"+port, ret);
		return ret;
		
		
	}	
}
