package org.csi.yucca.dataservice.insertdataapi.hdfs.model;

public class GeoPoint {

	private Double[] idxLocation;

	public GeoPoint() {
		super();
	}

	public GeoPoint(Double longitude, Double latitude) {
		super();
		setIdxLocation(new Double[] { longitude, latitude });
	}

	public void setLatitude(Double latitude) {
		if (idxLocation == null)
			idxLocation = new Double[2];
		idxLocation[0] = latitude;
	}

	public void setLongitude(Double longitude) {
		if (idxLocation == null)
			idxLocation = new Double[2];
		idxLocation[1] = longitude;
	}

	public Double[] getIdxLocation() {
		return idxLocation;
	}

	public void setIdxLocation(Double[] idxLocation) {
		this.idxLocation = idxLocation;
	}

	public boolean isValid() {
		return idxLocation != null && idxLocation.length == 2 && idxLocation[0] != null && idxLocation[1] != null;
	}

}
