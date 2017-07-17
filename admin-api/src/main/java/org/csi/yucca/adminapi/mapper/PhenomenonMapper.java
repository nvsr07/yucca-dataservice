package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.Phenomenon;
import org.csi.yucca.adminapi.util.Constants;

public interface PhenomenonMapper {
	
	String PHENOMENON_TABLE = Constants.SCHEMA_DB + ".yucca_d_phenomenon";
	
	public static final String SELECT = 
			
			" SELECT id_phenomenon, phenomenonname, phenomenoncetegory FROM " + PHENOMENON_TABLE + " " +

			"<if test=\"sortList != null\">" +
				" ORDER BY " +
			
				" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
				
				"<if test=\"propName == 'idPhenomenon-'\">" +
			        " id_phenomenon desc" +
		        "</if>" +
		        "<if test=\"propName == 'idPhenomenon'\">" +
		            " id_phenomenon" +
	            "</if>" +			
				
				"<if test=\"propName == 'phenomenonname-'\">" +
		           " phenomenonname desc" +
	            "</if>" +
	            "<if test=\"propName == 'phenomenonname'\">" +
	               " phenomenonname" +
	            "</if>" +			
				
				"<if test=\"propName == 'phenomenoncetegory-'\">" +
		           " phenomenoncetegory desc" +
	            "</if>" +
	            "<if test=\"propName == 'phenomenoncetegory'\">" +
	               " phenomenoncetegory" +
	            "</if>" +			
	            "</foreach>" +
            "</if>";
	

	/*************************************************************************
	 * 
	 * 					select measure unit
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idPhenomenon", column = "id_phenomenon"),
        @Result(property = "phenomenonname", column = "phenomenonname"),
        @Result(property = "phenomenoncetegory", column = "phenomenoncetegory")
      })
	@Select({"<script>",
				SELECT,
             "</script>"}) 
	List<Phenomenon> selectPhenomenon(@Param("sortList") List<String> sortList);	
	
}
