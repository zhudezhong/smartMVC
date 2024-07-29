package com.scnu.smartmvc.annotation;


import java.lang.annotation.*;

/**
 * 被`@RequestParam`标注，需要从request中取出对应的参数，
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    // 从request取参数的名字，该参数必填
    String name();

    // 说明该参数是否必填，默认是true
    boolean required() default true;

    // 如果request中找不到对应的参数，那么就用默认值
    String defaultValue() default "";
}
