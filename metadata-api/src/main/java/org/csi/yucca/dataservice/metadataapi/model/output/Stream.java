package org.csi.yucca.dataservice.metadataapi.model.output;

import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Stream {
	private String code;  // campo stream dello stream
	private String name;  // campo name dello stream
 	private Smartobject smartobject;
	private Double  fps;  // FIXME dallo store torna double, va bene?
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

	public StreamComponent[] getComponents() {
		return components;
	}

	public void setComponents(StreamComponent[] components) {
		this.components = components;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
