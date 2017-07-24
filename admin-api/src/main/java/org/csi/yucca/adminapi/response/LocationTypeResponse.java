package org.csi.yucca.adminapi.response;

public class LocationTypeResponse extends Response{

	private Integer idLocationType;
	private String locationtype;
	private String description;
	
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
