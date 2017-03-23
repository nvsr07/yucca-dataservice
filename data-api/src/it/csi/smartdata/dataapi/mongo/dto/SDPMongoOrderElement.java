package it.csi.smartdata.dataapi.mongo.dto;

public class SDPMongoOrderElement {

	
	public SDPMongoOrderElement (String nomeCampo,int ordine) {
		this.nomeCampo = nomeCampo;
		this.ordine = ordine;
	}
	
	
	
	private String nomeCampo=null;
	private int ordine=1;
	public String getNomeCampo() {
		return nomeCampo;
	}
	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}
	public int getOrdine() {
		return ordine;
	}
	public void setOrdine(int ordine) {
		this.ordine = ordine;
	}
	
	
	
		
}
