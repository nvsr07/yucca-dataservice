package org.csi.yucca.dataservice.metadataapi.model.dcat;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class VCardTypeDCAT extends TypeDCAT {
	
	public VCardTypeDCAT(){
		
	}

	public String getjson() {
		// TODO Auto-generated method stub
		this.setType("v:VCard");
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this, TypeDCAT.class);
	}
}