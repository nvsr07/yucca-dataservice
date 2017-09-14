package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.License;
import org.csi.yucca.adminapi.util.Errors;

import com.fasterxml.jackson.annotation.JsonInclude;

public class LicenseResponse extends Response{
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer idLicense;
	
	private String licensecode;
	private String description;
	
	public LicenseResponse() {
		super();
	}

	public LicenseResponse(License license) {
		super();
		this.idLicense = license.getIdLicense();
		this.licensecode = license.getLicensecode();
		this.description = license.getDescription();
	}
	
	public LicenseResponse(Errors errors, String arg) {
		super(errors, arg);
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

	public Integer getIdLicense() {
		return idLicense;
	}

	public void setIdLicense(Integer idLicense) {
		this.idLicense = idLicense;
	}
	
}
