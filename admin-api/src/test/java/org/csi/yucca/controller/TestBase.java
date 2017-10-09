package org.csi.yucca.controller;

import static io.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestBase {

	public static final int VALUE_VERSION = 146;
	public static final String ECOSYSTEM_CODE_TEST_VALUE              = "eco00" + VALUE_VERSION;
	public static final String ORGANIZATION_CODE_TEST_VALUE           = "org00" + VALUE_VERSION;
	public static final String DOMAIN_CODE_TEST_VALUE                 = "dom00" + VALUE_VERSION;
	public static final String SMARTOBJECT_CODE_TEST_VALUE            = "soc00" + VALUE_VERSION;
	public static final String SMARTOBJECT_CODE_TEST_VALUE_NO_DEVICE  = "SOC00" + VALUE_VERSION;
	public static final String SUBDOMAIN_CODE_TEST_VALUE              = "sub00" + VALUE_VERSION;
	public static final String TAG_CODE_TEST_VALUE                    = "tag00" + VALUE_VERSION;
	public static final String TENANT_CODE_TEST_VALUE                 = "ten00" + VALUE_VERSION;
	public static final String TWTUSERNAME_TEST_VALUE                 = "twtusn00" + VALUE_VERSION;
	public static final String SLUG_TEST_VALUE                        = "slg00" + VALUE_VERSION;
	
	public static final String JSON_KEY_UPDATE_ERROR_NAME           = "expected.update-errorName";
	public static final String JSON_KEY_UPDATE_RESPONSE             = "expected.httpStatus.update-response";
	public static final String JSON_KEY_MESSAGE_UPDATE              = "adminapi.message.update";
	public static final String JSON_KEY_MESSAGE                     = "adminapi.message";
	public static final String JSON_KEY_EXPECTED_ERROR_NAME         = "expected.errorName";
	public static final String JSON_KEY_UPDATE_MESSAGE              = "adminapi.update-message";
	public static final String JSON_KEY_EXPECTED_HTTP_STATUS        = "expected.httpStatus.response";
	public static final String JSON_KEY_EXPECTED_HTTP_STATUS_DELETE = "expected.httpStatus.delete-response";
	public static final String JSON_KEY_ID_GENERATED                = "adminapi.id-generated";
	public static final String JSON_KEY_APICODE                     = "adminapi.apicode"; 
	public static final String JSON_KEY_ENTITY_SET                  = "adminapi.entityset"; 
	public static final String JSON_KEY_ID_SO_TYPE                  = "adminapi.idSoType";
	public static final String JSON_KEY_SOCODE                      = "adminapi.socode";
	public static final String JSON_KEY_TWTUSERNAME                 = "adminapi.twtusername";
	public static final String JSON_KEY_ID_TENANT                   = "adminapi.idTenant";

	private static final String KEY_URL = "url";
	private static final String KEY_VERSION = "version";
	private static final String KEY_APICODE = "apicode";
	private static final String KEY_ENTITYSET = "entityset";
	private static final String KEY_FORMAT = "$format";
	private static final String APP_NAME = "adminapi";
	private static final String STRING_TYPE = "String";
	private static final String INT_TYPE = "Int";
	private static final Map<String, String> KEY_TYPE;
	
	private Integer idEcosystem;
	private Integer idTag;
	private Integer idDomain;
	private Integer idSubdomain;		
	private Integer idOrganization;
	private Integer idTenant;
	private String socode;
	private Integer idSmartObject;
	
	protected JSONObject secretObject = new JSONObject();
	protected JSONObject jsonObject = null;

	static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("sort",             STRING_TYPE);
        aMap.put("ecosystemCode",    STRING_TYPE);
        aMap.put("lang",             STRING_TYPE);
        aMap.put("organizationCode", STRING_TYPE);
        aMap.put("domainCode",       STRING_TYPE);
        aMap.put("datasetTypeCode",  STRING_TYPE);
        
        KEY_TYPE = Collections.unmodifiableMap(aMap);
    }
	
	private void reset(){
		this.jsonObject = null;
	}
	
	protected void init(JSONObject dato){
		this.idEcosystem    = postEchosystem(dato);
		this.idTag          = postTag(idEcosystem, dato);
		this.idDomain       = postDomain(idEcosystem, dato);
		this.idSubdomain    = postSubdomain(idDomain, dato);		
		this.idOrganization = postOrganization(idEcosystem, dato);
		this.idTenant       = postTenant(idEcosystem, idOrganization, dato);
	}

	protected void initAll(JSONObject dato){
		this.idEcosystem    = postEchosystem(dato);
		this.idTag          = postTag(idEcosystem, dato);
		this.idDomain       = postDomain(idEcosystem, dato);
		this.idSubdomain    = postSubdomain(idDomain, dato);		
		this.idOrganization = postOrganization(idEcosystem, dato);
		this.idTenant       = postTenant(idEcosystem, idOrganization, dato);
		this.idSmartObject  = postSmartObject(dato);
	}
	
	protected void resetAll(JSONObject dato){
		deleteSmartobject(dato);
		reset(dato);
	}
	
