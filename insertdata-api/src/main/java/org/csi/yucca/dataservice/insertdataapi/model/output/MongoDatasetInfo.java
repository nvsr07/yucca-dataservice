package org.csi.yucca.dataservice.insertdataapi.model.output;

import java.util.ArrayList;

public class MongoDatasetInfo {
	private long datasetId=-1;
	private long datasetVersion=-1;
	private String datasetType=null;
	private String datasetSubType=null;
	private  ArrayList<FieldsMongoDto> campi=new ArrayList<FieldsMongoDto>();
	private String tenantcode=null;

	
	
	
	public String getTenantcode() {
		return tenantcode;
	}
	public void setTenantcode(String tenantcode) {
		this.tenantcode = tenantcode;
	}
	public ArrayList<FieldsMongoDto> getCampi() {
		return campi;
	}
	public void setCampi(ArrayList<FieldsMongoDto> campi) {
		this.campi = campi;
	}
	public long getDatasetVersion() {
		return datasetVersion;
	}
	public void setDatasetVersion(long datasetVersion) {
		this.datasetVersion = datasetVersion;
	}
	public long getDatasetId() {
		return datasetId;
	}
	public void setDatasetId(long datasetId) {
		this.datasetId = datasetId;
	}
	public String getDatasetType() {
		return datasetType;
	}
	public void setDatasetType(String datasetType) {
		this.datasetType = datasetType;
	}
	public String getDatasetSubType() {
		return datasetSubType;
	}
	public void setDatasetSubType(String datasetSubType) {
		this.datasetSubType = datasetSubType;
	}

}
