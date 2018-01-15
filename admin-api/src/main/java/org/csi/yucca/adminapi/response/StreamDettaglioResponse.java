package org.csi.yucca.adminapi.response;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.InternalDettaglioStream;
import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;

public class StreamDettaglioResponse {

	private Long usedInInternalCount;
	private Long streamsCountBySO;
	private String internalquery;
	private TwitterInfoResponse twitterInfo;
	private List<StreamDettaglioResponse> internalStreams = new ArrayList<StreamDettaglioResponse>();
	private DettaglioSmartobjectResponse smartobject;
	private Double fps;
	private Integer idstream;
	private String streamcode;
	private String streamname;
	private String streamalias;
	private Integer savedata;
	
	public StreamDettaglioResponse() {
		super();
	}

	private void addInternalStreams(List<InternalDettaglioStream> listInternalStream)throws Exception{
		for (InternalDettaglioStream dettaglioStream : listInternalStream) {
			internalStreams.add(new StreamDettaglioResponse(dettaglioStream));
		}
	}
	
	public StreamDettaglioResponse(InternalDettaglioStream dettaglioStream)throws Exception{
		this.setIdstream(dettaglioStream.getIdstream());
		this.setStreamcode(dettaglioStream.getStreamcode());
		this.setStreamname(dettaglioStream.getStreamname());
		this.setStreamalias(dettaglioStream.getAliasName());
		this.setSmartobject(new DettaglioSmartobjectResponse(dettaglioStream));
	}
	
	
	public StreamDettaglioResponse( DettaglioStream dettaglioStream, DettaglioSmartobject dettaglioSmartobject, 
			List<InternalDettaglioStream> listInternalStream ) throws Exception{
		super();
		this.usedInInternalCount = dettaglioStream.getUsedInInternalCount();
		this.streamsCountBySO = dettaglioStream.getStreamsCountBySO();
		this.internalquery = dettaglioStream.getInternalquery();
		this.twitterInfo = new TwitterInfoResponse(dettaglioStream,dettaglioSmartobject);
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

	public List<StreamDettaglioResponse> getInternalStreams() {
		return internalStreams;
	}

	public void setInternalStreams(List<StreamDettaglioResponse> internalStreams) {
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

	public String getStreamalias() {
		return streamalias;
	}

	public void setStreamalias(String streamalias) {
		this.streamalias = streamalias;
	}
	
	
}
