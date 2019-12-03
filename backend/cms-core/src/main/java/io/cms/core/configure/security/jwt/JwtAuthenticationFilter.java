package io.cms.core.configure.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.cms.core.module.system.persistence.entity.User;
import io.jsonwebtoken.Claims;

/**
 * Reference
 * org.springframework.security.web.authentication.www.BasicAuthenticationFilter
 * 
 * @author PomeloMan
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

	private Gson gson;
	private AuthenticationManager authenticationManager;
	private JwtTokenAuthenticationService jwtTokenAuthenticationService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final boolean debug = this.logger.isDebugEnabled();

		String header = request.getHeader(jwtTokenAuthenticationService.getHeader());

		if (header == null) {
			chain.doFilter(request, response);
			return;
		}

		try {
			String token = StringUtils.substringAfter(header, jwtTokenAuthenticationService.getPrefix());
			Claims claims = null;

			try {
				claims = jwtTokenAuthenticationService.getClaimByToken(token);
			} catch (Exception e) {
				throw new AuthenticationServiceException(e.getMessage(), e);
			}

			// expired?
			if (jwtTokenAuthenticationService.isTokenExpired(claims.getExpiration())) {
				throw new AuthenticationServiceException("Token expired");
			}

			String username = claims.getSubject();

			if (debug) {
				this.logger.debug("JWT Authentication Authorization header found for user '" + username + "'");
			}

			if (authenticationIsRequired(username)) {
				Object principalJson = claims.get(jwtTokenAuthenticationService.getPrincipal());

				User principal = null;
				try {
					principal = gson.fromJson(principalJson.toString(), new TypeToken<User>() { }.getType());
				} catch (Exception e) {
					throw new AuthenticationServiceException(e.getMessage(), e);
				}

				UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, null, principal.getAuthorities());
				authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));

				Authentication authResult = authRequest;
//				Authentication authResult = this.authenticationManager.authenticate(authRequest);

				if (debug) {
					this.logger.debug("Authentication success: " + authResult);
				}

				SecurityContextHolder.getContext().setAuthentication(authResult);

				onSuccessfulAuthentication(request, response, authResult);
			}
		} catch (AuthenticationException failed) {
			SecurityContextHolder.clearContext();

			if (debug) {
				this.logger.debug("Authentication request for failed: " + failed);
			}

			onUnsuccessfulAuthentication(request, response, failed);

			return;
		}
		chain.doFilter(request, response);
	}

	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) throws IOException {
	}

	protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException {
	}

	private boolean authenticationIsRequired(String username) {
		Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

		if (existingAuth == null || !existingAuth.isAuthenticated()) {
			return true;
		}

		if (existingAuth instanceof UsernamePasswordAuthenticationToken && !existingAuth.getName().equals(username)) {
			return true;
		}

		if (existingAuth instanceof AnonymousAuthenticationToken) {
			return true;
		}

		return false;
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		Assert.notNull(authenticationManager, "authenticationManager cannot be null");
		this.authenticationManager = authenticationManager;
	}

	public JwtTokenAuthenticationService getJwtTokenAuthenticationService() {
		return jwtTokenAuthenticationService;
	}

	public void setJwtTokenAuthenticationService(JwtTokenAuthenticationService jwtTokenAuthenticationService) {
		this.jwtTokenAuthenticationService = jwtTokenAuthenticationService;
	}

}
