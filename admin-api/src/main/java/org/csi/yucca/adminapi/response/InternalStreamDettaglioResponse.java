package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.InternalDettaglioStream;


public class InternalStreamDettaglioResponse extends StreamDettaglioResponse{

	private String streamalias;

	
	public String getStreamalias() {
		return streamalias;
	}

	public void setStreamalias(String streamalias) {
		this.streamalias = streamalias;
	}
	
	public InternalStreamDettaglioResponse(InternalDettaglioStream dettaglioStream)throws Exception{
		this.setIdstream(dettaglioStream.getIdstream());
		this.setStreamcode(dettaglioStream.getStreamcode());
		this.setStreamname(dettaglioStream.getStreamname());
		this.setStreamalias(dettaglioStream.getAliasName());
		this.setSmartobject(new DettaglioSmartobjectResponse(dettaglioStream));
	}
	
	
}
