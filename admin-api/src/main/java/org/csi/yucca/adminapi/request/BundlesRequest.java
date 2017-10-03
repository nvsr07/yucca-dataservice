package org.csi.yucca.adminapi.request;

public class BundlesRequest {

	private Integer maxdatasetnum;
	private Integer maxstreamsnum;
	private String hasstage;
	private Integer maxOdataResultperpage;
	private Integer zeppelin;
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
	public Integer getZeppelin() {
		return zeppelin;
	}
	public void setZeppelin(Integer zeppelin) {
		this.zeppelin = zeppelin;
	}
	
}
