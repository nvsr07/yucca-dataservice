package org.csi.yucca.adminapi.controller.v1;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.controller.YuccaController;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.request.DomainRequest;
import org.csi.yucca.adminapi.service.PublicClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.csi.yucca.adminapi.util.ApiCallable;

@RestController
@RequestMapping("1/backoffice")
public class BackOfficeController extends YuccaController{
	
	private static final Logger logger = Logger.getLogger(BackOfficeController.class);

	@Autowired
	private PublicClassificationService classificationService;
	

	@DeleteMapping("/domains/{idDomain}")
	public ResponseEntity<Object> deleteDomain(@PathVariable int idDomain){
		logger.info("deleteDomain");
		
		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
//				return classificationService.insertDomain(domainRequest);
				return null;
			}
		}, logger);		
	}

	
	/**
	 * CREATE SEQUENCE foo_a_seq;
       ALTER TABLE foo ALTER COLUMN a SET DEFAULT nextval('foo_a_seq');
       ALTER TABLE foo ALTER COLUMN a SET NOT NULL;
       ALTER SEQUENCE foo_a_seq OWNED BY foo.a;    -- 8.2 or later
       
       ALTER SEQUENCE int_yucca.yucca_d_domain_seq RESTART WITH 22;
       
	 * @param domainRequest
	 * @return
	 */
	@PostMapping("/domains")
	public ResponseEntity<Object> createDomain(@RequestBody final DomainRequest domainRequest ){
		logger.info("createDomain");
		
		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.insertDomain(domainRequest);
			}
		}, logger);		
	}
	
	
	
}




