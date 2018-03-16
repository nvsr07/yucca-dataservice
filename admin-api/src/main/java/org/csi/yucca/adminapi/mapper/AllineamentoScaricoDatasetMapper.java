package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.csi.yucca.adminapi.model.AllineamentoScaricoDataset;
import org.csi.yucca.adminapi.util.Constants;

/**
 * 
 * @author gianfranco.stolfa
 *
 */
public interface AllineamentoScaricoDatasetMapper {

	public static final String ALLINEAMENTO_TABLE = Constants.SCHEMA_DB + "yucca_allineamento_scarico_dataset";
	
	/*************************************************************************
	 * 
	 * 					INSERT ALLINEAMENTO
	 * 
	 * ***********************************************************************/
	public static final String INSERT_ALLINEAMENTO_SCARICO_DATASET =
	"INSERT INTO " + ALLINEAMENTO_TABLE + "( id_organization, id_dataset, dataset_version, last_mongo_object_id) "
			+ " VALUES (#{idOrganization}, #{idDataset}, #{datasetVersion}, #{lastMongoObjectId})";
	@Insert(INSERT_ALLINEAMENTO_SCARICO_DATASET)                      
	int insertAllineamentoScaricoDataset(AllineamentoScaricoDataset allineamento);
	
	/*******************************************************************************************
	 * 
	 * 					SELECT ALLINEAMENTO BY ID ORGANIZATION, DATASET AND VERSION
	 * 
	 ********************************************************************************************/
	public static final String SELECT_LAST_MONGO_OBJECT_ID = 
			" SELECT last_mongo_object_id " +
			" FROM int_yucca.yucca_allineamento_scarico_dataset " +
			" where id_organization = #{idOrganization} and " +
			" id_dataset            = #{idDataset} and " +
			" dataset_version       = #{datasetVersion} ";
	@Select(SELECT_LAST_MONGO_OBJECT_ID)
	String selectLastMongoObjectId(
			@Param("idOrganization") Integer idOrganization,
			@Param("idDataset") Integer idDataset,
			@Param("datasetVersion") Integer datasetVersion );
	

	/*******************************************************************************************
	 * 
	 * 					SELECT LIST ALLINEAMENTO BY ID ORGANIZATION
	 * 
	 ********************************************************************************************/
	public static final String SELECT_ALLINEAMENTO_BY_ID_ORG = 
			" SELECT id_organization, id_dataset, dataset_version, last_mongo_object_id " +
			" FROM " + ALLINEAMENTO_TABLE + " where id_organization = #{idOrganization} ";
	@Results({
		@Result(property = "idOrganization", column = "id_organization"),
		@Result(property = "idDataset", column = "id_dataset"),
		@Result(property = "lastMongoObjectId", column = "last_mongo_object_id"),
		@Result(property = "datasetVersion", column = "dataset_version")
	})
	@Select(SELECT_ALLINEAMENTO_BY_ID_ORG)
	List<AllineamentoScaricoDataset> selectAllineamentoScaricoDatasetByOrganization(@Param("idOrganization") Integer idOrganization);
	
	/*************************************************************************
	 * 
	 * 					UPDATE OBJECT ID
	 * 
	 * ***********************************************************************/
	public static final String UPDATE_LAST_MONGO_OBJECT_ID =
	" UPDATE int_yucca.yucca_allineamento_scarico_dataset " +
	" SET last_mongo_object_id = #{lastMongoObjectId} " +
	" WHERE id_organization    = #{idOrganization} and  " +
	" id_dataset               = #{idDataset} and  " +
	" dataset_version          = #{datasetVersion} ";
	@Update(UPDATE_LAST_MONGO_OBJECT_ID)
	int updateLastMongoObjectId(AllineamentoScaricoDataset allineamento);	
	
}