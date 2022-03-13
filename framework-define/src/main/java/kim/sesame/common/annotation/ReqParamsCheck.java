package kim.sesame.common.annotation;

import java.lang.annotation.*;

/**
 * 请求参数检查
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ReqParamsCheck {
}
