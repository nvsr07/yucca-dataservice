package org.csi.yucca.adminapi.client.cache.key;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class KeyCache implements Serializable{

	private static final long serialVersionUID = 4282547711900104665L;
	private String logger;
	private String adminBaseUrl;
	private Integer id;
	private String code;
	private Map<String, String> params;
	
	public KeyCache addParams(String key, String value){
		if(params == null) params = new HashMap<>();
		params.put(key, value);
		return this;
	}
	
	public KeyCache addParams(String key, Boolean value){
		addParams(key, value ? "true" : "false");
		return this;
	}
	
	public KeyCache id(Integer id){
		this.id = id;
		return this;
	}

	public KeyCache code(String code){
		this.code = code;
		return this;
	}
	
	public KeyCache(String adminBaseUrl, String logger) {
		super();
		this.adminBaseUrl = adminBaseUrl;
		this.logger = logger;
	}

	public String getAdminBaseUrl() {
		return adminBaseUrl;
	}

	public void setAdminBaseUrl(String adminBaseUrl) {
		this.adminBaseUrl = adminBaseUrl;
	}

	public String getLogger() {
		return logger;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getKeyUrl(){
		
		if (id != null) {
			return Integer.toString(id);	
		}
	
		if (code != null) {
			return code;
		}
		
		return "";
	}
	
	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((logger == null) ? 0 : logger.hashCode());
		result = prime * result + ((adminBaseUrl == null) ? 0 : adminBaseUrl.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		KeyCache other = (KeyCache) obj;
		
		if (adminBaseUrl == null) {
			if (other.adminBaseUrl != null) return false;
		} 
		else if (!adminBaseUrl.equals(other.adminBaseUrl))
			return false;

		if (logger == null) {
			if (other.logger != null) return false;
		} 
		else if (!logger.equals(other.logger))
			return false;
		
		if (code == null) {
			if (other.code != null) return false;
		} 
		else if (!code.equals(other.code))
			return false;
		
		if (id == null) {
			if (other.id != null) return false;
		} 
		else if (!id.equals(other.id))
			return false;
		
		
		return true;
	}
	
}