//	protected void reset(JSONObject dato){
//		deleteTenant(dato);
//		deleteOrganization(this.idOrganization, dato);
//		deleteSubdomain(this.idSubdomain, dato);
//		deleteDomain(this.idDomain, dato);
//		deleteTag(this.idTag, dato);
//		deleteEcosystem(this.idEcosystem, dato);
//	}
	protected void reset(JSONObject dato){
		deleteTenant(dato);
		deleteOrganization(this.idOrganization, dato);
		deleteSubdomain(this.idSubdomain, dato);
		deleteDomain(this.idDomain, dato);
		deleteTag(this.idTag, dato);
		deleteEcosystem(this.idEcosystem, dato);
	}
	
	/**
	 * 
	 * @param jsonObject
	 * @param format
	 * @return
	 */
    protected String makeUrl(JSONObject jsonObject, String format) {

    	setJsonObject(jsonObject);

    	StringBuilder url = new StringBuilder();

    	addApi(url);
    	addParameters(url);
    	addFormat(url, format);
    	
    	reset();
    	
    	return url.toString();
	}
    
	protected String getUrl(JSONObject dato){
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(dato.get("adminapi.url"))
		.append("/")
		.append(dato.get("adminapi.version"))
		.append("/");
		
		return urlBuilder.toString();
	}

	protected StringBuilder getUrl(String apiCode, String entitySet, JSONObject dato){
		StringBuilder builder = new StringBuilder();
		builder.append(getUrl(dato)).append(apiCode).append("/").append(entitySet);
		
		return builder;
	}
    
    private void setJsonObject(JSONObject jsonObject){
    	if(this.jsonObject == null){
    		this.jsonObject = jsonObject;
    	}
    }
    
    private void addFormat(StringBuilder url, String format){
		if (null!=format) {
			appendAndOrQuestion(url);
			url.append(KEY_FORMAT).append("=").append(format);
		}
    }
    
    private void addApi(StringBuilder url){
    	url.append(val(KEY_URL)).append("/")
 	   .append(val(KEY_VERSION)).append("/")
 	   .append(val(KEY_APICODE)).append("/")
 	   .append(val(KEY_ENTITYSET));
    }
    
    private void addParameters(StringBuilder url){
    	for(Map.Entry<String, String> entry : KEY_TYPE.entrySet()) {
    	    String key  = entry.getKey();
    	    String type = entry.getValue();
    	    
    	    if(STRING_TYPE.equals(type)){
    	    	addStringParameter(key, url);
    	    }
    	    else if(INT_TYPE.equals(type)){
    	    	addIntParameter(key, url);
    	    }
    	}
    }
    
	private Object val(String key){
		return this.jsonObject.get(APP_NAME + "." + key);
	}
	
	private void addStringParameter(String key, StringBuilder url){
		try {
			if( StringUtils.isNotEmpty((String)val(key))){
				appendParameter(key, url);
			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void addIntParameter(String key, StringBuilder url){
		try {
			if( (Integer)val(key) > 0){
				appendParameter(key, url);
			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void appendAndOrQuestion(StringBuilder url){
		if(url.toString().contains("?")){
			url.append("&");
		}
		else{
			url.append("?");
		}
	}
	
	private void appendParameter(String key, StringBuilder url){
		appendAndOrQuestion(url);		
		url.append(key).append("=").append(val(key));
	}
	
	public void setUpSecretObject(String file) throws IOException {
		String str = readFile(file);
		secretObject=  new JSONObject(str);
	}	
	

	public Iterator<Object[]> getFromJson(String...listFile){
		
		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		for (String fileName : listFile) {

			String str = readFile(fileName);
			
			JSONObject json =  new JSONObject(str);
			
			JSONArray jsArray = json.getJSONArray("data");


			for (int i = 0; i < jsArray.length(); i++) {
				JSONObject arr = jsArray.getJSONObject(i);
				addData(data, arr);	
			}
			
		}
		
		return data.iterator();
	}

	private void addData(ArrayList<Object[]> data, JSONObject arr){
		
		@SuppressWarnings("rawtypes")
		Iterator iterSecret = secretObject.keys();
		
		String tmp_key;
		
		while(iterSecret.hasNext()) {
		
			tmp_key = (String) iterSecret.next();
			
			if (!arr.has(tmp_key)){
				arr.put(tmp_key, secretObject.get(tmp_key));
			}
		}
		
		data.add(new Object[]{arr});		
	}
	
	
	public Iterator<Object[]> getFromJson_old(String file){
		
		ArrayList<Object[]> data = new ArrayList<Object[]>();

		String str = readFile(file);
		JSONObject json =  new JSONObject(str);
		JSONArray jsArray = json.getJSONArray("data");

		for (int i = 0; i < jsArray.length(); i++) {
			JSONObject arr = jsArray.getJSONObject(i);

			// merge with secret

			@SuppressWarnings("rawtypes")
			Iterator iterSecret = secretObject.keys();
			String tmp_key;
			while(iterSecret.hasNext()) {
				tmp_key = (String) iterSecret.next();
				if (!arr.has(tmp_key))
				{
					arr.put(tmp_key, secretObject.get(tmp_key));
				}
			}


			data.add(new Object[]{arr});
		}

		return data.iterator();
	}
	
	
	protected String readFile(String file)
	{
		String jsonData = "";
		BufferedReader br = null;
		try {
			String line;
			InputStream inputStream = this.getClass().getResourceAsStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			br = new BufferedReader(inputStreamReader);
			while ((line = br.readLine()) != null) {
				jsonData += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return jsonData;
	}	
	
	protected Integer postMessage(String url, String message, String idName){
		RequestSpecification requestSpecification = given().body(message).contentType(ContentType.JSON);
		Response response = requestSpecification.when().post(url);
		
		Integer id =  response.then().extract().path(idName);
		return id;
	}
	
	protected void deleteEcosystem(Integer idEcosystem, JSONObject dato){
		String url = getUrl("backoffice", "ecosystems", dato).append("/").append(idEcosystem).toString();
		given().when().contentType(ContentType.JSON).delete(url);
	}

	protected void deleteDomain(Integer idDomain, JSONObject dato){
		String url = getUrl("backoffice", "domains", dato).append("/").append(idDomain).toString();
		given().when().contentType(ContentType.JSON).delete(url);
	}
	
	protected Integer postTag(Integer idEcosystem, JSONObject dato){
		String url = getUrl("backoffice", "tags", dato).toString();
		String message = "{\"tagcode\": \"" + TAG_CODE_TEST_VALUE + "\",\"langit\": \"new-tag-it_1\",\"langen\": \"new-tag-en_1\",\"idEcosystem\": " + idEcosystem + "}";
		return postMessage(url, message, "idTag");			
	}

	protected void deleteTag(Integer idTag, JSONObject dato){
		String url = getUrl("backoffice", "tags", dato).append("/").append(idTag).toString();
		given().when().contentType(ContentType.JSON).delete(url);
	}

	protected void deleteSubdomain(Integer idSubdomain, JSONObject dato){
		String url = getUrl("backoffice", "subdomains", dato).append("/").append(idSubdomain).toString();
		given().when().contentType(ContentType.JSON).delete(url);
	}

	private void deleteSmartobject(JSONObject dato){
		String url = getUrl("management", "organizations", dato).append("/").append(ORGANIZATION_CODE_TEST_VALUE).append("/smartobjects/") + getSocode().toString();
		given().when().contentType(ContentType.JSON).delete(url);
	}
	
	public void deleteSmartobject(JSONObject dato, String socode, String organizationCode){
		String url = getUrl("management", "organizations", dato).append("/").append(organizationCode).append("/smartobjects/") + socode.toString();
		given().when().contentType(ContentType.JSON).delete(url);
	}
	
	protected Integer postDomain(Integer idEcosystem, JSONObject dato){
		String url = getUrl("backoffice", "domains", dato).toString();
		String message = "{\"langen\": \"newDomain_en_3333\",\"langit\": \"new-domain_it_3333\",\"domaincode\": \"" + DOMAIN_CODE_TEST_VALUE + "\",\"deprecated\": 1,\"ecosystemCodeList\":[" + idEcosystem + "]}";
		return postMessage(url, message, "idDomain");
	}	
	
	protected Integer postSmartObject(JSONObject dato){
		
		String url = getUrl("management", "organizations", dato)
				.append("/")
				.append(ORGANIZATION_CODE_TEST_VALUE).append("/smartobjects")				
				.toString();
		
		setSocode(SMARTOBJECT_CODE_TEST_VALUE_NO_DEVICE);
		
		String message = " { " +
				" \"twtusername\" : \"" + TWTUSERNAME_TEST_VALUE + "\", " +
				" \"slug\" : \"" + SLUG_TEST_VALUE + "\", " +
				" \"socode\" : \"" + SMARTOBJECT_CODE_TEST_VALUE_NO_DEVICE + "\", " +
				" \"positions\" :  " +
				" { " +
					" \"lat\":0, " + 
					" \"lon\":7511974, " +
					" \"elevation\":247, " +
					" \"room\": 22, " +
					" \"building\":\"CSI PIEM\", " +
					" \"floor\":33, " +
					" \"address\":\"Corso Unione\", " +
					" \"city\":\"Turin\", " +
					" \"country\":\"Piemonte\", " +
					" \"placegeometry\":\"aaa\" " +
				" }, " +
				" \"name\" : \"name_1\", " +
				" \"description\" : \"description\", " +
				" \"urladmin\" : \"urladmin\", " +
				" \"fbcoperationfeedback\" : \"fbcoperationfeedback\", " +
				" \"swclientversion\" : \"swclie\", " +
				" \"version\" : 1, " +
				" \"model\" : \"model\", " +
				" \"deploymentversion\" : 1, " +
				" \"sostatus\" : \"sostatus\", " +
				" \"twtmaxsearchnumber\" : 1, " +
				" \"twtmaxsearchinterval\" : 1, " +
				" \"twtusertoken\" : \"twtusertoken\", " +
				" \"twttokensecret\" : \"twttokensecret\", " +
				" \"twtname\" : \"twtname\", " +
				" \"twtuserid\" : 1, " +
				" \"twtmaxstreams\" : 1, " +
				" \"idLocationType\" : 1, " +
				" \"idExposureType\" : 1, " +
				" \"idSupplyType\" : 1, " +
				" \"idSoCategory\" : 1, " +
				" \"idSoType\" : 3, " +
				" \"idStatus\" : 1, " +
				" \"idTenant\": "+ getIdTenant() + " " +
				" } ";
		return postMessage(url, message, "idSmartObject");
	}
	
	protected Integer postTenant(Integer idEcosystem, Integer idOrganization, JSONObject dato){
		String url = getUrl("backoffice", "tenants", dato).toString();
		
		String message =
		" \"tenantcode\":    \"" + TENANT_CODE_TEST_VALUE + "\", " +
		" \"name\":          \"tenantName012\", " +
		" \"usagedaysnumber\": 4, " +
		" \"username\": \"tenantUserName012\", " +
		" \"userfirstname\": \"tenantUserfirstname001\", " +
		" \"userlastname\": \"tenantUserlastname001\", " +
		" \"useremail\": \"useremail@test.it\", " +
		" \"usertypeauth\": \"admin\", " +
		" \"idTenantType\": 1, " +
		" \"idTenantStatus\": 1, " +
		" \"description\": \"tenant desc\" }"; 
		
		String message_old = 
		"{\"tenantcode\":\"" + TENANT_CODE_TEST_VALUE + "\",\"name\":\"tenantName002\",\"maxdatasetnum\": 3,"
		+ "\"maxstreamsnum\": 3,\"usagedaysnumber\": 4,\"username\": \"tenantUserName001\",\"userfirstname\": \"tenantUserfirstname001\","
		+ "\"userlastname\": \"tenantUserlastname001\",\"useremail\": \"useremail@test.it\",\"usertypeauth\":\"usertypeauth001\","
		+ "\"idTenantType\": 1,\"idTenantStatus\": 1}";
		message = "{\"idEcosystem\":"+idEcosystem+"," + "\"idOrganization\":"+idOrganization+ "," + message.substring(1);
		return postMessage(url, message, "idTenant");
	}	
	
	protected Integer postSubdomain(Integer idDomain, JSONObject dato){
		String url = getUrl("backoffice", "subdomains", dato).toString();
		String message = "{\"subdomaincode\": \"" + SUBDOMAIN_CODE_TEST_VALUE + "\",\"langIt\": \"NEW_SUBDOMAIN_1_LANG_IT\",\"langEn\": \"NEW_SUBDOMAIN_1_LANG_EN\",\"idDomain\": "+ idDomain+ "}";
		return postMessage(url, message, "idSubdomain");			
	}
	
	protected Integer postOrganization(Integer idEcosystem, JSONObject dato){
		String url = getUrl("backoffice", "organizations", dato).toString();
		String message = "{\"organizationcode\": \"" + ORGANIZATION_CODE_TEST_VALUE + "\",\"description\": \"DESC TRIAL0041\",\"ecosystemCodeList\":[" + idEcosystem + "],\"datasolrcollectionname\":\"DATA_SOLR_COLLECTION_NAME_1\",\"measuresolrcollectionname\":\"MEASURE_SOLR_COLLECTION_NAME_1\",\"mediasolrcollectionname\":\"MEDIA_SOLR_COLLECTION_NAME_1\",\"socialsolrcollectionname\":\"SOCIAL_SOLR_COLLECTION_NAME_1\",\"dataphoenixtablename\":\"DATA_PHOENIX_TABLE_NAME_1\",\"dataphoenixschemaname\":\"DATA_PHOENIX_SCHEMA_NAME_1\",\"measuresphoenixtablename\":\"MEASURES_PHOENIX_TABLE_NAME_1\",\"measuresphoenixschemaname\":\"MEASURES_PHOENIX_SCHEMA_NAME_1\",\"mediaphoenixtablename\":\"MEDIA_PHOENIX_TABLE_NAME_1\",\"mediaphoenixschemaname\":\"MEDIA_PHOENIX_SCHEMA_NAME_1\",\"socialphoenixtablename\":\"SOCIAL_PHOENIX_TABLE_NAME_1\",\"socialphoenixschemaname\":\"SOCIAL_PHOENIX_SCHEMA_NAME_1\"}";


		
		return postMessage(url, message, "idOrganization");			
	}

	protected void deleteOrganization(Integer idOrganization, JSONObject dato){
		String url = getUrl("backoffice", "organizations", dato).append("/").append(idOrganization).toString();
		given().when().contentType(ContentType.JSON).delete(url);
	}

	protected void deleteTenant(JSONObject dato){
		String url = getUrl("backoffice", "tenants", dato).append("/").append(TENANT_CODE_TEST_VALUE).toString();
		given().when().contentType(ContentType.JSON).delete(url);
	}

	protected Integer postEchosystem(JSONObject dato){
		String url = getUrl("backoffice", "ecosystems", dato).toString();
		String message = "{\"ecosystemcode\":\"" + ECOSYSTEM_CODE_TEST_VALUE + "\",\"description\":\"" + ECOSYSTEM_CODE_TEST_VALUE + "_description\"}";
		return postMessage(url, message, "idEcosystem");			
	}
	
	public Integer getIdEcosystem() {
		return idEcosystem;
	}

	public void setIdEcosystem(Integer idEcosystem) {
		this.idEcosystem = idEcosystem;
	}

	public Integer getIdTag() {
		return idTag;
	}

	public void setIdTag(Integer idTag) {
		this.idTag = idTag;
	}

	public Integer getIdDomain() {
		return idDomain;
	}

	public void setIdDomain(Integer idDomain) {
		this.idDomain = idDomain;
	}

	public Integer getIdSubdomain() {
		return idSubdomain;
	}

	public void setIdSubdomain(Integer idSubdomain) {
		this.idSubdomain = idSubdomain;
	}

	public Integer getIdOrganization() {
		return idOrganization;
	}

	public void setIdOrganization(Integer idOrganization) {
		this.idOrganization = idOrganization;
	}

	public Integer getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Integer idTenant) {
		this.idTenant = idTenant;
	}

	public String getSocode() {
		return socode;
	}

	public void setSocode(String socode) {
		this.socode = socode;
	}

	public Integer getIdSmartObject() {
		return idSmartObject;
	}

	public void setIdSmartObject(Integer idSmartObject) {
		this.idSmartObject = idSmartObject;
	}
	
}
