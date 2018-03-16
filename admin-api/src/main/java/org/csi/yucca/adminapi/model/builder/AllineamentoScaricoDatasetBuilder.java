package org.csi.yucca.adminapi.model.builder;

import org.csi.yucca.adminapi.model.AllineamentoScaricoDataset;
import org.csi.yucca.adminapi.request.AllineamentoScaricoDatasetRequest;

public class AllineamentoScaricoDatasetBuilder {

	private AllineamentoScaricoDatasetRequest request;

	public AllineamentoScaricoDatasetBuilder(AllineamentoScaricoDatasetRequest request) {
		super();
		this.request = request;
	}

	public AllineamentoScaricoDatasetBuilder(AllineamentoScaricoDatasetRequest request, Integer idOrganization) {
		super();
		this.request = request;
		this.request.setIdOrganization(idOrganization);
	}
	
	public AllineamentoScaricoDataset build(){
		AllineamentoScaricoDataset scaricoDataset = new AllineamentoScaricoDataset();

		scaricoDataset.setIdOrganization(request.getIdOrganization());
		scaricoDataset.setLastMongoObjectId(request.getLastMongoObjectId());
		scaricoDataset.setDatasetVersion(request.getDatasetVersion());
		scaricoDataset.setIdDataset(request.getIdDataset());

		return scaricoDataset;
	}
	
}