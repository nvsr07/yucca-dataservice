package org.csi.yucca.adminapi.request;

public class LicenseRequest {

	private Integer idLicense;
	private String licensecode;
	private String description;
    private String disclaimer;
	
    
	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public Integer getIdLicense() {
		return idLicense;
	}

	public void setIdLicense(Integer idLicense) {
		this.idLicense = idLicense;
	}

	public String getLicensecode() {
		if(idLicense != null)return null;
		return licensecode;
	}

	public void setLicensecode(String licensecode) {
		this.licensecode = licensecode;
	}

	public String getDescription() {
		if(idLicense != null)return null;
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
