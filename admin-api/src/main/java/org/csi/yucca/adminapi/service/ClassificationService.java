package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.request.DomainRequest;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface ClassificationService {
	
	ServiceResponse selectDomain(Integer ecosystemCode, String lang, String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectEcosystem(Integer organizationCode, String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectLicense(String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectOrganization(Integer ecosystemCode, String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectSubdomain(Integer domainCode, String lang, String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectTag(String lang, String sort, Integer ecosystemCode) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse insertDomain(DomainRequest domainRequest) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse deleteDomain(Integer idDomain) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse updateDomain(DomainRequest domainRequest, Integer idDomain) throws BadRequestException, NotFoundException, Exception;
}
