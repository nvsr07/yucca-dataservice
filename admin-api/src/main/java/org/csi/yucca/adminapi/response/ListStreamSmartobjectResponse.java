package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.DettaglioStream;

public class ListStreamSmartobjectResponse extends Response {

	private Integer idSmartObject;
	private String socode;
	private String name;
	private String description;
	private String slug;
	private SoCategoryResponse soCategory;
	private SoTypeResponse soType;
	
	public ListStreamSmartobjectResponse(DettaglioStream arg) {
		super();
		this.idSmartObject = arg.getIdSmartObject();
		this.socode = arg.getSmartObjectCode();
		this.name = arg.getSmartObjectName();
		this.description = arg.getSmartObjectDescription();
		this.slug = arg.getSmartObjectSlug();
		this.soCategory = new SoCategoryResponse(arg);
		this.soType = new SoTypeResponse(arg);
	}

	public ListStreamSmartobjectResponse() {
		super();
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

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public SoCategoryResponse getSoCategory() {
		return soCategory;
	}

	public void setSoCategory(SoCategoryResponse soCategory) {
		this.soCategory = soCategory;
	}

	public SoTypeResponse getSoType() {
		return soType;
	}

	public void setSoType(SoTypeResponse soType) {
		this.soType = soType;
	}

}
