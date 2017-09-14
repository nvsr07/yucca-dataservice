package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.Subdomain;
import org.csi.yucca.adminapi.util.Errors;

import com.fasterxml.jackson.annotation.JsonInclude;

public class SubdomainResponse extends Response{

	private Integer idSubdomain;
	private String subdomaincode;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String langIt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String langEn;
	private int deprecated;
	private Integer idDomain;
	
	public SubdomainResponse(Subdomain subdomain ) {
		super();
		this.idSubdomain = subdomain.getIdSubdomain();
		this.subdomaincode = subdomain.getSubdomaincode();
		this.langIt = subdomain.getLangIt();
		this.langEn = subdomain.getLangEn();
		this.deprecated = subdomain.getDeprecated();
		this.idDomain = subdomain.getIdDomain();
	}

	public SubdomainResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SubdomainResponse(Errors errors, String arg) {
		super(errors, arg);
		// TODO Auto-generated constructor stub
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
