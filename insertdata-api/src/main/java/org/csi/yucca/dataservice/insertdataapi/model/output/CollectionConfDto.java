package org.csi.yucca.dataservice.insertdataapi.model.output;

public class CollectionConfDto {
	
	private Boolean forwardToBrokerFromCEP;
	
	private String solrCollectionName;
	private String phoenixSchemaName;
	private String phoenixTableName;
	

	
	public Boolean getForwardToBrokerFromCEP() {
		return forwardToBrokerFromCEP;
	}
	public void setForwardToBrokerFromCEP(Boolean forwardToBrokerFromCEP) {
		this.forwardToBrokerFromCEP = forwardToBrokerFromCEP;
	}
	public String getSolrCollectionName() {
		return solrCollectionName;
	}
	public void setSolrCollectionName(String solrCollectionName) {
		this.solrCollectionName = solrCollectionName;
	}
	public String getPhoenixSchemaName() {
		return phoenixSchemaName;
	}
	public void setPhoenixSchemaName(String phoenixSchemaName) {
		this.phoenixSchemaName = phoenixSchemaName;
	}
	public String getPhoenixTableName() {
		return phoenixTableName;
	}
	public void setPhoenixTableName(String phoenixTableName) {
		this.phoenixTableName = phoenixTableName;
	}

	
	

}
