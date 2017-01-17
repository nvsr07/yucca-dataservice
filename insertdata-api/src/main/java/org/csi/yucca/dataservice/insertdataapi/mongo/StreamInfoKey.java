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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sensor == null) ? 0 : sensor.hashCode());
		result = prime * result + ((stream == null) ? 0 : stream.hashCode());
		result = prime * result + ((tenant == null) ? 0 : tenant.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StreamInfoKey other = (StreamInfoKey) obj;
		if (sensor == null) {
			if (other.sensor != null)
				return false;
		} else if (!sensor.equals(other.sensor))
			return false;
		if (stream == null) {
			if (other.stream != null)
				return false;
		} else if (!stream.equals(other.stream))
			return false;
		if (tenant == null) {
			if (other.tenant != null)
				return false;
		} else if (!tenant.equals(other.tenant))
			return false;
		return true;
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
