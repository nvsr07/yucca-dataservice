package org.csi.yucca.adminapi.service;

import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.response.Response;

public interface PublicClassificationService {
	
	List<Response> selectDomain(Integer ecosystemCode, String lang, String sort) throws BadRequestException, NotFoundException, Exception;
	
	List<Response> selectEcosystem(Integer organizationCode, String sort) throws BadRequestException, NotFoundException, Exception;
	
	List<Response> selectLicense(String sort) throws BadRequestException, NotFoundException, Exception;
}
