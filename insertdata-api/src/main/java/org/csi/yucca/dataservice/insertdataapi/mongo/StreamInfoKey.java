package org.csi.yucca.dataservice.insertdataapi.mongo;

import java.io.Serializable;

public class StreamInfoKey implements Serializable{
	private String tenant;
	private String stream;
	private String sensor;
	
	
	
	public StreamInfoKey(String tenant, String stream, String sensor) {
		super();
		this.tenant = tenant;
		this.stream = stream;
		this.sensor = sensor;
	}
	
	
	
	@Override
	public String toString() {
		return "StreamInfoKey [tenant=" + tenant + ", stream=" + stream
				+ ", sensor=" + sensor + "]";
	}



	public String getTenant() {
		return tenant;
	}
	public String getStream() {
		return stream;
	}
	public String getSensor() {
		return sensor;
	}
	
	
	

	
	
}
