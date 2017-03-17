package org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.model.ckan.ExtraV2;
import org.csi.yucca.dataservice.metadataapi.model.ckan.Resource;
import org.csi.yucca.dataservice.metadataapi.model.output.v01.DatasetColumn;
import org.csi.yucca.dataservice.metadataapi.model.output.v01.StreamComponent;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.stream.Twitter;
import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineJsonField;
import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineJsonFieldElement;
import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineJsonSo;
import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineMetadata;
import org.csi.yucca.dataservice.metadataapi.service.CkanService;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.csi.yucca.dataservice.metadataapi.util.Constants;
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

	static Logger log = Logger.getLogger(CkanService.class);
	
	private String name;
	// private String code;
	private String version;
	private String description;
	private List<String> type;
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

	public List<String> getType() {
		return type;
	}

	public void setType(List<String> type) {
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
		return StringUtils.substringAfter(packageId,"_");

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
		if (getStream()!=null)
		{
			ckanDataset.setTitle(getName()+" - "+getTenantName());
			ckanDataset.setNotes(getDescription()+" - "+getTenantName()+" - "+getStream().getSmartobject().getDescription());
		}
		else
		{
			ckanDataset.setTitle(getName());
			ckanDataset.setNotes(getDescription()+" - "+getTenantName());
		}
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

		if (getType().contains("stream")) {
			downloadCsvUrl += "current";
		} else {
			downloadCsvUrl += "all";
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
			if (getOpendata().getDataUpdateDate() != null)
				extras.setPackage_modified(Util.formatDateCkan(new Date(getOpendata().getDataUpdateDate())));

		}
		if (getRegistrationDate() != null)
			extras.setPackage_created(Util.formatDateCkan(getRegistrationDate()));

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


		
		extras.setPackage_type("CSV");
		ckanDataset.setExtras(extras);
		return ckanDataset.toJson();

	}

	public static Metadata createFromSearchEngineItem(SearchEngineMetadata searchEngineItem, String lang) {
		Gson gson = JSonHelper.getInstance();

		Metadata metadata = new Metadata();

		metadata.setVersion(searchEngineItem.getVersion());
		metadata.setName(searchEngineItem.getName()); // FIXME sicuro?
//		metadata.setDomain(I18nDelegate.translate(searchEngineItem.getDomainCode(), lang,
//				("en".equals(lang) ? searchEngineItem.getDomainLangEN() : searchEngineItem.getDomainLangIT())));
//		metadata.setSubdomain(I18nDelegate.translate(searchEngineItem.getSubdomainCode(), lang,
//				("en".equals(lang) ? searchEngineItem.getSubdomainLangEN() : searchEngineItem.getSubdomainLangIT())));

		metadata.setDomain(
				("en".equals(lang) ? searchEngineItem.getDomainLangEN() : searchEngineItem.getDomainLangIT()));
		metadata.setSubdomain(
				("en".equals(lang) ? searchEngineItem.getSubdomainLangEN() : searchEngineItem.getSubdomainLangIT()));

		
		metadata.setDomainCode(searchEngineItem.getDomainCode());
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
//			metadata.setTags(I18nDelegate.translateMulti(metadata.getTagCodes(), lang));
			metadata.setTags(
					("en".equals(lang) ? searchEngineItem.getTagLangEN() : searchEngineItem.getTagLangIT()));
		}

		metadata.setType(searchEngineItem.getEntityType());
		metadata.setSubtype(metadata.getSubtype());

		String detailUrl = Config.getInstance().getMetadataapiBaseUrl() + "v02/detail/";
		String iconUrl = Config.getInstance().getMetadataapiBaseUrl() + "resource/icon/" + searchEngineItem.getTenantCode() + "/";

		if (searchEngineItem.getEntityType().contains("stream")) {
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
//			if (searchEngineItem.getIdDataset() != null && searchEngineItem.getIdDataset().size() > 0)
//				dataset.setDatasetId(searchEngineItem.getIdDataset().get(0));
			// Id dataset calculated from datasetCode
			
			String idDataset = StringUtils.substringAfterLast(searchEngineItem.getDatasetCode(), "_");
			try {
				dataset.setDatasetId(Long.parseLong(idDataset));
			} catch (NumberFormatException e) {
				log.error("DatasetCode not ending with a long ["+searchEngineItem.getDatasetCode()+"]",e);
			}
			
			dataset.setCode(searchEngineItem.getDatasetCode());
			dataset.setDatasetType(searchEngineItem.getDataseType());
			dataset.setDatasetSubtype(searchEngineItem.getDatasetSubtype());
			metadata.setDataset(dataset);
		}

		// Dcat
		if (searchEngineItem.getDcatReady()) {
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

		if (searchEngineItem.getIsOpendata()) {
			Opendata opendata = new Opendata();
			opendata.setDataUpdateDate(searchEngineItem.getOpendataUpdateDateLong());
			opendata.setMetadaUpdateDate(searchEngineItem.getOpendataMetaUpdateDateDate());
			opendata.setLanguage(searchEngineItem.getOpendataLanguage());
			opendata.setOpendata(true);
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

	

		return metadata;
	}

	public Boolean getIsOpendata() {
		return isOpendata;
	}

	public void setIsOpendata(Boolean isOpendata) {
		this.isOpendata = isOpendata;
	}

	public List<org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata> toV01(String outputFormatV01) {

		
		if (Constants.OUTPUT_FORMAT_V01_STREAM.equals(outputFormatV01) || 
			Constants.OUTPUT_FORMAT_V01_DATASET.equals(outputFormatV01) )
		{
			org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata metadatav1 = new org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata();
			if (getStream()!=null)
			{
				metadatav1.setType(METADATA_TYPE_STREAM);

				if (Constants.OUTPUT_FORMAT_V01_STREAM.equals(outputFormatV01))
					metadatav1.setCode(getTenantCode()+"."+getStream().getSmartobject().getCode()+"_"+getStream().getCode());
				else
					metadatav1.setCode(getDataset().getCode());
				
				metadatav1.setVersion("" + getVersion());
				metadatav1.setName(getStream().getCode() + " - " + getStream().getSmartobject().getCode());
				metadatav1.setDescription(getStream().getCode() + " - " +getStream().getSmartobject().getCode() + " - " + getStream().getSmartobject().getDescription());
				metadatav1.setIcon(Config.getInstance().getMetadataapiBaseUrl() + "resource/icon/" + this.tenantCode + "/" + this.getStream().getSmartobject().getCode() + "/"
						+ this.getStream().getCode());

				metadatav1.setFps(getFps());
				metadatav1.setRegistrationDate(getRegistrationDate());
				
				org.csi.yucca.dataservice.metadataapi.model.output.v01.Smartobject smartobjectv1 = 
						new org.csi.yucca.dataservice.metadataapi.model.output.v01.Smartobject();
				smartobjectv1.setCode(getStream().getSmartobject().getCode());
				smartobjectv1.setName(getStream().getSmartobject().getName());
				smartobjectv1.setDescription(getStream().getSmartobject().getDescription());
				smartobjectv1.setCategory(getStream().getSmartobject().getCategory());
				smartobjectv1.setType(getStream().getSmartobject().getType());
				if (getStream().getSmartobject().getType().equals(Smartobject.SMARTOBJECT_TYPE_TWITTER)) {
					smartobjectv1.setTwtCount(getStream().getTwitter().getTwtCount());
					smartobjectv1.setTwtGeolocLat(getStream().getTwitter().getTwtGeolocLat());
					smartobjectv1.setTwtGeolocLon(getStream().getTwitter().getTwtGeolocLon());
					smartobjectv1.setTwtGeolocRadius(getStream().getTwitter().getTwtGeolocRadius());
					smartobjectv1.setTwtRatePercentage(getStream().getTwitter().getTwtRatePercentage());
					smartobjectv1.setTwtMaxStreams(getStream().getTwitter().getTwtMaxStreams());
					smartobjectv1.setTwtQuery(getStream().getTwitter().getTwtQuery());
					smartobjectv1.setTwtLang(getStream().getTwitter().getTwtLang());
				}

				smartobjectv1.setAltitude(getStream().getSmartobject().getAltitude());
				smartobjectv1.setBuilding(getStream().getSmartobject().getBuilding());
				smartobjectv1.setFloor(Util.nvlt(getStream().getSmartobject().getFloor()));
				smartobjectv1.setLatitude(getStream().getSmartobject().getLatitude());
				smartobjectv1.setLongitude(getStream().getSmartobject().getLongitude());
				smartobjectv1.setRoom(Util.nvlt(getStream().getSmartobject().getRoom()));
			
				org.csi.yucca.dataservice.metadataapi.model.output.v01.Stream streamv1 = 
						new org.csi.yucca.dataservice.metadataapi.model.output.v01.Stream();
				streamv1.setCode(getStream().getCode());
				streamv1.setName(getTenantName()); // ?? FIXME
				streamv1.setFps(getStream().getFps());
				streamv1.setSavedata(getStream().getSavedata());
				streamv1.setSmartobject(smartobjectv1);

				if (getComponents()!= null && getComponents().size()>0)
				{
					StreamComponent[] componentsv1 = new StreamComponent[getComponents().size()];
					int counter = 0;
					for (Component component : getComponents()) {
						StreamComponent compv1 = new StreamComponent();
						compv1.setDatatype(component.getDatatype());
						compv1.setMeasureunit(component.getMeasureunit());
						compv1.setName(component.getName());
						compv1.setPhenomenon(component.getPhenomenon());
						compv1.setTolerance(component.getTolerance());
						componentsv1[counter] = compv1;
						counter++;
					}
					streamv1.setComponents(componentsv1);
				}
				
				metadatav1.setStream(streamv1);

				
			} else {
				
				metadatav1.setType(METADATA_TYPE_DATASET);
				metadatav1.setCode(getDataset().getCode());
				metadatav1.setVersion("" + getVersion());
				metadatav1.setName(getName());
				metadatav1.setDescription(getDescription());
				metadatav1.setIcon(Config.getInstance().getMetadataapiBaseUrl() + "resource/icon/" + getTenantCode() + "/" + getDataset().getCode());
				metadatav1.setFps(getFps());
				
			}

			metadatav1.setDomain(getDomain());
			metadatav1.setTenantCode(getTenantCode());
			metadatav1.setTenantName(getTenantName());
			metadatav1.setTagCodes(getTagCodes());
			metadatav1.setTags(getTags());
			metadatav1.setVisibility(getVisibility());
			metadatav1.setLicense(getLicense());
			metadatav1.setDisclaimer(getDisclaimer());
			metadatav1.setCopyright(getCopyright());


			
			if (getDataset()!=null) {
				org.csi.yucca.dataservice.metadataapi.model.output.v01.Dataset datasetv1 = 
						new org.csi.yucca.dataservice.metadataapi.model.output.v01.Dataset();

				datasetv1.setDatasetType(getDataset().getDatasetSubtype());
				datasetv1.setDatasetId(getDataset().getDatasetId());
				datasetv1.setImportFileType(getDataset().getImportFileType());

				if (getComponents()!= null && getComponents().size()>0)
				{
					DatasetColumn[] columnsv1 = new DatasetColumn[getComponents().size()];
					int counter = 0;
					for (Component component : getComponents()) {
						DatasetColumn column = new DatasetColumn();
						column.setAlias(component.getAlias());
						column.setName(component.getName());
						column.setIskey(false);
						column.setDatatype(component.getDatatype());
						column.setMeasureunit(component.getMeasureunit());
						columnsv1[counter] = column;
						counter++;
					}
					datasetv1.setColumns(columnsv1);
				}
				metadatav1.setDataset(datasetv1);
			}
			
			if (getIsopendata()!=null) {
				metadatav1.setIsopendata(getIsopendata());

				org.csi.yucca.dataservice.metadataapi.model.output.v01.Opendata opendatav1 = 
						new org.csi.yucca.dataservice.metadataapi.model.output.v01.Opendata();
				opendatav1.setAuthor(getOpendata().getAuthor());
				opendatav1.setDataUpdateDate(getOpendata().getDataUpdateDate());
				opendatav1.setLanguage(getOpendata().getLanguage());
				opendatav1.setMetadaUpdateDate(getOpendata().getMetadaUpdateDate());
				metadatav1.setOpendata(opendatav1);
			}
			
			if (getDcat()!=null)  {
				metadatav1.setDcatReady(1);
				metadatav1.setDcatCreatorName(getDcat().getDcatCreatorName());
				metadatav1.setDcatCreatorType(getDcat().getDcatCreatorType());
				metadatav1.setDcatCreatorId(getDcat().getDcatCreatorId());
				metadatav1.setDcatRightsHolderName(getDcat().getDcatRightsHolderName());
				metadatav1.setDcatRightsHolderType(getDcat().getDcatRightsHolderType());
				metadatav1.setDcatRightsHolderId(getDcat().getDcatRightsHolderId());
				metadatav1.setDcatNomeOrg(getDcat().getDcatNomeOrg());
				metadatav1.setDcatEmailOrg(getDcat().getDcatEmailOrg());
			} else {
				metadatav1.setDcatReady(0);
			}
		
			return Arrays.asList(metadatav1);
		}
		else if (Constants.OUTPUT_FORMAT_V01_LIST.equals(outputFormatV01))
		{
			List<org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata> metadatas = new ArrayList();
			if (getStream()!=null)
			{
				org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata 
					metadatav1StreamSummary = getMetadatav1StreamSummaryFromObject(true);
				
				if (getDataset() != null) {
					org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata 
					metadatav1DatasetStreamSummary = getMetadatav1StreamSummaryFromObject(false);
					
					metadatas.add(metadatav1DatasetStreamSummary);
					
				}
				metadatas.add(metadatav1StreamSummary);
			
			}
			else {
				metadatas.add(getMetadatav1DatasetSummaryFromObject());
			}
			
			return  metadatas;
		}
		else
			return null;
	}

	private org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata getMetadatav1StreamSummaryFromObject(boolean codeForStream) {
		org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata metadatav1StreamSummary = 
				new org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata();
		metadatav1StreamSummary.setType(METADATA_TYPE_STREAM);
		if (codeForStream)
			metadatav1StreamSummary.setCode(getTenantCode()+"."+getStream().getSmartobject().getCode()+"_"+getStream().getCode());
		else
			metadatav1StreamSummary.setCode(getDataset().getCode());

		metadatav1StreamSummary.setVersion("" + getVersion());
		metadatav1StreamSummary.setName(getStream().getCode() + " - " + getStream().getSmartobject().getCode());
		metadatav1StreamSummary.setDescription(getStream().getCode() + " - " +getStream().getSmartobject().getCode() + " - " + getStream().getSmartobject().getDescription());
		metadatav1StreamSummary.setFps(getFps());

		metadatav1StreamSummary.setDomain(getDomain());
		metadatav1StreamSummary.setTenantCode(getTenantCode());
		metadatav1StreamSummary.setTenantName(getTenantName());
		metadatav1StreamSummary.setTagCodes(getTagCodes());
		metadatav1StreamSummary.setTags(getTags());
		metadatav1StreamSummary.setIcon(Config.getInstance().getMetadataapiBaseUrl() + "resource/icon/" + this.tenantCode + "/" + this.getStream().getSmartobject().getCode() + "/"
				+ this.getStream().getCode());
		metadatav1StreamSummary.setVisibility(getVisibility());
		metadatav1StreamSummary.setLicense(getLicense());
		metadatav1StreamSummary.setDisclaimer(getDisclaimer());
		metadatav1StreamSummary.setCopyright(getCopyright());

		org.csi.yucca.dataservice.metadataapi.model.output.v01.Stream streamv1 = 
				new org.csi.yucca.dataservice.metadataapi.model.output.v01.Stream();
		org.csi.yucca.dataservice.metadataapi.model.output.v01.Smartobject smartobjectv1 = 
				new org.csi.yucca.dataservice.metadataapi.model.output.v01.Smartobject();
		smartobjectv1.setCode(getStream().getSmartobject().getCode());
		smartobjectv1.setName(getStream().getSmartobject().getName());
		smartobjectv1.setDescription(getStream().getSmartobject().getDescription());
		streamv1.setSmartobject(smartobjectv1);
		metadatav1StreamSummary.setStream(streamv1);
		
		if (getIsopendata()!=null) {
			metadatav1StreamSummary.setIsopendata(getIsopendata());

			org.csi.yucca.dataservice.metadataapi.model.output.v01.Opendata opendatav1 = 
					new org.csi.yucca.dataservice.metadataapi.model.output.v01.Opendata();
			opendatav1.setAuthor(getOpendata().getAuthor());
			opendatav1.setDataUpdateDate(getOpendata().getDataUpdateDate());
			opendatav1.setLanguage(getOpendata().getLanguage());
			opendatav1.setMetadaUpdateDate(getOpendata().getMetadaUpdateDate());
			metadatav1StreamSummary.setOpendata(opendatav1);
		}
		
		if (getDcat() != null)  {
			metadatav1StreamSummary.setDcatReady(1);
			metadatav1StreamSummary.setDcatCreatorName(getDcat().getDcatCreatorName());
			metadatav1StreamSummary.setDcatCreatorType(getDcat().getDcatCreatorType());
			metadatav1StreamSummary.setDcatCreatorId(getDcat().getDcatCreatorId());
			metadatav1StreamSummary.setDcatRightsHolderName(getDcat().getDcatRightsHolderName());
			metadatav1StreamSummary.setDcatRightsHolderType(getDcat().getDcatRightsHolderType());
			metadatav1StreamSummary.setDcatRightsHolderId(getDcat().getDcatRightsHolderId());
			metadatav1StreamSummary.setDcatNomeOrg(getDcat().getDcatNomeOrg());
			metadatav1StreamSummary.setDcatEmailOrg(getDcat().getDcatEmailOrg());
		}
		else {			
			metadatav1StreamSummary.setDcatReady(0);
		}
		
		if (!codeForStream){
			org.csi.yucca.dataservice.metadataapi.model.output.v01.Dataset datasetv1 = 
					new org.csi.yucca.dataservice.metadataapi.model.output.v01.Dataset();
			datasetv1.setDatasetId(getDataset().getDatasetId());
			metadatav1StreamSummary.setDataset(datasetv1);

		}
		
		metadatav1StreamSummary.setDetailUrl(Config.getInstance().getMetadataapiBaseUrl() + "detail/" + getTenantCode() + "/" + metadatav1StreamSummary.getCode() );
		
		return metadatav1StreamSummary;
	}
	
	private org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata getMetadatav1DatasetSummaryFromObject() {
		org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata metadatav1DatasetSummary = 
				new org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata();
		metadatav1DatasetSummary.setType(METADATA_TYPE_DATASET);
		metadatav1DatasetSummary.setCode(getDataset().getCode());

		metadatav1DatasetSummary.setVersion("" + getVersion());
		metadatav1DatasetSummary.setName(getName());
		metadatav1DatasetSummary.setDescription(getDescription());
		metadatav1DatasetSummary.setFps(getFps());

		metadatav1DatasetSummary.setDomain(getDomain());
		metadatav1DatasetSummary.setTenantCode(getTenantCode());
		metadatav1DatasetSummary.setTenantName(getTenantName());
		metadatav1DatasetSummary.setTagCodes(getTagCodes());
		metadatav1DatasetSummary.setTags(getTags());
		metadatav1DatasetSummary.setIcon(Config.getInstance().getMetadataapiBaseUrl() + "resource/icon/" + getTenantCode() + "/" + getDataset().getCode());
		metadatav1DatasetSummary.setVisibility(getVisibility());
		metadatav1DatasetSummary.setLicense(getLicense());
		metadatav1DatasetSummary.setDisclaimer(getDisclaimer());
		metadatav1DatasetSummary.setCopyright(getCopyright());


		
		if (getIsopendata()!=null) {
			metadatav1DatasetSummary.setIsopendata(getIsopendata());

			org.csi.yucca.dataservice.metadataapi.model.output.v01.Opendata opendatav1 = 
					new org.csi.yucca.dataservice.metadataapi.model.output.v01.Opendata();
			opendatav1.setAuthor(getOpendata().getAuthor());
			opendatav1.setDataUpdateDate(getOpendata().getDataUpdateDate());
			opendatav1.setLanguage(getOpendata().getLanguage());
			opendatav1.setMetadaUpdateDate(getOpendata().getMetadaUpdateDate());
			metadatav1DatasetSummary.setOpendata(opendatav1);
		}
		
		if (getDcat() != null)  {
			metadatav1DatasetSummary.setDcatReady(1);
			metadatav1DatasetSummary.setDcatCreatorName(getDcat().getDcatCreatorName());
			metadatav1DatasetSummary.setDcatCreatorType(getDcat().getDcatCreatorType());
			metadatav1DatasetSummary.setDcatCreatorId(getDcat().getDcatCreatorId());
			metadatav1DatasetSummary.setDcatRightsHolderName(getDcat().getDcatRightsHolderName());
			metadatav1DatasetSummary.setDcatRightsHolderType(getDcat().getDcatRightsHolderType());
			metadatav1DatasetSummary.setDcatRightsHolderId(getDcat().getDcatRightsHolderId());
			metadatav1DatasetSummary.setDcatNomeOrg(getDcat().getDcatNomeOrg());
			metadatav1DatasetSummary.setDcatEmailOrg(getDcat().getDcatEmailOrg());
		} else {
			metadatav1DatasetSummary.setDcatReady(0);
		}
		
		org.csi.yucca.dataservice.metadataapi.model.output.v01.Dataset datasetv1 = 
				new org.csi.yucca.dataservice.metadataapi.model.output.v01.Dataset();
		datasetv1.setDatasetId(getDataset().getDatasetId());
		metadatav1DatasetSummary.setDataset(datasetv1);

		
		metadatav1DatasetSummary.setDetailUrl(Config.getInstance().getMetadataapiBaseUrl() + "detail/" + getTenantCode() + "/" + metadatav1DatasetSummary.getCode() );
		
		return metadatav1DatasetSummary;
	}

}
