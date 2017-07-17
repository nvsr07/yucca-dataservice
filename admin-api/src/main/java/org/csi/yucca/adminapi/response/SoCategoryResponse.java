package org.csi.yucca.adminapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SoCategoryResponse implements Response{
	
	private Integer idSoCategory;
	private String socategorycode;
	private String description;
	
	public Integer getIdSoCategory() {
		return idSoCategory;
	}
	public void setIdSoCategory(Integer idSoCategory) {
		this.idSoCategory = idSoCategory;
	}
	public String getSocategorycode() {
		return socategorycode;
	}
	public void setSocategorycode(String socategorycode) {
		this.socategorycode = socategorycode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
}
