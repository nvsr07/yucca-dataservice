package org.csi.yucca.adminapi.util;

import static org.csi.yucca.adminapi.util.Util.notEqual;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
import org.csi.yucca.adminapi.mapper.SmartobjectMapper;
import org.csi.yucca.adminapi.mapper.StreamMapper;
import org.csi.yucca.adminapi.mapper.TenantMapper;
import org.csi.yucca.adminapi.messaging.MessageSender;
import org.csi.yucca.adminapi.model.Component;
import org.csi.yucca.adminapi.model.DataSource;
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.Dcat;
import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.InternalDettaglioStream;
import org.csi.yucca.adminapi.model.License;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.model.Smartobject;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.model.TenantDataSource;
import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;
import org.csi.yucca.adminapi.request.ActionRequest;
import org.csi.yucca.adminapi.request.ComponentRequest;
import org.csi.yucca.adminapi.request.DcatRequest;
import org.csi.yucca.adminapi.request.IDataSourceRequest;
import org.csi.yucca.adminapi.request.IVisibility;
import org.csi.yucca.adminapi.request.LicenseRequest;
import org.csi.yucca.adminapi.request.OpenDataRequest;
import org.csi.yucca.adminapi.request.SharingTenantRequest;
import org.csi.yucca.adminapi.request.SmartobjectRequest;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ServiceUtil {

	private static final String SORT_PROPERTIES_SEPARATOR = ",";
	private static final String DESC_CHAR = "-";
	public static final String MULTI_SUBDOMAIN_PATTERN = "^[\\S]*$";
	public static final String UUID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
	public static final String NOT_DEVICE_PATTERN = "^[a-zA-Z0-9-]{5,100}$";
	public static final String ALPHANUMERIC_PATTERN = "^[a-zA-Z0-9]*$";
	public static final String ALPHANUMERICOrUnderscore_PATTERN = "^[a-zA-Z0-9_]*$";
	public static final String COMPONENT_NAME_PATTERN = "(.)*[\u00C0-\u00F6\u00F8-\u00FF\u0020]+(.)*|^[0-9]*$";

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
	public static final Integer MAXIMUM_ERRORS_ALLOWED = 20;
	public static final String FAULT_STRING_CHILD_NODE_NAME = "faultstring";
	public static final String FAULT_STRING_NODE_NAME = "soapenv:Fault";

	/**
	 * 
	 * @param list
	 * @param fileName
	 * @param delimiterChar
	 * @param httpServletResponse
	 * @param header
	 * @throws Exception
	 */
	public static <T> void downloadCsv(List<T> list, String fileName, int delimiterChar,
			HttpServletResponse httpServletResponse, String... header) throws Exception {

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

	/**
	 * 
	 * @param dettaglioDataset
	 * @param streamMapper
	 * @param smartobjectMapper
	 * @param datasetMapper
	 * @return
	 * @throws Exception
	 */
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDataset(DettaglioDataset dettaglioDataset,
			StreamMapper streamMapper, SmartobjectMapper smartobjectMapper, DatasetMapper datasetMapper)
			throws Exception {

		checkIfFoundRecord(dettaglioDataset);

		if (DatasetSubtype.STREAM.id().equals(dettaglioDataset.getIdDatasetSubtype())
				|| DatasetSubtype.SOCIAL.id().equals(dettaglioDataset.getIdDatasetSubtype())) {

			DettaglioStream dettaglioStream = streamMapper.selectStreamByDatasource(dettaglioDataset.getIdDataSource(),
					dettaglioDataset.getDatasourceversion());
			if (dettaglioStream != null) {

				DettaglioSmartobject dettaglioSmartobject = smartobjectMapper
						.selectSmartobjectById(dettaglioStream.getIdSmartObject());

				List<InternalDettaglioStream> listInternalStream = streamMapper.selectInternalStream(
						dettaglioStream.getIdDataSource(), dettaglioStream.getDatasourceversion());

				return new BackofficeDettaglioStreamDatasetResponse(dettaglioStream, dettaglioDataset,
						dettaglioSmartobject, listInternalStream);
			}

		}

		DettaglioDataset dettaglioBinary = null;

		if (dettaglioDataset.getIdDataSourceBinary() != null) {
			dettaglioBinary = datasetMapper.selectDettaglioDatasetByDatasource(dettaglioDataset.getIdDataSourceBinary(),
					dettaglioDataset.getDatasourceversionBinary());
		}

		return new BackofficeDettaglioStreamDatasetResponse(dettaglioDataset, dettaglioBinary);
	}

	/**
	 * 
	 * @param idStatus
	 * @param idDataSource
	 * @param dataSourceMapper
	 * @throws Exception
	 */
	public static void updateDataSourceStatusAllVersion(Integer idStatus, Integer idDataSource,
			DataSourceMapper dataSourceMapper) throws Exception {
		dataSourceMapper.updateDataSourceStatus(idStatus, idDataSource, null);
	}

	/**
	 * 
	 * @param nodeList
	 * @return
	 */
	public static String getFaultString(NodeList nodeList) {
		if (nodeList != null) {

			for (int i = 0; i < nodeList.getLength(); i++) {

				Node nodeItem = nodeList.item(i);

				if (FAULT_STRING_CHILD_NODE_NAME.equals(nodeItem.getNodeName())) {
					return nodeItem.getTextContent();
				}
			}
		}

		return "";
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	public static String getFaultString(Node node) {

		if (node == null) {
			return "";
		}

		if (FAULT_STRING_NODE_NAME.equals(node.getNodeName())) {
			return getFaultString(node.getChildNodes());
		} else {
			return getFaultString(node.getFirstChild());
		}

	}

	/**
	 * 
	 * @param webServiceResponse
	 * @return
	 * @throws Exception
	 */
	public static String getFaultString(WebServiceResponse webServiceResponse) throws Exception {
		return getFaultString(getDocument(webServiceResponse).getFirstChild());
	}

	/**
	 * 
	 * @param webServiceResponse
	 * @return
	 * @throws Exception
	 */
	public static Document getDocument(WebServiceResponse webServiceResponse) throws Exception {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		InputSource inputSource = new InputSource(new StringReader(webServiceResponse.getMessage()));

		Document document = documentBuilder.parse(inputSource);

		return document;
	}

	/**
	 * 
	 * @param actionRequest
	 * @param name
	 * @param idOrCode
	 * @param messageSender
	 * @throws Exception
	 */
	public static void sendMessage(ActionRequest actionRequest, String name, String idOrCode,
			MessageSender messageSender) throws Exception {
		String steps = actionRequest.getStartStep();
		if (actionRequest.getEndStep() != null) {
			steps += ":" + actionRequest.getEndStep();
		}

		String msg = actionRequest.getAction() + "|" + name + "|" + idOrCode + "|" + steps;
		messageSender.sendMessage(msg);
	}

	/**
	 * 
	 * @param actionRequest
	 * @param name
	 * @param idOrCode
	 * @param messageSender
	 * @throws Exception
	 */
	public static void sendMessage(ActionRequest actionRequest, String name, Integer idOrCode,
			MessageSender messageSender) throws Exception {
		sendMessage(actionRequest, name, String.valueOf(idOrCode), messageSender);
	}

	/**
	 * 
	 * @param errors
	 * @return
	 */
	public static boolean maximumLimitErrorsReached(List<String> errors) {
		return errors.size() == MAXIMUM_ERRORS_ALLOWED;
	}

	/**
	 * 
	 * @param idTagList
	 * @param idDataSource
	 * @param dataSourceMapper
	 */
	public static void insertTags(List<Integer> idTagList, Integer idDataSource, Integer dataSourceVersion,
			DataSourceMapper dataSourceMapper) {
		for (Integer idTag : idTagList) {
			dataSourceMapper.insertTagDataSource(idDataSource, dataSourceVersion, idTag);
		}
	}

	/**
	 * 
	 * @param listTags
	 * @param idDataSource
	 * @param dataSourceVersion
	 * @param dataSourceMapper
	 * @throws Exception
	 */
	public static void updateTagDataSource(List<Integer> listTags, Integer idDataSource, Integer dataSourceVersion,
			DataSourceMapper dataSourceMapper) throws Exception {

		dataSourceMapper.deleteTagDataSource(idDataSource, dataSourceVersion);

		for (Integer idTag : listTags) {
			dataSourceMapper.insertTagDataSource(idDataSource, dataSourceVersion, idTag);
		}
	}

	/**
	 * 
	 * @param streamRequest
	 * @param idDcat
	 * @param idLicense
	 * @return
	 * @throws Exception
	 */
	public static int updateDataSource(IDataSourceRequest streamRequest, Integer idDcat, Integer idLicense,
			Integer idDataSource, Integer dataSourceVersion, DataSourceMapper dataSourceMapper) throws Exception {

		DataSource dataSource = new DataSource();
		dataSource.setIdDataSource(idDataSource);
		dataSource.setDatasourceversion(dataSourceVersion);

		dataSource.setUnpublished(Util.booleanToInt(streamRequest.getUnpublished()));
		dataSource.setName(streamRequest.getName());
		dataSource.setVisibility(streamRequest.getVisibility());
		dataSource.setCopyright(streamRequest.getCopyright());
		dataSource.setDisclaimer(streamRequest.getDisclaimer());
		dataSource.setIcon(streamRequest.getIcon());
		dataSource.setIsopendata(
				streamRequest.getOpendata() != null ? Util.booleanToInt(true) : Util.booleanToInt(false));
		if (streamRequest.getOpendata() != null) {
			dataSource.setExternalreference(streamRequest.getExternalreference());
			dataSource.setOpendataauthor(streamRequest.getOpendata().getOpendataauthor());
			dataSource.setOpendataupdatedate(
					Util.dateStringToTimestamp(streamRequest.getOpendata().getOpendataupdatedate()));
			dataSource.setOpendatalanguage(streamRequest.getOpendata().getOpendatalanguage());
			dataSource.setLastupdate(streamRequest.getOpendata().getLastupdate());
		}
		dataSource.setIdDcat(idDcat);
		dataSource.setIdLicense(idLicense);

		return dataSourceMapper.updateDataSource(dataSource);
	}

	/**
	 * 
	 * @param listComponentRequest
	 * @param idDataSource
	 * @param dataSourceVersion
	 * @param componentMapper
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	public static void checkComponents(List<ComponentRequest> listComponentRequest, Integer idDataSource,
			Integer dataSourceVersion, ComponentMapper componentMapper) throws NotFoundException, BadRequestException {
		checkComponents(listComponentRequest, null, idDataSource, dataSourceVersion, componentMapper);
	}

	/**
	 * 
	 * @param request
	 * @param idSoType
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	public static void checkComponents(List<ComponentRequest> listComponentRequest, Integer idSoType,
			ComponentMapper componentMapper) throws NotFoundException, BadRequestException {
		checkComponents(listComponentRequest, idSoType, null, null, componentMapper);
	}

	/**
	 * 
	 * @param listComponentRequest
	 * @param componentMapper
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	public static void checkComponents(List<ComponentRequest> listComponentRequest, ComponentMapper componentMapper)
			throws NotFoundException, BadRequestException {
		checkComponents(listComponentRequest, null, componentMapper);
	}

	/**
	 * 
	 * @param listComponentRequest
	 * @param name
	 * @throws BadRequestException
	 */
	private static void checkUnicComponentName(List<ComponentRequest> listComponentRequest, String name)
			throws BadRequestException {
		int count = 0;
		for (ComponentRequest component : listComponentRequest) {

			if (component.getName().equals(name)) {
				count++;
			}

			if (count > 1) {
				throw new BadRequestException(Errors.NOT_ACCEPTABLE, "The name field must be unique.");
			}
		}
	}

	/**
	 * 
	 * @param listToCheck
	 * @param name
	 * @return
	 */
	private static boolean doesNotContainComponent(List<ComponentRequest> listToCheck, String name) {
		for (ComponentRequest component : listToCheck) {
			if (component.getIdComponent() != null && component.getName().equals(name)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param listToCheck
	 * @param component
	 * @return
	 */
	private static boolean doesNotContainComponent(List<Component> listToCheck, Integer idComponent) {
		for (Component component : listToCheck) {
			if (component.getIdComponent().equals(idComponent)) {
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
	private static List<Component> selectAlreadyPresentComponents(Integer idDataSource, Integer dataSourceVersion,
			ComponentMapper componentMapper) {

		if (idDataSource == null || dataSourceVersion == null) {
			return null;
		}

		return componentMapper.selectComponentByDataSourceAndVersion(idDataSource, dataSourceVersion);
	}

	/**
	 * 
	 * @param listComponentRequest
	 * @param idSoType
	 * @param idDataSource
	 * @param dataSourceVersion
	 * @param componentMapper
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	public static void checkComponents(List<ComponentRequest> listComponentRequest, Integer idSoType,
			Integer idDataSource, Integer dataSourceVersion, ComponentMapper componentMapper)
			throws NotFoundException, BadRequestException {

		checkComponents(listComponentRequest, idSoType, idDataSource, dataSourceVersion, componentMapper, false);

	}

	/**
	 * 
	 * @param listComponentRequest
	 * @param idSoType
	 * @param idDataSource
	 * @param dataSourceVersion
	 * @param componentMapper
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	public static void checkStreamComponents(List<ComponentRequest> listComponentRequest, Integer idSoType,
			Integer idDataSource, Integer dataSourceVersion, ComponentMapper componentMapper)
			throws NotFoundException, BadRequestException {

		checkComponents(listComponentRequest, idSoType, idDataSource, dataSourceVersion, componentMapper, true);

	}

	/**
	 * 
	 * @param request
	 * @param idSoType
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	public static void checkComponents(List<ComponentRequest> listComponentRequest, Integer idSoType,
			Integer idDataSource, Integer dataSourceVersion, ComponentMapper componentMapper, boolean isStream)
			throws NotFoundException, BadRequestException {

		List<Component> alreadyPresentComponentsPreviousVersion = null;
		if (dataSourceVersion != null) {
			Integer dataSourceVersionPrevVersion = dataSourceVersion;
			if (!isStream) // nel caso di dataset la versione che arriva dalla
							// request coicide con la versione installata,
							// allora alreadyPresentComponentsPreviousVersion
							// diventa uguale a alreadyPresentComponents
				dataSourceVersionPrevVersion++;
			alreadyPresentComponentsPreviousVersion = getAlreadyPresentComponentsPreviousVersion(idDataSource,
					dataSourceVersionPrevVersion, componentMapper);
		}
		if (idSoType == null || Type.FEED_TWEET.id() != idSoType) {

			ServiceUtil.checkList(listComponentRequest);

			List<Component> alreadyPresentComponents = selectAlreadyPresentComponents(idDataSource, dataSourceVersion,
					componentMapper);

			for (ComponentRequest component : listComponentRequest) {

				ServiceUtil.checkMandatoryParameter(component.getName(), "component name");

				/**
				 * ALREADY_PRESENT Verificare che tutti gli idComponent siano
				 * compresi tra quelli ritornati dalla query. In caso contrario
				 * RITORNARE: Errore: Some idComponent is incorrect
				 */
				if (component.getIdComponent() != null
						&& doesNotContainComponent(alreadyPresentComponents, component.getIdComponent())) {
					throw new BadRequestException(Errors.NOT_ACCEPTABLE,
							"Some idComponent is incorrect: " + component.getIdComponent());
				}

				// sono stati inseriti campi non modificabili nei vcampi delle
				// precedenti versione
				if (component.getIdComponent() != null) {
					for (Component cmp : alreadyPresentComponentsPreviousVersion) {

						if (component.getIdComponent().equals(cmp.getIdComponent()) &&

								(notEqual(component.getName(), cmp.getName())
										|| notEqual(component.getTolerance(), cmp.getTolerance())
										|| notEqual(component.getIdPhenomenon(), cmp.getIdPhenomenon())
										|| notEqual(component.getIdDataType(), cmp.getIdDataType())
										|| notEqual(component.getIskey(), Util.intToBoolean(cmp.getIskey()))
										|| notEqual(component.getSourcecolumn(), cmp.getSourcecolumn())
										|| notEqual(component.getSourcecolumnname(), cmp.getSourcecolumnname())
										|| notEqual(component.getRequired(), Util.intToBoolean(cmp.getRequired())))) {
							throw new BadRequestException(Errors.NOT_ACCEPTABLE,
									"The only field you can modify are: alias, inorder and idMeasureUnit");
						}
					}
				}

				/**
				 * NEW
				 */
				if (component.getIdComponent() == null) {
					checkUnicComponentName(listComponentRequest, component.getName());
					ServiceUtil.checkAphanumeric(component.getName(), "component name");
					ServiceUtil.checkMandatoryParameter(component.getAlias(), "alias");
					ServiceUtil.checkMandatoryParameter(component.getInorder(), "inorder");
					if (isStream) {
						ServiceUtil.checkMandatoryParameter(component.getTolerance(), "tolerance");
						ServiceUtil.checkMandatoryParameter(component.getIdPhenomenon(), "idPhenomenon");
						// Se il dataType è lat, lon o dateTime idMeasureUnit
						// non è valorizzato
						if (component.getIdDataType() != Constants.ADMINAPI_DATA_TYPE_DATETIME
								&& component.getIdDataType() != Constants.ADMINAPI_DATA_TYPE_LONGITUDE
								&& component.getIdDataType() != Constants.ADMINAPI_DATA_TYPE_LATITUDE)
							ServiceUtil.checkMandatoryParameter(component.getIdMeasureUnit(), "idMeasureUnit");
						ServiceUtil.checkMandatoryParameter(component.getRequired(), "required");
					}

					ServiceUtil.checkMandatoryParameter(component.getIdDataType(), "idDataType");

				}

			}
		}

		/**
		 * ALREADY_PRESENT Verificare che tutti i campi name estratti dalla
		 * query siano presenti nei campi name degli ALREADY_PRESENT_req. In
		 * caso contrario RITORNARE: Errore: You can't remove components from
		 * previous version.
		 */
		if (alreadyPresentComponentsPreviousVersion != null) {
			for (Component prevcomponent : alreadyPresentComponentsPreviousVersion) {
				if (doesNotContainComponent(listComponentRequest, prevcomponent.getName())) {
					throw new BadRequestException(Errors.NOT_ACCEPTABLE,
							" You can't remove components from previous version.");
				}
			}
		}
	}

	/**
	 * 
	 * @param idDataSource
	 * @param version
	 * @param componentMapper
	 * @return
	 */
	private static List<Component> getAlreadyPresentComponentsPreviousVersion(Integer idDataSource, Integer version,
			ComponentMapper componentMapper) {

		if (idDataSource == null || version == null) {
			return null;
		}

		if (version > 1) {
			return componentMapper.selectComponentByDataSourceAndVersion(idDataSource, (version - 1));
		}

		return null;
	}

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
			Timestamp now, Integer dataOptions, Integer manageOptions, TenantMapper tenantMapper) throws Exception {
		insertSharingTenants(listSharingTenantRequest, idDataSource, now, dataOptions, manageOptions, null,
				tenantMapper);
	}

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
			Timestamp now, Integer dataOptions, Integer manageOptions, Integer dataSourceVersion,
			TenantMapper tenantMapper) throws Exception {

		if (listSharingTenantRequest != null) {
			for (SharingTenantRequest sharingTenantRequest : listSharingTenantRequest) {
				TenantDataSource tenantDataSource = new TenantDataSource();
				tenantDataSource.setIdDataSource(idDataSource);
				tenantDataSource.setDatasourceversion(
						dataSourceVersion == null ? ServiceUtil.DATASOURCE_VERSION : dataSourceVersion);
				tenantDataSource.setIdTenant(sharingTenantRequest.getIdTenant());
				tenantDataSource.setIsactive(Util.booleanToInt(true));
				tenantDataSource.setIsmanager(Util.booleanToInt(false));
				tenantDataSource.setActivationdate(now);
				tenantDataSource.setManagerfrom(now);
				tenantDataSource
						.setDataoptions(dataOptions == null ? sharingTenantRequest.getDataOptions() : dataOptions);
				tenantDataSource.setManageoptions(
						manageOptions == null ? sharingTenantRequest.getManageOptions() : manageOptions);
				tenantMapper.insertTenantDataSource(tenantDataSource);
			}
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
	public static void insertSharingTenants(List<SharingTenantRequest> listSharingTenantRequest, Integer idDataSource,
			Timestamp now, TenantMapper tenantMapper) throws Exception {
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
	public static void insertTenantDataSource(Integer idTenant, Integer idDataSource, Integer dataSourceVersion,
			Timestamp now, TenantMapper tenantMapper) throws Exception {
		TenantDataSource tenantDataSource = new TenantDataSource();
		tenantDataSource.setIdDataSource(idDataSource);
		tenantDataSource.setDatasourceversion(dataSourceVersion);
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
			Integer datasourceVersion, Integer sinceVersion, ComponentMapper componentMapper) throws Exception {

		for (ComponentRequest componentRequest : listComponentRequest) {

			if (componentRequest.getIdComponent() == null) {
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
				component.required(componentRequest.getRequired());
				component.setIskey(componentRequest.getIskey());
				component.setSinceVersion(sinceVersion);
				component.setIdDataSource(idDataSource);
				component.setDatasourceversion(datasourceVersion);
				componentMapper.insertComponent(component);
			}
		}
	}

	// /**
	// *
	// * @param listComponentRequest
	// * @param idDataSource
	// * @param datasourceVersion
	// * @param sinceVersion
	// * @param componentMapper
	// * @throws Exception
	// */
	// public static void insertComponents(List<ComponentRequest>
	// listComponentRequest, Integer idDataSource,
	// Integer datasourceVersion, Integer sinceVersion, ComponentMapper
	// componentMapper)throws Exception{
	// insertComponents(listComponentRequest, idDataSource, datasourceVersion,
	// sinceVersion, null, componentMapper);
	//
	// }

	/**
	 * 
	 * @param idBinaryDataSource
	 * @param componentMapper
	 * @throws Exception
	 */
	public static void insertBinaryComponents(Integer idBinaryDataSource, ComponentMapper componentMapper)
			throws Exception {
		componentMapper.insertComponent(new Component().idDataType(DataType.LONG.id()).name("idBinary").alias("Id")
				.idDataSource(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(new Component().idDataType(DataType.STRING.id()).name("filenameBinary")
				.alias("File").idDataSource(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(new Component().idDataType(DataType.STRING.id()).name("aliasNameBinary")
				.alias("File").idDataSource(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(new Component().idDataType(DataType.STRING.id()).name("sizeBinary")
				.alias("File Size").idDataSource(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(
				new Component().idDataType(DataType.DATE_TIME.id()).name("insertDateBinary").alias("Insert Date")
						.idDataSource(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(
				new Component().idDataType(DataType.DATE_TIME.id()).name("lastUpdateDateBinary").alias("Last Update")
						.idDataSource(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(
				new Component().idDataType(DataType.STRING.id()).name("contentTypeBinary").alias("Content Type")
						.idDataSource(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(
				new Component().idDataType(DataType.STRING.id()).name("urlDownloadBinary").alias("Download url")
						.idDataSource(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
		componentMapper.insertComponent(new Component().idDataType(DataType.STRING.id()).name("metadataBinary")
				.alias("Metadata").idDataSource(idBinaryDataSource).datasourceversion(ServiceUtil.DATASOURCE_VERSION));
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
	public static Dataset insertDataset(Integer idDataSource, Integer dataSourceVersion, String datasetName,
			Integer idDatasetSubtype, String importFileData, Integer dataSourceVersionBinary,
			Integer idDataSourceBinary, String jdbcdburl, String jdbcdbname, String jdbcdbtype, String jdbctablename,
			Tenant tenant, Organization organization, DatasetMapper datasetMapper, SequenceMapper sequenceMapper) throws Exception {
		return insertDataset(
				true, // saveData
				idDataSource, 
				null, // idSoType
				dataSourceVersion, datasetName, idDatasetSubtype, importFileData, dataSourceVersionBinary,
				idDataSourceBinary, jdbcdburl, jdbcdbname, jdbcdbtype, jdbctablename, tenant, organization, datasetMapper, sequenceMapper);
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
	public static Dataset insertDataset(Integer idDataSource, Integer dataSourceVersion, String datasetName,
			Integer idDatasetSubtype, Tenant tenant, Organization organization, DatasetMapper datasetMapper, SequenceMapper sequenceMapper) throws Exception {
		return insertDataset(true, // saveData
				idDataSource, null, // idSoType
				dataSourceVersion, datasetName, idDatasetSubtype, null, // importFiledata
				null, // dataSourceVersionBinary,
				null, // idDataSourceBinary,
				null, // jdbcdburl,
				null, // jdbcdbname,
				null, // jdbcdbtype,
				null, // jdbctablename,
				tenant, organization,
				datasetMapper, sequenceMapper);
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
	public static Dataset insertDataset(boolean saveData, Integer idDataSource, Integer idSoType,
			Integer dataSourceVersion, String datasetName, Tenant tenant, Organization organization, DatasetMapper datasetMapper, SequenceMapper sequenceMapper)
			throws Exception {
		return insertDataset(saveData, idDataSource, idSoType, dataSourceVersion, datasetName, null, // idDatasetSubtype
				null, // importFiledata
				null, // dataSourceVersionBinary,
				null, // idDataSourceBinary,
				null, // jdbcdburl,
				null, // jdbcdbname,
				null, // jdbcdbtype,
				null, // jdbctablename,
				tenant, organization,
				datasetMapper, sequenceMapper);
	}

	/**
	 * 
	 * @param datasetName
	 * @return
	 */
	private static String getDatasetDescription(String datasetName){
		return "Dataset " + datasetName;
	}
	
	/**
	 * 
	 * @param saveData
	 * @param idDataSource
	 * @param idSoType
	 * @param dataSourceVersion
	 * @param datasetName
	 * @param idDatasetSubtype
	 * @param importFileType
	 * @param dataSourceVersionBinary
	 * @param idDataSourceBinary
	 * @param jdbcdburl
	 * @param jdbcdbname
	 * @param jdbcdbtype
	 * @param jdbctablename
	 * @param tenant
	 * @param organization
	 * @param datasetMapper
	 * @param sequenceMapper
	 * @return
	 * @throws Exception
	 */
	private static Dataset insertDataset(boolean saveData, Integer idDataSource, Integer idSoType,
			Integer dataSourceVersion, String datasetName, Integer idDatasetSubtype, String importFileType,
			Integer dataSourceVersionBinary, Integer idDataSourceBinary, String jdbcdburl, String jdbcdbname,
			String jdbcdbtype, String jdbctablename, Tenant tenant, Organization organization,
			DatasetMapper datasetMapper, SequenceMapper sequenceMapper) throws Exception {

		Dataset checkDataSet = datasetMapper.selectDataSet(idDataSource, dataSourceVersion);

		if (checkDataSet != null) {
			return checkDataSet;
		}

		if (saveData) {
			Integer iddataset               = sequenceMapper.selectDatasetSequence();
			Integer currentIdDatasetSubtype = getIdDatasetSubtype(idSoType, idDatasetSubtype);
			Dataset slrCollectionAndPhoenix = getSlrCollectionAndPhoenix(currentIdDatasetSubtype, organization, tenant);
			String datasetCode              = getDatasetcode(currentIdDatasetSubtype, datasetName, iddataset);
			String datasetDescription       = getDatasetDescription(datasetName);
			
			Dataset dataset = new Dataset();
			dataset.setIdDatasetType(DatasetType.DATASET.id());
			dataset.setAvailablespeed(Util.booleanToInt(true));
			dataset.setIstransformed(Util.booleanToInt(false));
			dataset.setIddataset(iddataset);
			dataset.setIdDataSource(idDataSource);
			dataset.setDatasourceversion(dataSourceVersion);
			dataset.setDatasetname(datasetName);
			dataset.setDatasetcode(datasetCode);
			dataset.setDescription(datasetDescription);
			dataset.setIdDatasetSubtype(currentIdDatasetSubtype);
			dataset.setImportfiletype(importFileType);
			dataset.setIdDataSourceBinary(idDataSourceBinary);
			dataset.setDatasourceversionBinary(dataSourceVersionBinary);
			dataset.setJdbcdburl(jdbcdburl);
			dataset.setJdbcdbname(jdbcdbname);
			dataset.setJdbcdbtype(jdbcdbtype);
			dataset.setJdbctablename(jdbctablename);
			dataset.setSolrcollectionname(slrCollectionAndPhoenix.getSolrcollectionname());
			dataset.setPhoenixtablename(slrCollectionAndPhoenix.getPhoenixtablename());
			dataset.setPhoenixschemaname(slrCollectionAndPhoenix.getPhoenixschemaname());
			
			datasetMapper.insertDataset(dataset);
			return dataset;
		}

		return null;
	}

	/**
	 * Si confronta con quella dell'organizzaione (yucca_organization) e, per
	 * ognuno, se il valore del tenant è diverso da quello dell'organizzazione e
	 * non è nullo... allora si mette quello nel dataset.
	 * 
	 * @param fromTenant
	 * @param fromOrg
	 * @return
	 */
	private static String getSolrPhoenixName(String fromTenant, String fromOrg) {
		if (fromTenant != null && !fromTenant.equals(fromOrg)) {
			return fromTenant;
		}
		return null;
	}

	/**
	 * -- BULK: datasolrcollectionname, dataphoenixtablename,
	 * dataphoenixschemaname,
	 * 
	 * -- STREAM: measuresphoenixtablename, measuresphoenixschemaname,
	 * measuresolrcollectionname,
	 * 
	 * -- BINARY: mediasolrcollectionname, mediaphoenixtablename,
	 * mediaphoenixschemaname,
	 * 
	 * -- SOCIAL: socialsolrcollectionname, socialphoenixtablename,
	 * socialphoenixschemaname Si confronta con quella dell'organizzaione
	 * (yucca_organization) e, per ognuno, se il valore del tenant è diverso da
	 * quello dell'organizzazione e non è nullo... allora si mette quello nel
	 * dataset.
	 * 
	 * 
	 * @param dataset
	 * @param org
	 * @param tenant
	 * @param idDatasetSubtype
	 */
	private static Dataset getSlrCollectionAndPhoenix(Integer idDatasetSubtype, Organization org, Tenant tenant) {

		Dataset dataset = new Dataset();
		
		if (DatasetSubtype.BULK.id().equals(idDatasetSubtype)) {
			dataset.setSolrcollectionname(getSolrPhoenixName(tenant.getDatasolrcollectionname(), org.getDatasolrcollectionname()));
			dataset.setPhoenixtablename(getSolrPhoenixName(tenant.getDataphoenixtablename(), org.getDataphoenixtablename()));
			dataset.setPhoenixschemaname(getSolrPhoenixName(tenant.getDataphoenixschemaname(), org.getDataphoenixschemaname()));
		} 
		else if (DatasetSubtype.STREAM.id().equals(idDatasetSubtype)) {
			dataset.setSolrcollectionname(getSolrPhoenixName(tenant.getMeasuresolrcollectionname(), org.getMeasuresolrcollectionname()));
			dataset.setPhoenixtablename(getSolrPhoenixName(tenant.getMeasuresphoenixtablename(), org.getMeasuresphoenixtablename()));
			dataset.setPhoenixschemaname(getSolrPhoenixName(tenant.getMeasuresphoenixschemaname(), org.getMeasuresphoenixschemaname()));
		} 
		else if (DatasetSubtype.BINARY.id().equals(idDatasetSubtype)) {
			dataset.setSolrcollectionname(getSolrPhoenixName(tenant.getMediasolrcollectionname(), org.getMediasolrcollectionname()));
			dataset.setPhoenixtablename(getSolrPhoenixName(tenant.getMediaphoenixtablename(), org.getMediaphoenixtablename()));
			dataset.setPhoenixschemaname(getSolrPhoenixName(tenant.getMediaphoenixschemaname(), org.getMediaphoenixschemaname()));
		}
		else if (DatasetSubtype.SOCIAL.id().equals(idDatasetSubtype)) {
			dataset.setSolrcollectionname(getSolrPhoenixName(tenant.getSocialsolrcollectionname(), org.getSocialsolrcollectionname()));
			dataset.setPhoenixtablename(getSolrPhoenixName(tenant.getSocialphoenixtablename(), org.getSocialphoenixtablename()));
			dataset.setPhoenixschemaname(getSolrPhoenixName(tenant.getSocialphoenixschemaname(), org.getSocialphoenixschemaname()));
		}
		
		return dataset;
		
	}

	/**
	 * 
	 * @param idSoType
	 * @param idDatasetSubtype
	 * @return
	 */
	private static Integer getIdDatasetSubtype(Integer idSoType, Integer idDatasetSubtype) {
		
		if (Type.FEED_TWEET.id() == idSoType) {
			return DatasetSubtype.SOCIAL.id();
		}		

		if (idSoType != null) {
			return DatasetSubtype.STREAM.id();
		}		
		
		return idDatasetSubtype;
	}

	/**
	 * 
	 * @param idDatasetSubtype
	 * @param datasetName
	 * @param iddataset
	 * @return
	 */
	private static String getDatasetcode(Integer idDatasetSubtype, String datasetName, Integer iddataset) {
		
		if (DatasetSubtype.BINARY.id() == idDatasetSubtype) {
			return BINARY_DATASET_PREFIX_CODE + Util.cleanStringCamelCase(datasetName) + "_" + iddataset;
		} 
		
		if (DatasetSubtype.BULK.id() == idDatasetSubtype) {
			return Util.cleanStringCamelCase(datasetName) + "_" + iddataset;
		} 
		
		return STREAM_DATASET_PREFIX_CODE + Util.cleanStringCamelCase(datasetName) + "_" + iddataset;
	}
	
	/**
	 * 
	 * @param dcatRequest
	 * @return
	 */
	public static Integer insertDcat(DcatRequest dcatRequest, DcatMapper dcatMapper) throws Exception {

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
	public static Integer insertLicense(LicenseRequest licenseRequest, LicenseMapper licenseMapper) throws Exception {

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
	public static Integer insertDataSource(IDataSourceRequest request, Integer idOrganization, Integer idStatus,
			DataSourceMapper dataSourceMapper) throws Exception {
		return insertDataSource(request, idOrganization, null, null, idStatus, dataSourceMapper);
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
	public static Integer insertDataSource(IDataSourceRequest request, Integer idOrganization, Integer idDcat,
			Integer idLicense, Integer idStatus, DataSourceMapper dataSourceMapper) throws Exception {

		DataSource dataSource = new DataSource();
		dataSource.setDatasourceversion(DATASOURCE_VERSION);
		dataSource.setIscurrent(Util.booleanToInt(true));
		dataSource.setUnpublished(Util.booleanToInt(request.getUnpublished()));
		dataSource.setName(request.getName());
		dataSource.setVisibility(request.getVisibility());
		dataSource.setCopyright(request.getCopyright());
		dataSource.setDisclaimer(request.getDisclaimer());
		dataSource.setRegistrationdate(Util.getNow());
		dataSource.setRequestername(request.getRequestername());
		dataSource.setRequestersurname(request.getRequestersurname());
		dataSource.setRequestermail(request.getRequestermail());
		dataSource.setPrivacyacceptance(Util.booleanToInt(request.getPrivacyacceptance()));
		dataSource.setIcon(request.getIcon());
		dataSource.setIsopendata(request.getOpendata() != null ? Util.booleanToInt(true) : Util.booleanToInt(false));
		dataSource.setExternalreference(request.getExternalreference());
		dataSource.setOpendataauthor(request.getOpendata() != null ? request.getOpendata().getOpendataauthor() : null);
		dataSource.setOpendataupdatedate(request.getOpendata() != null
				? Util.dateStringToTimestamp(request.getOpendata().getOpendataupdatedate()) : null);
		dataSource.setOpendatalanguage(
				request.getOpendata() != null ? request.getOpendata().getOpendatalanguage() : null);
		dataSource.setLastupdate(request.getOpendata() != null ? request.getOpendata().getLastupdate() : null);
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
	public static ServiceResponse buildResponse(Object object) {
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
	public static void checkVisibility(IVisibility request, TenantMapper tenantMapper)
			throws BadRequestException, NotFoundException {
		checkVisibility(request.getVisibility(), request.getLicense(), request.getOpendata(),
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
	public static Tenant checkTenant(Integer idTenant, String organizationCode, TenantMapper tenantMapper)
			throws NotFoundException, BadRequestException {

		ServiceUtil.checkMandatoryParameter(idTenant, "idTenant");

		Tenant tenant = tenantMapper.selectTenantByIdAndOrgCodeCode(idTenant, organizationCode);

		ServiceUtil.checkIfFoundRecord(tenant,
				"tenant not found idTenant [" + idTenant + "], organizationcode [" + organizationCode + "] ");

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
			List<SharingTenantRequest> sharingTenants, String copyright, TenantMapper tenantMapper)
			throws BadRequestException, NotFoundException {

		ServiceUtil.checkValue("visibility", visibility, Visibility.PRIVATE.code(), Visibility.PUBLIC.code());

		// PRIVATE
		if (Visibility.PRIVATE.code().equals(visibility)) {
			if (license != null) {
				throw new BadRequestException(Errors.INCORRECT_VALUE,
						"License only for public visibility, provided: " + visibility);
			}
			if (openData != null) {
				throw new BadRequestException(Errors.INCORRECT_VALUE,
						"Opendata only for public visibility, provided: " + visibility);
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
		if (Visibility.PUBLIC.code().equals(visibility)) {

			if (sharingTenants != null) {
				throw new BadRequestException(Errors.INCORRECT_VALUE,
						"Sharing Tenants permitted only for private visibility!");
			}
			if (copyright != null) {
				throw new BadRequestException(Errors.INCORRECT_VALUE,
						"Copyright permitted only for private visibility!");
			}
		}
	}

	/**
	 * 
	 * @param licenseRequest
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	public static void checkLicense(LicenseRequest licenseRequest) throws BadRequestException, NotFoundException {
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
	public static void checkAuthTenant(JwtUser authorizedUser, Integer idTenant, TenantMapper tenantMapper)
			throws UnauthorizedException, NotFoundException, BadRequestException {

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
	public static void checkAuthTenant(JwtUser authorizedUser, String tenantCode) throws UnauthorizedException {

		if (tenantCode == null)
			return;

		List<String> userAuthorizedTenantCodeList = ServiceUtil.getTenantCodeListFromUser(authorizedUser);

		for (String authTenant : userAuthorizedTenantCodeList) {
			if (authTenant.equals(tenantCode))
				return;
		}

		throw new UnauthorizedException(Errors.UNAUTHORIZED, "not authorized tenantCode [" + tenantCode + "]");
	}

	/**
	 * 
	 * @param authorizedUser
	 * @return
	 */
	public static List<String> getTenantCodeListFromUser(JwtUser authorizedUser) {

		if (authorizedUser == null || authorizedUser.getRoles() == null || authorizedUser.getRoles().isEmpty())
			return null;

		List<String> tenantCodeList = new ArrayList<>();

		for (String role : authorizedUser.getRoles()) {
			if (role.contains("_subscriber")) {
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
	public static String getDefaultInternalSocode(String organizationcode) {
		return "SOinternal" + organizationcode;
	}

	/**
	 * 
	 * @param TYPE
	 * @param smartobjectRequest
	 * @return
	 */
	public static boolean isType(Type TYPE, SmartobjectRequest smartobjectRequest) {
		return isType(TYPE, smartobjectRequest.getIdSoType());
	}

	/**
	 * 
	 * @param TYPE
	 * @param idSoType
	 * @return
	 */
	public static boolean isType(Type TYPE, Integer idSoType) {
		return TYPE.id() == idSoType;
	}

	/**
	 * 
	 * @param TYPE
	 * @param smartobject
	 * @return
	 */
	public static boolean isType(Type TYPE, Smartobject smartobject) {
		return TYPE.id() == smartobject.getIdSoType();
	}

	/**
	 * 
	 * @param count
	 * @throws NotFoundException
	 */
	public static void checkCount(int count) throws NotFoundException {
		if (count == 0) {
			throw new NotFoundException(Errors.RECORD_NOT_FOUND);
		}
	}

	/**
	 * 
	 * @param object
	 * @throws NotFoundException
	 */
	public static void checkIfFoundRecord(Object object) throws NotFoundException {
		checkIfFoundRecord(object, null);
	}

	/**
	 * 
	 * @param object
	 * @param arg
	 * @throws NotFoundException
	 */
	public static void checkIfFoundRecord(Object object, String arg) throws NotFoundException {
		if (object == null) {

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
	public static boolean isAlphaNumeric(String s) {
		return s.matches(ALPHANUMERIC_PATTERN);
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isAlphaNumericOrUnderscore(String s) {
		return s.matches(ALPHANUMERICOrUnderscore_PATTERN);
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean matchUUIDPattern(String s) {
		return s.matches(UUID_PATTERN);
	}

	// public static boolean matchComponentNamePattern(String s){
	// return s.matches(COMPONENT_NAME_PATTERN);
	// }

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean matchNotDevicePattern(String s) {
		return s.matches(NOT_DEVICE_PATTERN);
	}

	/**
	 * 
	 * @param s
	 * @param fieldName
	 * @throws BadRequestException
	 */
	// public static void checkComponentName(String s) throws
	// BadRequestException{
	// if (!matchComponentNamePattern(s)){
	// throw new BadRequestException(Errors.INCORRECT_VALUE, "received component
	// [ " + s + " ]");
	// }
	// }

	public static void checkAphanumeric(String s, String fieldName) throws BadRequestException {
		if (!isAlphaNumeric(s)) {
			throw new BadRequestException(Errors.ALPHANUMERIC_VALUE_REQUIRED,
					"received " + fieldName + " [ " + s + " ]");
		}

	}

	private static void checkAphanumericAndUnderscore(String s, String fieldName) throws BadRequestException {
		if (!isAlphaNumericOrUnderscore(s)) {
			throw new BadRequestException(Errors.ALPHANUMERIC_VALUE_REQUIRED,
					"received " + fieldName + " [ " + s + " ]");
		}
	}

	/**
	 * 
	 * @param codeTenantStatus
	 * @throws BadRequestException
	 */
	public static void checkCodeTenantStatus(String codeTenantStatus) throws BadRequestException {

		for (Status status : Status.values()) {
			if (status.code().equals(codeTenantStatus))
				return;
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
	public static void checkIdTenantType(Integer idTenantType) throws BadRequestException {

		for (TenantType type : TenantType.values()) {
			if (type.id() == idTenantType)
				return;
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
	public static void checkTenantTypeAndUserTypeAuth(String userTypeAuth, Integer idTenantType)
			throws BadRequestException {

		if ((TenantType.DEFAULT.id() == idTenantType || TenantType.PLUS.id() == idTenantType
				|| TenantType.ZERO.id() == idTenantType || TenantType.DEVELOP.id() == idTenantType)
				&& !UserTypeAuth.ADMIN.description().equals(userTypeAuth)) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, "tenant type " + tenantTypeDescription(idTenantType)
					+ " [ " + idTenantType + " ] permitted only for " + UserTypeAuth.ADMIN.description() + " user");
		}

		if (UserTypeAuth.SOCIAL.description().equals(userTypeAuth) && TenantType.TRIAL.id() != idTenantType) {
			throw new BadRequestException(Errors.INCORRECT_VALUE,
					"user type [ " + UserTypeAuth.SOCIAL.description() + " ] permitted only for "
							+ TenantType.TRIAL.description() + " [ " + TenantType.TRIAL.id() + " ] " + " idTenantType");
		}
	}

	/**
	 * 
	 * @param userTypeAuth
	 * @throws BadRequestException
	 */
	public static void checkUserTypeAuth(String userTypeAuth) throws BadRequestException {

		for (UserTypeAuth type : UserTypeAuth.values()) {
			if (type.description().equals(userTypeAuth))
				return;
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
	public static boolean containsWhitespace(String s) {

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
		if (containsWhitespace(s)) {
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

	public static void checkValue(String parameterName, String value, String... aspectedValues)
			throws BadRequestException {
		for (String aspectedValue : aspectedValues) {
			if (aspectedValue.equals(value))
				return;
		}

		StringBuilder sAspectedValues = new StringBuilder();
		String or = "";
		for (String aspectedValue : aspectedValues) {
			sAspectedValues.append(or).append(aspectedValue);
			or = " or ";
		}

		throw new BadRequestException(Errors.INCORRECT_VALUE,
				parameterName + " possible values are " + sAspectedValues);
	}

	public static void checkValue(String parameterName, Integer value, Integer... aspectedValues)
			throws BadRequestException {
		for (Integer aspectedValue : aspectedValues) {
			if (aspectedValue == value)
				return;
		}

		StringBuilder sAspectedValues = new StringBuilder();
		String or = "";
		for (Integer aspectedValue : aspectedValues) {
			sAspectedValues.append(or).append(aspectedValue);
			or = " or ";
		}

		throw new BadRequestException(Errors.INCORRECT_VALUE,
				parameterName + " possible values are " + sAspectedValues);
	}

	/**
	 * 
	 * @param list
	 * @throws NotFoundException
	 */
	public static void checkList(List<?> list, String arg) throws NotFoundException {
		if (list == null || list.isEmpty()) {
			if (arg != null)
				throw new NotFoundException(Errors.RECORD_NOT_FOUND, arg);
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
	public static String tenantTypeDescription(int id) {

		for (TenantType type : TenantType.values()) {
			if (type.id() == id) {
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
	public static String codeTenantStatus(int id) {

		for (Status status : Status.values()) {
			if (status.id() == id) {
				return status.code();
			}
		}
		return null;

	}

}
