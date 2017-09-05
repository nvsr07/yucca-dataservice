package org.csi.yucca.adminapi.response;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.model.Domain;
import org.csi.yucca.adminapi.model.Subdomain;
import org.csi.yucca.adminapi.util.Errors;

public class BackofficeLoadDomainResponse extends Response{
	
	private String langit;
	private String langen;
	private Integer idDomain;
	private String domaincode;
	private Integer deprecated;
	private List<Integer> subdomains;
	
	public BackofficeLoadDomainResponse(Domain domain, List<Subdomain> subdomains) {
		super();
		this.langit = domain.getLangit();
		this.langen = domain.getLangen();
		this.idDomain = domain.getIdDomain();
		this.domaincode = domain.getDomaincode();
		this.deprecated = domain.getDeprecated();
		
		if (subdomains != null && !subdomains.isEmpty()) {
			
			this.subdomains = new ArrayList<Integer>();
			
			for (Subdomain subdomain : subdomains) {
				this.subdomains.add(subdomain.getIdSubdomain());
			}
		}
	}
	
	public BackofficeLoadDomainResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BackofficeLoadDomainResponse(Errors errors) {
		super(errors);
		// TODO Auto-generated constructor stub
	}

	public String getLangit() {
		return langit;
	}

	public void setLangit(String langit) {
		this.langit = langit;
	}

	public String getLangen() {
		return langen;
	}

	public void setLangen(String langen) {
		this.langen = langen;
	}

	public Integer getIdDomain() {
		return idDomain;
	}

	public void setIdDomain(Integer idDomain) {
		this.idDomain = idDomain;
	}

	public String getDomaincode() {
		return domaincode;
	}

	public void setDomaincode(String domaincode) {
		this.domaincode = domaincode;
	}

	public Integer getDeprecated() {
		return deprecated;
	}

	public void setDeprecated(Integer deprecated) {
		this.deprecated = deprecated;
	}

	public List<Integer> getSubdomains() {
		return subdomains;
	}

	public void setSubdomains(List<Integer> subdomains) {
		this.subdomains = subdomains;
	}
	
}
