package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;
import org.csi.yucca.adminapi.util.Util;

public class DettaglioSmartobjectResponse extends Response {
	
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
	private String creationdate;
	private String twtusername;
	private String twtusertoken;
	private String twttokensecret;
	private String twtname;
	private Long twtuserid;
	private Integer twtmaxstreams;
	private String slug;
	// -----------------------------------------------------
	private OrganizationResponse organization;
	private StatusResponse status;
	private SoTypeResponse soType;
	private SoCategoryResponse soCategory;
	private SupplyTypeResponse supplyType;
	private ExposureTypeResponse exposureType;
	private LocationTypeResponse locationType;
	private DettaglioSmartobjectPositionResponse position;
	// -----------------------------------------------------
	public DettaglioSmartobjectResponse(DettaglioSmartobject smartobject) {
		super();
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
		this.creationdate =  Util.dateString(smartobject.getCreationdate());
		this.twtusername = smartobject.getTwtusername();
		this.twtusertoken = smartobject.getTwtusertoken();
		this.twttokensecret = smartobject.getTwttokensecret();
		this.twtname = smartobject.getTwtname();
		this.twtuserid = smartobject.getTwtuserid();
		this.twtmaxstreams = smartobject.getTwtmaxstreams();
		this.slug = smartobject.getSlug();
		
		this.organization = new OrganizationResponse(smartobject);
		this.status = new StatusResponse(smartobject);
		this.soType = new SoTypeResponse(smartobject);
		this.soCategory = new SoCategoryResponse(smartobject);
		this.supplyType = new SupplyTypeResponse(smartobject);
		this.exposureType = new ExposureTypeResponse(smartobject);
		this.locationType = new LocationTypeResponse(smartobject);
		this.position = new DettaglioSmartobjectPositionResponse(smartobject);
	}	
	// -----------------------------------------------------
	
	public Integer getIdSmartObject() {
		return idSmartObject;
	}
	public DettaglioSmartobjectPositionResponse getPosition() {
		if(position != null && position.isEmpty()) return null;
		return position;
	}

	public void setPosition(DettaglioSmartobjectPositionResponse position) {
		this.position = position;
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
	public String getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(String creationdate) {
		this.creationdate = creationdate;
	}
	public String getTwtusername() {
		return twtusername;
	}
	public void setTwtusername(String twtusername) {
		this.twtusername = twtusername;
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
	public SoTypeResponse getSoType() {
		return soType;
	}
	public void setSoType(SoTypeResponse soType) {
		this.soType = soType;
	}
	public SoCategoryResponse getSoCategory() {
		if(soCategory != null && soCategory.isEmpty()) return null;
		return soCategory;
	}
	public void setSoCategory(SoCategoryResponse soCategory) {
		this.soCategory = soCategory;
	}
	public SupplyTypeResponse getSupplyType() {
		if(supplyType != null && supplyType.isEmpty()) return null;
		return supplyType;
	}
	public void setSupplyType(SupplyTypeResponse supplyType) {
		this.supplyType = supplyType;
	}
	public ExposureTypeResponse getExposureType() {
		if(exposureType != null && exposureType.isEmpty()) return null;
		return exposureType;
	}
	public void setExposureType(ExposureTypeResponse exposureType) {
		this.exposureType = exposureType;
	}
	public LocationTypeResponse getLocationType() {
		if(locationType != null && locationType.isEmpty()) return null;
		return locationType;
	}
	public void setLocationType(LocationTypeResponse locationType) {
		this.locationType = locationType;
	}
}
