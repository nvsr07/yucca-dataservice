package org.csi.yucca.adminapi.response;

import java.sql.Date;

import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.util.Errors;

public class TenantResponse extends Response{

	private Integer idTenant;
	private String tenantcode;
	private String name;
	private String description;
	private String clientkey;
	private String clientsecret;
	private Integer maxdatasetnum;
	private Integer maxstreamsnum;
	private String tenantPassword;
	private Date activationdate;
	private Date deactivationdate;
	private Integer usagedaysnumber;
	private String username;
	private String userfirstname;
	private String userlastname;
	private String useremail;
	private String usertypeauth;
	private Date creationdate;
	private Date expirationdate;
	private String zeppelin;
	private String hasstage;
	private Integer idEcosystem;
	private Integer idOrganization;
	private Integer idTenantType;
	private Integer idTenantStatus;
	private String datacollectionname;
	private String datacollectiondb;
	private String measurecollectionname;
	private String measurecollectiondb;
	private String mediacollectionname;
	private String mediacollectiondb;
	private String socialcollectionname;
	private String socialcollectiondb;
	private String archivedatacollectionname;
	private String archivedatacollectiondb;
	private String archivemeasurescollectionname;
	private String archivemeasurescollectiondb;
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
	
	public TenantResponse(Errors errors) {
		super(errors);
	}

