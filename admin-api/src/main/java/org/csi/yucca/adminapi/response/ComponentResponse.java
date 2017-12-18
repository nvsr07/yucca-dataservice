package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.ComponentJson;

public class ComponentResponse extends Response {

	private Integer idComponent;
	private String name;
	private String alias;
	private Integer inorder;
	private Double tolerance;
	private PhenomenonResponse phenomenon;
	private DataTypeResponse dataType;
	private MeasureUnitResponse measureUnit;
	private Integer required;
	private Integer sinceVersion;
	private Integer iskey;
	private Integer sourcecolumn;
	private String sourcecolumnname;
	
	public ComponentResponse(ComponentJson componentJson) {
		super();
		this.idComponent = componentJson.getId_component();
		this.name = componentJson.getName();
		this.alias = componentJson.getAlias();
		this.inorder = componentJson.getInorder();
		this.tolerance = componentJson.getTolerance();
		this.phenomenon = new PhenomenonResponse(componentJson);
		this.dataType = new DataTypeResponse(componentJson);
		this.measureUnit = new MeasureUnitResponse(componentJson);
		this.required = componentJson.getRequired();
		this.sinceVersion = componentJson.getSince_version();
		this.iskey = componentJson.getIskey();
		this.sourcecolumn = componentJson.getSourcecolumn();
		this.sourcecolumnname = componentJson.getSourcecolumnname();
	}
	
	public ComponentResponse idDataType(Integer idDataType){
		DataTypeResponse dataTypeResponse = new DataTypeResponse();
		dataTypeResponse.setIdDataType(idDataType);
		this.dataType = dataTypeResponse;
		return this;
	}
	
	public ComponentResponse name(String name){
		this.name = name;
		return this;
	}

	public ComponentResponse idComponent(Integer idComponent){
		this.idComponent = idComponent;
		return this;
	}
	
	public ComponentResponse() {
		super();
		// TODO Auto-generated constructor stub
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

	public Double getTolerance() {
		return tolerance;
	}

	public void setTolerance(Double tolerance) {
		this.tolerance = tolerance;
	}

	public PhenomenonResponse getPhenomenon() {
		return phenomenon;
	}

	public void setPhenomenon(PhenomenonResponse phenomenon) {
		this.phenomenon = phenomenon;
	}

	public DataTypeResponse getDataType() {
		return dataType;
	}

	public void setDataType(DataTypeResponse dataType) {
		this.dataType = dataType;
	}

	public MeasureUnitResponse getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(MeasureUnitResponse measureUnit) {
		this.measureUnit = measureUnit;
	}

	public Integer getRequired() {
		return required;
	}

	public void setRequired(Integer required) {
		this.required = required;
	}

	public Integer getSinceVersion() {
		return sinceVersion;
	}

	public void setSinceVersion(Integer sinceVersion) {
		this.sinceVersion = sinceVersion;
	}

	public Integer getIskey() {
		return iskey;
	}

	public void setIskey(Integer iskey) {
		this.iskey = iskey;
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

}
