package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.csi.yucca.adminapi.model.MeasureUnit;
import org.csi.yucca.adminapi.util.Constants;

public interface MeasureUnitMapper {
	
	String MEASURE_UNIT_TABLE = Constants.SCHEMA_DB + ".yucca_d_measure_unit";
	
	public static final String SELECT = 
			
			" SELECT id_measure_unit, measureunit, measureunitcategory FROM " + MEASURE_UNIT_TABLE + " " +

			"<if test=\"sortList != null\">" +
				" ORDER BY " +
			
				" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
				
				"<if test=\"propName == 'idMeasureUnit-'\">" +
			        " id_measure_unit desc" +
		        "</if>" +
		        "<if test=\"propName == 'idMeasureUnit'\">" +
		            " id_measure_unit" +
	            "</if>" +			
				
				"<if test=\"propName == 'measureunit-'\">" +
		           " measureunit desc" +
	            "</if>" +
	            "<if test=\"propName == 'measureunit'\">" +
	               " measureunit" +
	            "</if>" +			
				
				"<if test=\"propName == 'measureunitcategory-'\">" +
		           " measureunitcategory desc" +
	            "</if>" +
	            "<if test=\"propName == 'measureunitcategory'\">" +
	               " measureunitcategory" +
	            "</if>" +			
	            "</foreach>" +
            "</if>";
	
	/*************************************************************************
	 * 
	 * 					DELETE MEASURE UNIT
	 * 
	 * ***********************************************************************/
	public static final String DELETE_MEASURE_UNIT = "DELETE FROM " + MEASURE_UNIT_TABLE + " WHERE id_measure_unit=#{idMeasureUnit}";
	@Delete(DELETE_MEASURE_UNIT)
	int deleteMeasureUnit(int idMeasureUnit);	
	
	
	/*************************************************************************
	 * 
	 * 					UPDATE MEASURE UNIT
	 * 
	 * ***********************************************************************/
	public static final String UPDATE_MEASURE_UNIT = 
			"<script>" +
			"UPDATE " + MEASURE_UNIT_TABLE + 
			" SET measureunit=#{measureunit} " +
			"<if test=\"measureunitcategory != null\">" +
				", measureunitcategory=#{measureunitcategory}" +
	        "</if>" + " WHERE id_measure_unit=#{idMeasureUnit}" +
	        "</script>" ;
	@Update(UPDATE_MEASURE_UNIT)
	int updateMeasureUnit(MeasureUnit measureUnit);	
	
	/*************************************************************************
	 * 
	 * 					INSERT MEASURE UNIT
	 * 
	 * ***********************************************************************/
	public static final String INSERT_MEASURE_UNIT = 
			"INSERT INTO " + MEASURE_UNIT_TABLE + "( measureunit, measureunitcategory) VALUES (#{measureunit}, #{measureunitcategory})";
	@Insert(INSERT_MEASURE_UNIT)
	@Options(useGeneratedKeys=true, keyProperty="idMeasureUnit")
	int insertMeasureUnit(MeasureUnit measureUnit);
	

	/*************************************************************************
	 * 
	 * 					select measure unit
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idMeasureUnit", column = "id_measure_unit"),
        @Result(property = "measureunit", column = "measureunit"),
        @Result(property = "measureunitcategory", column = "measureunitcategory")
      })
	@Select({"<script>",
				SELECT,
             "</script>"}) 
	List<MeasureUnit> selectMeasureUnit(@Param("sortList") List<String> sortList);	
	

	/*************************************************************************
	 * 
	 * 					select measure unit by id
	 * 
	 * 
	 * 
	 * ***********************************************************************/
	public static final String SELECT_MEASURE_UNIT_BY_ID = 
			"SELECT id_measure_unit, measureunit, measureunitcategory FROM "
			+ MEASURE_UNIT_TABLE + " WHERE id_measure_unit=#{idMeasureUnit}";
	@Results({
        @Result(property = "idMeasureUnit", column = "id_measure_unit"),
        @Result(property = "measureunit", column = "measureunit"),
        @Result(property = "measureunitcategory", column = "measureunitcategory")
      })
	@Select(SELECT_MEASURE_UNIT_BY_ID) 
	MeasureUnit selectMeasureUnitById(@Param("idMeasureUnit") Integer idMeasureUnit);	
	
}
