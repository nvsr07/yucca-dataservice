package org.csi.yucca.adminapi.util;

public enum Languages {
	
	IT("langIT"),
	EN("langEN");
	
	private String value;
	
	Languages(String value){
		this.value = value;
	}
	
	public String value(){
		return this.value;
	}
	
}
