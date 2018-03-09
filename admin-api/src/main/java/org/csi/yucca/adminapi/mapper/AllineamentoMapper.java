package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.csi.yucca.adminapi.model.Allineamento;
import org.csi.yucca.adminapi.util.Constants;

/**
 * 
 * @author gianfranco.stolfa
 *
 */
public interface AllineamentoMapper {

	public static final String ALLINEAMENTO_TABLE = Constants.SCHEMA_DB + "yucca_allineamento2";
	
	/*************************************************************************
	 * 
	 * 					INSERT ALLINEAMENTO
	 * 
	 * ***********************************************************************/
	public static final String INSERT_ALLINEAMENTO = 
			"INSERT INTO " + ALLINEAMENTO_TABLE
			+ " (id_organization, locked, lastobjectid) VALUES (#{idOrganization}, #{locked}, #{lastobjectid})";
	@Insert(INSERT_ALLINEAMENTO)                      
	int insertAllineamento(Allineamento allineamento);
	
	/*************************************************************************
	 * 
	 * 					SELECT ALLINEAMENTO BY ID ORGANIZATION
	 * 
	 ***********************************************************************/
	public static final String SELECT_ALLINEAMNETO_BY_ORGANIZATION_CODE = "SELECT id_organization, locked, lastobjectid FROM "
			+ ALLINEAMENTO_TABLE + " where id_organization = #{idOrganization}";

	@Results({ @Result(property = "idOrganization", column = "id_organization") })
	@Select(SELECT_ALLINEAMNETO_BY_ORGANIZATION_CODE)
	Allineamento selectAllineamentoByIdOrganization(@Param("idOrganization") Integer idOrganization);

	
	/*************************************************************************
	 * 
	 * 					UPDATE ALLINEAMENTO
	 * 
	 * ***********************************************************************/
	public static final String UPDATE_ALLINEAMENTO = 
			"UPDATE " + ALLINEAMENTO_TABLE + 
			" SET locked=#{locked}, lastobjectid=#{lastobjectid} WHERE id_organization=#{idOrganization}";
	@Update(UPDATE_ALLINEAMENTO)
	int updateAllineamento(Allineamento allineamento);	
	
	
	
	
	

}