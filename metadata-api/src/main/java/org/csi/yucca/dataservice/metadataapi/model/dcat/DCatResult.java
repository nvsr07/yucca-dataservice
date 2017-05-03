package org.csi.yucca.dataservice.metadataapi.model.dcat;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DCatResult{

	@SerializedName("@context")
	private String context;
	@SerializedName("@graph")
	private List<DCatObject> items;

	public DCatResult() {
		super();
		this.setContext("https://raw.githubusercontent.com/insideout10/open_data_dcat_ap/develop/data/v1.01/context.it.jsonld");
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public List<DCatObject> getItems() {
		return items;
	}

	public void setItems(List<DCatObject> items) {
		this.items = items;
	}

	public void addItem(DCatObject item) {
		if (items == null)
			items = new LinkedList<DCatObject>();
		items.add(item);
	}

}
