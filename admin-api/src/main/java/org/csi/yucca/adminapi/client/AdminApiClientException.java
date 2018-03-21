package org.csi.yucca.adminapi.client;

public class AdminApiClientException extends Exception {

	private static final long serialVersionUID = 6265043452983045465L;

	Throwable e;
	
	private int httpStatusCode;
	
	public AdminApiClientException(Throwable e) {
		this.e = e;
	}

	public AdminApiClientException(Throwable e, int httpStatusCode) {
		this(e);
		this.httpStatusCode = httpStatusCode;
	}

	public AdminApiClientException(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}
}
