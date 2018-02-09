package org.csi.yucca.dataservice.binaryapi.model.output;

import java.util.ArrayList;
import java.util.Map;

import net.minidev.json.JSONObject;

public class DatasetBulkInsert {

	private String globalReqId=null;
	
	public String getGlobalReqId() {
		return globalReqId;
	}
	public void setGlobalReqId(String globalReqId) {
		this.globalReqId = globalReqId;
	}
	public static final String STATUS_SYNTAX_CHECKED="validate";
	public static final String STATUS_START_INS="start_ins";
	public static final String STATUS_END_INS="end_ins";
	public static final String STATUS_KO_INS="ins_KO";

	public static final String STATUS_START_INDEX="start_index";
	public static final String STATUS_END_INDEX="end_index";
	public static final String STATUS_KO_INDEX="index_KO";
	

	
	private long idDataset=-1;
	private long datasetVersion=-1;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private ArrayList<String> rowsToInsert=null;
	
	private ArrayList<JSONObject> jsonRowsToInsert=null;
	
	private Map<String, FieldsMongoDto> fieldsType = null;
	public Map<String, FieldsMongoDto> getFieldsType() {
		return fieldsType;
	}
	public void setFieldsType(Map<String, FieldsMongoDto> fieldsType) {
		this.fieldsType = fieldsType;
	}
	private int numRowToInsFromJson=-1;
	private long timestamp=-1;
	private String requestId=null;
	private String stream=null;
	private String sensor=null;
	private String status=null;
	private String datasetCode=null;
	private String datasetType=null;
	
	
	public String getDatasetType() {
		return datasetType;
	}
	public void setDatasetType(String datasetType) {
		this.datasetType = datasetType;
	}
	public String getDatasetCode() {
		return datasetCode;
	}
	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
	}
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
	public ArrayList<JSONObject> getJsonRowsToInsert() {
		return jsonRowsToInsert;
	}
	public void setJsonRowsToInsert(ArrayList<JSONObject> jsonRowsToInsert) {
		this.jsonRowsToInsert = jsonRowsToInsert;
	}
	
}
