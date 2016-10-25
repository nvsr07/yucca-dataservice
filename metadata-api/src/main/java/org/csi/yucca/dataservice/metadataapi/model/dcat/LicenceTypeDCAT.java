package org.csi.yucca.dataservice.metadataapi.model.dcat;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class LicenceTypeDCAT extends TypeDCAT {
	
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	private String licenseType = "http://purl.org/adms/licencetype/PublicDomain";
	private String name = "";
	private String version = "";
	
	public String getLicenseType() {
		return licenseType;
	}

	public LicenceTypeDCAT(){
		
	}
	
	public String getjson() {
		this.setType("dct:LicenseDocument");
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this, TypeDCAT.class);
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
