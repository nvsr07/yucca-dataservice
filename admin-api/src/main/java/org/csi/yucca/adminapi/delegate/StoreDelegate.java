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
import org.csi.yucca.adminapi.store.response.StoreResponse;
import org.csi.yucca.adminapi.store.response.SubscriptionByUsernameResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@PropertySource(value = { "classpath:adminapi.properties" })
public class StoreDelegate {

	private static final Logger logger = Logger.getLogger(StoreDelegate.class);

	private static StoreDelegate storeDelegate;

	
	@Value("${store.url}")
	private String storeUrl;
	private ObjectMapper mapper = new ObjectMapper();
	
	

	public StoreDelegate() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		logger.info("[StoreDelegate::StoreDelegate]  store url " + storeUrl);
	}

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

		List<NameValuePair> loginParams = new LinkedList<NameValuePair>();
		loginParams.add(new BasicNameValuePair("action", "login"));
		loginParams.add(new BasicNameValuePair("username", username));
		loginParams.add(new BasicNameValuePair("password", password));

		String url = storeUrl + "site/blocks/user/login/ajax/login.jag";
		String response = HttpDelegate.makeHttpPost(httpclient, url, loginParams);
		logger.debug("[StoreDelegate::loginOnStore] response " + response);

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

		//String url = storeUrl + "site/blocks/subscription/subscription-add/ajax/subscription-add.jag";
		String url = storeUrl + "site/blocks/secure/subscription.jag"; 
		String response = HttpDelegate.makeHttpPost(httpclient, url, subscribeAdminApiParams);
		logger.debug("[StoreDelegate::subscribeApi] response " + response);
		return response;
	}
	
	public String subscribeApiWithUsername(CloseableHttpClient httpclient,String apiName,  String appName, String username) throws Exception {
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
		subscribeAdminApiParams.add(new BasicNameValuePair("username", username));

		//String url = storeUrl + "site/blocks/subscription/subscription-add/ajax/subscription-add.jag";
		String url = storeUrl + "site/blocks/secure/subscription.jag"; 
		String response = HttpDelegate.makeHttpPost(httpclient, url, subscribeAdminApiParams);
		logger.debug("[StoreDelegate::subscribeApi] response " + response);
		return response;
	}
	
	public  String unSubscribeApiWithUsername(CloseableHttpClient httpclient,String apiName,  String appName, int applicationId, String username) throws Exception {

		logger.debug("[StoreDelegate::unsubscribeApi] appName: " + appName);
		List<NameValuePair> subscribeAdminApiParams = new LinkedList<NameValuePair>();
		
		subscribeAdminApiParams.add(new BasicNameValuePair("action", "removeSubscription"));
		subscribeAdminApiParams.add(new BasicNameValuePair("apiname", apiName));
		subscribeAdminApiParams.add(new BasicNameValuePair("apiversion", "1.0"));
		subscribeAdminApiParams.add(new BasicNameValuePair("provider", "admin"));
		subscribeAdminApiParams.add(new BasicNameValuePair("username", username));
		subscribeAdminApiParams.add(new BasicNameValuePair("applicationId", Integer.toString(applicationId)));
		
		String url = storeUrl + "site/blocks/secure/subscription.jag";  
		String response = HttpDelegate.makeHttpPost(httpclient, url, subscribeAdminApiParams);
		logger.debug("[StoreDelegate::subscribeApi] response " + response);
		return response;
}
	
