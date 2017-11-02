package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.csi.yucca.adminapi.model.Api;
import org.csi.yucca.adminapi.util.Constants;

/**
 * 
 * @author gianfranco.stolfa
 *
 */
public interface ApiMapper {
	
	public static final String COMPONENT_TABLE = Constants.SCHEMA_DB + "yucca_component";

	/*************************************************************************
	 * 					INSERT API
	 * ***********************************************************************/
	public static final String INSERT_API = 
	" INSERT INTO int_yucca.yucca_api( apicode, apiname, apitype, apisubtype, id_data_source, datasourceversion ) "
	+ "VALUES (#{apicode}, #{apiname}, #{apitype}, #{apisubtype}, #{idDataSource}, #{datasourceversion})";
	@Insert(INSERT_API)
	@Options(useGeneratedKeys=true, keyProperty="idapi")
	int insertApi(Api api);
	
}
