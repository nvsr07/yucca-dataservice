package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.LocationType;
import org.csi.yucca.adminapi.util.Constants;

public interface LocationTypeMapper {
	
	String LOCATION_TYPE_TABLE = Constants.SCHEMA_DB + "yucca_d_location_type";
	
	public static final String SELECT = 
			
			" SELECT id_location_type, locationtype, description FROM " + LOCATION_TYPE_TABLE + " " +

			"<if test=\"sortList != null\">" +
				" ORDER BY " +
			
				" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
				
				"<if test=\"propName == 'idLocationType-'\">" +
			        " id_location_type desc" +
		        "</if>" +
		        "<if test=\"propName == 'idLocationType'\">" +
		            " id_location_type" +
	            "</if>" +			
				
				"<if test=\"propName == 'locationtype-'\">" +
		           " locationtype desc" +
	            "</if>" +
	            "<if test=\"propName == 'locationtype'\">" +
	               " locationtype" +
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
        @Result(property = "idLocationType", column = "id_location_type"),
        @Result(property = "locationtype", column = "locationtype"),
        @Result(property = "description", column = "description")
      })
	@Select({"<script>",
				SELECT,
             "</script>"}) 
	List<LocationType> selectLocationType(@Param("sortList") List<String> sortList);	
	
}
