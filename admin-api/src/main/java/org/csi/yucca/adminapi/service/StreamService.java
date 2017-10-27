package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.request.PostStreamRequest;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface StreamService {
	ServiceResponse createStreamDataset(PostStreamRequest request,String organizationCode, String soCode, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;
}
