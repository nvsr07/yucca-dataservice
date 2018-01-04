package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.Stream;
import org.csi.yucca.adminapi.model.StreamInternal;
import org.csi.yucca.adminapi.model.StreamToUpdate;
import org.csi.yucca.adminapi.util.Constants;

public interface StreamMapper {

	String STREAM_TABLE = Constants.SCHEMA_DB + "yucca_stream";
	String STREAM_INTERNAL_TABLE = Constants.SCHEMA_DB + "yucca_r_stream_internal";


	/*************************************************************************
	 * 					clone INTERNAL STREAM con new version
	 * ***********************************************************************/
	public static final String CLONE_STREAM_INTERNAL = 
	" INSERT INTO " + STREAM_INTERNAL_TABLE + "( " +
            " id_data_sourceinternal, datasourceversioninternal, idstream, " + 
            " stream_alias ) " +
	" SELECT id_data_sourceinternal, #{newDataSourceVersion}, idstream, " + 
            " stream_alias " +
	" FROM " +  STREAM_INTERNAL_TABLE +       
	" WHERE idstream = #{idStream} and datasourceversion=#{currentDataSourceVersion}";
	@Insert(CLONE_STREAM_INTERNAL)
	int cloneStreamInternal(@Param("newDataSourceVersion") Integer newDataSourceVersion,
			@Param("currentDataSourceVersion") Integer currentDataSourceVersion, 
			@Param("idStream") Integer idStream);

	
	/*************************************************************************
	 * 					clone STREAM con new version
	 * ***********************************************************************/
	public static final String CLONE_STREAM = 
	" INSERT INTO " + STREAM_TABLE + "( " +
            " id_data_source, datasourceversion, idstream, streamcode, streamname," + 
            " publishstream, savedata, fps, internalquery, twtquery, twtgeoloclat," + 
            " twtgeoloclon, twtgeolocradius, twtgeolocunit, twtlang, twtlocale," + 
            " twtcount, twtresulttype, twtuntil, twtratepercentage, twtlastsearchid," + 
            " id_smart_object ) " +
	" SELECT id_data_source, #{newDataSourceVersion}, , idstream, streamcode, streamname," + 
            " publishstream, savedata, fps, internalquery, twtquery, twtgeoloclat," + 
            " twtgeoloclon, twtgeolocradius, twtgeolocunit, twtlang, twtlocale," + 
            " twtcount, twtresulttype, twtuntil, twtratepercentage, twtlastsearchid," + 
            " id_smart_object " +
	" FROM " +  STREAM_TABLE +       
	" WHERE idstream = #{idStream} and datasourceversion=#{currentDataSourceVersion}";
	@Insert(CLONE_STREAM)
	int cloneStream(@Param("newDataSourceVersion") Integer newDataSourceVersion,
			@Param("currentDataSourceVersion") Integer currentDataSourceVersion, 
			@Param("idStream") Integer idStream);
	
	
	/*************************************************************************
	 * 
	 * 					DELETE STREAM INTERNAL
	 * 
	 * ***********************************************************************/
	public static final String DELETE_STREAM_INTERNAL =
			"DELETE from " + STREAM_INTERNAL_TABLE + " WHERE id_data_source = #{idDataSource} and datasourceversion =#{dataSourceVersion}";
	@Delete(DELETE_STREAM_INTERNAL)
	int deleteStreamInternal(@Param("idDataSource") Integer idDataSource, @Param("dataSourceVersion") Integer dataSourceVersion);	
	
	
	/*************************************************************************
	 * 
	 * 					UPDATE STREAM
	 * 
	 * ***********************************************************************/
	public static final String UPDATE_STREAM =
    " UPDATE " + STREAM_TABLE +
			" SET streamname=#{streamname}, " +
			" publishstream=#{publishstream}, " +    
			" savedata=#{savedata}, " +
			" fps=#{fps}, " +
			" internalquery=#{internalquery}, " +
			" twtquery=#{twtquery}, " +
			" twtgeoloclat=#{twtgeoloclat}, " +
			" twtgeoloclon=#{twtgeoloclon}, " +
			" twtgeolocradius=#{twtgeolocradius}, " +
			" twtgeolocunit=#{twtgeolocunit}, " +
			" twtlang=#{twtlang}, " +
			" twtlocale=#{twtlocale}, " +
			" twtcount=#{twtcount}, " +
			" twtresulttype=#{twtresulttype}, " +
			" twtuntil=#{twtuntil}, " +
			" twtratepercentage=#{twtratepercentage}, " +     
			" twtlastsearchid=#{twtlastsearchid} " +
			" WHERE id_data_source=#{idDataSource} and datasourceversion=#{datasourceversion} ";
	@Update(UPDATE_STREAM)
	int updateStream(Stream stream);

	
	/*************************************************************************
	 * 
	 * 					SELECT STREAM TO UPDATE
	 * 
	 * ***********************************************************************/
	public static final String SELECT_STREAM_TO_UPDATE =
			" SELECT " + 
			" yucca_stream.idstream, " + 
			" yucca_stream.id_data_source, " +
			" yucca_stream.streamcode, " + 
			" yucca_stream.streamname, " + 
			" yucca_stream.datasourceversion, " +
			" yucca_stream.savedata, " +
			" yucca_d_status.statuscode, " + 
			" yucca_d_status.description statusDescription, " + 
			" yucca_d_status.id_status, " + 
			" yucca_organization.organizationcode, " + 
			" yucca_organization.description organizationDescription, " + 
			" yucca_organization.id_organization, " + 
			" yucca_r_tenant_data_source.isactive, " + 
			" yucca_r_tenant_data_source.ismanager, " + 
			" yucca_tenant.tenantcode, " + 
			" yucca_tenant.name, " + 
			" yucca_tenant.description tenantDescription, " + 
			" yucca_tenant.id_tenant, " + 
			" yucca_smart_object.id_smart_object, " +
			" yucca_smart_object.soCode " +
			" FROM " +  STREAM_TABLE + " yucca_stream " + 
			" INNER JOIN " + DataSourceMapper.DATA_SOURCE_TABLE  + " yucca_data_source ON yucca_stream.id_data_source = yucca_data_source.id_data_source AND yucca_stream.datasourceversion = yucca_data_source.datasourceversion " +
			" INNER JOIN " + OrganizationMapper.ORGANIZATION_TABLE + " yucca_organization ON  yucca_data_source.id_organization = yucca_organization.id_organization " +
			" INNER JOIN " + SmartobjectMapper.STATUS_TABLE + " yucca_d_status ON yucca_data_source.id_status = yucca_d_status.id_status " + 
			" INNER JOIN " + SmartobjectMapper.SMARTOBJECT_TABLE + " yucca_smart_object ON yucca_stream.id_smart_object = yucca_smart_object.id_smart_object " +
			" LEFT JOIN " + TenantMapper.R_TENANT_DATA_SOURCE_TABLE  + " yucca_r_tenant_data_source ON yucca_r_tenant_data_source.id_data_source = yucca_data_source.id_data_source AND " +
			"                     yucca_r_tenant_data_source.datasourceversion = yucca_data_source.datasourceversion AND " +
			"                     yucca_r_tenant_data_source.isactive = 1 AND yucca_r_tenant_data_source.ismanager = 1 " +
			" LEFT JOIN " + TenantMapper.TENANT_TABLE + " yucca_tenant ON yucca_tenant.id_tenant = yucca_r_tenant_data_source.id_tenant " +
			" WHERE (yucca_data_source.id_data_source, yucca_data_source.datasourceversion) IN " +
			
			"    (select id_data_source, max(datasourceversion) from " + DataSourceMapper.DATA_SOURCE_TABLE + " where id_data_source = yucca_stream.id_data_source group by id_data_source) " +

			"  AND yucca_organization.organizationcode = #{organizationCode} " +
			
			"<if test=\"tenantCodeManager != null\">" +
			" AND yucca_tenant.tenantcode =  #{tenantCodeManager} " +
			"</if>" +
			
