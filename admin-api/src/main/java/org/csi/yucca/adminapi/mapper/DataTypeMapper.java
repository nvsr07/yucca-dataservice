package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import org.csi.yucca.adminapi.model.DataType;

public interface DataTypeMapper {
	
	String DATA_TYPE_TABLE_NAME = "int_yucca.yucca_d_data_type";
	
	/*******************************************************************************/
	
	@Results({
        @Result(property = "idDataType", column = "id_data_type"),
        @Result(property = "dataTypeCode", column = "datatypecode"),
        @Result(property = "description", column = "description")
      })
	@Select("SELECT id_data_type, datatypecode, description "
		  + "from " + DATA_TYPE_TABLE_NAME + " "
		  + "WHERE id_data_type = #{id}")
	DataType selectDataType(int id);
	
	/*******************************************************************************/
	
	@Insert("INSERT into " + DATA_TYPE_TABLE_NAME + 
			"( id_data_type, datatypecode, description ) "
			+ "VALUES(#{idDataType}, #{dataTypeCode}, #{description})")
	void insertDataType(DataType dataType);
	
	/*******************************************************************************/
	
	@Update("UPDATE " + DATA_TYPE_TABLE_NAME + " "
		  + "SET datatypecode = #{dataTypeCode}, description = #{description} "
		  + "WHERE id_data_type = #{idDataType}")
	void updateDataType(DataType dataType);

	
	/*******************************************************************************/
	
	@Delete("DELETE FROM " + DATA_TYPE_TABLE_NAME + " WHERE id_data_type =#{id}")
	void deleteDataType(int id);	
	
}
