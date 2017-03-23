package org.csi.yucca.dataservice.metadataapi.delegate.security;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.delegate.WebServiceDelegate;
import org.csi.yucca.dataservice.metadataapi.exception.UserWebServiceException;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wso2.carbon.identity.oauth2.stub.OAuth2TokenValidationServiceStub;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationRequestDTO;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationRequestDTO_OAuth2AccessToken;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationRequestDTO_TokenValidationContextParam;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationResponseDTO;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SecurityDelegate {
	static Logger log = Logger.getLogger(SecurityDelegate.class);
	private String proxyHostname;
	private int proxyPort;
	private OAuth2TokenValidationServiceStub oAuth2TokenValidationServiceStub;

	private static SecurityDelegate instance;

	protected String SEARCH_ENGINE_BASE_URL = Config.getInstance().getSearchEngineBaseUrl();

	private SecurityDelegate() {
		super();
	}

	public static SecurityDelegate getInstance() {
		if (instance == null)
			instance = new SecurityDelegate();
		return instance;
	}

	public List<String> getTenantAuthorized(HttpServletRequest httpRequest) throws UserWebServiceException{
		try {
			String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				String token = authorizationHeader.substring("Bearer".length()).trim();
				if (token != "") {
					OAuth2TokenValidationRequestDTO dto = new OAuth2TokenValidationRequestDTO();
					OAuth2TokenValidationRequestDTO_OAuth2AccessToken tokenDto = new OAuth2TokenValidationRequestDTO_OAuth2AccessToken();
					tokenDto.setIdentifier(token);
					tokenDto.setTokenType("bearer");
					dto.setAccessToken(tokenDto);
					OAuth2TokenValidationRequestDTO_TokenValidationContextParam[] arrayCt = new OAuth2TokenValidationRequestDTO_TokenValidationContextParam[1];
					arrayCt[0] = new OAuth2TokenValidationRequestDTO_TokenValidationContextParam();
					dto.setContext(arrayCt);
					OAuth2TokenValidationResponseDTO response = getOAuth2TokenValidationServiceStub().validate(dto);
					String authorizedUser = response.getAuthorizedUser();
					boolean isValidUser = response.getValid();
					if (!isValidUser)
						throw new UserWebServiceException(Response.status(Status.UNAUTHORIZED).build());
					else {
						return loadTenants(authorizedUser); 
					}
				}	
			}
			return null;
		} catch (Exception e) {
			throw new UserWebServiceException(Response.status(Status.UNAUTHORIZED).build());
		}

	}
	
	private OAuth2TokenValidationServiceStub getOAuth2TokenValidationServiceStub() throws AxisFault {

		if (oAuth2TokenValidationServiceStub == null) {
			String oauthServerUrl = Config.getInstance().getOauthBaseUrl();
			String oauthUsername = Config.getInstance().getOauthUsername();
			String oauthPassword = Config.getInstance().getOauthPassword();

			String oAuth2TokenValidationServiceEndPoint = oauthServerUrl + "/services/OAuth2TokenValidationService";

			oAuth2TokenValidationServiceStub = new OAuth2TokenValidationServiceStub(
					ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null)
					, oAuth2TokenValidationServiceEndPoint);
			ServiceClient oauth2TokenValidationService = oAuth2TokenValidationServiceStub._getServiceClient();
			Options optionOauth2Validation = oauth2TokenValidationService.getOptions();
			setProxyToOptions(optionOauth2Validation, oauthUsername, oauthPassword);
		}

		return oAuth2TokenValidationServiceStub;
	}

	private void setProxyToOptions(Options option, String username, String password) {
		/**
		 * Setting a authenticated cookie that is received from Carbon server.
		 * If you have authenticated with Carbon server earlier, you can use
		 * that cookie, if it has not been expired
		 */
		option.setProperty(HTTPConstants.COOKIE_STRING, null);
		/**
		 * Setting proxy property if exists
		 */
		if (proxyHostname != null && !proxyHostname.trim().isEmpty()) {
			HttpTransportProperties.ProxyProperties proxyProperties = new HttpTransportProperties.ProxyProperties();
			proxyProperties.setProxyName(proxyHostname);
			proxyProperties.setProxyPort(proxyPort);
			option.setProperty(HTTPConstants.PROXY, proxyProperties);
		}
		/**
		 * Setting basic auth headers for authentication for carbon server
		 */
		HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
		auth.setUsername(username);
		auth.setPassword(password);
		auth.setPreemptiveAuthentication(true);
		option.setProperty(HTTPConstants.AUTHENTICATE, auth);
		option.setManageSession(true);
	}


	private List<String> loadTenants(String username) throws KeyManagementException, NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException {

		log.debug("[SecurityDelegate::loadRoles] - START");
		String filter = "*_subscriber";
		List<String> roles = new LinkedList<String>();
		try {

			String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://org.apache.axis2/xsd\">";
			xmlInput += "   <soapenv:Header/>";
			xmlInput += "   <soapenv:Body>";
			xmlInput += "      <xsd:getRolesOfUser>";
			xmlInput += "         <xsd:userName>" + username + "</xsd:userName>";
			xmlInput += "         <xsd:filter>" + filter + "</xsd:filter>";
			xmlInput += "         <xsd:limit>-1</xsd:limit>";

			xmlInput += "      </xsd:getRolesOfUser>";
			xmlInput += "   </soapenv:Body>";
			xmlInput += "</soapenv:Envelope>";

			String SOAPAction = "getRolesOfUser";



			String webserviceUrl = Config.getInstance().getOauthRolesWebserviceUrl();
			String user = Config.getInstance().getOauthUsername();
			String password = Config.getInstance().getOauthPassword();
			
			String webServiceResponse = WebServiceDelegate.callWebService(webserviceUrl, user, password, xmlInput, SOAPAction, "text/xml");
			log.debug("[SecurityDelegate::loadRoles] - webServiceResponse: " + webServiceResponse);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource(new StringReader(webServiceResponse));
			Document doc = db.parse(is);

			NodeList rolessNodeList = doc.getFirstChild().getFirstChild().getFirstChild().getChildNodes();
			if (rolessNodeList != null) {
				for (int i = 0; i < rolessNodeList.getLength(); i++) {

					Node roleNode = rolessNodeList.item(i);

					String selected = "";
					String role = "";
					for (int j = 0; j < roleNode.getChildNodes().getLength(); j++) {
						Node node = roleNode.getChildNodes().item(j);
						if ("ax2644:selected".equals(node.getNodeName())) {
							selected = node.getTextContent();
						} else if ("ax2644:itemName".equals(node.getNodeName())) {
							role = node.getTextContent();
						}
					}

					if (selected.equals("true") && !role.equals(""))
						roles.add(role.replace("_subscriber", ""));

				}
			}

		} finally {
			log.debug("[SecurityDelegate::loadRoles] - END");
		}
		return roles;
	}

}
