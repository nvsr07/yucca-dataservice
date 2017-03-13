package org.csi.yucca.dataservice.metadataapi.service;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.delegate.v01.metadata.MetadataDelegate;
import org.csi.yucca.dataservice.metadataapi.model.dcat.CatalogDCAT;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DatasetDCAT;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DistributionDCAT;
import org.csi.yucca.dataservice.metadataapi.model.dcat.LicenceTypeDCAT;
import org.csi.yucca.dataservice.metadataapi.model.dcat.VcardDCAT;
import org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.csi.yucca.dataservice.metadataapi.util.Constants;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

@Path("/dcat")
public class DcatService extends AbstractService {

	@Context
	ServletContext context;
	static Logger log = Logger.getLogger(DcatService.class);

	@GET
	@Path("/dataset_list")
	@Produces("application/ld+json; charset=UTF-8")
	public String searchDCAT(@Context HttpServletRequest request, @QueryParam("q") String q, @QueryParam("page") Integer page, @QueryParam("tenant") String tenant,
			@QueryParam("domain") String domain, @QueryParam("opendata") Boolean opendata, @QueryParam("geolocalized") Boolean geolocalized, @QueryParam("minLat") Double minLat,
			@QueryParam("minLon") Double minLon, @QueryParam("maxLat") Double maxLat, @QueryParam("maxLon") Double maxLon, @QueryParam("lang") String lang)
			throws NumberFormatException, UnknownHostException {

		SimpleDateFormat catalogDateFormat = new SimpleDateFormat("yyyy-MM-dd");// dd/MM/yyyy

		CatalogDCAT catalog = new CatalogDCAT();
		catalog.setDescription("Catalogo Smart Data Piemonte");
		catalog.setTitle("CATALOGO SMART DATA");
		catalog.setHomepage("http://userportal.smartdatanet.it");
		catalog.setModified(catalogDateFormat.format(new Date()));

		LicenceTypeDCAT lic = new LicenceTypeDCAT();
		lic.setLicenseType("http://purl.org/adms/licencetype/PublicDomain");
		catalog.setLicense(lic);

		if (page == null)
			page = 1;

		String userAuth = (String) request.getSession().getAttribute("userAuth");

		Integer numElementForPage = 10;
		Integer end = page * numElementForPage;
		Integer start = (end - numElementForPage) + 1;

		log.info("[DcatService::searchDCAT] numElementForPage: " + numElementForPage + ", end: " + end + ", start: " + start);
		log.info("[DcatService::searchDCAT] query: " + q);

		List<Metadata> metadataList = MetadataDelegate.getInstance().search(userAuth, q, start, end, tenant, domain, opendata, geolocalized, minLat, minLon, maxLat, maxLon, lang,
				true);

		Gson gson = JSonHelper.getInstance();
		Config cfg = Config.getInstance();

		for (Metadata metadata : metadataList) {
			if (metadata.getDataset() != null && metadata.getDataset().getDatasetId() != null) {

				Metadata metadataST = gson.fromJson(MetadataDelegate.getInstance().loadMetadata(userAuth, metadata.getCode() + "_odata", null, Constants.OUTPUT_FORMAT_JSON, lang),
						Metadata.class);

				DatasetDCAT dsDCAT = new DatasetDCAT();

				if (metadataST.getDcatCreatorName() != null) {
					dsDCAT.getCreator().setName(metadataST.getDcatCreatorName());
				} else {
					dsDCAT.getCreator().setName("CSI PIEMONTE");
				}

				if (metadataST.getDcatCreatorType() != null) {
					dsDCAT.getCreator().setType(metadataST.getDcatCreatorType());
				} else {
					dsDCAT.getCreator().setType("http://purl.org/adms/publishertype/Company");
				}

				if (metadataST.getDcatCreatorId() != null) {
					dsDCAT.getCreator().setId(metadataST.getDcatCreatorId());
				} else {
					dsDCAT.getCreator().setId("01995120019");
				}

				if (metadataST.getDcatRightsHolderName() != null) {
					dsDCAT.getRightsHolder().setName(metadataST.getDcatRightsHolderName());
				} else {
					dsDCAT.getRightsHolder().setName("CSI PIEMONTE");
				}

				if (metadataST.getDcatRightsHolderType() != null) {
					dsDCAT.getRightsHolder().setType(metadataST.getDcatRightsHolderType());
				} else {
					dsDCAT.getRightsHolder().setType("http://purl.org/adms/publishertype/Company");
				}

				if (metadataST.getDcatRightsHolderId() != null) {
					dsDCAT.getRightsHolder().setId(metadataST.getDcatRightsHolderId());
				} else {
					dsDCAT.getRightsHolder().setId("01995120019");
				}

				dsDCAT.setDescription(metadata.getDescription());
				dsDCAT.setTitle(metadata.getName());
				// V01 - fixed value
				// http://publications.europa.eu/resource/authority/frequency/UNKNOWN

				dsDCAT.setAccrualPeriodicity("http://publications.europa.eu/resource/authority/frequency/UNKNOWN");
				// dsDCAT.setAccrualPeriodicity(metadata.getFps());

				// String keyWords = "";
				if (metadata.getTags() != null) {
					for (String tag : metadata.getTags()) {
						dsDCAT.addKeyword(tag);
					}
				}
				dsDCAT.setTheme(metadata.getDomain());
				dsDCAT.setAccessRights(metadata.getVisibility());
				dsDCAT.setIdentifier(metadata.getCode() + "_" + metadata.getVersion());
				if (metadata.getOpendata() != null)
					dsDCAT.setModified(metadata.getOpendata().getDataUpdateDate());
				dsDCAT.setVersionInfo(metadata.getVersion());
				dsDCAT.setSubTheme(metadataST.getCodsubdomain());

				DistributionDCAT distr = new DistributionDCAT();
				distr.setAccessURL(cfg.getUserportalBaseUrl() + "#/dataexplorer/dataset/" + metadata.getTenantCode() + "/" + metadata.getCode());
				distr.setDownloadURL(cfg.getOauthBaseUrl() + "api/" + metadata.getCode() + "/download/" + metadataST.getDataset().getDatasetId() + "/all");

				// https://int-api.smartdatanet.it/api/Inputdataond_567/download/567/all
				// distr.getLicense().setName(metadata.getLicense());
				LicenceTypeDCAT licDist = new LicenceTypeDCAT();
				if (metadata.getLicense() != null) {

					if (metadata.getLicense().equals("CC BY 4.0")) {
						licDist.setName("CC BY");
						licDist.setType("https://creativecommons.org/licenses/by/4.0/");
						licDist.setLicenseType("http://purl.org/adms/licencetype/Attribution");
						licDist.setVersion("4.0");
					} else if (metadata.getLicense().equals("CC 0 1.0")) {
						licDist.setName("CC 0");
						licDist.setType("https://creativecommons.org/publicdomain/zero/1.0/");
						licDist.setLicenseType("http://purl.org/adms/licencetype/PublicDomain");
						licDist.setVersion("1.0");
					} else {
						licDist.setName(metadata.getLicense());
					}
					distr.setLicense(licDist);
				}

				distr.setIssued(metadata.getRegistrationDate());
				dsDCAT.addDistribution(distr);

				VcardDCAT publisher = new VcardDCAT();
				publisher.setHasEmail(metadataST.getDcatEmailOrg());
				publisher.setHasTelephone(metadataST.getDcatEmailOrg());
				dsDCAT.setContactPoint(publisher);

				catalog.getDataset().add(dsDCAT);
			}
		}
		String json = gson.toJson(catalog).replaceAll("\"context\"", "\"@context\"").replaceAll("\"id\"", "\"@id\"").replaceAll("\"type\"", "\"@type\"");
		return json;
	}
}
