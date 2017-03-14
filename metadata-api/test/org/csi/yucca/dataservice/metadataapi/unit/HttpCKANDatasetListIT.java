package org.csi.yucca.dataservice.metadataapi.unit;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.Iterator;

import org.apache.http.HttpStatus;
import org.csi.yucca.dataservice.metadataapi.test.RestBase;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class HttpCKANDatasetListIT extends RestBase {

	@BeforeClass
	public void setUpSecretObject() throws IOException {
		super.setUpSecretObject("/testSecret.json");
	}
	
	@DataProvider(name = "ckanTest")
	public Iterator<Object[]> getFromJson() {
		return super.getFromJson("/ckanTest.json");
	}
	
	@Test(dataProvider = "ckanTest")
	public void dcatDatasetTesting(JSONObject dato) {
		/*
			if (dato.optBoolean("rt.toskip") || dato.optBoolean("rt.httptoskip"))
				throw new SkipException("TODO in future version");
			//RequestSpecification rs = given().body(dato.get("dmapi.message")).contentType(ContentType.JSON);
		*/
		
		RequestSpecification rs = given().contentType(ContentType.JSON);

		rs.basePath(dato.getString("ckanlist.url"));
		if (dato.optString("ckan.tenant")!=null)
			rs.param("tenant", dato.optString("ckan.tenant"));
		
			
		Response rsp = rs.log().all().when().get("");
		rsp.then().statusCode(HttpStatus.SC_OK);
		
		if (dato.optInt("ckan.size")==0)
		{
			rsp.then().assertThat().body(equalToIgnoringWhiteSpace("[]"));
		}

		/*
		 if (StringUtils.isNotEmpty(dato.optString("rt.errorCode")))
			rsp.then().body("error_code", Matchers.equalTo(dato.optString("rt.errorCode")));
		*/
	}
}