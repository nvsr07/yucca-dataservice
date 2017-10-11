package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.TenantManagement;

public class ShareTypeResponse extends Response{

	private Integer idShareType;

	private String description;
	
	public ShareTypeResponse(TenantManagement tenantManagement) {
		super();
		this.idShareType = tenantManagement.getIdShareType();
		this.description = tenantManagement.getSharetypedescription();
	}

	public Integer getIdShareType() {
		return idShareType;
	}

	public void setIdShareType(Integer idShareType) {
		this.idShareType = idShareType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
