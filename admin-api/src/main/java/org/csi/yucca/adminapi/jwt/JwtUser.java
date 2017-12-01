package org.csi.yucca.adminapi.jwt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minidev.json.JSONObject;

public class JwtUser {

	public static final String KEY_APPLICATION_NAME = "http://wso2.org/gateway/applicationname";
	public static final String KEY_ROLES = "http://wso2.org/claims/role";
	public static final String KEY_TERM_CONDITION_TENANTS = "http://wso2.org/claims/identity/termConditionTenants";
	public static final String KEY_ISS = "iss";
	public static final String KEY_LASTNAME = "http://wso2.org/claims/lastname";
	public static final String KEY_EXP = "exp";
	public static final String KEY_GIVENNAME = "http://wso2.org/claims/givenname";
	public static final String KEY_SUBSCRIBER = "http://wso2.org/gateway/subscriber";
	public static final String KEY_ENDUSER = "http://wso2.org/gateway/enduser";

	public static final String ROLES_SEPARETOR = ",";
	public static final String TERM_COND_TENANT_SEPARETOR = "|";
	
	private String applicationName;
	private List<String> roles;
	private List<String> termConditionTenants;
	private String iss;
	private String lastname;
	private Integer exp;
	private String givenname;
	private String subscriber;
	private String enduser;
	
	public JwtUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JwtUser(JSONObject object ) {
		super();
		this.applicationName = object.getAsString(KEY_APPLICATION_NAME);
		this.roles = (object.getAsString(KEY_ROLES)!=null) ? Arrays.asList(object.getAsString(KEY_ROLES).split(ROLES_SEPARETOR)) : null;
		setTermConditionTenants(object.getAsString(KEY_TERM_CONDITION_TENANTS));
		this.iss = object.getAsString(KEY_ISS);
		this.lastname = object.getAsString(KEY_LASTNAME);
		this.exp = object.getAsNumber(KEY_EXP).intValue();
		this.givenname = object.getAsString(KEY_GIVENNAME);
		this.subscriber = object.getAsString(KEY_SUBSCRIBER);
		this.enduser = object.getAsString(KEY_ENDUSER);
	}
	
	public void setTermConditionTenants(String termConditionTenants) {
		
		if (termConditionTenants != null) {

			String [] termConditionTenantsArray = termConditionTenants.replace("|", ",").split(",");
			
			List<String> termConditionTenantsList = Arrays.asList(termConditionTenantsArray);

			setTermConditionTenants(listRemove(termConditionTenantsList));
		}
		
	}
	
	private List<String> listRemove(List<String> list) {
		List<String> result = new ArrayList<String>();

	    for (String str : list) {
	        if (str != null && !str.isEmpty()) {
	            result.add(str);
	        }
	    }
	    return result;
	}
	
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public List<String> getTermConditionTenants() {
		return termConditionTenants;
	}
	public void setTermConditionTenants(List<String> termConditionTenants) {
		this.termConditionTenants = termConditionTenants;
	}
	public String getIss() {
		return iss;
	}
	public void setIss(String iss) {
		this.iss = iss;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Integer getExp() {
		return exp;
	}
	public void setExp(Integer exp) {
		this.exp = exp;
	}
	public String getGivenname() {
		return givenname;
	}
	public void setGivenname(String givenname) {
		this.givenname = givenname;
	}
	public String getSubscriber() {
		return subscriber;
	}
	public void setSubscriber(String subscriber) {
		this.subscriber = subscriber;
	}
	public String getEnduser() {
		return enduser;
	}
	public void setEnduser(String enduser) {
		this.enduser = enduser;
	}
	
}