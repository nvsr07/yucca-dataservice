package org.csi.yucca.adminapi.util;

public enum StreamAction {
	
	REQUEST_INSTALLATION   ("req_install"),
	REQUEST_UNINSTALLATION ("req_uninstall"),
	NEW_VERSION            ("new_version");
	
	private String code;
	
	StreamAction(String code){
		this.code = code;
	}

	public String code() {
		return code;
	}

}
