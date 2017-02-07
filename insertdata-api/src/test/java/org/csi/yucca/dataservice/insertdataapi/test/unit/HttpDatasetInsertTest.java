package org.csi.yucca.dataservice.insertdataapi.test.unit;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.csi.yucca.dataservice.insertdataapi.test.RestBase;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpDatasetInsertTest extends RestBase {

	@BeforeClass
	public void setUpSecretObject() throws IOException {
		super.setUpSecretObject("/testSecret.json");
	}
	
	@DataProvider(name = "ValidationDatasetInsertTest")
	public Iterator<Object[]> getFromJson() {

		return super.getFromJson("/ValidationDatasetInsertTest.json");
		
	}

	
	@Test(dataProvider = "ValidationDatasetInsertTest")
	public void sendHTTPStatusErrorCodeTesting(JSONObject dato) {
		if (dato.optBoolean("api.toskip") || dato.optBoolean("api.httptoskip"))
				throw new SkipException("TODO in future version");
		
		RequestSpecification rs = given().body(dato.get("api.message")).contentType(ContentType.JSON);

		if (StringUtils.isNotEmpty(dato.optString("api.username"))) {
			rs = rs.auth().basic(dato.getString("api.username"), dato.getString("api.password"));
		}

		Response rsp = rs.when().post(dato.get("api.dataset.url") + "" + dato.get("api.tenant") + "/");

		rsp.then().statusCode(dato.getInt("api.httpStatusExcepted"));

		if (StringUtils.isNotEmpty(dato.optString("api.errorCode")))
			rsp.then().body("error_code", Matchers.equalTo(dato.optString("api.errorCode")));
	}

}
