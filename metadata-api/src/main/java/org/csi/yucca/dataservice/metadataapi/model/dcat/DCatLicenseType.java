package org.csi.yucca.dataservice.metadataapi.model.dcat;

import org.csi.yucca.dataservice.metadataapi.util.DCatSdpHelper;

import com.google.gson.annotations.SerializedName;

public class DCatLicenseType extends DCatObject {

	@SerializedName("foaf:name")
	private String name;
	@SerializedName("owl:versionInfo")
	private String version;
	
	@SerializedName("dcterms:type")
	private IdString dcterms_type;
	
	public void setId(String id) {
		this.id = BASE_ID + "license/" + DCatSdpHelper.cleanForId(id);
	}

	public DCatLicenseType() {
		this.addType("dcterms:LicenseDocument");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public IdString getDcterms_type() {
		return dcterms_type;
	}

	public void setDcterms_type(IdString dcterms_type) {
		this.dcterms_type = dcterms_type;
	}
}
