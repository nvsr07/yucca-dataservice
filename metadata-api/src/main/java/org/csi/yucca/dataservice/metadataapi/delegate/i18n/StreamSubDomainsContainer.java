package org.csi.yucca.dataservice.metadataapi.delegate.i18n;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class StreamSubDomainsContainer {

	private StreamSubDomains streamSubDomains;

	public static StreamSubDomainsContainer fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, StreamSubDomainsContainer.class);
	}

	public StreamSubDomains getStreamSubDomains() {
		return streamSubDomains;
	}

	public void setStreamSubDomains(StreamSubDomains streamSubDomains) {
		this.streamSubDomains = streamSubDomains;
	}

}
