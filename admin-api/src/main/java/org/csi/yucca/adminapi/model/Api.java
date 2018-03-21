package org.csi.yucca.adminapi.model;

import org.apache.ibatis.annotations.Result;

public class Api {

	public static final String API_TYPE = "output"; 
	public static final String ENTITY_NAMESPACE = "it.csi.smartdata.odata.";

	private Integer maxOdataResultperpage;
	private Integer idapi;
	private String apicode;
	private String apiname;
	private String apitype;
	private String apisubtype;
	private Integer idDataSource;
	private Integer datasourceversion;
	private String entitynamespace = null;

	public static Api buildOutput(Integer datasourceversion){
		Api out = new Api();
		out.setApitype(API_TYPE);
		out.setDatasourceversion(datasourceversion);
		return out;
	}
	public Api maxOdataResultperpage (Integer maxOdataResultperpage){
		this.maxOdataResultperpage = maxOdataResultperpage;
		return this;
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
	public Api entitynamespace (String entitynamespace){
		this.entitynamespace = entitynamespace;
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

	public String getEntitynamespace() {
		return entitynamespace;
	}

	public void setEntitynamespace(String entitynamespace) {
		this.entitynamespace = entitynamespace;
	}
	public Integer getMaxOdataResultperpage() {
		return maxOdataResultperpage;
	}
	public void setMaxOdataResultperpage(Integer maxOdataResultperpage) {
		this.maxOdataResultperpage = maxOdataResultperpage;
	}

}
