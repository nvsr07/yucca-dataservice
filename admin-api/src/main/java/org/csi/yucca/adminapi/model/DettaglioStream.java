package org.csi.yucca.adminapi.model;

import java.sql.Timestamp;

public class DettaglioStream implements IOrganization, ITenant, IStatus, IDomain, ISubdomain, ISoCategory, ISoType{
	
	private Long usedInInternalCount;
	private Long streamsCountBySO;
	private String internalquery;		
	private String twtquery;	
	private Double twtgeoloclat;
	private Double twtgeoloclon;	
	private Double twtgeolocradius;
	private String twtgeolocunit;	
	private String twtlang;
	private String twtlocale;	
	private Integer twtcount;	
	private String twtresulttype;	
	private String twtuntil;	
	private Integer twtratepercentage;		
	private Long twtlastsearchid;	
	private String dataSourceCopyright;
	private Integer dataSourceIsopendata;	
	private String dataSourceOpenDataExternalReference;		
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
	private String sotypecode;
	private Integer idStream;
	private Integer idDataSource;
	private String streamCode;
	private String streamName;
	private Integer streamSaveData;
	private Integer dataSourceVersion;
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
	private String dataSourceName;
	
	private String tenantCode;
	private String tenantName;
	private String tenantDescription;
	private Integer idTenant;

	private String smartObjectCode;
	private String smartObjectName;
	private Integer idSmartObject;
	private String smartObjectDescription;
	private String smartObjectSlug;

	private String smartObjectCategoryCode;
	private String smartObjectCategoryDescription;
	private Integer idSoCategory;

	private String soTypeCode;
	private String smartObjectTypeDescription;
	private Integer idSoType;
	private String tags;

	
	
	public String getSotypecode() {
		return sotypecode;
	}

	public void setSotypecode(String sotypecode) {
		this.sotypecode = sotypecode;
	}

	public Long getUsedInInternalCount() {
		return usedInInternalCount;
	}

	public void setUsedInInternalCount(Long usedInInternalCount) {
		this.usedInInternalCount = usedInInternalCount;
	}

	public Long getStreamsCountBySO() {
		return streamsCountBySO;
	}

	public void setStreamsCountBySO(Long streamsCountBySO) {
		this.streamsCountBySO = streamsCountBySO;
	}

	public String getInternalquery() {
		return internalquery;
	}

	public void setInternalquery(String internalquery) {
		this.internalquery = internalquery;
	}

	public String getTwtquery() {
		return twtquery;
	}

	public void setTwtquery(String twtquery) {
		this.twtquery = twtquery;
	}

	public Double getTwtgeoloclat() {
		return twtgeoloclat;
	}

	public void setTwtgeoloclat(Double twtgeoloclat) {
		this.twtgeoloclat = twtgeoloclat;
	}

	public Double getTwtgeoloclon() {
		return twtgeoloclon;
	}

	public void setTwtgeoloclon(Double twtgeoloclon) {
		this.twtgeoloclon = twtgeoloclon;
	}

	public Double getTwtgeolocradius() {
		return twtgeolocradius;
	}

	public void setTwtgeolocradius(Double twtgeolocradius) {
		this.twtgeolocradius = twtgeolocradius;
	}

	public String getTwtgeolocunit() {
		return twtgeolocunit;
	}

	public void setTwtgeolocunit(String twtgeolocunit) {
		this.twtgeolocunit = twtgeolocunit;
	}

	public String getTwtlang() {
		return twtlang;
	}

	public void setTwtlang(String twtlang) {
		this.twtlang = twtlang;
	}

	public String getTwtlocale() {
		return twtlocale;
	}

	public void setTwtlocale(String twtlocale) {
		this.twtlocale = twtlocale;
	}

	public Integer getTwtcount() {
		return twtcount;
	}

	public void setTwtcount(Integer twtcount) {
		this.twtcount = twtcount;
	}

	public String getTwtresulttype() {
		return twtresulttype;
	}

	public void setTwtresulttype(String twtresulttype) {
		this.twtresulttype = twtresulttype;
	}

