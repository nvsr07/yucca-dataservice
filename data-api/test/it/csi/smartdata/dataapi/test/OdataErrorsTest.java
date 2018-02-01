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

public class OdataErrorsTest extends OdataTestBase{

	
	//OdataRecordContentTest
	@BeforeClass
	public void setUpSecretObject() throws IOException {
		super.setUpSecretObject("/testSecret.json");
	}	

	@DataProvider(name="json")
	public Iterator<Object[]> getFromJson()
	{
		return super.getFromJson("/OdataErrorsTest_dataIn.json");
	}
	
	
	@Test(dataProvider="json")
	public void odataCheckOrderedContent(JSONObject dato) {

		//verifica unicamente il conteggio totale dei record
		
		RequestSpecification rs =  given();

		if (StringUtils.isNotEmpty(dato.optString("odata.username")))
		{
			rs = rs.auth().basic(dato.getString("odata.username"), dato.getString("odata.password"));
		}	 


		
		
		
		Response rsp = rs.when().get(makeUrl(dato,"json"));

		rsp.then().assertThat().body("error.code",  (StringUtils.isEmpty(dato.getString("odata.retdata.errorCode"))  ? Matchers.nullValue() :  Matchers.equalTo( dato.getString("odata.retdata.errorCode") )) );
		rsp.then().assertThat().body("error.message.value",  (StringUtils.isEmpty(dato.getString("odata.retdata.errorMessage"))  ? Matchers.nullValue() :  Matchers.startsWith( dato.getString("odata.retdata.errorMessage") )) );
		//rsp.then().assertThat().body("error.code",  Matchers.equalTo(  (dato.get("odata.retdata.errorCode") == null ?   ));
		//rsp.then().assertThat().body("error.message.value",  Matchers.startsWith(dato.getString("odata.retdata.errorMessage")));
		
		
		
		
		
		
	}	

}
