package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.Dataset;

public class DatasetSubtypeResponse extends Response{

	private Integer idDatasetSubtype;
	private String datasetSubtype;
	private String description;
	private Integer idDatasetType;
	
	public DatasetSubtypeResponse(Dataset dataset) {
		super();
		this.idDatasetSubtype = dataset.getIdDatasetSubtype();
		this.datasetSubtype = dataset.getDatasetSubtype();
		this.description = dataset.getDatasetSubtypeDescription();
		this.idDatasetType = dataset.getIdDatasetType();
	}
	
	public Integer getIdDatasetSubtype() {
		return idDatasetSubtype;
	}
	public void setIdDatasetSubtype(Integer idDatasetSubtype) {
		this.idDatasetSubtype = idDatasetSubtype;
	}
	public String getDatasetSubtype() {
		return datasetSubtype;
	}
	public void setDatasetSubtype(String datasetSubtype) {
		this.datasetSubtype = datasetSubtype;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getIdDatasetType() {
		return idDatasetType;
	}
	public void setIdDatasetType(Integer idDatasetType) {
		this.idDatasetType = idDatasetType;
	}

	
}
