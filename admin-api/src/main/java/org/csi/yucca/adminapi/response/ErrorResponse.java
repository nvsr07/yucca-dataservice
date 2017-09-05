package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.util.Errors;

public class ErrorResponse extends Response{
	
	private String errorCode;
	private String errorName;

	public ErrorResponse(Errors errors){
		this.errorCode = errors.errorCode();
		this.errorName = errors.errorName();
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
