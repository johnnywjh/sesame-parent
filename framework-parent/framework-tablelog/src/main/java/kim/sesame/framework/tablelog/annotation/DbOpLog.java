package kim.sesame.framework.tablelog.annotation;

import java.lang.annotation.*;

/**
 * 数据库操作日志
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DbOpLog {

}
