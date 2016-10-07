package org.csi.yucca.dataservice.metadataapi.model.dcat;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class LicenceTypeDCAT extends TypeDCAT {
	
	private String licenseType = "http://purl.org/adms/licencetype/PublicDomain";
	
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
}
