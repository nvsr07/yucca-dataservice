package org.csi.yucca.dataservice.metadataapi.service.response;


public class ErrorResponse  extends AbstractResponse{
	private String errorCode;
	private String message;

	public ErrorResponse() {

	}



	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
