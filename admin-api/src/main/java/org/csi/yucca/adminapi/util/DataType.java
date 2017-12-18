package org.csi.yucca.adminapi.util;

public enum DataType {
	
	INT (1,   "int", "int"),
	LONG  (2,   "long",  "long"),
	DOUBLE   (3,   "double",   "double"),
	FLOAT   (4,   "float",   "float"),
	STRING   (5,   "string",   "string"),
	BOOLEAN   (6,   "boolean",   "boolean"),
	DATE_TIME   (7,   "dateTime",   "dateTime"),
	LONGITUDE   (8,   "longitude",   "longitude"),
	LATITUDE   (9,   "latitude",   "latitude"),
	BINARY   (10,   "binary",   "binary");
	
	private Integer id;
	private String code;
	private String description;
	
	DataType(Integer id, String code, String description){
		this.id = id;
		this.code = code;
		this.description = description;
	}

	public Integer id() {
		return id;
	}

	public String code() {
		return code;
	}

	public String description() {
		return description;
	}
	
	public void checkValue(String value)throws Exception{
		if(this == INT){
			Integer.parseInt(value);
		}
		else if(this == LONG){
			Long.valueOf(value);
		}
		else if(this == DOUBLE){
			Double.valueOf(value);
		}
	}
	
	
	
}