package org.csi.yucca.dataservice.binaryapi.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class BinaryConfig {


	public static final String BASE_API_URL = "BASE_API_URL";
	public static final String DAMMI_INFO = "DAMMI_INFO";
	public static final String CONSOLE_ADDRESS = "CONSOLE_ADDRESS";
	public static final String HTTP_OK = "HTTP_OK";
	public static final String RESPONSE_OK = "RESPONSE_OK";
	public static final String STORE_API_ADDRESS = "STORE_API_ADDRESS";
	public static final String HDFS_ROOT_DIR = "HDFS_ROOT_DIR";
	public static final String HDFS_OLD_ROOT_DIR = "HDFS_OLD_ROOT_DIR";
	public static final String HDFS_USERNAME = "HDFS_USERNAME";
	public static final String KNOX_URL = "KNOX_URL";
	public static final String KNOX_PWD = "KNOX_PWD";
	public static final String KNOX_USER = "KNOX_USER";
	public static final String KNOX_GROUP = "KNOX_GROUP";
	public static final String HDFS_LIBRARY = "HDFS_LIBRARY";
	public static final String API_ADMIN_SERVICES_URL = "API_ADMIN_SERVICES_URL";
	public static final String DATA_INSERT_BASE_URL = "DATA_INSERT_BASE_URL";

	private static Map<String, String> params = null;
	private static BinaryConfig instance = null;

	private BinaryConfig() {

		params = new HashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle("SDPDataApiConfig");
		
		params.put(DAMMI_INFO, rb.getString(DAMMI_INFO));
		params.put(CONSOLE_ADDRESS, rb.getString(CONSOLE_ADDRESS));
		params.put(HTTP_OK, rb.getString(HTTP_OK));
		params.put(RESPONSE_OK, rb.getString(RESPONSE_OK));
		params.put(STORE_API_ADDRESS, rb.getString(STORE_API_ADDRESS));
		params.put(HDFS_ROOT_DIR, rb.getString(HDFS_ROOT_DIR));
		params.put(HDFS_USERNAME, rb.getString(HDFS_USERNAME));
		params.put(HDFS_LIBRARY, rb.getString(HDFS_LIBRARY));
		params.put(API_ADMIN_SERVICES_URL, rb.getString(API_ADMIN_SERVICES_URL));
		params.put(DATA_INSERT_BASE_URL, rb.getString(DATA_INSERT_BASE_URL));
		
		
		ResourceBundle rbSecret = ResourceBundle.getBundle("SDPDataApiSecret");
		params.put(KNOX_URL, rbSecret.getString(KNOX_URL));
		params.put(KNOX_PWD, rbSecret.getString(KNOX_PWD));
		params.put(KNOX_USER, rbSecret.getString(KNOX_USER));
		params.put(KNOX_GROUP, rbSecret.getString(KNOX_GROUP));
	}

	public static BinaryConfig getInstance() {
		if (instance == null) {
			instance = new BinaryConfig();
		}
		return instance;
	}



	public String getBaseApiUrl() {
		return params.get(BASE_API_URL);
	}
	
	public String getDammiInfo() {
		return params.get(DAMMI_INFO);
	}
	
	public String getConsoleAddress() {
		return params.get(CONSOLE_ADDRESS);
	}
	
	public String getHttpOk() {
		return params.get(HTTP_OK);
	}
	
	public String getResponseOk() {
		return params.get(RESPONSE_OK);
	}
	
	public String getStoreApiAddress() {
		return params.get(STORE_API_ADDRESS);
	}
	
	public static String getHdfsRootDir() {
		return params.get(HDFS_ROOT_DIR);
	}
	
	public static String getHdfsUsername() {
		return params.get(HDFS_USERNAME);
	}

	public static String getKnoxUrl() {
		return params.get(KNOX_URL);
	}

	public static String getKnoxPwd() {
		return params.get(KNOX_PWD);
	}

	public static String getKnoxUser() {
		return params.get(KNOX_USER);
	}

	public static String getKnoxGroup() {
		return params.get(KNOX_GROUP);
	}

	public static String getHdfsLibrary() {
		return params.get(HDFS_LIBRARY);
	}
	
	public String getApiAdminServicesUrl() {
		return params.get(API_ADMIN_SERVICES_URL);
	}
	
	public String getDataInsertBaseUrl() {
		return params.get(DATA_INSERT_BASE_URL);
	}
}
