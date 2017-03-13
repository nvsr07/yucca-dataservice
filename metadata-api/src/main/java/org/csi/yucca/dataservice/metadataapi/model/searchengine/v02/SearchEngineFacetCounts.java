package org.csi.yucca.dataservice.metadataapi.model.searchengine.v02;

import java.util.List;
import java.util.Map;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class SearchEngineFacetCounts {
	private Map<String, List<Object>> facet_fields;

	
	public SearchEngineFacetCounts() {
		super();
	}
	
	public static SearchEngineFacetCounts fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, SearchEngineFacetCounts.class);
	}


	public Map<String, List<Object>> getFacet_fields() {
		return facet_fields;
	}

	public void setFacet_fields(Map<String, List<Object>> facet_fields) {
		this.facet_fields = facet_fields;
	}
	
	
}
