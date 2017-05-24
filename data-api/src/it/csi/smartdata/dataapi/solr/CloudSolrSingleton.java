package it.csi.smartdata.dataapi.solr;

import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpClientUtil;

public class CloudSolrSingleton {
	//private CloudSolrClient server;
	private SolrClient server;
	
	private CloudSolrSingleton() {
		try {
//			System.out.println("------------------>>>> PRIMAAAAAAAAAAAAAAAAAA " + System.getProperty("java.security.auth.login.config"));
//			
//			System.setProperty("java.security.auth.login.config", "/appserv/jboss/ajb620/part001node01/standalone/configuration/jaas-client.conf");			
//			System.setProperty("solr.kerberos.jaas.appname", "Client");			
//			HttpClientUtil.setConfigurer( new Krb5HttpClientConfigurer());
//
//			System.out.println("------------------>>>> DOPOOOOOOOOOOOOOOOO java.security.auth.login.config --- " + System.getProperty("java.security.auth.login.config"));
//			System.out.println("------------------>>>> DOPOOOOOOOOOOOOOOOO solr.kerberos.jaas.appname ---" + System.getProperty("solr.kerberos.jaas.appname"));
//			System.out.println("------------------>>>> DOPOOOOOOOOOOOOOOOO javax.security.auth.useSubjectCredsOnly ---" + System.getProperty("javax.security.auth.useSubjectCredsOnly"));
//						
//			
//			
//			System.out.println("------------------>>>> PRIMAAAAAAAAAAAAAAAAAA " + System.getProperty("java.security.auth.login.config"));
//			
//			System.setProperty("java.security.auth.login.config", "/appserv/jboss/ajb620/part001node01/standalone/configuration/jaas-client.conf");			
//			System.setProperty("solr.kerberos.jaas.appname", "Client");			
//			HttpClientUtil.setConfigurer( new Krb5HttpClientConfigurer());
//
//			System.out.println("------------------>>>> DOPOOOOOOOOOOOOOOOO java.security.auth.login.config --- " + System.getProperty("java.security.auth.login.config"));
//			System.out.println("------------------>>>> DOPOOOOOOOOOOOOOOOO solr.kerberos.jaas.appname ---" + System.getProperty("solr.kerberos.jaas.appname"));
//			System.out.println("------------------>>>> DOPOOOOOOOOOOOOOOOO javax.security.auth.useSubjectCredsOnly ---" + System.getProperty("javax.security.auth.useSubjectCredsOnly"));
						
			HttpClientUtil.setConfigurer( new org.apache.solr.client.solrj.impl.Krb5HttpClientConfigurer());
			
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
