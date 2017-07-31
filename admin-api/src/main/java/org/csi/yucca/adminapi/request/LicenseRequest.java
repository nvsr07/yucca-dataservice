package org.csi.yucca.adminapi.request;

public class LicenseRequest {

	private Integer idLicense;
	private String licensecode;
	private String description;

	public Integer getIdLicense() {
		return idLicense;
	}

	public void setIdLicense(Integer idLicense) {
		this.idLicense = idLicense;
	}

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
