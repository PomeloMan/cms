package io.cms.core.configure.security.jwt;

import java.util.Date;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import io.cms.util.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@ConfigurationProperties("jwt")
public class JwtTokenAuthenticationService {

	private String prefix;
	private String header;
	private String principal;
	private String algorithm;
	private String secret;
	private long expiration;

	@Bean
	public JwtUtil jwtUtil() {
		JwtUtil jwtUtil = new JwtUtil();
		jwtUtil.setAlgorithm(SignatureAlgorithm.valueOf(algorithm));
		jwtUtil.setSecret(secret);
		jwtUtil.setExpiration(expiration);
		return jwtUtil;
	}

	public String generateToken(String subject, Map<String, Object> claims) {
		return jwtUtil().generateToken(subject, claims);
	}

	public Claims getClaimByToken(String token) {
		return jwtUtil().getClaimByToken(token);
	}

	public boolean isTokenExpired(Date expiration) {
		return expiration.before(new Date());
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

}
