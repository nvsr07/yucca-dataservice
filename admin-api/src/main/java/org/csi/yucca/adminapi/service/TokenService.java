package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface TokenService {

	ServiceResponse get(String clientKey, String clientSecret) throws BadRequestException, NotFoundException, Exception;
	
}
