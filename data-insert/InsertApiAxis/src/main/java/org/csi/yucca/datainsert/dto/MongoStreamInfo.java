package org.csi.yucca.datainsert.dto;

public class MongoStreamInfo extends MongoDatasetInfo{
	private long streamId=-1;
	private long streamDeploymentVersion=-1;
	private String streamCode=null;
	private String sensorCode=null;
	
	
	public String getStreamCode() {
		return streamCode;
	}
	public void setStreamCode(String streamCode) {
		this.streamCode = streamCode;
	}
	public String getSensorCode() {
		return sensorCode;
	}
	public void setSensorCode(String sensorCode) {
		this.sensorCode = sensorCode;
	}
	public long getStreamDeploymentVersion() {
		return streamDeploymentVersion;
	}
	public void setStreamDeploymentVersion(long streamDeploymentVersion) {
		this.streamDeploymentVersion = streamDeploymentVersion;
	}
	public long getStreamId() {
		return streamId;
	}
	public void setStreamId(long streamId) {
		this.streamId = streamId;
	}
	
}
