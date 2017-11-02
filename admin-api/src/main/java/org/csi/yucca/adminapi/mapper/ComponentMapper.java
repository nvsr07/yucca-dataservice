package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
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
	 * 					INSERT DATA SOURCE
	 * ***********************************************************************/
	public static final String INSERT_COMPONENT = 
	" INSERT INTO " + COMPONENT_TABLE + "( name, alias, inorder, tolerance, since_version, "
	+ "id_phenomenon, id_data_type, id_measure_unit, id_data_source, datasourceversion, iskey, "
	+ "sourcecolumn, sourcecolumnname, required)"
	+ "VALUES (#{name}, #{alias}, #{inorder}, #{tolerance}, #{sinceVersion}, #{idPhenomenon}, "
	+ "#{idDataType}, #{idMeasureUnit}, #{idDataSource}, #{datasourceversion}, #{iskey}, #{sourcecolumn}, "
	+ "#{sourcecolumnname}, #{required})";

	@Insert(INSERT_COMPONENT)
	@Options(useGeneratedKeys=true, keyProperty="idComponent")
	int insertComponent(Component component);
	
}
