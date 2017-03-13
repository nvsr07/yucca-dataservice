package org.csi.yucca.dataservice.metadataapi.delegate.v01.i18n;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class StreamSubDomains {

	private SubDomainElement[] element;

	public StreamSubDomains() {
	}

	public static StreamSubDomains fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, StreamSubDomains.class);
	}

	public SubDomainElement[] getElement() {
		return element;
	}

	public void SubDomainElement(SubDomainElement[] element) {
		this.element = element;
	}

}
