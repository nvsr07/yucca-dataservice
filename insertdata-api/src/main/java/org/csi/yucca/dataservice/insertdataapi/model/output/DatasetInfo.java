package org.csi.yucca.dataservice.insertdataapi.model.output;

import java.util.ArrayList;

public class DatasetInfo {
	private long datasetId = -1;
	private long datasetVersion = -1;
	private String datasetType = null;
	private String datasetSubType = null;
	private ArrayList<FieldsDto> campi = new ArrayList<FieldsDto>();
	private String tenantcode = null;
	private String datasetDomain = null;
	private String datasetSubdomain = null;
	private String datasetCode = null;

	private String organizationCode = null;
	
	// streamVirtualEntitySlug, streamCode

	public String getTenantcode() {
		return tenantcode;
	}

	public void setTenantcode(String tenantcode) {
		this.tenantcode = tenantcode;
	}

	public ArrayList<FieldsDto> getCampi() {
		return campi;
	}

	public void setCampi(ArrayList<FieldsDto> campi) {
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

	public String getDatasetDomain() {
		return datasetDomain;
	}

	public void setDatasetDomain(String datasetDomain) {
		this.datasetDomain = datasetDomain;
	}

	public String getDatasetSubdomain() {
		return datasetSubdomain;
	}

	public void setDatasetSubdomain(String datasetSubdomain) {
		this.datasetSubdomain = datasetSubdomain;
	}

	public String getDatasetCode() {
		return datasetCode;
	}

	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

}
