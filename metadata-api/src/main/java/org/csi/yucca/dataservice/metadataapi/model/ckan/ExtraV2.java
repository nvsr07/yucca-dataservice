package org.csi.yucca.dataservice.metadataapi.model.ckan;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class ExtraV2 {

	private String package_id;
	private String topic;
	private String hidden_field;
	private String metadata_created;
	private String metadata_modified;
	private String package_created;
	private String package_modified;
	private String tag;
	private String title;
	private String description;
	private String license_id;
	private String package_type;
	private List<String> resource;
	private String disclaimer;
	private String copyright;
	private String dcatCreatorName;
	private String dcatCreatorType;
	private String dcatCreatorId;
	private String dcatRightsHolderName;
	private String dcatRightsHolderType;
	private String dcatRightsHolderId;
	private String dcatNomeOrg;
	private String dcatEmailOrg;

	private List<Component> components;

	private Long dataset_id;
	private Date registration_date;
	private Long registration_date_millis;
	private String smartobject_code;
	private String smartobject_name;
	private String smartobject_description;
	private String smartobject_model;
	private String smartobject_room;
	private String smartobject_floor;
	private Double smartobject_latitude;
	private Double smartobject_longitude;
	private Double smartobject_altitude;
	private String smartobject_building;
	private String domain;
	private String subdomain;

	private String stream_fps;

	public Long getDataset_id() {
		return dataset_id;
	}

	public void setDataset_id(Long dataset_id) {
		this.dataset_id = dataset_id;
	}

	public String getSmartobject_code() {
		return smartobject_code;
	}

	public void setSmartobject_code(String smartobject_code) {
		this.smartobject_code = smartobject_code;
	}

	public String getSmartobject_name() {
		return smartobject_name;
	}

	public void setSmartobject_name(String smartobject_name) {
		this.smartobject_name = smartobject_name;
	}

	public String getSmartobject_description() {
		return smartobject_description;
	}

	public void setSmartobject_description(String smartobject_description) {
		this.smartobject_description = smartobject_description;
	}

	public String getSmartobject_model() {
		return smartobject_model;
	}

	public void setSmartobject_model(String smartobject_model) {
		this.smartobject_model = smartobject_model;
	}

	public String getSmartobject_room() {
		return smartobject_room;
	}

	public void setSmartobject_room(String smartobject_room) {
		this.smartobject_room = smartobject_room;
	}

	public String getSmartobject_floor() {
		return smartobject_floor;
	}

	public void setSmartobject_floor(String smartobject_floor) {
		this.smartobject_floor = smartobject_floor;
	}

	public Double getSmartobject_latitude() {
		return smartobject_latitude;
	}

	public void setSmartobject_latitude(Double smartobject_latitude) {
		this.smartobject_latitude = smartobject_latitude;
	}

	public Double getSmartobject_longitude() {
		return smartobject_longitude;
	}

	public void setSmartobject_longitude(Double smartobject_longitude) {
		this.smartobject_longitude = smartobject_longitude;
	}

	public Double getSmartobject_altitude() {
		return smartobject_altitude;
	}

	public void setSmartobject_altitude(Double smartobject_altitude) {
		this.smartobject_altitude = smartobject_altitude;
	}

	public String getSmartobject_building() {
		return smartobject_building;
	}

	public void setSmartobject_building(String smartobject_building) {
		this.smartobject_building = smartobject_building;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}

	public String getStream_fps() {
		return stream_fps;
	}

	public void setStream_fps(String stream_fps) {
		this.stream_fps = stream_fps;
	}

	public ExtraV2() {
		super();
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public String getDcatCreatorName() {
		return dcatCreatorName;
	}

	public void setDcatCreatorName(String dcatCreatorName) {
		this.dcatCreatorName = dcatCreatorName;
	}

	public String getDcatCreatorType() {
		return dcatCreatorType;
	}

	public void setDcatCreatorType(String dcatCreatorType) {
		this.dcatCreatorType = dcatCreatorType;
	}

	public String getDcatCreatorId() {
		return dcatCreatorId;
	}

	public void setDcatCreatorId(String dcatCreatorId) {
		this.dcatCreatorId = dcatCreatorId;
	}

	public String getDcatRightsHolderName() {
		return dcatRightsHolderName;
	}

	public void setDcatRightsHolderName(String dcatRightsHolderName) {
		this.dcatRightsHolderName = dcatRightsHolderName;
	}

	public String getDcatRightsHolderType() {
		return dcatRightsHolderType;
	}

	public void setDcatRightsHolderType(String dcatRightsHolderType) {
		this.dcatRightsHolderType = dcatRightsHolderType;
	}

	public String getDcatRightsHolderId() {
		return dcatRightsHolderId;
	}

	public void setDcatRightsHolderId(String dcatRightsHolderId) {
		this.dcatRightsHolderId = dcatRightsHolderId;
	}

	public String getDcatNomeOrg() {
		return dcatNomeOrg;
	}

	public void setDcatNomeOrg(String dcatNomeOrg) {
		this.dcatNomeOrg = dcatNomeOrg;
	}

	public String getDcatEmailOrg() {
		return dcatEmailOrg;
	}

	public void setDcatEmailOrg(String dcatEmailOrg) {
		this.dcatEmailOrg = dcatEmailOrg;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getHidden_field() {
		return hidden_field;
	}

	public void setHidden_field(String hidden_field) {
		this.hidden_field = hidden_field;
	}

	public String getMetadata_created() {
		return metadata_created;
	}

	public void setMetadata_created(String metadata_created) {
		this.metadata_created = metadata_created;
	}

	public String getMetadata_modified() {
		return metadata_modified;
	}

	public void setMetadata_modified(String metadata_modified) {
		this.metadata_modified = metadata_modified;
	}

	public String getPackage_created() {
		return package_created;
	}

	public void setPackage_created(String package_created) {
		this.package_created = package_created;
	}

	public String getPackage_modified() {
		return package_modified;
	}

	public void setPackage_modified(String package_modified) {
		this.package_modified = package_modified;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLicense_id() {
		return license_id;
	}

	public void setLicense_id(String license_id) {
		this.license_id = license_id;
	}

	public String getPackage_type() {
		return package_type;
	}

	public void setPackage_type(String package_type) {
		this.package_type = package_type;
	}

	public String getPackage_id() {
		return package_id;
	}

	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}

	public List<String> getResource() {
		return resource;
	}

	public void setResource(List<String> resource) {
		this.resource = resource;
	}

	public void addResource(Resource newResource) {
		if (resource == null)
			resource = new LinkedList<String>();
		resource.add("format=" + newResource.getFormat() + "||url=" + newResource.getUrl());
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

	public Date getRegistration_date() {
		return registration_date;
	}

	public void setRegistration_date(Date registration_date) {
		this.registration_date = registration_date;
	}

	public Long getRegistration_date_millis() {
		return registration_date_millis;
	}

	public void setRegistration_date_millis(Long registration_date_millis) {
		this.registration_date_millis = registration_date_millis;
	}

}
