package org.csi.yucca.dataservice.metadataapi.model.dcat;

import java.util.Date;

public class DistributionDCAT {

	private String accessURL; // url su portale SDP del dataset (da valorizzare
								// a runtime)
	private String format = "http://publications.europa.eu/resource/authority/file-type/CSV";
	private String license; // lo prendi dal campo license del Dataset
	//private Integer byteSize = 0; // fisso
	private String downloadURL; // link per il download del dataset in csv (da
								// valorizzare a runtime)
	private String language = "http://publications.europa.eu/resource/authority/language/ITA";
	private Date issued; // metadata.info.registrationDate
	private String modified; // opendata.dataUpdateDate
	private String type = "dcat:Distribution";

	public String getAccessURL() {
		return accessURL;
	}

	public void setAccessURL(String accessURL) {
		this.accessURL = accessURL;
	}

	public String getFormat() {
		return format;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	/*public Integer getByteSize() {
		return byteSize;
	}*/

	public String getDownloadURL() {
		return downloadURL;
	}

	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	public String getLanguage() {
		return language;
	}

	public Date getIssued() {
		return issued;
	}

	public void setIssued(Date date) {
		this.issued = date;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}
}
