package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.util.Errors;

import com.fasterxml.jackson.annotation.JsonInclude;


public class Response {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String errorCode;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String errorName;
	
	public Response(Errors errors, String arg){
		this.errorCode = errors.errorCode();
		if (arg != null) {
			this.errorName = errors.errorName() + ": " + arg;
		}
		else{
			this.errorName = errors.errorName();	
		}
	}

	public Response() {
		super();
//		this.errorCode = "def";
//		this.errorName = "def";
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorName() {
		return errorName;
	}

	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

	
}
