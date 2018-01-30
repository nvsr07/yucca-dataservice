package org.csi.yucca.dataservice.metadataapi.model.dcat;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DCatResult{

	@SerializedName("@context")
	private DCatContext context;
	@SerializedName("@graph")
	private List<DCatObject> items;

	public DCatResult() {
		super();
		setContext(new DCatContext());
	}

	public DCatContext getContext() {
		return context;
	}

	public void setContext(DCatContext context) {
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
