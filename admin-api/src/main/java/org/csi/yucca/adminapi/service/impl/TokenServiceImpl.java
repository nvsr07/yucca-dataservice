package org.csi.yucca.adminapi.service.impl;

import org.apache.http.entity.ContentType;
import org.csi.yucca.adminapi.delegate.HttpDelegate;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.response.TokenResponse;
import org.csi.yucca.adminapi.service.TokenService;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

@Service
@PropertySources({ @PropertySource("classpath:adminapi.properties") })
public class TokenServiceImpl implements TokenService {
	
	@Value("${gateway-api.base.url}")
	private String gatewayApiBaseUrl;

	public static final String URL = "token";
	
	public static final String  STRING_DATA = "scope=PRODUCTION&grant_type=client_credentials";
	
	@Override
	public ServiceResponse get(String clientKey, String clientSecret) throws BadRequestException, NotFoundException, Exception {

		String stringTokenResponse =  HttpDelegate.makeHttpPost( null, 
							                                     gatewayApiBaseUrl + URL, 
							                                     null, 
							                                     clientKey, 
							                                     clientSecret, 
							                                     STRING_DATA, 
							                                     ContentType.APPLICATION_FORM_URLENCODED);

		TokenResponse response = Util.getFromJsonString(stringTokenResponse, TokenResponse.class);
		
		return ServiceResponse.build().object(response);
	}


	

}
