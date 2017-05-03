package org.csi.yucca.dataservice.metadataapi.model.dcat;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class DCatDate {
	@SerializedName("@type")
	private String type;

	@SerializedName("@value")
	private Date value;

	//private DateFormat dcatDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public DCatDate() {
		super();
	}

	public DCatDate(Date dateValue) {
		super();
		this.type = "xsd:date";
		this.value = dateValue;
	//	if (dateValue != null)
	//		this.value = dcatDateFormat.format(dateValue);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getValue() {
		return value;
	}

	public void setValue(Date value) {
		this.value = value;
	}

}
