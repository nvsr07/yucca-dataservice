package org.csi.yucca.dataservice.metadataapi.delegate;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class StreamDomains {

	private DomainElement[] element;

	public StreamDomains() {
	}

	public static StreamDomains fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, StreamDomains.class);
	}

	public DomainElement[] getElement() {
		return element;
	}

	public void DomainElement(DomainElement[] element) {
		this.element = element;
	}

}
