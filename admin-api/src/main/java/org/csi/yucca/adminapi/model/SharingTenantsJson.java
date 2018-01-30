package org.csi.yucca.adminapi.model;

public class SharingTenantsJson {

	private Integer id_tenant;
	private String tenantcode;
	private String name;
	private String description;
	private Integer dataoptions;
	private Integer manageoptions;

	public Integer getId_tenant() {
		return id_tenant;
	}

	public void setId_tenant(Integer id_tenant) {
		this.id_tenant = id_tenant;
	}

	public String getTenantcode() {
		return tenantcode;
	}

	public void setTenantcode(String tenantcode) {
		this.tenantcode = tenantcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDataoptions() {
		return dataoptions;
	}

	public void setDataoptions(Integer dataoptions) {
		this.dataoptions = dataoptions;
	}

	public Integer getManageoptions() {
		return manageoptions;
	}

	public void setManageoptions(Integer manageoptions) {
		this.manageoptions = manageoptions;
	}

}
