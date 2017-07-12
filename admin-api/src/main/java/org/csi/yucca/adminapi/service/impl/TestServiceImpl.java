package org.csi.yucca.adminapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.csi.yucca.adminapi.mapper.DataTypeMapper;
import org.csi.yucca.adminapi.model.DataType;
import org.csi.yucca.adminapi.service.TestService;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class TestServiceImpl implements TestService{
	
	@Autowired
	private DataTypeMapper  dataTypeMapper;
	
	public int insertNewDataType(String dataTypeCode, String description) throws Exception {
		DataType dataType = new DataType();
		
		dataType.setIdDataType(14);
		dataType.setDataTypeCode(dataTypeCode);;
		dataType.setDescription(description);
		
		dataTypeMapper.insertDataType(dataType);
		
		return dataType.getIdDataType();
	}

	public void updateDataType(int idDataType, String dataTypeCode, String description) {
		DataType dataType = new DataType(); 
		dataType.setIdDataType(idDataType);
		dataType.setDataTypeCode(dataTypeCode);
		dataType.setDescription(description);
		
		dataTypeMapper.updateDataType(dataType);	
	}

	public void deleteDataType(int idDataType) {
		dataTypeMapper.deleteDataType(idDataType);
	}

	public DataType selectById(int idDataType) {
		return dataTypeMapper.selectDataType(idDataType);
	}
}
