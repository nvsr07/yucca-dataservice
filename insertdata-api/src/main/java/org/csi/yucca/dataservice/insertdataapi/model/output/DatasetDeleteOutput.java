package org.csi.yucca.dataservice.insertdataapi.model.output;

import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiBaseException;

public class DatasetDeleteOutput {

	private InsertApiBaseException deleteException = null;
	private String globalRequestId = null;
	private Boolean deleteOnPhoenix = false;
	private String deleteOnPhoenixMessage = null;
	private Boolean deleteOnSolr = false;
	private String deleteOnSolrMessage = null;

	public Boolean getDeleteOnPhoenix() {
		return deleteOnPhoenix;
	}

	public void setDeleteOnPhoenix(Boolean deleteOnPhoenix) {
		this.deleteOnPhoenix = deleteOnPhoenix;
	}

	public String getDeleteOnPhoenixMessage() {
		return deleteOnPhoenixMessage;
	}

	public void setDeleteOnPhoenixMessage(String deleteOnPhoenixMessage) {
		this.deleteOnPhoenixMessage = deleteOnPhoenixMessage;
	}

	public Boolean getDeleteOnSolr() {
		return deleteOnSolr;
	}

	public void setDeleteOnSolr(Boolean deleteOnSolr) {
		this.deleteOnSolr = deleteOnSolr;
	}

	public String getDeleteOnSolrMessage() {
		return deleteOnSolrMessage;
	}

	public void setDeleteOnSolrMessage(String deleteOnSolrMessage) {
		this.deleteOnSolrMessage = deleteOnSolrMessage;
	}

	public InsertApiBaseException getDeleteException() {
		return deleteException;
	}

	public void setDeleteException(InsertApiBaseException deleteException) {
		this.deleteException = deleteException;
	}

	public String getGlobalRequestId() {
		return globalRequestId;
	}

	public void setGlobalRequestId(String globalRequestId) {
		this.globalRequestId = globalRequestId;
	}

}
