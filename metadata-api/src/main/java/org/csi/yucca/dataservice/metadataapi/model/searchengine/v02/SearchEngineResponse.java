package org.csi.yucca.dataservice.metadataapi.model.searchengine.v02;

import java.util.List;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class SearchEngineResponse {

	private Integer numFound;
	private Integer start;
	private Double maxScore;
	private List<SearchEngineMetadata> docs;

	public SearchEngineResponse() {
		super();
	}

	public static SearchEngineResponse fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, SearchEngineResponse.class);
	}

	public Integer getNumFound() {
		return numFound;
	}

	public void setNumFound(Integer numFound) {
		this.numFound = numFound;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Double getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(Double maxScore) {
		this.maxScore = maxScore;
	}

	public List<SearchEngineMetadata> getDocs() {
		return docs;
	}

	public void setDocs(List<SearchEngineMetadata> docs) {
		this.docs = docs;
	}

}
