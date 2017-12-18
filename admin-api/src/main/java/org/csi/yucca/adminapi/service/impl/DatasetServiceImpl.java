package org.csi.yucca.adminapi.service.impl;

import static org.csi.yucca.adminapi.util.ServiceUtil.API_SUBTYPE_ODATA;
import static org.csi.yucca.adminapi.util.ServiceUtil.DATASOURCE_VERSION;
import static org.csi.yucca.adminapi.util.ServiceUtil.MULTI_SUBDOMAIN_ID_DOMAIN;
import static org.csi.yucca.adminapi.util.ServiceUtil.MULTI_SUBDOMAIN_LANG_EN;
import static org.csi.yucca.adminapi.util.ServiceUtil.MULTI_SUBDOMAIN_LANG_IT;
import static org.csi.yucca.adminapi.util.ServiceUtil.MULTI_SUBDOMAIN_PATTERN;
import static org.csi.yucca.adminapi.util.ServiceUtil.buildResponse;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkAuthTenant;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkComponents;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkIfFoundRecord;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkLicense;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkList;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkMandatoryParameter;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkTenant;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkVisibility;
import static org.csi.yucca.adminapi.util.ServiceUtil.getSortList;
import static org.csi.yucca.adminapi.util.ServiceUtil.getTenantCodeListFromUser;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertBinaryComponents;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertComponents;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertDataSource;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertDcat;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertLicense;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertSharingTenants;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertTags;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertTenantDataSource;
import static org.csi.yucca.adminapi.util.ServiceUtil.maximumLimitErrorsReached;
import static org.csi.yucca.adminapi.util.ServiceUtil.updateDataSource;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.exception.UnauthorizedException;
import org.csi.yucca.adminapi.importmetadata.DatabaseReader;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.mapper.ApiMapper;
import org.csi.yucca.adminapi.mapper.ComponentMapper;
import org.csi.yucca.adminapi.mapper.DataSourceMapper;
import org.csi.yucca.adminapi.mapper.DatasetMapper;
import org.csi.yucca.adminapi.mapper.DcatMapper;
import org.csi.yucca.adminapi.mapper.LicenseMapper;
import org.csi.yucca.adminapi.mapper.OrganizationMapper;
import org.csi.yucca.adminapi.mapper.SequenceMapper;
import org.csi.yucca.adminapi.mapper.SubdomainMapper;
import org.csi.yucca.adminapi.mapper.TenantMapper;
import org.csi.yucca.adminapi.mapper.UserMapper;
import org.csi.yucca.adminapi.model.Api;
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.model.Subdomain;
import org.csi.yucca.adminapi.model.User;
import org.csi.yucca.adminapi.request.ComponentInfoRequest;
import org.csi.yucca.adminapi.request.ComponentRequest;
import org.csi.yucca.adminapi.request.DatasetRequest;
import org.csi.yucca.adminapi.request.InvioCsvRequest;
import org.csi.yucca.adminapi.response.ComponentResponse;
import org.csi.yucca.adminapi.response.DataTypeResponse;
import org.csi.yucca.adminapi.response.DatasetResponse;
import org.csi.yucca.adminapi.response.DettaglioDatasetResponse;
import org.csi.yucca.adminapi.response.PostDatasetResponse;
import org.csi.yucca.adminapi.service.DatasetService;
import org.csi.yucca.adminapi.util.DataOption;
import org.csi.yucca.adminapi.util.DataType;
import org.csi.yucca.adminapi.util.DatasetSubtype;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.ManageOption;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.csi.yucca.adminapi.util.Status;
import org.csi.yucca.adminapi.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.csi.yucca.adminapi.request.ImportMetadataDatasetRequest;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class DatasetServiceImpl implements DatasetService {

	@Autowired
	private DatasetMapper datasetMapper;

	@Autowired
	private TenantMapper tenantMapper;

	@Autowired
	private OrganizationMapper organizationMapper;

	@Autowired
	private DataSourceMapper dataSourceMapper;

	@Autowired
	private LicenseMapper licenseMapper;

	@Autowired
	private DcatMapper dcatMapper;

	@Autowired
	private SequenceMapper sequenceMapper;

	@Autowired
	private ComponentMapper componentMapper;

	@Autowired
	private ApiMapper apiMapper;

	@Autowired
	private SubdomainMapper subdomainMapper;

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public ServiceResponse insertCSVData(MultipartFile file, Boolean skipFirstRow, String encoding,
			String csvSeparator, String componentInfoRequestsJson, String organizationCode, Integer idDataset, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception {
		
		List<ComponentInfoRequest> componentInfoRequests = Util.getComponentInfoRequests(componentInfoRequestsJson);
		
		DettaglioDataset  dataset = datasetMapper.selectDettaglioDataset(null, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));
		checkIfFoundRecord(dataset);
		List<ComponentResponse> components = Util.getComponents(dataset.getComponents());
		
		checkComponentsSize(components, componentInfoRequests);

		List<String> errors = checkCsvFile(file, skipFirstRow, csvSeparator, components, componentInfoRequests);
		if (!errors.isEmpty()) {
			return ServiceResponse.build().object(errors);
		}

		List<String> csvRows = getCsvRows(file, skipFirstRow, components, componentInfoRequests, csvSeparator);
		
		InvioCsvRequest invioCsvRequest = new InvioCsvRequest().datasetCode(dataset.getDatasetcode()).datasetVersion(dataset.getDatasourceversion()).values(csvRows);

		// invio api todo
		User user = userMapper.selectUserByIdDataSourceAndVersion(dataset.getIdDataSource(), dataset.getDatasourceversion());
		
        return ServiceResponse.build().object(invioCsvRequest);
	}
	
	
	private String getCsvRow(String row, String csvSeparator, List<ComponentResponse> components, List<ComponentInfoRequest> componentInfoRequests){
		
		StringBuilder resultRow = new StringBuilder();
		
		String[] columns =  row.split(csvSeparator);
		
		for (int c = 0; c < columns.length; c++) {

			ComponentInfoRequest info = getInfoByNumColumn(c, componentInfoRequests);
			ComponentResponse component = getComponentResponseById(info.getIdComponent(), components);

			if (info.isSkipColumn()) continue;
			
			String doubleQuote = "";
			
			if(DataType.STRING.id().equals(component.getDataType().getIdDataType())||DataType.DATE_TIME.id().equals(component.getDataType().getIdDataType())){
				doubleQuote = "\"";
			}
			
			resultRow.append("\"").append(component.getName()).append("\"")
				     .append(":")
				     .append(doubleQuote).append(columns[c]).append(doubleQuote).append(",");
		}

		resultRow.setLength(resultRow.length() - 1);
		
		return resultRow.toString();

	}
	
	
	private List<String> getCsvRows(MultipartFile file, boolean skipFirstRow, List<ComponentResponse> components, List<ComponentInfoRequest> componentInfoRequests, String csvSeparator)throws Exception{
		
		String [] rows = Util.getCsvRows(file, skipFirstRow);
		
		List<String> result =  new ArrayList<String>();

		for (int i = 0; i < rows.length; i++) {
			
			String row = getCsvRow(rows[i], csvSeparator, components, componentInfoRequests);
			
			result.add("{" + row + "}" );
		}
		
		return result;

	}
	
	
	
	/**
	 * 
	 * @param file
	 * @param skipFirstRow
	 * @param csvSeparator
	 * @param components
	 * @param componentInfoRequests
	 * @return
	 * @throws Exception
	 */
	private List<String> checkCsvFile(MultipartFile file, boolean skipFirstRow, String csvSeparator, List<ComponentResponse> components, List<ComponentInfoRequest> componentInfoRequests) throws Exception{

		checkComponentsSize(components, componentInfoRequests);
		
//		String contentType = file.getContentType(); // application/vnd.ms-excel
//		String name = file.getName();   // file
//		String originalFileName = file.getOriginalFilename(); // file.csv

//		byte[] bytes = file.getBytes();
//		String completeData = new String(bytes);
		
		String[] rows = Util.getCsvRows(file, skipFirstRow);
		
		List<String> errors = new ArrayList<String>();
		
		for (int r = 0; r < rows.length; r++) {
			
			String[] columns =  rows[r].split(csvSeparator);
			
			for (int c = 0; c < columns.length; c++) {

				if(maximumLimitErrorsReached(errors)) break;
				
				ComponentInfoRequest info = getInfoByNumColumn(c, componentInfoRequests);
				ComponentResponse component = getComponentResponseById(info.getIdComponent(), components);

				if(info.isSkipColumn() || DataType.STRING.id().equals(component.getDataType().getIdDataType())){
					continue;
				}
				
				String column = columns[c];
				DataTypeResponse typeResponse = component.getDataType();
				for (DataType dateType : DataType.values()) {
					if(dateType.id().equals(typeResponse.getIdDataType())){
						try {
							
							if (DataType.DATE_TIME == dateType) {
								if(!Util.isThisDateValid(column, info.getDateFormat())){
									throw new Exception();
								}
								continue;
							}
							dateType.checkValue(column);
						} 
						catch (Exception e) {
							errors.add("Errore alla riga " + r + ", il dato della colonna " + component.getName() + " non è di tipo " + dateType.description());
						}
						break;
					}
				}
			}
		}
		return errors;
	}
	
	/**
	 * 
	 * @param datasetRequest
	 * @throws Exception
	 */
	private void updateDatasetComponent(DatasetRequest datasetRequest) throws Exception{

		// COMPONENT already present
		List<Integer> listIdComponent = new ArrayList<Integer>(); 
		for (ComponentRequest component : datasetRequest.getComponents()) {
			if(component.getIdComponent() != null){
				listIdComponent.add(component.getIdComponent());
			}
		}
		componentMapper.cloneComponent(datasetRequest.getNewDataSourceVersion(), listIdComponent);
		
		for (ComponentRequest component : datasetRequest.getComponents()) {
			if(component.getIdComponent() != null){
				componentMapper.updateClonedComponent(
						component.getName(), 
						component.getAlias(), 
						component.getInorder(), 
						component.getIdMeasureUnit(),
						datasetRequest.getNewDataSourceVersion(), 
						datasetRequest.getIdDataSource(),
						component.getForeignkey());
			}
		}
		
		// new component
		ServiceUtil.insertComponents(datasetRequest.getComponents(), datasetRequest.getIdDataSource(), datasetRequest.getNewDataSourceVersion(), datasetRequest.getNewDataSourceVersion(), componentMapper);
	}
	
	/**
	 * 
	 * @param datasetRequest
	 * @throws Exception
	 */
	private void updateSharingTenants(DatasetRequest datasetRequest) throws Exception{
		if (datasetRequest.getSharingTenants() != null && !datasetRequest.getSharingTenants().isEmpty()) {
			tenantMapper.deleteNotManagerTenantDataSource(datasetRequest.getIdDataSource(), datasetRequest.getCurrentDataSourceVersion());
			
			ServiceUtil.insertSharingTenants(datasetRequest.getSharingTenants(), datasetRequest.getIdDataSource(),
					Util.getNow(),  DataOption.READ_AND_USE.id() , ManageOption.NO_RIGHT.id(), datasetRequest.getNewDataSourceVersion(), tenantMapper);
		}
	}
	
	/**
	 * 
	 * @param datasetRequest
	 * @throws Exception
	 */
	private void updateDatasetTransaction(DatasetRequest datasetRequest) throws Exception {
		
		// dcat
		Long idDcat = insertDcat(datasetRequest.getDcat(), dcatMapper);
		
		// license
		Integer idLicense = insertLicense(datasetRequest.getLicense(), licenseMapper);

		// data source
		dataSourceMapper.cloneDataSource( datasetRequest.getNewDataSourceVersion(), datasetRequest.getCurrentDataSourceVersion(), datasetRequest.getIdDataSource());
		updateDataSource(datasetRequest, idDcat, idLicense, datasetRequest.getIdDataSource(), datasetRequest.getNewDataSourceVersion(), dataSourceMapper);
		
		// data set
		datasetMapper.cloneDataset(datasetRequest.getNewDataSourceVersion(), datasetRequest.getCurrentDataSourceVersion(), datasetRequest.getIdDataSource());
		datasetMapper.updateDataset(datasetRequest.getDatasetname(), datasetRequest.getDescription(), datasetRequest.getIdDataSource(), datasetRequest.getNewDataSourceVersion());

		// tags
		insertTags(datasetRequest.getTags(), datasetRequest.getIdDataSource(), datasetRequest.getNewDataSourceVersion(), dataSourceMapper);

		// components
		updateDatasetComponent(datasetRequest);
		
		// sharing tenants		
		updateSharingTenants(datasetRequest);
		
	}

	/**
	 * 
	 */
	public ServiceResponse updateDataset(String organizationCode, Integer idDataset, DatasetRequest datasetRequest,  String tenantCodeManager, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {

		updateDatasetValidation(datasetRequest, authorizedUser, organizationCode, idDataset, tenantCodeManager);
		
		updateDatasetTransaction(datasetRequest);
		
		return ServiceResponse.build().object(PostDatasetResponse.build(idDataset)
				.datasetcode(datasetRequest.getDatasetcode()).datasetname(datasetRequest.getDatasetname()));
	
	}

	/**
	 * 
	 * @param datasetRequest
	 * @param authorizedUser
	 * @param organizationCode
	 * @param organization
	 * @param idDataset
	 * @param tenantCodeManager
	 * @throws Exception
	 */
	private void updateDatasetValidation(DatasetRequest datasetRequest, JwtUser authorizedUser,
			String organizationCode, Integer idDataset, String tenantCodeManager) throws Exception {

		checkAuthTenant(authorizedUser, datasetRequest.getIdTenant(), tenantMapper);
		
		// check organization code:
		Organization organization = organizationMapper.selectOrganizationByCode(organizationCode);
		checkIfFoundRecord(organization, "Not found organization code: " + organizationCode);

		// check tag list:
		checkList(datasetRequest.getTags(), "tags");

		// datasetname
		checkMandatoryParameter(datasetRequest.getDatasetname(), "datasetname");

		// license
		checkLicense(datasetRequest.getLicense());
		
		// visibility
		checkVisibility(datasetRequest, tenantMapper);

		// dataset
		Dataset dataset = datasetMapper.selectDatasetForUpdate(tenantCodeManager, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));
		checkIfFoundRecord(dataset);
		
		if(Status.UNINSTALLATION.id().equals(dataset.getIdStatus())){
			throw new BadRequestException(Errors.INCORRECT_VALUE, "Status: " + Status.UNINSTALLATION.description());
		}
		
		if(dataset.getDomIdDomain().equals(-1) && datasetRequest.getUnpublished() == false){
			throw new BadRequestException(Errors.INCORRECT_VALUE, "Multi domain must be unpublished.");
		}
		
		checkComponents(datasetRequest.getComponents(), dataset.getIdDataSource(), dataset.getDatasourceversion(), componentMapper);
		
		datasetRequest.setNewDataSourceVersion(dataset.getDatasourceversion() + 1);
		datasetRequest.setCurrentDataSourceVersion(dataset.getDatasourceversion());
		datasetRequest.setIdDataSource(dataset.getIdDataSource());
		datasetRequest.setDatasetcode(dataset.getDatasetcode());
	}
	
	
	/**
	 * 
	 */
	@Override
	public ServiceResponse insertDataset(String organizationCode, DatasetRequest postDatasetRequest,
			JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {

		Organization organization = organizationMapper.selectOrganizationByCode(organizationCode);

		insertDatasetValidation(postDatasetRequest, authorizedUser, organizationCode, organization);

		Dataset dataset = insertDatasetTransaction(postDatasetRequest, authorizedUser, organization);

		return ServiceResponse.build().object(PostDatasetResponse.build(dataset.getIddataset())
				.datasetcode(dataset.getDatasetcode()).datasetname(dataset.getDatasetname()));
	}

	/**
	 * 
	 */
	@Override
	public ServiceResponse selectDataset(String organizationCode, Integer idDataset, String tenantCodeManager,
			JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {

		DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDataset(tenantCodeManager, idDataset,
				organizationCode, getTenantCodeListFromUser(authorizedUser));

		checkIfFoundRecord(dettaglioDataset);

		return buildResponse(new DettaglioDatasetResponse(dettaglioDataset));
	}

	/**
	 * 
	 */
	@Override
	public ServiceResponse selectDatasets(String organizationCode, String tenantCodeManager, String sort,
			JwtUser authorizedUser) throws BadRequestException, NotFoundException, UnauthorizedException, Exception {

		checkAuthTenant(authorizedUser, tenantCodeManager);

		List<String> sortList = getSortList(sort, Dataset.class);

		List<Dataset> dataSetList = datasetMapper.selectDataSets(tenantCodeManager, organizationCode, sortList,
				getTenantCodeListFromUser(authorizedUser));

		checkList(dataSetList);

		List<DatasetResponse> listResponse = new ArrayList<DatasetResponse>();
		for (Dataset dataset : dataSetList) {
			listResponse.add(new DatasetResponse(dataset));
		}

		return buildResponse(listResponse);

	}

	/**
	 * 
	 * @param components
	 * @return
	 */
	private boolean isBinaryDataset(List<ComponentRequest> components) {
		for (ComponentRequest component : components) {
			if (DataType.BINARY.id() == component.getIdDataType()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param postDatasetRequest
	 * @param authorizedUser
	 * @param organizationCode
	 * @param organization
	 * @throws Exception
	 */
	private void insertDatasetValidation(DatasetRequest datasetRequest, JwtUser authorizedUser,
			String organizationCode, Organization organization) throws Exception {

		// component
		checkComponents(datasetRequest.getComponents(), componentMapper);
		
		// verifica che sia presente idSubdomain o multisubdomanin
		if (datasetRequest.getIdSubdomain() == null && datasetRequest.getMultiSubdomain() == null) {
			throw new BadRequestException(Errors.MANDATORY_PARAMETER, "Mandatory idSubdomanin or multiSubdomain");
		}

		// verifica il multisubdomain
		if (datasetRequest.getMultiSubdomain() != null
				&& !datasetRequest.getMultiSubdomain().matches(MULTI_SUBDOMAIN_PATTERN)) {
			throw new BadRequestException(Errors.INCORRECT_VALUE,
					"Incorrect pattern for multisubdomain. Must be [ " + MULTI_SUBDOMAIN_PATTERN + " ]");
		}

		// Se id_subdomain è nullo verificare che "unpublished " sia true
		if (datasetRequest.getIdSubdomain() == null && datasetRequest.getUnpublished() == false) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, "If idSubdomain is null unpublished must be true.");
		}

		checkIfFoundRecord(organization, "Not found organization code: " + organizationCode);
		checkList(datasetRequest.getTags(), "tags");
		checkTenant(datasetRequest.getIdTenant(), organizationCode, tenantMapper);
		checkAuthTenant(authorizedUser, datasetRequest.getIdTenant(), tenantMapper);
		checkMandatoryParameter(datasetRequest.getDatasetname(), "datasetname");
		checkLicense(datasetRequest.getLicense());
		checkVisibility(datasetRequest, tenantMapper);
	}

	/**
	 * 
	 * @param postDatasetRequest
	 * @param organization
	 * @return
	 * @throws Exception
	 */
	private Integer insertBinary(DatasetRequest postDatasetRequest, Organization organization, Integer idSubdomain)
			throws Exception {
		Integer idBinaryDataSource = null;
		if (isBinaryDataset(postDatasetRequest.getComponents())) {

			// BINARY DATASOURCE
			idBinaryDataSource = insertDataSource(
					new DatasetRequest().datasetname(postDatasetRequest.getDatasetname()).idSubdomain(idSubdomain),
					organization.getIdOrganization(), Status.INSTALLED.id(), dataSourceMapper);

			// INSERT DATASET
			ServiceUtil.insertDataset(idBinaryDataSource, DATASOURCE_VERSION,
					postDatasetRequest.getDatasetname(), DatasetSubtype.BINARY.id(), datasetMapper, sequenceMapper);

			// BINARY COMPONENT
			insertBinaryComponents(idBinaryDataSource, componentMapper);
		}
		return idBinaryDataSource;
	}

	/**
	 * 
	 * @param postDatasetRequest
	 * @return
	 */
	private Integer insertSubdomain(DatasetRequest postDatasetRequest) {

		if (postDatasetRequest.getIdSubdomain() != null) {
			return postDatasetRequest.getIdSubdomain();
		}

		Subdomain subdomain = new Subdomain().idDomain(MULTI_SUBDOMAIN_ID_DOMAIN)
				.langEn(MULTI_SUBDOMAIN_LANG_EN).langIt(MULTI_SUBDOMAIN_LANG_IT)
				.subdomaincode(postDatasetRequest.getMultiSubdomain());

		subdomainMapper.insertSubdomain(subdomain);

		return subdomain.getIdSubdomain();
	}

	/**
	 * 
	 * @param postDatasetRequest
	 * @param authorizedUser
	 * @param organization
	 * @return
	 * @throws Exception
	 */
	private Dataset insertDatasetTransaction(DatasetRequest postDatasetRequest, JwtUser authorizedUser,
			Organization organization) throws Exception {

		// insert subdomain
		Integer idSubdomain = insertSubdomain(postDatasetRequest);

		// BINARY
		Integer idBinaryDataSource = insertBinary(postDatasetRequest, organization, idSubdomain);

		// INSERT LICENSE:
		Integer idLicense = insertLicense(postDatasetRequest.getLicense(), licenseMapper);

		// INSERT DCAT:
		Long idDcat = insertDcat(postDatasetRequest.getDcat(), dcatMapper);

		// INSERT DATA SOURCE:
		Integer idDataSource = insertDataSource(postDatasetRequest.idSubdomain(idSubdomain),
				organization.getIdOrganization(), idDcat, idLicense, Status.INSTALLED.id(), dataSourceMapper);

		// INSERT DATASET
		Dataset dataset = ServiceUtil.insertDataset(idDataSource, DATASOURCE_VERSION,
				postDatasetRequest.getDatasetname(), DatasetSubtype.BULK.id(), postDatasetRequest.getImportfiletype(),
				DATASOURCE_VERSION, idBinaryDataSource, postDatasetRequest.getJdbcdburl(),
				postDatasetRequest.getJdbcdbname(), postDatasetRequest.getJdbcdbtype(),
				postDatasetRequest.getJdbctablename(), datasetMapper, sequenceMapper);

		// TAGS
		for (Integer idTag : postDatasetRequest.getTags()) {
			dataSourceMapper.insertTagDataSource(idDataSource, DATASOURCE_VERSION, idTag);
		}

		// COMPONENT
		insertComponents(postDatasetRequest.getComponents(), idDataSource, ServiceUtil.DATASOURCE_VERSION,
				Util.booleanToInt(true), componentMapper);

		// TENANT-DATASOURCE
		insertTenantDataSource(postDatasetRequest.getIdTenant(), idDataSource, Util.getNow(), tenantMapper);

		// SHARING TENANT
		insertSharingTenants(postDatasetRequest.getSharingTenants(), idDataSource, Util.getNow(),
				DataOption.READ_AND_USE.id(), ManageOption.NO_RIGHT.id(), tenantMapper);

		// API
		apiMapper.insertApi(Api.buildOutput(DATASOURCE_VERSION).apicode(dataset.getDatasetcode())
				.apiname(dataset.getDatasetname()).apisubtype(API_SUBTYPE_ODATA)
				.idDataSource(idDataSource));
		
		return dataset;
	}

	/**
	 * 
	 * @param numColumn
	 * @param list
	 * @return
	 */
	private ComponentInfoRequest getInfoByNumColumn(Integer numColumn, List<ComponentInfoRequest> list){
		for (ComponentInfoRequest componentInfoRequest : list) {
			if (componentInfoRequest.getNumColumn().equals(numColumn)) {
				return componentInfoRequest;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param idComponent
	 * @param list
	 * @return
	 */
	private ComponentResponse getComponentResponseById(Integer idComponent, List<ComponentResponse> list){
		for (ComponentResponse componentResponse : list) {
			
			if (componentResponse.getIdComponent().equals(idComponent)) {
				return componentResponse;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param components
	 * @param componentInfoRequests
	 * @throws Exception
	 */
	private void checkComponentsSize(List<ComponentResponse> components, List<ComponentInfoRequest> componentInfoRequests)throws Exception{
		int columnCount = 0;
		for (ComponentInfoRequest info : componentInfoRequests) {
			if(!info.isSkipColumn())
				columnCount++;
		}
		if (columnCount != components.size()) {
			throw new BadRequestException(Errors.NOT_ACCEPTABLE);
		}
	}
	
	@Override
	public ServiceResponse importMetadata(String organizationCode, ImportMetadataDatasetRequest importMetadataRequest, JwtUser authorizedUser)  throws BadRequestException, NotFoundException, Exception {
		
		DatabaseReader databaseReader = new DatabaseReader(organizationCode, importMetadataRequest.getTenantCode(), importMetadataRequest.getDbType(), importMetadataRequest.getJdbcHostname(), 
				importMetadataRequest.getJdbcDbname(), importMetadataRequest.getJdbcUsername(), importMetadataRequest.getJdbcPassword());
		String schema = databaseReader.loadSchema();

		return buildResponse(schema);
	}


}
