package org.csi.yucca.dataservice.binaryapi.model.api;

import java.io.InputStream;
import java.io.Serializable;

import javax.ws.rs.FormParam;

public class DataUploadForm implements Serializable {
	static final long serialVersionUID = 1L;

	@FormParam("alias")
	private String alias;

	@FormParam("datasetCode")
	private String datasetCode;

	@FormParam("datasetVersion")
	private String datasetVersion;

	@FormParam("idBinary")
	private String idBinary;

	@FormParam("upfile")
	private InputStream upfile;

	public DataUploadForm() {
		super();
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDatasetCode() {
		return datasetCode;
	}

	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
	}

	public String getDatasetVersion() {
		return datasetVersion;
	}

	public void setDatasetVersion(String datasetVersion) {
		this.datasetVersion = datasetVersion;
	}

	public String getIdBinary() {
		return idBinary;
	}

	public void setIdBinary(String idBinary) {
		this.idBinary = idBinary;
	}

	public InputStream getUpfile() {
		return upfile;
	}

	public void setUpfile(InputStream upfile) {
		this.upfile = upfile;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}