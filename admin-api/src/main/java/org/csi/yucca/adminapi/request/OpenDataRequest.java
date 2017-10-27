package org.csi.yucca.adminapi.request;

public class OpenDataRequest {
	
    private String opendataexternalreference;
    private String opendataauthor;
    private String opendataupdatedate;
    private String opendatalanguage;
    private String lastupdate;
    private String fabriccontrolleroutcome;
    
	public String getOpendataupdatedate() {
		return opendataupdatedate;
	}
	public void setOpendataupdatedate(String opendataupdatedate) {
		this.opendataupdatedate = opendataupdatedate;
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
	public String getFabriccontrolleroutcome() {
		return fabriccontrolleroutcome;
	}
	public void setFabriccontrolleroutcome(String fabriccontrolleroutcome) {
		this.fabriccontrolleroutcome = fabriccontrolleroutcome;
	}
    

}
