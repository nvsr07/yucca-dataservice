package org.csi.yucca.dataservice.metadataapi.delegate.security;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.delegate.WebServiceDelegate;
import org.csi.yucca.dataservice.metadataapi.exception.UserWebServiceException;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SecurityDelegate {
	static Logger log = Logger.getLogger(SecurityDelegate.class);
	// private String proxyHostname;
	// private int proxyPort;

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

	public List<String> getTenantAuthorized(HttpServletRequest httpRequest) throws UserWebServiceException {
		try {
			// String authorizationHeader =
			// httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
			// if (authorizationHeader != null &&
			// authorizationHeader.startsWith("Bearer ")) {
			// String token =
			// authorizationHeader.substring("Bearer".length()).trim();
			// if (token != "") {
			//
			// String xmlInput =
			// "	<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://org.apache.axis2/xsd\" xmlns:xsd1=\"http://dto.oauth2.identity.carbon.wso2.org/xsd\">";
			// xmlInput += "	   <soapenv:Header/>";
			// xmlInput += "	   <soapenv:Body>";
			// xmlInput += "	      <xsd:validate>";
			// xmlInput += "	         <xsd:validationReqDTO>";
			// xmlInput += "	<xsd1:accessToken>";
			// xmlInput += "<xsd1:identifier>" + token + "</xsd1:identifier>";
			// xmlInput += "<xsd1:tokenType>bearer</xsd1:tokenType>";
			// xmlInput += "</xsd1:accessToken>";
			// xmlInput += "<xsd1:context>";
			// xmlInput += "<xsd1:key></xsd1:key>";
			// xmlInput += "<xsd1:value></xsd1:value>";
			// xmlInput += "</xsd1:context>";
			// xmlInput += "<xsd1:requiredClaimURIs>?</xsd1:requiredClaimURIs>";
			// xmlInput += "</xsd:validationReqDTO>";
			// xmlInput += "</xsd:validate>";
			// xmlInput += "</soapenv:Body>";
			// xmlInput += "</soapenv:Envelope>";
			//
			// String SOAPAction = "validate";
			//
			// String webserviceUrl = Config.getInstance().getOauthBaseUrl() +
			// "/services/OAuth2TokenValidationService";
			// String user = Config.getInstance().getOauthUsername();
			// String password = Config.getInstance().getOauthPassword();
			//
			// String webServiceResponse =
			// WebServiceDelegate.callWebService(webserviceUrl, user, password,
			// xmlInput, SOAPAction, "text/xml");
			// log.debug("[SecurityDelegate::getTenantAuthorized] - webServiceResponse: "
			// + webServiceResponse);
			//
			// DocumentBuilderFactory dbf =
			// DocumentBuilderFactory.newInstance();
			// DocumentBuilder db = dbf.newDocumentBuilder();
			//
			// InputSource is = new InputSource(new
			// StringReader(webServiceResponse));
			// Document doc = db.parse(is);
			//
			// String authorizedUser = null;
			// boolean isValidUser = false;
			// String error = null;
			//
			// NodeList authorizationsNodeList =
			// doc.getFirstChild().getFirstChild().getFirstChild().getChildNodes();
			// if (authorizationsNodeList != null) {
			// for (int i = 0; i < authorizationsNodeList.getLength(); i++) {
			// Node authorizationNode = authorizationsNodeList.item(i);
			// if ("ax2335:valid".equals(authorizationNode.getNodeName())) {
			// isValidUser = "true".equals(authorizationNode.getTextContent());
			// } else if
			// ("ax2335:authorizedUser".equals(authorizationNode.getNodeName()))
			// {
			// authorizedUser = authorizationNode.getTextContent();
			// } else if
			// ("ax2335:errorMsg".equals(authorizationNode.getNodeName())) {
			// error = authorizationNode.getTextContent();
			// }
			// }
			// }
			//
			// if (!isValidUser) {
			// log.error("[SecurityDelegate::getTenantAuthorized] - ERROR " +
			// error);
			// throw new
			// UserWebServiceException(Response.status(Status.UNAUTHORIZED).build());
			// } else {
			// return loadTenants(authorizedUser);
			// }
			// }
			// }
			// return null;
			String authorizedUser = validateTokenBearer(httpRequest);
			return loadTenants(authorizedUser);
		} catch (Exception e) {
			log.error("[SecurityDelegate::getTenantAuthorized] - Unexpected ERROR");

			throw new UserWebServiceException(Response.status(Status.UNAUTHORIZED).build());
		}

		// NodeList rolessNodeList =
		// doc.getFirstChild().getFirstChild().getFirstChild().getChildNodes();
		// if (rolessNodeList != null) {
		// for (int i = 0; i < rolessNodeList.getLength(); i++) {
		//
		// Node roleNode = rolessNodeList.item(i);
		//
		// String selected = "";
		// String role = "";
		// for (int j = 0; j < roleNode.getChildNodes().getLength(); j++) {
		// Node node = roleNode.getChildNodes().item(j);
		// if ("ax2644:selected".equals(node.getNodeName())) {
		// selected = node.getTextContent();
		// } else if ("ax2644:itemName".equals(node.getNodeName())) {
		// role = node.getTextContent();
		// }
		// }
		//
		// if (selected.equals("true") && !role.equals(""))
		// roles.add(role.replace("_subscriber", ""));
		//
		// }
		// }
		//

		/*
		 * 
		 * <soapenv:Envelope
		 * xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
		 * <soapenv:Body> <ns:validateResponse
		 * xmlns:ns="http://org.apache.axis2/xsd"> <ns:return
		 * xsi:type="ax2335:OAuth2TokenValidationResponseDTO"
		 * xmlns:ax2335="http://dto.oauth2.identity.carbon.wso2.org/xsd"
		 * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		 * <ax2335:authorizationContextToken xsi:nil="true"/>
		 * <ax2335:authorizedUser xsi:nil="true"/> <ax2335:errorMsg>Invalid
		 * input. Access token validation failed</ax2335:errorMsg>
		 * <ax2335:expiryTime>0</ax2335:expiryTime> <ax2335:scope
		 * xsi:nil="true"/> <ax2335:valid>false</ax2335:valid> </ns:return>
		 * </ns:validateResponse> </soapenv:Body> </soapenv:Envelope>
		 * 
		 * <soapenv:Envelope
		 * xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
		 * <soapenv:Body> <ns:validateResponse
		 * xmlns:ns="http://org.apache.axis2/xsd"> <ns:return
		 * xsi:type="ax2335:OAuth2TokenValidationResponseDTO"
		 * xmlns:ax2335="http://dto.oauth2.identity.carbon.wso2.org/xsd"
		 * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		 * <ax2335:authorizationContextToken xsi:nil="true"/>
		 * <ax2335:authorizedUser>prdcld75d29a052v</ax2335:authorizedUser>
		 * <ax2335:errorMsg xsi:nil="true"/>
		 * <ax2335:expiryTime>25752355</ax2335:expiryTime>
		 * <ax2335:scope>default</ax2335:scope>
		 * <ax2335:valid>true</ax2335:valid> </ns:return> </ns:validateResponse>
		 * </soapenv:Body> </soapenv:Envelope> *
		 */

		// OAuth2TokenValidationRequestDTO dto = new
		// OAuth2TokenValidationRequestDTO();
		// OAuth2TokenValidationRequestDTO_OAuth2AccessToken tokenDto = new
		// OAuth2TokenValidationRequestDTO_OAuth2AccessToken();
		// tokenDto.setIdentifier(token);
		// tokenDto.setTokenType("bearer");
		// dto.setAccessToken(tokenDto);
		// OAuth2TokenValidationRequestDTO_TokenValidationContextParam[] arrayCt
		// = new OAuth2TokenValidationRequestDTO_TokenValidationContextParam[1];
		// arrayCt[0] = new
		// OAuth2TokenValidationRequestDTO_TokenValidationContextParam();
		// dto.setContext(arrayCt);
		// OAuth2TokenValidationResponseDTO response =
		// getOAuth2TokenValidationServiceStub().validate(dto);
		// String authorizedUser = response.getAuthorizedUser();
		// boolean isValidUser = response.getValid();

	}

	public String validateTokenBearer(HttpServletRequest httpRequest) throws Exception {
		String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
		String authorizedUser = null;
		String error = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			boolean isValidUser = false;
			String token = authorizationHeader.substring("Bearer".length()).trim();
			if (token != "") {

				String xmlInput = "	<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://org.apache.axis2/xsd\" xmlns:xsd1=\"http://dto.oauth2.identity.carbon.wso2.org/xsd\">";
				xmlInput += "	   <soapenv:Header/>";
				xmlInput += "	   <soapenv:Body>";
				xmlInput += "	      <xsd:validate>";
				xmlInput += "	         <xsd:validationReqDTO>";
				xmlInput += "	<xsd1:accessToken>";
				xmlInput += "<xsd1:identifier>" + token + "</xsd1:identifier>";
				xmlInput += "<xsd1:tokenType>bearer</xsd1:tokenType>";
				xmlInput += "</xsd1:accessToken>";
				xmlInput += "<xsd1:context>";
				xmlInput += "<xsd1:key></xsd1:key>";
				xmlInput += "<xsd1:value></xsd1:value>";
				xmlInput += "</xsd1:context>";
				xmlInput += "<xsd1:requiredClaimURIs>?</xsd1:requiredClaimURIs>";
				xmlInput += "</xsd:validationReqDTO>";
				xmlInput += "</xsd:validate>";
				xmlInput += "</soapenv:Body>";
				xmlInput += "</soapenv:Envelope>";

				String SOAPAction = "validate";

				String webserviceUrl = Config.getInstance().getOauthBaseUrl() + "/services/OAuth2TokenValidationService";
				String user = Config.getInstance().getOauthUsername();
				String password = Config.getInstance().getOauthPassword();

				String webServiceResponse = WebServiceDelegate.callWebService(webserviceUrl, user, password, xmlInput, SOAPAction, "text/xml");
				log.debug("[SecurityDelegate::getTenantAuthorized] - webServiceResponse: " + webServiceResponse);

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();

				InputSource is = new InputSource(new StringReader(webServiceResponse));
				Document doc = db.parse(is);

				NodeList authorizationsNodeList = doc.getFirstChild().getFirstChild().getFirstChild().getChildNodes();
				if (authorizationsNodeList != null) {
					for (int i = 0; i < authorizationsNodeList.getLength(); i++) {
						Node authorizationNode = authorizationsNodeList.item(i);
						if ("ns:return".equals(authorizationNode.getNodeName())) {
							for (int j = 0; j < authorizationNode.getChildNodes().getLength(); j++) {
								Node authReturnNodeItem = authorizationNode.getChildNodes().item(j);
								String fullNodeName = authReturnNodeItem.getNodeName();
								if (fullNodeName.indexOf(":") > 0) {
									String nodeName = fullNodeName.split(":")[1];
									if ("valid".equals(nodeName)) {
										isValidUser = "true".equals(authReturnNodeItem.getTextContent());
									} else if ("authorizedUser".equals(nodeName)) {
										authorizedUser = authReturnNodeItem.getTextContent();
									} else if ("errorMsg".equals(nodeName)) {
										error = authReturnNodeItem.getTextContent();
									}
								}
							}
						}
					}
				}
			}
			if (!isValidUser) {
				throw new Exception("Invalid User: " + error);
			}
		}

		return authorizedUser;

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
