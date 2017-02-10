package org.csi.yucca.dataservice.insertdataapi.hdfs.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class POJOHdfs {
	
	@JsonProperty("FileStatuses")
	private FileStatuses fileStatuses;

	public POJOHdfs() {
		super();
	}

	public FileStatuses getFileStatuses() {
		return fileStatuses;
	}

	public void setFileStatuses(FileStatuses fileStatuses) {
		this.fileStatuses = fileStatuses;
	}

	@Override
	public String toString() {
		return "POJOHdfs [FileStatuses = " + getFileStatuses() + "]";
	}
}