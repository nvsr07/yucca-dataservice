package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.SoType;
import org.csi.yucca.adminapi.util.Constants;

public interface SoTypeMapper {
	
	String SO_TYPE_TABLE = Constants.SCHEMA_DB + "yucca_d_so_type";
	
	public static final String SELECT = 
			
			" SELECT id_so_type, sotypecode, description FROM " + SO_TYPE_TABLE + " " +

			"<if test=\"sortList != null\">" +
				" ORDER BY " +
			
				" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
				
				"<if test=\"propName == 'idSoType-'\">" +
			        " id_so_type desc" +
		        "</if>" +
		        "<if test=\"propName == 'idSoType'\">" +
		            " id_so_type" +
	            "</if>" +			
				
				"<if test=\"propName == 'sotypecode-'\">" +
		           " sotypecode desc" +
	            "</if>" +
	            "<if test=\"propName == 'sotypecode'\">" +
	               " sotypecode" +
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
	 * 					select so TYPE
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idSoType", column = "id_so_type"),
        @Result(property = "sotypecode", column = "sotypecode"),
        @Result(property = "description", column = "description")
      })
	@Select({"<script>",
				SELECT,
             "</script>"}) 
	List<SoType> selectSoType(@Param("sortList") List<String> sortList);	
	
}
