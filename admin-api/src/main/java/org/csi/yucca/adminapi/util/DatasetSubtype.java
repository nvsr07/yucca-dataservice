package org.csi.yucca.adminapi.util;

public enum DatasetSubtype {
	
	BULK   (1,"bulkDataset",   "bulkDataset"),
	STREAM (2,"streamDataset", "streamDataset"),
	BINARY (3,"binaryDataset", "binaryDataset"),
	SOCIAL (4,"socialDataset", "socialDataset");
	
	private int id;
	private String code;
	private String description;
	
	DatasetSubtype(int id, String code, String description){
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
