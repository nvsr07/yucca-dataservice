package org.csi.yucca.adminapi.util;

public enum Errors {

	MANDATORY_PARAMETER   ("E01", "Mandatory parameter"),
	RECORD_NOT_FOUND      ("E02", "Record not found"),
	PROPERTY_NOT_FOUND    ("E03", "Property not found"),
    INTERNAL_SERVER_ERROR ("E04", "Internal Server Error"),
    LANGUAGE_NOT_SUPPORTED("E05", "Language not supported"),
    PARAMETER_TYPE_ERROR  ("E06", "Parameter type error");
	
	private String errorName;
	private String errorCode;
	private String arg;
	
	Errors(String errorCode, String errorName){
		this.errorName = errorName;
		this.errorCode = errorCode;
	}
	
	public Errors arg(String arg){
		this.arg = arg;
		return this;
	}
	
	public String errorName(){
		if(this.arg != null && !this.arg.isEmpty()){
			return this.errorName + ": " + this.arg;
		}
		return this.errorName;
	}

	public String errorCode(){
		return this.errorCode;
	}
	
}
