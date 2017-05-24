package org.csi.yucca.dataservice.metadataapi.model.store.output.doc;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class DocStreamContent {
	private Streams streams;


	public DocStreamContent() {
		super();
	}

	public static DocStreamContent fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, DocStreamContent.class);
	}

	public Streams getStreams() {
		return streams;
	}

	public void setStreams(Streams streams) {
		this.streams = streams;
	}


}
