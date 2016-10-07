package org.csi.yucca.dataservice.metadataapi.model.dcat;

public class VcardDCAT {

	private String description; //dct:description
	private String Kind; //vcard:Kind
	private String organizationName; //vcard:organizationName
	private String hasEmail; //vcard:hasEmail
	private String hasTelephone; //vcard:hasTelephone
	private String hasURL; //vcard:hasURL
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKind() {
		return Kind;
	}
	public void setKind(String kind) {
		Kind = kind;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getHasEmail() {
		return hasEmail;
	}
	public void setHasEmail(String hasEmail) {
		this.hasEmail = hasEmail;
	}
	public String getHasTelephone() {
		return hasTelephone;
	}
	public void setHasTelephone(String hasTelephone) {
		this.hasTelephone = hasTelephone;
	}
	public String getHasURL() {
		return hasURL;
	}
	public void setHasURL(String hasURL) {
		this.hasURL = hasURL;
	}
	
	
}
