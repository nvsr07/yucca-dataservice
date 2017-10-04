package org.csi.yucca.adminapi.util;

public enum Status {
	
	DRAFT                   (1, "draft",      "draft"),
	INSTALLED               (2, "inst",       "installed"),
	REQUEST_INSTALLATION    (3, "req_inst",   "installation requested"),
	REQUEST_UNINSTALLATION  (4, "req_uninst", "uninstall in progress"),
	UNINSTALLATION          (5, "uninst",     "uninstalled and historicized"),
	INSTALLATION_IN_PROGRESS   (6, "prg_inst",   "installation in progress"),
	UNINSTALLATION_IN_PROGRESS (7, "prg_uninst", "uninstallation in progress");
	
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
