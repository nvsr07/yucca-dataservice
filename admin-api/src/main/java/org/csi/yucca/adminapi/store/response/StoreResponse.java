package org.csi.yucca.adminapi.store.response;

public class StoreResponse {

	private Boolean error;
	private String message;

	public StoreResponse() {
		super();
	}

	public StoreResponse(Boolean error, String message) {
		super();
		this.setError(error);
		this.setMessage(message);
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
