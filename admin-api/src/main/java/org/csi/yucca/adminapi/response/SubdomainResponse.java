package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.ISubdomain;
import org.csi.yucca.adminapi.model.Subdomain;
import org.csi.yucca.adminapi.util.Errors;

import com.fasterxml.jackson.annotation.JsonInclude;

public class SubdomainResponse extends Response{

	private Integer idSubdomain;
	private String subdomaincode;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String langit;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String langen;
	private int deprecated;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer idDomain;

	public SubdomainResponse(ISubdomain iSubdomain) {
		super();
		if (iSubdomain != null) {
			this.idSubdomain = iSubdomain.getSubIdSubDomain();
			this.subdomaincode = iSubdomain.getSubSubDomainCode();
			this.langit = iSubdomain.getSubLangIt();
			this.langen = iSubdomain.getSubLangEn();
		}
	}
	
	public SubdomainResponse(Subdomain subdomain ) {
		super();
		this.idSubdomain = subdomain.getIdSubdomain();
		this.subdomaincode = subdomain.getSubdomaincode();
		this.langit = subdomain.getLangIt();
		this.langen = subdomain.getLangEn();
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
