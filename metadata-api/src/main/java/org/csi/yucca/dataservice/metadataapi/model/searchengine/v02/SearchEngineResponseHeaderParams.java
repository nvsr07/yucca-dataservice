package org.csi.yucca.dataservice.metadataapi.model.searchengine.v02;

import java.util.List;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class SearchEngineResponseHeaderParams {
	private String q;
	private String facet_prefix;
	private List<String> facet_field;
	private String json;

	public SearchEngineResponseHeaderParams() {
		super();
	}

	public static SearchEngineResponseHeaderParams fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, SearchEngineResponseHeaderParams.class);
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getFacet_prefix() {
		return facet_prefix;
	}

	public void setFacet_prefix(String facet_prefix) {
		this.facet_prefix = facet_prefix;
	}

	public List<String> getFacet_field() {
		return facet_field;
	}

	public void setFacet_field(List<String> facet_field) {
		this.facet_field = facet_field;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}
