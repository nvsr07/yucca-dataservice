package org.csi.yucca.adminapi.util;

public enum DataOption {

	READ               (0, "read"),
	READ_AND_SUBSCRIBE (1, "read & subscribe"),
	READ_AND_USE       (2, "read & use"),
	WRITE              (3, "write");
	
	private int id;
	private String code;
	
	DataOption(int id, String code){
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
