package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.TenantManagement;

public class ManagementOrganizationResponse extends Response{
	
	private Integer idOrganization;
	private String organizationcode;
	private String description;
	
	public ManagementOrganizationResponse(TenantManagement tenantManagement) {
		super();
		this.idOrganization = tenantManagement.getIdOrganization();
		this.organizationcode = tenantManagement.getOrganizationcode();
		this.description = tenantManagement.getOrganizationdescription();
	}

	public Integer getIdOrganization() {
		return idOrganization;
	}

	public void setIdOrganization(Integer idOrganization) {
		this.idOrganization = idOrganization;
	}

	public String getOrganizationcode() {
		return organizationcode;
	}

	public void setOrganizationcode(String organizationcode) {
		this.organizationcode = organizationcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
