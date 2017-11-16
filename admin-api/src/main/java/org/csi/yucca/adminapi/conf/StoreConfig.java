package org.csi.yucca.adminapi.conf;

public class StoreConfig {
	private String storeUrl;

	public StoreConfig(String storeUrl) {
		super();
		this.storeUrl = storeUrl;
	}

	public String getStoreUrl() {
		return storeUrl;
	}

	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}


}
