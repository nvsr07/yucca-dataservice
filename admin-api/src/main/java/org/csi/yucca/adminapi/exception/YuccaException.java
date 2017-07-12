package org.csi.yucca.adminapi.exception;

import org.csi.yucca.adminapi.util.Errors;
import org.springframework.http.HttpStatus;

public class YuccaException extends Exception {

	private Errors errors;
	
	private HttpStatus httpStatus;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7650157029898117208L;

	public YuccaException(String message) {
		super(message);
	}
	
	public YuccaException(Errors errors) {
		super(errors.errorName());
		this.errors = errors;
	}

	public Errors errors(){
		return this.errors;
	}

	public Errors getErrors() {
		return errors;
	}

	public void setErrors(Errors errors) {
		this.errors = errors;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	
}
