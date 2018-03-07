package org.csi.yucca.adminapi.response.builder;

import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.response.OrganizationResponse;

public class OrganizationResponseBuilder{

	// model
	private Organization model;

	public OrganizationResponseBuilder(Organization organization) {
		super();
		this.model = organization;
	}

	public OrganizationResponse build(){
		
		if (this.model == null) {
			return null;
		}
		
		OrganizationResponse response = new OrganizationResponse();
		
		response.setIdOrganization(this.model.getIdOrganization());
		response.setDescription(this.model.getDescription());
		response.setOrganizationcode(this.model.getOrganizationcode());
		
		return response;
	}

}
