package org.csi.yucca.adminapi.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class WebServiceDelegate {
	static Logger log = Logger.getLogger(WebServiceDelegate.class);

	public static String callWebService( String wsURL, 
		                                 String username, 
		                                 String password, 
		                                 String xmlInput, 
		                                 String SOAPAction, 
		                                 String contentType)

			throws NoSuchAlgorithmException, KeyManagementException, IOException {

   		// -----------------------------------------------------------------------
        // evita l'utilizzo di un certificato se usi SSL
   		// -----------------------------------------------------------------------				
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
			
		} }; 


		// -----------------------------------------------------------------------
		// SSL CONTEXT
		// -----------------------------------------------------------------------
		SSLContext sc = SSLContext.getInstance("SSL");
		
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		String responseString = "";
		String outputString = "";
		
		log.info("Going to call " + wsURL);
		
		URL url = new URL(wsURL);
		URLConnection uc = null;
		
		if (username != null) {
			String userPassword = username + ":" + password;
			String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
			uc = url.openConnection();
			uc.setRequestProperty("Authorization", "Basic " + encoding);
		} else
			uc = url.openConnection();
		
		HttpURLConnection httpConn;
		if (url.getProtocol().equals("https"))
			httpConn = (HttpsURLConnection) uc;
		else
			httpConn = (HttpURLConnection) uc;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[xmlInput.length()];
		buffer = xmlInput.getBytes();
		bout.write(buffer);
		byte[] b = bout.toByteArray();
		httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
		httpConn.setRequestProperty("Content-Type", contentType + "; charset=utf-8"); // application/json
																						// o
																						// text/xml
		httpConn.setRequestProperty("SOAPAction", SOAPAction);
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		OutputStream out = httpConn.getOutputStream();
		// Write the content of the request to the outputstream of the HTTP
		// Connection.
		out.write(b);
		out.close();
		// Ready with sending the request.

		// Read the response.
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
		BufferedReader in = new BufferedReader(isr);

		// Write the SOAP message response to a String.
		while ((responseString = in.readLine()) != null) {
			outputString = outputString + responseString;
		}
		// Parse the String output to a org.w3c.dom.Document and be able to
		// reach every node with the org.w3c.dom API.
		log.info("Result: " + outputString);
		return outputString;
	}

//	public static void main(String[] args) {
//		//int TEST = 0;
//		int INT = 1;
//		int ENV = INT;
//		String user[] = { "admin", "admin" };
//String password[] = { "******", "**********" };
//		String[] url = { "https://*************services/RemoteAuthorizationManagerService",
//				"https://int-sso.smartdatanet.it/services/RemoteAuthorizationManagerService" };
//		
//		try {
//			testRoles(url[ENV], user[ENV], password[ENV], "*_subscriber");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	@SuppressWarnings("unused")
	private static void testPermission(String url, String user, String password){
		List<String> permissions = new LinkedList<String>();
		String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.ws.um.carbon.wso2.org\">";
		xmlInput += "   <soapenv:Header/>";
		xmlInput += "   <soapenv:Body>";
		xmlInput += "      <ser:getAllowedUIResourcesForUser>";
		xmlInput += "         <ser:userName>smartlab_developer</ser:userName>";
		xmlInput += "         <ser:permissionRootPath>permission/Applications/userportal</ser:permissionRootPath>";
		xmlInput += "      </ser:getAllowedUIResourcesForUser>";
		xmlInput += "   </soapenv:Body>";
		xmlInput += "</soapenv:Envelope>";

		String SOAPAction = "getAllowedUIResourcesForUser";

		// test "JabooCh0"
		try {
			String webServiceResponse = WebServiceDelegate.callWebService(url, user, password, xmlInput, SOAPAction, "text/xml");
			System.out.println(webServiceResponse);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource(new StringReader(webServiceResponse));
			Document doc = db.parse(is);

			NodeList permissionsNodeList = doc.getFirstChild().getFirstChild().getFirstChild().getChildNodes();
			if(permissionsNodeList!=null){
				for (int i = 0; i < permissionsNodeList.getLength(); i++) {
					Node permissionNode = permissionsNodeList.item(i);
					permissions.add(permissionNode.getTextContent());
				}
			}
			
			for (String string : permissions) {
				System.out.println("node - " + string);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static List<String> testRoles_(String url, String user, String password, String filter) throws KeyManagementException, NoSuchAlgorithmException, IOException, ParserConfigurationException,
	SAXException {

		log.debug("[SAML2ConsumerServlet::loadRoles] - START");
		List<String> roles = new LinkedList<String>();
		try {

			String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://org.apache.axis2/xsd\">";
			xmlInput += "   <soapenv:Header/>";
			xmlInput += "   <soapenv:Body>";
			xmlInput += "      <xsd:getRolesOfUser>";
			xmlInput += "         <xsd:userName>"+user+"</xsd:userName>";
			xmlInput += "         <xsd:filter>"+filter+"</xsd:filter>";
			xmlInput += "         <xsd:limit>-1</xsd:limit>";
			
			xmlInput += "      </xsd:getRolesOfUser>";
			xmlInput += "   </soapenv:Body>";
			xmlInput += "</soapenv:Envelope>";

			String SOAPAction = "getRolesOfUser";

		//	Properties config = Config.loadServerConfiguration();
		//	Properties authConfig = Config.loadAuthorizationConfiguration();

			String webServiceResponse = WebServiceDelegate.callWebService(url, user, password, xmlInput, SOAPAction, "text/xml");
			log.debug("[SAML2ConsumerServlet::loadRoles] - webServiceResponse: " + webServiceResponse);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource(new StringReader(webServiceResponse));
			Document doc = db.parse(is);

			NodeList rolessNodeList = doc.getFirstChild().getFirstChild().getFirstChild().getChildNodes();
			if (rolessNodeList != null) {
				for (int i = 0; i < rolessNodeList.getLength(); i++) {

					Node roleNode = rolessNodeList.item(i);
					String role = roleNode.getTextContent();
					log.debug("[SAML2ConsumerServlet::loadRoles] - role: " + role);
					roles.add(role);
				}
			}

		} finally {
			log.debug("[SAML2ConsumerServlet::loadRoles] - END");
		}
		return roles;
	}
}
