package org.csi.yucca.adminapi.delegate;

public class SolrStreamComponent {
	private String idComponent;
	private String componentName;
	private String componentAlias;
	private String dataType;
	private Double tolerance;
	private String measureUnit;
	private String measureUnitCategory;
	private String phenomenon;
	private String phenomenonCategory;
	private String sinceVersion;


	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getComponentAlias() {
		return componentAlias;
	}

	public void setComponentAlias(String componentAlias) {
		this.componentAlias = componentAlias;
	}

	public Double getTolerance() {
		return tolerance;
	}

	public void setTolerance(Double tolerance) {
		this.tolerance = tolerance;
	}

	public String getMeasureUnitCategory() {
		return measureUnitCategory;
	}

	public void setMeasureUnitCategory(String measureUnitCategory) {
		this.measureUnitCategory = measureUnitCategory;
	}

	public String getPhenomenon() {
		return phenomenon;
	}

	public void setPhenomenon(String phenomenon) {
		this.phenomenon = phenomenon;
	}

	public String getPhenomenonCategory() {
		return phenomenonCategory;
	}

	public void setPhenomenonCategory(String phenomenonCategory) {
		this.phenomenonCategory = phenomenonCategory;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public String getSinceVersion() {
		return sinceVersion;
	}

	public void setSinceVersion(String sinceVersion) {
		this.sinceVersion = sinceVersion;
	}

	public String getIdComponent() {
		return idComponent;
	}

	public void setIdComponent(String idComponent) {
		this.idComponent = idComponent;
	}
	
	

	

}
