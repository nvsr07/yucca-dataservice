package org.csi.yucca.controller;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.Iterator;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class PublicControllerTest extends TestBase {

	@BeforeClass
	public void setUpSecretObject() throws IOException {
		super.setUpSecretObject("/testSecret.json");
	}	
	
	@DataProvider(name="json")
	public Iterator<Object[]> getFromJson(){  
		return super.getFromJson(
			     "/PublicController_loadEcosystems_dataIn.json",
				 "/PublicController_loadDatasetTypes_dataIn.json", 
				 "/PublicController_datasetSubtypes_dataIn.json",
				 "/PublicController_loadSupplyTypes_dataIn.json",
				 "/PublicController_loadSoTypes_dataIn.json",
				 "/PublicController_loadSoCategories_dataIn.json", 
				 "/PublicController_loadLocationTypes_dataIn.json", 
				 "/PublicController_loadExposureTypes_dataIn.json", 
				 "/PublicController_loadPhenomenons_dataIn.json",
				 "/PublicController_loadMeasureUnit_dataIn.json",
				 "/PublicController_loadDataTypes_dataIn.json",
				 "/PublicController_loadTags_dataIn.json",
				 "/PublicController_loadDomains_dataIn.json",
			     "/PublicController_loadSubDomains_dataIn.json",
			     "/PublicController_loadOrganizations_dataIn.json",
			     "/PublicController_loadLicenses_dataIn.json"
				 );
	}	
	
	@Test(dataProvider="json")
	public void prova(JSONObject dato) {

		RequestSpecification rs = given();

		Integer idEcosystem    = postEchosystem( "eco111", dato);
		Integer idTag          = postTag(idEcosystem, dato);
		Integer idDomain       = postDomain(idEcosystem, "newDomainCode11111", dato);
        Integer idSubdomain    = postSubdomain(idDomain, dato);		
		Integer idOrganization = postOrganization(idEcosystem, dato);
		
		
		Response rsp = rs.when().get(makeUrl(dato,"json"));
		
		ValidatableResponse response = rsp.then().assertThat().statusCode(Matchers.equalTo(dato.get("expected.httpStatus.response")));
		
		if(!dato.optString("expected.errorName").isEmpty()){
			response.assertThat().body("errorName", Matchers.equalTo(dato.get("expected.errorName")));
		}

		
		deleteOrganization(idOrganization, dato);
		deleteSubdomain(idSubdomain, dato);
		deleteDomain(idDomain, dato);
		deleteTag(idTag, dato);
		deleteEcosystem(idEcosystem, dato);
	}
	
	
	private Integer postEchosystem(String ecosystemCode, JSONObject dato){
		
		String url = getUrl("backoffice", "ecosystems", dato).toString();
		
		String message = "{\"ecosystemcode\":\"" + ecosystemCode + "\",\"description\":\"" + ecosystemCode + "_description\"}";
		
		return postMessage(url, message, "idEcosystem");			
	}
	
	private void deleteEcosystem(Integer idEcosystem, JSONObject dato){
		String url = getUrl("backoffice", "ecosystems", dato).append("/").append(idEcosystem).toString();
		given().when().contentType(ContentType.JSON).delete(url);
	}

	private void deleteDomain(Integer idDomain, JSONObject dato){
		String url = getUrl("backoffice", "domains", dato).append("/").append(idDomain).toString();
		given().when().contentType(ContentType.JSON).delete(url);
	}
	
	private Integer postTag(Integer idEcosystem, JSONObject dato){
		String url = getUrl("backoffice", "tags", dato).toString();
		String message = "{\"tagcode\": \"newTagCode1111\",\"langit\": \"new-tag-it_1\",\"langen\": \"new-tag-en_1\",\"idEcosystem\": " + idEcosystem + "}";
		return postMessage(url, message, "idTag");			
	}

	private void deleteTag(Integer idTag, JSONObject dato){
		String url = getUrl("backoffice", "tags", dato).append("/").append(idTag).toString();
		given().when().contentType(ContentType.JSON).delete(url);
	}

	private void deleteSubdomain(Integer idSubdomain, JSONObject dato){
		String url = getUrl("backoffice", "subdomains", dato).append("/").append(idSubdomain).toString();
		given().when().contentType(ContentType.JSON).delete(url);
	}
	
	private Integer postDomain(Integer idEcosystem, String domainCode, JSONObject dato){
		String url = getUrl("backoffice", "domains", dato).toString();
		String message = "{\"langen\": \"newDomain_en_3333\",\"langit\": \"new-domain_it_3333\",\"domaincode\": \"" + domainCode + "\",\"deprecated\": 1,\"ecosystemCodeList\":[" + idEcosystem + "]}";
		return postMessage(url, message, "idDomain");
	}	
	
	private Integer postSubdomain(Integer idDomain, JSONObject dato){
		String url = getUrl("backoffice", "subdomains", dato).toString();
		String message = "{\"subdomaincode\": \"code11111\",\"langIt\": \"NEW_SUBDOMAIN_1_LANG_IT\",\"langEn\": \"NEW_SUBDOMAIN_1_LANG_EN\",\"idDomain\": "+ idDomain+ "}";
		return postMessage(url, message, "idSubdomain");			
	}
	
	private Integer postOrganization(Integer idEcosystem, JSONObject dato){
		String url = getUrl("backoffice", "organizations", dato).toString();
		String message = "{\"organizationcode\": \"codeOrg1111\",\"description\": \"DESC TRIAL0041\",\"ecosystemCodeList\":[" + idEcosystem + "]}";
		return postMessage(url, message, "idOrganization");			
	}

	private void deleteOrganization(Integer idOrganization, JSONObject dato){
		String url = getUrl("backoffice", "organizations", dato).append("/").append(idOrganization).toString();
		given().when().contentType(ContentType.JSON).delete(url);
	}

	
	
	
	
}
