package org.csi.yucca.adminapi.request;

public class PhenomenonRequest {

	private Integer idPhenomenon;
	private String phenomenonname;
	private String phenomenoncetegory;

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
