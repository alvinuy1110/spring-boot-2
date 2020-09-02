package com.myproject.springboot.logging.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface MDCValue {

    String value();

    String content() default "";

    /*
    For non-string, will use the format to conver the object, if available
     */
    String format() default "";
}
