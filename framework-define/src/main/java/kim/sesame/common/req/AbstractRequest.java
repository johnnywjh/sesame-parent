package kim.sesame.common.req;


import kim.sesame.common.exception.BizArgumentException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * facade基本请求参数
 */
public abstract class AbstractRequest extends PrintFriendliness {

    private static final long serialVersionUID = 1L;

    private static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    /*
     * https://blog.csdn.net/m0_46360888/article/details/116004078
     * @NotEmpty :不能为null，且Size>0，一般用于集合、数组、字符序列
     * @NotNull:不能为null，但可以为empty,没有Size的约束，一般用于包装类型判断
     * @NotBlank:只用于String,不能为null且trim()之后size>0，一般用于字符串
     * @Length：java中的length()方法是针对字符串String说的,如果想看这个字符串的长度则用到length()这个方法.一般用于字符型长度判断
     * @Size：java中的size()方法是针对泛型集合说的,如果想看这个泛型有多少个元素,一般用于集合、数组、字符序列长度的判断
     */
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


}

