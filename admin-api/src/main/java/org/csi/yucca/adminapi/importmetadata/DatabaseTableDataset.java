package org.csi.yucca.adminapi.importmetadata;

import java.util.LinkedList;
import java.util.List;

import org.csi.yucca.adminapi.model.ComponentJson;
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.response.DettaglioStreamDatasetResponse;

public class DatabaseTableDataset {

	public static final String DATABASE_TABLE_DATASET_STATUS_NEW = "new";
	public static final String DATABASE_TABLE_DATASET_STATUS_EXISTING = "existing";

	private String tableName;
	private String tableType;
	private String status;
	private DettaglioStreamDatasetResponse dataset;
	private List<String> warnings;
	private List<ComponentJson> newComponents;

	public DatabaseTableDataset() {
		super();
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public DettaglioStreamDatasetResponse getDataset() {
		return dataset;
	}

	public void setDataset(DettaglioStreamDatasetResponse dataset) {
		this.dataset = dataset;
	}

	public List<ComponentJson> getNewComponents() {
		return newComponents;
	}

	public void setNewComponents(List<ComponentJson> newComponents) {
		this.newComponents = newComponents;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}
	
	public void addWarning(String warning){
		if(warnings==null)
			warnings = new LinkedList<String>();
		warnings.add(warning);
	}
}
