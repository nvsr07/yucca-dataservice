package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
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
	 * 					INSERT TAG-DATA SOURCE
	 * ***********************************************************************/
	public static final String INSERT_R_TAG_DATA_SOURCE = 
	" INSERT INTO " + R_TAG_DATA_SOURCE_TABLE + "(id_data_source, datasourceversion, id_tag) VALUES (#{idDataSource},#{datasourceversion},#{idTag})";
	@Insert(INSERT_R_TAG_DATA_SOURCE)
	int insertTagDataSource(@Param("idDataSource") Integer idDataSource, @Param("datasourceversion") Integer datasourceversion, 
			@Param("idTag") Integer idTag);

	/*************************************************************************
	 * 					INSERT DATA SOURCE
	 * ***********************************************************************/
	public static final String INSERT_DATA_SOURCE = 
	" INSERT INTO " + DATA_SOURCE_TABLE + "( datasourceversion, iscurrent, name, visibility, copyright, disclaimer, "
			+ "registrationdate, requestername, requestersurname, requestermail, privacyacceptance, icon, isopendata, "
			+ "opendataexternalreference, opendataauthor, opendataupdatedate, opendatalanguage, lastupdate, unpublished, "
			+ "fabriccontrolleroutcome, fbcoperationfeedback, id_organization, id_subdomain, id_dcat, id_license, id_status) "
			+ "VALUES (#{datasourceversion}, #{iscurrent}, #{name}, #{visibility}, #{copyright}, #{disclaimer}, "
			+ "#{registrationdate}, #{requestername}, #{requestersurname}, #{requestermail}, #{privacyacceptance}, #{icon}, #{isopendata}, "
			+ "#{opendataexternalreference}, #{opendataauthor}, #{opendataupdatedate}, #{opendatalanguage}, #{lastupdate}, #{unpublished}, "
			+ "#{fabriccontrolleroutcome}, #{fbcoperationfeedback}, #{idOrganization}, #{idSubdomain}, #{idDcat}, #{idLicense}, #{idStatus})";
	@Insert(INSERT_DATA_SOURCE)
	@Options(useGeneratedKeys=true, keyProperty="idDataSource")
	int insertDataSource(DataSource dataSource);

	
	
	
	
}
