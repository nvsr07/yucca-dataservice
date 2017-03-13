package org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata;

import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineJsonField;
import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineJsonFieldElement;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Component {
	private String name;
	private String alias;
	private String measureunit;
	private Double tolerance;
	private String phenomenon;
	private String datatype;
	private Integer inOrder;

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

	public String getMeasureunit() {
		return measureunit;
	}

	public void setMeasureunit(String measureunit) {
		this.measureunit = measureunit;
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
			c.setMeasureunit(jsonField.getMeasureUnit());
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
			c.setMeasureunit(jsonFieldElement.getMeasureUnit());
			c.setDatatype(jsonFieldElement.getDataType());
			c.setTolerance(jsonFieldElement.getTolerance());
			c.setPhenomenon(jsonFieldElement.getPhenomenon());
		}
		return c;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}
