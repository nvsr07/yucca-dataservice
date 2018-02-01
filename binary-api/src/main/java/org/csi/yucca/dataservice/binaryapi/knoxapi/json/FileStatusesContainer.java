package org.csi.yucca.dataservice.binaryapi.knoxapi.json;

import org.csi.yucca.dataservice.binaryapi.util.json.JSonHelper;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileStatusesContainer {

	@Expose
	@SerializedName("FileStatuses")
	private FileStatuses fileStatuses;

	

	
	public FileStatuses getFileStatuses() {
		return fileStatuses;
	}




	public void setFileStatuses(FileStatuses fileStatuses) {
		this.fileStatuses = fileStatuses;
	}




	public static void main(String[] args) {
		
		String resp = "{\"FileStatuses\":{\"FileStatus\":[{\"accessTime\":1478180884618,\"blockSize\":134217728,\"childrenNum\":0,\"fileId\":19962318,\"group\":\"hdfs\",\"length\":3100113,\"modificationTime\":1470123117917,\"owner\":\"sdp\",\"pathSuffix\":\"57a04713dacf057adb21bee4-CopieLibriBi_560-1.csv\",\"permission\":\"640\",\"replication\":3,\"storagePolicy\":0,\"type\":\"FILE\"}]}}";

		
		Gson gson = JSonHelper.getInstance();
		FileStatusesContainer fs = gson.fromJson(resp,FileStatusesContainer.class);
		
		
		System.out.println(fs.getFileStatuses().getFileStatus());
	}
	
}
