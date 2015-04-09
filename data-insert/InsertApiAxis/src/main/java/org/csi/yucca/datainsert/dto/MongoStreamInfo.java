package org.csi.yucca.datainsert.dto;

public class MongoStreamInfo {
	private long streamId=-1;
	private long streamDeploymentVersion=-1;
	
	private long datasetId=-1;
	private long datasetVersion=-1;
	
	public long getStreamDeploymentVersion() {
		return streamDeploymentVersion;
	}
	public void setStreamDeploymentVersion(long streamDeploymentVersion) {
		this.streamDeploymentVersion = streamDeploymentVersion;
	}
	public long getDatasetVersion() {
		return datasetVersion;
	}
	public void setDatasetVersion(long datasetVersion) {
		this.datasetVersion = datasetVersion;
	}
	public long getStreamId() {
		return streamId;
	}
	public void setStreamId(long streamId) {
		this.streamId = streamId;
	}
	public long getDatasetId() {
		return datasetId;
	}
	public void setDatasetId(long datasetId) {
		this.datasetId = datasetId;
	}
	
}
