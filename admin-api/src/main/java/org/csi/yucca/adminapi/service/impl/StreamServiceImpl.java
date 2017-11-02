package org.csi.yucca.adminapi.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
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
import org.csi.yucca.adminapi.model.License;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.model.Smartobject;
import org.csi.yucca.adminapi.model.Stream;
import org.csi.yucca.adminapi.model.StreamInternal;
import org.csi.yucca.adminapi.model.Subdomain;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.model.TenantDataSource;
import org.csi.yucca.adminapi.request.ComponentRequest;
import org.csi.yucca.adminapi.request.DcatRequest;
import org.csi.yucca.adminapi.request.InternalStreamRequest;
import org.csi.yucca.adminapi.request.LicenseRequest;
import org.csi.yucca.adminapi.request.PostStreamRequest;
import org.csi.yucca.adminapi.request.SharingTenantRequest;
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
	
	@Override
	public ServiceResponse createStreamDataset(PostStreamRequest request, String organizationCode, String soCode, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, UnauthorizedException, Exception {

		Organization organization = organizationMapper.selectOrganizationByCode(organizationCode);
		ServiceUtil.checkIfFoundRecord(organization);

		Smartobject smartobject = smartobjectMapper.selectSmartobjectBySocodeAndOrgcode(soCode, organizationCode);
		ServiceUtil.checkIfFoundRecord(smartobject, "smartobject not found socode [" + soCode + "], organizationcode [" + organizationCode + "] ");
		
		validation(request, organizationCode, smartobject, authorizedUser);
		
		Stream stream = streamTransaction(request, organization, smartobject);
		
		return ServiceResponse.build().object(PostStreamResponse.build(stream.getIdstream()).streamcode(stream.getStreamcode()).streamname(stream.getStreamname()));
	}
	
	/**
	 * 
	 * @param request
	 * @param idDataSource
	 */
	private void insertTags(PostStreamRequest request, Integer idDataSource){
		for (Integer idTag : request.getTags()) {
			dataSourceMapper.insertTagDataSource(idDataSource, 1, idTag);
		}
	}
	
	
	/**
	 * 
	 * @param request
	 * @param smartobject
	 */
	private void insertComponents(PostStreamRequest request, Smartobject smartobject, Integer idDataSource){
		
		if(Type.FEED_TWEET.id() != smartobject.getIdSoType()){
			
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
	private void insertTweetComponents(PostStreamRequest request, Smartobject smartobject){
		if(Type.FEED_TWEET.id() == smartobject.getIdSoType()){
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
	private Stream streamTransaction(PostStreamRequest request, Organization organization, Smartobject smartobject){
		
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
		
		Dataset dataset = insertDataset(request, idDataSource, smartobject.getIdSoType());		
		
		insertApi(request, smartobject, dataset, idDataSource);
		
		return stream;
		
	}

	/**
	 * 
	 * @param request
	 * @param smartobject
	 * @param dataset
	 * @param idDataSource
	 */
	private void insertApi(PostStreamRequest request, Smartobject smartobject, Dataset dataset, Integer idDataSource){
		apiMapper.insertApi(
				Api.buildOutput(DATASOURCE_VERSION)
					.apicode(API_CODE_PREFIX_WEBSOCKET + smartobject.getSocode() + request.getStreamcode())
					.apiname(request.getStreamname())
					.apisubtype(API_SUBTYPE_WEBSOCKET)
					.idDataSource(idDataSource));
		
		apiMapper.insertApi(
				Api.buildOutput(DATASOURCE_VERSION)
					.apicode(API_CODE_PREFIX_MQTT + smartobject.getSocode() + request.getStreamcode())
					.apiname(request.getStreamname())
					.apisubtype(API_SUBTYPE_MQTT)
					.idDataSource(idDataSource));

		if(request.getSavedata() && dataset != null){
			apiMapper.insertApi(
					Api.buildOutput(DATASOURCE_VERSION)
						.apicode(dataset.getDatasetcode())
						.apiname(dataset.getDatasetname())
						.apisubtype(API_SUBTYPE_ODATA)
						.idDataSource(idDataSource));
		}
	}
	
	
	/**
	 * 
	 * SOLO SE savedata == true allora aggiungo una riga su yucca_dataset yucca_dataset
	 * 
	 * @param request
	 * @param idDataSource
	 * @param idSoType
	 * @return
	 */
	private Dataset insertDataset(PostStreamRequest request, Integer idDataSource, Integer idSoType){
		if (request.getSavedata()) {
			Integer iddataset = sequenceMapper.selectDatasetSequence();
			
			Dataset dataset = new Dataset();
			
			dataset.setIddataset(iddataset);
			dataset.setIdDataSource(idDataSource);
			dataset.setDatasourceversion(DATASOURCE_VERSION);
			dataset.setDatasetname(request.getStreamcode());
			dataset.setDatasetcode(generateStreamDatasetCode(iddataset, request.getStreamcode()));
			dataset.setDescription("Dataset " + request.getStreamcode());
			dataset.setIdDatasetType(DatasetType.DATASET.id());
			if(Type.FEED_TWEET.id() == idSoType){
				dataset.setIdDatasetSubtype(DatasetSubtype.SOCIAL.id());
			}
			else{
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
	private void insertStreamInternal(PostStreamRequest request, Integer idDataSource){
		if(request.getInternalStreams() != null){
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
	private void insertSharingTenants(PostStreamRequest request, Integer idDataSource, Timestamp now){
		if(request.getSharingTenants() != null){
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
	private void insertTenantDataSource(PostStreamRequest request, Integer idDataSource, Timestamp now){
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
	private Stream insertStream(PostStreamRequest request, Integer idDataSource, Integer idSmartobject){
		Stream stream = new Stream();
		stream.setIdDataSource(idDataSource);
		stream.setDatasourceversion(1);
		stream.setStreamcode(request.getStreamcode());
		stream.setStreamname(request.getStreamname());
		stream.setPublishstream(Util.booleanToInt(true));
		stream.setSavedata(Util.booleanToInt(request.getSavedata()));
		stream.setFps(request.getFps());
		stream.setInternalquery(request.getInternalquery());
		stream.setTwtquery(request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtquery():null);
		stream.setTwtgeoloclat(request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtgeoloclat():null);
		stream.setTwtgeoloclon(request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtgeoloclon():null);
		stream.setTwtgeolocradius(request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtgeolocradius():null);
		stream.setTwtgeolocunit(request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtgeolocunit():null);
		stream.setTwtlang(request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtlang():null);
		stream.setTwtlocale(request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtlocale():null);
		stream.setTwtcount(request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtcount():null);
		stream.setTwtresulttype(request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtresulttype():null);
		stream.setTwtuntil(request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtuntil():null);
		stream.setTwtratepercentage(request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtratepercentage():null);
		stream.setTwtlastsearchid(request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtlastsearchid():null);
		stream.setTwtquery(request.getTwitterInfoRequest() != null ? request.getTwitterInfoRequest().getTwtquery():null);
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
	private void validation( PostStreamRequest request, String organizationCode, Smartobject smartobject, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {

		Tenant tenant = checkTenant(request.getIdTenant(), organizationCode);
		
		checkAuthTenant(authorizedUser, tenant.getTenantcode());		
		
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
	private Integer insertDataSource(PostStreamRequest request, Integer idOrganization, Long idDcat, Integer idLicense){
		DataSource dataSource = new DataSource();
		dataSource.setDatasourceversion(1);
		dataSource.setIscurrent(1);
		dataSource.setUnpublished(Util.booleanToInt(request.getUnpublished()));
		dataSource.setName(request.getName());
		dataSource.setVisibility(request.getVisibility());
		dataSource.setCopyright(request.getVisibility()); // potrebbe essere nullo
		dataSource.setDisclaimer(request.getLicense() != null ? request.getLicense().getDisclaimer() : null);
		dataSource.setRegistrationdate(Util.getNow());
		dataSource.setRequestername(request.getRequestername());
		dataSource.setRequestersurname(request.getRequestersurname());
		dataSource.setRequestermail(request.getRequestermail());
		dataSource.setPrivacyacceptance(Util.booleanToInt(true));
		dataSource.setIcon(request.getIcon()); // potrebbe essere nullo
		dataSource.setIsopendata(request.getOpenData() != null ? Util.booleanToInt(true) : Util.booleanToInt(false));
		dataSource.setOpendataexternalreference(request.getOpenData() != null ? request.getOpenData().getOpendataexternalreference() : null);
		dataSource.setOpendataauthor(request.getOpenData() != null ? request.getOpenData().getOpendataauthor() : null);
		dataSource.setOpendataupdatedate(request.getOpenData() != null ?  Util.dateStringToTimestamp(request.getOpenData().getOpendataupdatedate()) : null);
		dataSource.setOpendatalanguage(request.getOpenData() != null ? request.getOpenData().getOpendatalanguage() : null);
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
	private Long insertDcat(DcatRequest dcatRequest){
		
		if(dcatRequest == null) return null;
		
		// ----------------------------------------------------------------------------------
		// INSERT DCAT
		// yucca_dcat --> inserimento record solo se non è stato indicato il campo dcat.idDcat. 
		// Bisogna inserire i campi nei nomi corrispondenti.
		// ----------------------------------------------------------------------------------
		if(dcatRequest.getIdDcat() == null){
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
	private Integer insertLicense(LicenseRequest licenseRequest){
		
		if (licenseRequest == null) {
			return null;
		}
		
		// ----------------------------------------------------------------------------------
		//INSERT LICENSE
		// 	yucca_d_license --> inserimento record solo se non è stato indicato il campo license.idLicense 
		// Bisogna inserire i campi nei nomi corrispondenti.
		// ----------------------------------------------------------------------------------
		if(licenseRequest != null && licenseRequest.getIdLicense() == null){
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
	private void checkInternalSmartObject(PostStreamRequest request, Integer idSoType) throws BadRequestException, NotFoundException{
		//   INTERNAL SO TYPE
		if(Type.INTERNAL.id() == idSoType){
			ServiceUtil.checkMandatoryParameter(request.getInternalquery(), "internalquery mandatory (only for internal smartobject)");
			ServiceUtil.checkList(request.getInternalStreams(), "InternalStreams mandatory (only for internal smartobject)");
			for (InternalStreamRequest internalStream : request.getInternalStreams()) {
				ServiceUtil.checkMandatoryParameter(internalStream.getIdStream(), "internalStream => idStream");
				ServiceUtil.checkMandatoryParameter(internalStream.getStreamAlias(), "internalStream => streamAlias");
			}
		}

		//   NOT INTERNAL SO TYPE
		if(Type.INTERNAL.id() != idSoType && request.getInternalquery() != null){
			throw new BadRequestException(Errors.INCORRECT_VALUE, "internalquery: is not internal smartobject.");
		}
	}
	
	/**
	 * 
	 * @param request
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkMandatories(PostStreamRequest request) throws BadRequestException, NotFoundException{
		ServiceUtil.checkMandatoryParameter(request.getStreamname(), "streamname");
		ServiceUtil.checkMandatoryParameter(request.getName(), "name");
		ServiceUtil.checkMandatoryParameter(request.getSavedata(), "savedata");
		ServiceUtil.checkMandatoryParameter(request.getVisibility(), "visibility");
		ServiceUtil.checkMandatoryParameter(request.getRequestername(), "requestername");
		ServiceUtil.checkMandatoryParameter(request.getRequestersurname(), "requestersurname");
		ServiceUtil.checkMandatoryParameter(request.getRequestermail(), "requestermail");
		ServiceUtil.checkMandatoryParameter(request.getUnpublished(), "unpublished");
		ServiceUtil.checkList(request.getTags(), "tags");
		
		if(request.getDcat() != null && request.getDcat().getIdDcat() == null){
			ServiceUtil.checkMandatoryParameter(request.getDcat().getDcatnomeorg(), "dcatnomeorg");
			ServiceUtil.checkMandatoryParameter(request.getDcat().getDcatemailorg(), "dcatemailorg");
			ServiceUtil.checkMandatoryParameter(request.getDcat().getDcatrightsholdername(), "dcatrightsholdername");
		}
		
		if(request.getLicense() != null && request.getLicense().getIdLicense() == null){
			ServiceUtil.checkMandatoryParameter(request.getLicense().getLicensecode(), "licensecode");
			ServiceUtil.checkMandatoryParameter(request.getLicense().getDescription(), "license => description");
		}
		
	}
	
	/**
	 * 
	 * @param request
	 * @param idSoType
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkFeedTweetSmartobject(PostStreamRequest request, Integer idSoType) throws BadRequestException, NotFoundException{
		
		if(Type.FEED_TWEET.id() == idSoType){
			ServiceUtil.checkMandatoryParameter(request.getTwitterInfoRequest(), "twitterInfo mandatory (only for Feed Tweet smartobject)");
			ServiceUtil.checkMandatoryParameter(request.getTwitterInfoRequest().getTwtquery(), "twitterInfo => Twtquery");
			if(request.getComponents() != null){
				throw new BadRequestException(Errors.INCORRECT_VALUE, "Component not allowed for Feed Tweet smartobject!");
			}
		}

		if(Type.FEED_TWEET.id() != idSoType && request.getTwitterInfoRequest() != null){
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
	private void checkComponents(PostStreamRequest request, Integer idSoType) throws NotFoundException, BadRequestException{
		if(Type.FEED_TWEET.id() != idSoType){
			ServiceUtil.checkList(request.getComponents());
			for (ComponentRequest component : request.getComponents()) {
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
	 * 
	 * @param request
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkVisibility(PostStreamRequest request) throws BadRequestException, NotFoundException{

		ServiceUtil.checkValue("visibility", request.getVisibility(), StreamVisibility.PRIVATE.code(), StreamVisibility.PUBLIC.code());
		
		// PRIVATE
		if(StreamVisibility.PRIVATE.code().equals(request.getVisibility())){
			if(request.getLicense() != null){
				throw new BadRequestException(Errors.INCORRECT_VALUE, "License only for public visibility, provided: " + request.getVisibility());
			}
			if(request.getOpenData() != null){
				throw new BadRequestException(Errors.INCORRECT_VALUE, "Opendata only for public visibility, provided: " + request.getVisibility());
			}
			if(request.getSharingTenants() != null){
				for (SharingTenantRequest sharingTenant : request.getSharingTenants() ) {
					ServiceUtil.checkMandatoryParameter(sharingTenant.getIdTenant(), "sharingTenant => idTenant");
					ServiceUtil.checkValue("dataOptions", sharingTenant.getDataOptions(), DataOption.READ.id(), DataOption.READ_AND_SUBSCRIBE.id(), DataOption.READ_AND_USE.id(), DataOption.WRITE.id());
					ServiceUtil.checkValue("manageOptions", sharingTenant.getManageOptions(), ManageOption.EDIT_METADATA.id(), ManageOption.LIFE_CYCLE_HANDLING.id(), ManageOption.NO_RIGHT.id());
					Tenant selectedTenant = tenantMapper.selectTenantByidTenant(sharingTenant.getIdTenant());
					ServiceUtil.checkIfFoundRecord(selectedTenant, "Sharing Tenant with [ " + sharingTenant.getIdTenant() + " ] not found!");
				}
			}
		}

		// PUBLIC
		if(StreamVisibility.PUBLIC.code().equals(request.getVisibility())){
			
			if (request.getSharingTenants() != null) {
				throw new BadRequestException(Errors.INCORRECT_VALUE, "Sharing Tenants permitted only for private visibility!");
			}
			if (request.getCopyright() != null) {
				throw new BadRequestException(Errors.INCORRECT_VALUE, "Copyright permitted only for private visibility!");
			}
		}	
	}

	/**
	 * 
	 * @param request
	 * @throws BadRequestException
	 */
	private void checkOpendataupdatedate(PostStreamRequest request) throws BadRequestException{
		if(request.getOpenData() != null && request.getOpenData().getOpendataupdatedate() != null && 
				!Util.isValidDateFormat(request.getOpenData().getOpendataupdatedate())){
			throw new BadRequestException(Errors.INCORRECT_VALUE, "Aspected format date [ " + Constants.CLIENT_FORMAT_DATE
					+ " ]: opendataupdatedate is " + request.getOpenData().getOpendataupdatedate());
		}
	}
	
	/**
	 * 
	 * @param authorizedUser
	 * @param tenantCode
	 * @throws UnauthorizedException
	 */
	private void checkAuthTenant(JwtUser authorizedUser, String tenantCode) throws UnauthorizedException{
		List<String> userAuthorizedTenantCodeList = ServiceUtil.getTenantCodeListFromUser(authorizedUser);
		for (String authTenant : userAuthorizedTenantCodeList) {
			if(authTenant.equals(tenantCode))return;
		}
		throw new UnauthorizedException(Errors.UNAUTHORIZED, "not authorized tenantCode [" + tenantCode + "]");
	}

	/**
	 * 
	 * @param idSubdomain
	 * @return
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	private Subdomain checkSubdomain(Integer idSubdomain) throws NotFoundException, BadRequestException{
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
	private Tenant checkTenant(Integer idTenant, String organizationCode) throws NotFoundException, BadRequestException{
		ServiceUtil.checkMandatoryParameter(idTenant, "idTenant");
		Tenant tenant = tenantMapper.selectTenantByIdAndOrgCodeCode(idTenant, organizationCode);
		ServiceUtil.checkIfFoundRecord(tenant, "tenant not found idTenant [" + idTenant + "], organizationcode [" + organizationCode + "] ");
		return tenant;
	}

	/**
	 * 
	 * @param idTenant
	 * @throws UnauthorizedException
	 */
	private void checkMaxNumStream(Integer idTenant) throws UnauthorizedException{
		Bundles bundles = bundlesMapper.selectBundlesByTenant(idTenant);
		if(bundles.getMaxstreamsnum() != -1){
			Integer countOfTenantStream = streamMapper.selectCountOfTenantStream(idTenant);
			if(countOfTenantStream+1 > bundles.getMaxstreamsnum()){
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
	private void checkStreamCode(String streamcode, Integer idSmartobject) throws BadRequestException{
		ServiceUtil.checkCode(streamcode, "streamcode");
		Stream stream = streamMapper.selectStreamByStreamcodeAndIdSmartObject(streamcode, idSmartobject);
		if(stream != null){
			throw new BadRequestException(Errors.INTEGRITY_VIOLATION, "There is another stream with streamcode [ " + streamcode + " ] and idSmartObject [ " + idSmartobject + " ]");
		}
	}

}
