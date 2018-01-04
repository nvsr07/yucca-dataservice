package org.csi.yucca.adminapi.util;

public enum StreamAction {
	
	REQUEST_INSTALLATION   ("req_install"),
	REQUEST_UNINSTALLATION ("req_uninstall"),
	NEW_VERSION            ("new_version"),
	INSTALLATION           ("install"),
	UNINSTALLATION         ("uninstall"),
	DELETE                 ("delete"),
	MIGRATE                ("migrate"),
	UPGRADE                ("upgrade");
	
	private String code;
	
	StreamAction(String code){
		this.code = code;
	}

	public String code() {
		return code;
	}

}
