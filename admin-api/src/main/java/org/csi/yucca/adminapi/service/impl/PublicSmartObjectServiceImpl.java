package org.csi.yucca.adminapi.service.impl;

import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.mapper.ExposureTypeMapper;
import org.csi.yucca.adminapi.model.ExposureType;
import org.csi.yucca.adminapi.response.ExposureTypeResponse;
import org.csi.yucca.adminapi.response.Response;
import org.csi.yucca.adminapi.service.PublicSmartObjectService;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class PublicSmartObjectServiceImpl implements PublicSmartObjectService{

	@Autowired
	private ExposureTypeMapper exposureTypeMapper;

	public List<Response> selectExposureType(String sort) throws BadRequestException, NotFoundException, Exception{
		
		List<String> sortList = ServiceUtil.getSortList(sort, ExposureType.class);
		
		List<ExposureType> modelList = exposureTypeMapper.selectExposureType(sortList);
		
		ServiceUtil.checkList(modelList);
		
		return ServiceUtil.getResponseList(modelList, ExposureTypeResponse.class);
		
	}		

}
