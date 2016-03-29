package org.csi.yucca.dataservice.metadataapi.model.output;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Stream {
	private Smartobject smartobject;
	private Integer fps;
	private Boolean savedata;
	private StreamComponent[] components;

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

	public Integer getFps() {
		return fps;
	}

	public void setFps(Integer fps) {
		this.fps = fps;
	}

	public Boolean getSavedata() {
		return savedata;
	}

	public void setSavedata(Boolean savedata) {
		this.savedata = savedata;
	}

	public StreamComponent[] getComponents() {
		return components;
	}

	public void setComponents(StreamComponent[] components) {
		this.components = components;
	}

}
