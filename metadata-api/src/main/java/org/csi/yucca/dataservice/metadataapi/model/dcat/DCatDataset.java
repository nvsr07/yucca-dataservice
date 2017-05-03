package org.csi.yucca.dataservice.metadataapi.model.dcat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.csi.yucca.dataservice.metadataapi.util.DCatSdpHelper;

import com.google.gson.annotations.SerializedName;

public class DCatDataset extends DCatObject {

	// mandatory
	@SerializedName("dcterms:identifier")
	private String identifier;

	@SerializedName("dcterms:title")
	private I18NString title;

	@SerializedName("dcterms:description")
	private I18NString description;

	@SerializedName("dcterms:modified")
	private DCatDate modified;

	@SerializedName("dcterms:theme")
	private DCatTheme theme;

	@SerializedName("dcterms:rightsHolder")
	private DCatAgent rightsHolder;

	@SerializedName("dcterms:accrualPeriodicity")
	private IdString accrualPeriodicity;

	@SerializedName("dcat:distribution")
	private List<DCatDistribution> distributions = new ArrayList<DCatDistribution>();

	// reccomended
	@SerializedName("dcterms:subject")
	private List<IdString> subThemes;

	@SerializedName("dcat:contactPoint")
	private DCatVCard contactPoint = new DCatVCard();

	@SerializedName("dcterms:publisher")
	private DCatAgent publisher;

	// optional
	@SerializedName("dcterms:creator")
	private DCatAgent creator;

	@SerializedName("owl:versionInfo")
	private String versionInfo;

	@SerializedName("dcterms:issued")
	private DCatDate issued;

	@SerializedName("dcat:landingPage")
	private IdString landingPage;

	@SerializedName("dcterms:language")
	private IdString language;

	@SerializedName("dcat:keyword")
	private List<String> keywords;

	@SerializedName("dcterms:isVersionOf")
	private IdString isVersionOf;

	@SerializedName("dcterms:temporal")
	private IdString temporal;

	@SerializedName("dcterms:spatial")
	private IdString spatial;

	@SerializedName("dcterms:conformsTo")
	private IdString conformsTo;
	@SerializedName("adms:identifier")
	private IdString identifier_alternative;

	public DCatDataset() {
		super();
		types.add("dcat:Dataset");
		creator = DCatSdpHelper.getCSIAgentDcat();
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public I18NString getTitle() {
		return title;
	}

	public void setTitle(I18NString title) {
		this.title = title;
	}

	public I18NString getDescription() {
		return description;
	}

	public void setDescription(I18NString description) {
		this.description = description;
	}

	public DCatDate getModified() {
		return modified;
	}

	public void setModified(DCatDate modified) {
		this.modified = modified;
	}

	public DCatTheme getTheme() {
		return theme;
	}

	public void setTheme(DCatTheme theme) {
		this.theme = theme;
	}

	public DCatAgent getRightsHolder() {
		return rightsHolder;
	}

	public void setRightsHolder(DCatAgent rightsHolder) {
		this.rightsHolder = rightsHolder;
	}

	public IdString getAccrualPeriodicity() {
		return accrualPeriodicity;
	}

	public void setAccrualPeriodicity(IdString accrualPeriodicity) {
		this.accrualPeriodicity = accrualPeriodicity;
	}

	public List<DCatDistribution> getDistributions() {
		return distributions;
	}

	public void setDistributions(List<DCatDistribution> distributions) {
		this.distributions = distributions;
	}

	public List<IdString> getSubThemes() {
		return subThemes;
	}

	public void setSubThemes(List<IdString> subThemes) {
		this.subThemes = subThemes;
	}

	public DCatVCard getContactPoint() {
		return contactPoint;
	}

	public void setContactPoint(DCatVCard contactPoint) {
		this.contactPoint = contactPoint;
	}

	public DCatAgent getPublisher() {
		return publisher;
	}

	public void setPublisher(DCatAgent publisher) {
		this.publisher = publisher;
	}

	public DCatAgent getCreator() {
		return creator;
	}

	public void setCreator(DCatAgent creator) {
		this.creator = creator;
	}

	public String getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(String versionInfo) {
		this.versionInfo = versionInfo;
	}

	public DCatDate getIssued() {
		return issued;
	}

	public void setIssued(DCatDate issued) {
		this.issued = issued;
	}

	public IdString getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(IdString landingPage) {
		this.landingPage = landingPage;
	}

	public IdString getLanguage() {
		return language;
	}

	public void setLanguage(IdString language) {
		this.language = language;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public IdString getIsVersionOf() {
		return isVersionOf;
	}

	public void setIsVersionOf(IdString isVersionOf) {
		this.isVersionOf = isVersionOf;
	}

	public IdString getTemporal() {
		return temporal;
	}

	public void setTemporal(IdString temporal) {
		this.temporal = temporal;
	}

	public IdString getSpatial() {
		return spatial;
	}

	public void setSpatial(IdString spatial) {
		this.spatial = spatial;
	}

	public IdString getConformsTo() {
		return conformsTo;
	}

	public void setConformsTo(IdString conformsTo) {
		this.conformsTo = conformsTo;
	}

	public IdString getIdentifier_alternative() {
		return identifier_alternative;
	}

	public void setIdentifier_alternative(IdString identifier_alternative) {
		this.identifier_alternative = identifier_alternative;
	}

	public void addKeyword(String keyword) {
		if (keywords == null)
			keywords = new LinkedList<String>();
		keywords.add(keyword);
	}

	public void addSubTheme(IdString subTheme) {
		if (subThemes == null)
			subThemes = new LinkedList<IdString>();
		subThemes.add(subTheme);
	}

	public void addDistribution(DCatDistribution distribution) {
		if (distributions == null)
			distributions = new LinkedList<DCatDistribution>();
		distributions.add(distribution);
	}

	// []
	// private String accessRights; // metadata.info.visibility
	// private String identifier; // metadata.datasetCode + "_" +
	// metadata.datasetVersion

	// private String spatial = "WGS84/UTM 32N";
	// private String type = "dcat:Dataset"; // oppure metadata.configData.type
	// ??

	// private String subTheme; // metadata.info.codSubDomain
	// private String creator = "CSI PIEMONTE";

}
