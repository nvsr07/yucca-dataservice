package org.csi.yucca.dataservice.insertdataapi.solr;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public class KnoxSolrSingleton{

	
	private SolrClient server;
	
	private KnoxSolrSingleton() {
		
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		if (SDPInsertApiConfig.instance.getSolrUsername()!=null)
		{
			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
					SDPInsertApiConfig.instance.getSolrUsername(), SDPInsertApiConfig.instance.getSolrPassword());
			clientBuilder.setDefaultCredentialsProvider(provider);
		}
		clientBuilder.setMaxConnTotal(128);
				
		server = new HttpSolrClient(SDPInsertApiConfig.getInstance().getSolrUrl(),
				clientBuilder.build());
		
		
    }
	
		  /**
		   * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
		   * or the first access to SingletonHolder.INSTANCE, not before.
		   */
	  static class SingletonHolder { 
	    static final KnoxSolrSingleton INSTANCE = new KnoxSolrSingleton();
	  }

	  public static SolrClient getServer() {
		    return SingletonHolder.INSTANCE.server;
		  }
	  
}
