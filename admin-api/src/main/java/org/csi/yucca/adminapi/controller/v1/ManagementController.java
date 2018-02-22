package org.csi.yucca.adminapi.controller.v1;

import static org.csi.yucca.adminapi.util.ApiDoc.M_ACTION_ON_STREAM;
import static org.csi.yucca.adminapi.util.ApiDoc.M_ACTION_ON_STREAM_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_CREATE_SMARTOBJECT;
import static org.csi.yucca.adminapi.util.ApiDoc.M_CREATE_SMARTOBJECT_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_CREATE_STREAM_DATASET;
import static org.csi.yucca.adminapi.util.ApiDoc.M_CREATE_STREAM_DATASET_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_CREATE_TENANT_SOCIAL;
import static org.csi.yucca.adminapi.util.ApiDoc.M_CREATE_TENANT_SOCIAL_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_DELETE_SMARTOBJECT;
import static org.csi.yucca.adminapi.util.ApiDoc.M_DELETE_SMARTOBJECT_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_IMPORT_METADATA_DATASET;
import static org.csi.yucca.adminapi.util.ApiDoc.M_IMPORT_METADATA_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_INSERT_CSV_DATA;
import static org.csi.yucca.adminapi.util.ApiDoc.M_INSERT_CSV_DATA_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_INSERT_DATASET;
import static org.csi.yucca.adminapi.util.ApiDoc.M_INSERT_DATASET_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_DATASET;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_DATASET_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_DATA_SETS;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_DATA_SETS_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_SMART_OBJECT;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_SMART_OBJECTS;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_SMART_OBJECTS_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_SMART_OBJECT_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_STREAM;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_STREAMS;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_STREAMS_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_STREAM_ICON;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_STREAM_ICON_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_STREAM_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_TENANT;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_TENANT_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_UPDATE_DATASET;
import static org.csi.yucca.adminapi.util.ApiDoc.M_UPDATE_DATASET_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_UPDATE_SMARTOBJECT;
import static org.csi.yucca.adminapi.util.ApiDoc.M_UPDATE_SMARTOBJECT_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_UPDATE_STREAM;
import static org.csi.yucca.adminapi.util.ApiDoc.M_UPDATE_STREAM_NOTES;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.controller.YuccaController;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.request.ActionRequest;
import org.csi.yucca.adminapi.request.DatasetRequest;
import org.csi.yucca.adminapi.request.ImportMetadataDatasetRequest;
import org.csi.yucca.adminapi.request.PostStreamRequest;
import org.csi.yucca.adminapi.request.PostTenantSocialRequest;
import org.csi.yucca.adminapi.request.PostValidateSiddhiQueriesRequest;
import org.csi.yucca.adminapi.request.SmartobjectRequest;
import org.csi.yucca.adminapi.request.StreamRequest;
import org.csi.yucca.adminapi.response.DataTypeResponse;
import org.csi.yucca.adminapi.response.DatasetResponse;
import org.csi.yucca.adminapi.response.DettaglioSmartobjectResponse;
import org.csi.yucca.adminapi.response.DettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.DomainResponse;
import org.csi.yucca.adminapi.response.ListStreamResponse;
import org.csi.yucca.adminapi.response.PostStreamResponse;
import org.csi.yucca.adminapi.response.Response;
import org.csi.yucca.adminapi.response.SmartobjectResponse;
import org.csi.yucca.adminapi.response.TenantResponse;
import org.csi.yucca.adminapi.service.DatasetService;
import org.csi.yucca.adminapi.service.SmartObjectService;
import org.csi.yucca.adminapi.service.StreamService;
import org.csi.yucca.adminapi.service.TenantService;
import org.csi.yucca.adminapi.util.ApiCallable;
import org.csi.yucca.adminapi.util.ApiUserType;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("1/management")
public class ManagementController extends YuccaController {

	private static final Logger logger = Logger.getLogger(ManagementController.class);

	@Autowired
	private SmartObjectService smartObjectService;

	@Autowired
	private TenantService tenantService;

	@Autowired
	private StreamService streamService;

	@Autowired
	private DatasetService datasetService;
	
