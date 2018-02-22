package org.csi.yucca.adminapi.model;

public class Mailtemplates {
	
	private Integer idTenantType;
	private String mailbody;
	private String mailobject;
	
	public Integer getIdTenantType() {
		return idTenantType;
	}
	public void setIdTenantType(Integer idTenantType) {
		this.idTenantType = idTenantType;
	}
	public String getMailbody() {
		return mailbody;
	}
	public void setMailbody(String mailbody) {
		this.mailbody = mailbody;
	}
	public String getMailobject() {
		return mailobject;
	}
	public void setMailobject(String mailobject) {
		this.mailobject = mailobject;
	}

}
