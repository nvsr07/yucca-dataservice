package org.csi.yucca.datainsert.dto;

import java.util.ArrayList;

public class DatasetBulkInsert {

	public static final String STATUS_SYNTAX_CHECKED="validate";
	public static final String STATUS_START_INS="start_ins";
	public static final String STATUS_END_INS="end_ins";
	public static final String STATUS_KO_INS="ins_KO";

	
	public static final String STATUS_START_COPY="start_copy";
	public static final String STATUS_END_COPY="end_copy";
	public static final String STATUS_KO_COPY="copy_KO";
	
	
	private long idDataset=-1;
	private long datasetVersion=-1;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private ArrayList<String> rowsToInsert=null;
	private int numRowToInsFromJson=-1;
	private long timestamp=-1;
	private String requestId=null;
	private String stream=null;
	private String sensor=null;
	private String status=null;
	
	
	
	public int getNumRowToInsFromJson() {
		return numRowToInsFromJson;
	}
	public void setNumRowToInsFromJson(int numRowToInsFromJson) {
		this.numRowToInsFromJson = numRowToInsFromJson;
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
	public ArrayList<String> getRowsToInsert() {
		return rowsToInsert;
	}
	public void setRowsToInsert(ArrayList<String> rowsToInsert) {
		this.rowsToInsert = rowsToInsert;
	}
	
}
