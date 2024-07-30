package com.scnu.smartmvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface View {
    /**
     * 控制视图支持的ContentType是什么，默认是返回空
     *
     * @return
     */
    default String getContentType() {
        return null;
    }

    /**
     * 通过response把model中的数据渲染成视图返回给用户
     *
     * @param model
     * @param request
     * @param response
     * @throws Exception
     */
    void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception;


}
