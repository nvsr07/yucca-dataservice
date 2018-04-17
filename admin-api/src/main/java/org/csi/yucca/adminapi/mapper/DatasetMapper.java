package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.model.join.IngestionConfiguration;
import org.csi.yucca.adminapi.util.Constants;

/**
 * 
 * @author gianfranco.stolfa
 *
 */
public interface DatasetMapper {
	
	public static final String DATASET_TABLE = Constants.SCHEMA_DB + "yucca_dataset";
	
	// aggiunta la "distinct" per evitare duplicati sui componenti senza sourcecolumnname:
	public static final String SELECT_INGESTION_CONFIGURATION = 
			" SELECT distinct " +
				" YUCCA_DOMAIN.domaincode, " +
				" SUBDOMAIN.subdomaincode, " +

				" COMPONENT.sourcecolumn, " +   
				" COMPONENT.sourcecolumnname, " +   
				" COMPONENT.alias, " +   

				" DATASET.jdbcdbschema, " +   
				" DATASET.jdbcdburl, " +   
				" DATASET.jdbcdbname, " +   
				" DATASET.jdbctablename, " +   
				" DATASET.datasetcode, " +   

				" DATA_SOURCE.visibility, " +   
				" DATA_SOURCE.isopendata, " +   
				" DATA_SOURCE.registrationdate " + 

			" FROM " + TenantMapper.TENANT_TABLE                + " TENANT, " +  
			           TenantMapper.R_TENANT_DATA_SOURCE_TABLE  + " TENANT_DATASOURCE, " +   
			           DataSourceMapper.DATA_SOURCE_TABLE       + " DATA_SOURCE, " +   
			           DatasetMapper.DATASET_TABLE              + " DATASET, " +   
			           ComponentMapper.COMPONENT_TABLE          + " COMPONENT, " +
			           DomainMapper.DOMAIN_TABLE                + " YUCCA_DOMAIN, " +
			           SubdomainMapper.SUBDOMAIN_TABLE          + " SUBDOMAIN " +
			         
			" WHERE TENANT.tenantcode       = #{tenantCode} AND  " +
				" TENANT.id_tenant              = TENANT_DATASOURCE.id_tenant AND  " +
				" DATA_SOURCE.id_data_source    = TENANT_DATASOURCE.id_data_source AND  " +
				" DATA_SOURCE.datasourceversion = TENANT_DATASOURCE.datasourceversion AND  " +
				" DATASET.id_data_source        = DATA_SOURCE.id_data_source AND  " +
				" DATASET.datasourceversion     = DATA_SOURCE.datasourceversion AND  " +
				" COMPONENT.id_data_source      = TENANT_DATASOURCE.id_data_source AND  " +
				" COMPONENT.datasourceversion   = TENANT_DATASOURCE.datasourceversion AND " +
				         
				" DATA_SOURCE.id_subdomain      = SUBDOMAIN.id_subdomain AND " +
				" YUCCA_DOMAIN.id_domain        = SUBDOMAIN.id_domain  " +
	
				// se onlyImported è true -> jdbcdbtype non nullo
				"<if test=\"onlyImported\">" +
				" AND DATASET.jdbcdbtype IS NOT NULL " +
				"</if>" +
	
				// se il parametro dbName è valorizzato filtrare anche sulla colonna jdbcdbname
				"<if test=\"dbName != null\">" +
				" AND DATASET.jdbcdbname = #{dbName} " +
				"</if>"; 
	  @Results({
		@Result(property = "table", column = "jdbctablename"),
		@Result(property = "column", column = "sourcecolumnname"),
		@Result(property = "comments", column = "alias"),
		@Result(property = "datasetCode", column = "datasetcode"),
		@Result(property = "domain", column = "domaincode"),
		@Result(property = "subdomain", column = "subdomaincode"),
		@Result(property = "visibility", column = "visibility"),
		@Result(property = "opendata", column = "isopendata"),
		@Result(property = "registrationDate", column = "registrationdate"),
		@Result(property = "dbName", column = "jdbcdbname"),
		@Result(property = "dbSchema", column = "jdbcdbschema"),
		@Result(property = "dbUrl", column = "jdbcdburl"),
		@Result(property = "columnIndex", column = "sourcecolumn")
	  })
	  @Select({"<script>", SELECT_INGESTION_CONFIGURATION , "</script>"}) 
	  List<IngestionConfiguration> selectIngestionConfiguration(
			  @Param("dbName")  String dbName,
			  @Param("tenantCode")  String tenantCode,
			  @Param("onlyImported")  Boolean onlyImported );	
	
	public static final String SELECT_LISTA_DATASET_BY_ORGANIZATION = 
		    " SELECT " + 
		     " case yucca_d_dataset_subtype.dataset_subtype " +
			  "	when 'bulkDataset' then coalesce(yucca_dataset.solrcollectionname, yucca_organization.datasolrcollectionname) " +
			 "	when 'socialDataset' then coalesce(yucca_dataset.solrcollectionname, yucca_organization.socialsolrcollectionname) " +
			  "	when 'streamDataset' then coalesce(yucca_dataset.solrcollectionname, yucca_organization.measuresolrcollectionname) " +
			"	else coalesce(yucca_dataset.solrcollectionname, yucca_organization.mediasolrcollectionname) " +
			"     end solrcollectionname, " +
			
			" case yucca_d_dataset_subtype.dataset_subtype " +   
			"	when 'bulkDataset' then coalesce(yucca_dataset.phoenixtablename, yucca_organization.dataphoenixtablename) " +
		   "	when 'socialDataset' then coalesce(yucca_dataset.phoenixtablename, yucca_organization.socialphoenixtablename) " +
			"	when 'streamDataset' then coalesce(yucca_dataset.phoenixtablename, yucca_organization.measuresphoenixtablename) " +
			"	else coalesce(yucca_dataset.phoenixtablename, yucca_organization.mediaphoenixtablename) " +
			"     end phoenixtablename, " +
			
			" case yucca_d_dataset_subtype.dataset_subtype " +  
			"	when 'bulkDataset' then coalesce(yucca_dataset.phoenixschemaname, yucca_organization.dataphoenixschemaname) " +
			"	when 'socialDataset' then coalesce(yucca_dataset.phoenixschemaname, yucca_organization.socialphoenixschemaname) " +
			"	when 'streamDataset' then coalesce(yucca_dataset.phoenixschemaname, yucca_organization.measuresphoenixschemaname) " +
			"	else coalesce(yucca_dataset.phoenixschemaname, yucca_organization.mediaphoenixschemaname) " +
			"     end phoenixschemaname," +
			
			" yucca_dataset.importedfiles, yucca_dataset.jdbcdbschema, yucca_dataset.id_data_source, yucca_dataset.datasourceversion, yucca_dataset.iddataset, yucca_dataset.datasetcode, " +
			" yucca_dataset.datasetname, yucca_dataset.description dataset_description, yucca_d_dataset_type.id_dataset_type, " +
			" yucca_d_dataset_type.dataset_type, yucca_d_dataset_type.description dataset_type_description, yucca_d_dataset_subtype.id_dataset_subtype, " +
			" yucca_d_dataset_subtype.dataset_subtype, yucca_d_dataset_subtype.description dataset_subtype_description, yucca_dataset.id_data_source_binary, " +
			" yucca_dataset.datasourceversion_binary, yucca_data_source.visibility data_source_visibility, yucca_data_source.copyright data_source_copyright, " +
			" yucca_data_source.unpublished data_source_unpublished, yucca_data_source.registrationdate data_source_registration_date, " +
			" yucca_data_source.isopendata data_source_is_opendata, yucca_data_source.externalreference data_source_external_reference, " +
			" yucca_data_source.opendataauthor data_source_open_data_author, yucca_data_source.opendataupdatedate data_source_open_data_update_date, yucca_data_source.opendataupdatefrequency data_source_open_data_update_frequency, " +
			" yucca_data_source.opendatalanguage data_source_open_data_language, yucca_data_source.lastupdate data_source_last_update, " +
			" yucca_data_source.disclaimer data_source_disclaimer, yucca_data_source.requestername data_source_requester_name, " +
			" yucca_data_source.requestersurname data_source_requester_surname, yucca_data_source.requestermail data_source_requester_mail, " +
			" yucca_data_source.privacyacceptance data_source_privacy_acceptance, yucca_data_source.icon data_source_icon, yucca_d_status.statuscode, " +
			" yucca_d_status.description status_description, yucca_d_status.id_status, subdom.dom_id_domain, subdom.dom_langen, subdom.dom_langit, " +
			" subdom.dom_domaincode, subdom.sub_id_subdomain, subdom.sub_subdomaincode, subdom.sub_lang_it, subdom.sub_lang_en, yucca_organization.organizationcode, " +
			" yucca_organization.description organization_description, yucca_organization.id_organization, yucca_r_tenant_data_source.isactive, " +
			" yucca_r_tenant_data_source.ismanager, yucca_tenant.tenantcode, yucca_tenant.name tenant_name, " +
			" yucca_tenant.description tenant_description, yucca_tenant.id_tenant, " +
		// COMPONENTS
			" ( select (row_to_json(yucca_d_license)) " + 
			" from " + LicenseMapper.LICENSE_TABLE  + " yucca_d_license " + 
			" where yucca_d_license.id_license = yucca_data_source.id_license " +
			" ) license, " +
			" ( select array_to_json(array_agg(row_to_json(comp))) " + 
			" from ( select yucca_component.id_component, " +
			"        		yucca_component.name, " +
			"        		yucca_component.alias, " +
			"        		yucca_component.inorder, " +
			"        		yucca_component.tolerance, " +
			"        		yucca_component.since_version, " +
			"        		yucca_component.id_measure_unit \"idMeasureUnit\", " +
			"        		yucca_component.iskey, " +
			"        		yucca_component.id_data_source, " +
			"        		yucca_component.datasourceversion, " +
			"        		yucca_component.sourcecolumn, " +
			"        		yucca_component.sourcecolumnname, " +
			"        		yucca_component.required, " +
			"        		yucca_component.foreignkey, " +
			"         		yucca_d_phenomenon.id_phenomenon \"idPhenomenon\", " +
			"         		yucca_d_phenomenon.phenomenonname phenomenonname, " +
			"         		yucca_d_phenomenon.phenomenoncetegory phenomenoncetegory, " +
			"         		yucca_d_data_type.id_data_type \"idDataType\", " +
			"         		yucca_d_data_type.datatypecode datatypecode, " +
			"         		yucca_d_data_type.description datatypedescription, " +
			"         		yucca_d_measure_unit.id_measure_unit \"idMeasureUnit\", " +
			"         		yucca_d_measure_unit.measureunit, " +
			"         		yucca_d_measure_unit.measureunitcategory " +
			"          from " + ComponentMapper.COMPONENT_TABLE  + " yucca_component " + 
			"  	  LEFT JOIN " + PhenomenonMapper.PHENOMENON_TABLE  + " yucca_d_phenomenon ON yucca_component.id_phenomenon = yucca_d_phenomenon.id_phenomenon " + 
			"     LEFT JOIN " +  DataTypeMapper.DATA_TYPE_TABLE  + " yucca_d_data_type ON yucca_component.id_data_type = yucca_d_data_type.id_data_type  " +
			" 	  LEFT JOIN " + MeasureUnitMapper.MEASURE_UNIT_TABLE  + " yucca_d_measure_unit ON yucca_component.id_measure_unit = yucca_d_measure_unit.id_measure_unit " + 
			"         where yucca_data_source.id_data_source = yucca_component.id_data_source AND  " +
			"         yucca_data_source.datasourceversion = yucca_component.datasourceversion " +
			" ) comp  " +
			" ) componentsString, " +
			
