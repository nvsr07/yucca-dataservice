package org.csi.yucca.adminapi.util;

import org.springframework.http.HttpStatus;

public class ServiceResponse {

	public Object object;
	
	public HttpStatus httpStatus = HttpStatus.OK;
	
	public boolean isImage = false;

	public ServiceResponse OK(){
		this.httpStatus = HttpStatus.OK;
		return this;
	}

	public ServiceResponse httpStatus(int status){

		if(status == 400){
			this.httpStatus = HttpStatus.BAD_REQUEST;
		}
		else if(status == 503){
			this.httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		}
		else if(status == 408){
			this.httpStatus = HttpStatus.REQUEST_TIMEOUT;
		}
		else{
			this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return this;
	}
	
	public ServiceResponse NO_CONTENT(){
		this.httpStatus = HttpStatus.NO_CONTENT;
		return this;
	}
	
	
	public ServiceResponse object(Object object){
		this.object = object;
		this.isImage = false;
		return this;
	}
	
	public ServiceResponse image(String base64image) {
		this.object = Util.convertIconFromDBToByte(base64image);
		this.isImage = true;
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

	public boolean getIsImage() {
		return isImage;
	}
	public void setIsImage(Boolean isImage) {
		this.isImage = isImage;
	}

	
}
