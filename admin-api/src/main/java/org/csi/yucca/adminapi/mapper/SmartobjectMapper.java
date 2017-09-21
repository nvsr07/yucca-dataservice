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
import org.csi.yucca.adminapi.util.Constants;

public interface SmartobjectMapper {

	String SMARTOBJECT_TABLE = Constants.SCHEMA_DB + ".yucca_smart_object";
	
	String TENANT_SMARTOBJECT_TABLE = Constants.SCHEMA_DB + ".yucca_r_tenant_smart_object";
	
	
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