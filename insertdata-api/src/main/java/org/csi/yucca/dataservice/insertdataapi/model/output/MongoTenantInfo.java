package org.csi.yucca.dataservice.insertdataapi.model.output;

import com.mongodb.DBObject;

public class MongoTenantInfo {
	private Long idTenant;
	private String tenantName;
	private String tenantDescription;
	private String tenantCode;
	private String dataCollectionName;
	private String dataCollectionDb;
	private String measuresCollectionName;
	private String measuresCollectionDb;
	private String socialCollectionName;
	private String socialCollectionDb;
	private String mediaCollectionName;
	private String mediaCollectionDb;
	private String archiveDataCollectionName;
	private String archiveDataCollectionDb;
	private String archiveMeasuresCollectionName;
	private String archiveMeasuresCollectionDb;
	private Long maxDatasetNum;
	private Long maxStreamsNum;
	private String organizationCode;
	private String tenantType;
	private String codDeploymentStatus;
	private String dataAttivazione;
	private Integer numGiorniAttivo;
	private Long idEcosystem;
	private String userName;
	private String userLastName;
	private String userEmail;
	private String userTypeAuth;
	private String userFirstName;
	private String dataSolrCollectionName;
	private String measuresSolrCollectionName;
	private String socialSolrCollectionName;
	private String mediaSolrCollectionName;
	private String mediaPhoenixSchemaName;
	private String mediaPhoenixTableName;
	private String dataPhoenixSchemaName;
	private String dataPhoenixTableName;
	private String socialPhoenixSchemaName;
	private String socialPhoenixTableName;
	private String measuresPhoenixSchemaName;
	private String measuresPhoenixTableName;

	public MongoTenantInfo() {
		super();
	}

	public MongoTenantInfo(DBObject tenantMongoObj) {
		if (tenantMongoObj != null) {
			setIdTenant(takeNvlValuesLong(tenantMongoObj.get("idTenant")));
			setTenantName(takeNvlValuesString(tenantMongoObj.get("tenantName")));
			setTenantDescription(takeNvlValuesString(tenantMongoObj.get("tenantDescription")));
			setTenantCode(takeNvlValuesString(tenantMongoObj.get("tenantCode")));
			setDataCollectionName(takeNvlValuesString(tenantMongoObj.get("dataCollectionName")));
			setDataCollectionDb(takeNvlValuesString(tenantMongoObj.get("dataCollectionDb")));
			setMeasuresCollectionName(takeNvlValuesString(tenantMongoObj.get("measuresCollectionName")));
			setMeasuresCollectionDb(takeNvlValuesString(tenantMongoObj.get("measuresCollectionDb")));
			setSocialCollectionName(takeNvlValuesString(tenantMongoObj.get("socialCollectionName")));
			setSocialCollectionDb(takeNvlValuesString(tenantMongoObj.get("socialCollectionDb")));
			setMediaCollectionName(takeNvlValuesString(tenantMongoObj.get("mediaCollectionName")));
			setMediaCollectionDb(takeNvlValuesString(tenantMongoObj.get("mediaCollectionDb")));
			setArchiveDataCollectionName(takeNvlValuesString(tenantMongoObj.get("archiveDataCollectionName")));
			setArchiveDataCollectionDb(takeNvlValuesString(tenantMongoObj.get("archiveDataCollectionDb")));
			setArchiveMeasuresCollectionName(takeNvlValuesString(tenantMongoObj.get("archiveMeasuresCollectionName")));
			setArchiveMeasuresCollectionDb(takeNvlValuesString(tenantMongoObj.get("archiveMeasuresCollectionDb")));
			setMaxDatasetNum(takeNvlValuesLong(tenantMongoObj.get("maxDatasetNum")));
			setMaxStreamsNum(takeNvlValuesLong(tenantMongoObj.get("maxStreamsNum")));
			setOrganizationCode(takeNvlValuesString(tenantMongoObj.get("organizationCode")));
			setTenantType(takeNvlValuesString(tenantMongoObj.get("tenantType")));
			setCodDeploymentStatus(takeNvlValuesString(tenantMongoObj.get("codDeploymentStatus")));
			setDataAttivazione(takeNvlValuesString(tenantMongoObj.get("dataAttivazione")));
			setNumGiorniAttivo(takeNvlValuesInteger(tenantMongoObj.get("numGiorniAttivo")));
			setIdEcosystem(takeNvlValuesLong(tenantMongoObj.get("idEcosystem")));
			setUserName(takeNvlValuesString(tenantMongoObj.get("userName")));
			setUserLastName(takeNvlValuesString(tenantMongoObj.get("userLastName")));
			setUserEmail(takeNvlValuesString(tenantMongoObj.get("userEmail")));
			setUserTypeAuth(takeNvlValuesString(tenantMongoObj.get("userTypeAuth")));
			setUserFirstName(takeNvlValuesString(tenantMongoObj.get("userFirstName")));
			setDataSolrCollectionName(takeNvlValuesString(tenantMongoObj.get("dataSolrCollectionName")));
			setMeasuresSolrCollectionName(takeNvlValuesString(tenantMongoObj.get("measuresSolrCollectionName")));
			setSocialSolrCollectionName(takeNvlValuesString(tenantMongoObj.get("socialSolrCollectionName")));
			setMediaSolrCollectionName(takeNvlValuesString(tenantMongoObj.get("mediaSolrCollectionName")));
			setMediaPhoenixSchemaName(takeNvlValuesString(tenantMongoObj.get("mediaPhoenixSchemaName")));
			setMediaPhoenixTableName(takeNvlValuesString(tenantMongoObj.get("mediaPhoenixTableName")));
			setDataPhoenixSchemaName(takeNvlValuesString(tenantMongoObj.get("dataPhoenixSchemaName")));
			setDataPhoenixTableName(takeNvlValuesString(tenantMongoObj.get("dataPhoenixTableName")));
			setSocialPhoenixSchemaName(takeNvlValuesString(tenantMongoObj.get("socialPhoenixSchemaName")));
			setSocialPhoenixTableName(takeNvlValuesString(tenantMongoObj.get("socialPhoenixTableName")));
			setMeasuresPhoenixSchemaName(takeNvlValuesString(tenantMongoObj.get("measuresPhoenixSchemaName")));
			setMeasuresPhoenixTableName(takeNvlValuesString(tenantMongoObj.get("measuresPhoenixTableName")));
		}
	}

