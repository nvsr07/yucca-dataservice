package org.csi.yucca.dataservice.metadataapi.model.dcat;

import java.util.LinkedList;
import java.util.List;

import org.csi.yucca.dataservice.metadataapi.util.DCatSdpHelper;

import com.google.gson.annotations.SerializedName;

public class DCatAgent extends DCatObject {

	@SerializedName("dcterms:identifier")
	private String dcterms_identifier;

	@SerializedName("foaf:name")
	private String name;

	@SerializedName("dcterms:type")
	private List<IdString> dcterms_types;

	// private String publisherType =
	// "http://purl.org/adms/publishertype/Company";

	public DCatAgent() {
		// id = "01995120019";
		addType("foaf:Agent");
		addType("dcatapit:Agent");
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addDcterms_type(IdString dcterms_type) {
		if (getDcterms_types() == null)
			setDcterms_types(new LinkedList<IdString>());
		getDcterms_types().add(dcterms_type);
	}

	public List<IdString> getDcterms_types() {
		return dcterms_types;
	}

	public void setDcterms_types(List<IdString> dcterms_types) {
		this.dcterms_types = dcterms_types;
	}
}
