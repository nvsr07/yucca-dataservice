package org.csi.yucca.adminapi.model;

import java.sql.Timestamp;

public class Dettaglio {
	
	private String dataSourceVisibility;
	private Integer dataSourceUnpublished;
	private Timestamp dataSourceRegistrationDate;
	private String statusCode;
	private String statusDescription;
	private Integer idStatus;

	private Integer domIdDomain;
	private String domLangEn;
	private String domLangIt;
	private String domDomainCode;
	private Integer subIdSubDomain;
	private String subSubDomainCode;
	private String subLangIt;
	private String subLangEn;
	private String organizationCode;
	private String organizationDescription;
	private Integer idOrganization;
	private Integer dataSourceIsActive;
	private Integer dataSourceIsManager;

	private String tenantCode;
	private String tenantName;
	private String tenantDescription;
	private Integer idTenant;
	private String tags;

	public String getDataSourceVisibility() {
		return dataSourceVisibility;
	}

	public void setDataSourceVisibility(String dataSourceVisibility) {
		this.dataSourceVisibility = dataSourceVisibility;
	}

	public Integer getDataSourceUnpublished() {
		return dataSourceUnpublished;
	}

	public void setDataSourceUnpublished(Integer dataSourceUnpublished) {
		this.dataSourceUnpublished = dataSourceUnpublished;
	}

	public Timestamp getDataSourceRegistrationDate() {
		return dataSourceRegistrationDate;
	}

	public void setDataSourceRegistrationDate(Timestamp dataSourceRegistrationDate) {
		this.dataSourceRegistrationDate = dataSourceRegistrationDate;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public Integer getIdStatus() {
		return idStatus;
	}

	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}

	public Integer getDomIdDomain() {
		return domIdDomain;
	}

	public void setDomIdDomain(Integer domIdDomain) {
		this.domIdDomain = domIdDomain;
	}

	public String getDomLangEn() {
		return domLangEn;
	}

	public void setDomLangEn(String domLangEn) {
		this.domLangEn = domLangEn;
	}

	public String getDomLangIt() {
		return domLangIt;
	}

	public void setDomLangIt(String domLangIt) {
		this.domLangIt = domLangIt;
	}

	public String getDomDomainCode() {
		return domDomainCode;
	}

	public void setDomDomainCode(String domDomainCode) {
		this.domDomainCode = domDomainCode;
	}

	public Integer getSubIdSubDomain() {
		return subIdSubDomain;
	}

	public void setSubIdSubDomain(Integer subIdSubDomain) {
		this.subIdSubDomain = subIdSubDomain;
	}

	public String getSubSubDomainCode() {
		return subSubDomainCode;
	}

	public void setSubSubDomainCode(String subSubDomainCode) {
		this.subSubDomainCode = subSubDomainCode;
	}

	public String getSubLangIt() {
		return subLangIt;
	}

	public void setSubLangIt(String subLangIt) {
		this.subLangIt = subLangIt;
	}

	public String getSubLangEn() {
		return subLangEn;
	}

	public void setSubLangEn(String subLangEn) {
		this.subLangEn = subLangEn;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getOrganizationDescription() {
		return organizationDescription;
	}

	public void setOrganizationDescription(String organizationDescription) {
		this.organizationDescription = organizationDescription;
	}

	public Integer getIdOrganization() {
		return idOrganization;
	}

	public void setIdOrganization(Integer idOrganization) {
		this.idOrganization = idOrganization;
	}

	public Integer getDataSourceIsActive() {
		return dataSourceIsActive;
	}

	public void setDataSourceIsActive(Integer dataSourceIsActive) {
		this.dataSourceIsActive = dataSourceIsActive;
	}

	public Integer getDataSourceIsManager() {
		return dataSourceIsManager;
	}

	public void setDataSourceIsManager(Integer dataSourceIsManager) {
		this.dataSourceIsManager = dataSourceIsManager;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getTenantDescription() {
		return tenantDescription;
	}

	public void setTenantDescription(String tenantDescription) {
		this.tenantDescription = tenantDescription;
	}

	public Integer getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Integer idTenant) {
		this.idTenant = idTenant;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

}
