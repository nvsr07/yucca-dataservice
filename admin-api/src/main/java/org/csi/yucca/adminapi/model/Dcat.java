package org.csi.yucca.adminapi.model;

import java.sql.Timestamp;

public class Dcat {

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
	private Integer dcatready;
	
	public Long getIdDcat() {
		return idDcat;
	}
	public void setIdDcat(Long idDcat) {
		this.idDcat = idDcat;
	}
	public Timestamp getDcatdataupdate() {
		return dcatdataupdate;
	}
	public void setDcatdataupdate(Timestamp dcatdataupdate) {
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
	public Integer getDcatready() {
		return dcatready;
	}
	public void setDcatready(Integer dcatready) {
		this.dcatready = dcatready;
	}
}
