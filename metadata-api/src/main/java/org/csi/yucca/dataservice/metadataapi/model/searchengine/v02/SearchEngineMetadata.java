package org.csi.yucca.dataservice.metadataapi.model.searchengine.v02;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class SearchEngineMetadata {

	
	static Logger log = Logger.getLogger(SearchEngineMetadata.class);

	private String id;
	private List<String> entityType;
//	private List<Long> idDataset;
	private String name;
	private String visibility;
	private String copyright;
	private String organizationCode;
	private String organizationDescription;
	private String domainCode;
	private String domainLangIT;
	private String domainLangEN;
	private String subdomainCode;
	private String subdomainLangIT;
	private String subdomainLangEN;
	private String licenseCode;
	private String licenceDescription;
	private String tenantCode;
	private List<String> tenantsCode;
	private String tenantName;
	private String tenantDescription;
	private List<String> tagCode;
	private List<String> tagLangIT;
	private List<String> tagLangEN;
	private String dcatDataUpdate;
	private String dcatNomeOrg;
	private String dcatEmailOrg;
	private String dcatCreatorName;
	private String dcatCreatorType;
	private String dcatCreatorId;
	private String dcatRightsHolderName;
	private String dcatRightsHolderType;
	private String dcatRightsHolderId;
	private Boolean dcatReady;
	private String datasetCode;
	private String datasetDescription;
	private String version;
	private String dataseType;
	private String datasetSubtype;
	private String streamCode;
	private String twtQuery;
	private String twtGeolocLat;
	private String twtGeolocLon;
	private String twtGeolocRadius;
	private String twtGeolocUnit;
	private String twtLang;
	private String twtLocale;
	private String twtCount;
	private String twtResultType;
	private String twtUntil;
	private String twtRatePercentage;
	private String twtLastSearchId;
	private String soCode;
	private String soName;
	private String soDescription;
	private String jsonFields;
	private String jsonSo;
	private String lat;
	private String lon;
	private List<String> sdpComponentsName;
	private List<String> phenomenon;
	private String isCurrent;
	private Boolean isOpendata;
	private String opendataAuthor;
	private String opendataUpdateDate;
	private String opendataMetaUpdateDate;
	private String opendataLanguage;
	private String opendataUpdateFrequency;
	private List<String> soFps;
	private List<String> soCategory;
	private String registrationDate;
	private String externalReference;
	private String soType;
	
	public SearchEngineMetadata() {
		super();
	}


	public static SearchEngineMetadata fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, SearchEngineMetadata.class);
	}

	
