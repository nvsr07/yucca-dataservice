package org.csi.yucca.adminapi.util;

public class MapperField {

	private String property;
	private String column;
	
	public MapperField(String property, String column) {
		super();
		this.property = property;
		this.column = column;
	}

	public MapperField(String value) {
		super();
		this.property = value;
		this.column = value;
	}
	
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	
	
}
