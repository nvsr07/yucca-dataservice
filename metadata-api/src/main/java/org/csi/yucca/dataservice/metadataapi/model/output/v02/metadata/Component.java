package org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata;

import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineJsonField;
import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineJsonFieldElement;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Component {
	private String name;
	private String alias;
	private String measureUnit;
	private String measureUnitCategory;
	private Double tolerance;
	private String phenomenon;
	private String phenomenonCategory;
	private String datatype;
	private Integer inOrder;
	private String sinceVersion;

	public Component() {
		super();
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public String getMeasureUnitCategory() {
		return measureUnitCategory;
	}

	public void setMeasureUnitCategory(String measureUnitCategory) {
		this.measureUnitCategory = measureUnitCategory;
	}

	public Double getTolerance() {
		return tolerance;
	}

	public void setTolerance(Double tolerance) {
		this.tolerance = tolerance;
	}

	public String getPhenomenon() {
		return phenomenon;
	}

	public void setPhenomenon(String phenomenon) {
		this.phenomenon = phenomenon;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public Integer getInOrder() {
		return inOrder;
	}

	public void setInOrder(Integer inOrder) {
		this.inOrder = inOrder;
	}

	public static Component createFromSearchEngineJsonField(SearchEngineJsonField jsonField) {
		Component c = null;
		if (jsonField != null) {
			c = new Component();
			c.setName(jsonField.getFieldName());
			c.setAlias(jsonField.getFieldAlias());
			c.setMeasureUnit(jsonField.getMeasureUnit());
			c.setMeasureUnitCategory(jsonField.getMeasureUnitCategory());
			c.setDatatype(jsonField.getDataType());
			c.setInOrder(jsonField.getOrder());
		}
		return c;
	}

	public static Component createFromSearchEngineJsonFieldElement(SearchEngineJsonFieldElement jsonFieldElement) {
		Component c = null;
		if (jsonFieldElement != null) {
			c = new Component();
			c.setName(jsonFieldElement.getComponentName());
			c.setAlias(jsonFieldElement.getComponentAlias());
			c.setMeasureUnit(jsonFieldElement.getMeasureUnit());
			c.setMeasureUnitCategory(jsonFieldElement.getMeasureUnitCategory());
			c.setDatatype(jsonFieldElement.getDataType());
			c.setTolerance(jsonFieldElement.getTolerance());
			c.setPhenomenon(jsonFieldElement.getPhenomenon());
			c.setSinceVersion(jsonFieldElement.getSinceVersion());
			c.setPhenomenonCategory(jsonFieldElement.getPhenomenonCategory());
		}
		return c;
	} 	

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getSinceVersion() {
		return sinceVersion;
	}

	public void setSinceVersion(String sinceVersion) {
		this.sinceVersion = sinceVersion;
	}

	public String getPhenomenonCategory() {
		return phenomenonCategory;
	}

	public void setPhenomenonCategory(String phenomenonCategory) {
		this.phenomenonCategory = phenomenonCategory;
	}
}
