package org.csi.yucca.dataservice.ingest.model.metadata;

import org.csi.yucca.dataservice.ingest.util.json.JSonHelper;

import com.google.gson.Gson;

public class Stream extends AbstractEntity {

 	private String id;
	private Long idStream; 
	private String streamCode;
	private String streamName;
	
	private StreamConfigData configData;
	private Streams streams;

	public StreamConfigData getConfigData() {
		return configData;
	}

	public void setConfigData(StreamConfigData configData) {
		this.configData = configData;
	}

	public Streams getStreams() {
		return streams;
	}

	public void setStreams(Streams streams) {
		this.streams = streams;
	}

	public static Stream fromJson(String json) {
		Gson gson = JSonHelper.getInstance();
		return gson.fromJson(json, Stream.class);
	}

	public Stream() {
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getIdStream() {
		return idStream;
	}

	public void setIdStream(Long idStream) {
		this.idStream = idStream;
	}

	public String getStreamCode() {
		return streamCode;
	}

	public void setStreamCode(String streamCode) {
		this.streamCode = streamCode;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}
}
