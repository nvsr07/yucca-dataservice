package org.csi.yucca.dataservice.ingest.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.csi.yucca.dataservice.ingest.model.metadata.BinaryData;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class MongoDBBinaryDAO {
	private DBCollection collection;
	static Logger log = Logger.getLogger(MongoDBBinaryDAO.class);

	public MongoDBBinaryDAO(MongoClient mongo, String db, String collection) {
		this.collection = mongo.getDB(db).getCollection(collection);
	}

	public void createBinary(BinaryData binary) {
		try {
			BasicDBObject newObj = new BasicDBObject();

			newObj.put("tenantBinary", binary.getTenantBinary());
			newObj.put("filenameBinary", binary.getFilenameBinary());
			newObj.put("idBinary", binary.getIdBinary());
			newObj.put("sizeBinary", binary.getSizeBinary());
			newObj.put("contentTypeBinary", binary.getContentTypeBinary());
			newObj.put("aliasNameBinary", binary.getAliasNameBinary());
			newObj.put("pathHdfsBinary", binary.getPathHdfsBinary());
			newObj.put("insertDateBinary", new java.util.Date());
			newObj.put("lastUpdateDateBinary", new java.util.Date());
			newObj.put("idDataset", binary.getIdDataset());
			newObj.put("datasetVersion", binary.getDatasetVersion());

			this.collection.insert(newObj);
		} catch (Exception e) {
			log.error("[] - ERROR in insert. Message: " + e.getMessage());
		}
	}

	public BinaryData createNewVersion(BinaryData binary) {

		binary.setDatasetVersion(binary.getDatasetVersion() + 1);

		String json = binary.toJson();
		DBObject dbObject = (DBObject) JSON.parse(json);

		this.collection.insert(dbObject);
		ObjectId id = (ObjectId) dbObject.get("_id");
		binary.setId(id.toString());
		return binary;
	}

	public void updateBinaryData(BinaryData binary) {
		DBObject query = BasicDBObjectBuilder.start().append("_id", new ObjectId(binary.getId())).get();
		
		BasicDBObject newObj = new BasicDBObject();

		newObj.put("tenantBinary", binary.getTenantBinary());
		newObj.put("filenameBinary", binary.getFilenameBinary());
		newObj.put("idBinary", binary.getIdBinary());
		newObj.put("sizeBinary", binary.getSizeBinary());
		newObj.put("contentTypeBinary", binary.getContentTypeBinary());
		newObj.put("aliasNameBinary", binary.getAliasNameBinary());
		newObj.put("pathHdfsBinary", binary.getPathHdfsBinary());
		newObj.put("insertDateBinary", binary.getInsertDateBinary());
		newObj.put("lastUpdateDateBinary", binary.getLastUpdateDateBinary());
		newObj.put("idDataset", binary.getIdDataset());
		newObj.put("datasetVersion", binary.getDatasetVersion());
		newObj.put("metadataBinary", binary.getMetadataBinary());

		this.collection.update(query, newObj);
	}

	public void deleteBinaryData(BinaryData binary) {
		DBObject query = BasicDBObjectBuilder.start().append("_id", new ObjectId(binary.getId())).get();
		this.collection.remove(query);
	}

	public BinaryData readBinaryData(BinaryData binary) {
		DBObject query = BasicDBObjectBuilder.start().append("_id", new ObjectId(binary.getId())).get();
		DBObject data = this.collection.findOne(query);
		ObjectId id = (ObjectId) data.get("_id");
		BinaryData binaryLoaded = BinaryData.fromJson(JSON.serialize(data));
		binaryLoaded.setId(id.toString());
		return binaryLoaded;
	}

	public BinaryData readCurrentBinaryDataByIdBinary(String idBinary) {
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("idBinary", idBinary);
		
		DBObject data = collection.find(searchQuery).one();
		BinaryData binaryLoaded = null;
		
		if (data != null){
			
			ObjectId id = (ObjectId) data.get("_id");
			
			binaryLoaded = new BinaryData(data);
			binaryLoaded.setId(id.toString());
		}
		return binaryLoaded;
	}

	public BinaryData readCurrentBinaryDataByTntAndIDDS(Long idDataset, Integer datasetVersion, String tenantCode) {
		BasicDBObject searchQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("idDataset", idDataset));
		obj.add(new BasicDBObject("tenantBinary", tenantCode));
		obj.add(new BasicDBObject("datasetVersion", datasetVersion));
		searchQuery.put("$and", obj);

		DBObject data = collection.find(searchQuery).one();
		ObjectId id = (ObjectId) data.get("_id");
		BinaryData binaryLoaded = BinaryData.fromJson(JSON.serialize(data));
		binaryLoaded.setId(id.toString());
		return binaryLoaded;
	}

}
