package org.csi.yucca.adminapi.util;

public enum ShareType {

	NONE   (1, "none"),
	PUBLIC (2, "public");
	
	private int id;
	private String description;
	
	ShareType(int id, String description){
		this.id = id;
		this.description = description;
	}

	public int id() {
		return id;
	}

	public String description() {
		return description;
	}
}