			// SHARING TENANTS
			" ( select array_to_json(array_agg(row_to_json(tenantshr))) " + 
			" from ( select yucca_tenant.id_tenant, " +
			" yucca_tenant.tenantcode, " +
			" yucca_tenant.name, " +
			" yucca_tenant.description, " +
			" yucca_r_tenant_data_source.dataoptions, " +
			" yucca_r_tenant_data_source.manageoptions  " +
			" from " +  TenantMapper.TENANT_TABLE + " yucca_tenant, " +
			            TenantMapper.R_TENANT_DATA_SOURCE_TABLE  + " yucca_r_tenant_data_source " + 
			" where yucca_tenant.id_tenant = yucca_r_tenant_data_source.id_tenant AND " + 
			" yucca_r_tenant_data_source.id_data_source = yucca_data_source.id_data_source AND " + 
			" yucca_r_tenant_data_source.datasourceversion = yucca_data_source.datasourceversion AND " + 
			" yucca_r_tenant_data_source.isactive = 1 AND  " +
			" yucca_r_tenant_data_source.ismanager = 0  " +
			" ) tenantshr  " +
			" ) sharing_tenant " + 
			// FROM
			" FROM " +  DatasetMapper.DATASET_TABLE  + " yucca_dataset  " +
			" INNER JOIN " + DataSourceMapper.DATA_SOURCE_TABLE  + " yucca_data_source ON yucca_dataset.id_data_source = yucca_data_source.id_data_source AND  " +
			" yucca_dataset.datasourceversion = yucca_data_source.datasourceversion  " +
			" INNER JOIN " + OrganizationMapper.ORGANIZATION_TABLE + " yucca_organization ON yucca_data_source.id_organization = yucca_organization.id_organization  " +
			" INNER JOIN " +  SmartobjectMapper.STATUS_TABLE  + " yucca_d_status ON yucca_data_source.id_status = yucca_d_status.id_status  " +
			" INNER JOIN " + DatasetTypeMapper.DATASET_TYPE_TABLE  + " yucca_d_dataset_type ON yucca_d_dataset_type.id_dataset_type = yucca_dataset.id_dataset_type  " +
			" INNER JOIN " +  DatasetSubtypeMapper.DATASET_SUBTYPE_TABLE  + " yucca_d_dataset_subtype ON yucca_d_dataset_subtype.id_dataset_subtype = yucca_dataset.id_dataset_subtype " + 
			" INNER JOIN ( select yucca_d_domain.id_domain dom_id_domain, " +
			" yucca_d_domain.langen dom_langen, " +
			" yucca_d_domain.langit dom_langit, " +
			" yucca_d_domain.domaincode dom_domaincode, " +
			" yucca_d_subdomain.id_subdomain sub_id_subdomain, " +
			" yucca_d_subdomain.subdomaincode sub_subdomaincode, " +
			" yucca_d_subdomain.lang_it sub_lang_it, " +
			" yucca_d_subdomain.lang_en sub_lang_en " + 
			" from " + SubdomainMapper.SUBDOMAIN_TABLE + " yucca_d_subdomain  " +
			" INNER JOIN " + DomainMapper.DOMAIN_TABLE + " yucca_d_domain ON yucca_d_subdomain.id_domain = yucca_d_domain.id_domain  " +
			" ) SUBDOM ON yucca_data_source.id_subdomain = SUBDOM.sub_id_subdomain " + 

			" LEFT JOIN " + TenantMapper.R_TENANT_DATA_SOURCE_TABLE  + " yucca_r_tenant_data_source ON yucca_r_tenant_data_source.id_data_source = yucca_data_source.id_data_source AND " + 
			" yucca_r_tenant_data_source.datasourceversion = yucca_data_source.datasourceversion AND  " +
			" yucca_r_tenant_data_source.isactive = 1 AND  " +
			" yucca_r_tenant_data_source.ismanager = 1  " +
			" LEFT JOIN " + TenantMapper.TENANT_TABLE  + " yucca_tenant ON yucca_tenant.id_tenant = yucca_r_tenant_data_source.id_tenant ";
	
	@Results({
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"),
		@Result(property = "idDataSource", column = "id_data_source"),
		@Result(property = "description", column = "dataset_description"),
		@Result(property = "idDatasetType", column = "id_dataset_type"),	
		@Result(property = "idDatasetSubtype", column = "id_dataset_subtype"),
	    @Result(property = "idDataSourceBinary", column = "id_data_source_binary"),
	    @Result(property = "datasourceversionBinary", column = "datasourceversion_binary"),	
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
		@Result(property = "dataSourceIsActive", column = "isactive"),
		@Result(property = "dataSourceIsManager", column = "ismanager"),
		@Result(property = "tenantCode", column = "tenantcode"),
		@Result(property = "tenantName", column = "tenant_name"),
		@Result(property = "tenantDescription", column = "tenant_description"),
		@Result(property = "idTenant", column = "id_tenant"),
		@Result(property = "datasetType", column = "dataset_type"),
		@Result(property = "datasetTypeDescription", column = "dataset_type_description"),
		@Result(property = "datasetSubtype", column = "dataset_subtype"), 	
		@Result(property = "datasetSubtypeDescription", column = "dataset_subtype_description"), 
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"), 	
		@Result(property = "dataSourceIsopendata", column = "data_source_is_opendata"), 
		@Result(property = "dataSourceExternalReference", column = "data_source_external_reference"), 
		@Result(property = "dataSourceOpenDataAuthor", column = "data_source_open_data_author"), 
		@Result(property = "dataSourceOpenDataUpdateDate", column = "data_source_open_data_update_date"), 
		@Result(property = "dataSourceOpenDataUpdateFrequency", column = "data_source_open_data_update_frequency"),
		@Result(property = "dataSourceOpenDataLanguage", column = "data_source_open_data_language"), 
		@Result(property = "dataSourceLastUpdate", column = "data_source_last_update"), 
		@Result(property = "dataSourceDisclaimer", column = "data_source_disclaimer"), 
		@Result(property = "dataSourceRequesterName", column = "data_source_requester_name"), 
		@Result(property = "dataSourceRequesterSurname", column = "data_source_requester_surname"), 
		@Result(property = "dataSourceRequesterMail", column = "data_source_requester_mail"), 
		@Result(property = "dataSourcePrivacyAcceptance", column = "data_source_privacy_acceptance"), 
		@Result(property = "dataSourceIcon", column = "data_source_icon"), 
		@Result(property = "sharingTenant", column = "sharing_tenant") 
      })		
	  @Select({"<script>", SELECT_LISTA_DATASET_BY_ORGANIZATION, WHERE_DETTAGLIO_DATASET_START + 
		  WHERE_DETTAGLIO_DATASET_ORGANIZATION_CODE+"</script>"}) 
	  List<DettaglioDataset> selectListaDettaglioDatasetByOrganizationCode(@Param("organizationCode")  String organizationCode);	
	
	
	/*************************************************************************
	 * 					INSERT DATASET
	 * ***********************************************************************/
	public static final String CLONE_DATASET = 
		" INSERT INTO yucca_dataset( " +
			" id_data_source, datasourceversion, iddataset, datasetcode, datasetname, " + 
			" description, startingestiondate, endingestiondate, importfiletype, " + 
			" id_dataset_type, id_dataset_subtype, solrcollectionname, phoenixtablename, " + 
			" phoenixschemaname, availablehive, availablespeed, istransformed, " + 
			" dbhiveschema, dbhivetable, id_data_source_binary, datasourceversion_binary, " + 
			" jdbcdburl, jdbcdbname, jdbcdbtype, jdbctablename) " +
		" SELECT id_data_source, #{newDataSourceVersion}, iddataset, datasetcode, datasetname, " + 
			" description, startingestiondate, endingestiondate, importfiletype, " + 
			" id_dataset_type, id_dataset_subtype, solrcollectionname, phoenixtablename, " + 
			" phoenixschemaname, availablehive, availablespeed, istransformed, " + 
			" dbhiveschema, dbhivetable, id_data_source_binary, datasourceversion_binary, " + 
			" jdbcdburl, jdbcdbname, jdbcdbtype, jdbctablename " +
		" FROM yucca_dataset " +
			" WHERE id_data_source = #{idDataSource} and datasourceversion = #{currentDataSourceVersion}";
	@Insert(CLONE_DATASET)
	int cloneDataset( @Param("newDataSourceVersion") Integer newDataSourceVersion, @Param("currentDataSourceVersion") Integer currentDataSourceVersion, @Param("idDataSource") Integer idDataSource );
	
