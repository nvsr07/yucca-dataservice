package org.csi.yucca.adminapi.response;

import java.util.LinkedList;
import java.util.List;

public class PostDatasetResponse {

	private Integer iddataset;
	private String datasetcode;
	private String datasetname;
	private List<String> warnings;

	public static PostDatasetResponse build(Integer iddataset) {
		PostDatasetResponse response = new PostDatasetResponse();
		return response.iddataset(iddataset);
	}

	public PostDatasetResponse iddataset(Integer iddataset) {
		this.iddataset = iddataset;
		return this;
	}

	public PostDatasetResponse datasetcode(String datasetcode) {
		this.datasetcode = datasetcode;
		return this;
	}

	public PostDatasetResponse datasetname(String datasetname) {
		this.datasetname = datasetname;
		return this;
	}

	public Integer getIddataset() {
		return iddataset;
	}

	public void setIddataset(Integer iddataset) {
		this.iddataset = iddataset;
	}

	public String getDatasetcode() {
		return datasetcode;
	}

	public void setDatasetcode(String datasetcode) {
		this.datasetcode = datasetcode;
	}

	public String getDatasetname() {
		return datasetname;
	}

	public void setDatasetname(String datasetname) {
		this.datasetname = datasetname;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	public void addWarning(String warning) {
		if (warnings == null)
			warnings = new LinkedList<String>();
		warnings.add(warning);
	}

}
