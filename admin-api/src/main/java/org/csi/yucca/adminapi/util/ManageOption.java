package org.csi.yucca.adminapi.util;

public enum ManageOption {
	
	NO_RIGHT            (0, "read"),
	EDIT_METADATA       (1, "read & subscribe"),
	LIFE_CYCLE_HANDLING (2, "read & use");
	
	private int id;
	private String code;
	
	ManageOption(int id, String code){
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
