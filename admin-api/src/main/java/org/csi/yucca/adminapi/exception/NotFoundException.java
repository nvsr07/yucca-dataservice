package org.csi.yucca.adminapi.exception;

import org.csi.yucca.adminapi.util.Errors;
import org.springframework.http.HttpStatus;

public class NotFoundException extends YuccaException {
	
	private static final long serialVersionUID = 1L;
	
	public NotFoundException(Errors errors) {
		this(errors, null);
	}
	
	public NotFoundException(Errors errors, String arg) {
		super(errors, arg);
		super.setHttpStatus(HttpStatus.NOT_FOUND);
	}
	
}
