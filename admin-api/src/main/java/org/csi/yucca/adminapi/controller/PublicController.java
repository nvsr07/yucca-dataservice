package org.csi.yucca.adminapi.controller;

import org.apache.log4j.Logger;
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
@RequestMapping(Constants.ADMIN_API_VERSION + "/public")
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
