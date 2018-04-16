package org.csi.yucca.adminapi.client.db;

import java.io.Serializable;

public class KeyCacheDB implements Serializable{

	private static final long serialVersionUID = 4282547711900104665L;

//	private Integer id;
	private Integer idStream;
//	private String code;
	private Boolean onlyInstalled;
	private String smartObjectCode;
	private String streamCode;
	private String datasetCode;
	private Integer idDataset;
	private Integer datasetVersion;
	private Integer idMeasureUnit;
	private String apiCode;
	private String organizationCode;
	private Integer idOrganization;

	public KeyCacheDB idOrganization(Integer idOrganization){
		this.idOrganization = idOrganization;
		return this;
	}
	public KeyCacheDB organizationCode(String organizationCode){
		this.organizationCode = organizationCode;
		return this;
	}
	public KeyCacheDB apiCode(String apiCode){
		this.apiCode = apiCode;
		return this;
	}
	public KeyCacheDB idMeasureUnit(Integer idMeasureUnit){
		this.idMeasureUnit = idMeasureUnit;
		return this;
	}
	public KeyCacheDB datasetVersion(Integer datasetVersion){
		this.datasetVersion = datasetVersion;
		return this;
	}
	public KeyCacheDB datasetCode(String datasetCode){
		this.datasetCode = datasetCode;
		return this;
	}
	public KeyCacheDB smartObjectCode(String smartObjectCode){
		this.smartObjectCode = smartObjectCode;
		return this;
	}
	public KeyCacheDB streamCode(String streamCode){
		this.streamCode = streamCode;
		return this;
	}
	
//	public KeyCacheDB id(Integer id){
//		this.id = id;
//		return this;
//	}

	public KeyCacheDB idDataset(Integer idDataset){
		this.idDataset = idDataset;
		return this;
	}

	public KeyCacheDB idStream(Integer idStream){
		this.idStream = idStream;
		return this;
	}

//	public KeyCacheDB code(String code){
//		this.code = code;
//		return this;
//	}
	
	public KeyCacheDB onlyInstalled(Boolean onlyInstalled){
		this.onlyInstalled = onlyInstalled;
		return this;
	}

	
	public Integer getIdOrganization() {
		return idOrganization;
	}
	public void setIdOrganization(Integer idOrganization) {
		this.idOrganization = idOrganization;
	}
	public String getOrganizationCode() {
		return organizationCode;
	}
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}
	public String getApiCode() {
		return apiCode;
	}
	public void setApiCode(String apiCode) {
		this.apiCode = apiCode;
	}
	public Integer getIdMeasureUnit() {
		return idMeasureUnit;
	}
	public void setIdMeasureUnit(Integer idMeasureUnit) {
		this.idMeasureUnit = idMeasureUnit;
	}
	public Integer getDatasetVersion() {
		return datasetVersion;
	}
	public void setDatasetVersion(Integer datasetVersion) {
		this.datasetVersion = datasetVersion;
	}
	public Integer getIdDataset() {
		return idDataset;
	}
	public void setIdDataset(Integer idDataset) {
		this.idDataset = idDataset;
	}
	public String getDatasetCode() {
		return datasetCode;
	}
	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
	}
	public String getSmartObjectCode() {
		return smartObjectCode;
	}
	public void setSmartObjectCode(String smartObjectCode) {
		this.smartObjectCode = smartObjectCode;
	}
	public String getStreamCode() {
		return streamCode;
	}
	public void setStreamCode(String streamCode) {
		this.streamCode = streamCode;
	}
	public Integer getIdStream() {
		return idStream;
	}

	public void setIdStream(Integer idStream) {
		this.idStream = idStream;
	}

//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}

//	public String getCode() {
//		return code;
//	}
//
//	public void setCode(String code) {
//		this.code = code;
//	}

	
	
	public Boolean getOnlyInstalled() {
		return onlyInstalled;
	}

	public void setOnlyInstalled(Boolean onlyInstalled) {
		this.onlyInstalled = onlyInstalled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result + ((code == null) ? 0 : code.hashCode());
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idStream == null) ? 0 : idStream.hashCode());
		result = prime * result + ((onlyInstalled == null) ? 0 : onlyInstalled.hashCode());
		result = prime * result + ((streamCode == null) ? 0 : streamCode.hashCode());
		result = prime * result + ((smartObjectCode == null) ? 0 : smartObjectCode.hashCode());
		result = prime * result + ((datasetCode == null) ? 0 : datasetCode.hashCode());
		result = prime * result + ((idDataset == null) ? 0 : idDataset.hashCode());
		result = prime * result + ((datasetVersion == null) ? 0 : datasetVersion.hashCode());
		result = prime * result + ((idMeasureUnit == null) ? 0 : idMeasureUnit.hashCode());
		result = prime * result + ((apiCode == null) ? 0 : apiCode.hashCode());
		result = prime * result + ((organizationCode == null) ? 0 : organizationCode.hashCode());
		result = prime * result + ((idOrganization == null) ? 0 : idOrganization.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		KeyCacheDB other = (KeyCacheDB) obj;
		
//		if (code == null) {
//			if (other.code != null) return false;
//		} 
//		else if (!code.equals(other.code)){
//			return false;
//		}
//		
//		if (id == null) {
//			if (other.id != null) return false;
//		} 
//		else if (!id.equals(other.id)){
//			return false;
//		}
		
		if (idStream == null) {
			if (other.idStream != null) return false;
		} 
		else if (!idStream.equals(other.idStream)){
			return false;
		}
			
		if (onlyInstalled == null) {
			if (other.onlyInstalled != null) return false;
		} 
		else if (!onlyInstalled.equals(other.onlyInstalled)){
			return false;
		}
		
		
		if (streamCode == null) {
			if (other.streamCode != null) return false;
		} 
		else if (!streamCode.equals(other.streamCode)){
			return false;
		}
		
		if (smartObjectCode == null) {
			if (other.smartObjectCode != null) return false;
		} 
		else if (!smartObjectCode.equals(other.smartObjectCode)){
			return false;
		}
		
		if (datasetCode == null) {
			if (other.datasetCode != null) return false;
		} 
		else if (!datasetCode.equals(other.datasetCode)){
			return false;
		}

		if (idDataset == null) {
			if (other.idDataset != null) return false;
		} 
		else if (!idDataset.equals(other.idDataset)){
			return false;
		}
		
		if (datasetVersion == null) {
			if (other.datasetVersion != null) return false;
		} 
		else if (!datasetVersion.equals(other.datasetVersion)){
			return false;
		}

		if (idMeasureUnit == null) {
			if (other.idMeasureUnit != null) return false;
		} 
		else if (!idMeasureUnit.equals(other.idMeasureUnit)){
			return false;
		}
		
		if (apiCode == null) {
			if (other.apiCode != null) return false;
		} 
		else if (!apiCode.equals(other.apiCode)){
			return false;
		}

		if (organizationCode == null) {
			if (other.organizationCode != null) return false;
		} 
		else if (!organizationCode.equals(other.organizationCode)){
			return false;
		}
		
		if (idOrganization == null) {
			if (other.idOrganization != null) return false;
		} 
		else if (!idOrganization.equals(other.idOrganization)){
			return false;
		}
		
		return true;
	}
	
}
