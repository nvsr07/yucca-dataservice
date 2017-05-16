package org.csi.yucca.dataservice.metadataapi.model.dcat;

import java.util.LinkedList;
import java.util.List;

import org.csi.yucca.dataservice.metadataapi.util.DCatSdpHelper;

import com.google.gson.annotations.SerializedName;

public class DCatDistribution extends DCatObject{

	@SerializedName("dcterms:format")
	private IdString format;

	@SerializedName("dcat:accessURL")
	private IdString accessURL;

	@SerializedName("dcterms:license")
	private DCatLicenseType license;

	// reccomended
	@SerializedName("dcterms:description")
	private I18NString description_it;
	//@SerializedName("dcterms:description")
	//private I18NString description_en;

	// optional
	@SerializedName("dcterms:title")
	private I18NString title_it;
//	@SerializedName("dcterms:title")
//	private I18NString title_en;

	@SerializedName("dcat:downloadURL")
	private IdString downloadURL;

	@SerializedName("dcterms:modified")
	private DCatDate modified;

	@SerializedName("dcterms:byteSize")
	private DCatFloat byteSize;

	// private Integer byteSize = 0; // fisso
	// private String language =
	// "http://publications.europa.eu/resource/authority/language/ITA";
	// private Date issued; // metadata.info.registrationDate

	public DCatDistribution() {
		format = new IdString("http://publications.europa.eu/resource/authority/file-type/CSV");
		types = new LinkedList<String>();
		addType("dcat:Distribution");
	}

	public void setId(String id) {
		this.id = BASE_ID + "distribution/" + DCatSdpHelper.cleanForId(id);
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public IdString getFormat() {
		return format;
	}

	public void setFormat(IdString format) {
		this.format = format;
	}

	public IdString getAccessURL() {
		return accessURL;
	}

	public void setAccessURL(IdString accessURL) {
		this.accessURL = accessURL;
	}

	public DCatLicenseType getLicense() {
		return license;
	}

	public void setLicense(DCatLicenseType license) {
		this.license = license;
	}

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

	public IdString getDownloadURL() {
		return downloadURL;
	}

	public void setDownloadURL(IdString downloadURL) {
		this.downloadURL = downloadURL;
	}

	public DCatDate getModified() {
		return modified;
	}

	public void setModified(DCatDate modified) {
		this.modified = modified;
	}

	public DCatFloat getByteSize() {
		return byteSize;
	}

	public void setByteSize(DCatFloat byteSize) {
		this.byteSize = byteSize;
	}

}
