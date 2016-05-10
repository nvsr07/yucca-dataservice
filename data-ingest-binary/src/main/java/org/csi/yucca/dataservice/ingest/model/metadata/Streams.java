package org.csi.yucca.dataservice.ingest.model.metadata;

import java.util.List;

import org.csi.yucca.dataservice.ingest.util.json.JSonHelper;

import com.google.gson.Gson;

public class Streams extends AbstractEntity {

	private StreamField stream;

	public static Streams fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, Streams.class);
	}

	public Streams() {
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public StreamField getStream() {
		return stream;
	}

	public void setStream(StreamField stream) {
		this.stream = stream;
	}

}