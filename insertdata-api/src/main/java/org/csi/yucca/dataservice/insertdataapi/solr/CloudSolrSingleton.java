package org.csi.yucca.dataservice.insertdataapi.solr;

import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public enum CloudSolrSingleton {

	INSTANCE;
	public CloudSolrClient getClient() {
        return new CloudSolrClient(SDPInsertApiConfig.getInstance().getSolrUrl());
	}
	
}
