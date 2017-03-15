package org.csi.yucca.dataservice.metadataapi.model.output.v01;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.csi.yucca.dataservice.metadataapi.model.ckan.ExtraV2;
import org.csi.yucca.dataservice.metadataapi.model.ckan.Resource;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.csi.yucca.dataservice.metadataapi.util.Util;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Metadata {
	public static final String METADATA_TYPE_STREAM = "stream";
	public static final String METADATA_TYPE_DATASET = "dataset";

	private String name; // cerco logica usata per lo store
	private String code; // codice da usare per il dettaglio
	private String version;
	private String description; // sempre dallo store se stream-> nome stream
	private String type;
	private String domain;
	private String codsubdomain;
	private String requestorname;
	private String requestorsurname;
	private String requestoremail;
	private String tenantCode;
	private String tenantName;
	private List<String> tagCodes;
	private List<String> tags;
	private String icon;
	private String visibility;
	private List<String> sharedtenants; // FIXME non mettere
	private Boolean isopendata;
	private String author;
	private String language;
	private Date registrationDate;
	private Date datalastupdate; // FIXME non mettere
	private String externalreference;
	private Boolean ispublished; // FIXME non mettere
	private String license;
	private String disclaimer;
	private String copyright;
	private Double latitude;
	private Double longitude;
	private String fps;

	private Stream stream;
	private Dataset dataset;
	private Opendata opendata;

	private boolean dcatReady;
	private String dcatCreatorName;
	private String dcatCreatorType;
	private String dcatCreatorId;
	private String dcatRightsHolderName;
	private String dcatRightsHolderType;
	private String dcatRightsHolderId;
	private String dcatNomeOrg;
	private String dcatEmailOrg;

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

	public String getCodsubdomain() {
		return codsubdomain;
	}

	public void setCodsubdomain(String codsubdomain) {
		this.codsubdomain = codsubdomain;
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

	public List<String> getSharedtenants() {
		return sharedtenants;
	}

	public void setSharedtenants(List<String> sharedtenants) {
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

	public static Metadata createFromStoreSearchItemv2(
			org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.Metadata metadatav2,
			String lang) {
		// TODO Auto-generated method stub
		return null;
	}

	

	private void addTagCode(String code) {
		if (tagCodes == null)
			tagCodes = new LinkedList<String>();

		tagCodes.add(code);

	}

	private static String cleadMainCode(String mainCode) {
		if (mainCode.endsWith("_odata"))
			mainCode = mainCode.substring(0, mainCode.lastIndexOf("_odata"));
		else if (mainCode.endsWith("_stream"))
			mainCode = mainCode.substring(0, mainCode.lastIndexOf("_stream"));

		return mainCode;

	}

	
	
	

	public String getCkanPackageId() {
		return "smartdatanet.it_" + getCode();
	}

	public static String getApiNameFromCkanPackageId(String packageId) {
		return packageId.substring(packageId.indexOf("_") + 1);

	}

	@Deprecated
	public String toCkan() {
		org.csi.yucca.dataservice.metadataapi.model.ckan.Dataset ckanDataset = new org.csi.yucca.dataservice.metadataapi.model.ckan.Dataset();
		ckanDataset.setId(getCkanPackageId());
		ckanDataset.setName(getCkanPackageId());
		ckanDataset.setTitle(getName());
		ckanDataset.setNotes(getDescription());
		ckanDataset.setVersion(getVersion());

		String metadataUrl = Config.getInstance().getUserportalBaseUrl() + "#/dataexplorer/dataset/" + getTenantCode() + "/" + getCode();

		ckanDataset.setUrl(metadataUrl);

		Resource resourceApiOdata = new Resource();
		resourceApiOdata.setDescription("Api Odata");
		resourceApiOdata.setFormat("ODATA");
		String exposedApiBaseUrl = Config.getInstance().getExposedApiBaseUrl();
		String apiOdataUrl = exposedApiBaseUrl + getCode();
		resourceApiOdata.setUrl(apiOdataUrl);
		ckanDataset.addResource(resourceApiOdata);

		Resource resourceDownload = new Resource();
		resourceDownload.setDescription("Csv download url");
		resourceDownload.setFormat("CSV");

		String downloadCsvUrl = exposedApiBaseUrl + getCode() + "/download/" + getDataset().getDatasetId() + "/";

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

		extras.setDcatCreatorName(getDcatCreatorName());
		extras.setDcatCreatorType(getDcatCreatorType());
		extras.setDcatCreatorId(getDcatCreatorId());
		extras.setDcatRightsHolderName(getDcatRightsHolderName());
		extras.setDcatRightsHolderType(getDcatRightsHolderType());
		extras.setDcatRightsHolderId(getDcatRightsHolderId());
		extras.setDcatNomeOrg(getDcatNomeOrg());
		extras.setDcatEmailOrg(getDcatEmailOrg());

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

	public static void main(String[] args) {
		String packageId = "smartdatanet.it_ds_Rumore_480";
		System.out.println("1 " + (packageId));
		System.out.println("2 " + getApiNameFromCkanPackageId(packageId));

		String mainCode = "sandbox.internal_33_odata";
		mainCode = mainCode.substring(0, mainCode.lastIndexOf("_odata"));
		System.out.println("m " + mainCode);

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

	public boolean getDcatReady() {
		return dcatReady;
	}

	public void setDcatReady(boolean dcatReady) {
		this.dcatReady = dcatReady;
	}

	public String getDcatCreatorName() {
		return dcatCreatorName;
	}

	public void setDcatCreatorName(String dcatCreatorName) {
		this.dcatCreatorName = dcatCreatorName;
	}

	public String getDcatCreatorType() {
		return dcatCreatorType;
	}

	public void setDcatCreatorType(String dcatCreatorType) {
		this.dcatCreatorType = dcatCreatorType;
	}

	public String getDcatCreatorId() {
		return dcatCreatorId;
	}

	public void setDcatCreatorId(String dcatCreatorId) {
		this.dcatCreatorId = dcatCreatorId;
	}

	public String getDcatRightsHolderName() {
		return dcatRightsHolderName;
	}

	public void setDcatRightsHolderName(String dcatRightsHolderName) {
		this.dcatRightsHolderName = dcatRightsHolderName;
	}

	public String getDcatRightsHolderType() {
		return dcatRightsHolderType;
	}

	public void setDcatRightsHolderType(String dcatRightsHolderType) {
		this.dcatRightsHolderType = dcatRightsHolderType;
	}

	public String getDcatRightsHolderId() {
		return dcatRightsHolderId;
	}

	public void setDcatRightsHolderId(String dcatRightsHolderId) {
		this.dcatRightsHolderId = dcatRightsHolderId;
	}

	public String getDcatNomeOrg() {
		return dcatNomeOrg;
	}

	public void setDcatNomeOrg(String dcatNomeOrg) {
		this.dcatNomeOrg = dcatNomeOrg;
	}

	public String getDcatEmailOrg() {
		return dcatEmailOrg;
	}

	public void setDcatEmailOrg(String dcatEmailOrg) {
		this.dcatEmailOrg = dcatEmailOrg;
	}

	public static String getMetadataTypeStream() {
		return METADATA_TYPE_STREAM;
	}

	public static String getMetadataTypeDataset() {
		return METADATA_TYPE_DATASET;
	}

}
