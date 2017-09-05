package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.License;
import org.csi.yucca.adminapi.util.Constants;

/**
 * @author gianfranco.stolfa
 *
 */
public interface LicenseMapper {
	
	String LICENSE_TABLE = Constants.SCHEMA_DB + ".yucca_d_license";
	String R_ECOSYSTEM_ORGANIZATION_TABLE = Constants.SCHEMA_DB + ".yucca_r_ecosystem_organization";
	
	public static final String SELECT = 
			"SELECT id_license, licensecode, description FROM " + LICENSE_TABLE + " " + 
	
			"<if test=\"sortList != null\">" +
				" ORDER BY " +
			
				" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
				
				"<if test=\"propName == 'licensecode-'\">" +
			        " licensecode desc" +
		        "</if>" +
		        "<if test=\"propName == 'licensecode'\">" +
		            " licensecode" +
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
	 * 					select licenses by idLicense
	 * 
	 * ***********************************************************************/
	public static final String SELECT_LICENSE_BY_ID = 
			"SELECT id_license, licensecode, description FROM " + LICENSE_TABLE + " WHERE id_license=#{idLicense}";
	@Results({
        @Result(property = "idLicense", column = "id_license"),
        @Result(property = "licensecode", column = "licensecode"),
        @Result(property = "description", column = "description")
      })
	@Select(SELECT_LICENSE_BY_ID) 
	License selectLicenseById(@Param("idLicense") Integer idLicense);
	
	
	/*************************************************************************
	 * 
	 * 					DELETE LICENSE
	 * 
	 * ***********************************************************************/
	public static final String DELETE_LICENSE = "DELETE FROM " + LICENSE_TABLE + " WHERE id_license=#{idLicense}";
	@Delete(DELETE_LICENSE)
	int deleteLicense(int idLicense);	

	
	/*************************************************************************
	 * 
	 * 					UPDATE LICENSE
	 * 
	 * ***********************************************************************/
	public static final String UPDATE_LICENSE = 
			"UPDATE " + LICENSE_TABLE + 
			" SET licensecode=#{licensecode}, description=#{description} WHERE id_license=#{idLicense}";
	@Delete(UPDATE_LICENSE)
	int updateLicense(License license);	
	
	
	/*************************************************************************
	 * 
	 * 					INSERT LICENSE
	 * 
	 * ***********************************************************************/
	public static final String INSERT_LICENSE = "INSERT INTO " + LICENSE_TABLE
			+ "(licensecode, description) VALUES (#{licensecode}, #{description})";
	@Insert(INSERT_LICENSE)                      
	@Options(useGeneratedKeys=true, keyProperty="idLicense")
	int insertLicense(License license);

	/*************************************************************************
	 * 
	 * 					select all licenses
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idLicense", column = "id_license"),
        @Result(property = "licensecode", column = "licensecode"),
        @Result(property = "description", column = "description")
      })
	@Select({"<script>",
				SELECT,
             "</script>"}) 
	List<License> selectLicense(@Param("sortList") List<String> sortList);
	
}
