package com.scnu.smartmvc.view.resolver;

import com.scnu.smartmvc.view.View;

/**
 * ViewResolver组件会将viewName解析成View对象，View对象再调用render完成结果的渲染。
 */
public interface ViewResolver {
    /**
     * 将viewName解析成View对象，View对象再调用render完成结果的渲染。
     *
     * @param viewName
     * @return
     */
    View resolveViewName(String viewName) throws Exception;
}
