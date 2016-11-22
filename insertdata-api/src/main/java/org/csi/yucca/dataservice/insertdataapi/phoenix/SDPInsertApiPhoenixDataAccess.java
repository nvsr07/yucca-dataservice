package org.csi.yucca.dataservice.insertdataapi.phoenix;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minidev.json.JSONObject;

import org.apache.calcite.avatica.remote.Service.ConnectionSyncRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiBaseException;
import org.csi.yucca.dataservice.insertdataapi.model.output.CollectionConfDto;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetBulkInsert;
import org.csi.yucca.dataservice.insertdataapi.model.output.FieldsMongoDto;
import org.csi.yucca.dataservice.insertdataapi.mongo.SDPInsertApiMongoConnectionSingleton;
import org.csi.yucca.dataservice.insertdataapi.util.DateUtil;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

import com.mongodb.BulkWriteResult;
import com.mongodb.DBObject;

public class SDPInsertApiPhoenixDataAccess {

	private static final Log log=LogFactory.getLog("org.csi.yucca.datainsert");

	 Connection conn = null;
     Statement stmt = null;
	
	private static String takeNvlValues(Object obj) {
		if (null==obj) return null;
		else return obj.toString();
	}

	public SDPInsertApiPhoenixDataAccess() throws ClassNotFoundException {
		Class.forName("org.apache.phoenix.queryserver.client.Driver");
	}




