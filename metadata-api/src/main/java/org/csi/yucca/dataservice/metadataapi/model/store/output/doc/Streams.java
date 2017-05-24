package org.csi.yucca.dataservice.metadataapi.model.store.output.doc;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Streams {
	private Stream stream;

	public Streams() {
		super();
	}

	public static Streams fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, Streams.class);
	}

	public Stream getStream() {
		return stream;
	}

	public void setStream(Stream stream) {
		this.stream = stream;
	}
}
