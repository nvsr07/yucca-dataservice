package org.csi.yucca.adminapi.model;

public class LicenseJson {

	private Integer id_license;
	private String licensecode;
	private String description;

	public Integer getId_license() {
		return id_license;
	}

	public void setId_license(Integer id_license) {
		this.id_license = id_license;
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
