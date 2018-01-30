package org.csi.yucca.adminapi.util;

public enum FeedbackStatus {
	
	OK ("ok"),
	KO ("ko");
	
	private String code;
	
	FeedbackStatus(String code){
		this.code = code;
	}

	public String code() {
		return code;
	}

}
