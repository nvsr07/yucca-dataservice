package it.csi.smartdata.dataapi.test;

import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.config.XmlConfig;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OdataMetadataTest extends OdataTestBase{
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
	public void odataCheckMetadataRoot(JSONObject dato) {

		//verifica unicamente il conteggio totale dei record
		
		XmlConfig xmlConfig =new XmlConfig();
		
		RequestSpecification rs =  given().config(RestAssured.config().xmlConfig(xmlConfig.declareNamespace("edmx", "http://schemas.microsoft.com/ado/2007/06/edmx")));

		if (StringUtils.isNotEmpty(dato.optString("odata.username")))
		{
			rs = rs.auth().basic(dato.getString("odata.username"), dato.getString("odata.password"));
		}	 


		rs.urlEncodingEnabled(false);
		Response rsp = rs.when().get(makeUrl(dato,null)+"$metadata");

		
		
		//rsp.then().assertThat().body(RestAssuredMatchers.matchesXsd(readFile("/schema001.xsd")));
		

		if (dato.getString("odata.apiType").equals("Measures")) {


			
			
			//Entity Misure
			rsp.then().assertThat().body("Edmx.DataServices.Schema.EntityType.find{it.@Name == 'Measure'}.Key.PropertyRef.@Name" , Matchers.equalTo("internalId") );
			//Entity MisureStats
			rsp.then().assertThat().body("Edmx.DataServices.Schema.EntityType.find{it.@Name == 'MeasureStat'}.Key.PropertyRef.find{it.@Name == 'year'}.@Name" ,Matchers.equalTo("year") );
			rsp.then().assertThat().body("Edmx.DataServices.Schema.EntityType.find{it.@Name == 'MeasureStat'}.Key.PropertyRef.find{it.@Name == 'month'}.@Name" ,Matchers.equalTo("month") );
			rsp.then().assertThat().body("Edmx.DataServices.Schema.EntityType.find{it.@Name == 'MeasureStat'}.Key.PropertyRef.find{it.@Name == 'dayofmonth'}.@Name" ,Matchers.equalTo("dayofmonth") );
			rsp.then().assertThat().body("Edmx.DataServices.Schema.EntityType.find{it.@Name == 'MeasureStat'}.Key.PropertyRef.find{it.@Name == 'hour'}.@Name" ,Matchers.equalTo("hour") );
			rsp.then().assertThat().body("Edmx.DataServices.Schema.EntityType.find{it.@Name == 'MeasureStat'}.Key.PropertyRef.find{it.@Name == 'minute'}.@Name" ,Matchers.equalTo("minute") );
			rsp.then().assertThat().body("Edmx.DataServices.Schema.EntityType.find{it.@Name == 'MeasureStat'}.Key.PropertyRef.find{it.@Name == 'dayofweek'}.@Name" ,Matchers.equalTo("dayofweek") );
			
			
			
			//EntitySet Misure
			rsp.then().assertThat().body("Edmx.DataServices.Schema.EntityContainer.EntitySet.find{it.@Name == 'Measures'}.@Name" , Matchers.equalTo("Measures") );

			//EntitySet MisureStat
			rsp.then().assertThat().body("Edmx.DataServices.Schema.EntityContainer.EntitySet.find{it.@Name == 'MeasuresStats'}.@Name" , Matchers.equalTo("MeasuresStats") );
			
			
			
			JSONArray arrAtteso=(JSONArray)dato.get("odata.retdata.Measures.fileds");
			for (int i = 0 ; i<arrAtteso.length();i++) {
				
				JSONObject current=arrAtteso.getJSONObject(i);
				rsp.then().assertThat().body("Edmx.DataServices.Schema.EntityType.find{it.@Name == 'Measure'}.Property.find{it.@Name == '"+current.getString("Name")+"'}.@Type" , Matchers.equalTo(current.getString("Type")) );
				rsp.then().assertThat().body("Edmx.DataServices.Schema.EntityType.find{it.@Name == 'Measure'}.Property.find{it.@Name == '"+current.getString("Name")+"'}.@Nullable" , Matchers.equalTo(current.getString("Nullable")) );
				
				
			}
			
			arrAtteso=(JSONArray)dato.get("odata.retdata.MeasuresStats.fileds");
			for (int i = 0 ; i<arrAtteso.length();i++) {
				
				JSONObject current=arrAtteso.getJSONObject(i);
				rsp.then().assertThat().body("Edmx.DataServices.Schema.EntityType.find{it.@Name == 'MeasureStat'}.Property.find{it.@Name == '"+current.getString("Name")+"'}.@Type" , Matchers.equalTo(current.getString("Type")) );
				rsp.then().assertThat().body("Edmx.DataServices.Schema.EntityType.find{it.@Name == 'MeasureStat'}.Property.find{it.@Name == '"+current.getString("Name")+"'}.@Nullable" , Matchers.equalTo(current.getString("Nullable")) );
				
				
			}
			
			
		}
		
		
		
		
		
		
		
	}	

}
