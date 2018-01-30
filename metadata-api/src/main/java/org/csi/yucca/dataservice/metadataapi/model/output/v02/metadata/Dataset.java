package org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;
public class Dataset {
	public static final String DATASET_TYPE_BULK = "bulkDataset";
	public static final String DATASET_TYPE_BINARY= "binaryDataset";
	public static final String DATASET_TYPE_STREAM = "streamDataset";
	public static final String DATASET_TYPE_SOCIAL = "socialDataset";


	private Long datasetId;
	private String code; 
	private String datasetType;
	private String datasetSubtype;
	private String importFileType;
	
	public Dataset() {

	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDatasetType() {
		return datasetType;
	}

	public void setDatasetType(String datasetType) {
		this.datasetType = datasetType;
	}

	public Long getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(Long datasetId) {
		this.datasetId = datasetId;
	}

	public String getImportFileType() {
		return importFileType;
	}

	public void setImportFileType(String importFileType) {
		this.importFileType = importFileType;
	}

	public String getDatasetSubtype() {
		return datasetSubtype;
	}

	public void setDatasetSubtype(String datasetSubtype) {
		this.datasetSubtype = datasetSubtype;
	}


}
