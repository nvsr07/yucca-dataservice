package org.csi.yucca.dataservice.insertdataapi.model.output;

public class DatasetBulkInsertIOperationReport {
	private long idDataset=-1;
	private long datasetVersion=-1;
	private int numRowToInsFromJson=-1;
	private int numRowInserted=-1;
	private long timestamp=-1;
	private String requestId=null;
	private String stream=null;
	private String sensor=null;
	private String status=null;
	public long getIdDataset() {
		return idDataset;
	}
	public void setIdDataset(long idDataset) {
		this.idDataset = idDataset;
	}
	public long getDatasetVersion() {
		return datasetVersion;
	}
	public void setDatasetVersion(long datasetVersion) {
		this.datasetVersion = datasetVersion;
	}
	public int getNumRowToInsFromJson() {
		return numRowToInsFromJson;
	}
	public void setNumRowToInsFromJson(int numRowToInsFromJson) {
		this.numRowToInsFromJson = numRowToInsFromJson;
	}
	public int getNumRowInserted() {
		return numRowInserted;
	}
	public void setNumRowInserted(int numRowInserted) {
		this.numRowInserted = numRowInserted;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getStream() {
		return stream;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}
	public String getSensor() {
		return sensor;
	}
	public void setSensor(String sensor) {
		this.sensor = sensor;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
