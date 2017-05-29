package org.csi.yucca.dataservice.metadataapi.solr;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.csi.yucca.dataservice.metadataapi.util.Config;
public class CloudSolrSingleton {
	private SolrClient server;
	
	private CloudSolrSingleton() {
		HttpClientUtil.setConfigurer( new  Krb5HttpClientConfigurer(Config.getInstance().getSolrSecurityDomainName()));

		server = new CloudSolrClient(Config.getInstance().getSearchEngineBaseUrl());
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