	/*************************************************************************
	 * 					UPDATE DATASET
	 * ***********************************************************************/	
	public static final String UPDATE_DATASET = 
		"UPDATE " + DATASET_TABLE + 
		" SET datasetname=#{dataSetName}, description=#{dataSetDescription} " + 
		" WHERE id_data_source=#{idDataSource} and datasourceversion=#{dataSourceVersion}";
	@Update(UPDATE_DATASET)
	int updateDataset( 
			@Param("dataSetName") String dataSetName,
			@Param("dataSetDescription") String dataSetDescription,
			@Param("idDataSource") Integer idDataSource,
			@Param("dataSourceVersion") Integer dataSourceVersion );
	
	
	
	
	public static final String UPDATE_DATASET_IMPORTEDFILES = 
			"UPDATE " + DATASET_TABLE + 
			" SET importedfiles=#{importedfiles}  " + 
			" WHERE id_data_source=#{idDataSource} and datasourceversion=#{dataSourceVersion}";
		@Update(UPDATE_DATASET_IMPORTEDFILES)
	void updateImportedFiles(@Param("idDataSource") Integer idDataSource, @Param("dataSourceVersion") Integer dataSourceVersion, @Param("importedfiles") String importedfiles);

	
	/**
	 * SELECT DATASET FOR UPDATE
	 */
	public static final String SELECT_DATASET_FOR_UPDATE = 
			" SELECT " + 
			" yucca_dataset.iddataset, " + 
			" yucca_dataset.id_data_source, " +
			" yucca_dataset.datasetcode, " + 
			" yucca_dataset.datasetname, " + 
			" yucca_dataset.datasourceversion, " + 
			" yucca_d_status.id_status, " + 
			" yucca_d_status.statuscode, " + 
			" yucca_d_status.description status_description, " + 
			" yucca_organization.organizationcode, " + 
			" yucca_organization.description organization_description, " + 
			" yucca_organization.id_organization, " + 
			" yucca_r_tenant_data_source.isactive, " + 
			" yucca_r_tenant_data_source.ismanager, " + 
			" yucca_tenant.tenantcode, " + 
			" yucca_tenant.name tenant_name, " + 
			" yucca_tenant.description tenant_description, " + 
			" yucca_tenant.id_tenant, " +
			" yucca_data_source.id_subdomain, " +
			" yucca_d_subdomain.id_domain " +
			" FROM " +  DATASET_TABLE + " yucca_dataset " + 
			" INNER JOIN " + DataSourceMapper.DATA_SOURCE_TABLE  + " yucca_data_source ON yucca_dataset.id_data_source = yucca_data_source.id_data_source AND yucca_dataset.datasourceversion = yucca_data_source.datasourceversion " +
			" INNER JOIN " + OrganizationMapper.ORGANIZATION_TABLE + " yucca_organization ON  yucca_data_source.id_organization = yucca_organization.id_organization " +
			" INNER JOIN " + SmartobjectMapper.STATUS_TABLE + " yucca_d_status ON yucca_data_source.id_status = yucca_d_status.id_status " + 
			" INNER JOIN " + SubdomainMapper.SUBDOMAIN_TABLE  + " yucca_d_subdomain ON yucca_data_source.id_subdomain = yucca_d_subdomain.id_subdomain " + 
			" LEFT JOIN " + TenantMapper.R_TENANT_DATA_SOURCE_TABLE  + " yucca_r_tenant_data_source ON yucca_r_tenant_data_source.id_data_source = yucca_data_source.id_data_source AND " +
			"                     yucca_r_tenant_data_source.datasourceversion = yucca_data_source.datasourceversion AND " +
			"                     yucca_r_tenant_data_source.isactive = 1 AND yucca_r_tenant_data_source.ismanager = 1 " +
			" LEFT JOIN " + TenantMapper.TENANT_TABLE  + " yucca_tenant ON yucca_tenant.id_tenant = yucca_r_tenant_data_source.id_tenant " +
			" WHERE  " +
			" (yucca_data_source.id_data_source, yucca_data_source.datasourceversion) IN " + 
			"    (select id_data_source, max(datasourceversion) from " + DataSourceMapper.DATA_SOURCE_TABLE  + "  where id_data_source = yucca_dataset.id_data_source group by id_data_source) " +
			"  AND yucca_organization.organizationcode = #{organizationCode} " +