			"  AND (yucca_data_source.visibility = 'public' OR " +
			"   EXISTS ( " +
			"   SELECT yucca_tenant.tenantcode " + 
			"   FROM " +  TenantMapper.TENANT_TABLE  + " yucca_tenant, " + TenantMapper.R_TENANT_DATA_SOURCE_TABLE + " yucca_r_tenant_data_source " +
			"   WHERE yucca_tenant.id_tenant = yucca_r_tenant_data_source.id_tenant " + 
			"   AND yucca_r_tenant_data_source.id_data_source = yucca_data_source.id_data_source AND " +
			"       yucca_r_tenant_data_source.datasourceversion = yucca_data_source.datasourceversion AND " +
			"       yucca_r_tenant_data_source.isactive = 1 AND tenantcode IN ("
			
			+ " <foreach item=\"authorizedTenantCode\" separator=\",\" index=\"index\" collection=\"userAuthorizedTenantCodeList\">"
			+ "#{authorizedTenantCode}"
			+ " </foreach>"
			
			+ ") " + 
			"   ) " +
			"   ) " +
			" AND yucca_stream.idstream = #{idStream} ";			
	@Results({
		@Result(property = "idTenant", column = "id_tenant"),
		@Result(property = "idStream", column = "idstream"),
		@Result(property = "idDataSource", column = "id_data_source"),
		@Result(property = "streamCode", column = "streamcode"),
		@Result(property = "streamName", column = "streamname"),
		@Result(property = "dataSourceVersion", column = "datasourceversion"),
		@Result(property = "statusCode", column = "statuscode"),
		@Result(property = "statusDescription", column = "statusDescription"),
		@Result(property = "idStatus", column = "id_status"),
		@Result(property = "organizationCode", column = "organizationcode"),
		@Result(property = "idOrganization", column = "id_organization"),
		@Result(property = "dataSourceIsActive", column = "isactive"),
		@Result(property = "dataSourceIsManager", column = "ismanager"),
		@Result(property = "tenantCode", column = "tenantcode"),
		@Result(property = "tenantName", column = "name"),
		@Result(property = "idSmartObject", column = "id_smart_object"),
		@Result(property = "smartObjectCode", column = "soCode"),
		@Result(property = "saveData", column = "savedata")
      })	
	@Select({"<script>", SELECT_STREAM_TO_UPDATE, "</script>"}) 
	StreamToUpdate selectStreamToUpdate(@Param("tenantCodeManager") String tenantCodeManager,@Param("idStream") Integer idStream,
		      @Param("organizationCode") String organizationCode, @Param("userAuthorizedTenantCodeList") List<String> userAuthorizedTenantCodeList);	
	
	
	/*************************************************************************
	 * 
	 * 					SELECT INTERNAL STREAMs
	 * 
	 * ***********************************************************************/
	public static final String SELECT_INTERNAL_STREAM = 
	" select yucca_tenant.id_tenant, " +
			" yucca_tenant.description tenant_description, " +
			" yucca_tenant.tenantcode, " +
			" yucca_tenant.name tenant_name, " +
			" yucca_organization.id_organization, " + 
			" yucca_organization.description organization_description, " +
			" yucca_organization.organizationcode, " +
			" yucca_stream.idstream, " +
			" yucca_stream.datasourceversion, " +
			" yucca_stream.streamcode, " +
			" yucca_stream.streamname, " +
			" yucca_smart_object.id_smart_object, " +
			" yucca_smart_object.socode, " +
			" yucca_smart_object.name smart_object_name, " +
			" yucca_smart_object.slug smart_object_slug, " +
			" yucca_smart_object.description smart_object_description, " +
			" yucca_d_so_category.socategorycode smart_object_category_code, " + 
			" yucca_d_so_category.description smart_object_category_description, " +
			" yucca_d_so_category.id_so_category, " +
			" yucca_d_so_type.sotypecode, " +
			" yucca_d_so_type.description smart_object_type_description, " +
			" yucca_d_so_type.id_so_type, " +
	  
