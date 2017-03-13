package org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata;

import org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.stream.Twitter;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Stream {
	private String code;
	private Smartobject smartobject;
	private Double fps;
	private Boolean savedata;
	private Twitter twitter;

	public Stream() {
		super();
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public Smartobject getSmartobject() {
		return smartobject;
	}

	public void setSmartobject(Smartobject smartobject) {
		this.smartobject = smartobject;
	}

	public Double getFps() {
		return fps;
	}

	public void setFps(Double fps) {
		this.fps = fps;
	}

	public Boolean getSavedata() {
		return savedata;
	}

	public void setSavedata(Boolean savedata) {
		this.savedata = savedata;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Twitter getTwitter() {
		return twitter;
	}

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}

}
