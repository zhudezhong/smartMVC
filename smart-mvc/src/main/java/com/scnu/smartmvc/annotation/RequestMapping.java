package com.scnu.smartmvc.annotation;

import com.scnu.smartmvc.http.RequestMethod;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String path(); // 表示请求得URL路径

    RequestMethod method() default RequestMethod.GET; // 表示http请求的方式 ` GET`，`POST`
}
