package org.csi.yucca.adminapi.exception;

import org.csi.yucca.adminapi.util.Errors;
import org.springframework.http.HttpStatus;

public class BadRequestException extends YuccaException {

	private static final long serialVersionUID = 2438358754392518832L;

	public BadRequestException(Errors errors) {
		super(errors);
		super.setHttpStatus(HttpStatus.BAD_REQUEST);
	}
}
