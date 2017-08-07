package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.MeasureUnit;
import org.csi.yucca.adminapi.util.Errors;

public class MeasureUnitResponse extends Response{
	
	private Integer idMeasureUnit;
	private String measureunit;
	private String measureunitcategory;
	
	public MeasureUnitResponse(MeasureUnit measureUnit) {
		super();
		this.idMeasureUnit = measureUnit.getIdMeasureUnit();
		this.measureunit = measureUnit.getMeasureunit();
		this.measureunitcategory = measureUnit.getMeasureunitcategory();
	}
	
	public MeasureUnitResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MeasureUnitResponse(Errors errors) {
		super(errors);
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
		return measureunitcategory;
	}
	public void setMeasureunitcategory(String measureunitcategory) {
		this.measureunitcategory = measureunitcategory;
	}
}
