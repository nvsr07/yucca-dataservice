package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.DettaglioTenantBackoffice;

public class BackofficeDettaglioTenantResponse {

	private Integer idTenant;
	private String description;
	private String password;
	private String tenantcode;
	private String name;
	private Integer usagedaysnumber;
	private String useremail;
	private String userfirstname;
	private String userlastname;
	private String usertypeauth;
	private String username;
	private String datasolrcollectionname;
	private String measuresolrcollectionname;
	private String measuresphoenixschemaname;
	private String measuresphoenixtablename;
	private String mediaphoenixschemaname;
	private String mediaphoenixtablename;
	private String mediasolrcollectionname;
	private String socialphoenixschemaname;
	private String socialphoenixtablename;
	private String socialsolrcollectionname;
	private String dataphoenixtablename;			
	private String dataphoenixschemaname;	
	private BundlesResponse bundles;
	private EcosystemResponse ecosystem; 
	private OrganizationResponse organization;
	private TenantStatusResponse tenantStatus;
	private TenantTypeResponse tenantType;
	private ShareTypeResponse shareType;

	
	public BackofficeDettaglioTenantResponse(DettaglioTenantBackoffice dettaglioTenant) {
		super();
		this.password = dettaglioTenant.getPassword();
		this.tenantcode = dettaglioTenant.getTenantcode();
		this.idTenant = dettaglioTenant.getIdTenant();
		this.description = dettaglioTenant.getDescription();
		this.name = dettaglioTenant.getName();
		this.usagedaysnumber = dettaglioTenant.getUsagedaysnumber();
		this.useremail = dettaglioTenant.getUseremail();
		this.userfirstname = dettaglioTenant.getUserfirstname();
		this.userlastname = dettaglioTenant.getUserlastname();
		this.usertypeauth = dettaglioTenant.getUsertypeauth();
		this.username = dettaglioTenant.getUsername();
		this.bundles = new BundlesResponse(dettaglioTenant);
		this.ecosystem = new EcosystemResponse(dettaglioTenant);
		this.organization = new OrganizationResponse(dettaglioTenant);
		this.tenantStatus = new TenantStatusResponse(dettaglioTenant);
		this.tenantType = new TenantTypeResponse(dettaglioTenant);
		this.shareType = new ShareTypeResponse(dettaglioTenant);
		this.datasolrcollectionname = dettaglioTenant.getDatasolrcollectionname();
		this.measuresolrcollectionname = dettaglioTenant.getMeasuresolrcollectionname();
		this.measuresphoenixschemaname = dettaglioTenant.getMeasuresphoenixschemaname();
		this.measuresphoenixtablename = dettaglioTenant.getMeasuresphoenixtablename();
		this.mediaphoenixschemaname = dettaglioTenant.getMediaphoenixschemaname();
		this.mediaphoenixtablename = dettaglioTenant.getMediaphoenixtablename();
		this.mediasolrcollectionname = dettaglioTenant.getMediasolrcollectionname();
		this.socialphoenixschemaname = dettaglioTenant.getSocialphoenixschemaname();
		this.socialphoenixtablename = dettaglioTenant.getSocialphoenixtablename();
		this.socialsolrcollectionname = dettaglioTenant.getSocialsolrcollectionname();
		this.dataphoenixtablename = dettaglioTenant.getDataphoenixtablename();			
		this.dataphoenixschemaname = dettaglioTenant.getDataphoenixschemaname();	
	}
	
	public String getDataphoenixtablename() {
		return dataphoenixtablename;
	}

	public void setDataphoenixtablename(String dataphoenixtablename) {
		this.dataphoenixtablename = dataphoenixtablename;
	}

	public String getDataphoenixschemaname() {
		return dataphoenixschemaname;
	}

	public void setDataphoenixschemaname(String dataphoenixschemaname) {
		this.dataphoenixschemaname = dataphoenixschemaname;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTenantcode() {
		return tenantcode;
	}
	public void setTenantcode(String tenantcode) {
		this.tenantcode = tenantcode;
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
	public String getDatasolrcollectionname() {
		return datasolrcollectionname;
	}
	public void setDatasolrcollectionname(String datasolrcollectionname) {
		this.datasolrcollectionname = datasolrcollectionname;
	}
	public String getMeasuresolrcollectionname() {
		return measuresolrcollectionname;
	}
	public void setMeasuresolrcollectionname(String measuresolrcollectionname) {
		this.measuresolrcollectionname = measuresolrcollectionname;
	}
	public String getMeasuresphoenixschemaname() {
		return measuresphoenixschemaname;
	}
	public void setMeasuresphoenixschemaname(String measuresphoenixschemaname) {
		this.measuresphoenixschemaname = measuresphoenixschemaname;
	}
	public String getMeasuresphoenixtablename() {
		return measuresphoenixtablename;
	}
	public void setMeasuresphoenixtablename(String measuresphoenixtablename) {
		this.measuresphoenixtablename = measuresphoenixtablename;
	}
	public String getMediaphoenixschemaname() {
		return mediaphoenixschemaname;
	}
	public void setMediaphoenixschemaname(String mediaphoenixschemaname) {
		this.mediaphoenixschemaname = mediaphoenixschemaname;
	}
	public String getMediaphoenixtablename() {
		return mediaphoenixtablename;
	}
	public void setMediaphoenixtablename(String mediaphoenixtablename) {
		this.mediaphoenixtablename = mediaphoenixtablename;
	}
	public String getMediasolrcollectionname() {
		return mediasolrcollectionname;
	}
	public void setMediasolrcollectionname(String mediasolrcollectionname) {
		this.mediasolrcollectionname = mediasolrcollectionname;
	}
	public String getSocialphoenixschemaname() {
		return socialphoenixschemaname;
	}
	public void setSocialphoenixschemaname(String socialphoenixschemaname) {
		this.socialphoenixschemaname = socialphoenixschemaname;
	}
	public String getSocialphoenixtablename() {
		return socialphoenixtablename;
	}
	public void setSocialphoenixtablename(String socialphoenixtablename) {
		this.socialphoenixtablename = socialphoenixtablename;
	}
	public String getSocialsolrcollectionname() {
		return socialsolrcollectionname;
	}
	public void setSocialsolrcollectionname(String socialsolrcollectionname) {
		this.socialsolrcollectionname = socialsolrcollectionname;
	}
	
}
