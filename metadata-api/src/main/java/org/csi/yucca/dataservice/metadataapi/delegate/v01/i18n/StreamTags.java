package org.csi.yucca.dataservice.metadataapi.delegate.v01.i18n;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class StreamTags {

	private TagElement[] element;

	public StreamTags() {
	}

	public static StreamTags fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, StreamTags.class);
	}

	public TagElement[] getElement() {
		return element;
	}

	public void TagElement(TagElement[] element) {
		this.element = element;
	}

}
