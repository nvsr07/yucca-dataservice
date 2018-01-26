package org.csi.yucca.adminapi.util;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class WebServiceResponse {

	private int statusCode;
	private String message;
	
	public WebServiceResponse(CloseableHttpResponse closeableHttpResponse) {
		super();
		
		this.statusCode = closeableHttpResponse.getStatusLine().getStatusCode();

		try {
			this.message = EntityUtils.toString(closeableHttpResponse.getEntity());
		} catch (ParseException | IOException e) {
			e.printStackTrace();
			this.message = "";
		}
		
	}

	public WebServiceResponse() {
		super();
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
}
