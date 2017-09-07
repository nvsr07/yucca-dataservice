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
			     "/BackOfficeController_licenses_dataIn.json",
			     "/BackOfficeController_ecosystem_dataIn.json",
			     "/BackOfficeController_domain_dataIn.json",
			     "/BackOfficeController_organization_dataIn.json",
			     "/BackOfficeController_tag_dataIn.json",
			     "/BackOfficeController_subdomain_dataIn.json",
			     "/BackOfficeController_data_type_dataIn.json",
			     "/BackOfficeController_measureunit_dataIn.json",
			     "/BackOfficeController_phenomenon_dataIn.json"
			     );
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
	public void backOfficeTestCrud(JSONObject dato) throws JSONException, InterruptedException {
		
		init(dato);

		// define url
		StringBuilder urlBuilder = getUrl(dato.getString("adminapi.apicode"), dato.getString("adminapi.entityset"), dato);
		
		// test post
		Integer id = testPost(urlBuilder.toString(), dato, getIdEcosystem(), getIdDomain());
		
		// test put and delete
		if (id != null) {
			urlBuilder.append("/").append(id);
			testPut(urlBuilder.toString(), dato, getIdEcosystem(), getIdDomain());
			testDelete(urlBuilder.toString(), dato);
		}

		
		reset(dato);
	}
	
	private String getMessage(JSONObject dato, String keyMessage, Integer idEcosystem, Integer idDomain){
		String jsonString = (String)dato.get(keyMessage);
		if(!dato.getString("test-name").contains("ecosystem")){
			jsonString = "{\"idEcosystem\":"+idEcosystem+"," + "\"idDomain\":"+idDomain+ "," + jsonString.substring(1);	
		}
		return jsonString;
	}
	
	private Integer testPost(String url, JSONObject dato, Integer idEcosystem, Integer idDomain){
		String jsonString = getMessage(dato, "adminapi.message", idEcosystem, idDomain);
		RequestSpecification requestSpecification = given().body(jsonString).contentType(ContentType.JSON);
		
		Response response = requestSpecification.when().post(url);
		ValidatableResponse validatableResponse  = response.then().statusCode(dato.getInt("expected.httpStatus.response"));
		Integer idGenerated =  validatableResponse.extract().path(dato.getString("adminapi.id-generated"));
		// check dell'eventuale messaggio di errore:
		if(!dato.optString("expected.errorName").isEmpty()){
			validatableResponse.assertThat().body("errorName", Matchers.containsString(dato.getString("expected.errorName")));
		}
		return idGenerated;

	}

	private void testDelete(String url, JSONObject dato){
		given().when().contentType(ContentType.JSON).delete(url).then().statusCode(dato.getInt("expected.httpStatus.delete-response"));
	}
	
	private void testPut(String url, JSONObject dato, Integer idEcosystem, Integer idDomain){
		String messageUpdate = getMessage(dato, "adminapi.message.update", idEcosystem, idDomain);
		
		int expectedHttpStatusUpdateResponse = dato.getInt("expected.httpStatus.update-response");
		
		RequestSpecification updateRequestSpecification = given().body(messageUpdate).contentType(ContentType.JSON);
		Response updateResponse = updateRequestSpecification.when().put(url);
		ValidatableResponse updateValidatableResponse  = updateResponse.then().statusCode(expectedHttpStatusUpdateResponse);
		// check dell'eventuale messaggio di errore:
		if(!dato.optString("expected.update-errorName").isEmpty()){
			updateValidatableResponse.assertThat().body("errorName", Matchers.containsString(dato.getString("expected.update-errorName")));
		}
		
	}
	
}
