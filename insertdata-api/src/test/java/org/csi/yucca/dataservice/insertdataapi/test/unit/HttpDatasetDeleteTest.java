package org.csi.yucca.dataservice.insertdataapi.test.unit;

import static io.restassured.RestAssured.given;
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

public class HttpDatasetDeleteTest extends RestBase {

	@BeforeClass
	public void setUpSecretObject() throws IOException {
		super.setUpSecretObject("/testSecret.json");
	}
	
	@DataProvider(name = "ValidationDatasetDeleteTest")
	public Iterator<Object[]> getFromJson() {

		return super.getFromJson("/ValidationDatasetDeleteTest.json");
		
	}

	
	@Test(dataProvider = "ValidationDatasetDeleteTest")
	public void sendHTTPStatusErrorCodeTesting(JSONObject dato) {
		if (dato.optBoolean("api.toskip") || dato.optBoolean("api.httptoskip"))
				throw new SkipException("TODO in future version");
		
		RequestSpecification rs = given();//.body(dato.get("api.message")).contentType(ContentType.JSON);

//int-sdnet-intapi.sdp.csi.it:90/insertdataapi/dataset/delete/tst_csp/697/2
//int-sdnet-gw1.sdp.csi.it:8282/insert/dataset/input/",
		
		Response rsp = rs.when().log().all().delete(dato.get("api.dataset.delete.url") + "" + dato.get("api.tenant") + "/" + dato.get("api.datasetId")+ "/" + dato.get("api.datasetVersion"));

		rsp.then().log().all().statusCode(dato.getInt("api.httpStatusExcepted"));

		if (StringUtils.isNotEmpty(dato.optString("api.errorCode")))
			rsp.then().body("error_code", Matchers.equalTo(dato.optString("api.errorCode")));
		
		if (dato.opt("api.dataDeleted")!=null)
			rsp.then().body("dataDeleted", Matchers.equalTo(dato.optBoolean("api.dataDeleted")));
		if (dato.opt("api.indexDeleted")!=null)
			rsp.then().body("indexDeleted", Matchers.equalTo(dato.optBoolean("api.indexDeleted")));

	}

	

}
