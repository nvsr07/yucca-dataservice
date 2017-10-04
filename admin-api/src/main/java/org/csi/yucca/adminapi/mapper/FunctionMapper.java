package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.util.Constants;

/**
 * @author gianfranco.stolfa
 *
 */
public interface FunctionMapper {
	
	public static final String SELECT_RANDOM_PASSWORD_FUNCTION =  "select " + Constants.SCHEMA_DB + ".fnc_random_string(" + Constants.PASSWORD_LENGTH + ")"; 
	@Select(SELECT_RANDOM_PASSWORD_FUNCTION)                      
	String selectRandomPassword();

	public static final String SELECT_RANDOM_STRING_FUNCTION =  "select " + Constants.SCHEMA_DB + ".fnc_random_string(#{length})"; 
	@Select(SELECT_RANDOM_STRING_FUNCTION)                      
	String selectRandomString(int length);
	
}
