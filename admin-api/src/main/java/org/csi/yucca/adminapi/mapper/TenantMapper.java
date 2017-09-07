package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.util.Constants;

/**
 * 
 * @author gianfranco.stolfa
 *
 */
public interface TenantMapper {
	
	
	String TENANT_TABLE = Constants.SCHEMA_DB + ".yucca_tenant";

	/*************************************************************************
	 * 
	 * 					SELECT ORGANIZATION BY ID TENANT
	 * 
	 * ***********************************************************************/
	public static final String SELECT_ORGANIZATION_BY_ID_TENANT = 
			"SELECT id_organization FROM " + TENANT_TABLE + " where id_tenant = #{idTenant}";
	@Select(SELECT_ORGANIZATION_BY_ID_TENANT) 
	Integer selectIdOrganizationByIdTenant(@Param("idTenant") int idTenant);

	/*************************************************************************
	 * 
	 * 					INSERT TENANT
	 * 
	 * ***********************************************************************/
	public static final String INSERT_TENANT = 
		"INSERT INTO " + TENANT_TABLE +
			"( tenantcode, name, description, clientkey, clientsecret, " + 
			" maxdatasetnum, maxstreamsnum, tenant_password, activationdate, " + 
			" deactivationdate, usagedaysnumber, username, userfirstname, userlastname, " + 
			" useremail, usertypeauth, creationdate, expirationdate, zeppelin, " + 
			" hasstage, id_ecosystem, id_organization, id_tenant_type, id_tenant_status, " + 
			" datacollectionname, datacollectiondb, measurecollectionname, " + 
			" measurecollectiondb, mediacollectionname, mediacollectiondb, " + 
			" socialcollectionname, socialcollectiondb, archivedatacollectionname, " + 
			" archivedatacollectiondb, archivemeasurescollectionname, archivemeasurescollectiondb, " + 
			" datasolrcollectionname, measuresolrcollectionname, mediasolrcollectionname, " + 
			" socialsolrcollectionname, dataphoenixtablename, dataphoenixschemaname, " + 
			" measuresphoenixtablename, measuresphoenixschemaname, mediaphoenixtablename, " + 
			" mediaphoenixschemaname, socialphoenixtablename, socialphoenixschemaname) " +
		" VALUES (#{tenantcode}, #{name}, #{description}, #{clientkey}, #{clientsecret}, " + 
			" #{maxdatasetnum}, #{maxstreamsnum}, #{tenantPassword}, #{activationdate}, " + 
			" #{deactivationdate}, #{usagedaysnumber}, #{username}, #{userfirstname}, #{userlastname}, " + 
			" #{useremail}, #{usertypeauth}, #{creationdate}, #{expirationdate}, #{zeppelin}, " + 
			" #{hasstage}, #{idEcosystem}, #{idOrganization}, #{idTenantType}, #{idTenantStatus}, " + 
			" #{datacollectionname}, #{datacollectiondb}, #{measurecollectionname}, " + 
			" #{measurecollectiondb}, #{mediacollectionname}, #{mediacollectiondb}, " + 
			" #{socialcollectionname}, #{socialcollectiondb}, #{archivedatacollectionname}, " + 
			" #{archivedatacollectiondb}, #{archivemeasurescollectionname}, #{archivemeasurescollectiondb}, " + 
			" #{datasolrcollectionname}, #{measuresolrcollectionname}, #{mediasolrcollectionname}, " + 
			" #{socialsolrcollectionname}, #{dataphoenixtablename}, #{dataphoenixschemaname}, " + 
			" #{measuresphoenixtablename}, #{measuresphoenixschemaname}, #{mediaphoenixtablename}, " + 
			" #{mediaphoenixschemaname}, #{socialphoenixtablename}, #{socialphoenixschemaname}) ";
	@Insert(INSERT_TENANT)
	@Options(useGeneratedKeys=true, keyProperty="idTenant")
	int insertTenant(Tenant tenant);
	
}
