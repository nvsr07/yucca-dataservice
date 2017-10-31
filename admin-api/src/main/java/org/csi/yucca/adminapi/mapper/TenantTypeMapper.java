package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.model.TenantsType;
import org.csi.yucca.adminapi.util.Constants;


public interface TenantTypeMapper {

	String TENANTTYPE_TABLE = Constants.SCHEMA_DB + "yucca_d_tenant_type";
	
		
	
	public static final String SELECT_STAR_FROM_TENANTTYPE_TABLE = 
			" SELECT id_tenant_type,tenanttypecode,description  FROM " + TENANTTYPE_TABLE ;
	
	
	/*************************************************************************
	 * 
	 * 					select TENANT TYPES
	 * 
	 * ***********************************************************************/
	@Results({
        @Result(property = "idTenantType", column = "idTenantType"),
        @Result(property = "tenanttypecode", column = "tenanttypecode"),
        @Result(property = "description", column = "description")
      })
	@Select (SELECT_STAR_FROM_TENANTTYPE_TABLE)
	List<TenantsType>  selectTenantTypes() ;
}
