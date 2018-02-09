package org.csi.yucca.dataservice.binaryapi.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SDPInsertApiConfig {
	
	public static final int MAX_DOCUMENTS_IN_REQUEST=10000;
	
	public static final String MONGO_DB_CFG_TENANT="MONGO_DB_CFG_TENANT";
	public static final String MONGO_DB_CFG_METADATA="MONGO_DB_CFG_METADATA";
	public static final String MONGO_DB_CFG_STREAM="MONGO_DB_CFG_STREAM";
	public static final String MONGO_DB_CFG_APPOGGIO="MONGO_DB_CFG_APPOGGIO";
	public static final String MONGO_DB_CFG_STATUS="MONGO_DB_CFG_STATUS";
	public static final String MONGO_DB_DEFAULT="MONGO_DB_DEFAULT";

	
	public static final String PHOENIX_URL="PHOENIX_URL";
	public static final String SOLR_URL="SOLR_URL";
	

	public static SDPInsertApiConfig instance=null;
	private static int anno_init = 0;
	private static int mese_init = 0;
	private static int giorno_init = 0;

	private HashMap<String, String> params = new HashMap<String, String>();



	private static boolean singletonToRefresh() {
		int curAnno = Calendar.getInstance().get(Calendar.YEAR);
		int curMese = Calendar.getInstance().get(Calendar.MONTH);
		int curGiorno = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if (curAnno > anno_init) return true;
		else if (curMese > mese_init) return true;
		else if (curGiorno > giorno_init)return true;
		return false;
	}
	public synchronized static SDPInsertApiConfig getInstance() {
		if(instance == null || singletonToRefresh()) {
			instance = new SDPInsertApiConfig();
			anno_init = Calendar.getInstance().get(Calendar.YEAR);
			mese_init = Calendar.getInstance().get(Calendar.MONTH);
			giorno_init = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		}
		return instance;
	}

	private SDPInsertApiConfig() {
		ResourceBundle rb= ResourceBundle.getBundle("InsertdataApiConfig");
		params = new HashMap<String, String>();
		//params.put("SDP_WEB_FILTER_PATTERN", rb.getString("SDP_WEB_FILTER_PATTERN"));
		//params.put("SDP_WEB_SERVLET_URL", rb.getString("SDP_WEB_SERVLET_URL"));
		//params.put("SDP_WEB_BASE_URL", rb.getString("SDP_WEB_BASE_URL"));

		params.put("SDP_MONGO_CFG_DATASET_HOST", rb.getString("SDP_MONGO_CFG_DATASET_HOST"));
		params.put("SDP_MONGO_CFG_DATASET_PORT", rb.getString("SDP_MONGO_CFG_DATASET_PORT"));
		params.put("SDP_MONGO_CFG_DATASET_DB", rb.getString("SDP_MONGO_CFG_DATASET_DB"));
		params.put("SDP_MONGO_CFG_DATASET_METADATA_COLLECTION", rb.getString("SDP_MONGO_CFG_DATASET_METADATA_COLLECTION"));
		params.put("SDP_MONGO_CFG_DATASET_STREAM_COLLECTION", rb.getString("SDP_MONGO_CFG_DATASET_STREAM_COLLECTION"));
		params.put("SDP_MONGO_CFG_DATASET_TENANT_COLLECTION", rb.getString("SDP_MONGO_CFG_DATASET_TENANT_COLLECTION"));


		params.put("SDP_MONGO_CFG_APPOGGIO_HOST", rb.getString("SDP_MONGO_CFG_APPOGGIO_HOST"));
		params.put("SDP_MONGO_CFG_APPOGGIO_PORT", rb.getString("SDP_MONGO_CFG_APPOGGIO_PORT"));
		params.put("SDP_MONGO_CFG_APPOGGIO_DB", rb.getString("SDP_MONGO_CFG_APPOGGIO_DB"));
		params.put("SDP_MONGO_CFG_APPOGGIO_DATA_COLLECTION", rb.getString("SDP_MONGO_CFG_APPOGGIO_DATA_COLLECTION"));
		params.put("SDP_MONGO_CFG_APPOGGIO_STATUS_COLLECTION", rb.getString("SDP_MONGO_CFG_APPOGGIO_STATUS_COLLECTION"));

		params.put("SDP_MONGO_CFG_DEFAULT_USER", rb.getString("SDP_MONGO_CFG_DEFAULT_USER"));
		params.put("SDP_MONGO_CFG_DEFAULT_PWD", rb.getString("SDP_MONGO_CFG_DEFAULT_PWD"));
		params.put("SDP_MONGO_CFG_DEFAULT_HOST", rb.getString("SDP_MONGO_CFG_DEFAULT_HOST"));
		params.put("SDP_MONGO_CFG_DEFAULT_PORT", rb.getString("SDP_MONGO_CFG_DEFAULT_PORT"));


		params.put(PHOENIX_URL, rb.getString(PHOENIX_URL));
		params.put(SOLR_URL, rb.getString(SOLR_URL));

	}
//MONGO_DB_DEFAULT
	public String getMongoCfgHost(String cfgType) {
		if (MONGO_DB_CFG_TENANT.equals(cfgType)) {
			return params.get("SDP_MONGO_CFG_DATASET_HOST");
		} else if (MONGO_DB_CFG_METADATA.equals(cfgType)) {
			return params.get("SDP_MONGO_CFG_DATASET_HOST");
		} else if (MONGO_DB_CFG_APPOGGIO.equals(cfgType)) {
			return params.get("SDP_MONGO_CFG_APPOGGIO_HOST");
		} else if (MONGO_DB_CFG_STREAM.equals(cfgType)) {
			return params.get("SDP_MONGO_CFG_DATASET_HOST");
		} else if (MONGO_DB_CFG_STATUS.equals(cfgType)) {
			return params.get("SDP_MONGO_CFG_APPOGGIO_HOST");
		} else if (MONGO_DB_DEFAULT.equals(cfgType)) {
			return params.get("SDP_MONGO_CFG_DEFAULT_HOST");
		} else return null;
	}
	public String getMongoCfgDB(String cfgType) {
		if (MONGO_DB_CFG_TENANT.equals(cfgType)) {
			return params.get("SDP_MONGO_CFG_DATASET_DB");
		} else if (MONGO_DB_CFG_METADATA.equals(cfgType)) {
			return params.get("SDP_MONGO_CFG_DATASET_DB");
		} else if (MONGO_DB_CFG_APPOGGIO.equals(cfgType)) {
			return params.get("SDP_MONGO_CFG_APPOGGIO_DB");
		} else if (MONGO_DB_CFG_STREAM.equals(cfgType)) {
			return params.get("SDP_MONGO_CFG_DATASET_DB");
		} else if (MONGO_DB_CFG_STATUS.equals(cfgType)) {
			return params.get("SDP_MONGO_CFG_APPOGGIO_DB");
		} else return null;
	}
	public String getMongoCfgCollection(String cfgType) {
		if (MONGO_DB_CFG_TENANT.equals(cfgType)) {
			return params.get("SDP_MONGO_CFG_DATASET_TENANT_COLLECTION");
		} else if (MONGO_DB_CFG_METADATA.equals(cfgType)) {
				return params.get("SDP_MONGO_CFG_DATASET_METADATA_COLLECTION");
		} else if (MONGO_DB_CFG_APPOGGIO.equals(cfgType)) {
			return params.get("SDP_MONGO_CFG_APPOGGIO_DATA_COLLECTION");
		} else if (MONGO_DB_CFG_STREAM.equals(cfgType)) {
			return params.get("SDP_MONGO_CFG_DATASET_STREAM_COLLECTION");
		} else if (MONGO_DB_CFG_STATUS.equals(cfgType)) {
			return params.get("SDP_MONGO_CFG_APPOGGIO_STATUS_COLLECTION");
		} else return null;
	}
	public int getMongoCfgPort(String cfgType) {
		if (MONGO_DB_CFG_TENANT.equals(cfgType)) {
			return Integer.parseInt(params.get("SDP_MONGO_CFG_DATASET_PORT"));
		} else if (MONGO_DB_CFG_METADATA.equals(cfgType)) {
				return Integer.parseInt(params.get("SDP_MONGO_CFG_DATASET_PORT"));
		} else if (MONGO_DB_CFG_APPOGGIO.equals(cfgType)) {
			return Integer.parseInt(params.get("SDP_MONGO_CFG_APPOGGIO_PORT"));
		} else if (MONGO_DB_CFG_STREAM.equals(cfgType)) {
			return Integer.parseInt(params.get("SDP_MONGO_CFG_DATASET_PORT"));
		} else if (MONGO_DB_CFG_STATUS.equals(cfgType)) {
			return Integer.parseInt(params.get("SDP_MONGO_CFG_APPOGGIO_PORT"));
		} else if (MONGO_DB_DEFAULT.equals(cfgType)) {
			return Integer.parseInt(params.get("SDP_MONGO_CFG_DEFAULT_PORT"));
			
		} else return -1;
	}	

	public String getSolrUrl() {
		return params.get(SOLR_URL);
	}

	public String getPhoenixUrl() {
		return params.get(PHOENIX_URL);
	}
	
	public String getMongoDefaultUser() {
		return params.get("SDP_MONGO_CFG_DEFAULT_USER");
	}
	public String getMongoDefaultPassword() {
		return params.get("SDP_MONGO_CFG_DEFAULT_PWD");
	}


}
