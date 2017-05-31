package it.csi.smartdata.dataapi.multiapi.constants;

import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SDPDataMultiApiConfig {


	public final static SDPDataMultiApiConfig instance= new SDPDataMultiApiConfig();


	private HashMap<String, String> params = new HashMap<String, String>();
		

	private SDPDataMultiApiConfig() {
		
		ResourceBundle rb= ResourceBundle.getBundle("SDPDataApiConfig");
		params = new HashMap<String, String>();
		params.put("API_EXTERNAL_ODATA_BASE_URL", rb.getString("API_EXTERNAL_ODATA_BASE_URL"));
		params.put("MULTIAPI_ODATA_BASE_URL", rb.getString("MULTIAPI_ODATA_BASE_URL"));
		
	}

	
    public String getApiExternalOdataBaseUrl() {
		return params.get("API_EXTERNAL_ODATA_BASE_URL");
    }
    
    public String getMultiapiOdataBaseUrl() {
		return params.get("MULTIAPI_ODATA_BASE_URL");
    }
    
  
    
	
}
