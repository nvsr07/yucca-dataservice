package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.Api;

public class BackofficeDettaglioApiResponse {

	private Integer idapi;
	private String apicode;
	private String apiname;
	private String apitype;
	private String apisubtype;
	private String entitynamespace;
	private Integer maxOdataResultperpage;
	
	private BackofficeDettaglioStreamDatasetResponse backofficeDettaglioStreamDatasetResponse;
	
	public BackofficeDettaglioApiResponse() {
		super();
	}

	public BackofficeDettaglioApiResponse(Api api, BackofficeDettaglioStreamDatasetResponse dettaglioStreamDatasetResponse) {
		this.maxOdataResultperpage = api.getMaxOdataResultperpage();
		this.idapi = api.getIdapi();
		this.apicode = api.getApicode();
		this.apiname = api.getApiname();
		this.apitype = api.getApitype();
		this.apisubtype = api.getApisubtype();
		this.entitynamespace = api.getEntitynamespace();
		this.backofficeDettaglioStreamDatasetResponse = dettaglioStreamDatasetResponse;
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

	public String getEntitynamespace() {
		return entitynamespace;
	}

	public void setEntitynamespace(String entitynamespace) {
		this.entitynamespace = entitynamespace;
	}

	public BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDatasetResponse() {
		return backofficeDettaglioStreamDatasetResponse;
	}

	public void setDettaglioStreamDatasetResponse(
			BackofficeDettaglioStreamDatasetResponse dettaglioStreamDatasetResponse) {
		this.backofficeDettaglioStreamDatasetResponse = dettaglioStreamDatasetResponse;
	}

	public Integer getMaxOdataResultperpage() {
		return maxOdataResultperpage;
	}

	public void setMaxOdataResultperpage(Integer maxOdataResultperpage) {
		this.maxOdataResultperpage = maxOdataResultperpage;
	}
	
}
