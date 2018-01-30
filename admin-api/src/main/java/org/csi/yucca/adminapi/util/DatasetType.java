package org.csi.yucca.adminapi.util;

public enum DatasetType {
	
	DATASET (1, "dataset", "dataset");
	
	private int id;
	private String code;
	private String description;
	
	DatasetType(int id, String code, String description){
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
