package org.csi.yucca.dataservice.insertdataapi.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SDPInsertApiConfig {

	public static final int MAX_DOCUMENTS_IN_REQUEST = 100000;

	public static final String MONGO_DB_CFG_TENANT = "MONGO_DB_CFG_TENANT";
	public static final String MONGO_DB_CFG_METADATA = "MONGO_DB_CFG_METADATA";
	public static final String MONGO_DB_CFG_STREAM = "MONGO_DB_CFG_STREAM";
	public static final String MONGO_DB_CFG_APPOGGIO = "MONGO_DB_CFG_APPOGGIO";
	public static final String MONGO_DB_CFG_STATUS = "MONGO_DB_CFG_STATUS";
	public static final String MONGO_DB_DEFAULT = "MONGO_DB_DEFAULT";

	public static final String PHOENIX_URL = "PHOENIX_URL";
	public static final String SOLR_URL = "SOLR_URL";
	public static final String SOLR_TYPE_ACCESS = "SOLR_TYPE_ACCESS";
	public static final String SOLR_USERNAME = "SOLR_USERNAME";
	public static final String SOLR_PASSWORD = "SOLR_PASSWORD";
	public static final String SOLR_SECURITY_DOMAIN_NAME = "SOLR_SECURITY_DOMAIN_NAME";

	
	public static final String ADMIN_API_URL= "ADMIN_API_URL";
	
	public static final String SOURCE_METADATA= "SOURCE_METADATA"; // MONGO vs ADMIN_API
	public static final String SOURCE_METADATA_MONGO= "MONGO";
	public static final String SOURCE_METADATA_ADMIN_API= "ADMIN_API"; 
	
	
	public static final String JMS_MB_INTERNAL_URL = "JMS_MB_INTERNAL_URL";
	public static final String JMS_MB_INTERNAL_USERNAME = "JMS_MB_INTERNAL_USERNAME";
	public static final String JMS_MB_INTERNAL_PASSWORD = "JMS_MB_INTERNAL_PASSWORD";

	public static final String JMS_MB_EXTERNAL_URL = "JMS_MB_EXTERNAL_URL";
	public static final String JMS_MB_EXTERNAL_USERNAME = "JMS_MB_EXTERNAL_USERNAME";
	public static final String JMS_MB_EXTERNAL_PASSWORD = "JMS_MB_EXTERNAL_PASSWORD";

	public static final String SOLR_INDEXER_ENABLED = "SOLR_INDEXER_ENABLED";

	public static final String KNOX_SDNET_ULR = "KNOX_SDNET_ULR";
	public static final String KNOX_SDNET_USERNAME = "KNOX_SDNET_USERNAME";
	public static final String KNOX_SDNET_PASSWORD = "KNOX_SDNET_PASSWORD";

	public static final String MAIL_SERVER = "MAIL_SERVER";
	public static final String MAIL_TO_ADDRESS = "MAIL_TO_ADDRESS";
	public static final String MAIL_FROM_ADDRESS = "MAIL_FROM_ADDRESS";

	public static final String DELETE_MAIL_SUBJECT_404 = "DELETE_MAIL_SUBJECT_404";
	public static final String DELETE_MAIL_BODY_404 = "DELETE_MAIL_BODY_404";
	public static final String DELETE_MAIL_SUBJECT_500 = "DELETE_MAIL_SUBJECT_500";
	public static final String DELETE_MAIL_BODY_500 = "DELETE_MAIL_BODY_500";
	public static final String DELETE_MAIL_SUBJECT_200 = "DELETE_MAIL_SUBJECT_200";
	public static final String DELETE_MAIL_BODY_200 = "DELETE_MAIL_BODY_200";

	
	
	
	public static SDPInsertApiConfig instance = null;
	private static int anno_init = 0;
	private static int mese_init = 0;
	private static int giorno_init = 0;

	private HashMap<String, String> params = new HashMap<String, String>();

	private static boolean singletonToRefresh() {
		int curAnno = Calendar.getInstance().get(Calendar.YEAR);
		int curMese = Calendar.getInstance().get(Calendar.MONTH);
		int curGiorno = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if (curAnno > anno_init)
			return true;
		else if (curMese > mese_init)
			return true;
		else if (curGiorno > giorno_init)
			return true;
		return false;
	}

	public synchronized static SDPInsertApiConfig getInstance() {
		if (instance == null || singletonToRefresh()) {
			instance = new SDPInsertApiConfig();
			anno_init = Calendar.getInstance().get(Calendar.YEAR);
			mese_init = Calendar.getInstance().get(Calendar.MONTH);
			giorno_init = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		}
		return instance;
	}

	private SDPInsertApiConfig() {
		ResourceBundle rb = ResourceBundle.getBundle("InsertdataApiConfig");
		params = new HashMap<String, String>();
		// params.put("SDP_WEB_FILTER_PATTERN",
		// rb.getString("SDP_WEB_FILTER_PATTERN"));
		// params.put("SDP_WEB_SERVLET_URL",
		// rb.getString("SDP_WEB_SERVLET_URL"));
		// params.put("SDP_WEB_BASE_URL", rb.getString("SDP_WEB_BASE_URL"));

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

		params.put(SOLR_TYPE_ACCESS, rb.getString(SOLR_TYPE_ACCESS));
		params.put(SOLR_USERNAME, rb.getString(SOLR_USERNAME));
		params.put(SOLR_PASSWORD, rb.getString(SOLR_PASSWORD));
		params.put(SOLR_SECURITY_DOMAIN_NAME, rb.getString(SOLR_SECURITY_DOMAIN_NAME));

		
		params.put(JMS_MB_INTERNAL_URL, rb.getString(JMS_MB_INTERNAL_URL));
		params.put(JMS_MB_INTERNAL_USERNAME, rb.getString(JMS_MB_INTERNAL_USERNAME));
		params.put(JMS_MB_INTERNAL_PASSWORD, rb.getString(JMS_MB_INTERNAL_PASSWORD));

		params.put(JMS_MB_EXTERNAL_URL, rb.getString(JMS_MB_EXTERNAL_URL));
		params.put(JMS_MB_EXTERNAL_USERNAME, rb.getString(JMS_MB_EXTERNAL_USERNAME));
		params.put(JMS_MB_EXTERNAL_PASSWORD, rb.getString(JMS_MB_EXTERNAL_PASSWORD));

		params.put(SOLR_INDEXER_ENABLED, rb.getString(SOLR_INDEXER_ENABLED));

		params.put(KNOX_SDNET_ULR, rb.getString(KNOX_SDNET_ULR));
		params.put(KNOX_SDNET_USERNAME, rb.getString(KNOX_SDNET_USERNAME));
		params.put(KNOX_SDNET_PASSWORD, rb.getString(KNOX_SDNET_PASSWORD));

		params.put(MAIL_SERVER, rb.getString(MAIL_SERVER));
		params.put(MAIL_TO_ADDRESS, rb.getString(MAIL_TO_ADDRESS));
		params.put(MAIL_FROM_ADDRESS, rb.getString(MAIL_FROM_ADDRESS));
		params.put(DELETE_MAIL_SUBJECT_404, rb.getString(DELETE_MAIL_SUBJECT_404));
		params.put(DELETE_MAIL_BODY_404, rb.getString(DELETE_MAIL_BODY_404));
		params.put(DELETE_MAIL_SUBJECT_500, rb.getString(DELETE_MAIL_SUBJECT_500));
		params.put(DELETE_MAIL_BODY_500, rb.getString(DELETE_MAIL_BODY_500));
		params.put(DELETE_MAIL_SUBJECT_200, rb.getString(DELETE_MAIL_SUBJECT_200));
		params.put(DELETE_MAIL_BODY_200, rb.getString(DELETE_MAIL_BODY_200));
		
		params.put(ADMIN_API_URL, rb.getString(ADMIN_API_URL));
		params.put(SOURCE_METADATA, rb.getString(SOURCE_METADATA));
	}

	// MONGO_DB_DEFAULT
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
		} else
			return null;
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
		} else
			return null;
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
		} else
			return null;
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

		} else
			return -1;
	}

	
	public String getSolrSecurityDomainName() {
		return (params.get(SOLR_SECURITY_DOMAIN_NAME) != null ? params.get(SOLR_SECURITY_DOMAIN_NAME) : "");
	}
	
	public String getSolrUrl() {
		return params.get(SOLR_URL);
	}
	
	public String getSolrTypeAccess(){
		return params.get(SOLR_TYPE_ACCESS);
	}
	
	public String getSolrUsername(){
		return params.get(SOLR_USERNAME);
	}
	
	public String getSolrPassword(){
		return params.get(SOLR_PASSWORD);
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

	public String getJMSMbInternalUrl() {
		return params.get(JMS_MB_INTERNAL_URL);
	}

	public String getJMSMbInternalUsername() {
		return params.get(JMS_MB_INTERNAL_USERNAME);
	}

	public String getJMSMbInternalPassword() {
		return params.get(JMS_MB_INTERNAL_PASSWORD);
	}

	public String getJMSMbExternalUrl() {
		return params.get(JMS_MB_EXTERNAL_URL);
	}

	public String getJMSMbExternalUsername() {
		return params.get(JMS_MB_EXTERNAL_USERNAME);
	}

	public String getJMSMbExternalPassword() {
		return params.get(JMS_MB_EXTERNAL_PASSWORD);
	}

	public boolean isSolrIndexerEnabled() {
		String solrIndexerParam = params.get(SOLR_INDEXER_ENABLED);
		if (solrIndexerParam != null) {
			try {
				return Boolean.parseBoolean(solrIndexerParam);
			} catch (Exception e) {
				return false;
			}
		} else
			return false;
	}

	public String getKnoxSdnetUlr() {
		return params.get(KNOX_SDNET_ULR);
	}

	public String getKnoxSdnetUsername() {
		return params.get(KNOX_SDNET_USERNAME);
	}

	public String getKnoxSdnetPassword() {
		return params.get(KNOX_SDNET_PASSWORD);
	}

	public String getMailServer() {
		return params.get(MAIL_SERVER);
	}

	public String getMailToAddress() {
		return params.get(MAIL_TO_ADDRESS);
	}

	public String getMailFromAddress() {
		return params.get(MAIL_FROM_ADDRESS);
	}

	public String getDeleteMailSubject404() {
		return params.get(DELETE_MAIL_SUBJECT_404);
	}

	public String getDeleteMailBody404() {
		return params.get(DELETE_MAIL_BODY_404);
	}

	public String getDeleteMailSubject500() {
		return params.get(DELETE_MAIL_SUBJECT_500);
	}

	public String getDeleteMailBody500() {
		return params.get(DELETE_MAIL_BODY_500);
	}

	public String getDeleteMailSubject200() {
		return params.get(DELETE_MAIL_SUBJECT_200);
	}

	public String getDeleteMailBody200() {
		return params.get(DELETE_MAIL_BODY_200);
	}

	public String getAdminApiUrl() {
		return params.get(ADMIN_API_URL);
	}
	
	public boolean isSourceAdminApi() {
		return params.get(SOURCE_METADATA).equals(SOURCE_METADATA_ADMIN_API);
	}
	
	public boolean isSourceMongo() {
		return params.get(SOURCE_METADATA).equals(SOURCE_METADATA_MONGO);
	}
}
