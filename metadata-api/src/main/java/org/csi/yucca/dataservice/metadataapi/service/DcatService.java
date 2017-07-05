package org.csi.yucca.dataservice.metadataapi.service;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata.MetadataDelegate;
import org.csi.yucca.dataservice.metadataapi.exception.UserWebServiceException;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatAgent;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatCatalog;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatDataset;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatDate;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatDistribution;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatResult;
import org.csi.yucca.dataservice.metadataapi.model.dcat.I18NString;
import org.csi.yucca.dataservice.metadataapi.model.dcat.IdString;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatLicenseType;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatVCard;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.Result;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.Metadata;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.csi.yucca.dataservice.metadataapi.util.DCatSdpHelper;
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
	public Response searchDCAT(@Context HttpServletRequest request, @QueryParam("q") String q, @QueryParam("page") Integer page, @QueryParam("start") Integer start,
			@QueryParam("rows") Integer rows, @QueryParam("tenant") String tenant, @QueryParam("organization") String organization, @QueryParam("domain") String domain,
			@QueryParam("subdomain") String subdomain, @QueryParam("opendata") Boolean opendata, @QueryParam("geolocalized") Boolean geolocalized,
			@QueryParam("minLat") Double minLat, @QueryParam("minLon") Double minLon, @QueryParam("maxLat") Double maxLat, @QueryParam("maxLon") Double maxLon,
			@QueryParam("lang") String lang, @QueryParam("tags") String tags, @QueryParam("visibility") String visibility, @QueryParam("isSearchExact") Boolean isSearchExact,
			@QueryParam("includeSandbox") Boolean includeSandbox, @QueryParam("externalReference") String externalReference) throws NumberFormatException, UnknownHostException {

		// SimpleDateFormat catalogDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");// dd/MM/yyyy

		DCatCatalog catalog = new DCatCatalog();
		catalog.setDescription_it(new I18NString("it", "Catalogo Smart Data Piemonte"));
		catalog.setTitle_it(new I18NString("it", "CATALOGO SMART DATA"));
		// catalog.setDescription_en(new I18NString("en",
		// "Smart Data Piemonte Catalog"));
		// catalog.setTitle_en(new I18NString("en", "SMART DATA CATALOG"));

		catalog.setModified(new DCatDate(new Date()));
		catalog.setHomepage(new IdString("http://userportal.smartdatanet.it"));

		// LicenceTypeDCAT licenseType = new LicenceTypeDCAT();
		// licenseType.setType("http://purl.org/adms/licencetype/PublicDomain");
		// catalog.setLicense(licenseType);

		if (page == null)
			page = 1;
		if (rows == null)
			rows = 10;

		// Integer end = page * rows;

		if (start == null)
			start = (page * rows - rows);

		log.info("[DcatService::searchDCAT] start: " + start + ", rows: " + rows);
		log.info("[DcatService::searchDCAT] query: " + q);

		Result searchResult;
		try {
			searchResult = MetadataDelegate.getInstance().search(request, q, start, rows, null, tenant, organization, domain, subdomain, opendata, geolocalized, minLat, minLon,
					maxLat, maxLon, lang, true, null, true, null, tags, visibility, isSearchExact, includeSandbox, externalReference);
		} catch (UnsupportedEncodingException e) {
			return Response.ok(new ErrorResponse("", "Invalid param").toJson()).build();
		} catch (UserWebServiceException e) {
			return e.getResponse();
		} catch (Exception e) {
			log.error("[MetadataService::getStream]" + e.getMessage(), e);
			return Response.serverError().build();
		}

		Gson gson = JSonHelper.getInstanceDcat();

		Config cfg = Config.getInstance();

		if (searchResult != null && searchResult.getMetadata() != null) {

			for (Metadata metadataST : searchResult.getMetadata()) {
				if (metadataST.getDataset() != null && metadataST.getDataset().getDatasetId() != null) {

					DCatDataset dsDCAT = new DCatDataset();

					if (metadataST.getDcat() != null) {
						DCatAgent creator = new DCatAgent();
						if (metadataST.getDcat().getDcatCreatorName() != null) {
							creator.setName(metadataST.getDcat().getDcatCreatorName());
							if (metadataST.getDcat().getDcatCreatorType() != null) {
								creator.addType(DCatSdpHelper.cleanForId(metadataST.getDcat().getDcatCreatorType()));
							} else {
								creator.addDcterms_type(new IdString("http://purl.org/adms/publishertype/Company"));
							}
							if (metadataST.getDcat().getDcatCreatorId() != null) {
								creator.setId(metadataST.getDcat().getDcatCreatorId());
								creator.setDcterms_identifier(metadataST.getDcat().getDcatCreatorId());
							} else {
								creator.setId(metadataST.getDcat().getDcatCreatorName());
								creator.setDcterms_identifier(metadataST.getDcat().getDcatCreatorName());
							}
						} else {
							creator = DCatSdpHelper.getCSIAgentDcat();
						}

						dsDCAT.setCreator(creator);

						DCatAgent rightsHolder = new DCatAgent();
						if (metadataST.getDcat().getDcatRightsHolderName() != null) {
							rightsHolder.setName(metadataST.getDcat().getDcatRightsHolderName());
							if (metadataST.getDcat().getDcatRightsHolderType() != null) {
								rightsHolder.addType(DCatSdpHelper.cleanForId(metadataST.getDcat().getDcatRightsHolderType()));
							} else {
								rightsHolder.addDcterms_type(new IdString("http://purl.org/adms/publishertype/Company"));
							}

							if (metadataST.getDcat().getDcatRightsHolderId() != null) {
								rightsHolder.setId(metadataST.getDcat().getDcatRightsHolderId());
								rightsHolder.setDcterms_identifier(metadataST.getDcat().getDcatRightsHolderId());
							} else {
								rightsHolder.setId(metadataST.getDcat().getDcatRightsHolderName());
								rightsHolder.setDcterms_identifier(metadataST.getDcat().getDcatRightsHolderName());
							}

						} else {
							rightsHolder = DCatSdpHelper.getCSIAgentDcat();
						}

						dsDCAT.setRightsHolder(rightsHolder);

						DCatVCard publisherVCard = new DCatVCard();
						publisherVCard.setHasEmail(new IdString(metadataST.getDcat().getDcatEmailOrg()));
						publisherVCard.setName(metadataST.getDcat().getDcatNomeOrg());
						dsDCAT.setContactPoint(publisherVCard);

						DCatAgent publisher = new DCatAgent();
						if (metadataST.getDcat().getDcatNomeOrg() != null) {
							publisher.setName(metadataST.getDcat().getDcatNomeOrg());
							publisher.addDcterms_type(new IdString("http://purl.org/adms/publishertype/Company"));
							publisher.setId(metadataST.getDcat().getDcatNomeOrg());
							publisher.setDcterms_identifier(metadataST.getDcat().getDcatNomeOrg());

						} else {
							publisher = DCatSdpHelper.getCSIAgentDcat();
						}

						dsDCAT.setPublisher(publisher);

					}
					dsDCAT.setDescription(new I18NString("it", metadataST.getDescription()));
					dsDCAT.setTitle(new I18NString("it", metadataST.getName()));
					// V01 - fixed value
					// http://publications.europa.eu/resource/authority/frequency/UNKNOWN

					dsDCAT.setAccrualPeriodicity(new IdString("http://publications.europa.eu/resource/authority/frequency/UNKNOWN"));
					// dsDCAT.setAccrualPeriodicity(metadata.getFps());

					// String keyWords = "";
					if (metadataST.getTags() != null) {
						for (String tag : metadataST.getTags()) {
							dsDCAT.addKeyword(tag);
						}
					}
					dsDCAT.setTheme(DCatSdpHelper.getDcatTheme(metadataST.getDomainCode()));
					// dsDCAT.setAccessRights(metadataST.getVisibility());
					dsDCAT.setIdentifier(metadataST.getDataset().getCode() + "_" + metadataST.getVersion());
					if (metadataST.getOpendata() != null && metadataST.getOpendata().getDataUpdateDate() != null)
						dsDCAT.setModified(new DCatDate(metadataST.getOpendata().getDataUpdateDate()));

					dsDCAT.setVersionInfo(metadataST.getVersion());

					// dsDCAT.addSubTheme(new
					// IdString(metadataST.getSubdomainCode()));

					DCatDistribution distribution = new DCatDistribution();
					distribution.setAccessURL(new IdString(cfg.getUserportalBaseUrl() + "#/dataexplorer/dataset/" + metadataST.getTenantCode() + "/"
							+ metadataST.getDataset().getCode()));
					distribution.setDownloadURL(new IdString(cfg.getOauthBaseUrl() + "api/" + metadataST.getDataset().getCode() + "/download/"
							+ metadataST.getDataset().getDatasetId() + "/all"));

					// https://int-api.smartdatanet.it/api/Inputdataond_567/download/567/all
					// distr.getLicense().setName(metadataST.getLicense());
					DCatLicenseType licenseDistribution = new DCatLicenseType();
					if (metadataST.getLicense() != null) {

						if (metadataST.getLicense().startsWith("CC BY")) {
							licenseDistribution.setName("CC BY");
							String version = metadataST.getLicense().substring(metadataST.getLicense().lastIndexOf(" ") + 1);
							// licenseDistribution.addType("https://creativecommons.org/licenses/by/"
							// + version + "/");
							licenseDistribution.setDcterms_type(new IdString("http://purl.org/adms/licencetype/Attribution"));
							licenseDistribution.setVersion(version);
						} else if (metadataST.getLicense().startsWith("CC 0")) {
							licenseDistribution.setName("CC 0");
							String version = metadataST.getLicense().substring(metadataST.getLicense().lastIndexOf(" ") + 1);
							// licenseDistribution.addType("https://creativecommons.org/publicdomain/zero/"
							// + version + "/");
							licenseDistribution.setDcterms_type(new IdString("http://purl.org/adms/licencetype/PublicDomain"));
							licenseDistribution.setVersion(version);
						} else {
							licenseDistribution.setName(metadataST.getLicense());
							licenseDistribution.setDcterms_type(new IdString("http://purl.org/adms/licencetype/UnknownIPR"));
						}
						distribution.setLicense(licenseDistribution);
					}

					// distr.setIssued(new
					// DcatDate(metadataST.getRegistrationDate()));
					dsDCAT.addDistribution(distribution);
					catalog.addDataset(dsDCAT);
					catalog.setPublisher(DCatSdpHelper.getCSIAgentDcat());

				}
			}
		}
		DCatResult result = new DCatResult();
		result.addItem(catalog);
		String json = gson.toJson(result);
		return Response.ok(json).build();
	}
}
