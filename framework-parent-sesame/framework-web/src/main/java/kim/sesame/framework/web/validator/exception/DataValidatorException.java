package kim.sesame.framework.web.validator.exception;


import kim.sesame.framework.exception.BusinessException;

/**
 * 数据验证异常
 *
 * @author johnny-王江海
 * @date 2017/10/23 20:27
 **/
public class DataValidatorException extends BusinessException {
    public DataValidatorException(String msg) {
        super(msg);
    }

    public DataValidatorException(String code, String msg) {
        super(code, msg);
    }
}
