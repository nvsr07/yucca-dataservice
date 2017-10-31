package org.csi.yucca.adminapi.model;

public class TenantsType {

	private Integer idTenantType;
	private String tenanttypecode;
	private String description;
	
	
	public TenantsType(Integer idTenantType, String tenanttypecode, String description) {
		super();
		this.idTenantType = idTenantType;
		this.tenanttypecode = tenanttypecode;
		this.description = description;
	}
	
	public TenantsType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getIdTenantType() {
		return idTenantType;
	}

	public void setIdTenantType(Integer idTenantType) {
		this.idTenantType = idTenantType;
	}

	public String getTenanttypecode() {
		return tenanttypecode;
	}

	public void setTenanttypecode(String tenanttypecode) {
		this.tenanttypecode = tenanttypecode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
