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
import org.csi.yucca.adminapi.store.response.StoreResponse;
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
	public static final String API_TYPE_ODATA = "api_odata";
	public static final String API_TYPE_TOPIC = "api_topic";

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

	public static final String createApiNameTopic(DettaglioStream dettaglioStream) {
		return dettaglioStream.getTenantCode() + "." + dettaglioStream.getSmartObjectCode() + "_" + dettaglioStream.getStreamcode() + "_stream";
	}

	public String addApi(CloseableHttpClient httpclient, DettaglioStream stream) throws HttpException, IOException, Exception {
		logger.debug("[PublisherDelegate::addApi] STREAM");

		List<NameValuePair> addApiParams = new LinkedList<NameValuePair>();

		addApiParams.add(new BasicNameValuePair("description", stream.getStreamname() != null ? Util.safeSubstring(stream.getStreamname(), API_FIELD_MAX_LENGTH) : ""));
		addApiParams.add(new BasicNameValuePair("codiceStream", stream.getStreamcode() != null ? stream.getStreamcode() : ""));
		addApiParams.add(new BasicNameValuePair("nomeStream", stream.getStreamname() != null ? stream.getStreamname() : ""));
		addApiParams.add(new BasicNameValuePair("virtualEntityCode", stream.getSmartObjectCode() != null ? stream.getSmartObjectCode() : ""));
		addApiParams.add(new BasicNameValuePair("virtualEntityName", stream.getSmartObjectName() != null ? stream.getSmartObjectName() : ""));
		addApiParams.add(new BasicNameValuePair("virtualEntityDescription", stream.getSmartObjectDescription() != null ? Util.safeSubstring(stream.getSmartObjectDescription(),
				API_FIELD_MAX_LENGTH) : ""));

		String apiName = createApiNameTopic(stream);
		String apiContext = "/api/topic/output." + stream.getTenantCode() + "." + stream.getSmartObjectCode() + "_" + stream.getStreamcode();
		String endpoint = "http://api.smartdatanet.it/dammiInfo";
		return addApi(httpclient, addApiParams, apiName, endpoint, apiContext, stream.getDataSourceVisibility(), stream.getSharingTenant(), stream.getDomDomainCode(),
				stream.getTags(), stream.getDataSourceIcon(), stream.getLicense(), stream.getDataSourceDisclaimer(), stream.getTenantCode(), stream.getTenantName());
	}

	public String addApi(CloseableHttpClient httpclient, DettaglioStream stream, String datasetcode) throws HttpException, IOException, Exception {
		logger.debug("[PublisherDelegate::addApi] STREAM");

		List<NameValuePair> addApiParams = new LinkedList<NameValuePair>();

		addApiParams.add(new BasicNameValuePair("description", stream.getStreamname() != null ? Util.safeSubstring(stream.getStreamname(), API_FIELD_MAX_LENGTH) : ""));
		addApiParams.add(new BasicNameValuePair("codiceStream", stream.getStreamcode() != null ? stream.getStreamcode() : ""));
		addApiParams.add(new BasicNameValuePair("nomeStream", stream.getStreamname() != null ? stream.getStreamname() : ""));
		addApiParams.add(new BasicNameValuePair("virtualEntityCode", stream.getSmartObjectCode() != null ? stream.getSmartObjectCode() : ""));
		addApiParams.add(new BasicNameValuePair("virtualEntityName", stream.getSmartObjectName() != null ? stream.getSmartObjectName() : ""));
		addApiParams.add(new BasicNameValuePair("virtualEntityDescription", stream.getSmartObjectDescription() != null ? Util.safeSubstring(stream.getSmartObjectDescription(),
				API_FIELD_MAX_LENGTH) : ""));

		String apiName = createApiNameOData(datasetcode);
		String endpoint = baseApiUrl + apiName;
		String apiContext = "/api/" + datasetcode;
		return addApi(httpclient, addApiParams, apiName, endpoint, apiContext, stream.getDataSourceVisibility(), stream.getSharingTenant(), stream.getDomDomainCode(),
				stream.getTags(), stream.getDataSourceIcon(), stream.getLicense(), stream.getDataSourceDisclaimer(), stream.getTenantCode(), stream.getTenantName());

	}

	public String addApi(CloseableHttpClient httpclient, DettaglioDataset dataset) throws HttpException, IOException, Exception {
		logger.debug("[PublisherDelegate::addApi] DATASET");
		List<NameValuePair> addApiParams = new LinkedList<NameValuePair>();

		// addApiParams.add(new BasicNameValuePair("action", "addAPI"));
		addApiParams.add(new BasicNameValuePair("description", dataset.getDescription() != null ? Util.safeSubstring(dataset.getDescription(), API_FIELD_MAX_LENGTH) : ""));
		addApiParams.add(new BasicNameValuePair("codiceStream", ""));
		addApiParams.add(new BasicNameValuePair("nomeStream", ""));
		addApiParams.add(new BasicNameValuePair("virtualEntityName", ""));
		addApiParams.add(new BasicNameValuePair("virtualEntityDescription", ""));

		String apiName = createApiNameOData(dataset.getDatasetcode());
		String endpoint = baseApiUrl + apiName;
		String apiContext = "/api/" + apiName;
		return addApi(httpclient, addApiParams, apiName, endpoint, apiContext, dataset.getDataSourceVisibility(), dataset.getSharingTenant(), dataset.getDomDomainCode(),
				dataset.getTags(), dataset.getDataSourceIcon(), dataset.getLicense(), dataset.getDataSourceDisclaimer(), dataset.getTenantCode(), dataset.getTenantName());

	}

	private String addApi(CloseableHttpClient httpclient, List<NameValuePair> addApiParams, String apiName, String endpoint, String apiContext, String visibility,
			String sharingTenant, String domainCode, String tags, String icon, String license, String disclaimer, String tenantcode, String tenantname) throws Exception {

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
				// List<String> tenantsCode = new LinkedList<String>();
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

		addApiParams.add(new BasicNameValuePair("endpoint_config", "{\"production_endpoints\":{\"url\":\" " + endpoint + " \",\"config\":null},\"endpoint_type\":\"address\"}"));

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


		addApiParams.add(new BasicNameValuePair("name", apiName));
		addApiParams.add(new BasicNameValuePair("context", apiContext));

		addApiParams.add(0, new BasicNameValuePair("action", "addAPI"));
		String response = HttpDelegate.makeHttpPost(httpclient, url, addApiParams);
		if (response != null) {
			StoreResponse publisherResponse = mapper.readValue(response, StoreResponse.class);
			if (publisherResponse.getError()) {
				logger.info("[PublisherDelegate::addApi] - ERROR " + publisherResponse.getMessage());
				if (publisherResponse.getMessage() != null && publisherResponse.getMessage().toLowerCase().contains("duplicate")) {
					addApiParams.set(0, new BasicNameValuePair("action", "updateAPI"));
					response = HttpDelegate.makeHttpPost(httpclient, url, addApiParams);
					publisherResponse = mapper.readValue(response, StoreResponse.class);
					if (publisherResponse.getError())
						throw new Exception(publisherResponse.getMessage());
				} else
					throw new Exception(publisherResponse.getMessage());
			}
		}

		// response {"error" : true, "message" :
		// " Error occurred while adding the API. A duplicate API already exists for ProvaConErrori4_3070_odata-1.0"}
		logger.debug("[PublisherDelegate::addApi] response " + response);
		return apiName;

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

}
