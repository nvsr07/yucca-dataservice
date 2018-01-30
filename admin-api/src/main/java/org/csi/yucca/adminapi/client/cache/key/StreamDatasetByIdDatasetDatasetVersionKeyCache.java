package org.csi.yucca.adminapi.client.cache.key;

public class StreamDatasetByIdDatasetDatasetVersionKeyCache extends KeyCache{

	private static final long serialVersionUID = -1784823421681805844L;

	private Integer idDataset; 
	private Integer datasetVersion;
	
	public StreamDatasetByIdDatasetDatasetVersionKeyCache(String adminBaseUrl, String logger) {
		super(adminBaseUrl, logger);
	}

	public StreamDatasetByIdDatasetDatasetVersionKeyCache idDataset(Integer idDataset){
		this.idDataset = idDataset;
		return this;
	}
	
	public StreamDatasetByIdDatasetDatasetVersionKeyCache datasetVersion(Integer datasetVersion){
		this.datasetVersion = datasetVersion;
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idDataset == null) ? 0 : idDataset.hashCode());
		result = prime * result + ((datasetVersion == null) ? 0 : datasetVersion.hashCode());
		return result + super.hashCode();

	}

	@Override
	public String getKeyUrl(){
		return Integer.toString(idDataset) + "/" + Integer.toString(datasetVersion);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		StreamDatasetByIdDatasetDatasetVersionKeyCache other = (StreamDatasetByIdDatasetDatasetVersionKeyCache) obj;

		if (datasetVersion == null) {
			if (other.datasetVersion != null) return false;
		} 
		else if (!datasetVersion.equals(other.datasetVersion))
			return false;

		if (idDataset == null) {
			if (other.idDataset != null) return false;
		} 
		else if (!idDataset.equals(other.idDataset))
			return false;
		
		return super.equals(obj);
	}	
	
	
	
	
	
	
	
	
	
}
