package org.csi.yucca.dataservice.ingest.model.metadata;

import java.util.List;

import org.csi.yucca.dataservice.ingest.util.json.JSonHelper;

import com.google.gson.Gson;

public class StreamField extends AbstractEntity {
	
	private String virtualEntityName;
	private String virtualEntityDescription;
	private String virtualEntityCode;
	private String virtualEntityType;
	private String virtualEntityCategory;
	private String domainStream;
	private String virtualEntitySlug;

	public String getVirtualEntityName() {
		return virtualEntityName;
	}

	public void setVirtualEntityName(String virtualEntityName) {
		this.virtualEntityName = virtualEntityName;
	}

	public String getVirtualEntityDescription() {
		return virtualEntityDescription;
	}

	public void setVirtualEntityDescription(String virtualEntityDescription) {
		this.virtualEntityDescription = virtualEntityDescription;
	}

	public String getVirtualEntityCode() {
		return virtualEntityCode;
	}

	public void setVirtualEntityCode(String virtualEntityCode) {
		this.virtualEntityCode = virtualEntityCode;
	}

	public String getVirtualEntityType() {
		return virtualEntityType;
	}

	public void setVirtualEntityType(String virtualEntityType) {
		this.virtualEntityType = virtualEntityType;
	}

	public String getVirtualEntityCategory() {
		return virtualEntityCategory;
	}

	public void setVirtualEntityCategory(String virtualEntityCategory) {
		this.virtualEntityCategory = virtualEntityCategory;
	}

	public String getDomainStream() {
		return domainStream;
	}

	public void setDomainStream(String domainStream) {
		this.domainStream = domainStream;
	}

	public String getVirtualEntitySlug() {
		return virtualEntitySlug;
	}

	public void setVirtualEntitySlug(String virtualEntitySlug) {
		this.virtualEntitySlug = virtualEntitySlug;
	}

	public static StreamField fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, StreamField.class);
	}

	public StreamField() {
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}


}