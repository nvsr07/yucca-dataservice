package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.Ecosystem;
import org.csi.yucca.adminapi.model.join.DettaglioTenantBackoffice;
import org.csi.yucca.adminapi.model.join.TenantManagement;
import org.csi.yucca.adminapi.util.Errors;

public class EcosystemResponse extends Response{
	private Integer idEcosystem;
	private String ecosystemcode;
	private String description;

	public EcosystemResponse(TenantManagement tenantManagement) {
		super();
		this.idEcosystem = tenantManagement.getIdEcosystem();
		this.ecosystemcode = tenantManagement.getEcosystemcode();
		this.description = tenantManagement.getEcosystemdescription();
	}

	public EcosystemResponse(DettaglioTenantBackoffice dettaglioTenantBackoffice) {
		super();
		this.idEcosystem = dettaglioTenantBackoffice.getIdEcosystem();
		this.ecosystemcode = dettaglioTenantBackoffice.getEcosystemcode();
		this.description = dettaglioTenantBackoffice.getEcosystemdescription();
	}
	
	public EcosystemResponse(Ecosystem ecosystem) {
		super();
		this.idEcosystem = ecosystem.getIdEcosystem();
		this.ecosystemcode = ecosystem.getEcosystemcode();
		this.description = ecosystem.getDescription();
	}
	
	public EcosystemResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public EcosystemResponse(Errors errors, String arg) {
		super(errors, arg);
		// TODO Auto-generated constructor stub
	}
	public Integer getIdEcosystem() {
		return idEcosystem;
	}
	public void setIdEcosystem(Integer idEcosystem) {
		this.idEcosystem = idEcosystem;
	}
	public String getEcosystemcode() {
		return ecosystemcode;
	}
	public void setEcosystemcode(String ecosystemcode) {
		this.ecosystemcode = ecosystemcode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
