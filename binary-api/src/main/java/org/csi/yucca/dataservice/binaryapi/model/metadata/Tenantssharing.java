package org.csi.yucca.dataservice.binaryapi.model.metadata;

import org.csi.yucca.dataservice.binaryapi.model.metadata.AbstractEntity;
import org.csi.yucca.dataservice.binaryapi.model.metadata.Tenantsharing;
import org.csi.yucca.dataservice.binaryapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Tenantssharing extends AbstractEntity {
	private Tenantsharing[] tenantsharing;

	public Tenantssharing() {
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public Tenantsharing[] getTenantsharing() {
		return tenantsharing;
	}

	public void setTenantsharing(Tenantsharing[] tenantsharing) {
		this.tenantsharing = tenantsharing;
	}


}
