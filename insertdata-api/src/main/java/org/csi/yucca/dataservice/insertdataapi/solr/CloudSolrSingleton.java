package org.csi.yucca.dataservice.insertdataapi.solr;


import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public class CloudSolrSingleton {

	private SolrClient server;
	
	private CloudSolrSingleton() {
		HttpClientUtil.setConfigurer( new  Krb5HttpClientConfigurer("KERBEROS-POCHDP"));

		server = new CloudSolrClient(SDPInsertApiConfig.getInstance().getSolrUrl());
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