			"<if test=\"tenantCodeManager != null\">" +
			" AND yucca_tenant.tenantcode =  #{tenantCodeManager} " +
			"</if>" +
			"  AND (yucca_data_source.visibility = 'public' OR " +
			"   EXISTS ( " +
			"   SELECT yucca_tenant.tenantcode " + 
			"   FROM " + TenantMapper.TENANT_TABLE  + " yucca_tenant , " + TenantMapper.R_TENANT_DATA_SOURCE_TABLE + " yucca_r_tenant_data_source " +
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
			" AND yucca_dataset.id_dataset_type = 1 " +
			" AND yucca_dataset.id_dataset_subtype = 1 " +
			" AND yucca_dataset.iddataset = #{idDataset} ";
	@Results({
		@Result(property = "idDataSource", column = "id_data_source"),
		@Result(property = "idStatus", column = "id_status"),
		@Result(property = "statusCode", column = "statuscode"), 
		@Result(property = "statusDescription", column = "status_description"),
		@Result(property = "organizationCode", column = "organizationcode"), 
		@Result(property = "organizationDescription", column = "organization_description"),
		@Result(property = "idOrganization", column = "id_organization"), 
		@Result(property = "tenantCode", column = "tenantcode"), 
		@Result(property = "tenantName", column = "tenant_name"), 
		@Result(property = "tenantDescription", column = "tenant_description"), 
		@Result(property = "idTenant", column = "id_tenant"),
		@Result(property = "subIdSubDomain", column = "id_subdomain"),
		@Result(property = "domIdDomain", column = "id_domain") 
      })		
	@Select({"<script>", SELECT_DATASET_FOR_UPDATE, "</script>"}) 
	DettaglioDataset selectDatasetForUpdate( 
								  @Param("tenantCodeManager") String tenantCodeManager,
							      @Param("idDataset") Integer idDataset,
							      @Param("organizationCode") String organizationcode,
							      @Param("userAuthorizedTenantCodeList") List<String> userAuthorizedTenantCodeList);	

	
	/******************************************************************
	 * 				SELECT DETTAGLIO DATSET
	 ******************************************************************/
	public static final String SELECT_DETTAGLIO_DATASET_CAMPI = 
		    " SELECT " + 
		     " case yucca_d_dataset_subtype.dataset_subtype " +   
			  "	when 'bulkDataset' then coalesce(yucca_dataset.solrcollectionname, yucca_organization.datasolrcollectionname) " +
			 "	when 'socialDataset' then coalesce(yucca_dataset.solrcollectionname, yucca_organization.socialsolrcollectionname) " +
			  "	when 'streamDataset' then coalesce(yucca_dataset.solrcollectionname, yucca_organization.measuresolrcollectionname) " +
			"	else coalesce(yucca_dataset.solrcollectionname, yucca_organization.mediasolrcollectionname) " +
			"     end solrcollectionname, " +
			" case yucca_d_dataset_subtype.dataset_subtype " +   
			"	when 'bulkDataset' then coalesce(yucca_dataset.phoenixtablename, yucca_organization.dataphoenixtablename) " +
		   "	when 'socialDataset' then coalesce(yucca_dataset.phoenixtablename, yucca_organization.socialphoenixtablename) " +
			"	when 'streamDataset' then coalesce(yucca_dataset.phoenixtablename, yucca_organization.measuresphoenixtablename) " +
			"	else coalesce(yucca_dataset.phoenixtablename, yucca_organization.mediaphoenixtablename) " +
			"     end phoenixtablename, " +
			" case yucca_d_dataset_subtype.dataset_subtype " +  
			"	when 'bulkDataset' then coalesce(yucca_dataset.phoenixschemaname, yucca_organization.dataphoenixschemaname) " +
			"	when 'socialDataset' then coalesce(yucca_dataset.phoenixschemaname, yucca_organization.socialphoenixschemaname) " +
			"	when 'streamDataset' then coalesce(yucca_dataset.phoenixschemaname, yucca_organization.measuresphoenixschemaname) " +
			"	else coalesce(yucca_dataset.phoenixschemaname, yucca_organization.mediaphoenixschemaname) " +
			"     end phoenixschemaname," +
			" yucca_dataset.importedfiles, yucca_dataset.jdbcdbschema, yucca_dataset.id_data_source, yucca_dataset.datasourceversion, yucca_dataset.iddataset, yucca_dataset.datasetcode, " +
			" yucca_dataset.datasetname, yucca_dataset.description dataset_description, yucca_d_dataset_type.id_dataset_type, " +
			" yucca_d_dataset_type.dataset_type, yucca_d_dataset_type.description dataset_type_description, yucca_d_dataset_subtype.id_dataset_subtype, " +
			" yucca_d_dataset_subtype.dataset_subtype, yucca_d_dataset_subtype.description dataset_subtype_description, yucca_dataset.id_data_source_binary, " +
			" yucca_dataset.datasourceversion_binary, yucca_data_source.visibility data_source_visibility, yucca_data_source.copyright data_source_copyright, " +
			" yucca_data_source.unpublished data_source_unpublished, yucca_data_source.registrationdate data_source_registration_date, " +
			" yucca_data_source.isopendata data_source_is_opendata, yucca_data_source.externalreference data_source_external_reference, " +
			" yucca_data_source.opendataauthor data_source_open_data_author, yucca_data_source.opendataupdatedate data_source_open_data_update_date, yucca_data_source.opendataupdatefrequency data_source_open_data_update_frequency, " +
			" yucca_data_source.opendatalanguage data_source_open_data_language, yucca_data_source.lastupdate data_source_last_update, " +
			" yucca_data_source.disclaimer data_source_disclaimer, yucca_data_source.requestername data_source_requester_name, " +
			" yucca_data_source.requestersurname data_source_requester_surname, yucca_data_source.requestermail data_source_requester_mail, " +
			" yucca_data_source.privacyacceptance data_source_privacy_acceptance, yucca_data_source.icon data_source_icon, yucca_d_status.statuscode, " +
			" yucca_d_status.description status_description, yucca_d_status.id_status, subdom.dom_id_domain, subdom.dom_langen, subdom.dom_langit, " +
			" subdom.dom_domaincode, subdom.sub_id_subdomain, subdom.sub_subdomaincode, subdom.sub_lang_it, subdom.sub_lang_en, yucca_organization.organizationcode, " +
			" yucca_organization.description organization_description, yucca_organization.id_organization, yucca_r_tenant_data_source.isactive, " +
			" yucca_r_tenant_data_source.ismanager, yucca_tenant.tenantcode, yucca_tenant.name tenant_name, " +
			" yucca_tenant.description tenant_description, yucca_tenant.id_tenant, " +
			// TAGS
			" ( select array_to_json(array_agg(row_to_json(yucca_d_tag))) " + 
			" from " +  DataSourceMapper.R_TAG_DATA_SOURCE_TABLE + " yucca_r_tag_data_source, " + TagMapper.TAG_TABLE + " yucca_d_tag " + 
			" where yucca_data_source.id_data_source = yucca_r_tag_data_source.id_data_source AND " +
			" yucca_data_source.datasourceversion = yucca_r_tag_data_source.datasourceversion AND " + 
			" yucca_r_tag_data_source.id_tag = yucca_d_tag.id_tag " +
			" ) tags, " +
			" ( select (row_to_json(yucca_dcat)) " + 
			" from " + DcatMapper.DCAT_TABLE  + " yucca_dcat " + 
			" where yucca_dcat.id_dcat = yucca_data_source.id_dcat " +
			" ) dcatString, " +
			// COMPONENTS
			" ( select (row_to_json(yucca_d_license)) " + 
			" from " + LicenseMapper.LICENSE_TABLE  + " yucca_d_license " + 
			" where yucca_d_license.id_license = yucca_data_source.id_license " +
			" ) license, " +
			" ( select array_to_json(array_agg(row_to_json(comp))) " + 
			" from ( select yucca_component.id_component, " +
			"        		yucca_component.name, " +
			"        		yucca_component.alias, " +
			"        		yucca_component.inorder, " +
			"        		yucca_component.tolerance, " +
			"        		yucca_component.since_version, " +
			"        		yucca_component.id_measure_unit \"idMeasureUnit\", " +
			"        		yucca_component.iskey, " +
			"        		yucca_component.id_data_source, " +
			"        		yucca_component.datasourceversion, " +
			"        		yucca_component.sourcecolumn, " +
			"        		yucca_component.sourcecolumnname, " +
			"        		yucca_component.required, " +
			"        		yucca_component.foreignkey, " +
			"         		yucca_d_phenomenon.id_phenomenon \"idPhenomenon\", " +
			"         		yucca_d_phenomenon.phenomenonname phenomenonname, " +
			"         		yucca_d_phenomenon.phenomenoncetegory phenomenoncetegory, " +
			"         		yucca_d_data_type.id_data_type \"idDataType\", " +
			"         		yucca_d_data_type.datatypecode datatypecode, " +
			"         		yucca_d_data_type.description datatypedescription, " +
			"         		yucca_d_measure_unit.id_measure_unit \"idMeasureUnit\", " +
			"         		yucca_d_measure_unit.measureunit, " +
			"         		yucca_d_measure_unit.measureunitcategory " +
			"          from " + ComponentMapper.COMPONENT_TABLE  + " yucca_component " + 
			"  	  LEFT JOIN " + PhenomenonMapper.PHENOMENON_TABLE  + " yucca_d_phenomenon ON yucca_component.id_phenomenon = yucca_d_phenomenon.id_phenomenon " + 
			"     LEFT JOIN " +  DataTypeMapper.DATA_TYPE_TABLE  + " yucca_d_data_type ON yucca_component.id_data_type = yucca_d_data_type.id_data_type  " +
			" 	  LEFT JOIN " + MeasureUnitMapper.MEASURE_UNIT_TABLE  + " yucca_d_measure_unit ON yucca_component.id_measure_unit = yucca_d_measure_unit.id_measure_unit " + 
			"         where yucca_data_source.id_data_source = yucca_component.id_data_source AND  " +
			"         yucca_data_source.datasourceversion = yucca_component.datasourceversion " +
			" ) comp  " +
			" ) componentsString, " +
			// SHARING TENANTS
			" ( select array_to_json(array_agg(row_to_json(tenantshr))) " + 
			" from ( select yucca_tenant.id_tenant, " +
			" yucca_tenant.tenantcode, " +
			" yucca_tenant.name, " +
			" yucca_tenant.description, " +
			" yucca_r_tenant_data_source.dataoptions, " +
			" yucca_r_tenant_data_source.manageoptions  " +
			" from " +  TenantMapper.TENANT_TABLE + " yucca_tenant, " +
			            TenantMapper.R_TENANT_DATA_SOURCE_TABLE  + " yucca_r_tenant_data_source " + 
			" where yucca_tenant.id_tenant = yucca_r_tenant_data_source.id_tenant AND " + 
			" yucca_r_tenant_data_source.id_data_source = yucca_data_source.id_data_source AND " + 
			" yucca_r_tenant_data_source.datasourceversion = yucca_data_source.datasourceversion AND " + 
			" yucca_r_tenant_data_source.isactive = 1 AND  " +
			" yucca_r_tenant_data_source.ismanager = 0  " +
			" ) tenantshr  " +
			" ) sharing_tenant ";
	
	public static final String SELECT_DETTAGLIO_DATASET_FROM = 
			// FROM
			" FROM " +  DatasetMapper.DATASET_TABLE  + " yucca_dataset  " +
			" INNER JOIN " + DataSourceMapper.DATA_SOURCE_TABLE  + " yucca_data_source ON yucca_dataset.id_data_source = yucca_data_source.id_data_source AND  " +
			" yucca_dataset.datasourceversion = yucca_data_source.datasourceversion  " +
			" INNER JOIN " + OrganizationMapper.ORGANIZATION_TABLE + " yucca_organization ON yucca_data_source.id_organization = yucca_organization.id_organization  " +
			" INNER JOIN " +  SmartobjectMapper.STATUS_TABLE  + " yucca_d_status ON yucca_data_source.id_status = yucca_d_status.id_status  " +
			" INNER JOIN " + DatasetTypeMapper.DATASET_TYPE_TABLE  + " yucca_d_dataset_type ON yucca_d_dataset_type.id_dataset_type = yucca_dataset.id_dataset_type  " +
			" INNER JOIN " +  DatasetSubtypeMapper.DATASET_SUBTYPE_TABLE  + " yucca_d_dataset_subtype ON yucca_d_dataset_subtype.id_dataset_subtype = yucca_dataset.id_dataset_subtype " + 

			" INNER JOIN ( select yucca_d_domain.id_domain dom_id_domain, " +
			" yucca_d_domain.langen dom_langen, " +
			" yucca_d_domain.langit dom_langit, " +
			" yucca_d_domain.domaincode dom_domaincode, " +
			" yucca_d_subdomain.id_subdomain sub_id_subdomain, " +
			" yucca_d_subdomain.subdomaincode sub_subdomaincode, " +
			" yucca_d_subdomain.lang_it sub_lang_it, " +
			" yucca_d_subdomain.lang_en sub_lang_en " + 
			" from " + SubdomainMapper.SUBDOMAIN_TABLE + " yucca_d_subdomain  " +
			" INNER JOIN " + DomainMapper.DOMAIN_TABLE + " yucca_d_domain ON yucca_d_subdomain.id_domain = yucca_d_domain.id_domain  " +
			" ) SUBDOM ON yucca_data_source.id_subdomain = SUBDOM.sub_id_subdomain " + 

