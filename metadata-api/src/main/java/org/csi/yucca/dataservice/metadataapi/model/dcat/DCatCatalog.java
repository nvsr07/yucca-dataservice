package org.csi.yucca.dataservice.metadataapi.model.dcat;

import java.util.LinkedList;
import java.util.List;

import org.csi.yucca.dataservice.metadataapi.util.DCatSdpHelper;

import com.google.gson.annotations.SerializedName;

public class DCatCatalog extends DCatObject {


	// mandatory
	@SerializedName("dcterms:title")
	private I18NString title_it;
	//@SerializedName("dcterms:title")
	//private I18NString title_en;

	@SerializedName("dcterms:description")
	private I18NString description_it;
//	@SerializedName("dcterms:description")
//	private I18NString description_en;

	@SerializedName("dcterms:publisher")
	private DCatAgent publisher;

	@SerializedName("dcterms:modified")
	private DCatDate modified;

	@SerializedName("dcat:dataset")
	private List<DCatDataset> datasets;

	// reccomended
	@SerializedName("foaf:homepage")
	private IdString homepage;

	@SerializedName("dcterms:language")
	private IdString language;

	@SerializedName("dcterms:issued")
	private DCatDate releaseDate;

	@SerializedName("dcat:themeTaxonomy")
	private IdString themes;

	// private String spatial =
	// "http://publications.europa.eu/resource/authority/country/ITA";
	// private LicenceTypeDCAT license = new LicenceTypeDCAT();

	public DCatCatalog() {
		setId("dataset_catalog");
		addType("dcat:Catalog");
		datasets = new LinkedList<DCatDataset>();
		language = new IdString("http://publications.europa.eu/resource/authority/language/ITA");
		themes = new IdString("http://eurovoc.europa.eu");
		setPublisher(DCatSdpHelper.getCSIAgentDcat());
	}

	public void setId(String id) {
		this.id = BASE_ID + "catalog/" + DCatSdpHelper.cleanForId(id);
	}

	public I18NString getTitle_it() {
		return title_it;
	}

	public void setTitle_it(I18NString title_it) {
		this.title_it = title_it;
	}

//	public I18NString getTitle_en() {
//		return title_en;
//	}
//
//	public void setTitle_en(I18NString title_en) {
//		this.title_en = title_en;
//	}

	public I18NString getDescription_it() {
		return description_it;
	}

	public void setDescription_it(I18NString description_it) {
		this.description_it = description_it;
	}

//	public I18NString getDescription_en() {
//		return description_en;
//	}
//
//	public void setDescription_en(I18NString description_en) {
//		this.description_en = description_en;
//	}

	public DCatAgent getPublisher() {
		return publisher;
	}

	public void setPublisher(DCatAgent publisher) {
		this.publisher = publisher;
	}

	public DCatDate getModified() {
		return modified;
	}

	public void setModified(DCatDate modified) {
		this.modified = modified;
	}

	public List<DCatDataset> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<DCatDataset> datasets) {
		this.datasets = datasets;
	}

	public IdString getHomepage() {
		return homepage;
	}

	public void setHomepage(IdString homepage) {
		this.homepage = homepage;
	}

	public IdString getLanguage() {
		return language;
	}

	public void setLanguage(IdString language) {
		this.language = language;
	}

	public DCatDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(DCatDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public IdString getThemes() {
		return themes;
	}

	public void setThemes(IdString themes) {
		this.themes = themes;
	}

	public void addDataset(DCatDataset dataset) {
		if (datasets == null)
			datasets = new LinkedList<DCatDataset>();
		datasets.add(dataset);
	}
	
	//
	// public String getSpatial() {
	// return spatial;
	// }
	//
	// public void setSpatial(String spatial) {
	// this.spatial = spatial;
	// }
	//
	// public LicenceTypeDCAT getLicense() {
	// return license;
	// }
	//
	// public void setLicense(LicenceTypeDCAT license) {
	// this.license = license;
	// }

}
