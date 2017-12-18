package org.csi.yucca.adminapi.service.impl;

import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.ConflictException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.mapper.DataTypeMapper;
import org.csi.yucca.adminapi.mapper.MeasureUnitMapper;
import org.csi.yucca.adminapi.mapper.PhenomenonMapper;
import org.csi.yucca.adminapi.model.DataType;
import org.csi.yucca.adminapi.model.MeasureUnit;
import org.csi.yucca.adminapi.model.Phenomenon;
import org.csi.yucca.adminapi.request.DataTypeRequest;
import org.csi.yucca.adminapi.request.MeasureUnitRequest;
import org.csi.yucca.adminapi.request.PhenomenonRequest;
import org.csi.yucca.adminapi.response.DataTypeResponse;
import org.csi.yucca.adminapi.response.MeasureUnitResponse;
import org.csi.yucca.adminapi.response.PhenomenonResponse;
import org.csi.yucca.adminapi.service.ComponentService;
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
public class ComponentServiceImpl implements ComponentService{

	@Autowired
	private DataTypeMapper dataTypeMapper;

	@Autowired
	private MeasureUnitMapper measureUnitMapper;
	
	@Autowired
	private PhenomenonMapper phenomenonMapper;

	/**
	 * SELECT DATA TYPE
	 * 
	 * @param idDataType
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse selectDataType(Integer idDataType) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(idDataType, "idDataType");

		DataType dataType = dataTypeMapper.selectDataTypeById(idDataType);
		
		ServiceUtil.checkIfFoundRecord(dataType);
		
		return ServiceResponse.build().object(new DataTypeResponse(dataType));
	}

	
	/**
	 * 
	 * SELECT MEASUE UNIT
	 * 
	 * @param idMeasureUnit
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse selectMeasureUnit(Integer idMeasureUnit) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(idMeasureUnit, "idMeasureUnit");

		
		MeasureUnit measureUnit = measureUnitMapper.selectMeasureUnitById(idMeasureUnit);
		
		ServiceUtil.checkIfFoundRecord(measureUnit);
		
		return ServiceResponse.build().object(new MeasureUnitResponse(measureUnit));
	}

	
	/**
	 * 
	 * SELECT PHENOMENON
	 * 
	 * @param idPhenomenon
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse selectPhenomenon(Integer idPhenomenon) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(idPhenomenon, "idPhenomenon");

		Phenomenon phenomenon = phenomenonMapper.selectPhenomenonById(idPhenomenon);
		
		ServiceUtil.checkIfFoundRecord(phenomenon);
		
		return ServiceResponse.build().object(new PhenomenonResponse(phenomenon));
	}

	
	/**
	 * 
	 * DELETE PHENOMENON
	 * 
	 * @param idPhenomenon
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse deletePhenomenon(Integer idPhenomenon) throws BadRequestException, NotFoundException, Exception{
		ServiceUtil.checkMandatoryParameter(idPhenomenon, "idPhenomenon");
	
		int count = 0;
		try {
			count = phenomenonMapper.deletePhenomenon(idPhenomenon);
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
	 * UPDATE PHENOMENON
	 */
	public ServiceResponse updatePhenomenon(PhenomenonRequest phenomenonRequest, Integer idPhenomenon) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(phenomenonRequest, 			           "phenomenonRequest");
		ServiceUtil.checkMandatoryParameter(phenomenonRequest.getPhenomenonname(), "phenomenonname");
		ServiceUtil.checkMandatoryParameter(idPhenomenon,                           "idPhenomenon");
		
		Phenomenon phenomenon = new Phenomenon(idPhenomenon, phenomenonRequest.getPhenomenonname(), phenomenonRequest.getPhenomenoncetegory()); 
		
		int count = phenomenonMapper.updatePhenomenon(phenomenon);
		
		ServiceUtil.checkCount(count);
		
		if(phenomenonRequest.getPhenomenoncetegory() == null){
			phenomenon = phenomenonMapper.selectPhenomenonById(idPhenomenon);
		}
		
