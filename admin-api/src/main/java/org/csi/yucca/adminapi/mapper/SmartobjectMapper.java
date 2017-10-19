package org.csi.yucca.adminapi.mapper;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.csi.yucca.adminapi.model.Smartobject;
import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;
import org.csi.yucca.adminapi.util.Constants;

public interface SmartobjectMapper {

	String SMARTOBJECT_TABLE = Constants.SCHEMA_DB + "yucca_smart_object";
	
	String TENANT_SMARTOBJECT_TABLE = Constants.SCHEMA_DB + "yucca_r_tenant_smart_object";

	String STATUS_TABLE = Constants.SCHEMA_DB + "yucca_d_status";
	
	/*************************************************************************
	 * 
	 * 					DELETE TENANT SMARTOBJECT
	 * 
	 * ***********************************************************************/
	public static final String DELETE_TENANT_SMARTOBJECT = "DELETE FROM " + TENANT_SMARTOBJECT_TABLE + " WHERE id_smart_object = #{idSmartObject}";
	@Delete(DELETE_TENANT_SMARTOBJECT)
	int deleteTenantSmartobject( @Param("idSmartObject") Integer idSmartObject);	

	
	/*************************************************************************
	 * 
	 * 					DELETE TENANT SMARTOBJECT BY idOrganization and idSoType
	 * 
	 * ***********************************************************************/
	public static final String DELETE_TENANT_SMARTOBJECT_BY_ID_ORGANIZATION_AND_ID_SO_TYPE = 
			" delete from " + TENANT_SMARTOBJECT_TABLE + " where id_smart_object in( " +
			" SELECT SMARTOBJECT.id_smart_object " +
			" FROM " + TENANT_SMARTOBJECT_TABLE + " TENANT_SMARTOBJECT, " + SMARTOBJECT_TABLE + " SMARTOBJECT " +
			" WHERE SMARTOBJECT.id_organization = #{idOrganization} AND " +
			" SMARTOBJECT.id_so_type = #{idSoType} AND " +
			" TENANT_SMARTOBJECT.id_smart_object = SMARTOBJECT.id_smart_object) ";	
	@Delete(DELETE_TENANT_SMARTOBJECT_BY_ID_ORGANIZATION_AND_ID_SO_TYPE)
	int deleteTenantSmartobjectByOrgAndSoType( @Param("idSoType") Integer idSoType, @Param("idOrganization") Integer idOrganization );	

	
	/*************************************************************************
	 * 
	 * 					DELETE SMARTOBJECT
	 * 
	 * ***********************************************************************/
	public static final String DELETE_SMARTOBJECT = "DELETE FROM " + SMARTOBJECT_TABLE + " WHERE socode = #{socode} AND id_organization = #{idOrganization}";
	@Delete(DELETE_SMARTOBJECT)
	int deleteSmartobject( @Param("socode") String socode, @Param("idOrganization") Integer idOrganization);	

	/*************************************************************************
	 * 
	 * 					DELETE INTERNAL SMARTOBJECT
	 * 
	 * ***********************************************************************/
	public static final String DELETE_INTERNAL_SMARTOBJECT 
		= "DELETE FROM " + SMARTOBJECT_TABLE + " WHERE id_so_type = #{idSoType} AND id_organization = #{idOrganization}";
	@Delete(DELETE_INTERNAL_SMARTOBJECT)
	int deleteInternalSmartobject( @Param("idSoType") Integer idSoType, @Param("idOrganization") Integer idOrganization);	
	
	
	/************************************************************************************
	 * 
	 * 					SELECT SMARTOBJECT BY ORGANIZATION AND TENANAT
	 * 								LOAD ELENCO SO
	 * 
	 ************************************************************************************/
	public static final String SELECT_SMARTOBJECT_BY_ORGANIZATION_AND_TENANAT =
			" SELECT " +
			" SMARTOBJECT.id_smart_object, socode, " +
			" ORGANIZATION.organizationcode, ORGANIZATION.description AS description_organization, " +
			" STATUS.statuscode, STATUS.description AS description_status, " +
			" SO_TYPE.sotypecode, SO_TYPE.description AS description_so_type, " +
			" SO_CATEGORY.socategorycode, SO_CATEGORY.description AS description_so_category, " +
			" SUPPLY_TYPE.supplytype, SUPPLY_TYPE.description AS description_supplytype, " +
			" EXPOSURE_TYPE.exposuretype, EXPOSURE_TYPE.description AS description_exposuretype, " +
			" LOCATION_TYPE.locationtype, LOCATION_TYPE.description AS description_locationtype, " +
			" name, SMARTOBJECT.description, urladmin, " +
			" fbcoperationfeedback, swclientversion, version, model, deploymentversion, sostatus, creationdate, twtusername, twtmaxsearchnumber, " + 
			" twtmaxsearchinterval, twtusertoken, twttokensecret, twtname, twtuserid, twtmaxstreams, slug, " +
			" SMARTOBJECT.id_location_type, " +
			" SMARTOBJECT.id_exposure_type, " +
			" SMARTOBJECT.id_supply_type, " +
			" SMARTOBJECT.id_so_category, " +
			" SMARTOBJECT.id_so_type, " +
			" SMARTOBJECT.id_status, " +
			" SMARTOBJECT.id_organization " +
			
