package org.csi.yucca.adminapi.request;

public class PostStreamRequest extends StreamRequest implements IDataSourceRequest{

	private Integer idTenant;
	private String streamcode;
	private String requestername;
	private String requestersurname;
	private String requestermail;
	private Boolean privacyacceptance;
	private Integer idSubdomain;
	private String externalreference;
	
	public String getExternalreference() {
		return externalreference;
	}

	public void setExternalreference(String externalreference) {
		this.externalreference = externalreference;
	}

	public Integer getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Integer idTenant) {
		this.idTenant = idTenant;
	}

	public String getStreamcode() {
		return streamcode;
	}

	public void setStreamcode(String streamcode) {
		this.streamcode = streamcode;
	}

	public String getRequestername() {
		return requestername;
	}

	public void setRequestername(String requestername) {
		this.requestername = requestername;
	}

	public String getRequestersurname() {
		return requestersurname;
	}

	public void setRequestersurname(String requestersurname) {
		this.requestersurname = requestersurname;
	}

	public String getRequestermail() {
		return requestermail;
	}

	public void setRequestermail(String requestermail) {
		this.requestermail = requestermail;
	}

	public Boolean getPrivacyacceptance() {
		return privacyacceptance;
	}

	public void setPrivacyacceptance(Boolean privacyacceptance) {
		this.privacyacceptance = privacyacceptance;
	}

	public Integer getIdSubdomain() {
		return idSubdomain;
	}

	public void setIdSubdomain(Integer idSubdomain) {
		this.idSubdomain = idSubdomain;
	}

}
