package org.csi.yucca.dataservice.binaryapi.knoxapi.json;

import com.google.gson.annotations.Expose;

public class FileStatuses {

	@Expose
	private FileStatus[] fileStatus;

	public FileStatus[] getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(FileStatus[] fileStatus) {
		this.fileStatus = fileStatus;
	}
	
	
}
