package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.Dcat;
import org.csi.yucca.adminapi.model.DcatJson;
import org.csi.yucca.adminapi.util.Util;

public class DcatResponse extends Response {

	private Integer idDcat;
	private String dcatdataupdate;
	private String dcatnomeorg;
	private String dcatemailorg;
	private String dcatcreatorname;
	private String dcatcreatortype;
	private String dcatcreatorid;
	private String dcatrightsholdername;
	private String dcatrightsholdertype;
	private String dcatrightsholderid;
	
	public DcatResponse(String dcatJsonString) {
		
		super();
		
		DcatJson dcatJson =  null;
		
		try {
			dcatJson = Util.getFromJsonString(dcatJsonString, DcatJson.class);	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if (dcatJson != null) {
			this.idDcat = dcatJson.getId_dcat();
			this.dcatdataupdate = dcatJson.getDcatdataupdate();
			this.dcatnomeorg = dcatJson.getDcatnomeorg();
			this.dcatemailorg = dcatJson.getDcatemailorg();
			this.dcatcreatorname = dcatJson.getDcatcreatorname();
			this.dcatcreatortype = dcatJson.getDcatcreatortype();
			this.dcatcreatorid = dcatJson.getDcatcreatorid();
			this.dcatrightsholdername = dcatJson.getDcatrightsholdername();
			this.dcatrightsholdertype = dcatJson.getDcatrightsholdertype();
			this.dcatrightsholderid = dcatJson.getDcatrightsholderid();			
		}
	}

	public DcatResponse() {
		super();
	}

	public DcatResponse(Dcat dcat) {
		super();
		if (dcat != null) {
			this.idDcat = dcat.getIdDcat();
			this.dcatdataupdate = "" + dcat.getDcatdataupdate();
			this.dcatnomeorg = dcat.getDcatnomeorg();
			this.dcatemailorg = dcat.getDcatemailorg();
			this.dcatcreatorname = dcat.getDcatcreatorname();
			this.dcatcreatortype = dcat.getDcatcreatortype();
			this.dcatcreatorid = dcat.getDcatcreatorid();
			this.dcatrightsholdername = dcat.getDcatrightsholdername();
			this.dcatrightsholdertype = dcat.getDcatrightsholdertype();
			this.dcatrightsholderid = dcat.getDcatrightsholderid();			
		}	}

	public Integer getIdDcat() {
		return idDcat;
	}

	public void setIdDcat(Integer idDcat) {
		this.idDcat = idDcat;
	}

	public String getDcatdataupdate() {
		return dcatdataupdate;
	}

	public void setDcatdataupdate(String dcatdataupdate) {
		this.dcatdataupdate = dcatdataupdate;
	}

	public String getDcatnomeorg() {
		return dcatnomeorg;
	}

	public void setDcatnomeorg(String dcatnomeorg) {
		this.dcatnomeorg = dcatnomeorg;
	}

	public String getDcatemailorg() {
		return dcatemailorg;
	}

	public void setDcatemailorg(String dcatemailorg) {
		this.dcatemailorg = dcatemailorg;
	}

	public String getDcatcreatorname() {
		return dcatcreatorname;
	}

	public void setDcatcreatorname(String dcatcreatorname) {
		this.dcatcreatorname = dcatcreatorname;
	}

	public String getDcatcreatortype() {
		return dcatcreatortype;
	}

	public void setDcatcreatortype(String dcatcreatortype) {
		this.dcatcreatortype = dcatcreatortype;
	}

	public String getDcatcreatorid() {
		return dcatcreatorid;
	}

	public void setDcatcreatorid(String dcatcreatorid) {
		this.dcatcreatorid = dcatcreatorid;
	}

	public String getDcatrightsholdername() {
		return dcatrightsholdername;
	}

	public void setDcatrightsholdername(String dcatrightsholdername) {
		this.dcatrightsholdername = dcatrightsholdername;
	}

	public String getDcatrightsholdertype() {
		return dcatrightsholdertype;
	}

	public void setDcatrightsholdertype(String dcatrightsholdertype) {
		this.dcatrightsholdertype = dcatrightsholdertype;
	}

	public String getDcatrightsholderid() {
		return dcatrightsholderid;
	}

	public void setDcatrightsholderid(String dcatrightsholderid) {
		this.dcatrightsholderid = dcatrightsholderid;
	}


}
