package org.csi.yucca.adminapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.Bundles;
import org.csi.yucca.adminapi.util.Constants;

/**
 * @author gianfranco.stolfa
 *
 */
public interface BundlesMapper {
	
	String R_TENANT_BUNDLES_TABLE = Constants.SCHEMA_DB + ".yucca_r_tenant_bundles";
	String BUNDLES_TABLE = Constants.SCHEMA_DB + ".yucca_bundles";
	
	/*************************************************************************
	 * 
	 * 					INSERT BUNDLES
	 * 
	 * ***********************************************************************/
	public static final String INSERT_BUNDLES = "INSERT INTO " + BUNDLES_TABLE + " (maxdatasetnum, maxstreamsnum, hasstage, max_odata_resultperpage, zeppelin) VALUES (#{maxdatasetnum}, #{maxstreamsnum}, #{hasstage}, #{maxOdataResultperpage}, #{zeppelin})";
	@Insert(INSERT_BUNDLES)                      
	@Options(useGeneratedKeys=true, keyProperty="idBundles")
	int insertBundles(Bundles bundles);

	
	/*************************************************************************
	 * 
	 * 					DELETE BUNDLES
	 * 
	 * ***********************************************************************/
	public static final String DELETE_BUNDLES = "DELETE FROM " + BUNDLES_TABLE + " WHERE id_bundles = #{idBundles}";
	@Delete(DELETE_BUNDLES)
	int deleteBundles(int idBundles);	

	
	/*************************************************************************
	 * 
	 * 					SELECT BUNDLES BY ID_TENANT
	 * 
	 * ***********************************************************************/
	public static final String SELECT_BUNDLES_BY_TENANT =
	" SELECT BUNDLES.id_bundles, maxdatasetnum, maxstreamsnum, hasstage, max_odata_resultperpage, " + 
	" zeppelin FROM " + BUNDLES_TABLE
	+ " BUNDLES, " + R_TENANT_BUNDLES_TABLE + " TENANT_BUNDLES " +
	" WHERE TENANT_BUNDLES.id_tenant = #{idTenant} AND BUNDLES.id_bundles = TENANT_BUNDLES.id_bundles ";
	@Results({
        @Result(property = "idBundles", column = "id_bundles"),
        @Result(property = "maxOdataResultperpage", column = "max_odata_resultperpage")
      })
	@Select(SELECT_BUNDLES_BY_TENANT) 
	Bundles selectBundlesByTenant(@Param("idTenant") Integer idTenant);
	
	
	/*************************************************************************
	 * 
	 * 					SELECT BUNDLES BY ORGANIZATION
	 * 
	 * ***********************************************************************/
	public static final String SELECT_BUNDLES_BY_ORGANIZATION =
	" SELECT BUNDLES.id_bundles, maxdatasetnum, maxstreamsnum, hasstage, max_odata_resultperpage, zeppelin " +
	" FROM " + BUNDLES_TABLE + " BUNDLES, " + TenantMapper.TENANT_TABLE + " TENANT, " +  TenantMapper.R_TENANT_BUNDLES_TABLE  + " TENANT_BUNDLES " +
	" WHERE TENANT.id_organization = #{idOrganization} AND " +
	" TENANT.id_tenant = TENANT_BUNDLES.id_tenant AND " +
	" BUNDLES.id_bundles = TENANT_BUNDLES.id_bundles ";
	@Results({
        @Result(property = "idBundles", column = "id_bundles"),
        @Result(property = "maxOdataResultperpage", column = "max_odata_resultperpage")
      })
	@Select(SELECT_BUNDLES_BY_ORGANIZATION) 
	List<Bundles> selectBundlesByOrganization(@Param("idOrganization") Integer idOrganization);
	
}
