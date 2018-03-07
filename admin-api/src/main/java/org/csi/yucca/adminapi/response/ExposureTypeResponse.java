package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ExposureTypeResponse extends Response {

	private Integer idExposureType;
	private String exposuretype;
	private String description;
	
	@JsonIgnore
	public boolean isEmpty(){
		return this.idExposureType == null && this.exposuretype == null && this.description == null;
	}
	
	public ExposureTypeResponse(DettaglioSmartobject smartobject) {
		super();
		this.idExposureType = smartobject.getIdExposureType();
		this.exposuretype = smartobject.getExposuretype();
		this.description = smartobject.getDescriptionExposuretype();
	}

	public ExposureTypeResponse() {
		super();
	}

	public Integer getIdExposureType() {
		return idExposureType;
	}

	public void setIdExposureType(Integer idExposureType) {
		this.idExposureType = idExposureType;
	}

	public String getExposuretype() {
		return exposuretype;
	}

	public void setExposuretype(String exposuretype) {
		this.exposuretype = exposuretype;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
