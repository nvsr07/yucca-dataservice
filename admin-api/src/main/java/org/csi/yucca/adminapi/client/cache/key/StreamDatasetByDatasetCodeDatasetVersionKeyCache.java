package org.csi.yucca.adminapi.client.cache.key;

public class StreamDatasetByDatasetCodeDatasetVersionKeyCache extends KeyCache{

	private static final long serialVersionUID = -5604952224310717385L;

	private String datasetCode;
	private Integer datasetVersion;
	
	public StreamDatasetByDatasetCodeDatasetVersionKeyCache(String adminBaseUrl, String logger) {
		super(adminBaseUrl, logger);
	}

	public StreamDatasetByDatasetCodeDatasetVersionKeyCache datasetCode(String datasetCode){
		this.datasetCode = datasetCode;
		return this;
	}
	
	public StreamDatasetByDatasetCodeDatasetVersionKeyCache datasetVersion(Integer datasetVersion){
		this.datasetVersion = datasetVersion;
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datasetCode == null) ? 0 : datasetCode.hashCode());
		result = prime * result + ((datasetVersion == null) ? 0 : datasetVersion.hashCode());
		return result + super.hashCode();

	}

	@Override
	public String getKeyUrl(){
		return datasetCode + "/" + Integer.toString(datasetVersion);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		StreamDatasetByDatasetCodeDatasetVersionKeyCache other = (StreamDatasetByDatasetCodeDatasetVersionKeyCache) obj;

		if (datasetVersion == null) {
			if (other.datasetVersion != null) return false;
		} 
		else if (!datasetVersion.equals(other.datasetVersion))
			return false;

		if (datasetCode == null) {
			if (other.datasetCode != null) return false;
		} 
		else if (!datasetCode.equals(other.datasetCode))
			return false;
		
		return super.equals(obj);
	}	
	
	
	
	
	
	
	
	
	
}
