package org.csi.yucca.adminapi.service.impl;

import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.mapper.DataTypeMapper;
import org.csi.yucca.adminapi.mapper.MeasureUnitMapper;
import org.csi.yucca.adminapi.model.DataType;
import org.csi.yucca.adminapi.model.MeasureUnit;
import org.csi.yucca.adminapi.response.DataTypeResponse;
import org.csi.yucca.adminapi.response.MeasureUnitResponse;
import org.csi.yucca.adminapi.response.Response;
import org.csi.yucca.adminapi.service.PublicComponentService;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class PublicComponentServiceImpl implements PublicComponentService{

	@Autowired
	private DataTypeMapper dataTypeMapper;

	@Autowired
	private MeasureUnitMapper measureUnitMapper;
	
	/**
	 * 
	 * @param sort
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public List<Response> selectMeasureUnit(String sort) throws BadRequestException, NotFoundException, Exception{
		
		List<String> sortList = ServiceUtil.getSortList(sort, MeasureUnit.class);
		
		List<MeasureUnit> modelList = measureUnitMapper.selectMeasureUnit(sortList);
		
		ServiceUtil.checkList(modelList);
		
		return ServiceUtil.getResponseList(modelList, MeasureUnitResponse.class);
		
	}		
	
	/**
	 * 
	 * @param sort
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public List<Response> selectDataType(String sort) throws BadRequestException, NotFoundException, Exception{
		
		List<String> sortList = ServiceUtil.getSortList(sort, DataType.class);
		
		List<DataType> modelList = dataTypeMapper.selectDataType(sortList);
		
		ServiceUtil.checkList(modelList);
		
		return ServiceUtil.getResponseList(modelList, DataTypeResponse.class);
		
	}		

}
