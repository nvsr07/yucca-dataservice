package org.csi.yucca.adminapi.exception;

import org.csi.yucca.adminapi.util.Errors;
import org.springframework.http.HttpStatus;

public class NotFoundException extends YuccaException {
	
	private static final long serialVersionUID = 1L;

	public NotFoundException(Errors errors) {
		super(errors);
		super.setHttpStatus(HttpStatus.NOT_FOUND);
	}
	
}
