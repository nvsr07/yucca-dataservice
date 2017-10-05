package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.util.Constants;

/**
 * 
 * @author gianfranco.stolfa
 *
 */
public interface OrganizationMapper {

	String ORGANIZATION_TABLE = Constants.SCHEMA_DB + ".yucca_organization";
	String R_ECOSYSTEM_ORGANIZATION_TABLE = Constants.SCHEMA_DB + ".yucca_r_ecosystem_organization";
		
	
	public static final String SELECT_STAR_FROM_ORG_TABLE = 
			" SELECT ORG.id_organization, ORG.organizationcode, ORG.description, ORG.datasolrcollectionname, " + 
					" ORG.measuresolrcollectionname, ORG.mediasolrcollectionname, ORG.socialsolrcollectionname, " + 
					" ORG.dataphoenixtablename, ORG.dataphoenixschemaname, ORG.measuresphoenixtablename, " + 
					" ORG.measuresphoenixschemaname, ORG.mediaphoenixtablename, ORG.mediaphoenixschemaname, " + 
					" ORG.socialphoenixtablename, ORG.socialphoenixschemaname FROM " + ORGANIZATION_TABLE + " ORG " ;
	
	public static final String SELECT = SELECT_STAR_FROM_ORG_TABLE + ", " + R_ECOSYSTEM_ORGANIZATION_TABLE + " R_ORG_ECO, "
					+ EcosystemMapper.ECOSYSTEM_TABLE + " ECO " +
			" WHERE ECO.ecosystemcode = #{ecosystemCode} AND  " +
			" R_ORG_ECO.id_ecosystem = ECO.id_ecosystem AND " + 
			" ORG.id_organization = R_ORG_ECO.id_organization  " +

			"<if test=\"sortList != null\">" +
				" ORDER BY " +
			
				" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
				
				"<if test=\"propName == 'idOrganization-'\">" +
			        " id_organization desc" +
		        "</if>" +
		        "<if test=\"propName == 'idOrganization'\">" +
		            " id_organization" +
	            "</if>" +			
				
				"<if test=\"propName == 'organizationcode-'\">" +
		           " organizationcode desc" +
	            "</if>" +
	            "<if test=\"propName == 'organizationcode'\">" +
	               " organizationcode" +
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
	 * 					select ORGANIZATIONS by id
	 * 
	 * ***********************************************************************/
	public static final String SELECT_ORGANIZATION_BY_ID = 
			SELECT_STAR_FROM_ORG_TABLE + " WHERE id_organization=#{idOrganization}";
	@Results({
        @Result(property = "idOrganization", column = "id_organization")
      })
	@Select(SELECT_ORGANIZATION_BY_ID) 
	Organization selectOrganizationById(@Param("idOrganization") Integer idOrganization);
	
	/*************************************************************************
	 * 
	 * 					select ORGANIZATIONS by organizationcode
	 * 
	 * ***********************************************************************/
	public static final String SELECT_ORGANIZATION_BY_CODE = SELECT_STAR_FROM_ORG_TABLE + " WHERE organizationcode=#{organizationCode}";
	@Results({
        @Result(property = "idOrganization", column = "id_organization"),
        @Result(property = "organizationcode", column = "organizationcode"),
        @Result(property = "description", column = "description")
      })
	@Select(SELECT_ORGANIZATION_BY_CODE) 
	Organization selectOrganizationByCode(@Param("organizationCode") String organizationCode);
	
	/*************************************************************************
	 * 
	 * 					DELETE ECOSYSTEM-ORGANIZATION
	 * 
	 * ***********************************************************************/
	public static final String DELETE_ECOSYSTEM_ORGANIZATION = "DELETE FROM " + R_ECOSYSTEM_ORGANIZATION_TABLE + " WHERE id_organization=#{idOrganization}";
	@Delete(DELETE_ECOSYSTEM_ORGANIZATION)
	void deleteEcosystemOrganization(int idOrganization);	

	
	/*************************************************************************
	 * 
	 * 					DELETE ORGANIZATION
	 * 
	 * ***********************************************************************/
	public static final String DELETE_ORGANIZATION = "DELETE FROM " + ORGANIZATION_TABLE + " WHERE id_organization=#{idOrganization}";
	@Delete(DELETE_ORGANIZATION)
	int deleteOrganization(int idOrganization);	

	
	/*************************************************************************
	 * 
	 * 					INSERT ECOSYSTEM ORGANIZATION
	 * 
	 * ***********************************************************************/
	public static final String INSERT_ECOSYSTEM_ORGANIZATION = 
			"INSERT INTO " + R_ECOSYSTEM_ORGANIZATION_TABLE + "(id_ecosystem, id_organization)VALUES (#{idEcosystem}, #{idOrganization})";
	
	@Insert(INSERT_ECOSYSTEM_ORGANIZATION)
	int insertEcosystemOrganization(@Param("idEcosystem") int idEcosystem, @Param("idOrganization") int idOrganization);
	
	
	/*************************************************************************
	 * 
	 * 					INSERT ORGANIZATION
	 * 
	 * ***********************************************************************/
	public static final String INSERT_ORGANIZATION 
	= "INSERT INTO " + ORGANIZATION_TABLE + "( organizationcode, description, datasolrcollectionname, measuresolrcollectionname, mediasolrcollectionname, socialsolrcollectionname, dataphoenixtablename, dataphoenixschemaname, measuresphoenixtablename, measuresphoenixschemaname, mediaphoenixtablename, mediaphoenixschemaname, socialphoenixtablename, socialphoenixschemaname) VALUES (#{organizationcode}, #{description}, #{datasolrcollectionname}, #{measuresolrcollectionname}, #{mediasolrcollectionname}, #{socialsolrcollectionname}, #{dataphoenixtablename}, #{dataphoenixschemaname}, #{measuresphoenixtablename}, #{measuresphoenixschemaname}, #{mediaphoenixtablename}, #{mediaphoenixschemaname}, #{socialphoenixtablename}, #{socialphoenixschemaname})";
	@Insert(INSERT_ORGANIZATION)
	@Options(useGeneratedKeys=true, keyProperty="idOrganization")
	int insertOrganization(Organization organization);
	
	
	/*************************************************************************
	 * 
	 * 					UPDATE ORGANIZATION
	 * 
	 * ***********************************************************************/
	public static final String UPDATE_ORGANIZATION = 
			"UPDATE " + ORGANIZATION_TABLE + 
			" SET organizationcode=#{organizationcode}, description=#{description} WHERE id_organization=#{idOrganization}";
	@Delete(UPDATE_ORGANIZATION)
	int updateOrganization(Organization organization);	
	
	
	/*************************************************************************
	 * 
	 * 					select ORGANIZATIONS
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idOrganization", column = "id_organization"),
        @Result(property = "organizationcode", column = "organizationcode"),
        @Result(property = "description", column = "description")
      })
	@Select({"<script>",
				SELECT,
             "</script>"}) 
	List<Organization> selectOrganization(@Param("ecosystemCode") String ecosystemCode, @Param("sortList") List<String> sortList);
	
}
