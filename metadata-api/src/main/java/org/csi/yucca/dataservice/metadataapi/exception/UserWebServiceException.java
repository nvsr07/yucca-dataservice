package org.csi.yucca.dataservice.metadataapi.exception;

import java.io.Serializable;

import javax.ws.rs.core.Response;

public class UserWebServiceException extends Exception implements Serializable {

	private Response response;
	
	public UserWebServiceException(Response response) {
		this.response = response;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
}
