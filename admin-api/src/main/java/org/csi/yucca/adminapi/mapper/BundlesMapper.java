package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.csi.yucca.adminapi.model.Bundles;
import org.csi.yucca.adminapi.util.Constants;

/**
 * @author gianfranco.stolfa
 *
 */
public interface BundlesMapper {
	
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
	
}
