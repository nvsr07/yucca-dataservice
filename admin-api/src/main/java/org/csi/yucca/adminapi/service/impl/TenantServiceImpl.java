package org.csi.yucca.adminapi.service.impl;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.ConflictException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.mapper.TenantMapper;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.request.TenantRequest;
import org.csi.yucca.adminapi.response.TenantResponse;
import org.csi.yucca.adminapi.service.TenantService;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class TenantServiceImpl implements TenantService {

	@Autowired
	private TenantMapper tenantMapper;

	/**
	 * DELETE TENANT
	 */
	public ServiceResponse deleteTenant(String tenantcode) throws BadRequestException, NotFoundException, Exception{
		ServiceUtil.checkMandatoryParameter(tenantcode, "tenantcode");
	
		int count = 0;
		try {
			count = tenantMapper.deleteTenant(tenantcode);
		} 		
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new ConflictException(Errors.INTEGRITY_VIOLATION, "Not possible to delete, dependency problems.");
		}
		
		if (count == 0 ) {
			throw new BadRequestException(Errors.RECORD_NOT_FOUND);
		}
		
		return ServiceResponse.build().NO_CONTENT();
		
	}
	
	/**
	 * INSERT TENANT
	 */
	public ServiceResponse insertTenant(TenantRequest tenantRequest) throws BadRequestException, NotFoundException, Exception {

		ServiceUtil.checkMandatoryParameter(tenantRequest, "tenantRequest");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getTenantcode(), "tenantcode");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getName(), "name");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getMaxdatasetnum(), "maxdatasetnum");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getMaxstreamsnum(), "maxstreamsnum");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUsagedaysnumber(), "usagedaysnumber");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUsername(), "username");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUserfirstname(), "userfirstname");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUserlastname(), "userlastname");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUseremail(), "useremail");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUsertypeauth(), "usertypeauth");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getIdOrganization(), "idOrganization");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getIdTenantType(), "idTenantType");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getIdTenantStatus(), "idTenantStatus");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getIdEcosystem(), "idEcosystem");
		ServiceUtil.checkCode(tenantRequest.getTenantcode(), "tenantcode");

		Tenant tenant = new Tenant();
		BeanUtils.copyProperties(tenantRequest, tenant);

		insertTenant(tenant);

		return ServiceResponse.build().object(new TenantResponse(tenant));
	}
	
	private void insertTenant(Tenant tenant)throws BadRequestException{
		
		try {
			tenantMapper.insertTenant(tenant);
		} 
		catch (DuplicateKeyException duplicateKeyException) {
			throw new BadRequestException(Errors.DUPLICATE_KEY, "tenantcode");
		}
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new BadRequestException(Errors.INTEGRITY_VIOLATION, dataIntegrityViolationException.getRootCause().toString());
		}
		
	}
	
	
	
	
}
