package org.csi.yucca.dataservice.metadataapi.service;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatObject;
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

	// documantazione:
	// http://linee-guida-cataloghi-dati-profilo-dcat-ap-it.readthedocs.io/it/latest/index.html
	
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
			@QueryParam("includeSandbox") Boolean includeSandbox, @QueryParam("externalReference") String externalReference, @QueryParam("linkedData") Boolean linkedData)
			throws NumberFormatException, UnknownHostException {

		// SimpleDateFormat catalogDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");// dd/MM/yyyy

		if (linkedData == null)
			linkedData = false;
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

		Map<String, DCatObject> objectsMap = new HashMap<String, DCatObject>();

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
					dsDCAT.setId(metadataST.getDataset().getCode() + "_" + metadataST.getVersion());

					if (metadataST.getDcat() != null) {
						DCatAgent creator = new DCatAgent();
						if (metadataST.getDcat().getDcatCreatorName() != null) {
							if (DCatSdpHelper.isCSIAgent(metadataST.getDcat().getDcatCreatorName()))
								creator = DCatSdpHelper.getCSIAgentDcat();
							else {

								creator.setName(metadataST.getDcat().getDcatCreatorName());
								if (metadataST.getDcat().getDcatCreatorType() != null) {
									creator.addDcterms_type(new IdString(DCatSdpHelper.cleanForId(metadataST.getDcat().getDcatCreatorType())));
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
							}
						} else {
							creator = DCatSdpHelper.getCSIAgentDcat();
						}
						if (linkedData) {
							if (!objectsMap.containsKey(creator.getId()))
								objectsMap.put(creator.getId(), creator);
							DCatAgent empty = new DCatAgent();
							empty.cloneId(creator.getId(), true);
							dsDCAT.setCreator(empty);
						} else
							dsDCAT.setCreator(creator);

						DCatAgent rightsHolder = new DCatAgent();
						if (metadataST.getDcat().getDcatRightsHolderName() != null) {
							if (DCatSdpHelper.isCSIAgent(metadataST.getDcat().getDcatRightsHolderName()))
								rightsHolder = DCatSdpHelper.getCSIAgentDcat();
							else {

								rightsHolder.setName(metadataST.getDcat().getDcatRightsHolderName());
								if (metadataST.getDcat().getDcatRightsHolderType() != null) {
									rightsHolder.addDcterms_type(new IdString(DCatSdpHelper.cleanForId(metadataST.getDcat().getDcatRightsHolderType())));
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
							}
						} else {
							rightsHolder = DCatSdpHelper.getCSIAgentDcat();
						}

						if (linkedData) {
							if (!objectsMap.containsKey(rightsHolder.getId()))
								objectsMap.put(rightsHolder.getId(), rightsHolder);
							DCatAgent empty = new DCatAgent();
							empty.cloneId(rightsHolder.getId(), true);
							dsDCAT.setRightsHolder(empty);
						} else
							dsDCAT.setRightsHolder(rightsHolder);

						DCatVCard publisherVCard = new DCatVCard();
						publisherVCard.setHasEmail(new IdString("mailto:"+metadataST.getDcat().getDcatEmailOrg()));
						publisherVCard.setName(metadataST.getDcat().getDcatNomeOrg());
						
						if (linkedData) {
							if (!objectsMap.containsKey(publisherVCard.getId()))
								objectsMap.put(publisherVCard.getId(), publisherVCard);
							DCatVCard empty = new DCatVCard();
							empty.cloneId(publisherVCard.getId(), true);
							dsDCAT.setContactPoint(empty);
						} else
							dsDCAT.setContactPoint(publisherVCard);

						DCatAgent publisher = new DCatAgent();
						if (metadataST.getDcat().getDcatNomeOrg() != null) {
							if (DCatSdpHelper.isCSIAgent(metadataST.getDcat().getDcatNomeOrg()))
								publisher = DCatSdpHelper.getCSIAgentDcat();
							else {

								publisher.setName(metadataST.getDcat().getDcatNomeOrg());
								publisher.addDcterms_type(new IdString("http://purl.org/adms/publishertype/Company"));
								publisher.setId(metadataST.getDcat().getDcatNomeOrg());
								publisher.setDcterms_identifier(metadataST.getDcat().getDcatNomeOrg());
							}
						} else {
							publisher = DCatSdpHelper.getCSIAgentDcat();
						}
						if (linkedData) {
							if (!objectsMap.containsKey(publisher.getId()))
								objectsMap.put(publisher.getId(), publisher);
							DCatAgent empty = new DCatAgent();
							empty.cloneId(publisher.getId(), true);
							dsDCAT.setPublisher(empty);
						} else
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

					String dcatSubject = DCatSdpHelper.getDcatSubject(metadataST.getSubdomainCode());
					if (dcatSubject != null)
						dsDCAT.addSubTheme(new IdString(dcatSubject));

					DCatDistribution distribution = new DCatDistribution();
					distribution.setAccessURL(new IdString(cfg.getUserportalBaseUrl() + "#/dataexplorer/detail/" + metadataST.getTenantCode() + "/"
							+ metadataST.getDataset().getCode()));
					distribution.setDownloadURL(new IdString(cfg.getOauthBaseUrl() + "api/" + metadataST.getDataset().getCode() + "/download/"
							+ metadataST.getDataset().getDatasetId() + "/all"));

					// https://int-api.smartdatanet.it/api/Inputdataond_567/download/567/all
					// distr.getLicense().setName(metadataST.getLicense());
					DCatLicenseType licenseDistribution = new DCatLicenseType();
					if (metadataST.getLicense() != null) {

						if (metadataST.getLicense().startsWith("CC BY") || metadataST.getLicense().startsWith("CC-BY")) {
							licenseDistribution.setName("CC BY");
							String version = metadataST.getLicense().substring(5).trim();
							licenseDistribution.setDcterms_type(new IdString("http://purl.org/adms/licencetype/Attribution"));
							licenseDistribution.setVersion(version);
						} else if (metadataST.getLicense().startsWith("CC 0")) {
							licenseDistribution.setName("CC 0");
							String version = metadataST.getLicense().substring(4).trim();
							licenseDistribution.setDcterms_type(new IdString("http://purl.org/adms/licencetype/PublicDomain"));
							licenseDistribution.setVersion(version);
						} else {
							licenseDistribution.setName(metadataST.getLicense());
							licenseDistribution.setDcterms_type(new IdString("http://purl.org/adms/licencetype/UnknownIPR"));
						}
						if (linkedData) {
							if (!objectsMap.containsKey(licenseDistribution.getId()))
								objectsMap.put(licenseDistribution.getId(), licenseDistribution);
							DCatLicenseType empty = new DCatLicenseType();
							empty.cloneId(licenseDistribution.getId(), true);
							distribution.setLicense(empty);
						} else
							distribution.setLicense(licenseDistribution);
					}

					// distr.setIssued(new
					// DcatDate(metadataST.getRegistrationDate()));
					if (linkedData) {
						if (!objectsMap.containsKey(distribution.getId()))
							objectsMap.put(distribution.getId(), distribution);
						DCatDistribution empty = new DCatDistribution();
						empty.setFormat(null);
						empty.cloneId(distribution.getId(), true);
						dsDCAT.addDistribution(empty);
					} else
						dsDCAT.addDistribution(distribution);

					if (linkedData) {
						if (!objectsMap.containsKey(dsDCAT.getId()))
							objectsMap.put(dsDCAT.getId(), dsDCAT);
						DCatDataset empty = new DCatDataset();
						empty.cloneId(dsDCAT.getId(), true);
						dsDCAT.setCreator(null);
						dsDCAT.setDistributions(null);
						dsDCAT.setContactPoint(null);
						catalog.addDataset(empty);
					} else
						catalog.addDataset(dsDCAT);

					if (linkedData) {
						if (!objectsMap.containsKey(DCatSdpHelper.getCSIAgentDcat().getId()))
							objectsMap.put(DCatSdpHelper.getCSIAgentDcat().getId(), DCatSdpHelper.getCSIAgentDcat());
						DCatAgent empty = new DCatAgent();
						empty.cloneId(DCatSdpHelper.getCSIAgentDcat().getId(), true);
						dsDCAT.setPublisher(empty);
					} else
						catalog.setPublisher(DCatSdpHelper.getCSIAgentDcat());

				}
			}
		}
		DCatResult result = new DCatResult();
		result.addItem(catalog);
		if (linkedData) {
			for (String objectKey : objectsMap.keySet()) {
				result.addItem(objectsMap.get(objectKey));
			}

		}
		String json = gson.toJson(result);
		return Response.ok(json).build();
	}
}
