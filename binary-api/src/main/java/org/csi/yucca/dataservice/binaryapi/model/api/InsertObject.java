package org.csi.yucca.dataservice.binaryapi.model.api;

import java.util.List;

import com.google.gson.annotations.Expose;

public class InsertObject {

    @Expose
    private String datasetCode;
    @Expose
    private List<MediaObject> values;
    
	public String getDatasetCode() {
		return datasetCode;
	}
	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
	}
	public List<MediaObject> getValues() {
		return values;
	}
	public void setValues(List<MediaObject> values) {
		this.values = values;
	}
	public void addMediaObject(MediaObject v) {
		this.values.add(v);
	}
	/*
	public MediaObject[] getValues(){
		MediaObject[] simpleArray = new MediaObject[ this.values.size() ];
		this.values.toArray( simpleArray );
		return simpleArray;
	}
	 */
}
