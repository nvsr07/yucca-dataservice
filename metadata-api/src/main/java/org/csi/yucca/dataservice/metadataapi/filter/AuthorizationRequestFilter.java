package org.csi.yucca.dataservice.metadataapi.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.wso2.carbon.identity.oauth2.stub.OAuth2TokenValidationServiceStub;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationRequestDTO;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationRequestDTO_OAuth2AccessToken;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationRequestDTO_TokenValidationContextParam;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationResponseDTO;

@WebFilter(filterName = "AuthorizationFilter", urlPatterns = { "/*" })
public class AuthorizationRequestFilter implements Filter {

	private ConfigurationContext configContext;
	private String proxyHostname;
	private int proxyPort;
	private OAuth2TokenValidationServiceStub oAuth2TokenValidationServiceStub;

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		try {

			if (httpRequest.getSession().getAttribute("userAuth") != null)
				httpRequest.getSession().removeAttribute("userAuth");

			String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

			if (authorizationHeader != null && !authorizationHeader.startsWith("Bearer ")) {

				String token = authorizationHeader.substring("Bearer".length()).trim();
				if (token != "") {
					configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);

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

					if (!isValidUser) {
						httpRequest.getSession().setAttribute("userAuth", authorizedUser);
					}
				}
			}

		} catch (Exception e) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		filterChain.doFilter(httpRequest, servletResponse);

	}

	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			getOAuth2TokenValidationServiceStub();
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private OAuth2TokenValidationServiceStub getOAuth2TokenValidationServiceStub() throws AxisFault {

		if (oAuth2TokenValidationServiceStub == null) {
			String oauthServerUrl = Config.getInstance().getOauthBaseUrl();
			String oauthUsername = Config.getInstance().getOauthUsername();
			String oauthPassword = Config.getInstance().getOauthPassword();

			String oAuth2TokenValidationServiceEndPoint = oauthServerUrl + "/services/OAuth2TokenValidationService";

			oAuth2TokenValidationServiceStub = new OAuth2TokenValidationServiceStub(configContext, oAuth2TokenValidationServiceEndPoint);
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

}
