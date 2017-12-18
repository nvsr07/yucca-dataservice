package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.DettaglioTenantBackoffice;
import org.csi.yucca.adminapi.model.join.TenantManagement;

public class BundlesResponse extends Response{

	private Integer idBundles;
	private Integer maxdatasetnum;
	private Integer maxstreamsnum;
	private String hasstage;
	private Integer maxOdataResultperpage;
	private String zeppelin;

	public BundlesResponse(TenantManagement tenantManagement) {
		super();
		this.idBundles = tenantManagement.getIdBundles();
		this.maxdatasetnum = tenantManagement.getMaxdatasetnum();
		this.maxstreamsnum = tenantManagement.getMaxstreamsnum();
		this.hasstage = tenantManagement.getHasstage();
		this.maxOdataResultperpage = tenantManagement.getMaxOdataResultperpage();
		this.zeppelin = tenantManagement.getZeppelin();
	}

	public BundlesResponse(DettaglioTenantBackoffice dettaglioTenantBackoffice) {
		super();
		this.idBundles = dettaglioTenantBackoffice.getIdBundles();
		this.maxdatasetnum = dettaglioTenantBackoffice.getMaxdatasetnum();
		this.maxstreamsnum = dettaglioTenantBackoffice.getMaxstreamsnum();
		this.hasstage = dettaglioTenantBackoffice.getHasstage();
		this.maxOdataResultperpage = dettaglioTenantBackoffice.getMaxOdataResultperpage();
		this.zeppelin = dettaglioTenantBackoffice.getZeppelin();
	}
	
	public Integer getIdBundles() {
		return idBundles;
	}
	public void setIdBundles(Integer idBundles) {
		this.idBundles = idBundles;
	}
	public Integer getMaxdatasetnum() {
		return maxdatasetnum;
	}
	public void setMaxdatasetnum(Integer maxdatasetnum) {
		this.maxdatasetnum = maxdatasetnum;
	}
	public Integer getMaxstreamsnum() {
		return maxstreamsnum;
	}
	public void setMaxstreamsnum(Integer maxstreamsnum) {
		this.maxstreamsnum = maxstreamsnum;
	}
	public String getHasstage() {
		return hasstage;
	}
	public void setHasstage(String hasstage) {
		this.hasstage = hasstage;
	}
	public Integer getMaxOdataResultperpage() {
		return maxOdataResultperpage;
	}
	public void setMaxOdataResultperpage(Integer maxOdataResultperpage) {
		this.maxOdataResultperpage = maxOdataResultperpage;
	}
	public String getZeppelin() {
		return zeppelin;
	}
	public void setZeppelin(String zeppelin) {
		this.zeppelin = zeppelin;
	}
}
