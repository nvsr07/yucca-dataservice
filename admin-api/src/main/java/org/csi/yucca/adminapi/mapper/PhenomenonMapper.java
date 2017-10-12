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
import org.csi.yucca.adminapi.model.Phenomenon;
import org.csi.yucca.adminapi.util.Constants;

public interface PhenomenonMapper {
	
	String PHENOMENON_TABLE = Constants.SCHEMA_DB + "yucca_d_phenomenon";
	
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
	 * 					DELETE PHENOMENON
	 * 
	 * ***********************************************************************/
	public static final String DELETE_PHENOMENON = "DELETE FROM " + PHENOMENON_TABLE + " WHERE id_phenomenon=#{id_phenomenon}";
	@Delete(DELETE_PHENOMENON)
	int deletePhenomenon(int idPhenomenon);	

	
	/*************************************************************************
	 * 
	 * 					UPDATE PHENOMENON
	 * 
	 * ***********************************************************************/
	public static final String UPDATE_PHENOMENON = 
			"<script>" +
			"UPDATE " + PHENOMENON_TABLE + 
			" SET phenomenonname=#{phenomenonname} " +
			"<if test=\"phenomenoncetegory != null\">" +
				", phenomenoncetegory=#{phenomenoncetegory}" +
	        "</if>" + " WHERE id_phenomenon=#{idPhenomenon}" +
	        "</script>" ;
	@Update(UPDATE_PHENOMENON)
	int updatePhenomenon(Phenomenon phenomenon);	

	
	/*************************************************************************
	 * 
	 * 					INSERT PHENOMENON
	 * 
	 * ***********************************************************************/
	public static final String INSERT_PHENOMENON = 
			"INSERT INTO " + PHENOMENON_TABLE + "( phenomenonname, phenomenoncetegory) VALUES (#{phenomenonname}, #{phenomenoncetegory})";
	@Insert(INSERT_PHENOMENON)
	@Options(useGeneratedKeys=true, keyProperty="idPhenomenon")
	int insertPhenomenon(Phenomenon phenomenons);

	
	/*************************************************************************
	 * 
	 * 					select phenomenon list
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
	
	/*************************************************************************
	 * 
	 * 					select phenomenon by id
	 * 
	 * ***********************************************************************/
	public static final String SELECT_PHENOMENON_BY_ID =  
			"SELECT id_phenomenon, phenomenonname, phenomenoncetegory FROM " + PHENOMENON_TABLE + " WHERE id_phenomenon=#{idPhenomenon}";
	@Results({
        @Result(property = "idPhenomenon", column = "id_phenomenon"),
        @Result(property = "phenomenonname", column = "phenomenonname"),
        @Result(property = "phenomenoncetegory", column = "phenomenoncetegory")
      })
	@Select(SELECT_PHENOMENON_BY_ID) 
	Phenomenon selectPhenomenonById(@Param("idPhenomenon") Integer idPhenomenon);	
	
}
