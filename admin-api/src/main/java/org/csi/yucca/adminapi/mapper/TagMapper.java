package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
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
	
	String TAG_TABLE = Constants.SCHEMA_DB + "yucca_d_tag";
	
	public static final String FROM =
		" FROM " + TAG_TABLE + " TAG, " + EcosystemMapper.ECOSYSTEM_TABLE + " ECO " +
		" where TAG.id_ecosystem = ECO.id_ecosystem AND " +
		" ECO.ecosystemcode = #{ecosystemCode} ";
	
	public static final String SELECT =
			FROM + 
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
	 * 					DELETE TAG
	 * 
	 * ***********************************************************************/
	public static final String DELETE_TAG = "DELETE FROM " + TAG_TABLE + " WHERE id_tag=#{idTag}";
	@Delete(DELETE_TAG)
	int deleteTag(int idTag);	
	
	/*************************************************************************
	 * 
	 * 					UPDATE TAG
	 * 
	 * ***********************************************************************/
	public static final String UPDATE_TAG = 
			"<script>" +
			"UPDATE " + TAG_TABLE + 
			" SET tagcode=#{tagcode}, langit=#{langit}, langen=#{langen} " +
			"<if test=\"idEcosystem != null \">" +
				", id_ecosystem=#{idEcosystem}" +
	        "</if>" + " WHERE id_tag=#{idTag}" +
	        "</script>" ;
	@Delete(UPDATE_TAG)
	int updateTag(Tag tag);	

	
	/*************************************************************************
	 * 
	 * 					INSERT TAG
	 * 
	 * ***********************************************************************/
	public static final String INSERT_TAG = "INSERT INTO " + TAG_TABLE
			+ "(tagcode, langit, langen, id_ecosystem) VALUES (#{tagcode}, #{langit}, #{langen}, #{idEcosystem})";
	@Insert(INSERT_TAG)
	@Options(useGeneratedKeys=true, keyProperty="idTag")
	int insertTag(Tag tag);
	
	
	/*************************************************************************
	 * 
	 * 					select tag by id
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idTag", column = "id_tag"),
        @Result(property = "tagcode", column = "tagcode"),
        @Result(property = "langit", column = "langit"),
        @Result(property = "langen", column = "langen"),
        @Result(property = "idEcosystem", column = "id_ecosystem")
	})
	@Select({" SELECT id_tag, tagcode, langit, langen, id_ecosystem FROM " + TAG_TABLE + " WHERE id_tag=#{idTag}"}) 
	Tag selectTagById(@Param("idTag") Integer idTag);
	
	
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
				" SELECT id_tag, tagcode, langit, langen, TAG.id_ecosystem ",
				SELECT,
             "</script>"}) 
	List<Tag> selectTagAllLanguage(@Param("sortList") List<String> sortList, @Param("ecosystemCode") String ecosystemCode);
	
	
	@Results({
        @Result(property = "idTag", column = "id_tag"),
        @Result(property = "tagcode", column = "tagcode"),
        @Result(property = "langit", column = "langit"),
        @Result(property = "idEcosystem", column = "id_ecosystem")
	})
	@Select({"<script>",
				" SELECT id_tag, tagcode, langit, TAG.id_ecosystem ",
				SELECT,
             "</script>"}) 
	List<Tag> selectTagITLanguage(@Param("sortList") List<String> sortList, @Param("ecosystemCode") String ecosystemCode);
	

	@Results({
        @Result(property = "idTag", column = "id_tag"),
        @Result(property = "tagcode", column = "tagcode"),
        @Result(property = "langen", column = "langen"),
        @Result(property = "idEcosystem", column = "id_ecosystem")
	})
	@Select({"<script>",
				" SELECT id_tag, tagcode, langen, TAG.id_ecosystem ",
				SELECT,
             "</script>"}) 
	List<Tag> selectTagENLanguage(@Param("sortList") List<String> sortList, @Param("ecosystemCode") String ecosystemCode);
	

	
}
