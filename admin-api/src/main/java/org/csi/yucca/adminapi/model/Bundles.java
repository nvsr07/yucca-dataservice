package org.csi.yucca.adminapi.model;

public class Bundles {

	private Integer idBundles;
	private Integer maxdatasetnum;
	private Integer maxstreamsnum;
	private String hasstage;
	private Integer maxOdataResultperpage;
	private String zeppelin;
	
	public Bundles() {
		super();
		this.maxdatasetnum = -1;
		this.maxstreamsnum = -1;
		this.maxOdataResultperpage = 1000;
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
