package org.csi.yucca.dataservice.metadataapi.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Config {

	public static final String METADATAAPI_BASE_URL = "METADATAAPI_BASE_URL";

	public static final String STORE_BASE_URL = "STORE_BASE_URL";
	public static final String SERVICE_BASE_URL = "SERVICE_BASE_URL";
	public static final String MANAGEMENT_BASE_URL = "MANAGEMENT_BASE_URL";
	public static final String OAUTH_BASE_URL = "OAUTH_BASE_URL";
	public static final String OAUTH_USERNAME = "OAUTH_USERNAME";
	public static final String OAUTH_PASSWORD = "OAUTH_PASSWORD";

	private static Map<String, String> params = null;
	private static Config instance = null;

	private Config() {

		params = new HashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle("MetadataApiConfig");
		params.put(METADATAAPI_BASE_URL, rb.getString(METADATAAPI_BASE_URL));
		params.put(STORE_BASE_URL, rb.getString(STORE_BASE_URL));
		params.put(SERVICE_BASE_URL, rb.getString(SERVICE_BASE_URL));
		params.put(MANAGEMENT_BASE_URL, rb.getString(MANAGEMENT_BASE_URL));
		params.put(OAUTH_BASE_URL, rb.getString(OAUTH_BASE_URL));
		params.put(OAUTH_USERNAME, rb.getString(OAUTH_USERNAME));
		ResourceBundle rbSecret = ResourceBundle.getBundle("MetadataApiSecret");
		params.put(OAUTH_PASSWORD, rbSecret.getString(OAUTH_PASSWORD));
	}

	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

	public String getMetadataapiBaseUrl() {
		return params.get(METADATAAPI_BASE_URL);
	}

	public String getStoreBaseUrl() {
		return params.get(STORE_BASE_URL);
	}

	public String getOauthBaseUrl() {
		return params.get(OAUTH_BASE_URL);
	}

	public String getOauthUsername() {
		return params.get(OAUTH_USERNAME);
	}

	public String getOauthPassword() {
		return params.get(OAUTH_PASSWORD);
	}

	public String getServiceBaseUrl() {
		return params.get(SERVICE_BASE_URL);
	}

	public String getManagementBaseUrl() {
		return params.get(MANAGEMENT_BASE_URL);
	}

}
