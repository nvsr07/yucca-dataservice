package it.csi.smartdata.odata.dto;

import java.util.List;
import java.util.Map;

public class SmartDataDiscoveryResponseDTO {
	
	
	
	private List<Map<String, Object>>  dati= null;
	private int totalCount=0;
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
	

}
