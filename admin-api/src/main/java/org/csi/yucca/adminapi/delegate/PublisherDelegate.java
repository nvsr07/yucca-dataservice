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
import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.SharingTenantsJson;
import org.csi.yucca.adminapi.model.TagJson;
import org.csi.yucca.adminapi.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@PropertySource(value = { "classpath:adminapi.properties" })
public class PublisherDelegate {

	private static final Logger logger = Logger.getLogger(PublisherDelegate.class);

	public static final int API_FIELD_MAX_LENGTH = 600;

	private static PublisherDelegate publisherDelegate;

	@Value("${publisher.url}")
	private String publisherUrl;
	@Value("${publisher.consoleAddress}")
	private String consoleAddress;
	@Value("${publisher.baseExposedApiUrl}")
	private String baseExposedApiUrl;
	@Value("${publisher.httpOk}")
	private String httpOk;
	@Value("${publisher.responseOk}")
	private String responseOk;
	@Value("${publisher.baseApiUrl}")
	private String baseApiUrl;
	@Value("${store.user}")
	private String publisherUser;
	@Value("${store.password}")
	private String publisherPassword;

	private ObjectMapper mapper = new ObjectMapper();

	public PublisherDelegate() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		logger.info("[PublisherDelegate::PublisherDelegate]  store url " + publisherUrl);
	}

	public static PublisherDelegate build() {
		if (publisherDelegate == null)
			publisherDelegate = new PublisherDelegate();
		return publisherDelegate;
	}

	public CloseableHttpClient registerToStoreInit() throws Exception {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		// login
		loginOnPublisher(httpclient);

		return httpclient;
	}

	private String loginOnPublisher(CloseableHttpClient httpclient) throws HttpException, IOException {
		logger.info("[PublisherDelegate::loginOnStore] username " + publisherUser + " - publisher url " + publisherUrl);

		List<NameValuePair> loginParams = new LinkedList<NameValuePair>();
		loginParams.add(new BasicNameValuePair("action", "login"));
		loginParams.add(new BasicNameValuePair("username", publisherUser));
		loginParams.add(new BasicNameValuePair("password", publisherPassword));

		String url = publisherUrl + "site/blocks/user/login/ajax/login.jag";
		String response = HttpDelegate.makeHttpPost(httpclient, url, loginParams);
		logger.debug("[PublisherDelegate::loginOnStore] response " + response);

		return response;

	}

	public static final String createApiNameOData(String datasetCode) {
		return datasetCode + "_odata";
	}

	public String addApi(CloseableHttpClient httpclient, boolean update, DettaglioStream stream, String datasetcode) throws HttpException, IOException, Exception {
		logger.debug("[PublisherDelegate::addApi] STREAM");

		List<NameValuePair> addApiParams = new LinkedList<NameValuePair>();

		String apiName = datasetcode;
		addApiParams.add(new BasicNameValuePair("description", stream.getStreamname() != null ? Util.safeSubstring(stream.getStreamname(), API_FIELD_MAX_LENGTH) : ""));
		addApiParams.add(new BasicNameValuePair("codiceStream", stream.getStreamcode() != null ? stream.getStreamcode() : ""));
		addApiParams.add(new BasicNameValuePair("nomeStream", stream.getStreamname() != null ? stream.getStreamname() : ""));
		addApiParams.add(new BasicNameValuePair("virtualEntityName", stream.getSmartObjectCode() != null ? stream.getSmartObjectCode() : ""));
		addApiParams.add(new BasicNameValuePair("virtualEntityName", stream.getSmartObjectName() != null ? stream.getSmartObjectName() : ""));
		addApiParams.add(new BasicNameValuePair("virtualEntityDescription", stream.getSmartObjectDescription() != null ? Util.safeSubstring(stream.getSmartObjectDescription(),
				API_FIELD_MAX_LENGTH) : ""));

		return addApi(httpclient, update, addApiParams, apiName, stream.getDataSourceVisibility(), stream.getSharingTenant(), stream.getDomDomainCode(), stream.getTags(),
				stream.getDataSourceIcon(), stream.getLicense(), stream.getDataSourceDisclaimer(), stream.getTenantCode(), stream.getTenantName());

	}

	private String addApi(CloseableHttpClient httpclient, boolean update, List<NameValuePair> addApiParams, String apiName, String visibility, String sharingTenant,
			String domainCode, String tags, String icon, String license, String disclaimer, String tenantcode, String tenantname) throws Exception {

		String endpoint = baseApiUrl + apiName;
		String apiFinalName = createApiNameOData(apiName);

		if (update) {
			addApiParams.add(new BasicNameValuePair("action", "updateAPI"));
		} else {
			addApiParams.add(new BasicNameValuePair("action", "addAPI"));
		}

		if ("public".equals(visibility)) {
			addApiParams.add(new BasicNameValuePair("visibility", "public"));
			addApiParams.add(new BasicNameValuePair("roles", ""));
			addApiParams.add(new BasicNameValuePair("resourceMethodAuthType-0", "None"));
		} else {
			addApiParams.add(new BasicNameValuePair("visibility", "restricted"));
			String roles = "";

			if (sharingTenant != null) {
				List<SharingTenantsJson> tenants = mapper.readValue(sharingTenant, new TypeReference<List<SharingTenantsJson>>() {
				});
				List<String> tenantsCode = new LinkedList<String>();
				for (SharingTenantsJson tenant : tenants) {
					if (!roles.equals(""))
						roles += ",";
					roles += tenant.getTenantcode() + "_subscriber";
				}
			}
			addApiParams.add(new BasicNameValuePair("roles", roles));
			addApiParams.add(new BasicNameValuePair("resourceMethodAuthType-0", "Application & Application User"));

		}

		addApiParams.add(new BasicNameValuePair("apimanConsoleAddress", consoleAddress));
		addApiParams.add(new BasicNameValuePair("username", publisherUser));
		addApiParams.add(new BasicNameValuePair("password", publisherPassword));
		addApiParams.add(new BasicNameValuePair("httpok", httpOk));
		addApiParams.add(new BasicNameValuePair("ok", responseOk));

		addApiParams.add(new BasicNameValuePair("version", "1.0"));
		addApiParams.add(new BasicNameValuePair("name", apiFinalName));
		addApiParams.add(new BasicNameValuePair("context", "/api/" + apiName));
		addApiParams.add(new BasicNameValuePair("P", ""));
		addApiParams.add(new BasicNameValuePair("endpoint", endpoint));

		addApiParams.add(new BasicNameValuePair("extra_isApi", "false"));
		String allTags = "";
		if (domainCode != null) {
			allTags += domainCode;
		}

		List<String> tagCodes = null;
		if (tags != null) {
			tagCodes = new LinkedList<String>();
			List<TagJson> tagsJson = mapper.readValue(tags, new TypeReference<List<TagJson>>() {
			});
			tagCodes = new LinkedList<String>();
			for (TagJson t : tagsJson) {
				allTags += "," + t.getTagcode();
				tagCodes.add(t.getTagcode());
			}
		}
		addApiParams.add(new BasicNameValuePair("tags", Util.safeSubstring(allTags, API_FIELD_MAX_LENGTH)));

		addApiParams.add(new BasicNameValuePair("licence", license != null && license != null ? Util.safeSubstring(license, API_FIELD_MAX_LENGTH) : ""));
		addApiParams.add(new BasicNameValuePair("disclaimer", disclaimer != null ? Util.safeSubstring(disclaimer, API_FIELD_MAX_LENGTH) : ""));

		addApiParams.add(new BasicNameValuePair("codiceTenant", tenantcode != null ? tenantcode : ""));
		addApiParams.add(new BasicNameValuePair("nomeTenant", tenantname != null ? tenantname : ""));

		// nel file prop
		addApiParams.add(new BasicNameValuePair("address", consoleAddress + "/publisher/site/blocks/item-add/ajax/add.jag"));
		addApiParams.add(new BasicNameValuePair("method", "POSTMULTI"));
		addApiParams.add(new BasicNameValuePair("endpoint_type", "address"));
		addApiParams
				.add(new BasicNameValuePair("endpoint_config",
						"{\"production_endpoints\":{\"url\":\"http://int-sdnet-intapi.sdp.csi.it:90/odata/SmartDataOdataService.svc/Prova600_487\",\"config\":null},\"endpoint_type\":\"address\"}"));

		addApiParams.add(new BasicNameValuePair("production_endpoints", endpoint));
		addApiParams.add(new BasicNameValuePair("sandbox_endpoints", ""));
		addApiParams.add(new BasicNameValuePair("wsdl", ""));
		addApiParams.add(new BasicNameValuePair("tier", ""));
		addApiParams.add(new BasicNameValuePair("FILE.apiThumb.name", icon));
		addApiParams.add(new BasicNameValuePair("bizOwner", "bizOwner"));
		addApiParams.add(new BasicNameValuePair("bizOwnerMail", "bizOwner@csi.it"));
		addApiParams.add(new BasicNameValuePair("techOwner", "tecnikus"));
		addApiParams.add(new BasicNameValuePair("techOwnerMail", "tecnikus@csi.it"));
		addApiParams.add(new BasicNameValuePair("tiersCollection", "Unlimited"));
		addApiParams.add(new BasicNameValuePair("resourceCount", "0"));
		addApiParams.add(new BasicNameValuePair("resourceMethod-0", "GET"));
		addApiParams.add(new BasicNameValuePair("resourceMethodThrottlingTier-0", "Unlimited"));
		addApiParams.add(new BasicNameValuePair("uriTemplate-0", "/*"));
		addApiParams.add(new BasicNameValuePair("transports.1", "http"));
		addApiParams.add(new BasicNameValuePair("transports.1.name", "transports"));
		addApiParams.add(new BasicNameValuePair("http_checked", "http"));
		addApiParams.add(new BasicNameValuePair("https_checked", "https"));
		addApiParams.add(new BasicNameValuePair("default_version_checked", "default_version"));

		// EXTRA
		addApiParams.add(new BasicNameValuePair("extra_codiceTenant", ""));
		addApiParams.add(new BasicNameValuePair("extra_copyright", ""));
		addApiParams.add(new BasicNameValuePair("extra_codiceStream", ""));
		addApiParams.add(new BasicNameValuePair("extra_nomeStream", ""));
		addApiParams.add(new BasicNameValuePair("extra_nomeTenant", ""));
		addApiParams.add(new BasicNameValuePair("extra_licence", ""));
		addApiParams.add(new BasicNameValuePair("extra_virtualEntityName", ""));
		addApiParams.add(new BasicNameValuePair("extra_virtualEntityDescription", ""));
		addApiParams.add(new BasicNameValuePair("extra_disclaimer", ""));
		addApiParams.add(new BasicNameValuePair("extra_virtualEntityCode", ""));
		addApiParams.add(new BasicNameValuePair("provider", "admin"));
		addApiParams.add(new BasicNameValuePair("extra_apiDescription", ""));
		addApiParams.add(new BasicNameValuePair("extra_latitude", ""));
		addApiParams.add(new BasicNameValuePair("extra_longitude", ""));

		String url = publisherUrl + "site/blocks/item-add/ajax/add.jag";
		String response = HttpDelegate.makeHttpPost(httpclient, url, addApiParams);
		if (response != null) {
			PublisherResponse publisherResponse = mapper.readValue(response, PublisherResponse.class);
			if (publisherResponse.getError())
				throw new Exception(publisherResponse.getMessage());
		}

		// response {"error" : true, "message" :
		// " Error occurred while adding the API. A duplicate API already exists for ProvaConErrori4_3070_odata-1.0"}
		logger.debug("[PublisherDelegate::addApi] response " + response);
		return apiFinalName;

	}

	private void setupAddApiUpdateProperties(List<NameValuePair> addApiParams) {
		// TODO Auto-generated method stub

	}

	public String addApi(CloseableHttpClient httpclient, boolean update, DettaglioDataset dataset) throws HttpException, IOException, Exception {
		logger.debug("[PublisherDelegate::addApi] DATASET");
		List<NameValuePair> addApiParams = new LinkedList<NameValuePair>();

		String apiName = dataset.getDatasetcode();

		// addApiParams.add(new BasicNameValuePair("action", "addAPI"));
		addApiParams.add(new BasicNameValuePair("description", dataset.getDescription() != null ? Util.safeSubstring(dataset.getDescription(), API_FIELD_MAX_LENGTH) : ""));
		addApiParams.add(new BasicNameValuePair("codiceStream", ""));
		addApiParams.add(new BasicNameValuePair("nomeStream", ""));
		addApiParams.add(new BasicNameValuePair("virtualEntityName", ""));
		addApiParams.add(new BasicNameValuePair("virtualEntityDescription", ""));

		return addApi(httpclient, update, addApiParams, apiName, dataset.getDataSourceVisibility(), dataset.getSharingTenant(), dataset.getDomDomainCode(), dataset.getTags(),
				dataset.getDataSourceIcon(), dataset.getLicense(), dataset.getDataSourceDisclaimer(), dataset.getTenantCode(), dataset.getTenantName());

		// if("public".equals(dataset.getDataSourceVisibility())){
		// addApiParams.add(new BasicNameValuePair("visibility", "public"));
		// addApiParams.add(new BasicNameValuePair("roles", ""));
		// addApiParams.add(new BasicNameValuePair("resourceMethodAuthType-0",
		// "None"));
		// }
		// else{
		// addApiParams.add(new BasicNameValuePair("visibility", "restricted"));
		// String roles = "";
		//
		// if(dataset.getSharingTenant()!=null){
		// List<SharingTenantsJson> tenants =
		// mapper.readValue(dataset.getSharingTenant(), new
		// TypeReference<List<SharingTenantsJson>>() {
		// });
		// List<String> tenantsCode = new LinkedList<String>();
		// for (SharingTenantsJson tenant : tenants) {
		// if (!roles.equals(""))
		// roles += ",";
		// roles += tenant.getTenantcode() + "_subscriber";
		// }
		// }
		// addApiParams.add(new BasicNameValuePair("roles", roles));
		// addApiParams.add(new BasicNameValuePair("resourceMethodAuthType-0",
		// "Application & Application User"));
		//
		// }
		//

		// addApiParams.add(new BasicNameValuePair("apimanConsoleAddress",
		// consoleAddress));
		// addApiParams.add(new BasicNameValuePair("username", publisherUser));
		// addApiParams.add(new BasicNameValuePair("password",
		// publisherPassword));
		// addApiParams.add(new BasicNameValuePair("httpok", httpOk));
		// addApiParams.add(new BasicNameValuePair("ok", responseOk));
		//
		// addApiParams.add(new BasicNameValuePair("version", "1.0"));
		// addApiParams.add(new BasicNameValuePair("name", apiFinalName));
		// addApiParams.add(new BasicNameValuePair("context", "/api/" +
		// apiName));
		//
		// addApiParams.add(new BasicNameValuePair("P", ""));
		// addApiParams.add(new BasicNameValuePair("endpoint", endpoint));
		//
		//
		// addApiParams.add(new BasicNameValuePair("extra_isApi", "false"));
		// addApiParams.add(new BasicNameValuePair("extra_apiDescription",
		// dataset.getDatasetname() != null ? dataset.getDatasetname() : ""));
		// addApiParams.add(new BasicNameValuePair("codiceTenant",
		// dataset.getIdTenant() != null ? dataset.getIdTenant() : "");
		// addApiParams.add(new BasicNameValuePair("nomeTenant",
		// dataset.getIdTenant() != null ? dataset.getIdTenant() : ""));

		// String tags = "";
		// if (dataset.getDomDomainCode() != null) {
		// tags += dataset.getDomDomainCode();
		// }
		//
		// List<String> tagCodes = null;
		// if (dataset.getTags() != null) {
		// tagCodes = new LinkedList<String>();
		// List<TagJson> tagsJson = mapper.readValue(dataset.getTags(), new
		// TypeReference<List<TagJson>>() {
		// });
		// tagCodes = new LinkedList<String>();
		// for (TagJson t : tagsJson) {
		// tags += "," + t.getTagcode();
		// tagCodes.add(t.getTagcode());
		// }
		// }
		// addApiParams.add(new BasicNameValuePair("tags",
		// Util.safeSubstring(tags, API_FIELD_MAX_LENGTH)));
		//
		//
		// //nel file prop
		// addApiParams.add(new BasicNameValuePair("address",consoleAddress +
		// "/publisher/site/blocks/item-add/ajax/add.jag"));
		// addApiParams.add(new BasicNameValuePair("method","POSTMULTI"));
		// addApiParams.add(new BasicNameValuePair("endpoint_type","address"));
		// addApiParams.add(new
		// BasicNameValuePair("endpoint_config","{\"production_endpoints\":{\"url\":\"http://int-sdnet-intapi.sdp.csi.it:90/odata/SmartDataOdataService.svc/Prova600_487\",\"config\":null},\"endpoint_type\":\"address\"}"));
		//
		//
		//
		// addApiParams.add(new
		// BasicNameValuePair("production_endpoints",endpoint));
		// addApiParams.add(new BasicNameValuePair("sandbox_endpoints",""));
		// addApiParams.add(new BasicNameValuePair("wsdl",""));
		// addApiParams.add(new BasicNameValuePair("tier",""));
		// addApiParams.add(new
		// BasicNameValuePair("FILE.apiThumb.name",dataset.getDataSourceIcon()));
		// addApiParams.add(new BasicNameValuePair("bizOwner","bizOwner"));
		// addApiParams.add(new
		// BasicNameValuePair("bizOwnerMail","bizOwner@csi.it"));
		// addApiParams.add(new BasicNameValuePair("techOwner","tecnikus"));
		// addApiParams.add(new
		// BasicNameValuePair("techOwnerMail","tecnikus@csi.it"));
		// addApiParams.add(new
		// BasicNameValuePair("tiersCollection","Unlimited"));
		// addApiParams.add(new BasicNameValuePair("resourceCount","0"));
		// addApiParams.add(new BasicNameValuePair("resourceMethod-0","GET"));
		// addApiParams.add(new
		// BasicNameValuePair("resourceMethodThrottlingTier-0","Unlimited"));
		// addApiParams.add(new BasicNameValuePair("uriTemplate-0","/*"));
		// addApiParams.add(new BasicNameValuePair("transports.1","http"));
		// addApiParams.add(new
		// BasicNameValuePair("transports.1.name","transports"));
		// addApiParams.add(new BasicNameValuePair("http_checked","http"));
		// addApiParams.add(new BasicNameValuePair("https_checked","https"));
		// addApiParams.add(new
		// BasicNameValuePair("default_version_checked","default_version"));
		//
		//
		//
		//
		//
		//
		//
		//
		//
		//
		//
		//
		// // EXTRA
		// addApiParams.add(new BasicNameValuePair("extra_codiceTenant",""));
		// addApiParams.add(new BasicNameValuePair("extra_copyright",""));
		// addApiParams.add(new BasicNameValuePair("extra_codiceStream",""));
		// addApiParams.add(new BasicNameValuePair("extra_nomeStream",""));
		// addApiParams.add(new BasicNameValuePair("extra_nomeTenant",""));
		// addApiParams.add(new BasicNameValuePair("extra_licence",""));
		// addApiParams.add(new
		// BasicNameValuePair("extra_virtualEntityName",""));
		// addApiParams.add(new
		// BasicNameValuePair("extra_virtualEntityDescription",""));
		// addApiParams.add(new BasicNameValuePair("extra_disclaimer",""));
		// addApiParams.add(new
		// BasicNameValuePair("extra_virtualEntityCode",""));//
		// addApiParams.add(new BasicNameValuePair("provider","admin"));
		// addApiParams.add(new BasicNameValuePair("extra_apiDescription",""));
		// addApiParams.add(new BasicNameValuePair("extra_latitude",""));
		// addApiParams.add(new BasicNameValuePair("extra_longitude",""));

		// String url = publisherUrl +
		// "site/blocks/application/application-add/ajax/application-add.jag";
	}

	public String publishApi(CloseableHttpClient httpclient, String apiVersion, String apiName, String provider) throws HttpException, IOException {
		logger.debug("[PublisherDelegate::publishApi] apiName " + apiName);
		return updateLifeCycles(httpclient, apiName, "PUBLISHED");
	}

	public String removeApi(CloseableHttpClient httpclient, String apiName) throws HttpException, IOException, Exception {
		logger.debug("[PublisherDelegate::removeApi]");
		return updateLifeCycles(httpclient, apiName, "BLOCKED");
	}

	private String updateLifeCycles(CloseableHttpClient httpclient, String apiName, String status) throws HttpException, IOException {
		logger.debug("[PublisherDelegate::publishApi] apiName " + apiName);
		List<NameValuePair> publishApi = new LinkedList<NameValuePair>();
		publishApi.add(new BasicNameValuePair("action", "updateStatus"));
		publishApi.add(new BasicNameValuePair("apimanConsoleAddress", consoleAddress));
		publishApi.add(new BasicNameValuePair("httpok", httpOk));
		publishApi.add(new BasicNameValuePair("ok", responseOk));

		publishApi.add(new BasicNameValuePair("status", "PUBLISHED"));
		publishApi.add(new BasicNameValuePair("version", "1.0"));
		publishApi.add(new BasicNameValuePair("name", apiName));
		publishApi.add(new BasicNameValuePair("provider", "admin"));
		publishApi.add(new BasicNameValuePair("publishToGateway", "true"));

		// publishApi.add(new BasicNameValuePair("address",consoleAddress +
		// "/publisher/site/blocks/item-add/ajax/add.jag"));
		publishApi.add(new BasicNameValuePair("method", "POST"));
		// publishApi.add(new BasicNameValuePair("endpoint_type","address"));

		String url = publisherUrl + "site/blocks/life-cycles/ajax/life-cycles.jag";
		String response = HttpDelegate.makeHttpPost(httpclient, url, publishApi);
		logger.debug("[PublisherDelegate::addApi] response " + response);
		return response;
	}

	// public GeneralResponse generateKey(CloseableHttpClient httpclient, String
	// application) throws Exception {
	// logger.debug("[PublisherDelegate::generetateKey] application: " +
	// application);
	// List<NameValuePair> generetateKeyParams = new
	// LinkedList<NameValuePair>();
	// generetateKeyParams.add(new BasicNameValuePair("action",
	// "generateApplicationKey"));
	// generetateKeyParams.add(new BasicNameValuePair("name", ""));
	// generetateKeyParams.add(new BasicNameValuePair("version", ""));
	// generetateKeyParams.add(new BasicNameValuePair("tier", ""));
	// generetateKeyParams.add(new BasicNameValuePair("applicationName", ""));
	// generetateKeyParams.add(new BasicNameValuePair("application",
	// application));
	// generetateKeyParams.add(new BasicNameValuePair("provider", "admin"));
	// generetateKeyParams.add(new BasicNameValuePair("keytype", "PRODUCTION"));
	// generetateKeyParams.add(new BasicNameValuePair("callbackUrl", ""));
	// generetateKeyParams.add(new BasicNameValuePair("authorizedDomains",
	// "ALL"));
	// generetateKeyParams.add(new BasicNameValuePair("validityTime",
	// "999999999"));
	//
	// GeneralResponse generalResponse = null;
	// String url = publisherUrl +
	// "site/blocks/subscription/subscription-add/ajax/subscription-add.jag";
	// String response = HttpDelegate.makeHttpPost(httpclient, url,
	// generetateKeyParams);
	// ObjectMapper mapper = new ObjectMapper();
	// if (response != null)
	// generalResponse = mapper.readValue(response, GeneralResponse.class);
	// if (generalResponse.getError())
	// throw new Exception("Add Application for " + application + " failed: " +
	// generalResponse.getMessage());
	// if (generalResponse.getData() == null ||
	// generalResponse.getData().getKey() == null ||
	// generalResponse.getData().getKey().getConsumerKey() == null
	// || generalResponse.getData().getKey().getConsumerSecret() == null)
	// throw new Exception("Add Application for " + application +
	// " failed: Invalid consumerKey and consumerSecret - " +
	// generalResponse.getMessage());
	//
	// return generalResponse;
	// }
	//
	// public String subscribeApi(CloseableHttpClient httpclient,String apiName,
	// String appName) throws Exception {
	// logger.debug("[PublisherDelegate::subscribeApi] appName: " + appName);
	// List<NameValuePair> subscribeAdminApiParams = new
	// LinkedList<NameValuePair>();
	// subscribeAdminApiParams.add(new BasicNameValuePair("action",
	// "addAPISubscription"));
	// subscribeAdminApiParams.add(new BasicNameValuePair("name",apiName));
	// subscribeAdminApiParams.add(new BasicNameValuePair("version", "1.0"));
	// subscribeAdminApiParams.add(new BasicNameValuePair("tier", "Unlimited"));
	// subscribeAdminApiParams.add(new BasicNameValuePair("applicationName",
	// appName));
	// subscribeAdminApiParams.add(new BasicNameValuePair("application", ""));
	// subscribeAdminApiParams.add(new BasicNameValuePair("provider", "admin"));
	// subscribeAdminApiParams.add(new BasicNameValuePair("keytype", ""));
	// subscribeAdminApiParams.add(new BasicNameValuePair("callbackUrl", ""));
	// subscribeAdminApiParams.add(new BasicNameValuePair("authorizedDomains",
	// ""));
	// subscribeAdminApiParams.add(new BasicNameValuePair("validityTime", ""));
	//
	// String url = publisherUrl +
	// "site/blocks/subscription/subscription-add/ajax/subscription-add.jag";
	// String response = HttpDelegate.makeHttpPost(httpclient, url,
	// subscribeAdminApiParams);
	// logger.debug("[PublisherDelegate::subscribeApi] response " + response);
	// return response;
	// }

	public String logoutFromStore(CloseableHttpClient httpclient, String username, String password) throws Exception {
		logger.debug("[PublisherDelegate::logoutFromStore] username " + username);

		List<NameValuePair> logoutParams = new LinkedList<NameValuePair>();
		logoutParams.add(new BasicNameValuePair("action", "logout"));
		logoutParams.add(new BasicNameValuePair("username", username));
		logoutParams.add(new BasicNameValuePair("password", password));

		String url = publisherUrl + "site/blocks/user/login/ajax/login.jag";
		String response = HttpDelegate.makeHttpPost(httpclient, url, logoutParams);
		logger.debug("[PublisherDelegate::loginOnStore] response " + response);
		return response;
	}

	public class PublisherResponse {
		private boolean error;
		private String message;

		public PublisherResponse() {
			super();
		}

		public PublisherResponse(boolean error, String message) {
			super();
			this.setError(error);
			this.setMessage(message);
		}

		public boolean getError() {
			return error;
		}

		public void setError(boolean error) {
			this.error = error;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

	// public String createApiForBulk(Dataset dataset) {
	// logger.debug("[PublisherDelegate::createApiForBulk] appName: " +
	// appName);
	// String apiFinalName=null;
	// try {
	// String apiName = dataset.getDatasetcode();
	// apiFinalName = dataset.getDatasecode() + "_odata";
	//
	// AddStream addStream = new AddStream();
	// addStream.setProperties(update);
	//
	// // FIXME get the list of roles(tenants) from the stream info
	// if ("public".equals(dataset.getVisibility())) {
	// addApplicationParams.add(new BasicNameValuePair("visibility", "public");
	// addApplicationParams.add(new BasicNameValuePair("roles", "");
	// addApplicationParams.add(new BasicNameValuePair("authType", "None");
	// } else {
	// addApplicationParams.add(new BasicNameValuePair("visibility",
	// "restricted");
	//
	// String ruoli = "";
	//
	// if (dataset.getTenantssharing() != null &&
	// dataset.getTenantssharing().getTenantsharing() != null) {
	// for (org.csi.yucca.storage.datamanagementapi.model.metadata.Tenantsharing
	// t : dataset.getTenantssharing().getTenantsharing()) {
	// if (!ruoli.equals(""))
	// ruoli += ",";
	// ruoli += t.getTenantCode() + "_subscriber";
	// }
	// }
	//
	// if (!ruoli.contains(dataset.getTenantCode() + "_subscriber")) {
	// ruoli += dataset.getTenantCode() + "_subscriber";
	// }
	//
	// addApplicationParams.add(new BasicNameValuePair("roles", ruoli);
	// addApplicationParams.add(new BasicNameValuePair("authType",
	// "Application & Application User");
	// }
	//
	// if (update) {
	// addApplicationParams.add(new BasicNameValuePair("actionAPI",
	// "updateAPI");
	// } else {
	// addApplicationParams.add(new BasicNameValuePair("actionAPI", "addAPI");
	// }
	//
	// addApplicationParams.add(new BasicNameValuePair("apimanConsoleAddress",
	// Config.getInstance().getConsoleAddress());
	// addApplicationParams.add(new BasicNameValuePair("username",
	// Config.getInstance().getStoreUsername());
	// addApplicationParams.add(new BasicNameValuePair("password",
	// Config.getInstance().getStorePassword());
	// addApplicationParams.add(new BasicNameValuePair("httpok",
	// Config.getInstance().getHttpOk());
	// addApplicationParams.add(new BasicNameValuePair("ok",
	// Config.getInstance().getResponseOk());
	//
	// // addApplicationParams.add(new BasicNameValuePair("icon", path +
	// fileName);
	// addApplicationParams.add(new BasicNameValuePair("apiVersion", "1.0");
	// addApplicationParams.add(new BasicNameValuePair("apiName", apiFinalName);
	// addApplicationParams.add(new BasicNameValuePair("context", "/api/" +
	// apiName);// ds_Voc_28;
	// addApplicationParams.add(new BasicNameValuePair("P", "");
	// addApplicationParams.add(new BasicNameValuePair("endpoint",
	// Config.getInstance().getBaseApiUrl() + apiName);
	// addApplicationParams.add(new BasicNameValuePair("desc",
	// dataset.getDescription() != null ?
	// Util.safeSubstring(dataset.getDescription(), API_FIELD_MAX_LENGTH) : "");
	// addApplicationParams.add(new BasicNameValuePair("copiright",
	// dataset.getCopyright() != null ?
	// Util.safeSubstring(dataset.getCopyright(), API_FIELD_MAX_LENGTH) : "");
	//
	// addApplicationParams.add(new BasicNameValuePair("extra_isApi", "false");
	// addApplicationParams.add(new BasicNameValuePair("extra_apiDescription",
	// dataset.getDatasetName() != null ? dataset.getDatasetName() : "");
	// addApplicationParams.add(new BasicNameValuePair("codiceTenant",
	// dataset.getTenantCode() != null ? dataset.getTenantCode() : "");
	// addApplicationParams.add(new BasicNameValuePair("codiceStream", "");
	// addApplicationParams.add(new BasicNameValuePair("nomeStream", "");
	// addApplicationParams.add(new BasicNameValuePair("nomeTenant",
	// dataset.getTenantCode() != null ? dataset.getTenantCode() : "");
	// addApplicationParams.add(new BasicNameValuePair("licence",
	// dataset.getLicense() != null ? Util.safeSubstring(dataset.getLicense(),
	// API_FIELD_MAX_LENGTH) : "");
	// addApplicationParams.add(new BasicNameValuePair("disclaimer",
	// dataset.getDisclaimer() != null ?
	// Util.safeSubstring(dataset.getDisclaimer(), API_FIELD_MAX_LENGTH) : "");
	// addApplicationParams.add(new BasicNameValuePair("virtualEntityName", "");
	// addApplicationParams.add(new
	// BasicNameValuePair("virtualEntityDescription", "");
	//
	// String tags = "";
	//
	// if (dataset.getDataDomain() != null) {
	// tags += dataset.getDataDomain();
	// }
	// List<String> tagCodes = null;
	// if (dataset.getTags() != null) {
	// tagCodes = new LinkedList<String>();
	// for (org.csi.yucca.storage.datamanagementapi.model.metadata.Tag t :
	// dataset.getTags()) {
	// tags += "," + t.getTagCode();
	// tagCodes.add(t.getTagCode());
	// }
	// }
	//
	// addApplicationParams.add(new BasicNameValuePair("tags",
	// Util.safeSubstring(tags, API_FIELD_MAX_LENGTH));
	//
	// // DT Add document ? Why restart from jsonFile? we lost init
	// //String contentJson =
	// extractMetadataContentForDocument(jsonFile,dataset.getTenantCode() !=
	// null ? dataset.getTenantCode() : "");
	// String contentJson =
	// extractMetadataContentForDocument(metadata,dataset.getTenantCode() !=
	// null ? dataset.getTenantCode() : "");
	//
	//
	// //SOLR
	// //addApplicationParams.add(new BasicNameValuePair("content",
	// contentJson);
	// Metadata metadatan = Metadata.fromJson(contentJson);
	// metadatan.setDatasetCode(metadata.getDatasetCode());
	// SearchEngineMetadata newdocument = new SearchEngineMetadata();
	// newdocument.setupEngine(metadatan);
	// Gson gson = JSonHelper.getInstance();
	// String newJsonDoc= gson.toJson(newdocument);
	//
	//
	//
	// // CloudSolrClient solrServer = CloudSolrSingleton.getServer();
	// //
	// solrServer.setDefaultCollection(Config.getInstance().getSolrCollection());
	// SolrInputDocument doc = newdocument.getSolrDocument();
	//
	//
	// if ("KNOX".equalsIgnoreCase(Config.getInstance().getSolrTypeAccess()))
	// {
	// SolrClient solrServer= null;
	// solrServer = KnoxSolrSingleton.getServer();
	// log.info("[StoreService::createApiForBulk] - --KNOX------" +
	// doc.toString());
	// log.info("[StoreService::createApiForBulk] - --user------" +
	// Config.getInstance().getSolrUsername());
	// log.info("[StoreService::createApiForBulk] - --pwd------" +
	// Config.getInstance().getSolrPassword());
	// log.info("[StoreService::createApiForBulk] - --collection------" +
	// Config.getInstance().getSolrCollection());
	//
	//
	// ((TEHttpSolrClient)solrServer).setDefaultCollection(Config.getInstance().getSolrCollection());
	// solrServer.add(Config.getInstance().getSolrCollection(),doc);
	// //solrServer.add(doc);
	// solrServer.commit();
	// }
	// else {
	// CloudSolrClient solrServer = CloudSolrSingleton.getServer();
	// solrServer.setDefaultCollection(Config.getInstance().getSolrCollection());
	// log.info("[StoreService::createApiForBulk] - ---------------------" +
	// doc.toString());
	// solrServer.add(Config.getInstance().getSolrCollection(),doc);
	// solrServer.commit();
	// }
	//
	//
	// addStream.run();
	//
	// } catch (Exception e) {
	// log.info("[StoreService::createApiForBulk] ERROREEEEE ");
	// e.printStackTrace();throw e;
	// }
	//
	// return apiFinalName;
	//
	//
	//
	//
	//
	// }

}
