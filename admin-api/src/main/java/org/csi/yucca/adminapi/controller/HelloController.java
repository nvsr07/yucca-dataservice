package org.csi.yucca.adminapi.controller;

import org.csi.yucca.adminapi.model.DataType;
import org.csi.yucca.adminapi.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

//	@Autowired
//	private TestService  testService;
//
////	@Autowired
////	private DomainMapper  domainMapper;
//
//	
//	@GetMapping("/hello")
//	public String hello(Model model) {
//
//		model.addAttribute("name", "John Doe");
//
////		List<Domain> listDomain = domainMapper.selectDomain(1);
//		
//// ------------------------------------------------
////		INSERIMENTO
//		int newId = 0;
//		try {
//			newId = testService.insertNewDataType("NEW CODE", "NEW DESC");
//		} 
//		catch (Exception e) {
//			System.out.println("ECCEZIONE: " + e.toString());
//			// TODO: handle exception
//		}
//		
//		
//// ------------------------------------------------		
////		LETTURA
//		DataType dataType = testService.selectById(newId);
//		model.addAttribute("newDataType_id", dataType.getIdDataType());
//		model.addAttribute("newDataType_CODE", dataType.getDataTypeCode());
//		model.addAttribute("newDataType_DESC", dataType.getDescription());
//		
//		
//// ------------------------------------------------		
////		UPDATE
//		testService.updateDataType(dataType.getIdDataType(), "MODIFIED CODE", "MODIFIED DESC");
//		
//// ------------------------------------------------
////		LETTURA
//		dataType = testService.selectById(dataType.getIdDataType());
//		model.addAttribute("newDataType_CODE", dataType.getDataTypeCode());
//		model.addAttribute("newDataType_DESC", dataType.getDescription());
//
//		
//// ------------------------------------------------		
////		DELETE		
////		testService.deleteDataType(dataType.getId());
////		dataType = testService.selectById(dataType.getId());
////		String confirmDeleted = "il record non e' stato cancellato";
////		if (dataType == null) {
////			confirmDeleted = "RECORD CANCELLATO CON SUCCESSO!!";
////		}
////		model.addAttribute("confirmDeleted", confirmDeleted);
//
//		
//		return "welcome";
//	}
//	
//	
}
