package kim.sesame.common.web.annotation;

import java.lang.annotation.*;

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
