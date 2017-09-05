package org.csi.yucca.adminapi.request;

public class SubdomainRequest {

	private Integer idSubdomain;
	private String subdomaincode;
	private String langIt;
	private String langEn;
	private int deprecated;
	private Integer idDomain;

	public Integer getIdSubdomain() {
		return idSubdomain;
	}

	public void setIdSubdomain(Integer idSubdomain) {
		this.idSubdomain = idSubdomain;
	}

	public String getSubdomaincode() {
		return subdomaincode;
	}

	public void setSubdomaincode(String subdomaincode) {
		this.subdomaincode = subdomaincode;
	}

	public String getLangIt() {
		return langIt;
	}

	public void setLangIt(String langIt) {
		this.langIt = langIt;
	}

	public String getLangEn() {
		return langEn;
	}

	public void setLangEn(String langEn) {
		this.langEn = langEn;
	}

	public int getDeprecated() {
		return deprecated;
	}

	public void setDeprecated(int deprecated) {
		this.deprecated = deprecated;
	}

	public Integer getIdDomain() {
		return idDomain;
	}

	public void setIdDomain(Integer idDomain) {
		this.idDomain = idDomain;
	}

}
