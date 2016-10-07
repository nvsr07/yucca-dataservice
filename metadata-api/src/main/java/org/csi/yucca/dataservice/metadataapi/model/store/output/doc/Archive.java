package org.csi.yucca.dataservice.metadataapi.model.store.output.doc;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Archive {
	private String archiveCollection;
	private String archiveDatabase;

	public Archive() {
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public String getArchiveCollection() {
		return archiveCollection;
	}

	public void setArchiveCollection(String archiveCollection) {
		this.archiveCollection = archiveCollection;
	}

	public String getArchiveDatabase() {
		return archiveDatabase;
	}

	public void setArchiveDatabase(String archiveDatabase) {
		this.archiveDatabase = archiveDatabase;
	}

}
