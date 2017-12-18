package org.csi.yucca.adminapi.model;

import java.io.IOException;
import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
public class DettaglioDataset extends Dataset {

	private String jdbcdbschema;
	private String importedfiles;
	private String dataSourceCopyright;
	private Integer dataSourceIsOpendata;
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
	private String dcat;
	private String license;
	private String components;
	private String sharingTenant;
	
	public String getJdbcdbschema() {
		return jdbcdbschema;
	}

	public void setJdbcdbschema(String jdbcdbschema) {
		this.jdbcdbschema = jdbcdbschema;
	}

	public String getImportedfiles() {
		return importedfiles;
	}

	public void setImportedfiles(String importedfiles) {
		this.importedfiles = importedfiles;
	}

	public String getDataSourceCopyright() {
		return dataSourceCopyright;
	}

	public void setDataSourceCopyright(String dataSourceCopyright) {
		this.dataSourceCopyright = dataSourceCopyright;
	}

	public Integer getDataSourceIsOpendata() {
		return dataSourceIsOpendata;
	}

	public void setDataSourceIsOpendata(Integer dataSourceIsOpendata) {
		this.dataSourceIsOpendata = dataSourceIsOpendata;
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

	public Component[] deserializeComponents() throws JsonParseException, JsonMappingException, IOException {
		Component[] deserializedComponents = null;
		if (getComponents() != null) {
			
			ObjectMapper mapper = new ObjectMapper();
			deserializedComponents = mapper.readValue(getComponents(), Component[].class);

		}
		return deserializedComponents;

	}

}
