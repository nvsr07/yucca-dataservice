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
import org.csi.yucca.adminapi.model.ComponentJson;
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.Dettaglio;
import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.LicenseJson;
import org.csi.yucca.adminapi.model.SharingTenantsJson;
import org.csi.yucca.adminapi.model.TagJson;
import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;
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

	public static final String createIdForStream(DettaglioStream dettaglioStream){
		return 	dettaglioStream.getTenantCode() + "_" + dettaglioStream.getSharingTenant() + "_" + dettaglioStream.getStreamcode();
	}

	public void addDocument(DettaglioDataset dettaglioDataset) throws Exception {
		SolrInputDocument doc = createSolrDocument(dettaglioDataset);
		addDocument(doc);
	}

	public void addDocument(DettaglioStream dettaglioStream, DettaglioSmartobject dettaglioSmartobject, Dataset dataset) throws Exception {
		SolrInputDocument doc = createSolrDocument(dettaglioStream, dettaglioSmartobject, dataset);
		addDocument(doc);
	}

	public void removeDocument(String documentId) throws Exception {
		solrClient.deleteById(solrCollection, documentId);
		solrClient.commit();
	}

	private SolrInputDocument createSolrDocument(DettaglioDataset dataset) throws Exception {
		SolrInputDocument doc = createSolrDocumentFromDettaglio(dataset);

		doc.addField("id", dataset.getDatasetcode());
		doc.addField("entityType", new ArrayList<String>(Arrays.asList("dataset")));
		doc.addField("name", dataset.getDatasetname());
		doc.addField("datasetCode", dataset.getDatasetcode());
		doc.addField("datasetDescription", dataset.getDescription());
		doc.addField("datasetType", dataset.getDatasetType());
		doc.addField("datasetSubtype", dataset.getDatasetSubtype());
		doc.addField("importFileType", dataset.getImportfiletype());
		
		//Componenti
		if (dataset.getComponents() != null) {
			List<String> sdpComponentsName = new LinkedList<String>();
			List<String> phenomenonList = new LinkedList<String>();
			List<SolrDatasetComponent> solrDatasetComponents = new LinkedList<SolrDatasetComponent>();
			for (ComponentJson component : dataset.getComponents()) {
				sdpComponentsName.add(component.getName());
				phenomenonList.add(component.getPhenomenonname());
				
				SolrDatasetComponent datasetComponent= new SolrDatasetComponent();
				datasetComponent.setFieldName(component.getName());
				datasetComponent.setFieldAlias(component.getAlias());
				datasetComponent.setDataType(component.getDatatypedescription());
				datasetComponent.setSourceColumn(component.getSourcecolumn());
				datasetComponent.setIsKey(component.getIskey());
				datasetComponent.setMeasureUnit(component.getMeasureunit());
				datasetComponent.setMeasureUnitCategory(component.getMeasureunitcategory());
				datasetComponent.setOrder(component.getInorder());
				solrDatasetComponents.add(datasetComponent);				
			}
			doc.addField("sdpComponentsName", sdpComponentsName);
			doc.addField("phenomenon", phenomenonList);			
		
			String componentJsonElement = mapper.writeValueAsString(solrDatasetComponents);
			doc.addField("jsonFields", componentJsonElement);
			
			//String componentJsonElement = "{\"element\":"+mapper.writeValueAsString(dataset.getComponents())+"}";
			
			//String componentJsonElement = "[{";
			//for (SolrDatasetComponent component : solrDatasetComponents) {
			//	componentJsonElement+=mapper.writeValueAsString(component);			
			//}
			//componentJsonElement+="]}";
			
			logger.info("[SolrDelegate::createSolrDocumentFromDettaglio] componentJsonElement: " + componentJsonElement);

			//doc.addField("jsonFields", componentJsonElement);
		}

		return doc;
	}

	private SolrInputDocument createSolrDocument(DettaglioStream stream, DettaglioSmartobject smartobject, Dataset dataset) throws Exception {
		SolrInputDocument doc = createSolrDocumentFromDettaglio(stream);
		List<String> entityTypes = new LinkedList<String>();
		entityTypes.add("stream");
		
		if (stream.getSavedata() == 1) {
			entityTypes.add("dataset");
			doc.addField("datasetCode", dataset.getDatasetcode());
			String namespace = Dettaglio.generateNameSpace(stream.getTenantCode(), dataset.getDatasetcode());
		}
		
		doc.addField("id", createIdForStream(stream));
		doc.addField("name", stream.getStreamname());
		doc.addField("entityType", entityTypes);
		doc.addField("streamCode", stream.getStreamcode());
		doc.addField("twtQuery", stream.getTwtquery());
		doc.addField("twtGeolocLat", stream.getTwtgeoloclat());
		doc.addField("twtGeolocLon", stream.getTwtgeoloclon());
		doc.addField("twtGeolocRadius", stream.getTwtgeolocradius());
		doc.addField("twtGeolocUnit", stream.getTwtgeolocunit());
		doc.addField("twtLang", stream.getTwtlang());
		doc.addField("twtLocale", stream.getTwtlocale());
		doc.addField("twtCount", stream.getTwtcount());
		doc.addField("twtResultType", stream.getTwtresulttype());
		doc.addField("twtUntil", stream.getTwtuntil());
		doc.addField("twtRatePercentage", stream.getTwtratepercentage());
		doc.addField("twtLastSearchId", stream.getTwtlastsearchid());
		doc.addField("soCode", smartobject.getSocode());
		doc.addField("soName", smartobject.getName());
		doc.addField("soType", smartobject.getSotypecode());
		doc.addField("soCategory", stream.getSmartObjectCategoryDescription());
		doc.addField("soDescription", stream.getSmartObjectDescription());

		if (smartobject.getLat() != null && smartobject.getLon() != null) {
			doc.addField("lat", smartobject.getLat());
			doc.addField("lon", smartobject.getLon());
			if (smartobject.getLat() >= -90 && smartobject.getLat() <= 90 && smartobject.getLat() >= -180 && smartobject.getLon() <= 180)
				doc.addField("geogeo", smartobject.getLat() + "," + smartobject.getLon());

			String jsonSo = "{\"position\":[{";
			if (smartobject.getLat() != null)
				jsonSo += "\"lat\":" + smartobject.getLat() + ",";
			if (smartobject.getLon() != null)
				jsonSo += "\"lon\":" + smartobject.getLon() + ",";
			if (smartobject.getElevation() != null)
				jsonSo += "\"elevation\":" + smartobject.getElevation() + ",";
			if (smartobject.getFloor() != null)
				jsonSo += "\"floor\":" + smartobject.getFloor() + ",";
			if (smartobject.getBuilding() != null)
				jsonSo += "\"building\":\"" + smartobject.getBuilding() + "\",";
			if (smartobject.getRoom() != null)
				jsonSo += "\"room\":\"" + smartobject.getRoom() + "\",";

			if (jsonSo.endsWith(","))
				jsonSo = jsonSo.substring(0, jsonSo.length() - 1);

			jsonSo += "}]}";
			doc.addField("jsonSo", jsonSo);
			
			//Componenti
			if (stream.getComponents() != null) {
				List<SolrStreamComponent> solrStreamComponents = new LinkedList<SolrStreamComponent>();
				for (ComponentJson component : stream.getComponents()) {					
					SolrStreamComponent streamComponent= new SolrStreamComponent();
					streamComponent.setIdComponent(component.getId_component().toString());
					streamComponent.setComponentName(component.getName());
					streamComponent.setComponentAlias(component.getAlias());
					streamComponent.setDataType(component.getDatatypedescription());
					streamComponent.setTolerance(component.getTolerance());
					streamComponent.setMeasureUnit(component.getMeasureunit());
					streamComponent.setMeasureUnitCategory(component.getMeasureunitcategory());
					streamComponent.setPhenomenon(component.getPhenomenonname());
					streamComponent.setPhenomenonCategory(component.getPhenomenoncetegory());
					streamComponent.setSinceVersion(component.getSince_version().toString());
					solrStreamComponents.add(streamComponent);				
				}	
			
				//String componentJsonElement = "{\"element\":"+mapper.writeValueAsString(dataset.getComponents())+"}";
				
				String componentJsonElement = "{\"element\":"+mapper.writeValueAsString(solrStreamComponents)+"}";
//				for (SolrStreamComponent component : solrStreamComponents) {
//					componentJsonElement+=mapper.writeValueAsString(component);			
//				}
//				componentJsonElement+="}";
				logger.info("[SolrDelegate::createSolrDocumentFromDettaglio] componentJsonElement: " + componentJsonElement);

				doc.addField("jsonFields", componentJsonElement);
			}

		}

		return doc;
	}

	private SolrInputDocument createSolrDocumentFromDettaglio(Dettaglio dataset) throws Exception {
		SolrInputDocument doc = new SolrInputDocument();

		doc.addField("version", dataset.getDatasourceversion());
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
		if(dataset.getLicense()!=null) {
			try {
				LicenseJson license = mapper.readValue(dataset.getLicense(), LicenseJson.class);
				doc.addField("licenseCode", license.getLicensecode());
				doc.addField("licenceDescription", license.getDescription());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("[SolrDelegate::createSolrDocumentFromDettaglio] ERROR on lincese: " + e.getMessage());
			}
		}
		doc.addField("tenantCode", dataset.getTenantCode());
		doc.addField("tenantName", dataset.getTenantName());
		doc.addField("tenantDescription", dataset.getTenantDescription());

		if (dataset.getSharingTenant() != null) {
			List<SharingTenantsJson> tenants = mapper.readValue(dataset.getSharingTenant(), new TypeReference<List<SharingTenantsJson>>() {
			});
			List<String> tenantsCode = new LinkedList<String>();
			for (SharingTenantsJson tenant : tenants) {
				tenantsCode.add(tenant.getTenantcode());
			}
			doc.addField("tenantsCode", tenantsCode);
		}

		if (dataset.getTags() != null) {
			List<TagJson> tags = mapper.readValue(dataset.getTags(), new TypeReference<List<TagJson>>() {
			});
			List<String> tagCode = new LinkedList<String>();
			List<String> tagLangEN = new LinkedList<String>();
			List<String> tagLangIT = new LinkedList<String>();
			for (TagJson tag : tags) {
				tagCode.add(tag.getTagcode());
				tagLangEN.add(tag.getLangen());
				tagLangIT.add(tag.getLangit());
			}

			doc.addField("tagCode", tagCode);
			doc.addField("tagLangIT", tagLangIT);
			doc.addField("tagLangEN", tagLangEN);
		}

		if (dataset.getDcat() != null) {
			//DcatJson dcat = Util.getFromJsonString(dataset.getDcat(), DcatJson.class);
			doc.addField("dcatDataUpdate", dataset.getDcat().getDcatdataupdate());
			doc.addField("dcatNomeOrg", dataset.getDcat().getDcatnomeorg());
			doc.addField("dcatEmailOrg", dataset.getDcat().getDcatemailorg());
			doc.addField("dcatCreatorName", dataset.getDcat().getDcatcreatorname());
			doc.addField("dcatCreatorType", dataset.getDcat().getDcatcreatortype());
			doc.addField("dcatCreatorId", dataset.getDcat().getDcatcreatorid());
			doc.addField("dcatRightsHolderName", dataset.getDcat().getDcatrightsholdername());
			doc.addField("dcatRightsHolderType", dataset.getDcat().getDcatrightsholdertype());
			doc.addField("dcatRightsHolderId", dataset.getDcat().getDcatrightsholderid());
			doc.addField("dcatReady", dataset.getDcat().getDcatready());
		}

		
		//doc.addField("jsonFields", dataset.getComponents());
		//List<ComponentJson> jsonFields = new LinkedList<ComponentJson>();
		

		if (dataset.getDataSourceIsopendata() == 1) {
			doc.addField("opendataAuthor", dataset.getDataSourceOpenDataAuthor());
			doc.addField("opendataLanguage", dataset.getDataSourceOpenDataLanguage());
			// doc.addField("opendataMetaUpdateDate",
			// dataset.opendataMetaUpdateDate );//FIXME manca il campo
			doc.addField("opendataUpdateDate", dataset.getDataSourceOpenDataUpdateDate());
			doc.addField("isOpendata", true);
		}

		doc.addField("registrationDate", formatDate(dataset.getDataSourceRegistrationDate()));
		doc.addField("isCurrent", "1");
		doc.addField("externalReference", dataset.getDataSourceExternalReference());

		return doc;
	}
	
	/*public static void main(String[] args) {
		List<ComponentJson> jsonFields = new LinkedList<ComponentJson>();
		
		ComponentJson component1 = new  ComponentJson();
		component1.setAlias("ciao");

		try {
			ComponentJson component2 = new  ComponentJson();
			component1.setAlias("ciao2");
			jsonFields.add(component1);
			jsonFields.add(component2);
			
			StringWriter sw = new StringWriter();
			mapper.writer().writeValue(sw, jsonFields);
			String componentJsonElement = "{\"element\":"+sw.toString()+"}";
			System.out.println("result " + componentJsonElement);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/

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
