package org.csi.yucca.adminapi.request;

import java.util.List;

public class PostValidateSiddhiQueriesRequest {
	
	private List<String> inputStreamDefiniitons;

	private String queryExpressions;
	
	public List<String> getInputStreamDefiniitons() {
		return inputStreamDefiniitons;
	}
	public void setInputStreamDefiniitons(List<String> inputStreamDefiniitons) {
		this.inputStreamDefiniitons = inputStreamDefiniitons;
	}
	public String getQueryExpressions() {
		return queryExpressions;
	}
	public void setQueryExpressions(String queryExpressions) {
		this.queryExpressions = queryExpressions;
	}
	
}
