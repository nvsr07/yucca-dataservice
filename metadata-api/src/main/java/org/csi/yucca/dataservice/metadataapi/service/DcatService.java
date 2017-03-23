package org.csi.yucca.dataservice.metadataapi.service;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata.MetadataDelegate;
import org.csi.yucca.dataservice.metadataapi.exception.UserWebServiceException;
import org.csi.yucca.dataservice.metadataapi.model.dcat.CatalogDCAT;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DatasetDCAT;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DistributionDCAT;
import org.csi.yucca.dataservice.metadataapi.model.dcat.LicenceTypeDCAT;
import org.csi.yucca.dataservice.metadataapi.model.dcat.VcardDCAT;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.Result;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.Metadata;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;
import org.csi.yucca.dataservice.metadataapi.util.Config;
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
	public Response searchDCAT(@Context HttpServletRequest request, @QueryParam("q") String q, @QueryParam("page") Integer page, 
			 @QueryParam("tenant") String tenant, @QueryParam("organization") String organization, @QueryParam("domain") String domain,
				@QueryParam("subdomain") String subdomain, @QueryParam("opendata") Boolean opendata, @QueryParam("geolocalized") Boolean geolocalized,
				@QueryParam("minLat") Double minLat, @QueryParam("minLon") Double minLon, @QueryParam("maxLat") Double maxLat, @QueryParam("maxLon") Double maxLon,
				@QueryParam("lang") String lang, @QueryParam("tags") String tags, @QueryParam("visibility") String visibility,@QueryParam("isSearchExact") Boolean isSearchExact,
				@QueryParam("includeSandbox") Boolean includeSandbox)
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


		Integer numElementForPage = 10;
		Integer end = page * numElementForPage;
		Integer start = (end - numElementForPage) + 1;

		log.info("[DcatService::searchDCAT] numElementForPage: " + numElementForPage + ", end: " + end + ", start: " + start);
		log.info("[DcatService::searchDCAT] query: " + q);

		
		Result searchResult;
		try {
			searchResult = MetadataDelegate.getInstance()
					.search(
					request, q, start, numElementForPage, null, tenant, organization, domain, subdomain, opendata, geolocalized, minLat, minLon, maxLat, maxLon, lang,
					true,null,true,null, tags, visibility, isSearchExact, includeSandbox);
		} catch (UnsupportedEncodingException e) {
			return Response.ok(new ErrorResponse("", "Invalid param").toJson()).build();
		} catch (UserWebServiceException e) {
			return e.getResponse();
		}

		Gson gson = JSonHelper.getInstance();
		Config cfg = Config.getInstance();

		if (searchResult!=null && searchResult.getMetadata()!=null)
		{
		
			for (Metadata metadataST : searchResult.getMetadata()) {
				if (metadataST.getDataset() != null && metadataST.getDataset().getDatasetId() != null) {
	
					DatasetDCAT dsDCAT = new DatasetDCAT();
					
					if (metadataST.getDcat()!=null)
					{
						if (metadataST.getDcat().getDcatCreatorName() != null) {
							dsDCAT.getCreator().setName(metadataST.getDcat().getDcatCreatorName());
						} else {
							dsDCAT.getCreator().setName("CSI PIEMONTE");
						}
		
						if (metadataST.getDcat().getDcatCreatorType() != null) {
							dsDCAT.getCreator().setType(metadataST.getDcat().getDcatCreatorType());
						} else {
							dsDCAT.getCreator().setType("http://purl.org/adms/publishertype/Company");
						}
		
						if (metadataST.getDcat().getDcatCreatorId() != null) {
							dsDCAT.getCreator().setId(metadataST.getDcat().getDcatCreatorId());
						} else {
							dsDCAT.getCreator().setId("01995120019");
						}
		
						if (metadataST.getDcat().getDcatRightsHolderName() != null) {
							dsDCAT.getRightsHolder().setName(metadataST.getDcat().getDcatRightsHolderName());
						} else {
							dsDCAT.getRightsHolder().setName("CSI PIEMONTE");
						}
		
						if (metadataST.getDcat().getDcatRightsHolderType() != null) {
							dsDCAT.getRightsHolder().setType(metadataST.getDcat().getDcatRightsHolderType());
						} else {
							dsDCAT.getRightsHolder().setType("http://purl.org/adms/publishertype/Company");
						}
		
						if (metadataST.getDcat().getDcatRightsHolderId() != null) {
							dsDCAT.getRightsHolder().setId(metadataST.getDcat().getDcatRightsHolderId());
						} else {
							dsDCAT.getRightsHolder().setId("01995120019");
						}

						VcardDCAT publisher = new VcardDCAT();
						publisher.setHasEmail(metadataST.getDcat().getDcatEmailOrg());
						publisher.setOrganizationName(metadataST.getDcat().getDcatNomeOrg());
						dsDCAT.setContactPoint(publisher);

					}
					dsDCAT.setDescription(metadataST.getDescription());
					dsDCAT.setTitle(metadataST.getName());
					// V01 - fixed value
					// http://publications.europa.eu/resource/authority/frequency/UNKNOWN
	
					dsDCAT.setAccrualPeriodicity("http://publications.europa.eu/resource/authority/frequency/UNKNOWN");
					// dsDCAT.setAccrualPeriodicity(metadata.getFps());
	
					// String keyWords = "";
					if (metadataST.getTags() != null) {
						for (String tag : metadataST.getTags()) {
							dsDCAT.addKeyword(tag);
						}
					}
					dsDCAT.setTheme(metadataST.getDomain());
					dsDCAT.setAccessRights(metadataST.getVisibility());
					dsDCAT.setIdentifier(metadataST.getDataset().getCode() + "_" + metadataST.getVersion());
					if (metadataST.getOpendata() != null)
						dsDCAT.setModified(metadataST.getOpendata().getDataUpdateDate());
					dsDCAT.setVersionInfo(metadataST.getVersion());
					dsDCAT.setSubTheme(metadataST.getSubdomainCode());
	
					DistributionDCAT distr = new DistributionDCAT();
					distr.setAccessURL(cfg.getUserportalBaseUrl() + "#/dataexplorer/dataset/" + metadataST.getTenantCode() + "/" + metadataST.getDataset().getCode());
					distr.setDownloadURL(cfg.getOauthBaseUrl() + "api/" + metadataST.getDataset().getCode() + "/download/" + metadataST.getDataset().getDatasetId() + "/all");
	
					// https://int-api.smartdatanet.it/api/Inputdataond_567/download/567/all
					// distr.getLicense().setName(metadataST.getLicense());
					LicenceTypeDCAT licDist = new LicenceTypeDCAT();
					if (metadataST.getLicense() != null) {
	
						if (metadataST.getLicense().equals("CC BY 4.0")) {
							licDist.setName("CC BY");
							licDist.setType("https://creativecommons.org/licenses/by/4.0/");
							licDist.setLicenseType("http://purl.org/adms/licencetype/Attribution");
							licDist.setVersion("4.0");
						} else if (metadataST.getLicense().equals("CC 0 1.0")) {
							licDist.setName("CC 0");
							licDist.setType("https://creativecommons.org/publicdomain/zero/1.0/");
							licDist.setLicenseType("http://purl.org/adms/licencetype/PublicDomain");
							licDist.setVersion("1.0");
						} else {
							licDist.setName(metadataST.getLicense());
							
						}
						distr.setLicense(licDist);
					}
	
					distr.setIssued(metadataST.getRegistrationDate());
					dsDCAT.addDistribution(distr);
					catalog.getDataset().add(dsDCAT);
				}
			}
		}
		String json = gson.toJson(catalog).replaceAll("\"context\"", "\"@context\"").replaceAll("\"id\"", "\"@id\"").replaceAll("\"type\"", "\"@type\"");
		return Response.ok(json).build();
	}
}
