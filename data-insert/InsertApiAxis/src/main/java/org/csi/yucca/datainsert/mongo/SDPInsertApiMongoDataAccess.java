package org.csi.yucca.datainsert.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.csi.yucca.datainsert.constants.SDPInsertApiConfig;
import org.csi.yucca.datainsert.dto.DatasetBulkInsert;
import org.csi.yucca.datainsert.dto.DbConfDto;
import org.csi.yucca.datainsert.dto.FieldsMongoDto;
import org.csi.yucca.datainsert.dto.MongoDatasetInfo;
import org.csi.yucca.datainsert.dto.MongoStreamInfo;
import org.csi.yucca.datainsert.util.JSONCallbackTimeZone;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

public class SDPInsertApiMongoDataAccess {
	private static final Logger log=Logger.getLogger("org.csi.yucca.datainsert");

	private static String takeNvlValues(Object obj) {
		if (null==obj) return null;
		else return obj.toString();
	}





	public boolean insertStatusRecord(String tenant, DatasetBulkInsert dati) throws Exception {

		try {
			MongoClient mongoClient =SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_STATUS);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_STATUS));
			DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_STATUS));

			BasicDBObject doc = new BasicDBObject("tenant ", tenant);
			doc.append("idRequest", dati.getRequestId());
			doc.append("datainserimento", "TODO");
			doc.append("status", dati.getStatus());
			doc.append("idDataset", dati.getIdDataset());
			doc.append("datasetVersion", dati.getDatasetVersion());
			doc.append("numDocuments", dati.getNumRowToInsFromJson());

			WriteResult res = coll.insert(doc);

			if (res.getN()<1) return false;
		} catch (Exception e ) {
			throw e;
		}
		return true;
	}


	public String insertStatusRecordArray(String tenant, HashMap<String, DatasetBulkInsert> datiToIns) throws Exception {
		String globIdRequest=null;
		try {
			MongoClient mongoClient =SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_STATUS);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_STATUS));
			DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_STATUS));

			long millis=new Date().getTime();


			globIdRequest=tenant+"_"+millis;
			List<BasicDBObject> arrDocs= new ArrayList<BasicDBObject>();
			DatasetBulkInsert dati=null;

			Iterator<String> iter= datiToIns.keySet().iterator();
			while (iter.hasNext()) {
				String key=iter.next();
				dati=datiToIns.get(key);
				BasicDBObject doc = new BasicDBObject("tenant ", tenant);
				doc.append("idRequest", dati.getRequestId());
				doc.append("datainserimento", "TODO");
				doc.append("status", dati.getStatus());
				doc.append("idDataset", dati.getIdDataset());
				doc.append("datasetVersion", dati.getDatasetVersion());
				doc.append("numDocuments", dati.getNumRowToInsFromJson());


				doc.append("stream", dati.getStream());
				doc.append("datasetCode", dati.getDatasetCode());
				doc.append("sensor", dati.getSensor());



				arrDocs.add(doc);
			}
			BasicDBObject obj=new BasicDBObject();
			obj.append("globalRequestID", globIdRequest);
			obj.append("globStatus", "start_ins");
			obj.put("richieste", arrDocs);
			WriteResult res = coll.insert(obj);
			//System.out.println(res.getN()+"---->"+res);
		} catch (Exception e ) {
			throw e;
		}
		return globIdRequest;
	}


	public int copyRecords(String tenant,String globIdRequest, DatasetBulkInsert blockInfo , DbConfDto cfgDb) throws Exception {
		DBCursor  cursor=null;
		BulkWriteResult result=null;

		try {
			MongoClient mongoClient =SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_APPOGGIO);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_APPOGGIO));


			//DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_APPOGGIO));
			DBCollection coll = db.getCollection("stage_"+globIdRequest);



			MongoClient mongoClientTarget =SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(cfgDb.getHost(),cfgDb.getPort());
			DB dbTarget = mongoClientTarget.getDB(cfgDb.getDataBase());
			DBCollection collTarget = dbTarget.getCollection(cfgDb.getCollection());
			BulkWriteOperation builder = collTarget.initializeOrderedBulkOperation();



			BasicDBObject query=new BasicDBObject("idRequest",blockInfo.getRequestId());

			cursor=coll.find(query);

			int count=0;
			while (cursor.hasNext()) {
				count++;
				DBObject oo = cursor.next();
				//TODO... verificare per le date
				builder.insert(oo);
			}
			cursor.close();

			//System.out.println("copy bulk ready ... idRequest="+blockInfo.getRequestId()+"   countExpected="+blockInfo.getNumRowToInsFromJson()+ "     documents in bulkoperation="+count);


			//TODO
			if (count!=blockInfo.getNumRowToInsFromJson()) System.out.println("TODO ... gestire conteggi sbagliati");

			result = builder.execute();


		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				cursor.close();
			} catch (Exception e ) {}

		}
		return result==null? -1 : result.getInsertedCount();
	}



	public boolean updateStatusRecordArray(String tenant,String globIdRequest, String newStatus, HashMap<String, DatasetBulkInsert> datiToIns) throws Exception {

		try {
			MongoClient mongoClient =SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_STATUS);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_STATUS));
			DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_STATUS));


			BasicDBObject obj=new BasicDBObject();
			obj.append("globalRequestID", globIdRequest);
			coll.remove(obj);



			List<BasicDBObject> arrDocs= new ArrayList<BasicDBObject>();
			DatasetBulkInsert dati=null;

			Iterator<String> iter= datiToIns.keySet().iterator();
			while (iter.hasNext()) {
				String key=iter.next();
				dati=datiToIns.get(key);
				BasicDBObject doc = new BasicDBObject("tenant ", tenant);
				doc.append("idRequest", dati.getRequestId());
				doc.append("datainserimento", "TODO");
				doc.append("status", dati.getStatus());
				doc.append("idDataset", dati.getIdDataset());
				doc.append("datasetVersion", dati.getDatasetVersion());
				doc.append("numDocuments", dati.getNumRowToInsFromJson());


				doc.append("stream", dati.getStream());
				doc.append("datasetCode", dati.getDatasetCode());
				doc.append("sensor", dati.getSensor());


				arrDocs.add(doc);
			}
			obj.append("globStatus", newStatus);
			obj.put("richieste", arrDocs);
			WriteResult res = coll.insert(obj);
			//System.out.println(res.getN()+"---->"+res);
			//if (res.getN()<1) return false;
		} catch (Exception e ) {
			throw e;
		}
		return true;
	}




	public boolean updateGlobalRequestStatus(String  globIdRequest, String stato, String statoOld) throws Exception {

		try {
			MongoClient mongoClient =SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_STATUS);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_STATUS));
			DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_STATUS));

			BasicDBObject searchQuery = new BasicDBObject().append("globalRequestID", globIdRequest).append("globStatus", statoOld);			


			BasicDBObject doc = new BasicDBObject();

			doc.append("$set",new BasicDBObject("globStatus", stato));
			WriteResult res = coll.update(searchQuery,doc);
			//System.out.println(res.getN());
			if (res.getN()<1) return false;
		} catch (Exception e ) {
			throw e;
		}
		return true;
	}	

	public ArrayList<DatasetBulkInsert> getElencoRichiesteByGlobRequestId(String globIdRequest) throws Exception {
		DBCursor  cursor=null;
		ArrayList<DatasetBulkInsert> ret = new ArrayList<DatasetBulkInsert>();
		try {
			MongoClient mongoClient =SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_STATUS);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_STATUS));
			DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_STATUS));

			BasicDBObject searchQuery = new BasicDBObject().append("globalRequestID", globIdRequest);			

			cursor = coll.find(searchQuery);

			if (!cursor.hasNext()) {
				//todo eccezione 
			}

			DBObject docOrig=cursor.next();
			BasicDBList richieste= (BasicDBList)docOrig.get("richieste"); 
			for (int i = 0 ; i< richieste.size();i++) {
				BasicDBObject curReq=(BasicDBObject)richieste.get(i);
				DatasetBulkInsert cur=new DatasetBulkInsert();
				cur.setDatasetVersion(curReq.getLong("datasetVersion"));
				cur.setIdDataset(curReq.getLong("idDataset"));
				cur.setRequestId(curReq.getString("idRequest"));
				cur.setNumRowToInsFromJson(curReq.getInt("numDocuments"));
				cur.setStatus(curReq.getString("status"));



				cur.setStream(curReq.getString("stream"));
				cur.setDatasetCode(curReq.getString("datasetCode"));
				cur.setSensor(curReq.getString("sensor"));




				ret.add(cur);
			}



		} catch (Exception e ) {
			throw e;
		} finally {
			try {
				cursor.close();
			} catch (Exception e ) {}
		}
		return ret;
	}


	public boolean updateSingleArreayRequestStatus(String  globIdRequest, String stato, String idRequest) throws Exception {
		DBCursor  cursor=null;
		try {
			MongoClient mongoClient =SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_STATUS);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_STATUS));
			DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_STATUS));

			BasicDBObject searchQuery = new BasicDBObject().append("globalRequestID", globIdRequest);			

			cursor = coll.find(searchQuery);


			if (!cursor.hasNext()) {
				//todo eccezione 
			}
			DBObject docOrig=cursor.next();

			BasicDBList richieste= (BasicDBList)docOrig.get("richieste"); 


			List<BasicDBObject> arrDocs= new ArrayList<BasicDBObject>();


			for (int i = 0 ; i< richieste.size();i++) {
				BasicDBObject curReq=(BasicDBObject)richieste.get(i);
				if (  ((String)curReq.get("idRequest")).equalsIgnoreCase(idRequest) ) {
					//curReq.append("$set",new BasicDBObject("status", stato));
					//curReq.remove("status");
					curReq.put("status",stato);

				}
				arrDocs.add(curReq);
			}


			((BasicDBObject)docOrig).remove("richieste");
			((BasicDBObject)docOrig).put("richieste",arrDocs);


			WriteResult res = coll.update(searchQuery,docOrig);
			//System.out.println(res.getN());
			if (res.getN()<1) return false;
		} catch (Exception e ) {
			throw e;
		} finally {
			try {
				cursor.close();
			} catch (Exception e ) {}
		}
		return true;
	}	


	public boolean updateStatusRecord(String tenant, DatasetBulkInsert dati) throws Exception {

		try {
			MongoClient mongoClient =SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_STATUS);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_STATUS));
			DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_STATUS));

			BasicDBObject searchQuery = new BasicDBObject().append("idRequest", dati.getRequestId());			


			BasicDBObject doc = new BasicDBObject();

			doc.append("$set",new BasicDBObject("status", dati.getStatus()));
			WriteResult res = coll.update(searchQuery,doc);
			//System.out.println(res.getN());
			if (res.getN()<1) return false;
		} catch (Exception e ) {
			throw e;
		}
		return true;
	}





	public int insertBulk(String tenant, DatasetBulkInsert dati, boolean creatIndex) throws Exception {
		String riga=null;
		DBObject dbObject = null;
		BulkWriteResult result=null;
		try {
			//System.out.println("###########################################");
			MongoClient mongoClient =SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_APPOGGIO);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_APPOGGIO));


			//DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_APPOGGIO));
			DBCollection coll = db.getCollection("stage_"+dati.getGlobalReqId());
			if (creatIndex) coll.createIndex(new BasicDBObject("idRequest", 1));


			BulkWriteOperation builder = coll.initializeOrderedBulkOperation();
			for (int i = 0; i<dati.getRowsToInsert().size(); i++) {
				riga="{idRequest:\""+dati.getRequestId()+"\" , "+dati.getRowsToInsert().get(i)+"}";
				//System.out.println(riga);
				builder.insert((DBObject) JSON.parse(riga,new JSONCallbackTimeZone()));
			}

			result = builder.execute();
		} catch (Exception e ) {
			e.printStackTrace();
			throw e;
		}
		return result==null? -1 : result.getInsertedCount();

	}


	public boolean dropCollection(String tenant,String globIdRequest) throws Exception {
		BulkWriteResult result=null;

		try {
			MongoClient mongoClient =SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_APPOGGIO);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_APPOGGIO));


			//DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_APPOGGIO));
			DBCollection coll = db.getCollection("stage_"+globIdRequest);

			coll.drop();



			//System.out.println("copy bulk ready ... idRequest="+blockInfo.getRequestId()+"   countExpected="+blockInfo.getNumRowToInsFromJson()+ "     documents in bulkoperation="+count);




		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {

		}
		return true;
	}



	public MongoStreamInfo getStreamInfoForDataset (String tenant,long idDataset,long datasetVersion) {
		MongoStreamInfo ret=null;
		DBCursor  cursor=null;
		try {
			MongoClient mongoClient =SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_STREAM);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_STREAM));
			DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_STREAM));
			BasicDBList queryTot=new BasicDBList();
			queryTot.add( new BasicDBObject("configData.tenantCode",tenant));
			queryTot.add( new BasicDBObject("configData.idDataset",new Long (idDataset)));
			queryTot.add( new BasicDBObject("configData.datasetVersion",new Long (datasetVersion)));

			BasicDBObject query = new BasicDBObject("$and", queryTot);

			cursor = coll.find(query);
			if (cursor.hasNext()) {
				DBObject obj=cursor.next();
				String streamCode=takeNvlValues(obj.get("streamCode"));


				String sensore=takeNvlValues(((DBObject)((DBObject)(DBObject)obj.get("streams")).get("stream")).get("virtualEntityCode"));

				ret=new MongoStreamInfo();
				ret.setSensorCode(sensore);
				ret.setDatasetId(idDataset);
				ret.setDatasetVersion(datasetVersion);
				ret.setStreamCode(streamCode);

			}



		} catch (Exception e) {
			//TODO
			log.log(Level.SEVERE,"[SDPInsertApiMongoDataAccess::getStreamInfoForDataset] GenericException "+e);

		} finally {
			try {
				cursor.close();
			} catch (Exception e ) {}
		}
		return ret;

	}

	public ArrayList<MongoStreamInfo> getStreamInfo (String tenant,String streamApplication, String sensor) {
		ArrayList<MongoStreamInfo> ret=null;
		DBCursor  cursor=null;
		try {
			MongoClient mongoClient =SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_STREAM);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_STREAM));
			DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_STREAM));
			BasicDBList queryTot=new BasicDBList();
			queryTot.add( new BasicDBObject("configData.tenantCode",tenant));

			///queryTot.add( new BasicDBObject("datasetVersion",new Integer(new Double(datasetToFindVersion).intValue())));
			queryTot.add( new BasicDBObject("streamCode",streamApplication));
			BasicDBObject sort = new BasicDBObject();
			sort.put("datasetVersion",1);

			BasicDBList orderby=new BasicDBList();
			orderby.add(sort);
			queryTot.add( new BasicDBObject("streams.stream.virtualEntityCode",sensor));
			BasicDBObject query = new BasicDBObject("$and", queryTot);

			cursor = coll.find(query).sort(orderby);
			while (cursor.hasNext()) {
				DBObject obj=cursor.next();
				String idDatasetStr=takeNvlValues(((DBObject)obj.get("configData")).get("idDataset"));
				String datasetVersionStr=takeNvlValues(((DBObject)obj.get("configData")).get("datasetVersion"));
				String tipo=takeNvlValues(       ((DBObject)((DBObject)obj.get("streams")).get("stream")).get("idTipoVe"));



				if (null==idDatasetStr || null==datasetVersionStr) throw new Exception ("problemi idDataset datasetVersion");
				MongoStreamInfo cur=new MongoStreamInfo();
				cur.setDatasetId(Long.parseLong(idDatasetStr));
				cur.setDatasetVersion(Long.parseLong(datasetVersionStr));

				int tipoVe=-1;
				if (null!= tipo && !"".equals(tipo.trim()) ) {
					tipoVe= Integer.parseInt(tipo);
				} 

				// da tavola sdp_d_tipove su adim api
				switch (tipoVe) {
				case 0:
					cur.setTipoStream(MongoStreamInfo.STREAM_TYPE_INTERNAL);
					break;
				case 1:
					cur.setTipoStream(MongoStreamInfo.STREAM_TYPE_SENSOR);
					break;
				case 2:
					cur.setTipoStream(MongoStreamInfo.STREAM_TYPE_APPLICATION);
					break;
				case 3:
					cur.setTipoStream(MongoStreamInfo.STREAM_TYPE_TWEET);
					break;
				default:
					cur.setTipoStream(MongoStreamInfo.STREAM_TYPE_UNDEFINED);
					break;

				}

				if (null==ret ) ret=new ArrayList<MongoStreamInfo>();
				ret.add(cur);
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, "[SDPInsertApiMongoDataAccess::getStreamInfo] GenericException "+e);
		} finally {
			try {
				cursor.close();
			} catch (Exception e ) {}
		}
		return ret;
	}



	public MongoDatasetInfo getInfoDataset(String datasetCode,long datasetVersion,String codiceTenant) throws Exception {
		DBCursor  cursor=null;
		MongoDatasetInfo ret=null;
		try {
			BasicDBList curDataset = new BasicDBList();
			curDataset.add(new BasicDBObject("datasetCode",datasetCode ));
			if (datasetVersion==-1) {
				curDataset.add(new BasicDBObject("configData.current",new Long(1) ));

			} else {
				curDataset.add(new BasicDBObject("datasetVersion",new Long(datasetVersion) ));
			}
			curDataset.add(new BasicDBObject("configData.tenantCode",codiceTenant ));



			BasicDBObject query = new BasicDBObject("$and", curDataset);	
			MongoClient mongoClient =SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_METADATA);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_METADATA));
			DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_METADATA));
			cursor = coll.find(query);			
			if (cursor.hasNext()) {
				DBObject obj=cursor.next();
				String datasetDatasetVersion=takeNvlValues(obj.get("datasetVersion"));
				String datasetDatasetId=takeNvlValues(obj.get("idDataset"));

				BasicDBObject configData=(BasicDBObject)obj.get("configData");

				String type=takeNvlValues(configData.get("type"));
				String subtype=takeNvlValues(configData.get("subtype"));
				String tenanTcode=takeNvlValues(configData.get("tenantCode"));
				ArrayList<FieldsMongoDto> campi=getCampiFromDbObject(obj);

				ret = new MongoDatasetInfo();
				ret.setCampi(campi);
				ret.setDatasetId(Long.parseLong(datasetDatasetId));
				ret.setDatasetVersion(Long.parseLong(datasetDatasetVersion));
				ret.setDatasetType(type);
				ret.setDatasetSubType(subtype);
				ret.setTenantcode(tenanTcode);

			}


		} catch (Exception e) {
			log.log(Level.SEVERE, "[SDPInsertApiMongoDataAccess::getInfoDataset]  --> " + e );
			e.printStackTrace();
		} finally {
			try {
				cursor.close();
			} catch (Exception e ) {}
		}
		return ret;

	}

	public ArrayList<FieldsMongoDto> getCampiDataSet(ArrayList<MongoStreamInfo> elencoStream, long datasetVersion) throws Exception {
		ArrayList<FieldsMongoDto> ret=null;
		DBCursor  cursor=null;
		try {

			BasicDBList arrDataset = new BasicDBList();
			for (int i=0;i< elencoStream.size();i++) {
				BasicDBList curDataset = new BasicDBList();
				curDataset.add(new BasicDBObject("idDataset",new Long(elencoStream.get(i).getDatasetId()) ));
				curDataset.add(new BasicDBObject("datasetVersion",new Long(elencoStream.get(i).getDatasetVersion()) ));
				arrDataset.add(new BasicDBObject("$and",curDataset));
			}
			BasicDBObject query = new BasicDBObject("$or", arrDataset);			

			MongoClient mongoClient =SDPInsertApiMongoConnectionSingleton.getInstance().getMongoClient(SDPInsertApiMongoConnectionSingleton.MONGO_DB_CFG_METADATA);
			DB db = mongoClient.getDB(SDPInsertApiConfig.getInstance().getMongoCfgDB(SDPInsertApiConfig.MONGO_DB_CFG_METADATA));
			DBCollection coll = db.getCollection(SDPInsertApiConfig.getInstance().getMongoCfgCollection(SDPInsertApiConfig.MONGO_DB_CFG_METADATA));

			cursor = coll.find(query);			
			boolean trovato=false;
			HashMap<String, FieldsMongoDto> campiMongo= new HashMap<String, FieldsMongoDto>();
			while (cursor.hasNext() && !trovato) {
				DBObject obj=cursor.next();
				String datasetDatasetVersion=takeNvlValues(obj.get("datasetVersion"));
				String datasetDatasetId=takeNvlValues(obj.get("idDataset"));
				String isCurrent=takeNvlValues(((DBObject)obj.get("configData")).get("current"));
				if (datasetVersion!=-1 && datasetVersion==Integer.parseInt(datasetDatasetVersion)) trovato=true;
				if (datasetVersion==-1 && Integer.parseInt(isCurrent)==1) trovato= true;
				if (trovato) {

					ret = getCampiFromDbObject(obj);

					//					Object eleCapmpi=((BasicDBObject)obj.get("info")).get("fields");
					//
					//					BasicDBList lista=null;
					//					if (eleCapmpi instanceof BasicDBList) {
					//						lista=(BasicDBList)eleCapmpi;
					//					} else {
					//						lista=new BasicDBList();
					//						lista.add(eleCapmpi);
					//					}
					//
					//					for (int i=0;i<lista.size();i++) {
					//						DBObject elemento=(DBObject)lista.get(i);
					//						Set<String> chiavi= elemento.keySet();
					//						String propName=null;
					//						String porpType=null;
					//						Iterator<String> itcomp=chiavi.iterator();
					//						while (itcomp.hasNext()) {
					//							String chiaveCur=itcomp.next();
					//							String valor=takeNvlValues(elemento.get(chiaveCur));
					//
					//							if (chiaveCur.equals("fieldName")) propName=valor;
					//							if (chiaveCur.equals("dataType")) porpType=valor;
					//
					//
					//						}
					//						campiMongo.put(propName, new FieldsMongoDto(propName, porpType));
					//						if (null==ret) ret = new ArrayList<FieldsMongoDto>(); 
					//							ret.add(new FieldsMongoDto(propName, porpType,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
					//					}
					//					ret.add(new FieldsMongoDto("c1", FieldsMongoDto.DATA_TYPE_BOOLEAN,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
					//					ret.add(new FieldsMongoDto("c2", FieldsMongoDto.DATA_TYPE_DATETIME,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
					//					ret.add(new FieldsMongoDto("c3", FieldsMongoDto.DATA_TYPE_DOUBLE,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
					//					ret.add(new FieldsMongoDto("c4", FieldsMongoDto.DATA_TYPE_FLOAT,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
					//					ret.add(new FieldsMongoDto("c5", FieldsMongoDto.DATA_TYPE_INT,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
					//					ret.add(new FieldsMongoDto("c6", FieldsMongoDto.DATA_TYPE_LAT,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
					//					ret.add(new FieldsMongoDto("c7", FieldsMongoDto.DATA_TYPE_LON,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
					//					ret.add(new FieldsMongoDto("c8", FieldsMongoDto.DATA_TYPE_LONG,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
					//					ret.add(new FieldsMongoDto("c9", FieldsMongoDto.DATA_TYPE_STRING,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));



				}				
			}


		} catch (Exception e) {
			//TODO
		} finally {
			try {
				cursor.close();
			} catch (Exception e ) {}
		}
		return ret;

	}

	private ArrayList<FieldsMongoDto> getCampiFromDbObject(DBObject obj) throws Exception {
		String datasetDatasetVersion=takeNvlValues(obj.get("datasetVersion"));
		String datasetDatasetId=takeNvlValues(obj.get("idDataset"));
		ArrayList<FieldsMongoDto> ret=null;
		Object eleCapmpi=((BasicDBObject)obj.get("info")).get("fields");

		BasicDBList lista=null;
		if (eleCapmpi instanceof BasicDBList) {
			lista=(BasicDBList)eleCapmpi;
		} else {
			lista=new BasicDBList();
			lista.add(eleCapmpi);
		}

		for (int i=0;i<lista.size();i++) {
			DBObject elemento=(DBObject)lista.get(i);
			Set<String> chiavi= elemento.keySet();
			String propName=null;
			String porpType=null;
			Iterator<String> itcomp=chiavi.iterator();
			while (itcomp.hasNext()) {
				String chiaveCur=itcomp.next();
				String valor=takeNvlValues(elemento.get(chiaveCur));

				if (chiaveCur.equals("fieldName")) propName=valor;
				if (chiaveCur.equals("dataType")) porpType=valor;


			}

			if (null==ret) ret = new ArrayList<FieldsMongoDto>(); 
			ret.add(new FieldsMongoDto(propName, porpType,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		}
		//		ret.add(new FieldsMongoDto("c1", FieldsMongoDto.DATA_TYPE_BOOLEAN,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		//		ret.add(new FieldsMongoDto("c2", FieldsMongoDto.DATA_TYPE_DATETIME,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		//		ret.add(new FieldsMongoDto("c3", FieldsMongoDto.DATA_TYPE_DOUBLE,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		//		ret.add(new FieldsMongoDto("c4", FieldsMongoDto.DATA_TYPE_FLOAT,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		//		ret.add(new FieldsMongoDto("c5", FieldsMongoDto.DATA_TYPE_INT,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		//		ret.add(new FieldsMongoDto("c6", FieldsMongoDto.DATA_TYPE_LAT,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		//		ret.add(new FieldsMongoDto("c7", FieldsMongoDto.DATA_TYPE_LON,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		//		ret.add(new FieldsMongoDto("c8", FieldsMongoDto.DATA_TYPE_LONG,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));
		//		ret.add(new FieldsMongoDto("c9", FieldsMongoDto.DATA_TYPE_STRING,Long.parseLong(datasetDatasetId),Long.parseLong(datasetDatasetVersion)));


		return ret;
	}

}
