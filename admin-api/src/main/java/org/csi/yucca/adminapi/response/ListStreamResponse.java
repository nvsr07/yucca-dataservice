package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.DettaglioStream;

public class ListStreamResponse extends StreamResponse{
	
	private ListStreamSmartobjectResponse smartobject;
	
	public ListStreamResponse(DettaglioStream dettaglioStream) throws Exception {
		super(dettaglioStream);
		this.smartobject = new ListStreamSmartobjectResponse(dettaglioStream);
	}

	public ListStreamResponse() {
		super();
	}

	public ListStreamSmartobjectResponse getSmartobject() {
		return smartobject;
	}

	public void setSmartobject(ListStreamSmartobjectResponse smartobject) {
		this.smartobject = smartobject;
	}
	
}
