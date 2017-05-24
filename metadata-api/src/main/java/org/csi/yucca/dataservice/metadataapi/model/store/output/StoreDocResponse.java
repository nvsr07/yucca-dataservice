package org.csi.yucca.dataservice.metadataapi.model.store.output;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class StoreDocResponse {

	private boolean error;
	private StoreDoc doc;

	public StoreDocResponse() {
	}

	public static StoreDocResponse fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, StoreDocResponse.class);
	}

	public StoreDoc getDoc() {
		return doc;
	}

	public void setDoc(StoreDoc doc) {
		this.doc = doc;
	}

	public boolean getError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
