package org.csi.yucca.dataservice.metadataapi.model.output;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Smartobject {

	public static final String SMARTOBJECT_TYPE_TWITTER = "Feed Tweet";
	public static final String SMARTOBJECT_TYPE_DEVICE = "device";
	public static final String SMARTOBJECT_TYPE_APPLICATION= "application";


	private String type;
	private String category;
	private String code;
	private String name;
	private String description;
	private Boolean mobile; // FIXME serve?
	private String esposition;
	private Double longitude;
	private Double latitude;
	private Double altitude;
	private String building;
	private String floor;
	private String room;

	private Integer twtMaxStreams;
	private Integer twtRatePercentage;
	private Integer twtCount;
	private Double twtGeolocLat;
	private Double twtGeolocLon;
	private Double twtGeolocRadius;
	private String twtQuery;
	private String twtLang;


	public Smartobject() {
		super();
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getMobile() {
		return mobile;
	}

	public void setMobile(Boolean mobile) {
		this.mobile = mobile;
	}

	public String getEsposition() {
		return esposition;
	}

	public void setEsposition(String esposition) {
		this.esposition = esposition;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getAltitude() {
		return altitude;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
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

	public Integer getTwtMaxStreams() {
		return twtMaxStreams;
	}

	public void setTwtMaxStreams(Integer twtMaxStreams) {
		this.twtMaxStreams = twtMaxStreams;
	}

	public Integer getTwtRatePercentage() {
		return twtRatePercentage;
	}

	public void setTwtRatePercentage(Integer twtRatePercentage) {
		this.twtRatePercentage = twtRatePercentage;
	}

	public Integer getTwtCount() {
		return twtCount;
	}

	public void setTwtCount(Integer twtCount) {
		this.twtCount = twtCount;
	}

	public Double getTwtGeolocLat() {
		return twtGeolocLat;
	}

	public void setTwtGeolocLat(Double twtGeolocLat) {
		this.twtGeolocLat = twtGeolocLat;
	}

	public Double getTwtGeolocLon() {
		return twtGeolocLon;
	}

	public void setTwtGeolocLon(Double twtGeolocLon) {
		this.twtGeolocLon = twtGeolocLon;
	}

	public Double getTwtGeolocRadius() {
		return twtGeolocRadius;
	}

	public void setTwtGeolocRadius(Double twtGeolocRadius) {
		this.twtGeolocRadius = twtGeolocRadius;
	}

	public String getTwtQuery() {
		return twtQuery;
	}

	public void setTwtQuery(String twtQuery) {
		this.twtQuery = twtQuery;
	}

	public String getTwtLang() {
		return twtLang;
	}

	public void setTwtLang(String twtLang) {
		this.twtLang = twtLang;
	}
}
