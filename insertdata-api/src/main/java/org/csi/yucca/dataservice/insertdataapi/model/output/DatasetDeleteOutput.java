package org.csi.yucca.dataservice.insertdataapi.model.output;

import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiBaseException;

public class DatasetDeleteOutput {

	
	private InsertApiBaseException deleteException=null;
	private String globalRequestId=null;

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
