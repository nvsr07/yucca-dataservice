package org.csi.yucca.dataservice.metadataapi.util;

import java.util.HashMap;
import java.util.Map;

public class FacetParams {
	private String[] facetFields;
	private String facetPrefix;
	private String facetSort;
	private String facetContains;
	private String facetContainsIgnoreCase;
	private String facetLimit;
	private String facetOffset;
	private String facetMinCount;
	private String facetMissing;

	public FacetParams() {
		super();
	}

	public FacetParams(String facetFields, String facetPrefix, String facetSort, String facetContains, String facetContainsIgnoreCase, String facetLimit, String facetOffset,
			String facetMinCount, String facetMissing) {
		super();
		setFacetFields(facetFields);
		this.setFacetPrefix(facetPrefix);
		this.facetSort = facetSort;
		this.facetContains = facetContains;
		this.facetContainsIgnoreCase = facetContainsIgnoreCase;
		this.facetLimit = facetLimit;
		this.facetOffset = facetOffset;
		this.facetMinCount = facetMinCount;
		this.facetMissing = facetMissing;
	}

	public void setFacetContains(String facetContains) {
		this.facetContains = facetContains;
	}

	public String getFacetContains() {
		return facetContains;
	}

	public String getFacetContainsIgnoreCase() {
		return facetContainsIgnoreCase;
	}

	public void setFacetContainsIgnoreCase(String facetContainsIgnoreCase) {
		this.facetContainsIgnoreCase = facetContainsIgnoreCase;
	}

	public String[] getFacetFields() {
		return facetFields;
	}

	public void setFacetFields(String[] facetField) {
		this.facetFields = facetField;
	}

	public void setFacetFields(String facetFields) {
		if (facetFields != null)
			this.facetFields = facetFields.split(",");
	}

	public String getFacetPrefix() {
		return facetPrefix;
	}

	public void setFacetPrefix(String facetPrefix) {
		this.facetPrefix = facetPrefix;
	}

	public String getFacetSort() {
		return facetSort;
	}

	public void setFacetSort(String facetSort) {
		this.facetSort = facetSort;
	}

	public String getFacetLimit() {
		return facetLimit;
	}

	public void setFacetLimit(String facetLimit) {
		this.facetLimit = facetLimit;
	}

	public String getFacetOffset() {
		return facetOffset;
	}

	public void setFacetOffset(String facetOffset) {
		this.facetOffset = facetOffset;
	}

	public String getFacetMinCount() {
		return facetMinCount;
	}

	public void setFacetMinCount(String facetMinCount) {
		this.facetMinCount = facetMinCount;
	}

	public String getFacetMissing() {
		return facetMissing;
	}

	public void setFacetMissing(String facetMissing) {
		this.facetMissing = facetMissing;
	}

	public String toSorlParams() {
		StringBuffer params = new StringBuffer("");
		if (facetFields != null) {
			params.append("&facet=true");
			for (String facetField : facetFields) {
				params.append("&facet.field=" + facetField);
			}

			if (getFacetContains() != null)
				params.append("&facet.contains=" + getFacetContains());
			if (getFacetContainsIgnoreCase() != null)
				params.append("&facet.contains.ignoreCase=" + getFacetContainsIgnoreCase());
			if (getFacetPrefix() != null)
				params.append("&facet.prefix=" + getFacetPrefix());
			if (getFacetSort() != null)
				params.append("&facet.sort=" + getFacetSort());
			if (getFacetLimit() != null)
				params.append("&facet.limit=" + getFacetLimit());
			if (getFacetOffset() != null)
				params.append("&facet.offset=" + getFacetOffset());
			if (getFacetMinCount() != null)
				params.append("&facet.mincount=" + getFacetMinCount());
			if (getFacetMissing() != null)
				params.append("&facet.missing=" + getFacetMissing());
		}
		return params.toString();
	}
	
	public Map<String, String> getParamsMap(){
		Map<String, String> paramsMap = new HashMap<String, String>();
		if (facetFields != null) {
			for (String facetField : facetFields) {
				paramsMap.put("facet.field",facetField);
			}

			if (getFacetContains() != null)
				paramsMap.put("facet.contains",getFacetContains());
			if (getFacetContainsIgnoreCase() != null)
				paramsMap.put("facet.contains.ignoreCase",getFacetContainsIgnoreCase());
			if (getFacetPrefix() != null)
				paramsMap.put("facet.prefix",getFacetPrefix());
			if (getFacetSort() != null)
				paramsMap.put("facet.sort",getFacetSort());
			if (getFacetLimit() != null)
				paramsMap.put("facet.limit",getFacetLimit());
			if (getFacetOffset() != null)
				paramsMap.put("facet.offset",getFacetOffset());
			if (getFacetMinCount() != null)
				paramsMap.put("facet.mincount",getFacetMinCount());
			if (getFacetMissing() != null)
				paramsMap.put("facet.missing",getFacetMissing());
		}
		return paramsMap;
	}
	
	
}