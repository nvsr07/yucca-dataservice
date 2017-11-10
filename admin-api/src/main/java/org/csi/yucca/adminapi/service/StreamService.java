package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.request.PostStreamRequest;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface StreamService {
	ServiceResponse createStreamDataset(PostStreamRequest request,String organizationCode, String soCode, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectStreams(String organizationCode, String tenantCodeManager, String sort, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectStream(String organizationCode, Integer idStream, String tenantCodeManager, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectStreamIcon(String organizationCode, Integer idStream, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

}
