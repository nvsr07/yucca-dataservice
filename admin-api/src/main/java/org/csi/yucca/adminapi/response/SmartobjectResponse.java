package org.csi.yucca.adminapi.response;

import java.sql.Timestamp;

import org.csi.yucca.adminapi.model.Smartobject;
import org.csi.yucca.adminapi.request.SoPositionRequest;
import org.csi.yucca.adminapi.util.Errors;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

public class SmartobjectResponse extends Response {

	private Integer idSmartObject;
	private String socode;
	private String name;
	private String description;
	private String urladmin;
	private String fbcoperationfeedback;
	private String swclientversion;
	private Integer version;
	private String model;
	private Integer deploymentversion;
	private String sostatus;
	private Timestamp creationdate;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private SoPositionResponse position; 
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String twtusername;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long twtmaxsearchnumber;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long twtmaxsearchinterval;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String twtusertoken;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String twttokensecret;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String twtname;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long twtuserid;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer twtmaxstreams;
	
	private String slug;
	private Integer idLocationType;
	private Integer idExposureType;
	private Integer idSupplyType;
	private Integer idSoCategory;
	private Integer idSoType;
	private Integer idStatus;
	private Integer idOrganization;

	public SmartobjectResponse(Smartobject smartobject, SoPositionRequest positionRequest) {
		super();
		if (positionRequest != null) {
			SoPositionResponse positionResponse = new SoPositionResponse();
			BeanUtils.copyProperties(positionRequest, positionResponse);
			this.position=positionResponse;
		}
		this.idSmartObject = smartobject.getIdSmartObject();
		this.socode = smartobject.getSocode();
		this.name = smartobject.getName();
		this.description = smartobject.getDescription();
		this.urladmin = smartobject.getUrladmin();
		this.fbcoperationfeedback = smartobject.getFbcoperationfeedback();
		this.swclientversion = smartobject.getSwclientversion();
		this.version = smartobject.getVersion();
		this.model = smartobject.getModel();
		this.deploymentversion = smartobject.getDeploymentversion();
		this.sostatus = smartobject.getSostatus();
		this.creationdate = smartobject.getCreationdate();
		this.twtusername = smartobject.getTwtusername();
		this.twtmaxsearchnumber = smartobject.getTwtmaxsearchnumber();
		this.twtmaxsearchinterval = smartobject.getTwtmaxsearchinterval();
		this.twtusertoken = smartobject.getTwtusertoken();
		this.twttokensecret = smartobject.getTwttokensecret();
		this.twtname = smartobject.getTwtname();
		this.twtuserid = smartobject.getTwtuserid();
		this.twtmaxstreams = smartobject.getTwtmaxstreams();
		this.slug = smartobject.getSlug();
		this.idLocationType = smartobject.getIdLocationType();
		this.idExposureType = smartobject.getIdExposureType();
		this.idSupplyType = smartobject.getIdSupplyType();
		this.idSoCategory = smartobject.getIdSoCategory();
		this.idSoType = smartobject.getIdSoType();
		this.idStatus = smartobject.getIdStatus();
		this.idOrganization = smartobject.getIdOrganization();
	}

	public SmartobjectResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SmartobjectResponse(Errors errors, String arg) {
		super(errors, arg);
		// TODO Auto-generated constructor stub
	}

	public Integer getIdSmartObject() {
		return idSmartObject;
	}

	public void setIdSmartObject(Integer idSmartObject) {
		this.idSmartObject = idSmartObject;
	}

	public String getSocode() {
		return socode;
	}

	public void setSocode(String socode) {
		this.socode = socode;
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

	public String getUrladmin() {
		return urladmin;
	}

	public void setUrladmin(String urladmin) {
		this.urladmin = urladmin;
	}

	public String getFbcoperationfeedback() {
		return fbcoperationfeedback;
	}

	public void setFbcoperationfeedback(String fbcoperationfeedback) {
		this.fbcoperationfeedback = fbcoperationfeedback;
	}

	public String getSwclientversion() {
		return swclientversion;
	}

	public void setSwclientversion(String swclientversion) {
		this.swclientversion = swclientversion;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getDeploymentversion() {
		return deploymentversion;
	}

	public void setDeploymentversion(Integer deploymentversion) {
		this.deploymentversion = deploymentversion;
	}

	public String getSostatus() {
		return sostatus;
	}

	public void setSostatus(String sostatus) {
		this.sostatus = sostatus;
	}

	public Timestamp getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(Timestamp creationdate) {
		this.creationdate = creationdate;
	}

	public String getTwtusername() {
		return twtusername;
	}

	public void setTwtusername(String twtusername) {
		this.twtusername = twtusername;
	}

	public Long getTwtmaxsearchnumber() {
		return twtmaxsearchnumber;
	}

	public void setTwtmaxsearchnumber(Long twtmaxsearchnumber) {
		this.twtmaxsearchnumber = twtmaxsearchnumber;
	}

	public Long getTwtmaxsearchinterval() {
		return twtmaxsearchinterval;
	}

	public void setTwtmaxsearchinterval(Long twtmaxsearchinterval) {
		this.twtmaxsearchinterval = twtmaxsearchinterval;
	}

	public String getTwtusertoken() {
		return twtusertoken;
	}

	public void setTwtusertoken(String twtusertoken) {
		this.twtusertoken = twtusertoken;
	}

	public String getTwttokensecret() {
		return twttokensecret;
	}

	public void setTwttokensecret(String twttokensecret) {
		this.twttokensecret = twttokensecret;
	}

	public String getTwtname() {
		return twtname;
	}

	public void setTwtname(String twtname) {
		this.twtname = twtname;
	}

	public Long getTwtuserid() {
		return twtuserid;
	}

	public void setTwtuserid(Long twtuserid) {
		this.twtuserid = twtuserid;
	}

	public Integer getTwtmaxstreams() {
		return twtmaxstreams;
	}

	public void setTwtmaxstreams(Integer twtmaxstreams) {
		this.twtmaxstreams = twtmaxstreams;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public Integer getIdLocationType() {
		return idLocationType;
	}

	public void setIdLocationType(Integer idLocationType) {
		this.idLocationType = idLocationType;
	}

	public Integer getIdExposureType() {
		return idExposureType;
	}

	public void setIdExposureType(Integer idExposureType) {
		this.idExposureType = idExposureType;
	}

	public Integer getIdSupplyType() {
		return idSupplyType;
	}

	public void setIdSupplyType(Integer idSupplyType) {
		this.idSupplyType = idSupplyType;
	}

	public Integer getIdSoCategory() {
		return idSoCategory;
	}

	public void setIdSoCategory(Integer idSoCategory) {
		this.idSoCategory = idSoCategory;
	}

	public Integer getIdSoType() {
		return idSoType;
	}

	public void setIdSoType(Integer idSoType) {
		this.idSoType = idSoType;
	}

	public Integer getIdStatus() {
		return idStatus;
	}

	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}

	public Integer getIdOrganization() {
		return idOrganization;
	}

	public void setIdOrganization(Integer idOrganization) {
		this.idOrganization = idOrganization;
	}

	public SoPositionResponse getPosition() {
		return position;
	}

	public void setPosition(SoPositionResponse position) {
		this.position = position;
	}


}
