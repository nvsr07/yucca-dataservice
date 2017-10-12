package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.ExposureType;
import org.csi.yucca.adminapi.util.Constants;

public interface ExposureTypeMapper {
	
	String EXPOSURE_TYPE_TABLE = Constants.SCHEMA_DB + "yucca_d_exposure_type";
	
	public static final String SELECT = 
			
			" SELECT id_exposure_type, exposuretype, description FROM " + EXPOSURE_TYPE_TABLE + " " +

			"<if test=\"sortList != null\">" +
				" ORDER BY " +
			
				" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
				
				"<if test=\"propName == 'idExposureType-'\">" +
			        " id_exposure_type desc" +
		        "</if>" +
		        "<if test=\"propName == 'idExposureType'\">" +
		            " id_exposure_type" +
	            "</if>" +			
				
				"<if test=\"propName == 'exposuretype-'\">" +
		           " exposuretype desc" +
	            "</if>" +
	            "<if test=\"propName == 'exposuretype'\">" +
	               " exposuretype" +
	            "</if>" +			
				
				"<if test=\"propName == 'description-'\">" +
		           " description desc" +
	            "</if>" +
	            "<if test=\"propName == 'description'\">" +
	               " description" +
	            "</if>" +			
	            "</foreach>" +
            "</if>";
	

	/*************************************************************************
	 * 
	 * 					select EXPOSURE TYPE
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idExposureType", column = "id_exposure_type"),
        @Result(property = "exposuretype", column = "exposuretype"),
        @Result(property = "description", column = "description")
      })
	@Select({"<script>",
				SELECT,
             "</script>"}) 
	List<ExposureType> selectExposureType(@Param("sortList") List<String> sortList);	
	
}
