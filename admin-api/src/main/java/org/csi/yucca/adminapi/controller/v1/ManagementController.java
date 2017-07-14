package org.csi.yucca.adminapi.controller.v1;

import org.csi.yucca.adminapi.model.DataType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("management")
public class ManagementController {
	
	@GetMapping("/data_types")
	public ResponseEntity<DataType> loadDataTypes( @RequestParam String sort  ) {

		DataType dataType = new DataType();
		dataType.setDataTypeCode("prova");
		dataType.setDescription("prova");
		dataType.setIdDataType(100);
		
		return new ResponseEntity<DataType>(dataType, HttpStatus.OK);
	}
	
	
	
}
