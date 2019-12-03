package io.cms.core.module.system.security;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.password.PasswordEncoder;

import io.cms.util.security.IrreversibleEncryptor;
import io.cms.util.security.IrreversibleEncryptor.Algorithm;

/**
 * 密码加密类
 */
public class PasswordEncoderImpl implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return null;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return false;
	}

	/**
	 * @param rawPassword
	 * @param salt
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String rawPassword, String salt)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return IrreversibleEncryptor.encrypt(rawPassword, Algorithm.MD5,
				IrreversibleEncryptor.encrypt(salt, Algorithm.SHA));
	}

	/**
	 * @param rawPassword
	 * @param salt
	 * @param encodedPassword
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static boolean matches(String rawPassword, String salt, String encodedPassword)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return encode(rawPassword, salt).equals(encodedPassword);
	}
}
