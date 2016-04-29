package org.csi.yucca.dataservice.ingest.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.csi.yucca.dataservice.ingest.model.metadata.Metadata;
import org.csi.yucca.dataservice.ingest.model.metadata.Tenant;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class MongoDBTenantDAO {
	private DBCollection collection;
	static Logger log = Logger.getLogger(MongoDBMetadataDAO.class);

	public MongoDBTenantDAO(MongoClient mongo, String db, String collection) {
		this.collection = mongo.getDB(db).getCollection(collection);
	}

	public String getOrganizationByTenantCode(String tenantCode) {
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("tenantCode", tenantCode);

		DBObject data = collection.find(searchQuery).one();
		ObjectId id = (ObjectId) data.get("_id");
		Tenant tenantLoaded = Tenant.fromJson(JSON.serialize(data));
		tenantLoaded.setId(id.toString());
		return tenantLoaded.getOrganizationCode();
	}
	
	/*

	public Metadata createMetadata(Metadata metadata, Long idDataset) {

		for (int i = 0; i < 5; i++) {
			try {
				if (idDataset == null) {
					metadata.setIdDataset(MongoDBUtils.getIdForInsert(this.collection, "idDataset"));
				} else {
					metadata.setIdDataset(idDataset);
				}
				metadata.generateCode();
				metadata.generateNameSpace();

				String json = metadata.toJson();
				DBObject dbObject = (DBObject) JSON.parse(json);

				DBObject uniqueMetadata = new BasicDBObject("idDataset", metadata.getIdDataset());
				uniqueMetadata.put("datasetVersion", metadata.getDatasetVersion());

				// if the metadata with that id and version exists .. update it,
				// otherwise insert the new one.
				// upsert:true multi:false
				this.collection.update(uniqueMetadata, dbObject, true, false);
				// ObjectId id = (ObjectId) dbObject.get("_id");
				// metadata.setId(id.toString());
				break;
			} catch (Exception e) {
				log.error("[] - ERROR in insert. Attempt " + i + " - message: " + e.getMessage());
			}
		}

		return metadata;
	}

	public Metadata createNewVersion(Metadata metadata) {

		metadata.setDatasetVersion(metadata.getDatasetVersion() + 1);
		metadata.getConfigData().setCurrent(1);
		metadata.getInfo().setRegistrationDate(new Date());

		String json = metadata.toJson();
		DBObject dbObject = (DBObject) JSON.parse(json);

		this.collection.insert(dbObject);
		ObjectId id = (ObjectId) dbObject.get("_id");
		metadata.setId(id.toString());
		return metadata;
	}

	public void updateMetadata(Metadata metadata) {
		DBObject query = BasicDBObjectBuilder.start().append("_id", new ObjectId(metadata.getId())).get();
		DBObject dbObject = (DBObject) JSON.parse(metadata.toJson());
		dbObject.removeField("id");
		this.collection.update(query, dbObject);
	}

	public List<Metadata> readAllMetadata(String tenant, boolean onlyCurrent) {
		List<Metadata> data = new ArrayList<Metadata>();
		BasicDBObject searchQuery = new BasicDBObject();
		if (tenant != null)
			searchQuery.put("configData.tenantCode", tenant);
		if (onlyCurrent)
			searchQuery.put("configData.current", 1);

		DBCursor cursor = collection.find(searchQuery);
		while (cursor.hasNext()) {
			DBObject doc = cursor.next();
			ObjectId id = (ObjectId) doc.get("_id");
			Metadata metadata = Metadata.fromJson(JSON.serialize(doc));
			metadata.setId(id.toString());
			data.add(metadata);
		}
		return data;
	}

	public void deleteMetadata(Metadata metadata) {
		DBObject query = BasicDBObjectBuilder.start().append("_id", new ObjectId(metadata.getId())).get();
		this.collection.remove(query);
	}

	public Metadata readMetadata(Metadata metadata) {
		DBObject query = BasicDBObjectBuilder.start().append("_id", new ObjectId(metadata.getId())).get();
		DBObject data = this.collection.findOne(query);
		ObjectId id = (ObjectId) data.get("_id");
		Metadata metadataLoaded = Metadata.fromJson(JSON.serialize(data));
		metadataLoaded.setId(id.toString());
		return metadataLoaded;
	}

	public Metadata readCurrentMetadataByCode(String metadataCode) {
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("datasetCode", metadataCode);
		searchQuery.put("configData.current", 1);

		DBObject data = collection.find(searchQuery).one();
		ObjectId id = (ObjectId) data.get("_id");
		Metadata metadataLoaded = Metadata.fromJson(JSON.serialize(data));
		metadataLoaded.setId(id.toString());
		return metadataLoaded;
	}

	public Metadata readCurrentMetadataByTntAndDSCode(String metadataCode, String tenantCode) {
		BasicDBObject searchQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("datasetCode", metadataCode));
		obj.add(new BasicDBObject("configData.tenantCode", tenantCode));
		obj.add(new BasicDBObject("configData.current", 1));
		searchQuery.put("$and", obj);

		DBObject data = collection.find(searchQuery).one();
		ObjectId id = (ObjectId) data.get("_id");
		Metadata metadataLoaded = Metadata.fromJson(JSON.serialize(data));
		metadataLoaded.setId(id.toString());
		return metadataLoaded;
	}

	public Metadata readCurrentMetadataByTntAndDSCode(String metadataCode, Integer dataSetVersion, String tenantCode) {
		BasicDBObject searchQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("datasetCode", metadataCode));
		obj.add(new BasicDBObject("configData.tenantCode", tenantCode));
		obj.add(new BasicDBObject("datasetVersion", dataSetVersion));
		searchQuery.put("$and", obj);

		DBObject data = collection.find(searchQuery).one();
		ObjectId id = (ObjectId) data.get("_id");
		Metadata metadataLoaded = Metadata.fromJson(JSON.serialize(data));
		metadataLoaded.setId(id.toString());
		return metadataLoaded;
	}

	public Metadata getCurrentMetadaByBinaryID(Long binaryIdDataset) {
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("idDataset", binaryIdDataset);
		searchQuery.put("configData.current", 1);
		
		System.out.println("searchQuery in getCurrentMetadaByBinaryID = " + searchQuery.toString());

		DBObject data = collection.find(searchQuery).one();
		ObjectId id = (ObjectId) data.get("_id");
		Metadata metadataLoaded = Metadata.fromJson(JSON.serialize(data));
		metadataLoaded.setId(id.toString());
		return metadataLoaded;
	}

	public Metadata readCurrentMetadataByTntAndIDDS(Long idDataset, Integer datasetVersion, String tenantCode) {
		BasicDBObject searchQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("idDataset", idDataset));
		obj.add(new BasicDBObject("configData.tenantCode", tenantCode));
		obj.add(new BasicDBObject("datasetVersion", datasetVersion));
		searchQuery.put("$and", obj);

		DBObject data = collection.find(searchQuery).one();
		if (data!=null)
		{
		ObjectId id = (ObjectId) data.get("_id");
			Metadata metadataLoaded = Metadata.fromJson(JSON.serialize(data));
			metadataLoaded.setId(id.toString());
			return metadataLoaded;
		}
		else 
			return null;
	}

	public List<Metadata> readCurrentMetadataByTntAndIDDS(Long idDataset, String tenantCode) {
		BasicDBObject searchQuery = new BasicDBObject();
		List<Metadata> data = new ArrayList<Metadata>();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("idDataset", idDataset));
		obj.add(new BasicDBObject("configData.tenantCode", tenantCode));
		searchQuery.put("$and", obj);

		DBCursor cursor = collection.find(searchQuery);
		while (cursor.hasNext()) {
			DBObject doc = cursor.next();
			ObjectId id = (ObjectId) doc.get("_id");
			Metadata metadata = Metadata.fromJson(JSON.serialize(doc));
			metadata.setId(id.toString());
			data.add(metadata);
		}
		return data;
	}

	public Metadata getCurrentMetadaByDataSetCode(String dataSetCode, Integer dataSetVersion, String tenantCode) {
		BasicDBObject searchQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("datasetCode", dataSetCode));
		obj.add(new BasicDBObject("configData.tenantCode", tenantCode));
		obj.add(new BasicDBObject("datasetVersion", dataSetVersion));
		obj.add(new BasicDBObject("configData.subtype", "binaryDataset"));
		searchQuery.put("$and", obj);

		DBObject data = collection.find(searchQuery).one();
		ObjectId id = (ObjectId) data.get("_id");
		Metadata metadataLoaded = Metadata.fromJson(JSON.serialize(data));
		metadataLoaded.setId(id.toString());
		return metadataLoaded;
	}
	*/

}
