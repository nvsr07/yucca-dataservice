package org.csi.yucca.adminapi.response;

public class LicenseResponse extends Response{

	private String licensecode;
	private String description;
	
	public String getLicensecode() {
		return licensecode;
	}
	public void setLicensecode(String licensecode) {
		this.licensecode = licensecode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