			" (select array_to_json(array_agg(row_to_json(comp))) from " + 
			" ( select yucca_component.*, " +
			" yucca_d_phenomenon.*, " +
			" yucca_d_data_type.id_data_type dt_id_data_type, " +
			" yucca_d_data_type.datatypecode dt_datatypecode, " +
			" yucca_d_data_type.description dt_description, " +
			" yucca_d_measure_unit.* " +
			" from " + ComponentMapper.COMPONENT_TABLE  + " yucca_component " +
			" LEFT JOIN " +  PhenomenonMapper.PHENOMENON_TABLE  + " yucca_d_phenomenon ON yucca_component.id_phenomenon = yucca_d_phenomenon.id_phenomenon " +
			" LEFT JOIN " + DataTypeMapper.DATA_TYPE_TABLE + " yucca_d_data_type ON yucca_component.id_data_type = yucca_d_data_type.id_data_type " +
			" LEFT JOIN " + MeasureUnitMapper.MEASURE_UNIT_TABLE + " yucca_d_measure_unit ON yucca_component.id_measure_unit = yucca_d_measure_unit.id_measure_unit " +
			" where " +
			" yucca_data_source.id_data_source = yucca_component.id_data_source AND " +
			" yucca_data_source.datasourceversion = yucca_component.datasourceversion) comp " +
			" ) components " +  
			" from " +  STREAM_INTERNAL_TABLE + " yucca_r_stream_internal " + 
			" JOIN " + STREAM_TABLE + " yucca_stream ON yucca_r_stream_internal.idstream = yucca_stream.idstream " +
			" JOIN " + DataSourceMapper.DATA_SOURCE_TABLE + " yucca_data_source ON yucca_data_source.id_data_source = yucca_stream.id_data_source " +
			" AND yucca_data_source.datasourceversion = yucca_stream.datasourceversion " +
			" JOIN " +  SmartobjectMapper.SMARTOBJECT_TABLE + " yucca_smart_object ON yucca_stream.id_smart_object = yucca_smart_object.id_smart_object " +
			" JOIN " + SoCategoryMapper.SO_CATEGORY_TYPE_TABLE  + " yucca_d_so_category ON yucca_smart_object.id_so_category = yucca_d_so_category.id_so_category " +
			" JOIN " + SoTypeMapper.SO_TYPE_TABLE + " yucca_d_so_type ON yucca_smart_object.id_so_type = yucca_d_so_type.id_so_type " +
			" JOIN " +  TenantMapper.R_TENANT_DATA_SOURCE_TABLE  + " yucca_r_tenant_data_source ON yucca_r_tenant_data_source.id_data_source = yucca_data_source.id_data_source AND " +
			" yucca_r_tenant_data_source.datasourceversion = yucca_data_source.datasourceversion " + 
			" AND	  yucca_r_tenant_data_source.isactive = 1 AND yucca_r_tenant_data_source.ismanager = 1 " +
			" JOIN " + TenantMapper.TENANT_TABLE  + " yucca_tenant ON yucca_tenant.id_tenant = yucca_r_tenant_data_source.id_tenant " +
			" JOIN " + OrganizationMapper.ORGANIZATION_TABLE + " yucca_organization ON  yucca_data_source.id_organization = yucca_organization.id_organization " +
			" WHERE " + 
			" yucca_r_stream_internal.id_data_sourceinternal = #{idDataSource} AND " + //-- <--Esempio: id_data_source " + 
			" yucca_r_stream_internal.datasourceversioninternal = #{dataSourceVersion} AND " + // -- <--Esempio: datasourceversion " +
			" (yucca_data_source.id_data_source, yucca_data_source.datasourceversion) IN  " +
			" (select id_data_source, max(datasourceversion) from int_yucca.yucca_data_source  where id_data_source = yucca_stream.id_data_source group by id_data_source) ";	
	@Results({
		@Result(property = "idTenant", column = "id_tenant"),
		@Result(property = "tenantDescription", column = "tenant_description"),
		@Result(property = "tenantCode", column = "tenantcode"),
		@Result(property = "tenantName", column = "tenant_name"),
		@Result(property = "idOrganization", column = "id_organization"),  
		@Result(property = "organizationDescription", column = "organization_description"), 
		@Result(property = "organizationCode", column = "organizationcode"), 
        //@Result(property = "idStream", column = "idstream"), 
        //@Result(property = "idDataSource", column = "id_data_source"),
        //@Result(property = "streamCode", column = "streamcode"), 
        //@Result(property = "streamName", column = "streamname"), 
        //@Result(property = "streamSaveData", column = "savedata"),
        //@Result(property = "dataSourceVersion", column = "datasourceversion"), 
		@Result(property = "idSmartObject", column = "id_smart_object"), 
		@Result(property = "socode", column = "smartObjectCode"),
		@Result(property = "smartObjectName", column = "smart_object_name"), 
		@Result(property = "smartObjectSlug", column = "smart_object_slug"), 
		@Result(property = "smartObjectDescription", column = "smart_object_description"), 
		@Result(property = "smartObjectCategoryCode", column = "smart_object_category_code"),  
		@Result(property = "smartObjectCategoryDescription", column = "smart_object_category_description"), 
		@Result(property = "idSoCategory", column = "id_so_category"), 
		@Result(property = "sotypecode", column = "soTypeCode"),
		@Result(property = "smartObjectTypeDescription", column = "smart_object_type_description"), 
		@Result(property = "idSoType", column = "id_so_type")
      })	
	@Select({"<script>", SELECT_INTERNAL_STREAM, "</script>"}) 
	List<DettaglioStream> selectInternalStream( @Param("idDataSource") Integer idDataSource, @Param("dataSourceVersion") Integer dataSourceVersion);	

	
	/*************************************************************************
	 * 
	 * 					SELECT STREAM
	 * 
	 * ***********************************************************************/
	public static final String SELECT_STREAM = 
			" SELECT yucca_stream.idstream, "
			+ "yucca_stream.id_data_source, "
			+ "yucca_stream.streamcode, "
			+ "yucca_stream.streamname, "
			+ "yucca_stream.savedata, "
			+ "yucca_stream.datasourceversion, " 
			+ "yucca_stream.fps, " +
			" (select count(*) from " + STREAM_INTERNAL_TABLE + " yucca_r_stream_internal, " + DataSourceMapper.DATA_SOURCE_TABLE + " d1  where " +
			" yucca_r_stream_internal.idstream = yucca_stream.idstream " +
			" AND d1.id_data_source = yucca_r_stream_internal.id_data_sourceinternal " + 
			" AND d1.datasourceversion = yucca_r_stream_internal.datasourceversioninternal " +  
			" AND  (d1.id_data_source, d1.datasourceversion) IN " + 
			" (select id_data_source, max(datasourceversion) from " +  DataSourceMapper.DATA_SOURCE_TABLE
			+ "  where id_data_source = yucca_r_stream_internal.id_data_sourceinternal group by id_data_source) " +
			" ) as usedInInternalCount, " +
			" (select count(*) from " + STREAM_TABLE + " s, " +  DataSourceMapper.DATA_SOURCE_TABLE + " d2  where " +  
			" s.id_smart_object = yucca_stream.id_smart_object " +
			" AND d2.id_data_source = s.id_data_source " +
			" AND d2.datasourceversion = s.datasourceversion " + 
			" AND  (d2.id_data_source, d2.datasourceversion) IN " + 
			" (select id_data_source, max(datasourceversion) from " + DataSourceMapper.DATA_SOURCE_TABLE + "  where id_data_source = s.id_data_source group by id_data_source) " +
			" ) as streamsCountBySO, " +
			" yucca_stream.internalquery,	"
			+ "yucca_stream.twtquery, "
			+ "yucca_stream.twtgeoloclat, "
			+ "yucca_stream.twtgeoloclon, "
			+ "yucca_stream.twtgeolocradius, "
			+ "yucca_stream.twtgeolocunit, " +			
			" yucca_stream.twtlang, "
			+ "yucca_stream.twtlocale, "
			+ "yucca_stream.twtcount, "
			+ "yucca_stream.twtresulttype, "
			+ "yucca_stream.twtuntil, "
			+ "yucca_stream.twtratepercentage, " +			
			" yucca_stream.twtlastsearchid, "
			+ "yucca_data_source.visibility data_source_visibility, "
			+ "yucca_data_source.copyright data_source_copyright, "
			+ "yucca_data_source.unpublished data_source_unpublished, "
			+ "yucca_data_source.registrationdate data_source_registration_date, " + 
			" yucca_data_source.isopendata data_source_isopendata, "
			+ "yucca_data_source.externalreference data_source_external_reference, "
			+ "yucca_data_source.opendataauthor data_source_open_data_author, "
			+ "yucca_data_source.opendataupdatedate data_source_open_data_update_date, " + 
			" yucca_data_source.opendatalanguage data_source_open_data_language, "
			+ "yucca_data_source.lastupdate data_source_last_update, "
			+ "yucca_data_source.disclaimer data_source_disclaimer, "
			+ "yucca_data_source.requestername data_source_requester_name, "
			+ "yucca_data_source.requestersurname data_source_requester_surname, " +			
			" yucca_data_source.requestermail data_source_requester_mail, "
			+ "yucca_data_source.privacyacceptance data_source_privacy_acceptance, "
			+ "yucca_data_source.icon data_source_icon, "
			+ "yucca_d_status.statuscode, "
			+ "yucca_d_status.description status_description, " + 
			" yucca_d_status.id_status, "
			+ "subdom.dom_id_domain, "
			+ "subdom.dom_langen, "
			+ "subdom.dom_langit, "
			+ "subdom.dom_domaincode, "
			+ "subdom.sub_id_subdomain, "
			+ "subdom.sub_subdomaincode, " + 
			" subdom.sub_lang_it, "
			+ "subdom.sub_lang_en, "
			+ "yucca_organization.organizationcode, "
			+ "yucca_organization.description organization_description, "
			+ "yucca_organization.id_organization, "
			+ "yucca_r_tenant_data_source.isactive data_source_is_active, " + 
			" yucca_r_tenant_data_source.ismanager data_source_is_manager, "
			+ "yucca_tenant.tenantcode, "
			+ "yucca_tenant.name tenant_name, "
			+ "yucca_tenant.description tenant_description, "
			+ "yucca_tenant.id_tenant, "
			+ "yucca_smart_object.id_smart_object, " + 
			  "yucca_smart_object.socode smart_object_code, " + 
			  
			" (select array_to_json(array_agg(row_to_json(yucca_d_tag))) from " +  DataSourceMapper.R_TAG_DATA_SOURCE_TABLE 
			+ " yucca_r_tag_data_source, " + TagMapper.TAG_TABLE  + " yucca_d_tag " +
			" where yucca_data_source.id_data_source = yucca_r_tag_data_source.id_data_source AND " +
			" yucca_data_source.datasourceversion = yucca_r_tag_data_source.datasourceversion " +
			" and yucca_r_tag_data_source.id_tag = yucca_d_tag.id_tag) tags, " +
					
			" (select (row_to_json(yucca_dcat)) from " + DcatMapper.DCAT_TABLE  + " yucca_dcat " + 
			" where yucca_dcat.id_dcat = yucca_data_source.id_dcat) dcat, " +
					
			" (select (row_to_json(yucca_d_license)) from " +  LicenseMapper.LICENSE_TABLE + " yucca_d_license " +
			" where yucca_d_license.id_license = yucca_data_source.id_license) license, " +
					
			" (select array_to_json(array_agg(row_to_json(comp))) from " + 
			" ( select yucca_component.*, yucca_d_phenomenon.*, yucca_d_data_type.id_data_type dt_id_data_type, yucca_d_data_type.datatypecode dt_datatypecode, " + 
			" yucca_d_data_type.description dt_description, yucca_d_measure_unit.* " +
			" from " + ComponentMapper.COMPONENT_TABLE  + " yucca_component " + 
			" LEFT JOIN " +  PhenomenonMapper.PHENOMENON_TABLE  + " yucca_d_phenomenon ON yucca_component.id_phenomenon = yucca_d_phenomenon.id_phenomenon " +
			" LEFT JOIN " + DataTypeMapper.DATA_TYPE_TABLE + " yucca_d_data_type ON yucca_component.id_data_type = yucca_d_data_type.id_data_type " +
			" LEFT JOIN " +  MeasureUnitMapper.MEASURE_UNIT_TABLE + " yucca_d_measure_unit ON yucca_component.id_measure_unit = yucca_d_measure_unit.id_measure_unit " +
			" where yucca_data_source.id_data_source = yucca_component.id_data_source AND " +
			" yucca_data_source.datasourceversion = yucca_component.datasourceversion) comp " +
			" ) components, " +
				
