package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface ApiService {

	ServiceResponse selectBackofficeLastInstalledDettaglioApi(String apiCode) throws BadRequestException, NotFoundException, Exception;

	
	
}
