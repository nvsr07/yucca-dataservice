package org.csi.yucca.adminapi.delegate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.Base64;
import org.apache.solr.common.util.NamedList;
import org.csi.yucca.adminapi.conf.Krb5HttpClientConfigurer;
import org.csi.yucca.adminapi.model.Component;
import org.csi.yucca.adminapi.model.Dcat;
import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.model.Tag;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@PropertySource(value = { "classpath:adminapi.properties" })
public class SolrDelegate {

	private static final Logger logger = Logger.getLogger(SolrDelegate.class);

	public static final String SOLR_TYPE_ACCESS_KNOX = "KNOX";
	public static final String SOLR_TYPE_ACCESS_CLOUD = "CLOUD";

	private static SolrDelegate solrDelegate;

	@Value("${solr.type.access}")
	private String solrTypeAccess;

	@Value("${solr.username}")
	private String solrUsername;

	@Value("${solr.url}")
	private String solrUrl;

	@Value("${solr.password}")
	private String solrPassword;

	@Value("${solr.collection}")
	private String solrCollection;

	@Value("${solr.security.domain.name}")
	private String solrSecurityDomainName;

	private SolrClient solrClient;

	private static ObjectMapper mapper = new ObjectMapper();

	public SolrDelegate() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		logger.info("[SolrDelegate::SolrDelegate]  solr type access url " + solrTypeAccess);
		if (SOLR_TYPE_ACCESS_KNOX.equalsIgnoreCase(solrTypeAccess)) {
			createKnoxClient();
		} else {
			createCloudClient();
		}
	}

	public static SolrDelegate build() {
		if (solrDelegate == null)
			solrDelegate = new SolrDelegate();
		return solrDelegate;
	}

	public void addDocument(SolrInputDocument doc) throws SolrServerException, IOException {
		logger.info("[SolrDelegate::addDocument] START - solrTypeAccess: " + solrTypeAccess + " -  doc" + doc.toString());

		if (SOLR_TYPE_ACCESS_KNOX.equalsIgnoreCase(solrTypeAccess)) {
			((TEHttpSolrClient) solrClient).setDefaultCollection(solrCollection);
		} else {
			((CloudSolrClient) solrClient).setDefaultCollection(solrCollection);
		}
		solrClient.add(solrCollection, doc);
		solrClient.commit();

	}

	public void addDocument(DettaglioDataset dettaglioDataset) throws Exception {

		SolrInputDocument doc = createSolrDocument(dettaglioDataset);
		addDocument(doc);
	}

	private SolrInputDocument createSolrDocument(DettaglioDataset dataset) throws Exception {
		SolrInputDocument doc = new SolrInputDocument();

		doc.addField("entityType", new ArrayList<String>(Arrays.asList("dataset")));
		doc.addField("name", dataset.getDatasetname());
		doc.addField("visibility", dataset.getDataSourceVisibility());
		doc.addField("copyright", dataset.getDataSourceCopyright());
		doc.addField("organizationCode", dataset.getOrganizationCode());
		doc.addField("organizationDescription", dataset.getOrganizationDescription());
		doc.addField("domainCode", dataset.getDomDomainCode());
		doc.addField("domainLangIT", dataset.getDomLangIt());
		doc.addField("domainLangEN", dataset.getDomLangEn());
		doc.addField("subdomainCode", dataset.getSubSubDomainCode());
		doc.addField("subdomainLangIT", dataset.getSubLangIt());
		doc.addField("subdomainLangEN", dataset.getSubLangEn());
		doc.addField("licenseCode", dataset.getLicense()); // FIXME description
		doc.addField("licenceDescription", dataset.getLicense());
		doc.addField("tenantCode", dataset.getTenantCode());
		doc.addField("tenantName", dataset.getTenantName());
		doc.addField("tenantDescription", dataset.getTenantDescription());

		if (dataset.getSharingTenant() != null) {
			List<Tenant> tenants = mapper.readValue(dataset.getComponents(), new TypeReference<List<Tenant>>() {
			});
			List<String> tenantsCode = new LinkedList<String>();
			for (Tenant tenant : tenants) {
				tenantsCode.add(tenant.getTenantcode());
			}
			doc.addField("tenantsCode", tenantsCode);
		}

		if (dataset.getTags() != null) {
			List<Tag> tags = mapper.readValue(dataset.getTags(), new TypeReference<List<Tag>>() {
			});
			List<String> tagCode = new LinkedList<String>();
			List<String> tagLangEN = new LinkedList<String>();
			List<String> tagLangIT = new LinkedList<String>();
			for (Tag tag : tags) {
				tagCode.add(tag.getTagcode());
				tagLangEN.add(tag.getLangen());
				tagLangIT.add(tag.getLangit());
			}

			doc.addField("tagCode", tagCode);
			doc.addField("tagLangIT", tagLangIT);
			doc.addField("tagLangEN", tagLangEN);
		}

		if (dataset.getDcat() != null) {
			Dcat dcat = Util.getFromJsonString(dataset.getDcat(), Dcat.class);
			doc.addField("dcatDataUpdate", dcat.getDcatdataupdate());
			doc.addField("dcatNomeOrg", dcat.getDcatnomeorg());
			doc.addField("dcatEmailOrg", dcat.getDcatemailorg());
			doc.addField("dcatCreatorName", dcat.getDcatcreatorname());
			doc.addField("dcatCreatorType", dcat.getDcatcreatortype());
			doc.addField("dcatCreatorId", dcat.getDcatcreatorid());
			doc.addField("dcatRightsHolderName", dcat.getDcatrightsholdername());
			doc.addField("dcatRightsHolderType", dcat.getDcatrightsholdertype());
			doc.addField("dcatRightsHolderId", dcat.getDcatrightsholderid());
			doc.addField("dcatReady", dcat.getDcatready());
		}
		doc.addField("datasetCode", dataset.getDatasetcode());
		doc.addField("datasetDescription", dataset.getDescription());
		doc.addField("version", dataset.getDatasourceversion());
		doc.addField("datasetType", dataset.getDatasetType());
		doc.addField("datasetSubtype", dataset.getDatasetSubtype());

		// doc.addField("streamCode", dataset.streamCode);
		// doc.addField("twtQuery", dataset.twtQuery);
		// doc.addField("twtGeolocLat", dataset.twtGeolocLat);
		// doc.addField("twtGeolocLon", dataset.twtGeolocLon);
		// doc.addField("twtGeolocRadius", dataset.twtGeolocRadius);
		// doc.addField("twtGeolocUnit", dataset.twtGeolocUnit);
		// doc.addField("twtLang", dataset.twtLang);
		// doc.addField("twtLocale", dataset.twtLocale);
		// doc.addField("twtCount", dataset.twtCount);
		// doc.addField("twtResultType", dataset.twtResultType);
		// doc.addField("twtUntil", dataset.twtUntil);
		// doc.addField("twtRatePercentage", dataset.twtRatePercentage);
		// doc.addField("twtLastSearchId", dataset.twtLastSearchId);
		// doc.addField("soCode", dataset.soCode);
		// doc.addField("soName", dataset.soName);
		// doc.addField("soType", dataset.soType);
		// doc.addField("soCategory", dataset.soCategory);
		// doc.addField("soDescription", dataset.soDescription);
		// doc.addField("jsonSo", dataset.jsonSo);
		// doc.addField("lat", dataset.lat);
		// doc.addField("lon", dataset.lon);
		// doc.addField("phenomenon", dataset. phenomenon );
		// doc.addField("geogeo", dataset. geogeo);

		doc.addField("jsonFields", dataset.getComponents());
		if (dataset.getComponents() != null) {
			List<Component> components = mapper.readValue(dataset.getComponents(), new TypeReference<List<Component>>() {
			});
			List<String> sdpComponentsName = new LinkedList<String>();
			for (Component component : components) {
				sdpComponentsName.add(component.getName());
			}
			doc.addField("sdpComponentsName", sdpComponentsName);
		}

		if (dataset.getDataSourceIsOpendata() == 1) {
			doc.addField("opendataAuthor", dataset.getDataSourceOpenDataAuthor());
			doc.addField("opendataLanguage", dataset.getDataSourceOpenDataLanguage());
			// doc.addField("opendataMetaUpdateDate", dataset.
			// opendataMetaUpdateDate );//FIXME manca il campo
			doc.addField("opendataUpdateDate", dataset.getDataSourceOpenDataUpdateDate());
			doc.addField("isOpendata", true);
		}

		doc.addField("registrationDate", formatDate(dataset.getDataSourceRegistrationDate()));
		doc.addField("importFileType", dataset.getImportfiletype());
		doc.addField("isCurrent", "1");
		doc.addField("externalReference", dataset.getDataSourceExternalReference());

		doc.addField("id", dataset.getDatasetcode());

		return doc;
	}

	private static String formatDate(Date date) {
		String formattedDate = null;
		if (date != null) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			formattedDate = df.format(date);
		}
		return formattedDate;
	}

	private void createKnoxClient() {
		logger.info("[SolrDelegate::createKnoxClient] START");

		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		if (solrUsername != null) {
			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(solrUsername, solrPassword);

			provider.setCredentials(AuthScope.ANY, credentials);
			clientBuilder.setDefaultCredentialsProvider(provider);

		}

		clientBuilder.setMaxConnTotal(128);

		try {
			solrClient = new TEHttpSolrClient(solrUrl);
		} catch (Exception e) {
			logger.error("[SolrDelegate::createKnoxClient] ERROR " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void createCloudClient() {
		logger.info("[SolrDelegate::createCloudClient] START");

		try {
			if (solrSecurityDomainName != null && solrSecurityDomainName.trim().length() > 0 && !(solrSecurityDomainName.equals("NO_SECURITY"))) {
				HttpClientUtil.setConfigurer(new Krb5HttpClientConfigurer(solrSecurityDomainName));
			}

			solrClient = new CloudSolrClient(solrUrl);
		} catch (Exception e) {
			logger.error("[SolrDelegate::createCloudClient] ERROR " + e.getMessage());
			e.printStackTrace();

		}
	}

	public class TEHttpSolrClient extends HttpSolrClient {

		private static final long serialVersionUID = 1L;
		private String defaultCollection = null;

		public void setDefaultCollection(String defaultCollection) {
			this.defaultCollection = defaultCollection;
		}

		private final String UTF_8 = StandardCharsets.UTF_8.name();

		public TEHttpSolrClient(String baseURL) {
			super(baseURL);
		}

		@Override
		public NamedList<Object> request(final SolrRequest request, String collection) throws SolrServerException, IOException {
			ResponseParser responseParser = request.getResponseParser();
			if (responseParser == null) {
				responseParser = this.parser;
			}

			if (collection == null && this.defaultCollection != null)
				collection = this.defaultCollection;
			return request(request, responseParser, collection);
		}

		public NamedList<Object> request(final SolrRequest request, final ResponseParser processor, String collection) throws SolrServerException, IOException {

			HttpRequestBase method = createMethod(request, collection);

			String userPass = solrUsername + ":" + solrPassword;
			String encoded = Base64.byteArrayToBase64(userPass.getBytes(UTF_8));
			// below line will make sure that it sends authorization token every
			// time in all your requests
			method.setHeader(new BasicHeader("Authorization", "Basic " + encoded));

			try {
				return executeMethod(method, processor);
			} catch (Exception e) {
				e.printStackTrace();
				throw new SolrServerException(e.getMessage());
			}

		}
	}

}
