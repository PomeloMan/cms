package io.cms.core.configure.exception;

@SuppressWarnings("serial")
public class BusinessException extends Exception {

	public BusinessException(String msg) {
		super(msg);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
