package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.util.Errors;

public class OrganizationResponse extends Response{
	
	private Integer idOrganization;
	private String organizationcode;
	private String description;
	
	public OrganizationResponse(Organization organization) {
		super();
		this.idOrganization = organization.getIdOrganization();
		this.organizationcode = organization.getOrganizationcode();
		this.description = organization.getDescription();
	}
	
	public OrganizationResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrganizationResponse(Errors errors, String arg) {
		super(errors, arg);
		// TODO Auto-generated constructor stub
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
