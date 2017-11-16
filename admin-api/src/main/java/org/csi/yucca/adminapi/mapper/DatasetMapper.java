package org.csi.yucca.adminapi.mapper;

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
