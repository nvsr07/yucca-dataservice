package org.csi.yucca.dataservice.metadataapi.model.dcat;

import org.csi.yucca.dataservice.metadataapi.util.DCatSdpHelper;

import com.google.gson.annotations.SerializedName;

public class DCatAgent extends DCatObject {

	@SerializedName("dcterms:identifier")
	private String dcterms_identifier;

	@SerializedName("foaf:name")
	private I18NString name;

	// private String publisherType =
	// "http://purl.org/adms/publishertype/Company";

	public DCatAgent() {
		// id = "01995120019";
		addType("foaf:Agent");
		// type.add("http://dati.gov.it/onto/dcatapit#\"Agent");
		// type.add("http://purl.org/adms/publishertype/Company");

		// foaf_name = new I18NString("it", "CSI PIEMONTE");

	}
	
	public void setId(String id) {
		this.id = BASE_ID + "agent/" + DCatSdpHelper.cleanForId(id);
	}


	public String getDcterms_identifier() {
		return dcterms_identifier;
	}

	public void setDcterms_identifier(String dcterms_identifier) {
		this.dcterms_identifier = dcterms_identifier;
	}

	public I18NString getName() {
		return name;
	}

	public void setName(I18NString name) {
		this.name = name;
	}

}
