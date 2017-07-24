package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.Domain;
import org.csi.yucca.adminapi.util.Constants;

/**
 * 
 * @author gianfranco.stolfa
 *
 */
public interface DomainMapper {
	
	String DOMAIN_TABLE = Constants.SCHEMA_DB + ".yucca_d_domain";
	String R_ECOSYSTEM_DOMAIN_TABLE = Constants.SCHEMA_DB + ".yucca_r_ecosystem_domain";
	String ECOSYSTEM_TABLE = Constants.SCHEMA_DB + ".yucca_ecosystem";
	
	public static final String INSERT_DOMAIN 
	= "INSERT INTO " + DOMAIN_TABLE + "( domaincode, langit, langen, deprecated) VALUES (#{domaincode}, #{langit}, #{langen}, #{deprecated})";

	public static final String INSERT_ECOSYSTEM_DOMAIN = 
		"INSERT INTO " + R_ECOSYSTEM_DOMAIN_TABLE + "(id_ecosystem, id_domain)VALUES (#{idEcosystem}, #{idDomain})";
	
	public static final String SELECT_DOMAIN = "FROM " + DOMAIN_TABLE + " " +
			"JOIN "+ R_ECOSYSTEM_DOMAIN_TABLE + " ON " + DOMAIN_TABLE + ".id_domain = " + R_ECOSYSTEM_DOMAIN_TABLE + ".id_domain " +
			"JOIN " + ECOSYSTEM_TABLE + " ON " + R_ECOSYSTEM_DOMAIN_TABLE + ".id_ecosystem = " + ECOSYSTEM_TABLE + ".id_ecosystem " +
			"WHERE " + ECOSYSTEM_TABLE + ".id_ecosystem = #{id} " + 

			"<if test=\"sortList != null\">" +
		      " ORDER BY " +
			
			" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
		    
			  "<if test=\"propName == 'idDomain-'\">" +
		        " id_domain desc" +
	          "</if>" +
	          "<if test=\"propName == 'idDomain'\">" +
	            " id_domain" +
              "</if>" +
	          
		      "<if test=\"propName == 'langit-'\">" +
		        " langit desc" +
	          "</if>" +
	          "<if test=\"propName == 'langit'\">" +
	            " langit" +
              "</if>" +
			
		      "<if test=\"propName == 'langen-'\">" +
		        " langen desc" +
	          "</if>" +
	          "<if test=\"propName == 'langen'\">" +
	            " langen" +
              "</if>" +
			
		      "<if test=\"propName == 'deprecated-'\">" +
		        " deprecated desc" +
	          "</if>" +
	          "<if test=\"propName == 'deprecated'\">" +
	            " deprecated" +
              "</if>" +
			
		      "<if test=\"propName == 'domaincode-'\">" +
		        " domaincode desc" +
	          "</if>" +
	          "<if test=\"propName == 'domaincode'\">" +
	            " domaincode" +
              "</if>" +
            
            "</foreach>" +
            "</if>";
	
	
	/*************************************************************************
	 * 
	 * 					INSERT DOMAIN
	 * 
	 * ***********************************************************************/
	@Insert(INSERT_DOMAIN)
	@Options(useGeneratedKeys=true, keyProperty="idDomain")
	int insertDomain(Domain domain);

	/*************************************************************************
	 * 
	 * 					INSERT ECOSYSTEM
	 * 
	 * ***********************************************************************/
	@Insert(INSERT_ECOSYSTEM_DOMAIN)
	int insertEcosystemDomain(@Param("idEcosystem") int idEcosystem, @Param("idDomain") int idDomain);
	
	
	
	/*************************************************************************
	 * 
	 * 					DELETE DOMAIN
	 * 
	 * ***********************************************************************/
	public static final String DELETE_DOMAIN = "DELETE FROM " + DOMAIN_TABLE + " WHERE id_domain=#{idDomain}";
	@Delete(DELETE_DOMAIN)
	void deleteDomain(int idDomain);	


	/*************************************************************************
	 * 
	 * 					DELETE ECOSYSTEM-DOMAIN
	 * 
	 * ***********************************************************************/
	public static final String DELETE_ECOSYSTEM_DOMAIN = "DELETE FROM " + R_ECOSYSTEM_DOMAIN_TABLE + " WHERE id_domain=#{idDomain}";
	@Delete(DELETE_ECOSYSTEM_DOMAIN)
	void deleteEcosystemDomain(int idDomain);	

	
	
	
	
	
	
	
	
	
	
	
	
	
	/*************************************************************************
	 * 
	 * 					selectDomainAllLanguage
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idDomain", column = "id_domain"),
        @Result(property = "domaincode", column = "domaincode"),
        @Result(property = "langit", column = "langit"),
        @Result(property = "langen", column = "langen"),
        @Result(property = "deprecated", column = "deprecated")
      })
	@Select({"<script>",
		        "SELECT domaincode, ", DOMAIN_TABLE, ".id_domain, deprecated, langit, langen " ,
		        SELECT_DOMAIN,
             "</script>"}) 
	List<Domain> selectDomainAllLanguage(@Param("id") int id, @Param("sortList") List<String> sortList);
	
	
	/*************************************************************************
	 * 
	 * 					selectDomainITLanguage
	 * 
	 * ***********************************************************************/	
	@Results({
        @Result(property = "idDomain", column = "id_domain"),
        @Result(property = "domaincode", column = "domaincode"),
        @Result(property = "langit", column = "langit"),
        @Result(property = "deprecated", column = "deprecated")
      })
	@Select({"<script>",
		        "SELECT domaincode, ", DOMAIN_TABLE, ".id_domain, deprecated, langit" ,
		        SELECT_DOMAIN,
             "</script>"}) 
	List<Domain> selectDomainITLanguage(@Param("id") int id, @Param("sortList") List<String> sortList);
	
	
	/*************************************************************************
	 * 
	 * 					selectDomainENLanguage
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idDomain", column = "id_domain"),
        @Result(property = "domaincode", column = "domaincode"),
        @Result(property = "langit", column = "langit"),
        @Result(property = "langen", column = "langen"),
        @Result(property = "deprecated", column = "deprecated")
      })
	@Select({"<script>",
		        "SELECT domaincode, ", DOMAIN_TABLE, ".id_domain, deprecated, langen " ,
		        SELECT_DOMAIN, 
             "</script>"}) 
	List<Domain> selectDomainENLanguage(@Param("id") int id, @Param("sortList") List<String> sortList);
	
}
