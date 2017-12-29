package org.csi.yucca.adminapi.service.impl;

import static org.csi.yucca.adminapi.util.ServiceUtil.API_CODE_PREFIX_MQTT;
import static org.csi.yucca.adminapi.util.ServiceUtil.API_CODE_PREFIX_WEBSOCKET;
import static org.csi.yucca.adminapi.util.ServiceUtil.API_SUBTYPE_MQTT;
import static org.csi.yucca.adminapi.util.ServiceUtil.API_SUBTYPE_ODATA;
import static org.csi.yucca.adminapi.util.ServiceUtil.API_SUBTYPE_WEBSOCKET;
import static org.csi.yucca.adminapi.util.ServiceUtil.DATASOURCE_VERSION;
import static org.csi.yucca.adminapi.util.ServiceUtil.SINCE_VERSION;
import static org.csi.yucca.adminapi.util.ServiceUtil.buildResponse;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkAuthTenant;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkCode;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkComponents;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkIfFoundRecord;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkList;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkMandatoryParameter;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkTenant;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkVisibility;
import static org.csi.yucca.adminapi.util.ServiceUtil.getSortList;
import static org.csi.yucca.adminapi.util.ServiceUtil.getTenantCodeListFromUser;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertDataSource;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertDataset;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertDcat;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertLicense;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertTags;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertTenantDataSource;
import static org.csi.yucca.adminapi.util.ServiceUtil.updateDataSource;
import static org.csi.yucca.adminapi.util.ServiceUtil.updateTagDataSource;

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
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.model.DettaglioStream;
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
import org.csi.yucca.adminapi.request.InternalStreamRequest;
import org.csi.yucca.adminapi.request.PostStreamRequest;
import org.csi.yucca.adminapi.request.SharingTenantRequest;
import org.csi.yucca.adminapi.request.StreamRequest;
import org.csi.yucca.adminapi.request.TwitterInfoRequest;
import org.csi.yucca.adminapi.response.DettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.ListStreamResponse;
import org.csi.yucca.adminapi.response.PostStreamResponse;
import org.csi.yucca.adminapi.service.StreamService;
import org.csi.yucca.adminapi.util.Constants;
import org.csi.yucca.adminapi.util.DataOption;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.ManageOption;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.csi.yucca.adminapi.util.Status;
import org.csi.yucca.adminapi.util.Type;
import org.csi.yucca.adminapi.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class StreamServiceImpl implements StreamService {

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
	
	@Override
	public ServiceResponse actionOnStream(String action, String organizationCode, String soCode, Integer idStream, JwtUser authorizedUser)  throws BadRequestException, NotFoundException, Exception {

		DettaglioStream dettaglioStream = streamMapper.selectStream(null, idStream, organizationCode, getTenantCodeListFromUser(authorizedUser));
		
		checkIfFoundRecord(dettaglioStream);
		
		// ----------------------------------------------------------------------------
		//	1. Se stato DRAFT --> unica action accettata è req_install 
		//  In questo caso l'unica operazione da fare è mettere lo stato in REQ_INST
		if(Status.DRAFT.code().equals(dettaglioStream.getStatusCode())){
			if (!Status.REQUEST_INSTALLATION.code().equals(action)) {
				throw new BadRequestException(Errors.INCORRECT_VALUE);
			}
			
			// TO DO
			// fare update con stato in "REQ_INST"
			
			return ServiceResponse.build().OK();
		}
		
		
//		2. Se stato REQ_INST --> nessuna action accettata da management ma solo da BO
		if(Status.REQUEST_INSTALLATION.code().equals(dettaglioStream.getStatusCode()) || 
		   Status.REQUEST_UNINSTALLATION.code().equals(dettaglioStream.getStatusCode()) ||
		   Status.INSTALLATION_IN_PROGRESS.code().equals(dettaglioStream.getStatusCode()) ||
		   Status.UNINSTALLATION_IN_PROGRESS.code().equals(dettaglioStream.getStatusCode()) ||
		   Status.INSTALLATION_FAIL.code().equals(dettaglioStream.getStatusCode()) ||
		   Status.UNINSTALLATION.code().equals(dettaglioStream.getStatusCode()) ){
			throw new BadRequestException(Errors.INCORRECT_VALUE);
		}
		
//		6. Se stato INST --> uniche action accettata sono new_version e req_uninstall
//		se new_version
		if(Status.INSTALLED.code().equals(dettaglioStream.getStatusCode())){
			if (!"new_version".equals(action) && !"req_uninstall".equals(action)) {
				throw new BadRequestException(Errors.INCORRECT_VALUE);
			}
			
			if ("new_version".equals(action)) {
				
			}
			else{
				// req_uninstall
				
			}
			
			return ServiceResponse.build().OK();
		}
		
		
		return ServiceResponse.build().NO_CONTENT();
	}
	
    /**
     * update stream
     */
	@Override
	public ServiceResponse updateStream(String organizationCode, String soCode, Integer idStream, StreamRequest streamRequest, 
			String tenantCodeManager, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {
		
		// streamToUpdate
		StreamToUpdate streamToUpdate =	streamMapper.selectStreamToUpdate(tenantCodeManager, idStream, organizationCode, getTenantCodeListFromUser(authorizedUser));
		checkIfFoundRecord(streamToUpdate);

		// smartobject
		Smartobject smartObject = smartobjectMapper.selectSmartobjectBySocodeAndOrgcode(soCode, organizationCode);
		checkIfFoundRecord(smartObject,"smartobject not found socode [" + soCode + "], organizationcode [" + organizationCode + "] ");

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

		List<String> tenantCodeListFromUser = getTenantCodeListFromUser(authorizedUser);
		
		DettaglioStream dettaglioStream = streamMapper.selectStream(tenantCodeManager, idStream, organizationCode, tenantCodeListFromUser);

		checkIfFoundRecord(dettaglioStream);

		DettaglioSmartobject dettaglioSmartobject = smartobjectMapper.selectSmartobjectById(dettaglioStream.getIdSmartObject());
		
		List<DettaglioStream> listInternalStream = streamMapper.selectInternalStream( dettaglioStream.getIdDataSource(), dettaglioStream.getDatasourceversion() );
		   
		DettaglioDataset dettaglioDataset = getDettaglioDataset(dettaglioStream, tenantCodeManager, organizationCode, tenantCodeListFromUser);
		
		if(dettaglioDataset != null){
			return buildResponse(new DettaglioStreamDatasetResponse(dettaglioStream, dettaglioDataset, dettaglioSmartobject, listInternalStream)); 
		}
		else{
			return buildResponse(new DettaglioStreamDatasetResponse(dettaglioStream, dettaglioSmartobject, listInternalStream));
		}
		
	}
	
	/**
	 * 
	 * @param dettaglioStream
	 * @param tenantCodeManager
	 * @param organizationCode
	 * @param tenantCodeListFromUser
	 * @return
	 */
	private DettaglioDataset getDettaglioDataset(DettaglioStream dettaglioStream, String tenantCodeManager, String organizationCode, List<String> tenantCodeListFromUser) {
		
		DettaglioDataset dettaglioDataset = null;
		
		if (dettaglioStream.getSavedata() != null && dettaglioStream.getSavedata().equals(Util.booleanToInt(true))) {
			dettaglioDataset = datasetMapper.selectDettaglioDatasetByDatasource(dettaglioStream.getIdDataSource(), dettaglioStream.getDatasourceversion());
		}

		return dettaglioDataset;
	}
	
	/**
	 * 
	 */
	@Override
	public byte[] selectStreamIcon(String organizationCode, Integer idStream, JwtUser authorizedUser) 
			throws BadRequestException, NotFoundException, Exception {

		DettaglioStream dettaglioStream = streamMapper.selectStream(null, idStream, organizationCode, 
				getTenantCodeListFromUser(authorizedUser));

		checkIfFoundRecord(dettaglioStream);

		return Util.convertIconFromDBToByte(dettaglioStream.getDataSourceIcon());
	}

	/**
	 *  SELECT STREAMS FROM MANAGEMENT
	 */
	@Override
	public ServiceResponse selectStreams(String organizationCode, String tenantCodeManager, String sort, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, UnauthorizedException, Exception {
		
		checkAuthTenant(authorizedUser, tenantCodeManager);
		
		ServiceUtil.checkMandatoryParameter(organizationCode, "organizationCode");		
		
		List<String> sortList = getSortList(sort, DettaglioStream.class);
		
		List<DettaglioStream> list = streamMapper.selectStreams(tenantCodeManager, organizationCode, sortList, getTenantCodeListFromUser(authorizedUser));
		
		checkList(list);
		
		return buildResponse(buildSelectStreamsResponse(list));
	}
	
	/**
	 * SELECT STREAMS FROM BACKOFFICE
	 */
	@Override
	public ServiceResponse selectStreams(String sort)
			throws BadRequestException, NotFoundException, UnauthorizedException, Exception {		
		
		List<String> sortList = getSortList(sort, DettaglioStream.class);
		
		List<DettaglioStream> list = streamMapper.selectStreams(null, null, sortList, null);
		
		checkList(list);
		
		return buildResponse(buildSelectStreamsResponse(list));
	}

	/**
	 * 
	 */
	@Override
	public ServiceResponse createStreamDataset(PostStreamRequest request, String organizationCode, String soCode,
			JwtUser authorizedUser) throws BadRequestException, NotFoundException, UnauthorizedException, Exception {

		Organization organization = organizationMapper.selectOrganizationByCode(organizationCode);
		checkIfFoundRecord(organization);

		Smartobject smartobject = smartobjectMapper.selectSmartobjectBySocodeAndOrgcode(soCode, organizationCode);
		checkIfFoundRecord(smartobject,
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
//	private List<Component> getAlreadyPresentComponentsPreviousVersion(Integer idDataSource, Integer version){
//		
//		if(idDataSource == null || version == null){
//			return null;
//		}
//		
//		if(version > 1){
//			return componentMapper.selectComponentByDataSourceAndVersion(idDataSource,  (version-1) );
//		}
//		
//		return null;
//	}	
	
	
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
	 * @param smartobject
	 */
	private void insertComponents(PostStreamRequest request, Smartobject smartobject, Integer idDataSource) throws Exception {
		if (Type.FEED_TWEET.id() != smartobject.getIdSoType()) {
			ServiceUtil.insertComponents(request.getComponents(), idDataSource, DATASOURCE_VERSION, SINCE_VERSION, Util.booleanToInt(false), componentMapper);
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
	private Stream insertStreamTransaction(PostStreamRequest request, Organization organization, Smartobject smartobject) throws Exception{

		Timestamp now = Util.getNow();

		Long idDcat = insertDcat(request.getDcat(), dcatMapper);

		Integer idLicense = insertLicense(request.getLicense(), licenseMapper);

		Integer idDataSource = insertDataSource(request, organization.getIdOrganization(), idDcat, idLicense, Status.DRAFT.id(), dataSourceMapper);

		Stream stream = insertStream(request, idDataSource, smartobject.getIdSmartObject());

		insertTags(request.getTags(), idDataSource, DATASOURCE_VERSION, dataSourceMapper);

		insertComponents(request, smartobject, idDataSource);

		insertTweetComponents(request, smartobject);

		insertTenantDataSource(request.getIdTenant(), idDataSource, now, tenantMapper);

		insertSharingTenants(request, idDataSource, now);

		insertStreamInternal(request, idDataSource);
		Dataset dataset = insertDataset(request.getSavedata(), idDataSource, smartobject.getIdSoType(), DATASOURCE_VERSION, 
				request.getStreamcode(), datasetMapper, sequenceMapper);

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
					.apiname(dataset.getDatasetname()).apisubtype(API_SUBTYPE_ODATA)
					.idDataSource(idDataSource).entitynamespace("it.csi.smartdata.odata."+dataset.getDatasetcode()));
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
//	private Dataset insertDataset(StreamRequest request, Integer idDataSource, Integer idSoType, Integer dataSourceVersion, String streamCode) {
//		
//		Dataset checkDataSet = datasetMapper.selectDataSet(idDataSource, dataSourceVersion);
//		if (checkDataSet != null) {
//			return checkDataSet;
//		}
//		
//		if (request.getSavedata()) { // no
//			Integer iddataset = sequenceMapper.selectDatasetSequence();
//
//			Dataset dataset = new Dataset();
//
//			dataset.setIddataset(iddataset);
//			dataset.setIdDataSource(idDataSource);
//			dataset.setDatasourceversion(dataSourceVersion);
//			dataset.setDatasetname(streamCode); // datasetName
//			dataset.setDatasetcode(generateStreamDatasetCode(iddataset, streamCode));
//			dataset.setDescription("Dataset " + streamCode);
//			dataset.setIdDatasetType(DatasetType.DATASET.id());
//			
//			// BULK o binary
//			if (Type.FEED_TWEET.id() == idSoType) {
//				dataset.setIdDatasetSubtype(DatasetSubtype.SOCIAL.id());
//			} else {
//				dataset.setIdDatasetSubtype(DatasetSubtype.STREAM.id());
//			}
//			dataset.setAvailablespeed(Util.booleanToInt(true));
//			dataset.setIstransformed(Util.booleanToInt(false));
//
//			datasetMapper.insertDataset(dataset);
//
//			return dataset;
//		}
//		
//		return null;
//	}


	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param iddataset
	 * @param streamcode
	 * @return
	 */
//	private String generateStreamDatasetCode(Integer iddataset, String streamcode) {
//		return STREAM_DATASET_PREFIX_CODE + Util.cleanStringCamelCase(streamcode) + "_" + iddataset;
//	}

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
	private void insertSharingTenants(PostStreamRequest request, Integer idDataSource, Timestamp now) throws Exception{
		if (request.getSharingTenants() != null) {
			ServiceUtil.insertSharingTenants(request.getSharingTenants(), idDataSource, now, tenantMapper);
		}
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

		Tenant tenant = checkTenant(request.getIdTenant(), organizationCode, tenantMapper);

		checkAuthTenant(authorizedUser, tenant.getTenantcode());

		checkMandatories(request);

		checkSubdomain(request.getIdSubdomain());

		checkOpendataupdatedate(request);

		checkMaxNumStream(request.getIdTenant());

		checkStreamCode(request.getStreamcode(), smartobject.getIdSmartObject());

		checkInternalSmartObject(request, smartobject.getIdSoType());
		
		checkFeedTweetSmartobject(request, smartobject.getIdSoType());

		checkComponents(request.getComponents(), smartobject.getIdSoType(), componentMapper);
		
		checkVisibility(request, tenantMapper);
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
			checkMandatoryParameter(internalQuery, "internalquery mandatory (only for internal smartobject)");
			checkList(internalStreams, "InternalStreams mandatory (only for internal smartobject)");
			for (InternalStreamRequest internalStream : internalStreams) {
				checkMandatoryParameter(internalStream.getIdStream(), "internalStream => idStream");
				checkMandatoryParameter(internalStream.getStreamAlias(), "internalStream => streamAlias");
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
		checkMandatoryParameter(request.getStreamname(), "streamname");
		checkMandatoryParameter(request.getName(), "name");
		checkMandatoryParameter(request.getSavedata(), "savedata");
		checkMandatoryParameter(request.getVisibility(), "visibility");
		checkMandatoryParameter(request.getRequestername(), "requestername");
		checkMandatoryParameter(request.getRequestersurname(), "requestersurname");
		checkMandatoryParameter(request.getRequestermail(), "requestermail");
		checkList(request.getTags(), "tags");
		
		checkDcat(request);

		ServiceUtil.checkLicense(request.getLicense());
		
	}

	/**
	 * 
	 * @param dcatRequest
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkDcat(StreamRequest request) throws BadRequestException, NotFoundException {
		if (request.getDcat()!= null && request.getDcat().getIdDcat() == null) {
			checkMandatoryParameter(request.getDcat().getDcatnomeorg(), "dcatnomeorg");
			checkMandatoryParameter(request.getDcat().getDcatemailorg(), "dcatemailorg");
			checkMandatoryParameter(request.getDcat().getDcatrightsholdername(), "dcatrightsholdername");
		}
	}
	
	/**
	 * 
	 * @param request
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkLicense(StreamRequest request) throws BadRequestException, NotFoundException{
		ServiceUtil.checkLicense(request.getLicense());
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
			checkMandatoryParameter(twitterInfoRequest, "twitterInfo mandatory (only for Feed Tweet smartobject)");
			checkMandatoryParameter(twitterInfoRequest.getTwtquery(), "twitterInfo => Twtquery");
			if (components != null) {
				throw new BadRequestException(Errors.INCORRECT_VALUE, "Component not allowed for Feed Tweet smartobject!");
			}
		}

		if (Type.FEED_TWEET.id() != idSoType && twitterInfoRequest != null) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, "TwitterInfo: is not feed tweet smartobject.");
		}
	}

//	/**
//	 * 
//	 * @param request
//	 * @param idSoType
//	 * @throws NotFoundException
//	 * @throws BadRequestException
//	 */
//	private void checkComponents(List<ComponentRequest> listComponentRequest, Integer idSoType) throws NotFoundException, BadRequestException {
//		checkComponents(listComponentRequest, idSoType, null, null);
//	}
	
//	/**
//	 * 
//	 * @param listToCheck
//	 * @param component
//	 * @return
//	 */
//	private boolean doesNotContainComponent(List<Component> listToCheck, Integer idComponent){
//		for (Component component : listToCheck) {
//			if (component.getIdComponent().equals(idComponent)) {
//				return false;
//			}
//		}
//		return true;
//	}
	
//	private boolean doesNotContainComponent(List<ComponentRequest> listToCheck, String name){
//		for (ComponentRequest component : listToCheck) {
//			if (component.getIdComponent() != null && component.getName().equals(name)) {
//				return false;
//			}
//		}
//		return true;
//	}

//	/**
//	 * 
//	 * @param idDataSource
//	 * @param dataSourceVersion
//	 * @return
//	 */
//	private List<Component> selectAlreadyPresentComponents(Integer idDataSource, Integer dataSourceVersion){
//		
//		if(idDataSource == null || dataSourceVersion == null){
//			return null;
//		}
//		
//		return componentMapper.selectComponentByDataSourceAndVersion(idDataSource, dataSourceVersion);
//	}

//	public void checkComponents(List<ComponentRequest> listComponentRequest, Integer idDataSource, Integer dataSourceVersion) throws NotFoundException, BadRequestException {
//		checkComponents(listComponentRequest, null, idDataSource, dataSourceVersion);
//	}

	
//	/**
//	 * 
//	 * @param request
//	 * @param idSoType
//	 * @throws NotFoundException
//	 * @throws BadRequestException
//	 */
//	private void checkComponents(List<ComponentRequest> listComponentRequest, Integer idSoType, 
//			Integer idDataSource, Integer dataSourceVersion) throws NotFoundException, BadRequestException {
//			
//		
//		List<Component> alreadyPresentComponentsPreviousVersion = getAlreadyPresentComponentsPreviousVersion(idDataSource, dataSourceVersion);
//		
//		if (idSoType == null || Type.FEED_TWEET.id() != idSoType) {
//			
//			checkList(listComponentRequest);
//			
//			List<Component> alreadyPresentComponents = selectAlreadyPresentComponents(idDataSource, dataSourceVersion);
//			
//			for (ComponentRequest component : listComponentRequest) {
//
//				/**
//				 * ALREADY_PRESENT
//				 *  Verificare che tutti gli idComponent siano compresi tra quelli ritornati dalla query. 
//				 *  In caso contrario RITORNARE: Errore: Some idComponent is incorrect
//				 */
//				if(component.getIdComponent() != null && doesNotContainComponent(alreadyPresentComponents, component.getIdComponent())){
//					throw new BadRequestException(Errors.NOT_ACCEPTABLE, "Some idComponent is incorrect: " + component.getIdComponent());
//				}
//
//				/**
//				 * NEW
//				 */
//				if (component.getIdComponent() == null) {
//					checkUnicComponentName(listComponentRequest, component.getName());					
//					checkMandatoryParameter(component.getName(), "name");
//					checkAphanumeric(component.getName(), "component name");
//					checkMandatoryParameter(component.getAlias(), "alias");
//					checkMandatoryParameter(component.getInorder(), "inorder");
//					checkMandatoryParameter(component.getTolerance(), "tolerance");
//					checkMandatoryParameter(component.getIdPhenomenon(), "idPhenomenon");
//					checkMandatoryParameter(component.getIdDataType(), "idDataType");
//					checkMandatoryParameter(component.getIdMeasureUnit(), "idMeasureUnit");
//					checkMandatoryParameter(component.getRequired(), "required");
//				}
//				
//			}
//		}
//		
//	    /**
//	     * ALREADY_PRESENT
//		*  Verificare che tutti i campi name estratti dalla query siano presenti nei campi name degli ALREADY_PRESENT_req. 
//		*  In caso contrario RITORNARE: Errore: You can't remove components from previous version.
//		*/
//		if(alreadyPresentComponentsPreviousVersion != null){
//			for (Component prevcomponent : alreadyPresentComponentsPreviousVersion) {
//				if (doesNotContainComponent(listComponentRequest, prevcomponent.getName())) {
//					throw new BadRequestException(Errors.NOT_ACCEPTABLE, " You can't remove components from previous version.");
//				}
//			}
//		}
//	}

//	/**
//	 * 
//	 * @param listComponentRequest
//	 * @param name
//	 * @throws BadRequestException
//	 */
//	private void checkUnicComponentName(List<ComponentRequest> listComponentRequest, String name) throws BadRequestException{
//		int count = 0;
//		for (ComponentRequest component : listComponentRequest) {
//			
//			if(component.getName().equals(name)){
//				count++;
//			}
//
//			if (count > 1) {
//				throw new BadRequestException(Errors.NOT_ACCEPTABLE, "The name field must be unique.");
//			}
//		}
//	}

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
		checkMandatoryParameter(idSubdomain, "idSubdomain");
		Subdomain subdomain = subdomainMapper.selectSubdomainByIdSubdomain(idSubdomain);
		checkIfFoundRecord(subdomain, "subdomain not found idSubdomain [" + idSubdomain + "] ");
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
//	private Tenant checkTenant(Integer idTenant, String organizationCode) throws NotFoundException, BadRequestException {
//		
//		checkMandatoryParameter(idTenant, "idTenant");
//		
//		Tenant tenant = tenantMapper.selectTenantByIdAndOrgCodeCode(idTenant, organizationCode);
//		
//		checkIfFoundRecord(tenant, "tenant not found idTenant [" + idTenant + "], organizationcode [" + organizationCode + "] ");
//		
//		return tenant;
//	}

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
		checkCode(streamcode, "streamcode");
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
		if(Status.DRAFT.id() != streamToUpdate.getIdStatus()){
			throw new NotAcceptableException(Errors.NOT_ACCEPTABLE, "Only Stream in DRAFT version can be updated.");
		}
			
		checkComponents(streamRequest.getComponents(), smartObject.getIdSoType(), streamToUpdate.getIdDataSource(), streamToUpdate.getDataSourceVersion(), componentMapper);
		
		checkInternalSmartObject(streamRequest, smartObject.getIdSoType());
		
		checkFeedTweetSmartobject(streamRequest, smartObject.getIdSoType());
		
		checkLicense(streamRequest);
		
		checkVisibility(streamRequest, tenantMapper);
		
		checkDcat(streamRequest);
		
		checkList(streamRequest.getTags(), "tags");
		
		checkMandatoryParameter(streamRequest.getStreamname(), "streamName");
		checkMandatoryParameter(streamRequest.getName(), "name");
		checkMandatoryParameter(streamRequest.getSavedata(), "saveData");
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
		
		return insertDataset(streamRequest.getSavedata(), streamToUpdate.getIdDataSource(), smartObject.getIdSoType(), 
				streamToUpdate.getDataSourceVersion(), streamToUpdate.getStreamCode(), datasetMapper, sequenceMapper);
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
		
		Long idDcat = insertDcat(streamRequest.getDcat(), dcatMapper);
		Integer idLicense = insertLicense(streamRequest.getLicense(), licenseMapper);

		updateDataSource(streamRequest, idDcat, idLicense, streamToUpdate.getIdDataSource(), streamToUpdate.getDataSourceVersion(), dataSourceMapper);
		
		updateStream(streamRequest, streamToUpdate.getIdDataSource(), streamToUpdate.getDataSourceVersion());
		
		updateTagDataSource(streamRequest.getTags(), streamToUpdate.getIdDataSource(), streamToUpdate.getDataSourceVersion(), dataSourceMapper);		
		
		updateComponentTransaction(streamRequest, streamToUpdate);

		updateSharingTenantTransaction(streamRequest, streamToUpdate, now);		

		updateInternalStreamsTransaction(streamRequest, streamToUpdate);		

		Dataset dataset = updateDataSet(streamRequest, streamToUpdate, smartObject);		
		
		updateApi(streamRequest, streamToUpdate, smartObject, dataset);
		
	}

	/**
	 * Remove odata api if saveData is "false" and insert if saveData is "true"
	 * @param streamRequest
	 * @param streamToUpdate
	 * @param smartObject
	 * @param dataset
	 * @throws Exception
	 */
	private void updateApi(StreamRequest streamRequest, StreamToUpdate streamToUpdate, Smartobject smartObject , Dataset dataset) throws Exception{
		
		if (streamRequest.getSavedata().booleanValue() && streamToUpdate.getSaveData().intValue() == 0 && dataset!=null)
		{
			apiMapper.insertApi(Api.buildOutput(streamToUpdate.getDataSourceVersion()).apicode(dataset.getDatasetcode())
					.apiname(dataset.getDatasetname()).apisubtype(API_SUBTYPE_ODATA)
					.idDataSource(streamToUpdate.getIdDataSource()).entitynamespace("it.csi.smartdata.odata."+dataset.getDatasetcode()));
		}
		if (streamRequest.getSavedata().booleanValue() == false && streamToUpdate.getSaveData().intValue() >= 1)
		{
			apiMapper.deleteApi(streamToUpdate.getIdDataSource(), streamToUpdate.getDataSourceVersion(), API_SUBTYPE_ODATA);
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

}
