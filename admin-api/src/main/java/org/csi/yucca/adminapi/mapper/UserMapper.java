package org.csi.yucca.adminapi.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.csi.yucca.adminapi.model.User;
import org.csi.yucca.adminapi.util.Constants;

/**
 * @author gianfranco.stolfa
 *
 */
public interface UserMapper {
	
	String USER_TABLE = Constants.SCHEMA_DB + ".yucca_users";
	String R_TENANT_USERS_TABLE = Constants.SCHEMA_DB + ".yucca_r_tenant_users";

	/*************************************************************************
	 * 					
	 * 					INSERT R_TENANT_USER
	 * 
	 * ***********************************************************************/
	public static final String INSERT_TENANT_USERS = 
			"INSERT INTO " + R_TENANT_USERS_TABLE + "(id_tenant, id_user) VALUES (#{idTenant}, #{idUser})";
	@Insert(INSERT_TENANT_USERS)
	int insertTenantUser(@Param("idTenant") int idTenant, @Param("idUser") int idUser);
	
	/*************************************************************************
	 * 
	 * 					INSERT USER
	 * 
	 * ***********************************************************************/
	public static final String INSERT_USER = "INSERT INTO " + USER_TABLE
			+ "(username, id_organization, password) VALUES (#{username}, #{idOrganization}, #{password})";
	@Insert(INSERT_USER)                      
	@Options(useGeneratedKeys=true, keyProperty="idUser")
	int insertUser(User user);
	
	/*************************************************************************
	 * 
	 * 					select user by username
	 * 
	 * ***********************************************************************/
	public static final String SELECT_USER_BY_USERNAME = 
			"SELECT id_user, username, id_organization, password FROM " + USER_TABLE + " WHERE username=#{username}";
	@Results({
        @Result(property = "idUser", column = "id_user"),
        @Result(property = "idOrganization", column = "id_organization")
      })
	@Select(SELECT_USER_BY_USERNAME) 
	User selectUserByUserName(@Param("username") String username);
	
}