			" LEFT JOIN " + TenantMapper.R_TENANT_DATA_SOURCE_TABLE  + " yucca_r_tenant_data_source ON yucca_r_tenant_data_source.id_data_source = yucca_data_source.id_data_source AND " + 
			" yucca_r_tenant_data_source.datasourceversion = yucca_data_source.datasourceversion AND  " +
			" yucca_r_tenant_data_source.isactive = 1 AND  " +
			" yucca_r_tenant_data_source.ismanager = 1  " +
			" LEFT JOIN " + TenantMapper.TENANT_TABLE  + " yucca_tenant ON yucca_tenant.id_tenant = yucca_r_tenant_data_source.id_tenant "; 

	
	public static final String SELECT_DETTAGLIO_DATASET = SELECT_DETTAGLIO_DATASET_CAMPI + SELECT_DETTAGLIO_DATASET_FROM;
	public static final String SELECT_DETTAGLIO_DATASET_CON_JDBC = SELECT_DETTAGLIO_DATASET_CAMPI + 	
			", yucca_dataset.jdbcdburl, yucca_dataset.jdbcdbname, yucca_dataset.jdbcdbtype,  yucca_dataset.jdbctablename "
			 + SELECT_DETTAGLIO_DATASET_FROM;

	public static final String WHERE_DETTAGLIO_DATASET_START = " WHERE 1=1 ";
	
	public static final String WHERE_DETTAGLIO_DATASET_MAX_VERSION =
			" AND (yucca_data_source.id_data_source, yucca_data_source.datasourceversion) IN " + 
			" (select id_data_source, max(datasourceversion) from " +  DataSourceMapper.DATA_SOURCE_TABLE
			+ "  where id_data_source = yucca_dataset.id_data_source group by id_data_source) " ;

	public static final String WHERE_DETTAGLIO_DATASET_MAX_VERSION_OPT_ONLY_INSTALLED =
			" AND (yucca_data_source.id_data_source, yucca_data_source.datasourceversion) IN " + 
			" (select id_data_source, max(datasourceversion) from " +  DataSourceMapper.DATA_SOURCE_TABLE
			+ "  where id_data_source = yucca_dataset.id_data_source  " 
			+ " <if test=\"onlyInstalled == true\"> " +
			" AND id_status = 2 " +
			"  </if>  group by id_data_source)";


	

	public static final String WHERE_DETTAGLIO_DATASET_ORGANIZATION_CODE =
			" AND yucca_organization.organizationcode = #{organizationCode} ";

	public static final String WHERE_DETTAGLIO_DATASET_TENANT_MANAGER_CODE =
			"<if test=\"tenantCodeManager != null\">" +
					" AND yucca_tenant.tenantcode =  #{tenantCodeManager} " +
			"</if>";
	
	public static final String WHERE_DETTAGLIO_DATASET_TENANT_VISIBILITY =
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

	public static final String WHERE_DETTAGLIO_DATASET_IDDATASET = " AND yucca_dataset.iddataset = #{idDataSet}  ";
	
	public static final String WHERE_DETTAGLIO_DATASET_IDDATASOURCE = " AND yucca_data_source.id_data_source = #{idDataSource} ";
	
	public static final String WHERE_DETTAGLIO_DATASET_DATASOURCEVERSION = " AND yucca_data_source.datasourceversion = #{dataSourceVersion} ";

	public static final String WHERE_DETTAGLIO_DATASET_DATASETCODE = " AND yucca_dataset.datasetcode = #{datasetCode} ";

	public static final String WHERE_DETTAGLIO_DATASET_JDBC_PARAMS = "AND jdbcdburl=#{jdbcdburl} and jdbcdbname=#{jdbcdbname} and jdbcdbtype=#{jdbcdbtype} AND yucca_data_source.id_status!=5  ";
	
	
	@Results({
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"),
		@Result(property = "idDataSource", column = "id_data_source"),
		@Result(property = "description", column = "dataset_description"),
		@Result(property = "idDatasetType", column = "id_dataset_type"),	
		@Result(property = "idDatasetSubtype", column = "id_dataset_subtype"),
	    @Result(property = "idDataSourceBinary", column = "id_data_source_binary"),
	    @Result(property = "datasourceversionBinary", column = "datasourceversion_binary"),	
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
		@Result(property = "dataSourceIsActive", column = "isactive"),
		@Result(property = "dataSourceIsManager", column = "ismanager"),
		@Result(property = "tenantCode", column = "tenantcode"),
		@Result(property = "tenantName", column = "tenant_name"),
		@Result(property = "tenantDescription", column = "tenant_description"),
		@Result(property = "idTenant", column = "id_tenant"),
		@Result(property = "datasetType", column = "dataset_type"),
		@Result(property = "datasetTypeDescription", column = "dataset_type_description"),
		@Result(property = "datasetSubtype", column = "dataset_subtype"), 	
		@Result(property = "datasetSubtypeDescription", column = "dataset_subtype_description"), 
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"), 	
		@Result(property = "dataSourceIsopendata", column = "data_source_is_opendata"), 
		@Result(property = "dataSourceExternalReference", column = "data_source_external_reference"), 
		@Result(property = "dataSourceOpenDataAuthor", column = "data_source_open_data_author"), 
		@Result(property = "dataSourceOpenDataUpdateDate", column = "data_source_open_data_update_date"),
		@Result(property = "dataSourceOpenDataUpdateFrequency", column = "data_source_open_data_update_frequency"),
		@Result(property = "dataSourceOpenDataLanguage", column = "data_source_open_data_language"), 
		@Result(property = "dataSourceLastUpdate", column = "data_source_last_update"), 
		@Result(property = "dataSourceDisclaimer", column = "data_source_disclaimer"), 
		@Result(property = "dataSourceRequesterName", column = "data_source_requester_name"), 
		@Result(property = "dataSourceRequesterSurname", column = "data_source_requester_surname"), 
		@Result(property = "dataSourceRequesterMail", column = "data_source_requester_mail"), 
		@Result(property = "dataSourcePrivacyAcceptance", column = "data_source_privacy_acceptance"), 
		@Result(property = "dataSourceIcon", column = "data_source_icon"), 
		@Result(property = "sharingTenant", column = "sharing_tenant") 
      })		
	@Select({"<script>", SELECT_DETTAGLIO_DATASET,WHERE_DETTAGLIO_DATASET_START + WHERE_DETTAGLIO_DATASET_MAX_VERSION 
		+ WHERE_DETTAGLIO_DATASET_TENANT_MANAGER_CODE  
		+ WHERE_DETTAGLIO_DATASET_ORGANIZATION_CODE
		+ WHERE_DETTAGLIO_DATASET_TENANT_VISIBILITY
		+ WHERE_DETTAGLIO_DATASET_IDDATASET,  "</script>"}) 
	DettaglioDataset selectDettaglioDataset( @Param("tenantCodeManager") String tenantCodeManager,
							      @Param("idDataSet") Integer idDataSet,
							      @Param("organizationCode") String organizationcode,
							      @Param("userAuthorizedTenantCodeList") List<String> userAuthorizedTenantCodeList);	
	
	
	
