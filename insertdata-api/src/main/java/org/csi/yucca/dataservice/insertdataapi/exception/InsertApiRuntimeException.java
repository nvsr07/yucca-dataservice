package org.csi.yucca.dataservice.insertdataapi.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.csi.yucca.dataservice.insertdataapi.model.output.ErrorOutput;


public class InsertApiRuntimeException extends  WebApplicationException{

	private static final long serialVersionUID = 1L;

	private String errorName=null;
	private String errorCode=null;
	private String output=null;
	private String errorMessage=null;
	
	private String getMessageFromCode(String errorCode) {
		return "unknown error";
	}

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

	
	@Override
	public javax.ws.rs.core.Response getResponse() {
		ResponseBuilder response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		response.entity(new ErrorOutput(errorName, errorCode, output, errorMessage));
		return response.build();
	};
	
	public InsertApiRuntimeException(Throwable e) {
		super(e);
		this.errorCode="UNKNOWN";
		this.errorName=getMessageFromCode(errorCode);
	}
	
	
}
