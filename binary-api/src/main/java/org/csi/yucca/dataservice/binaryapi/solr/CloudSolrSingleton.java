package org.csi.yucca.dataservice.binaryapi.solr;

import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.csi.yucca.dataservice.binaryapi.util.SDPInsertApiConfig;

public class CloudSolrSingleton {

	private CloudSolrClient server;
	
	private CloudSolrSingleton() {
		server = new CloudSolrClient(SDPInsertApiConfig.getInstance().getSolrUrl());
    }
	
		  /**
		   * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
		   * or the first access to SingletonHolder.INSTANCE, not before.
		   */
	  private static class SingletonHolder { 
	    private static final CloudSolrSingleton INSTANCE = new CloudSolrSingleton();
	  }

	  public static CloudSolrClient getServer() {
	    return SingletonHolder.INSTANCE.server;
	  }

}
