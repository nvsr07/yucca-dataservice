package org.csi.yucca.adminapi.util;

public enum Type {

	INTERNAL    (0, "Internal",    "Internal"),
	DEVICE      (1, "Device",      "Device"),
	APPLICATION (2, "Application", "Application"),
	FEED_TWEET  (3, "Feed Tweet",  "Feed Tweet");
	
	private int id;
	private String code;
	private String description;
	
	Type(int id, String code, String description){
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