	public TenantResponse(Tenant tenant) {
		this.idTenant = tenant.getIdTenant();
		this.tenantcode = tenant.getTenantcode();
		this.name = tenant.getName();
		this.description = tenant.getDescription();
		this.clientkey = tenant.getClientkey();
		this.clientsecret = tenant.getClientsecret();
		this.maxdatasetnum = tenant.getMaxdatasetnum();
		this.maxstreamsnum = tenant.getMaxstreamsnum();
		this.tenantPassword = tenant.getTenantPassword();
		this.activationdate = tenant.getActivationdate();
		this.deactivationdate = tenant.getDeactivationdate();
		this.usagedaysnumber = tenant.getUsagedaysnumber();
		this.username = tenant.getUsername();
		this.userfirstname = tenant.getUserfirstname();
		this.userlastname = tenant.getUserlastname();
		this.useremail = tenant.getUseremail();
		this.usertypeauth = tenant.getUsertypeauth();
		this.creationdate = tenant.getCreationdate();
		this.expirationdate = tenant.getExpirationdate();
		this.zeppelin = tenant.getZeppelin();
		this.hasstage = tenant.getHasstage();
		this.idEcosystem = tenant.getIdEcosystem();
		this.idOrganization = tenant.getIdOrganization();
		this.idTenantType = tenant.getIdTenantType();
		this.idTenantStatus = tenant.getIdTenantStatus();
		this.datacollectionname = tenant.getDatacollectionname();
		this.datacollectiondb = tenant.getDatacollectiondb();
		this.measurecollectionname = tenant.getMeasurecollectionname();
		this.measurecollectiondb = tenant.getMeasurecollectiondb();
		this.mediacollectionname = tenant.getMediacollectionname();
		this.mediacollectiondb = tenant.getMediacollectiondb();
		this.socialcollectionname = tenant.getSocialcollectionname();
		this.socialcollectiondb = tenant.getSocialcollectiondb();
		this.archivedatacollectionname = tenant.getArchivedatacollectionname();
		this.archivedatacollectiondb = tenant.getArchivedatacollectiondb();
		this.archivemeasurescollectionname = tenant.getArchivemeasurescollectionname();
		this.archivemeasurescollectiondb = tenant.getArchivemeasurescollectiondb();
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
	public Integer getMaxdatasetnum() {
		return maxdatasetnum;
	}
	public void setMaxdatasetnum(Integer maxdatasetnum) {
		this.maxdatasetnum = maxdatasetnum;
	}
	public Integer getMaxstreamsnum() {
		return maxstreamsnum;
	}
	public void setMaxstreamsnum(Integer maxstreamsnum) {
		this.maxstreamsnum = maxstreamsnum;
	}
	public String getTenantPassword() {
		return tenantPassword;
	}
	public void setTenantPassword(String tenantPassword) {
		this.tenantPassword = tenantPassword;
	}
	public Date getActivationdate() {
		return activationdate;
	}
	public void setActivationdate(Date activationdate) {
		this.activationdate = activationdate;
	}
	public Date getDeactivationdate() {
		return deactivationdate;
	}
	public void setDeactivationdate(Date deactivationdate) {
		this.deactivationdate = deactivationdate;
	}
	public Integer getUsagedaysnumber() {
		return usagedaysnumber;
	}
	public void setUsagedaysnumber(Integer usagedaysnumber) {
		this.usagedaysnumber = usagedaysnumber;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public Date getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}
	public Date getExpirationdate() {
		return expirationdate;
	}
	public void setExpirationdate(Date expirationdate) {
		this.expirationdate = expirationdate;
	}
	public String getZeppelin() {
		return zeppelin;
	}
	public void setZeppelin(String zeppelin) {
		this.zeppelin = zeppelin;
	}
	public String getHasstage() {
		return hasstage;
	}
	public void setHasstage(String hasstage) {
		this.hasstage = hasstage;
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
	public String getDatacollectionname() {
		return datacollectionname;
	}
	public void setDatacollectionname(String datacollectionname) {
		this.datacollectionname = datacollectionname;
	}
	public String getDatacollectiondb() {
		return datacollectiondb;
	}
	public void setDatacollectiondb(String datacollectiondb) {
		this.datacollectiondb = datacollectiondb;
	}
	public String getMeasurecollectionname() {
		return measurecollectionname;
	}
	public void setMeasurecollectionname(String measurecollectionname) {
		this.measurecollectionname = measurecollectionname;
	}
	public String getMeasurecollectiondb() {
		return measurecollectiondb;
	}
	public void setMeasurecollectiondb(String measurecollectiondb) {
		this.measurecollectiondb = measurecollectiondb;
	}
	public String getMediacollectionname() {
		return mediacollectionname;
	}
	public void setMediacollectionname(String mediacollectionname) {
		this.mediacollectionname = mediacollectionname;
	}
	public String getMediacollectiondb() {
		return mediacollectiondb;
	}
	public void setMediacollectiondb(String mediacollectiondb) {
		this.mediacollectiondb = mediacollectiondb;
	}
	public String getSocialcollectionname() {
		return socialcollectionname;
	}
	public void setSocialcollectionname(String socialcollectionname) {
		this.socialcollectionname = socialcollectionname;
	}
	public String getSocialcollectiondb() {
		return socialcollectiondb;
	}
	public void setSocialcollectiondb(String socialcollectiondb) {
		this.socialcollectiondb = socialcollectiondb;
	}
	public String getArchivedatacollectionname() {
		return archivedatacollectionname;
	}
	public void setArchivedatacollectionname(String archivedatacollectionname) {
		this.archivedatacollectionname = archivedatacollectionname;
	}
	public String getArchivedatacollectiondb() {
		return archivedatacollectiondb;
	}
	public void setArchivedatacollectiondb(String archivedatacollectiondb) {
		this.archivedatacollectiondb = archivedatacollectiondb;
	}
	public String getArchivemeasurescollectionname() {
		return archivemeasurescollectionname;
	}
	public void setArchivemeasurescollectionname(String archivemeasurescollectionname) {
		this.archivemeasurescollectionname = archivemeasurescollectionname;
	}
	public String getArchivemeasurescollectiondb() {
		return archivemeasurescollectiondb;
	}
	public void setArchivemeasurescollectiondb(String archivemeasurescollectiondb) {
		this.archivemeasurescollectiondb = archivemeasurescollectiondb;
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
}
