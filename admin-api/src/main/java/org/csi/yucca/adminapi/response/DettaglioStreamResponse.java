package org.csi.yucca.adminapi.response;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;
import org.csi.yucca.adminapi.util.Util;

public class DettaglioStreamResponse extends StreamResponse{
	

	private Long usedInInternalCount;	
	private Long streamsCountBySO;
	private String internalquery;
	private String icon;
	private String copyright;
	private String requestername;
	private String requestersurname;
	private String requestermail;
	private Integer privacyacceptance;
	private TwitterInfoResponse twitterInfo;
	private OpenDataResponse openData;
	private LicenseResponse license;	
	private DcatResponse dcat;
	private List<ComponentResponse> components = new ArrayList<ComponentResponse>();	
	private List<TenantResponse> sharingTenants = new ArrayList<TenantResponse>();
	private List<DettaglioStreamResponse> internalStreams = new ArrayList<DettaglioStreamResponse>();
	private DettaglioSmartobjectResponse smartobject;
	private Double fps;
	
	public DettaglioStreamResponse(DettaglioStream dettaglioStream, DettaglioSmartobject dettaglioSmartobject, 
			List<DettaglioStream> listInternalStream) throws Exception {
		super(dettaglioStream);
		
		addInternalStreams(listInternalStream);
		Util.addSharingTenants(dettaglioStream.getSharingTenant(), this.sharingTenants);
		Util.addComponents(dettaglioStream.getComponents(), this.components);
		this.dcat = new DcatResponse(dettaglioStream.getDcat());
		this.license = new LicenseResponse(dettaglioStream.getLicense());
		this.openData = new OpenDataResponse(dettaglioStream);
		this.twitterInfo = new TwitterInfoResponse(dettaglioStream);
		this.usedInInternalCount = dettaglioStream.getUsedInInternalCount();
		this.streamsCountBySO = dettaglioStream.getStreamsCountBySO();
		this.internalquery = dettaglioStream.getInternalquery();
		this.copyright = dettaglioStream.getDataSourceCopyright();
		this.requestername = dettaglioStream.getDataSourceRequesterName();
		this.requestersurname = dettaglioStream.getDataSourceRequesterSurname();
		this.requestermail = dettaglioStream.getDataSourceRequesterMail();
		this.privacyacceptance = dettaglioStream.getDataSourcePrivacyAcceptance();
		this.smartobject = new DettaglioSmartobjectResponse(dettaglioSmartobject);
		this.icon = dettaglioStream.getDataSourceIcon();
		this.fps = dettaglioStream.getFps();

	}

	public DettaglioStreamResponse(DettaglioStream dettaglioStream)throws Exception{
		this.setTenantManager(new TenantResponse(dettaglioStream));
		this.setOrganization(new OrganizationResponse(dettaglioStream));
		this.setIdstream(dettaglioStream.getIdStream());
		this.setVersion(dettaglioStream.getDataSourceVersion());
		this.setStreamcode(dettaglioStream.getStreamCode());
		this.setStreamname(dettaglioStream.getStreamName());
		this.setName(dettaglioStream.getDataSourceName());
		this.setSmartobject(new DettaglioSmartobjectResponse(dettaglioStream));
		Util.addComponents(dettaglioStream.getComponents(), this.components);
	}
	
	private void addInternalStreams(List<DettaglioStream> listInternalStream)throws Exception{
		for (DettaglioStream dettaglioStream : listInternalStream) {
			internalStreams.add(new DettaglioStreamResponse(dettaglioStream));
		}
	}
	
//	private void addSharingTenants(String sharingTenants) throws Exception {
//		if (sharingTenants != null) {
//			
//			ObjectMapper mapper = new ObjectMapper();
//			List<SharingTenantsJson> list = mapper.readValue(sharingTenants, new TypeReference<List<SharingTenantsJson>>() {});
//
//			for (SharingTenantsJson json : list) {
//				this.sharingTenants.add(new TenantResponse(json));
//			}
//		}
//	}
	
//	private void addComponents(String components) throws Exception {
//		if (components != null) {
//			
//			ObjectMapper mapper = new ObjectMapper();
//			List<ComponentJson> listComponentJson = mapper.readValue(components, new TypeReference<List<ComponentJson>>() {});
//
//			for (ComponentJson componentJson : listComponentJson) {
//				this.components.add(new ComponentResponse(componentJson));
//			}
//		}
//	}
	
	public DettaglioStreamResponse() {
		super();
	}


	public Long getUsedInInternalCount() {
		return usedInInternalCount;
	}


	public void setUsedInInternalCount(Long usedInInternalCount) {
		this.usedInInternalCount = usedInInternalCount;
	}


	public Long getStreamsCountBySO() {
		return streamsCountBySO;
	}


	public void setStreamsCountBySO(Long streamsCountBySO) {
		this.streamsCountBySO = streamsCountBySO;
	}


	public String getInternalquery() {
		return internalquery;
	}


	public void setInternalquery(String internalquery) {
		this.internalquery = internalquery;
	}


	public List<DettaglioStreamResponse> getInternalStreams() {
		return internalStreams;
	}


	public void setInternalStreams(List<DettaglioStreamResponse> internalStreams) {
		this.internalStreams = internalStreams;
	}


	public TwitterInfoResponse getTwitterInfo() {
		return twitterInfo;
	}


	public void setTwitterInfo(TwitterInfoResponse twitterInfo) {
		this.twitterInfo = twitterInfo;
	}


	public OpenDataResponse getOpenData() {
		return openData;
	}


	public void setOpenData(OpenDataResponse openData) {
		this.openData = openData;
	}


	public LicenseResponse getLicense() {
		return license;
	}


	public void setLicense(LicenseResponse license) {
		this.license = license;
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


	public DcatResponse getDcat() {
		return dcat;
	}


	public void setDcat(DcatResponse dcat) {
		this.dcat = dcat;
	}


	public DettaglioSmartobjectResponse getSmartobject() {
		return smartobject;
	}


	public void setSmartobject(DettaglioSmartobjectResponse smartobject) {
		this.smartobject = smartobject;
	}


	public List<ComponentResponse> getComponents() {
		return components;
	}


	public void setComponents(List<ComponentResponse> components) {
		this.components = components;
	}

	public List<TenantResponse> getSharingTenants() {
		return sharingTenants;
	}

	public void setSharingTenants(List<TenantResponse> sharingTenants) {
		this.sharingTenants = sharingTenants;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Double getFps() {
		return fps;
	}

	public void setFps(Double fps) {
		this.fps = fps;
	}
	
	
	
}
