package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.csi.yucca.adminapi.model.Component;
import org.csi.yucca.adminapi.util.Constants;

/**
 * 
 * @author gianfranco.stolfa
 *
 */
public interface ComponentMapper {
	
	public static final String COMPONENT_TABLE = Constants.SCHEMA_DB + "yucca_component";

	/*************************************************************************
	 * 					CLONE COMPONENT
	 * ***********************************************************************/
	public static final String CLONE_COMPONENT = 
			" INSERT INTO " + COMPONENT_TABLE + "( " +
				" name, alias, inorder, tolerance, since_version, " + 
				" id_phenomenon, id_data_type, id_measure_unit, id_data_source, " + 
				" datasourceversion, iskey, sourcecolumn, sourcecolumnname, required, foreignkey) " +
			" SELECT name, alias, inorder, tolerance, since_version, " + 
				" id_phenomenon, id_data_type, id_measure_unit, id_data_source, " + 
				" #{newDataSourceVersion}, iskey, sourcecolumn, sourcecolumnname, required, foreignkey " +
			" FROM " + COMPONENT_TABLE + " WHERE id_component in ( " +
				"<foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"listIdComponent\">" +
					"#{propName}" +
				"</foreach>" +
					 " ) ";
	@Insert({"<script>",CLONE_COMPONENT, "</script>"})
	int cloneComponent( @Param("newDataSourceVersion") Integer newDataSourceVersion, 
						@Param("listIdComponent") List<Integer> listIdComponent );

	/*************************************************************************
	 * 					UPDATE COMPONENT
	 * ***********************************************************************/	
	public static final String UPDATE_CLONED_COMPONENT = 
		"UPDATE " + COMPONENT_TABLE + 
		" SET alias=#{alias}, inorder=#{inorder}, id_measure_unit=#{idMeasureUnit}, foreignkey=#{foreignkey} " + 
		" WHERE name=#{name} and datasourceversion=#{dataSourceVersion} and id_data_source=#{idDataSource}";
	@Update(UPDATE_CLONED_COMPONENT)
	int updateClonedComponent(
			@Param("name") String name,
			@Param("alias") String alias,
			@Param("inorder") Integer inorder,
			@Param("idMeasureUnit") Integer idMeasureUnit,
			@Param("dataSourceVersion") Integer dataSourceVersion,
			@Param("idDataSource") Integer idDataSource,
			@Param("foreignkey") String foreignkey);
	
	/*************************************************************************
	 * 					UPDATE COMPONENT
	 * ***********************************************************************/	
	public static final String UPDATE_COMPONENT = 
		"UPDATE " + COMPONENT_TABLE + 
		" SET alias=#{alias}, inorder=#{inorder}, tolerance=#{tolerance},id_phenomenon=#{idPhenomenon},id_measure_unit=#{idMeasureUnit}, foreignkey=#{foreignkey} " + 
		" WHERE id_component=#{idComponent}";
	@Update(UPDATE_COMPONENT)
	int updateComponent(Component component);

	
	/*************************************************************************
	 * 					DELETE COMPONENTS BY ID_DATA_SOURCE AND VERSION
	 * ***********************************************************************/
	public static final String DELETE_COMPONENT_BY_DATA_SOURCE_AND_VERSION =
	" DELETE FROM " + COMPONENT_TABLE + " WHERE id_data_source = #{idDataSource} and datasourceversion = #{dataSourceVersion} " +
	" AND id_component not in (" +
		"<foreach item=\"propName\" separator=\",\" index=\"index\" collection=\"alreadyPresentIdList\">" +
			"#{propName}" +
		"</foreach>" +
	 ") " ; 
	@Delete({"<script>",DELETE_COMPONENT_BY_DATA_SOURCE_AND_VERSION, "</script>"})
	int deleteComponents(@Param("idDataSource") Integer idDataSource, @Param("dataSourceVersion") Integer dataSourceVersion,
			@Param("alreadyPresentIdList") List<Integer> alreadyPresentIdList);
	
	
	/*************************************************************************
	 * 					SELECT COMPONENT BY DATA SOURCE AND VERSION
	 * ***********************************************************************/
	public static final String SELECT_COMPONENT_BY_DATA_SOURCE_AND_VERSION =
			"SELECT id_component, name, alias, inorder, tolerance, since_version, "
			+ "id_phenomenon, id_data_type, id_measure_unit, id_data_source, "
			+ "datasourceversion, iskey, sourcecolumn, sourcecolumnname, required, foreignkey "
			+ "from " + COMPONENT_TABLE + "  where id_data_source  = #{idDataSource} and "
					+ "datasourceversion =#{dataSourceVersion}";
	@Results({
		@Result(property = "idComponent",   column = "id_component"),
		@Result(property = "sinceVersion",  column = "since_version"),
		@Result(property = "idPhenomenon",  column = "id_phenomenon"),
		@Result(property = "idDataType",    column = "id_data_type"),
		@Result(property = "idMeasureUnit", column = "id_measure_unit"),
		@Result(property = "idDataSource",  column = "id_data_source")
      })	
	@Select(SELECT_COMPONENT_BY_DATA_SOURCE_AND_VERSION) 
	List<Component> selectComponentByDataSourceAndVersion( 
			@Param("idDataSource") Integer idDataSource, @Param("dataSourceVersion") Integer dataSourceVersion);	
	
	/*************************************************************************
	 * 					INSERT COMPONENT
	 * ***********************************************************************/
	public static final String INSERT_COMPONENT = 
	" INSERT INTO " + COMPONENT_TABLE + "( name, alias, inorder, tolerance, since_version, "
	+ "id_phenomenon, id_data_type, id_measure_unit, id_data_source, datasourceversion, iskey, "
	+ "sourcecolumn, sourcecolumnname, required, foreignkey)"
	+ "VALUES (#{name}, #{alias}, #{inorder}, #{tolerance}, #{sinceVersion}, #{idPhenomenon}, "
	+ "#{idDataType}, #{idMeasureUnit}, #{idDataSource}, #{datasourceversion}, #{iskey}, #{sourcecolumn}, "
	+ "#{sourcecolumnname}, #{required}, #{foreignkey})";
	@Insert(INSERT_COMPONENT)
	@Options(useGeneratedKeys=true, keyProperty="idComponent")
	int insertComponent(Component component);
	
}
