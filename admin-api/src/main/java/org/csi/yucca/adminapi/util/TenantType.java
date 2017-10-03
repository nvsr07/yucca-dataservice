package org.csi.yucca.adminapi.util;

public enum TenantType {

	DEFAULT    (1, "default", "default"),
	PERSONAL   (2, "personal", "personal"),
	PLUS       (3, "plus", "plus"),
	TRIAL      (4, "trial", "trial"),
	ZERO       (5, "zero", "zero"),
	DEVELOP    (6, "develop", "develop");
	
	private int id;
	private String code;
	private String description;
	
	TenantType(int id, String code, String description){
		this.id = id;
		this.code = code;
		this.description = description;
	}

	public int id() {
		return id;
	}

	public String code() {
		return code;
	}

	public String description() {
		return description;
	}
	
}
