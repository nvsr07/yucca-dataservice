package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;

public class LocationTypeResponse extends Response{

	private Integer idLocationType;
	private String locationtype;
	private String description;
	
	public LocationTypeResponse(DettaglioSmartobject smartobject) {
		super();
		this.idLocationType = smartobject.getIdLocationType();
		this.locationtype = smartobject.getLocationtype();
		this.description = smartobject.getDescriptionLocationtype();
	}
	
	public LocationTypeResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Integer getIdLocationType() {
		return idLocationType;
	}
	public void setIdLocationType(Integer idLocationType) {
		this.idLocationType = idLocationType;
	}
	public String getLocationtype() {
		return locationtype;
	}
	public void setLocationtype(String locationtype) {
		this.locationtype = locationtype;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	

}