public  SubscriptionByUsernameResponse listSubscriptionByApiAndUserName(CloseableHttpClient httpclient, String apiname, String username) throws Exception {
		
		logger.debug("[StoreDelegate::listSubscription]"); 
		List<NameValuePair> subscribeAdminApiParams = new LinkedList<NameValuePair>();
		subscribeAdminApiParams.add(new BasicNameValuePair("action", "getAPISubscriptions"));
		subscribeAdminApiParams.add(new BasicNameValuePair("apiversion", "1.0"));
		subscribeAdminApiParams.add(new BasicNameValuePair("provider", "admin"));
		subscribeAdminApiParams.add(new BasicNameValuePair("apiname", apiname));
		subscribeAdminApiParams.add(new BasicNameValuePair("username", username));
		
		String url = storeUrl + "site/blocks/secure/subscription.jag";
		String response = HttpDelegate.makeHttpGet(httpclient, url, subscribeAdminApiParams);

	
		SubscriptionByUsernameResponse mySubscriptionResponse = mapper.readValue(response, SubscriptionByUsernameResponse.class);
		return mySubscriptionResponse;
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

//	public String createApiForBulk(Dataset dataset) {
//		logger.debug("[StoreDelegate::createApiForBulk] appName: " + appName);
//		String apiFinalName=null; 
//		try {
//			String apiName = dataset.getDatasetcode();
//			apiFinalName = dataset.getDatasecode() + "_odata";
//
//		AddStream addStream = new AddStream();
//		addStream.setProperties(update);
//
//		// FIXME get the list of roles(tenants) from the stream info
//		if ("public".equals(metadata.getInfo().getVisibility())) {
//			addStream.setVar("visibility", "public");
//			addStream.setVar("roles", "");
//			addStream.setVar("authType", "None");
//		} else {
//			addStream.setVar("visibility", "restricted");
//
//			String ruoli = "";
//
//			if (metadata.getInfo().getTenantssharing() != null && metadata.getInfo().getTenantssharing().getTenantsharing() != null) {
//				for (org.csi.yucca.storage.datamanagementapi.model.metadata.Tenantsharing t : metadata.getInfo().getTenantssharing().getTenantsharing()) {
//					if (!ruoli.equals(""))
//						ruoli += ",";
//					ruoli += t.getTenantCode() + "_subscriber";
//				}
//			}
//
//			if (!ruoli.contains(metadata.getConfigData().getTenantCode() + "_subscriber")) {
//				ruoli += metadata.getConfigData().getTenantCode() + "_subscriber";
//			}
//
//			addStream.setVar("roles", ruoli);
//			addStream.setVar("authType", "Application & Application User");
//		}
//
//		if (update) {
//			addStream.setVar("actionAPI", "updateAPI");
//		} else {
//			addStream.setVar("actionAPI", "addAPI");
//		}
//
//		addStream.setVar("apimanConsoleAddress", Config.getInstance().getConsoleAddress());
//		addStream.setVar("username", Config.getInstance().getStoreUsername());
//		addStream.setVar("password", Config.getInstance().getStorePassword());
//		addStream.setVar("httpok", Config.getInstance().getHttpOk());
//		addStream.setVar("ok", Config.getInstance().getResponseOk());
//
//		// addStream.setVar("icon", path + fileName);
//		addStream.setVar("apiVersion", "1.0");
//		addStream.setVar("apiName", apiFinalName);
//		addStream.setVar("context", "/api/" + apiName);// ds_Voc_28;
//		addStream.setVar("P", "");
//		addStream.setVar("endpoint", Config.getInstance().getBaseApiUrl() + apiName);
//		addStream.setVar("desc", metadata.getInfo().getDescription() != null ? Util.safeSubstring(metadata.getInfo().getDescription(), API_FIELD_MAX_LENGTH) : "");
//		addStream.setVar("copiright", metadata.getInfo().getCopyright() != null ? Util.safeSubstring(metadata.getInfo().getCopyright(), API_FIELD_MAX_LENGTH) : "");
//
//		addStream.setVar("extra_isApi", "false");
//		addStream.setVar("extra_apiDescription", metadata.getInfo().getDatasetName() != null ? metadata.getInfo().getDatasetName() : "");
//		addStream.setVar("codiceTenant", metadata.getConfigData().getTenantCode() != null ? metadata.getConfigData().getTenantCode() : "");
//		addStream.setVar("codiceStream", "");
//		addStream.setVar("nomeStream", "");
//		addStream.setVar("nomeTenant", metadata.getConfigData().getTenantCode() != null ? metadata.getConfigData().getTenantCode() : "");
//		addStream.setVar("licence", metadata.getInfo().getLicense() != null ? Util.safeSubstring(metadata.getInfo().getLicense(), API_FIELD_MAX_LENGTH) : "");
//		addStream.setVar("disclaimer", metadata.getInfo().getDisclaimer() != null ? Util.safeSubstring(metadata.getInfo().getDisclaimer(), API_FIELD_MAX_LENGTH) : "");
//		addStream.setVar("virtualEntityName", "");
//		addStream.setVar("virtualEntityDescription", "");
//
//		String tags = "";
//
//		if (metadata.getInfo().getDataDomain() != null) {
//			tags += metadata.getInfo().getDataDomain();
//		}
//		List<String> tagCodes = null;
//		if (metadata.getInfo().getTags() != null) {
//			tagCodes = new LinkedList<String>();
//			for (org.csi.yucca.storage.datamanagementapi.model.metadata.Tag t : metadata.getInfo().getTags()) {
//				tags += "," + t.getTagCode();
//				tagCodes.add(t.getTagCode());
//			}
//		}
//
//		addStream.setVar("tags", Util.safeSubstring(tags, API_FIELD_MAX_LENGTH));
//
//		// DT Add document ? Why restart from jsonFile? we lost init
//		//String contentJson = extractMetadataContentForDocument(jsonFile,metadata.getConfigData().getTenantCode() != null ? metadata.getConfigData().getTenantCode() : "");
//		String contentJson = extractMetadataContentForDocument(metadata,metadata.getConfigData().getTenantCode() != null ? metadata.getConfigData().getTenantCode() : "");
//		
//		
//		//SOLR
//		//addStream.setVar("content", contentJson);
//		Metadata metadatan = Metadata.fromJson(contentJson);
//		metadatan.setDatasetCode(metadata.getDatasetCode());
//		SearchEngineMetadata newdocument = new SearchEngineMetadata();
//		newdocument.setupEngine(metadatan);
//		Gson gson = JSonHelper.getInstance();
//		String newJsonDoc= gson.toJson(newdocument);
//
//		
//		
////		CloudSolrClient solrServer =  CloudSolrSingleton.getServer();
////		solrServer.setDefaultCollection(Config.getInstance().getSolrCollection());
//		SolrInputDocument doc = newdocument.getSolrDocument();
//		
//		 
//		if ("KNOX".equalsIgnoreCase(Config.getInstance().getSolrTypeAccess()))
//		{
//			SolrClient solrServer= null;
//			solrServer = KnoxSolrSingleton.getServer();
//			log.info("[StoreService::createApiForBulk] - --KNOX------" + doc.toString());
//			log.info("[StoreService::createApiForBulk] - --user------" + Config.getInstance().getSolrUsername());
//			log.info("[StoreService::createApiForBulk] - --pwd------" + Config.getInstance().getSolrPassword());
//			log.info("[StoreService::createApiForBulk] - --collection------" + Config.getInstance().getSolrCollection());
//			
// 
//			((TEHttpSolrClient)solrServer).setDefaultCollection(Config.getInstance().getSolrCollection());
//			solrServer.add(Config.getInstance().getSolrCollection(),doc);
//			//solrServer.add(doc);
//			solrServer.commit();
//		}
//		else {
//			CloudSolrClient solrServer = CloudSolrSingleton.getServer();
//			solrServer.setDefaultCollection(Config.getInstance().getSolrCollection());
//			log.info("[StoreService::createApiForBulk] - ---------------------" + doc.toString());
//			solrServer.add(Config.getInstance().getSolrCollection(),doc);
//			solrServer.commit();
//		}
//		
//		
//		addStream.run();
//
//		} catch (Exception e) {
//			log.info("[StoreService::createApiForBulk] ERROREEEEE ");
//			e.printStackTrace();throw e;
//		}
//
//		return apiFinalName;
//		
//		
//		
//		
//		
//	}

}