	private String takeNvlValuesString(Object object) {
		return object == null ? null : object.toString();
	}

	private Long takeNvlValuesLong(Object object) {
		return object == null ? null : new Long(object.toString());
	}

	private Integer takeNvlValuesInteger(Object object) {
		return object == null ? null : new Integer(object.toString());
	}

	public Long getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Long idTenant) {
		this.idTenant = idTenant;
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

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	public String getDataCollectionName() {
		return dataCollectionName;
	}

	public void setDataCollectionName(String dataCollectionName) {
		this.dataCollectionName = dataCollectionName;
	}

	public String getDataCollectionDb() {
		return dataCollectionDb;
	}

	public void setDataCollectionDb(String dataCollectionDb) {
		this.dataCollectionDb = dataCollectionDb;
	}

	public String getMeasuresCollectionName() {
		return measuresCollectionName;
	}

	public void setMeasuresCollectionName(String measuresCollectionName) {
		this.measuresCollectionName = measuresCollectionName;
	}

	public String getMeasuresCollectionDb() {
		return measuresCollectionDb;
	}

	public void setMeasuresCollectionDb(String measuresCollectionDb) {
		this.measuresCollectionDb = measuresCollectionDb;
	}

	public String getSocialCollectionName() {
		return socialCollectionName;
	}

	public void setSocialCollectionName(String socialCollectionName) {
		this.socialCollectionName = socialCollectionName;
	}

	public String getSocialCollectionDb() {
		return socialCollectionDb;
	}

	public void setSocialCollectionDb(String socialCollectionDb) {
		this.socialCollectionDb = socialCollectionDb;
	}

	public String getMediaCollectionName() {
		return mediaCollectionName;
	}

	public void setMediaCollectionName(String mediaCollectionName) {
		this.mediaCollectionName = mediaCollectionName;
	}

	public String getMediaCollectionDb() {
		return mediaCollectionDb;
	}

	public void setMediaCollectionDb(String mediaCollectionDb) {
		this.mediaCollectionDb = mediaCollectionDb;
	}

	public String getArchiveDataCollectionName() {
		return archiveDataCollectionName;
	}

	public void setArchiveDataCollectionName(String archiveDataCollectionName) {
		this.archiveDataCollectionName = archiveDataCollectionName;
	}

	public String getArchiveDataCollectionDb() {
		return archiveDataCollectionDb;
	}

	public void setArchiveDataCollectionDb(String archiveDataCollectionDb) {
		this.archiveDataCollectionDb = archiveDataCollectionDb;
	}

	public String getArchiveMeasuresCollectionName() {
		return archiveMeasuresCollectionName;
	}

	public void setArchiveMeasuresCollectionName(String archiveMeasuresCollectionName) {
		this.archiveMeasuresCollectionName = archiveMeasuresCollectionName;
	}

	public String getArchiveMeasuresCollectionDb() {
		return archiveMeasuresCollectionDb;
	}

	public void setArchiveMeasuresCollectionDb(String archiveMeasuresCollectionDb) {
		this.archiveMeasuresCollectionDb = archiveMeasuresCollectionDb;
	}

	public Long getMaxDatasetNum() {
		return maxDatasetNum;
	}

	public void setMaxDatasetNum(Long maxDatasetNum) {
		this.maxDatasetNum = maxDatasetNum;
	}

	public Long getMaxStreamsNum() {
		return maxStreamsNum;
	}

	public void setMaxStreamsNum(Long maxStreamsNum) {
		this.maxStreamsNum = maxStreamsNum;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getTenantType() {
		return tenantType;
	}

	public void setTenantType(String tenantType) {
		this.tenantType = tenantType;
	}

	public String getCodDeploymentStatus() {
		return codDeploymentStatus;
	}

	public void setCodDeploymentStatus(String codDeploymentStatus) {
		this.codDeploymentStatus = codDeploymentStatus;
	}

	public String getDataAttivazione() {
		return dataAttivazione;
	}

	public void setDataAttivazione(String dataAttivazione) {
		this.dataAttivazione = dataAttivazione;
	}

	public Integer getNumGiorniAttivo() {
		return numGiorniAttivo;
	}

	public void setNumGiorniAttivo(Integer numGiorniAttivo) {
		this.numGiorniAttivo = numGiorniAttivo;
	}

	public Long getIdEcosystem() {
		return idEcosystem;
	}

	public void setIdEcosystem(Long idEcosystem) {
		this.idEcosystem = idEcosystem;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserTypeAuth() {
		return userTypeAuth;
	}

	public void setUserTypeAuth(String userTypeAuth) {
		this.userTypeAuth = userTypeAuth;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getDataSolrCollectionName() {
		return dataSolrCollectionName;
	}

	public void setDataSolrCollectionName(String dataSolrCollectionName) {
		this.dataSolrCollectionName = dataSolrCollectionName;
	}

	public String getMeasuresSolrCollectionName() {
		return measuresSolrCollectionName;
	}

	public void setMeasuresSolrCollectionName(String measuresSolrCollectionName) {
		this.measuresSolrCollectionName = measuresSolrCollectionName;
	}

	public String getSocialSolrCollectionName() {
		return socialSolrCollectionName;
	}

	public void setSocialSolrCollectionName(String socialSolrCollectionName) {
		this.socialSolrCollectionName = socialSolrCollectionName;
	}

	public String getMediaSolrCollectionName() {
		return mediaSolrCollectionName;
	}

	public void setMediaSolrCollectionName(String mediaSolrCollectionName) {
		this.mediaSolrCollectionName = mediaSolrCollectionName;
	}

	public String getMediaPhoenixSchemaName() {
		return mediaPhoenixSchemaName;
	}

	public void setMediaPhoenixSchemaName(String mediaPhoenixSchemaName) {
		this.mediaPhoenixSchemaName = mediaPhoenixSchemaName;
	}

	public String getMediaPhoenixTableName() {
		return mediaPhoenixTableName;
	}

	public void setMediaPhoenixTableName(String mediaPhoenixTableName) {
		this.mediaPhoenixTableName = mediaPhoenixTableName;
	}

	public String getDataPhoenixSchemaName() {
		return dataPhoenixSchemaName;
	}

	public void setDataPhoenixSchemaName(String dataPhoenixSchemaName) {
		this.dataPhoenixSchemaName = dataPhoenixSchemaName;
	}

	public String getDataPhoenixTableName() {
		return dataPhoenixTableName;
	}

	public void setDataPhoenixTableName(String dataPhoenixTableName) {
		this.dataPhoenixTableName = dataPhoenixTableName;
	}

	public String getSocialPhoenixSchemaName() {
		return socialPhoenixSchemaName;
	}

	public void setSocialPhoenixSchemaName(String socialPhoenixSchemaName) {
		this.socialPhoenixSchemaName = socialPhoenixSchemaName;
	}

	public String getSocialPhoenixTableName() {
		return socialPhoenixTableName;
	}

	public void setSocialPhoenixTableName(String socialPhoenixTableName) {
		this.socialPhoenixTableName = socialPhoenixTableName;
	}

	public String getMeasuresPhoenixSchemaName() {
		return measuresPhoenixSchemaName;
	}

	public void setMeasuresPhoenixSchemaName(String measuresPhoenixSchemaName) {
		this.measuresPhoenixSchemaName = measuresPhoenixSchemaName;
	}

	public String getMeasuresPhoenixTableName() {
		return measuresPhoenixTableName;
	}

	public void setMeasuresPhoenixTableName(String measuresPhoenixTableName) {
		this.measuresPhoenixTableName = measuresPhoenixTableName;
	}

}
