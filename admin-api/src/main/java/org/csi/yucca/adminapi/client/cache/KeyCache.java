package org.csi.yucca.adminapi.client.cache;

public class KeyCache {
	
	private String code;
	
	private Integer id;
	
	private String adminApiBaseUrl;
	
	private String logger;

	
	public KeyCache code(String code){
		this.code = code;
		return this;
	}
	
	public KeyCache id(Integer id){
		this.id = id;
		return this;
	}
	
	public KeyCache adminApiBaseUrl(String adminApiBaseUrl){
		this.adminApiBaseUrl = adminApiBaseUrl;
		return this;
	}
	
	public KeyCache logger(String logger){
		this.logger = logger;
		return this;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAdminApiBaseUrl() {
		return adminApiBaseUrl;
	}

	public void setAdminApiBaseUrl(String adminApiBaseUrl) {
		this.adminApiBaseUrl = adminApiBaseUrl;
	}

	public String getLogger() {
		return logger;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}

	
}
