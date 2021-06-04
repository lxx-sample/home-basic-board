package li.dongpo.home.annotation;

import java.lang.annotation.*;

/**
 * @author dongpo.li
 * @date 2021/6/4
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HandlerMapping {

    String value() default "";

}
