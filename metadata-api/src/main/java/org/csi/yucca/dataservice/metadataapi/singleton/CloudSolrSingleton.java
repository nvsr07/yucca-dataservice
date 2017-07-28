package org.csi.yucca.dataservice.metadataapi.singleton;




import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.csi.yucca.dataservice.metadataapi.util.Config;

public class CloudSolrSingleton {
	private CloudSolrClient server;
	
	private CloudSolrSingleton() {
		try {
			//HttpClientUtil.setConfigurer( new  Krb5HttpClientConfigurer("KERBEROS-POCHDP"));
//			HttpClientUtil.setConfigurer( new  Krb5HttpClientConfigurer(Config.getInstance().getSolrSecurityDomainName()));

			
			if (Config.getInstance().getSolrSecurityDomainName() != null && Config.getInstance().getSolrSecurityDomainName().trim().length()>0 && 
					!(Config.getInstance().getSolrSecurityDomainName().equals("NO_SECURITY"))) {
				HttpClientUtil.setConfigurer( new  Krb5HttpClientConfigurer(Config.getInstance().getSolrSecurityDomainName()));
			}
			
			
		server = new CloudSolrClient(Config.getInstance().getSearchEngineBaseUrl());
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

	  public static CloudSolrClient getServer() {
	    return SingletonHolder.INSTANCE.server;
	  }

}
