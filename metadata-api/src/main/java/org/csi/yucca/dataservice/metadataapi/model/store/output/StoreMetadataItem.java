package org.csi.yucca.dataservice.metadataapi.model.store.output;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class StoreMetadataItem {

	private String name;
	private String provider;
	private String version;
	private String description;
	private String rates;
	private String endpoint;
	private String thumbnailurl;
	private String visibility;
	private String visibleRoles;
	private String docName;
	private String docSummary;
	private String docSourceURL;
	private String docFilePath;
	private String extraApi;
	private String extraCodiceTenant;
	private String extraCopyright;
	private String extraDisclaimer;
	private String extraCodiceStream;
	private String extraNomeStream;
	private String extraNomeTenant;
	private String extraLicence;
	private String extraVirtualEntityName;
	private String extraVirtualEntityDescription;
	private String extraVirtualEntityCode;
	private String extraApiDescription;
	private String extraDomain;
	private String Tags;

	public StoreMetadataItem() {
	}

	public static StoreMetadataItem fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, StoreMetadataItem.class);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRates() {
		return rates;
	}

	public void setRates(String rates) {
		this.rates = rates;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getThumbnailurl() {
		return thumbnailurl;
	}

	public void setThumbnailurl(String thumbnailurl) {
		this.thumbnailurl = thumbnailurl;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getVisibleRoles() {
		return visibleRoles;
	}

	public void setVisibleRoles(String visibleRoles) {
		this.visibleRoles = visibleRoles;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocSummary() {
		return docSummary;
	}

	public void setDocSummary(String docSummary) {
		this.docSummary = docSummary;
	}

	public String getDocSourceURL() {
		return docSourceURL;
	}

	public void setDocSourceURL(String docSourceURL) {
		this.docSourceURL = docSourceURL;
	}

	public String getDocFilePath() {
		return docFilePath;
	}

	public void setDocFilePath(String docFilePath) {
		this.docFilePath = docFilePath;
	}

	public String getExtraApi() {
		return extraApi;
	}

	public void setExtraApi(String extraApi) {
		this.extraApi = extraApi;
	}

	public String getExtraCodiceTenant() {
		return extraCodiceTenant;
	}

	public void setExtraCodiceTenant(String extraCodiceTenant) {
		this.extraCodiceTenant = extraCodiceTenant;
	}

	public String getExtraCopyright() {
		return extraCopyright;
	}

	public void setExtraCopyright(String extraCopyright) {
		this.extraCopyright = extraCopyright;
	}

	public String getExtraDisclaimer() {
		return extraDisclaimer;
	}

	public void setExtraDisclaimer(String extraDisclaimer) {
		this.extraDisclaimer = extraDisclaimer;
	}

	public String getExtraCodiceStream() {
		return extraCodiceStream;
	}

	public void setExtraCodiceStream(String extraCodiceStream) {
		this.extraCodiceStream = extraCodiceStream;
	}

	public String getExtraNomeStream() {
		return extraNomeStream;
	}

	public void setExtraNomeStream(String extraNomeStream) {
		this.extraNomeStream = extraNomeStream;
	}

	public String getExtraNomeTenant() {
		return extraNomeTenant;
	}

	public void setExtraNomeTenant(String extraNomeTenant) {
		this.extraNomeTenant = extraNomeTenant;
	}

	public String getExtraLicence() {
		return extraLicence;
	}

	public void setExtraLicence(String extraLicence) {
		this.extraLicence = extraLicence;
	}

	public String getExtraVirtualEntityName() {
		return extraVirtualEntityName;
	}

	public void setExtraVirtualEntityName(String extraVirtualEntityName) {
		this.extraVirtualEntityName = extraVirtualEntityName;
	}

	public String getExtraVirtualEntityDescription() {
		return extraVirtualEntityDescription;
	}

	public void setExtraVirtualEntityDescription(String extraVirtualEntityDescription) {
		this.extraVirtualEntityDescription = extraVirtualEntityDescription;
	}

	public String getExtraVirtualEntityCode() {
		return extraVirtualEntityCode;
	}

	public void setExtraVirtualEntityCode(String extraVirtualEntityCode) {
		this.extraVirtualEntityCode = extraVirtualEntityCode;
	}

	public String getExtraApiDescription() {
		return extraApiDescription;
	}

	public void setExtraApiDescription(String extraApiDescription) {
		this.extraApiDescription = extraApiDescription;
	}

	public String getExtraDomain() {
		return extraDomain;
	}

	public void setExtraDomain(String extraDomain) {
		this.extraDomain = extraDomain;
	}

	public String getTags() {
		return Tags;
	}

	public void setTags(String Tags) {
		this.Tags = Tags;
	}

}
