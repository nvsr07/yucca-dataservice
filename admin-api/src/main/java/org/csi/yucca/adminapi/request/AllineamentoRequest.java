package org.csi.yucca.adminapi.request;

public class AllineamentoRequest {

	private Integer idOrganization;
	private Integer locked;
	private String lastobjectid;

	public Integer getIdOrganization() {
		return idOrganization;
	}

	public void setIdOrganization(Integer idOrganization) {
		this.idOrganization = idOrganization;
	}

	public Integer getLocked() {
		if (this.locked == null) {
			return 0;
		}
		return locked;
	}

	public void setLocked(Integer locked) {
		this.locked = locked;
	}

	public String getLastobjectid() {
		return lastobjectid;
	}

	public void setLastobjectid(String lastobjectid) {
		this.lastobjectid = lastobjectid;
	}
	
}
