package kim.sesame.framework.web.annotation;

import java.lang.annotation.*;

/**
 * 忽略签名检查
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface IgnoreAuthCheck {
}
