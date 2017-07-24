package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.Tag;
import org.csi.yucca.adminapi.util.Constants;

/**
 * @author gianfranco.stolfa
 *
 */
public interface TagMapper {
	
	String TAG_TABLE = Constants.SCHEMA_DB + ".yucca_d_tag";
	
	public static final String SELECT =
			" FROM " + TAG_TABLE + " where id_ecosystem= #{ecosystemCode} " + 
	
			"<if test=\"sortList != null\">" +
				" ORDER BY " +
			
				" <foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"sortList\">" +
				
				"<if test=\"propName == 'idTag-'\">" +
			        " id_tag desc" +
		        "</if>" +
		        "<if test=\"propName == 'idTag'\">" +
		            " id_tag" +
	            "</if>" +			
				
				"<if test=\"propName == 'tagcode-'\">" +
		           " tagcode desc" +
	            "</if>" +
	            "<if test=\"propName == 'tagcode'\">" +
	               " tagcode" +
	            "</if>" +			
				
				"<if test=\"propName == 'langit-'\">" +
		           " langit desc" +
	            "</if>" +
	            "<if test=\"propName == 'langit'\">" +
	               " langit" +
	            "</if>" +			
				
				"<if test=\"propName == 'langen-'\">" +
		           " langen desc" +
	            "</if>" +
	            "<if test=\"propName == 'langen'\">" +
	               " langen" +
	            "</if>" +			
				
				"<if test=\"propName == 'idEcosystem-'\">" +
		           " id_ecosystem desc" +
	            "</if>" +
	            "<if test=\"propName == 'idEcosystem'\">" +
	               " id_ecosystem" +
	            "</if>" +			
	            
	            "</foreach>" +
            "</if>";

	
	/*************************************************************************
	 * 
	 * 					select all licenses
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idTag", column = "id_tag"),
        @Result(property = "tagcode", column = "tagcode"),
        @Result(property = "langit", column = "langit"),
        @Result(property = "langen", column = "langen"),
        @Result(property = "idEcosystem", column = "id_ecosystem")
	})
	@Select({"<script>",
				" SELECT id_tag, tagcode, langit, langen, id_ecosystem ",
				SELECT,
             "</script>"}) 
	List<Tag> selectTagAllLanguage(@Param("sortList") List<String> sortList, @Param("ecosystemCode") Integer ecosystemCode);
	
	
	@Results({
        @Result(property = "idTag", column = "id_tag"),
        @Result(property = "tagcode", column = "tagcode"),
        @Result(property = "langit", column = "langit"),
        @Result(property = "idEcosystem", column = "id_ecosystem")
	})
	@Select({"<script>",
				" SELECT id_tag, tagcode, langit, id_ecosystem ",
				SELECT,
             "</script>"}) 
	List<Tag> selectTagITLanguage(@Param("sortList") List<String> sortList, @Param("ecosystemCode") Integer ecosystemCode);
	

	@Results({
        @Result(property = "idTag", column = "id_tag"),
        @Result(property = "tagcode", column = "tagcode"),
        @Result(property = "langen", column = "langen"),
        @Result(property = "idEcosystem", column = "id_ecosystem")
	})
	@Select({"<script>",
				" SELECT id_tag, tagcode, langen, id_ecosystem ",
				SELECT,
             "</script>"}) 
	List<Tag> selectTagENLanguage(@Param("sortList") List<String> sortList, @Param("ecosystemCode") Integer ecosystemCode);
	

	
}