		return ServiceResponse.build().object(new PhenomenonResponse(phenomenon));
	}


	
	
	/**
	 * 
	 * INSERT PHENOMENON
	 * 
	 * @param phenomenonRequest
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse insertPhenomenon(PhenomenonRequest phenomenonRequest) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(phenomenonRequest, "phenomenonRequest");
		
		ServiceUtil.checkMandatoryParameter(phenomenonRequest.getPhenomenonname(), "phenomenonname"); 
		
		Phenomenon phenomenon = new Phenomenon();
		
		BeanUtils.copyProperties(phenomenonRequest, phenomenon);

		insertPhenomenon(phenomenon);
		
		return ServiceResponse.build().object(new PhenomenonResponse(phenomenon));
	}

	private void insertPhenomenon(Phenomenon phenomenon)throws BadRequestException{
		
		try {
			phenomenonMapper.insertPhenomenon(phenomenon);
		} 
		catch (DuplicateKeyException duplicateKeyException) {
			throw new BadRequestException(Errors.DUPLICATE_KEY);
		}
	}


	
	/**
	 * DELETE DATA TYPE
	 */
	public ServiceResponse deleteMeasureUnit(Integer idMeasureUnit) throws BadRequestException, NotFoundException, Exception{
		ServiceUtil.checkMandatoryParameter(idMeasureUnit, "idMeasureUnit");
	
		int count = 0;
		try {
			count = measureUnitMapper.deleteMeasureUnit(idMeasureUnit);
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
	 * 
	 * UPDATE MEASURE UNIT
	 * 
	 * @param measureUnitRequest
	 * @param idMeasureUnit
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse updateMeasureUnit(MeasureUnitRequest measureUnitRequest, Integer idMeasureUnit) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(measureUnitRequest, 			     "measureUnitRequest");
		ServiceUtil.checkMandatoryParameter(measureUnitRequest.getMeasureunit(), "measureunit");
		ServiceUtil.checkMandatoryParameter(idMeasureUnit,                       "idMeasureUnit");
		
		MeasureUnit measureUnit = new MeasureUnit(idMeasureUnit, measureUnitRequest.getMeasureunit(), measureUnitRequest.getMeasureunitcategory());
		
		int count = measureUnitMapper.updateMeasureUnit(measureUnit);
		
		ServiceUtil.checkCount(count);
		
		if(measureUnit.getMeasureunitcategory() == null){
			measureUnit = measureUnitMapper.selectMeasureUnitById(idMeasureUnit);
		}
		
		return ServiceResponse.build().object(new MeasureUnitResponse(measureUnit));
	}

	
	/**
	 * 
	 * INSERT DATA TYPE
	 * 
	 * @param dataTypeRequest
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse insertMeasureUnit(MeasureUnitRequest measureUnitRequest) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(measureUnitRequest, "measureUnitRequest");
		
		ServiceUtil.checkMandatoryParameter(measureUnitRequest.getMeasureunit(), "measureunit"); 

		MeasureUnit measureUnit = new MeasureUnit();
		BeanUtils.copyProperties(measureUnitRequest, measureUnit);

		insertMeasureUnit(measureUnit);
		
		return ServiceResponse.build().object(new MeasureUnitResponse(measureUnit));
	}

	private void insertMeasureUnit(MeasureUnit measureUnit)throws BadRequestException{
		
		try {
			measureUnitMapper.insertMeasureUnit(measureUnit);
		} 
		catch (DuplicateKeyException duplicateKeyException) {
			throw new BadRequestException(Errors.DUPLICATE_KEY);
		}
	}
	
	
	/**
	 * DELETE DATA TYPE
	 */
	public ServiceResponse deleteDataType(Integer idDataType) throws BadRequestException, NotFoundException, Exception{
		ServiceUtil.checkMandatoryParameter(idDataType, "idDataType");
	
		int count = 0;
		try {
			count = dataTypeMapper.deleteDataType(idDataType);
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
	 * UPDATE DATA TYPE
	 * 
	 * @param dataTypeRequest
	 * @param idDataType
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse updateDataType(DataTypeRequest dataTypeRequest, Integer idDataType) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(dataTypeRequest, 			       "dataTypeRequest");
		ServiceUtil.checkMandatoryParameter(dataTypeRequest.getDatatypecode(), "datatypecode");
		ServiceUtil.checkMandatoryParameter(idDataType,                        "idDataType");
		
		DataType dataType = new DataType(idDataType, dataTypeRequest.getDatatypecode(), dataTypeRequest.getDescription());
		
		int count = dataTypeMapper.updateDataType(dataType);
		
		ServiceUtil.checkCount(count);
		
		return ServiceResponse.build().object(new DataTypeResponse(dataType));
	}

	
	/**
	 * 
	 * INSERT DATA TYPE
	 * 
	 * @param dataTypeRequest
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse insertDataType(DataTypeRequest dataTypeRequest) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(dataTypeRequest, "tagRequest");
		
//		ServiceUtil.checkMandatoryParameter(dataTypeRequest.getDatatypecode(), "datatypecode"); 
		ServiceUtil.checkCode(dataTypeRequest.getDatatypecode(), "datatypecode"); 

		DataType dataType = new DataType();
		BeanUtils.copyProperties(dataTypeRequest, dataType);

		insertDataType(dataType);
		
		return ServiceResponse.build().object(new DataTypeResponse(dataType));
	}

	private void insertDataType(DataType dataType)throws BadRequestException{
		
		try {
			dataTypeMapper.insertDataType(dataType);
		} 
		catch (DuplicateKeyException duplicateKeyException) {
			throw new BadRequestException(Errors.DUPLICATE_KEY);
		}

	}

	
	
	/**
	 * 
	 * @param sort
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse selectPhenomenon(String sort) throws BadRequestException, NotFoundException, Exception{
		
		List<String> sortList = ServiceUtil.getSortList(sort, Phenomenon.class);
		
		List<Phenomenon> modelList = phenomenonMapper.selectPhenomenon(sortList);
		
		ServiceUtil.checkList(modelList);
		
//		return ServiceUtil.getResponseList(modelList, PhenomenonResponse.class);
		return ServiceResponse.build().object(ServiceUtil.getResponseList(modelList, PhenomenonResponse.class));
		
	}		
	
	
	/**
	 * 
	 * @param sort
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse selectMeasureUnit(String sort) throws BadRequestException, NotFoundException, Exception{
		
		List<String> sortList = ServiceUtil.getSortList(sort, MeasureUnit.class);
		
		List<MeasureUnit> modelList = measureUnitMapper.selectMeasureUnit(sortList);
		
		ServiceUtil.checkList(modelList);
		
//		return ServiceUtil.getResponseList(modelList, MeasureUnitResponse.class);
		return ServiceResponse.build().object(ServiceUtil.getResponseList(modelList, MeasureUnitResponse.class));
	}		
	
	/**
	 * 
	 * @param sort
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse selectDataType(String sort) throws BadRequestException, NotFoundException, Exception{
		
		List<String> sortList = ServiceUtil.getSortList(sort, DataType.class);
		
		List<DataType> modelList = dataTypeMapper.selectDataType(sortList);
		
		ServiceUtil.checkList(modelList);
		
//		return ServiceUtil.getResponseList(modelList, DataTypeResponse.class);
		return ServiceResponse.build().object(ServiceUtil.getResponseList(modelList, DataTypeResponse.class));
	}		

}
