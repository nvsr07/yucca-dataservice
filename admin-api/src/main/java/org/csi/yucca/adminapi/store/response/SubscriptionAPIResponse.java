package org.csi.yucca.adminapi.store.response;

public class SubscriptionAPIResponse {
	private boolean error;
	private Apis[] apis;

	public SubscriptionAPIResponse() {

	}

	public boolean getError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public Apis[] getApis() {
		return apis;
	}

	public void setApis(Apis[] apis) {
		this.apis = apis;
	}
}
