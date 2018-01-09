package org.csi.yucca.adminapi.request;

import java.util.List;

public class DomainRequest {
	
	private String langit;
	private String langen;
	private Integer idDomain;
	private String domaincode;
	public Boolean deprecated;
	private List<Integer> ecosystemCodeList;
	
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
	public Boolean getDeprecated() {
		return deprecated;
	}
	public void setDeprecated(Boolean deprecated) {
		this.deprecated = deprecated;
	}
	public List<Integer> getEcosystemCodeList() {
		return ecosystemCodeList;
	}
	public void setEcosystemCodeList(List<Integer> ecosystemCodeList) {
		this.ecosystemCodeList = ecosystemCodeList;
	}
	
}
