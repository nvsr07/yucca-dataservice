package org.csi.yucca.dataservice.metadataapi.unit;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.Iterator;

import org.apache.http.HttpStatus;
import org.csi.yucca.dataservice.metadataapi.test.RestBase;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpDCATDatasetListIT extends RestBase {

	@BeforeClass
	public void setUpSecretObject() throws IOException {
		super.setUpSecretObject("/testSecret.json");
	}
	
	@DataProvider(name = "dcatTest")
	public Iterator<Object[]> getFromJson() {
		return super.getFromJson("/dcatTest.json");
	}
	
	@Test(dataProvider = "dcatTest")
	public void dcatDatasetTesting(JSONObject dato) {
		/*
			if (dato.optBoolean("rt.toskip") || dato.optBoolean("rt.httptoskip"))
				throw new SkipException("TODO in future version");
			//RequestSpecification rs = given().body(dato.get("dmapi.message")).contentType(ContentType.JSON);
		*/
		
		RequestSpecification rs = given().contentType(ContentType.JSON);

		Response rsp = rs.log().all().when().get(dato.getString("dcat.url") + dato.getString("dcat.page"));
		rsp.then().statusCode(HttpStatus.SC_OK);
		
		rsp.then().log().all().body("dataset", Matchers.arrayWithSize(dato.getInt("dcat.size")));

		/*
		 if (StringUtils.isNotEmpty(dato.optString("rt.errorCode")))
			rsp.then().body("error_code", Matchers.equalTo(dato.optString("rt.errorCode")));
		*/
	}
}