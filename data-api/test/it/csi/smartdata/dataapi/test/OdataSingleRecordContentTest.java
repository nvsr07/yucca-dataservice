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

public class OdataSingleRecordContentTest extends OdataTestBase{

	
	//OdataRecordContentTest
	@BeforeClass
	public void setUpSecretObject() throws IOException {
		super.setUpSecretObject("/testSecret.json");
	}	

	@DataProvider(name="json")
	public Iterator<Object[]> getFromJson()
	{
		return super.getFromJson("/OdataSingleRecordContentTest_dataIn.json");
	}
	
	
	@Test(dataProvider="json")
	public void odataCheckOrderedContent(JSONObject dato) {

		//verifica unicamente il conteggio totale dei record
		
		RequestSpecification rs =  given();

		if (StringUtils.isNotEmpty(dato.optString("odata.username")))
		{
			rs = rs.auth().basic(dato.getString("odata.username"), dato.getString("odata.password"));
		}	 

		rs.urlEncodingEnabled(false);
		Response rsp = rs.when().get(makeUrl(dato,"json"));
		JSONArray arrAtteso=(JSONArray)dato.get("odata.retdata.dataRecords");
		JSONArray campi=(JSONArray)dato.get("odata.fieldstocheck");
		
		
		//rsp.then().assertThat().body("d.results.size()",  is(dato.getInt("odata.retdata.resultCount")));
		
		for (int i = 0 ; i<arrAtteso.length();i++) {
			for (int j=0;j<campi.length();j++) {
				String campo=campi.getString(j);
				rsp.then().assertThat().body("d."+campo,  Matchers.equalTo(arrAtteso.getJSONObject(i).get(campo)));
				
				
			}
		}
		
		
		
		
	}	

}
