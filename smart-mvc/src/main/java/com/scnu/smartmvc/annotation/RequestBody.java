package com.scnu.smartmvc.annotation;

import java.lang.annotation.*;

/**
 * 当被这个注解的参数，需要把request流中的数据转换成对象
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestBody {
    boolean required() default true;
}
