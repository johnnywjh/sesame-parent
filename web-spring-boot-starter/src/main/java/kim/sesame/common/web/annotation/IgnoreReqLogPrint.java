package kim.sesame.common.web.annotation;

import java.lang.annotation.*;

/**
 * 忽略请求日志打印
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface IgnoreReqLogPrint {
}
