package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.Mailtemplates;
import org.csi.yucca.adminapi.util.Constants;

/**
 * @author gianfranco.stolfa
 *
 */
public interface MailTemplatesMapper {
	
	String MAILTEMPLATES_TABLE = Constants.SCHEMA_DB + "yucca_d_mailtemplates";
	
	/*************************************************************************
	 * 
	 * 					SELECT MAILTEMPLATES BY ID_TENANT_TYPE
	 * 
	 * ***********************************************************************/
	public static final String SELECT_MAILTEMPLATES_BY_ID_TENANT_TYPE =
			" SELECT id_tenant_type, mailbody, mailobject " + 
			" FROM int_yucca.yucca_d_mailtemplates " +
			" WHERE id_tenant_type = #{idTenantType}";
	@Results({
        @Result(property = "idTenantType", column = "id_tenant_type")
      })
	@Select(SELECT_MAILTEMPLATES_BY_ID_TENANT_TYPE) 
	Mailtemplates selectBundlesByIdTenantType(@Param("idTenantType") Integer idTenantType);

}
