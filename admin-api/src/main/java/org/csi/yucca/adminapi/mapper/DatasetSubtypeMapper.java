package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.DatasetSubtype;
import org.csi.yucca.adminapi.util.Constants;

/**
 * 
 * @author gianfranco.stolfa
 *
 */
public interface DatasetSubtypeMapper {
	
//	SELECT id_dataset_subtype, dataset_subtype, description, id_dataset_type
//	  FROM int_yucca.yucca_d_dataset_subtype where id_dataset_type = 1;
	
	String DATASET_SUBTYPE_TABLE = Constants.SCHEMA_DB + ".yucca_d_dataset_subtype";
	
	public static final String SELECT = 
			
			
			" SELECT id_dataset_subtype, dataset_subtype, description, id_dataset_type " +
			" FROM " + DATASET_SUBTYPE_TABLE +
			" WHERE id_dataset_type = #{datasetTypeCode} " +

			"<if test=\"sortList != null\">" +
				" ORDER BY " +
			
				" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
				
				"<if test=\"propName == 'idDatasetSubtype-'\">" +
			        " id_dataset_subtype desc" +
		        "</if>" +
		        "<if test=\"propName == 'idDatasetSubtype'\">" +
		            " id_dataset_subtype" +
	            "</if>" +			
				
				"<if test=\"propName == 'datasetSubtype-'\">" +
		           " dataset_subtype desc" +
	            "</if>" +
	            "<if test=\"propName == 'datasetSubtype'\">" +
	               " dataset_subtype" +
	            "</if>" +			
				
				"<if test=\"propName == 'description-'\">" +
		           " description desc" +
	            "</if>" +
	            "<if test=\"propName == 'description'\">" +
	               " description" +
	            "</if>" +
	               
				"<if test=\"propName == 'idDatasetType-'\">" +
		           " id_dataset_type desc" +
	            "</if>" +
	            "<if test=\"propName == 'idDatasetType'\">" +
	               " id_dataset_type" +
	            "</if>" +			
	            
	            "</foreach>" +
            "</if>";
	

	/*************************************************************************
	 * 
	 * 					select dataset subtype
	 * 
	 * ***********************************************************************/
//	SELECT id_dataset_subtype, dataset_subtype, description, id_dataset_type
//	  FROM int_yucca.yucca_d_dataset_subtype where id_dataset_type = 1;

	
	@Results({
        @Result(property = "idDatasetSubtype", column = "id_dataset_subtype"),
        @Result(property = "datasetSubtype", column = "dataset_subtype"),
        @Result(property = "description", column = "description"),
        @Result(property = "idDatasetType", column = "id_dataset_type")
      })
	@Select({"<script>",
				SELECT,
             "</script>"}) 
	List<DatasetSubtype> selectDatasetSubtype(@Param("datasetTypeCode") int datasetTypeCode, @Param("sortList") List<String> sortList);
	
}
