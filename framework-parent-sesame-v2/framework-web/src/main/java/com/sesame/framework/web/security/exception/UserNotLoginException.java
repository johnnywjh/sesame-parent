package com.sesame.framework.web.security.exception;


import com.sesame.framework.exception.GeneralException;

/**
* @ClassName: UserNotLoginException
* @Description: 用户未登录异常
*
*/
public class UserNotLoginException extends GeneralException {

	private static final long serialVersionUID = -8447576671797891073L;

	public static final String ERROR_CODE = "ERROR.SECURITY.USERNOTLOGIN";

	public static final String MESSAGE = "User not logged in";

	public UserNotLoginException() {
		super(MESSAGE);
		super.errCode = ERROR_CODE;
	}

	public UserNotLoginException(String message, Throwable cause) {
		super(message, cause);
		super.errCode = ERROR_CODE;
	}

	public UserNotLoginException(String msg) {
		super(msg);
		super.errCode = ERROR_CODE;
	}

	public UserNotLoginException(Throwable cause) {
		super(cause);
		super.errCode = ERROR_CODE;
	}

}