	@ApiOperation(value = M_INSERT_CSV_DATA, notes = M_INSERT_CSV_DATA_NOTES, response = Response.class)
	@PostMapping("/validate/internalStream/query")
	public ResponseEntity<Object> validateSiddhiQueries(
			@RequestBody final PostValidateSiddhiQueriesRequest postValidateSiddhiQueriesRequest,			
			final HttpServletRequest request) {

		logger.info("validateSiddhiQueries");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return streamService.validateSiddhiQueries(postValidateSiddhiQueriesRequest);
			}
		}, logger);

	}
	
	
	/**
	 * 
	 * @param action
	 * @param organizationCode
	 * @param soCode
	 * @param idStream
	 * @param httpRequest
	 * @return
	 */
	@ApiOperation(value = M_ACTION_ON_STREAM, notes = M_ACTION_ON_STREAM_NOTES, response = ServiceResponse.class)
	@PutMapping("/organizations/{organizationCode}/smartobjects/{soCode}/streams/{idStream}/action")
	public ResponseEntity<Object> actionOnStream(
			@RequestBody final ActionRequest actionRequest,	
			@PathVariable final String organizationCode,
			@PathVariable final String soCode,
			@PathVariable final Integer idStream,
			final HttpServletRequest httpRequest) {
		
		logger.info("actionOnStream");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return streamService.actionOnStream(actionRequest, organizationCode, soCode, idStream, ApiUserType.MANAGEMENT, getAuthorizedUser(httpRequest));
			}
		}, logger);
	}


	/**
	 * 
	 * @param file
	 * @param filename
	 * @param skipFirstRow
	 * @param encoding
	 * @param csvSeparator
	 * @param dateFormat
	 * @param organizationCode
	 * @param idDataset
	 * @param request
	 * @return
	 */
	@ApiOperation(value = M_INSERT_CSV_DATA, notes = M_INSERT_CSV_DATA_NOTES, response = Response.class)
	@PostMapping("/organizations/{organizationCode}/datasets/{idDataset}/addData")
	public ResponseEntity<Object> addCSVData(
			@RequestParam("file") final MultipartFile file, 
			@RequestParam("skipFirstRow") final Boolean skipFirstRow,
			@RequestParam("encoding") final String encoding, 
			@RequestParam("csvSeparator") final String csvSeparator, 
			@PathVariable final String organizationCode,
			@PathVariable final Integer idDataset, 
			@RequestParam final String componentInfoRequests, 
			final HttpServletRequest request,
			@RequestParam(required = true) final String tenantCodeManager) {

		logger.info("addCSVData");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return datasetService.insertCSVData(file, skipFirstRow, encoding, csvSeparator, componentInfoRequests, organizationCode, idDataset, tenantCodeManager, getAuthorizedUser(request));
			}
		}, logger);

	}
	
	
	/**
	 * 
	 * @param organizationCode
	 * @param idDataset
	 * @param request
	 * @return
	 */
	@ApiOperation(value = M_UPDATE_DATASET, notes = M_UPDATE_DATASET_NOTES, response = SmartobjectResponse.class)
	@PutMapping("/organizations/{organizationCode}/datasets/{idDataset}")
	public ResponseEntity<Object> updateDataset(@PathVariable final String organizationCode, @PathVariable final Integer idDataset,
			@RequestBody final DatasetRequest datasetRequest, @RequestParam(required = false) final String tenantCodeManager, final HttpServletRequest request) {

		logger.info("updateDataset");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return datasetService.updateDataset(organizationCode, idDataset, datasetRequest, tenantCodeManager, getAuthorizedUser(request));
			}
		}, logger);
	}

	/**
	 * @param organizationCode
	 * @param file
	 * @param dataset
	 * @param formatType
	 * @param csvSeparator
	 * @param encoding
	 * @param skipFirstRow
	 * @return
	 */
	@ApiOperation(value = M_INSERT_DATASET, notes = M_INSERT_DATASET_NOTES, response = Response.class)
	@PostMapping("/organizations/{organizationCode}/datasets")
	public ResponseEntity<Object> addDataSet(@PathVariable final String organizationCode, @RequestBody final DatasetRequest postDatasetRequest, final HttpServletRequest request) {
		logger.info("addDataSet");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return datasetService.insertDataset(organizationCode, postDatasetRequest, getAuthorizedUser(request));
			}
		}, logger);
	}

	/**
	 * 
	 * http://redmine.sdp.csi.it/projects/yucca-smart-data-platform/wiki/
	 * ImportMetadata_Dataset
	 * 
	 * @param organizationCode
	 * @param file
	 * @param dataset
	 * @param formatType
	 * @param csvSeparator
	 * @param encoding
	 * @param skipFirstRow
	 * @return
	 */
	@ApiOperation(value = M_IMPORT_METADATA_DATASET, notes = M_IMPORT_METADATA_NOTES, response = Response.class)
	@PostMapping("/organizations/{organizationCode}/datasets/importMetadata")
	public ResponseEntity<Object> importMetadata(@PathVariable final String organizationCode, @RequestBody final ImportMetadataDatasetRequest importMetadataRequest,
			final HttpServletRequest request) {
		logger.info("importMetadata");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return datasetService.importMetadata(organizationCode, importMetadataRequest, getAuthorizedUser(request));
			}
		}, logger);
	}

	/**
	 * 
	 * @param organizationCode
	 * @param idDataset
	 * @param tenantCodeManager
	 * @param request
	 * @return
	 */
	@ApiOperation(value = M_LOAD_DATASET, notes = M_LOAD_DATASET_NOTES, response = DettaglioStreamDatasetResponse.class)
	@GetMapping("/organizations/{organizationCode}/datasets/{idDataset}")
	public ResponseEntity<Object> loadDataset(@PathVariable final String organizationCode, @PathVariable final Integer idDataset,
			@RequestParam(required = false) final String tenantCodeManager, final HttpServletRequest request) {

		logger.info("loadDataset");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return datasetService.selectDataset(organizationCode, idDataset, tenantCodeManager, getAuthorizedUser(request));
			}
		}, logger);
	}

	/**
	 * 
	 * @param organizationCode
	 * @param tenantCodeManager
	 * @param sort
	 * @param request
	 * @return
	 */
	@ApiOperation(value = M_LOAD_DATA_SETS, notes = M_LOAD_DATA_SETS_NOTES, response = DatasetResponse.class, responseContainer = "List")
	@GetMapping("/organizations/{organizationCode}/datasets")
	public ResponseEntity<Object> loadDataSets(@PathVariable final String organizationCode, @RequestParam(required = false) final String tenantCodeManager,
			@RequestParam(required = false) final String sort, final HttpServletRequest request) {

		logger.info("loadDataSets");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return datasetService.selectDatasets(organizationCode, tenantCodeManager, sort, getAuthorizedUser(request));
			}
		}, logger);

	}

	/**
	 * 
	 * @param tenantCodeManager
	 * @param streamRequest
	 * @param organizationCode
	 * @param soCode
	 * @param idStream
	 * @param request
	 * @return
	 */
	@ApiOperation(value = M_UPDATE_STREAM, notes = M_UPDATE_STREAM_NOTES, response = Response.class)
	@PutMapping("/organizations/{organizationCode}/smartobjects/{soCode}/streams/{idStream}")
	public ResponseEntity<Object> updateStream(@RequestParam(required = false) final String tenantCodeManager, @RequestBody final StreamRequest streamRequest,
			@PathVariable final String organizationCode, @PathVariable final String soCode, @PathVariable final Integer idStream, final HttpServletRequest request) {
		logger.info("updateDraftStream");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return streamService.updateStream(organizationCode, soCode, idStream, streamRequest, tenantCodeManager, getAuthorizedUser(request));
			}
		}, logger);
	}

	/**
	 * 
	 * @param organizationCode
	 * @param idstream
	 * @param tenantCodeManager
	 * @param request
	 * @return
	 */
	@ApiOperation(value = M_LOAD_STREAM, notes = M_LOAD_STREAM_NOTES, response = DettaglioStreamDatasetResponse.class)
	@GetMapping("/organizations/{organizationCode}/streams/{idstream}")
	public ResponseEntity<Object> loadStream(@PathVariable final String organizationCode, @PathVariable final Integer idstream,
			@RequestParam(required = false) final String tenantCodeManager, final HttpServletRequest request) {

		logger.info(">>>>>> loadStream <<<<<<<< ");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return streamService.selectStream(organizationCode, idstream, tenantCodeManager, getAuthorizedUser(request));
			}
		}, logger);
	}

	/**
	 * 
	 * @param organizationCode
	 * @param idstream
	 * @param request
	 * @param response
	 */
	@ApiOperation(value = M_LOAD_STREAM_ICON, notes = M_LOAD_STREAM_ICON_NOTES, response = Byte[].class)
	@GetMapping("/organizations/{organizationCode}/streams/{idstream}/icon")
	public void loadStreamIcon(@PathVariable final String organizationCode, @PathVariable final Integer idstream, final HttpServletRequest request,
			final HttpServletResponse response) {

		logger.info("loadStreamIcon");

		byte[] imgByte = null;
		try {
			imgByte = streamService.selectStreamIcon(organizationCode, idstream, getAuthorizedUser(request));
			if (imgByte != null) {
				response.setHeader("Pragma", "no-cache");
				response.setDateHeader("Expires", 0);
				response.setContentType("image/png");
				ServletOutputStream responseOutputStream = response.getOutputStream();
				responseOutputStream.write(imgByte);
				responseOutputStream.flush();
				responseOutputStream.close();
			} else 
				response.sendRedirect(Util.defaultIconPath(request, "stream"));
		} catch (Exception e) {
			logger.info("loadStreamIcon ERROR: " + e.getMessage());
			e.printStackTrace();
			imgByte = null;
		}
	}
	
	/**
	 * 
	 * @param organizationCode
	 * @param idstream
	 * @param request
	 * @param response
	 */
	@ApiOperation(value = M_LOAD_STREAM_ICON, notes = M_LOAD_STREAM_ICON_NOTES, response = Byte[].class)
	@GetMapping("/organizations/{organizationCode}/datasets/{iddataset}/icon")
	public void loadDatasetIcon(@PathVariable final String organizationCode, @PathVariable final Integer iddataset, final HttpServletRequest request,
			final HttpServletResponse response) {

		logger.info("loadDatasetIcon");

		byte[] imgByte = null;
		try {
			imgByte = datasetService.selectDatasetIcon(organizationCode, iddataset, getAuthorizedUser(request));
			if (imgByte != null) {
				response.setHeader("Pragma", "no-cache");
				response.setDateHeader("Expires", 0);
				response.setContentType("image/png");
				ServletOutputStream responseOutputStream = response.getOutputStream();
				responseOutputStream.write(imgByte);
				responseOutputStream.flush();
				responseOutputStream.close();
			} else 
				response.sendRedirect(Util.defaultIconPath(request, "dataset"));
		} catch (Exception e) {
			logger.info("loadDatasetIcon ERROR: " + e.getMessage());
			e.printStackTrace();
			imgByte = null;
		}
	}
	
	
	
	/**
	 * 
	 * @param organizationCode
	 * @param tenantCodeManager
	 * @param sort
	 * @param request
	 * @return
	 */
	@ApiOperation(value = M_LOAD_STREAMS, notes = M_LOAD_STREAMS_NOTES, response = ListStreamResponse.class, responseContainer = "List")
	@GetMapping("/organizations/{organizationCode}/streams")
	public ResponseEntity<Object> loadStreams(@PathVariable final String organizationCode, @RequestParam(required = false) final String tenantCodeManager,
			@RequestParam(required = false) final String sort, final HttpServletRequest request) {
		logger.info("loadStreams");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return streamService.selectStreams(organizationCode, tenantCodeManager, sort, getAuthorizedUser(request));
			}
		}, logger);
	}

	/**
	 * 
	 * @param request
	 * @param organizationCode
	 * @param soCode
	 * @param httpRequest
	 * @return
	 */
	@ApiOperation(value = M_CREATE_STREAM_DATASET, notes = M_CREATE_STREAM_DATASET_NOTES, response = PostStreamResponse.class)
	@PostMapping("/organizations/{organizationCode}/smartobjects/{soCode}/streams")
	public ResponseEntity<Object> createStreamDataset(@RequestBody final PostStreamRequest request, @PathVariable final String organizationCode, @PathVariable final String soCode,
			final HttpServletRequest httpRequest) {

		logger.info("createStreamDataset");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return streamService.createStreamDataset(request, organizationCode, soCode, getAuthorizedUser(httpRequest));
			}
		}, logger);
	}

	/**
	 * 
	 * @param installationTenantRequest
	 * @return
	 */
	@ApiOperation(value = M_CREATE_TENANT_SOCIAL, notes = M_CREATE_TENANT_SOCIAL_NOTES, response = TenantResponse.class)
	@PostMapping("/tenants")
	public ResponseEntity<Object> createTenantSocial(@RequestBody final PostTenantSocialRequest installationTenantRequest) {
		logger.info("createTenantSocial");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return tenantService.insertTenantSocial(installationTenantRequest);
			}
		}, logger);
	}

	/**
	 * 
	 * @param organizationCode
	 * @param socode
	 * @param request
	 * @return
	 */
	@ApiOperation(value = M_LOAD_SMART_OBJECT, notes = M_LOAD_SMART_OBJECT_NOTES, response = DettaglioSmartobjectResponse.class)
	@GetMapping("/organizations/{organizationCode}/smartobjects/{socode}")
	public ResponseEntity<Object> loadSmartobject(@PathVariable final String organizationCode, @PathVariable final String socode, final HttpServletRequest request) {
		logger.info("loadSmartobject");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.selectSmartObject(organizationCode, socode, getAuthorizedUser(request));
			}
		}, logger);
	}

	/**
	 * 
	 * @param organizationCode
	 * @param tenantCode
	 * @param request
	 * @return
	 */
	@ApiOperation(value = M_LOAD_SMART_OBJECTS, notes = M_LOAD_SMART_OBJECTS_NOTES, response = DettaglioSmartobjectResponse.class, responseContainer = "List")
	@GetMapping("/organizations/{organizationCode}/smartobjects")
	public ResponseEntity<Object> loadSmartobjects(@PathVariable final String organizationCode, @RequestParam(required = false) final String tenantCode,
			final HttpServletRequest request) {
		logger.info("loadSmartobjects");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.selectSmartObjects(organizationCode, tenantCode, getAuthorizedUser(request));
			}
		}, logger);
	}

	/**
	 * LOAD TENANT
	 * 
	 * @param skip
	 * @param limit
	 * @param fields
	 * @param sort
	 * @param embed
	 * @return
	 */
	@ApiOperation(value = M_LOAD_TENANT, notes = M_LOAD_TENANT_NOTES, response = DomainResponse.class, responseContainer = "List")
	@GetMapping("/tenants")
	public ResponseEntity<Object> loadTenants(@RequestParam(required = false) final Integer skip, @RequestParam(required = false) final Integer limit,
			@RequestParam(required = false) final String fields, @RequestParam(required = false) final String sort, @RequestParam(required = false) final String embed) {
		logger.info("loadTenants");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return tenantService.selectTenants(sort);
			}
		}, logger);
	}

	/**
	 * @param smartobjectRequest
	 * @param organizationCode
	 * @param soCode
	 * @return
	 */
	@ApiOperation(value = M_UPDATE_SMARTOBJECT, notes = M_UPDATE_SMARTOBJECT_NOTES, response = SmartobjectResponse.class)
	@PutMapping("/organizations/{organizationCode}/smartobjects/{soCode}")
	public ResponseEntity<Object> updateSmartobject(@RequestBody final SmartobjectRequest smartobjectRequest, @PathVariable final String organizationCode,
			@PathVariable final String soCode) {
		logger.info("updateSmartobject");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.updateSmartobject(smartobjectRequest, organizationCode, soCode);
			}
		}, logger);
	}

	/**
	 * 
	 * @param organizationCode
	 * @param soCode
	 * @return
	 */
	@ApiOperation(value = M_DELETE_SMARTOBJECT, notes = M_DELETE_SMARTOBJECT_NOTES, response = ServiceResponse.class)
	@DeleteMapping("/organizations/{organizationCode}/smartobjects/{soCode}")
	public ResponseEntity<Object> deleteSmartobject(@PathVariable final String organizationCode, @PathVariable final String soCode) {
		logger.info("deleteSmartobject");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.deleteSmartObject(organizationCode, soCode);
			}
		}, logger);
	}

	/**
	 * @param smartobjectRequest
	 * @param organizationCode
	 * @return
	 */
	@ApiOperation(value = M_CREATE_SMARTOBJECT, notes = M_CREATE_SMARTOBJECT_NOTES, response = DataTypeResponse.class)
	@PostMapping("/organizations/{organizationCode}/smartobjects")
	public ResponseEntity<Object> createSmartobject(@RequestBody final SmartobjectRequest smartobjectRequest, @PathVariable final String organizationCode) {
		logger.info("createSmartobject");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.insertSmartobject(smartobjectRequest, organizationCode);
			}
		}, logger);
	}
}
