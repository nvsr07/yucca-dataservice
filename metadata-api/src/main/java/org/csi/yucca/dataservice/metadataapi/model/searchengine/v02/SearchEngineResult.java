package org.csi.yucca.dataservice.metadataapi.model.searchengine.v02;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class SearchEngineResult {
	private SearchEngineResponseHeader responseHeader;
	private SearchEngineResponse response;
	private SearchEngineFacetCounts facet_counts;

	public SearchEngineResult() {
		super();
	}

	public static SearchEngineResult fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, SearchEngineResult.class);
	}

	public SearchEngineResponseHeader getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(SearchEngineResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}

	public SearchEngineResponse getResponse() {
		return response;
	}

	public void setResponse(SearchEngineResponse response) {
		this.response = response;
	}

	public SearchEngineFacetCounts getFacet_counts() {
		return facet_counts;
	}

	public void setFacet_counts(SearchEngineFacetCounts facet_counts) {
		this.facet_counts = facet_counts;
	}

}
