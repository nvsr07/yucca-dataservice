package org.csi.yucca.adminapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExposureTypeResponse implements Response {

	private int idExposureType;
	private String exposuretype;
	private String description;

	public int getIdExposureType() {
		return idExposureType;
	}

	public void setIdExposureType(int idExposureType) {
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
