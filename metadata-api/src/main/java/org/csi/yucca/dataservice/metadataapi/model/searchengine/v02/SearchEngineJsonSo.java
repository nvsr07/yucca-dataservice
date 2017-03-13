package org.csi.yucca.dataservice.metadataapi.model.searchengine.v02;

import java.util.List;

public class SearchEngineJsonSo {
	private List<SearchEngineJsonSoPosition> position;

	public SearchEngineJsonSo() {
		super();
	}

	public List<SearchEngineJsonSoPosition> getPosition() {
		return position;
	}

	public void setPositions(List<SearchEngineJsonSoPosition> position) {
		this.position = position;
	}

}
