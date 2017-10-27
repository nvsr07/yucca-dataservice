package org.csi.yucca.adminapi.request;

import java.sql.Timestamp;

public class DcatRequest {
	
	private Integer idDcat; 
	private Timestamp dcatdataupdate;		
	private String dcatnomeorg;
	private String dcatemailorg;	  
	private String dcatcreatorname;	
	private String dcatcreatortype;	
	private String dcatcreatorid;	
	private String dcatrightsholdername;		  	
	private String dcatrightsholdertype;			
	private String dcatrightsholderid;
	
	public Integer getIdDcat() {
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

	
	
	
}
