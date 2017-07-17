package org.csi.yucca.adminapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SoTypeResponse implements Response{

	private Integer idSoType;
	private String sotypecode;
	private String description;
	
	public Integer getIdSoType() {
		return idSoType;
	}
	public void setIdSoType(Integer idSoType) {
		this.idSoType = idSoType;
	}
	public String getSotypecode() {
		return sotypecode;
	}
	public void setSotypecode(String sotypecode) {
		this.sotypecode = sotypecode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
