package org.csi.yucca.dataservice.metadataapi.model.dcat;

public class AgentDCAT {

	private String name = null;
	private String type = "foaf:Agent";
	private String id = "http://spcdata.digitpa.gov.it/Amministrazione/agid";
	private String publisherType = "http://purl.org/adms/publishertype/NationalAuthority";
	
	public AgentDCAT(){
		
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

	public String getPublisherType() {
		return publisherType;
	}
}
