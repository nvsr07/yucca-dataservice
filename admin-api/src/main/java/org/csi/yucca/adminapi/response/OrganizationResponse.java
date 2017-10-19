package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;
import org.csi.yucca.adminapi.model.join.DettaglioTenantBackoffice;
import org.csi.yucca.adminapi.model.join.TenantManagement;

public class OrganizationResponse extends Response{
	
	private Integer idOrganization;
	private String organizationcode;
	private String description;
	
	public OrganizationResponse(TenantManagement tenantManagement) {
		super();
		this.idOrganization = tenantManagement.getIdOrganization();
		this.organizationcode = tenantManagement.getOrganizationcode();
		this.description = tenantManagement.getOrganizationdescription();
	}

	public OrganizationResponse(DettaglioSmartobject smartobject) {
		super();
		this.idOrganization = smartobject.getIdOrganization();
		this.organizationcode = smartobject.getOrganizationcode();
		this.description = smartobject.getDescriptionOrganization();
	}

	public OrganizationResponse(DettaglioTenantBackoffice dettaglioTenantBackoffice) {
		super();
		this.idOrganization = dettaglioTenantBackoffice.getIdOrganization();
		this.organizationcode = dettaglioTenantBackoffice.getOrganizationcode();
		this.description = dettaglioTenantBackoffice.getOrganizationdescription();
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
