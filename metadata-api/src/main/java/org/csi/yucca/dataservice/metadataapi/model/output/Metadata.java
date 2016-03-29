package org.csi.yucca.dataservice.metadataapi.model.output;

import java.util.Date;

import org.csi.yucca.dataservice.metadataapi.delegate.I18nDelegate;
import org.csi.yucca.dataservice.metadataapi.model.store.output.StoreMetadataItem;
import org.csi.yucca.dataservice.metadataapi.util.Util;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Metadata {
	public static final String METADATA_TYPE_STREAM = "stream";
	public static final String METADATA_TYPE_DATASET = "dataset";

	private String name;
	private String code;
	private String version;
	private String description;
	private String type;
	private String domain;
	private String requestorname;
	private String requestorsurname;
	private String requestoremail;
	private String tenantCode;
	private String tenantName;
	private String[] tagCodes;
	private String[] tags;
	private String icon;
	private String visibility;
	private String[] sharedtenants;
	private Boolean isopendata;
	private String author;
	private String language;
	private Date datalastupdate;
	private String externalreference;
	private Boolean ispublished;
	private String license;
	private String disclaimer;
	private String copyright;

	private Stream stream;
	private Dataset dataset;

	public Metadata() {
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getRequestorname() {
		return requestorname;
	}

	public void setRequestorname(String requestorname) {
		this.requestorname = requestorname;
	}

	public String getRequestorsurname() {
		return requestorsurname;
	}

	public void setRequestorsurname(String requestorsurname) {
		this.requestorsurname = requestorsurname;
	}

	public String getRequestoremail() {
		return requestoremail;
	}

	public void setRequestoremail(String requestoremail) {
		this.requestoremail = requestoremail;
	}

	public String[] getTagCodes() {
		return tagCodes;
	}

	public void setTagCodes(String[] tagCodes) {
		this.tagCodes = tagCodes;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String[] getSharedtenants() {
		return sharedtenants;
	}

	public void setSharedtenants(String[] sharedtenants) {
		this.sharedtenants = sharedtenants;
	}

	public Boolean getIsopendata() {
		return isopendata;
	}

	public void setIsopendata(Boolean isopendata) {
		this.isopendata = isopendata;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getDatalastupdate() {
		return datalastupdate;
	}

	public void setDatalastupdate(Date datalastupdate) {
		this.datalastupdate = datalastupdate;
	}

	public String getExternalreference() {
		return externalreference;
	}

	public void setExternalreference(String externalreference) {
		this.externalreference = externalreference;
	}

	public Boolean getIspublished() {
		return ispublished;
	}

	public void setIspublished(Boolean ispublished) {
		this.ispublished = ispublished;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public Stream getStream() {
		return stream;
	}

	public void setStream(Stream stream) {
		this.stream = stream;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public static Metadata createFromStoreItem(StoreMetadataItem item, String lang) {

		Metadata metadata = new Metadata();
		metadata.setCode(item.getName());
		metadata.setVersion(item.getVersion());
		metadata.setName(item.getDescription());
		metadata.setDescription(item.getExtraApiDescription());
		metadata.setDomain(I18nDelegate.translateDomain(item.getExtraDomain(), lang));
		metadata.setVisibility(item.getVisibility());
		metadata.setLicense(item.getExtraLicence());
		metadata.setDisclaimer(item.getExtraDisclaimer());
		metadata.setCopyright(item.getExtraCopyright());
		metadata.setTenantCode(item.getExtraCodiceTenant());
		metadata.setTenantName(item.getExtraNomeTenant());

		if (item.getTags() != null) {
			metadata.setTagCodes(item.getTags().split(","));
			metadata.setTags(I18nDelegate.translateTags(metadata.getTagCodes(), lang));
		}
		if (item.getName().endsWith("_odata")) {
			metadata.setType(METADATA_TYPE_DATASET);
			// data.datasetIcon = Constants.API_RESOURCES_URL +
			// "dataset/icon/"+data.tenantCode+"/"+data.datasetCode;
		} else if (item.getName().endsWith("_stream")) {
			metadata.setType(METADATA_TYPE_STREAM);
			metadata.setCode(item.getName().substring(0, item.getName().length() - 7));
			metadata.setName(item.getExtraCodiceStream() + " " + item.getExtraVirtualEntityCode());
			// data.datasetIcon = Constants.API_RESOURCES_URL +
			// "stream/icon/"+data.tenantCode+"/"+data.virtualentityCode+"/"+data.streamCode;
		}

		if (!Util.isEmpty(item.getExtraVirtualEntityCode())) {
			Smartobject smartobject = new Smartobject();
			smartobject.setCode(item.getExtraVirtualEntityCode());
			smartobject.setName(item.getExtraVirtualEntityName());
			smartobject.setDescription(item.getExtraVirtualEntityDescription());

			Stream stream = metadata.getStream() == null ? new Stream() : metadata.getStream();
			stream.setSmartobject(smartobject);
			metadata.setStream(stream);
		}

		// private String provider;
		// private String rates;
		// private String endpoint;
		// private String thumbnailurl;
		// private String visibleRoles;
		// private String docName;
		// private String docSummary;
		// private String docSourceURL;
		// private String docFilePath;
		// private String extraApi;

		// private String extraVirtualEntityName;
		// private String extraVirtualEntityDescription;
		// private String extraVirtualEntityCode;
		// private String extraApiDescription;
		// private String extraDomain;
		// private String tags;

		return metadata;
	}

}
