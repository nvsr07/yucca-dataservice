package org.csi.yucca.adminapi.response;

import java.util.List;

import org.csi.yucca.adminapi.util.Errors;

import com.fasterxml.jackson.annotation.JsonInclude;


public class Response {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String errorCode;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String errorName;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<String> args;
	
	public Response args(List<String> args){
		setArgs(args);
		return this;
	}
	
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

	public List<String> getArgs() {
		return args;
	}

	public void setArgs(List<String> args) {
		this.args = args;
	}

	
}
