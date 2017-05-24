package org.csi.yucca.dataservice.metadataapi.service.response;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public abstract class AbstractResponse {

	public AbstractResponse() {
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}
}
