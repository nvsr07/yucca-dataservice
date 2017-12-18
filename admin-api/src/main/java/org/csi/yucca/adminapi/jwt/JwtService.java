package org.csi.yucca.adminapi.jwt;

//import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.csi.yucca.adminapi.jwt.JwtUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.util.Base64;

import net.minidev.json.JSONObject;

@Service
@PropertySource(value = { "classpath:jwt.properties" })
public class JwtService {

	@Value("${jwt.token.secret}")
	private String tokenSecret;

	@Value("${jwt.expire.hours}")
	private Long expireHours;

	private String encodedSecret;

	@PostConstruct
	protected void init() {
		this.encodedSecret = generateEncodedSecret(this.tokenSecret);
	}

	protected JwtUser getUser(String encodedSecret, String token) throws java.text.ParseException {
		
		JWSObject jwsObject;
		
		try {
			
			jwsObject = JWSObject.parse(token);

			JSONObject jsonObject = jwsObject.getPayload().toJSONObject();

			return new JwtUser(jsonObject);
		} 
		catch (java.text.ParseException e) {
			throw e;
		}

	}

	public JwtUser getUser(String token) throws java.text.ParseException {
		return getUser(this.encodedSecret, token);
	}

	protected String generateEncodedSecret(String plainSecret) {
		if (StringUtils.isEmpty(plainSecret)) {
			throw new IllegalArgumentException("JWT secret cannot be null or empty.");
		}
		
		return Base64.encode(plainSecret.getBytes()).toString();
//		return Base64.getEncoder().encodeToString(plainSecret.getBytes());
	}

	protected Date getExpirationTime() {
		Date now = new Date();
		Long expireInMilis = TimeUnit.HOURS.toMillis(expireHours);
		return new Date(expireInMilis + now.getTime());
	}

}