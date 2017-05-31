package it.csi.smartdata.dataapi.multiapi.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

public class SDPDataMultiApiConfig {


	public final static SDPDataMultiApiConfig instance= new SDPDataMultiApiConfig();


	private HashMap<String, String> params = new HashMap<String, String>();
		

	private SDPDataMultiApiConfig() {
		
		ResourceBundle rb= ResourceBundle.getBundle("SDPDataApiConfig");
		params = new HashMap<String, String>();
		params.put("MULTIAPI_EXTERNAL_ODATA_BASE_URL", rb.getString("MULTIAPI_EXTERNAL_ODATA_BASE_URL"));
		params.put("MULTIAPI_ODATA_BASE_URL", rb.getString("MULTIAPI_ODATA_BASE_URL"));

		params.put("MULTIAPI_NAME", rb.getString("MULTIAPI_NAME"));
		params.put("MULTIAPI_TOKEN", rb.getString("MULTIAPI_TOKEN"));
		params.put("MULTIAPI_DATASETS", rb.getString("MULTIAPI_DATASETS"));
		params.put("MULTIAPI_DATASET_SCHEMAS", rb.getString("MULTIAPI_DATASET_SCHEMAS"));
		
	}

	
    public String getMultiapiExternalOdataBaseUrl() {
		return params.get("MULTIAPI_EXTERNAL_ODATA_BASE_URL");
    }
    
    public String getMultiapiOdataBaseUrl() {
		return params.get("MULTIAPI_ODATA_BASE_URL");
    }
    
    public String getMultiapiName() {
		return params.get("MULTIAPI_NAME");
    }
    public String getMultiapiToken() {
		return params.get("MULTIAPI_TOKEN");
    }
    public List<String> getMultiapiDatasets() {
    	
    	List<String> ds = new ArrayList<>();
     	if (params.get("MULTIAPI_DATASETS")!=null)
     	{
     		ds = Arrays.asList(StringUtils.split( params.get("MULTIAPI_DATASETS"), ','));
     	}
		return ds;
    }
  
    public List<String> getMultiapiDatasetSchemas() {
    	
    	List<String> ds = new ArrayList<>();
     	if (params.get("MULTIAPI_DATASET_SCHEMAS")!=null)
     	{
     		ds = Arrays.asList(StringUtils.split( params.get("MULTIAPI_DATASET_SCHEMAS"), ','));
     	}
		return ds;
    }
    
	
}
