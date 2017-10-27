package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.Stream;
import org.csi.yucca.adminapi.util.Constants;

public interface StreamMapper {

	String STREAM_TABLE = Constants.SCHEMA_DB + "yucca_stream";
	
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