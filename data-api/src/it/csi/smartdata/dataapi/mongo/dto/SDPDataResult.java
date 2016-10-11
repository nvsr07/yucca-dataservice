package it.csi.smartdata.dataapi.mongo.dto;

import java.util.List;
import java.util.Map;

public class SDPDataResult {

	private String tenant="-";
	private String datasetCode="-";
	
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	public String getDatasetCode() {
		return datasetCode;
	}
	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
	}


	private List<Map<String, Object>> dati=null;
	private long totalCount=-1;
	public List<Map<String, Object>> getDati() {
		return dati;
	}
	public void setDati(List<Map<String, Object>> dati) {
		this.dati = dati;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	
	public SDPDataResult (List<Map<String, Object>> dati,int totalCount,String tenant,String datasetCode) {
		this.setDati(dati);
		this.setTotalCount(totalCount);
		this.setTenant(tenant);
		this.setDatasetCode(datasetCode);
	}
	public SDPDataResult (List<Map<String, Object>> dati,int totalCount) {
		this.setDati(dati);
		this.setTotalCount(totalCount);
//		this.setTenant(tenant);
//		this.setDatasetCode(datasetCode);
	}
	public SDPDataResult (List<Map<String, Object>> dati,long totalCount) {
		this.setDati(dati);
		this.setTotalCount(totalCount);
//		this.setTenant(tenant);
//		this.setDatasetCode(datasetCode);
	}
	
	
}
