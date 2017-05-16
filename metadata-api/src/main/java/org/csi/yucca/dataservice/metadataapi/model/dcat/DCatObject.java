package org.csi.yucca.dataservice.metadataapi.model.dcat;

import java.util.LinkedList;
import java.util.List;

import org.csi.yucca.dataservice.metadataapi.util.DCatSdpHelper;
import org.csi.yucca.dataservice.metadataapi.util.json.IgnoredJSON;

import com.google.gson.annotations.SerializedName;

public abstract class DCatObject {

	@IgnoredJSON
	protected String BASE_ID = "https://api.smartdatanet.it/metadataapi/api/dcat/";

	@SerializedName("@id")
	protected String id;
	@SerializedName("@type")
	protected List<String> types;

	public DCatObject() {
	}

	public String getId() {
		return id;
	}

	public abstract void setId(String id);

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public void addType(String type) {
		if (types == null)
			types = new LinkedList<String>();
		types.add(type);
	}

}
