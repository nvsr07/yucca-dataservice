package org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata;

import java.util.Date;


public class Opendata{
	private String author;
	private Date dataUpdateDate;
	private Long dataUpdateDateMillis;
	private Date metadaUpdateDate;
	private Long metadaUpdateDateMillis;
	
	private String language;

	private boolean isOpendata;
	private String sourceId;
	private Date metadaCreateDate;

	public Opendata() {

	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDataUpdateDate() {
		return dataUpdateDate;
	}

	public void setDataUpdateDate(Date dataUpdateDate) {
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

	public boolean isOpendata() {
		return isOpendata;
	}

	public void setOpendata(boolean isOpendata) {
		this.isOpendata = isOpendata;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public Date getMetadaCreateDate() {
		return metadaCreateDate;
	}

	public void setMetadaCreateDate(Date metadaCreateDate) {
		this.metadaCreateDate = metadaCreateDate;
	}

	public Long getMetadaUpdateDateMillis() {
		return metadaUpdateDateMillis;
	}

	public void setMetadaUpdateDateMillis(Long metadaUpdateDateMillis) {
		this.metadaUpdateDateMillis = metadaUpdateDateMillis;
	}

	public Long getDataUpdateDateMillis() {
		return dataUpdateDateMillis;
	}

	public void setDataUpdateDateMillis(Long dataUpdateDateMillis) {
		this.dataUpdateDateMillis = dataUpdateDateMillis;
	}

}
