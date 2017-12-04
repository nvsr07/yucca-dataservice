package org.csi.yucca.adminapi.util;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.exception.UnauthorizedException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.mapper.ComponentMapper;
import org.csi.yucca.adminapi.mapper.DataSourceMapper;
import org.csi.yucca.adminapi.mapper.DatasetMapper;
import org.csi.yucca.adminapi.mapper.DcatMapper;
import org.csi.yucca.adminapi.mapper.LicenseMapper;
import org.csi.yucca.adminapi.mapper.SequenceMapper;
import org.csi.yucca.adminapi.mapper.TenantMapper;
import org.csi.yucca.adminapi.model.Component;
import org.csi.yucca.adminapi.model.DataSource;
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.Dcat;
import org.csi.yucca.adminapi.model.License;
import org.csi.yucca.adminapi.model.Smartobject;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.model.TenantDataSource;
import org.csi.yucca.adminapi.request.ComponentRequest;
import org.csi.yucca.adminapi.request.DcatRequest;
import org.csi.yucca.adminapi.request.IDataSourceRequest;
import org.csi.yucca.adminapi.request.IVisibility;
import org.csi.yucca.adminapi.request.LicenseRequest;
import org.csi.yucca.adminapi.request.OpenDataRequest;
import org.csi.yucca.adminapi.request.SharingTenantRequest;
import org.csi.yucca.adminapi.request.SmartobjectRequest;
import org.csi.yucca.adminapi.response.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

public class ServiceUtil {

	private static final String SORT_PROPERTIES_SEPARATOR = ",";
	private static final String DESC_CHAR = "-";
	public static final String MULTI_SUBDOMAIN_PATTERN = "^[\\S]*$";
	public static final String UUID_PATTERN         = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
	public static final String NOT_DEVICE_PATTERN   = "^[a-zA-Z0-9-]{5,100}$";
	public static final String ALPHANUMERIC_PATTERN = "^[a-zA-Z0-9]*$";
	public static final String ALPHANUMERICOrUnderscore_PATTERN = "^[a-zA-Z0-9_]*$";
	public static final String COMPONENT_NAME_PATTERN =  "(.)*[\u00C0-\u00F6\u00F8-\u00FF\u0020]+(.)*|^[0-9]*$";
	
	public static final String MULTI_SUBDOMAIN_LANG_EN = "";
	public static final String MULTI_SUBDOMAIN_LANG_IT = "";
	public static final Integer MULTI_SUBDOMAIN_ID_DOMAIN = -1;  
	public static final Integer DATASOURCE_VERSION = 1;
	public static final Integer SINCE_VERSION = 1;
	public static final Integer TENANT_DATA_SERVICE_DATA_OPTIONS = 3;
	public static final Integer TENANT_DATA_SERVICE_MANAGE_OPTIONS = 2;
	public static final String STREAM_DATASET_PREFIX_CODE = "ds_";
	public static final String BINARY_DATASET_PREFIX_CODE = "bn_";
	public static final String API_SUBTYPE_WEBSOCKET = "websocket";
	public static final String API_SUBTYPE_MQTT = "mqtt";
	public static final String API_SUBTYPE_ODATA = "odata";
	public static final String API_CODE_PREFIX_WEBSOCKET = "ws_";
	public static final String API_CODE_PREFIX_MQTT = "mqtt_";
	
	
	/**
	 * 
	 * @param listSharingTenantRequest
	 * @param idDataSource
	 * @param now
	 * @param dataOptions
	 * @param manageOptions
	 * @param tenantMapper
	 * @throws Exception
	 */
	public static void insertSharingTenants(List<SharingTenantRequest> listSharingTenantRequest, Integer idDataSource,
			Timestamp now, Integer dataOptions, Integer manageOptions, TenantMapper tenantMapper) throws Exception{

		for (SharingTenantRequest sharingTenantRequest : listSharingTenantRequest) {
			TenantDataSource tenantDataSource = new TenantDataSource();
			tenantDataSource.setIdDataSource(idDataSource);
			tenantDataSource.setDatasourceversion(ServiceUtil.DATASOURCE_VERSION);
			tenantDataSource.setIdTenant(sharingTenantRequest.getIdTenant());
			tenantDataSource.setIsactive(Util.booleanToInt(true));
			tenantDataSource.setIsmanager(Util.booleanToInt(false));
			tenantDataSource.setActivationdate(now);
			tenantDataSource.setManagerfrom(now);
			tenantDataSource.setDataoptions(dataOptions==null?sharingTenantRequest.getDataOptions():dataOptions);
			tenantDataSource.setManageoptions(manageOptions==null?sharingTenantRequest.getManageOptions():manageOptions);
			tenantMapper.insertTenantDataSource(tenantDataSource);
		}
	}

