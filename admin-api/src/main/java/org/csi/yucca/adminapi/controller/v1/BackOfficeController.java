package org.csi.yucca.adminapi.controller.v1;

import static org.csi.yucca.adminapi.util.ApiDoc.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.controller.YuccaController;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.request.ActionRequest;
import org.csi.yucca.adminapi.request.ActionfeedbackOnTenantRequest;
import org.csi.yucca.adminapi.request.DataTypeRequest;
import org.csi.yucca.adminapi.request.DomainRequest;
import org.csi.yucca.adminapi.request.EcosystemRequest;
import org.csi.yucca.adminapi.request.LicenseRequest;
import org.csi.yucca.adminapi.request.MeasureUnitRequest;
import org.csi.yucca.adminapi.request.OrganizationRequest;
import org.csi.yucca.adminapi.request.PhenomenonRequest;
import org.csi.yucca.adminapi.request.PostTenantRequest;
import org.csi.yucca.adminapi.request.SubdomainRequest;
import org.csi.yucca.adminapi.request.TagRequest;
import org.csi.yucca.adminapi.response.BackOfficeOrganizationResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.adminapi.response.DataTypeResponse;
import org.csi.yucca.adminapi.response.DomainResponse;
import org.csi.yucca.adminapi.response.EcosystemResponse;
import org.csi.yucca.adminapi.response.LicenseResponse;
import org.csi.yucca.adminapi.response.ListStreamResponse;
import org.csi.yucca.adminapi.response.MeasureUnitResponse;
import org.csi.yucca.adminapi.response.PhenomenonResponse;
import org.csi.yucca.adminapi.response.SubdomainResponse;
import org.csi.yucca.adminapi.response.TagResponse;
import org.csi.yucca.adminapi.service.ApiService;
import org.csi.yucca.adminapi.service.ClassificationService;
import org.csi.yucca.adminapi.service.ComponentService;
import org.csi.yucca.adminapi.service.DatasetService;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "backoffice", description = "Endpoint for backoffice")
@RestController
@RequestMapping("1/backoffice")
public class BackOfficeController extends YuccaController {

	private static final Logger logger = Logger.getLogger(BackOfficeController.class);

	@Autowired
	private ClassificationService classificationService;

	@Autowired
	private ComponentService componentService;

	@Autowired
	private TenantService tenantService;
	
	@Autowired
	private StreamService streamService;

	@Autowired
	private DatasetService datasetService;

