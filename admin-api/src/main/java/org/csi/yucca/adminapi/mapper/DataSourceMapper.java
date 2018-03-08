package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.csi.yucca.adminapi.model.DataSource;
import org.csi.yucca.adminapi.util.Constants;

/**
 * 
 * @author gianfranco.stolfa
 *
 */
public interface DataSourceMapper {
	
	public static final String DATA_SOURCE_TABLE = Constants.SCHEMA_DB + "yucca_data_source";
	public static final String R_TAG_DATA_SOURCE_TABLE = Constants.SCHEMA_DB + "yucca_r_tag_data_source";
	

	/*************************************************************************
	 * 					DELETE TAG-DATA SOURCE
	 * ***********************************************************************/
	public static final String DELETE_R_TAG_DATA_SOURCE =
	" DELETE FROM " + R_TAG_DATA_SOURCE_TABLE + " WHERE id_data_source = #{idDataSource} AND datasourceversion=#{dataSourceVersion}";
	@Delete(DELETE_R_TAG_DATA_SOURCE)
	int deleteTagDataSource(@Param("idDataSource") Integer idDataSource, @Param("dataSourceVersion") Integer dataSourceVersion);
	
	/*************************************************************************
	 * 					INSERT TAG-DATA SOURCE
	 * ***********************************************************************/
	public static final String INSERT_R_TAG_DATA_SOURCE = 
	" INSERT INTO " + R_TAG_DATA_SOURCE_TABLE + "(id_data_source, datasourceversion, id_tag) VALUES (#{idDataSource},#{datasourceversion},#{idTag})";
	@Insert(INSERT_R_TAG_DATA_SOURCE)
	int insertTagDataSource(@Param("idDataSource") Integer idDataSource, @Param("datasourceversion") Integer datasourceversion, 
			@Param("idTag") Integer idTag);

	/*************************************************************************
	 * 					clone DATA SOURCE con new version
	 * ***********************************************************************/
	public static final String CLONE_DATA_SOURCE = 
	" INSERT INTO " + DATA_SOURCE_TABLE + "( " +
		" id_data_source, datasourceversion, iscurrent, name, visibility, " + 
		" copyright, disclaimer, registrationdate, requestername, requestersurname, " + 
		" requestermail, privacyacceptance, icon, isopendata, externalreference, " + 
		" opendataauthor, opendataupdatedate, opendatalanguage, lastupdate, " + 
		" unpublished, fabriccontrolleroutcome, fbcoperationfeedback, id_organization, " + 
		" id_subdomain, id_dcat, id_license, id_status) " +
	" SELECT id_data_source, #{newDataSourceVersion}, iscurrent, name, visibility, " + 
		" copyright, disclaimer, registrationdate, requestername, requestersurname, " + 
		" requestermail, privacyacceptance, icon, isopendata, externalreference, " + 
		" opendataauthor, opendataupdatedate, opendatalanguage, lastupdate, " + 
		" unpublished, fabriccontrolleroutcome, fbcoperationfeedback, id_organization, " + 
		" id_subdomain, id_dcat, id_license, id_status " +
	" FROM " +  DATA_SOURCE_TABLE +       
	" WHERE id_data_source = #{idDataSource} and datasourceversion=#{currentDataSourceVersion}";
	@Insert(CLONE_DATA_SOURCE)
	int cloneDataSource(@Param("newDataSourceVersion") Integer newDataSourceVersion,
			@Param("currentDataSourceVersion") Integer currentDataSourceVersion, 
			@Param("idDataSource") Integer idDataSource);
	
