package com.scnu.smartmvc.view;

import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 当在控制器中返回的视图名是以`redirect:`开头的都将视为重定向视图；
 */
public class RedirectView extends AbstractView {

    /**
     * 表示重定向的地址，实际也就是控制器中返回的视图名截取`redirect:`之后的字符串
     */
    private String url;

    public RedirectView(String url) {
        this.url = url;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String targetURL = createTargetUrl(model, request);
        response.sendRedirect(targetURL);
    }

    /**
     * model中的数据添加到URL后面作为参数
     *
     * 根据url拼接出重定向的地址，如果有设置`contentPath`，需要把`contentPath`拼接到链接的前面；
     * 如果Model中有属性值，需要把model中的属性值拼接到链接后面
     *
     * @param model
     * @param request
     * @return
     */
    private String createTargetUrl(Map<String, Object> model, HttpServletRequest request) {
        Assert.notNull(this.url, "url can not null");

        StringBuilder queryParams = new StringBuilder();
        model.forEach((key, value) -> {
            queryParams.append(key).append("=").append(value).append("&");
        });

        StringBuilder targetURL = new StringBuilder();
        if (this.url.startsWith("/")) {
            // Do not apply context path to relative URLs
            targetURL.append(getContexPath(request));
        }

        targetURL.append(url);

        if (queryParams.length() > 0) {
            targetURL.append("?").append(queryParams.toString());
        }
        return targetURL.toString();
    }

    private String getContexPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();

        while (contextPath.startsWith("//")) {
            contextPath = contextPath.substring(1);
        }

        return contextPath;
    }

    public String getUrl() {
        return url;
    }

}
