package com.scnu.smartmvc.hanler.mapping;

import com.scnu.smartmvc.hanler.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * `MappingRegistry`是`RequestMappingInfo`、`HandlerMethod`的注册中心，
 * 当解析完一个控制器的method后就会向`MappingRegistry`中注册一个；
 * 最后当接收到用户请求后，根据请求的url在`MappingRegistry`找到对应的`HandlerMethod`；
 */
public class MappingRegistry {

    //将解析完成的`RequestMappingInfo`注册到Map中, 其中key为前端请求url路径，value为Mapping对象
    private Map<String, RequestMappingInfo> pathMappingInfo = new ConcurrentHashMap<>();
    //通过`method`，`handler`构建`HandlerMethod`对象，然后也加入到Map中，其中key为前端请求url路径，value为HandlerMethod对象
    private Map<String, HandlerMethod> pathHandlerMethod = new ConcurrentHashMap<>();

    public Map<String, RequestMappingInfo> getPathMappingInfo() {
        return pathMappingInfo;
    }

    public Map<String, HandlerMethod> getPathHandlerMethod() {
        return pathHandlerMethod;
    }

    /**
     * 注册url和Mapping/HandlerMethod的对应关系
     *
     * @param requestMappingInfo
     * @param method
     * @param handler
     */
    public void register(RequestMappingInfo requestMappingInfo, Object handler, Method method) {
        // 其中key为前端请求url路径，value为Mapping对象
        pathMappingInfo.put(requestMappingInfo.getPath(), requestMappingInfo);

        HandlerMethod handlerMethod = new HandlerMethod(handler, method);
        // key为前端请求url路径，value为HandlerMethod对象
        pathHandlerMethod.put(requestMappingInfo.getPath(), handlerMethod);
    }


    public RequestMappingInfo getMappingByPath(String path) {
        return this.pathMappingInfo.get(path);
    }

    public HandlerMethod getHandlerMethodByPath(String path) {
        return this.pathHandlerMethod.get(path);
    }
}
