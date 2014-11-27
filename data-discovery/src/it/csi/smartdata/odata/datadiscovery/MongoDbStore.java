package it.csi.smartdata.odata.datadiscovery;

import it.csi.smartdata.odata.dbconfig.ConfigParamsSingleton;
import it.csi.smartdata.odata.dbconfig.MongoSingleton;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoDbStore {
	MongoClient mongoClient;
	Map<String,String> mongoParams = null;
	//	MongoCredential credential=null;
	private Logger logger = Logger.getLogger(MongoDbStore.class);
	public MongoDbStore(){
		mongoParams = ConfigParamsSingleton.getInstance().getParams();
		//		credential = MongoCredential.createMongoCRCredential(mongoParams.get("MONGO_USERNAME"), mongoParams.get("MONGO_DB_AUTH"), mongoParams.get("MONGO_PASSWORD").toCharArray());

		try {
			//			ServerAddress serverAdd = new ServerAddress(mongoParams.get("MONGO_HOST"), Integer.parseInt(mongoParams.get("MONGO_PORT")));
			mongoClient = MongoSingleton.getMongoClient();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public Map<String, Object> getDataset(Long idDataset) {


		Map<String,Object> ret = new HashMap<String,Object>();


		DB db = mongoClient.getDB(mongoParams.get("MONGO_DB_META"));
		DBCollection colldataset = db.getCollection(mongoParams.get("MONGO_COLLECTION_DATASET"));

		DBCollection collapi = db.getCollection(mongoParams.get("MONGO_COLLECTION_API"));
		DBCollection collstream = db.getCollection(mongoParams.get("MONGO_COLLECTION_STREAM"));

		DBObject searchById = new BasicDBObject("idDataset", idDataset);
		DBObject found = colldataset.findOne(searchById);


		if (found != null) {
			Long id = found.get("idDataset") == null ? null :((Number)found.get("idDataset")).longValue();
			Long datasetVersion = found.get("datasetVersion") == null ? null :((Number)found.get("datasetVersion")).longValue();
			String datasetCode =(String)found.get("datasetCode") ;
			DBObject configData = (DBObject) found.get("configData");
			String tenant=configData.get("tenantCode").toString();
			String datasetStatus=(String)configData.get("datasetStatus");


			DBObject info = (DBObject) found.get("info");

			String licence=(String)info.get("licence");
			String dataDomain=(String)info.get("dataDomain");
			String description = (String)info.get("description");
			Double fps = info.get("fps") ==null ? null : ((Number)info.get("fps")).doubleValue();

			String datasetName = (String)info.get("datasetName");
			String visibility=(String)info.get("visibility");
			String registrationDate=(String)info.get("registrationDate");
			String startIngestionDate=(String)info.get("startIngestionDate");
			String endIngestionDate=(String)info.get("endIngestionDate");
			String importFileType=(String)info.get("importFileType");



			StringBuilder fieldsBuilder = new StringBuilder();
			BasicDBList fieldsList = (BasicDBList) info.get("fields");

			for (int i =0;i<fieldsList.size();i++){
				DBObject measure = (DBObject) fieldsList.get(i);
				String mis = measure.get("measureUnit") == null ? null : measure.get("measureUnit").toString();
				if(mis!=null){
					fieldsBuilder.append(mis);
					fieldsBuilder.append(",");
				}
			}
			String unitaMisura=fieldsBuilder.toString();
			StringBuilder tagsBuilder = new StringBuilder();
			BasicDBList  tagsList = (BasicDBList) info.get("tags");

			String tags=null;
			if(tagsList!=null){
				for (int i =0;i<tagsList.size();i++){
					DBObject tagObj = (DBObject) tagsList.get(i);
					tagsBuilder.append(tagObj.get("tagCode").toString());
					tagsBuilder.append(",");
				}
				tags = tagsBuilder.toString();
			}
			Map<String,Object> cur = new HashMap<String, Object>();
			cur.put("idDataset", id);
			cur.put("tenantCode", tenant);
			cur.put("dataDomain", dataDomain);
			cur.put("licence", licence);
			cur.put("description", description);
			
			cur.put("fps", fps);

			cur.put("measureUnit", unitaMisura);
			cur.put("tags",tags );

			BasicDBObject findapi = new BasicDBObject();
			findapi.append("dataset.idDataset", id);
			findapi.append("dataset.datasetVersion", datasetVersion);

			DBCursor apis = collapi.find(findapi);


			StringBuilder apibuilder = new StringBuilder(); 
			while(apis.hasNext()){
				DBObject parent = apis.next();
				//				DBObject config = (DBObject) parent.get("configData");
				apibuilder.append(mongoParams.get("MONGO_API_ADDRESS"));
				apibuilder.append("/");					
				apibuilder.append(parent.get("apiCode"));
				if(apis.hasNext())
					apibuilder.append(",");
			}

			cur.put("API",apibuilder.toString());

			BasicDBObject findstream = new BasicDBObject();
			findstream.append("configData.idDataset", id);
			findstream.append("configData.datasetVersion", datasetVersion);
			DBCursor streams = collstream.find(findstream);


			StringBuilder streambuilder = new StringBuilder(); 
			while(streams.hasNext()){
				DBObject nx = streams.next();
				DBObject config = (DBObject) nx.get("configData");
				DBObject streamsObj = (DBObject) nx.get("streams");
				DBObject stream = (DBObject) streamsObj.get("stream");
				streambuilder.append(mongoParams.get("MONGO_STREAM_TOPIC"));
				streambuilder.append("/");					
				streambuilder.append(config.get("tenantCode"));
				streambuilder.append("/");
				streambuilder.append(stream.get("virtualEntityCode"));
				streambuilder.append("_");
				streambuilder.append(nx.get("streamCode"));
				if(streams.hasNext())
					streambuilder.append(",");
			}
			
			
			String download = mongoParams.get("MONGO_DOWNLOAD_ADDRESS")+"/"+tenant+"/"+datasetCode+"/csv";
			
			
			cur.put("STREAM",streambuilder.toString() );
			
			
			
			
			cur.put("download", download);

			cur.put("datasetName", datasetName);
			cur.put("visibility", visibility);
			cur.put("registrationDate", registrationDate);
			cur.put("startIngestionDate", startIngestionDate);
			cur.put("endIngestionDate", endIngestionDate);
			cur.put("importFileType", importFileType);
			cur.put("datasetStatus", datasetStatus);

			ret=cur;
		}



		return ret;

	}


	public List<Map<String, Object>> getAllFilteredDatasets(Object userQuery) {
		List<Map<String,Object>> ret = new ArrayList<Map<String,Object>>();
		DB db = mongoClient.getDB(mongoParams.get("MONGO_DB_META"));
		DBCollection coll = db.getCollection(mongoParams.get("MONGO_COLLECTION_DATASET"));
		DBCollection collapi = db.getCollection(mongoParams.get("MONGO_COLLECTION_API"));
		DBCollection collstream = db.getCollection(mongoParams.get("MONGO_COLLECTION_STREAM"));

		BasicDBObject query = (BasicDBObject) userQuery;
		DBCursor cursor = coll.find(query);

		while (cursor.hasNext()) {

			DBObject obj=cursor.next();

			Long id = obj.get("idDataset") == null ? null :((Number)obj.get("idDataset")).longValue();
			Long datasetVersion = obj.get("datasetVersion") == null ? null :((Number)obj.get("datasetVersion")).longValue();
			String datasetCode =(String)obj.get("datasetCode") ;
			DBObject configData = (DBObject) obj.get("configData");
			String tenant=configData.get("tenantCode").toString();
			String datasetStatus=(String)configData.get("datasetStatus");


			DBObject info = (DBObject) obj.get("info");

			String licence=(String)info.get("licence");
			String dataDomain=(String)info.get("dataDomain");
			String description = (String)info.get("description");
			Double fps = info.get("fps") ==null ? null : ((Number)info.get("fps")).doubleValue();

			String datasetName = (String)info.get("datasetName");
			String visibility=(String)info.get("visibility");
			String registrationDate=(String)info.get("registrationDate");
			String startIngestionDate=(String)info.get("startIngestionDate");
			String endIngestionDate=(String)info.get("endIngestionDate");
			String importFileType=(String)info.get("importFileType");



			StringBuilder fieldsBuilder = new StringBuilder();
			BasicDBList fieldsList = (BasicDBList) info.get("fields");

			for (int i =0;i<fieldsList.size();i++){
				DBObject measure = (DBObject) fieldsList.get(i);
				String mis = measure.get("measureUnit") == null ? null : measure.get("measureUnit").toString();
				if(mis!=null){
					fieldsBuilder.append(mis);
					fieldsBuilder.append(",");
				}
			}
			String unitaMisura=fieldsBuilder.toString();
			StringBuilder tagsBuilder = new StringBuilder();

			BasicDBList  tagsList = (BasicDBList) info.get("tags");
			String tags=null;
			if(tagsList!=null){
				for (int i =0;i<tagsList.size();i++){
					DBObject tagObj = (DBObject) tagsList.get(i);
					tagsBuilder.append(tagObj.get("tagCode").toString());
					tagsBuilder.append(",");
				}
				tags = tagsBuilder.toString();
			}
			Map<String,Object> cur = new HashMap<String, Object>();
			cur.put("idDataset", id);
			cur.put("tenantCode", tenant);
			cur.put("dataDomain", dataDomain);
			cur.put("licence", licence);
			cur.put("description", description);
			cur.put("fps", fps);

			cur.put("measureUnit", unitaMisura);
			cur.put("tags",tags );

			BasicDBObject findapi = new BasicDBObject();
			findapi.append("dataset.idDataset", id);
			findapi.append("dataset.datasetVersion", datasetVersion);

			DBCursor apis = collapi.find(findapi);


			StringBuilder apibuilder = new StringBuilder(); 
			while(apis.hasNext()){
				DBObject parent = apis.next();
				DBObject config = (DBObject) parent.get("configData");
				apibuilder.append(mongoParams.get("MONGO_API_ADDRESS"));
				apibuilder.append("/");					
				apibuilder.append(config.get("entityNameSpace"));
				apibuilder.append(",");
			}

			cur.put("API",apibuilder.toString());

			BasicDBObject findstream = new BasicDBObject();
			findstream.append("configData.idDataset", id);
			findstream.append("configData.datasetVersion", datasetVersion);
			DBCursor streams = collstream.find(findstream);


			StringBuilder streambuilder = new StringBuilder(); 
			while(streams.hasNext()){
				DBObject nx = streams.next();
				DBObject config = (DBObject) nx.get("configData");
				DBObject streamsObj = (DBObject) nx.get("streams");
				DBObject stream = (DBObject) streamsObj.get("stream");
				streambuilder.append(mongoParams.get("MONGO_STREAM_TOPIC"));
				streambuilder.append("/");					
				streambuilder.append(config.get("tenantCode"));
				streambuilder.append("/");
				streambuilder.append(stream.get("virtualEntityCode"));
				streambuilder.append("_");
				streambuilder.append(nx.get("streamCode"));
				streambuilder.append(",");
			}
			cur.put("STREAM",streambuilder.toString() );
			
			
			
			String download = mongoParams.get("MONGO_DOWNLOAD_ADDRESS")+"/"+tenant+"/"+datasetCode+"/csv";

			cur.put("download", download);
			
			cur.put("datasetName", datasetName);
			cur.put("visibility", visibility);
			cur.put("registrationDate", registrationDate);
			cur.put("startIngestionDate", startIngestionDate);
			cur.put("endIngestionDate", endIngestionDate);
			cur.put("importFileType", importFileType);
			cur.put("datasetStatus", datasetStatus);

			ret.add(cur);
		}

		return ret;
	}


	public List<Map<String, Object>> getDatasetFields(Long datasetKey) {

		List<Map<String,Object>> ret = new ArrayList<Map<String,Object>>();
		DB db = mongoClient.getDB(mongoParams.get("MONGO_DB_META"));
		DBCollection coll = db.getCollection(mongoParams.get("MONGO_COLLECTION_DATASET"));


		BasicDBObject query = new BasicDBObject();
		query.append("idDataset", datasetKey);
		DBCursor cursor = coll.find(query);

		while (cursor.hasNext()) {

			DBObject obj=cursor.next();

			DBObject dataset = (DBObject) obj.get("info");

			BasicDBList fieldsList = (BasicDBList) dataset.get("fields");

			for (int i =0;i<fieldsList.size();i++){
				DBObject measure = (DBObject) fieldsList.get(i);

				String fieldName = (String) measure.get("fieldName");
				String fieldAlias = (String)measure.get("fieldAlias");
				String dataType = (String)measure.get("dataType");
				Integer sourceColumn = measure.get("sourceColumn") == null ? null : ((Number) measure.get("sourceColumn")).intValue();
				Integer isKey = measure.get("isKey") == null ? null :  ((Number)measure.get("isKey")).intValue();

				String measureUnit = (String)measure.get("measureUnit");

				Map<String,Object> cur = new HashMap<String, Object>();
				cur.put("fieldName", fieldName);
				cur.put("fieldAlias", fieldAlias);
				cur.put("dataType", dataType);
				cur.put("sourceColumn", sourceColumn);
				cur.put("isKey", isKey);
				cur.put("measureUnit", measureUnit);
				ret.add(cur);
			}
		}

		return ret;
	}

}
