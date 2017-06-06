package org.csi.yucca.dataservice.insertdataapi.solr;


import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public class CloudSolrSingleton {

	private SolrClient server;

	private CloudSolrSingleton() {
		
		if (SDPInsertApiConfig.getInstance().getSolrSecurityDomainName() != null && SDPInsertApiConfig.getInstance().getSolrSecurityDomainName().trim().length()>0 && 
				!(SDPInsertApiConfig.getInstance().getSolrSecurityDomainName().equals("NO_SECURITY"))) {
		HttpClientUtil.setConfigurer( new  Krb5HttpClientConfigurer(SDPInsertApiConfig.getInstance().getSolrSecurityDomainName()));
		
		}

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
