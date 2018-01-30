package org.csi.yucca.adminapi.importmetadata.dbConf;

import org.csi.yucca.adminapi.importmetadata.DatabaseConfiguration;
import org.csi.yucca.adminapi.util.Constants;


public class HiveConfiguration extends DatabaseConfiguration {

	


	@Override
	protected void initTypesMap() {
		typesMap.put("TINYINT", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("SMALLINT", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("INT", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("INTEGER", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("BIGINT", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("FLOAT", Constants.ADMINAPI_DATA_TYPE_FLOAT);
		typesMap.put("DOUBLE", Constants.ADMINAPI_DATA_TYPE_DOUBLE);
		typesMap.put("DOUBLE PRECISION", Constants.ADMINAPI_DATA_TYPE_DOUBLE);
		typesMap.put("DECIMAL", Constants.ADMINAPI_DATA_TYPE_DOUBLE);
		typesMap.put("NUMERIC", Constants.ADMINAPI_DATA_TYPE_DOUBLE);
		typesMap.put("TIMESTAMP", Constants.ADMINAPI_DATA_TYPE_DATETIME);
		typesMap.put("DATE", Constants.ADMINAPI_DATA_TYPE_DATETIME);
		typesMap.put("INTERVAL", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("STRING", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("VARCHAR", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("CHAR", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("BOOLEAN", Constants.ADMINAPI_DATA_TYPE_BOOLEAN);
		
		
	}

	@Override
	protected String getConnectionUrl(String hostname, String dbname) {
		return hostname;
	}

	@Override
	protected void initDbDriver() {
		//dbDriver = "org.apache.hadoop.hive.jdbc.HiveDriver";
		dbDriver = "org.apache.hive.jdbc.HiveDriver";
	}

}
