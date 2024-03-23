package kim.sesame.common.web.annotation;

import java.lang.annotation.*;

/**
 * 固定登录用户
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface FixedLoginUser {

}