			" FROM " + SMARTOBJECT_TABLE + " SMARTOBJECT " + 
			
			" LEFT JOIN " + "int_yucca.yucca_d_location_type" + " LOCATION_TYPE ON SMARTOBJECT.id_location_type = LOCATION_TYPE.id_location_type " +   
			" LEFT JOIN " + ExposureTypeMapper.EXPOSURE_TYPE_TABLE + " EXPOSURE_TYPE ON SMARTOBJECT.id_exposure_type = EXPOSURE_TYPE.id_exposure_type " +
			" LEFT JOIN " + SupplyTypeMapper.SUPPLY_TYPE_TABLE + " SUPPLY_TYPE ON SMARTOBJECT.id_supply_type = SUPPLY_TYPE.id_supply_type " +
			" LEFT JOIN " + SoCategoryMapper.SO_CATEGORY_TYPE_TABLE + " SO_CATEGORY ON SMARTOBJECT.id_so_category = SO_CATEGORY.id_so_category " +
			" LEFT JOIN " + SoTypeMapper.SO_TYPE_TABLE + " SO_TYPE ON SMARTOBJECT.id_so_type = SO_TYPE.id_so_type " +
			" LEFT JOIN " + STATUS_TABLE + " STATUS ON SMARTOBJECT.id_status = STATUS.id_status " +
			" INNER JOIN " + OrganizationMapper.ORGANIZATION_TABLE + " ORGANIZATION ON ORGANIZATION.id_organization = SMARTOBJECT.id_organization " +
			
			" WHERE ORGANIZATION.organizationcode = #{organizationCode} AND " +
			" SMARTOBJECT.id_smart_object in ( " +
			" select id_smart_object from "
			+  TENANT_SMARTOBJECT_TABLE  + " TENANT_SO where " + 
			
			" (" 
	
			 	+ " <foreach item=\"propName\" separator=\" OR \" index=\"index\" collection=\"idTenantList\">" 
			
				+  " TENANT_SO.id_tenant = #{propName} "

				+ " </foreach>" 
			
