package org.csi.yucca.adminapi.response;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.TagJson;
import org.csi.yucca.adminapi.util.Util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataSourceResponse extends Response {

	private String copyright;
	private String visibility;
	private Integer version;
	private String disclaimer;
	private String registrationDate;
	private Boolean unpublished;
	private String icon;
	private String requestermail;
	private String lastUpdate;
	private String requestername;
	private String requestersurname;
	private String externalReference;
	
	private TenantResponse tenantManager;
	private OrganizationResponse organization;
	private List<TagResponse> tags = new ArrayList<TagResponse>();
	private SubdomainResponse subdomain;
	private DomainResponse domain;
	private StatusResponse status;

	public DataSourceResponse() {
		super();
	}

	public DataSourceResponse(DettaglioStream dettaglioStream) throws Exception {
		setAllParameter(dettaglioStream);
	}

	public DataSourceResponse(DettaglioDataset dettaglioDataset) throws Exception {
		super();
		setAllParameter(dettaglioDataset);
	}

//	public DataSourceResponse(DettaglioStream dettaglioStream, DettaglioDataset dettaglioDataset) throws Exception {
//		super();
//		if (dettaglioStream != null) {
//			setAllParameter(dettaglioStream);
//		} else {
//			setAllParameter(dettaglioDataset);
//		}
//	}
	
	private void addTags(String tags) throws Exception {

		if (tags != null) {

			ObjectMapper mapper = new ObjectMapper();
			List<TagJson> listTagJson = mapper.readValue(tags, new TypeReference<List<TagJson>>() {
			});

			for (TagJson tagJson : listTagJson) {
				this.tags.add(new TagResponse(tagJson));
			}
		}

	}

	public TenantResponse getTenantManager() {
		return tenantManager;
	}

	public void setTenantManager(TenantResponse tenantManager) {
		this.tenantManager = tenantManager;
	}

	public OrganizationResponse getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationResponse organization) {
		this.organization = organization;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public List<TagResponse> getTags() {
		return tags;
	}

	public void setTags(List<TagResponse> tags) {
		this.tags = tags;
	}

	public SubdomainResponse getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(SubdomainResponse subdomain) {
		this.subdomain = subdomain;
	}

	public DomainResponse getDomain() {
		return domain;
	}

	public void setDomain(DomainResponse domain) {
		this.domain = domain;
	}

	public StatusResponse getStatus() {
		return status;
	}

	public void setStatus(StatusResponse status) {
		this.status = status;
	}

	public Boolean getUnpublished() {
		return unpublished;
	}

	public void setUnpublished(Boolean unpublished) {
		this.unpublished = unpublished;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getRequestermail() {
		return requestermail;
	}

	public void setRequestermail(String requestermail) {
		this.requestermail = requestermail;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
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

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	private void setAllParameter(DettaglioDataset dettaglioDataset) throws Exception {
		this.tenantManager = new TenantResponse(dettaglioDataset);
		this.organization = new OrganizationResponse(dettaglioDataset);
		this.copyright = dettaglioDataset.getDataSourceCopyright();
		this.visibility = dettaglioDataset.getDataSourceVisibility();
		this.version = dettaglioDataset.getDatasourceversion();
		this.disclaimer = dettaglioDataset.getDataSourceDisclaimer();
		this.registrationDate = Util.dateString(dettaglioDataset.getDataSourceRegistrationDate());
		this.addTags(dettaglioDataset.getTags());
		this.subdomain = new SubdomainResponse(dettaglioDataset);
		this.domain = new DomainResponse(dettaglioDataset);
		this.status = new StatusResponse(dettaglioDataset);
		this.unpublished = Util.intToBoolean(dettaglioDataset.getDataSourceUnpublished());
		this.icon = dettaglioDataset.getDataSourceIcon();
		this.requestermail = dettaglioDataset.getDataSourceRequesterMail();
		this.lastUpdate = dettaglioDataset.getDataSourceLastUpdate();
		this.requestername = dettaglioDataset.getDataSourceRequesterName();
		this.requestersurname = dettaglioDataset.getDataSourceRequesterSurname();
		this.externalReference = dettaglioDataset.getDataSourceExternalReference();
	}

	private void setAllParameter(DettaglioStream dettaglioStream) throws Exception {
		this.tenantManager = new TenantResponse(dettaglioStream);
		this.organization = new OrganizationResponse(dettaglioStream);
		this.copyright = dettaglioStream.getDataSourceCopyright();
		this.visibility = dettaglioStream.getDataSourceVisibility();
		this.version = dettaglioStream.getDatasourceversion();
		this.disclaimer = dettaglioStream.getDataSourceDisclaimer();
		this.registrationDate = Util.dateString(dettaglioStream.getDataSourceRegistrationDate());
		this.addTags(dettaglioStream.getTags());
		this.subdomain = new SubdomainResponse(dettaglioStream);
		this.domain = new DomainResponse(dettaglioStream);
		this.status = new StatusResponse(dettaglioStream);
		this.unpublished = Util.intToBoolean(dettaglioStream.getDataSourceUnpublished());
		this.icon = dettaglioStream.getDataSourceIcon();
		this.requestermail = dettaglioStream.getDataSourceRequesterMail();
		this.lastUpdate = dettaglioStream.getDataSourceLastUpdate();
		this.requestername = dettaglioStream.getDataSourceRequesterName();
		this.requestersurname = dettaglioStream.getDataSourceRequesterSurname();
		this.externalReference = dettaglioStream.getDataSourceExternalReference();
	}

}
