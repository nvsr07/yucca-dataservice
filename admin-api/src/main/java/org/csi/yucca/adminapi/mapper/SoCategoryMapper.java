package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.SoCategory;
import org.csi.yucca.adminapi.util.Constants;

public interface SoCategoryMapper {
	
	String SO_CATEGORY_TYPE_TABLE = Constants.SCHEMA_DB + "yucca_d_so_category";
	
	public static final String SELECT = 
			
			" SELECT id_so_category, socategorycode, description FROM " + SO_CATEGORY_TYPE_TABLE + " " +

			"<if test=\"sortList != null\">" +
				" ORDER BY " +
			
				" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
				
				"<if test=\"propName == 'idSoCategory-'\">" +
			        " id_so_category desc" +
		        "</if>" +
		        "<if test=\"propName == 'idSoCategory'\">" +
		            " id_so_category" +
	            "</if>" +			
				
				"<if test=\"propName == 'socategorycode-'\">" +
		           " socategorycode desc" +
	            "</if>" +
	            "<if test=\"propName == 'socategorycode'\">" +
	               " socategorycode" +
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
	 * 					select SO CATEGORY
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idSoCategory", column = "id_so_category"),
        @Result(property = "socategorycode", column = "socategorycode"),
        @Result(property = "description", column = "description")
      })
	@Select({"<script>",
				SELECT,
             "</script>"}) 
	List<SoCategory> selectSoCategory(@Param("sortList") List<String> sortList);	
	
}