	@Results({
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"),
		@Result(property = "idDataSource", column = "id_data_source"),
		@Result(property = "description", column = "dataset_description"),
		@Result(property = "idDatasetType", column = "id_dataset_type"),	
		@Result(property = "idDatasetSubtype", column = "id_dataset_subtype"),
	    @Result(property = "idDataSourceBinary", column = "id_data_source_binary"),
	    @Result(property = "datasourceversionBinary", column = "datasourceversion_binary"),	
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
		@Result(property = "dataSourceIsActive", column = "isactive"),
		@Result(property = "dataSourceIsManager", column = "ismanager"),
		@Result(property = "tenantCode", column = "tenantcode"),
		@Result(property = "tenantName", column = "tenant_name"),
		@Result(property = "tenantDescription", column = "tenant_description"),
		@Result(property = "idTenant", column = "id_tenant"),
		@Result(property = "datasetType", column = "dataset_type"),
		@Result(property = "datasetTypeDescription", column = "dataset_type_description"),
		@Result(property = "datasetSubtype", column = "dataset_subtype"), 	
		@Result(property = "datasetSubtypeDescription", column = "dataset_subtype_description"), 
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"), 	
		@Result(property = "dataSourceIsopendata", column = "data_source_is_opendata"), 
		@Result(property = "dataSourceExternalReference", column = "data_source_external_reference"), 
		@Result(property = "dataSourceOpenDataAuthor", column = "data_source_open_data_author"), 
		@Result(property = "dataSourceOpenDataUpdateDate", column = "data_source_open_data_update_date"),
		@Result(property = "dataSourceOpenDataUpdateFrequency", column = "data_source_open_data_update_frequency"),
		@Result(property = "dataSourceOpenDataLanguage", column = "data_source_open_data_language"), 
		@Result(property = "dataSourceLastUpdate", column = "data_source_last_update"), 
		@Result(property = "dataSourceDisclaimer", column = "data_source_disclaimer"), 
		@Result(property = "dataSourceRequesterName", column = "data_source_requester_name"), 
		@Result(property = "dataSourceRequesterSurname", column = "data_source_requester_surname"), 
		@Result(property = "dataSourceRequesterMail", column = "data_source_requester_mail"), 
		@Result(property = "dataSourcePrivacyAcceptance", column = "data_source_privacy_acceptance"), 
		@Result(property = "dataSourceIcon", column = "data_source_icon"), 
		@Result(property = "sharingTenant", column = "sharing_tenant") 
      })
	  @Select({"<script>", SELECT_DETTAGLIO_DATASET,WHERE_DETTAGLIO_DATASET_START + 
		  			WHERE_DETTAGLIO_DATASET_IDDATASOURCE+ WHERE_DETTAGLIO_DATASET_DATASOURCEVERSION+"</script>"}) 
	  DettaglioDataset selectDettaglioDatasetByDatasource( @Param("idDataSource") Integer idDataSource,
		      @Param("dataSourceVersion") Integer dataSourceVersion
		      );
	
	
	@Results({
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"),
		@Result(property = "idDataSource", column = "id_data_source"),
		@Result(property = "description", column = "dataset_description"),
		@Result(property = "idDatasetType", column = "id_dataset_type"),	
		@Result(property = "idDatasetSubtype", column = "id_dataset_subtype"),
	    @Result(property = "idDataSourceBinary", column = "id_data_source_binary"),
	    @Result(property = "datasourceversionBinary", column = "datasourceversion_binary"),	
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
		@Result(property = "dataSourceIsActive", column = "isactive"),
		@Result(property = "dataSourceIsManager", column = "ismanager"),
		@Result(property = "tenantCode", column = "tenantcode"),
		@Result(property = "tenantName", column = "tenant_name"),
		@Result(property = "tenantDescription", column = "tenant_description"),
		@Result(property = "idTenant", column = "id_tenant"),
		@Result(property = "datasetType", column = "dataset_type"),
		@Result(property = "datasetTypeDescription", column = "dataset_type_description"),
		@Result(property = "datasetSubtype", column = "dataset_subtype"), 	
		@Result(property = "datasetSubtypeDescription", column = "dataset_subtype_description"), 
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"), 	
		@Result(property = "dataSourceIsopendata", column = "data_source_is_opendata"), 
		@Result(property = "dataSourceExternalReference", column = "data_source_external_reference"), 
		@Result(property = "dataSourceOpenDataAuthor", column = "data_source_open_data_author"), 
		@Result(property = "dataSourceOpenDataUpdateDate", column = "data_source_open_data_update_date"), 
		@Result(property = "dataSourceOpenDataUpdateFrequency", column = "data_source_open_data_update_frequency"),
		@Result(property = "dataSourceOpenDataLanguage", column = "data_source_open_data_language"), 
		@Result(property = "dataSourceLastUpdate", column = "data_source_last_update"), 
		@Result(property = "dataSourceDisclaimer", column = "data_source_disclaimer"), 
		@Result(property = "dataSourceRequesterName", column = "data_source_requester_name"), 
		@Result(property = "dataSourceRequesterSurname", column = "data_source_requester_surname"), 
		@Result(property = "dataSourceRequesterMail", column = "data_source_requester_mail"), 
		@Result(property = "dataSourcePrivacyAcceptance", column = "data_source_privacy_acceptance"), 
		@Result(property = "dataSourceIcon", column = "data_source_icon"), 
		@Result(property = "sharingTenant", column = "sharing_tenant") 
      })
	  @Select({"<script>", SELECT_DETTAGLIO_DATASET,WHERE_DETTAGLIO_DATASET_START + 
		  			WHERE_DETTAGLIO_DATASET_IDDATASET+ WHERE_DETTAGLIO_DATASET_MAX_VERSION_OPT_ONLY_INSTALLED+"</script>"}) 
	  DettaglioDataset selectDettaglioDatasetByIdDataset( @Param("idDataSet") Integer idDataset,
			  @Param("onlyInstalled") boolean onlyInstalled );
	
	
	@Results({
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"),
		@Result(property = "idDataSource", column = "id_data_source"),
		@Result(property = "description", column = "dataset_description"),
		@Result(property = "idDatasetType", column = "id_dataset_type"),	
		@Result(property = "idDatasetSubtype", column = "id_dataset_subtype"),
	    @Result(property = "idDataSourceBinary", column = "id_data_source_binary"),
	    @Result(property = "datasourceversionBinary", column = "datasourceversion_binary"),	
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
		@Result(property = "dataSourceIsActive", column = "isactive"),
		@Result(property = "dataSourceIsManager", column = "ismanager"),
		@Result(property = "tenantCode", column = "tenantcode"),
		@Result(property = "tenantName", column = "tenant_name"),
		@Result(property = "tenantDescription", column = "tenant_description"),
		@Result(property = "idTenant", column = "id_tenant"),
		@Result(property = "datasetType", column = "dataset_type"),
		@Result(property = "datasetTypeDescription", column = "dataset_type_description"),
		@Result(property = "datasetSubtype", column = "dataset_subtype"), 	
		@Result(property = "datasetSubtypeDescription", column = "dataset_subtype_description"), 
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"), 	
		@Result(property = "dataSourceIsopendata", column = "data_source_is_opendata"), 
		@Result(property = "dataSourceExternalReference", column = "data_source_external_reference"), 
		@Result(property = "dataSourceOpenDataAuthor", column = "data_source_open_data_author"), 
		@Result(property = "dataSourceOpenDataUpdateDate", column = "data_source_open_data_update_date"),
		@Result(property = "dataSourceOpenDataUpdateFrequency", column = "data_source_open_data_update_frequency"),
		@Result(property = "dataSourceOpenDataLanguage", column = "data_source_open_data_language"), 
		@Result(property = "dataSourceLastUpdate", column = "data_source_last_update"), 
		@Result(property = "dataSourceDisclaimer", column = "data_source_disclaimer"), 
		@Result(property = "dataSourceRequesterName", column = "data_source_requester_name"), 
		@Result(property = "dataSourceRequesterSurname", column = "data_source_requester_surname"), 
		@Result(property = "dataSourceRequesterMail", column = "data_source_requester_mail"), 
		@Result(property = "dataSourcePrivacyAcceptance", column = "data_source_privacy_acceptance"), 
		@Result(property = "dataSourceIcon", column = "data_source_icon"), 
		@Result(property = "sharingTenant", column = "sharing_tenant") 
      })
	  @Select({"<script>", SELECT_DETTAGLIO_DATASET,WHERE_DETTAGLIO_DATASET_START + 
		  			WHERE_DETTAGLIO_DATASET_IDDATASET+ WHERE_DETTAGLIO_DATASET_DATASOURCEVERSION+"</script>"}) 
	  DettaglioDataset selectDettaglioDatasetByIdDatasetDatasourceVersion( @Param("idDataSet") Integer idDataset, 
			  @Param("dataSourceVersion") Integer dataSourceVersion);
	
	
	@Results({
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"),
		@Result(property = "idDataSource", column = "id_data_source"),
		@Result(property = "description", column = "dataset_description"),
		@Result(property = "idDatasetType", column = "id_dataset_type"),	
		@Result(property = "idDatasetSubtype", column = "id_dataset_subtype"),
	    @Result(property = "idDataSourceBinary", column = "id_data_source_binary"),
	    @Result(property = "datasourceversionBinary", column = "datasourceversion_binary"),	
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
		@Result(property = "dataSourceIsActive", column = "isactive"),
		@Result(property = "dataSourceIsManager", column = "ismanager"),
		@Result(property = "tenantCode", column = "tenantcode"),
		@Result(property = "tenantName", column = "tenant_name"),
		@Result(property = "tenantDescription", column = "tenant_description"),
		@Result(property = "idTenant", column = "id_tenant"),
		@Result(property = "datasetType", column = "dataset_type"),
		@Result(property = "datasetTypeDescription", column = "dataset_type_description"),
		@Result(property = "datasetSubtype", column = "dataset_subtype"), 	
		@Result(property = "datasetSubtypeDescription", column = "dataset_subtype_description"), 
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"), 	
		@Result(property = "dataSourceIsopendata", column = "data_source_is_opendata"), 
		@Result(property = "dataSourceExternalReference", column = "data_source_external_reference"), 
		@Result(property = "dataSourceOpenDataAuthor", column = "data_source_open_data_author"), 
		@Result(property = "dataSourceOpenDataUpdateDate", column = "data_source_open_data_update_date"), 
		@Result(property = "dataSourceOpenDataUpdateFrequency", column = "data_source_open_data_update_frequency"),
		@Result(property = "dataSourceOpenDataLanguage", column = "data_source_open_data_language"), 
		@Result(property = "dataSourceLastUpdate", column = "data_source_last_update"), 
		@Result(property = "dataSourceDisclaimer", column = "data_source_disclaimer"), 
		@Result(property = "dataSourceRequesterName", column = "data_source_requester_name"), 
		@Result(property = "dataSourceRequesterSurname", column = "data_source_requester_surname"), 
		@Result(property = "dataSourceRequesterMail", column = "data_source_requester_mail"), 
		@Result(property = "dataSourcePrivacyAcceptance", column = "data_source_privacy_acceptance"), 
		@Result(property = "dataSourceIcon", column = "data_source_icon"), 
		@Result(property = "sharingTenant", column = "sharing_tenant") 
      })
	  @Select({"<script>", SELECT_DETTAGLIO_DATASET,WHERE_DETTAGLIO_DATASET_START + 
		  			WHERE_DETTAGLIO_DATASET_DATASETCODE+ WHERE_DETTAGLIO_DATASET_MAX_VERSION_OPT_ONLY_INSTALLED+"</script>"}) 
	  DettaglioDataset selectDettaglioDatasetByDatasetCode(
			  @Param("datasetCode")  String datasetCode, @Param("onlyInstalled") boolean onlyInstalled);
	
	
		// dataset icon for metadataapi
		@Select({"<script>", "SELECT icon FROM " + DataSourceMapper.DATA_SOURCE_TABLE + ", " + DATASET_TABLE ,WHERE_DETTAGLIO_DATASET_START + 
	  			WHERE_DETTAGLIO_DATASET_DATASETCODE+ WHERE_DETTAGLIO_DATASET_MAX_VERSION_OPT_ONLY_INSTALLED+
	  			" AND yucca_data_source.id_data_source = yucca_dataset.id_data_source AND  yucca_data_source.datasourceversion = yucca_dataset.datasourceversion" +
				"</script>"}) 
		String  selectDatasetIconByStreamcodeAndSoCode( @Param("datasetCode") String datasetCode, @Param("onlyInstalled") boolean onlyInstalled);	

	
	

	
	@Results({
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"),
		@Result(property = "idDataSource", column = "id_data_source"),
		@Result(property = "description", column = "dataset_description"),
		@Result(property = "idDatasetType", column = "id_dataset_type"),	
		@Result(property = "idDatasetSubtype", column = "id_dataset_subtype"),
	    @Result(property = "idDataSourceBinary", column = "id_data_source_binary"),
	    @Result(property = "datasourceversionBinary", column = "datasourceversion_binary"),	
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
		@Result(property = "dataSourceIsActive", column = "isactive"),
		@Result(property = "dataSourceIsManager", column = "ismanager"),
		@Result(property = "tenantCode", column = "tenantcode"),
		@Result(property = "tenantName", column = "tenant_name"),
		@Result(property = "tenantDescription", column = "tenant_description"),
		@Result(property = "idTenant", column = "id_tenant"),
		@Result(property = "datasetType", column = "dataset_type"),
		@Result(property = "datasetTypeDescription", column = "dataset_type_description"),
		@Result(property = "datasetSubtype", column = "dataset_subtype"), 	
		@Result(property = "datasetSubtypeDescription", column = "dataset_subtype_description"), 
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"), 	
		@Result(property = "dataSourceIsopendata", column = "data_source_is_opendata"), 
		@Result(property = "dataSourceExternalReference", column = "data_source_external_reference"), 
		@Result(property = "dataSourceOpenDataAuthor", column = "data_source_open_data_author"), 
		@Result(property = "dataSourceOpenDataUpdateDate", column = "data_source_open_data_update_date"), 
		@Result(property = "dataSourceOpenDataUpdateFrequency", column = "data_source_open_data_update_frequency"),
		@Result(property = "dataSourceOpenDataLanguage", column = "data_source_open_data_language"), 
		@Result(property = "dataSourceLastUpdate", column = "data_source_last_update"), 
		@Result(property = "dataSourceDisclaimer", column = "data_source_disclaimer"), 
		@Result(property = "dataSourceRequesterName", column = "data_source_requester_name"), 
		@Result(property = "dataSourceRequesterSurname", column = "data_source_requester_surname"), 
		@Result(property = "dataSourceRequesterMail", column = "data_source_requester_mail"), 
		@Result(property = "dataSourcePrivacyAcceptance", column = "data_source_privacy_acceptance"), 
		@Result(property = "dataSourceIcon", column = "data_source_icon"), 
		@Result(property = "sharingTenant", column = "sharing_tenant") 
      })
	  @Select({"<script>", SELECT_DETTAGLIO_DATASET,WHERE_DETTAGLIO_DATASET_START + 
		  			WHERE_DETTAGLIO_DATASET_DATASETCODE+ WHERE_DETTAGLIO_DATASET_DATASOURCEVERSION+"</script>"}) 
	  DettaglioDataset selectDettaglioDatasetByDatasetCodeDatasourceVersion(
			  @Param("datasetCode")  String datasetCode, @Param("dataSourceVersion") Integer dataSourceVersion);	
	
	
	@Results({
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"),
		@Result(property = "idDataSource", column = "id_data_source"),
		@Result(property = "description", column = "dataset_description"),
		@Result(property = "idDatasetType", column = "id_dataset_type"),	
		@Result(property = "idDatasetSubtype", column = "id_dataset_subtype"),
	    @Result(property = "idDataSourceBinary", column = "id_data_source_binary"),
	    @Result(property = "datasourceversionBinary", column = "datasourceversion_binary"),	
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
		@Result(property = "dataSourceIsActive", column = "isactive"),
		@Result(property = "dataSourceIsManager", column = "ismanager"),
		@Result(property = "tenantCode", column = "tenantcode"),
		@Result(property = "tenantName", column = "tenant_name"),
		@Result(property = "tenantDescription", column = "tenant_description"),
		@Result(property = "idTenant", column = "id_tenant"),
		@Result(property = "datasetType", column = "dataset_type"),
		@Result(property = "datasetTypeDescription", column = "dataset_type_description"),
		@Result(property = "datasetSubtype", column = "dataset_subtype"), 	
		@Result(property = "datasetSubtypeDescription", column = "dataset_subtype_description"), 
		@Result(property = "dataSourceCopyright", column = "data_source_copyright"), 	
		@Result(property = "dataSourceIsopendata", column = "data_source_is_opendata"), 
		@Result(property = "dataSourceExternalReference", column = "data_source_external_reference"), 
		@Result(property = "dataSourceOpenDataAuthor", column = "data_source_open_data_author"), 
		@Result(property = "dataSourceOpenDataUpdateDate", column = "data_source_open_data_update_date"), 
		@Result(property = "dataSourceOpenDataUpdateFrequency", column = "data_source_open_data_update_frequency"),
		@Result(property = "dataSourceOpenDataLanguage", column = "data_source_open_data_language"), 
		@Result(property = "dataSourceLastUpdate", column = "data_source_last_update"), 
		@Result(property = "dataSourceDisclaimer", column = "data_source_disclaimer"), 
		@Result(property = "dataSourceRequesterName", column = "data_source_requester_name"), 
		@Result(property = "dataSourceRequesterSurname", column = "data_source_requester_surname"), 
		@Result(property = "dataSourceRequesterMail", column = "data_source_requester_mail"), 
		@Result(property = "dataSourcePrivacyAcceptance", column = "data_source_privacy_acceptance"), 
		@Result(property = "dataSourceIcon", column = "data_source_icon"), 
		@Result(property = "sharingTenant", column = "sharing_tenant")
		
      })		
	@Select({"<script>", SELECT_DETTAGLIO_DATASET_CON_JDBC,WHERE_DETTAGLIO_DATASET_START + WHERE_DETTAGLIO_DATASET_MAX_VERSION 
		+ WHERE_DETTAGLIO_DATASET_TENANT_MANAGER_CODE  
		+ WHERE_DETTAGLIO_DATASET_ORGANIZATION_CODE
		+ WHERE_DETTAGLIO_DATASET_TENANT_VISIBILITY
		+ WHERE_DETTAGLIO_DATASET_JDBC_PARAMS,  "</script>"}) 
	List<DettaglioDataset> selectDatasetFromJdbc(@Param("jdbcdburl") String jdbcdburl, 
									@Param("jdbcdbname") String jdbcdbname, 
									@Param("jdbcdbtype") String jdbcdbtype, 
									@Param("tenantCodeManager") String tenantCodeManager,
								    @Param("organizationCode") String organizationcode,
								    @Param("userAuthorizedTenantCodeList") List<String> userAuthorizedTenantCodeList);	

	
	
