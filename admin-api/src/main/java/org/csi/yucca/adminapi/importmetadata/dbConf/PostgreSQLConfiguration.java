package org.csi.yucca.adminapi.importmetadata.dbConf;

import org.csi.yucca.adminapi.importmetadata.DatabaseConfiguration;
import org.csi.yucca.adminapi.util.Constants;


public class PostgreSQLConfiguration extends DatabaseConfiguration {

	@Override
	protected void initTypesMap() {
		typesMap.put("bigint", Constants.ADMINAPI_DATA_TYPE_LONG);
		typesMap.put("int8", Constants.ADMINAPI_DATA_TYPE_LONG);
		typesMap.put("bigserial", Constants.ADMINAPI_DATA_TYPE_LONG);
		typesMap.put("serial8", Constants.ADMINAPI_DATA_TYPE_LONG);
		typesMap.put("bit", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("bit varying", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("varbit", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("boolean",  Constants.ADMINAPI_DATA_TYPE_BOOLEAN);
		typesMap.put("bool",  Constants.ADMINAPI_DATA_TYPE_BOOLEAN);
		typesMap.put("box", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("bytea", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("character", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("char", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("character varying", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("varchar", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("cidr", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("circle", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("date", Constants.ADMINAPI_DATA_TYPE_DATETIME);
		typesMap.put("double precision", Constants.ADMINAPI_DATA_TYPE_DOUBLE);
		typesMap.put("float8", Constants.ADMINAPI_DATA_TYPE_DOUBLE);
		typesMap.put("inet", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("int4", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("int", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("integer", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("interval", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("json", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("line", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("lseg", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("macaddr", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("money", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("numeric", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("decimal", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("path", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("point", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("polygon", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("real", Constants.ADMINAPI_DATA_TYPE_FLOAT);
		typesMap.put("float4", Constants.ADMINAPI_DATA_TYPE_FLOAT);
		typesMap.put("smallint", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("int2", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("smallserial", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("serial2", Constants.ADMINAPI_DATA_TYPE_INT);
		typesMap.put("serial", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("serial4", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("text", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("time", Constants.ADMINAPI_DATA_TYPE_DATETIME);
		typesMap.put("timetz", Constants.ADMINAPI_DATA_TYPE_DATETIME);
		typesMap.put("timestamp", Constants.ADMINAPI_DATA_TYPE_LONG);
		typesMap.put("timestamptz", Constants.ADMINAPI_DATA_TYPE_LONG);
		typesMap.put("tsquery", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("tsvector", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("txid_snapshot", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("uuid", Constants.ADMINAPI_DATA_TYPE_STRING);
		typesMap.put("xml", Constants.ADMINAPI_DATA_TYPE_STRING);
		
		hiveTypesMap.put("BIGINT",                  "BIGINT");
		hiveTypesMap.put("BIGSERIAL",               "BIGINT");
		hiveTypesMap.put("BIT",                     "BINARY");
		hiveTypesMap.put("BIT VARYING",             "BINARY");
		hiveTypesMap.put("BYTEA",                   "BINARY");
		hiveTypesMap.put("BOOLEAN",                 "BOOLEAN");
		hiveTypesMap.put("CHARACTER",               "CHAR");
		hiveTypesMap.put("CHAR",                    "CHAR");
		hiveTypesMap.put("DATE",                    "DATE");
		hiveTypesMap.put("DOUBLE PRECISION",        "DOUBLE");
		hiveTypesMap.put("REAL",                    "FLOAT");
		hiveTypesMap.put("INTEGER",                 "INT");
		hiveTypesMap.put("SERIAL",                  "INT");
		hiveTypesMap.put("NUMERIC",                 "DECIMAL");
		hiveTypesMap.put("SMALLINT",                "SMALLINT");
		hiveTypesMap.put("CHARACTER VARYING",       "CHAR");
		hiveTypesMap.put("TEXT",                    "STRING");
		hiveTypesMap.put("XML",                     "STRING");
		hiveTypesMap.put("TIMESTAMP",               "TIMESTAMP");
		hiveTypesMap.put("TIME WITH TIMEZONE",      "TIMESTAMP");
		hiveTypesMap.put("TIMESTAMP WITH TIMEZONE", "TIMESTAMP");
		hiveTypesMap.put("TIME",                    "TIMESTAMP");
		hiveTypesMap.put("VARCHAR",                 "VARCHAR");
	}

	@Override
	protected String getConnectionUrl(String hostname, String dbname) {
		return "jdbc:postgresql://" + hostname + "/" + dbname; // jdbc:postgresql://hostname:port/dbname
	}

	@Override
	protected void initDbDriver() {
		dbDriver = "org.postgresql.Driver";
	}

}
