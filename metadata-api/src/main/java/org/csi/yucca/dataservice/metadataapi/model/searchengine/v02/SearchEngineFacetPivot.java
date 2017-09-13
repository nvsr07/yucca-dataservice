package org.csi.yucca.dataservice.metadataapi.model.searchengine.v02;

import java.util.List;

public class SearchEngineFacetPivot {
	private String field;
	private String value;
	private Integer count;
	private List<SearchEngineFacetPivot> pivot;
	
	public SearchEngineFacetPivot() {
		super();
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<SearchEngineFacetPivot> getPivot() {
		return pivot;
	}

	public void setPivot(List<SearchEngineFacetPivot> pivot) {
		this.pivot = pivot;
	}

	
	
}
