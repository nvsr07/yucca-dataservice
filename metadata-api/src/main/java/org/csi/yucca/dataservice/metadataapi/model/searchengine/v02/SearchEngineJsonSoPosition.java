package org.csi.yucca.dataservice.metadataapi.model.searchengine.v02;

public class SearchEngineJsonSoPosition {
	private Double lon;
	private Double lat;
	private Double elevation;
	private String floor;
	private String room;

	public SearchEngineJsonSoPosition() {
		super();
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getElevation() {
		return elevation;
	}

	public void setElevation(Double elevation) {
		this.elevation = elevation;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}
}
