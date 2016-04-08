package org.csi.yucca.dataservice.metadataapi.model.store.output.doc;

import java.util.Date;

public class Opendata {

	private Boolean isOpendata;
	private String author;
	private Long dataUpdateDate;
	private Date metadaUpdateDate;
	private String language;

	public Opendata() {
	}

	public Boolean getIsOpendata() {
		return isOpendata;
	}

	public void setIsOpendata(Boolean isOpendata) {
		this.isOpendata = isOpendata;
	}

	public Long getDataUpdateDate() {
		return dataUpdateDate;
	}

	public void setDataUpdateDate(Long dataUpdateDate) {
		this.dataUpdateDate = dataUpdateDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getMetadaUpdateDate() {
		return metadaUpdateDate;
	}

	public void setMetadaUpdateDate(Date metadaUpdateDate) {
		this.metadaUpdateDate = metadaUpdateDate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
