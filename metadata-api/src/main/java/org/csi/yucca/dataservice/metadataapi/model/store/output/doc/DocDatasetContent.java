package org.csi.yucca.dataservice.metadataapi.model.store.output.doc;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class DocDatasetContent {

	private String id;
	private Long idDataset;
	private String datasetCode;
	private Integer datasetVersion;

	private ConfigData configData;
	private Info info;
	private Opendata opendata;

	private DCAT dcat;

	public DCAT getDcat() {
		return dcat;
	}

	public void setDcat(DCAT dcat) {
		this.dcat = dcat;
	}

	public DocDatasetContent() {
		super();
	}

	public static DocDatasetContent fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, DocDatasetContent.class);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getIdDataset() {
		return idDataset;
	}

	public void setIdDataset(Long idDataset) {
		this.idDataset = idDataset;
	}

	public String getDatasetCode() {
		return datasetCode;
	}

	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
	}

	public Integer getDatasetVersion() {
		return datasetVersion;
	}

	public void setDatasetVersion(Integer datasetVersion) {
		this.datasetVersion = datasetVersion;
	}

	public ConfigData getConfigData() {
		return configData;
	}

	public void setConfigData(ConfigData configData) {
		this.configData = configData;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public Opendata getOpendata() {
		return opendata;
	}

	public void setOpendata(Opendata opendata) {
		this.opendata = opendata;
	}

}
