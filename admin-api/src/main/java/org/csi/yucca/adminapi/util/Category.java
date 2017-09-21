package org.csi.yucca.adminapi.util;

public enum Category {
	
	GATEWAY (1,   "Gateway", "Gateway"),
	WEBCAM  (2,   "Webcam",  "Webcam"),
	SMART   (3,   "Smart",   "Smart"),
	NONE    (999, "None",    "None");
	
	private int id;
	private String code;
	private String description;
	
	Category(int id, String code, String description){
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
