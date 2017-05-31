package it.csi.smartdata.dataapi.multiapi.constants;

import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SDPDataMultiApiConfig {

	public static final String API_ODATA_BASE_URL="API_ODATA_BASE_URL";

	public final static SDPDataMultiApiConfig instance= new SDPDataMultiApiConfig();


	private HashMap<String, String> params = new HashMap<String, String>();
		

	private SDPDataMultiApiConfig() {
		
		ResourceBundle rb= ResourceBundle.getBundle("SDPDataApiConfig");
		params = new HashMap<String, String>();
		params.put("API_EXTERNAL_ODATA_BASE_URL", rb.getString("API_EXTERNAL_ODATA_BASE_URL"));
		
	}

	
    public String getApiOdataBaseUrl() {
		return params.get("API_EXTERNAL_ODATA_BASE_URL");
    }
    
    
  
    
	
}
