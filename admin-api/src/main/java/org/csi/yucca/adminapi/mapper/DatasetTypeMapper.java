package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.DatasetType;
import org.csi.yucca.adminapi.util.Constants;

public interface DatasetTypeMapper {
	
	String DATASET_TYPE_TABLE = Constants.SCHEMA_DB + "yucca_d_dataset_type";
	
	public static final String SELECT = 
			
			" SELECT id_dataset_type, dataset_type, description FROM " + DATASET_TYPE_TABLE + " " +

			"<if test=\"sortList != null\">" +
				" ORDER BY " +
			
				" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
				
				"<if test=\"propName == 'idDatasetType-'\">" +
			        " id_dataset_type desc" +
		        "</if>" +
		        "<if test=\"propName == 'idDatasetType'\">" +
		            " id_dataset_type" +
	            "</if>" +			
				
				"<if test=\"propName == 'datasetType-'\">" +
		           " dataset_type desc" +
	            "</if>" +
	            "<if test=\"propName == 'datasetType'\">" +
	               " dataset_type" +
	            "</if>" +			
				
				"<if test=\"propName == 'description-'\">" +
		           " description desc" +
	            "</if>" +
	            "<if test=\"propName == 'description'\">" +
	               " description" +
	            "</if>" +			
	            "</foreach>" +
            "</if>";
	

	/*************************************************************************
	 * 
	 * 					select dataset TYPE
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idDatasetType", column = "id_dataset_type"),
        @Result(property = "datasetType", column = "dataset_type"),
        @Result(property = "description", column = "description")
      })
	@Select({"<script>",
				SELECT,
             "</script>"}) 
	List<DatasetType> selectDatasetType(@Param("sortList") List<String> sortList);	
	
}
