package org.csi.yucca.adminapi.model;

import org.csi.yucca.adminapi.request.PhenomenonRequest;

public class Phenomenon {

	private Integer idPhenomenon;
	private String phenomenonname;
	private String phenomenoncetegory;

	public Phenomenon(Integer idPhenomenon, String phenomenonname, String phenomenoncetegory) {
		super();
		this.idPhenomenon = idPhenomenon;
		this.phenomenonname = phenomenonname;
		this.phenomenoncetegory = phenomenoncetegory;
	}

	public Phenomenon() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getIdPhenomenon() {
		return idPhenomenon;
	}

	public void setIdPhenomenon(Integer idPhenomenon) {
		this.idPhenomenon = idPhenomenon;
	}

	public String getPhenomenonname() {
		return phenomenonname;
	}

	public void setPhenomenonname(String phenomenonname) {
		this.phenomenonname = phenomenonname;
	}

	public String getPhenomenoncetegory() {
		return phenomenoncetegory;
	}

	public void setPhenomenoncetegory(String phenomenoncetegory) {
		this.phenomenoncetegory = phenomenoncetegory;
	}

}
