package org.csi.yucca.adminapi.request;

public class InternalStreamRequest {

	private Integer idStream;
	private String streamAlias;
	
	public Integer getIdStream() {
		return idStream;
	}
	public void setIdStream(Integer idStream) {
		this.idStream = idStream;
	}
	public String getStreamAlias() {
		return streamAlias;
	}
	public void setStreamAlias(String streamAlias) {
		this.streamAlias = streamAlias;
	}
	
}