	/**
	 * 
	 * @param listSharingTenantRequest
	 * @param idDataSource
	 * @param now
	 * @param tenantMapper
	 * @throws Exception
	 */
	public static void insertSharingTenants(List<SharingTenantRequest> listSharingTenantRequest, Integer idDataSource, Timestamp now, TenantMapper tenantMapper)throws Exception{
		insertSharingTenants(listSharingTenantRequest, idDataSource, now, null, null, tenantMapper);
	}

	
	/**
	 * 
	 * @param idTenant
	 * @param idDataSource
	 * @param now
	 * @param tenantMapper
	 * @throws Exception
	 */
	public static void insertTenantDataSource(Integer idTenant, Integer idDataSource, Timestamp now, TenantMapper tenantMapper)throws Exception {
		TenantDataSource tenantDataSource = new TenantDataSource();
		tenantDataSource.setIdDataSource(idDataSource);
		tenantDataSource.setDatasourceversion(ServiceUtil.DATASOURCE_VERSION);
		tenantDataSource.setIdTenant(idTenant);
		tenantDataSource.setIsactive(Util.booleanToInt(true));
		tenantDataSource.setIsmanager(Util.booleanToInt(true));
		tenantDataSource.setActivationdate(now);
		tenantDataSource.setManagerfrom(now);
		tenantDataSource.setDataoptions(ServiceUtil.TENANT_DATA_SERVICE_DATA_OPTIONS);
		tenantDataSource.setManageoptions(ServiceUtil.TENANT_DATA_SERVICE_MANAGE_OPTIONS);
		tenantMapper.insertTenantDataSource(tenantDataSource);
	}
	
	/**
	 * 
	 * @param listComponentRequest
	 * @param idDataSource
	 * @param datasourceVersion
	 * @param sinceVersion
	 * @param isKey
	 * @param componentMapper
	 * @throws Exception
	 */
	public static void insertComponents(List<ComponentRequest> listComponentRequest, Integer idDataSource, 
			Integer datasourceVersion, Integer sinceVersion, Integer isKey, ComponentMapper componentMapper)throws Exception{
		
		for (ComponentRequest componentRequest : listComponentRequest) {

			Component component = new Component();
			
			component.name(componentRequest.getName());
			component.alias(componentRequest.getAlias());
			component.inorder(componentRequest.getInorder());
			component.tolerance(componentRequest.getTolerance());
			component.idPhenomenon(componentRequest.getIdPhenomenon());
			component.idDataType(componentRequest.getIdDataType());
			component.idMeasureUnit(componentRequest.getIdMeasureUnit());
			component.sourcecolumn(componentRequest.getSourcecolumn());
			component.sourcecolumnname(componentRequest.getSourcecolumnname());
			component.required( Util.booleanToInt(componentRequest.getRequired()));
			component.setIskey(isKey==null ? Util.booleanToInt(componentRequest.getIskey()) : isKey);
			component.setSinceVersion(ServiceUtil.SINCE_VERSION);			
			component.setIdDataSource(idDataSource);
			component.setDatasourceversion(datasourceVersion);
			componentMapper.insertComponent(component);
		}
	}

