package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;

public class SoTypeResponse extends Response{

	private Integer idSoType;
	private String sotypecode;
	private String description;
	
	public SoTypeResponse(DettaglioSmartobject smartobject) {
		super();
		this.idSoType = smartobject.getIdSoType();
		this.sotypecode = smartobject.getSotypecode();
		this.description = smartobject.getDescriptionSoType();
	}

	public SoTypeResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
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
