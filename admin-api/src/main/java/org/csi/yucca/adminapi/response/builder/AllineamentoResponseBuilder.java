package org.csi.yucca.adminapi.response.builder;

import org.csi.yucca.adminapi.model.Allineamento;
import org.csi.yucca.adminapi.response.AllineamentoResponse;

public class AllineamentoResponseBuilder{

	// model
	private Allineamento model;

	public AllineamentoResponseBuilder(Allineamento allineamento) {
		super();
		this.model = allineamento;
	}

	public AllineamentoResponse build(){
		
		if (this.model == null) {
			return null;
		}
		
		AllineamentoResponse response = new AllineamentoResponse();
		
		response.setIdOrganization(this.model.getIdOrganization());
		response.setLocked(this.model.getLocked());
		response.setLastobjectid(this.model.getLastobjectid());
		
		return response;
	}

}
