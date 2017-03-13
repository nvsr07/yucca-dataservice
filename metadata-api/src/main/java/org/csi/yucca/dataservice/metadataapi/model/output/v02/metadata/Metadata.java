package org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.csi.yucca.dataservice.metadataapi.delegate.v01.i18n.I18nDelegate;
import org.csi.yucca.dataservice.metadataapi.model.ckan.ExtraV2;
import org.csi.yucca.dataservice.metadataapi.model.ckan.Resource;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.stream.Twitter;
import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineJsonField;
import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineJsonFieldElement;
import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineJsonSo;
import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineMetadata;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.csi.yucca.dataservice.metadataapi.util.Util;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Metadata {
	public static final String METADATA_TYPE_STREAM = "stream";
	public static final String METADATA_TYPE_DATASET = "dataset";
	public static final String METADATA_TYPE_STREAM_DATASET = "stream dataset";

	public static final String METADATA_SUBTYPE_SOCIAL = "social";
	public static final String METADATA_SUBTYPE_BULK = "bulk";
	public static final String METADATA_SUBTYPE_BINARY = "binary";

	private String name;
	// private String code;
	private String version;
	private String description;
	private String type;
	private String subtype;
	private String domainCode;
	private String subdomainCode;
	private String domain;
	private String subdomain;
	private String organizationCode;
	private String organizationDescription;
	private String tenantCode;
	private List<String> tenantDelegateCodes;
	private String tenantName;
	private String tenantDescription;
	private List<String> tagCodes;
	private List<String> tags;
	private String icon;
	private String visibility;
	private Boolean isopendata;
	private String author;
	private String language;
	private Date registrationDate;
	private String externalreference;
	private String license;
	private String disclaimer;
	private String copyright;
	private Double latitude;
	private Double longitude;
	private String fps;

	private Stream stream;
	private Dataset dataset;
	private Opendata opendata;
	private DCat dcat;
	private List<Component> components;

	private int dcatReady;
	private Boolean isOpendata;

	public void setAuthor(String author) {
		this.author = author;
	}

	private String detailUrl;

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

	public List<String> getTagCodes() {
		return tagCodes;
	}

	public void setTagCodes(List<String> tagCodes) {
		this.tagCodes = tagCodes;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
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

	public Boolean getIsopendata() {
		return isopendata;
	}

	public void setIsopendata(Boolean isopendata) {
		this.isopendata = isopendata;
	}

	public String getAuthor() {
		return author;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getExternalreference() {
		return externalreference;
	}

	public void setExternalreference(String externalreference) {
		this.externalreference = externalreference;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
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

	public Opendata getOpendata() {
		return opendata;
	}

	public void setOpendata(Opendata opendata) {
		this.opendata = opendata;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public static String createCodeFromStream(String tenantCode, String smartobjectCode, String streamCode) {
		return tenantCode + "." + smartobjectCode + "_" + streamCode;
	}

	public String getCkanPackageId() {
		return "smartdatanet.it_" + getDataset().getCode();
	}

	public static String getApiNameFromCkanPackageId(String packageId) {
		return packageId.substring(packageId.indexOf("_") + 1);

	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getFps() {
		return fps;
	}

	public void setFps(String fps) {
		this.fps = fps;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public int getDcatReady() {
		return dcatReady;
	}

	public void setDcatReady(int dcatReady) {
		this.dcatReady = dcatReady;
	}

	public static String getMetadataTypeStream() {
		return METADATA_TYPE_STREAM;
	}

	public static String getMetadataTypeDataset() {
		return METADATA_TYPE_DATASET;
	}

	public String getDomainCode() {
		return domainCode;
	}

	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}

	public List<String> getTenantDelegateCodes() {
		return tenantDelegateCodes;
	}

	public void setTenantDelegateCodes(List<String> tenantDelegateCodes) {
		this.tenantDelegateCodes = tenantDelegateCodes;
	}

	public String getTenantDescription() {
		return tenantDescription;
	}

	public void setTenantDescription(String tenantDescription) {
		this.tenantDescription = tenantDescription;
	}

	public DCat getDcat() {
		return dcat;
	}

	public void setDcat(DCat dcat) {
		this.dcat = dcat;
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

	public String getSubdomainCode() {
		return subdomainCode;
	}

	public void setSubdomainCode(String subdomainCode) {
		this.subdomainCode = subdomainCode;
	}

	public String getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
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

	private void addComponent(Component component) {
		if (this.components == null)
			this.components = new LinkedList<Component>();
		this.components.add(component);

	}

	public String toCkan() {
		org.csi.yucca.dataservice.metadataapi.model.ckan.Dataset ckanDataset = new org.csi.yucca.dataservice.metadataapi.model.ckan.Dataset();
		ckanDataset.setId(getCkanPackageId());
		ckanDataset.setName(getCkanPackageId());
		ckanDataset.setTitle(getName());
		ckanDataset.setNotes(getDescription());
		ckanDataset.setVersion(getVersion());

		String metadataUrl = Config.getInstance().getUserportalBaseUrl() + "#/dataexplorer/dataset/" + getTenantCode() + "/" + getDataset().getCode();

		ckanDataset.setUrl(metadataUrl);

		Resource resourceApiOdata = new Resource();
		resourceApiOdata.setDescription("Api Odata");
		resourceApiOdata.setFormat("ODATA");
		String exposedApiBaseUrl = Config.getInstance().getExposedApiBaseUrl();
		String apiOdataUrl = exposedApiBaseUrl + getDataset().getCode();
		resourceApiOdata.setUrl(apiOdataUrl);
		ckanDataset.addResource(resourceApiOdata);

		Resource resourceDownload = new Resource();
		resourceDownload.setDescription("Csv download url");
		resourceDownload.setFormat("CSV");

		String downloadCsvUrl = exposedApiBaseUrl + getDataset().getCode() + "/download/" + getDataset().getDatasetId() + "/";

		if (METADATA_TYPE_DATASET.equals(getType()) && Dataset.DATASET_TYPE_BULK.equals(getDataset().getDatasetType())) {
			downloadCsvUrl += "all";
		} else {
			downloadCsvUrl += "current";
		}

		resourceDownload.setUrl(downloadCsvUrl);
		ckanDataset.addResource(resourceDownload);
		ExtraV2 extras = new ExtraV2();
		if (getDomain() != null) {
			extras.setTopic(getDomain());
			extras.setHidden_field(getDomain());
		}

		if (getOpendata() != null) {
			ckanDataset.setAuthor(getOpendata().getAuthor());
			if (getOpendata().getMetadaUpdateDate() != null)
				ckanDataset.setMetadata_created(Util.formatDateCkan(getOpendata().getMetadaUpdateDate()));
			if (getOpendata().getMetadaCreateDate() != null)
				extras.setMetadata_created(Util.formatDateCkan(getOpendata().getMetadaCreateDate()));
			if (getOpendata().getMetadaUpdateDate() != null)
				extras.setMetadata_modified(Util.formatDateCkan(getOpendata().getMetadaUpdateDate()));
			if (getRegistrationDate() != null)
				extras.setPackage_created(Util.formatDateCkan(getRegistrationDate()));
			if (getOpendata().getDataUpdateDate() != null)
				extras.setPackage_modified(Util.formatDateCkan(new Date(getOpendata().getDataUpdateDate())));

		}

		if (getDcat() != null) {
			extras.setDcatCreatorName(getDcat().getDcatCreatorName());
			extras.setDcatCreatorType(getDcat().getDcatCreatorType());
			extras.setDcatCreatorId(getDcat().getDcatCreatorId());
			extras.setDcatRightsHolderName(getDcat().getDcatRightsHolderName());
			extras.setDcatRightsHolderType(getDcat().getDcatRightsHolderType());
			extras.setDcatRightsHolderId(getDcat().getDcatRightsHolderId());
			extras.setDcatNomeOrg(getDcat().getDcatNomeOrg());
			extras.setDcatEmailOrg(getDcat().getDcatEmailOrg());
		}
		ckanDataset.setLicense(getLicense());
		extras.setDisclaimer(getDisclaimer());
		extras.setCopyright(getCopyright());

		ckanDataset.setIsopen(getOpendata() != null && getOpendata().isOpendata());

		if (getTags() != null) {
			for (String tag : getTags()) {
				ckanDataset.addTag(tag);
			}
		}

		if (ckanDataset.getResources() != null) {
			List<String> resourcesList = new LinkedList<String>();
			for (Resource resource : ckanDataset.getResources()) {
				resourcesList.add(resource.createResourceV2());

			}
			Map<String, List<String>> extrasList = new HashMap<String, List<String>>();
			extrasList.put("resource", resourcesList);
			ckanDataset.setExtrasList(extrasList);
		}

		// if (getDataset().getImportFileType() != null)
		extras.setPackage_type("CSV");
		ckanDataset.setExtras(extras);
		return ckanDataset.toJson();

	}

	public static Metadata createFromSearchEngineItem(SearchEngineMetadata searchEngineItem, String lang) {
		Gson gson = JSonHelper.getInstance();

		Metadata metadata = new Metadata();

		metadata.setVersion(searchEngineItem.getVersion());
		metadata.setName(searchEngineItem.getName()); // FIXME sicuro?
		metadata.setDomain(I18nDelegate.translate(searchEngineItem.getDomainCode(), lang,
				("en".equals(lang) ? searchEngineItem.getDomainLangEN() : searchEngineItem.getDomainLangIT())));
		metadata.setDomainCode(searchEngineItem.getDomainCode());
		metadata.setSubdomain(I18nDelegate.translate(searchEngineItem.getSubdomainCode(), lang,
				("en".equals(lang) ? searchEngineItem.getSubdomainLangEN() : searchEngineItem.getSubdomainLangIT())));
		metadata.setVisibility(searchEngineItem.getVisibility());
		metadata.setLicense(searchEngineItem.getLicenseCode());// FIXME sicuro?
		metadata.setDisclaimer(searchEngineItem.getLicenceDescription());// FIXME
																			// sicuro?
		metadata.setCopyright(searchEngineItem.getCopyright());
		metadata.setTenantCode(searchEngineItem.getTenantCode());
		metadata.setTenantName(searchEngineItem.getTenantName());
		metadata.setTenantDescription(searchEngineItem.getTenantDescription());
		metadata.setLatitude(searchEngineItem.getLatDouble());
		metadata.setLongitude(searchEngineItem.getLonDouble());
		metadata.setOrganizationCode(searchEngineItem.getOrganizationCode());
		metadata.setOrganizationDescription(searchEngineItem.getOrganizationDescription());

		if (searchEngineItem.getTagCode() != null) {
			metadata.setTagCodes(searchEngineItem.getTagCode());
			metadata.setTags(I18nDelegate.translateMulti(metadata.getTagCodes(), lang));
		}

		metadata.setType(searchEngineItem.getEntityType().get(0));
		metadata.setSubtype(metadata.getSubtype());

		String detailUrl = Config.getInstance().getMetadataapiBaseUrl() + "metadata/";
		String iconUrl = Config.getInstance().getMetadataapiBaseUrl() + "resource/icon/" + searchEngineItem.getTenantCode() + "/";

		if (searchEngineItem.getEntityType().get(0).contains("stream")) {
			metadata.setDescription(searchEngineItem.getName());
			detailUrl += searchEngineItem.getOrganizationCode() + "/" + searchEngineItem.getSoCode() + "/" + searchEngineItem.getStreamCode();
			iconUrl += searchEngineItem.getSoCode() + "/" + searchEngineItem.getStreamCode();
			Stream stream = new Stream();
			stream.setCode(searchEngineItem.getStreamCode());
			stream.setFps(searchEngineItem.getFps());

			Smartobject smartobject = new Smartobject();
			if (searchEngineItem.getJsonSo() != null) {
				SearchEngineJsonSo jsonSo = gson.fromJson(searchEngineItem.getJsonSo(), SearchEngineJsonSo.class);
				smartobject = Smartobject.createFromSearchEngineJsonSo(jsonSo);
			}
			if (searchEngineItem.getLat() != null)
				smartobject.setLatitude(searchEngineItem.getLatDouble());
			if (searchEngineItem.getLon() != null)
				smartobject.setLatitude(searchEngineItem.getLonDouble());

			smartobject.setCode(searchEngineItem.getSoCode());
			smartobject.setName(searchEngineItem.getSoName());
			smartobject.setDescription(searchEngineItem.getSoDescription());
			if (searchEngineItem.getSoCategory() != null && searchEngineItem.getSoCategory().size() > 0)
				smartobject.setCategory(searchEngineItem.getSoCategory().get(0));

			stream.setSmartobject(smartobject);

			if (searchEngineItem.getTwtQuery() != null) {
				// metadata.setSubtype(METADATA_SUBTYPE_SOCIAL);
				Twitter twitter = new Twitter();
				twitter.setTwtRatePercentage(searchEngineItem.getTwtRatePercentage());
				twitter.setTwtCount(searchEngineItem.getTwtCount());
				twitter.setTwtGeolocLat(searchEngineItem.getTwtGeolocLat());
				twitter.setTwtGeolocLon(searchEngineItem.getTwtGeolocLon());
				twitter.setTwtGeolocRadius(searchEngineItem.getTwtGeolocRadius());
				twitter.setTwtQuery(searchEngineItem.getTwtQuery());
				twitter.setTwtLang(searchEngineItem.getTwtLang());
				twitter.setTwtGeolocUnit(searchEngineItem.getTwtGeolocUnit());
				twitter.setTwtLocale(searchEngineItem.getTwtLocale());
				twitter.setTwtResultType(searchEngineItem.getTwtResultType());
				twitter.setTwtUntil(searchEngineItem.getTwtUntil());
				twitter.setTwtLastSearchId(searchEngineItem.getTwtLastSearchId());
				stream.setTwitter(twitter);
			}
			metadata.setStream(stream);
		} else {
			metadata.setDescription(searchEngineItem.getDatasetDescription());
			detailUrl += searchEngineItem.getDatasetCode();
			iconUrl += searchEngineItem.getDatasetCode();

		}

		metadata.setDetailUrl(detailUrl);
		metadata.setIcon(iconUrl);

		if (searchEngineItem.getDatasetCode() != null) {
			Dataset dataset = new Dataset();
			if (searchEngineItem.getIdDataset() != null && searchEngineItem.getIdDataset().size() > 0)
				dataset.setDatasetId(searchEngineItem.getIdDataset().get(0));
			dataset.setCode(searchEngineItem.getDatasetCode());
			dataset.setDatasetType(searchEngineItem.getDataseType());
			metadata.setDataset(dataset);
		}

		// Dcat
		if (searchEngineItem.isDcatReady()) {
			metadata.setDcatReady(1);

			DCat dcat = new DCat();
			dcat.setDcatCreatorName(searchEngineItem.getDcatCreatorName());
			dcat.setDcatCreatorType(searchEngineItem.getDcatCreatorType());
			dcat.setDcatCreatorId(searchEngineItem.getDcatCreatorId());
			dcat.setDcatRightsHolderName(searchEngineItem.getDcatRightsHolderName());
			dcat.setDcatRightsHolderType(searchEngineItem.getDcatRightsHolderType());
			dcat.setDcatRightsHolderId(searchEngineItem.getDcatRightsHolderId());
			dcat.setDcatNomeOrg(searchEngineItem.getDcatNomeOrg());
			dcat.setDcatEmailOrg(searchEngineItem.getDcatEmailOrg());
			metadata.setDcat(dcat);
		}

		if (searchEngineItem.isOpendata()) {
			Opendata opendata = new Opendata();
			opendata.setDataUpdateDate(searchEngineItem.getOpendataUpdateDateLong());
			opendata.setMetadaUpdateDate(searchEngineItem.getOpendataMetaUpdateDateDate());
			opendata.setLanguage(searchEngineItem.getOpendataLanguage());
			metadata.setOpendata(opendata);

		}

		if (searchEngineItem.getJsonFields() != null) {
			// SearchEngineJsonFields searchEngineFields =
			// gson.fromJson(searchEngineItem.getJsonFields(),
			// SearchEngineJsonFields.class);
			if (searchEngineItem.getJsonFields().startsWith("[")) {
				SearchEngineJsonField[] searchEngineFields = gson.fromJson(searchEngineItem.getJsonFields(), SearchEngineJsonField[].class);
				if (searchEngineFields != null && searchEngineFields.length > 0) {
					for (SearchEngineJsonField jsonField : searchEngineFields) {
						metadata.addComponent(Component.createFromSearchEngineJsonField(jsonField));
					}
				}
			} else {
				SearchEngineJsonField searchEngineFields = gson.fromJson(searchEngineItem.getJsonFields(), SearchEngineJsonField.class);
				if (searchEngineFields != null && searchEngineFields.getElement() != null) {
					for (SearchEngineJsonFieldElement jsonFieldElement : searchEngineFields.getElement()) {
						metadata.addComponent(Component.createFromSearchEngineJsonFieldElement(jsonFieldElement));
					}
				}

			}
		}

		// String detailUrl = Config.getInstance().getMetadataapiBaseUrl() +
		// "detail/" + searchEngineItem.getExtraCodiceTenant() + "/";

		// if (searchEngineItem.getName().endsWith("_odata")) {
		// metadata.setType(METADATA_TYPE_DATASET);
		// metadata.setIcon(Config.getInstance().getMetadataapiBaseUrl() +
		// "resource/icon/" + searchEngineItem.getExtraCodiceTenant() + "/" +
		// metadata.getCode());
		// Dataset dataset = new Dataset();
		//
		// String[] nameSplitted = searchEngineItem.getName().split("_");
		// String datasetId = nameSplitted[nameSplitted.length - 2];
		// dataset.setDatasetId(new Long(datasetId));
		// metadata.setDataset(dataset);
		//
		// detailUrl += metadata.getCode();
		//
		// } else if (searchEngineItem.getName().endsWith("_stream")) {
		// metadata.setType(METADATA_TYPE_STREAM);
		// metadata.setCode(searchEngineItem.getName().substring(0,
		// searchEngineItem.getName().length() - 7));
		// metadata.setName(searchEngineItem.getExtraCodiceStream() + " " +
		// searchEngineItem.getExtraVirtualEntityCode());
		// metadata.setIcon(Config.getInstance().getMetadataapiBaseUrl() +
		// "resource/icon/" + searchEngineItem.getExtraCodiceTenant() + "/" +
		// searchEngineItem.getExtraVirtualEntityCode() + "/"
		// + searchEngineItem.getExtraCodiceStream());
		//
		// detailUrl += searchEngineItem.getExtraVirtualEntityCode() + "/" +
		// searchEngineItem.getExtraCodiceStream();
		//
		// }
		// metadata.setDetailUrl(detailUrl);
		//
		// if (!Util.isEmpty(searchEngineItem.getExtraVirtualEntityCode())) {
		// Smartobject smartobject = new Smartobject();
		// smartobject.setCode(searchEngineItem.getExtraVirtualEntityCode());
		// smartobject.setName(searchEngineItem.getExtraVirtualEntityName());
		// smartobject.setDescription(searchEngineItem.getExtraVirtualEntityDescription());
		// smartobject.setLatitude(searchEngineItem.getExtraLatitude());
		// smartobject.setLongitude(searchEngineItem.getExtraLongitude());
		// Stream stream = metadata.getStream() == null ? new Stream() :
		// metadata.getStream();
		// stream.setSmartobject(smartobject);
		// metadata.setStream(stream);
		// }
		//
		// // private String provider;
		// // private String rates;
		// // private String endpoint;
		// // private String thumbnailurl;
		// // private String visibleRoles;
		// // private String docName;
		// // private String docSummary;
		// // private String docSourceURL;
		// // private String docFilePath;
		// // private String extraApi;
		//
		// // private String extraVirtualEntityName;
		// // private String extraVirtualEntityDescription;
		// // private String extraVirtualEntityCode;
		// // private String extraApiDescription;
		// // private String extraDomain;
		// // private String tags;

		return metadata;
	}

	public Boolean getIsOpendata() {
		return isOpendata;
	}

	public void setIsOpendata(Boolean isOpendata) {
		this.isOpendata = isOpendata;
	}

}
