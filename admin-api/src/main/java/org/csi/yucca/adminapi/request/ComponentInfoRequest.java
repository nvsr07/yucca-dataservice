package org.csi.yucca.adminapi.request;

public class ComponentInfoRequest {
	
	private Integer numColumn;
	private String dateFormat;
	private boolean skipColumn;
	private Integer idComponent;
	
	public Integer getNumColumn() {
		return numColumn;
	}
	public void setNumColumn(Integer numColumn) {
		this.numColumn = numColumn;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public boolean isSkipColumn() {
		return skipColumn;
	}
	public void setSkipColumn(boolean skipColumn) {
		this.skipColumn = skipColumn;
	}
	public Integer getIdComponent() {
		return idComponent;
	}
	public void setIdComponent(Integer idComponent) {
		this.idComponent = idComponent;
	}
	
}
