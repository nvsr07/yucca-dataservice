package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.request.ActionRequest;
import org.csi.yucca.adminapi.request.ActionfeedbackOnTenantRequest;
import org.csi.yucca.adminapi.request.PostTenantRequest;
import org.csi.yucca.adminapi.request.PostTenantSocialRequest;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface TenantService {

	ServiceResponse insertTenantSocial(PostTenantSocialRequest request) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse insertTenant(PostTenantRequest tenantRequest) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse deleteTenant(String tenantcode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse actionOnTenant(ActionRequest actionOnTenantRequest, String tenantcode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse actionfeedbackOnTenant(ActionfeedbackOnTenantRequest actionfeedbackOnTenantRequest, String tenantcode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse addAdminApplication(String tenantcode, String username, String password);

	ServiceResponse subscribeAdminApiInStore(String tenantcode, String username, String password);

	ServiceResponse generetateAdminKey(String tenantcode, String username, String password);

	ServiceResponse selectTenants(String sort) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectTenant(String tenantcode) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectTenantTypes() throws BadRequestException, NotFoundException, Exception;

}