	public int insertBulk(String tenant, DatasetBulkInsert dati) throws Exception {
		String riga=null;
		DBObject dbObject = null;
		BulkWriteResult result=null;
		CollectionConfDto conf = SDPInsertApiMongoConnectionSingleton.getInstance().getDataDbConfiguration(tenant);
		try {
			//System.out.println("###########################################");
			conn = DriverManager.getConnection(SDPInsertApiConfig.getInstance().getPhoenixUrl());
			try {conn.commit();} catch (Exception e) {log.warn("[SDPInsertApiPhoenixDataAccess:insertBulk] Invalid Connection..... Exception catched");}
					
			  
			conn.setAutoCommit(false);

			//String schema = "DB_"+tenant.toUpperCase();
			
			String schema = "";
			String table = "";
			
			String campiSQL="iddataset_l, datasetversion_, id ";
			String valuesSql= "(?, ?, ?  "; 
			if (dati.getDatasetType().equals("streamDataset"))
			{
				schema = conf.getMeasuresPhoenixSchemaName();
				if (schema == null)
					schema = "db_"+tenant;
				table = conf.getMeasuresPhoenixTableName();
				if (table == null)
					table = "measures";
				campiSQL= campiSQL + ", time_dt, sensor_s, streamcode_s ";
				valuesSql= valuesSql + ",? ,? ,? ";  
				
			}
			else if (dati.getDatasetType().equals("socialDataset"))
			{
				schema = conf.getSocialPhoenixSchemaName();
				if (schema == null)
					schema = "db_"+tenant;
				table = conf.getSocialPhoenixTableName();
				if (table == null)
					table = "social";
				campiSQL= campiSQL + ", time_dt, sensor_s, streamcode_s ";
				valuesSql= valuesSql + ",? ,? ,? ";  
				
			}
			else if (dati.getDatasetType().equals("binaryDataset"))
			{
				schema = conf.getMediaPhoenixSchemaName();
				if (schema == null)
					schema = "db_"+tenant;
				table = conf.getMediaPhoenixTableName();
				if (table == null)
					table = "media";
			}
			else {
				schema = conf.getDataPhoenixSchemaName();
				if (schema == null)
					schema = "db_"+tenant;
				table = conf.getDataPhoenixTableName();
				if (table == null)
					table = "data";
			}
			
	        Iterator<Entry<String, FieldsMongoDto>> fieldIter = dati.getFieldsType().entrySet().iterator();
			
	        while(fieldIter.hasNext())
	        {
	        	Entry<String, FieldsMongoDto> field = fieldIter.next();
	        	String nome=field.getKey();
	            String tipo=(field.getValue()).getFieldType();
	            
	            if ("int".equalsIgnoreCase(tipo)) {
		            campiSQL+=","+nome;
	                campiSQL+="_i INTEGER";
	            } else if ("long".equalsIgnoreCase(tipo)) {
		            campiSQL+=","+nome;
	                campiSQL+="_l BIGINT";
	            } else if ("double".equalsIgnoreCase(tipo)) {
		            campiSQL+=","+nome;
	                campiSQL+="_d DOUBLE";
	            } else if ("float".equalsIgnoreCase(tipo)) {
		            campiSQL+=","+nome;
	                campiSQL+="_f FLOAT";
	            } else if ("string".equalsIgnoreCase(tipo)) {
		            campiSQL+=","+nome;
	                campiSQL+="_s VARCHAR";
	            } else if ("boolean".equalsIgnoreCase(tipo)) {
		            campiSQL+=","+nome;
	                campiSQL+="_b TINYINT";
	            } else if ("datetime".equalsIgnoreCase(tipo)) {
		            campiSQL+=","+nome;
	                campiSQL+="_dt TIMESTAMP";
	            } else if ("date".equalsIgnoreCase(tipo)) {
		            campiSQL+=","+nome;
	                campiSQL+="_dt TIMESTAMP";
	            } else if ("longitude".equalsIgnoreCase(tipo)) {
		            campiSQL+=","+nome;
	                campiSQL+="_d DOUBLE";
	            } else if ("latitude".equalsIgnoreCase(tipo)) {
		            campiSQL+=","+nome;
	                campiSQL+="_d DOUBLE";
	            } else if ("binary".equalsIgnoreCase(tipo)) {
		            campiSQL+=","+nome;
	                campiSQL+="_s VARCHAR";
	            }
	            
	            
	            valuesSql+=",?";
	        }
	       
	        
	        String sql = "UPSERT INTO "+schema+"."+table+" ("+campiSQL+")  VALUES  "+valuesSql+") ";
			
	        PreparedStatement stmt=conn.prepareStatement(sql);
	        Iterator<JSONObject> iter =  dati.getJsonRowsToInsert().iterator() ;
	        while(iter.hasNext()) {
	            JSONObject json = iter.next();
	            
	            stmt.setInt(1,  (Integer.parseInt(Long.toString(dati.getIdDataset()))));
	            stmt.setInt(2,  (Integer.parseInt(Long.toString(dati.getDatasetVersion()))));
	            stmt.setString(3, json.get("objectid").toString());
	            
	            int pos=4;
	            if (!dati.getDatasetType().equals("bulkDataset")&& !dati.getDatasetType().equals("binaryDataset"))
				{
	            	stmt.setTimestamp(4, new Timestamp(DateUtil.multiParseDate(json.get("time").toString()).getTime()));
	            	stmt.setString(5, dati.getSensor());
	            	stmt.setString(6, dati.getStream());
	            	pos=7;
				}
	            fieldIter  = dati.getFieldsType().entrySet().iterator();
	            while(fieldIter.hasNext())
		        {
		        	Entry<String, FieldsMongoDto> field = fieldIter.next();
		        	String nome=field.getKey();
		            String tipo=(field.getValue()).getFieldType();
		            campiSQL+=","+nome;
		            
		            Object value = json.get(nome);
		            
	                if ("int".equalsIgnoreCase(tipo)) {
	                    if ( null== value) stmt.setNull(pos,java.sql.Types.INTEGER);
	                    else stmt.setInt(pos, Integer.parseInt(value.toString()));
	                } else if ("long".equalsIgnoreCase(tipo)) {
	                    if ( null== value) stmt.setNull(pos,java.sql.Types.BIGINT);
	                    else stmt.setLong(pos, Long.parseLong(value.toString()));
	                } else if ("double".equalsIgnoreCase(tipo)) {
	                	if ( null== value) stmt.setNull(pos,java.sql.Types.DOUBLE);
	                    else stmt.setDouble(pos, Double.parseDouble(value.toString()));
	                } else if ("float".equalsIgnoreCase(tipo)) {
	                	if ( null== value) stmt.setNull(pos,java.sql.Types.FLOAT);
	                    else stmt.setFloat(pos, (Float.parseFloat(value.toString())));
	                } else if ("string".equalsIgnoreCase(tipo)) {
	                	if ( null== value) stmt.setNull(pos,java.sql.Types.VARCHAR);
	                	else stmt.setString(pos, value.toString());
	                } else if ("binary".equalsIgnoreCase(tipo)) {
	                	if ( null== value) stmt.setNull(pos,java.sql.Types.VARCHAR);
	                	else stmt.setString(pos, value.toString());
	                } else if ("boolean".equalsIgnoreCase(tipo)) {
	                	if ( null== value) stmt.setNull(pos,java.sql.Types.TINYINT);
	                    else stmt.setInt(pos, Boolean.parseBoolean(value.toString())?1:0);
	                } else if ("datetime".equalsIgnoreCase(tipo)) {
	                	if ( null== value) stmt.setNull(pos,java.sql.Types.TIMESTAMP);
	                    stmt.setTimestamp(pos,new Timestamp(DateUtil.multiParseDate(value.toString()).getTime()));
	                } else if ("date".equalsIgnoreCase(tipo)) {
	                	if ( null== value) stmt.setNull(pos,java.sql.Types.TIMESTAMP);
	                    stmt.setTimestamp(pos,new Timestamp(DateUtil.multiParseDate(value.toString()).getTime()));
	                } else if ("longitude".equalsIgnoreCase(tipo)) {
	                	if ( null== value) stmt.setNull(pos,java.sql.Types.DOUBLE);
	                	else stmt.setDouble(pos, Double.parseDouble(value.toString()));
	                } else if ("latitude".equalsIgnoreCase(tipo)) {
	                	if ( null== value) stmt.setNull(pos,java.sql.Types.DOUBLE);
	                	else stmt.setDouble(pos, Double.parseDouble(value.toString()));
	                } 
	                
	                pos++;
		        }
	            
	            
	            
	          
	            stmt.addBatch();
//	            //stmt.executeUpdate();
//	            
//	            recIns++;
//	            if (recIns % BATCH_SIZE == 0) {
//	                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date())   + "--- BEGIN ciclo " +ciclo );
//	               
//	                if (recIns % COMMIT_SIZE == 0)
//	                {
//	                	conn.commit();
//	                    stmt.clearBatch();
//	                }
//	                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date())   + "--- END ciclo " +ciclo );
//	                ciclo++;
//	            }
//	            
//	            stmt.executeBatch();
//	            
	            
	        }
	        try {
	            stmt.executeBatch();
	            conn.commit();            
	        } catch (Exception e) {	
	        	log.error("Insert Phoenix Error", e);
	        	conn.rollback();
	        	throw new Exception(e);
	        } finally {
	        	 stmt.close();
	        	 conn.close();
	        }
	       
	    
	        
	
		} catch (Exception e ) {
			e.printStackTrace();
			throw e;
		}
		return result==null? -1 : result.getInsertedCount();

	}



}
