package org.csi.yucca.dataservice.ingest.model.metadata;

import java.util.Date;
import org.csi.yucca.dataservice.ingest.util.json.JSonHelper;
import com.google.gson.Gson;
import com.mongodb.DBObject;

public class BinaryData extends AbstractEntity {

	public static final String CONFIG_DATA_TYPE_DATASET = "dataset";
	public static final String CONFIG_DATA_SUBTYPE_BULK_DATASET = "bulkDataset";
	public static final String CONFIG_DATA_SUBTYPE_BINARY_DATASET = "binaryDataset";
	public static final String CONFIG_DATA_SUBTYPE_STREAM_DATASET = "streamDataset";
	public static final String CONFIG_DATA_TYPE_API = "api";
	public static final String CONFIG_DATA_SUBTYPE_API_MULTI_BULK = "apiMultiBulk";


	private String id;

	private String tenantBinary;
	private String filenameBinary;
	private String idBinary;
	private Long sizeBinary;
	private String contentTypeBinary;
	private String aliasNameBinary;
	private String pathHdfsBinary;
	private Date insertDateBinary;
	private Date lastUpdateDateBinary;
	private Long idDataset;
	private String datasetCode;
	private Integer datasetVersion;
	private String metadataBinary;

	public static BinaryData fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, BinaryData.class);
	}

	public BinaryData() {
	}

	public BinaryData(DBObject obj) {
		
		this.setTenantBinary((String) obj.get("tenantBinary"));
		this.setFilenameBinary((String) obj.get("filenameBinary"));
		this.setIdBinary((String) obj.get("idBinary"));
		this.setSizeBinary((Long) obj.get("sizeBinary"));
		this.setContentTypeBinary((String) obj.get("contentTypeBinary"));
		this.setAliasNameBinary((String) obj.get("aliasNameBinary"));
		this.setPathHdfsBinary((String) obj.get("pathHdfsBinary"));
		
		this.setInsertDateBinary((Date) obj.get("insertDateBinary"));
		this.setLastUpdateDateBinary((Date) obj.get("lastUpdateDateBinary"));
		
		this.setIdDataset((Long) obj.get("idDataset"));
		this.setDatasetVersion((Integer) obj.get("datasetVersion"));
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public Date getInsertDateBinary() {
		return insertDateBinary;
	}

	public void setInsertDateBinary(Date insertDateBinary) {
		this.insertDateBinary = insertDateBinary;
	}

	public Date getLastUpdateDateBinary() {
		return lastUpdateDateBinary;
	}

	public void setLastUpdateDateBinary(Date lastUpdateDateBinary) {
		this.lastUpdateDateBinary = lastUpdateDateBinary;
	}

	public Long getIdDataset() {
		return idDataset;
	}

	public void setIdDataset(Long idDataset) {
		this.idDataset = idDataset;
	}

	public String getDatasetCode() {
		return datasetCode;
	}

	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
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
