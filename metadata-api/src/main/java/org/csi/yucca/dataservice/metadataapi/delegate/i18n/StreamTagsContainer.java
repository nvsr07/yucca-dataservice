package org.csi.yucca.dataservice.metadataapi.delegate.i18n;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class StreamTagsContainer {

	private StreamTags streamTags;

	public static StreamTagsContainer fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, StreamTagsContainer.class);
	}

	public StreamTags getStreamTags() {
		return streamTags;
	}

	public void setStreamTags(StreamTags streamTags) {
		this.streamTags = streamTags;
	}

}
