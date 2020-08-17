package com.dns007.annotation;

import java.lang.annotation.*;

/**
 * @author dns007
 * @version 1.0
 * @date 2020/8/17 8:00
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IocService {
    String name() default "";
}