	public String getTwtuntil() {
		return twtuntil;
	}

	public void setTwtuntil(String twtuntil) {
		this.twtuntil = twtuntil;
	}

	public Integer getTwtratepercentage() {
		return twtratepercentage;
	}

	public void setTwtratepercentage(Integer twtratepercentage) {
		this.twtratepercentage = twtratepercentage;
	}

	public Long getTwtlastsearchid() {
		return twtlastsearchid;
	}

	public void setTwtlastsearchid(Long twtlastsearchid) {
		this.twtlastsearchid = twtlastsearchid;
	}

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

	public String getDataSourceOpenDataExternalReference() {
		return dataSourceOpenDataExternalReference;
	}

	public void setDataSourceOpenDataExternalReference(String dataSourceOpenDataExternalReference) {
		this.dataSourceOpenDataExternalReference = dataSourceOpenDataExternalReference;
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

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public Integer getIdStream() {
		return idStream;
	}

	public void setIdStream(Integer idStream) {
		this.idStream = idStream;
	}

	public Integer getIdDataSource() {
		return idDataSource;
	}

	public void setIdDataSource(Integer idDataSource) {
		this.idDataSource = idDataSource;
	}

	public String getStreamCode() {
		return streamCode;
	}

	public void setStreamCode(String streamCode) {
		this.streamCode = streamCode;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public Integer getStreamSaveData() {
		return streamSaveData;
	}

	public void setStreamSaveData(Integer streamSaveData) {
		this.streamSaveData = streamSaveData;
	}

	public Integer getDataSourceVersion() {
		return dataSourceVersion;
	}

	public void setDataSourceVersion(Integer dataSourceVersion) {
		this.dataSourceVersion = dataSourceVersion;
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

	public String getSmartObjectCode() {
		return smartObjectCode;
	}

	public void setSmartObjectCode(String smartObjectCode) {
		this.smartObjectCode = smartObjectCode;
	}

	public String getSmartObjectName() {
		return smartObjectName;
	}

	public void setSmartObjectName(String smartObjectName) {
		this.smartObjectName = smartObjectName;
	}

	public Integer getIdSmartObject() {
		return idSmartObject;
	}

	public void setIdSmartObject(Integer idSmartObject) {
		this.idSmartObject = idSmartObject;
	}

	public String getSmartObjectDescription() {
		return smartObjectDescription;
	}

	public void setSmartObjectDescription(String smartObjectDescription) {
		this.smartObjectDescription = smartObjectDescription;
	}

	public String getSmartObjectSlug() {
		return smartObjectSlug;
	}

	public void setSmartObjectSlug(String smartObjectSlug) {
		this.smartObjectSlug = smartObjectSlug;
	}

	public String getSmartObjectCategoryCode() {
		return smartObjectCategoryCode;
	}

	public void setSmartObjectCategoryCode(String smartObjectCategoryCode) {
		this.smartObjectCategoryCode = smartObjectCategoryCode;
	}

	public String getSmartObjectCategoryDescription() {
		return smartObjectCategoryDescription;
	}

	public void setSmartObjectCategoryDescription(String smartObjectCategoryDescription) {
		this.smartObjectCategoryDescription = smartObjectCategoryDescription;
	}

	public Integer getIdSoCategory() {
		return idSoCategory;
	}

	public void setIdSoCategory(Integer idSoCategory) {
		this.idSoCategory = idSoCategory;
	}

	public String getSoTypeCode() {
		return soTypeCode;
	}

	public void setSoTypeCode(String soTypeCode) {
		this.soTypeCode = soTypeCode;
	}

	public String getSmartObjectTypeDescription() {
		return smartObjectTypeDescription;
	}

	public void setSmartObjectTypeDescription(String smartObjectTypeDescription) {
		this.smartObjectTypeDescription = smartObjectTypeDescription;
	}

	public Integer getIdSoType() {
		return idSoType;
	}

	public void setIdSoType(Integer idSoType) {
		this.idSoType = idSoType;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

}
