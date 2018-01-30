package org.csi.yucca.adminapi.request;

public class EcosystemRequest {

	private Integer idEcosystem;
	private String ecosystemcode;
	private String description;
	
	public Integer getIdEcosystem() {
		return idEcosystem;
	}

	public void setIdEcosystem(Integer idEcosystem) {
		this.idEcosystem = idEcosystem;
	}

	public String getEcosystemcode() {
		return ecosystemcode;
	}

	public void setEcosystemcode(String ecosystemcode) {
		this.ecosystemcode = ecosystemcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
