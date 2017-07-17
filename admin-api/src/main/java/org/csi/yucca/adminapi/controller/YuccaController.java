package org.csi.yucca.adminapi.controller;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.exception.YuccaException;
import org.csi.yucca.adminapi.response.ErrorResponse;
import org.csi.yucca.adminapi.util.ApiCallable;
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
	
	/**
	 * 
	 * @param apiCallable
	 * @param logger
	 * @return
	 */
	public ResponseEntity<Object> run(ApiCallable apiCallable, Logger logger){
		
		Object obj = null;
		
		try {
			
			obj = apiCallable.call();
			
		} 
		catch (BadRequestException badRequestException) {
			logger.error("BadRequestException: " + badRequestException);
			return buildErrorResponse(badRequestException);
		}
		catch (NotFoundException notFoundException) {
			logger.error("NotFoundException: " + notFoundException);			
			return buildErrorResponse(notFoundException);
		}
		catch (Exception e) {
			return internalServerError(e);
		}
		
		
		return buildResponse(obj);
		
	}	
	
	
}
