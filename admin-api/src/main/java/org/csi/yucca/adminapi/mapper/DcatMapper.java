package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.csi.yucca.adminapi.model.Dcat;
import org.csi.yucca.adminapi.util.Constants;

/**
 * 
 * @author gianfranco.stolfa
 *
 */
public interface DcatMapper {
	
	String DCAT_TABLE = Constants.SCHEMA_DB + "yucca_dcat";

	/*************************************************************************
	 * 					INSERT DCAT
	 * ***********************************************************************/
	public static final String INSERT_DCAT = 
			"INSERT INTO " + DCAT_TABLE + "(dcatdataupdate, dcatnomeorg, dcatemailorg, dcatcreatorname, dcatcreatortype, dcatcreatorid, dcatrightsholdername, dcatrightsholdertype, dcatrightsholderid, dcatready) "
			+ "VALUES (#{dcatdataupdate}, #{dcatnomeorg}, #{dcatemailorg}, #{dcatcreatorname}, #{dcatcreatortype}, #{dcatcreatorid}, #{dcatrightsholdername}, #{dcatrightsholdertype}, #{dcatrightsholderid}, #{dcatready}) ";
	@Insert(INSERT_DCAT)
	@Options(useGeneratedKeys=true, keyProperty="idDcat")
	int insertDcat(Dcat dcat);
	
}
