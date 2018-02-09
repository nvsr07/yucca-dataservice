package org.csi.yucca.dataservice.binaryapi.knoxapi.json;

import org.csi.yucca.dataservice.binaryapi.util.json.JSonHelper;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileStatuses {

	@Expose
	@SerializedName("FileStatus")
	private FileStatus[] fileStatus;

	public FileStatus[] getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(FileStatus[] fileStatus) {
		this.fileStatus = fileStatus;
	}
	

	
	
}
