package org.csi.yucca.datainsert.dto;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.datainsert.exception.InsertApiBaseException;

public class DatasetBulkInsertOutput {

	
	private InsertApiBaseException insertException=null;
	public InsertApiBaseException getInsertException() {
		return insertException;
	}

	public void setInsertException(InsertApiBaseException insertException) {
		this.insertException = insertException;
	}

	private List<DatasetBulkInsertIOperationReport> dataBLockreport=new ArrayList<DatasetBulkInsertIOperationReport>();
	
	private String globalRequestId=null;

	public List<DatasetBulkInsertIOperationReport> getDataBLockreport() {
		return dataBLockreport;
	}

	public void setDataBLockreport(List<DatasetBulkInsertIOperationReport> dataBLockreport) {
		this.dataBLockreport = dataBLockreport;
	}

	public String getGlobalRequestId() {
		return globalRequestId;
	}

	public void setGlobalRequestId(String globalRequestId) {
		this.globalRequestId = globalRequestId;
	}
	
}
