package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.Util;

public class TenantResponse extends Response{

	private Integer idShareType;
	private Integer idTenant;
	private String tenantcode;
	private String name;
	private String description;
	private String clientkey;
	private String clientsecret;
	private String activationdate;
	private String deactivationdate;
	private Integer usagedaysnumber;
	private String userfirstname;
	private String userlastname;
	private String useremail;
	private String usertypeauth;
	private String creationdate;
	private String expirationdate;
	private Integer idEcosystem;
	private Integer idOrganization;
	private Integer idTenantType;
	private Integer idTenantStatus;
	private String datasolrcollectionname;
	private String measuresolrcollectionname;
	private String mediasolrcollectionname;
	private String socialsolrcollectionname;
	private String dataphoenixtablename;
	private String dataphoenixschemaname;
	private String measuresphoenixtablename;
	private String measuresphoenixschemaname;
	private String mediaphoenixtablename;
	private String mediaphoenixschemaname;
	private String socialphoenixtablename;
	private String socialphoenixschemaname;
	
	public TenantResponse() {
		super();
	}
	
	public TenantResponse(Errors errors, String arg) {
		super(errors, arg);
	}

	public TenantResponse(Tenant tenant) {
		
		this.activationdate = Util.dateString(tenant.getActivationdate());
		this.deactivationdate = Util.dateString(tenant.getDeactivationdate());
		this.creationdate = Util.dateString(tenant.getCreationdate());
		this.expirationdate = Util.dateString(tenant.getExpirationdate());
		
		this.idShareType = tenant.getIdShareType();
		this.idTenant = tenant.getIdTenant();
		this.tenantcode = tenant.getTenantcode();
		this.name = tenant.getName();
		this.description = tenant.getDescription();
		this.clientkey = tenant.getClientkey();
		this.clientsecret = tenant.getClientsecret();
		this.usagedaysnumber = tenant.getUsagedaysnumber();
		this.userfirstname = tenant.getUserfirstname();
		this.userlastname = tenant.getUserlastname();
		this.useremail = tenant.getUseremail();
		this.usertypeauth = tenant.getUsertypeauth();
		this.idEcosystem = tenant.getIdEcosystem();
		this.idOrganization = tenant.getIdOrganization();
		this.idTenantType = tenant.getIdTenantType();
		this.idTenantStatus = tenant.getIdTenantStatus();
		this.datasolrcollectionname = tenant.getDatasolrcollectionname();
		this.measuresolrcollectionname = tenant.getMeasuresolrcollectionname();
		this.mediasolrcollectionname = tenant.getMediasolrcollectionname();
		this.socialsolrcollectionname = tenant.getSocialsolrcollectionname();
		this.dataphoenixtablename = tenant.getDataphoenixtablename();
		this.dataphoenixschemaname = tenant.getDataphoenixschemaname();
		this.measuresphoenixtablename = tenant.getMeasuresphoenixtablename();
		this.measuresphoenixschemaname = tenant.getMeasuresphoenixschemaname();
		this.mediaphoenixtablename = tenant.getMediaphoenixtablename();
		this.mediaphoenixschemaname = tenant.getMediaphoenixschemaname();
		this.socialphoenixtablename = tenant.getSocialphoenixtablename();
		this.socialphoenixschemaname = tenant.getSocialphoenixschemaname();
	}

	public Integer getIdShareType() {
		return idShareType;
	}

	public void setIdShareType(Integer idShareType) {
		this.idShareType = idShareType;
	}

	public Integer getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Integer idTenant) {
		this.idTenant = idTenant;
	}

	public String getTenantcode() {
		return tenantcode;
	}

	public void setTenantcode(String tenantcode) {
		this.tenantcode = tenantcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClientkey() {
		return clientkey;
	}

	public void setClientkey(String clientkey) {
		this.clientkey = clientkey;
	}

	public String getClientsecret() {
		return clientsecret;
	}

	public void setClientsecret(String clientsecret) {
		this.clientsecret = clientsecret;
	}

	public Integer getUsagedaysnumber() {
		return usagedaysnumber;
	}

	public void setUsagedaysnumber(Integer usagedaysnumber) {
		this.usagedaysnumber = usagedaysnumber;
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

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getUsertypeauth() {
		return usertypeauth;
	}

	public void setUsertypeauth(String usertypeauth) {
		this.usertypeauth = usertypeauth;
	}

	public Integer getIdEcosystem() {
		return idEcosystem;
	}

	public void setIdEcosystem(Integer idEcosystem) {
		this.idEcosystem = idEcosystem;
	}

	public Integer getIdOrganization() {
		return idOrganization;
	}

	public void setIdOrganization(Integer idOrganization) {
		this.idOrganization = idOrganization;
	}

	public Integer getIdTenantType() {
		return idTenantType;
	}

	public void setIdTenantType(Integer idTenantType) {
		this.idTenantType = idTenantType;
	}

	public Integer getIdTenantStatus() {
		return idTenantStatus;
	}

	public void setIdTenantStatus(Integer idTenantStatus) {
		this.idTenantStatus = idTenantStatus;
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

	public String getMediasolrcollectionname() {
		return mediasolrcollectionname;
	}

	public void setMediasolrcollectionname(String mediasolrcollectionname) {
		this.mediasolrcollectionname = mediasolrcollectionname;
	}

	public String getSocialsolrcollectionname() {
		return socialsolrcollectionname;
	}

	public void setSocialsolrcollectionname(String socialsolrcollectionname) {
		this.socialsolrcollectionname = socialsolrcollectionname;
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

	public String getMeasuresphoenixtablename() {
		return measuresphoenixtablename;
	}

	public void setMeasuresphoenixtablename(String measuresphoenixtablename) {
		this.measuresphoenixtablename = measuresphoenixtablename;
	}

	public String getMeasuresphoenixschemaname() {
		return measuresphoenixschemaname;
	}

	public void setMeasuresphoenixschemaname(String measuresphoenixschemaname) {
		this.measuresphoenixschemaname = measuresphoenixschemaname;
	}

	public String getMediaphoenixtablename() {
		return mediaphoenixtablename;
	}

	public void setMediaphoenixtablename(String mediaphoenixtablename) {
		this.mediaphoenixtablename = mediaphoenixtablename;
	}

	public String getMediaphoenixschemaname() {
		return mediaphoenixschemaname;
	}

	public void setMediaphoenixschemaname(String mediaphoenixschemaname) {
		this.mediaphoenixschemaname = mediaphoenixschemaname;
	}

	public String getSocialphoenixtablename() {
		return socialphoenixtablename;
	}

	public void setSocialphoenixtablename(String socialphoenixtablename) {
		this.socialphoenixtablename = socialphoenixtablename;
	}

	public String getSocialphoenixschemaname() {
		return socialphoenixschemaname;
	}

	public void setSocialphoenixschemaname(String socialphoenixschemaname) {
		this.socialphoenixschemaname = socialphoenixschemaname;
	}

	public String getActivationdate() {
		return activationdate;
	}

	public void setActivationdate(String activationdate) {
		this.activationdate = activationdate;
	}

	public String getDeactivationdate() {
		return deactivationdate;
	}

	public void setDeactivationdate(String deactivationdate) {
		this.deactivationdate = deactivationdate;
	}

	public String getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(String creationdate) {
		this.creationdate = creationdate;
	}

	public String getExpirationdate() {
		return expirationdate;
	}

	public void setExpirationdate(String expirationdate) {
		this.expirationdate = expirationdate;
	}
	

}
