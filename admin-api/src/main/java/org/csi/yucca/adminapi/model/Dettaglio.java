package org.csi.yucca.adminapi.model;

import java.io.IOException;
import java.sql.Timestamp;

import org.csi.yucca.adminapi.util.Constants;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Dettaglio {

	private Integer idDataSource;
	private Integer datasourceversion;
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

	private String dataSourceCopyright;
	private Integer dataSourceIsopendata;
	private String dataSourceExternalReference;
	private String dataSourceOpenDataAuthor;
	private Timestamp dataSourceOpenDataUpdateDate;
	private String dataSourceOpenDataLanguage;
	private String dataSourceLastUpdate;
	private String dataSourceDisclaimer;
	private String dataSourceRequesterName;
	private String dataSourceRequesterSurname;
	private String dataSourceRequesterMail;
	private Integer dataSourcePrivacyAcceptance;
	private String dataSourceIcon;
	private String dcat; // JSON
	private String license; // JSON
	private String components; // JSON
	private String sharingTenant; // JSON

	public String getDataSourceCopyright() {
		return dataSourceCopyright;
	}

	public void setDataSourceCopyright(String dataSourceCopyright) {
		this.dataSourceCopyright = dataSourceCopyright;
	}

	public Integer getDataSourceIsopendata() {
		return dataSourceIsopendata;
	}

	public void setDataSourceIsopendata(Integer dataSourceIsopendata) {
		this.dataSourceIsopendata = dataSourceIsopendata;
	}

	public String getDataSourceExternalReference() {
		return dataSourceExternalReference;
	}

	public void setDataSourceExternalReference(String dataSourceExternalReference) {
		this.dataSourceExternalReference = dataSourceExternalReference;
	}

	public String getDataSourceOpenDataAuthor() {
		return dataSourceOpenDataAuthor;
	}

	public void setDataSourceOpenDataAuthor(String dataSourceOpenDataAuthor) {
		this.dataSourceOpenDataAuthor = dataSourceOpenDataAuthor;
	}

	public Timestamp getDataSourceOpenDataUpdateDate() {
		return dataSourceOpenDataUpdateDate;
	}

	public void setDataSourceOpenDataUpdateDate(Timestamp dataSourceOpenDataUpdateDate) {
		this.dataSourceOpenDataUpdateDate = dataSourceOpenDataUpdateDate;
	}

	public String getDataSourceOpenDataLanguage() {
		return dataSourceOpenDataLanguage;
	}

	public void setDataSourceOpenDataLanguage(String dataSourceOpenDataLanguage) {
		this.dataSourceOpenDataLanguage = dataSourceOpenDataLanguage;
	}

	public String getDataSourceLastUpdate() {
		return dataSourceLastUpdate;
	}

	public void setDataSourceLastUpdate(String dataSourceLastUpdate) {
		this.dataSourceLastUpdate = dataSourceLastUpdate;
	}

	public String getDataSourceDisclaimer() {
		return dataSourceDisclaimer;
	}

	public void setDataSourceDisclaimer(String dataSourceDisclaimer) {
		this.dataSourceDisclaimer = dataSourceDisclaimer;
	}

	public String getDataSourceRequesterName() {
		return dataSourceRequesterName;
	}

	public void setDataSourceRequesterName(String dataSourceRequesterName) {
		this.dataSourceRequesterName = dataSourceRequesterName;
	}

	public String getDataSourceRequesterSurname() {
		return dataSourceRequesterSurname;
	}

	public void setDataSourceRequesterSurname(String dataSourceRequesterSurname) {
		this.dataSourceRequesterSurname = dataSourceRequesterSurname;
	}

	public String getDataSourceRequesterMail() {
		return dataSourceRequesterMail;
	}

	public void setDataSourceRequesterMail(String dataSourceRequesterMail) {
		this.dataSourceRequesterMail = dataSourceRequesterMail;
	}

	public Integer getDataSourcePrivacyAcceptance() {
		return dataSourcePrivacyAcceptance;
	}

	public void setDataSourcePrivacyAcceptance(Integer dataSourcePrivacyAcceptance) {
		this.dataSourcePrivacyAcceptance = dataSourcePrivacyAcceptance;
	}

	public String getDataSourceIcon() {
		return dataSourceIcon;
	}

	public void setDataSourceIcon(String dataSourceIcon) {
		this.dataSourceIcon = dataSourceIcon;
	}

	public String getDcat() {
		return dcat;
	}

	public void setDcat(String dcat) {
		this.dcat = dcat;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getComponents() {
		return components;
	}

	public void setComponents(String components) {
		this.components = components;
	}

	public String getSharingTenant() {
		return sharingTenant;
	}

	public void setSharingTenant(String sharingTenant) {
		this.sharingTenant = sharingTenant;
	}

	public Integer getIdDataSource() {
		return idDataSource;
	}

	public void setIdDataSource(Integer idDataSource) {
		this.idDataSource = idDataSource;
	}

	public Integer getDatasourceversion() {
		return datasourceversion;
	}

	public void setDatasourceversion(Integer datasourceversion) {
		this.datasourceversion = datasourceversion;
	}

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

	public Component[] deserializeComponents() throws JsonParseException, JsonMappingException, IOException {
		Component[] deserializedComponents = null;
		if (getComponents() != null) {

			ObjectMapper mapper = new ObjectMapper();
			deserializedComponents = mapper.readValue(getComponents(), Component[].class);

		}
		return deserializedComponents;

	}

	public static String generateNameSpace(String tenantCode, String datasetcode) {
		return Constants.API_NAMESPACE_BASE + "." + tenantCode + "." + datasetcode;
	}

}
