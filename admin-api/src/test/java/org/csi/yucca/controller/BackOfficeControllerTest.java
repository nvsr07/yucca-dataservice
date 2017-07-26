package org.csi.yucca.controller;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.Iterator;

import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;


/**
 * https://github.com/csipiemonte/yucca-dataservice/blob/oData-multiapi/insertdata-api/src/test/java/org/csi/yucca/dataservice/insertdataapi/test/unit/HttpDatasetInsertTest.java

 * @author gianfranco.stolfa
 *
 */
public class BackOfficeControllerTest extends TestBase{
	
	@BeforeClass
	public void setUpSecretObject() throws IOException {
		super.setUpSecretObject("/testSecret.json");
	}

	@DataProvider(name="json")
	public Iterator<Object[]> getFromJson(){  
		return super.getFromJson(
			     "/BackOfficeController_domain_dataIn.json");
	}	

	/**
	 * POST
	 * http://localhost:8080/adminapi/1/backoffice/domains
	 * 
	 * @param dato
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	@Test(dataProvider = "json")
	public void testCrudDomain(JSONObject dato) throws JSONException, InterruptedException {
		
		// define url
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(dato.get("adminapi.url")).append("/").append(dato.get("adminapi.version")).append("/").append(dato.get("adminapi.apicode")).append("/domains");

		// test post
		Integer idDomain = testPostDomain(urlBuilder.toString(), dato);
		
		// test put and delete
		if (idDomain != null) {
			urlBuilder.append("/").append(idDomain);
			testPutDomain(urlBuilder.toString(), dato);
			testDeleteDomain(urlBuilder.toString(), dato);
		}
		
	}
	
	private Integer testPostDomain(String url, JSONObject dato){
		RequestSpecification requestSpecification = given().body(dato.get("adminapi.message")).contentType(ContentType.JSON);
		Response response = requestSpecification.when().post(url);
		ValidatableResponse validatableResponse  = response.then().statusCode(dato.getInt("expected.httpStatus.response"));
		Integer idDomain =  validatableResponse.extract().path("idDomain");
		// check dell'eventuale messaggio di errore:
		if(!dato.optString("expected.errorName").isEmpty()){
			validatableResponse.assertThat().body("errorName", Matchers.equalTo(dato.get("expected.errorName")));
		}
		return idDomain;

	}

	private void testDeleteDomain(String url, JSONObject dato){
		given().when().contentType(ContentType.JSON).delete(url).then().statusCode(dato.getInt("expected.httpStatus.delete-response"));
	}
	
	private void testPutDomain(String url, JSONObject dato){
		RequestSpecification updateRequestSpecification = given().body(dato.get("adminapi.message.update")).contentType(ContentType.JSON);
		Response updateResponse = updateRequestSpecification.when().put(url);
		ValidatableResponse updateValidatableResponse  = updateResponse.then().statusCode(dato.getInt("expected.httpStatus.update-response"));
		// check dell'eventuale messaggio di errore:
		if(!dato.optString("expected.update-errorName").isEmpty()){
			updateValidatableResponse.assertThat().body("errorName", Matchers.equalTo(dato.get("expected.update-errorName")));
		}
		
	}
	
	
	
	
}
