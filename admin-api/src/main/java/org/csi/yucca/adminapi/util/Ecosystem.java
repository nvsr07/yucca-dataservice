package org.csi.yucca.adminapi.util;

public enum Ecosystem {

	SDNET (1, "SDNET");
	
	private int id;
	private String code;
	
	Ecosystem(int id, String code){
		this.id = id;
		this.code = code;
	}

	public int id() {
		return id;
	}

	public String code() {
		return code;
	}
	
}
