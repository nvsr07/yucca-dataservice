package org.csi.yucca.adminapi.request;

public class ComponentRequest {
	
	private Integer idComponent;
	private String name;
	private String alias;
	private Integer inorder;
	private Integer tolerance;
	private Integer idPhenomenon;
	private Integer idDataType;
	private Integer idMeasureUnit;
	private Boolean required;
	private Boolean iskey;
	private String datetimeformat;
	private Integer sourcecolumn;
	private String sourcecolumnname;
	private String foreignkey;
	
	public String getForeignkey() {
		return foreignkey;
	}

	public void setForeignkey(String foreignkey) {
		this.foreignkey = foreignkey;
	}

	public Integer getSourcecolumn() {
		return sourcecolumn;
	}

	public void setSourcecolumn(Integer sourcecolumn) {
		this.sourcecolumn = sourcecolumn;
	}

	public String getSourcecolumnname() {
		return sourcecolumnname;
	}

	public void setSourcecolumnname(String sourcecolumnname) {
		this.sourcecolumnname = sourcecolumnname;
	}

	public String getDatetimeformat() {
		return datetimeformat;
	}

	public void setDatetimeformat(String datetimeformat) {
		this.datetimeformat = datetimeformat;
	}

	public Boolean getIskey() {
		return iskey;
	}

	public void setIskey(Boolean iskey) {
		this.iskey = iskey;
	}

	public Integer getIdComponent() {
		return idComponent;
	}

	public void setIdComponent(Integer idComponent) {
		this.idComponent = idComponent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Integer getInorder() {
		return inorder;
	}

	public void setInorder(Integer inorder) {
		this.inorder = inorder;
	}

	public Integer getTolerance() {
		return tolerance;
	}

	public void setTolerance(Integer tolerance) {
		this.tolerance = tolerance;
	}

	public Integer getIdPhenomenon() {
		return idPhenomenon;
	}

	public void setIdPhenomenon(Integer idPhenomenon) {
		this.idPhenomenon = idPhenomenon;
	}

	public Integer getIdDataType() {
		return idDataType;
	}

	public void setIdDataType(Integer idDataType) {
		this.idDataType = idDataType;
	}

	public Integer getIdMeasureUnit() {
		return idMeasureUnit;
	}

	public void setIdMeasureUnit(Integer idMeasureUnit) {
		this.idMeasureUnit = idMeasureUnit;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

}
