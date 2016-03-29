package org.csi.yucca.dataservice.metadataapi.delegate;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class StreamDomainsContainer {

	private StreamDomains streamDomains;

	public static StreamDomainsContainer fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, StreamDomainsContainer.class);
	}

	public StreamDomains getStreamDomains() {
		return streamDomains;
	}

	public void setStreamDomains(StreamDomains streamDomains) {
		this.streamDomains = streamDomains;
	}

}
