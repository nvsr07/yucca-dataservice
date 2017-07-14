package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.License;
import org.csi.yucca.adminapi.model.Subdomain;
import org.csi.yucca.adminapi.util.Constants;

/**
 * @author gianfranco.stolfa
 *
 */
public interface SubdomainMapper {
	
	String SUBDOMAIN_TABLE = Constants.SCHEMA_DB + ".yucca_d_subdomain";
	
	public static final String SELECT =
			" FROM " + SUBDOMAIN_TABLE + " " + 
			" WHERE id_domain = #{domainCode} " + 
	
			"<if test=\"sortList != null\">" +
				" ORDER BY " +
			
				" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
				
				"<if test=\"propName == 'idSubdomain-'\">" +
			        " id_subdomain desc" +
		        "</if>" +
		        "<if test=\"propName == 'idSubdomain'\">" +
		            " id_subdomain" +
	            "</if>" +			
				
				"<if test=\"propName == 'subdomaincode-'\">" +
		           " subdomaincode desc" +
	            "</if>" +
	            "<if test=\"propName == 'subdomaincode'\">" +
	               " subdomaincode" +
	            "</if>" +			
	               
				"<if test=\"propName == 'langIt-'\">" +
		           " lang_it desc" +
	            "</if>" +
	            "<if test=\"propName == 'langIt'\">" +
	               " lang_it" +
	            "</if>" +			
	               
				"<if test=\"propName == 'langEn-'\">" +
		           " lang_en desc" +
	            "</if>" +
	            "<if test=\"propName == 'langEn'\">" +
	               " lang_en" +
	            "</if>" +			
	               
				"<if test=\"propName == 'deprecated-'\">" +
		           " deprecated desc" +
	            "</if>" +
	            "<if test=\"propName == 'deprecated'\">" +
	               " deprecated" +
	            "</if>" +			
	            
	            "</foreach>" +
            "</if>";

	
	/*************************************************************************
	 * 
	 * 					select all licenses
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idSubdomain", column = "id_subdomain"),
        @Result(property = "subdomaincode", column = "subdomaincode"),
        @Result(property = "langIt", column = "lang_it"),
        @Result(property = "langEn", column = "lang_en"),
        @Result(property = "deprecated", column = "deprecated"),
        @Result(property = "idDomain", column = "id_domain")
	})
	@Select({"<script>",
				" SELECT id_subdomain, subdomaincode, lang_it, lang_en, deprecated, id_domain ",
				SELECT,
             "</script>"}) 
	List<Subdomain> selectSubdomainAllLanguage(@Param("domainCode") int domainCode, @Param("sortList") List<String> sortList);
	
	
	@Results({
        @Result(property = "idSubdomain", column = "id_subdomain"),
        @Result(property = "subdomaincode", column = "subdomaincode"),
        @Result(property = "langIt", column = "lang_it"),
        @Result(property = "deprecated", column = "deprecated"),
        @Result(property = "idDomain", column = "id_domain")
	})
	@Select({"<script>",
				" SELECT id_subdomain, subdomaincode, lang_it, deprecated, id_domain ",
				SELECT,
             "</script>"}) 
	List<Subdomain> selectSubdomainITLanguage(@Param("domainCode") int domainCode, @Param("sortList") List<String> sortList);
	

	@Results({
        @Result(property = "idSubdomain", column = "id_subdomain"),
        @Result(property = "subdomaincode", column = "subdomaincode"),
        @Result(property = "langEn", column = "lang_en"),
        @Result(property = "deprecated", column = "deprecated"),
        @Result(property = "idDomain", column = "id_domain")
	})
	@Select({"<script>",
				" SELECT id_subdomain, subdomaincode, lang_en, deprecated, id_domain ",
				SELECT,
             "</script>"}) 
	List<Subdomain> selectSubdomainENLanguage(@Param("domainCode") int domainCode, @Param("sortList") List<String> sortList);
	

	
}
