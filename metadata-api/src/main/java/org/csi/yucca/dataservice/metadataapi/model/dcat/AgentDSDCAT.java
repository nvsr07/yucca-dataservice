package org.csi.yucca.dataservice.metadataapi.model.dcat;

public class AgentDSDCAT {

	private String name = null;
	private String type = "foaf:Agent";
	private String id = "http://www.csipiemonte.it";
	
	public AgentDSDCAT(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}
}
