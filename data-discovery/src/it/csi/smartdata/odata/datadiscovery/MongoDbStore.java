package it.csi.smartdata.odata.datadiscovery;

import it.csi.smartdata.odata.dbconfig.ConfigParamsSingleton;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.provider.Facets;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoDbStore {

	Map<String,String> mongoParams = null;
	public MongoDbStore(){
		mongoParams = ConfigParamsSingleton.getInstance().getParams();
	}

	public Map<String, Object> getDataset(String idDataset) {


		Map<String,Object> ret = new HashMap<String,Object>();
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient(mongoParams.get("MONGO_HOST"), Integer.parseInt(mongoParams.get("MONGO_PORT")));

			DB db = mongoClient.getDB(mongoParams.get("MONGO_DB_META"));
			DBCollection colldataset = db.getCollection(mongoParams.get("MONGO_COLLECTION_DATASET"));
			
			DBCollection collapi = db.getCollection(mongoParams.get("MONGO_COLLECTION_API"));
			DBCollection collstream = db.getCollection(mongoParams.get("MONGO_COLLECTION_STREAM"));
			
			DBObject searchById = new BasicDBObject("configData.idDataset", idDataset);
			DBObject found = colldataset.findOne(searchById);


			if (found != null) {

				DBObject configData = (DBObject) found.get("configData");
				String id = configData.get("idDataset").toString();
				String tenant=configData.get("tenant").toString();

				DBObject dataset = (DBObject) found.get("dataset");

				String licence=(String)dataset.get("licence");
				String dataDomain=(String)dataset.get("dataDomain");
				Double fps=(Double) dataset.get("fps");
				StringBuilder fieldsBuilder = new StringBuilder();

				String name =(String) dataset.get("name");
				String visibility=(String)dataset.get("visibility");
				String registrationDate=(String)dataset.get("registrationDate");
				String startIngestionDate=(String)dataset.get("startIngestionDate");
				String endIngestionDate=(String)dataset.get("endIngestionDate");
				String importFileType=(String)dataset.get("importFileType");
				String datasetStatus=(String)dataset.get("datasetStatus");

				BasicDBList fieldsList = (BasicDBList) dataset.get("fields");
				for (int i =0;i<fieldsList.size();i++){
					DBObject measure = (DBObject) fieldsList.get(i);
					fieldsBuilder.append(measure.get("measureUnit").toString());
					fieldsBuilder.append(",");
				}

				String unitaMisura=fieldsBuilder.toString();

				StringBuilder tagsBuilder = new StringBuilder();
				BasicDBList  tagsList = (BasicDBList) dataset.get("tags");

				for (int i =0;i<tagsList.size();i++){
					DBObject tagObj = (DBObject) tagsList.get(i);
					tagsBuilder.append(tagObj.get("tagCode").toString());
					tagsBuilder.append(",");
				}
				String tags = tagsBuilder.toString();

				Map<String,Object> cur = new HashMap<String, Object>();
				cur.put("idDataset", id);
				cur.put("tenant", tenant);
				cur.put("dataDomain", dataDomain);
				cur.put("licence", licence);
				cur.put("fps", fps);
				cur.put("measureUnit", unitaMisura);
				cur.put("tags",tags );
				
//				cur.put("API", "COMING SOON");
//				cur.put("STREAM","COMING SOON" );

				cur.put("name", name);
				cur.put("visibility", visibility);
				cur.put("registrationDate", registrationDate);
				cur.put("startIngestionDate", startIngestionDate);
				cur.put("endIngestionDate", endIngestionDate);
				cur.put("importFileType", importFileType);
				cur.put("datasetStatus", datasetStatus);
				
				
				BasicDBObject findapi = new BasicDBObject();
				findapi.append("dataset.idDataset", id);
				DBCursor apis = collapi.find(findapi);
				
				
				StringBuilder apibuilder = new StringBuilder(); 
				while(apis.hasNext()){
					DBObject obj = apis.next();
					DBObject config = (DBObject) obj.get("configData");
					apibuilder.append(mongoParams.get("MONGO_API_ADDRESS"));
					apibuilder.append("/");					
					apibuilder.append(config.get("codiceApi"));
					apibuilder.append(",");
				}
				
				cur.put("API",apibuilder.toString());
				
				BasicDBObject findstream = new BasicDBObject();
				findapi.append("configData.idDataset", id);
				DBCursor streams = collstream.find(findstream);
				
				
				StringBuilder streambuilder = new StringBuilder(); 
				while(streams.hasNext()){
					DBObject obj = streams.next();
					DBObject streamsObj = (DBObject) obj.get("streams");
					DBObject stream = (DBObject) streamsObj.get("stream");
					streambuilder.append(mongoParams.get("MONGO_STREAM_TOPIC"));
					streambuilder.append("/");					
					streambuilder.append(stream.get("codiceTenant"));
					streambuilder.append("/");
					streambuilder.append(stream.get("codiceVirtualEntity"));
					streambuilder.append("_");
					streambuilder.append(stream.get("codiceStream"));
					streambuilder.append(",");
				}
				cur.put("STREAM",streambuilder.toString() );
				ret=cur;
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;

	}


	public List<Map<String, Object>> getAllFilteredDatasets(Object userQuery) {
		List<Map<String,Object>> ret = new ArrayList<Map<String,Object>>();
		MongoClient mongoClient;
		try {
			//			mongoClient = new MongoClient("tst-sdnet-bgslave1.sdp.csi.it", 27017);
			mongoClient = new MongoClient(mongoParams.get("MONGO_HOST"), Integer.parseInt(mongoParams.get("MONGO_PORT")));

			DB db = mongoClient.getDB(mongoParams.get("MONGO_DB_META"));
			DBCollection coll = db.getCollection(mongoParams.get("MONGO_COLLECTION_DATASET"));
			DBCollection collapi = db.getCollection(mongoParams.get("MONGO_COLLECTION_API"));
			DBCollection collstream = db.getCollection(mongoParams.get("MONGO_COLLECTION_STREAM"));

			BasicDBObject query = (BasicDBObject) userQuery;
			DBCursor cursor = coll.find(query);

			while (cursor.hasNext()) {

				DBObject obj=cursor.next();
				DBObject configData = (DBObject) obj.get("configData");
				String id = configData.get("idDataset").toString();
				String tenant=configData.get("tenant").toString();

				DBObject dataset = (DBObject) obj.get("dataset");

				String licence=(String)dataset.get("licence");
				String dataDomain=(String)dataset.get("dataDomain");
				Double fps=(Double) dataset.get("fps");
				
				String name = (String)dataset.get("name");
				String visibility=(String)dataset.get("visibility");
				String registrationDate=(String)dataset.get("registrationDate");
				String startIngestionDate=(String)dataset.get("startIngestionDate");
				String endIngestionDate=(String)dataset.get("endIngestionDate");
				String importFileType=(String)dataset.get("importFileType");
				String datasetStatus=(String)dataset.get("datasetStatus");
				
				
				StringBuilder fieldsBuilder = new StringBuilder();
				BasicDBList fieldsList = (BasicDBList) dataset.get("fields");

				for (int i =0;i<fieldsList.size();i++){
					DBObject measure = (DBObject) fieldsList.get(i);
					fieldsBuilder.append(measure.get("measureUnit").toString());
					fieldsBuilder.append(",");
				}
				String unitaMisura=fieldsBuilder.toString();
				StringBuilder tagsBuilder = new StringBuilder();
				BasicDBList  tagsList = (BasicDBList) dataset.get("tags");

				for (int i =0;i<tagsList.size();i++){
					DBObject tagObj = (DBObject) tagsList.get(i);
					tagsBuilder.append(tagObj.get("tagCode").toString());
					tagsBuilder.append(",");
				}
				String tags = tagsBuilder.toString();

				Map<String,Object> cur = new HashMap<String, Object>();
				cur.put("idDataset", id);
				cur.put("tenant", tenant);
				cur.put("dataDomain", dataDomain);
				cur.put("licence", licence);
				cur.put("fps", fps);
				
				cur.put("measureUnit", unitaMisura);
				cur.put("tags",tags );
				
				BasicDBObject findapi = new BasicDBObject();
				findapi.append("dataset.idDataset", id);
				DBCursor apis = collapi.find(findapi);
				
				
				StringBuilder apibuilder = new StringBuilder(); 
				while(apis.hasNext()){
					DBObject parent = apis.next();
					DBObject config = (DBObject) parent.get("configData");
					apibuilder.append(mongoParams.get("MONGO_API_ADDRESS"));
					apibuilder.append("/");					
					apibuilder.append(config.get("codiceApi"));
					apibuilder.append(",");
				}
				
				cur.put("API",apibuilder.toString());
				
				BasicDBObject findstream = new BasicDBObject();
				findapi.append("configData.idDataset", id);
				DBCursor streams = collstream.find(findstream);
				
				
				StringBuilder streambuilder = new StringBuilder(); 
				while(streams.hasNext()){
					DBObject nx = streams.next();
					DBObject streamsObj = (DBObject) nx.get("streams");
					DBObject stream = (DBObject) streamsObj.get("stream");
					streambuilder.append(mongoParams.get("MONGO_STREAM_TOPIC"));
					streambuilder.append("/");					
					streambuilder.append(stream.get("codiceTenant"));
					streambuilder.append("/");
					streambuilder.append(stream.get("codiceVirtualEntity"));
					streambuilder.append("_");
					streambuilder.append(stream.get("codiceStream"));
					streambuilder.append(",");
				}
				cur.put("STREAM",streambuilder.toString() );
		
				cur.put("name", name);
				cur.put("visibility", visibility);
				cur.put("registrationDate", registrationDate);
				cur.put("startIngestionDate", startIngestionDate);
				cur.put("endIngestionDate", endIngestionDate);
				cur.put("importFileType", importFileType);
				cur.put("datasetStatus", datasetStatus);
				
				ret.add(cur);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}


	public List<Map<String, Object>> getDatasetFields(String datasetKey) {

		List<Map<String,Object>> ret = new ArrayList<Map<String,Object>>();
		MongoClient mongoClient;
		try {
			//			mongoClient = new MongoClient("tst-sdnet-bgslave1.sdp.csi.it", 27017);
			mongoClient = new MongoClient(mongoParams.get("MONGO_HOST"), Integer.parseInt(mongoParams.get("MONGO_PORT")));

			DB db = mongoClient.getDB(mongoParams.get("MONGO_DB_META"));
			DBCollection coll = db.getCollection(mongoParams.get("MONGO_COLLECTION_DATASET"));


			BasicDBObject query = new BasicDBObject();
			query.append("configData.idDataset", datasetKey);
			DBCursor cursor = coll.find(query);

			while (cursor.hasNext()) {

				DBObject obj=cursor.next();

				DBObject dataset = (DBObject) obj.get("dataset");

				BasicDBList fieldsList = (BasicDBList) dataset.get("fields");

				for (int i =0;i<fieldsList.size();i++){
					DBObject measure = (DBObject) fieldsList.get(i);

					String fieldName = (String) measure.get("fieldName");
					String fieldAlias = (String)measure.get("fieldAlias");
					String dataType = (String)measure.get("dataType");
					Integer sourceColumn = (Integer) measure.get("sourceColumn");
					Integer isKey = (Integer)measure.get("isKey");
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
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

}
