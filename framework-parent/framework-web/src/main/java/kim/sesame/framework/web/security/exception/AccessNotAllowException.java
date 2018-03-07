package kim.sesame.framework.web.security.exception;


import kim.sesame.framework.exception.GeneralException;

/**
* Description: 访问拒绝异常
*
*/
public class AccessNotAllowException extends GeneralException {

	private static final long serialVersionUID = -5710513168282003818L;
	
	public static final String ERROR_CODE = "ERROR.SECURITY.NOTALLOW";
	
	public static final String MESSAGE = "Method not allow access";
	
	public AccessNotAllowException() {
		this(MESSAGE);
		super.errCode = ERROR_CODE;
	}

	public AccessNotAllowException(String msg) {
		super(msg);
		super.errCode = ERROR_CODE;
	}

	@Override
	public String getErrorCode() {
		return super.errCode;
	}

}
