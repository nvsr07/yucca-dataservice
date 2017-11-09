package org.csi.yucca.adminapi.response;

import java.sql.Timestamp;

import org.csi.yucca.adminapi.model.DettaglioStream;

public class OpenDataResponse extends Response {

	private String opendataexternalreference;
	private String opendataauthor;
	private Timestamp opendataupdatedate;
	private String opendatalanguage;
	private String lastupdate;
	
	public OpenDataResponse(DettaglioStream dettaglioStream) {
		super();
		this.opendataexternalreference = dettaglioStream.getDataSourceOpenDataExternalReference();
		this.opendataauthor = dettaglioStream.getDataSourceOpenDataAuthor();
		this.opendataupdatedate = dettaglioStream.getDataSourceOpenDataUpdateDate();
		this.opendatalanguage = dettaglioStream.getDataSourceOpenDataLanguage();
		this.lastupdate = dettaglioStream.getDataSourceLastUpdate();
	}

	public OpenDataResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getOpendataexternalreference() {
		return opendataexternalreference;
	}

	public void setOpendataexternalreference(String opendataexternalreference) {
		this.opendataexternalreference = opendataexternalreference;
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

}
