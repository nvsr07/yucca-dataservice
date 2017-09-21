package org.csi.yucca.adminapi.util;

public enum Status {

	DRAFT                   (1, "draft",      "draft"),
	INSTALLED               (2, "inst",       "inst"),
	REQUEST_INSTALLATION    (3, "req_inst",   "req_inst"),
	REQUEST_UNINSTALLATION  (4, "req_uninst", "req_uninst"),
	UNINSTALLATION          (5, "uninst",     "uninst");
	
	private int id;
	private String code;
	private String description;
	
	Status(int id, String code, String description){
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
