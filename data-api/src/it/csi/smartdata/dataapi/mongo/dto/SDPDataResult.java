package it.csi.smartdata.dataapi.mongo.dto;

import java.util.List;
import java.util.Map;

public class SDPDataResult {

	private List<Map<String, Object>> dati=null;
	private int totalCount=-1;
	public List<Map<String, Object>> getDati() {
		return dati;
	}
	public void setDati(List<Map<String, Object>> dati) {
		this.dati = dati;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	
	public SDPDataResult (List<Map<String, Object>> dati,int totalCount) {
		this.setDati(dati);
		this.setTotalCount(totalCount);
	}
	
	
}
