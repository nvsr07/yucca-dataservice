package org.csi.yucca.dataservice.insertdataapi.model.output;

import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiBaseException;

public class DatasetDeleteOutput {

	private InsertApiBaseException deleteException = null;
	private Boolean dataDeleted = false;
	private Integer numRowDeleted = 0;
	private String dataDeletedMessage = null;
	private Boolean fileDeleted = false;
	private String fileDeletedMessage = null;
	private Boolean indexDeleted = false;
	private String indexDeletedMessage = null;

	public InsertApiBaseException getDeleteException() {
		return deleteException;
	}

	public void setDeleteException(InsertApiBaseException deleteException) {
		this.deleteException = deleteException;
	}

	public Boolean getDataDeleted() {
		return dataDeleted;
	}

	public void setDataDeleted(Boolean dataDeleted) {
		this.dataDeleted = dataDeleted;
	}

	public Integer getNumRowDeleted() {
		return numRowDeleted;
	}

	public void setNumRowDeleted(Integer numRowDeleted) {
		this.numRowDeleted = numRowDeleted;
	}

	public String getDataDeletedMessage() {
		return dataDeletedMessage;
	}

	public void setDataDeletedMessage(String dataDeletedMessage) {
		this.dataDeletedMessage = dataDeletedMessage;
	}

	public Boolean getFileDeleted() {
		return fileDeleted;
	}

	public void setFileDeleted(Boolean fileDeleted) {
		this.fileDeleted = fileDeleted;
	}

	public String getFileDeletedMessage() {
		return fileDeletedMessage;
	}

	public void setFileDeletedMessage(String fileDeletedMessage) {
		this.fileDeletedMessage = fileDeletedMessage;
	}

	public Boolean getIndexDeleted() {
		return indexDeleted;
	}

	public void setIndexDeleted(Boolean indexDeleted) {
		this.indexDeleted = indexDeleted;
	}

	public String getIndexDeletedMessage() {
		return indexDeletedMessage;
	}

	public void setIndexDeletedMessage(String indexDeletedMessage) {
		this.indexDeletedMessage = indexDeletedMessage;
	}

}
