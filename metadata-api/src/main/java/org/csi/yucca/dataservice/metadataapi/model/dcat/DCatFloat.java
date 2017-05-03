package org.csi.yucca.dataservice.metadataapi.model.dcat;

import com.google.gson.annotations.SerializedName;

public class DCatFloat {
	@SerializedName("@type")
	private String type;

	@SerializedName("@value")
	private String value;

	public DCatFloat() {
		super();
	}

	public DCatFloat(String floatValue) {
		super();
		this.type = "xsd:float";
		if (floatValue != null)
			this.value = floatValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
