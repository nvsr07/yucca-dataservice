package org.csi.yucca.adminapi.importmetadata.dbConf;

import org.csi.yucca.adminapi.importmetadata.DatabaseConfiguration;
import org.csi.yucca.adminapi.util.Constants;

public class MySQLConfiguration extends DatabaseConfiguration {

	@Override
	protected void initTypesMap() {
		typesMap.put("INT", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("TINYINT", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("SMALLINT", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("MEDIUMINT", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("BIGINT", Constants.ADMINAPI_DATA_TYPE_LONG);
		typesMap.put("FLOAT", Constants.ADMINAPI_DATA_TYPE_FLOAT);
		typesMap.put("DOUBLE", Constants.ADMINAPI_DATA_TYPE_DOUBLE);
		typesMap.put("DECIMAL", Constants.ADMINAPI_DATA_TYPE_DOUBLE);
		typesMap.put("DATE", Constants.ADMINAPI_DATA_TYPE_DATETIME);
		typesMap.put("DATETIME", Constants.ADMINAPI_DATA_TYPE_DATETIME);
		typesMap.put("TIMESTAMP", Constants.ADMINAPI_DATA_TYPE_LONG);
		typesMap.put("TIME", Constants.ADMINAPI_DATA_TYPE_LONG);
		typesMap.put("YEAR", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("CHAR", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("VARCHAR", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("BLOB", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("TEXT", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("TINYBLOB", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("TINYTEXT", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("MEDIUMBLOB", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("MEDIUMTEXT", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("LONGBLOB", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("LONGTEXT", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("ENUM", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("BIT", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("INT UNSIGNED", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("TINYINT UNSIGNED", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("SMALLINT UNSIGNED", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("MEDIUMINT UNSIGNED", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("BIGINT UNSIGNED", Constants.ADMINAPI_DATA_TYPE_LONG);
	}

	@Override
	protected String getConnectionUrl(String hostname, String dbname) {
		return "jdbc:mysql://" + hostname + "/" + dbname; // jdbc:mysql://hostname:port/dbname
	}

	@Override
	protected void initDbDriver() {
		dbDriver = "com.mysql.jdbc.Driver";
	}

}
