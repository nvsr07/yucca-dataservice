package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
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
	String R_TENANT_BUNDLES_TABLE = Constants.SCHEMA_DB + ".yucca_r_tenant_bundles";
	
	/*************************************************************************
	 * 
	 * 					SELECT TENANT BY USERNAME
	 * 
	 * ***********************************************************************/
	public static final String SELECT_ACTIVE_TENANT_BY_USERNAME_AND_ID_TENANT_TYPE =
	" SELECT TENANT.id_tenant, tenantcode, name, description, clientkey, " + 
	" clientsecret, activationdate, deactivationdate, usagedaysnumber, " + 
	" userfirstname, userlastname, useremail, usertypeauth, creationdate, " + 
	" expirationdate, id_ecosystem, TENANT.id_organization, id_tenant_type, " + 
	" id_tenant_status, datasolrcollectionname, measuresolrcollectionname, " + 
	" mediasolrcollectionname, socialsolrcollectionname, dataphoenixtablename, " + 
	" dataphoenixschemaname, measuresphoenixtablename, measuresphoenixschemaname, " + 
	" mediaphoenixtablename, mediaphoenixschemaname, socialphoenixtablename, " + 
	" socialphoenixschemaname, id_share_type " +
	" FROM " + TENANT_TABLE + " TENANT, " + UserMapper.USER_TABLE + " USERS, " + UserMapper.R_TENANT_USERS_TABLE + " TENANT_USERS " + 
	" WHERE TENANT.id_tenant_type = #{idTenantType} AND " +
	" TENANT.activationdate <= current_timestamp AND " +
	" (TENANT.deactivationdate > current_timestamp or TENANT.deactivationdate is null) AND " +
	" USERS.username = #{username} AND " +
	" USERS.id_user = TENANT_USERS.id_user AND " +
	" TENANT.id_tenant = TENANT_USERS.id_tenant ";
	@Results({
        @Result(property = "idTenant",       column = "id_tenant"),
        @Result(property = "idEcosystem",    column = "id_ecosystem"),
        @Result(property = "idOrganization", column = "id_organization"),
        @Result(property = "idTenantType",   column = "id_tenant_type"),
        @Result(property = "idTenantStatus", column = "id_tenant_status"),
        @Result(property = "idShareType",    column = "id_share_type")
      })	
	@Select(SELECT_ACTIVE_TENANT_BY_USERNAME_AND_ID_TENANT_TYPE) 
	List<Tenant> selectActiveTenantByUserNameAndIdTenantType(@Param("username") String username, @Param("idTenantType") Integer idTenantType);

	
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
	 * 					SELECT ID TENANT BY ID ORGANIZATION
	 * 
	 * ***********************************************************************/
	public static final String SELECT_ID_TENANT_BY_ID_ORGANIZATION = "SELECT id_tenant FROM " + TENANT_TABLE
	+ " where id_organization = #{idOrganization}";
	@Select(SELECT_ID_TENANT_BY_ID_ORGANIZATION) 
	List<Integer> selectIdTenantByIdOrganization(@Param("idOrganization") int idOrganization);
	
	
	/*************************************************************************
	 * 
	 * 					INSERT TENANT
	 * 
	 * ***********************************************************************/

	
	/*
id_tenant integer NOT NULL DEFAULT nextval('int_yucca.tenant_id_tenant_seq'::regclass),
  id_tenant integer NOT NULL DEFAULT nextval('int_yucca.tenant_id_tenant_seq'::regclass),
  tenantcode character varying(100) NOT NULL,
  name character varying(200) NOT NULL,
  description character varying(1000),
  clientkey character varying(500),
  clientsecret character varying(500),
  activationdate date DEFAULT now(),
  deactivationdate date,
  usagedaysnumber smallint NOT NULL DEFAULT (-1),
  userfirstname character varying(100) NOT NULL,
  userlastname character varying(100) NOT NULL,
  useremail character varying(100) NOT NULL,
  usertypeauth character varying(50) NOT NULL,
  creationdate date DEFAULT now(),
  expirationdate date,
  id_ecosystem bigint,
  id_organization bigint NOT NULL,
  id_tenant_type integer NOT NULL DEFAULT 1,
  id_tenant_status integer NOT NULL DEFAULT 1,
  datasolrcollectionname character varying(200),
  measuresolrcollectionname character varying(200),
  mediasolrcollectionname character varying(200),
  socialsolrcollectionname character varying(200),
  dataphoenixtablename character varying(200),
  dataphoenixschemaname character varying(200),
  measuresphoenixtablename character varying(200),
  measuresphoenixschemaname character varying(200),
  mediaphoenixtablename character varying(200),
  mediaphoenixschemaname character varying(200),
  socialphoenixtablename character varying(200),
  socialphoenixschemaname character varying(200),
  id_share_type integer NOT NULL DEFAULT 2,	 */


	
	
	public static final String INSERT_TENANT = 
		"INSERT INTO " + TENANT_TABLE +
			" ( creationdate, expirationdate, activationdate, deactivationdate, id_share_type, " +
			" tenantcode, name, description, clientkey, clientsecret, usagedaysnumber, " +
			" userfirstname, userlastname, useremail, usertypeauth, id_ecosystem, " +
			" id_organization, id_tenant_type, id_tenant_status, datasolrcollectionname, " +
			" measuresolrcollectionname, mediasolrcollectionname, socialsolrcollectionname, " +
			" dataphoenixtablename, dataphoenixschemaname, measuresphoenixtablename, " +
			" measuresphoenixschemaname, mediaphoenixtablename, mediaphoenixschemaname, " +
			" socialphoenixtablename, socialphoenixschemaname ) " +
			" VALUES (	#{creationdate}, #{expirationdate}, #{activationdate}, #{deactivationdate}, #{idShareType}, " +
			" #{tenantcode}, #{name}, #{description}, #{clientkey}, #{clientsecret}, #{usagedaysnumber}, " +
			" #{userfirstname}, #{userlastname}, #{useremail}, #{usertypeauth}, #{idEcosystem}, " +
			" #{idOrganization}, #{idTenantType}, #{idTenantStatus}, #{datasolrcollectionname}, " +
			" #{measuresolrcollectionname}, #{mediasolrcollectionname}, #{socialsolrcollectionname}, " +
			" #{dataphoenixtablename}, #{dataphoenixschemaname}, #{measuresphoenixtablename}, " +
			" #{measuresphoenixschemaname}, #{mediaphoenixtablename}, #{mediaphoenixschemaname}, " +
			" #{socialphoenixtablename}, #{socialphoenixschemaname})";
	@Insert(INSERT_TENANT)
	@Options(useGeneratedKeys=true, keyProperty="idTenant")
	int insertTenant(Tenant tenant);
	
	
	/*************************************************************************
	 * 
	 * 					INSERT TENANT BUNDLES
	 * 
	 * ***********************************************************************/	
	public static final String INSERT_TENANT_BUNDLES = 
			"INSERT INTO " + R_TENANT_BUNDLES_TABLE + "(id_tenant, id_bundles)VALUES (#{idTenant}, #{idBundles})";
	@Insert(INSERT_TENANT_BUNDLES)
	int insertTenantBundles(@Param("idTenant") int idTenant, @Param("idBundles") int idBundles);
	
	
	/*************************************************************************
	 * 
	 * 					DELETE TENANT
	 * 
	 * ***********************************************************************/
	public static final String DELETE_TENANT = "DELETE FROM " + TENANT_TABLE + " WHERE tenantcode=#{tenantcode}";
	@Delete(DELETE_TENANT)
	int deleteTenant(String tenantcode);	
	
}
