package org.csi.yucca.adminapi.model;

public class Subdomain {

	private Integer idSubdomain;
	private String subdomaincode;
	private String langIt;
	private String langEn;
	private int deprecated;
	private Integer idDomain;

	public Subdomain subdomaincode(String subdomaincode){
		setSubdomaincode(subdomaincode);
		return this;
	}
	public Subdomain langIt(String langIt){
		setLangIt(langIt);
		return this;
	}
	public Subdomain langEn(String langEn){
		setLangEn(langEn);
		return this;
	}
	public Subdomain idDomain(Integer idDomain){
		setIdDomain(idDomain);
		return this;
	}
	
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

	public Integer getIdDomain() {
		return idDomain;
	}

	public void setIdDomain(Integer idDomain) {
		this.idDomain = idDomain;
	}

	public int getDeprecated() {
		return deprecated;
	}

	public void setDeprecated(int deprecated) {
		this.deprecated = deprecated;
	}

}
