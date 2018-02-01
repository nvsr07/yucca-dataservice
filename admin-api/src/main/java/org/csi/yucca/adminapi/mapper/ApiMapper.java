package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.Api;
import org.csi.yucca.adminapi.util.Constants;

/**
 * 
 * @author gianfranco.stolfa
 *
 */
public interface ApiMapper {
	
	public static final String API_TABLE = Constants.SCHEMA_DB + "yucca_api";

	
	/*************************************************************************
	 * 					CLONE API
	 * ***********************************************************************/
	public static final String CLONE_API = 
			" INSERT INTO " + API_TABLE + " ( apicode, apiname, apitype, apisubtype, id_data_source, datasourceversion, entitynamespace, max_odata_resultperpage) " +
			" SELECT datasetcode, datasetname, #{apitype}, #{apisubtype}, #{idDataSource}, #{newDataSourceVersion}, TEXTCAT('" + Api.ENTITY_NAMESPACE + "', datasetcode), #{maxOdataResultperpage} " + 
			" FROM " + DatasetMapper.DATASET_TABLE + 
			" WHERE id_data_source = #{idDataSource} and datasourceversion = #{currentDataSourceVersion}";
		@Insert(CLONE_API)
		int cloneApi( @Param("newDataSourceVersion") Integer newDataSourceVersion, 
			          @Param("currentDataSourceVersion") Integer currentDataSourceVersion, 
			          @Param("idDataSource") Integer idDataSource,
					  @Param("apitype") String apitype,
					  @Param("apisubtype") String apisubtype,
					  @Param("maxOdataResultperpage") Integer maxOdataResultperpage
				);

		
	/*************************************************************************
	 * 					DELETE API
	 * ***********************************************************************/
	public static final String DELETE_API =
		"DELETE FROM " + API_TABLE + " WHERE id_data_source = #{idDataSource} AND datasourceversion = #{dataSourceVersion} AND apisubtype = #{apisubtype}";
	@Delete(DELETE_API)
	int deleteApi(@Param("idDataSource") Integer idDataSource, @Param("dataSourceVersion") Integer dataSourceVersion, @Param("apisubtype") String apisubtype);
	
	/*************************************************************************
	 * 					INSERT API
	 * ***********************************************************************/
	public static final String INSERT_API = 
	" INSERT INTO " + API_TABLE + "( apicode, apiname, apitype, apisubtype, id_data_source, datasourceversion, entitynamespace, max_odata_resultperpage ) "
	+ "VALUES (#{apicode}, #{apiname}, #{apitype}, #{apisubtype}, #{idDataSource}, #{datasourceversion}, #{entitynamespace}, #{maxOdataResultperpage})";
	@Insert(INSERT_API)
	@Options(useGeneratedKeys=true, keyProperty="idapi")
	int insertApi(Api api);
	
	/*************************************************************************
	 * 					SELECT API
	 * ***********************************************************************/
	public static final String SELECT_API_BY_CODE = 
	"SELECT idapi, apicode, apiname, apitype, apisubtype, id_data_source, datasourceversion, entitynamespace, max_odata_resultperpage "
	+ " FROM " + API_TABLE + " WHERE apicode=#{apicode} AND (id_data_source, datasourceversion) IN " + 
			" (select api2.id_data_source, max(api2.datasourceversion) from " +  API_TABLE + " api2 "
			+ "  where api2.id_data_source = id_data_source group by id_data_source) ";
	@Results({
	    @Result(property = "maxOdataResultperpage", column = "max_odata_resultperpage"),
	    @Result(property = "idapi", column = "idapi"),
	    @Result(property = "apicode", column = "apicode"),
	    @Result(property = "apiname", column = "apiname"),
	    @Result(property = "apitype", column = "apitype"),
	    @Result(property = "apisubtype", column = "apisubtype"),
	    @Result(property = "idDataSource", column = "id_data_source"),
	    @Result(property = "datasourceversion", column = "datasourceversion"),
	    @Result(property = "entitynamespace", column = "entitynamespace")
	  })
	@Select(SELECT_API_BY_CODE) 
		Api selectApi(@Param("apicode") String apiCode);
		
	}
