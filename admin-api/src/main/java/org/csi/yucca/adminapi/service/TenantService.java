package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.request.ActionOnTenantRequest;
import org.csi.yucca.adminapi.request.PostTenantRequest;
import org.csi.yucca.adminapi.request.PostTenantSocialRequest;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface TenantService {

	ServiceResponse insertTenantSocial(PostTenantSocialRequest request) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse insertTenant(PostTenantRequest tenantRequest) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse deleteTenant(String tenantcode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse actionOnTenant(ActionOnTenantRequest actionOnTenantRequest, String tenantCode) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectTenants(String sort) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectTenant(String tenantcode) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectTenantTypes() throws BadRequestException, NotFoundException, Exception;
}
