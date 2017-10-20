package org.csi.yucca.adminapi.service;

import java.sql.Timestamp;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.exception.UnauthorizedException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.model.Smartobject;
import org.csi.yucca.adminapi.request.SmartobjectRequest;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface SmartObjectService {

	ServiceResponse selectSmartObject(String organizationCode, String socode, JwtUser authorizedUser) throws BadRequestException, NotFoundException,UnauthorizedException, Exception;
	
	ServiceResponse selectSmartObjects(String organizationCode, String tenantCode, JwtUser authorizedUser) throws BadRequestException, NotFoundException,UnauthorizedException, Exception;
	
	Smartobject selectSmartObjectByOrganizationAndSoType(Integer idOrganization, Integer idSoType);
	
	ServiceResponse updateSmartobject(SmartobjectRequest smartobjectRequest, String organizationCode, String soCode) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse deleteSmartObject(String organizationCode, String socode) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectExposureType(String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectSoCategory(String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectLocationType(String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectSoType(String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectSupplyType(String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse insertSmartobject(SmartobjectRequest smartobjectRequest, String organizationCode) throws BadRequestException, NotFoundException, Exception;
	
	Smartobject insertSmartObject(SmartobjectRequest smartobjectRequest, Organization organization)throws BadRequestException;
	
	Smartobject insertInternalSmartObject(Organization organization)throws BadRequestException;
	
	void deleteInternalSmartObject(Integer idOrganization) throws  Exception;
	
	void insertManagerTenantSmartobject(Integer idTenant, Integer idSmartobject, Timestamp now);

	void insertNotManagerTenantSmartobject(Integer idTenant, Integer idSmartobject, Timestamp now);
	
}
