package org.csi.yucca.adminapi.service.impl;

import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.mapper.ExposureTypeMapper;
import org.csi.yucca.adminapi.mapper.LocationTypeMapper;
import org.csi.yucca.adminapi.mapper.SoCategoryMapper;
import org.csi.yucca.adminapi.mapper.SoTypeMapper;
import org.csi.yucca.adminapi.mapper.SupplyTypeMapper;
import org.csi.yucca.adminapi.model.ExposureType;
import org.csi.yucca.adminapi.model.LocationType;
import org.csi.yucca.adminapi.model.SoCategory;
import org.csi.yucca.adminapi.model.SoType;
import org.csi.yucca.adminapi.model.SupplyType;
import org.csi.yucca.adminapi.response.ExposureTypeResponse;
import org.csi.yucca.adminapi.response.LocationTypeResponse;
import org.csi.yucca.adminapi.response.SoCategoryResponse;
import org.csi.yucca.adminapi.response.SoTypeResponse;
import org.csi.yucca.adminapi.response.SupplyTypeResponse;
import org.csi.yucca.adminapi.service.SmartObjectService;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SmartObjectServiceImpl implements SmartObjectService{

	@Autowired
	private ExposureTypeMapper exposureTypeMapper;

	@Autowired
	private LocationTypeMapper locationTypeMapper;
	
	@Autowired
	private SoCategoryMapper soCategoryMapper;

	@Autowired
	private SoTypeMapper soTypeMapper;

	@Autowired
	private SupplyTypeMapper supplyTypeMapper;

	public ServiceResponse selectSupplyType(String sort) throws BadRequestException, NotFoundException, Exception{
		
		List<String> sortList = ServiceUtil.getSortList(sort, SupplyType.class);
		
		List<SupplyType> modelList = supplyTypeMapper.selectSupplyType(sortList);
		
		ServiceUtil.checkList(modelList);
		
//		return ServiceUtil.getResponseList(modelList, SupplyTypeResponse.class);
		
		return ServiceResponse.build().object(ServiceUtil.getResponseList(modelList, SupplyTypeResponse.class));
		
	}			
	
	public ServiceResponse selectSoType(String sort) throws BadRequestException, NotFoundException, Exception{
		
		List<String> sortList = ServiceUtil.getSortList(sort, SoType.class);
		
		List<SoType> modelList = soTypeMapper.selectSoType(sortList);
		
		ServiceUtil.checkList(modelList);
		
//		return ServiceUtil.getResponseList(modelList, SoTypeResponse.class);
		return ServiceResponse.build().object(ServiceUtil.getResponseList(modelList, SoTypeResponse.class));
	}			

	public ServiceResponse selectSoCategory(String sort) throws BadRequestException, NotFoundException, Exception{
		
		List<String> sortList = ServiceUtil.getSortList(sort, SoCategory.class);
		
		List<SoCategory> modelList = soCategoryMapper.selectSoCategory(sortList);
		
		ServiceUtil.checkList(modelList);
		
//		return ServiceUtil.getResponseList(modelList, SoCategoryResponse.class);
		return ServiceResponse.build().object(ServiceUtil.getResponseList(modelList, SoCategoryResponse.class));
	}			
	
	public ServiceResponse selectExposureType(String sort) throws BadRequestException, NotFoundException, Exception{
		
		List<String> sortList = ServiceUtil.getSortList(sort, ExposureType.class);
		
		List<ExposureType> modelList = exposureTypeMapper.selectExposureType(sortList);
		
		ServiceUtil.checkList(modelList);
		
//		return ServiceUtil.getResponseList(modelList, ExposureTypeResponse.class);
		return ServiceResponse.build().object(ServiceUtil.getResponseList(modelList, ExposureTypeResponse.class));
		
	}		
	
	public ServiceResponse selectLocationType(String sort) throws BadRequestException, NotFoundException, Exception{
		
		List<String> sortList = ServiceUtil.getSortList(sort, LocationType.class);
		
		List<LocationType> modelList = locationTypeMapper.selectLocationType(sortList);
		
		ServiceUtil.checkList(modelList);
		
//		return ServiceUtil.getResponseList(modelList, LocationTypeResponse.class);
		return ServiceResponse.build().object(ServiceUtil.getResponseList(modelList, LocationTypeResponse.class));
		
	}			
	

}