	/********************************************************************************************
	 * 				SELECT DATASETS
	 *******************************************************************************************/
	public static final String SELECT_DATA_SETS = 
			" SELECT " + 
			" yucca_dataset.id_data_source, " + 
			" yucca_dataset.datasourceversion, " + 
			" yucca_dataset.iddataset, " + 
			" yucca_dataset.datasetcode, " + 
			" yucca_dataset.datasetname, " + 
			" yucca_dataset.description, " + 
			" yucca_dataset.id_data_source_binary, " + 
			" yucca_dataset.datasourceversion_binary, " + 
			
			" yucca_data_source.visibility data_source_visibility, " + 
			" yucca_data_source.unpublished data_source_unpublished, " + 
			" yucca_data_source.registrationdate data_source_registration_date, " + 

// DATA SET TYPE
			" yucca_d_dataset_type.description dataset_type_description, " + 
			" yucca_d_dataset_type.dataset_type, " + 
			" yucca_d_dataset_type.id_dataset_type, " +

// DATA SET SUB TYPE
			" yucca_d_dataset_subtype.id_dataset_subtype, " + 
			" yucca_d_dataset_subtype.dataset_subtype, " + 
			" yucca_d_dataset_subtype.description dataset_subtype_description, " +
			
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
			" INNER JOIN " + DatasetTypeMapper.DATASET_TYPE_TABLE  + " yucca_d_dataset_type ON yucca_dataset.id_dataset_type = yucca_d_dataset_type.id_dataset_type " +
			" INNER JOIN " + DatasetSubtypeMapper.DATASET_SUBTYPE_TABLE  + " yucca_d_dataset_subtype ON yucca_dataset.id_dataset_subtype = yucca_d_dataset_subtype.id_dataset_subtype " +
			
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
		
