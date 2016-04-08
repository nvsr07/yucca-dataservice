package org.csi.yucca.dataservice.metadataapi.model.store.output.doc;

public class ConfigData {

	private Long idTenant;
	private String tenantCode;
	private String type;
	private String subtype;
	private String entityNameSpace;
	private Integer current;

	public ConfigData() {

	}

	public Long getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Long idTenant) {
		this.idTenant = idTenant;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getEntityNameSpace() {
		return entityNameSpace;
	}

	public void setEntityNameSpace(String entityNameSpace) {
		this.entityNameSpace = entityNameSpace;
	}

	public Integer getCurrent() {
		return current;
	}

	public void setCurrent(Integer current) {
		this.current = current;
	}

}
