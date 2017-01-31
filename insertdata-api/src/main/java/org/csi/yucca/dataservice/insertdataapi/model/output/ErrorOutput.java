package org.csi.yucca.dataservice.insertdataapi.model.output;

import org.codehaus.jackson.annotate.JsonProperty;
	

public class ErrorOutput {

	private String errorName=null;
	private String errorCode=null;
	private String output=null;
	private String errorMessage=null;
	
	
	@JsonProperty("error_name")
	public String getErrorName() {
		return errorName;
	}
	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}
	@JsonProperty("error_code")
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
	
	@JsonProperty("error_message")
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	
}
