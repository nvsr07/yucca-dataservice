package org.csi.yucca.dataservice.insertdataapi.hdfs.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class FileStatuses {

	@JsonProperty("FileStatus")
	private List<FileStatus> fileStatus;

	public FileStatuses() {
		super();
	}

	public List<FileStatus> getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(List<FileStatus> fileStatus) {
		this.fileStatus = fileStatus;
	}

	@Override
	public String toString() {
		return "ClassPojo [FileStatus = " + fileStatus + "]";
	}
}