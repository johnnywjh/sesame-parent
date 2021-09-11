package kim.sesame.framework.web.validator.exception;


import kim.sesame.framework.exception.BusinessException;

/**
 * 数据验证异常
 **/
public class DataValidatorException extends BusinessException {
    public DataValidatorException(String msg) {
        super(msg);
    }

    public DataValidatorException(String code, String msg) {
        super(code, msg);
    }
}
