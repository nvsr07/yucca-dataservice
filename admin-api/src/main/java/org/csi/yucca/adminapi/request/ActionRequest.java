package org.csi.yucca.adminapi.request;

public class ActionRequest {

	private String action;
	private String startStep;
	private String endStep;
	private String status;
	private String description;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getStartStep() {
		return startStep;
	}
	public void setStartStep(String startStep) {
		this.startStep = startStep;
	}
	public String getEndStep() {
		return endStep;
	}
	public void setEndStep(String endStep) {
		this.endStep = endStep;
	}	

	
	
}
