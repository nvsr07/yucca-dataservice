package org.csi.yucca.dataservice.metadataapi.model.searchengine.v02;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class SearchEngineResponseHeader {
	private Integer status;
	private Integer QTime;
	private SearchEngineResponseHeaderParams params;

	public static SearchEngineResponseHeader fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, SearchEngineResponseHeader.class);
	}

	public SearchEngineResponseHeader() {
		super();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getQTime() {
		return QTime;
	}

	public void setQTime(Integer qTime) {
		QTime = qTime;
	}

	public SearchEngineResponseHeaderParams getParams() {
		return params;
	}

	public void setParams(SearchEngineResponseHeaderParams params) {
		this.params = params;
	}

}
