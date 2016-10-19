package org.csi.yucca.dataservice.metadataapi.model.dcat;

public class AgentDCAT {

	private String name = "CSI PIEMONTE";
	private String type = "foaf:Agent";
	private String id = "01995120019";
	private String publisherType = "http://purl.org/adms/publishertype/Company";
	
	public AgentDCAT(){
		
	}

	public String getName() {
		return name;
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
