package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.Phenomenon;
import org.csi.yucca.adminapi.util.Errors;

public class PhenomenonResponse extends Response{
	
	private Integer idPhenomenon;
	private String phenomenonname;
	private String phenomenoncetegory;
	
	public PhenomenonResponse(Phenomenon phenomenon) {
		super();
		this.idPhenomenon = phenomenon.getIdPhenomenon();
		this.phenomenonname = phenomenon.getPhenomenonname();
		this.phenomenoncetegory = phenomenon.getPhenomenoncetegory();
	}
	public PhenomenonResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PhenomenonResponse(Errors errors) {
		super(errors);
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
