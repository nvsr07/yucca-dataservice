package org.csi.yucca.adminapi.util;

public enum Errors {

	MANDATORY_PARAMETER         ("E01", "Mandatory parameter"),
	RECORD_NOT_FOUND            ("E02", "Record not found"),
	PROPERTY_NOT_FOUND          ("E03", "Property not found"),
    INTERNAL_SERVER_ERROR       ("E04", "Internal Server Error"),
    LANGUAGE_NOT_SUPPORTED      ("E05", "Language not supported"),
    PARAMETER_TYPE_ERROR        ("E06", "Parameter type error"),
    INTEGRITY_VIOLATION         ("E07", "Integrity violation"),
    DUPLICATE_KEY               ("E08", "Duplicate key"),
    WHITE_SPACES                ("E09", "Could not contain white spaces"),
    NO_TWEET_SO_TYPE            ("E10", "Is not Tweet SO Type"),
    NOT_CONSISTENT_DATA         ("E11", "Not consistent data"),
    INCORRECT_VALUE             ("E12", "Incorrect value"),
    ALPHANUMERIC_VALUE_REQUIRED ("E13", "Alphanumeric value required"),
    UNAUTHORIZED                ("E14", "Unauthorized User")
    
    ;
	
	private String errorName;
	private String errorCode;
	
	Errors(String errorCode, String errorName){
		this.errorName = errorName;
		this.errorCode = errorCode;
	}
	
	public String errorName(){
		return this.errorName;
	}

	public String errorCode(){
		return this.errorCode;
	}
	
}
