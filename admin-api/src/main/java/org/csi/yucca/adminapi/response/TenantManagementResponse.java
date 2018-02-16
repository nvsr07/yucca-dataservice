package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.TenantManagement;
import org.csi.yucca.adminapi.util.Errors;

public class TenantManagementResponse extends Response{

	private String dataSolrCollectionName; 
	
	private String measureSolrCollectionName; 
	
	private String mediaSolrCollectionName;
	
	private String socialSolrCollectionName;
	
	private Integer idTenant;

	private String description;

	private String username;

	private String name;

	private String tenantcode;

	private Integer usagedaysnumber;
	
	private String useremail;
	
	private String userfirstname;
	
	private String userlastname;
	
	private String usertypeauth;

	private BundlesResponse bundles;
	
	private EcosystemResponse ecosystem;

    private OrganizationResponse organization;	
	
	private TenantStatusResponse tenantStatus;
	
	private TenantTypeResponse tenantType;
	
	private ShareTypeResponse shareType;
	
	public TenantManagementResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TenantManagementResponse(Errors errors, String arg) {
		super(errors, arg);
		// TODO Auto-generated constructor stub
	}

	public TenantManagementResponse(TenantManagement tenantManagement) {
		super();
		
		this.dataSolrCollectionName = tenantManagement.getDataSolrCollectionName(); 
		this.measureSolrCollectionName = tenantManagement.getMeasureSolrCollectionName(); 
		this.mediaSolrCollectionName = tenantManagement.getMediaSolrCollectionName();
		this.socialSolrCollectionName = tenantManagement.getSocialSolrCollectionName();
		
		this.username = tenantManagement.getUsername();
		this.bundles = new BundlesResponse(tenantManagement);
		this.ecosystem = new EcosystemResponse(tenantManagement);
		this.organization = new OrganizationResponse(tenantManagement);
		this.tenantStatus = new TenantStatusResponse(tenantManagement);
		this.tenantType = new TenantTypeResponse(tenantManagement);
		this.shareType = new ShareTypeResponse(tenantManagement);
		
		this.idTenant = tenantManagement.getIdTenant();
		this.description = tenantManagement.getDescription();
		this.name = tenantManagement.getName();
		this.tenantcode = tenantManagement.getTenantcode();
		this.usagedaysnumber = tenantManagement.getUsagedaysnumber();
		this.useremail = tenantManagement.getUseremail();
		this.userfirstname = tenantManagement.getUserfirstname();
		this.userlastname = tenantManagement.getUserlastname();
		this.usertypeauth = tenantManagement.getUsertypeauth();
	}

	public String getDataSolrCollectionName() {
		return dataSolrCollectionName;
	}

	public void setDataSolrCollectionName(String dataSolrCollectionName) {
		this.dataSolrCollectionName = dataSolrCollectionName;
	}

	public String getMeasureSolrCollectionName() {
		return measureSolrCollectionName;
	}

	public void setMeasureSolrCollectionName(String measureSolrCollectionName) {
		this.measureSolrCollectionName = measureSolrCollectionName;
	}

	public String getMediaSolrCollectionName() {
		return mediaSolrCollectionName;
	}

	public void setMediaSolrCollectionName(String mediaSolrCollectionName) {
		this.mediaSolrCollectionName = mediaSolrCollectionName;
	}

	public String getSocialSolrCollectionName() {
		return socialSolrCollectionName;
	}

	public void setSocialSolrCollectionName(String socialSolrCollectionName) {
		this.socialSolrCollectionName = socialSolrCollectionName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public BundlesResponse getBundles() {
		return bundles;
	}

	public void setBundles(BundlesResponse bundles) {
		this.bundles = bundles;
	}

	public EcosystemResponse getEcosystem() {
		return ecosystem;
	}

	public void setEcosystem(EcosystemResponse ecosystem) {
		this.ecosystem = ecosystem;
	}

	public OrganizationResponse getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationResponse organization) {
		this.organization = organization;
	}

	public TenantStatusResponse getTenantStatus() {
		return tenantStatus;
	}

	public void setTenantStatus(TenantStatusResponse tenantStatus) {
		this.tenantStatus = tenantStatus;
	}

	public TenantTypeResponse getTenantType() {
		return tenantType;
	}

	public void setTenantType(TenantTypeResponse tenantType) {
		this.tenantType = tenantType;
	}

	public ShareTypeResponse getShareType() {
		return shareType;
	}

	public void setShareType(ShareTypeResponse shareType) {
		this.shareType = shareType;
	}

	public Integer getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Integer idTenant) {
		this.idTenant = idTenant;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTenantcode() {
		return tenantcode;
	}

	public void setTenantcode(String tenantcode) {
		this.tenantcode = tenantcode;
	}

	public Integer getUsagedaysnumber() {
		return usagedaysnumber;
	}

	public void setUsagedaysnumber(Integer usagedaysnumber) {
		this.usagedaysnumber = usagedaysnumber;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getUserfirstname() {
		return userfirstname;
	}

	public void setUserfirstname(String userfirstname) {
		this.userfirstname = userfirstname;
	}

	public String getUserlastname() {
		return userlastname;
	}

	public void setUserlastname(String userlastname) {
		this.userlastname = userlastname;
	}

	public String getUsertypeauth() {
		return usertypeauth;
	}

	public void setUsertypeauth(String usertypeauth) {
		this.usertypeauth = usertypeauth;
	}
	
	
	
}
