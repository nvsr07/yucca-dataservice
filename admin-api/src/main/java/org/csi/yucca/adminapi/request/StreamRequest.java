package org.csi.yucca.adminapi.request;

import java.util.List;

public class StreamRequest implements IVisibility, IDataSourceRequest {

	private String streamname;
	private String name;
	private boolean unpublished;
	private Boolean savedata;
	private Double fps;
	private String internalquery;
	private List<InternalStreamRequest> internalStreams;
	private TwitterInfoRequest twitterInfo;
	private LicenseRequest license;
	private String visibility;
	private List<SharingTenantRequest> sharingTenants;
	private String copyright;
	private String icon;
	private OpenDataRequest opendata;
	private DcatRequest dcat;
	private List<ComponentRequest> components;
	private List<Integer> tags;
	private String disclaimer;
	private Boolean privacyacceptance;
	private Integer idSubdomain;
	private String requestermail;;
	private String requestername;
	private String requestersurname;
	private String externalreference;

	public String getExternalreference() {
		return externalreference;
	}

	public void setExternalreference(String externalreference) {
		this.externalreference = externalreference;
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

	public Integer getIdSubdomain() {
		return idSubdomain;
	}

	public void setIdSubdomain(Integer idSubdomain) {
		this.idSubdomain = idSubdomain;
	}

	public Boolean getPrivacyacceptance() {
		return privacyacceptance;
	}

	public void setPrivacyacceptance(Boolean privacyacceptance) {
		this.privacyacceptance = privacyacceptance;
	}

	public void setUnpublished(boolean unpublished) {
		this.unpublished = unpublished;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
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

	public boolean getUnpublished() {
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

	public List<InternalStreamRequest> getInternalStreams() {
		return internalStreams;
	}

	public void setInternalStreams(List<InternalStreamRequest> internalStreams) {
		this.internalStreams = internalStreams;
	}

	public TwitterInfoRequest getTwitterInfoRequest() {
		return twitterInfo;
	}

	public void setTwitterInfoRequest(TwitterInfoRequest twitterInfo) {
		this.twitterInfo= twitterInfo;
	}

	public LicenseRequest getLicense() {
		return license;
	}

	public void setLicense(LicenseRequest license) {
		this.license = license;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}



	public OpenDataRequest getOpendata() {
		return opendata;
	}

	public void setOpendata(OpenDataRequest opendata) {
		this.opendata = opendata;
	}

	public DcatRequest getDcat() {
		return dcat;
	}

	public void setDcat(DcatRequest dcat) {
		this.dcat = dcat;
	}

	public List<ComponentRequest> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentRequest> components) {
		this.components = components;
	}

	public List<Integer> getTags() {
		return tags;
	}

	public void setTags(List<Integer> tags) {
		this.tags = tags;
	}

}
