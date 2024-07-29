package com.scnu.smartmvc.hanler;

import com.scnu.smartmvc.http.HttpStatus;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Objects;

/**
 * 该类的使用场景是每个请求进来都会新建一个对象，主要用于保存Handler处理过程中Model以及返回的View对象；
 * 该类将会用于参数解析器`HandlerMethodArgumentResolver`和Handler返回值解析器`HandlerMethodReturnValueHandler`
 */
public class ModelAndViewContainer {

    // view定义的类型是Object，是因为Handler既可以返回一个String表示视图的名字，也可以直接返回一个视图对象View
    private Object view;

    // Spring中定义的类，可以直接看做是Map
    private Model model;

    private HttpStatus httpStatus;

    // 标记本次请求是否已经处理完成，后期在处理注解`@ResponseBody`将会使用到
    private boolean requestHandled = false;

    public Object getView() {
        return view;
    }

    public void setView(Object view) {
        this.view = view;
    }

    public boolean isViewReference() {
        return (this.view instanceof String);
    }

    public String getViewName() {
        return (this.view instanceof String ? (String) this.view : null);
    }

    public Model getModel() {
        if (Objects.isNull(this.model)) {
            this.model = new ExtendedModelMap();
        }
        return this.model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public boolean isRequestHandled() {
        return requestHandled;
    }

    public void setRequestHandled(boolean requestHandled) {
        this.requestHandled = requestHandled;
    }


}