			+ ") AND TENANT_SO.isactive = 1) ";
	@Results({
        @Result(property = "descriptionOrganization",  column = "description_organization"),
        @Result(property = "descriptionStatus",        column = "description_status"),
        @Result(property = "descriptionSoType",        column = "description_so_type"),
        @Result(property = "descriptionSoCategory",    column = "description_so_category"),
        @Result(property = "descriptionSupplytype",    column = "description_supplytype"),
        @Result(property = "descriptionExposuretype",  column = "description_exposuretype"),
        @Result(property = "descriptionLocationtype",  column = "description_locationtype"),
        @Result(property = "idSmartObject",            column = "id_smart_object"),
        @Result(property = "idSoType",                 column = "id_so_type"),
        @Result(property = "idLocationType",           column = "id_location_type"),
        @Result(property = "idExposureType",           column = "id_exposure_type"),
        @Result(property = "idSupplyType",             column = "id_supply_type"),
        @Result(property = "idSoCategory",             column = "id_so_category"),
        @Result(property = "idStatus",                 column = "id_status"),
        @Result(property = "idOrganization",           column = "id_organization")
      })
	@Select({"<script>",SELECT_SMARTOBJECT_BY_ORGANIZATION_AND_TENANAT,"</script>"})
	List<DettaglioSmartobject> selectSmartobjectByOrganizationAndTenant(@Param("organizationCode") String organizationCode, 
			@Param("idTenantList") List<Integer> idTenantList);	
	
	
	/*************************************************************************
	 * 
	 * 					SELECT SMARTOBJECT BY ID_ORGANIZATION AND ID_SO TYPE
	 * 
	 * ***********************************************************************/	
	public static final String SELECT_ID_SMARTOBJECT_BY_ORGANIZATION_AND_SO_TYPE
		= " SELECT id_smart_object, socode, name, description, " + 
				" urladmin, fbcoperationfeedback, swclientversion, version, model, " + 
				" deploymentversion, creationdate, twtusername, twtmaxsearchnumber, " + 
				" twtmaxsearchinterval, twtusertoken, twttokensecret, twtname, " + 
				" twtuserid, twtmaxstreams, slug, id_location_type, id_exposure_type, " + 
				" id_supply_type, id_so_category, id_so_type, id_status, id_organization " + 
		  " FROM " + SMARTOBJECT_TABLE + 
		  " WHERE id_so_type = #{idSoType} AND id_organization = #{idOrganization}";
	@Results({
        @Result(property = "idSmartObject",  column = "id_smart_object"),
        @Result(property = "idSoType",       column = "id_so_type"),
        @Result(property = "idLocationType", column = "id_location_type"),
        @Result(property = "idExposureType", column = "id_exposure_type"),
        @Result(property = "idSupplyType",   column = "id_supply_type"),
        @Result(property = "idSoCategory",   column = "id_so_category"),
        @Result(property = "idStatus",       column = "id_status"),
        @Result(property = "idOrganization", column = "id_organization")
      })
	@Select(SELECT_ID_SMARTOBJECT_BY_ORGANIZATION_AND_SO_TYPE)
	Smartobject selectSmartobjectByOrganizationAndSoType(@Param("idOrganization") Integer idOrganization, @Param("idSoType") Integer idSoType);	
	
	
	/*************************************************************************
	 * 
	 * 					SELECT SMARTOBJECT
	 * 
	 * ***********************************************************************/	
	public static final String SELECT_ID_SMARTOBJECT_SO_TYPE 
		= " SELECT id_smart_object, socode, name, description, " + 
				" urladmin, fbcoperationfeedback, swclientversion, version, model, " + 
				" deploymentversion, creationdate, twtusername, twtmaxsearchnumber, " + 
				" twtmaxsearchinterval, twtusertoken, twttokensecret, twtname, " + 
				" twtuserid, twtmaxstreams, slug, id_location_type, id_exposure_type, " + 
				" id_supply_type, id_so_category, id_so_type, id_status, id_organization " + 
		  " FROM " + SMARTOBJECT_TABLE + 
		  " WHERE socode = #{socode} AND id_organization = #{idOrganization}";
	@Results({
        @Result(property = "idSmartObject",  column = "id_smart_object"),
        @Result(property = "idSoType",       column = "id_so_type"),
        @Result(property = "idLocationType", column = "id_location_type"),
        @Result(property = "idExposureType", column = "id_exposure_type"),
        @Result(property = "idSupplyType",   column = "id_supply_type"),
        @Result(property = "idSoCategory",   column = "id_so_category"),
        @Result(property = "idStatus",       column = "id_status"),
        @Result(property = "idOrganization", column = "id_organization")
      })
	@Select(SELECT_ID_SMARTOBJECT_SO_TYPE)
	Smartobject selectSmartobject( @Param("socode") String socode, @Param("idOrganization") Integer idOrganization);	

	
	/*************************************************************************
	 * 
	 * 					SELECT SMARTOBJECT BY SLUG AND ID_ORGANIZATION
	 * 
	 * ***********************************************************************/	
	public static final String SELECT_ID_SMARTOBJECT_SO_TYPE_BY_SLUG 
		= "SELECT id_smart_object, id_so_type FROM " + SMARTOBJECT_TABLE + " WHERE slug = #{slug} AND id_organization = #{idOrganization}";
	@Results({
        @Result(property = "idSmartObject", column = "id_smart_object"),
        @Result(property = "idSoType", column = "id_so_type")
      })
	@Select(SELECT_ID_SMARTOBJECT_SO_TYPE_BY_SLUG)
	Smartobject selectSmartobjectBySlugAndOrganization( @Param("slug") String slug, @Param("idOrganization") Integer idOrganization);	

	
	
	
	/*************************************************************************
	 * 
	 * 					UPDATE SMART OBJECT
	 * 
	 * ***********************************************************************/
	public static final String UPDATE_SMARTOBJECT = 
			" UPDATE " + SMARTOBJECT_TABLE +
			" SET name=#{name}, twttokensecret=#{twttokensecret},  "
			+ " twtusername=#{twtusername},  twtmaxsearchnumber=#{twtmaxsearchnumber}, "
			+ " twtmaxsearchinterval=#{twtmaxsearchinterval}, " 
			+ " twtusertoken=#{twtusertoken}, twtname=#{twtname}, twtuserid=#{twtuserid}, "
	        + " twtmaxstreams=#{twtmaxstreams}, id_exposure_type=#{idExposureType}, "
	        + " id_location_type=#{idLocationType}, " 
	        + " urladmin=#{urladmin}, swclientversion=#{swclientversion}, id_supply_type=#{idSupplyType}, model=#{model} " +
			" WHERE socode=#{socode} and id_organization=#{idOrganization} ";
	@Update(UPDATE_SMARTOBJECT)
	int updateSmartobject(Smartobject smartobject);	
	
	
	
	
	/*************************************************************************
	 * 
	 * 					update slug
	 * 
	 * ***********************************************************************/
	public static final String UPDATE_SLUG_BY_ID = 
			"UPDATE " + SMARTOBJECT_TABLE + " SET slug=#{slug} WHERE id_smart_object=#{idSmartObject}";
	@Update(UPDATE_SLUG_BY_ID)
	int updateSlugById(@Param("slug") String slug, @Param("idSmartObject") Integer idSmartObject);	