		@Result(property = "idDataSourceBinary", column = "id_data_source_binary"), 
		@Result(property = "datasourceversionBinary", column = "datasourceversion_binary"), 
		@Result(property = "datasetTypeDescription", column = "dataset_type_description"), 
		@Result(property = "datasetType", column = "dataset_type"),
		@Result(property = "idDatasetType", column = "id_dataset_type"),
		@Result(property = "idDatasetSubtype", column = "id_dataset_subtype"), 
		@Result(property = "datasetSubtype", column = "dataset_subtype"),
		@Result(property = "datasetSubtypeDescription", column = "dataset_subtype_description"),
		
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
	" INSERT INTO yucca_dataset( iddataset, id_data_source, datasourceversion, datasetcode, datasetname, description, "
	+ "startingestiondate, endingestiondate, importfiletype, id_dataset_type, id_dataset_subtype, solrcollectionname, "
	+ "phoenixtablename, phoenixschemaname, availablehive, availablespeed, istransformed, dbhiveschema, dbhivetable, "
	+ "id_data_source_binary, datasourceversion_binary, jdbcdburl, jdbcdbname, jdbcdbtype, jdbctablename) "
	+ "VALUES (#{iddataset}, #{idDataSource}, #{datasourceversion}, #{datasetcode}, #{datasetname}, #{description}, #{startingestiondate}, "
	+ "#{endingestiondate}, #{importfiletype}, #{idDatasetType}, #{idDatasetSubtype}, #{solrcollectionname}, #{phoenixtablename}, "
	+ "#{phoenixschemaname}, #{availablehive}, #{availablespeed}, #{istransformed}, #{dbhiveschema}, #{dbhivetable}, "
	+ "#{idDataSourceBinary}, #{datasourceversionBinary}, #{jdbcdburl},#{jdbcdbname}, #{jdbcdbtype}, #{jdbctablename})";
	@Insert(INSERT_DATASET)
	int insertDataset(Dataset dataset);

	
	/*************************************************************************
	* 					SELECT DATASETS FROM JDBC
	* ***********************************************************************/
	public static final String SELECT_DATASET_FROM_JDBC = 
//	" SELECT yucca_dataset.id_data_source, yucca_dataset.datasourceversion, iddataset, datasetcode, datasetname, " +
//	"description, startingestiondate, endingestiondate, importfiletype, " +
//	"id_dataset_type, id_dataset_subtype, solrcollectionname, phoenixtablename, " +
//	"phoenixschemaname, availablehive, availablespeed, istransformed, " +
//	"dbhiveschema, dbhivetable, id_data_source_binary, datasourceversion_binary, " +
//	"jdbcdburl, jdbcdbname, jdbcdbtype, jdbctablename " +
//	" FROM " +  DatasetMapper.DATASET_TABLE  + " yucca_dataset  " +
//	" INNER JOIN " + DataSourceMapper.DATA_SOURCE_TABLE  + " yucca_data_source ON yucca_dataset.id_data_source = yucca_data_source.id_data_source AND  " +
//	" yucca_dataset.datasourceversion = yucca_data_source.datasourceversion  " +
//	" LEFT JOIN " + TenantMapper.R_TENANT_DATA_SOURCE_TABLE  + " yucca_r_tenant_data_source ON yucca_r_tenant_data_source.id_data_source = yucca_data_source.id_data_source " + 
//	" INNER JOIN " + OrganizationMapper.ORGANIZATION_TABLE + " yucca_organization ON  yucca_data_source.id_organization = yucca_organization.id_organization " +
//	" where " + 
//	" yucca_r_tenant_data_source.datasourceversion = yucca_data_source.datasourceversion AND  " +
//	" yucca_r_tenant_data_source.isactive = 1 AND  " +
//	" yucca_r_tenant_data_source.ismanager = 1  " + 
//	" where jdbcdburl=#{jdbcdburl} and jdbcdbname=#{jdbcdbname} and jdbcdbtype=#{jdbcdbtype} and id_tenant=#{idTenant} " + 
//	" (yucca_data_source.id_data_source, yucca_data_source.datasourceversion) IN " + 
//	"    (select id_data_source, max(datasourceversion) from " + DataSourceMapper.DATA_SOURCE_TABLE  + "  where id_data_source = yucca_dataset.id_data_source group by id_data_source) " +
//	"  AND yucca_organization.organizationcode = #{organizationCode} ";
	" SELECT " + 
	" yucca_dataset.iddataset, " + 
	" yucca_dataset.id_data_source, " +
	" yucca_dataset.datasetcode, " + 
	" yucca_dataset.datasetname, " + 
	" yucca_dataset.datasourceversion, " + 
	" yucca_d_status.id_status, " + 
	" yucca_d_status.statuscode, " + 
	" yucca_d_status.description status_description, " + 
	" yucca_organization.organizationcode, " + 
	" yucca_organization.description organization_description, " + 
	" yucca_organization.id_organization, " + 
	" yucca_r_tenant_data_source.isactive, " + 
	" yucca_r_tenant_data_source.ismanager, " + 
	" yucca_tenant.tenantcode, " + 
	" yucca_tenant.name tenant_name, " + 
	" yucca_tenant.description tenant_description, " + 
	" yucca_tenant.id_tenant, " +
	" yucca_data_source.id_subdomain, " +
	" yucca_d_subdomain.id_domain, " +
	" yucca_dataset.jdbcdburl,"+
	" yucca_dataset.jdbcdbname,"+
	" yucca_dataset.jdbcdbtype, "+
	" yucca_dataset.jdbctablename" +
	" FROM " +  DATASET_TABLE + " yucca_dataset " + 
	" INNER JOIN " + DataSourceMapper.DATA_SOURCE_TABLE  + " yucca_data_source ON yucca_dataset.id_data_source = yucca_data_source.id_data_source AND yucca_dataset.datasourceversion = yucca_data_source.datasourceversion " +
	" INNER JOIN " + OrganizationMapper.ORGANIZATION_TABLE + " yucca_organization ON  yucca_data_source.id_organization = yucca_organization.id_organization " +
	" INNER JOIN " + SmartobjectMapper.STATUS_TABLE + " yucca_d_status ON yucca_data_source.id_status = yucca_d_status.id_status " + 
	" INNER JOIN " + SubdomainMapper.SUBDOMAIN_TABLE  + " yucca_d_subdomain ON yucca_data_source.id_subdomain = yucca_d_subdomain.id_subdomain " + 
	" LEFT JOIN " + TenantMapper.R_TENANT_DATA_SOURCE_TABLE  + " yucca_r_tenant_data_source ON yucca_r_tenant_data_source.id_data_source = yucca_data_source.id_data_source AND " +
	"                     yucca_r_tenant_data_source.datasourceversion = yucca_data_source.datasourceversion AND " +
	"                     yucca_r_tenant_data_source.isactive = 1 AND yucca_r_tenant_data_source.ismanager = 1 " +
	" LEFT JOIN " + TenantMapper.TENANT_TABLE  + " yucca_tenant ON yucca_tenant.id_tenant = yucca_r_tenant_data_source.id_tenant " +
	" WHERE  " +
	" jdbcdburl=#{jdbcdburl} and jdbcdbname=#{jdbcdbname} and jdbcdbtype=#{jdbcdbtype} and tenantcode=#{tenantCode} AND yucca_data_source.id_status!=5 and " + 
	" (yucca_data_source.id_data_source, yucca_data_source.datasourceversion) IN " + 
	"    (select id_data_source, max(datasourceversion) from " + DataSourceMapper.DATA_SOURCE_TABLE  + "  where id_data_source = yucca_dataset.id_data_source group by id_data_source) " +
	"  AND yucca_organization.organizationcode = #{organizationcode} " +

	"<if test=\"tenantCodeManager != null\">" +
	" AND yucca_tenant.tenantcode =  #{tenantCodeManager} " +
	"</if>" +
	"  AND (yucca_data_source.visibility = 'public' OR " +
	"   EXISTS ( " +
	"   SELECT yucca_tenant.tenantcode " + 
	"   FROM " + TenantMapper.TENANT_TABLE  + " yucca_tenant , " + TenantMapper.R_TENANT_DATA_SOURCE_TABLE + " yucca_r_tenant_data_source " +
	"   WHERE yucca_tenant.id_tenant = yucca_r_tenant_data_source.id_tenant " + 
	"   AND yucca_r_tenant_data_source.id_data_source = yucca_data_source.id_data_source AND " +
	"       yucca_r_tenant_data_source.datasourceversion = yucca_data_source.datasourceversion AND " +
	"       yucca_r_tenant_data_source.isactive = 1 AND tenantcode IN ("
	+ " <foreach item=\"authorizedTenantCode\" separator=\",\" index=\"index\" collection=\"userAuthorizedTenantCodeList\">"
	+ "#{authorizedTenantCode}"
	+ " </foreach>"
	+ ") " +
	"   ) " +
	"   ) ";
	@Results({
		@Result(property = "idDataSource", column = "id_data_source"),
		@Result(property = "idStatus", column = "id_status"),
		@Result(property = "statusCode", column = "statuscode"), 
		@Result(property = "statusDescription", column = "status_description"),
		@Result(property = "organizationCode", column = "organizationcode"), 
		@Result(property = "organizationDescription", column = "organization_description"),
		@Result(property = "idOrganization", column = "id_organization"), 
		@Result(property = "tenantCode", column = "tenantcode"), 
		@Result(property = "tenantName", column = "tenant_name"), 
		@Result(property = "tenantDescription", column = "tenant_description"), 
		@Result(property = "idTenant", column = "id_tenant"),
		@Result(property = "subIdSubDomain", column = "id_subdomain"),
		@Result(property = "domIdDomain", column = "id_domain") 
      })		
	@Select({"<script>", SELECT_DATASET_FROM_JDBC, "</script>"}) 
	List<DettaglioDataset> selectDatasetFromJdbc_old(@Param("jdbcdburl") String jdbcdburl, @Param("jdbcdbname") String jdbcdbname, @Param("jdbcdbtype") String jdbcdbtype, 
			@Param("tenantCode") String tenantCode, @Param("organizationcode") String organizationcode, @Param("tenantCodeManager") String  tenantCodeManager, @Param("userAuthorizedTenantCodeList") List<String> userAuthorizedTenantCodeList);






	


	
	
}
