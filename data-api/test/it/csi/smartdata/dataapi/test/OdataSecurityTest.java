package it.csi.smartdata.dataapi.test;

import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.config.XmlConfig;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OdataSecurityTest extends OdataTestBase{


	@BeforeClass
	public void setUpSecretObject() throws IOException {
		super.setUpSecretObject("/testSecret.json");
	}	

	@DataProvider(name="json")
	public Iterator<Object[]> getFromJson()
	{
		return super.getFromJson("/OdataServiceRootSecurityTest_dataIn.json");
	}


	@Test(dataProvider="json")
	public void odataCheckServiceRootSecurity(JSONObject dato) {

		//verifica unicamente il conteggio totale dei record

		XmlConfig xmlConfig =new XmlConfig();
		
		
		xmlConfig.declaredNamespaces().put("atom", "http://www.w3.org/2005/Atom");
		xmlConfig.declaredNamespaces().put("ams", "http://wso2.org/apimanager/security");
		
		RequestSpecification rs =  given().config(RestAssured.config().xmlConfig(xmlConfig));

		if (StringUtils.isNotEmpty(dato.optString("odata.username")) && StringUtils.isNotEmpty(dato.optString("odata."+dato.optString("odata.username")+".user")))
		{
			rs = rs.auth().basic(dato.getString("odata."+dato.optString("odata.username")+".user"), dato.getString("odata."+dato.optString("odata.username")+".token"));
			ArrayList<Header> hl=new ArrayList<Header>();
			hl.add(new Header("Authorization", "Bearer "+dato.getString("odata."+dato.optString("odata.username")+".token")));
			Headers hs=new Headers(hl);
			rs.headers(hs);
			
		}	 



		Response rsp = rs.when().get(makeUrl(dato,null));


		if (dato.getString("odata.apiType").equals("Measures")) {
			//rsp.then().assertThat().body("service.workspace.collection.@href" , Matchers.hasItems(Matchers.equalTo(operand)"Measures","MeasuresStats") );
			//rsp.then().assertThat().body("service.workspace.collection.@href" , Matchers.hasItem(Matchers.equalTo("Measuresaa")) );
			rsp.then().assertThat().body("service.workspace.collection.@href" , Matchers.containsInAnyOrder("Measures","MeasuresStats") );
		} else if (dato.getString("odata.apiType").equals("DataEntities")) {
			rsp.then().assertThat().body("service.workspace.collection.@href" , Matchers.equalTo("DataEntities") );
		} else if (dato.getString("odata.apiType").equals("InvalidCredentials")) {
			rsp.then().assertThat().body("fault.message" , Matchers.equalTo("Invalid Credentials") );
		} else if (dato.getString("odata.apiType").equals("MissingCredentials")) {
			rsp.then().assertThat().body("fault.message" , Matchers.equalTo("Missing Credentials") );
		}



		



	}	



}	


