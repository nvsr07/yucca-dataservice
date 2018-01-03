package it.csi.smartdata.dataapi.constants;

import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SDPDataApiConfig {

	
	public static final String SDP_SOLR_URL="SDP_SOLR_URL";
	public static final String SDP_AMBIENTE="SDP_AMBIENTE";
	public static final String SDP_PHOENIX_URL="PHOENIX_URL";
	
	
	public static final String SDP_ADMINAPI_URL="SDP_ADMINAPI_URL";
	
	
		
	public static SDPDataApiConfig instance=null;
	private static int anno_init = 0;
	private static int mese_init = 0;
	private static int giorno_init = 0;

	private HashMap<String, String> params = new HashMap<String, String>();
		

	
	private static boolean singletonToRefresh() {
		int curAnno = Calendar.getInstance().get(Calendar.YEAR);
		int curMese = Calendar.getInstance().get(Calendar.MONTH);
		int curGiorno = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if (curAnno > anno_init) return true;
		else if (curMese > mese_init) return true;
		else if (curGiorno > giorno_init)return true;
		return false;
	}
	public synchronized static SDPDataApiConfig getInstance() {
		if(instance == null || singletonToRefresh()) {
			instance = new SDPDataApiConfig();
			anno_init = Calendar.getInstance().get(Calendar.YEAR);
			mese_init = Calendar.getInstance().get(Calendar.MONTH);
			giorno_init = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		}
		return instance;
	}

	private SDPDataApiConfig(){
		
		ResourceBundle rb= ResourceBundle.getBundle("SDPDataApiConfig");
		
		 params = new HashMap<String, String>();
		 
			params.put("SDP_SOLR_URL", rb.getString("SDP_SOLR_URL"));
			params.put("SDP_AMBIENTE", rb.getString("SDP_AMBIENTE"));
			params.put(SDP_PHOENIX_URL, rb.getString(SDP_PHOENIX_URL));
			
			params.put(SDP_ADMINAPI_URL, rb.getString(SDP_ADMINAPI_URL));
		 
		params.put("SDP_WEB_FILTER_PATTERN", rb.getString("SDP_WEB_FILTER_PATTERN"));
		params.put("SDP_WEB_SERVLET_URL", rb.getString("SDP_WEB_SERVLET_URL"));
		params.put("SDP_WEB_BASE_URL", rb.getString("SDP_WEB_BASE_URL"));
		params.put("SDP_WEB_LOCALHOST_PORT", rb.getString("SDP_WEB_LOCALHOST_PORT"));
		
		params.put("SDP_WEB_PUB_URI", rb.getString("SDP_WEB_PUB_URI"));
		
		
		params.put("SDP_MAX_DOCS_PER_PAGE", rb.getString("SDP_MAX_DOCS_PER_PAGE"));
		params.put("SDP_MAX_SKIP_PAGE", rb.getString("SDP_MAX_SKIP_PAGE"));
		params.put("SDP_ENABLE_NEXT", rb.getString("SDP_ENABLE_NEXT"));
		
		
		
		params.put("SOLR_TYPE_ACCESS", rb.getString("SOLR_TYPE_ACCESS"));
		params.put("SOLR_USERNAME", rb.getString("SOLR_USERNAME"));
		params.put("SOLR_PASSWORD", rb.getString("SOLR_PASSWORD"));
		params.put("SOLR_SECURITY_DOMAIN_NAME", rb.getString("SOLR_SECURITY_DOMAIN_NAME"));

		
	
		
		
	}

	
	public String getSolrSecurityDomainName() {
		return (params.get("SOLR_SECURITY_DOMAIN_NAME") != null ? params.get("SOLR_SECURITY_DOMAIN_NAME") : "");
	}
	
	
	public String getSolrTypeAccess() {
		return (params.get("SOLR_TYPE_ACCESS") != null ? params.get("SOLR_TYPE_ACCESS") : "");
	}
	
	public String getSolrUsername(){
		return (params.get("SOLR_USERNAME") != null ? params.get("SOLR_USERNAME") : "");
		
	}
	
	public String getSolrPassword() {
		return (params.get("SOLR_PASSWORD") != null ? params.get("SOLR_PASSWORD") : "");
		
	}
	
	public String getPhoenixUrl() {
		return params.get(SDP_PHOENIX_URL);
	}
	
	public String getAdminApiUrl()
	{
		return params.get(SDP_ADMINAPI_URL);
	}
	
	public String getSolrUrl() {
		return params.get(SDP_SOLR_URL);
	}
	
	public String getSdpAmbiente() {
		return (null==params.get(SDP_AMBIENTE) ? "" : params.get(SDP_AMBIENTE));
	}
	
	public int getMaxDocumentPerPage() {
		
		return Integer.parseInt(params.get("SDP_MAX_DOCS_PER_PAGE"));
		
	}

	public int getMaxSkipPages() {
		
		return Integer.parseInt(params.get("SDP_MAX_SKIP_PAGE"));
		
	}
	
	
	public String getWebFilterPattern() {
		return params.get("SDP_WEB_FILTER_PATTERN");
	}
	public String getWebServletUrl() {
		return params.get("SDP_WEB_SERVLET_URL");
	}
	public String getWebBaseUrl() {
		return params.get("SDP_WEB_BASE_URL");
	}
	public String getWebLocalHostPort() {
		return params.get("SDP_WEB_LOCALHOST_PORT");
	}
	
	public String getPubUri() {
		return params.get("SDP_WEB_PUB_URI");
	}
	
    
    public boolean isNextEnabled() {
    	if (null!=params.get("SDP_ENABLE_NEXT") && "true".equalsIgnoreCase(params.get("SDP_ENABLE_NEXT"))) return true;
    	return false;
    }
      
    
	
}