	/*************************************************************************
	 * 					clone DATA SOURCE con new version e status
	 * ***********************************************************************/
	public static final String CLONE_DATA_SOURCE_NEW_VERSION_AND_STATUS = 
	" INSERT INTO " + DATA_SOURCE_TABLE + "( " +
		" id_data_source, datasourceversion, iscurrent, name, visibility, " + 
		" copyright, disclaimer, registrationdate, requestername, requestersurname, " + 
		" requestermail, privacyacceptance, icon, isopendata, externalreference, " + 
		" opendataauthor, opendataupdatedate, opendatalanguage, lastupdate, " + 
		" unpublished, fabriccontrolleroutcome, fbcoperationfeedback, id_organization, " + 
		" id_subdomain, id_dcat, id_license, id_status) " +
	" SELECT id_data_source, #{newDataSourceVersion}, iscurrent, name, visibility, " + 
		" copyright, disclaimer, registrationdate, requestername, requestersurname, " + 
		" requestermail, privacyacceptance, icon, isopendata, externalreference, " + 
		" opendataauthor, opendataupdatedate, opendatalanguage, lastupdate, " + 
		" unpublished, fabriccontrolleroutcome, fbcoperationfeedback, id_organization, " + 
		" id_subdomain, id_dcat, id_license, #{idStatus} " +
	" FROM " +  DATA_SOURCE_TABLE +       
	" WHERE id_data_source = #{idDataSource} and datasourceversion=#{currentDataSourceVersion}";
	@Insert(CLONE_DATA_SOURCE_NEW_VERSION_AND_STATUS)
	int cloneDataSourceNewVersionAndStatus(@Param("newDataSourceVersion") Integer newDataSourceVersion,
			@Param("currentDataSourceVersion") Integer currentDataSourceVersion, 
			@Param("idDataSource") Integer idDataSource,
			@Param("idStatus") Integer idStatus);
	
	
	/*************************************************************************
	 * 					INSERT DATA SOURCE
	 * ***********************************************************************/
	public static final String INSERT_DATA_SOURCE = 
	" INSERT INTO " + DATA_SOURCE_TABLE + "( datasourceversion, iscurrent, name, visibility, copyright, disclaimer, "
			+ "registrationdate, requestername, requestersurname, requestermail, privacyacceptance, icon, isopendata, "
			+ "externalreference, opendataauthor, opendataupdatedate, opendatalanguage, lastupdate, unpublished, "
			+ "fabriccontrolleroutcome, fbcoperationfeedback, id_organization, id_subdomain, id_dcat, id_license, id_status) "
	+ "VALUES (#{datasourceversion}, #{iscurrent}, #{name}, #{visibility}, #{copyright}, #{disclaimer}, "
			+ "#{registrationdate}, #{requestername}, #{requestersurname}, #{requestermail}, #{privacyacceptance}, #{icon}, #{isopendata}, "
			+ "#{externalreference}, #{opendataauthor}, #{opendataupdatedate}, #{opendatalanguage}, #{lastupdate}, #{unpublished}, "
			+ "#{fabriccontrolleroutcome}, #{fbcoperationfeedback}, #{idOrganization}, #{idSubdomain}, #{idDcat}, #{idLicense}, #{idStatus})";
	@Insert(INSERT_DATA_SOURCE)
	@Options(useGeneratedKeys=true, keyProperty="idDataSource", keyColumn="id_data_source")
	int insertDataSource(DataSource dataSource);
	
	/*************************************************************************
	 * 					UPDATE DATA SOURCE
	 * ***********************************************************************/
	public static final String UPDATE_DATA_SOURCE = 
		" UPDATE " + DATA_SOURCE_TABLE  +
			" SET unpublished=#{unpublished}, " +
			" name=#{name}, " +
			" visibility=#{visibility}, " +
			" copyright=#{copyright}, " +
			" disclaimer=#{disclaimer}, " +
			" icon=#{icon}, " +
			" isopendata=#{isopendata}, " +
			" externalreference=#{externalreference}, " +
			" opendataauthor=#{opendataauthor}, " +
			" opendataupdatedate=#{opendataupdatedate}, " +
			" opendatalanguage=#{opendatalanguage}, " +
			" lastupdate=#{lastupdate}, " +
			" id_dcat=#{idDcat}, " +
			" id_license=#{idLicense} " +
			" WHERE id_data_source = #{idDataSource} and datasourceversion=#{datasourceversion} ";
	@Update(UPDATE_DATA_SOURCE)
	int updateDataSource(DataSource dataSource);
	
	
	/*************************************************************************
	 * 					UPDATE DATA SOURCE STATUS
	 * ***********************************************************************/
	public static final String UPDATE_DATA_SOURCE_STATUS = 
		" UPDATE " + DATA_SOURCE_TABLE  +
			" SET id_status=#{idStatus}" +
			" WHERE id_data_source = #{idDataSource} "
			+ "<if test=\"datasourceversion != null\">"
			+ " AND datasourceversion=#{datasourceversion} "
			+ "</if>";
	@Update({"<script>", UPDATE_DATA_SOURCE_STATUS, "</script>"})
	int updateDataSourceStatus(@Param("idStatus") Integer idStatus, 
			@Param("idDataSource") Integer idDataSource, 
			@Param("datasourceversion") Integer datasourceversion);

}