package org.csi.yucca.controller;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.Iterator;

import org.csi.yucca.adminapi.util.ServiceUtil;
import org.csi.yucca.adminapi.util.Type;
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
	
	
	// ******************************************************************
	//
	//                  PRIVATE METHODS
	//
	// ******************************************************************
	

	private Integer testPost(JSONObject dato){
		
		String url = getUrl(dato) + dato.getString(JSON_KEY_APICODE) + "/organizations/" + ORGANIZATION_CODE_TEST_VALUE + "/smartobjects" ;
		
		Integer idSoType   = getIdSoTypeFromJsonObject(dato);
		String socode      = getSocodeFromJsonObject(dato);
		String twtUsername = getTwtusernameFromJsonObject(dato); 
		Integer idTenant   = getIdTenantFromJsonObject(dato); 
		
		String jsonBody = (String)dato.get(JSON_KEY_MESSAGE);
		jsonBody = addToJsonObj("slug",         SLUG_TEST_VALUE,             jsonBody);

//		jsonBody = addToJsonObj("idTenant",     getIdTenant(),               jsonBody);
		jsonBody = addIdTenantToJsonObj(jsonBody, idTenant);
		jsonBody = addTwtUsernameToJsonObj(jsonBody, twtUsername);
		jsonBody = addSocodeToJsonObj(jsonBody, idSoType, socode);
		jsonBody = addToJsonObj("idSoType",     idSoType,                    jsonBody);
		
		RequestSpecification requestSpecification = null;
		try {
			requestSpecification = given().body(jsonBody).contentType(ContentType.JSON);	
		} catch (Exception e) {
		}
		
		Response response                         = requestSpecification.when().post(url);
		ValidatableResponse validatableResponse   = response.then().statusCode(dato.getInt(JSON_KEY_EXPECTED_HTTP_STATUS));
		Integer idGenerated                       = validatableResponse.extract().path(dato.getString(JSON_KEY_ID_GENERATED));
		return idGenerated;
	}	
	
	
	
	private String addToJsonObj(String key, String value, String jsonBody){
		
		if (!jsonBody.contains(key)) {
			return "{\"" + key + "\":\"" + value  + "\"," + jsonBody.substring(1);
		}
		
		return jsonBody;
	}

	private String addToJsonObj(String key, int value, String jsonBody){
		
		if (!jsonBody.contains(key)) {
			return "{\"" + key + "\":"+ value + "," + jsonBody.substring(1);
		}
		
		return jsonBody;
	}
	
	private String addSocodeToJsonObj(String jsonBody, Integer idSoType, String socode){
		
		if(socode != null){
			setSocode(socode);
			return addToJsonObj("socode", socode, jsonBody);
		}
		
		if (!ServiceUtil.isType(Type.DEVICE, idSoType)) {
			setSocode(SMARTOBJECT_CODE_TEST_VALUE_NO_DEVICE);
			return addToJsonObj("socode", SMARTOBJECT_CODE_TEST_VALUE_NO_DEVICE, jsonBody);
		}

		setSocode(SMARTOBJECT_CODE_TEST_VALUE);
		return addToJsonObj("socode", SMARTOBJECT_CODE_TEST_VALUE, jsonBody);
	}

	private String addTwtUsernameToJsonObj(String jsonBody, String twtUsername){
		
		if(twtUsername != null){
			return jsonBody;		
		}
		
		return addToJsonObj("twtusername", TWTUSERNAME_TEST_VALUE, jsonBody);
	}
	
	private String addIdTenantToJsonObj(String jsonBody, Integer idTenant){
		if(idTenant != null){
			return addToJsonObj("idTenant", idTenant, jsonBody);		
		}
		return addToJsonObj("idTenant", getIdTenant(), jsonBody);
	}
	
	private Integer getIdSoTypeFromJsonObject(JSONObject dato){
		return (Integer)dato.get(JSON_KEY_ID_SO_TYPE);
	}

	private String getSocodeFromJsonObject(JSONObject dato){
		try {
			return (String)dato.get(JSON_KEY_SOCODE);
		} 
		catch (Exception e) {
			return null;
		}
	}

	private String getTwtusernameFromJsonObject(JSONObject dato){
		try {
			return (String)dato.get(JSON_KEY_TWTUSERNAME);
		} 
		catch (Exception e) {
			return null;
		}
	}

	private Integer getIdTenantFromJsonObject(JSONObject dato){
		try {
			return (Integer)dato.get(JSON_KEY_ID_TENANT);
		} 
		catch (Exception e) {
			return null;
		}
	}
	
	private void testDelete(JSONObject dato){
		String url = getUrl(dato) 
				+ dato.getString(JSON_KEY_APICODE) 
				+ "/organizations/" + ORGANIZATION_CODE_TEST_VALUE 
				+ "/smartobjects/" + getSocode() ;
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
