package org.csi.yucca.dataservice.metadataapi.model.output;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Dataset {
	private DatasetColumn[] columns;

	public Dataset() {

	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public DatasetColumn[] getColumns() {
		return columns;
	}

	public void setColumns(DatasetColumn[] columns) {
		this.columns = columns;
	}

}
