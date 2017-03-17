package org.csi.yucca.dataservice.metadataapi.service.response;

import java.util.List;

import org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class ListResponse extends AbstractResponse {

	private String message;
	private List<Metadata> result;
	private Integer count;

	public ListResponse() {
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Metadata> getResult() {
		return result;
	}

	public void setResult(List<Metadata> result) {
		this.result = result;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
