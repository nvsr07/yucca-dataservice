package org.csi.yucca.dataservice.ingest.model.metadata;

import org.csi.yucca.dataservice.ingest.util.json.JSonHelper;

import com.google.gson.Gson;

public class StreamConfigData extends AbstractEntity {

	private Long idTenant;
	private String tenantCode;
	private Long idDataset;
	private Long datasetVersion;

	public static StreamConfigData fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, StreamConfigData.class);
	}

	public StreamConfigData() {
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public Long getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Long idTenant) {
		this.idTenant = idTenant;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	public Long getIdDataset() {
		return idDataset;
	}

	public void setIdDataset(Long idDataset) {
		this.idDataset = idDataset;
	}

	public Long getDatasetVersion() {
		return datasetVersion;
	}

	public void setDatasetVersion(Long datasetVersion) {
		this.datasetVersion = datasetVersion;
	}

}