			" (select array_to_json(array_agg(row_to_json(tenantshr))) from " + 
			" ( select yucca_tenant.id_tenant, yucca_tenant.tenantcode, yucca_tenant.name, yucca_tenant.description, yucca_r_tenant_data_source.dataoptions, " + 
			" yucca_r_tenant_data_source.manageoptions " + 
			" from " +  TenantMapper.TENANT_TABLE + " yucca_tenant, " + TenantMapper.R_TENANT_DATA_SOURCE_TABLE + " yucca_r_tenant_data_source where " + 
			" yucca_tenant.id_tenant = yucca_r_tenant_data_source.id_tenant AND " +
			" yucca_r_tenant_data_source.id_data_source = yucca_data_source.id_data_source AND " +
			" yucca_r_tenant_data_source.datasourceversion = yucca_data_source.datasourceversion AND " +
			" yucca_r_tenant_data_source.isactive = 1 AND yucca_r_tenant_data_source.ismanager = 0 " +
			" ) tenantshr " +
			" ) sharing_tenant " +	
				
			" FROM " +  STREAM_TABLE + 
			" INNER JOIN " +  DataSourceMapper.DATA_SOURCE_TABLE + " yucca_data_source ON yucca_stream.id_data_source = yucca_data_source.id_data_source AND yucca_stream.datasourceversion = yucca_data_source.datasourceversion " +
			" INNER JOIN " + OrganizationMapper.ORGANIZATION_TABLE + " yucca_organization ON  yucca_data_source.id_organization = yucca_organization.id_organization " +
			" INNER JOIN " + SmartobjectMapper.STATUS_TABLE + " yucca_d_status ON yucca_data_source.id_status = yucca_d_status.id_status " + 
			" INNER JOIN " + SmartobjectMapper.SMARTOBJECT_TABLE  + " yucca_smart_object ON yucca_stream.id_smart_object = yucca_smart_object.id_smart_object " +
			" INNER JOIN (select yucca_d_domain.id_domain dom_id_domain, yucca_d_domain.langen dom_langen, yucca_d_domain.langit dom_langit, yucca_d_domain.domaincode dom_domaincode, " + 
			" yucca_d_subdomain.id_subdomain sub_id_subdomain, yucca_d_subdomain.subdomaincode sub_subdomaincode, yucca_d_subdomain.lang_it sub_lang_it, yucca_d_subdomain.lang_en sub_lang_en " + 
			" from " +  SubdomainMapper.SUBDOMAIN_TABLE + " yucca_d_subdomain INNER JOIN " + DomainMapper.DOMAIN_TABLE + " yucca_d_domain on yucca_d_subdomain.id_domain = yucca_d_domain.id_domain ) SUBDOM " +
			" on yucca_data_source.id_subdomain = SUBDOM.sub_id_subdomain " + 
			" LEFT JOIN " +  TenantMapper.R_TENANT_DATA_SOURCE_TABLE + " yucca_r_tenant_data_source ON yucca_r_tenant_data_source.id_data_source = yucca_data_source.id_data_source AND " +
			" yucca_r_tenant_data_source.datasourceversion = yucca_data_source.datasourceversion AND " +
			" yucca_r_tenant_data_source.isactive = 1 AND yucca_r_tenant_data_source.ismanager = 1 " +
			" LEFT JOIN " +  TenantMapper.TENANT_TABLE  + " yucca_tenant ON yucca_tenant.id_tenant = yucca_r_tenant_data_source.id_tenant ";

	public static final String WHERE_STREAM_START = " WHERE 1=1 ";
	
	public static final String WHERE_STREAM_MAX_VERSION =
			" AND (yucca_data_source.id_data_source, yucca_data_source.datasourceversion) IN " + 
			" (select id_data_source, max(datasourceversion) from " +  DataSourceMapper.DATA_SOURCE_TABLE
			+ "  where id_data_source = yucca_stream.id_data_source group by id_data_source) " ;
	
	public static final String WHERE_STREAM_ORGANIZATION_CODE =
			" AND yucca_organization.organizationcode = #{organizationCode} ";

	public static final String WHERE_STREAM_TENANT_MANAGER_CODE =
			"<if test=\"tenantCodeManager != null\">" +
					" AND yucca_tenant.tenantcode =  #{tenantCodeManager} " +
			"</if>";
	
	public static final String WHERE_STREAM_TENANT_VISIBILITY =
			" AND (yucca_data_source.visibility = 'public' OR " +
			" EXISTS ( " +
			" SELECT yucca_tenant.tenantcode " + 
			" FROM " +  TenantMapper.TENANT_TABLE  + " yucca_tenant, " +  TenantMapper.R_TENANT_DATA_SOURCE_TABLE + " yucca_r_tenant_data_source " +
			" WHERE yucca_tenant.id_tenant = yucca_r_tenant_data_source.id_tenant " + 
			" AND yucca_r_tenant_data_source.id_data_source = yucca_data_source.id_data_source AND " +
			" yucca_r_tenant_data_source.datasourceversion = yucca_data_source.datasourceversion AND " +
			" yucca_r_tenant_data_source.isactive = 1 AND tenantcode IN ("
			+ " <foreach item=\"authorizedTenantCode\" separator=\",\" index=\"index\" collection=\"userAuthorizedTenantCodeList\">"
			+ "#{authorizedTenantCode}"
			+ " </foreach>"
			+ ") " + 
			" ) " +
			" ) ";
	
	public static final String WHERE_STREAM_IDSTREAM = " AND yucca_stream.idstream = #{idStream} ";
	
	public static final String WHERE_STREAM_IDDATASOURCE = " AND yucca_data_source.id_data_source = #{idDataSource} ";
	
	public static final String WHERE_STREAM_DATASOURCEVERSION = " AND yucca_data_source.datasourceversion = #{dataSourceVersion} ";

	public static final String WHERE_STREAM_STREAMCODE = " AND yucca_stream.streamcode = #{streamCode} ";

	public static final String WHERE_STREAM_SOCODE = " AND yucca_smart_object.socode = #{soCode} ";
	
