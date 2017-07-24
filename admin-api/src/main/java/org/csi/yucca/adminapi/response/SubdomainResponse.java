package org.csi.yucca.adminapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public class SubdomainResponse extends Response{

	private Integer idSubdomain;
	private String subdomaincode;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String langIt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String langEn;
	private String deprecated;
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

	public String getDeprecated() {
		return deprecated;
	}

	public void setDeprecated(String deprecated) {
		this.deprecated = deprecated;
	}

	public Integer getIdDomain() {
		return idDomain;
	}

	public void setIdDomain(Integer idDomain) {
		this.idDomain = idDomain;
	}

}
