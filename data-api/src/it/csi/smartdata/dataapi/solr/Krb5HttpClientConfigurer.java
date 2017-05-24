/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package it.csi.smartdata.dataapi.solr;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.solr.client.solrj.impl.HttpClientConfigurer;
import org.apache.solr.common.params.SolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Krb5HttpClientConfigurer extends HttpClientConfigurer {
	public static final String LOGIN_CONFIG_PROP = "java.security.auth.login.config";
	private static final Logger logger = LoggerFactory
			.getLogger(Krb5HttpClientConfigurer.class);

	private static final Configuration jaasConfig = new SolrJaasConfiguration();
	private HttpRequestInterceptor bufferedEntityInterceptor;

	public Krb5HttpClientConfigurer() {
		this.bufferedEntityInterceptor = new HttpRequestInterceptor() {
			public void process(HttpRequest request, HttpContext context)
					throws HttpException, IOException {
				if (request instanceof HttpEntityEnclosingRequest) {
					HttpEntityEnclosingRequest enclosingRequest = (HttpEntityEnclosingRequest) request;
					HttpEntity requestEntity = enclosingRequest.getEntity();
					enclosingRequest.setEntity(new BufferedHttpEntity(
							requestEntity));
				}
			}
		};
	}

	public void configure(DefaultHttpClient httpClient, SolrParams config) {
		super.configure(httpClient, config);

		if (System.getProperty("java.security.auth.login.config") != null) {
			String configValue = System
					.getProperty("java.security.auth.login.config");

			if (configValue != null) {
				logger.info("Setting up SPNego auth with config: "
						+ configValue);
				String useSubjectCredsProp = "javax.security.auth.useSubjectCredsOnly";
				String useSubjectCredsVal = System
						.getProperty("javax.security.auth.useSubjectCredsOnly");

				if (useSubjectCredsVal == null) {
					System.setProperty(
							"javax.security.auth.useSubjectCredsOnly", "false");
				} else if (!(useSubjectCredsVal.toLowerCase(Locale.ROOT)
						.equals("false"))) {
					logger.warn("System Property: javax.security.auth.useSubjectCredsOnly set to: "
							+ useSubjectCredsVal
							+ " not false.  SPNego authentication may not be successful.");
				}

				Configuration.setConfiguration(jaasConfig);
				httpClient.getAuthSchemes().register("Negotiate",
						new SPNegoSchemeFactory(true, false));

				Credentials useJaasCreds = new Credentials() {
					public String getPassword() {
						return null;
					}

					public Principal getUserPrincipal() {
						return null;
					}
				};
				httpClient.getCredentialsProvider().setCredentials(
						AuthScope.ANY, useJaasCreds);

				httpClient
						.addRequestInterceptor(this.bufferedEntityInterceptor);
			} else {
				httpClient.getCredentialsProvider().clear();
			}
		}
	}

	private static class SolrJaasConfiguration extends Configuration {
		private Configuration baseConfig;
		private Set<String> initiateAppNames = new HashSet(
				Arrays.asList(new String[] {
						"com.sun.security.jgss.krb5.initiate",
						"com.sun.security.jgss.initiate" }));

		public SolrJaasConfiguration() {
			try {
				this.baseConfig = Configuration.getConfiguration();
			} catch (SecurityException e) {
				this.baseConfig = null;
			}
		}

		public AppConfigurationEntry[] getAppConfigurationEntry(String appName) {
			if (this.baseConfig == null)
				return null;

			Krb5HttpClientConfigurer.logger.debug("Login prop: "
					+ System.getProperty("java.security.auth.login.config"));

			
			Krb5HttpClientConfigurer.logger
			.debug("BEGIN getAppConfigurationEntry invoked with appName: '"+appName );
			
			
			String clientAppName = System.getProperty(
					"solr.kerberos.jaas.appname", "Client");
			if (true || this.initiateAppNames.contains(appName)) {
				Krb5HttpClientConfigurer.logger
						.debug("Using AppConfigurationEntry for appName '"
								+ clientAppName + "' instead of: " + appName);
				
				Krb5HttpClientConfigurer.logger
				.debug("Using AppConfigurationEntry for appName '"
						+ clientAppName + "' instead of: " + appName);
				
				AppConfigurationEntry [] aa= this.baseConfig.getAppConfigurationEntry(clientAppName);
				for (int kk=0;aa!=null && kk<aa.length; kk++) {
					Krb5HttpClientConfigurer.logger.debug(" ---------- clientAppName " + clientAppName + "    " +aa[kk].toString() );
				}
				return aa;
				//return this.baseConfig.getAppConfigurationEntry(clientAppName);
			}
			return this.baseConfig.getAppConfigurationEntry(appName);
		}
	}
}
