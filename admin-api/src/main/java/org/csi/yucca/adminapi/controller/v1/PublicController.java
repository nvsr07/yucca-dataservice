package org.csi.yucca.adminapi.controller.v1;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.controller.YuccaController;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.service.PublicClassificationService;
import org.csi.yucca.adminapi.service.PublicComponentService;
import org.csi.yucca.adminapi.service.PublicSmartObjectService;
import org.csi.yucca.adminapi.service.PublicTechnicalService;
import org.csi.yucca.adminapi.util.Constants;
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

	@GetMapping("/tags")
	public ResponseEntity<Object> loadTags( @RequestParam(required=false) String sort, 
			@RequestParam(required=false) String lang  ) {

		logger.info("loadTags");
		
		Object list = null;
		
		try {
			list = classificationService.selectTag(lang, sort);
		} 
		catch (BadRequestException badRequestException) {
			logger.error("BadRequestException: " + badRequestException);
			return buildErrorResponse(badRequestException);
		}
		catch (NotFoundException notFoundException) {
			logger.error("NotFoundException: " + notFoundException);			
			return buildErrorResponse(notFoundException);
		}
		catch (Exception e) {
			return internalServerError(e);
		}
		
		return buildResponse(list);
		
	}		
	
	
	@GetMapping("/subdomains")
	public ResponseEntity<Object> loadSubdomains(@RequestParam(required=false) Integer domainCode, 
			@RequestParam(required=false) String sort, @RequestParam(required=false) String lang  ) {

		logger.info("loadSubdomains");
		
		Object list = null;
		
		try {
			list = classificationService.selectSubdomain(domainCode, lang, sort);
		} 
		catch (BadRequestException badRequestException) {
			logger.error("BadRequestException: " + badRequestException);
			return buildErrorResponse(badRequestException);
		}
		catch (NotFoundException notFoundException) {
			logger.error("NotFoundException: " + notFoundException);			
			return buildErrorResponse(notFoundException);
		}
		catch (Exception e) {
			return internalServerError(e);
		}
		
		return buildResponse(list);
		
	}		
	
	
	@GetMapping("/organizations")
	public ResponseEntity<Object> loadOrganizations( @RequestParam(required=false) Integer ecosystemCode, @RequestParam(required=false) String sort  ) {

		logger.info("loadOrganizations");
		
		Object list = null;
		
		try {
			list = classificationService.selectOrganization(ecosystemCode, sort);
		} 
		catch (BadRequestException badRequestException) {
			logger.error("BadRequestException: " + badRequestException);
			return buildErrorResponse(badRequestException);
		}
		catch (NotFoundException notFoundException) {
			logger.error("NotFoundException: " + notFoundException);			
			return buildErrorResponse(notFoundException);
		}
		catch (Exception e) {
			return internalServerError(e);
		}
		
		return buildResponse(list);
		
	}		
	
	@GetMapping("/licenses")
	public ResponseEntity<Object> loadLicenses( @RequestParam(required=false) String sort  ) {

		logger.info("loadLicenses");
		
		Object list = null;
		
		try {
			list = classificationService.selectLicense(sort);
		} 
		catch (BadRequestException badRequestException) {
			logger.error("BadRequestException: " + badRequestException);
			return buildErrorResponse(badRequestException);
		}
		catch (NotFoundException notFoundException) {
			logger.error("NotFoundException: " + notFoundException);			
			return buildErrorResponse(notFoundException);
		}
		catch (Exception e) {
			return internalServerError(e);
		}
		
		return buildResponse(list);
		
	}	

	
	@GetMapping("/ecosystems")
	public ResponseEntity<Object> loadEcosystems(@RequestParam(required=false) Integer organizationCode, 
			@RequestParam(required=false) String sort  ) {

		logger.info("loadEcosystems");
		
		Object list = null;
		
		try {
			list = classificationService.selectEcosystem(organizationCode, sort);
		} 
		catch (BadRequestException badRequestException) {
			logger.error("BadRequestException: " + badRequestException);
			return buildErrorResponse(badRequestException);
		}
		catch (NotFoundException notFoundException) {
			logger.error("NotFoundException: " + notFoundException);			
			return buildErrorResponse(notFoundException);
		}
		catch (Exception e) {
			return internalServerError(e);
		}
		
		return buildResponse(list);
		
	}	

	@GetMapping("/domains")
	public ResponseEntity<Object> loadDomains(@RequestParam(required=false) Integer ecosystemCode, 
			@RequestParam(required=false) String lang, @RequestParam(required=false) String sort  ) {

		logger.info("loadDomains");
		
		Object listDomain = null;
		
		try {
			listDomain = classificationService.selectDomain(ecosystemCode, lang, sort);
		} 
		catch (BadRequestException badRequestException) {
			logger.error("BadRequestException: " + badRequestException);
			return buildErrorResponse(badRequestException);
		}
		catch (NotFoundException notFoundException) {
			logger.error("NotFoundException: " + notFoundException);			
			return buildErrorResponse(notFoundException);
		}
		catch (Exception e) {
			return internalServerError(e);
		}
		
		return buildResponse(listDomain);
		
	}
	
}
