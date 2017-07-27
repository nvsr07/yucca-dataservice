package org.csi.yucca.adminapi.controller.v1;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.controller.YuccaController;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.request.DomainRequest;
import org.csi.yucca.adminapi.request.EcosystemRequest;
import org.csi.yucca.adminapi.request.OrganizationRequest;
import org.csi.yucca.adminapi.service.ClassificationService;
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

@RestController
@RequestMapping("1/backoffice")
public class BackOfficeController extends YuccaController{
	
	private static final Logger logger = Logger.getLogger(BackOfficeController.class);

	@Autowired
	private ClassificationService classificationService;
	
	
	
	@PutMapping("/organizations/{idOrganization}")
	public ResponseEntity<Object> updateOrganization(@RequestBody final OrganizationRequest organizationRequest, @PathVariable final Integer idOrganization ){
		logger.info("updateOrganization");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.updateOrganization(organizationRequest, idOrganization);
			}
		}, logger);		
	}	
	
	@DeleteMapping("/organizations/{idOrganization}")
	public ResponseEntity<Object> deleteOrganization(@PathVariable final Integer idOrganization){
		logger.info("deleteOrganization");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.deleteOrganization(idOrganization);
			}
		}, logger);		
	}
	
	@PostMapping("/organizations")
	public ResponseEntity<Object> createOrganization(@RequestBody final OrganizationRequest organizationRequest ){
		logger.info("createOrganization");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.insertOrganization(organizationRequest);
			}
		}, logger);		
	}
	
	@DeleteMapping("/ecosystems/{idEcosystem}")
	public ResponseEntity<Object> deleteEcosystem(@PathVariable final Integer idEcosystem){
		logger.info("deleteEcosystem");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.deleteEcosystem(idEcosystem);
			}
		}, logger);		
	}
	
	@PostMapping("/ecosystems")
	public ResponseEntity<Object> createEcosystem(@RequestBody final EcosystemRequest ecosystemRequest ){
		logger.info("createEcosystem");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.insertEcosystem(ecosystemRequest);
			}
		}, logger);		
	}
	
	
	@PutMapping("/ecosystems/{idEcosystem}")
	public ResponseEntity<Object> updateEcosystem(@RequestBody final EcosystemRequest ecosystemRequest, @PathVariable final Integer idEcosystem ){
		logger.info("updateEcosystem");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.updateEcosystem(ecosystemRequest, idEcosystem);
			}
		}, logger);		
	}	
	
	
	@DeleteMapping("/domains/{idDomain}")
	public ResponseEntity<Object> deleteDomain(@PathVariable final Integer idDomain){
		logger.info("deleteDomain");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.deleteDomain(idDomain);
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
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.insertDomain(domainRequest);
			}
		}, logger);		
	}

	@PutMapping("/domains/{idDomain}")
	public ResponseEntity<Object> updateDomain(@RequestBody final DomainRequest domainRequest, @PathVariable final Integer idDomain ){
		logger.info("updateDomain");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.updateDomain(domainRequest, idDomain);
			}
		}, logger);		
	}	
	
}




