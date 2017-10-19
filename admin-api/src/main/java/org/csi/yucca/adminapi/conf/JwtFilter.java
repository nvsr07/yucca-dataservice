package org.csi.yucca.adminapi.conf;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csi.yucca.adminapi.jwt.JwtService;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebFilter(urlPatterns = { "/api/secure/*", 
		                   "/1/management/*" 
		                 })
@PropertySource(value = { "classpath:jwt.properties" })
public class JwtFilter implements Filter {

	public static final String JWT_USER_REQUEST_ATTRIBUTE_KEY = "jwtUser";
	
	@Autowired
	private JwtService jwtService;

	@Value("${jwt.auth.header}")
	private String authHeader;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		final String authHeaderVal = httpRequest.getHeader(authHeader);

		if (null == authHeaderVal) {
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		try {

			JwtUser jwtUser = jwtService.getUser(authHeaderVal);

			httpRequest.setAttribute(JWT_USER_REQUEST_ATTRIBUTE_KEY, jwtUser);
		} catch (java.text.ParseException e) {
			httpResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}

		chain.doFilter(httpRequest, httpResponse);
	}
}