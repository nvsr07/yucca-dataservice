package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.DataType;
import org.csi.yucca.adminapi.util.Constants;

public interface DataTypeMapper {

	String DATA_TYPE_TABLE = Constants.SCHEMA_DB + "yucca_d_data_type";

	public static final String SELECT =

			" SELECT id_data_type, datatypecode, description FROM " + DATA_TYPE_TABLE + " " +

					"<if test=\"sortList != null\">" + " ORDER BY " +

					" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +

					"<if test=\"propName == 'idDataType-'\">" + " id_data_type desc" + "</if>"
					+ "<if test=\"propName == 'idDataType'\">" + " id_data_type" + "</if>" +

					"<if test=\"propName == 'datatypecode-'\">" + " datatypecode desc" + "</if>"
					+ "<if test=\"propName == 'datatypecode'\">" + " datatypecode" + "</if>" +

					"<if test=\"propName == 'description-'\">" + " description desc" + "</if>"
					+ "<if test=\"propName == 'description'\">" + " description" + "</if>" + "</foreach>" + "</if>";

	/*************************************************************************
	 * 
	 * DELETE DATA TYPE
	 * 
	 ***********************************************************************/
	public static final String DELETE_DATA_TYPE = "DELETE FROM " + DATA_TYPE_TABLE
			+ " WHERE id_data_type=#{idDataType}";

	@Delete(DELETE_DATA_TYPE)
	int deleteDataType(int idDataType);

	/*************************************************************************
	 * 
	 * UPDATE DATA TYPE
	 * 
	 * 
	 ***********************************************************************/
	public static final String UPDATE_DATA_TYPE = "<script>" + "UPDATE " + DATA_TYPE_TABLE
			+ " SET datatypecode=#{datatypecode} " + "<if test=\"description != null\">"
			+ ", description=#{description}" + "</if>" + " WHERE id_data_type=#{idDataType}" + "</script>";

	@Delete(UPDATE_DATA_TYPE)
	int updateDataType(DataType dataType);

	/*************************************************************************
	 * 
	 * INSERT DATA TYPE
	 * 
	 ***********************************************************************/
	public static final String INSERT_DATA_TYPE = "INSERT INTO " + DATA_TYPE_TABLE
			+ "( datatypecode, description) VALUES (#{datatypecode}, #{description})";

	@Insert(INSERT_DATA_TYPE)
	@Options(useGeneratedKeys = true, keyProperty = "idDataType")
	int insertDataType(DataType dataType);

	/*************************************************************************
	 * 
	 * select ORGANIZATIONS
	 * 
	 ***********************************************************************/
	@Results({ @Result(property = "idDataType", column = "id_data_type"),
			@Result(property = "datatypecode", column = "datatypecode"),
			@Result(property = "description", column = "description") })
	@Select({ "<script>", SELECT, "</script>" })
	List<DataType> selectDataType(@Param("sortList") List<String> sortList);

	
	/**
	 * 
	 * SELECT DATA TYPE BY ID 
	 * 
	 * @param idDataType
	 * @return
	 */
	@Results({ @Result(property = "idDataType", column = "id_data_type"),
		       @Result(property = "datatypecode", column = "datatypecode"),
		       @Result(property = "description", column = "description") })	
	@Select("SELECT id_data_type, datatypecode, description " + "from " + DATA_TYPE_TABLE + " "
			+ "WHERE id_data_type = #{idDataType}")
	DataType selectDataTypeById(int idDataType);

	
	// String DATA_TYPE_TABLE_NAME = "int_yucca.yucca_d_data_type";
	//
	// /*******************************************************************************/
	//
	// @Results({
	// @Result(property = "idDataType", column = "id_data_type"),
	// @Result(property = "dataTypeCode", column = "datatypecode"),
	// @Result(property = "description", column = "description")
	// })
	// @Select("SELECT id_data_type, datatypecode, description "
	// + "from " + DATA_TYPE_TABLE_NAME + " "
	// + "WHERE id_data_type = #{id}")
	// DataType selectDataType(int id);
	//
	// /*******************************************************************************/
	//
	// @Insert("INSERT into " + DATA_TYPE_TABLE_NAME +
	// "( id_data_type, datatypecode, description ) "
	// + "VALUES(#{idDataType}, #{dataTypeCode}, #{description})")
	// void insertDataType(DataType dataType);
	//
	// /*******************************************************************************/
	//
	// @Update("UPDATE " + DATA_TYPE_TABLE_NAME + " "
	// + "SET datatypecode = #{dataTypeCode}, description = #{description} "
	// + "WHERE id_data_type = #{idDataType}")
	// void updateDataType(DataType dataType);
	//
	//
	// /*******************************************************************************/
	//
	// @Delete("DELETE FROM " + DATA_TYPE_TABLE_NAME + " WHERE id_data_type
	// =#{id}")
	// void deleteDataType(int id);

}
