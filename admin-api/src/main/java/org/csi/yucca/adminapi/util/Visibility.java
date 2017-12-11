package org.csi.yucca.adminapi.util;

public enum Visibility {
	
	PRIVATE ("private"),
	PUBLIC  ("public");
	
	private String code;
	
	Visibility(String code){
		this.code = code;
	}

	public String code() {
		return code;
	}

}
