package org.csi.yucca.dataservice.metadataapi.model.dcat;

import com.google.gson.annotations.SerializedName;

public class IdString {

	@SerializedName("@id")
	private String id;

	public IdString() {
		super();
	}

	public IdString(String id) {
		super();
		this.setId(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

}
