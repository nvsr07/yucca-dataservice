package it.csi.smartdata.dataapi.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
public class ProvaTest extends it.csi.smartdata.dataapi.test.OdataTestBase {

	@BeforeClass
	public void setUpSecretObject() throws IOException {
		super.setUpSecretObject("/testSecret.json");
	}	

	@DataProvider(name="json")
	public Iterator<Object[]> getFromJson()
	{
		return super.getFromJson("/dataIn.json");
	}

	
	
	
	@Test(dataProvider="json")
	public void odataCheckIdsAndorder(JSONObject dato) {

		RequestSpecification rs =  given();

		if (StringUtils.isNotEmpty(dato.optString("odata.username")))
		{
			rs = rs.auth().basic(dato.getString("odata.username"), dato.getString("odata.password"));
		}	 

		
		Response rsp = rs.when().get(makeUrl(dato,"json"));

		
		JSONArray arrAtteso=(JSONArray)dato.get("odata.retdata.dataRecords");

		rsp.then().assertThat().body("d.results.size()",  is(arrAtteso.length()));
		rsp.then().assertThat().body("d.__count",  Matchers.equalTo(dato.getString("odata.retdata.totalCount")));
		
		
		for (int i = 0 ; i<arrAtteso.length();i++) {
			rsp.then().assertThat().body("d.results["+i+"].internalId",  Matchers.equalTo(arrAtteso.getJSONObject(i).getString("internalId")));
		}
		
		
		
	}

	@Test(dataProvider="json")
	public void odataCheckTotalCount(JSONObject dato) {

		RequestSpecification rs =  given();

		if (StringUtils.isNotEmpty(dato.optString("odata.username")))
		{
			rs = rs.auth().basic(dato.getString("odata.username"), dato.getString("odata.password"));
		}	 

		String url=makeUrl(dato,"json");
		System.out.println(url);
		Response rsp = rs.when().get(url);

		
		rsp.then().assertThat().body("d.__count",  Matchers.equalTo(dato.getString("odata.retdata.totalCount")));
		
		
		
	}



	
	

}
