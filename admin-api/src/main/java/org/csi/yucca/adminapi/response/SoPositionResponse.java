package org.csi.yucca.adminapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public class SoPositionResponse extends Response {

	private Integer idSmartObject;
	private Float lat;
	private Float lon;
	private Float elevation;
	private Float floor;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String room;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String building;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String address;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String city;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String country;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String placegeometry;

	public Integer getIdSmartObject() {
		return idSmartObject;
	}

	public void setIdSmartObject(Integer idSmartObject) {
		this.idSmartObject = idSmartObject;
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

	public Float getFloor() {
		return floor;
	}

	public void setFloor(Float floor) {
		this.floor = floor;
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
}
