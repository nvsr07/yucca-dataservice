package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
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
	
}
