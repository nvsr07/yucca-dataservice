package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.request.SmartobjectRequest;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface SmartObjectService {
	
	ServiceResponse deleteSmartObject(String organizationCode, String socode) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectExposureType(String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectSoCategory(String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectLocationType(String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectSoType(String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectSupplyType(String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse insertSmartobject(SmartobjectRequest smartobjectRequest, String organizationCode) throws BadRequestException, NotFoundException, Exception;
}
