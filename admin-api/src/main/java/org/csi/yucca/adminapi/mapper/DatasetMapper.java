package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Insert;
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
