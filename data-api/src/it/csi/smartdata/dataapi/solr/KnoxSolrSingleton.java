package it.csi.smartdata.dataapi.solr;
import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
public class KnoxSolrSingleton {
private SolrClient server;
	
	private KnoxSolrSingleton() {
		
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		if (SDPDataApiConfig.instance.getSolrUsername()!=null)
		{
			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
					SDPDataApiConfig.instance.getSolrUsername(), SDPDataApiConfig.instance.getSolrPassword());
			
			provider.setCredentials(new AuthScope(AuthScope.ANY), credentials);
			
			clientBuilder.setDefaultCredentialsProvider(provider);
		}
		clientBuilder.setMaxConnTotal(128);
				
		
		try {
		server = new HttpSolrClient(SDPDataApiConfig.getInstance().getSolrUrl(),
				clientBuilder.build());
		} catch (Exception e) {
			//TODO log
			e.printStackTrace();
		}
		
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
