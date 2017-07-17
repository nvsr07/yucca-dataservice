package org.csi.yucca.adminapi.service.impl;

import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.mapper.DatasetSubtypeMapper;
import org.csi.yucca.adminapi.mapper.DatasetTypeMapper;
import org.csi.yucca.adminapi.model.DatasetSubtype;
import org.csi.yucca.adminapi.model.DatasetType;
import org.csi.yucca.adminapi.model.ExposureType;
import org.csi.yucca.adminapi.response.DatasetSubtypeResponse;
import org.csi.yucca.adminapi.response.DatasetTypeResponse;
import org.csi.yucca.adminapi.response.ExposureTypeResponse;
import org.csi.yucca.adminapi.response.Response;
import org.csi.yucca.adminapi.service.PublicTechnicalService;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class PublicTechnicalServiceImpl implements PublicTechnicalService{

	@Autowired
	private DatasetTypeMapper datasetTypeMapper;

	@Autowired
	private DatasetSubtypeMapper datasetSubtypeMapper;
	
	public List<Response> selectDatasetType(String sort) throws BadRequestException, NotFoundException, Exception{
		
		List<String> sortList = ServiceUtil.getSortList(sort, DatasetType.class);
		
		List<DatasetType> modelList = datasetTypeMapper.selectDatasetType(sortList);
		
		ServiceUtil.checkList(modelList);
		
		return ServiceUtil.getResponseList(modelList, DatasetTypeResponse.class);
		
	}		
	
	public List<Response> selectDatasetSubtype(Integer datasetTypeCode, String sort) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(datasetTypeCode, "datasetTypeCode");
		
		List<String> sortList = ServiceUtil.getSortList(sort, DatasetSubtype.class);
		
		List<DatasetSubtype> modelList = datasetSubtypeMapper.selectDatasetSubtype(datasetTypeCode, sortList);
		
		ServiceUtil.checkList(modelList);
		
		return ServiceUtil.getResponseList(modelList, DatasetSubtypeResponse.class);
		
	}		

}
