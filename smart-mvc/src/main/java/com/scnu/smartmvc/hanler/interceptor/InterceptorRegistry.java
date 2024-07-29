package com.scnu.smartmvc.hanler.interceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器的注册中心, 管理所有的拦截器
 */
public class InterceptorRegistry {
    private List<MappedInterceptor> mappedInterceptors = new ArrayList<>();

    /**
     * 注册一个拦截到的Registry
     *
     * @param interceptor
     * @return
     */
    public MappedInterceptor addInterceptor(HandlerInterceptor interceptor) {
        MappedInterceptor mappedInterceptor = new MappedInterceptor(interceptor);

        mappedInterceptors.add(mappedInterceptor);

        return mappedInterceptor;
    }

    public List<MappedInterceptor> getMappedInterceptors() {
        return mappedInterceptors;
    }
}
