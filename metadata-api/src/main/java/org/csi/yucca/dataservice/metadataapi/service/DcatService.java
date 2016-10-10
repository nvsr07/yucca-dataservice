package org.csi.yucca.dataservice.metadataapi.service;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.model.dcat.AgentDCAT;
import org.csi.yucca.dataservice.metadataapi.model.dcat.CatalogDCAT;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DatasetDCAT;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DistributionDCAT;
import org.csi.yucca.dataservice.metadataapi.model.dcat.LicenceTypeDCAT;
import org.csi.yucca.dataservice.metadataapi.model.dcat.VCardTypeDCAT;
import org.csi.yucca.dataservice.metadataapi.model.dcat.VcardDCAT;
import org.csi.yucca.dataservice.metadataapi.model.output.Metadata;
import org.csi.yucca.dataservice.metadataapi.model.store.output.doc.DCAT;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.csi.yucca.dataservice.metadataapi.util.Constants;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.github.jsonldjava.core.JsonLdOptions;
import com.google.gson.Gson;

@Path("/dcat")
public class DcatService extends AbstractService {

	@Context
	ServletContext context;
	static Logger log = Logger.getLogger(DcatService.class);

	@GET
	@Path("/dataset_list")
	@Produces("'application/ld+json; charset=UTF-8")
	public String searchCkan(@Context HttpServletRequest request, @QueryParam("q") String q,
			@QueryParam("page") Integer page, @QueryParam("tenant") String tenant,
			@QueryParam("domain") String domain, @QueryParam("opendata") Boolean opendata,
			@QueryParam("geolocalized") Boolean geolocalized, @QueryParam("minLat") Double minLat,
			@QueryParam("minLon") Double minLon, @QueryParam("maxLat") Double maxLat,
			@QueryParam("maxLon") Double maxLon, @QueryParam("lang") String lang)
			throws NumberFormatException, UnknownHostException {

		CatalogDCAT catalog = new CatalogDCAT();
		catalog.setDescription("Catalogo Start Data Piemonte");
		catalog.setTitle("CATALOGO SMART DATA");
		catalog.setHomepage("http://userportal.smartdatanet.it");

		AgentDCAT agentCatalog = new AgentDCAT();
		agentCatalog.setName("CSI PIEMONTE"); 
		
		if (page == null)
			page = 1;

		String userAuth = (String) request.getSession().getAttribute("userAuth");
		
		Integer numElementForPage = 10;
		Integer end = page * numElementForPage;
		Integer start = (end - numElementForPage) + 1;

		List<Metadata> metadataList = search(userAuth, q, start, end, tenant, domain, opendata, geolocalized, minLat,
				minLon, maxLat, maxLon, lang, true);

		Gson gson = JSonHelper.getInstance();
		Config cfg = Config.getInstance();

		for (Metadata metadata : metadataList) {
			if (metadata.getDataset() != null && metadata.getDataset().getDatasetId() != null) {

				Metadata metadataST = gson.fromJson(
						loadMetadata(userAuth, metadata.getCode() + "_odata", null, Constants.OUTPUT_FORMAT_JSON, lang),
						Metadata.class);

				//if (metadataST.getDcat().isDcatReady()) {
					DatasetDCAT dsDCAT = new DatasetDCAT();

					VCardTypeDCAT type = new VCardTypeDCAT();
					dsDCAT.setContactPoint(type.getjson());
					
					dsDCAT.setDescription(metadata.getDescription());
					dsDCAT.setTitle(metadata.getName());
					String keyWords = "";
					if (metadata.getTags() != null) {
						for (String tag : metadata.getTags()) {
							dsDCAT.addKeyword(tag);
						}
					}
					dsDCAT.setTheme(metadata.getDomain());
					dsDCAT.setAccessRights(metadata.getVisibility());
					dsDCAT.setAccrualPeriodicity(metadata.getFps());
					dsDCAT.setIdentifier(metadata.getCode() + "_" + metadata.getVersion());
					if (metadata.getOpendata() != null)
						dsDCAT.setModified(metadata.getOpendata().getDataUpdateDate());
					dsDCAT.setVersionInfo(metadata.getVersion());
					dsDCAT.setSubTheme(metadataST.getCodsubdomain());

					DistributionDCAT distr = new DistributionDCAT();
					distr.setAccessURL(cfg.getUserportalBaseUrl() + "#/dataexplorer/dataset/" + metadata.getTenantCode()
							+ "/" + metadata.getCode());
					distr.setDownloadURL(cfg.getOauthBaseUrl() + "api/" + metadata.getCode()
							+ "/download/" + metadataST.getDataset().getDatasetId() + "/all");
					
					//https://int-api.smartdatanet.it/api/Inputdataond_567/download/567/all
					distr.setLicense(metadata.getLicense());
					distr.setIssued(metadata.getRegistrationDate());
					dsDCAT.addDistribution(distr);

					VcardDCAT publisher = new VcardDCAT();
					publisher.setHasEmail(metadataST.getDcat().getEmailOrg());
					publisher.setHasTelephone(metadataST.getDcat().getTelOrg());
					publisher.setHasURL(metadataST.getDcat().getUrlOrg());
					publisher.setOrganizationName(metadataST.getDcat().getNomeOrg());
					dsDCAT.setPublisher(publisher);

					LicenceTypeDCAT lic = new LicenceTypeDCAT();
					catalog.setLicense(lic.getjson());

					catalog.getDataset().add(dsDCAT);
				//}
			}
		}
		String json = gson.toJson(catalog)
				.replaceAll("\"context\"", "\"@context\"")
				.replaceAll("\"id\"", "\"@id\"")
				.replaceAll("\"type\"", "\"@type\"");
		return json;
	}
}
