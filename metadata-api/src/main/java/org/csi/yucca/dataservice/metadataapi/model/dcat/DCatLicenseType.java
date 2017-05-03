package org.csi.yucca.dataservice.metadataapi.model.dcat;

import com.google.gson.annotations.SerializedName;

public class DCatLicenseType extends DCatObject {

	@SerializedName("foaf:name")
	private String name;
	@SerializedName("owl:versionInfo")
	private String version;

	public DCatLicenseType() {
		this.addType("dct:LicenseDocument");
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
}
