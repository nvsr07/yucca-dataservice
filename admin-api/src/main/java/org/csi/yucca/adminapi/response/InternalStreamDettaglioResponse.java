package org.csi.yucca.adminapi.response;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.model.InternalDettaglioStream;
import org.csi.yucca.adminapi.util.Util;


public class InternalStreamDettaglioResponse extends StreamDettaglioResponse{

	private String streamalias;
	private String tenantCode;
	private List<ComponentResponse> components = new ArrayList<ComponentResponse>();
	
	public InternalStreamDettaglioResponse(InternalDettaglioStream dettaglioStream)throws Exception{
		this.setIdstream(dettaglioStream.getIdstream());
		this.setStreamcode(dettaglioStream.getStreamcode());
		this.setStreamname(dettaglioStream.getStreamname());
		this.setStreamalias(dettaglioStream.getAliasName());
		Util.addComponents(dettaglioStream.getComponents(), this.components);
		this.setTenantCode(dettaglioStream.getTenantCode());
		this.setSmartobject(new DettaglioSmartobjectResponse(dettaglioStream));
	}

	
	public String getStreamalias() {
		return streamalias;
	}

	public void setStreamalias(String streamalias) {
		this.streamalias = streamalias;
	}
	
	
	
	public List<ComponentResponse> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentResponse> components) {
		this.components = components;
	}


	public String getTenantCode() {
		return tenantCode;
	}


	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}
	
	


	
	
}
