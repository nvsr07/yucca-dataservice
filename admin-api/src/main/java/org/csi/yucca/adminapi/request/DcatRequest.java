package org.csi.yucca.adminapi.request;

import java.sql.Timestamp;

public class DcatRequest {
	
	private Long idDcat; 
	private Timestamp dcatdataupdate;		
	private String dcatnomeorg;
	private String dcatemailorg;	  
	private String dcatcreatorname;	
	private String dcatcreatortype;	
	private String dcatcreatorid;	
	private String dcatrightsholdername;		  	
	private String dcatrightsholdertype;			
	private String dcatrightsholderid;
	
	public Long getIdDcat() {
		return idDcat;
	}
	public Timestamp getDcatdataupdate() {
		if(idDcat != null)return null;
		return dcatdataupdate;
	}
	public String getDcatnomeorg() {
		if(idDcat != null)return null;
		return dcatnomeorg;
	}
	public String getDcatemailorg() {
		if(idDcat != null)return null;
		return dcatemailorg;
	}
	public String getDcatcreatorname() {
		if(idDcat != null)return null;
		return dcatcreatorname;
	}
	public String getDcatcreatortype() {
		if(idDcat != null)return null;
		return dcatcreatortype;
	}
	public String getDcatcreatorid() {
		if(idDcat != null)return null;
		return dcatcreatorid;
	}
	public String getDcatrightsholdername() {
		if(idDcat != null)return null;
		return dcatrightsholdername;
	}
	public String getDcatrightsholdertype() {
		if(idDcat != null)return null;
		return dcatrightsholdertype;
	}
	public String getDcatrightsholderid() {
		if(idDcat != null)return null;
		return dcatrightsholderid;
	}
	public void setIdDcat(Long idDcat) {
		this.idDcat = idDcat;
	}
	public void setDcatdataupdate(Timestamp dcatdataupdate) {
		this.dcatdataupdate = dcatdataupdate;
	}
	public void setDcatnomeorg(String dcatnomeorg) {
		this.dcatnomeorg = dcatnomeorg;
	}
	public void setDcatemailorg(String dcatemailorg) {
		this.dcatemailorg = dcatemailorg;
	}
	public void setDcatcreatorname(String dcatcreatorname) {
		this.dcatcreatorname = dcatcreatorname;
	}
	public void setDcatcreatortype(String dcatcreatortype) {
		this.dcatcreatortype = dcatcreatortype;
	}
	public void setDcatcreatorid(String dcatcreatorid) {
		this.dcatcreatorid = dcatcreatorid;
	}
	public void setDcatrightsholdername(String dcatrightsholdername) {
		this.dcatrightsholdername = dcatrightsholdername;
	}
	public void setDcatrightsholdertype(String dcatrightsholdertype) {
		this.dcatrightsholdertype = dcatrightsholdertype;
	}
	public void setDcatrightsholderid(String dcatrightsholderid) {
		this.dcatrightsholderid = dcatrightsholderid;
	}
}
