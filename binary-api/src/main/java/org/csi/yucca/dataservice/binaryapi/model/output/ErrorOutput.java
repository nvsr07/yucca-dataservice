package org.csi.yucca.dataservice.binaryapi.model.output;

public class ErrorOutput {

	private String errorName=null;
	private String errorCode=null;
	private String output=null;
	private String errorMessage=null;
	public String getErrorName() {
		return errorName;
	}
	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	
	public ErrorOutput() {
	}
	
	public ErrorOutput(String errorName, String errorCode, String output,
			String errorMessage) {
		super();
		this.errorName = errorName;
		this.errorCode = errorCode;
		this.output = output;
		this.errorMessage = errorMessage;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	
}
