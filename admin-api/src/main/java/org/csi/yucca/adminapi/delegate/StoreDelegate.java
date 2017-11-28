package org.csi.yucca.adminapi.delegate;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.store.response.GeneralResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.fasterxml.jackson.databind.ObjectMapper;

@PropertySource(value = { "classpath:datasource.properties" })
public class StoreDelegate {

	private static final Logger logger = Logger.getLogger(StoreDelegate.class);

	private static StoreDelegate storeDelegate;

	@Value("${store.url}")
	private String storeUrl;

	public static StoreDelegate build() {
		if (storeDelegate == null)
			storeDelegate = new StoreDelegate();
		return storeDelegate;
	}

	public CloseableHttpClient registerToStoreInit(String username, String password) throws Exception {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		// login
		loginOnStore(httpclient, username, password);

		return httpclient;
	}

	private String loginOnStore(CloseableHttpClient httpclient, String username, String password) throws HttpException, IOException {
		logger.info("[StoreDelegate::loginOnStore] username " + username + " - store url " + storeUrl);
		System.out.println("[StoreDelegate::loginOnStore] username " + username + " - store url " + storeUrl);

		List<NameValuePair> loginParams = new LinkedList<NameValuePair>();
		loginParams.add(new BasicNameValuePair("action", "login"));
		loginParams.add(new BasicNameValuePair("username", username));
		loginParams.add(new BasicNameValuePair("password", password));

		String url = storeUrl + "site/blocks/user/login/ajax/login.jag";
		String response = HttpDelegate.makeHttpPost(httpclient, url, loginParams);
		logger.debug("[StoreDelegate::loginOnStore] response " + response);
		System.out.println("[StoreDelegate::loginOnStore] response " + response);

		return response;

	}

	public String addApplication(CloseableHttpClient httpclient, String applicationCode) throws HttpException, IOException {
		logger.debug("[StoreDelegate::addApplication] applicationCode " + applicationCode);
		List<NameValuePair> addApplicationParams = new LinkedList<NameValuePair>();
		addApplicationParams.add(new BasicNameValuePair("action", "addApplication"));
		addApplicationParams.add(new BasicNameValuePair("application", applicationCode));
		addApplicationParams.add(new BasicNameValuePair("tier", "Unlimited"));
		addApplicationParams.add(new BasicNameValuePair("description", ""));
		addApplicationParams.add(new BasicNameValuePair("callbackUrl", ""));

		String url = storeUrl + "site/blocks/application/application-add/ajax/application-add.jag";
		String response = HttpDelegate.makeHttpPost(httpclient, url, addApplicationParams);
		logger.debug("[StoreDelegate::addApplication] response " + response);
		return response;

	}
	
	public GeneralResponse generateKey(CloseableHttpClient httpclient, String application) throws Exception {
		logger.debug("[StoreDelegate::generetateKey] application: " + application);
		List<NameValuePair> generetateKeyParams = new LinkedList<NameValuePair>();
		generetateKeyParams.add(new BasicNameValuePair("action", "generateApplicationKey"));
		generetateKeyParams.add(new BasicNameValuePair("name", ""));
		generetateKeyParams.add(new BasicNameValuePair("version", ""));
		generetateKeyParams.add(new BasicNameValuePair("tier", ""));
		generetateKeyParams.add(new BasicNameValuePair("applicationName", ""));
		generetateKeyParams.add(new BasicNameValuePair("application", application));
		generetateKeyParams.add(new BasicNameValuePair("provider", "admin"));
		generetateKeyParams.add(new BasicNameValuePair("keytype", "PRODUCTION"));
		generetateKeyParams.add(new BasicNameValuePair("callbackUrl", ""));
		generetateKeyParams.add(new BasicNameValuePair("authorizedDomains", "ALL"));
		generetateKeyParams.add(new BasicNameValuePair("validityTime", "999999999"));

		GeneralResponse generalResponse = null;
		String url = storeUrl + "site/blocks/subscription/subscription-add/ajax/subscription-add.jag";
		String response = HttpDelegate.makeHttpPost(httpclient, url, generetateKeyParams);
		ObjectMapper mapper = new ObjectMapper();
		if (response != null)
			generalResponse = mapper.readValue(response, GeneralResponse.class);
		if (generalResponse.getError())
			throw new Exception("Add Application for " + application + " failed: " + generalResponse.getMessage());
		if (generalResponse.getData() == null || generalResponse.getData().getKey() == null || generalResponse.getData().getKey().getConsumerKey() == null
				|| generalResponse.getData().getKey().getConsumerSecret() == null)
			throw new Exception("Add Application for " + application + " failed: Invalid consumerKey and consumerSecret - " + generalResponse.getMessage());

		return generalResponse;
	}
	
	public String subscribeApi(CloseableHttpClient httpclient,String apiName,  String appName) throws Exception {
		logger.debug("[StoreDelegate::subscribeApi] appName: " + appName);
		List<NameValuePair> subscribeAdminApiParams = new LinkedList<NameValuePair>();
		subscribeAdminApiParams.add(new BasicNameValuePair("action", "addAPISubscription"));
		subscribeAdminApiParams.add(new BasicNameValuePair("name",apiName));
		subscribeAdminApiParams.add(new BasicNameValuePair("version", "1.0"));
		subscribeAdminApiParams.add(new BasicNameValuePair("tier", "Unlimited"));
		subscribeAdminApiParams.add(new BasicNameValuePair("applicationName", appName));
		subscribeAdminApiParams.add(new BasicNameValuePair("application", ""));
		subscribeAdminApiParams.add(new BasicNameValuePair("provider", "admin"));
		subscribeAdminApiParams.add(new BasicNameValuePair("keytype", ""));
		subscribeAdminApiParams.add(new BasicNameValuePair("callbackUrl", ""));
		subscribeAdminApiParams.add(new BasicNameValuePair("authorizedDomains", ""));
		subscribeAdminApiParams.add(new BasicNameValuePair("validityTime", ""));

		String url = storeUrl + "site/blocks/subscription/subscription-add/ajax/subscription-add.jag";
		String response = HttpDelegate.makeHttpPost(httpclient, url, subscribeAdminApiParams);
		logger.debug("[StoreDelegate::subscribeApi] response " + response);
		return response;

	}
	
	public String logoutFromStore(CloseableHttpClient httpclient, String username, String password) throws Exception {
		logger.debug("[StoreDelegate::logoutFromStore] username " + username);

		List<NameValuePair> logoutParams = new LinkedList<NameValuePair>();
		logoutParams.add(new BasicNameValuePair("action", "logout"));
		logoutParams.add(new BasicNameValuePair("username", username));
		logoutParams.add(new BasicNameValuePair("password", password));

		String url = storeUrl + "site/blocks/user/login/ajax/login.jag";
		String response = HttpDelegate.makeHttpPost(httpclient, url, logoutParams);
		logger.debug("[StoreDelegate::loginOnStore] response " + response);
		return response;
	}

}
