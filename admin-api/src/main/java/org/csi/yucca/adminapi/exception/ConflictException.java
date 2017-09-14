package org.csi.yucca.adminapi.exception;

import org.csi.yucca.adminapi.util.Errors;
import org.springframework.http.HttpStatus;

public class ConflictException extends YuccaException {

	private static final long serialVersionUID = 2438358754392518832L;

	public ConflictException(Errors errors) {
		this(errors, null);
	}
	
	public ConflictException(Errors errors, String arg) {
		super(errors, arg);
		super.setHttpStatus(HttpStatus.CONFLICT);
		
	}
}
