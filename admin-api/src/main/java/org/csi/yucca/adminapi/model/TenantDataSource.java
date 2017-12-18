package org.csi.yucca.adminapi.model;

import java.sql.Timestamp;

public class TenantDataSource {

	private Integer idDataSource;
	private Integer datasourceversion;
	private Integer idTenant;
	private Integer isactive;
	private Integer ismanager;
	private Integer dataoptions;
	private Integer manageoptions;
	private Timestamp activationdate;
	private Timestamp deactivationdate;
	private Timestamp managerfrom;
	private Timestamp manageruntil;

	public Integer getIdDataSource() {
		return idDataSource;
	}

	public void setIdDataSource(Integer idDataSource) {
		this.idDataSource = idDataSource;
	}

	public Integer getDatasourceversion() {
		return datasourceversion;
	}

	public void setDatasourceversion(Integer datasourceversion) {
		this.datasourceversion = datasourceversion;
	}

	public Integer getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Integer idTenant) {
		this.idTenant = idTenant;
	}

	public Integer getIsactive() {
		return isactive;
	}

	public void setIsactive(Integer isactive) {
		this.isactive = isactive;
	}

	public Integer getIsmanager() {
		return ismanager;
	}

	public void setIsmanager(Integer ismanager) {
		this.ismanager = ismanager;
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

	public Timestamp getActivationdate() {
		return activationdate;
	}

	public void setActivationdate(Timestamp activationdate) {
		this.activationdate = activationdate;
	}

	public Timestamp getDeactivationdate() {
		return deactivationdate;
	}

	public void setDeactivationdate(Timestamp deactivationdate) {
		this.deactivationdate = deactivationdate;
	}

	public Timestamp getManagerfrom() {
		return managerfrom;
	}

	public void setManagerfrom(Timestamp managerfrom) {
		this.managerfrom = managerfrom;
	}

	public Timestamp getManageruntil() {
		return manageruntil;
	}

	public void setManageruntil(Timestamp manageruntil) {
		this.manageruntil = manageruntil;
	}

}
