package org.csi.yucca.adminapi.request;

import java.util.List;

public class PostStreamRequest {

	private Integer idTenant;
	private String streamcode;
	private String streamname;
	private String name;
	private Boolean unpublished;
	private Boolean savedata;
	private Double fps;
	private String internalquery;
	private String visibility;
	private String copyright;
	private String requestername;
	private String requestersurname;
	private String requestermail;
	private Integer privacyacceptance;
	private String icon;
	private Integer idSubdomain; 
	private List<Integer> tags;
	private List<ComponentRequest> components;
	private DcatRequest dcat;
	private OpenDataRequest openData;
	private List<InternalStreamRequest> internalStreams; 
	private TwitterInfoRequest twitterInfoRequest;
	private LicenseRequest license;
	private List<SharingTenantRequest> sharingTenants;
	
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

	public String getStreamname() {
		return streamname;
	}

	public void setStreamname(String streamname) {
		this.streamname = streamname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getUnpublished() {
		if (unpublished == null) {
			return false;
		}
		return unpublished;
	}

	public void setUnpublished(Boolean unpublished) {
		this.unpublished = unpublished;
	}

	public Boolean getSavedata() {
		return savedata;
	}

	public void setSavedata(Boolean savedata) {
		this.savedata = savedata;
	}

	public Double getFps() {
		return fps;
	}

	public void setFps(Double fps) {
		this.fps = fps;
	}

	public String getInternalquery() {
		return internalquery;
	}

	public void setInternalquery(String internalquery) {
		this.internalquery = internalquery;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public List<InternalStreamRequest> getInternalStreams() {
		return internalStreams;
	}

	public void setInternalStreams(List<InternalStreamRequest> internalStreams) {
		this.internalStreams = internalStreams;
	}

	public TwitterInfoRequest getTwitterInfoRequest() {
		return twitterInfoRequest;
	}

	public void setTwitterInfoRequest(TwitterInfoRequest twitterInfoRequest) {
		this.twitterInfoRequest = twitterInfoRequest;
	}

	public LicenseRequest getLicense() {
		return license;
	}

	public void setLicense(LicenseRequest license) {
		this.license = license;
	}

	public List<SharingTenantRequest> getSharingTenants() {
		return sharingTenants;
	}

	public void setSharingTenants(List<SharingTenantRequest> sharingTenants) {
		this.sharingTenants = sharingTenants;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
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

	public Integer getPrivacyacceptance() {
		return privacyacceptance;
	}

	public void setPrivacyacceptance(Integer privacyacceptance) {
		this.privacyacceptance = privacyacceptance;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public OpenDataRequest getOpenData() {
		return openData;
	}

	public void setOpenData(OpenDataRequest openData) {
		this.openData = openData;
	}

	public Integer getIdSubdomain() {
		return idSubdomain;
	}

	public void setIdSubdomain(Integer idSubdomain) {
		this.idSubdomain = idSubdomain;
	}

	public List<Integer> getTags() {
		return tags;
	}

	public void setTags(List<Integer> tags) {
		this.tags = tags;
	}

	public List<ComponentRequest> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentRequest> components) {
		this.components = components;
	}

	public DcatRequest getDcat() {
		return dcat;
	}

	public void setDcat(DcatRequest dcat) {
		this.dcat = dcat;
	}
	
}
