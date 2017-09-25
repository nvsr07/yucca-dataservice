package org.csi.yucca.controller;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.Iterator;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class PublicControllerTest extends TestBase {

	
	@BeforeClass
	public void setUpSecretObject() throws IOException {
		super.setUpSecretObject("/testSecret.json");
	}	
	
	@DataProvider(name="json")
	public Iterator<Object[]> getFromJson(){  
		return super.getFromJson(
			     "/PublicController_loadEcosystems_dataIn.json",
				 "/PublicController_loadDatasetTypes_dataIn.json", 
				 "/PublicController_datasetSubtypes_dataIn.json",
				 "/PublicController_loadSupplyTypes_dataIn.json",
				 "/PublicController_loadSoTypes_dataIn.json",
				 "/PublicController_loadSoCategories_dataIn.json", 
				 "/PublicController_loadLocationTypes_dataIn.json", 
				 "/PublicController_loadExposureTypes_dataIn.json", 
				 "/PublicController_loadPhenomenons_dataIn.json",
				 "/PublicController_loadMeasureUnit_dataIn.json",
				 "/PublicController_loadDataTypes_dataIn.json",
				 "/PublicController_loadTags_dataIn.json",
				 "/PublicController_loadDomains_dataIn.json",
			     "/PublicController_loadSubDomains_dataIn.json",
			     "/PublicController_loadOrganizations_dataIn.json",
			     "/PublicController_loadLicenses_dataIn.json"
				 );
	}	
	
	
	@Test(dataProvider="json")
	public void prova(JSONObject dato) {

		RequestSpecification rs = given();
		
		init(dato);
		
		Response rsp = rs.when().get(makeUrl(dato,"json"));
		
		ValidatableResponse response = rsp.then().assertThat().statusCode(Matchers.equalTo(dato.get(JSON_KEY_EXPECTED_HTTP_STATUS)));
		
		if(!dato.optString(JSON_KEY_EXPECTED_ERROR_NAME).isEmpty()){
			response.assertThat().body("errorName", Matchers.equalTo(dato.get(JSON_KEY_EXPECTED_ERROR_NAME)));
		}

		reset(dato);
	}
	
}
