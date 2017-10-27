package org.csi.yucca.adminapi.util;

public enum StreamVisibility {
	
	PRIVATE ("private"),
	PUBLIC  ("public");
	
	private String code;
	
	StreamVisibility(String code){
		this.code = code;
	}

	public String code() {
		return code;
	}

}
