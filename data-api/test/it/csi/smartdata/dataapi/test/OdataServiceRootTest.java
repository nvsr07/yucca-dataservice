package it.csi.smartdata.dataapi.test;

import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.config.XmlConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsArrayContaining;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OdataServiceRootTest extends OdataTestBase{
	//OdataRecordContentTest
	@BeforeClass
	public void setUpSecretObject() throws IOException {
		super.setUpSecretObject("/testSecret.json");
	}	

	@DataProvider(name="json")
	public Iterator<Object[]> getFromJson()
	{
		return super.getFromJson("/OdataServiceRootTest_dataIn.json");
	}
	
	
	@Test(dataProvider="json")
	public void odataCheckServiceRoot(JSONObject dato) {

		//verifica unicamente il conteggio totale dei record
		
		XmlConfig xmlConfig =new XmlConfig();
		RequestSpecification rs =  given().config(RestAssured.config().xmlConfig(xmlConfig.declareNamespace("atom", "http://www.w3.org/2005/Atom")));

		if (StringUtils.isNotEmpty(dato.optString("odata.username")))
		{
			rs = rs.auth().basic(dato.getString("odata.username"), dato.getString("odata.password"));
		}	 


		
		Response rsp = rs.when().get(makeUrl(dato,null));


		if (dato.getString("odata.apiType").equals("Measures")) {
			//rsp.then().assertThat().body("service.workspace.collection.@href" , Matchers.hasItems(Matchers.equalTo(operand)"Measures","MeasuresStats") );
			//rsp.then().assertThat().body("service.workspace.collection.@href" , Matchers.hasItem(Matchers.equalTo("Measuresaa")) );
			rsp.then().assertThat().body("service.workspace.collection.@href" , Matchers.containsInAnyOrder("Measures","MeasuresStats") );
		}
		
		
		
		
		
		
		
	}	
}
