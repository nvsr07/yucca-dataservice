package org.csi.yucca.adminapi.conf;

public class StoreConfig {
	private String storeUrl;
	private String storeUsername;
	private String storePassword;

	public StoreConfig(String storeUrl, String storeUsername, String storePassword) {
		super();
		this.storeUrl = storeUrl;
		this.storeUsername = storeUsername;
		this.storePassword = storePassword;
	}

	public String getStoreUrl() {
		return storeUrl;
	}

	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}

	public String getStoreUsername() {
		return storeUsername;
	}

	public void setStoreUsername(String storeUsername) {
		this.storeUsername = storeUsername;
	}

	public String getStorePassword() {
		return storePassword;
	}

	public void setStorePassword(String storePassword) {
		this.storePassword = storePassword;
	}

}
