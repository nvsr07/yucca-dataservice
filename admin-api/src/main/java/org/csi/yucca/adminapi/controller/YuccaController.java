package org.csi.yucca.adminapi.controller;

import org.csi.yucca.adminapi.exception.YuccaException;
import org.csi.yucca.adminapi.response.ErrorResponse;
import org.csi.yucca.adminapi.util.Errors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class YuccaController {

	public ResponseEntity<Object> buildErrorResponse(YuccaException exception){
		return new ResponseEntity<Object>(new ErrorResponse(exception.errors()), 
				exception.getHttpStatus());
	}
	
	public ResponseEntity<Object> buildResponse(Object obj){
		return new ResponseEntity<Object>(obj, 
				HttpStatus.OK);
	}
	
	public ResponseEntity<Object> internalServerError(Exception exception){
		return new ResponseEntity<Object>(new ErrorResponse(Errors.INTERNAL_SERVER_ERROR.arg(exception.toString())), 
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleBindException(Exception be) {
		return new ResponseEntity<Object>(new ErrorResponse(Errors.PARAMETER_TYPE_ERROR.arg(be.getMessage())), 
				HttpStatus.BAD_REQUEST);
	}
	
	
	
}
