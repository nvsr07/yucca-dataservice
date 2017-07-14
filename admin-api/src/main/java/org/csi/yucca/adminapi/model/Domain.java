package org.csi.yucca.adminapi.model;

public class Domain {
	
	private Integer idDomain;
	private String domaincode;
	private String langit;
	private String langen;
	private Integer deprecated;
	
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
	public Integer getDeprecated() {
		return deprecated;
	}
	public void setDeprecated(Integer deprecated) {
		this.deprecated = deprecated;
	}
	
}
