package org.csi.yucca.adminapi.model;

public class Organization {

	private Integer idOrganization;
	private String organizationcode;
	private String description;
	
	public Organization(Integer idOrganization, String organizationcode, String description) {
		super();
		this.idOrganization = idOrganization;
		this.organizationcode = organizationcode;
		this.description = description;
	}
	
	public Organization() {
		super();
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