	/**
	 * 
	 * @param listComponentRequest
	 * @param idDataSource
	 * @param datasourceVersion
	 * @param sinceVersion
	 * @param componentMapper
	 * @throws Exception
	 */
	public static void insertComponents(List<ComponentRequest> listComponentRequest, Integer idDataSource, 
			Integer datasourceVersion, Integer sinceVersion, ComponentMapper componentMapper)throws Exception{
		insertComponents(listComponentRequest, idDataSource, datasourceVersion, sinceVersion, null, componentMapper);

	}

	
	/**
	 * 
	 * @param idBinaryDataSource
	 * @param componentMapper
	 * @throws Exception
	 */
	public static void insertBinaryComponents(Integer idBinaryDataSource, ComponentMapper componentMapper) throws Exception{
		componentMapper.insertComponent(new Component().idDataType(DataType.LONG.id()).name("idBinary").alias("Id").idComponent(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(new Component().idDataType(DataType.STRING.id()).name("filenameBinary").alias("File").idComponent(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(new Component().idDataType(DataType.STRING.id()).name("aliasNameBinary").alias("File").idComponent(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(new Component().idDataType(DataType.STRING.id()).name("sizeBinary").alias("File Size").idComponent(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(new Component().idDataType(DataType.DATE_TIME.id()).name("insertDateBinary").alias("Insert Date").idComponent(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(new Component().idDataType(DataType.DATE_TIME.id()).name("lastUpdateDateBinary").alias("Last Update").idComponent(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(new Component().idDataType(DataType.STRING.id()).name("contentTypeBinary").alias("Content Type").idComponent(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(new Component().idDataType(DataType.STRING.id()).name("urlDownloadBinary").alias("Download url").idComponent(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(new Component().idDataType(DataType.STRING.id()).name("metadataBinary").alias("Metadata").idComponent(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
	}
	
	/**
	 * 
	 * @param idDataSource
	 * @param dataSourceVersion
	 * @param datasetName
	 * @param idDatasetSubtype
	 * @param importFileData
	 * @param datasetMapper
	 * @param sequenceMapper
	 * @return
	 * @throws Exception
	 */
	public static Dataset insertDataset(Integer idDataSource, Integer dataSourceVersion, String datasetName,  Integer idDatasetSubtype, 
			String importFileData, Integer dataSourceVersionBinary, Integer idDataSourceBinary,String jdbcdburl,String jdbcdbname,
			String jdbcdbtype, String jdbctablename, DatasetMapper datasetMapper, SequenceMapper sequenceMapper) throws Exception{
		return insertDataset(
				true, 				// saveData
				idDataSource, 
				null, 				// idSoType
				dataSourceVersion, 
				datasetName, 
				idDatasetSubtype,
				importFileData,	
				dataSourceVersionBinary, 
				idDataSourceBinary,
				jdbcdburl,
				jdbcdbname,
				jdbcdbtype,
				jdbctablename,				
				datasetMapper, 
				sequenceMapper);
	}
	
	/**
	 * 
	 * @param saveData
	 * @param idDataSource
	 * @param dataSourceVersion
	 * @param datasetName
	 * @param datasetCode
	 * @param idDatasetSubtype
	 * @param datasetMapper
	 * @param sequenceMapper
	 * @return
	 * @throws Exception
	 */
	public static Dataset insertDataset(Integer idDataSource, Integer dataSourceVersion, String datasetName,  Integer idDatasetSubtype, DatasetMapper datasetMapper, SequenceMapper sequenceMapper) throws Exception{
		return insertDataset(
				true, 				// saveData
				idDataSource, 
				null, 				// idSoType
				dataSourceVersion, 
				datasetName, 
				idDatasetSubtype,
				null,               // importFiledata
				null, 				// dataSourceVersionBinary, 
				null, 				// idDataSourceBinary,
				null, 				// jdbcdburl,
				null, 				// jdbcdbname,
				null, 				// jdbcdbtype,
				null, 				// jdbctablename,				
				datasetMapper, 
				sequenceMapper);
	}

	/**
	 * 
	 * @param saveData
	 * @param idDataSource
	 * @param idSoType
	 * @param dataSourceVersion
	 * @param datasetName
	 * @param datasetCode
	 * @param datasetMapper
	 * @param sequenceMapper
	 * @return
	 * @throws Exception
	 */
	public static Dataset insertDataset(boolean saveData, Integer idDataSource, Integer idSoType, Integer dataSourceVersion, String datasetName,  DatasetMapper datasetMapper, SequenceMapper sequenceMapper) throws Exception{
		return insertDataset(
				saveData, 
				idDataSource, 
				idSoType, 
				dataSourceVersion, 
				datasetName,  
				null, 				// idDatasetSubtype
				null,               // importFiledata
				null, 				// dataSourceVersionBinary, 
				null, 				// idDataSourceBinary,
				null, 				// jdbcdburl,
				null, 				// jdbcdbname,
				null, 				// jdbcdbtype,
				null, 				// jdbctablename,
				datasetMapper, 
				sequenceMapper);
	}
	
	/**
	 * 
	 * @param saveData
	 * @param idDataSource
	 * @param idSoType
	 * @param dataSourceVersion
	 * @param datasetName
	 * @param datasetCode
	 * @param idDatasetSubtype
	 * @param datasetMapper
	 * @return
	 */
	private static Dataset insertDataset(boolean saveData, Integer idDataSource, Integer idSoType, Integer dataSourceVersion, 
			String datasetName, Integer idDatasetSubtype, String importFileType, Integer dataSourceVersionBinary, 
			Integer idDataSourceBinary, String jdbcdburl, String jdbcdbname, String jdbcdbtype, String jdbctablename,
			DatasetMapper datasetMapper, SequenceMapper sequenceMapper) throws Exception{
		
		Dataset checkDataSet = datasetMapper.selectDataSet(idDataSource, dataSourceVersion);

		if (checkDataSet != null) {
			return checkDataSet;
		}
		
		if (saveData) {
			Integer iddataset = sequenceMapper.selectDatasetSequence();

			Dataset dataset = new Dataset();

			dataset.setIddataset(iddataset);
			dataset.setIdDataSource(idDataSource);
			dataset.setDatasourceversion(dataSourceVersion);
			dataset.setDatasetname(datasetName);
			setDatasetcode(dataset, idDatasetSubtype, datasetName, iddataset);			
			dataset.setDescription("Dataset " + datasetName);
			dataset.setIdDatasetType(DatasetType.DATASET.id());
			setIdDatasetSubtype(dataset, idSoType, idDatasetSubtype);			
			dataset.setAvailablespeed(Util.booleanToInt(true));
			dataset.setIstransformed(Util.booleanToInt(false));
			dataset.setImportfiletype(importFileType);

			dataset.setIdDataSourceBinary(idDataSourceBinary);
			dataset.setDatasourceversionBinary(dataSourceVersionBinary);
			
			
			datasetMapper.insertDataset(dataset);
			return dataset;
		}
		
		return null;
	}

	/**
	 * 
	 * @param dataset
	 * @param idSoType
	 * @param idDatasetSubtype
	 */
	private static void setIdDatasetSubtype(Dataset dataset, Integer idSoType, Integer idDatasetSubtype){
		dataset.setIdDatasetSubtype(idDatasetSubtype);
		if(idSoType != null){
			if (Type.FEED_TWEET.id() == idSoType) {
				dataset.setIdDatasetSubtype(DatasetSubtype.SOCIAL.id());
			} else {
				dataset.setIdDatasetSubtype(DatasetSubtype.STREAM.id());
			}
		}
	}
	
	/**
	 * 
	 * @param dataset
	 * @param idDatasetSubtype
	 * @param datasetName
	 * @param iddataset
	 */
	private static void setDatasetcode(Dataset dataset, Integer idDatasetSubtype, String datasetName, Integer iddataset){
		if (DatasetSubtype.BINARY.id() == idDatasetSubtype) {
			dataset.setDatasetcode(BINARY_DATASET_PREFIX_CODE + Util.cleanStringCamelCase(datasetName) + "_" + iddataset);
		}
		else if(DatasetSubtype.BULK.id() == idDatasetSubtype){
			dataset.setDatasetcode(Util.cleanStringCamelCase(datasetName) + "_" + iddataset);
		}
		else{
			dataset.setDatasetcode(STREAM_DATASET_PREFIX_CODE + Util.cleanStringCamelCase(datasetName) + "_" + iddataset);
		}
	}
	
	
	/**
	 * 
	 * @param dcatRequest
	 * @return
	 */
	public static Long insertDcat(DcatRequest dcatRequest, DcatMapper dcatMapper) throws Exception{

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
	 * @param licenseMapper
	 * @return
	 * @throws Exception
	 */
	public static Integer insertLicense(LicenseRequest licenseRequest, LicenseMapper licenseMapper) throws Exception{

		if (licenseRequest == null) {
			return null;
		}

		// ----------------------------------------------------------------------------------
		// INSERT LICENSE
		// yucca_d_license --> inserimento record solo se non è stato indicato
		// il campo license.idLicense
		// Bisogna inserire i campi nei nomi corrispondenti.
		// ----------------------------------------------------------------------------------
		if (licenseRequest.getIdLicense() == null) {
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
	 * @param idOrganization
	 * @param idStatus
	 * @param dataSourceMapper
	 * @return
	 * @throws Exception
	 */
	public static Integer insertDataSource(IDataSourceRequest request, Integer idOrganization, 
			Integer idStatus, DataSourceMapper dataSourceMapper) throws Exception{
		return 	insertDataSource(request, idOrganization, null, null, idStatus, dataSourceMapper);
	}
	
	/**
	 * 
	 * @param request
	 * @param idOrganization
	 * @param idDcat
	 * @param idLicense
	 * @param idStatus
	 * @param dataSourceMapper
	 * @return
	 * @throws Exception
	 */
	public static Integer insertDataSource(IDataSourceRequest request, Integer idOrganization, 
			Long idDcat, Integer idLicense, Integer idStatus, DataSourceMapper dataSourceMapper) throws Exception{
		
		DataSource dataSource = new DataSource();
		dataSource.setDatasourceversion(DATASOURCE_VERSION);
		dataSource.setIscurrent(Util.booleanToInt(true));
		dataSource.setUnpublished(Util.booleanToInt(request.getUnpublished()));
		dataSource.setName(request.getName());
		dataSource.setVisibility(request.getVisibility());
		dataSource.setCopyright(request.getVisibility());
		dataSource.setDisclaimer(request.getDisclaimer());		
		dataSource.setRegistrationdate(Util.getNow());
		dataSource.setRequestername(request.getRequestername());
		dataSource.setRequestersurname(request.getRequestersurname());
		dataSource.setRequestermail(request.getRequestermail());
		dataSource.setPrivacyacceptance(Util.booleanToInt(request.getPrivacyacceptance()));
		dataSource.setIcon(request.getIcon());		
		dataSource.setIsopendata(request.getOpenData() != null ? Util.booleanToInt(true) : Util.booleanToInt(false));
		dataSource.setOpendataexternalreference(request.getOpenData() != null ? request.getOpenData().getOpendataexternalreference() : null);
		dataSource.setOpendataauthor(request.getOpenData() != null ? request.getOpenData().getOpendataauthor() : null);
		dataSource.setOpendataupdatedate(request.getOpenData() != null? Util.dateStringToTimestamp(request.getOpenData().getOpendataupdatedate()) : null);
		dataSource.setOpendatalanguage(request.getOpenData() != null ? request.getOpenData().getOpendatalanguage() : null);
		dataSource.setLastupdate(request.getOpenData() != null ? request.getOpenData().getLastupdate() : null);
		dataSource.setIdOrganization(idOrganization);		
		dataSource.setIdSubdomain(request.getIdSubdomain());
		dataSource.setIdStatus(idStatus);		
		
		dataSource.setIdDcat(idDcat);
		dataSource.setIdLicense(idLicense);
		
		dataSourceMapper.insertDataSource(dataSource);
		
		return dataSource.getIdDataSource();
	}
	
	
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	public static ServiceResponse buildResponse(Object object){
		return ServiceResponse.build().object(object);
	}
	
	/**
	 * 
	 * @param base64image
	 * @return
	 */
	public static ServiceResponse buildResponseImage(String base64image) {
		return ServiceResponse.build().image(base64image);
	}
	
	/**
	 * 
	 * @param request
	 * @param tenantMapper
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	public static void checkVisibility(IVisibility request, TenantMapper tenantMapper) throws BadRequestException, NotFoundException {
		checkVisibility(request.getVisibility(), request.getLicense(), request.getOpenData(), 
				request.getSharingTenants(), request.getCopyright(), tenantMapper);
	}
	
	/**
	 * 
	 * @param idTenant
	 * @param organizationCode
	 * @param tenantMapper
	 * @return
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	public static Tenant checkTenant(Integer idTenant, String organizationCode, TenantMapper tenantMapper) throws NotFoundException, BadRequestException {
		
		ServiceUtil.checkMandatoryParameter(idTenant, "idTenant");
		
		Tenant tenant = tenantMapper.selectTenantByIdAndOrgCodeCode(idTenant, organizationCode);
		
		ServiceUtil.checkIfFoundRecord(tenant, "tenant not found idTenant [" + idTenant + "], organizationcode [" + organizationCode + "] ");
		
		return tenant;
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
	private static void checkVisibility(String visibility, LicenseRequest license, OpenDataRequest openData, 
			List<SharingTenantRequest> sharingTenants, String copyright, TenantMapper tenantMapper) throws BadRequestException, NotFoundException {

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
					ServiceUtil.checkValue("dataOptions", sharingTenant.getDataOptions(), DataOption.READ.id(), DataOption.READ_AND_SUBSCRIBE.id(), DataOption.READ_AND_USE.id(), DataOption.WRITE.id());
					ServiceUtil.checkValue("manageOptions", sharingTenant.getManageOptions(),ManageOption.EDIT_METADATA.id(), ManageOption.LIFE_CYCLE_HANDLING.id(), ManageOption.NO_RIGHT.id());
					Tenant selectedTenant = tenantMapper.selectTenantByidTenant(sharingTenant.getIdTenant());
					ServiceUtil.checkIfFoundRecord(selectedTenant, "Sharing Tenant with [ " + sharingTenant.getIdTenant() + " ] not found!");
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
	 * @param licenseRequest
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	public static void checkLicense(LicenseRequest licenseRequest) throws BadRequestException, NotFoundException{
		if (licenseRequest != null && licenseRequest.getIdLicense() == null) {
			ServiceUtil.checkMandatoryParameter(licenseRequest.getLicensecode(), "licensecode");
			ServiceUtil.checkMandatoryParameter(licenseRequest.getDescription(), "license => description");
		}
	}
	
	/**
	 * 
	 * @param authorizedUser
	 * @param idTenant
	 * @param tenantMapper
	 * @throws UnauthorizedException
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	public static void checkAuthTenant(JwtUser authorizedUser, Integer idTenant, TenantMapper tenantMapper) throws UnauthorizedException, NotFoundException, BadRequestException{
		
		checkMandatoryParameter(idTenant, "idTenant");
		
		Tenant tenant = tenantMapper.selectTenantByidTenant(idTenant);
		
		checkIfFoundRecord(tenant);
		
		checkAuthTenant(authorizedUser, tenant.getTenantcode());
	}
	
	/**
	 * 
	 * @param authorizedUser
	 * @param tenantCode
	 * @throws UnauthorizedException
	 */
	public static void checkAuthTenant(JwtUser authorizedUser, String tenantCode) throws UnauthorizedException{
		
		if (tenantCode == null) return;
		
		List<String> userAuthorizedTenantCodeList = ServiceUtil.getTenantCodeListFromUser(authorizedUser);

		for (String authTenant : userAuthorizedTenantCodeList) {
			if(authTenant.equals(tenantCode))return;
		}
		
		throw new UnauthorizedException(Errors.UNAUTHORIZED, "not authorized tenantCode [" + tenantCode + "]");
	}
	
	
	/**
	 * 
	 * @param authorizedUser
	 * @return
	 */
	public static List<String> getTenantCodeListFromUser(JwtUser authorizedUser){
		
		if(authorizedUser.getRoles() == null || authorizedUser.getRoles().isEmpty()) return null;
		
		List<String> tenantCodeList = new ArrayList<>();
		for (String role : authorizedUser.getRoles()) {
			if(role.contains("_subscriber")){
				tenantCodeList.add(role.substring(0, role.lastIndexOf("_")));
			}
		}
		
		return tenantCodeList;
	}

	
	/**
	 * 
	 * @param organizationcode
	 * @return
	 */
	public static String getDefaultInternalSocode(String organizationcode){
		return "SOinternal" + organizationcode;
	}
	
	/**
	 * 
	 * @param TYPE
	 * @param smartobjectRequest
	 * @return
	 */
	public static boolean isType(Type TYPE, SmartobjectRequest smartobjectRequest){
		return isType(TYPE, smartobjectRequest.getIdSoType());
	}

	/**
	 * 
	 * @param TYPE
	 * @param idSoType
	 * @return
	 */
	public static boolean isType(Type TYPE, Integer idSoType){
		return TYPE.id() == idSoType;
	}

	/**
	 * 
	 * @param TYPE
	 * @param smartobject
	 * @return
	 */
	public static boolean isType(Type TYPE, Smartobject smartobject){
		return TYPE.id() == smartobject.getIdSoType();
	}
	
	/**
	 * 
	 * @param count
	 * @throws NotFoundException
	 */
	public static void checkCount(int count)throws NotFoundException{
		if (count == 0 ) {
			throw new NotFoundException(Errors.RECORD_NOT_FOUND);
		}
	}

	/**
	 * 
	 * @param object
	 * @throws NotFoundException
	 */
	public static void checkIfFoundRecord(Object object)throws NotFoundException{
		checkIfFoundRecord(object, null);
	}

	/**
	 * 
	 * @param object
	 * @param arg
	 * @throws NotFoundException
	 */
	public static void checkIfFoundRecord(Object object, String arg)throws NotFoundException{
		if (object == null ) {
			
			if (arg != null) {
				throw new NotFoundException(Errors.RECORD_NOT_FOUND, arg);
			}
			
			throw new NotFoundException(Errors.RECORD_NOT_FOUND);
		}
	}
	
	/**
	 * 
	 * @param modelList
	 * @param responseClass
	 * @return
	 * @throws Exception
	 */
	public static <T> List<Response> getResponseList(List<T> modelList, Class<?> responseClass) throws Exception {
		List<Response> responsesList = new ArrayList<Response>();

		for (T model : modelList) {
			Response response = (Response) responseClass.newInstance();
			BeanUtils.copyProperties(model, response);
			responsesList.add(response);
		}

		return responsesList;
	}

	/**
	 * 
	 * @param object
	 * @param fieldName
	 */
	public static void checkNullInteger(Object object, String fieldName) {
		try {

			Field fieldID = object.getClass().getField(fieldName);

			Integer value = (Integer) fieldID.get(object);

			if (value == null) {
				fieldID.set(object, 0);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isAlphaNumeric(String s){
	    return s.matches(ALPHANUMERIC_PATTERN);
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isAlphaNumericOrUnderscore(String s){
	    return s.matches(ALPHANUMERICOrUnderscore_PATTERN);
	}

	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean matchUUIDPattern(String s){
	    return s.matches(UUID_PATTERN);
	}

//	public static boolean matchComponentNamePattern(String s){
//	    return s.matches(COMPONENT_NAME_PATTERN);
//	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean matchNotDevicePattern(String s){
	    return s.matches(NOT_DEVICE_PATTERN);
	}

	/**
	 * 
	 * @param s
	 * @param fieldName
	 * @throws BadRequestException
	 */
//	public static void checkComponentName(String s) throws BadRequestException{
//		if (!matchComponentNamePattern(s)){
//			throw new BadRequestException(Errors.INCORRECT_VALUE, "received component [ " + s + " ]");
//		}
//	}

	public static void checkAphanumeric(String s, String fieldName) throws BadRequestException{
		if (!isAlphaNumeric(s)){
			throw new BadRequestException(Errors.ALPHANUMERIC_VALUE_REQUIRED, "received " + fieldName + " [ " + s + " ]");
		}

	}
	
	private static void checkAphanumericAndUnderscore(String s,
			String fieldName) throws BadRequestException {
		if (!isAlphaNumericOrUnderscore(s)){
			throw new BadRequestException(Errors.ALPHANUMERIC_VALUE_REQUIRED, "received " + fieldName + " [ " + s + " ]");
		}
	}

	
	/**
	 * 
	 * @param codeTenantStatus
	 * @throws BadRequestException
	 */
	public static void checkCodeTenantStatus(String codeTenantStatus) throws BadRequestException{
		
		for (Status status : Status.values()) {
			if(status.code().equals(codeTenantStatus))return;
		}
		
		List<String> listCodeTenantStatus = new ArrayList<>();
		for (Status status : Status.values()) {
			listCodeTenantStatus.add(status.code());
		}
		
		String message = "received " + "codeTenantStatus" + " [ " + codeTenantStatus + " ]. Possible values are: " 
				+ StringUtils.collectionToCommaDelimitedString(listCodeTenantStatus);
		
		throw new BadRequestException(Errors.INCORRECT_VALUE, message);
	}
	
	/**
	 * 
	 * @param idTenantType
	 * @throws BadRequestException
	 */
	public static void checkIdTenantType(Integer idTenantType) throws BadRequestException{
		
		for (TenantType type : TenantType.values()) {
			if(type.id() == idTenantType)return;
		}
		
		List<Integer> listIdTenantType = new ArrayList<>();
		for (TenantType type : TenantType.values()) {
			listIdTenantType.add(type.id());
		}
		
		String message = "received " + "idTenantType" + " [ " + idTenantType + " ]. Possible values are: " 
				+ StringUtils.collectionToCommaDelimitedString(listIdTenantType);
		
		throw new BadRequestException(Errors.INCORRECT_VALUE, message);
	}
	
	/**
	 * 
	 * @param userTypeAuth
	 * @param idTenantType
	 * @throws BadRequestException
	 */
	public static void checkTenantTypeAndUserTypeAuth(String userTypeAuth, Integer idTenantType) throws BadRequestException{
		
		if ( ( TenantType.DEFAULT.id() == idTenantType || TenantType.PLUS.id()    == idTenantType || 
			   TenantType.ZERO.id()    == idTenantType || TenantType.DEVELOP.id() == idTenantType  ) && 
				!UserTypeAuth.ADMIN.description().equals(userTypeAuth)) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, "tenant type " + tenantTypeDescription(idTenantType) + " [ " + idTenantType + " ] permitted only for " + UserTypeAuth.ADMIN.description() + " user");
		}
		
		if (UserTypeAuth.SOCIAL.description().equals(userTypeAuth) && TenantType.TRIAL.id() != idTenantType) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, 
		"user type [ " + UserTypeAuth.SOCIAL.description() + " ] permitted only for " + TenantType.TRIAL.description() + " [ " + TenantType.TRIAL.id() + " ] " + " idTenantType");
		}
	}
	
	/**
	 * 
	 * @param userTypeAuth
	 * @throws BadRequestException
	 */
	public static void checkUserTypeAuth(String userTypeAuth) throws BadRequestException{
		
		for (UserTypeAuth type : UserTypeAuth.values()) {
			if(type.description().equals(userTypeAuth))return;
		}

		List<String> listUserTypeAuth = new ArrayList<>();
		for (UserTypeAuth type : UserTypeAuth.values()) {
			listUserTypeAuth.add(type.description());
		}
		
		String message = "received " + "userTypeAuth" + " [ " + userTypeAuth + " ]. Possible values are: " 
				+ StringUtils.collectionToCommaDelimitedString(listUserTypeAuth);
		
		throw new BadRequestException(Errors.INCORRECT_VALUE, message);
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean containsWhitespace(String s){
		
		Pattern pattern = Pattern.compile("\\s");
		
		Matcher matcher = pattern.matcher(s);
		
		return matcher.find();
		
	}
	
	/**
	 * 
	 * @param s
	 * @param parameterName
	 * @throws BadRequestException
	 */
	public static void checkCode(String s, String parameterName) throws BadRequestException {
		checkMandatoryParameter(s, parameterName);
		checkWhitespace(s, parameterName);
		checkAphanumeric(s, parameterName);
	}

	/**
	 * 
	 * @param s
	 * @param parameterName
	 * @throws BadRequestException
	 */
	public static void checkTenantCode(String s, String parameterName) throws BadRequestException {
		checkMandatoryParameter(s, parameterName);
		checkWhitespace(s, parameterName);
		checkAphanumericAndUnderscore(s, parameterName);
	}

	

	/**
	 * 
	 * @param s
	 * @param parameterName
	 * @throws BadRequestException
	 */
	public static void checkWhitespace(String s, String parameterName) throws BadRequestException {
		if(containsWhitespace(s)){
			throw new BadRequestException(Errors.WHITE_SPACES, parameterName);
		}
	}
	
	/**
	 * 
	 * @param isEmpty
	 * @param parameterName
	 * @throws BadRequestException
	 */
	public static void checkMandatoryParameter(boolean isEmpty, String parameterName) throws BadRequestException {
		if (isEmpty) {
			throw new BadRequestException(Errors.MANDATORY_PARAMETER, parameterName);
		}
	}

	/**
	 * 
	 * @param parameterObj
	 * @param parameterName
	 * @throws BadRequestException
	 */
	public static void checkMandatoryParameter(Object parameterObj, String parameterName) throws BadRequestException {
		if (parameterObj == null) {
			throw new BadRequestException(Errors.MANDATORY_PARAMETER, parameterName);
		}
	}
	
	/**
	 * 
	 * @param parameterObj
	 * @param parameterName
	 * @throws BadRequestException
	 */
	public static void checkMandatoryParameter(String parameterObj, String parameterName) throws BadRequestException {
		if (parameterObj == null || parameterObj.isEmpty()) {
			throw new BadRequestException(Errors.MANDATORY_PARAMETER, parameterName);
		}
	}

	public static void checkValue(String parameterName, String value, String...aspectedValues) throws BadRequestException {
		for (String aspectedValue : aspectedValues) {
			if(aspectedValue.equals(value))return;
		}
		
		StringBuilder sAspectedValues = new StringBuilder();
		String or = "";
		for (String aspectedValue : aspectedValues) {
			sAspectedValues.append(or).append(aspectedValue);
			or = " or ";
		}
		
		throw new BadRequestException(Errors.INCORRECT_VALUE, parameterName + " possible values are " + sAspectedValues);
	}

	public static void checkValue(String parameterName, Integer value, Integer...aspectedValues) throws BadRequestException {
		for (Integer aspectedValue : aspectedValues) {
			if(aspectedValue == value)return;
		}
		
		StringBuilder sAspectedValues = new StringBuilder();
		String or = "";
		for (Integer aspectedValue : aspectedValues) {
			sAspectedValues.append(or).append(aspectedValue);
			or = " or ";
		}
		
		throw new BadRequestException(Errors.INCORRECT_VALUE, parameterName + " possible values are " + sAspectedValues);
	}
	
	/**
	 * 
	 * @param list
	 * @throws NotFoundException
	 */
	public static void checkList(List<?> list, String arg) throws NotFoundException {
		if (list == null || list.isEmpty()) {
			if(arg != null)throw new NotFoundException(Errors.RECORD_NOT_FOUND, arg);
			throw new NotFoundException(Errors.RECORD_NOT_FOUND);
		}
	}

	public static void checkList(List<?> list) throws NotFoundException {
		checkList(list, null);
	}

	
	/**
	 * 
	 * @param sort
	 * @param clazz
	 * @return
	 * @throws BadRequestException
	 */
	public static List<String> getSortList(String sort, Class<?> clazz) throws BadRequestException {

		List<String> sortList = null;

		if (sort != null && !sort.isEmpty()) {

			if (sort.contains(SORT_PROPERTIES_SEPARATOR)) {
				sortList = Arrays.asList(sort.split(SORT_PROPERTIES_SEPARATOR));
			} else {
				sortList = Arrays.asList(sort);
			}

			validateSortParameter(sortList, clazz);
		}

		return sortList;

	}

	/**
	 * 
	 * @param sortList
	 * @param clazz
	 * @throws BadRequestException
	 */
	private static void validateSortParameter(List<String> sortList, Class<?> clazz) throws BadRequestException {
		for (String sortProperty : sortList) {

			if (propertyNotFound(sortProperty, clazz)) {
				throw new BadRequestException(Errors.PROPERTY_NOT_FOUND, sortProperty);
			}
		}
	}

	/**
	 * 
	 * @param sortProperty
	 * @param clazz
	 * @return
	 */
	private static boolean propertyNotFound(String sortProperty, Class<?> clazz) {

		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {

			String fieldName = field.getName();
			String fieldNameDesc = fieldName + DESC_CHAR;

			if (fieldName.equals(sortProperty) || fieldNameDesc.equals(sortProperty)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static String tenantTypeDescription(int id){
		
		for (TenantType type : TenantType.values()) {
			if(type.id() == id){
				return type.description();
			}
		}
		return null;
		
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static String codeTenantStatus(int id){
		
		for (Status status : Status.values()) {
			if(status.id() == id){
				return status.code();
			}
		}
		return null;
		
	}



}
