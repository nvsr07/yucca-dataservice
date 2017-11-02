package org.csi.yucca.adminapi.controller.v1;

import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_DATASET_SUBTYPES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_DATASET_SUBTYPES_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_DATASET_TYPES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_DATASET_TYPES_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_TENANT_TYPES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_TENANT_TYPES_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_DATA_TYPES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_DATA_TYPES_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_DOMAINS;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_DOMAINS_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_ECOSYSTEMS;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_ECOSYSTEMS_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_EXPOSURE_TYPES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_EXPOSURE_TYPES_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_LICENSES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_LICENSES_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_LOCATION_TYPES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_LOCATION_TYPES_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_MEASURE_UNIT_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_MESAURE_UNIT;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_ORGANIZATIONS;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_ORGANIZATIONS_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_PHENOMENONS;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_PHENOMENONS_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_SO_CATEGORIES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_SO_CATEGORIES_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_SO_TYPES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_SO_TYPES_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_SUBDOMAINS;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_SUBDOMAINS_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_SUPPLY_TYPES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_SUPPLY_TYPES_NOTES;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_TAGS;
import static org.csi.yucca.adminapi.util.ApiDoc.P_LOAD_TAGS_NOTES;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.controller.YuccaController;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.response.DataTypeResponse;
import org.csi.yucca.adminapi.response.DatasetSubtypeResponse;
import org.csi.yucca.adminapi.response.DatasetTypeResponse;
import org.csi.yucca.adminapi.response.DomainResponse;
import org.csi.yucca.adminapi.response.EcosystemResponse;
import org.csi.yucca.adminapi.response.ExposureTypeResponse;
import org.csi.yucca.adminapi.response.LicenseResponse;
import org.csi.yucca.adminapi.response.LocationTypeResponse;
import org.csi.yucca.adminapi.response.MeasureUnitResponse;
import org.csi.yucca.adminapi.response.PhenomenonResponse;
import org.csi.yucca.adminapi.response.PublicOrganizationResponse;
import org.csi.yucca.adminapi.response.SoCategoryResponse;
import org.csi.yucca.adminapi.response.SoTypeResponse;
import org.csi.yucca.adminapi.response.SubdomainResponse;
import org.csi.yucca.adminapi.response.SupplyTypeResponse;
import org.csi.yucca.adminapi.response.TagResponse;
import org.csi.yucca.adminapi.response.TenantTypeResponse;
import org.csi.yucca.adminapi.service.ClassificationService;
import org.csi.yucca.adminapi.service.ComponentService;
import org.csi.yucca.adminapi.service.SmartObjectService;
import org.csi.yucca.adminapi.service.TechnicalService;
import org.csi.yucca.adminapi.service.TenantService;
import org.csi.yucca.adminapi.util.ApiCallable;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "public", description = "Endpoint for public")
@RestController
@RequestMapping("1/public")
public class PublicController extends YuccaController{
	
	private static final Logger logger = Logger.getLogger(PublicController.class);

	@Autowired
	private ClassificationService classificationService;
	
	@Autowired
	private ComponentService componentService;  

	@Autowired
	private SmartObjectService smartObjectService;    
	
	@Autowired
	private TechnicalService technicalService;

