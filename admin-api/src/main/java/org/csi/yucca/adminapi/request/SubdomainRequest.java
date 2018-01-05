package org.csi.yucca.adminapi.request;

public class SubdomainRequest {

	private Integer idSubdomain;
	private String subdomaincode;
	private String langit;
	private String langen;
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
		return langit;
	}

	public void setLangIt(String langIt) {
		this.langit = langIt;
	}

	public String getLangEn() {
		return langen;
	}

	public void setLangEn(String langEn) {
		this.langen = langEn;
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
