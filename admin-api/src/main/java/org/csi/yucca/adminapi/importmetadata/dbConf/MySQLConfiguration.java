package org.csi.yucca.adminapi.importmetadata.dbConf;

import org.csi.yucca.adminapi.importmetadata.DatabaseConfiguration;
import org.csi.yucca.adminapi.util.Constants;

public class MySQLConfiguration extends DatabaseConfiguration {

	@Override
	protected void initTypesMap() {
		
		hiveTypesMap.put("INT", "INT");
		hiveTypesMap.put("BIGINT", "BIGINT");
		hiveTypesMap.put("BIGINT UNSIGNED", "BIGINT");
		hiveTypesMap.put("VARBINARY", "BINARY");
		hiveTypesMap.put("BINARY", "BINARY");
		hiveTypesMap.put("BIT", "BINARY");
		
		hiveTypesMap.put("BLOB", "BINARY");
		hiveTypesMap.put("LONGBLOB", "BINARY");
		hiveTypesMap.put("MEDIUMBLOB", "BINARY");
		hiveTypesMap.put("TINYBLOB", "BINARY");
		hiveTypesMap.put("CHAR", "CHAR");
		hiveTypesMap.put("DATE", "DATE");
		hiveTypesMap.put("DECIMAL", "DECIMAL");
		hiveTypesMap.put("DECIMAL UNSIGNED", "DECIMAL");
		
		hiveTypesMap.put("DOUBLE", "DOUBLE"); 
		hiveTypesMap.put("DOUBLE UNSIGNED", "DOUBLE");

		hiveTypesMap.put("FLOAT", "FLOAT");
		hiveTypesMap.put("FLOAT UNSIGNED", "FLOAT");
		
		hiveTypesMap.put("INTEGER", "INT");
		hiveTypesMap.put("INTEGER UNSIGNED", "INT");
		hiveTypesMap.put("MEDIUMINT", "INT");
		hiveTypesMap.put("MEDIUMINT UNSIGNED", "INT");

		
		hiveTypesMap.put("SMALLINT", "SMALLINT");
		hiveTypesMap.put("SMALLINT UNSIGNED", "SMALLINT");
		hiveTypesMap.put("YEAR", "SMALLINT");

		
		hiveTypesMap.put("VARCHAR", "STRING");
		hiveTypesMap.put("LONGTEXT", "STRING");
		hiveTypesMap.put("MEDIUMTEXT", "STRING");
		hiveTypesMap.put("TEXT", "STRING");
		hiveTypesMap.put("TINYTEXT", "STRING");

		hiveTypesMap.put("TIMESTAMP", "TIMESTAMP");
		hiveTypesMap.put("DATETIME", "TIMESTAMP");
		hiveTypesMap.put("TIME", "TIMESTAMP");
		
		hiveTypesMap.put("TINYINT", "TINYINT");
		hiveTypesMap.put("TINYINT UNSIGNED", "TINYINT");
		// --------------------------------------
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
