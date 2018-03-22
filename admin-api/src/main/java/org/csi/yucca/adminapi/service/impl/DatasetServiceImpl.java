package org.csi.yucca.adminapi.service.impl;

import static org.csi.yucca.adminapi.util.Constants.MAX_ODATA_RESULT_PER_PAGE;
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

import javax.servlet.http.HttpServletResponse;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.delegate.HttpDelegate;
import org.csi.yucca.adminapi.delegate.PublisherDelegate;
import org.csi.yucca.adminapi.delegate.SolrDelegate;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.exception.UnauthorizedException;
import org.csi.yucca.adminapi.importmetadata.DatabaseReader;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.mapper.AllineamentoScaricoDatasetMapper;
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
import org.csi.yucca.adminapi.mapper.UserMapper;
import org.csi.yucca.adminapi.model.AllineamentoScaricoDataset;
import org.csi.yucca.adminapi.model.Api;
import org.csi.yucca.adminapi.model.Bundles;
import org.csi.yucca.adminapi.model.ComponentJson;
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.InternalDettaglioStream;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.model.Subdomain;
import org.csi.yucca.adminapi.model.User;
import org.csi.yucca.adminapi.model.builder.AllineamentoScaricoDatasetBuilder;
import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;
import org.csi.yucca.adminapi.model.join.IngestionConfiguration;
import org.csi.yucca.adminapi.request.AllineamentoScaricoDatasetRequest;
import org.csi.yucca.adminapi.request.ComponentInfoRequest;
import org.csi.yucca.adminapi.request.ComponentRequest;
import org.csi.yucca.adminapi.request.DatasetRequest;
import org.csi.yucca.adminapi.request.ImportMetadataDatasetRequest;
import org.csi.yucca.adminapi.request.InvioCsvRequest;
import org.csi.yucca.adminapi.response.AllineamentoScaricoDatasetResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.DatasetResponse;
import org.csi.yucca.adminapi.response.DettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.PostDatasetResponse;
import org.csi.yucca.adminapi.response.builder.AllineamentoScaricoDatasetResponseBuilder;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
@Configuration
@PropertySources({ @PropertySource("classpath:adminapi.properties"), @PropertySource("classpath:adminapiSecret.properties") })
public class DatasetServiceImpl implements DatasetService {

	private static final Logger logger = Logger.getLogger(DatasetServiceImpl.class);

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

	@Autowired
	private StreamMapper streamMapper;

	@Autowired
	private SmartobjectMapper smartobjectMapper;

	@Autowired
	private BundlesMapper bundlesMapper;

	@Autowired
	private AllineamentoScaricoDatasetMapper allineamentoScaricoDatasetMapper;
	
	@Value("${hive.jdbc.user}")
	private String hiveUser;

	@Value("${hive.jdbc.password}")
	private String hivePassword;

	@Value("${hive.jdbc.url}")
	private String hiveUrl;

	@Value("${datainsert.base.url}")
	private String datainsertBaseUrl;

	@Value("${datainsert.delete.url}")
	private String datainsertDeleteUrl;
	
	@Override
	public ServiceResponse selectIngestionConfiguration(String tenantCode, String dbname, String dateformat,
			String separator, Boolean onlyImported, Boolean help, HttpServletResponse httpServletResponse)
			throws BadRequestException, NotFoundException, Exception {
		
		List<IngestionConfiguration> list =  datasetMapper.selectIngestionConfiguration(dbname, tenantCode, onlyImported);

		downloadCsv(list, "ingestionConf.csv", separator.charAt(0), httpServletResponse, 
				"table", "column", "comments", "datasetCode", "domain", "subdomain", "visibility",
				"opendata", "registrationDate", "dbName", "dbSchema", "dbUrl", "columnIndex");
		
		return buildResponse("Downloaded CSV file with " + list.size() + "records.");
	}
	
	/**
	 * 
	 * @param list
	 * @param fileName
	 * @param delimiterChar
	 * @param httpServletResponse
	 * @param header
	 * @throws Exception
	 */
	private <T> void  downloadCsv(List<T> list, String fileName, int delimiterChar, HttpServletResponse httpServletResponse, String...header) throws Exception{
		
		httpServletResponse.setContentType("text/csv");
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", fileName);
        httpServletResponse.setHeader(headerKey, headerValue);
        
        CsvPreference csvPreference = new CsvPreference.Builder('"', delimiterChar, "\r\n").build();
        
        CsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(), csvPreference);
  
        csvWriter.writeHeader(header);
 
        for (T ingestionConfiguration : list) {
        	csvWriter.write(ingestionConfiguration, header);
		}
 
