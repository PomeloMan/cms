package io.cms.core.configure.security.authentication;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import io.cms.core.module.system.enums.Status;
import io.cms.core.module.system.persistence.entity.User;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * AuthenticationProvider
 * 
 * @author PomeloMan
 */
public abstract class AbstractAuthenticationProvider implements AuthenticationProvider, MessageSourceAware {

	protected final Log logger = LogFactory.getLog(getClass());
	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	protected User user;

	protected abstract User loadUserByUsername(String username) throws AuthenticationException;
	protected abstract Collection<? extends GrantedAuthority> loadUserAuthorities(String username);

	/**
	 * AuthenticationProvider
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		String username = token.getName();

		UserDetails loaduser = null;
		try {
			this.user = loadUserByUsername(username);
			loaduser = new org.springframework.security.core.userdetails.User(username, user.getPassword(),
					user.getStatus().equals(Status.Valid), !user.getStatus().equals(Status.Expired), true,
					!user.getStatus().equals(Status.Invalid), loadUserAuthorities(username));
		} catch (UsernameNotFoundException notFound) {
			throw notFound;
		} catch (Exception repositoryProblem) {
			throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		}

		Assert.notNull(loaduser, "retrieveUser returned null - a violation of the interface contract");
		preCheck(loaduser);
		credentialsCheck(loaduser, token);
		postCheck(loaduser);
		return createSuccessAuthentication(loaduser, authentication, loaduser);
	}

	/**
	 * @param principal
	 * @param authentication
	 * @param user
	 * @return
	 */
	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
			UserDetails user) {
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(principal,
				authentication.getCredentials(), user.getAuthorities());
		result.setDetails(this.user);
		return result;
	}

	/**
	 * 认证状态
	 * 
	 * @param user
	 */
	protected void preCheck(UserDetails user) {
		if (!user.isAccountNonLocked()) {
			logger.debug("User account is locked");
			throw new LockedException(messages.getMessage("AuthenticationProvider.locked", "User account is locked"));
		}
		if (!user.isEnabled()) {
			logger.debug("User account is disabled");
			throw new DisabledException(messages.getMessage("AuthenticationProvider.disabled", "User is disabled"));
		}
		if (!user.isAccountNonExpired()) {
			logger.debug("User account is expired");
			throw new AccountExpiredException(
					messages.getMessage("AuthenticationProvider.expired", "User account has expired"));
		}
	}

	/**
	 * 认证密码
	 * 
	 * @param user
	 * @param authentication
	 */
	protected void credentialsCheck(UserDetails user, UsernamePasswordAuthenticationToken authentication) {
		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");
			throw new BadCredentialsException(
					messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
	}

	protected void postCheck(UserDetails user) {
		if (!user.isCredentialsNonExpired()) {
			logger.debug("User account credentials have expired");
			throw new CredentialsExpiredException(
					messages.getMessage("AuthenticationProvider.credentialsExpired", "User credentials have expired"));
		}
	}

	/**
	 * AuthenticationProvider
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);// return true
	}

	/**
	 * MessageSourceAware
	 */
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messages = new MessageSourceAccessor(messageSource);
	}
}
