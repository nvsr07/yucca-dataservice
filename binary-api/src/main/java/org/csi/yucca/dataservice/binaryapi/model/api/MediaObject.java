package org.csi.yucca.dataservice.binaryapi.model.api;

import com.google.gson.annotations.Expose;

public class MediaObject {

    @Expose
    private String tenantBinary;
    @Expose
    private String filenameBinary;
    @Expose
    private String idBinary;
    @Expose
    private Long sizeBinary;
    @Expose
    private String contentTypeBinary;
    @Expose
    private String aliasNameBinary;
    @Expose
    private String pathHdfsBinary;
    @Expose
    private String insertDateBinary;
    @Expose
    private String lastUpdateDateBinary;
    @Expose
    private Long idDataset;
    @Expose
    private Integer datasetVersion;
    @Expose
    private String metadataBinary;
    
	public String getTenantBinary() {
		return tenantBinary;
	}
	public void setTenantBinary(String tenantBinary) {
		this.tenantBinary = tenantBinary;
	}
	public String getFilenameBinary() {
		return filenameBinary;
	}
	public void setFilenameBinary(String filenameBinary) {
		this.filenameBinary = filenameBinary;
	}
	public String getIdBinary() {
		return idBinary;
	}
	public void setIdBinary(String idBinary) {
		this.idBinary = idBinary;
	}
	public Long getSizeBinary() {
		return sizeBinary;
	}
	public void setSizeBinary(Long sizeBinary) {
		this.sizeBinary = sizeBinary;
	}
	public String getContentTypeBinary() {
		return contentTypeBinary;
	}
	public void setContentTypeBinary(String contentTypeBinary) {
		this.contentTypeBinary = contentTypeBinary;
	}
	public String getAliasNameBinary() {
		return aliasNameBinary;
	}
	public void setAliasNameBinary(String aliasNameBinary) {
		this.aliasNameBinary = aliasNameBinary;
	}
	public String getPathHdfsBinary() {
		return pathHdfsBinary;
	}
	public void setPathHdfsBinary(String pathHdfsBinary) {
		this.pathHdfsBinary = pathHdfsBinary;
	}
	public String getInsertDateBinary() {
		return insertDateBinary;
	}
	public void setInsertDateBinary(String insertDateBinary) {
		this.insertDateBinary = insertDateBinary;
	}
	public String getLastUpdateDateBinary() {
		return lastUpdateDateBinary;
	}
	public void setLastUpdateDateBinary(String lastUpdateDateBinary) {
		this.lastUpdateDateBinary = lastUpdateDateBinary;
	}
	public Long getIdDataset() {
		return idDataset;
	}
	public void setIdDataset(Long idDataset) {
		this.idDataset = idDataset;
	}
	public Integer getDatasetVersion() {
		return datasetVersion;
	}
	public void setDatasetVersion(Integer datasetVersion) {
		this.datasetVersion = datasetVersion;
	}
	public String getMetadataBinary() {
		return metadataBinary;
	}
	public void setMetadataBinary(String metadataBinary) {
		this.metadataBinary = metadataBinary;
	}
}
