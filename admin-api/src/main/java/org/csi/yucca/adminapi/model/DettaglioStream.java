package org.csi.yucca.adminapi.model;

import java.sql.Timestamp;

public class DettaglioStream extends Stream
		implements IOrganization, ITenant, IStatus, IDomain, ISubdomain, ISoCategory, ISoType {

	
	private Long usedInInternalCount;
	private Long streamsCountBySO;
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
	private String dataSourceName;

	private String smartObjectCode;
	private String smartObjectName;
	private String smartObjectDescription;
	private String smartObjectSlug;

	private String smartObjectCategoryCode;
	private String smartObjectCategoryDescription;
	private Integer idSoCategory;

	private String soTypeCode;
	private String smartObjectTypeDescription;
	private Integer idSoType;

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

	public String getSotypecode() {
		return sotypecode;
	}

	public void setSotypecode(String sotypecode) {
		this.sotypecode = sotypecode;
	}

//	public Integer getIdStream() {
//		return idStream;
//	}
//
//	public void setIdStream(Integer idStream) {
//		this.idStream = idStream;
//	}
//
//	public String getStreamCode() {
//		return streamCode;
//	}
//
//	public void setStreamCode(String streamCode) {
//		this.streamCode = streamCode;
//	}
//
//	public String getStreamName() {
//		return streamName;
//	}
//
//	public void setStreamName(String streamName) {
//		this.streamName = streamName;
//	}
//
//	public Integer getStreamSaveData() {
//		return streamSaveData;
//	}
//
//	public void setStreamSaveData(Integer streamSaveData) {
//		this.streamSaveData = streamSaveData;
//	}
//
//	public Integer getDataSourceVersion() {
//		return dataSourceVersion;
//	}
//
//	public void setDataSourceVersion(Integer dataSourceVersion) {
//		this.dataSourceVersion = dataSourceVersion;
//	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
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

}
