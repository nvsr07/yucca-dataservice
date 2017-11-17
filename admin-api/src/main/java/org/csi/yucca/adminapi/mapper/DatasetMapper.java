package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.util.Constants;

/**
 * 
 * @author gianfranco.stolfa
 *
 */
public interface DatasetMapper {
	
	public static final String DATASET_TABLE = Constants.SCHEMA_DB + "yucca_dataset";

	public static final String SELECT_DATA_SETS = 
			" SELECT " + 
			" yucca_dataset.id_data_source, " + 
			" yucca_dataset.datasourceversion, " + 
			" yucca_dataset.iddataset, " + 
			" yucca_dataset.datasetcode, " + 
			" yucca_dataset.datasetname, " + 
			" yucca_dataset.description, " + 
			
			" yucca_data_source.visibility data_source_visibility, " + 
			" yucca_data_source.unpublished data_source_unpublished, " + 
			" yucca_data_source.registrationdate data_source_registration_date, " + 
			
			" yucca_d_status.statuscode, " + 
			" yucca_d_status.description status_description, " + 
			" yucca_d_status.id_status, " +
			
			" subdom.dom_id_domain, " +
			" subdom.dom_langen, " +
			" subdom.dom_langit, " +
			" subdom.dom_domaincode, " +
			" subdom.sub_id_subdomain, " +
			" subdom.sub_subdomaincode, " +
			" subdom.sub_lang_it, " +
			" subdom.sub_lang_en, " +
			
			" yucca_organization.organizationcode, " +
			" yucca_organization.description organization_description, " +
			" yucca_organization.id_organization, " +
			  
			" yucca_r_tenant_data_source.isactive data_source_is_active, " +
			" yucca_r_tenant_data_source.ismanager data_source_is_manager, " +
			
			" yucca_tenant.tenantcode, " +
			" yucca_tenant.name tenant_name, " +
			" yucca_tenant.description tenant_description, " +
			" yucca_tenant.id_tenant, " +
			  
			" (select array_to_json(array_agg(row_to_json(yucca_d_tag))) from " + DataSourceMapper.R_TAG_DATA_SOURCE_TABLE + " yucca_r_tag_data_source, " +  TagMapper.TAG_TABLE  + " yucca_d_tag " +
			"       where yucca_data_source.id_data_source = yucca_r_tag_data_source.id_data_source AND " +
			"       yucca_data_source.datasourceversion = yucca_r_tag_data_source.datasourceversion " +
			"       and yucca_r_tag_data_source.id_tag = yucca_d_tag.id_tag) tags " +
			" FROM " + DATASET_TABLE + " yucca_dataset " +
			
			" INNER JOIN " + DataSourceMapper.DATA_SOURCE_TABLE  + " yucca_data_source ON yucca_dataset.id_data_source = yucca_data_source.id_data_source AND yucca_dataset.datasourceversion = yucca_data_source.datasourceversion " +
			" INNER JOIN " + OrganizationMapper.ORGANIZATION_TABLE + " yucca_organization ON  yucca_data_source.id_organization = yucca_organization.id_organization " +
			" INNER JOIN " + SmartobjectMapper.STATUS_TABLE  + " yucca_d_status ON yucca_data_source.id_status = yucca_d_status.id_status " +

			" INNER JOIN (select " +
			"       yucca_d_domain.id_domain dom_id_domain, yucca_d_domain.langen dom_langen, " + 
			"       yucca_d_domain.langit dom_langit, yucca_d_domain.domaincode dom_domaincode, " +
			"       yucca_d_subdomain.id_subdomain sub_id_subdomain, yucca_d_subdomain.subdomaincode sub_subdomaincode, " + 
			"       yucca_d_subdomain.lang_it sub_lang_it, yucca_d_subdomain.lang_en sub_lang_en " +
			"       from " + SubdomainMapper.SUBDOMAIN_TABLE  + " yucca_d_subdomain INNER JOIN " + DomainMapper.DOMAIN_TABLE + " yucca_d_domain on yucca_d_subdomain.id_domain = yucca_d_domain.id_domain ) SUBDOM " +
			"   on yucca_data_source.id_subdomain = SUBDOM.sub_id_subdomain " +
			" LEFT JOIN " +  TenantMapper.R_TENANT_DATA_SOURCE_TABLE  + " yucca_r_tenant_data_source ON yucca_r_tenant_data_source.id_data_source = yucca_data_source.id_data_source AND " +
			"                     yucca_r_tenant_data_source.datasourceversion = yucca_data_source.datasourceversion AND " +
			"                     yucca_r_tenant_data_source.isactive = 1 AND yucca_r_tenant_data_source.ismanager = 1 " +
			" LEFT JOIN " +  TenantMapper.TENANT_TABLE  + " yucca_tenant ON yucca_tenant.id_tenant = yucca_r_tenant_data_source.id_tenant " +

			" WHERE " +

			" (yucca_data_source.id_data_source, yucca_data_source.datasourceversion) IN " + 
			"    (select id_data_source, max(datasourceversion) from " + DataSourceMapper.DATA_SOURCE_TABLE  + "  where id_data_source = yucca_dataset.id_data_source group by id_data_source) " +
			
