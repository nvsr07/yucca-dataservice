package org.csi.yucca.adminapi.response;

public class PostDatasetResponse {
	
	private Integer iddataset;
	private String datasetcode;
	private String datasetname;

	public static PostDatasetResponse build(Integer iddataset){
		PostDatasetResponse response = new PostDatasetResponse();
		return response.iddataset(iddataset);
	}
	
	public PostDatasetResponse iddataset(Integer iddataset){
		this.iddataset = iddataset;
		return this;
	}
	
	public PostDatasetResponse datasetcode (String datasetcode){
		this.datasetcode = datasetcode;
		return this;
	}
	
	public PostDatasetResponse datasetname (String datasetname){
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
}
