package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.TenantManagement;
import org.csi.yucca.adminapi.util.Errors;

public class TenantTypeResponse extends Response{

	private Integer idTenantType;

	private String tenanttypecode;

	private String description;
	
	public TenantTypeResponse(TenantManagement tenantManagement) {
		super();
		this.idTenantType = tenantManagement.getIdTenantType();
		this.tenanttypecode = tenantManagement.getTenanttypecode();
		this.description = tenantManagement.getTenanttypedescription();
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
