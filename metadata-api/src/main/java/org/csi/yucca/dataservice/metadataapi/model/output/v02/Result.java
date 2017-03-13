package org.csi.yucca.dataservice.metadataapi.model.output.v02;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.csi.yucca.dataservice.metadataapi.model.output.v02.facet.FacetCount;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.Metadata;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Result {
	private Integer start;
	private Integer count;
	private Integer totalCount;
	private Integer totalPages;
	private Map<String, String> params;
	private List<Metadata> metadata;
	private FacetCount facetCount;

	public Result() {
		super();
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public List<Metadata> getMetadata() {
		return metadata;
	}

	public void setMetadata(List<Metadata> metadata) {
		this.metadata = metadata;
	}

	public FacetCount getFacetCount() {
		return facetCount;
	}

	public void setFacetCount(FacetCount facetCount) {
		this.facetCount = facetCount;
	}

	public void addMetadata(Metadata metadata) {
		if (getMetadata() == null)
			this.metadata = new LinkedList<Metadata>();
		this.metadata.add(metadata);
	}

}
