package org.csi.yucca.adminapi.service;

import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.response.Response;

public interface PublicComponentService {
	
	List<Response> selectDataType(String sort) throws BadRequestException, NotFoundException, Exception;

	List<Response> selectMeasureUnit(String sort) throws BadRequestException, NotFoundException, Exception;
}