//	public static SearchEngineMetadata fromSolrDocument(SolrDocument curSolrDoc) {
//		Gson gson = JSonHelper.getInstance();
//		return gson.fromJson(JSONUtil.toJSON(curSolrDoc),SearchEngineMetadata.class);
//		
//	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getEntityType() {
		return entityType;
	}

	public void setEntityType(List<String> entityType) {
		this.entityType = entityType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
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

	public String getDomainCode() {
		return domainCode;
	}

	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}

	public String getDomainLangIT() {
		return domainLangIT;
	}

	public void setDomainLangIT(String domainLangIT) {
		this.domainLangIT = domainLangIT;
	}

	public String getDomainLangEN() {
		return domainLangEN;
	}

	public void setDomainLangEN(String domainLangEN) {
		this.domainLangEN = domainLangEN;
	}

	public String getSubdomainCode() {
		return subdomainCode;
	}

	public void setSubdomainCode(String subdomainCode) {
		this.subdomainCode = subdomainCode;
	}

	public String getSubdomainLangIT() {
		return subdomainLangIT;
	}

	public void setSubdomainLangIT(String subdomainLangIT) {
		this.subdomainLangIT = subdomainLangIT;
	}

	public String getSubdomainLangEN() {
		return subdomainLangEN;
	}

	public void setSubdomainLangEN(String subdomainLangEN) {
		this.subdomainLangEN = subdomainLangEN;
	}

	public String getLicenseCode() {
		return licenseCode;
	}

	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}

	public String getLicenceDescription() {
		return licenceDescription;
	}

	public void setLicenceDescription(String licenceDescription) {
		this.licenceDescription = licenceDescription;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	public List<String> getTenantsCode() {
		return tenantsCode;
	}

	public void setTenantsCode(List<String> tenantsCode) {
		this.tenantsCode = tenantsCode;
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

	public List<String> getTagCode() {
		return tagCode;
	}

	public void setTagCode(List<String> tagCode) {
		this.tagCode = tagCode;
	}

	public List<String> getTagLangIT() {
		return tagLangIT;
	}

	public void setTagLangIT(List<String> tagLangIT) {
		this.tagLangIT = tagLangIT;
	}

	public List<String> getTagLangEN() {
		return tagLangEN;
	}

	public void setTagLangEN(List<String> tagLangEN) {
		this.tagLangEN = tagLangEN;
	}

	public String getDcatDataUpdate() {
		return dcatDataUpdate;
	}

	public void setDcatDataUpdate(String dcatDataUpdate) {
		this.dcatDataUpdate = dcatDataUpdate;
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


	public String getDatasetCode() {
		return datasetCode;
	}

	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
	}

	public String getDatasetDescription() {
		return datasetDescription;
	}

	public void setDatasetDescription(String datasetDescription) {
		this.datasetDescription = datasetDescription;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDataseType() {
		return dataseType;
	}

	public void setDataseType(String dataseType) {
		this.dataseType = dataseType;
	}

	public String getDatasetSubtype() {
		return datasetSubtype;
	}

	public void setDatasetSubtype(String datasetSubtype) {
		this.datasetSubtype = datasetSubtype;
	}


	public String getStreamCode() {
		return streamCode;
	}

	public void setStreamCode(String streamCode) {
		this.streamCode = streamCode;
	}

	public String getTwtQuery() {
		return twtQuery;
	}

	public void setTwtQuery(String twtQuery) {
		this.twtQuery = twtQuery;
	}

	public String getTwtGeolocLat() {
		return twtGeolocLat;
	}

	public void setTwtGeolocLat(String twtGeolocLat) {
		this.twtGeolocLat = twtGeolocLat;
	}

	public String getTwtGeolocLon() {
		return twtGeolocLon;
	}

	public void setTwtGeolocLon(String twtGeolocLon) {
		this.twtGeolocLon = twtGeolocLon;
	}

	public String getTwtGeolocRadius() {
		return twtGeolocRadius;
	}

	public void setTwtGeolocRadius(String twtGeolocRadius) {
		this.twtGeolocRadius = twtGeolocRadius;
	}

	public String getTwtGeolocUnit() {
		return twtGeolocUnit;
	}

	public void setTwtGeolocUnit(String twtGeolocUnit) {
		this.twtGeolocUnit = twtGeolocUnit;
	}

	public String getTwtLang() {
		return twtLang;
	}

	public void setTwtLang(String twtLang) {
		this.twtLang = twtLang;
	}

	public String getTwtLocale() {
		return twtLocale;
	}

	public void setTwtLocale(String twtLocale) {
		this.twtLocale = twtLocale;
	}

	public String getTwtCount() {
		return twtCount;
	}

	public void setTwtCount(String twtCount) {
		this.twtCount = twtCount;
	}

	public String getTwtResultType() {
		return twtResultType;
	}

	public void setTwtResultType(String twtResultType) {
		this.twtResultType = twtResultType;
	}

	public String getTwtUntil() {
		return twtUntil;
	}

	public void setTwtUntil(String twtUntil) {
		this.twtUntil = twtUntil;
	}

	public String getTwtRatePercentage() {
		return twtRatePercentage;
	}

	public void setTwtRatePercentage(String twtRatePercentage) {
		this.twtRatePercentage = twtRatePercentage;
	}

	public String getTwtLastSearchId() {
		return twtLastSearchId;
	}

	public void setTwtLastSearchId(String twtLastSearchId) {
		this.twtLastSearchId = twtLastSearchId;
	}

	public String getSoCode() {
		return soCode;
	}

	public void setSoCode(String soCode) {
		this.soCode = soCode;
	}

	public String getSoName() {
		return soName;
	}

	public void setSoName(String soName) {
		this.soName = soName;
	}

	public String getSoDescription() {
		return soDescription;
	}

	public void setSoDescription(String soDescription) {
		this.soDescription = soDescription;
	}

	public String getJsonFields() {
		return jsonFields;
	}

	public void setJsonFields(String jsonFields) {
		this.jsonFields = jsonFields;
	}

	public String getJsonSo() {
		return jsonSo;
	}

	public void setJsonSo(String jsonSo) {
		this.jsonSo = jsonSo;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public List<String> getSdpComponentsName() {
		return sdpComponentsName;
	}

	public void setSdpComponentsName(List<String> sdpComponentsName) {
		this.sdpComponentsName = sdpComponentsName;
	}

	public List<String> getPhenomenon() {
		return phenomenon;
	}

	public void setPhenomenon(List<String> phenomenon) {
		this.phenomenon = phenomenon;
	}

	public Double getLatDouble() {
		Double ret = null;
		if (getLat() != null) {
			try {
				ret = new Double(getLat());
			} catch (Exception e) {
				log.error("ERROR Parsing getLatDouble"+getLat(),e);
			}
		}
		return ret;
	}

	public Double getLonDouble() {
		Double ret = null;
		if (getLon() != null) {
			try {
				ret = new Double(getLon());
			} catch (Exception e) {
				log.error("ERROR Parsing getLonDouble"+getLon(),e);
			}
		}
		return ret;
	}

//	public List<Long> getIdDataset() {
//		return idDataset;
//	}
//
//	public void setIdDataset(List<Long> idDataset) {
//		this.idDataset = idDataset;
//	}

	public String getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(String isCurrent) {
		this.isCurrent = isCurrent;
	}

	public List<String> getSoFps() {
		return soFps;
	}

	public void setSoFps(List<String> soFps) {
		this.soFps = soFps;
	}

	public List<String> getSoCategory() {
		return soCategory;
	}

	public void setSoCategory(List<String> soCategory) {
		this.soCategory = soCategory;
	}


	public void setOpendataUpdateDate(String opendataUpdateDate) {
		this.opendataUpdateDate = opendataUpdateDate;
	}

	public String getOpendataUpdateDate() {
		return opendataUpdateDate;
	}

	public String getOpendataMetaUpdateDate() {
		return opendataMetaUpdateDate;
	}

	public void setOpendataMetaUpdateDate(String opendataMetaUpdateDate) {
		this.opendataMetaUpdateDate = opendataMetaUpdateDate;
	}

	public String getOpendataLanguage() {
		return opendataLanguage;
	}

	public void setOpendataLanguage(String opendataLanguage) {
		this.opendataLanguage = opendataLanguage;
	}

	public Double getFps() {
		Double fps = null;
		if (soFps != null && soFps.size() > 0) {
			try {
				fps = new Double(soFps.get(0));
			} catch (Exception e) {
				log.error("ERROR Parsing getFps"+soFps,e);
			}
		}
		return fps;
	}

	public Boolean getDcatReady() {
		return BooleanUtils.isTrue(dcatReady);
	}

	public void setDcatReady(Boolean dcatReady) {
		this.dcatReady = dcatReady;
	}

	public Boolean getIsOpendata() {
		return BooleanUtils.isTrue(isOpendata);
	}

	public void setIsOpendata(Boolean isOpendata) {
		this.isOpendata = isOpendata;
	}
	
	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Date parseRegistrationDate() {
		return parseDate(registrationDate);
	}

	private Date parseDate(String date) {
		Date result = null;
		if (date != null) {
			try {
				DateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				parser.setTimeZone(TimeZone.getTimeZone("UTC"));
				result = parser.parse(date);
			} catch (Exception e) {
				try {
					DateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
					parser.setTimeZone(TimeZone.getTimeZone("UTC"));
					result = parser.parse(date);
				} catch (Exception eq)
				{
					try {
						Long millis = Long.parseLong(date);
						result = new Date(millis); 
					} catch (Exception eq1)
					{
						log.error("ERROR Parsing date "+date,eq1);
					}
				}
			}
		}
		return result;
	}

	public Long getRegistrationDateMillis() {
		Long result = null;
		Date d = parseRegistrationDate();
		if (d != null) {
			result = d.getTime();
		}
		return result;
	}
	
	public Date parseOpendataUpdateDate() {
		return parseDate(opendataUpdateDate);
	}

	public Long getOpendataUpdateDateMillis() {
		Long result = null;
		Date d = parseOpendataUpdateDate();
		if (d != null) {
			result = d.getTime();
		}
		return result;
	}


	
	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public String getOpendataAuthor() {
		return opendataAuthor;
	}

	public void setOpendataAuthor(String opendataAuthor) {
		this.opendataAuthor = opendataAuthor;
	}

	public String getSoType() {
		return soType;
	}

	public void setSoType(String soType) {
		this.soType = soType;
	}


	public String getOpendataUpdateFrequency() {
		return opendataUpdateFrequency;
	}


	public void setOpendataUpdateFrequency(String opendataUpdateFrequency) {
		this.opendataUpdateFrequency = opendataUpdateFrequency;
	}
	
	
}
