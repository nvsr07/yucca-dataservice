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
	
	private static final Logger logger = Logger.getLogger(PublicController.class);

	@Autowired
	private PublicClassificationService classificationService;
	
	@Autowired
	private PublicComponentService componentService;  

	@Autowired
	private PublicSmartObjectService smartObjectService;    
	
	@Autowired
	private PublicTechnicalService technicalService;

	@GetMapping("/dataset_types")
	public ResponseEntity<Object> loadDatasetTypes( @RequestParam(required=false) final String sort  ) {

		logger.info("loadDatasetTypes");

		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return technicalService.selectDatasetType(sort);
			}
		}, logger);
		
	}		
	
	@GetMapping("/dataset_subtypes")
	public ResponseEntity<Object> loadDatasetSubtypes( @RequestParam(required=false) final Integer datasetTypeCode, 
			@RequestParam(required=false) final String sort  ) {

		logger.info("loadDatasetSubtypes");

		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return technicalService.selectDatasetSubtype(datasetTypeCode, sort);
			}
		}, logger);
		
	}		
	
	@GetMapping("/supply_types")
	public ResponseEntity<Object> loadSupplyTypes(@RequestParam(required=false) final String sort  ) {
		logger.info("loadSupplyTypes");
				
		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.selectSupplyType(sort);
			}
		}, logger);
	}			
	
	@GetMapping("/so_types")
	public ResponseEntity<Object> loadSoType(@RequestParam(required=false) final String sort  ) {
		logger.info("loadSoType");
				
		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.selectSoType(sort);
			}
		}, logger);
	}			
	
	@GetMapping("/so_categories")
	public ResponseEntity<Object> loadSoCategory(@RequestParam(required=false) final String sort  ) {
		logger.info("loadSoCategory");
				
		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.selectSoCategory(sort);
			}
		}, logger);
	}			
	
	@GetMapping("/location_types")
	public ResponseEntity<Object> loadLocationType(@RequestParam(required=false) final String sort  ) {
		logger.info("loadLocationType");
				
		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.selectLocationType(sort);
			}
		}, logger);
	}			
	
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
			@RequestParam(required=false) final String lang,
			@RequestParam(required=false) final Integer ecosystemCode ) {

		logger.info("loadTags");

		return run(new ApiCallable() {
			public Object call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectTag(lang, sort, ecosystemCode);
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




