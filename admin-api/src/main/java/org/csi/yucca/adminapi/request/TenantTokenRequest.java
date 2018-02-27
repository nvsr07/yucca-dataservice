package org.csi.yucca.adminapi.request;

public class TenantTokenRequest {

	private String scope;
			
	private String grant_type;

	public TenantTokenRequest(String scope, String grant_type) {
		super();
		this.scope = scope;
		this.grant_type = grant_type;
	}

	public TenantTokenRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}
	
}
