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

	protected JSONObject secretObject = new JSONObject();
	protected JSONObject jsonObject = null;

	private static final String KEY_URL = "url";
	private static final String KEY_VERSION = "version";
	private static final String KEY_APICODE = "apicode";
	private static final String KEY_ENTITYSET = "entityset";
	private static final String KEY_FORMAT = "$format";
	private static final String APP_NAME = "adminapi";

	private static final String STRING_TYPE = "String";
	private static final String INT_TYPE = "Int";

	private static final Map<String, String> KEY_TYPE;
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
	
}
