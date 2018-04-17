package org.csi.yucca.adminapi.response;

import java.sql.Timestamp;

import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.model.DettaglioStream;

//import bsh.util.Util;
import org.csi.yucca.adminapi.util.Util;


public class OpenDataResponse extends Response {

	private String opendataauthor;
	private Timestamp opendataupdatedate;
	private String opendatalanguage;
	private String lastupdate;
	private Boolean isOpenData;
	private String opendataupdatefrequency;
	
	public OpenDataResponse(DettaglioStream dettaglioStream) {
		super();
		this.opendataauthor = dettaglioStream.getDataSourceOpenDataAuthor();
		this.opendataupdatedate = dettaglioStream.getDataSourceOpenDataUpdateDate();
		this.opendatalanguage = dettaglioStream.getDataSourceOpenDataLanguage();
		this.lastupdate = dettaglioStream.getDataSourceLastUpdate();
		this.isOpenData = Util.intToBoolean(dettaglioStream.getDataSourceIsopendata());
		this.opendataupdatefrequency = dettaglioStream.getDataSourceOpenDataUpdateFrequency();
	}

	public OpenDataResponse(DettaglioDataset dettaglioDataset) {
		super();
		this.opendataauthor = dettaglioDataset.getDataSourceOpenDataAuthor();
		this.opendataupdatedate = dettaglioDataset.getDataSourceOpenDataUpdateDate();
		this.opendatalanguage = dettaglioDataset.getDataSourceOpenDataLanguage();
		this.lastupdate = dettaglioDataset.getDataSourceLastUpdate();
		this.isOpenData = Util.intToBoolean(dettaglioDataset.getDataSourceIsopendata());
		this.opendataupdatefrequency = dettaglioDataset.getDataSourceOpenDataUpdateFrequency();
	}

	public OpenDataResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getOpendataauthor() {
		return opendataauthor;
	}

	public void setOpendataauthor(String opendataauthor) {
		this.opendataauthor = opendataauthor;
	}

	public Timestamp getOpendataupdatedate() {
		return opendataupdatedate;
	}

	public void setOpendataupdatedate(Timestamp opendataupdatedate) {
		this.opendataupdatedate = opendataupdatedate;
	}

	public String getOpendatalanguage() {
		return opendatalanguage;
	}

	public void setOpendatalanguage(String opendatalanguage) {
		this.opendatalanguage = opendatalanguage;
	}

	public String getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}

	public Boolean getIsOpenData() {
		return isOpenData;
	}

	public void setIsOpenData(Boolean isOpenData) {
		this.isOpenData = isOpenData;
	}

	public String getOpendataupdatefrequency() {
		return opendataupdatefrequency;
	}

	public void setOpendataupdatefrequency(String opendataupdatefrequency) {
		this.opendataupdatefrequency = opendataupdatefrequency;
	}
	
	
	

}
