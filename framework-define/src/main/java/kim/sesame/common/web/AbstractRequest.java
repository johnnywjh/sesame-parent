package kim.sesame.common.web;


import kim.sesame.common.exception.BizArgumentException;
import kim.sesame.common.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * facade基本请求参数
 */
public abstract class AbstractRequest extends PrintFriendliness {

    private static final long serialVersionUID = -4848830183604183658L;

    private static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 客户端应用ID
     */
    private String appId;

    /**
     * 请求ID
     */
    private String requestId;

    public String getAppId() {
        return appId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void validate() {
        StringBuilder errorMsgs = new StringBuilder();
        Set<ConstraintViolation<AbstractRequest>> violations = VALIDATOR.validate(this);
        if (violations != null && violations.size() > 0) {
            for (ConstraintViolation<AbstractRequest> violation : violations) {
                errorMsgs.append(violation.getPropertyPath()).append(":").append(violation.getMessage()).append("|");
            }
            throw new BizArgumentException(errorMsgs.substring(0, errorMsgs.length() - 1));
        }
    }


    /**
     * requestId是否强制必填
     * @return
     */
    protected boolean requiresRequestId( ) {
        return false;
    }

    /**
     * appId是否强制必填
     * @return
     */
    protected boolean requiresAppId( ) {
        return false;
    }

    /**
     * 构造该请求的日志上下文标识, 默认是 类名+requestId
     * @param
     * @return
     */
    protected String buildLogPrefix( ) {
        if (StringUtils.isBlank(requestId)) {
            setRequestId(UUIDUtil.getShortUUID());
        }
        return getClass().getSimpleName() + "|" + getRequestId();
    }

    /**
     * 是否需要打印请求日志
     * @return
     */
    protected boolean needsLog( ) {
        return true;
    }
}

