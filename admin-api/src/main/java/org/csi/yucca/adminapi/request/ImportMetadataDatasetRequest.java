package org.csi.yucca.adminapi.request;

public class ImportMetadataDatasetRequest {

	private String tenantCode;
	private String dbType;
	private String jdbcHostname;
	private String jdbcDbname;
	private String jdbcUsername;
	private String jdbcPassword;

	public ImportMetadataDatasetRequest() {
		super();
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getJdbcHostname() {
		return jdbcHostname;
	}

	public void setJdbcHostname(String jdbcHostname) {
		this.jdbcHostname = jdbcHostname;
	}

	public String getJdbcDbname() {
		return jdbcDbname;
	}

	public void setJdbcDbname(String jdbcDbname) {
		this.jdbcDbname = jdbcDbname;
	}

	public String getJdbcUsername() {
		return jdbcUsername;
	}

	public void setJdbcUsername(String jdbcUsername) {
		this.jdbcUsername = jdbcUsername;
	}

	public String getJdbcPassword() {
		return jdbcPassword;
	}

	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

}
