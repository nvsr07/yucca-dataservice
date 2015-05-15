package org.csi.yucca.dataservice.ingest.model.metadata;

import org.csi.yucca.dataservice.ingest.model.metadata.AbstractEntity;
import org.csi.yucca.dataservice.ingest.model.metadata.Tenantsharing;
import org.csi.yucca.dataservice.ingest.util.json.JSonHelper;

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