				"<if test=\"tenantCodeManager != null\">" +
				" AND yucca_tenant.tenantcode = #{tenantCodeManager} " +
				"</if>" +

			"  AND (yucca_data_source.visibility = 'public' OR " +
			"   EXISTS ( " +
			"   SELECT yucca_tenant.tenantcode " + 
			"   FROM " +  TenantMapper.TENANT_TABLE  + " yucca_tenant, " + TenantMapper.R_TENANT_DATA_SOURCE_TABLE  + " yucca_r_tenant_data_source " +
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

			" and yucca_organization.organizationcode = #{organizationcode}" +
			
			"<if test=\"sortList != null\">" +
		    " 	ORDER BY " +
			"	<foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
		    
			"		<if test=\"propName == 'iddataset-'\">" +
		    " 			iddataset" +
	        "		</if>" +
	        "		<if test=\"propName == 'iddataset'\">" +
	        " 			iddataset" +
	        "		</if>" +
            "	</foreach>" +
            "</if>";			
	@Results({
		
		@Result(property = "idDataSource", column = "id_data_source"),
		@Result(property = "dataSourceVisibility", column = "data_source_visibility"),

		@Result(property = "dataSourceUnpublished", column = "data_source_unpublished"),  
		@Result(property = "dataSourceRegistrationDate", column = "data_source_registration_date"), 
		
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
		@Result(property = "idTenant", column = "id_tenant")
      })	
	@Select({"<script>", SELECT_DATA_SETS, "</script>"}) 
	List<Dataset> selectDataSets( @Param("tenantCodeManager") String tenantCodeManager,
										 @Param("organizationcode") String organizationcode,
										 @Param("sortList") List<String> sortList, 
			                             @Param("userAuthorizedTenantCodeList") List<String> userAuthorizedTenantCodeList);	
	
	
	/*************************************************************************
	* 					SELECT DATASET
	* ***********************************************************************/
	public static final String SELECT_DATA_SET = 
	" SELECT id_data_source, datasourceversion, iddataset, datasetcode, datasetname, "
	+ "description, startingestiondate, endingestiondate, importfiletype, "
	+ "id_dataset_type, id_dataset_subtype, solrcollectionname, phoenixtablename, "
	+ "phoenixschemaname, availablehive, availablespeed, istransformed, "
	+ "dbhiveschema, dbhivetable, id_data_source_binary, datasourceversion_binary, "
	+ "jdbcdburl, jdbcdbname, jdbcdbtype, jdbctablename "
	+ "FROM " + DATASET_TABLE + " where id_data_source = #{idDataSource} and datasourceversion = #{dataSourceVersion} ";
	@Results({
		@Result(property = "idDataSource",   column = "id_data_source"),
		@Result(property = "idDatasetType",  column = "id_dataset_type"),
		@Result(property = "idDatasetSubtype",  column = "id_dataset_subtype"),
		@Result(property = "idDataSourceBinary",    column = "id_data_source_binary"),
		@Result(property = "datasourceversionBinary", column = "datasourceversion_binary")
      })	
	@Select(SELECT_DATA_SET) 
	Dataset selectDataSet(@Param("idDataSource") Integer idDataSource, @Param("dataSourceVersion") Integer dataSourceVersion);	
	
	
	/*************************************************************************
	 * 					DELETE DATASET
	 * ***********************************************************************/
	public static final String DELETE_DATASET = 
	" DELETE from " + DATASET_TABLE + " WHERE id_data_source = #{idDataSource} and datasourceversion =#{dataSourceVersion}";
	@Delete(DELETE_DATASET)
	int deleteDataSet(@Param("idDataSource") Integer idDataSource, @Param("dataSourceVersion") Integer dataSourceVersion);	
	
	/*************************************************************************
	 * 					INSERT DATASET
	 * ***********************************************************************/
	public static final String INSERT_DATASET = 
	" INSERT INTO int_yucca.yucca_dataset( iddataset, id_data_source, datasourceversion, datasetcode, datasetname, description, "
	+ "startingestiondate, endingestiondate, importfiletype, id_dataset_type, id_dataset_subtype, solrcollectionname, "
	+ "phoenixtablename, phoenixschemaname, availablehive, availablespeed, istransformed, dbhiveschema, dbhivetable, "
	+ "id_data_source_binary, datasourceversion_binary, jdbcdburl, jdbcdbname, jdbcdbtype, jdbctablename) "
	+ "VALUES (#{iddataset}, #{idDataSource}, #{datasourceversion}, #{datasetcode}, #{datasetname}, #{description}, #{startingestiondate}, "
	+ "#{endingestiondate}, #{importfiletype}, #{idDatasetType}, #{idDatasetSubtype}, #{solrcollectionname}, #{phoenixtablename}, "
	+ "#{phoenixschemaname}, #{availablehive}, #{availablespeed}, #{istransformed}, #{dbhiveschema}, #{dbhivetable}, "
	+ "#{idDataSourceBinary}, #{datasourceversionBinary}, #{jdbcdburl},#{jdbcdbname}, #{jdbcdbtype}, #{jdbctablename})";
	@Insert(INSERT_DATASET)
	int insertDataset(Dataset dataset);

	
	
}
