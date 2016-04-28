package org.csi.yucca.dataservice.metadataapi.model.store.input;

import org.csi.yucca.dataservice.metadataapi.util.Constants;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class QueryData {

	private String action;
	private String query;
	private Integer start;
	private Integer end;

	public QueryData() {
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Integer getStart() {
		if (start == null)
			start = 0;
		return start;
	}

	public void setStart(Integer start) {
		if (start == null)
			start = 0;
		this.start = start;
	}

	public Integer getEnd() {
		if (end == null)
			end = Constants.SEARCH_MAX_RESULT;
		return end;
	}

	public void setEnd(Integer end) {
		if (end == null)
			end = Constants.SEARCH_MAX_RESULT;
		this.end = end;
	}

}
