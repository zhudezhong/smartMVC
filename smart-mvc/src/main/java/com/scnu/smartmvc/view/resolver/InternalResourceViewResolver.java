package com.scnu.smartmvc.view.resolver;

import com.scnu.smartmvc.view.InternalResourceView;
import com.scnu.smartmvc.view.View;

/**
 * 实现了`UrlBasedViewResolver`中的模板方法`buildView`，
 * 拼接了url的前缀和后缀，返回视图`InternalResourceView`
 */
public class InternalResourceViewResolver extends UrlBasedViewResolver {
    @Override
    protected View buildView(String viewName) {
        String url = getPrefix() + viewName + getSuffix();
        return new InternalResourceView(url);
    }
}