	@ApiOperation(value = P_LOAD_DATASET_TYPES, notes = P_LOAD_DATASET_TYPES_NOTES, response = DatasetTypeResponse.class, responseContainer="List")
	@GetMapping("/dataset_types")
	public ResponseEntity<Object> loadDatasetTypes( @RequestParam(required=false) final String sort  ) {

		logger.info("loadDatasetTypes");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return technicalService.selectDatasetType(sort);
			}
		}, logger);
		
	}	
	
	@Autowired
	private TenantService tenantService;

	@ApiOperation(value = P_LOAD_TENANT_TYPES, notes = P_LOAD_TENANT_TYPES_NOTES, response = TenantTypeResponse.class, responseContainer="List")
	@GetMapping("/tenant_types")
	public ResponseEntity<Object> loadTenantTypes() {

		logger.info("loadTenantTypes");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return tenantService.selectTenantTypes();
			}
		}, logger);
		
	}		
	
	@ApiOperation(value = P_LOAD_DATASET_SUBTYPES, notes = P_LOAD_DATASET_SUBTYPES_NOTES, response = DatasetSubtypeResponse.class, responseContainer="List")
	@GetMapping("/dataset_subtypes")
	public ResponseEntity<Object> loadDatasetSubtypes( @RequestParam(required=false) final String datasetTypeCode, 
			@RequestParam(required=false) final String sort  ) {

		logger.info("loadDatasetSubtypes");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return technicalService.selectDatasetSubtype(datasetTypeCode, sort);
			}
		}, logger);
		
	}		

	@ApiOperation(value = P_LOAD_SUPPLY_TYPES, notes = P_LOAD_SUPPLY_TYPES_NOTES, response = SupplyTypeResponse.class, responseContainer="List")
	@GetMapping("/supply_types")
	public ResponseEntity<Object> loadSupplyTypes(@RequestParam(required=false) final String sort  ) {
		logger.info("loadSupplyTypes");
				
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.selectSupplyType(sort);
			}
		}, logger);
	}			
	
	@ApiOperation(value = P_LOAD_SO_TYPES, notes = P_LOAD_SO_TYPES_NOTES, response = SoTypeResponse.class, responseContainer="List")
	@GetMapping("/so_types")
	public ResponseEntity<Object> loadSoType(@RequestParam(required=false) final String sort  ) {
		logger.info("loadSoType");
				
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.selectSoType(sort);
			}
		}, logger);
	}			
	
	@ApiOperation(value = P_LOAD_SO_CATEGORIES, notes = P_LOAD_SO_CATEGORIES_NOTES, response = SoCategoryResponse.class, responseContainer="List")
	@GetMapping("/so_categories")
	public ResponseEntity<Object> loadSoCategory(@RequestParam(required=false) final String sort  ) {
		logger.info("loadSoCategory");
				
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.selectSoCategory(sort);
			}
		}, logger);
	}			
	
	@ApiOperation(value = P_LOAD_LOCATION_TYPES, notes = P_LOAD_LOCATION_TYPES_NOTES, response = LocationTypeResponse.class, responseContainer="List")
	@GetMapping("/location_types")
	public ResponseEntity<Object> loadLocationType(@RequestParam(required=false) final String sort  ) {
		logger.info("loadLocationType");
				
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.selectLocationType(sort);
			}
		}, logger);
	}			
	
	
	@ApiOperation(value = P_LOAD_EXPOSURE_TYPES, notes = P_LOAD_EXPOSURE_TYPES_NOTES, response = ExposureTypeResponse.class, responseContainer="List")
	@GetMapping("/exposure_types")
	public ResponseEntity<Object> loadExposureType(@RequestParam(required=false) final String sort  ) {
		logger.info("loadExposureType");
				
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return smartObjectService.selectExposureType(sort);
			}
		}, logger);
	}			
	
	@ApiOperation(value = P_LOAD_PHENOMENONS, notes = P_LOAD_PHENOMENONS_NOTES, response = PhenomenonResponse.class, responseContainer="List")
	@GetMapping("/phenomenons")
	public ResponseEntity<Object> loadPhenomenons(@RequestParam(required=false) final String sort  ) {
		logger.info("loadPhenomenons");
				
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return componentService.selectPhenomenon(sort);
			}
		}, logger);
	}			
	
	@ApiOperation(value = P_LOAD_MESAURE_UNIT, notes = P_LOAD_MEASURE_UNIT_NOTES, response = MeasureUnitResponse.class, responseContainer="List")
	@GetMapping("/measure_units")
	public ResponseEntity<Object> loadMeasureUnit(@RequestParam(required=false) final String sort  ) {

		logger.info("loadMeasureUnit");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return componentService.selectMeasureUnit(sort);
			}
		}, logger);
		
	}			
	
	@ApiOperation(value = P_LOAD_DATA_TYPES, notes = P_LOAD_DATA_TYPES_NOTES, response = DataTypeResponse.class, responseContainer="List")
	@GetMapping("/data_types")
	public ResponseEntity<Object> loadDataTypes(@RequestParam(required=false) final String sort  ) {

		logger.info("loadDataTypes");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return componentService.selectDataType(sort);
			}
		}, logger);
	}		
	
	@ApiOperation(value = P_LOAD_TAGS, notes = P_LOAD_TAGS_NOTES, response = TagResponse.class, responseContainer="List")
	@GetMapping("/tags")
	public ResponseEntity<Object> loadTags( @RequestParam(required=false) final String sort, 
			@RequestParam(required=false) final String lang,
			@RequestParam(required=false) final String ecosystemCode ) {

		logger.info("loadTags");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectTag(lang, sort, ecosystemCode);
			}
		}, logger);
	}		
	
	@ApiOperation(value = P_LOAD_SUBDOMAINS, notes = P_LOAD_SUBDOMAINS_NOTES, response = SubdomainResponse.class, responseContainer="List")
	@GetMapping("/subdomains")
	public ResponseEntity<Object> loadSubdomains(@RequestParam(required=false) final String domainCode, 
			@RequestParam(required=false) final String sort, @RequestParam(required=false) final String lang  ) {

		logger.info("loadSubdomains");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectSubdomain(domainCode, lang, sort);
			}
		}, logger);
		
	}		
	
	@ApiOperation(value = P_LOAD_ORGANIZATIONS, notes = P_LOAD_ORGANIZATIONS_NOTES, response = PublicOrganizationResponse.class, responseContainer="List")
	@GetMapping("/organizations")
	public ResponseEntity<Object> loadOrganizations( @RequestParam(required=false) final String ecosystemCode, 
			@RequestParam(required=false) final String sort  ) {

		logger.info("loadOrganizations");

		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectOrganization(ecosystemCode, sort);
			}
		}, logger);
		
	}		
	
	@ApiOperation(value = P_LOAD_LICENSES, notes = P_LOAD_LICENSES_NOTES, response = LicenseResponse.class, responseContainer="List")
	@GetMapping("/licenses")
	public ResponseEntity<Object> loadLicenses( @RequestParam(required=false) final String sort  ) {

		logger.info("loadLicenses");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectLicense(sort);
			}
		}, logger);		
		
	}	
	
	@ApiOperation(value = P_LOAD_ECOSYSTEMS, notes = P_LOAD_ECOSYSTEMS_NOTES, response = EcosystemResponse.class, responseContainer="List")
	@GetMapping("/ecosystems")
	public ResponseEntity<Object> loadEcosystems(@RequestParam(required=false) final String organizationCode, 
			@RequestParam(required=false) final String sort  ) {

		logger.info("loadEcosystems");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectEcosystem(organizationCode, sort);
			}
		}, logger);		
		
	}	

	@ApiOperation(value = P_LOAD_DOMAINS, notes = P_LOAD_DOMAINS_NOTES, response = DomainResponse.class, responseContainer="List")
	@GetMapping("/domains")
	public ResponseEntity<Object> loadDomains(@RequestParam(required=false)final String ecosystemCode, 
			@RequestParam(required=false) final String lang, @RequestParam(required=false) final String sort  ) {

		logger.info("loadDomains");
		
		return run(new ApiCallable() {
			public ServiceResponse call() throws BadRequestException, NotFoundException, Exception {
				return classificationService.selectDomain(ecosystemCode, lang, sort);
			}
		}, logger);		
	}
	
}




