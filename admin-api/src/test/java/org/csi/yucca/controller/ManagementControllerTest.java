package org.csi.yucca.controller;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.Iterator;

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
public class ManagementControllerTest extends TestBase{
	
	@BeforeClass
	public void setUpSecretObject() throws IOException {
		super.setUpSecretObject("/testSecret.json");
	}

	@DataProvider(name="json")
	public Iterator<Object[]> getFromJson(){  
		return super.getFromJson("/ManagementController_createSmartObject_dataIn.json");
	}	

	@Test(dataProvider = "json")
	public void backOfficeTestCrud(JSONObject dato) throws JSONException, InterruptedException {

		init(dato);
		
		testPost(dato);
		
		testDelete(dato);
		
		reset(dato);
	}


	private Integer testPost(JSONObject dato){
		
		String url = getUrl(dato) + dato.getString(JSON_KEY_APICODE) + "/organizations/" + ORGANIZATION_CODE_TEST_VALUE + "/smartobjects" ;
		
		String jsonBody                           = (String)dato.get(JSON_KEY_MESSAGE);
		jsonBody = "{\"idTenant\":"+ getIdTenant() + ","  + "\"twtusername\":\"" + TWTUSERNAME_TEST_VALUE + "\"," + "\"socode\":\"" + SMARTOBJECT_CODE_TEST_VALUE + "\"," + jsonBody.substring(1);
		
		RequestSpecification requestSpecification = null;
		try {
			requestSpecification = given().body(jsonBody).contentType(ContentType.JSON);	
		} catch (Exception e) {
			// TODO: handle exception
			String gg="";
			gg="";
		}
		
		Response response                         = requestSpecification.when().post(url);
		ValidatableResponse validatableResponse   = response.then().statusCode(dato.getInt(JSON_KEY_EXPECTED_HTTP_STATUS));
		Integer idGenerated                       = validatableResponse.extract().path(dato.getString(JSON_KEY_ID_GENERATED));
		return idGenerated;
	}
	
	private void testDelete(JSONObject dato){
		// http://localhost:8080/adminapi/1/management/organizations/SANDBOX/smartobjects/socod18		
		String url = getUrl(dato) + dato.getString(JSON_KEY_APICODE) + "/organizations/" + ORGANIZATION_CODE_TEST_VALUE + "/smartobjects/" + SMARTOBJECT_CODE_TEST_VALUE ;

		given().when().contentType(ContentType.JSON).delete(url).then().statusCode(dato.getInt(JSON_KEY_EXPECTED_HTTP_STATUS_DELETE));
	}
	
//	private void testPut(String url, JSONObject dato, Integer idEcosystem, Integer idDomain){
//		String messageUpdate = getMessage(dato, "adminapi.message.update", idEcosystem, idDomain);
//		
//		int expectedHttpStatusUpdateResponse = dato.getInt("expected.httpStatus.update-response");
//		
//		RequestSpecification updateRequestSpecification = given().body(messageUpdate).contentType(ContentType.JSON);
//		Response updateResponse = updateRequestSpecification.when().put(url);
//		ValidatableResponse updateValidatableResponse  = updateResponse.then().statusCode(expectedHttpStatusUpdateResponse);
//		// check dell'eventuale messaggio di errore:
//		if(!dato.optString("expected.update-errorName").isEmpty()){
//			updateValidatableResponse.assertThat().body("errorName", Matchers.containsString(dato.getString("expected.update-errorName")));
//		}
//		
//	}
	
//	private String getMessage(JSONObject dato, String keyMessage, Integer idEcosystem, Integer idDomain){
//	
//	String jsonString = (String)dato.get(keyMessage);
//	
//	if(!dato.getString("test-name").contains("ecosystem")){
//		jsonString = "{\"idEcosystem\":"+idEcosystem+"," + "\"idDomain\":"+idDomain+ "," + jsonString.substring(1);	
//	}
//	
//	return jsonString;
//}
	
	
}
