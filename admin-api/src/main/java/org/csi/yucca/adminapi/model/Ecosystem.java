package org.csi.yucca.adminapi.model;

public class Ecosystem {

	private Integer idEcosystem;
	private String ecosystemcode;
	private String description;
	
	public Ecosystem(Integer idEcosystem, String ecosystemcode, String description) {
		super();
		this.idEcosystem = idEcosystem;
		this.ecosystemcode = ecosystemcode;
		this.description = description;
	}
	public Ecosystem() {
		super();
		// TODO Auto-generated constructor stub
	}
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