	/*************************************************************************
	 * 
	 * 					select all id smart object
	 * 
	 * ***********************************************************************/
	public static final String SELECT_ALL_ID_SMARTOBJECT = 
			"SELECT id_smart_object FROM int_yucca.yucca_smart_object";
	@Select(SELECT_ALL_ID_SMARTOBJECT) 
	List<Integer> selectAllSmartobject();	

	/*************************************************************************
	 * 
	 * 					INSERT TENANT-SMART_OBJECT
	 * 
	 * ***********************************************************************/
	public static final String INSERT_TENANT_SMARTOBJECT = 
		" INSERT INTO int_yucca.yucca_r_tenant_smart_object( " +
		" id_tenant, id_smart_object, isactive, ismanager, activationdate, managerfrom) " +
		" VALUES (#{idTenant}, #{idSmartObject}, 1, #{isManager}, #{now}, #{now}) ";
	@Insert(INSERT_TENANT_SMARTOBJECT)
	int insertTenantSmartobject(@Param("idTenant") Integer idTenant, 
						        @Param("idSmartObject") Integer idSmartObject, 
						        @Param("now") Timestamp now,
						        @Param("isManager") Integer isManager);
	
	/*************************************************************************
	 * 
	 * 					INSERT SMART OBJECT
	 * 
	 * ***********************************************************************/
	public static final String INSERT_SMARTOBJECT = 
	" INSERT INTO " + SMARTOBJECT_TABLE + " ( " +
			" socode, name, description, urladmin, fbcoperationfeedback, " + 
			" swclientversion, version, model, deploymentversion, " +
			" creationdate, twtusername, twtmaxsearchnumber, twtmaxsearchinterval, " + 
			" twtusertoken, twttokensecret, twtname, twtuserid, twtmaxstreams, " +
			" slug, id_location_type, id_exposure_type, id_supply_type, id_so_category, " + 
			" id_so_type, id_status, id_organization) " +
			" VALUES (#{socode}, #{name}, #{description}, #{urladmin}, #{fbcoperationfeedback}, " + 
			"  #{swclientversion}, #{version}, #{model}, #{deploymentversion},  " +
			" #{creationdate}, #{twtusername}, #{twtmaxsearchnumber}, #{twtmaxsearchinterval}, " +
			" #{twtusertoken}, #{twttokensecret}, #{twtname}, #{twtuserid}, #{twtmaxstreams}, " +
			" #{slug}, #{idLocationType}, #{idExposureType}, #{idSupplyType}, #{idSoCategory}, " +
			" #{idSoType}, #{idStatus}, #{idOrganization}) ";
	@Insert(INSERT_SMARTOBJECT)
	@Options(useGeneratedKeys=true, keyProperty="idSmartObject")
	int insertSmartObject(Smartobject smartobject);

	
	
	
}