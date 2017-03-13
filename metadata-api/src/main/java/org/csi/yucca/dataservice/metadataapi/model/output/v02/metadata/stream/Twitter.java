package org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.stream;

public class Twitter {
	private Integer twtMaxStreams;
	private Integer twtRatePercentage;
	private Integer twtCount;
	private Double twtGeolocLat;
	private Double twtGeolocLon;
	private Double twtGeolocRadius;
	private String twtQuery;
	private String twtLang;
	private String twtGeolocUnit;
	private String twtLocale;
	private String twtResultType;
	private String twtUntil;
	private String twtLastSearchId;

	public Twitter() {
		super();
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

	public String getTwtGeolocUnit() {
		return twtGeolocUnit;
	}

	public void setTwtGeolocUnit(String twtGeolocUnit) {
		this.twtGeolocUnit = twtGeolocUnit;
	}

	public String getTwtLocale() {
		return twtLocale;
	}

	public void setTwtLocale(String twtLocale) {
		this.twtLocale = twtLocale;
	}

	public String getTwtResultType() {
		return twtResultType;
	}

	public void setTwtResultType(String twtResultType) {
		this.twtResultType = twtResultType;
	}

	public String getTwtUntil() {
		return twtUntil;
	}

	public void setTwtUntil(String twtUntil) {
		this.twtUntil = twtUntil;
	}

	public String getTwtLastSearchId() {
		return twtLastSearchId;
	}

	public void setTwtLastSearchId(String twtLastSearchId) {
		this.twtLastSearchId = twtLastSearchId;
	}

	public void setTwtRatePercentage(String twtRatePercentage) {
		if (twtRatePercentage != null) {
			try {
				this.twtRatePercentage = new Integer(twtRatePercentage);
			} catch (Exception e) {
			}
		} else
			this.twtRatePercentage = null;
	}

	public void setTwtCount(String twtCount) {
		if (twtCount != null) {
			try {
				this.twtCount = new Integer(twtCount);
			} catch (Exception e) {
			}
		} else
			this.twtCount = null;
	}

	public void setTwtGeolocLat(String twtGeolocLat) {
		if (twtGeolocLat != null) {
			try {
				this.twtGeolocLat = new Double(twtGeolocLat);
			} catch (Exception e) {
			}
		} else
			this.twtGeolocLat = null;
	}

	public void setTwtGeolocLon(String twtGeolocLon) {
		if (twtGeolocLon != null) {
			try {
				this.twtGeolocLon = new Double(twtGeolocLon);
			} catch (Exception e) {
			}
		} else
			this.twtGeolocLon = null;
	}

	public void setTwtGeolocRadius(String twtGeolocRadius) {
		if (twtGeolocRadius != null) {
			try {
				this.twtGeolocRadius = new Double(twtGeolocRadius);
			} catch (Exception e) {
			}
		} else
			this.twtGeolocRadius = null;
	}
}
