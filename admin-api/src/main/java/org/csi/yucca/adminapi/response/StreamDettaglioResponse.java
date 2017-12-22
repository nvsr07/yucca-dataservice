package org.csi.yucca.adminapi.response;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;

public class StreamDettaglioResponse {

	private Long usedInInternalCount;
	private Long streamsCountBySO;
	private String internalquery;
	private TwitterInfoResponse twitterInfo;
	private List<DettaglioStreamResponse> internalStreams = new ArrayList<DettaglioStreamResponse>();
	private DettaglioSmartobjectResponse smartobject;
	private Double fps;
	private Integer idstream;
	private String streamcode;
	private String streamname;
	private Integer savedata;
	
	public StreamDettaglioResponse() {
		super();
	}

	private void addInternalStreams(List<DettaglioStream> listInternalStream)throws Exception{
		for (DettaglioStream dettaglioStream : listInternalStream) {
			internalStreams.add(new DettaglioStreamResponse(dettaglioStream));
		}
	}
	
	public StreamDettaglioResponse( DettaglioStream dettaglioStream, DettaglioSmartobject dettaglioSmartobject, 
			List<DettaglioStream> listInternalStream ) throws Exception{
		super();
		this.usedInInternalCount = dettaglioStream.getUsedInInternalCount();
		this.streamsCountBySO = dettaglioStream.getStreamsCountBySO();
		this.internalquery = dettaglioStream.getInternalquery();
		this.twitterInfo = new TwitterInfoResponse(dettaglioStream);
		addInternalStreams(listInternalStream);
		this.smartobject = new DettaglioSmartobjectResponse(dettaglioSmartobject);
		this.fps = dettaglioStream.getFps();
		this.idstream = dettaglioStream.getIdstream();
		this.streamcode = dettaglioStream.getStreamcode();
		this.streamname = dettaglioStream.getStreamname();
		this.savedata = dettaglioStream.getSavedata();
		
	}

	public Long getUsedInInternalCount() {
		return usedInInternalCount;
	}

	public void setUsedInInternalCount(Long usedInInternalCount) {
		this.usedInInternalCount = usedInInternalCount;
	}

	public Long getStreamsCountBySO() {
		return streamsCountBySO;
	}

	public void setStreamsCountBySO(Long streamsCountBySO) {
		this.streamsCountBySO = streamsCountBySO;
	}

	public String getInternalquery() {
		return internalquery;
	}

	public void setInternalquery(String internalquery) {
		this.internalquery = internalquery;
	}

	public TwitterInfoResponse getTwitterInfo() {
		return twitterInfo;
	}

	public void setTwitterInfo(TwitterInfoResponse twitterInfo) {
		this.twitterInfo = twitterInfo;
	}

	public List<DettaglioStreamResponse> getInternalStreams() {
		return internalStreams;
	}

	public void setInternalStreams(List<DettaglioStreamResponse> internalStreams) {
		this.internalStreams = internalStreams;
	}

	public DettaglioSmartobjectResponse getSmartobject() {
		return smartobject;
	}

	public void setSmartobject(DettaglioSmartobjectResponse smartobject) {
		this.smartobject = smartobject;
	}

	public Double getFps() {
		return fps;
	}

	public void setFps(Double fps) {
		this.fps = fps;
	}

	public Integer getIdstream() {
		return idstream;
	}

	public void setIdstream(Integer idstream) {
		this.idstream = idstream;
	}

	public String getStreamcode() {
		return streamcode;
	}

	public void setStreamcode(String streamcode) {
		this.streamcode = streamcode;
	}

	public String getStreamname() {
		return streamname;
	}

	public void setStreamname(String streamname) {
		this.streamname = streamname;
	}

	public Integer getSavedata() {
		return savedata;
	}

	public void setSavedata(Integer savedata) {
		this.savedata = savedata;
	}
}
