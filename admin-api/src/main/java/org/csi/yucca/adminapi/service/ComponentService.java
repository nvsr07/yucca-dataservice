package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.request.DataTypeRequest;
import org.csi.yucca.adminapi.util.ServiceResponse;

public interface ComponentService {
	
	ServiceResponse deleteDataType(Integer idDataType) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse updateDataType(DataTypeRequest dataTypeRequest, Integer idDataType) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse insertDataType(DataTypeRequest dataTypeRequest) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectDataType(String sort) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectMeasureUnit(String sort) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectPhenomenon(String sort) throws BadRequestException, NotFoundException, Exception;
}
