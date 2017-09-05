package org.csi.yucca.adminapi.model;

public class License {

	private Integer idLicense;
	private String licensecode;
	private String description;

	public License(Integer idLicense, String licensecode, String description) {
		super();
		this.idLicense = idLicense;
		this.licensecode = licensecode;
		this.description = description;
	}

	public License() {
		super();
		// TODO Auto-generated constructor stub
	}

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