        csvWriter.close();
	}
	
	@Override
	public ServiceResponse insertLastMongoObjectId(AllineamentoScaricoDatasetRequest request, Integer idOrganization)throws BadRequestException, NotFoundException, Exception {

		Organization organization = organizationMapper.selectOrganizationById(idOrganization);
		
		checkIfFoundRecord(organization, "Not found organization: " + idOrganization);
		
		checkMandatoryParameter(request.getLastMongoObjectId(), "LastMongoObjectId");
		checkMandatoryParameter(request.getDatasetVersion(), "DatasetVersion");
		checkMandatoryParameter(request.getIdDataset(), "IdDataset");
		
		String lastMongoObjectId = allineamentoScaricoDatasetMapper.selectLastMongoObjectId(idOrganization, request.getIdDataset(), request.getDatasetVersion());
		
		if (lastMongoObjectId != null) {
			allineamentoScaricoDatasetMapper.updateLastMongoObjectId(new AllineamentoScaricoDatasetBuilder(request, idOrganization).build());
		}
		else{
			allineamentoScaricoDatasetMapper.insertAllineamentoScaricoDataset(new AllineamentoScaricoDatasetBuilder(request, idOrganization).build());
		}
		
		return buildResponse(new AllineamentoScaricoDatasetResponseBuilder()
				.idOrganization(idOrganization).idDataset(request.getIdDataset()).datasetVersion(request.getDatasetVersion()).lastMongoObjectId(request.getLastMongoObjectId()).build());

	}

	@Override
	public ServiceResponse selectAllineamentoScaricoDataset(Integer idOrganization, Integer idDataset, Integer datasetVersion) throws BadRequestException, NotFoundException, Exception {
		
		String lastMongoObjectId = allineamentoScaricoDatasetMapper.selectLastMongoObjectId(idOrganization, idDataset, datasetVersion);

		checkIfFoundRecord(lastMongoObjectId, "lastMongoObjectId");
		
		return buildResponse(new AllineamentoScaricoDatasetResponseBuilder()
				.idOrganization(idOrganization).idDataset(idDataset).datasetVersion(datasetVersion).lastMongoObjectId(lastMongoObjectId).build());
	}

	@Override
	public ServiceResponse selectAllineamentoScaricoDataset(Integer idOrganization) throws BadRequestException, NotFoundException, Exception {

		List<AllineamentoScaricoDataset> listModel = allineamentoScaricoDatasetMapper.selectAllineamentoScaricoDatasetByOrganization(idOrganization);
		
		checkList(listModel);

		List<AllineamentoScaricoDatasetResponse> listResponse = new ArrayList<>();
		
		for (AllineamentoScaricoDataset model : listModel) {
			listResponse.add(new AllineamentoScaricoDatasetResponseBuilder(model).build());
		}
		
		return buildResponse(listResponse);
	}
	
	@Override
	public ServiceResponse deleteDatasetData(String organizationCode, Integer idDataset, String tenantCodeManager,
			Integer version, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {

		// Verifica che l'utente loggato nel jwt possa utilizzare il tenant passato, se non puo' rilancia UNAUTHORIZED:
		ServiceUtil.checkAuthTenant(authorizedUser, tenantCodeManager);

		DettaglioDataset dataset = datasetMapper.selectDettaglioDataset(null, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));

		checkIfFoundRecord(dataset);

		User user = userMapper.selectUserByIdDataSourceAndVersion(dataset.getIdDataSource(), dataset.getDatasourceversion(), tenantCodeManager, DataOption.WRITE.id());
		
		String url = datainsertDeleteUrl + tenantCodeManager + "/" + idDataset + (version != null ? "/" + version : "");

		HttpDelegate.makeHttpDelete(url, user.getUsername(), user.getPassword());
		
		return ServiceResponse.build().NO_CONTENT();
	}
	
	@Override
	public ServiceResponse uninstallingDatasets(String organizationCode, Integer idDataset, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception {

		DettaglioDataset dataset = datasetMapper.selectDettaglioDataset(null, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));

		ServiceUtil.checkIfFoundRecord(dataset);
		
		// Aggiorna lo stato di tutte le versioni del datasource mettendolo a 'uninst':
		ServiceUtil.updateDataSourceStatusAllVersion(Status.UNINSTALLATION.id(), dataset.getIdDataSource(), dataSourceMapper);

		// spubblicazione delle api odata e la cancellazione del documento Solr
		removeOdataApiAndSolrDocument(dataset.getDatasetcode());
		
		return ServiceResponse.build().NO_CONTENT();
	}
	
	@Override
	public ServiceResponse selectDatasetByOrganizationCode(String organizationCode) throws BadRequestException, NotFoundException, Exception {
		
		List<BackofficeDettaglioStreamDatasetResponse> response = new ArrayList<>();

		List<DettaglioDataset> listDettaglioDataset = datasetMapper.selectListaDettaglioDatasetByOrganizationCode(organizationCode);

		checkList(listDettaglioDataset);
		
		for (DettaglioDataset dettaglioDataset : listDettaglioDataset) {
//			response.add(getBackofficeDettaglioStreamDatasetResponse(dettaglioDataset));
			response.add(ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper));
		}
		return buildResponse(response);
	}
	
	@Override
	public ServiceResponse insertCSVData(MultipartFile file, Boolean skipFirstRow, String encoding, String csvSeparator, String componentInfoRequestsJson, String organizationCode,
			Integer idDataset, String tenantCodeManager, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {

		logger.info("[DatasetServiceImpl::insertCSVData] Begin idDataset:["+idDataset+"], componentInfoRequestsJson:["+componentInfoRequestsJson +"]");

		List<ComponentInfoRequest> componentInfoRequests = Util.getComponentInfoRequests(componentInfoRequestsJson);

		DettaglioDataset dataset = datasetMapper.selectDettaglioDataset(null, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));
		checkIfFoundRecord(dataset);
		//List<ComponentResponse> components = Util.getComponents(dataset.getComponents());

		checkComponentsSize(dataset.getComponents(), componentInfoRequests);

		// could throw bad request
		checkCsvFile(file, skipFirstRow, csvSeparator, dataset.getComponents(), componentInfoRequests);

		List<String> csvRows = getCsvRows(file, skipFirstRow, dataset.getComponents(), componentInfoRequests, csvSeparator);

		InvioCsvRequest invioCsvRequest = new InvioCsvRequest().datasetCode(dataset.getDatasetcode()).datasetVersion(dataset.getDatasourceversion()).values(csvRows);

		logger.debug("tenantCodeManager: " + tenantCodeManager);

		User user = userMapper.selectUserByIdDataSourceAndVersion(dataset.getIdDataSource(), dataset.getDatasourceversion(), tenantCodeManager, DataOption.WRITE.id());

		logger.debug(user != null ? "user: " + user.getUsername() : "user è nullo!");

		logger.debug("[DatasetServiceImpl::insertCSVData] makeHttpPost invioCsvRequest.summary(100 chars):["+invioCsvRequest.toString().substring(0, 100)+"...]");
		HttpDelegate.makeHttpPost(null, datainsertBaseUrl + user.getUsername(), null, user.getUsername(), user.getPassword(), invioCsvRequest.toString());

		logger.info("[DatasetServiceImpl::insertCSVData] END");
		
		int number = csvRows == null ? 0 : csvRows.size();
		
		String message = "Row's number: " + number;
		
		String importedfiles = dataset.getImportedfiles();
		if(importedfiles == null || importedfiles.equals(""))
			importedfiles = file.getOriginalFilename();
		else
			importedfiles += "," + file.getOriginalFilename();
		
		datasetMapper.updateImportedFiles(dataset.getIdDataSource(), dataset.getDatasourceversion(), importedfiles);
		
		return ServiceResponse.build().object(message);
	}

	private String getCsvRow(String row, String csvSeparator, ComponentJson[] components, List<ComponentInfoRequest> componentInfoRequests) {

		StringBuilder resultRow = new StringBuilder();

		String[] columns = row.split(csvSeparator);

		for (int c = 0; c < columns.length; c++) {

			ComponentInfoRequest info = getInfoByNumColumn(c, componentInfoRequests);
			ComponentJson component = getComponentResponseById(info.getIdComponent(), components);

			if (info.isSkipColumn())
				continue;

			String doubleQuote = "";

			if (DataType.STRING.id().equals(component.getIdDataType()) || DataType.DATE_TIME.id().equals(component.getIdDataType())) {
				doubleQuote = "\"";
			}

			resultRow.append("\"").append(component.getName()).append("\"").append(":").append(doubleQuote).append(columns[c]).append(doubleQuote).append(",");
		}

		resultRow.setLength(resultRow.length() - 1);

		return resultRow.toString();

	}

	private List<String> getCsvRows(MultipartFile file, boolean skipFirstRow, ComponentJson[] components, List<ComponentInfoRequest> componentInfoRequests,
			String csvSeparator) throws Exception {

		String[] rows = Util.getCsvRows(file, skipFirstRow);

		List<String> result = new ArrayList<String>();

		for (int i = 0; i < rows.length; i++) {

			String row = getCsvRow(rows[i], csvSeparator, components, componentInfoRequests);

			result.add("{" + row + "}");
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
	private void checkCsvFile(MultipartFile file, boolean skipFirstRow, String csvSeparator, ComponentJson[] components,
			List<ComponentInfoRequest> componentInfoRequests) throws Exception {

		checkComponentsSize(components, componentInfoRequests);

		// String contentType = file.getContentType(); //
		// application/vnd.ms-excel
		// String name = file.getName(); // file
		// String originalFileName = file.getOriginalFilename(); // file.csv

		// byte[] bytes = file.getBytes();
		// String completeData = new String(bytes);

		String[] rows = Util.getCsvRows(file, skipFirstRow);

		List<String> errors = new ArrayList<String>();

		for (int r = 0; r < rows.length; r++) {

			String[] columns = rows[r].split(csvSeparator);

			if (columns.length != components.length) {
				errors.add("Errore alla riga " + r + ", il numero di colonne deve essere: " + components.length);
				continue;
			}
			
			for (int c = 0; c < columns.length; c++) {

				if (maximumLimitErrorsReached(errors))
					break;

				ComponentInfoRequest info = getInfoByNumColumn(c, componentInfoRequests);
				ComponentJson component = getComponentResponseById(info.getIdComponent(), components);

				if (info.isSkipColumn() || DataType.STRING.id().equals(component.getIdDataType())) {
					continue;
				}

				String column = columns[c];
				//DataTypeResponse typeResponse = component.getDataType();
				for (DataType dateType : DataType.values()) {
					if (dateType.id().equals(component.getIdDataType())) {
						try {

							if (DataType.DATE_TIME == dateType) {
								if (!Util.isThisDateValid(column, info.getDateFormat())) {
									throw new Exception();
								}
								continue;
							}
							dateType.checkValue(column);
						} catch (Exception e) {
							errors.add("Errore alla riga " + r + ", il dato della colonna " + component.getName() + " non è di tipo " + dateType.description());
						}
						break;
					}
				}
			}
		}
		
		if (!errors.isEmpty()) {
//			StringBuilder errorsString = new StringBuilder();
//			for (String error : errors) {
//				errorsString.append(error).append("\n");
//			}
			throw new BadRequestException(Errors.INCORRECT_VALUE).args(errors);
		}
	}

	/**
	 * 
	 * @param datasetRequest
	 * @throws Exception
	 */
	private void updateDatasetComponent(DatasetRequest datasetRequest) throws Exception {

		// COMPONENT already present
		List<Integer> listIdComponent = new ArrayList<Integer>();
		for (ComponentRequest component : datasetRequest.getComponents()) {
			if (component.getIdComponent() != null) {
				listIdComponent.add(component.getIdComponent());
			}
		}
		if (listIdComponent.size() > 0)
			componentMapper.cloneComponent(datasetRequest.getNewDataSourceVersion(), listIdComponent);

		for (ComponentRequest component : datasetRequest.getComponents()) {
			if (component.getIdComponent() != null) {
				componentMapper.updateClonedComponent(component.getName(), component.getAlias(), component.getInorder(), component.getIdMeasureUnit(),
						datasetRequest.getNewDataSourceVersion(), datasetRequest.getIdDataSource(), component.getForeignkey());
			}
		}

		// new component
		ServiceUtil.insertComponents(datasetRequest.getComponents(), datasetRequest.getIdDataSource(), datasetRequest.getNewDataSourceVersion(),
				datasetRequest.getNewDataSourceVersion(), componentMapper);
	}

	/**
	 * 
	 * @param datasetRequest
	 * @throws Exception
	 */
	private void updateSharingTenants(DatasetRequest datasetRequest) throws Exception {
		if (datasetRequest.getSharingTenants() != null && !datasetRequest.getSharingTenants().isEmpty()) {
			tenantMapper.deleteNotManagerTenantDataSource(datasetRequest.getIdDataSource(), datasetRequest.getCurrentDataSourceVersion());

			ServiceUtil.insertSharingTenants(datasetRequest.getSharingTenants(), datasetRequest.getIdDataSource(), Util.getNow(), DataOption.READ_AND_USE.id(),
					ManageOption.NO_RIGHT.id(), datasetRequest.getNewDataSourceVersion(), tenantMapper);
		}
	}

	/**
	 * 
	 * @param datasetRequest
	 * @throws Exception
	 */
	private void updateDatasetTransaction(DatasetRequest datasetRequest, String tenantCodeManager) throws Exception {

		// dcat
		Integer idDcat = insertDcat(datasetRequest.getDcat(), dcatMapper);

		// license
		Integer idLicense = insertLicense(datasetRequest.getLicense(), licenseMapper);

		// data source
		dataSourceMapper.cloneDataSource(datasetRequest.getNewDataSourceVersion(), datasetRequest.getCurrentDataSourceVersion(), datasetRequest.getIdDataSource());
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
		
		// TENANT-DATASOURCE
		insertTenantDataSource(datasetRequest.getIdTenant(), datasetRequest.getIdDataSource(),datasetRequest.getNewDataSourceVersion(), Util.getNow(), tenantMapper);


		// TODO vanno clonate le api?
		try {
			CloseableHttpClient httpclient = PublisherDelegate.build().registerToStoreInit();
			if (!datasetRequest.getUnpublished()) {

				DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByDatasetCode(datasetRequest.getDatasetcode(), false);
				String apiName = null;

				Bundles bundles = bundlesMapper.selectBundlesByTenantCode(tenantCodeManager);

				apiMapper.insertApi(

				Api.buildOutput(datasetRequest.getNewDataSourceVersion()).apicode(datasetRequest.getDatasetcode()).apiname(dettaglioDataset.getDatasetname())
					.entitynamespace( Api.ENTITY_NAMESPACE + datasetRequest.getDatasetcode())
						.apisubtype(API_SUBTYPE_ODATA).idDataSource(dettaglioDataset.getIdDataSource())
						.maxOdataResultperpage(bundles != null ? bundles.getMaxOdataResultperpage() : MAX_ODATA_RESULT_PER_PAGE));

				// publisher
				apiName = PublisherDelegate.build().addApi(httpclient, dettaglioDataset);
				PublisherDelegate.build().publishApi(httpclient, "1.0", apiName, "admin");
				SolrDelegate.build().addDocument(dettaglioDataset);

			} else {
				removeOdataApiAndSolrDocument(httpclient, datasetRequest.getDatasetcode());
			}
		} catch (Exception e) {
			logger.error("[DatasetServiceImpl::updateDatasetTransaction] Publish API - error " + e.getMessage());
			e.printStackTrace();
			throw new BadRequestException(Errors.INTERNAL_SERVER_ERROR, " An error occurred during the publication of the API, please try to save again");

		}

	}
	
	
	/**
	 * 
	 */
	@Override
	public ServiceResponse updateDataset(String organizationCode, Integer idDataset, DatasetRequest datasetRequest, String tenantCodeManager, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception {

		updateDatasetValidation(datasetRequest, authorizedUser, organizationCode, idDataset, tenantCodeManager);

		updateDatasetTransaction(datasetRequest, tenantCodeManager);

		return ServiceResponse.build().object(PostDatasetResponse.build(idDataset).datasetcode(datasetRequest.getDatasetcode()).datasetname(datasetRequest.getDatasetname()));

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
	private void updateDatasetValidation(DatasetRequest datasetRequest, JwtUser authorizedUser, String organizationCode, Integer idDataset, String tenantCodeManager)
			throws Exception {

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

		if (Status.UNINSTALLATION.id().equals(dataset.getIdStatus())) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, "Status: " + Status.UNINSTALLATION.description());
		}

		if (dataset.getDomIdDomain().equals(-1) && datasetRequest.getUnpublished() == false) {
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
	public ServiceResponse insertDataset(String organizationCode, DatasetRequest postDatasetRequest, JwtUser authorizedUser) throws BadRequestException, NotFoundException,
			Exception {

		Organization organization = organizationMapper.selectOrganizationByCode(organizationCode);
		
		if(postDatasetRequest.getMultiSubdomain()!=null )
			postDatasetRequest.setMultiSubdomain(postDatasetRequest.getMultiSubdomain().toUpperCase());

		insertDatasetValidation(postDatasetRequest, authorizedUser, organizationCode, organization);

		PostDatasetResponse response = insertDatasetTransaction(postDatasetRequest, authorizedUser, organization);
		return ServiceResponse.build().object(response);
	}

	/**
	 * 
	 */
	@Override
	public ServiceResponse selectDataset(String organizationCode, Integer idDataset, String tenantCodeManager, JwtUser authorizedUser) throws BadRequestException,
			NotFoundException, Exception {

		DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDataset(tenantCodeManager, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));

		checkIfFoundRecord(dettaglioDataset);

		if (DatasetSubtype.STREAM.id().equals(dettaglioDataset.getIdDatasetSubtype()) || DatasetSubtype.SOCIAL.id().equals(dettaglioDataset.getIdDatasetSubtype())) {

			DettaglioStream dettaglioStream = streamMapper.selectStreamByDatasource(dettaglioDataset.getIdDataSource(), dettaglioDataset.getDatasourceversion());
			if (dettaglioStream != null) {

				DettaglioSmartobject dettaglioSmartobject = smartobjectMapper.selectSmartobjectById(dettaglioStream.getIdSmartObject());

				List<InternalDettaglioStream> listInternalStream = streamMapper.selectInternalStream(dettaglioStream.getIdDataSource(), dettaglioStream.getDatasourceversion());

				return buildResponse(new DettaglioStreamDatasetResponse(dettaglioStream, dettaglioDataset, dettaglioSmartobject, listInternalStream));
			}
		}

		return buildResponse(new DettaglioStreamDatasetResponse(dettaglioDataset));
	}

	@Override
	public ServiceResponse selectDatasetByIdDataset(Integer idDataset, boolean onlyInstalled) throws BadRequestException, NotFoundException, Exception {

		DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByIdDataset(idDataset, onlyInstalled);
		
		BackofficeDettaglioStreamDatasetResponse dettaglio = ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper);		

		return buildResponse(dettaglio);
	}

	@Override
	public ServiceResponse selectDatasetByDatasetCodeDatasetVersion(String datasetCode, Integer datasetVersion) throws BadRequestException, NotFoundException, Exception {
	
		DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByDatasetCodeDatasourceVersion(datasetCode, datasetVersion);

		checkIfFoundRecord(dettaglioDataset);
		
//		return buildResponse(getBackofficeDettaglioStreamDatasetResponse(dettaglioDataset));	
		return buildResponse(ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper));	
	
	}

	@Override
	public ServiceResponse selectDatasetByDatasetCode(String datasetCode, boolean onlyInstalled) throws BadRequestException, NotFoundException, Exception {

		DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByDatasetCode(datasetCode, onlyInstalled);

		BackofficeDettaglioStreamDatasetResponse dettaglio = ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper);

		return buildResponse(dettaglio);
	}

	@Override
	public ServiceResponse selectDatasetByIdDatasetDatasetVersion(Integer idDataset, Integer datasetVersion) throws BadRequestException, NotFoundException, Exception {



		DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByIdDatasetDatasourceVersion(idDataset, datasetVersion);

		
		BackofficeDettaglioStreamDatasetResponse dettaglio = ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper);
		
		
		return buildResponse(dettaglio);
	}

	/**
	 * 
	 */
	@Override
	public byte[] selectDatasetIcon(String organizationCode, Integer idDataset, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {

		DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDataset(null, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));

		checkIfFoundRecord(dettaglioDataset);

		return Util.convertIconFromDBToByte(dettaglioDataset.getDataSourceIcon());
	}

	/**
	 * 
	 */
	@Override
	public ServiceResponse selectDatasets(String organizationCode, String tenantCodeManager, String sort, JwtUser authorizedUser) throws BadRequestException, NotFoundException,
			UnauthorizedException, Exception {

		checkAuthTenant(authorizedUser, tenantCodeManager);

		List<String> sortList = getSortList(sort, Dataset.class);

		List<Dataset> dataSetList = datasetMapper.selectDataSets(tenantCodeManager, organizationCode, sortList, getTenantCodeListFromUser(authorizedUser));

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
	private void insertDatasetValidation(DatasetRequest datasetRequest, JwtUser authorizedUser, String organizationCode, Organization organization) throws Exception {

		// component
		checkComponents(datasetRequest.getComponents(), componentMapper);

		// verifica che sia presente idSubdomain o multisubdomanin
		if (datasetRequest.getIdSubdomain() == null && datasetRequest.getMultiSubdomain() == null) {
			throw new BadRequestException(Errors.MANDATORY_PARAMETER, "Mandatory idSubdomanin or multiSubdomain");
		}

		// verifica il multisubdomain
		if (datasetRequest.getMultiSubdomain() != null && !datasetRequest.getMultiSubdomain().matches(MULTI_SUBDOMAIN_PATTERN)) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, "Incorrect pattern for multisubdomain. Must be [ " + MULTI_SUBDOMAIN_PATTERN + " ]");
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
	private Integer insertBinary(DatasetRequest postDatasetRequest, Organization organization, Integer idSubdomain) throws Exception {
		Integer idBinaryDataSource = null;
		if (isBinaryDataset(postDatasetRequest.getComponents())) {

			// BINARY DATASOURCE
			idBinaryDataSource = insertDataSource(new DatasetRequest().datasetname(postDatasetRequest.getDatasetname()).idSubdomain(idSubdomain), organization.getIdOrganization(),
					Status.INSTALLED.id(), dataSourceMapper);

			// INSERT DATASET
			ServiceUtil.insertDataset(idBinaryDataSource, DATASOURCE_VERSION, postDatasetRequest.getDatasetname(), DatasetSubtype.BINARY.id(), datasetMapper, sequenceMapper);

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

		Subdomain subdomain = new Subdomain().idDomain(MULTI_SUBDOMAIN_ID_DOMAIN).langEn(postDatasetRequest.getMultiSubdomain()).langIt(postDatasetRequest.getMultiSubdomain())
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
	private PostDatasetResponse insertDatasetTransaction(DatasetRequest postDatasetRequest, JwtUser authorizedUser, Organization organization) throws Exception {

		// insert subdomain
		Integer idSubdomain = insertSubdomain(postDatasetRequest);

		// BINARY
		Integer idBinaryDataSource = insertBinary(postDatasetRequest, organization, idSubdomain);

		// INSERT LICENSE:
		Integer idLicense = insertLicense(postDatasetRequest.getLicense(), licenseMapper);

		// INSERT DCAT:
		Integer idDcat = insertDcat(postDatasetRequest.getDcat(), dcatMapper);

		// INSERT DATA SOURCE:
		Integer idDataSource = insertDataSource(postDatasetRequest.idSubdomain(idSubdomain), organization.getIdOrganization(), idDcat, idLicense, Status.INSTALLED.id(),
				dataSourceMapper);

		// INSERT DATASET
		Dataset dataset = ServiceUtil.insertDataset(idDataSource, DATASOURCE_VERSION, postDatasetRequest.getDatasetname(), DatasetSubtype.BULK.id(),
				postDatasetRequest.getImportfiletype(), DATASOURCE_VERSION, idBinaryDataSource, postDatasetRequest.getJdbcdburl(), postDatasetRequest.getJdbcdbname(),
				postDatasetRequest.getJdbcdbtype(), postDatasetRequest.getJdbctablename(), datasetMapper, sequenceMapper);

		// TAGS
		for (Integer idTag : postDatasetRequest.getTags()) {
			dataSourceMapper.insertTagDataSource(idDataSource, DATASOURCE_VERSION, idTag);
		}

		// COMPONENT
		insertComponents(postDatasetRequest.getComponents(), idDataSource, ServiceUtil.DATASOURCE_VERSION, ServiceUtil.DATASOURCE_VERSION, componentMapper);

		// TENANT-DATASOURCE
		insertTenantDataSource(postDatasetRequest.getIdTenant(), idDataSource, ServiceUtil.DATASOURCE_VERSION, Util.getNow(), tenantMapper);

		// SHARING TENANT
		insertSharingTenants(postDatasetRequest.getSharingTenants(), idDataSource, Util.getNow(), DataOption.READ_AND_USE.id(), ManageOption.NO_RIGHT.id(), tenantMapper);

		PostDatasetResponse response = PostDatasetResponse.build(dataset.getIddataset());
		// API
		try {
			CloseableHttpClient httpclient = PublisherDelegate.build().registerToStoreInit();
			if (!postDatasetRequest.getUnpublished()) {
				DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByDatasetCode(dataset.getDatasetcode(), false);

			String apiName = null;
			
			Bundles bundles = bundlesMapper.selectBundlesByTenantCode(dettaglioDataset.getTenantCode());
			
			
			apiMapper.insertApi(
					Api.buildOutput(DATASOURCE_VERSION)
					.apicode(dataset.getDatasetcode())
					.apiname(dettaglioDataset.getDatasetname())
					.apisubtype(API_SUBTYPE_ODATA)
					.idDataSource(dettaglioDataset.getIdDataSource())
					.entitynamespace( Api.ENTITY_NAMESPACE + dataset.getDatasetcode())
					.maxOdataResultperpage( bundles!= null ? bundles.getMaxOdataResultperpage() : MAX_ODATA_RESULT_PER_PAGE ));
				// publisher
				apiName = PublisherDelegate.build().addApi(httpclient, dettaglioDataset);
				PublisherDelegate.build().publishApi(httpclient, "1.0", apiName, "admin");
				SolrDelegate.build().addDocument(dettaglioDataset);

			} else {
				logger.info("[DatasetServiceImpl::insertDatasetTransaction] - unpublish datasetcode: " + postDatasetRequest.getDatasetcode());
				try {
					String removeApiResponse = PublisherDelegate.build().removeApi(httpclient, PublisherDelegate.createApiNameOData(postDatasetRequest.getDatasetcode()));
					logger.info("[DatasetServiceImpl::insertDatasetTransaction] - unpublish removeApi: " + removeApiResponse);

				} catch (Exception ex) {
					logger.error("[DatasetServiceImpl::insertDatasetTransaction] unpublish removeApi ERROR" + postDatasetRequest.getDatasetcode() + " - " + ex.getMessage());
				}				
				try {
					SolrDelegate.build().removeDocument(postDatasetRequest.getDatasetcode());
					logger.info("[DatasetServiceImpl::insertDatasetTransaction] - unpublish removeDocument: " + postDatasetRequest.getDatasetcode());
				} catch (Exception ex) {
					logger.error("[DatasetServiceImpl::insertDatasetTransaction] unpublish removeDocument ERROR" + postDatasetRequest.getDatasetcode() + " - " + ex.getMessage());
				}	
			}
		} catch (Exception e) {
			logger.error("[DatasetServiceImpl::insertDatasetTransaction] Publish API - error " + e.getMessage());
			e.printStackTrace();
			response.addWarning(" An error occurred during the publication of the API, please try to save again");
			throw new BadRequestException(Errors.INTERNAL_SERVER_ERROR, " An error occurred during the publication of the API, please try to save again");
		}
		
		response.setDatasetcode(dataset.getDatasetcode());
		response.setDatasetname(dataset.getDatasetname());
		return response;
	}

	/**
	 * 
	 * @param numColumn
	 * @param list
	 * @return
	 */
	private ComponentInfoRequest getInfoByNumColumn(Integer numColumn, List<ComponentInfoRequest> list) {
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
	private ComponentJson getComponentResponseById(Integer idComponent, ComponentJson[] components) {
		for (ComponentJson component : components) {

			if (component.getId_component().equals(idComponent)) {
				return component;
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
	private void checkComponentsSize(ComponentJson[] components, List<ComponentInfoRequest> componentInfoRequests) throws Exception {
		int columnCount = 0;
		for (ComponentInfoRequest info : componentInfoRequests) {
			if (!info.isSkipColumn())
				columnCount++;
		}
		logger.debug("[DatasetServiceImpl::checkComponentsSize] components.size:["+components.length+"], componentInfoRequestsCount:["+columnCount +"]");
		if (columnCount != components.length || columnCount==0) {
			throw new BadRequestException(Errors.NOT_ACCEPTABLE);
		}
	}

	@Override
	public ServiceResponse importMetadata(String organizationCode, ImportMetadataDatasetRequest importMetadataRequest,  JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception {

		List<DettaglioDataset> existingMedatataList = datasetMapper.selectDatasetFromJdbc(importMetadataRequest.getJdbcHostname(), importMetadataRequest.getJdbcDbname(),
				importMetadataRequest.getDbType(), importMetadataRequest.getTenantCode(), organizationCode, getTenantCodeListFromUser(authorizedUser));
		
		//DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDataset(tenantCodeManager, idDataset, organizationCode, getTenantCodeListFromUser(authorizedUser));


		DatabaseReader databaseReader = new DatabaseReader(organizationCode, importMetadataRequest.getTenantCode(), importMetadataRequest.getDbType(),
				importMetadataRequest.getJdbcHostname(), importMetadataRequest.getJdbcDbname(), importMetadataRequest.getJdbcUsername(), importMetadataRequest.getJdbcPassword(),
				existingMedatataList, hiveUser, hivePassword, hiveUrl);
		String schema = databaseReader.loadSchema();

		return buildResponse(schema);
	}

	private BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetResponse(DettaglioDataset dettaglioDataset) throws Exception{

		if (dettaglioDataset.getIdDataSourceBinary() != null) {
			DettaglioDataset dettaglioBinary = datasetMapper.selectDettaglioDatasetByDatasource(dettaglioDataset.getIdDataSourceBinary(), dettaglioDataset.getDatasourceversionBinary());
			return new BackofficeDettaglioStreamDatasetResponse(dettaglioDataset, dettaglioBinary);
		}
		
		if (DatasetSubtype.STREAM.id().equals(dettaglioDataset.getIdDatasetSubtype()) || DatasetSubtype.SOCIAL.id().equals(dettaglioDataset.getIdDatasetSubtype())) {

			DettaglioStream dettaglioStream = streamMapper.selectStreamByDatasource(dettaglioDataset.getIdDataSource(), dettaglioDataset.getDatasourceversion());
			if (dettaglioStream != null) {

				DettaglioSmartobject dettaglioSmartobject = smartobjectMapper.selectSmartobjectById(dettaglioStream.getIdSmartObject());

				List<InternalDettaglioStream> listInternalStream = streamMapper.selectInternalStream(dettaglioStream.getIdDataSource(), dettaglioStream.getDatasourceversion());

				return new BackofficeDettaglioStreamDatasetResponse(dettaglioStream, dettaglioDataset, dettaglioSmartobject, listInternalStream);
			}
		}
		
		return new BackofficeDettaglioStreamDatasetResponse(dettaglioDataset, null);
	}

	private void removeOdataApiAndSolrDocument(String datasetCode) throws Exception{
		CloseableHttpClient httpclient = PublisherDelegate.build().registerToStoreInit();
		removeOdataApiAndSolrDocument(httpclient, datasetCode);
	}
	
	private void removeOdataApiAndSolrDocument(CloseableHttpClient httpclient, String datasetCode) throws Exception{
		logger.info("[DatasetServiceImpl::updateDatasetTransaction] - unpublish datasetcode: " + datasetCode);
		try {
			String removeApiResponse = PublisherDelegate.build().removeApi(httpclient, PublisherDelegate.createApiNameOData(datasetCode));
			logger.info("[DatasetServiceImpl::updateDatasetTransaction] - unpublish removeApi: " + removeApiResponse);

		} catch (Exception ex) {
			logger.error("[DatasetServiceImpl::updateDatasetTransaction] unpublish removeApi ERROR" + datasetCode + " - " + ex.getMessage());
		}

		SolrDelegate.build().removeDocument(datasetCode);
	}


	
}
