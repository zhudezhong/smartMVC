package com.scnu.smartmvc.view.resolver;

import com.scnu.smartmvc.utils.RequestContextHolder;
import com.scnu.smartmvc.view.InternalResourceView;
import com.scnu.smartmvc.view.RedirectView;
import com.scnu.smartmvc.view.View;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 视图协同器`ContentNegotiatingViewResolver`定义了所有`ViewResolver`以及默认支持的`View`，
 * 当接收到用户的请求后根据头信息中的`Accept`匹配出最优的视图
 */
public class ContentNegotiatingViewResolver implements ViewResolver, InitializingBean {

    private List<ViewResolver> viewResolvers;
    private List<View> defaultViews;

    @Override
    public View resolveViewName(String viewName) throws Exception {
        List<View> candidateViews = getCandidateViews(viewName);

        View bestView = getBestView(candidateViews);

        if (Objects.nonNull(bestView)) {
            return bestView;
        }

        return null;
    }

    /**
     * 根据请求找出最优的视图
     *
     * @param candidateViews
     * @return
     */
    private View getBestView(List<View> candidateViews) {
        Optional<View> viewOptional = candidateViews.stream()
                .filter(view -> view instanceof RedirectView)
                .findAny();

        if (viewOptional.isPresent()) {
            return viewOptional.get();
        }

        HttpServletRequest request = RequestContextHolder.getRequest();
        Enumeration<String> acceptHeaders = request.getHeaders("Accept");
        while (acceptHeaders.hasMoreElements()) {
            for (View view : candidateViews) {
                if (acceptHeaders.nextElement().equals(view.getContentType())) {
                    return view;
                }
            }
        }

        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(viewResolvers, "viewResolvers can not null");
    }

    /**
     * 先找出所有候选视图
     *
     * @param viewName
     * @return throws Exception
     */
    private List<View> getCandidateViews(String viewName) throws Exception {
        List<View> candidateViews = new ArrayList<>();

        for (ViewResolver viewResolver : viewResolvers) {
            View view = viewResolver.resolveViewName(viewName);
            if (Objects.nonNull(view)) {
                candidateViews.add(view);
            }
        }

        if (!CollectionUtils.isEmpty(defaultViews)) {
            candidateViews.addAll(defaultViews);
        }

        return candidateViews;
    }

    public List<ViewResolver> getViewResolvers() {
        return viewResolvers;
    }

    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    public List<View> getDefaultViews() {
        return defaultViews;
    }

    public void setDefaultViews(List<View> defaultViews) {
        this.defaultViews = defaultViews;
    }
}
