package kim.sesame.common.annotation;

import java.lang.annotation.*;

/**
 * 请求参数检查
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ReqParamsCheck {

    /**
     * 是否打印日志
     *
     * @return boolean
     */
    boolean printLog() default true;

}
