package org.csi.yucca.adminapi.util;

import org.springframework.http.HttpStatus;

public class ServiceResponse {

	public Object object;
	
	public HttpStatus httpStatus = HttpStatus.OK;

	public ServiceResponse OK(){
		this.httpStatus = HttpStatus.OK;
		return this;
	}
	
	public ServiceResponse NO_CONTENT(){
		this.httpStatus = HttpStatus.NO_CONTENT;
		return this;
	}
	
	
	public ServiceResponse object(Object object){
		this.object = object;
		return this;
	}
	
	public static ServiceResponse build(){
		return new ServiceResponse();
	}

	private ServiceResponse() {
		super();
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	
	
}
