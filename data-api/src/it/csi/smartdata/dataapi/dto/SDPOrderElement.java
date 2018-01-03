package it.csi.smartdata.dataapi.dto;

public class SDPOrderElement {

	
	public SDPOrderElement (String nomeCampo,int ordine) {
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
