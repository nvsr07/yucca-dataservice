package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface ComponentService {
	
	ServiceResponse selectDataType(String sort) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectMeasureUnit(String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectPhenomenon(String sort) throws BadRequestException, NotFoundException, Exception;
}
