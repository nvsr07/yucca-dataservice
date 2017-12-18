package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.ITenant;
import org.csi.yucca.adminapi.model.SharingTenantsJson;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.util.Errors;

import com.fasterxml.jackson.annotation.JsonInclude;

public class TenantResponse extends Response{

	private Integer idTenant;
	
	private String tenantcode;
	
	private String description;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String name;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer dataoptions;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer manageoptions;
	
	
	public TenantResponse() {
		super();
	}
	
	public TenantResponse(Errors errors, String arg) {
		super(errors, arg);
	}

	public TenantResponse(SharingTenantsJson sharingTenantsJson ) {
		if(sharingTenantsJson != null){
			this.idTenant = sharingTenantsJson.getId_tenant();
			this.tenantcode = sharingTenantsJson.getTenantcode();
			this.description = sharingTenantsJson.getDescription();			
			this.name = sharingTenantsJson.getName();
			this.dataoptions = sharingTenantsJson.getDataoptions();
			this.manageoptions = sharingTenantsJson.getManageoptions();
		}
	}
	
	public TenantResponse(Dataset dataset ) {
		if(dataset != null){
			this.idTenant = dataset.getIdTenant();
			this.tenantcode = dataset.getTenantCode();
			this.description = dataset.getTenantDescription();			
			this.name = dataset.getTenantName();
		}
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
