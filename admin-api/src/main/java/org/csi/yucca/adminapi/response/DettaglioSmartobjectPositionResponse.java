package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DettaglioSmartobjectPositionResponse extends Response {

	private String address;
	private String building;
	private String city;
	private String country;
	private String placegeometry;
	private String room;
	private Float lat;
	private Float lon;
	private Float elevation;
	private Float floor;
	
	@JsonIgnore
	public boolean isEmpty(){
		return this.address == null && this.building == null && this.city == null && this.country == null && this.placegeometry == null && this.room == null && this.lat == null && this.lon == null && this.elevation == null && this.floor == null;
	}
	
	public DettaglioSmartobjectPositionResponse(DettaglioSmartobject smartobject) {
		super();
		this.address = smartobject.getAddress();
		this.building = smartobject.getBuilding();
		this.city = smartobject.getCity();
		this.country = smartobject.getCountry();
		this.placegeometry = smartobject.getPlacegeometry();
		this.room = smartobject.getRoom();
		this.lat = smartobject.getLat();
		this.lon = smartobject.getLon();
		this.elevation = smartobject.getElevation();
		this.floor = smartobject.getFloor();
	}
	
	public DettaglioSmartobjectPositionResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
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
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
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

	
	
}
