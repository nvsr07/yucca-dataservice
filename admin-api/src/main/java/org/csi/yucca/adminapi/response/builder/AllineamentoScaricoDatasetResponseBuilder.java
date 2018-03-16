package org.csi.yucca.adminapi.response.builder;

import org.csi.yucca.adminapi.model.AllineamentoScaricoDataset;
import org.csi.yucca.adminapi.response.AllineamentoScaricoDatasetResponse;

public class AllineamentoScaricoDatasetResponseBuilder{

	private Integer idDataset;
	private Integer datasetVersion;
	private String lastMongoObjectId;
	private Integer idOrganization; 
	
	public AllineamentoScaricoDatasetResponseBuilder idDataset(Integer idDataset){
		this.idDataset = idDataset;
		return this;
	}

	public AllineamentoScaricoDatasetResponseBuilder datasetVersion(Integer datasetVersion){
		this.datasetVersion = datasetVersion;
		return this;
	}

	public AllineamentoScaricoDatasetResponseBuilder idOrganization(Integer idOrganization){
		this.idOrganization = idOrganization;
		return this;
	}
	
	public AllineamentoScaricoDatasetResponseBuilder lastMongoObjectId(String lastMongoObjectId){
		this.lastMongoObjectId = lastMongoObjectId;
		return this;
	}
	
	public AllineamentoScaricoDatasetResponseBuilder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AllineamentoScaricoDatasetResponseBuilder(AllineamentoScaricoDataset model) {
		super();
		this.idDataset = model.getIdDataset();
		this.datasetVersion = model.getDatasetVersion();
		this.lastMongoObjectId = model.getLastMongoObjectId();
		this.idOrganization = model.getIdOrganization(); 
	}

	public AllineamentoScaricoDatasetResponse build(){
		
		AllineamentoScaricoDatasetResponse response = new AllineamentoScaricoDatasetResponse();
		
		response.setIdDataset(this.idDataset);
		response.setDatasetVersion(this.datasetVersion);
		response.setLastMongoObjectId(this.lastMongoObjectId);
		response.setIdOrganization(this.idOrganization);
		
		return response;
	}

}