	@Results({
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"),
		@Result(property = "dataSourceIsopendata", column = "data_source_isopendata"),	
		@Result(property = "dataSourceExternalReference", column = "data_source_external_reference"),		
		@Result(property = "dataSourceOpenDataAuthor", column = "data_source_open_data_author"),
		@Result(property = "dataSourceOpenDataUpdateDate", column = "data_source_open_data_update_date"),
		@Result(property = "dataSourceOpenDataLanguage", column = "data_source_open_data_language"),
		@Result(property = "dataSourceLastUpdate", column = "data_source_last_update"),
		@Result(property = "dataSourceDisclaimer", column = "data_source_disclaimer"),	
		@Result(property = "dataSourceRequesterName", column = "data_source_requester_name"),	
		@Result(property = "dataSourceRequesterSurname", column = "data_source_requester_surname"),
		@Result(property = "dataSourceRequesterMail", column = "data_source_requester_mail"),	
		@Result(property = "dataSourcePrivacyAcceptance", column = "data_source_privacy_acceptance"), 			
		@Result(property = "dataSourceIcon", column = "data_source_icon"),
		
        @Result(property = "idDataSource", column = "id_data_source"),
        @Result(property = "idSmartObject", column = "id_smart_object"),
        
        //@Result(property = "idStream", column = "idstream"), 
        //@Result(property = "idDataSource", column = "id_data_source"),
        //@Result(property = "streamCode", column = "streamcode"), 
        //@Result(property = "streamName", column = "streamname"), 
        //@Result(property = "streamSaveData", column = "savedata"),
        //@Result(property = "dataSourceVersion", column = "datasourceversion"), 
        @Result(property = "fps", column = "fps"),
  	  
		@Result(property = "dataSourceVisibility", column = "data_source_visibility"), 
		@Result(property = "dataSourceUnpublished", column = "data_source_unpublished"),  
		@Result(property = "dataSourceRegistrationDate", column = "data_source_registration_date"), 
		@Result(property = "dataSourceName", column = "data_source_name"),
		
		@Result(property = "statusCode", column = "statuscode"), 
		@Result(property = "statusDescription", column = "status_description"),  
		@Result(property = "idStatus", column = "id_status"), 

		@Result(property = "domIdDomain", column = "dom_id_domain"),  
		@Result(property = "domLangEn", column = "dom_langen"),  
		@Result(property = "domLangIt", column = "dom_langit"),  
		@Result(property = "domDomainCode", column = "dom_domaincode"),  
		@Result(property = "subIdSubDomain", column = "sub_id_subdomain"), 
		@Result(property = "subSubDomainCode", column = "sub_subdomaincode"),  
		@Result(property = "subLangIt", column = "sub_lang_it"),  
		@Result(property = "subLangEn", column = "sub_lang_en"), 
  	
		@Result(property = "organizationCode", column = "organizationcode"),  
		@Result(property = "organizationDescription", column = "organization_description"),  
		@Result(property = "idOrganization", column = "id_organization"),
		
		@Result(property = "dataSourceIsActive", column = "data_source_is_active"), 
		@Result(property = "dataSourceIsManager", column = "data_source_is_manager"),
		@Result(property = "tenantCode", column = "tenantcode"),
		@Result(property = "tenantName", column = "tenant_name"),
		@Result(property = "tenantDescription", column = "tenant_description"),
		@Result(property = "idTenant", column = "id_tenant"),
		
		@Result(property = "smartObjectCode", column = "smart_object_code"),
		@Result(property = "smartObjectName", column = "smart_object_name"),
		@Result(property = "idSmartObject", column = "id_smart_object"),
		@Result(property = "smartObjectDescription", column = "smart_object_description"),
		@Result(property = "smartObjectSlug", column = "smart_object_slug"),
		  
		@Result(property = "smartObjectCategoryCode", column = "smart_object_category_code"),
		@Result(property = "smartObjectCategoryDescription", column = "smart_object_category_description"),
		@Result(property = "idSoCategory", column = "id_so_category"),
		  
		@Result(property = "soTypeCode", column = "sotypecode"),
		@Result(property = "smartObjectTypeDescription", column = "smart_object_type_description"),
		@Result(property = "idSoType", column = "id_so_type"),
		@Result(property = "sharingTenant", column = "sharing_tenant") 
      })	
	@Select({"<script>", SELECT_STREAM + WHERE_STREAM_START + WHERE_STREAM_MAX_VERSION 
						+ WHERE_STREAM_TENANT_MANAGER_CODE  
						+ WHERE_STREAM_ORGANIZATION_CODE
						+ WHERE_STREAM_TENANT_VISIBILITY
						+ WHERE_STREAM_IDSTREAM, "</script>"}) 
	DettaglioStream selectStream( @Param("tenantCodeManager") String tenantCodeManager,
							      @Param("idStream") Integer idStream,
							      @Param("organizationCode") String organizationcode,
							      @Param("userAuthorizedTenantCodeList") List<String> userAuthorizedTenantCodeList);	
	

	
	@Results({
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"),
		@Result(property = "dataSourceIsopendata", column = "data_source_isopendata"),	
		@Result(property = "dataSourceExternalReference", column = "data_source_external_reference"),		
		@Result(property = "dataSourceOpenDataAuthor", column = "data_source_open_data_author"),
		@Result(property = "dataSourceOpenDataUpdateDate", column = "data_source_open_data_update_date"),
		@Result(property = "dataSourceOpenDataLanguage", column = "data_source_open_data_language"),
		@Result(property = "dataSourceLastUpdate", column = "data_source_last_update"),
		@Result(property = "dataSourceDisclaimer", column = "data_source_disclaimer"),	
		@Result(property = "dataSourceRequesterName", column = "data_source_requester_name"),	
		@Result(property = "dataSourceRequesterSurname", column = "data_source_requester_surname"),
		@Result(property = "dataSourceRequesterMail", column = "data_source_requester_mail"),	
		@Result(property = "dataSourcePrivacyAcceptance", column = "data_source_privacy_acceptance"), 			
		@Result(property = "dataSourceIcon", column = "data_source_icon"),
		
        @Result(property = "idDataSource", column = "id_data_source"),
        @Result(property = "idSmartObject", column = "id_smart_object"),
        
        //@Result(property = "idStream", column = "idstream"), 
        //@Result(property = "idDataSource", column = "id_data_source"),
        //@Result(property = "streamCode", column = "streamcode"), 
        //@Result(property = "streamName", column = "streamname"), 
        //@Result(property = "streamSaveData", column = "savedata"),
        //@Result(property = "dataSourceVersion", column = "datasourceversion"), 
        @Result(property = "fps", column = "fps"),
  	  
		@Result(property = "dataSourceVisibility", column = "data_source_visibility"), 
		@Result(property = "dataSourceUnpublished", column = "data_source_unpublished"),  
		@Result(property = "dataSourceRegistrationDate", column = "data_source_registration_date"), 
		@Result(property = "dataSourceName", column = "data_source_name"),
		
		@Result(property = "statusCode", column = "statuscode"), 
		@Result(property = "statusDescription", column = "status_description"),  
		@Result(property = "idStatus", column = "id_status"), 

		@Result(property = "domIdDomain", column = "dom_id_domain"),  
		@Result(property = "domLangEn", column = "dom_langen"),  
		@Result(property = "domLangIt", column = "dom_langit"),  
		@Result(property = "domDomainCode", column = "dom_domaincode"),  
		@Result(property = "subIdSubDomain", column = "sub_id_subdomain"), 
		@Result(property = "subSubDomainCode", column = "sub_subdomaincode"),  
		@Result(property = "subLangIt", column = "sub_lang_it"),  
		@Result(property = "subLangEn", column = "sub_lang_en"), 
  	
		@Result(property = "organizationCode", column = "organizationcode"),  
		@Result(property = "organizationDescription", column = "organization_description"),  
		@Result(property = "idOrganization", column = "id_organization"),
		
		@Result(property = "dataSourceIsActive", column = "data_source_is_active"), 
		@Result(property = "dataSourceIsManager", column = "data_source_is_manager"),
		@Result(property = "tenantCode", column = "tenantcode"),
		@Result(property = "tenantName", column = "tenant_name"),
		@Result(property = "tenantDescription", column = "tenant_description"),
		@Result(property = "idTenant", column = "id_tenant"),
		
		@Result(property = "smartObjectCode", column = "smart_object_code"),
		@Result(property = "smartObjectName", column = "smart_object_name"),
		@Result(property = "idSmartObject", column = "id_smart_object"),
		@Result(property = "smartObjectDescription", column = "smart_object_description"),
		@Result(property = "smartObjectSlug", column = "smart_object_slug"),
		  
		@Result(property = "smartObjectCategoryCode", column = "smart_object_category_code"),
		@Result(property = "smartObjectCategoryDescription", column = "smart_object_category_description"),
		@Result(property = "idSoCategory", column = "id_so_category"),
		  
		@Result(property = "soTypeCode", column = "sotypecode"),
		@Result(property = "smartObjectTypeDescription", column = "smart_object_type_description"),
		@Result(property = "idSoType", column = "id_so_type"),
		@Result(property = "sharingTenant", column = "sharing_tenant")
      })	
	@Select({"<script>", SELECT_STREAM + WHERE_STREAM_START + WHERE_STREAM_IDDATASOURCE
						+ WHERE_STREAM_DATASOURCEVERSION, "</script>"}) 
	DettaglioStream selectStreamByDatasource( @Param("idDataSource") Integer idDataSource,
							      @Param("dataSourceVersion") Integer dataSourceVersion
							      );	
	
	
	/*************************************************************************
	 * 
	 * 					SELECT STREAMs
	 * 
	 * ***********************************************************************/
	public static final String SELECT_STREAMS = 
			" SELECT " + 
			" STREAM.idstream, " + 
			" STREAM.id_data_source, " +
			" STREAM.streamcode, " + 
			" STREAM.streamname, " + 
			" STREAM.savedata stream_save_data, " + 
			" STREAM.datasourceversion, " + 
			" DATA_SOURCE.visibility data_source_visibility, " +
			" DATA_SOURCE.name data_source_name, " +
			" DATA_SOURCE.unpublished data_source_unpublished, " + 
			" DATA_SOURCE.registrationdate data_source_registration_date, " +
			" YUCCA_STATUS.statuscode, " + 
			" YUCCA_STATUS.description status_description, " + 
			" YUCCA_STATUS.id_status, " +
			" subdom.dom_id_domain, " + 
			" subdom.dom_langen, " + 
			" subdom.dom_langit, " + 
			" subdom.dom_domaincode, " + 
			" subdom.sub_id_subdomain, " + 
			" subdom.sub_subdomaincode, " + 
			" subdom.sub_lang_it, " + 
			" subdom.sub_lang_en, " +
			" ORGANIZATION.organizationcode, " + 
			" ORGANIZATION.description organization_description, " + 
			" ORGANIZATION.id_organization, " + 
			" TENANT_DATA_SOURCE.isactive data_source_is_active, " + 
			" TENANT_DATA_SOURCE.ismanager data_source_is_manager, " + 
			" TENANT.tenantcode, " + 
			" TENANT.name tenant_name, " + 
			" TENANT.description tenant_description, " + 
			" TENANT.id_tenant, " + 
			" SMART_OBJ.socode smart_object_code, " + 
			" SMART_OBJ.name smart_object_name, " + 
			" SMART_OBJ.id_smart_object, " + 
			" SMART_OBJ.description smart_object_description, " + 
			" SMART_OBJ.slug smart_object_slug, " +
			
