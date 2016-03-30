package org.csi.yucca.dataservice.metadataapi.service.response;

public class SimpleResponse extends AbstractResponse{

	private String message;
	private Object object;
	public SimpleResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
}
