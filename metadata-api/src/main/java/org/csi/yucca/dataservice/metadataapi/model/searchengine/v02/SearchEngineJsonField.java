package org.csi.yucca.dataservice.metadataapi.model.searchengine.v02;

import java.util.List;

public class SearchEngineJsonField {
	private String fieldName;
	private String fieldAlias;
	private String dataType;
	private Integer sourceColumn;
	private Integer isKey;
	private String measureUnit;
	private String measureUnitCategory;
	private Integer order;

	private List<SearchEngineJsonFieldElement> element;

	public SearchEngineJsonField() {
		super();
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldAlias() {
		return fieldAlias;
	}

	public void setFieldAlias(String fieldAlias) {
		this.fieldAlias = fieldAlias;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getSourceColumn() {
		return sourceColumn;
	}

	public void setSourceColumn(Integer sourceColumn) {
		this.sourceColumn = sourceColumn;
	}

	public Integer getIsKey() {
		return isKey;
	}

	public void setIsKey(Integer isKey) {
		this.isKey = isKey;
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

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public List<SearchEngineJsonFieldElement> getElement() {
		return element;
	}

	public void setElement(List<SearchEngineJsonFieldElement> element) {
		this.element = element;
	}

}
