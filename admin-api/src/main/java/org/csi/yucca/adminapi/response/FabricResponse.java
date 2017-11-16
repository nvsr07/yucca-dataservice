package org.csi.yucca.adminapi.response;


public class FabricResponse extends Response {

	private Integer success;
	private String message;

	public FabricResponse() {
		super();
	}

	public FabricResponse(Integer success, String message) {
		this.success = success;
		this.message = message;
	}

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
