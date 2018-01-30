package org.csi.yucca.adminapi.model;


public class DettaglioStream extends Stream
		implements IOrganization, ITenant, IStatus, IDomain, ISubdomain, ISoCategory, ISoType {

	
	private Long usedInInternalCount;
	private Long streamsCountBySO;
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
