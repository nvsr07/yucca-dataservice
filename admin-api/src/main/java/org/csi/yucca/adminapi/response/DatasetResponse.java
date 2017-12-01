package org.csi.yucca.adminapi.response;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.TagJson;
import org.csi.yucca.adminapi.util.Util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DatasetResponse extends Response {

	private Integer iddataset;
	private String datasetcode;
	private String datasetname;
	private String description;
	private Integer idDataSourceBinary;  
	private Integer datasourceversionBinary;
	private TenantResponse tenantManager;
	private OrganizationResponse organization;
	private StatusResponse status;
	private DomainResponse domain;
	private SubdomainResponse subdomain;
	private DatasetTypeResponse datasetType;
	private DatasetSubtypeResponse datasetSubtype;
	
	private Integer version;
	private Integer unpublished;
	private String visibility;
	private String registrationdate;
	private List<TagResponse> tags = new ArrayList<TagResponse>();
	
	public DatasetResponse(Dataset dataset) throws Exception {
		super();
		this.idDataSourceBinary = dataset.getIdDataSourceBinary();  
		this.datasourceversionBinary = dataset.getDatasourceversionBinary();
		this.iddataset = dataset.getIddataset();
		this.datasetcode = dataset.getDatasetcode();
		this.datasetname = dataset.getDatasetname();
		this.description = dataset.getDescription();
		
		this.tenantManager = new TenantResponse(dataset);
		this.organization = new OrganizationResponse(dataset);
		this.version = dataset.getDatasourceversion(); // ????
		this.unpublished = dataset.getDataSourceUnpublished();
		this.visibility = dataset.getDataSourceVisibility();
		this.registrationdate = Util.dateString(dataset.getDataSourceRegistrationDate());
		this.status = new StatusResponse(dataset);
		this.domain = new DomainResponse(dataset);
		this.subdomain = new SubdomainResponse(dataset);
		this.addTags(dataset.getTags());
		this.datasetType = new DatasetTypeResponse(dataset);
		this.datasetSubtype = new DatasetSubtypeResponse(dataset);
	}

	private void addTags(String tags) throws Exception {

		if (tags != null) {
			
			ObjectMapper mapper = new ObjectMapper();
			List<TagJson> listTagJson = mapper.readValue(tags, new TypeReference<List<TagJson>>() {});

			for (TagJson tagJson : listTagJson) {
				this.tags.add(new TagResponse(tagJson));
			}
		}

	}

	
	public Integer getIdDataSourceBinary() {
		return idDataSourceBinary;
	}

	public void setIdDataSourceBinary(Integer idDataSourceBinary) {
		this.idDataSourceBinary = idDataSourceBinary;
	}

	public Integer getDatasourceversionBinary() {
		return datasourceversionBinary;
	}

	public void setDatasourceversionBinary(Integer datasourceversionBinary) {
		this.datasourceversionBinary = datasourceversionBinary;
	}

	public DatasetTypeResponse getDatasetType() {
		return datasetType;
	}

	public void setDatasetType(DatasetTypeResponse datasetType) {
		this.datasetType = datasetType;
	}

	public DatasetSubtypeResponse getDatasetSubtype() {
		return datasetSubtype;
	}

	public void setDatasetSubtype(DatasetSubtypeResponse datasetSubtype) {
		this.datasetSubtype = datasetSubtype;
	}

	public DatasetResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getIddataset() {
		return iddataset;
	}

	public void setIddataset(Integer iddataset) {
		this.iddataset = iddataset;
	}

	public String getDatasetcode() {
		return datasetcode;
	}

	public void setDatasetcode(String datasetcode) {
		this.datasetcode = datasetcode;
	}

	public String getDatasetname() {
		return datasetname;
	}

	public void setDatasetname(String datasetname) {
		this.datasetname = datasetname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public StatusResponse getStatus() {
		return status;
	}

	public void setStatus(StatusResponse status) {
		this.status = status;
	}

	public DomainResponse getDomain() {
		return domain;
	}

	public void setDomain(DomainResponse domain) {
		this.domain = domain;
	}

	public SubdomainResponse getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(SubdomainResponse subdomain) {
		this.subdomain = subdomain;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getUnpublished() {
		return unpublished;
	}

	public void setUnpublished(Integer unpublished) {
		this.unpublished = unpublished;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getRegistrationdate() {
		return registrationdate;
	}

	public void setRegistrationdate(String registrationdate) {
		this.registrationdate = registrationdate;
	}

	public List<TagResponse> getTags() {
		return tags;
	}

	public void setTags(List<TagResponse> tags) {
		this.tags = tags;
	}

}
