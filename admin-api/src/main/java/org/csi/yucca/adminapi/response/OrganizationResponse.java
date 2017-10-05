package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.util.Errors;

import com.fasterxml.jackson.annotation.JsonInclude;

public class OrganizationResponse extends Response{
	
	private Integer idOrganization;
	private String organizationcode;
	private String description;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String datasolrcollectionname;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String measuresolrcollectionname;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String mediasolrcollectionname;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String socialsolrcollectionname;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String dataphoenixtablename;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String dataphoenixschemaname;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String measuresphoenixtablename;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String measuresphoenixschemaname;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String mediaphoenixtablename;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String mediaphoenixschemaname;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String socialphoenixtablename;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String socialphoenixschemaname;	
	
	public OrganizationResponse(Organization organization) {
		super();
		this.idOrganization = organization.getIdOrganization();
		this.organizationcode = organization.getOrganizationcode();
		this.description = organization.getDescription();
		this.datasolrcollectionname = organization.getDatasolrcollectionname();
		this.measuresolrcollectionname = organization.getMeasuresolrcollectionname();
		this.mediasolrcollectionname = organization.getMediasolrcollectionname();
		this.socialsolrcollectionname = organization.getSocialsolrcollectionname();
		this.dataphoenixtablename = organization.getDataphoenixtablename();
		this.dataphoenixschemaname = organization.getDataphoenixschemaname();
		this.measuresphoenixtablename = organization.getMeasuresphoenixtablename();
		this.measuresphoenixschemaname = organization.getMeasuresphoenixschemaname();
		this.mediaphoenixtablename = organization.getMediaphoenixtablename();
		this.mediaphoenixschemaname = organization.getMediaphoenixschemaname();
		this.socialphoenixtablename = organization.getSocialphoenixtablename();
		this.socialphoenixschemaname = organization.getSocialphoenixschemaname();			
	}
	
	public OrganizationResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrganizationResponse(Errors errors, String arg) {
		super(errors, arg);
		// TODO Auto-generated constructor stub
	}
	public Integer getIdOrganization() {
		return idOrganization;
	}
	public void setIdOrganization(Integer idOrganization) {
		this.idOrganization = idOrganization;
	}
	public String getOrganizationcode() {
		return organizationcode;
	}
	public void setOrganizationcode(String organizationcode) {
		this.organizationcode = organizationcode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
