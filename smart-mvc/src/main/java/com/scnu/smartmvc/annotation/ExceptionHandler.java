package com.scnu.smartmvc.annotation;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionHandler {

    Class<? extends Throwable>[] value() default {};
}