			" SO_CAT.socategorycode smart_object_category_code, " + 
			" SO_CAT.description smart_object_category_description, " + 
			" SO_CAT.id_so_category, " + 
			
			" SO_TYPE.sotypecode, " + 
			" SO_TYPE.description smart_object_type_description, " + 
			" SO_TYPE.id_so_type, " +
			" (select array_to_json(array_agg(row_to_json(yucca_d_tag))) from " +  DataSourceMapper.R_TAG_DATA_SOURCE_TABLE + " yucca_r_tag_data_source, " + TagMapper.TAG_TABLE + " yucca_d_tag " +
			" where DATA_SOURCE.id_data_source = yucca_r_tag_data_source.id_data_source AND " +
			" DATA_SOURCE.datasourceversion = yucca_r_tag_data_source.datasourceversion " +
			" and yucca_r_tag_data_source.id_tag = yucca_d_tag.id_tag) tags " +
			" FROM " +  STREAM_TABLE + " STREAM " +
			" INNER JOIN " + DataSourceMapper.DATA_SOURCE_TABLE + " DATA_SOURCE ON STREAM.id_data_source = DATA_SOURCE.id_data_source AND STREAM.datasourceversion = DATA_SOURCE.datasourceversion " +
			" INNER JOIN " +  OrganizationMapper.ORGANIZATION_TABLE  + " ORGANIZATION ON  DATA_SOURCE.id_organization = ORGANIZATION.id_organization " +
			" INNER JOIN " +  SmartobjectMapper.STATUS_TABLE  + " YUCCA_STATUS ON DATA_SOURCE.id_status = YUCCA_STATUS.id_status " + 
			" INNER JOIN " +  SmartobjectMapper.SMARTOBJECT_TABLE  + " SMART_OBJ ON STREAM.id_smart_object = SMART_OBJ.id_smart_object " +
			" INNER JOIN " +  SoCategoryMapper.SO_CATEGORY_TYPE_TABLE + " SO_CAT ON SMART_OBJ.id_so_category = SO_CAT.id_so_category " +
			" INNER JOIN " +  SoTypeMapper.SO_TYPE_TABLE  + " SO_TYPE ON SMART_OBJ.id_so_type = SO_TYPE.id_so_type " +
			" INNER JOIN (select " + 
			" Y_DOMAIN.id_domain dom_id_domain, Y_DOMAIN.langen dom_langen, " + 
			" Y_DOMAIN.langit dom_langit, Y_DOMAIN.domaincode dom_domaincode, " + 
			" SUBDOMAIN.id_subdomain sub_id_subdomain, SUBDOMAIN.subdomaincode sub_subdomaincode, " + 
			" SUBDOMAIN.lang_it sub_lang_it, SUBDOMAIN.lang_en sub_lang_en " + 
			" from " + SubdomainMapper.SUBDOMAIN_TABLE + " SUBDOMAIN INNER JOIN " +  DomainMapper.DOMAIN_TABLE + " Y_DOMAIN on SUBDOMAIN.id_domain = Y_DOMAIN.id_domain ) SUBDOM " +
			" on DATA_SOURCE.id_subdomain = SUBDOM.sub_id_subdomain " + 
			" LEFT JOIN " +  TenantMapper.R_TENANT_DATA_SOURCE_TABLE + " TENANT_DATA_SOURCE ON TENANT_DATA_SOURCE.id_data_source = DATA_SOURCE.id_data_source AND " +
			" TENANT_DATA_SOURCE.datasourceversion = DATA_SOURCE.datasourceversion AND " +
			" TENANT_DATA_SOURCE.isactive = 1 AND TENANT_DATA_SOURCE.ismanager = 1 " +
			" LEFT JOIN " +  TenantMapper.TENANT_TABLE  + " TENANT ON TENANT.id_tenant = TENANT_DATA_SOURCE.id_tenant " +
			" WHERE " + 
			" (DATA_SOURCE.id_data_source, DATA_SOURCE.datasourceversion) IN " + 
			" (select id_data_source, max(datasourceversion) from " +  DataSourceMapper.DATA_SOURCE_TABLE +
			"  where id_data_source = STREAM.id_data_source group by id_data_source) " +
			"<if test=\"tenantCodeManager != null\">" +
			" AND TENANT.tenantcode = #{tenantCodeManager} " +
			"</if>" +
			" AND (DATA_SOURCE.visibility = 'public' OR " +
			" EXISTS ( " +
			" SELECT TENANT.tenantcode " + 
			" FROM " + TenantMapper.TENANT_TABLE + " TENANT, " + TenantMapper.R_TENANT_DATA_SOURCE_TABLE + " TENANT_DATASOURCE " +
			" WHERE TENANT.id_tenant = TENANT_DATASOURCE.id_tenant " + 
			" AND TENANT_DATASOURCE.id_data_source = DATA_SOURCE.id_data_source AND " +
			" TENANT_DATASOURCE.datasourceversion = DATA_SOURCE.datasourceversion AND " +
			" TENANT_DATASOURCE.isactive = 1 " +
			"<if test=\"userAuthorizedTenantCodeList != null\">" +
			" AND tenantcode IN (" 
			
			+ " <foreach item=\"authorizedTenantCode\" separator=\",\" index=\"index\" collection=\"userAuthorizedTenantCodeList\">"
			+ "#{authorizedTenantCode}"
			+ " </foreach>"	
			+ ") " +
			"</if>" +
			
			" ) " +
			" ) " +
			"<if test=\"organizationcode != null\">" +
			"and ORGANIZATION.organizationcode = #{organizationcode}" +
			"</if>" +
			
			"<if test=\"sortList != null\">" +
		      " ORDER BY " +
			
			" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
		    
			  "<if test=\"propName == 'idStream-'\">" +
		        " idstream desc" +
	          "</if>" +
	          "<if test=\"propName == 'idStream'\">" +
	            " idstream" +
              "</if>" +

