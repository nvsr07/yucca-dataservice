package org.csi.yucca.dataservice.insertdataapi.mongo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.csi.yucca.dataservice.insertdataapi.dto.DatasetInfoKey;
import org.csi.yucca.dataservice.insertdataapi.dto.StreamInfoKey;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiBaseException;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiRuntimeException;
import org.csi.yucca.dataservice.insertdataapi.exception.TenantListAccessException;
import org.csi.yucca.dataservice.insertdataapi.metadata.SDPInsertMetadataApiAccess;
import org.csi.yucca.dataservice.insertdataapi.model.output.CollectionConfDto;
import org.csi.yucca.dataservice.insertdataapi.model.output.FieldsDto;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetInfo;
import org.csi.yucca.dataservice.insertdataapi.model.output.StreamInfo;
import org.csi.yucca.dataservice.insertdataapi.model.output.TenantInfo;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class SDPInsertApiMongoDataAccess implements SDPInsertMetadataApiAccess {
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("org.csi.yucca.datainsert");

	// private static Map<String, ArrayList<MongoStreamInfo>> streamInfoCache =
	// Collections.synchronizedMap(new PassiveExpiringMap<String,
	// ArrayList<MongoStreamInfo>>(10,
	// TimeUnit.MINUTES));

	// private static Map<String, ArrayList<FieldsMongoDto>> campiDatasetCache =
	// Collections.synchronizedMap(new PassiveExpiringMap<String,
	// ArrayList<FieldsMongoDto>>(10,
	// TimeUnit.MINUTES));

	private static LoadingCache<DatasetInfoKey, ArrayList<FieldsDto>> campiDatasetCache = CacheBuilder.newBuilder().refreshAfterWrite(5, TimeUnit.MINUTES)
			.build(new CacheLoader<DatasetInfoKey, ArrayList<FieldsDto>>() {
				@Override
				public ArrayList<FieldsDto> load(DatasetInfoKey key) throws Exception {
					log.info("--------------> load" + key);
					return getCampiDatasetFromMongo(key.getIdDataset(), key.getDatasetVersion());
				}

				@Override
				public ListenableFuture<ArrayList<FieldsDto>> reload(final DatasetInfoKey key, ArrayList<FieldsDto> oldValue) throws Exception {
					log.info("--------------> reloaded" + key);
					ListenableFutureTask<ArrayList<FieldsDto>> task = ListenableFutureTask.create(new Callable<ArrayList<FieldsDto>>() {
						public ArrayList<FieldsDto> call() {
							return getCampiDatasetFromMongo(key.getIdDataset(), key.getDatasetVersion());
						}
					});
					Executors.newSingleThreadExecutor().execute(task);
					return task;
				}
			});

	private static LoadingCache<StreamInfoKey, ArrayList<StreamInfo>> streamInfoCache = CacheBuilder.newBuilder().refreshAfterWrite(5, TimeUnit.MINUTES)
			.build(new CacheLoader<StreamInfoKey, ArrayList<StreamInfo>>() {
				@Override
				public ArrayList<StreamInfo> load(StreamInfoKey key) throws Exception {
					log.info("--------------> load" + key);
					return getStreamInfoFromMongo(key.getTenant(), key.getStream(), key.getSensor());
				}

				@Override
				public ListenableFuture<ArrayList<StreamInfo>> reload(final StreamInfoKey key, ArrayList<StreamInfo> oldValue) throws Exception {
					log.info("--------------> reloaded" + key);
					ListenableFutureTask<ArrayList<StreamInfo>> task = ListenableFutureTask.create(new Callable<ArrayList<StreamInfo>>() {
						public ArrayList<StreamInfo> call() {
							return getStreamInfoFromMongo(key.getTenant(), key.getStream(), key.getSensor());
						}
					});
					Executors.newSingleThreadExecutor().execute(task);
					return task;
				}
			});

	private static String takeNvlValues(Object obj) {
		if (null == obj)
			return null;
		else
			return obj.toString();
	}

	@Override
	public StreamInfo getStreamInfoForDataset(String tenant, long idDataset, long datasetVersion) {
		StreamInfo ret = null;
		DBCursor cursor = null;
		try {
			MongoClient mongoClient = SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_STREAM);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_STREAM));
			DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_STREAM));
			BasicDBList queryTot = new BasicDBList();
			queryTot.add(new BasicDBObject("configData.tenantCode", tenant));
			queryTot.add(new BasicDBObject("configData.idDataset", new Long(idDataset)));
			queryTot.add(new BasicDBObject("configData.datasetVersion", new Long(datasetVersion)));

			BasicDBObject query = new BasicDBObject("$and", queryTot);

			cursor = coll.find(query);
			if (cursor.hasNext()) {
				DBObject obj = cursor.next();
				String streamCode = takeNvlValues(obj.get("streamCode"));

				String sensore = takeNvlValues(((DBObject) ((DBObject) (DBObject) obj.get("streams")).get("stream")).get("virtualEntityCode"));

				String virtualEntitySlug = takeNvlValues(((DBObject) ((DBObject) (DBObject) obj.get("streams")).get("stream")).get("virtualEntitySlug"));

				ret = new StreamInfo();
				ret.setSensorCode(sensore);
				ret.setDatasetId(idDataset);
				ret.setDatasetVersion(datasetVersion);
				ret.setStreamCode(streamCode);
				ret.setVirtualEntitySlug(virtualEntitySlug);

			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new InsertApiRuntimeException(e);
		} finally {
			try {
				cursor.close();
			} catch (Exception e) {
			}
		}
		return ret;

	}

	
	public TenantInfo getTenantInfo(String codTenant) {
		TenantInfo ret = null;
		DBObject tenant = null;
		try {
			MongoClient mongoClient = SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_TENANT);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_TENANT));
			String collection = SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_TENANT);
			DBCollection coll = db.getCollection(collection);

			BasicDBObject query = new BasicDBObject("tenantCode", codTenant);
			log.info("MongoTenantInfo query " + query);

			tenant = coll.findOne(query);
			log.info("MongoTenantInfo tenant " + tenant);
			if (tenant != null)
				ret = new TenantInfo(tenant);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new InsertApiRuntimeException(e);
		}
		return ret;

	}

	@Override
	public ArrayList<StreamInfo> getStreamInfo(String tenant, String streamApplication, String sensor) {
		// String cacheKey = createStreamCacheKey(tenant, streamApplication,
		// sensor);
		ArrayList<StreamInfo> ret;
		try {
			ret = streamInfoCache.get(new StreamInfoKey(tenant, streamApplication, sensor));
		} catch (ExecutionException e) {
			throw new InsertApiRuntimeException(e);
		}
		log.debug("Size:" + (ret == null ? 0 : ret.size()));
		return ret;
	}

	private static ArrayList<StreamInfo> getStreamInfoFromMongo(String tenant, String streamApplication, String sensor) {
		log.info("getStreamInfo -> loadinf " + tenant + "||" + sensor + "||" + streamApplication);
		DBCursor cursor = null;
		ArrayList<StreamInfo> ret = new ArrayList<StreamInfo>();
		try {
			MongoClient mongoClient = SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_STREAM);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_STREAM));
			DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_STREAM));
			BasicDBList queryTot = new BasicDBList();
			queryTot.add(new BasicDBObject("configData.tenantCode", tenant));

			// /queryTot.add( new BasicDBObject("datasetVersion",new
			// Integer(new Double(datasetToFindVersion).intValue())));
			queryTot.add(new BasicDBObject("streamCode", streamApplication));
			BasicDBObject sort = new BasicDBObject();
			sort.put("datasetVersion", 1);

			BasicDBList orderby = new BasicDBList();
			orderby.add(sort);
			queryTot.add(new BasicDBObject("streams.stream.virtualEntityCode", sensor));
			BasicDBObject query = new BasicDBObject("$and", queryTot);

			log.debug("[QUERY]" + query);

			cursor = coll.find(query).sort(orderby);
			while (cursor.hasNext()) {
				DBObject obj = cursor.next();
				String idDatasetStr = takeNvlValues(((DBObject) obj.get("configData")).get("idDataset"));
				String datasetVersionStr = takeNvlValues(((DBObject) obj.get("configData")).get("datasetVersion"));
				String tipo = takeNvlValues(((DBObject) ((DBObject) obj.get("streams")).get("stream")).get("idTipoVe"));

				if (null == idDatasetStr || null == datasetVersionStr)
					throw new Exception("problemi idDataset datasetVersion");
				StreamInfo cur = new StreamInfo();
				cur.setDatasetId(Long.parseLong(idDatasetStr));
				cur.setDatasetVersion(Long.parseLong(datasetVersionStr));

				int tipoVe = -1;
				if (null != tipo && !"".equals(tipo.trim())) {
					tipoVe = Integer.parseInt(tipo);
				}

				// da tavola sdp_d_tipove su adim api
				switch (tipoVe) {
				case 0:
					cur.setTipoStream(StreamInfo.STREAM_TYPE_INTERNAL);
					break;
				case 1:
					cur.setTipoStream(StreamInfo.STREAM_TYPE_SENSOR);
					break;
				case 2:
					cur.setTipoStream(StreamInfo.STREAM_TYPE_APPLICATION);
					break;
				case 3:
					cur.setTipoStream(StreamInfo.STREAM_TYPE_TWEET);
					break;
				default:
					cur.setTipoStream(StreamInfo.STREAM_TYPE_UNDEFINED);
					break;

				}

				ret.add(cur);
			}

		} catch (Exception e) {
			log.error("Error", e);
			throw new InsertApiRuntimeException(e);
		} finally {
			try {
				cursor.close();
			} catch (Exception e) {
			}
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see org.csi.yucca.dataservice.insertdataapi.mongo.SDPInsertMetadataApiAccess#getInfoDataset(java.lang.String, long, java.lang.String)
	 */
	@Override
	public DatasetInfo getInfoDataset(String datasetCode, long datasetVersion, String codiceTenant) throws Exception {
		DatasetInfo ret = null;
		BasicDBList curDataset = new BasicDBList();
		curDataset.add(new BasicDBObject("datasetCode", datasetCode));
		if (datasetVersion == -1) {
			curDataset.add(new BasicDBObject("configData.current", new Long(1)));

		} else {
			curDataset.add(new BasicDBObject("datasetVersion", new Long(datasetVersion)));
		}
		curDataset.add(new BasicDBObject("configData.tenantCode", codiceTenant));

		ret = loadDatasetInfo(curDataset);

		return ret;

	}

	/* (non-Javadoc)
	 * @see org.csi.yucca.dataservice.insertdataapi.mongo.SDPInsertMetadataApiAccess#getInfoDataset(java.lang.Long, java.lang.Long, java.lang.String)
	 */
	@Override
	public DatasetInfo getInfoDataset(Long idDataset, Long datasetVersion, String codiceTenant) throws Exception {
		DatasetInfo ret = null;
		BasicDBList curDataset = new BasicDBList();
		curDataset.add(new BasicDBObject("idDataset", idDataset));
		if (datasetVersion == null || datasetVersion == -1) {
			curDataset.add(new BasicDBObject("configData.current", new Long(1)));

		} else {
			curDataset.add(new BasicDBObject("datasetVersion", new Long(datasetVersion)));
		}
		curDataset.add(new BasicDBObject("configData.tenantCode", codiceTenant));

		ret = loadDatasetInfo(curDataset);
		return ret;

	}

	private DatasetInfo loadDatasetInfo(BasicDBList curDataset) {
		DBCursor cursor = null;
		DatasetInfo ret = null;
		try {
			BasicDBObject query = new BasicDBObject("$and", curDataset);
			MongoClient mongoClient = SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_METADATA);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_METADATA));
			DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_METADATA));
			log.info("[MongoDataAccess::getInfoDataset]  Query" + query);
			cursor = coll.find(query);
			if (cursor.hasNext()) {
				DBObject obj = cursor.next();
				String datasetDatasetVersion = takeNvlValues(obj.get("datasetVersion"));
				String datasetDatasetId = takeNvlValues(obj.get("idDataset"));
				String datasetCode = takeNvlValues(obj.get("datasetCode"));

				BasicDBObject configData = (BasicDBObject) obj.get("configData");

				String type = takeNvlValues(configData.get("type"));
				String subtype = takeNvlValues(configData.get("subtype"));
				String tenanTcode = takeNvlValues(configData.get("tenantCode"));
				ArrayList<FieldsDto> campi = getCampiFromDbObject(obj);

				BasicDBObject info = (BasicDBObject) obj.get("info");
				String datasetDomain = takeNvlValues(info.get("dataDomain"));
				String datasetSubdomain = takeNvlValues(info.get("codSubDomain"));

				ret = new DatasetInfo();
				ret.setCampi(campi);
				ret.setDatasetId(Long.parseLong(datasetDatasetId));
				ret.setDatasetVersion(Long.parseLong(datasetDatasetVersion));
				ret.setDatasetType(type);
				ret.setDatasetSubType(subtype);
				ret.setTenantcode(tenanTcode);
				ret.setDatasetCode(datasetCode);
				ret.setDatasetDomain(datasetDomain);
				ret.setDatasetSubdomain(datasetSubdomain);

			}
		} catch (Exception e) {
			log.error("[MongoDataAccess::getInfoDataset]  Error during query", e);
		} finally {
			try {
				cursor.close();
			} catch (Exception e) {
			}
		}
		return ret;

	}

	/* (non-Javadoc)
	 * @see org.csi.yucca.dataservice.insertdataapi.mongo.SDPInsertMetadataApiAccess#getCampiDataSet(java.lang.Long, long)
	 */
	@Override
	public ArrayList<FieldsDto> getCampiDataSet(Long idDataset, long datasetVersion) throws Exception {

		ArrayList<FieldsDto> ret;
		try {
			ret = campiDatasetCache.get(new DatasetInfoKey(idDataset, datasetVersion));
		} catch (Exception e) {
			throw new InsertApiRuntimeException(e);
		}

		return ret;

	}

	private static ArrayList<FieldsDto> getCampiDatasetFromMongo(Long idDataset, long datasetVersion) {
		ArrayList<FieldsDto> ret = null;
		try {

			// DBObject query = BasicDBObjectBuilder.start().append("_id", new
			// ObjectId(metadata.getId())).get();
			// DBObject data = this.collection.findOne(query);

			BasicDBList curDataset = new BasicDBList();
			curDataset.add(new BasicDBObject("idDataset", idDataset));
			if (datasetVersion != -1)
				curDataset.add(new BasicDBObject("datasetVersion", datasetVersion));
			else
				curDataset.add(new BasicDBObject("configData.current", 1));

			BasicDBObject query = new BasicDBObject("$and", curDataset);

			MongoClient mongoClient = SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_METADATA);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_METADATA));
			DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_METADATA));

			DBObject obj = coll.findOne(query);
			if (obj==null){
				log.error("Metadata not found for "+idDataset+"/v"+datasetVersion);
				throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_DATASETVERSION_INVALID);
			}
			ret = getCampiFromDbObject(obj);
		} catch (Exception e) {
			log.error("Error idDataset:"+idDataset+"]["+"datasetVersion:"+datasetVersion, e);
			throw new InsertApiRuntimeException(e);
		}
		return ret;
	}

	private static ArrayList<FieldsDto> getCampiFromDbObject(DBObject obj) throws Exception {
		String datasetDatasetVersion = takeNvlValues(obj.get("datasetVersion"));
		String datasetDatasetId = takeNvlValues(obj.get("idDataset"));
		ArrayList<FieldsDto> ret = null;
		Object eleCapmpi = ((BasicDBObject) obj.get("info")).get("fields");

		BasicDBList lista = null;
		if (eleCapmpi instanceof BasicDBList) {
			lista = (BasicDBList) eleCapmpi;
		} else {
			lista = new BasicDBList();
			lista.add(eleCapmpi);
		}

		for (int i = 0; i < lista.size(); i++) {
			DBObject elemento = (DBObject) lista.get(i);
			Set<String> chiavi = elemento.keySet();
			String propName = null;
			String porpType = null;
			Iterator<String> itcomp = chiavi.iterator();
			while (itcomp.hasNext()) {
				String chiaveCur = itcomp.next();
				String valor = takeNvlValues(elemento.get(chiaveCur));

				if (chiaveCur.equals("fieldName"))
					propName = valor;
				if (chiaveCur.equals("dataType"))
					porpType = valor;

			}

			if (null == ret)
				ret = new ArrayList<FieldsDto>();
			ret.add(new FieldsDto(propName, porpType, Long.parseLong(datasetDatasetId), Long.parseLong(datasetDatasetVersion)));
		}
		// ret.add(new FieldsMongoDto("c1",
		// FieldsMongoDto.DATA_TYPE_BOOLEAN,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		// ret.add(new FieldsMongoDto("c2",
		// FieldsMongoDto.DATA_TYPE_DATETIME,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		// ret.add(new FieldsMongoDto("c3",
		// FieldsMongoDto.DATA_TYPE_DOUBLE,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		// ret.add(new FieldsMongoDto("c4",
		// FieldsMongoDto.DATA_TYPE_FLOAT,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		// ret.add(new FieldsMongoDto("c5",
		// FieldsMongoDto.DATA_TYPE_INT,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		// ret.add(new FieldsMongoDto("c6",
		// FieldsMongoDto.DATA_TYPE_LAT,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		// ret.add(new FieldsMongoDto("c7",
		// FieldsMongoDto.DATA_TYPE_LON,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		// ret.add(new FieldsMongoDto("c8",
		// FieldsMongoDto.DATA_TYPE_LONG,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		// ret.add(new FieldsMongoDto("c9",
		// FieldsMongoDto.DATA_TYPE_STRING,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));

		return ret;
	}

	/* (non-Javadoc)
	 * @see org.csi.yucca.dataservice.insertdataapi.mongo.SDPInsertMetadataApiAccess#getTenantList()
	 */
	@Override
	public Set<String> getTenantList() throws TenantListAccessException {
		Set<String> tenants = new HashSet<String>();

		log.info("getTenantList....");
		DBCursor cursor = null;
		try {
			MongoClient mongoClient = SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_TENANT);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_TENANT));
			String collection = SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_TENANT);
			DBCollection coll = db.getCollection(collection);

			cursor = coll.find();
			while (cursor.hasNext()) {

				DBObject obj = cursor.next();
				String tenantCode = obj.get("tenantCode").toString();

				// if almost one phoenix schema defined
				boolean oneSchema = (obj.get("socialPhoenixSchemaName") != null) || (obj.get("dataPhoenixSchemaName") != null) || (obj.get("mediaPhoenixSchemaName") != null)
						|| (obj.get("measuresPhoenixSchemaName") != null);
				if (oneSchema)
					tenants.add(tenantCode);
			}
		} catch (Exception e) {
			log.error("Error during tenant List", e);
			throw new TenantListAccessException(e);
		} finally {
			try {
				cursor.close();
			} catch (Exception ec) {
			}

		}

		return tenants;

	}

	private static String createCampiDatasetCacheKey(Long idDataset, long datasetVersion) {
		return "idDataset_" + idDataset + "_datasetVersion_" + datasetVersion;
	}

	public static void clearCache(String tenant, String streamApplication, String sensor) {
		StreamInfoKey key = new StreamInfoKey(tenant, streamApplication, sensor);
		ArrayList<StreamInfo> streamList = streamInfoCache.getIfPresent(key);
		if (streamList != null) {
			log.info("clearCache -> streamList NOT NULL elimino " + key);
			streamInfoCache.refresh(key);
			for (StreamInfo stream : streamList) {
				campiDatasetCache.refresh(new DatasetInfoKey(stream.getDatasetId(), stream.getDatasetVersion()));
			}
		} else
			log.info("clearCache -> streamList NULL non faccio nulla " + key);
	}

	public static void main(String[] args) throws InterruptedException {
		Cache<String, String> prova = CacheBuilder.newBuilder().refreshAfterWrite(10, TimeUnit.MILLISECONDS).removalListener(new RemovalListener<String, String>() {
			@Override
			public void onRemoval(RemovalNotification<String, String> arg0) {
				System.out.println("-------------->" + arg0.getKey() + "_" + arg0.getValue());
			}
		}).build(new CacheLoader<String, String>() {

			@Override
			public String load(String arg0) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ListenableFuture<String> reload(final String key, String oldValue) throws Exception {
				System.out.println("--------------> reloaded" + key);
				if (oldValue.equals("exc"))
					throw new Exception("ERRORORO");
				else {
					ListenableFutureTask<String> task = ListenableFutureTask.create(new Callable<String>() {
						public String call() {
							return "__" + key;
						}
					});
					Executors.newSingleThreadExecutor().execute(task);
					return task;

				}

			}

		}

		);

		Map pm = prova.asMap();
		System.out.println("==============");
		pm.put("pippo", "pippoV");
		System.out.println("pippo:" + pm.get("pippo"));
		System.out.println("pippo:" + pm.get("pippo"));
		pm.put("pippo2", "pippoV2");
		System.out.println("pippo:" + pm.get("pippo2"));
		pm.put("pippo3", "pippoV3");
		System.out.println("pippo:" + pm.get("pippo3"));
		pm.put("pippo", "pippoVV");
		System.out.println("pippo:" + pm.get("pippo"));
		pm.put("pippo", "exc");
		System.out.println("pippo:" + pm.get("pippo"));
		Thread.sleep(2000);
		Thread.sleep(2000);
		System.out.println("---");
		Thread.sleep(2000);
		System.out.println("pippo:" + pm.get("pippo"));
	}

	@Override
	public CollectionConfDto getCollectionInfo(String tenant,
			long idDatasetTrovato, long datasetVersionTrovato,
			String datasetType) {
		CollectionMongoConfDto conf = SDPInsertApiMongoConnectionSingleton.getInstance().getDataDbConfiguration(tenant);
		
		CollectionConfDto confInfo = new CollectionConfDto();
		String schema ;
		String table;
		String collection;
		
		if (datasetType.equals("streamDataset")) {
			schema = conf.getMeasuresPhoenixSchemaName();
			if (schema == null)
				schema = "db_" + tenant;
			table = conf.getMeasuresPhoenixTableName();
			if (table == null)
				table = "measures";
			collection = conf.getMeasuresSolrCollectionName();
			if (collection == null)
				collection = "sdp_" + tenant + "_measures";

		} else if (datasetType.equals("socialDataset")) {
			schema = conf.getSocialPhoenixSchemaName();
			if (schema == null)
				schema = "db_" + tenant;
			table = conf.getSocialPhoenixTableName();
			if (table == null)
				table = "social";
			collection = conf.getSocialSolrCollectionName();
			if (collection == null)
				collection = "sdp_" + tenant + "_social";

		} else if (datasetType.equals("binaryDataset")) {
			schema = conf.getMediaPhoenixSchemaName();
			if (schema == null)
				schema = "db_" + tenant;
			table = conf.getMediaPhoenixTableName();
			if (table == null)
				table = "media";
			collection = conf.getMediaSolrCollectionName();
			if (collection == null)
				collection = "sdp_" + tenant + "_media";
		} else {
			schema = conf.getDataPhoenixSchemaName();
			if (schema == null)
				schema = "db_" + tenant;
			table = conf.getDataPhoenixTableName();
			if (table == null)
				table = "data";
			collection = conf.getDataSolrCollectionName();
			if (collection == null)
				collection = "sdp_" + tenant + "_data";
		}
		
		confInfo.setPhoenixTableName(table);
		confInfo.setPhoenixSchemaName(schema);
		confInfo.setSolrCollectionName(collection);
		return confInfo;

	}
}
