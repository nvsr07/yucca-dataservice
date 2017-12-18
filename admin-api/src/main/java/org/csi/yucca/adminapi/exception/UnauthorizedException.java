package org.csi.yucca.adminapi.exception;

import org.csi.yucca.adminapi.util.Errors;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends YuccaException {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -95836673838818709L;

	public UnauthorizedException(Errors errors) {
		this(errors, null);
	}

	public UnauthorizedException(Errors errors, String arg) {
		super(errors, arg);
		super.setHttpStatus(HttpStatus.UNAUTHORIZED);
	}
}
