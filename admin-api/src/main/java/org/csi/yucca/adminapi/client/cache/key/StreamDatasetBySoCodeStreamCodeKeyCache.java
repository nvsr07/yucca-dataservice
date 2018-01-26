package org.csi.yucca.adminapi.client.cache.key;

public class StreamDatasetBySoCodeStreamCodeKeyCache extends KeyCache{

	private static final long serialVersionUID = 1967866701585219421L;

	private String soCode;
	private String streamCode;

	public StreamDatasetBySoCodeStreamCodeKeyCache soCode(String soCode){
		this.soCode = soCode;
		return this;
	}

	public StreamDatasetBySoCodeStreamCodeKeyCache streamCode(String streamCode){
		this.streamCode = streamCode;
		return this;
	}
	
	public StreamDatasetBySoCodeStreamCodeKeyCache(String adminBaseUrl, String logger) {
		super(adminBaseUrl, logger);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((soCode == null) ? 0 : soCode.hashCode());
		result = prime * result + ((streamCode == null) ? 0 : streamCode.hashCode());
		return result + super.hashCode();

	}

	@Override
	public String getKeyUrl(){
		return soCode + "/" + streamCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		StreamDatasetBySoCodeStreamCodeKeyCache other = (StreamDatasetBySoCodeStreamCodeKeyCache) obj;

		if (soCode == null) {
			if (other.soCode != null) return false;
		} 
		else if (!soCode.equals(other.soCode))
			return false;

		if (streamCode == null) {
			if (other.streamCode != null) return false;
		} 
		else if (!streamCode.equals(other.streamCode))
			return false;
		
		return super.equals(obj);
	}
	
}
