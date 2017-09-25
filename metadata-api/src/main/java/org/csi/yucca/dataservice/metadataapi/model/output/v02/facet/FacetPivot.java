package org.csi.yucca.dataservice.metadataapi.model.output.v02.facet;

import java.util.LinkedList;
import java.util.List;

import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineFacetPivot;

public class FacetPivot {
	private String field;
	private String value;
	private Integer count;
	private List<FacetPivot> pivot;

	public FacetPivot() {
		super();
	}

	public FacetPivot(SearchEngineFacetPivot searchEngineFacetPivot) {
		this.field = searchEngineFacetPivot.getField();
		this.value = searchEngineFacetPivot.getValue();
		this.count = searchEngineFacetPivot.getCount();
		if (searchEngineFacetPivot.getPivot() != null) {
			this.pivot = new LinkedList<FacetPivot>();
			for (SearchEngineFacetPivot searchEngineFacetPivotInner : searchEngineFacetPivot.getPivot()) {
				this.pivot.add(new FacetPivot(searchEngineFacetPivotInner));
			}
		}
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

	public List<FacetPivot> getPivot() {
		return pivot;
	}

	public void setPivot(List<FacetPivot> pivot) {
		this.pivot = pivot;
	}

}
