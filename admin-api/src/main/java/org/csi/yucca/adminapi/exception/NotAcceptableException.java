package org.csi.yucca.adminapi.exception;

import org.csi.yucca.adminapi.util.Errors;
import org.springframework.http.HttpStatus;

public class NotAcceptableException extends YuccaException {
	
	private static final long serialVersionUID = 1L;
	
	public NotAcceptableException(Errors errors) {
		this(errors, null);
	}
	
	public NotAcceptableException(Errors errors, String arg) {
		super(errors, arg);
		super.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
	}
	
}
