package org.csi.yucca.dataservice.metadataapi.model.ckan;

public class Extra {
	private String key;
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String k) {
		key = k;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String v) {
		value = v;
	}

	public Extra() {
	}

	public Extra(String k, String v) {
		key = k;
		value = v;
	}

	public String toString() {
		return "<Extra:" + this.getKey() + "=" + this.getValue() + ">";
	}
}
