package com.scnu.smartmvc.hanler.mapping;

import com.scnu.smartmvc.annotation.RequestMapping;
import com.scnu.smartmvc.exception.NoHandlerFoundException;
import com.scnu.smartmvc.hanler.HandlerExecutionChain;
import com.scnu.smartmvc.hanler.HandlerMethod;
import com.scnu.smartmvc.hanler.interceptor.HandlerInterceptor;
import com.scnu.smartmvc.hanler.interceptor.MappedInterceptor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 因为在初始化的过程中，我们需要获取到容器中所有的Bean对象，
 * 所以`RequestMappingHandlerMapping`需要继承于`ApplicationObjectSupport`，
 */
public class RequestMappingHandlerMapping extends ApplicationObjectSupport implements HandlerMapping, InitializingBean {
    private MappingRegistry mappingRegistry = new MappingRegistry();

    // 拦截器interceptor集合
    private List<MappedInterceptor> interceptors = new ArrayList<>();

    public MappingRegistry getMappingRegistry() {
        return mappingRegistry;
    }

    /**
     * `RequestMappingHandlerMapping`需要在创建完对象后初始化`HandlerMethod`，
     * 所以实现了接口`InitializingBean`(提供了`afterPropertiesSet`方法，
     * 在对象创建完成后，spring容器会调用这个方法)
     * <p>
     * 初始化代码的入口就在`afterPropertiesSet`中。
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // System.out.println("创建完对象后初始化HandlerMethod, spring容器会调用.....");
        initialHandlerMethods();
    }

    private void initialHandlerMethods() {
        // 1、从容器中拿出所有的Bean，返回beanName和bean实例对象对应的map集合
        Map<String, Object> beansOfMap = (Map<String, Object>) BeanFactoryUtils.beansOfTypeIncludingAncestors(this.obtainApplicationContext(), Object.class);
        // 2、过滤出所有被标记`@Controller`的类
        beansOfMap.entrySet().stream()
                .filter(entry -> this.isHandler(entry.getValue()))
                .forEach(entry -> this.detectHandlerMethods(entry.getKey(), entry.getValue()));

    }


    /**
     * 类上标记又@Controller的注解就是我们需要找的handler
     *
     * @param handler
     * @return
     */
    private boolean isHandler(Object handler) {
        Class<?> beanType = handler.getClass();
        // 使用到了Spring中的工具类`AnnotatedElementUtils.hasAnnotation`判断类是否有添加注解`@Controller`
        return AnnotatedElementUtils.hasAnnotation(beanType, Controller.class);
    }

    /**
     * 解析出handler中 所有被RequestMapping注解的方法
     *
     * @param beanName
     * @param handler
     * @return
     */
    private void detectHandlerMethods(String beanName, Object handler) {
        Class<?> beanType = handler.getClass();
        Map<Method, RequestMappingInfo> methodOfMap = MethodIntrospector.selectMethods(beanType,
                (MethodIntrospector.MetadataLookup<RequestMappingInfo>) method
                        -> getMappingForMethod(method, beanType));

        // 当所有的方法都解析完成之后，需要把所有配置有`@RequestMapping`注解的方法注册到`MappingRegistry`,
        methodOfMap.forEach((method, requestMappingInfo) ->
                this.mappingRegistry.register(requestMappingInfo, handler, method));

    }

    /**
     * 查找method上面是否有RequestMapping，有 => 构建RequestMappingInfo
     *
     * @param method
     * @param beanType
     * @return
     */
    private RequestMappingInfo getMappingForMethod(Method method, Class<?> beanType) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            return null;
        }
        String prefix = getPathPrefix(beanType);
        return new RequestMappingInfo(prefix, requestMapping);

    }

    /**
     * 使用工具类`MethodIntrospector.selectMethods`找出Controller类中所有的方法，遍历每个方法，
     * 判断方法是否有添加注解`@RequestMapping`，如果没有就返回空，
     * 如果有就通过`@RequestMapping`构建`RequestMappingInfo`对象返回；
     * 如果所Controller类上有添加注解`@RequestMapping`，那么配的path将作为前缀
     *
     * @param beanType
     * @return
     */
    private String getPathPrefix(Class<?> beanType) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(beanType, RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            return "";
        }

        return requestMapping.path();
    }

    public void setInterceptors(List<MappedInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    /**
     * 通过request找到需要执行的handler，包装成`HandlerExecutionChain`，
     *
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        String lookupPath = request.getRequestURI();

        HandlerMethod handler = mappingRegistry.getHandlerMethodByPath(lookupPath);
        if (Objects.isNull(handler)) {
            throw new NoHandlerFoundException(request);
        }

        return createHandlerExecutionChain(lookupPath, handler);
    }

    /**
     * 根据请求路径lookupPath，从MappingRegistry中找到对应的HandlerMethod
     *
     * @param lookupPath 请求路径
     * @param handler
     * @return
     */
    private HandlerExecutionChain createHandlerExecutionChain(String lookupPath, HandlerMethod handler) {
        List<HandlerInterceptor> interceptors = this.interceptors.stream()
                .filter(mappedInterceptor -> mappedInterceptor.matches(lookupPath))
                .collect(Collectors.toList());
        return new HandlerExecutionChain(handler, interceptors);
    }

}