	@Autowired
	private ApiService apiService;
	
	
	@ApiOperation(value = BO_LOAD_TENANT, notes = BO_LOAD_TENANT_NOTES, response = DomainResponse.class, responseContainer = "List")
	@GetMapping("/mail/{tenantcode}")
	public ResponseEntity<Object> loadMail(@PathVariable final String tenantcode) {
		logger.info("loadTenant");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return tenantService.selectMail(tenantcode);
			}
		}, logger);
	}

	
	
	/**
	 * 
	 * @param actionRequest
	 * @param idStream
	 * @return
	 */
	@ApiOperation(value = BO_ACTION_ON_STREAM, notes = BO_ACTION_ON_STREAM_NOTES, response = ServiceResponse.class)
	@PutMapping("/streams/{idStream}/actionfeedback")
	public ResponseEntity<Object> actionFeedback(
			@RequestBody final ActionRequest actionRequest, 
			@PathVariable final Integer idStream) {
		logger.info("actionFeedback");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return streamService.actionFeedback(actionRequest, idStream);
			}
		}, logger);
	}
	
	
	/**
	 * 
	 * @param actionRequest
	 * @param idStream
	 * @return
	 */
	@ApiOperation(value = BO_ACTION_ON_STREAM, notes = BO_ACTION_ON_STREAM_NOTES, response = ServiceResponse.class)
	@PutMapping("/streams/{idStream}/action")
	public ResponseEntity<Object> actionOnStream(@RequestBody final ActionRequest actionRequest, 
			@PathVariable final Integer idStream) {
		logger.info("actionOnStream");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return streamService.actionOnStream(actionRequest, idStream, ApiUserType.BACK_OFFICE);
			}
		}, logger);
	}
	
	/**
	 * 
	 * @param actionOnTenantRequest
	 * @return
	 */
	@ApiOperation(value = BO_ACTION_ON_TENANT, notes = BO_ACTION_ON_TENANT_NOTES, response = ServiceResponse.class)
	@PutMapping("/tenants/{tenantcode}/action")
	public ResponseEntity<Object> actionOnTenant(@RequestBody final ActionRequest actionOnTenantRequest, @PathVariable final String tenantcode) {
		logger.info("actionOnTenant");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return tenantService.actionOnTenant(actionOnTenantRequest, tenantcode);
			}
		}, logger);
	}

	/**
	 * 
	 * @param tenantcode
	 * @param username
	 * @param password
	 * @return
	 */
	@ApiOperation(value = BO_ADD_ADMIN_APPLICATION_TENANT, notes = BO_ADD_ADMIN_APPLICATION_TENANT_NOTES, response = ServiceResponse.class)
	@PostMapping("/tenant/addAdminApplication")
	public ResponseEntity<Object> addAdminApplication(@RequestParam(required = true) final String tenantCode, @RequestParam(required = true) final String username,
			@RequestParam(required = true) final String password) {
		logger.info("addAdminApplication");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return tenantService.addAdminApplication(tenantCode, username, password);
			}
		}, logger);
	}

	/**
	 * 
	 * @param tenantcode
	 * @param username
	 * @param password
	 * @return
	 */
	@ApiOperation(value = BO_SUBSCRIBE_ADMIN_API_IN_STORE_TENANT, notes = BO_SUBSCRIBE_ADMIN_API_IN_STORE_TENANT_NOTES, response = ServiceResponse.class)
	@PostMapping("/tenant/subscribeAdminApi")
	public ResponseEntity<Object> subscribeAdminApiInStore(@RequestParam(required = true) final String tenantCode, @RequestParam(required = true) final String username,
			@RequestParam(required = true) final String password) {
		logger.info("subscribeAdminApiInStore");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return tenantService.subscribeAdminApiInStore(tenantCode, username, password);
			}
		}, logger);
	}

	/**
	 * 
	 * @param tenantcode
	 * @param username
	 * @param password
	 * @return
	 */
	@ApiOperation(value = BO_GENERETATE_ADMIN_KEY_TENANT, notes = BO_GENERETATE_ADMIN_KEY_TENANT_NOTES, response = ServiceResponse.class)
	@PostMapping("/tenant/generetateAdminKey")
	public ResponseEntity<Object> generetateAdminKey(@RequestParam(required = true) final String tenantCode, @RequestParam(required = true) final String username,
			@RequestParam(required = true) final String password) {
		logger.info("addAdminApplication");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return tenantService.generetateAdminKey(tenantCode, username, password);
			}
		}, logger);
	}

	/**
	 * 
	 * @param actionFeedbackOnTenantRequest
	 * @return
	 */
	@ApiOperation(value = BO_ACTION_ON_TENANT, notes = BO_ACTION_ON_TENANT_NOTES, response = ServiceResponse.class)
	@PutMapping("/tenants/{tenantcode}/actionfeedback")
	public ResponseEntity<Object> actionfeedbackOnTenant(@RequestBody final ActionfeedbackOnTenantRequest actionfeedbackOnTenantRequest, @PathVariable final String tenantcode) {
		logger.info("actionfeedbackOnTenant");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return tenantService.actionfeedbackOnTenant(actionfeedbackOnTenantRequest, tenantcode);
			}
		}, logger);
	}

	/**
	 * @param tenantRequest
	 * @return
	 */
	@ApiOperation(value = BO_CREATE_TENANT, notes = BO_CREATE_TENANT_NOTES, response = ServiceResponse.class)
	@PostMapping("/tenants")
	public ResponseEntity<Object> createTenant(@RequestBody final PostTenantRequest tenantRequest) {
		logger.info("createTenant");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return tenantService.insertTenant(tenantRequest);
			}
		}, logger);
	}

	/**
	 * 
	 * LOAD TENANT BY TENANTCODE
	 * 
	 * @param tenantcode
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_TENANT, notes = BO_LOAD_TENANT_NOTES, response = DomainResponse.class, responseContainer = "List")
	@GetMapping("/tenants/{tenantcode}")
	public ResponseEntity<Object> loadTenant(@PathVariable final String tenantcode) {
		logger.info("loadTenant");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return tenantService.selectTenant(tenantcode);
			}
		}, logger);
	}

	/**
	 * LOAD ALL TENANTS
	 * 
	 * @param skip
	 * @param limit
	 * @param fields
	 * @param sort
	 * @param embed
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_TENANTS, notes = BO_LOAD_TENANTS_NOTES, response = DomainResponse.class, responseContainer = "List")
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

	@ApiOperation(value = BO_DELETE_TENANT, notes = BO_DELETE_TENANT_NOTES, response = ServiceResponse.class)
	@DeleteMapping("/tenants/{tenantcode}")
	public ResponseEntity<Object> deleteTenant(@PathVariable final String tenantcode) {
		logger.info("deleteTenant");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return tenantService.deleteTenant(tenantcode);
			}
		}, logger);
	}

	/**
	 * @param dataTypeRequest
	 * @return
	 */
	@ApiOperation(value = BO_CREATE_DATA_TYPE, notes = BO_CREATE_DATA_TYPE_NOTES, response = DataTypeResponse.class)
	@PostMapping("/data_types")
	public ResponseEntity<Object> createDataType(@RequestBody final DataTypeRequest dataTypeRequest) {
		logger.info("createDataType");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return componentService.insertDataType(dataTypeRequest);
			}
		}, logger);
	}

	/**
	 * UPDATE DATA TYPE
	 * 
	 * @param dataTypeRequest
	 * @param idDataType
	 * @return
	 */
	@ApiOperation(value = BO_UPDATE_DATA_TYPE, notes = BO_UPDATE_DATA_TYPE_NOTES, response = DataTypeResponse.class)
	@PutMapping("/data_types/{idDataType}")
	public ResponseEntity<Object> updateDataType(@RequestBody final DataTypeRequest dataTypeRequest, @PathVariable final Integer idDataType) {
		logger.info("updateDataType");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return componentService.updateDataType(dataTypeRequest, idDataType);
			}
		}, logger);
	}

	/**
	 * 
	 * DELETE FDATA TYPE
	 * 
	 * @param idDataType
	 * @return
	 */
	@ApiOperation(value = BO_DELETE_DATA_TYPE, notes = BO_DELETE_DATA_TYPE_NOTES, response = ServiceResponse.class)
	@DeleteMapping("/data_types/{idDataType}")
	public ResponseEntity<Object> deleteDataType(@PathVariable final Integer idDataType) {
		logger.info("deleteDataType");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return componentService.deleteDataType(idDataType);
			}
		}, logger);
	}

	/**
	 * 
	 * LOAD DATA TYPE
	 * 
	 * @param idDataType
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_DATA_TYPE, notes = BO_LOAD_DATA_TYPE_NOTES, response = DataTypeResponse.class)
	@GetMapping("/data_types/{idDataType}")
	public ResponseEntity<Object> loadDataType(@PathVariable final Integer idDataType) {
		logger.info("loadDataType");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return componentService.selectDataType(idDataType);
			}
		}, logger);
	}

	/**
	 * 
	 * LOAD MEASURE UNIT
	 * 
	 * @param idMeasureUnit
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_MEASURE_UNIT, notes = BO_LOAD_MEASURE_UNIT_NOTES, response = MeasureUnitResponse.class)
	@GetMapping("/measure_units/{idMeasureUnit}")
	public ResponseEntity<Object> loadMeasureUnit(@PathVariable final Integer idMeasureUnit) {
		logger.info("loadMeasureUnit");
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return componentService.selectMeasureUnit(idMeasureUnit);
			}
		}, logger);
	}

	/**
	 * 
	 * LOAD PHENOMENON
	 * 
	 * @param idPhenomenon
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_PHENOMENON, notes = BO_LOAD_PHENOMENON_NOTES, response = PhenomenonResponse.class)
	@GetMapping("/phenomenons/{idPhenomenon}")
	public ResponseEntity<Object> loadPhenomenon(@PathVariable final Integer idPhenomenon) {
		logger.info("loadPhenomenon");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return componentService.selectPhenomenon(idPhenomenon);
			}
		}, logger);
	}

	/**
	 * 
	 * DELETE PHENOMENON
	 * 
	 * @param idPhenomenon
	 * @return
	 */
	@ApiOperation(value = BO_DELETE_PHENOMENON, notes = BO_DELETE_PHENOMENON_NOTES, response = ServiceResponse.class)
	@DeleteMapping("/phenomenons/{idPhenomenon}")
	public ResponseEntity<Object> deletePhenomenon(@PathVariable final Integer idPhenomenon) {
		logger.info("deletePhenomenon");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return componentService.deletePhenomenon(idPhenomenon);
			}
		}, logger);
	}

	/**
	 * 
	 * UPDATE PHENOMENON
	 * 
	 * @param phenomenonRequest
	 * @param idPhenomenon
	 * @return
	 */
	@ApiOperation(value = BO_UPDATE_PHENOMENON, notes = BO_UPDATE_PHENOMENON_NOTES, response = PhenomenonResponse.class)
	@PutMapping("/phenomenons/{idPhenomenon}")
	public ResponseEntity<Object> updatePhenomenon(@RequestBody final PhenomenonRequest phenomenonRequest, @PathVariable final Integer idPhenomenon) {
		logger.info("updatePhenomenon");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return componentService.updatePhenomenon(phenomenonRequest, idPhenomenon);
			}
		}, logger);
	}

	/**
	 * 
	 * CREATE SEQUENCE int_yucca.phenomenon_id_phenomenon_seq; ALTER TABLE
	 * int_yucca.yucca_d_phenomenon ALTER COLUMN id_phenomenon SET DEFAULT
	 * nextval('int_yucca.phenomenon_id_phenomenon_seq'); ALTER TABLE
	 * int_yucca.yucca_d_phenomenon ALTER COLUMN id_phenomenon SET NOT NULL;
	 * ALTER SEQUENCE int_yucca.phenomenon_id_phenomenon_seq OWNED BY
	 * int_yucca.yucca_d_phenomenon.id_phenomenon; -- 8.2 or later
	 * 
	 * ALTER SEQUENCE int_yucca.phenomenon_id_phenomenon_seq RESTART WITH 49;
	 * 
	 * 
	 * INSERT PHENOMENON
	 * 
	 * @param phenomenonRequest
	 * @return
	 */
	@ApiOperation(value = BO_CREATE_PHENOMENON, notes = BO_CREATE_PHENOMENON_NOTES, response = PhenomenonResponse.class)
	@PostMapping("/phenomenons")
	public ResponseEntity<Object> createPhenomenon(@RequestBody final PhenomenonRequest phenomenonRequest) {
		logger.info("createPhenomenon");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return componentService.insertPhenomenon(phenomenonRequest);
			}
		}, logger);
	}

	/**
	 * 
	 * DELETE MEASURE UNIT
	 * 
	 * @param idMeasureUnit
	 * @return
	 */
	@ApiOperation(value = BO_DELETE_MEASURE_UNIT, notes = BO_DELETE_MEASURE_UNIT_NOTES, response = ServiceResponse.class)
	@DeleteMapping("/measure_units/{idMeasureUnit}")
	public ResponseEntity<Object> deleteMeasureUnit(@PathVariable final Integer idMeasureUnit) {
		logger.info("deleteMeasureUnit");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return componentService.deleteMeasureUnit(idMeasureUnit);
			}
		}, logger);
	}

	/**
	 * 
	 * UPDATE MEASURE UNIT
	 * 
	 * @param measureUnitRequest
	 * @param idMeasureUnit
	 * @return
	 */
	@ApiOperation(value = BO_UPDATE_MEASURE_UNIT, notes = BO_UPDATE_MEASURE_UNIT_NOTES, response = MeasureUnitResponse.class)
	@PutMapping("/measure_units/{idMeasureUnit}")
	public ResponseEntity<Object> updateMeasureUnit(@RequestBody final MeasureUnitRequest measureUnitRequest, @PathVariable final Integer idMeasureUnit) {
		logger.info("updateMeasureUnit");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return componentService.updateMeasureUnit(measureUnitRequest, idMeasureUnit);
			}
		}, logger);
	}

	/**
	 * @param measureUnitRequest
	 * @return
	 */
	@ApiOperation(value = BO_CREATE_MEASURE_UNIT, notes = BO_CREATE_MEASURE_UNIT_NOTES, response = MeasureUnitResponse.class)
	@PostMapping("/measure_units")
	public ResponseEntity<Object> createMeasureUnit(@RequestBody final MeasureUnitRequest measureUnitRequest) {
		logger.info("createMeasureUnit");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return componentService.insertMeasureUnit(measureUnitRequest);
			}
		}, logger);
	}

	/**
	 * LOAD SUBDOMAIN
	 * 
	 * @param idSubdomain
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_SUBDOMAIN, notes = BO_LOAD_SUBDOMAIN_NOTES, response = SubdomainResponse.class)
	@GetMapping("/subdomains/{idSubdomain}")
	public ResponseEntity<Object> loadSubdomain(@PathVariable final Integer idSubdomain) {
		logger.info("loadSubdomain");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectSubdomain(idSubdomain);
			}
		}, logger);
	}

	/**
	 * SELECT ORGANIZATION
	 * 
	 * @param idOrganization
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_ORGANIZATION, notes = BO_LOAD_ORGANIZATION_NOTES, response = BackOfficeOrganizationResponse.class)
	@GetMapping("/organizations/{idOrganization}")
	public ResponseEntity<Object> loadOrganization(@PathVariable final Integer idOrganization) {
		logger.info("loadOrganization");
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectOrganization(idOrganization);
			}
		}, logger);
	}

	/**
	 * 
	 * LOAD LICENSE
	 * 
	 * @param idLicense
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_LICENSE, notes = BO_LOAD_LICENSE_NOTES, response = LicenseResponse.class)
	@GetMapping("/licenses/{idLicense}")
	public ResponseEntity<Object> loadLicense(@PathVariable final Integer idLicense) {
		logger.info("loadLicense");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectLicense(idLicense);
			}
		}, logger);
	}

	/**
	 * LOAD ECOSYSTEM
	 * 
	 * @param idEcosystem
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_ECOSYSTEM, notes = BO_LOAD_ECOSYSTEM_NOTES, response = EcosystemResponse.class)
	@GetMapping("/ecosystems/{idEcosystem}")
	public ResponseEntity<Object> loadEcosystem(@PathVariable final Integer idEcosystem) {
		logger.info("loadEcosystem");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectEcosystem(idEcosystem);
			}
		}, logger);
	}

	/**
	 * LOAD TAG
	 * 
	 * @param idTag
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_TAG, notes = BO_LOAD_TAG_NOTES, response = TagResponse.class)
	@GetMapping("/tags/{idTag}")
	public ResponseEntity<Object> loadTag(@PathVariable final Integer idTag) {
		logger.info("loadTag");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectTag(idTag);
			}
		}, logger);
	}

	/**
	 * 
	 * LOAD DOMAIN
	 * 
	 * @param idDomain
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_DOMAIN, notes = BO_LOAD_DOMAIN_NOTES, response = DomainResponse.class)
	@GetMapping("/domains/{idDomain}")
	public ResponseEntity<Object> loadDomain(@PathVariable final Integer idDomain) {
		logger.info("loadDomain");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectDomain(idDomain);
			}
		}, logger);
	}

	/**
	 * 
	 * DELETE SUBDOMAIN
	 * 
	 * @param idSubdomain
	 * @return
	 */
	@ApiOperation(value = BO_DELETE_SUBDOMAIN, notes = BO_DELETE_SUBDOMAIN_NOTES, response = ServiceResponse.class)
	@DeleteMapping("/subdomains/{idSubdomain}")
	public ResponseEntity<Object> deleteSubdomain(@PathVariable final Integer idSubdomain) {
		logger.info("deleteSubdomain");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.deleteSubdomain(idSubdomain);
			}
		}, logger);
	}

	/**
	 * 
	 * UPDATE SUBDOMAIN
	 * 
	 * @param subdomainRequest
	 * @param idSubdomain
	 * @return
	 */
	@ApiOperation(value = BO_UPDATE_SUBDOMAIN, notes = BO_UPDATE_SUBDOMAIN_NOTES, response = SubdomainResponse.class)
	@PutMapping("/subdomains/{idSubdomain}")
	public ResponseEntity<Object> updateSubdomain(@RequestBody final SubdomainRequest subdomainRequest, @PathVariable final Integer idSubdomain) {
		logger.info("updateSubdomain");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.updateSubdomain(subdomainRequest, idSubdomain);
			}
		}, logger);
	}

	/**
	 * @param subdomainRequest
	 * @return
	 */
	@ApiOperation(value = BO_CREATE_SUBDOMAIN, notes = BO_CREATE_SUBDOMAIN_NOTES, response = SubdomainResponse.class)
	@PostMapping("/subdomains")
	public ResponseEntity<Object> createSubdomain(@RequestBody final SubdomainRequest subdomainRequest) {
		logger.info("createSubdomain");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.insertSubdomain(subdomainRequest);
			}
		}, logger);
	}

	@ApiOperation(value = BO_DELETE_TAG, notes = BO_DELETE_TAG_NOTES, response = ServiceResponse.class)
	@DeleteMapping("/tags/{idTag}")
	public ResponseEntity<Object> deleteTag(@PathVariable final Integer idTag) {
		logger.info("deleteTag");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.deleteTag(idTag);
			}
		}, logger);
	}

	@ApiOperation(value = BO_UPDATE_TAG, notes = BO_UPDATE_TAG_NOTES, response = TagResponse.class)
	@PutMapping("/tags/{idTag}")
	public ResponseEntity<Object> updateTag(@RequestBody final TagRequest tagRequest, @PathVariable final Integer idTag) {
		logger.info("updateTag");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.updateTag(tagRequest, idTag);
			}
		}, logger);
	}

	@ApiOperation(value = BO_CREATE_TAG, notes = BO_CREATE_TAG_NOTES, response = TagResponse.class)
	@PostMapping("/tags")
	public ResponseEntity<Object> createTag(@RequestBody final TagRequest tagRequest) {
		logger.info("createTag");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.insertTag(tagRequest);
			}
		}, logger);
	}

	@ApiOperation(value = BO_DELETE_LICENSE, notes = BO_DELETE_LICENSE_NOTES, response = ServiceResponse.class)
	@DeleteMapping("/licenses/{idLicense}")
	public ResponseEntity<Object> deleteLicense(@PathVariable final Integer idLicense) {
		logger.info("deleteLicense");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.deleteLicense(idLicense);
			}
		}, logger);
	}

	@ApiOperation(value = BO_UPDATE_LICENSE, notes = BO_UPDATE_LICENSE_NOTES, response = LicenseResponse.class)
	@PutMapping("/licenses/{idLicense}")
	public ResponseEntity<Object> updateLicense(@RequestBody final LicenseRequest licenseRequest, @PathVariable final Integer idLicense) {
		logger.info("updateLicense");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.updateLicense(licenseRequest, idLicense);
			}
		}, logger);
	}

	/**
	 * @param licenseRequest
	 * @return
	 */
	@ApiOperation(value = BO_CREATE_LICENSE, notes = BO_CREATE_LICENSE_NOTES, response = LicenseResponse.class)
	@PostMapping("/licenses")
	public ResponseEntity<Object> createLicense(@RequestBody final LicenseRequest licenseRequest) {
		logger.info("createLicense");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.insertLicense(licenseRequest);
			}
		}, logger);
	}

	@ApiOperation(value = BO_UPDATE_ORGANIZATION, notes = BO_UPDATE_ORGANIZATION_NOTES, response = BackOfficeOrganizationResponse.class)
	@PutMapping("/organizations/{idOrganization}")
	public ResponseEntity<Object> updateOrganization(@RequestBody final OrganizationRequest organizationRequest, @PathVariable final Integer idOrganization) {
		logger.info("updateOrganization");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.updateOrganization(organizationRequest, idOrganization);
			}
		}, logger);
	}

	@ApiOperation(value = BO_DELETE_ORGANIZATION, notes = BO_DELETE_ORGANIZATION_NOTES, response = ServiceResponse.class)
	@DeleteMapping("/organizations/{idOrganization}")
	public ResponseEntity<Object> deleteOrganization(@PathVariable final Integer idOrganization) {
		logger.info("deleteOrganization");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.deleteOrganization(idOrganization);
			}
		}, logger);
	}

	/**
	 * 
	 * @param organizationRequest
	 * @return
	 */
	@ApiOperation(value = BO_CREATE_ORGANIZATION, notes = BO_CREATE_ORGANIZATION_NOTES, response = BackOfficeOrganizationResponse.class)
	@PostMapping("/organizations")
	public ResponseEntity<Object> createOrganization(@RequestBody final OrganizationRequest organizationRequest) {
		logger.info("createOrganization");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.insertOrganization(organizationRequest);
			}
		}, logger);
	}

	/**
	 * 
	 * @param idEcosystem
	 * @return
	 */
	@ApiOperation(value = BO_DELETE_ECOSYSTEM, notes = BO_DELETE_ECOSYSTEM_NOTES, response = ServiceResponse.class)
	@DeleteMapping("/ecosystems/{idEcosystem}")
	public ResponseEntity<Object> deleteEcosystem(@PathVariable final Integer idEcosystem) {
		logger.info("deleteEcosystem");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.deleteEcosystem(idEcosystem);
			}
		}, logger);
	}

	/**
	 * 
	 * @param ecosystemRequest
	 * @return
	 */
	@ApiOperation(value = BO_CREATE_ECOSYSTEM, notes = BO_CREATE_ECOSYSTEM_NOTES, response = EcosystemResponse.class)
	@PostMapping("/ecosystems")
	public ResponseEntity<Object> createEcosystem(@RequestBody final EcosystemRequest ecosystemRequest) {

		logger.info("createEcosystem");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.insertEcosystem(ecosystemRequest);
			}
		}, logger);
	}

	/**
	 * 
	 * @param ecosystemRequest
	 * @param idEcosystem
	 * @return
	 */
	@ApiOperation(value = BO_UPDATE_ECOSYSTEM, notes = BO_UPDATE_ECOSYSTEM_NOTES, response = EcosystemResponse.class)
	@PutMapping("/ecosystems/{idEcosystem}")
	public ResponseEntity<Object> updateEcosystem(@RequestBody final EcosystemRequest ecosystemRequest, @PathVariable final Integer idEcosystem) {
		logger.info("updateEcosystem");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.updateEcosystem(ecosystemRequest, idEcosystem);
			}
		}, logger);
	}

	/**
	 * 
	 * @param idDomain
	 * @return
	 */
	@ApiOperation(value = BO_DELETE_DOMAIN, notes = BO_DELETE_DOMAIN_NOTES, response = ServiceResponse.class)
	@DeleteMapping("/domains/{idDomain}")
	public ResponseEntity<Object> deleteDomain(@PathVariable final Integer idDomain) {
		logger.info("deleteDomain");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.deleteDomain(idDomain);
			}
		}, logger);
	}

	/**
	 * @param domainRequest
	 * @return
	 */
	@ApiOperation(value = BO_DELETE_DOMAIN, notes = BO_DELETE_DOMAIN_NOTES, response = ServiceResponse.class)
	@PostMapping("/domains")
	public ResponseEntity<Object> createDomain(@RequestBody final DomainRequest domainRequest) {
		logger.info("createDomain");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.insertDomain(domainRequest);
			}
		}, logger);
	}

	/**
	 * 
	 * @param domainRequest
	 * @param idDomain
	 * @return
	 */
	@ApiOperation(value = BO_UPDATE_DOMAIN, notes = BO_UPDATE_DOMAIN_NOTES, response = DomainResponse.class)
	@PutMapping("/domains/{idDomain}")
	public ResponseEntity<Object> updateDomain(@RequestBody final DomainRequest domainRequest, @PathVariable final Integer idDomain) {
		logger.info("updateDomain");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.updateDomain(domainRequest, idDomain);
			}
		}, logger);
	}
	
	/**
	 * LOAD ALL STREAMS
	 * @param sort	 
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_STREAMS, notes = BO_LOAD_STREAMS_NOTES, response = ListStreamResponse.class, responseContainer = "List")
	@GetMapping("/streams")
	public ResponseEntity<Object> loadStreams(
			@RequestParam(required = false) final String sort, final HttpServletRequest request) {
		logger.info("loadStreams");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return streamService.selectStreams(sort);
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
	@GetMapping("/streams/{idstream}/icon")
	public void loadStreamIcon(@RequestParam(required = true) final  String organizationCode, @PathVariable final Integer idstream, final HttpServletRequest request,
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
	 * LOAD API 
	 * 	 
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_API, notes = BO_LOAD_API_NOTES, response = BackofficeDettaglioApiResponse.class)
	@GetMapping("/api/{apiCode}")
	public ResponseEntity<Object> loadApi(
			@PathVariable final String apiCode, final HttpServletRequest request) {
		logger.info("loadApi");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return apiService.selectBackofficeDettaglioApi(apiCode);
			}
		}, logger);
	}
	
	
	/**
	 * LOAD Stream by IdStream
	 * 	 
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_STREAM_BY_IDSTREAM, notes = BO_LOAD_STREAM_BY_IDSTREAM_NOTES, response = BackofficeDettaglioApiResponse.class)
	@GetMapping("/streams/{idStream}")
	public ResponseEntity<Object> loadStreamByIdStream(
			@PathVariable final Integer idStream, final HttpServletRequest request) {
		logger.info("loadStreamByIdStream");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return streamService.selectStreamByIdStream(idStream);
			}
		}, logger);
	}
	
	/**
	 * LOAD Dataset by IdDataset
	 * 	 
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_DATASET_BY_IDDATASET, notes = BO_LOAD_DATASET_BY_IDDATASET_NOTES, response = BackofficeDettaglioApiResponse.class)
	@GetMapping("/datasets/{idDataset}")
	public ResponseEntity<Object> loadDatasetByIdDataset(
			@PathVariable final Integer idDataset, final HttpServletRequest request) {
		logger.info("loadDatasetByIdDataset");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return datasetService.selectDatasetByIdDataset(idDataset);
			}
		}, logger);
	}
	
	/**
	 * LOAD Dataset by IdDataset datasetVersion
	 * 	 
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_DATASET_BY_IDDATASET_DATASETVERSION, notes = BO_LOAD_DATASET_BY_IDDATASET_DATASETVERSION_NOTES, response = BackofficeDettaglioApiResponse.class)
	@GetMapping("/datasets/{idDataset}/{datasetVersion}")
	public ResponseEntity<Object> loadDatasetByIdDatasetDatasetVersion(
			@PathVariable final Integer idDataset,
			@PathVariable final Integer datasetVersion,final HttpServletRequest request) {
		logger.info("loadDatasetByIdDataset");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return datasetService.selectDatasetByIdDatasetDatasetVersion(idDataset, datasetVersion);
			}
		}, logger);
	}
	
	/**
	 * LOAD Dataset by IdDataset datasetVersion
	 * 	 
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_DATASET_BY_DATASETCODE_DATASETVERSION, notes = BO_LOAD_DATASET_BY_DATASETCODE_DATASETVERSION_NOTES, response = BackofficeDettaglioApiResponse.class)
	@GetMapping("/datasets/datasetCode={datasetCode}/{datasetVersion}")
	public ResponseEntity<Object> loadDatasetByDatasetCodeDatasetVersion(
			@PathVariable final String datasetCode,
			@PathVariable final Integer datasetVersion,final HttpServletRequest request) {
		logger.info("loadDatasetByDatasetCodeDatasetVersion");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return datasetService.selectDatasetByDatasetCodeDatasetVersion(datasetCode, datasetVersion);
			}
		}, logger);
	}
	
	@ApiOperation(value = BO_LOAD_DATASET_BY_DATASETCODE, notes = BO_LOAD_DATASET_BY_DATASETCODE_NOTES, response = BackofficeDettaglioApiResponse.class)
	@GetMapping("/datasets/datasetCode={datasetCode}")
	public ResponseEntity<Object> loadDatasetByDatasetCode(
			@PathVariable final String datasetCode,
			final HttpServletRequest request) {
		logger.info("loadDatasetByDatasetCode");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return datasetService.selectDatasetByDatasetCode(datasetCode);
			}
		}, logger);
	}
	
	/**
	 * LOAD Stream by IdStream
	 * 	 
	 * @return
	 */
	@ApiOperation(value = BO_LOAD_STREAM_BY_SOCODE_STREAMCODE, notes = BO_LOAD_STREAM_BY_SOCODE_STREAMCODE_NOTES, response = BackofficeDettaglioApiResponse.class)
	@GetMapping("/streams/{soCode}/{streamCode}")
	public ResponseEntity<Object> loadStreamBySoCodeStreamCode(
			@PathVariable final String  soCode, 
			@PathVariable final String  streamCode,
			final HttpServletRequest request) {
		logger.info("loadStreamBySoCodeStreamCode");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return streamService.selectStreamBySoCodeStreamCode(soCode, streamCode);
			}
		}, logger);
	}
}
