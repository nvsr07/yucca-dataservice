package org.csi.yucca.adminapi.request;

import java.util.List;

public class OrganizationRequest {
	
	private List<Integer> ecosystemCodeList;
	private Integer idOrganization;
	private String organizationcode;
	private String description;
	
	public List<Integer> getEcosystemCodeList() {
		return ecosystemCodeList;
	}
	public void setEcosystemCodeList(List<Integer> ecosystemCodeList) {
		this.ecosystemCodeList = ecosystemCodeList;
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
