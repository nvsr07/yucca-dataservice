package org.csi.yucca.dataservice.metadataapi.model.store.output;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class StoreListResponse {

	private boolean error;
	private StoreMetadataItem[] result;

	public StoreListResponse() {

	}

	public static StoreListResponse fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, StoreListResponse.class);
	}

	public boolean getError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public StoreMetadataItem[] getResult() {
		return result;
	}

	public void setResult(StoreMetadataItem[] result) {
		this.result = result;
	}

}
