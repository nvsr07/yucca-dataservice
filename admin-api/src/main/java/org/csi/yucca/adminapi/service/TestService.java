package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.model.DataType;

public interface TestService {
	
	int insertNewDataType(String dataTypeCode, String description)throws Exception;
	
	void updateDataType(int idDataType, String dataTypeCode, String description);
	
	void deleteDataType(int idDataType);
	
	DataType selectById(int idDataType);
	
}
