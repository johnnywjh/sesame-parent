package kim.sesame.framework.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略登录检查
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface IgnoreLoginCheck {

    /**
     * 是否加载用户数据
     *
     * @return boolean
     */
    boolean isLoadUser() default false;

}
