package org.csi.yucca.adminapi.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.conf.JwtFilter;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.ConflictException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.exception.UnauthorizedException;
import org.csi.yucca.adminapi.exception.YuccaException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.response.Response;
import org.csi.yucca.adminapi.util.ApiCallable;
import org.csi.yucca.adminapi.util.ApiExecutable;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class YuccaController {
	
	protected JwtUser getAuthorizedUser(HttpServletRequest request){
		return (JwtUser)request.getAttribute(JwtFilter.JWT_USER_REQUEST_ATTRIBUTE_KEY);
	}
	
	public ResponseEntity<Object> buildErrorResponse(YuccaException exception){
		return new ResponseEntity<Object>(new Response(exception.errors(), exception.getArg()), 
				exception.getHttpStatus());
	}
	
	public ResponseEntity<Object> buildResponse(ServiceResponse serviceResponse){
		return new ResponseEntity<Object>(serviceResponse.getObject(), 
				serviceResponse.getHttpStatus());
	}

	public ResponseEntity buildResponse(){
		return new ResponseEntity(HttpStatus.OK);
	}
	
	public ResponseEntity<Object> internalServerError(Exception exception){
		return new ResponseEntity<Object>(new Response(Errors.INTERNAL_SERVER_ERROR, exception.toString()), 
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleBindException(Exception be) {
		return new ResponseEntity<Object>(new Response(Errors.PARAMETER_TYPE_ERROR, be.getMessage()), 
				HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * 
	 * @param apiCallable
	 * @param logger
	 * @return
	 */
	public ResponseEntity<Object> run(ApiCallable apiCallable, Logger logger){
		
		ServiceResponse serviceResponse = null;
		
		try {
			
			serviceResponse = apiCallable.call();
			
		} 
		catch (UnauthorizedException unauthorizedException) {
			logger.error("UnauthorizedException: " + unauthorizedException);
			return buildErrorResponse(unauthorizedException);
		}
		catch (BadRequestException badRequestException) {
			logger.error("BadRequestException: " + badRequestException);
			return buildErrorResponse(badRequestException);
		}
		catch (NotFoundException notFoundException) {
			logger.error("NotFoundException: " + notFoundException);			
			return buildErrorResponse(notFoundException);
		}
		catch (ConflictException conflictException) {
			logger.error("ConflictException: " + conflictException);			
			return buildErrorResponse(conflictException);
		}
		catch (Exception e) {
			return internalServerError(e);
		}
		
		
		return buildResponse(serviceResponse);
		
	}	
	
	public ResponseEntity<Object> run(ApiExecutable apiExecutable, Logger logger){
		
		try {
			
			apiExecutable.call();
			
		} 
		catch (BadRequestException badRequestException) {
			logger.error("BadRequestException: " + badRequestException);
			return buildErrorResponse(badRequestException);
		}
		catch (NotFoundException notFoundException) {
			logger.error("NotFoundException: " + notFoundException);			
			return buildErrorResponse(notFoundException);
		}
		catch (ConflictException conflictException) {
			logger.error("ConflictException: " + conflictException);			
			return buildErrorResponse(conflictException);
		}
		catch (Exception e) {
			return internalServerError(e);
		}
		
		return buildResponse();
	}	
	
	
}
