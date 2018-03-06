package kim.sesame.framework.web.security.exception;


import kim.sesame.framework.exception.GeneralException;

/**
* Description: 功能无效异常
*
*/
public class FunctionNotValidException extends GeneralException {

	private static final long serialVersionUID = 1L;

	public static final String ERROR_CODE = "ERROR.SECURITY.NOTVALID";

	public static final String MESSAGE = "Function is not valid";

	public FunctionNotValidException() {
		super(MESSAGE);
		super.errCode = ERROR_CODE;
	}

	public FunctionNotValidException(String message, Throwable cause) {
		super(message, cause);
		super.errCode = ERROR_CODE;
	}

	public FunctionNotValidException(String msg) {
		super(msg);
		super.errCode = ERROR_CODE;
	}

	public FunctionNotValidException(Throwable cause) {
		super(cause);
		super.errCode = ERROR_CODE;
	}

}
