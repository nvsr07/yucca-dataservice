package org.csi.yucca.dataservice.metadataapi.model.dcat;

import java.util.ArrayList;
import java.util.List;

public class CatalogDCAT {

	private String context = "https://raw.githubusercontent.com/insideout10/open_data_dcat_ap/develop/data/v1.01/context.it.jsonld";
	private String id = "http://www.dati.gov.it/dcat.json";
	private String type = "dcat:Catalog";
	
	private String description;
	private String title;
	private String homepage;
	private String language = "http://publications.europa.eu/resource/authority/language/ITA";
	
	private String releaseDate; 
	private String modified;
	
	private String themes = "http://eurovoc.europa.eu";
	private String spatial = "http://publications.europa.eu/resource/authority/country/ITA"; 
	private String license = "";

	private AgentDCAT publisher; 
	
	private List<DatasetDCAT> dataset = new ArrayList<DatasetDCAT>();

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getLanguage() {
		return language;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public List<DatasetDCAT> getDataset() {
		return dataset;
	}

	public void setDataset(List<DatasetDCAT> dataset) {
		this.dataset = dataset;
	}

	public String getContext() {
		return context;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getThemes() {
		return themes;
	}

	public String getSpatial() {
		return spatial;
	}

	public String getLicense() {
		return license;
	}
	
	public void setLicense(String lic){
		this.license = lic;
	}

	public AgentDCAT getPublisher() {
		return publisher;
	}
}
