package org.csi.yucca.dataservice.metadataapi.model.dcat;

import com.google.gson.annotations.SerializedName;

public class DCatTheme {

	@SerializedName("@id")
	private String id;

	@SerializedName("@type")
	private String type;

	@SerializedName("skos:prefLabel")
	private I18NString label_it;
//	@SerializedName("skos:prefLabel")
//	private I18NString label_en;

	public DCatTheme() {
		super();
	}

	public DCatTheme(String id, String label_it, String label_en) {
		super();
		this.id = id;
		this.type = "skos:Concept";
		//this.label_it = new I18NString("it", label_it);
		//this.label_en = new I18NString("en", label_en);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public I18NString getLabel_it() {
		return label_it;
	}

	public void setLabel_it(I18NString label_it) {
		this.label_it = label_it;
	}

//	public I18NString getLabel_en() {
//		return label_en;
//	}
//
//	public void setLabel_en(I18NString label_en) {
//		this.label_en = label_en;
//	}

}
