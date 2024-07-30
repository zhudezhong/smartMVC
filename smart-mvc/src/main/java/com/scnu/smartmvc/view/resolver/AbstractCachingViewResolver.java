package com.scnu.smartmvc.view.resolver;

import com.scnu.smartmvc.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 因为启动一直一般会运行很长时间，很多用户都会请求同一个视图名称，
 * 为了避免每次都需要把viewName解析成View，所以我们需要做一层缓存，
 * 当有一次成功解析了viewName之后我们把返回的`View`缓存起来，下次直接先从缓存中取
 */
public abstract class AbstractCachingViewResolver implements ViewResolver {
    // 由于可能存在同一时刻多个用户请求到同一个视图，所以需要使用`synchronized`加锁
    private final Object lock = new Object();

    private static final View UNRESOLVER_VIEW = ((model, request, response) -> {
    });

    private Map<String, View> cachedViews = new HashMap<>();

    @Override
    public View resolveViewName(String viewName) {

        View view = cachedViews.get(viewName);
        if (Objects.nonNull(view)) {
            return (view != UNRESOLVER_VIEW ? view : null);
        }

        synchronized (lock) {
            view = cachedViews.get(viewName);

            if (Objects.nonNull(view)) {
                return (view != UNRESOLVER_VIEW ? view : null);
            }

            view = createView(viewName);

            if (Objects.isNull(view)) {
                view = UNRESOLVER_VIEW;
            }
            cachedViews.put(viewName, view);
        }

        return (view != UNRESOLVER_VIEW ? view : null);

    }

    protected abstract View createView(String viewName);
}