              "</foreach>" +
            "</if>";			
	@Results({
        @Result(property = "idDataSource", column = "id_data_source"),
        @Result(property = "idSmartObject", column = "id_smart_object"),
        
        //@Result(property = "idStream", column = "idstream"), 
        @Result(property = "idDataSource", column = "id_data_source"),
        //@Result(property = "streamCode", column = "streamcode"), 
        //@Result(property = "streamName", column = "streamname"), 
        //@Result(property = "streamSaveData", column = "stream_save_data"),
        //@Result(property = "dataSourceVersion", column = "datasourceversion"), 
  	  
		@Result(property = "dataSourceVisibility", column = "data_source_visibility"), 
		@Result(property = "dataSourceUnpublished", column = "data_source_unpublished"),  
		@Result(property = "dataSourceRegistrationDate", column = "data_source_registration_date"), 
		@Result(property = "dataSourceName", column = "data_source_name"),
		
		@Result(property = "statusCode", column = "statuscode"), 
		@Result(property = "statusDescription", column = "status_description"),  
		@Result(property = "idStatus", column = "id_status"), 

		@Result(property = "domIdDomain", column = "dom_id_domain"),  
		@Result(property = "domLangEn", column = "dom_langen"),  
		@Result(property = "domLangIt", column = "dom_langit"),  
		@Result(property = "domDomainCode", column = "dom_domaincode"),  
		@Result(property = "subIdSubDomain", column = "sub_id_subdomain"), 
		@Result(property = "subSubDomainCode", column = "sub_subdomaincode"),  
		@Result(property = "subLangIt", column = "sub_lang_it"),  
		@Result(property = "subLangEn", column = "sub_lang_en"), 
  	
		@Result(property = "organizationCode", column = "organizationcode"),  
		@Result(property = "organizationDescription", column = "organization_description"),  
		@Result(property = "idOrganization", column = "id_organization"),
		
		@Result(property = "dataSourceIsActive", column = "data_source_is_active"), 
		@Result(property = "dataSourceIsManager", column = "data_source_is_manager"),
		@Result(property = "tenantCode", column = "tenantcode"),
		@Result(property = "tenantName", column = "tenant_name"),
		@Result(property = "tenantDescription", column = "tenant_description"),
		@Result(property = "idTenant", column = "id_tenant"),
		
		@Result(property = "smartObjectCode", column = "smart_object_code"),
		@Result(property = "smartObjectName", column = "smart_object_name"),
		@Result(property = "idSmartObject", column = "id_smart_object"),
		@Result(property = "smartObjectDescription", column = "smart_object_description"),
		@Result(property = "smartObjectSlug", column = "smart_object_slug"),
		  
		@Result(property = "smartObjectCategoryCode", column = "smart_object_category_code"),
		@Result(property = "smartObjectCategoryDescription", column = "smart_object_category_description"),
		@Result(property = "idSoCategory", column = "id_so_category"),
		  
		@Result(property = "soTypeCode", column = "sotypecode"),
		@Result(property = "smartObjectTypeDescription", column = "smart_object_type_description"),
		@Result(property = "idSoType", column = "id_so_type")
      })	
	@Select({"<script>", SELECT_STREAMS, "</script>"}) 
	List<DettaglioStream> selectStreams( @Param("tenantCodeManager") String tenantCodeManager,
										 @Param("organizationcode") String organizationcode,
										 @Param("sortList") List<String> sortList, 
			                             @Param("userAuthorizedTenantCodeList") List<String> userAuthorizedTenantCodeList);	
	
	
	/*************************************************************************
	 * 
	 * 					INSERT STREAM-INTERNAL
	 * 
	 * ***********************************************************************/
	public static final String INSERT_STREAM_INTERNAL = 
	" INSERT INTO " + STREAM_INTERNAL_TABLE
	+ "( id_data_sourceinternal, datasourceversioninternal, idstream,  stream_alias) "
	+ "VALUES (#{idDataSourceinternal}, #{datasourceversioninternal}, #{idstream},  #{streamAlias})";
	@Insert(INSERT_STREAM_INTERNAL)
	int insertStreamInternal(StreamInternal streamInternal);
	
	/*************************************************************************
	 * 
	 * 					INSERT STREAM
	 * 
	 * ***********************************************************************/
	public static final String INSERT_STREAM = 
	" INSERT INTO " + STREAM_TABLE + "( id_data_source, datasourceversion, streamcode, streamname, "
	+ "publishstream, savedata, fps, internalquery, twtquery, twtgeoloclat, twtgeoloclon, twtgeolocradius, "
	+ "twtgeolocunit, twtlang, twtlocale, twtcount, twtresulttype, twtuntil, twtratepercentage, twtlastsearchid,  id_smart_object) "
	+ "VALUES (#{idDataSource},#{datasourceversion},#{streamcode},#{streamname},#{publishstream},#{savedata},#{fps},"
	+ "#{internalquery},#{twtquery},#{twtgeoloclat},#{twtgeoloclon},#{twtgeolocradius},#{twtgeolocunit},#{twtlang},"
	+ "#{twtlocale},#{twtcount},#{twtresulttype},#{twtuntil},#{twtratepercentage},#{twtlastsearchid},#{idSmartObject})";	
	@Insert(INSERT_STREAM)
	@Options(useGeneratedKeys=true, keyProperty="idstream")
	int insertStream(Stream stream);
	
	
	
