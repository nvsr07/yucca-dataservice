package org.csi.yucca.adminapi.model.join;

import org.csi.yucca.adminapi.model.Smartobject;

public class DettaglioSmartobject extends Smartobject{

	private Float lat;
	private Float lon;
	private Float elevation;
	private String room;
	private String building;
	private Float floor;
	private String address;
	private String city;
	private String country;
	private String placegeometry;
	private Integer idSoPosition;
	  
	private String organizationcode;
	private String descriptionOrganization;
	
	private String statuscode;
	private String descriptionStatus;

	private String sotypecode;
	private String descriptionSoType;

	private String socategorycode;
	private String descriptionSoCategory;
	
	private String supplytype;
	private String descriptionSupplytype;
	
	private String exposuretype;
	private String descriptionExposuretype;
	
	private String locationtype;
	private String descriptionLocationtype;
	
	public Integer getIdSoPosition() {
		return idSoPosition;
	}
	public void setIdSoPosition(Integer idSoPosition) {
		this.idSoPosition = idSoPosition;
	}

	public Float getLat() {
		return lat;
	}
	public void setLat(Float lat) {
		this.lat = lat;
	}
	public Float getLon() {
		return lon;
	}
	public void setLon(Float lon) {
		this.lon = lon;
	}
	public Float getElevation() {
		return elevation;
	}
	public void setElevation(Float elevation) {
		this.elevation = elevation;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	public Float getFloor() {
		return floor;
	}
	public void setFloor(Float floor) {
		this.floor = floor;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPlacegeometry() {
		return placegeometry;
	}
	public void setPlacegeometry(String placegeometry) {
		this.placegeometry = placegeometry;
	}
	public String getOrganizationcode() {
		return organizationcode;
	}
	public void setOrganizationcode(String organizationcode) {
		this.organizationcode = organizationcode;
	}
	public String getDescriptionOrganization() {
		return descriptionOrganization;
	}
	public void setDescriptionOrganization(String descriptionOrganization) {
		this.descriptionOrganization = descriptionOrganization;
	}
	public String getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}
	public String getDescriptionStatus() {
		return descriptionStatus;
	}
	public void setDescriptionStatus(String descriptionStatus) {
		this.descriptionStatus = descriptionStatus;
	}
	public String getSotypecode() {
		return sotypecode;
	}
	public void setSotypecode(String sotypecode) {
		this.sotypecode = sotypecode;
	}
	public String getDescriptionSoType() {
		return descriptionSoType;
	}
	public void setDescriptionSoType(String descriptionSoType) {
		this.descriptionSoType = descriptionSoType;
	}
	public String getSocategorycode() {
		return socategorycode;
	}
	public void setSocategorycode(String socategorycode) {
		this.socategorycode = socategorycode;
	}
	public String getDescriptionSoCategory() {
		return descriptionSoCategory;
	}
	public void setDescriptionSoCategory(String descriptionSoCategory) {
		this.descriptionSoCategory = descriptionSoCategory;
	}
	public String getSupplytype() {
		return supplytype;
	}
	public void setSupplytype(String supplytype) {
		this.supplytype = supplytype;
	}
	public String getDescriptionSupplytype() {
		return descriptionSupplytype;
	}
	public void setDescriptionSupplytype(String descriptionSupplytype) {
		this.descriptionSupplytype = descriptionSupplytype;
	}
	public String getExposuretype() {
		return exposuretype;
	}
	public void setExposuretype(String exposuretype) {
		this.exposuretype = exposuretype;
	}
	public String getDescriptionExposuretype() {
		return descriptionExposuretype;
	}
	public void setDescriptionExposuretype(String descriptionExposuretype) {
		this.descriptionExposuretype = descriptionExposuretype;
	}
	public String getLocationtype() {
		return locationtype;
	}
	public void setLocationtype(String locationtype) {
		this.locationtype = locationtype;
	}
	public String getDescriptionLocationtype() {
		return descriptionLocationtype;
	}
	public void setDescriptionLocationtype(String descriptionLocationtype) {
		this.descriptionLocationtype = descriptionLocationtype;
	}
	
}
