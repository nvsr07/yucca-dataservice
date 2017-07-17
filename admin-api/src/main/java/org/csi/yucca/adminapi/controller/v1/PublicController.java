package org.csi.yucca.adminapi.controller.v1;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.controller.YuccaController;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.service.PublicClassificationService;
import org.csi.yucca.adminapi.service.PublicComponentService;
import org.csi.yucca.adminapi.service.PublicSmartObjectService;
import org.csi.yucca.adminapi.service.PublicTechnicalService;
import org.csi.yucca.adminapi.util.ApiCallable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("1/public")
public class PublicController extends YuccaController{
	
//	https://www.petrikainulainen.net/programming/spring-framework/unit-testing-of-spring-mvc-controllers-configuration/
	
	private static final Logger logger = Logger.getLogger(PublicController.class);

	@Autowired
	private PublicClassificationService classificationService;
	
	@Autowired
	private PublicComponentService componentService;  

	@Autowired
	private PublicSmartObjectService smartObjectService;    
	
	@Autowired
	private PublicTechnicalService technicalService;     

	@GetMapping("/exposure_types")
	public ResponseEntity<Object> loadExposureType(@RequestParam(required=false) final String sort  ) {
		logger.info("loadExposureType");
				
		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.selectExposureType(sort);
			}
		}, logger);
	}			
	
	@GetMapping("/phenomenons")
	public ResponseEntity<Object> loadPhenomenons(@RequestParam(required=false) final String sort  ) {
		logger.info("loadPhenomenons");
				
		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return componentService.selectPhenomenon(sort);
			}
		}, logger);
	}			
	
	@GetMapping("/measure_units")
	public ResponseEntity<Object> loadMeasureUnit(@RequestParam(required=false) final String sort  ) {

		logger.info("loadMeasureUnit");

		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return componentService.selectMeasureUnit(sort);
			}
		}, logger);
		
	}			
	
	@GetMapping("/data_types")
	public ResponseEntity<Object> loadDataTypes(@RequestParam(required=false) final String sort  ) {

		logger.info("loadDataTypes");

		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return componentService.selectDataType(sort);
			}
		}, logger);
	}		
	
	@GetMapping("/tags")
	public ResponseEntity<Object> loadTags( @RequestParam(required=false) final String sort, 
			@RequestParam(required=false) final String lang  ) {

		logger.info("loadTags");

		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectTag(lang, sort);
			}
		}, logger);
	}		
	
	@GetMapping("/subdomains")
	public ResponseEntity<Object> loadSubdomains(@RequestParam(required=false) final Integer domainCode, 
			@RequestParam(required=false) final String sort, @RequestParam(required=false) final String lang  ) {

		logger.info("loadSubdomains");

		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectSubdomain(domainCode, lang, sort);
			}
		}, logger);
		
	}		
	
	@GetMapping("/organizations")
	public ResponseEntity<Object> loadOrganizations( @RequestParam(required=false) final Integer ecosystemCode, 
			@RequestParam(required=false) final String sort  ) {

		logger.info("loadOrganizations");

		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectOrganization(ecosystemCode, sort);
			}
		}, logger);
		
	}		
	
	@GetMapping("/licenses")
	public ResponseEntity<Object> loadLicenses( @RequestParam(required=false) final String sort  ) {

		logger.info("loadLicenses");
		
		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectLicense(sort);
			}
		}, logger);		
		
	}	
	
	@GetMapping("/ecosystems")
	public ResponseEntity<Object> loadEcosystems(@RequestParam(required=false) final Integer organizationCode, 
			@RequestParam(required=false) final String sort  ) {

		logger.info("loadEcosystems");
		
		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectEcosystem(organizationCode, sort);
			}
		}, logger);		
		
	}	

	@GetMapping("/domains")
	public ResponseEntity<Object> loadDomains(@RequestParam(required=false)final Integer ecosystemCode, 
			@RequestParam(required=false) final String lang, @RequestParam(required=false) final String sort  ) {

		logger.info("loadDomains");
		
		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectDomain(ecosystemCode, lang, sort);
			}
		}, logger);		
	}
	
}




