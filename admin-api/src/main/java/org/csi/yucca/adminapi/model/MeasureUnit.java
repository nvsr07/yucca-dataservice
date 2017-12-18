package org.csi.yucca.adminapi.model;

public class MeasureUnit {

	private Integer idMeasureUnit;
	private String measureunit;
	private String measureunitcategory;
	
	public MeasureUnit(Integer idMeasureUnit, String measureunit, String measureunitcategory) {
		super();
		this.idMeasureUnit = idMeasureUnit;
		this.measureunit = measureunit;
		this.measureunitcategory = measureunitcategory;
	}

	public MeasureUnit() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Integer getIdMeasureUnit() {
		return idMeasureUnit;
	}
	public void setIdMeasureUnit(Integer idMeasureUnit) {
		this.idMeasureUnit = idMeasureUnit;
	}
	public String getMeasureunit() {
		return measureunit;
	}
	public void setMeasureunit(String measureunit) {
		this.measureunit = measureunit;
	}
	public String getMeasureunitcategory() {
		if(this.measureunitcategory != null){
			return this.measureunitcategory.trim();
		}
		return null;
	}
	public void setMeasureunitcategory(String measureunitcategory) {
		this.measureunitcategory = measureunitcategory;
	}
	
}
