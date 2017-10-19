package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;

public class SoCategoryResponse extends Response{
	
	private Integer idSoCategory;
	private String socategorycode;
	private String description;
	
	public SoCategoryResponse(DettaglioSmartobject smartobject) {
		super();
		this.idSoCategory = smartobject.getIdSoCategory();
		this.socategorycode = smartobject.getSocategorycode();
		this.description = smartobject.getDescriptionSoCategory();
	}
	
	public SoCategoryResponse() {
		super();
	}
	
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