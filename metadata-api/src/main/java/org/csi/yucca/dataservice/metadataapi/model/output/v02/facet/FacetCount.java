package org.csi.yucca.dataservice.metadataapi.model.output.v02.facet;

import java.util.HashMap;
import java.util.Map;

public class FacetCount {
	private Map<String, FacetField> facetFields;

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

}
