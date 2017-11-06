package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.ITenant;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.util.Errors;

public class TenantResponse extends Response{

	private Integer idTenant;
	private String tenantcode;
	private String description;
	
	public TenantResponse() {
		super();
	}
	
	public TenantResponse(Errors errors, String arg) {
		super(errors, arg);
	}

	public TenantResponse(ITenant iTenantImpl ) {
		if(iTenantImpl != null){
			this.idTenant = iTenantImpl.getIdTenant();
			this.tenantcode = iTenantImpl.getTenantCode();
			this.description = iTenantImpl.getTenantDescription();			
		}
	}
	
	public TenantResponse(Tenant tenant) {
		
		this.idTenant = tenant.getIdTenant();
		this.tenantcode = tenant.getTenantcode();
		this.description = tenant.getDescription();
	}

	public Integer getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Integer idTenant) {
		this.idTenant = idTenant;
	}

	public String getTenantcode() {
		return tenantcode;
	}

	public void setTenantcode(String tenantcode) {
		this.tenantcode = tenantcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
