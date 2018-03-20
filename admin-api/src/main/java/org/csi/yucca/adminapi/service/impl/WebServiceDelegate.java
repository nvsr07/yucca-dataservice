package org.csi.yucca.adminapi.service.impl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.csi.yucca.adminapi.util.WebServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource(value = { "classpath:web-services.properties" })
public class WebServiceDelegate {
	
	@Value("${adminservice.soap.endpoint}")
	private String adminserviceSoapEndpoint;

	@Value("${adminservice.soap.endpoint.user}")
	private String adminserviceSoapEndpointUser;

	@Value("${adminservice.soap.endpoint.password}")
	private String adminserviceSoapEndpointPassword;
	
	public static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
	public static final String SOAP_ACTION_HEADER_KEY = "SOAPAction";
	
	public static final String VALIDATE_SIDDHI_QUERIES_SOAP_ACTION = "urn:validateSiddhiQueries";
	public static final String VALIDATE_SIDDHI_QUERIES_CONTENT_TYPE = "text/xml";
	
	public WebServiceResponse callWebService( String wsURL, String username, String password, 
            String xmlInput, String soapAction, String contentType)throws NoSuchAlgorithmException, KeyManagementException, IOException {
		
		HttpClientBuilder clientBuilder = HttpClients.custom();
		
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		clientBuilder.setDefaultCredentialsProvider(provider);
		CloseableHttpClient client = clientBuilder.build();
		HttpPost post = new HttpPost(wsURL);
		HttpEntity str = new StringEntity(xmlInput);
		post.setEntity(str);post.setHeader(CONTENT_TYPE_HEADER_KEY, contentType);
		post.setHeader(SOAP_ACTION_HEADER_KEY, soapAction);
		
		CloseableHttpResponse closeableHttpResponse = client.execute(post);
		
		return new WebServiceResponse(closeableHttpResponse);
	}
	
	/**
	 * 
	 * @param queryExpressions
	 * @param inputStreamDefiniitons
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	public WebServiceResponse validateSiddhiQueriesWebService(String queryExpressions, List<String> inputStreamDefiniitons)throws NoSuchAlgorithmException, KeyManagementException, IOException {
		
		String inputStreamDefiniitonsString;
		StringBuilder sb = new StringBuilder ();

		for (String element:inputStreamDefiniitons)
		{
			sb.append (element);
		}

		inputStreamDefiniitonsString= sb.toString ();
		
		String xmlInput =
			    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://admin.processor.event.carbon.wso2.org\">";
		xmlInput += "   <soapenv:Header/>";
		
		xmlInput += "   <soapenv:Body>";
		
		xmlInput += "      <ser:validateSiddhiQueries>";
		xmlInput += "         <ser:inputStreamDefiniitons>" + inputStreamDefiniitonsString + "</ser:inputStreamDefiniitons>";
	    xmlInput += "         <ser:queryExpressions>" + queryExpressions + "</ser:queryExpressions>";	    
		xmlInput += "      </ser:validateSiddhiQueries>";
		
		xmlInput += "   </soapenv:Body>";
		xmlInput += "</soapenv:Envelope>";
		
		return callWebService( this.adminserviceSoapEndpoint, 
							   this.adminserviceSoapEndpointUser, 
							   this.adminserviceSoapEndpointPassword, 
							   xmlInput, 
							   VALIDATE_SIDDHI_QUERIES_SOAP_ACTION, 
							   VALIDATE_SIDDHI_QUERIES_CONTENT_TYPE);
	}
}
