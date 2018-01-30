package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.ComponentJson;
import org.csi.yucca.adminapi.model.DataType;
import org.csi.yucca.adminapi.util.Errors;

public class DataTypeResponse extends Response{
	
	private Integer idDataType;
	private String datatypecode;
	private String description;
	
	public DataTypeResponse(ComponentJson componentJson) {
		super();
		
		if (componentJson != null) {
			this.idDataType = componentJson.getDt_id_data_type();
			this.datatypecode = componentJson.getDt_datatypecode();
			this.description = componentJson.getDt_description();			
		}
	}

	public DataTypeResponse(DataType dataType) {
		super();
		this.idDataType = dataType.getIdDataType();
		this.datatypecode = dataType.getDatatypecode();
		this.description = dataType.getDescription();
	}
	
	public DataTypeResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DataTypeResponse(Errors errors, String arg) {
		super(errors, arg);
		// TODO Auto-generated constructor stub
	}
	public Integer getIdDataType() {
		return idDataType;
	}
	public void setIdDataType(Integer idDataType) {
		this.idDataType = idDataType;
	}
	public String getDatatypecode() {
		return datatypecode;
	}
	public void setDatatypecode(String datatypecode) {
		this.datatypecode = datatypecode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
