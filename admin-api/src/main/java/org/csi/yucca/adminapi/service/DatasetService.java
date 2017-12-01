package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.request.PostDatasetRequest;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface DatasetService {

	ServiceResponse selectDatasets(String organizationCode, String tenantCodeManager, String sort, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectDataset(String organizationCode, Integer idDataset, String tenantCodeManager, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse insertDataset(String organizationCode, PostDatasetRequest postDatasetRequest, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception;
	
}
