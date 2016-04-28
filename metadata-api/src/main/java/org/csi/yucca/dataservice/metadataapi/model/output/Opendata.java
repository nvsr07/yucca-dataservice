package org.csi.yucca.dataservice.metadataapi.model.output;

import java.util.Date;

public class Opendata {


	private String author;
	private Long dataUpdateDate;
	private Date metadaUpdateDate;
	private String language;
	
	public Opendata() {
	
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Long getDataUpdateDate() {
		return dataUpdateDate;
	}

	public void setDataUpdateDate(Long dataUpdateDate) {
		this.dataUpdateDate = dataUpdateDate;
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