	/*************************************************************************
	 * 
	 * 					SELECT COUNT OF TENANT STREAM
	 * 
	 * ***********************************************************************/
	public static final String SELECT_COUNT_OF_TENANT_STREAM = 
			" select count(*) from ( "
			+ "SELECT distinct idstream "
				+ "FROM " + STREAM_TABLE + ", " + TenantMapper.R_TENANT_DATA_SOURCE_TABLE
				+ " WHERE yucca_stream.id_data_source = yucca_r_tenant_data_source.id_data_source AND "
					+ "yucca_stream.datasourceversion = yucca_r_tenant_data_source.datasourceversion AND "
					+ "yucca_r_tenant_data_source.id_tenant = #{idTenant} AND "
					+ "yucca_r_tenant_data_source.ismanager = 1 AND "
					+ " yucca_r_tenant_data_source.isactive = 1 ) s";
	@Select(SELECT_COUNT_OF_TENANT_STREAM)
	Integer selectCountOfTenantStream( @Param("idTenant") Integer idTenant);	

	
	/*************************************************************************
	 * 
	 * 					SELECT STREAM BY STREAMCODE AND ID_SMART_OBJECT
	 * 
	 * ***********************************************************************/
	public static final String SELECT_STREAM_BY_STREAMCODE_AND_CODE_SO = 
		" SELECT id_data_source, datasourceversion, idstream, streamcode, streamname, publishstream, "
		+ "savedata, fps, internalquery, twtquery, twtgeoloclat, twtgeoloclon, twtgeolocradius, twtgeolocunit, "
		+ "twtlang, twtlocale, twtcount, twtresulttype, twtuntil, twtratepercentage, twtlastsearchid, id_smart_object "
		+ "FROM " + STREAM_TABLE
		+ " INNER JOIN " +  SmartobjectMapper.SMARTOBJECT_TABLE  + " SMART_OBJ ON STREAM.id_smart_object = SMART_OBJ.id_smart_object "
		+ " where streamcode = #{streamcode} AND "
		+ " SMART_OBJ.socode = #{soCode}";
	@Results({
        @Result(property = "idDataSource", column = "id_data_source"),
        @Result(property = "idSmartObject", column = "id_smart_object"),
      })	
	@Select(SELECT_STREAM_BY_STREAMCODE_AND_CODE_SO)
	Stream selectStreamByStreamcodeAndSoCode( @Param("streamcode") String streamcode, @Param("soCode") String soCode);	

	
	/*************************************************************************
	 * 
	 * 					SELECT STREAM BY ID_STREAM
	 * 
	 * ***********************************************************************/
	@Results({
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"),
		@Result(property = "dataSourceIsopendata", column = "data_source_isopendata"),	
		@Result(property = "dataSourceExternalReference", column = "data_source_external_reference"),		
		@Result(property = "dataSourceOpenDataAuthor", column = "data_source_open_data_author"),
		@Result(property = "dataSourceOpenDataUpdateDate", column = "data_source_open_data_update_date"),
		@Result(property = "dataSourceOpenDataLanguage", column = "data_source_open_data_language"),
		@Result(property = "dataSourceLastUpdate", column = "data_source_last_update"),
		@Result(property = "dataSourceDisclaimer", column = "data_source_disclaimer"),	
		@Result(property = "dataSourceRequesterName", column = "data_source_requester_name"),	
		@Result(property = "dataSourceRequesterSurname", column = "data_source_requester_surname"),
		@Result(property = "dataSourceRequesterMail", column = "data_source_requester_mail"),	
		@Result(property = "dataSourcePrivacyAcceptance", column = "data_source_privacy_acceptance"), 			
		@Result(property = "dataSourceIcon", column = "data_source_icon"),
		
        @Result(property = "idDataSource", column = "id_data_source"),
        @Result(property = "idSmartObject", column = "id_smart_object"),
        
        //@Result(property = "idStream", column = "idstream"), 
        //@Result(property = "idDataSource", column = "id_data_source"),
        //@Result(property = "streamCode", column = "streamcode"), 
        //@Result(property = "streamName", column = "streamname"), 
        //@Result(property = "streamSaveData", column = "savedata"),
        //@Result(property = "dataSourceVersion", column = "datasourceversion"), 
        @Result(property = "fps", column = "fps"),
  	  
		@Result(property = "dataSourceVisibility", column = "data_source_visibility"), 
		@Result(property = "dataSourceUnpublished", column = "data_source_unpublished"),  
		@Result(property = "dataSourceRegistrationDate", column = "data_source_registration_date"), 
		@Result(property = "dataSourceName", column = "data_source_name"),
		
		@Result(property = "statusCode", column = "statuscode"), 
		@Result(property = "statusDescription", column = "status_description"),  
		@Result(property = "idStatus", column = "id_status"), 

		@Result(property = "domIdDomain", column = "dom_id_domain"),  
		@Result(property = "domLangEn", column = "dom_langen"),  
		@Result(property = "domLangIt", column = "dom_langit"),  
		@Result(property = "domDomainCode", column = "dom_domaincode"),  
		@Result(property = "subIdSubDomain", column = "sub_id_subdomain"), 
		@Result(property = "subSubDomainCode", column = "sub_subdomaincode"),  
		@Result(property = "subLangIt", column = "sub_lang_it"),  
		@Result(property = "subLangEn", column = "sub_lang_en"), 
  	
		@Result(property = "organizationCode", column = "organizationcode"),  
		@Result(property = "organizationDescription", column = "organization_description"),  
		@Result(property = "idOrganization", column = "id_organization"),
		
		@Result(property = "dataSourceIsActive", column = "data_source_is_active"), 
		@Result(property = "dataSourceIsManager", column = "data_source_is_manager"),
		@Result(property = "tenantCode", column = "tenantcode"),
		@Result(property = "tenantName", column = "tenant_name"),
		@Result(property = "tenantDescription", column = "tenant_description"),
		@Result(property = "idTenant", column = "id_tenant"),
		
		@Result(property = "smartObjectCode", column = "smart_object_code"),
		@Result(property = "smartObjectName", column = "smart_object_name"),
		@Result(property = "idSmartObject", column = "id_smart_object"),
		@Result(property = "smartObjectDescription", column = "smart_object_description"),
		@Result(property = "smartObjectSlug", column = "smart_object_slug"),
		  
		@Result(property = "smartObjectCategoryCode", column = "smart_object_category_code"),
		@Result(property = "smartObjectCategoryDescription", column = "smart_object_category_description"),
		@Result(property = "idSoCategory", column = "id_so_category"),
		  
		@Result(property = "soTypeCode", column = "sotypecode"),
		@Result(property = "smartObjectTypeDescription", column = "smart_object_type_description"),
		@Result(property = "idSoType", column = "id_so_type"),
		@Result(property = "sharingTenant", column = "sharing_tenant")
      })	
	@Select({"<script>", SELECT_STREAM + WHERE_STREAM_START + WHERE_STREAM_MAX_VERSION
						+ WHERE_STREAM_IDSTREAM, "</script>"}) 
	DettaglioStream selectStreamByIdStream( @Param("idStream") Integer idStream);
	
	
	/*************************************************************************
	 * 
	 * 					SELECT STREAM BY ID_STREAM
	 * 
	 * ***********************************************************************/
	@Results({
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"),
		@Result(property = "dataSourceIsopendata", column = "data_source_isopendata"),	
		@Result(property = "dataSourceExternalReference", column = "data_source_external_reference"),		
		@Result(property = "dataSourceOpenDataAuthor", column = "data_source_open_data_author"),
		@Result(property = "dataSourceOpenDataUpdateDate", column = "data_source_open_data_update_date"),
		@Result(property = "dataSourceOpenDataLanguage", column = "data_source_open_data_language"),
		@Result(property = "dataSourceLastUpdate", column = "data_source_last_update"),
		@Result(property = "dataSourceDisclaimer", column = "data_source_disclaimer"),	
		@Result(property = "dataSourceRequesterName", column = "data_source_requester_name"),	
		@Result(property = "dataSourceRequesterSurname", column = "data_source_requester_surname"),
		@Result(property = "dataSourceRequesterMail", column = "data_source_requester_mail"),	
		@Result(property = "dataSourcePrivacyAcceptance", column = "data_source_privacy_acceptance"), 			
		@Result(property = "dataSourceIcon", column = "data_source_icon"),
		
        @Result(property = "idDataSource", column = "id_data_source"),
        @Result(property = "idSmartObject", column = "id_smart_object"),
        
        //@Result(property = "idStream", column = "idstream"), 
        //@Result(property = "idDataSource", column = "id_data_source"),
        //@Result(property = "streamCode", column = "streamcode"), 
        //@Result(property = "streamName", column = "streamname"), 
        //@Result(property = "streamSaveData", column = "savedata"),
        //@Result(property = "dataSourceVersion", column = "datasourceversion"), 
        @Result(property = "fps", column = "fps"),
  	  
		@Result(property = "dataSourceVisibility", column = "data_source_visibility"), 
		@Result(property = "dataSourceUnpublished", column = "data_source_unpublished"),  
		@Result(property = "dataSourceRegistrationDate", column = "data_source_registration_date"), 
		@Result(property = "dataSourceName", column = "data_source_name"),
		
		@Result(property = "statusCode", column = "statuscode"), 
		@Result(property = "statusDescription", column = "status_description"),  
		@Result(property = "idStatus", column = "id_status"), 

		@Result(property = "domIdDomain", column = "dom_id_domain"),  
		@Result(property = "domLangEn", column = "dom_langen"),  
		@Result(property = "domLangIt", column = "dom_langit"),  
		@Result(property = "domDomainCode", column = "dom_domaincode"),  
		@Result(property = "subIdSubDomain", column = "sub_id_subdomain"), 
		@Result(property = "subSubDomainCode", column = "sub_subdomaincode"),  
		@Result(property = "subLangIt", column = "sub_lang_it"),  
		@Result(property = "subLangEn", column = "sub_lang_en"), 
  	
		@Result(property = "organizationCode", column = "organizationcode"),  
		@Result(property = "organizationDescription", column = "organization_description"),  
		@Result(property = "idOrganization", column = "id_organization"),
		
		@Result(property = "dataSourceIsActive", column = "data_source_is_active"), 
		@Result(property = "dataSourceIsManager", column = "data_source_is_manager"),
		@Result(property = "tenantCode", column = "tenantcode"),
		@Result(property = "tenantName", column = "tenant_name"),
		@Result(property = "tenantDescription", column = "tenant_description"),
		@Result(property = "idTenant", column = "id_tenant"),
		
		@Result(property = "smartObjectCode", column = "smart_object_code"),
		@Result(property = "smartObjectName", column = "smart_object_name"),
		@Result(property = "idSmartObject", column = "id_smart_object"),
		@Result(property = "smartObjectDescription", column = "smart_object_description"),
		@Result(property = "smartObjectSlug", column = "smart_object_slug"),
		  
		@Result(property = "smartObjectCategoryCode", column = "smart_object_category_code"),
		@Result(property = "smartObjectCategoryDescription", column = "smart_object_category_description"),
		@Result(property = "idSoCategory", column = "id_so_category"),
		  
		@Result(property = "soTypeCode", column = "sotypecode"),
		@Result(property = "smartObjectTypeDescription", column = "smart_object_type_description"),
		@Result(property = "idSoType", column = "id_so_type"),
		@Result(property = "sharingTenant", column = "sharing_tenant")
      })	
	@Select({"<script>", SELECT_STREAM + WHERE_STREAM_START + WHERE_STREAM_MAX_VERSION
						+ WHERE_STREAM_SOCODE + WHERE_STREAM_STREAMCODE, "</script>"}) 
	DettaglioStream selectDettaglioStreamBySoCodeStreamCode( @Param("soCode") String soCode, @Param("streamCode") String streamCode);
	
}