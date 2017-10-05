package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.request.ActionOnTenantRequest;
import org.csi.yucca.adminapi.request.TenantRequest;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface TenantService {

	ServiceResponse insertTenant(TenantRequest tenantRequest) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse deleteTenant(String tenantcode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse actionOnTenant(ActionOnTenantRequest actionOnTenantRequest, String tenantCode) throws BadRequestException, NotFoundException, Exception;
}
