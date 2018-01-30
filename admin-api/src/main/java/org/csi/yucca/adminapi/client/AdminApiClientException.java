package org.csi.yucca.adminapi.client;


public class AdminApiClientException extends Exception {

	Throwable e;
	
	public AdminApiClientException(Throwable e) {
		this.e = e;
	}

}
