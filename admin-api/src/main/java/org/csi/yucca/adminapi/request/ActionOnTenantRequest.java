package org.csi.yucca.adminapi.request;

public class ActionOnTenantRequest {

	private String action;
	private String tenantCode;
	private String startStep;
	private String endStep;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getTenantCode() {
		return tenantCode;
	}
	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
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
