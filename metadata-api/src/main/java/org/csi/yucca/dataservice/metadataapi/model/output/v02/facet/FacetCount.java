package org.csi.yucca.dataservice.metadataapi.model.output.v02.facet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacetCount {
	private Map<String, FacetField> facetFields;
	private Map<String, List<FacetPivot>> facetPivotList;
	
	

	public FacetCount() {
		super();
	}

	public Map<String, FacetField> getFacetFields() {
		return facetFields;
	}

	public void setFacetFields(Map<String, FacetField> facetFields) {
		this.facetFields = facetFields;
	}
	
	public void addFacetField(String key, FacetField facetField){
		if(this.facetFields==null)
			this.facetFields = new HashMap<String, FacetField>();
		if(!this.facetFields.containsKey(key))
			this.facetFields.put(key, facetField);
	}

	public Map<String, List<FacetPivot>> getFacetPivotList() {
		return facetPivotList;
	}

	public void setFacetPivotList(Map<String, List<FacetPivot>> facetPivotList) {
		this.facetPivotList = facetPivotList;
	}

	public void addFacetPivot(String key, List<FacetPivot> facetPivot){
		if(this.facetPivotList==null)
			this.facetPivotList = new HashMap<String, List<FacetPivot>>();
		if(!this.facetPivotList.containsKey(key))
			this.facetPivotList.put(key, facetPivot);
	}

}
