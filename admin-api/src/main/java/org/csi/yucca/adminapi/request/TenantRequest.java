package org.csi.yucca.adminapi.request;

public class TenantRequest {

	private String userPassword;
	private String creationDate;
	private Integer idTenantType;
	private Integer idEcosystem;	
	private String useremail;
	private String userfirstname;
	private String userlastname;
	private String username;
	private String usertypeauth;

	private String tenantcode;
	private String name;
	private String description;
	private Integer IdOrganization;

	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public Integer getIdTenantType() {
		return idTenantType;
	}
	public void setIdTenantType(Integer idTenantType) {
		this.idTenantType = idTenantType;
	}
	public Integer getIdEcosystem() {
		return idEcosystem;
	}
	public void setIdEcosystem(Integer idEcosystem) {
		this.idEcosystem = idEcosystem;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsertypeauth() {
		return usertypeauth;
	}
	public void setUsertypeauth(String usertypeauth) {
		this.usertypeauth = usertypeauth;
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
	public Integer getIdOrganization() {
		return IdOrganization;
	}
	public void setIdOrganization(Integer idOrganization) {
		IdOrganization = idOrganization;
	}

}
