package org.csi.yucca.adminapi.request;

public class PostTenantRequest extends TenantRequest{
	
	private BundlesRequest bundles;
	private Integer idShareType;
	private Integer idTenant;
	private String clientkey;
	private String clientsecret;
	private Integer usagedaysnumber;
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
	
	public BundlesRequest getBundles() {
		return bundles;
	}
	public void setBundles(BundlesRequest bundles) {
		this.bundles = bundles;
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
		if(usagedaysnumber == null) return -1;
		return usagedaysnumber;
	}
	public void setUsagedaysnumber(Integer usagedaysnumber) {
		this.usagedaysnumber = usagedaysnumber;
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
	
}
