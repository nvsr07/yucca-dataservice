package org.csi.yucca.adminapi.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotAcceptableException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.exception.UnauthorizedException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.mapper.ApiMapper;
import org.csi.yucca.adminapi.mapper.BundlesMapper;
import org.csi.yucca.adminapi.mapper.ComponentMapper;
import org.csi.yucca.adminapi.mapper.DataSourceMapper;
import org.csi.yucca.adminapi.mapper.DatasetMapper;
import org.csi.yucca.adminapi.mapper.DcatMapper;
import org.csi.yucca.adminapi.mapper.LicenseMapper;
import org.csi.yucca.adminapi.mapper.OrganizationMapper;
import org.csi.yucca.adminapi.mapper.SequenceMapper;
import org.csi.yucca.adminapi.mapper.SmartobjectMapper;
import org.csi.yucca.adminapi.mapper.StreamMapper;
import org.csi.yucca.adminapi.mapper.SubdomainMapper;
import org.csi.yucca.adminapi.mapper.TenantMapper;
import org.csi.yucca.adminapi.model.Api;
import org.csi.yucca.adminapi.model.Bundles;
import org.csi.yucca.adminapi.model.Component;
import org.csi.yucca.adminapi.model.DataSource;
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.Dcat;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.License;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.model.Smartobject;
import org.csi.yucca.adminapi.model.Stream;
import org.csi.yucca.adminapi.model.StreamInternal;
import org.csi.yucca.adminapi.model.StreamToUpdate;
import org.csi.yucca.adminapi.model.Subdomain;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.model.TenantDataSource;
import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;
import org.csi.yucca.adminapi.request.ComponentRequest;
import org.csi.yucca.adminapi.request.DcatRequest;
import org.csi.yucca.adminapi.request.InternalStreamRequest;
import org.csi.yucca.adminapi.request.LicenseRequest;
import org.csi.yucca.adminapi.request.OpenDataRequest;
import org.csi.yucca.adminapi.request.PostStreamRequest;
import org.csi.yucca.adminapi.request.SharingTenantRequest;
import org.csi.yucca.adminapi.request.StreamRequest;
import org.csi.yucca.adminapi.request.TwitterInfoRequest;
import org.csi.yucca.adminapi.response.DettaglioStreamResponse;
import org.csi.yucca.adminapi.response.ListStreamResponse;
import org.csi.yucca.adminapi.response.PostStreamResponse;
import org.csi.yucca.adminapi.service.StreamService;
import org.csi.yucca.adminapi.util.Constants;
import org.csi.yucca.adminapi.util.DataOption;
import org.csi.yucca.adminapi.util.DatasetSubtype;
import org.csi.yucca.adminapi.util.DatasetType;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.ManageOption;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.csi.yucca.adminapi.util.Status;
import org.csi.yucca.adminapi.util.StreamVisibility;
import org.csi.yucca.adminapi.util.Type;
import org.csi.yucca.adminapi.util.Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class StreamServiceImpl implements StreamService {

	public static final Integer DATASOURCE_VERSION = 1;
	public static final Integer SINCE_VERSION = 1;
	public static final Integer TENANT_DATA_SERVICE_DATA_OPTIONS = 3;
	public static final Integer TENANT_DATA_SERVICE_MANAGE_OPTIONS = 2;
	public static final String STREAM_DATASET_PREFIX_CODE = "ds_";
	public static final String API_SUBTYPE_WEBSOCKET = "websocket";
	public static final String API_SUBTYPE_MQTT = "mqtt";
	public static final String API_SUBTYPE_ODATA = "odata";
	public static final String API_CODE_PREFIX_WEBSOCKET = "ws_";
	public static final String API_CODE_PREFIX_MQTT = "mqtt_";

	@Autowired
	private SequenceMapper sequenceMapper;

	@Autowired
	private SubdomainMapper subdomainMapper;

	@Autowired
	private ApiMapper apiMapper;

	@Autowired
	private DatasetMapper datasetMapper;

	@Autowired
	private SmartobjectMapper smartobjectMapper;

	@Autowired
	private TenantMapper tenantMapper;

	@Autowired
	private BundlesMapper bundlesMapper;

	@Autowired
	private StreamMapper streamMapper;

	@Autowired
	private DcatMapper dcatMapper;

	@Autowired
	private LicenseMapper licenseMapper;

	@Autowired
	private OrganizationMapper organizationMapper;

	@Autowired
	private DataSourceMapper dataSourceMapper;

	@Autowired
	private ComponentMapper componentMapper;
	
    /**
     * update stream
     */
	@Override
	public ServiceResponse updateStream(String organizationCode, String soCode, Integer idStream, StreamRequest streamRequest, 
			String tenantCodeManager, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {
		
		// streamToUpdate
		StreamToUpdate streamToUpdate =	streamMapper.selectStreamToUpdate(tenantCodeManager, idStream, organizationCode, ServiceUtil.getTenantCodeListFromUser(authorizedUser));
		ServiceUtil.checkIfFoundRecord(streamToUpdate);

		// smartobject
		Smartobject smartObject = smartobjectMapper.selectSmartobjectBySocodeAndOrgcode(soCode, organizationCode);
		ServiceUtil.checkIfFoundRecord(smartObject,"smartobject not found socode [" + soCode + "], organizationcode [" + organizationCode + "] ");

		// validation
		updateValidation(smartObject, streamToUpdate, streamRequest);
		
		// update
		updateStreamTransaction(streamRequest, streamToUpdate, smartObject);
		
		return ServiceResponse.build().NO_CONTENT();
	}
	
	/**
	 * select stream
	 */
	@Override
	public ServiceResponse selectStream(String organizationCode, Integer idStream, String tenantCodeManager, JwtUser authorizedUser) 
			throws BadRequestException, NotFoundException, Exception {

		DettaglioStream dettaglioStream = streamMapper.selectStream(tenantCodeManager, idStream, organizationCode, 
				ServiceUtil.getTenantCodeListFromUser(authorizedUser));

		ServiceUtil.checkIfFoundRecord(dettaglioStream);

		DettaglioSmartobject dettaglioSmartobject = smartobjectMapper.selectSmartobjectByOrganizationAndTenant(dettaglioStream.getSmartObjectCode(), 
				organizationCode, ServiceUtil.getTenantCodeListFromUser(authorizedUser)).get(0);
		
		List<DettaglioStream> listInternalStream = streamMapper.selectInternalStream( dettaglioStream.getIdDataSource(), dettaglioStream.getDataSourceVersion() );
		   
		DettaglioStreamResponse response = new DettaglioStreamResponse(dettaglioStream, dettaglioSmartobject, listInternalStream);
		
		return ServiceUtil.buildResponse(response);
	}
	
	/**
	 * 
	 */
	@Override
	public byte[] selectStreamIcon(String organizationCode, Integer idStream, JwtUser authorizedUser) 
			throws BadRequestException, NotFoundException, Exception {

		DettaglioStream dettaglioStream = streamMapper.selectStream(null, idStream, organizationCode, 
				ServiceUtil.getTenantCodeListFromUser(authorizedUser));

		ServiceUtil.checkIfFoundRecord(dettaglioStream);

		return Util.convertIconFromDBToByte(dettaglioStream.getDataSourceIcon());
	}

	
	/**
	 * 
	 */
	@Override
	public ServiceResponse selectStreams(String organizationCode, String tenantCodeManager, String sort, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, UnauthorizedException, Exception {
		
		checkAuthTenant(tenantCodeManager, authorizedUser);
		
		List<String> sortList = ServiceUtil.getSortList(sort, DettaglioStream.class);
		
		List<DettaglioStream> list = streamMapper.selectStreams(tenantCodeManager, organizationCode, sortList, ServiceUtil.getTenantCodeListFromUser(authorizedUser));
		
		ServiceUtil.checkList(list);
		
		return ServiceUtil.buildResponse(buildSelectStreamsResponse(list));
	}

	/**
	 * 
	 */
	@Override
	public ServiceResponse createStreamDataset(PostStreamRequest request, String organizationCode, String soCode,
			JwtUser authorizedUser) throws BadRequestException, NotFoundException, UnauthorizedException, Exception {

		Organization organization = organizationMapper.selectOrganizationByCode(organizationCode);
		ServiceUtil.checkIfFoundRecord(organization);

		Smartobject smartobject = smartobjectMapper.selectSmartobjectBySocodeAndOrgcode(soCode, organizationCode);
		ServiceUtil.checkIfFoundRecord(smartobject,
				"smartobject not found socode [" + soCode + "], organizationcode [" + organizationCode + "] ");

		validation(request, organizationCode, smartobject, authorizedUser);

		Stream stream = insertStreamTransaction(request, organization, smartobject);

		return ServiceResponse.build().object(PostStreamResponse.build(stream.getIdstream())
				.streamcode(stream.getStreamcode()).streamname(stream.getStreamname()));
	}

	/**
	 * 
	 * @param idDataSource
	 * @param version
	 * @return
	 */
	private List<Component> getAlreadyPresentComponentsPreviousVersion(Integer idDataSource, Integer version){
		
		if(idDataSource == null || version == null){
			return null;
		}
		
		if(version > 1){
			return componentMapper.selectComponentByDataSourceAndVersion(idDataSource,  (version-1) );
		}
		
		return null;
	}	
	
	/**
	 * 
	 * @param tenantCodeManager
	 * @param authorizedUser
	 * @throws UnauthorizedException
	 */
	private void checkAuthTenant(String tenantCodeManager, JwtUser authorizedUser) throws UnauthorizedException {
		if (tenantCodeManager != null) {
			ServiceUtil.checkAuthTenant(authorizedUser, tenantCodeManager);
		}
	}

	/**
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	private List<ListStreamResponse> buildSelectStreamsResponse(List<DettaglioStream> list) throws Exception{
		List<ListStreamResponse> responseList = new ArrayList<ListStreamResponse>();
		for (DettaglioStream dettaglioStream : list) {
			responseList.add(new ListStreamResponse(dettaglioStream));
		}
		return responseList;
	}
	
	/**
	 * 
	 * @param request
	 * @param idDataSource
	 */
	private void insertTags(PostStreamRequest request, Integer idDataSource) {
		for (Integer idTag : request.getTags()) {
			dataSourceMapper.insertTagDataSource(idDataSource, 1, idTag);
		}
	}

	/**
	 * 
	 * @param request
	 * @param smartobject
	 */
	private void insertComponents(PostStreamRequest request, Smartobject smartobject, Integer idDataSource) {

		if (Type.FEED_TWEET.id() != smartobject.getIdSoType()) {

			for (ComponentRequest componentRequest : request.getComponents()) {
				Component component = new Component();
				BeanUtils.copyProperties(componentRequest, component);
				component.setIdDataSource(idDataSource);
				component.setDatasourceversion(DATASOURCE_VERSION);
				component.setSinceVersion(SINCE_VERSION);
				component.setIskey(Util.booleanToInt(false));
				componentMapper.insertComponent(component);
			}
		}
	}

	/**
	 * 
	 * @param request
	 * @param smartobject
	 */
	private void insertTweetComponents(PostStreamRequest request, Smartobject smartobject) {
		if (Type.FEED_TWEET.id() == smartobject.getIdSoType()) {
			for (Component tweetComponent : Constants.TWEET_COMPONENTS) {
				componentMapper.insertComponent(tweetComponent);
			}
		}
	}

	/**
	 * 
	 * @param request
	 * @param organization
	 * @param smartobject
	 */
	private Stream insertStreamTransaction(PostStreamRequest request, Organization organization, Smartobject smartobject) {

		Timestamp now = Util.getNow();

		Long idDcat = insertDcat(request.getDcat());

		Integer idLicense = insertLicense(request.getLicense());

		Integer idDataSource = insertDataSource(request, organization.getIdOrganization(), idDcat, idLicense);

		Stream stream = insertStream(request, idDataSource, smartobject.getIdSmartObject());

		insertTags(request, idDataSource);

		insertComponents(request, smartobject, idDataSource);

		insertTweetComponents(request, smartobject);

		insertTenantDataSource(request, idDataSource, now);

		insertSharingTenants(request, idDataSource, now);

		insertStreamInternal(request, idDataSource);

		Dataset dataset = insertDataset(request, idDataSource, smartobject.getIdSoType(), DATASOURCE_VERSION, request.getStreamcode());

		insertApi(request, smartobject, dataset, idDataSource, request.getStreamcode(), DATASOURCE_VERSION);

		return stream;

	}

	/**
	 * 
	 * @param request
	 * @param smartobject
	 * @param dataset
	 * @param idDataSource
	 */
	private void insertApi(StreamRequest request, Smartobject smartobject, Dataset dataset, Integer idDataSource, String streamCode, Integer dataSourceVersion) {
		apiMapper.insertApi(Api.buildOutput(dataSourceVersion)
				.apicode(API_CODE_PREFIX_WEBSOCKET + smartobject.getSocode() + streamCode)
				.apiname(request.getStreamname()).apisubtype(API_SUBTYPE_WEBSOCKET).idDataSource(idDataSource));

		apiMapper.insertApi(Api.buildOutput(dataSourceVersion)
				.apicode(API_CODE_PREFIX_MQTT + smartobject.getSocode() + streamCode)
				.apiname(request.getStreamname()).apisubtype(API_SUBTYPE_MQTT).idDataSource(idDataSource));

		if (request.getSavedata() && dataset != null) {
			apiMapper.insertApi(Api.buildOutput(dataSourceVersion).apicode(dataset.getDatasetcode())
					.apiname(dataset.getDatasetname()).apisubtype(API_SUBTYPE_ODATA).idDataSource(idDataSource));
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param idDataSource
	 * @param idSoType
	 * @param dataSourceVersion
	 * @param streamCode
	 * @return
	 */
	private Dataset insertDataset(StreamRequest request, Integer idDataSource, Integer idSoType, Integer dataSourceVersion, String streamCode) {
		
		Dataset checkDataSet = datasetMapper.selectDataSet(idDataSource, dataSourceVersion);
		if (checkDataSet != null) {
			return checkDataSet;
		}
		
		if (request.getSavedata()) {
			Integer iddataset = sequenceMapper.selectDatasetSequence();

			Dataset dataset = new Dataset();

			dataset.setIddataset(iddataset);
			dataset.setIdDataSource(idDataSource);
			dataset.setDatasourceversion(dataSourceVersion);
			dataset.setDatasetname(streamCode);
			dataset.setDatasetcode(generateStreamDatasetCode(iddataset, streamCode));
			dataset.setDescription("Dataset " + streamCode);
			dataset.setIdDatasetType(DatasetType.DATASET.id());
			if (Type.FEED_TWEET.id() == idSoType) {
				dataset.setIdDatasetSubtype(DatasetSubtype.SOCIAL.id());
			} else {
				dataset.setIdDatasetSubtype(DatasetSubtype.STREAM.id());
			}
			dataset.setAvailablespeed(Util.booleanToInt(true));
			dataset.setIstransformed(Util.booleanToInt(false));

			datasetMapper.insertDataset(dataset);

			return dataset;
		}
		
		return null;
	}

	
	/**
	 * 
	 * @param iddataset
	 * @param streamcode
	 * @return
	 */
	private String generateStreamDatasetCode(Integer iddataset, String streamcode) {
		return STREAM_DATASET_PREFIX_CODE + Util.cleanStringCamelCase(streamcode) + "_" + iddataset;
	}

	/**
	 * 
	 * @param request
	 * @param idDataSource
	 */
	private void insertStreamInternal(PostStreamRequest request, Integer idDataSource) {
		if (request.getInternalStreams() != null) {
			for (InternalStreamRequest internalStreamRequest : request.getInternalStreams()) {
				StreamInternal streamInternal = new StreamInternal();
				streamInternal.setDatasourceversioninternal(DATASOURCE_VERSION);
				streamInternal.setIdDataSourceinternal(idDataSource);
				streamInternal.setIdstream(internalStreamRequest.getIdStream());
				streamInternal.setStreamAlias(internalStreamRequest.getStreamAlias());
				streamMapper.insertStreamInternal(streamInternal);
			}
		}
	}

	/**
	 * 
	 * @param request
	 * @param idDataSource
	 * @param now
	 */
	private void insertSharingTenants(PostStreamRequest request, Integer idDataSource, Timestamp now) {
		if (request.getSharingTenants() != null) {
			for (SharingTenantRequest sharingTenantRequest : request.getSharingTenants()) {
				TenantDataSource tenantDataSource = new TenantDataSource();
				tenantDataSource.setIdDataSource(idDataSource);
				tenantDataSource.setDatasourceversion(DATASOURCE_VERSION);
				tenantDataSource.setIdTenant(sharingTenantRequest.getIdTenant());
				tenantDataSource.setIsactive(Util.booleanToInt(true));
				tenantDataSource.setIsmanager(Util.booleanToInt(false));
				tenantDataSource.setActivationdate(now);
				tenantDataSource.setManagerfrom(now);
				tenantDataSource.setDataoptions(sharingTenantRequest.getDataOptions());
				tenantDataSource.setManageoptions(sharingTenantRequest.getManageOptions());
				tenantMapper.insertTenantDataSource(tenantDataSource);
			}
		}
	}

	/**
	 * 
	 * @param request
	 * @param idDataSource
	 * @param now
	 */
	private void insertTenantDataSource(PostStreamRequest request, Integer idDataSource, Timestamp now) {
		TenantDataSource tenantDataSource = new TenantDataSource();
		tenantDataSource.setIdDataSource(idDataSource);
		tenantDataSource.setDatasourceversion(DATASOURCE_VERSION);
		tenantDataSource.setIdTenant(request.getIdTenant());
		tenantDataSource.setIsactive(Util.booleanToInt(true));
		tenantDataSource.setIsmanager(Util.booleanToInt(true));
		tenantDataSource.setActivationdate(now);
		tenantDataSource.setManagerfrom(now);
		tenantDataSource.setDataoptions(TENANT_DATA_SERVICE_DATA_OPTIONS);
		tenantDataSource.setManageoptions(TENANT_DATA_SERVICE_MANAGE_OPTIONS);
		tenantMapper.insertTenantDataSource(tenantDataSource);
	}

	/**
	 * 
	 * @param request
	 * @param idDataSource
	 * @param idSmartobject
	 * @return
	 */
	private Stream insertStream(PostStreamRequest request, Integer idDataSource, Integer idSmartobject) {
		Stream stream = new Stream();
		stream.setIdDataSource(idDataSource);
		stream.setDatasourceversion(1);
		stream.setStreamcode(request.getStreamcode());
		stream.setStreamname(request.getStreamname());
		stream.setPublishstream(Util.booleanToInt(true));
		stream.setSavedata(Util.booleanToInt(request.getSavedata()));
		stream.setFps(request.getFps());
		stream.setInternalquery(request.getInternalquery());
		stream.setTwtquery(
				request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtquery() : null);
		stream.setTwtgeoloclat(
				request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtgeoloclat() : null);
		stream.setTwtgeoloclon(
				request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtgeoloclon() : null);
		stream.setTwtgeolocradius(
				request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtgeolocradius() : null);
		stream.setTwtgeolocunit(
				request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtgeolocunit() : null);
		stream.setTwtlang(
				request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtlang() : null);
		stream.setTwtlocale(
				request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtlocale() : null);
		stream.setTwtcount(
				request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtcount() : null);
		stream.setTwtresulttype(
				request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtresulttype() : null);
		stream.setTwtuntil(
				request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtuntil() : null);
		stream.setTwtratepercentage(request.getTwitterInfoRequest() != null
				? request.getTwitterInfoRequest().getTwtratepercentage() : null);
		stream.setTwtlastsearchid(
				request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtlastsearchid() : null);
		stream.setTwtquery(
				request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtquery() : null);
		stream.setIdSmartObject(idSmartobject);

		streamMapper.insertStream(stream);

		return stream;
	}

	/**
	 * 
	 * @param request
	 * @param smartobject
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	private void validation(PostStreamRequest request, String organizationCode, Smartobject smartobject,
			JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {

		Tenant tenant = checkTenant(request.getIdTenant(), organizationCode);

		ServiceUtil.checkAuthTenant(authorizedUser, tenant.getTenantcode());

		checkMandatories(request);

		checkSubdomain(request.getIdSubdomain());

		checkOpendataupdatedate(request);

		checkMaxNumStream(request.getIdTenant());

		checkStreamCode(request.getStreamcode(), smartobject.getIdSmartObject());

		checkInternalSmartObject(request, smartobject.getIdSoType());
		
		checkFeedTweetSmartobject(request, smartobject.getIdSoType());

		checkComponents(request, smartobject.getIdSoType());
		
		checkVisibility(request);
	}

	/**
	 * 
	 * @param request
	 * @param idOrganization
	 * @param idDcat
	 * @param idLicense
	 * @return
	 */
	private Integer insertDataSource(PostStreamRequest request, Integer idOrganization, Long idDcat,
			Integer idLicense) {
		DataSource dataSource = new DataSource();
		dataSource.setDatasourceversion(1);
		dataSource.setIscurrent(1);
		dataSource.setUnpublished(Util.booleanToInt(request.getUnpublished()));
		dataSource.setName(request.getName());
		dataSource.setVisibility(request.getVisibility());
		dataSource.setCopyright(request.getVisibility()); // potrebbe essere
															// nullo
		dataSource.setDisclaimer(request.getDisclaimer());
		dataSource.setRegistrationdate(Util.getNow());
		dataSource.setRequestername(request.getRequestername());
		dataSource.setRequestersurname(request.getRequestersurname());
		dataSource.setRequestermail(request.getRequestermail());
		dataSource.setPrivacyacceptance(Util.booleanToInt(true));
		dataSource.setIcon(request.getIcon()); // potrebbe essere nullo
		dataSource.setIsopendata(request.getOpenData() != null ? Util.booleanToInt(true) : Util.booleanToInt(false));
		dataSource.setOpendataexternalreference(
				request.getOpenData() != null ? request.getOpenData().getOpendataexternalreference() : null);
		dataSource.setOpendataauthor(request.getOpenData() != null ? request.getOpenData().getOpendataauthor() : null);
		dataSource.setOpendataupdatedate(request.getOpenData() != null
				? Util.dateStringToTimestamp(request.getOpenData().getOpendataupdatedate()) : null);
		dataSource.setOpendatalanguage(
				request.getOpenData() != null ? request.getOpenData().getOpendatalanguage() : null);
		dataSource.setLastupdate(request.getOpenData() != null ? request.getOpenData().getLastupdate() : null);
		dataSource.setIdOrganization(idOrganization);
		dataSource.setIdSubdomain(request.getIdSubdomain());
		dataSource.setIdDcat(idDcat);
		dataSource.setIdLicense(idLicense);
		dataSource.setIdStatus(Status.DRAFT.id());
		dataSourceMapper.insertDataSource(dataSource);
		return dataSource.getIdDataSource();
	}

	/**
	 * 
	 * @param dcatRequest
	 * @return
	 */
	private Long insertDcat(DcatRequest dcatRequest) {

		if (dcatRequest == null)
			return null;

		// ----------------------------------------------------------------------------------
		// INSERT DCAT
		// yucca_dcat --> inserimento record solo se non è stato indicato il
		// campo dcat.idDcat.
		// Bisogna inserire i campi nei nomi corrispondenti.
		// ----------------------------------------------------------------------------------
		if (dcatRequest.getIdDcat() == null) {
			Dcat dcat = new Dcat();
			BeanUtils.copyProperties(dcatRequest, dcat);
			dcatMapper.insertDcat(dcat);
			return dcat.getIdDcat();
		}

		return dcatRequest.getIdDcat();
	}

	/**
	 * 
	 * @param licenseRequest
	 * @return
	 */
	private Integer insertLicense(LicenseRequest licenseRequest) {

		if (licenseRequest == null) {
			return null;
		}

		// ----------------------------------------------------------------------------------
		// INSERT LICENSE
		// yucca_d_license --> inserimento record solo se non è stato indicato
		// il campo license.idLicense
		// Bisogna inserire i campi nei nomi corrispondenti.
		// ----------------------------------------------------------------------------------
		if (licenseRequest != null && licenseRequest.getIdLicense() == null) {
			License license = new License();
			BeanUtils.copyProperties(licenseRequest, license);
			licenseMapper.insertLicense(license);
			return license.getIdLicense();
		}

		return licenseRequest.getIdLicense();

	}

	/**
	 * 
	 * @param request
	 * @param idSoType
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkInternalSmartObject(StreamRequest request, Integer idSoType) throws BadRequestException, NotFoundException {
		checkInternalSmartObject(request.getInternalquery(), request.getInternalStreams(), idSoType);
	}
	
	/**
	 * 
	 * @param request
	 * @param idSoType
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkInternalSmartObject(String internalQuery, List<InternalStreamRequest> internalStreams, Integer idSoType) throws BadRequestException, NotFoundException {
		// INTERNAL SO TYPE
		if (Type.INTERNAL.id() == idSoType) {
			ServiceUtil.checkMandatoryParameter(internalQuery, "internalquery mandatory (only for internal smartobject)");
			ServiceUtil.checkList(internalStreams, "InternalStreams mandatory (only for internal smartobject)");
			for (InternalStreamRequest internalStream : internalStreams) {
				ServiceUtil.checkMandatoryParameter(internalStream.getIdStream(), "internalStream => idStream");
				ServiceUtil.checkMandatoryParameter(internalStream.getStreamAlias(), "internalStream => streamAlias");
			}
		}

		// NOT INTERNAL SO TYPE
		if (Type.INTERNAL.id() != idSoType && internalQuery != null) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, "internalquery: is not internal smartobject.");
		}
	}

	/**
	 * 
	 * @param request
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkMandatories(PostStreamRequest request) throws BadRequestException, NotFoundException {
		ServiceUtil.checkMandatoryParameter(request.getStreamname(), "streamname");
		ServiceUtil.checkMandatoryParameter(request.getName(), "name");
		ServiceUtil.checkMandatoryParameter(request.getSavedata(), "savedata");
		ServiceUtil.checkMandatoryParameter(request.getVisibility(), "visibility");
		ServiceUtil.checkMandatoryParameter(request.getRequestername(), "requestername");
		ServiceUtil.checkMandatoryParameter(request.getRequestersurname(), "requestersurname");
		ServiceUtil.checkMandatoryParameter(request.getRequestermail(), "requestermail");
		ServiceUtil.checkMandatoryParameter(request.getUnpublished(), "unpublished");
		ServiceUtil.checkList(request.getTags(), "tags");
		
		checkDcat(request);

		checkLicense(request.getLicense());
		
	}

	/**
	 * 
	 * @param dcatRequest
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkDcat(StreamRequest request) throws BadRequestException, NotFoundException {
		if (request.getDcat()!= null && request.getDcat().getIdDcat() == null) {
			ServiceUtil.checkMandatoryParameter(request.getDcat().getDcatnomeorg(), "dcatnomeorg");
			ServiceUtil.checkMandatoryParameter(request.getDcat().getDcatemailorg(), "dcatemailorg");
			ServiceUtil.checkMandatoryParameter(request.getDcat().getDcatrightsholdername(), "dcatrightsholdername");
		}
	}
	
	/**
	 * 
	 * @param request
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkLicense(StreamRequest request) throws BadRequestException, NotFoundException{
		checkLicense(request.getLicense());
	}
	
	/**
	 * 
	 * @param licenseRequest
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkLicense(LicenseRequest licenseRequest) throws BadRequestException, NotFoundException{
		if (licenseRequest != null && licenseRequest.getIdLicense() == null) {
			ServiceUtil.checkMandatoryParameter(licenseRequest.getLicensecode(), "licensecode");
			ServiceUtil.checkMandatoryParameter(licenseRequest.getDescription(), "license => description");
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param idSoType
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkFeedTweetSmartobject(StreamRequest request, Integer idSoType) throws BadRequestException, NotFoundException {
		checkFeedTweetSmartobject(request.getTwitterInfoRequest(), request.getComponents(), idSoType);		
	}
	
	/**
	 * 
	 * @param request
	 * @param idSoType
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkFeedTweetSmartobject(TwitterInfoRequest twitterInfoRequest, List<ComponentRequest> components, Integer idSoType) throws BadRequestException, NotFoundException {

		if (Type.FEED_TWEET.id() == idSoType) {
			ServiceUtil.checkMandatoryParameter(twitterInfoRequest, "twitterInfo mandatory (only for Feed Tweet smartobject)");
			ServiceUtil.checkMandatoryParameter(twitterInfoRequest.getTwtquery(), "twitterInfo => Twtquery");
			if (components != null) {
				throw new BadRequestException(Errors.INCORRECT_VALUE, "Component not allowed for Feed Tweet smartobject!");
			}
		}

		if (Type.FEED_TWEET.id() != idSoType && twitterInfoRequest != null) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, "TwitterInfo: is not feed tweet smartobject.");
		}
	}

	/**
	 * 
	 * @param request
	 * @param idSoType
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	private void checkComponents(StreamRequest request, Integer idSoType) throws NotFoundException, BadRequestException {
		checkComponents(request, idSoType, null, null);
	}
	
	/**
	 * 
	 * @param listToCheck
	 * @param component
	 * @return
	 */
	private boolean doesNotContainComponent(List<Component> listToCheck, Integer idComponent){
		for (Component component : listToCheck) {
			if (component.getIdComponent() == idComponent) {
				return false;
			}
		}
		return true;
	}
	
	private boolean doesNotContainComponent(List<ComponentRequest> listToCheck, String name){
		for (ComponentRequest component : listToCheck) {
			if (component.getIdComponent() != null && component.getName().equals(name)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param idDataSource
	 * @param dataSourceVersion
	 * @return
	 */
	private List<Component> selectAlreadyPresentComponents(Integer idDataSource, Integer dataSourceVersion){
		
		if(idDataSource == null || dataSourceVersion == null){
			return null;
		}
		
		return componentMapper.selectComponentByDataSourceAndVersion(idDataSource, dataSourceVersion);
	}
	
	/**
	 * 
	 * @param request
	 * @param idSoType
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	private void checkComponents(StreamRequest request, Integer idSoType, 
			Integer idDataSource, Integer dataSourceVersion) throws NotFoundException, BadRequestException {
			
		
		List<Component> alreadyPresentComponentsPreviousVersion = getAlreadyPresentComponentsPreviousVersion(idDataSource, dataSourceVersion);
		
		if (Type.FEED_TWEET.id() != idSoType) {
			
			ServiceUtil.checkList(request.getComponents());
			
			List<Component> alreadyPresentComponents = selectAlreadyPresentComponents(idDataSource, dataSourceVersion);
			
			for (ComponentRequest component : request.getComponents()) {

				/**
				 * ALREADY_PRESENT
				 *  Verificare che tutti gli idComponent siano compresi tra quelli ritornati dalla query. 
				 *  In caso contrario RITORNARE: Errore: Some idComponent is incorrect
				 */
				if(component.getIdComponent() != null && doesNotContainComponent(alreadyPresentComponents, component.getIdComponent())){
					throw new BadRequestException(Errors.NOT_ACCEPTABLE, "Some idComponent is incorrect: " + component.getIdComponent());
				}

				/**
				 * NEW
				 */
				if (component.getIdComponent() == null) {
					checkUnicComponentName(request.getComponents(), component.getName());					
					ServiceUtil.checkMandatoryParameter(component.getName(), "name");
					ServiceUtil.checkAphanumeric(component.getName(), "component name");
					ServiceUtil.checkMandatoryParameter(component.getAlias(), "alias");
					ServiceUtil.checkMandatoryParameter(component.getInorder(), "inorder");
					ServiceUtil.checkMandatoryParameter(component.getTolerance(), "tolerance");
					ServiceUtil.checkMandatoryParameter(component.getIdPhenomenon(), "idPhenomenon");
					ServiceUtil.checkMandatoryParameter(component.getIdDataType(), "idDataType");
					ServiceUtil.checkMandatoryParameter(component.getIdMeasureUnit(), "idMeasureUnit");
					ServiceUtil.checkMandatoryParameter(component.getRequired(), "required");
				}
				
			}
		}
		
	    /**
	     * ALREADY_PRESENT
		*  Verificare che tutti i campi name estratti dalla query siano presenti nei campi name degli ALREADY_PRESENT_req. 
		*  In caso contrario RITORNARE: Errore: You can't remove components from previous version.
		*/
		if(alreadyPresentComponentsPreviousVersion != null){
			for (Component prevcomponent : alreadyPresentComponentsPreviousVersion) {
				if (doesNotContainComponent(request.getComponents(), prevcomponent.getName())) {
					throw new BadRequestException(Errors.NOT_ACCEPTABLE, " You can't remove components from previous version.");
				}
			}
		}
	}

	/**
	 * 
	 * @param listComponentRequest
	 * @param name
	 * @throws BadRequestException
	 */
	private void checkUnicComponentName(List<ComponentRequest> listComponentRequest, String name) throws BadRequestException{
		int count = 0;
		for (ComponentRequest component : listComponentRequest) {
			
			if(component.getName().equals(name)){
				count++;
			}

			if (count > 1) {
				throw new BadRequestException(Errors.NOT_ACCEPTABLE, "The name field must be unique.");
			}
		}
	}
	
	
	/**
	 * 
	 * @param request
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkVisibility(StreamRequest request) throws BadRequestException, NotFoundException {
		checkVisibility(request.getVisibility(), request.getLicense(), request.getOpenData(), 
				request.getSharingTenants(), request.getCopyright());
	}

	/**
	 * 
	 * @param visibility
	 * @param license
	 * @param openData
	 * @param sharingTenants
	 * @param copyright
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkVisibility(String visibility, LicenseRequest license, OpenDataRequest openData, 
			List<SharingTenantRequest> sharingTenants, String copyright) throws BadRequestException, NotFoundException {

		ServiceUtil.checkValue("visibility", visibility, StreamVisibility.PRIVATE.code(),
				StreamVisibility.PUBLIC.code());

		// PRIVATE
		if (StreamVisibility.PRIVATE.code().equals(visibility)) {
			if (license != null) {
				throw new BadRequestException(Errors.INCORRECT_VALUE, "License only for public visibility, provided: " + visibility);
			}
			if (openData != null) {
				throw new BadRequestException(Errors.INCORRECT_VALUE, "Opendata only for public visibility, provided: " + visibility);
			}
			if (sharingTenants != null) {
				for (SharingTenantRequest sharingTenant : sharingTenants) {
					ServiceUtil.checkMandatoryParameter(sharingTenant.getIdTenant(), "sharingTenant => idTenant");
					ServiceUtil.checkValue("dataOptions", sharingTenant.getDataOptions(), DataOption.READ.id(),
							DataOption.READ_AND_SUBSCRIBE.id(), DataOption.READ_AND_USE.id(), DataOption.WRITE.id());
					ServiceUtil.checkValue("manageOptions", sharingTenant.getManageOptions(),
							ManageOption.EDIT_METADATA.id(), ManageOption.LIFE_CYCLE_HANDLING.id(),
							ManageOption.NO_RIGHT.id());
					Tenant selectedTenant = tenantMapper.selectTenantByidTenant(sharingTenant.getIdTenant());
					ServiceUtil.checkIfFoundRecord(selectedTenant,
							"Sharing Tenant with [ " + sharingTenant.getIdTenant() + " ] not found!");
				}
			}
		}

		// PUBLIC
		if (StreamVisibility.PUBLIC.code().equals(visibility)) {

			if (sharingTenants != null) {
				throw new BadRequestException(Errors.INCORRECT_VALUE, "Sharing Tenants permitted only for private visibility!");
			}
			if (copyright != null) {
				throw new BadRequestException(Errors.INCORRECT_VALUE, "Copyright permitted only for private visibility!");
			}
		}
	}

	/**
	 * 
	 * @param request
	 * @throws BadRequestException
	 */
	private void checkOpendataupdatedate(PostStreamRequest request) throws BadRequestException {
		if (request.getOpenData() != null && request.getOpenData().getOpendataupdatedate() != null
				&& !Util.isValidDateFormat(request.getOpenData().getOpendataupdatedate())) {
			throw new BadRequestException(Errors.INCORRECT_VALUE,
					"Aspected format date [ " + Constants.CLIENT_FORMAT_DATE + " ]: opendataupdatedate is "
							+ request.getOpenData().getOpendataupdatedate());
		}
	}

	/**
	 * 
	 * @param idSubdomain
	 * @return
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	private Subdomain checkSubdomain(Integer idSubdomain) throws NotFoundException, BadRequestException {
		ServiceUtil.checkMandatoryParameter(idSubdomain, "idSubdomain");
		Subdomain subdomain = subdomainMapper.selectSubdomainByIdSubdomain(idSubdomain);
		ServiceUtil.checkIfFoundRecord(subdomain, "subdomain not found idSubdomain [" + idSubdomain + "] ");
		return subdomain;
	}

	/**
	 * 
	 * @param idTenant
	 * @param organizationCode
	 * @return
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	private Tenant checkTenant(Integer idTenant, String organizationCode)
			throws NotFoundException, BadRequestException {
		ServiceUtil.checkMandatoryParameter(idTenant, "idTenant");
		Tenant tenant = tenantMapper.selectTenantByIdAndOrgCodeCode(idTenant, organizationCode);
		ServiceUtil.checkIfFoundRecord(tenant,
				"tenant not found idTenant [" + idTenant + "], organizationcode [" + organizationCode + "] ");
		return tenant;
	}

	/**
	 * 
	 * @param idTenant
	 * @throws UnauthorizedException
	 */
	private void checkMaxNumStream(Integer idTenant) throws UnauthorizedException {
		Bundles bundles = bundlesMapper.selectBundlesByTenant(idTenant);
		if (bundles.getMaxstreamsnum() != -1) {
			Integer countOfTenantStream = streamMapper.selectCountOfTenantStream(idTenant);
			if (countOfTenantStream + 1 > bundles.getMaxstreamsnum()) {
				throw new UnauthorizedException(Errors.UNAUTHORIZED, "max num stream reached.");
			}
		}
	}

	/**
	 * 
	 * @param streamcode
	 * @param idSmartobject
	 * @throws BadRequestException
	 */
	private void checkStreamCode(String streamcode, Integer idSmartobject) throws BadRequestException {
		ServiceUtil.checkCode(streamcode, "streamcode");
		Stream stream = streamMapper.selectStreamByStreamcodeAndIdSmartObject(streamcode, idSmartobject);
		if (stream != null) {
			throw new BadRequestException(Errors.INTEGRITY_VIOLATION, "There is another stream with streamcode [ "
					+ streamcode + " ] and idSmartObject [ " + idSmartobject + " ]");
		}
	}
	
	/**
	 * 
	 * @param smartObject
	 * @param streamRequest
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	private void updateValidation(Smartobject smartObject, StreamToUpdate streamToUpdate, StreamRequest streamRequest)throws BadRequestException, NotFoundException, Exception {
		
		// checdkDraftStatus
		if(Status.DRAFT.id() == streamToUpdate.getIdStatus()){
			throw new NotAcceptableException(Errors.NOT_ACCEPTABLE, "Only Stream in DRAFT version can be updated.");
		}
			
		checkComponents(streamRequest, smartObject.getIdSoType(), streamToUpdate.getIdDataSource(), streamToUpdate.getDataSourceVersion());
		
		checkInternalSmartObject(streamRequest, smartObject.getIdSoType());
		
		checkFeedTweetSmartobject(streamRequest, smartObject.getIdSoType());
		
		checkLicense(streamRequest);
		
		checkVisibility(streamRequest);
		
		checkDcat(streamRequest);
		
		ServiceUtil.checkList(streamRequest.getTags(), "tags");
		
		ServiceUtil.checkMandatoryParameter(streamRequest.getStreamname(), "streamName");
		ServiceUtil.checkMandatoryParameter(streamRequest.getName(), "name");
		ServiceUtil.checkMandatoryParameter(streamRequest.getSavedata(), "saveData");
	}

	/**
	 * 
	 * @param streamRequest
	 * @param streamToUpdate
	 * @return
	 * @throws Exception
	 */
	private int deleteComponents(StreamRequest streamRequest, StreamToUpdate streamToUpdate) throws Exception{
		List<Integer> alreadyPresentIdList =  new ArrayList<Integer>();
		for (ComponentRequest component : streamRequest.getComponents()) {
			if (component.getIdComponent() != null) {
				alreadyPresentIdList.add(component.getIdComponent());
			}
		}
		return componentMapper.deleteComponents(streamToUpdate.getIdDataSource(), streamToUpdate.getDataSourceVersion(), alreadyPresentIdList);
	}

	/**
	 * 
	 * @param streamRequest
	 * @throws Exception
	 */
	private void updateComponent(StreamRequest streamRequest) throws Exception{
		for (ComponentRequest componentRequest : streamRequest.getComponents()) {
			if (componentRequest.getIdComponent() != null) {
				Component component = new Component();
				component.setIdComponent(componentRequest.getIdComponent());
				component.setAlias(componentRequest.getAlias());
				component.setInorder(componentRequest.getInorder());
				component.setTolerance(componentRequest.getTolerance());
				component.setIdPhenomenon(componentRequest.getIdPhenomenon());
				component.setIdMeasureUnit(componentRequest.getIdMeasureUnit());
				componentMapper.updateComponent(component);
			}
		}
	}
	
	/**
	 * 
	 * @param streamRequest
	 * @param streamToUpdate
	 * @throws Exception
	 */
	private void insertComponent(StreamRequest streamRequest, StreamToUpdate streamToUpdate) throws Exception{
		for (ComponentRequest componentRequest : streamRequest.getComponents()) {

			if(componentRequest.getIdComponent() == null){
				Component component = new Component();
				component.setIdDataSource(streamToUpdate.getIdDataSource());
				component.setDatasourceversion(streamToUpdate.getDataSourceVersion());
				component.setName(componentRequest.getName());
				component.setAlias(componentRequest.getAlias());
				component.setInorder(componentRequest.getInorder());
				component.setTolerance(componentRequest.getTolerance());
				component.setSinceVersion(streamToUpdate.getDataSourceVersion());
				component.setIdPhenomenon(componentRequest.getIdPhenomenon());
				component.setIdDataType(componentRequest.getIdDataType());
				component.setIdMeasureUnit(componentRequest.getIdMeasureUnit());
				component.setRequired(Util.booleanToInt(true));
				component.setIskey(Util.booleanToInt(false));
				componentMapper.insertComponent(component);
			}
		}
	}

	/**
	 * 
	 * @param streamRequest
	 * @param streamToUpdate
	 * @throws Exception
	 */
	private void updateComponentTransaction(StreamRequest streamRequest, StreamToUpdate streamToUpdate) throws Exception{
		
		if (streamRequest.getComponents() != null) {
			
			// delete 
			deleteComponents(streamRequest, streamToUpdate);
			
			// update
			updateComponent(streamRequest);
			
			// 3) inserisco i component NEW
			insertComponent(streamRequest, streamToUpdate);			
		}			
		
	}

	/**
	 * 
	 * @param streamRequest
	 * @param streamToUpdate
	 * @throws Exception
	 */
	private void updateSharingTenantTransaction(StreamRequest streamRequest, StreamToUpdate streamToUpdate, Timestamp now) throws Exception{
		
		if(streamRequest.getSharingTenants() != null && !streamRequest.getSharingTenants().isEmpty()){
			tenantMapper.deleteNotManagerTenantDataSource(streamToUpdate.getIdDataSource(), streamToUpdate.getDataSourceVersion());
			
			for (SharingTenantRequest sharingTenantRequest : streamRequest.getSharingTenants()) {

				TenantDataSource tenantDataSource = new TenantDataSource();
				
				tenantDataSource.setIdDataSource(streamToUpdate.getIdDataSource());        
				tenantDataSource.setDatasourceversion(streamToUpdate.getDataSourceVersion());
				tenantDataSource.setIdTenant(sharingTenantRequest.getIdTenant());
				tenantDataSource.setIsactive(Util.booleanToInt(true));
				tenantDataSource.setIsmanager(Util.booleanToInt(false));
				tenantDataSource.setActivationdate(now);    
				tenantDataSource.setManagerfrom(now);    
				tenantDataSource.setDataoptions(DataOption.READ_AND_USE.id());
				tenantDataSource.setManageoptions( ManageOption.NO_RIGHT.id());

				tenantMapper.insertTenantDataSource(tenantDataSource);
			}
		}
		
	}

	/**
	 * 
	 * @param streamRequest
	 * @param streamToUpdate
	 * @param now
	 * @throws Exception
	 */
	private void updateInternalStreamsTransaction(StreamRequest streamRequest, StreamToUpdate streamToUpdate) throws Exception{
		if(streamRequest.getInternalStreams() != null && !streamRequest.getInternalStreams().isEmpty()){
			
			streamMapper.deleteStreamInternal(streamToUpdate.getIdDataSource(), streamToUpdate.getDataSourceVersion());

			for (InternalStreamRequest internalStreamRequest : streamRequest.getInternalStreams()) {
				
				StreamInternal streamInternal = new StreamInternal();
				
				streamInternal.setIdDataSourceinternal(streamToUpdate.getIdDataSource());
				streamInternal.setDatasourceversioninternal(streamToUpdate.getDataSourceVersion());
				streamInternal.setIdstream(internalStreamRequest.getIdStream());
				streamInternal.setStreamAlias(internalStreamRequest.getStreamAlias());
				
				streamMapper.insertStreamInternal(streamInternal);
			}
			
		}
	}
	
	/**
	 * 
	 * @param streamRequest
	 * @param streamToUpdate
	 * @param smartObject
	 * @throws Exception
	 */
	private Dataset updateDataSet(StreamRequest streamRequest, StreamToUpdate streamToUpdate, Smartobject smartObject ) throws Exception{
		
		if (!streamRequest.getSavedata()) {
			datasetMapper.deleteDataSet(streamToUpdate.getIdDataSource(), streamToUpdate.getDataSourceVersion());
		}
		
		return insertDataset(streamRequest, streamToUpdate.getIdDataSource(), smartObject.getIdSoType(), streamToUpdate.getDataSourceVersion(), streamToUpdate.getStreamCode());
	}
	
	/**
	 * 
	 * @param streamRequest
	 * @param streamToUpdate
	 * @param smartObject
	 * @throws Exception
	 */
	private void updateStreamTransaction(StreamRequest streamRequest, StreamToUpdate streamToUpdate, Smartobject smartObject )throws  Exception{
	
		Timestamp now = Util.getNow();
		
		Long idDcat = insertDcat(streamRequest.getDcat());
		Integer idLicense = insertLicense(streamRequest.getLicense());

		updateDataSource(streamRequest, idDcat, idLicense, streamToUpdate.getIdDataSource(), streamToUpdate.getDataSourceVersion());
		
		updateStream(streamRequest, streamToUpdate.getIdDataSource(), streamToUpdate.getDataSourceVersion());
		
		updateTagDataSource(streamRequest, streamToUpdate);		

		updateComponentTransaction(streamRequest, streamToUpdate);

		updateSharingTenantTransaction(streamRequest, streamToUpdate, now);		

		updateInternalStreamsTransaction(streamRequest, streamToUpdate);		

		Dataset dataset = updateDataSet(streamRequest, streamToUpdate, smartObject);		
		
		updateApi(streamRequest, streamToUpdate, smartObject, dataset);
		
	}

	/**
	 * 
	 * @param streamRequest
	 * @param streamToUpdate
	 * @param smartObject
	 * @param dataset
	 * @throws Exception
	 */
	private void updateApi(StreamRequest streamRequest, StreamToUpdate streamToUpdate, Smartobject smartObject , Dataset dataset) throws Exception{
		apiMapper.deleteApi(streamToUpdate.getIdDataSource(), streamToUpdate.getDataSourceVersion());
		insertApi(streamRequest, smartObject, dataset, streamToUpdate.getIdDataSource(), streamToUpdate.getStreamCode(), streamToUpdate.getDataSourceVersion());
	}
	
	/**
	 * 
	 * @param streamRequest
	 * @param streamToUpdate
	 * @throws Exception
	 */
	private void updateTagDataSource(StreamRequest streamRequest, StreamToUpdate streamToUpdate)throws Exception{

		dataSourceMapper.deleteTagDataSource(streamToUpdate.getIdDataSource(), streamToUpdate.getDataSourceVersion());

		for (Integer idTag : streamRequest.getTags()) {
			dataSourceMapper.insertTagDataSource(streamToUpdate.getIdDataSource(), streamToUpdate.getDataSourceVersion(), idTag);
		}

	}
	
	/**
	 * 
	 * @param streamRequest
	 * @param idDcat
	 * @param idLicense
	 * @param idDataSource
	 * @param dataSourceVersion
	 * @return
	 * @throws Exception
	 */
	private int updateStream(StreamRequest streamRequest, Integer idDataSource, Integer dataSourceVersion) throws Exception{

		Stream stream = new Stream();
		stream.setIdDataSource(idDataSource);
		stream.setDatasourceversion(dataSourceVersion);
		stream.setStreamname(streamRequest.getStreamname());
		stream.setPublishstream(Util.booleanToInt(true));    
		stream.setSavedata( Util.booleanToInt(streamRequest.getSavedata()));
		stream.setFps(streamRequest.getFps());
		stream.setInternalquery(streamRequest.getInternalquery());
		
		if(streamRequest.getTwitterInfoRequest() != null){
			stream.setTwtquery(streamRequest.getTwitterInfoRequest().getTwtquery());
			stream.setTwtgeoloclat(streamRequest.getTwitterInfoRequest().getTwtgeoloclat());
			stream.setTwtgeoloclon(streamRequest.getTwitterInfoRequest().getTwtgeoloclon());
			stream.setTwtgeolocradius(streamRequest.getTwitterInfoRequest().getTwtgeolocradius());
			stream.setTwtgeolocunit(streamRequest.getTwitterInfoRequest().getTwtgeolocunit());
			stream.setTwtlang(streamRequest.getTwitterInfoRequest().getTwtlang());
			stream.setTwtlocale(streamRequest.getTwitterInfoRequest().getTwtlocale());
			stream.setTwtcount(streamRequest.getTwitterInfoRequest().getTwtcount());
			stream.setTwtresulttype(streamRequest.getTwitterInfoRequest().getTwtresulttype());
			stream.setTwtuntil(streamRequest.getTwitterInfoRequest().getTwtuntil());
			stream.setTwtratepercentage(streamRequest.getTwitterInfoRequest().getTwtratepercentage());     
			stream.setTwtlastsearchid(streamRequest.getTwitterInfoRequest().getTwtlastsearchid());			
		}

		return streamMapper.updateStream(stream);
		
	}
	
	/**
	 * 
	 * @param streamRequest
	 * @param idDcat
	 * @param idLicense
	 * @return
	 * @throws Exception
	 */
	private int updateDataSource(StreamRequest streamRequest, Long idDcat, Integer idLicense, Integer idDataSource, Integer dataSourceVersion) throws Exception{
		
		DataSource dataSource = new DataSource();
		dataSource.setIdDataSource(idDataSource);
		dataSource.setDatasourceversion(dataSourceVersion);
		
		dataSource.setUnpublished( Util.booleanToInt(streamRequest.getUnpublished()) );
		dataSource.setName(streamRequest.getName());
		dataSource.setVisibility(streamRequest.getVisibility());
		dataSource.setCopyright(streamRequest.getCopyright());
        dataSource.setDisclaimer(streamRequest.getDisclaimer());
        dataSource.setIcon(streamRequest.getIcon());
        dataSource.setIsopendata(streamRequest.getOpenData() != null ? Util.booleanToInt(true) : Util.booleanToInt(false));
        if(streamRequest.getOpenData() != null){
            dataSource.setOpendataexternalreference(streamRequest.getOpenData().getOpendataexternalreference());    
            dataSource.setOpendataauthor(streamRequest.getOpenData().getOpendataauthor());
            dataSource.setOpendataupdatedate(Util.dateStringToTimestamp(streamRequest.getOpenData().getOpendataupdatedate()));
            dataSource.setOpendatalanguage(streamRequest.getOpenData().getOpendatalanguage());
            dataSource.setLastupdate(streamRequest.getOpenData().getLastupdate());
        }
        dataSource.setIdDcat(idDcat); 
        dataSource.setIdLicense(idLicense);
        
        return dataSourceMapper.updateDataSource(dataSource);
	}
	

}
