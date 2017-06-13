package org.csi.yucca.dataservice.binaryapi;

public class HDFSFileProps {

	
	private String fullFilePath=null;
	private int maxFileds=0;
	private int datasetVersion=0;
	public String getFullFilePath() {
		return fullFilePath;
	}
	public void setFullFilePath(String fullFilePath) {
		this.fullFilePath = fullFilePath;
	}
	public int getMaxFileds() {
		return maxFileds;
	}
	public void setMaxFileds(int maxFileds) {
		this.maxFileds = maxFileds;
	}
	public int getDatasetVersion() {
		return datasetVersion;
	}
	public void setDatasetVersion(int datasetVersion) {
		this.datasetVersion = datasetVersion;
	}
	
	
	
}
