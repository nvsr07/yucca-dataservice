package org.csi.yucca.adminapi.controller.v1;

import static org.csi.yucca.adminapi.util.ApiDoc.*;
import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.controller.YuccaController;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.request.DomainRequest;
import org.csi.yucca.adminapi.request.SmartobjectRequest;
import org.csi.yucca.adminapi.response.DataTypeResponse;
import org.csi.yucca.adminapi.response.DomainResponse;
import org.csi.yucca.adminapi.response.SmartobjectResponse;
import org.csi.yucca.adminapi.service.SmartObjectService;
import org.csi.yucca.adminapi.util.ApiCallable;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("1/management")
public class ManagementController extends YuccaController{
	
	private static final Logger logger = Logger.getLogger(ManagementController.class);

	@Autowired
	private SmartObjectService smartObjectService;    

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
	public ResponseEntity<Object> updateSmartobject(@RequestBody final SmartobjectRequest smartobjectRequest, @PathVariable final Integer organizationCode, @PathVariable final String soCode ){
		logger.info("updateSmartobject");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
//				return smartObjectService.updateSmartobject(smartobjectRequest, organizationCode, soCode);
				return null;
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
	public ResponseEntity<Object> deleteSmartobject(@PathVariable final Integer organizationCode, @PathVariable final String soCode ){
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
	public ResponseEntity<Object> createSmartobject(@RequestBody final SmartobjectRequest smartobjectRequest, @PathVariable final Integer organizationCode){
		logger.info("createSmartobject");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.insertSmartobject(smartobjectRequest, organizationCode);
			}
		}, logger);		
	}
	
	
}
