package org.csi.yucca.dataservice.metadataapi.model.store.output.doc;

public class Field {

	private String fieldName;
	private String fieldAlias;
	private String dataType;
	private Integer sourceColumn;
	private Boolean isKey;

	public Field() {
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

	public Boolean getIsKey() {
		return isKey;
	}

	public void setIsKey(Boolean isKey) {
		this.isKey = isKey;
	}

}
