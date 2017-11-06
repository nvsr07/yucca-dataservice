package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.Stream;
import org.csi.yucca.adminapi.model.StreamInternal;
import org.csi.yucca.adminapi.util.Constants;

public interface StreamMapper {

	String STREAM_TABLE = Constants.SCHEMA_DB + "yucca_stream";
	String STREAM_INTERNAL_TABLE = Constants.SCHEMA_DB + "yucca_r_stream_internal";
	
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
			" (select array_to_json(array_agg(row_to_json(yucca_d_tag))) from int_yucca.yucca_r_tag_data_source, int_yucca.yucca_d_tag " +
			" where DATA_SOURCE.id_data_source = yucca_r_tag_data_source.id_data_source AND " +
			" DATA_SOURCE.datasourceversion = yucca_r_tag_data_source.datasourceversion " +
			" and yucca_r_tag_data_source.id_tag = yucca_d_tag.id_tag) tags " +
			" FROM " + 
			" int_yucca.yucca_stream STREAM " +
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
			" TENANT_DATASOURCE.isactive = 1 AND tenantcode IN ("
			+ " <foreach item=\"authorizedTenantCode\" separator=\",\" index=\"index\" collection=\"userAuthorizedTenantCodeList\">"
			+ "#{authorizedTenantCode}"
			+ " </foreach>"
			+ ") " +
			" ) " +
			" ) " +
			"and ORGANIZATION.organizationcode = #{organizationcode}" +
			
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
        
        @Result(property = "idStream", column = "idstream"), 
        @Result(property = "idDataSource", column = "id_data_source"),
        @Result(property = "streamCode", column = "streamcode"), 
        @Result(property = "streamName", column = "streamname"), 
        @Result(property = "streamSaveData", column = "stream_save_data"),
        @Result(property = "dataSourceVersion", column = "datasourceversion"), 
  	  
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
	public static final String SELECT_STREAM_BY_STREAMCODE_AND_ID_SO = 
		" SELECT id_data_source, datasourceversion, idstream, streamcode, streamname, publishstream, "
		+ "savedata, fps, internalquery, twtquery, twtgeoloclat, twtgeoloclon, twtgeolocradius, twtgeolocunit, "
		+ "twtlang, twtlocale, twtcount, twtresulttype, twtuntil, twtratepercentage, twtlastsearchid, id_smart_object "
		+ "FROM " + STREAM_TABLE
		+ " where streamcode = #{streamcode} AND "
		+ "id_smart_object = #{idSmartObject}";
	@Results({
        @Result(property = "idDataSource", column = "id_data_source"),
        @Result(property = "idSmartObject", column = "id_smart_object"),
      })	
	@Select(SELECT_STREAM_BY_STREAMCODE_AND_ID_SO)
	Stream selectStreamByStreamcodeAndIdSmartObject( @Param("streamcode") String streamcode, @Param("idSmartObject") Integer idSmartObject);	
	
}