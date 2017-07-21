package org.csi.yucca.adminapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplyTypeResponse implements Response{
	
	private Integer idSupplyType;
	private String supplytype;
	private String description;
	

	public Integer getIdSupplyType() {
		return idSupplyType;
	}
	public void setIdSupplyType(Integer idSupplyType) {
		this.idSupplyType = idSupplyType;
	}
	public String getSupplytype() {
		return supplytype;
	}
	public void setSupplytype(String supplytype) {
		this.supplytype = supplytype;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}