package org.csi.yucca.adminapi.model.builder;

import org.csi.yucca.adminapi.model.Allineamento;
import org.csi.yucca.adminapi.request.AllineamentoRequest;

public class AllineamentoBuilder {

	private AllineamentoRequest request;

	public AllineamentoBuilder(AllineamentoRequest request) {
		super();
		this.request = request;
	}

	public AllineamentoBuilder(AllineamentoRequest request, Integer idOrganization) {
		super();
		this.request = request;
		this.request.setIdOrganization(idOrganization);
	}
	
	public Allineamento build(){
		Allineamento allineamento = new Allineamento();
		allineamento.setIdOrganization(this.request.getIdOrganization());
		allineamento.setLastobjectid(this.request.getLastobjectid());
		allineamento.setLocked(this.request.getLocked());

		return allineamento;
	}
	
	
	
	
	
	
	
}
