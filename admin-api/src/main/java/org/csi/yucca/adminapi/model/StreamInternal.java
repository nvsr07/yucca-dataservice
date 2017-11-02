package org.csi.yucca.adminapi.model;

public class StreamInternal {

	private Integer idDataSourceinternal;
	private Integer datasourceversioninternal;
	private Integer idstream;
	private String streamAlias;

	public Integer getIdDataSourceinternal() {
		return idDataSourceinternal;
	}

	public void setIdDataSourceinternal(Integer idDataSourceinternal) {
		this.idDataSourceinternal = idDataSourceinternal;
	}

	public Integer getDatasourceversioninternal() {
		return datasourceversioninternal;
	}

	public void setDatasourceversioninternal(Integer datasourceversioninternal) {
		this.datasourceversioninternal = datasourceversioninternal;
	}

	public Integer getIdstream() {
		return idstream;
	}

	public void setIdstream(Integer idstream) {
		this.idstream = idstream;
	}

	public String getStreamAlias() {
		return streamAlias;
	}

	public void setStreamAlias(String streamAlias) {
		this.streamAlias = streamAlias;
	}

}
