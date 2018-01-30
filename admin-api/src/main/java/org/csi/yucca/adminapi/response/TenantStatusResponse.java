package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.DettaglioTenantBackoffice;
import org.csi.yucca.adminapi.model.join.TenantManagement;

public class TenantStatusResponse extends Response{

	private Integer idTenantStatus;

	private String tenantstatuscode;

	private String description;
	
	public TenantStatusResponse(TenantManagement tenantManagement) {
		super();
		this.idTenantStatus = tenantManagement.getIdTenantStatus();
		this.tenantstatuscode = tenantManagement.getTenantstatuscode();
		this.description = tenantManagement.getTenantstatusdescription();
	}

	public TenantStatusResponse(DettaglioTenantBackoffice dettaglioTenant) {
		super();
		this.idTenantStatus = dettaglioTenant.getIdTenantStatus();
		this.tenantstatuscode = dettaglioTenant.getTenantstatuscode();
		this.description = dettaglioTenant.getTenantstatusdescription();
	}

	
	
	
	public Integer getIdTenantStatus() {
		return idTenantStatus;
	}

	public void setIdTenantStatus(Integer idTenantStatus) {
		this.idTenantStatus = idTenantStatus;
	}

	public String getTenantstatuscode() {
		return tenantstatuscode;
	}

	public void setTenantstatuscode(String tenantstatuscode) {
		this.tenantstatuscode = tenantstatuscode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
