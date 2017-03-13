package org.csi.yucca.dataservice.metadataapi.model.output.v02.facet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacetField {
	private Map<String, Double> facetItems;

	public FacetField() {
		super();
	}

	public FacetField(List<Object> facetFieldValues) {
		if (facetFieldValues != null) {
			for (int i = 0; i < facetFieldValues.size(); i += 2) {
				addItem((String) facetFieldValues.get(i), (Double) facetFieldValues.get(i + 1));

			}
		}

	}

	private void addItem(String key, Double value) {
		if (this.facetItems == null)
			this.facetItems = new HashMap<String, Double>();
		if (!this.facetItems.containsKey(key))
			this.facetItems.put(key, value);

	}

	public Map<String, Double> getFacetItems() {
		return facetItems;
	}

	public void setFacetItems(Map<String, Double> facetItems) {
		this.facetItems = facetItems;
	}

}
