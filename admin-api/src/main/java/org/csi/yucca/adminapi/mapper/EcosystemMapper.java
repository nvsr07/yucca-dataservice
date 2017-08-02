package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.Ecosystem;
import org.csi.yucca.adminapi.util.Constants;

/**
 * @author gianfranco.stolfa
 *
 */
public interface EcosystemMapper {
	
	String ECOSYSTEM_TABLE = Constants.SCHEMA_DB + ".yucca_ecosystem";
	String R_ECOSYSTEM_ORGANIZATION_TABLE = Constants.SCHEMA_DB + ".yucca_r_ecosystem_organization";
	
	public static final String SELECT_BY_ORGANIZATION_CODE = 
			"SELECT ECOSYSTEM.id_ecosystem, ECOSYSTEM.ecosystemcode, ECOSYSTEM.description "
			+ "FROM " + ECOSYSTEM_TABLE + " ECOSYSTEM, " + R_ECOSYSTEM_ORGANIZATION_TABLE + " ECOS_ORG "
			+ "WHERE ECOS_ORG.id_organization = #{organizationCode} AND "
			+ "ECOS_ORG.id_ecosystem = ECOSYSTEM.id_ecosystem "; 

	public static final String SELECT_ALL = 
			"SELECT id_ecosystem, ecosystemcode, description "
			+ "FROM " + ECOSYSTEM_TABLE + " "; 
	
	public static final String ORDER_BY =  
			"<if test=\"sortList != null\">" +
				" ORDER BY " +
			
				" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
				
				"<if test=\"propName == 'idEcosystem-'\">" +
			        " id_ecosystem desc" +
		        "</if>" +
		        "<if test=\"propName == 'idEcosystem'\">" +
		            " id_ecosystem" +
	            "</if>" +			
				
				"<if test=\"propName == 'ecosystemcode-'\">" +
		           " ecosystemcode desc" +
	            "</if>" +
	            "<if test=\"propName == 'ecosystemcode'\">" +
	               " ecosystemcode" +
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
	 * 					select ecosystem by id
	 * 
	 * ***********************************************************************/
	public static final String SELECT_ECOSYSTEM_BY_ID = "SELECT id_ecosystem, ecosystemcode, description FROM " + ECOSYSTEM_TABLE + " where id_ecosystem=#{idEcosystem}";
	@Results({
        @Result(property = "idEcosystem", column = "id_ecosystem"),
        @Result(property = "ecosystemcode", column = "ecosystemcode"),
        @Result(property = "description", column = "description")
      })
	@Select(SELECT_ECOSYSTEM_BY_ID) 
	Ecosystem selectEcosystem(@Param("idEcosystem") int idEcosystem);
	
	/*************************************************************************
	 * 
	 * 					DELETE ECOSYSTEM
	 * 
	 * ***********************************************************************/
	public static final String DELETE_ECOSYSTEM = "DELETE FROM " + ECOSYSTEM_TABLE + " WHERE id_ecosystem=#{idEcosystem}";
	@Delete(DELETE_ECOSYSTEM)
	int deleteEcosystem(int idEcosystem);	
	
	/*************************************************************************
	 * 
	 * 					INSERT ECOSYSTEM
	 * 
	 * ***********************************************************************/
	public static final String INSERT_ECOSYSTEM = "INSERT INTO " + ECOSYSTEM_TABLE
			+ "(ecosystemcode, description) VALUES (#{ecosystemcode}, #{description})";
	@Insert(INSERT_ECOSYSTEM)
	@Options(useGeneratedKeys=true, keyProperty="idEcosystem")
	int insertEcosystem(Ecosystem ecosystem);
	
	/*************************************************************************
	 * 
	 * 					UPDATE ECOSYSTEM
	 * 
	 * ***********************************************************************/
	public static final String UPDATE_ECOSYSTEM = 
			"UPDATE " + ECOSYSTEM_TABLE + 
			" SET ecosystemcode=#{ecosystemcode}, description=#{description} WHERE id_ecosystem=#{idEcosystem}";
	@Delete(UPDATE_ECOSYSTEM)
	int updateEcosystem(Ecosystem ecosystem);	
	
	/*************************************************************************
	 * 
	 * 					selectDomainAllLanguage
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idEcosystem", column = "id_ecosystem"),
        @Result(property = "ecosystemcode", column = "ecosystemcode"),
        @Result(property = "description", column = "description")
      })
	@Select({"<script>",
		"SELECT ECOSYSTEM.id_ecosystem, ECOSYSTEM.ecosystemcode, ECOSYSTEM.description "
		+ "FROM " + ECOSYSTEM_TABLE + " ECOSYSTEM, " + R_ECOSYSTEM_ORGANIZATION_TABLE + " ECOS_ORG "
		+ "WHERE ECOS_ORG.id_organization = #{organizationCode} AND "
		+ "ECOS_ORG.id_ecosystem = ECOSYSTEM.id_ecosystem ",
				ORDER_BY,
             "</script>"}) 
	List<Ecosystem> selectEcosystem(@Param("organizationCode") int id, @Param("sortList") List<String> sortList);
	
	@Results({
        @Result(property = "idEcosystem", column = "id_ecosystem"),
        @Result(property = "ecosystemcode", column = "ecosystemcode"),
        @Result(property = "description", column = "description")
      })
	@Select({"<script>",
				SELECT_ALL,
				ORDER_BY,
             "</script>"}) 
	List<Ecosystem> selectAllEcosystem(@Param("organizationCode") int id, @Param("sortList") List<String> sortList);
	
}
