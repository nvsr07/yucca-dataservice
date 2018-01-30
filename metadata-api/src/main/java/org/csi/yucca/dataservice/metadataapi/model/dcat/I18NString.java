package org.csi.yucca.dataservice.metadataapi.model.dcat;

import com.google.gson.annotations.SerializedName;

public class I18NString {

	@SerializedName("@language")
	private String language;

	@SerializedName("@value")
	private String value;

	public I18NString() {
		super();
	}

	public I18NString(String language, String value) {
		super();
		this.language = language;
		this.value = value;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
