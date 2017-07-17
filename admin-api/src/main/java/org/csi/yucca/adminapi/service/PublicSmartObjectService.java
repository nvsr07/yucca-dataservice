package org.csi.yucca.adminapi.service;

import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.response.Response;

public interface PublicSmartObjectService {
	
	List<Response> selectExposureType(String sort) throws BadRequestException, NotFoundException, Exception;
	
	List<Response> selectSoCategory(String sort) throws BadRequestException, NotFoundException, Exception;
	
	List<Response> selectLocationType(String sort) throws BadRequestException, NotFoundException, Exception;
}
