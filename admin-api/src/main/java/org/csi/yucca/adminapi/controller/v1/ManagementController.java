package org.csi.yucca.adminapi.controller.v1;

import static org.csi.yucca.adminapi.util.ApiDoc.M_CREATE_SMARTOBJECT;
import static org.csi.yucca.adminapi.util.ApiDoc.M_CREATE_SMARTOBJECT_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_DELETE_SMARTOBJECT;
import static org.csi.yucca.adminapi.util.ApiDoc.M_DELETE_SMARTOBJECT_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_SMART_OBJECT;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_SMART_OBJECTS;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_SMART_OBJECTS_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_SMART_OBJECT_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_TENANT;
import static org.csi.yucca.adminapi.util.ApiDoc.M_LOAD_TENANT_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.M_UPDATE_SMARTOBJECT;
import static org.csi.yucca.adminapi.util.ApiDoc.M_UPDATE_SMARTOBJECT_NOTES;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.controller.YuccaController;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.request.SmartobjectRequest;
import org.csi.yucca.adminapi.response.DataTypeResponse;
import org.csi.yucca.adminapi.response.DettaglioSmartobjectResponse;
import org.csi.yucca.adminapi.response.DomainResponse;
import org.csi.yucca.adminapi.response.SmartobjectResponse;
import org.csi.yucca.adminapi.service.SmartObjectService;
import org.csi.yucca.adminapi.service.TenantService;
import org.csi.yucca.adminapi.util.ApiCallable;
import org.csi.yucca.adminapi.util.ServiceResponse;
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

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("1/management")
public class ManagementController extends YuccaController{
	
	private static final Logger logger = Logger.getLogger(ManagementController.class);

	@Autowired
	private SmartObjectService smartObjectService;    

	@Autowired
	private TenantService tenantService;    
	
	@ApiOperation(value = M_LOAD_SMART_OBJECT, notes = M_LOAD_SMART_OBJECT_NOTES, response = DettaglioSmartobjectResponse.class)
	@GetMapping("/organizations/{organizationCode}/smartobjects/{socode}")
	public ResponseEntity<Object> loadSmartobject(@PathVariable final String organizationCode, 
			@PathVariable final String socode, final HttpServletRequest request) {
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
	@ApiOperation(value = M_LOAD_SMART_OBJECTS, notes = M_LOAD_SMART_OBJECTS_NOTES, response = DettaglioSmartobjectResponse.class, responseContainer="List")
	@GetMapping("/organizations/{organizationCode}/smartobjects")
	public ResponseEntity<Object> loadSmartobjects(@PathVariable final String organizationCode,
			@RequestParam(required=false) final String tenantCode, final HttpServletRequest request) {
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
	@ApiOperation(value = M_LOAD_TENANT, notes = M_LOAD_TENANT_NOTES, response = DomainResponse.class, responseContainer="List")
	@GetMapping("/tenants")
	public ResponseEntity<Object> loadTenants(
			@RequestParam(required=false)final Integer skip, 
			@RequestParam(required=false) final Integer limit, 
			@RequestParam(required=false) final String fields,
			@RequestParam(required=false) final String sort,
			@RequestParam(required=false) final String embed) {
		logger.info("loadTenants");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return tenantService.selectTenants(sort);
			}
		}, logger);		
	}
	
	/**
	 * 
	 * campi che non possono essere cambiati:
	 * idsotype , slug, socode
	 * 
	 * @param smartobjectRequest
	 * @param organizationCode
	 * @param soCode
	 * @return
	 */
	@ApiOperation(value = M_UPDATE_SMARTOBJECT, notes = M_UPDATE_SMARTOBJECT_NOTES, response = SmartobjectResponse.class)
	@PutMapping("/organizations/{organizationCode}/smartobjects/{soCode}")
	public ResponseEntity<Object> updateSmartobject(@RequestBody final SmartobjectRequest smartobjectRequest, 
			@PathVariable final String organizationCode, @PathVariable final String soCode ){
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
	public ResponseEntity<Object> deleteSmartobject(@PathVariable final String organizationCode, @PathVariable final String soCode ){
		logger.info("deleteSmartobject");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.deleteSmartObject(organizationCode, soCode);
			}
		}, logger);		
	}
	
	/**
	 * IMPORTANTE!
	 * rendere univoche le colonne slug, e twtusername della tabella smartobject.
	 * Inoltre occorre creare l'autoincrement dell'id_smart_object.
	 * 
	 * CREATE SEQUENCE int_yucca.smart_object_id_smart_object_seq;
	 * ALTER TABLE int_yucca.yucca_smart_object ALTER COLUMN id_smart_object SET DEFAULT nextval('int_yucca.smart_object_id_smart_object_seq');
	 * ALTER TABLE int_yucca.yucca_smart_object ALTER COLUMN id_smart_object SET NOT NULL;
	 * ALTER SEQUENCE int_yucca.smart_object_id_smart_object_seq OWNED BY int_yucca.yucca_smart_object.id_smart_object;    -- 8.2 or later
	 * 
	 * ALTER SEQUENCE int_yucca.smart_object_id_smart_object_seq RESTART WITH 400;
	 * 
	 * @param smartobjectRequest
	 * @param organizationCode
	 * @return
	 */
	@ApiOperation(value = M_CREATE_SMARTOBJECT, notes = M_CREATE_SMARTOBJECT_NOTES, response = DataTypeResponse.class)
	@PostMapping("/organizations/{organizationCode}/smartobjects")
	public ResponseEntity<Object> createSmartobject(@RequestBody final SmartobjectRequest smartobjectRequest, @PathVariable final String organizationCode){
		logger.info("createSmartobject");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.insertSmartobject(smartobjectRequest, organizationCode);
			}
		}, logger);		
	}
	
	
}
