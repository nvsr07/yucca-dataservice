package it.csi.smartdata.dataapi.solr;

import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;

public class CloudSolrSingleton {
	//private CloudSolrClient server;
	private SolrClient server;
	
	private CloudSolrSingleton() {
		try {
			
			
			server = new CloudSolrClient(SDPDataApiConfig.getInstance().getSolrUrl());
		} catch (Exception e) {
			
		}
    }
	
		  /**
		   * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
		   * or the first access to SingletonHolder.INSTANCE, not before.
		   */
	  private static class SingletonHolder { 
	    private static final CloudSolrSingleton INSTANCE = new CloudSolrSingleton();
	  }

	  public static SolrClient getServer() {
	    return SingletonHolder.INSTANCE.server;
	  }

}