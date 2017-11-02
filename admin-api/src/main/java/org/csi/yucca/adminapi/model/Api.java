package org.csi.yucca.adminapi.model;

public class Api {

	private Integer idapi;
	private String apicode;
	private String apiname;
	private String apitype;
	private String apisubtype;
	private Integer idDataSource;
	private Integer datasourceversion;

	public static Api buildOutput(Integer datasourceversion){
		Api out = new Api();
		out.setApitype("output");
		out.setDatasourceversion(datasourceversion);
		return out;
	}

	public Api apicode (String apicode){
		this.apicode = apicode;
		return this;
	}
	public Api apiname (String apiname){
		this.apiname = apiname;
		return this;
	}
	public Api apitype (String apitype){
		this.apitype = apitype;
		return this;
	}
	public Api apisubtype (String apisubtype){
		this.apisubtype = apisubtype;
		return this;
	}
	public Api idDataSource (Integer idDataSource){
		this.idDataSource = idDataSource;
		return this;
	}
	public Api datasourceversion (Integer datasourceversion){
		this.datasourceversion = datasourceversion;
		return this;
	}

	public Integer getIdapi() {
		return idapi;
	}

	public void setIdapi(Integer idapi) {
		this.idapi = idapi;
	}

	public String getApicode() {
		return apicode;
	}

	public void setApicode(String apicode) {
		this.apicode = apicode;
	}

	public String getApiname() {
		return apiname;
	}

	public void setApiname(String apiname) {
		this.apiname = apiname;
	}

	public String getApitype() {
		return apitype;
	}

	public void setApitype(String apitype) {
		this.apitype = apitype;
	}

	public String getApisubtype() {
		return apisubtype;
	}

	public void setApisubtype(String apisubtype) {
		this.apisubtype = apisubtype;
	}

	public Integer getIdDataSource() {
		return idDataSource;
	}

	public void setIdDataSource(Integer idDataSource) {
		this.idDataSource = idDataSource;
	}

	public Integer getDatasourceversion() {
		return datasourceversion;
	}

	public void setDatasourceversion(Integer datasourceversion) {
		this.datasourceversion = datasourceversion;
	}

}
