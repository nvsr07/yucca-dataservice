package org.csi.yucca.adminapi.mapper;

import java.util.List;

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
	
	public static final String SELECT = 
			"SELECT ECOSYSTEM.id_ecosystem, ECOSYSTEM.ecosystemcode, ECOSYSTEM.description "
			+ "FROM " + ECOSYSTEM_TABLE + " ECOSYSTEM, " + R_ECOSYSTEM_ORGANIZATION_TABLE + " ECOS_ORG "
			+ "WHERE ECOS_ORG.id_organization = #{organizationCode} AND "
			+ "ECOS_ORG.id_ecosystem = ECOSYSTEM.id_ecosystem " + 
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
	 * 					selectDomainAllLanguage
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idEcosystem", column = "id_ecosystem"),
        @Result(property = "ecosystemcode", column = "ecosystemcode"),
        @Result(property = "description", column = "description")
      })
	@Select({"<script>",
				SELECT,
             "</script>"}) 
	List<Ecosystem> selectEcosystem(@Param("organizationCode") int id, @Param("sortList") List<String> sortList);
	
}
