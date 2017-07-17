package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.SupplyType;
import org.csi.yucca.adminapi.util.Constants;

public interface SupplyTypeMapper {
	
	String SUPPLY_TYPE_TABLE = Constants.SCHEMA_DB + ".yucca_d_supply_type";
	
	public static final String SELECT = 
			
			" SELECT id_supply_type, supplytype, description FROM " + SUPPLY_TYPE_TABLE + " " +

			"<if test=\"sortList != null\">" +
				" ORDER BY " +
			
				" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
				
				"<if test=\"propName == 'idSupplyType-'\">" +
			        " id_supply_type desc" +
		        "</if>" +
		        "<if test=\"propName == 'idSupplyType'\">" +
		            " id_supply_type" +
	            "</if>" +			
				
				"<if test=\"propName == 'supplytype-'\">" +
		           " supplytype desc" +
	            "</if>" +
	            "<if test=\"propName == 'supplytype'\">" +
	               " supplytype" +
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
        @Result(property = "idSupplyType", column = "id_supply_type"),
        @Result(property = "supplytype", column = "supplytype"),
        @Result(property = "description", column = "description")
      })
	@Select({"<script>",
				SELECT,
             "</script>"}) 
	List<SupplyType> selectSupplyType(@Param("sortList") List<String> sortList);	
	
}
